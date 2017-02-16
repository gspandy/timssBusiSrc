package com.timss.attendance.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveFileBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.dao.LeaveDao;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.util.CommonAtdToEipMobile;
import com.timss.attendance.util.CommonInvokeUtil;
import com.timss.attendance.util.PropertyUtil;
import com.timss.attendance.vo.AttendanceEipList;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 请假申请eip接口
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveToEipMobile.java
 * @author: fengzt
 * @createDate: 2014年10月15日
 * @updateUser: fengzt
 * @version: 1.0
 */
@EipAnnotation("leave")
@Component
public class LeaveToEipMobile implements EipMobileInterface {
    
    private Logger log = Logger.getLogger( LeaveToEipMobile.class );

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private CommonAtdToEipMobile emc;
    @Autowired
    private WorkflowService workflowService;

    /**
     * 根据参数获取流程表单详情
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2014-10-15
     * @param bean
     * @return:
     */
    @Override
    public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
        RetContentBean efdb = new RetContentBean();

        // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        try {
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            // 通过flowNo找到对应的id
            int id = leaveService.queryIdByFlowNo( flowNo, userInfo.getSiteId() );

            // 组装forms中数据
            List<RetContentInLineBean> forms = assembleContent( id, processId );
            efdb.setContent( forms );

            // 组装附件中数据
            List<LeaveFileBean> leaveFileBeans = leaveDao.queryFileByLeaveId( Integer.toString( id ) );
            List<String> fileIds = new ArrayList<String>();
            for( LeaveFileBean vo : leaveFileBeans ){
                fileIds.add( vo.getFileId() );
            }
            List<RetAttachmentBean> attachements = emc.assembleAttachements( fileIds );
            efdb.setAttachments( attachements );

            // 组装flows中数据
            String processDefKey = getPropertiesVal( "leaveDefKey" );
            String[] keyArr = processDefKey.split( "," );
            for ( String key : keyArr ) {
                if ( key.indexOf( userInfo.getSiteId().toLowerCase() ) > -1 ) {
                    processDefKey = key;
                }
            }
            // 组装flows中数据
            List<RetFlowsBean> flows = emc.assembleFlows( userInfo, processId, processDefKey );
            efdb.setFlows( flows );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return efdb;
    }

    /**
     * @description:组织form中的数据
     * @author: fengzt
     * @createDate: 2014-9-23
     * @param formId
     * @return:
     */
    private List<RetContentInLineBean> assembleContent(int id, String processId)
            throws Exception {
        List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
        // 主单
        RetContentInLineBean rcilbForm = new RetContentInLineBean();
        // 明细
        RetContentInLineBean rcilbList = new RetContentInLineBean();
        // 流程信息
        RetContentInLineBean rcilbOpi = new RetContentInLineBean();

        // 收集主单信息
        LeaveBean leaveBean = leaveDao.queryLeaveById( id );
        rcilbForm = settingFormData( leaveBean );
        epbList.add( rcilbForm );

        // 收集明细信息
        List<LeaveItemBean> leaveItemBeans = leaveService.queryLeaveItemList( String.valueOf( id ) );
        if ( null != leaveItemBeans && leaveItemBeans.size() > 0 ) {
            rcilbList = settingListData( leaveItemBeans );
            epbList.add( rcilbList );
        }

        // 组装opions中数据
        rcilbOpi = emc.assembleOpinions( processId );
        epbList.add( rcilbOpi );

        // 设置参数 请假天数
        workflowService.setVariable( processId, "countDay", leaveBean.getLeaveDays() );

        if ( leaveItemBeans != null && leaveItemBeans.size() > 0 ) {
            String applyType = null;
            for ( LeaveItemBean vo : leaveItemBeans ) {
                // 含有多条明细，如果有非病假事假就走非病假事假流程
                if ( vo.getCategory().equalsIgnoreCase( "itc_le_category_2" )
                        || vo.getCategory().equalsIgnoreCase( "itc_le_category_3" ) ) {
                    applyType = "sick";
                } else {
                    applyType = "nosick";
                    break;
                }
            }
            // 设置参数 是否病假
            workflowService.setVariable( processId, "applyType", applyType );
        }
        return epbList;
    }

    /**
     * @description:转换list数据信息到接口实体
     * @author: fengzt
     * @createDate: 2014-9-25
     * @param paiList
     * @return
     * @throws Exception:
     */
    private RetContentInLineBean settingListData(List<LeaveItemBean> leaveItemBeans)
            throws Exception {
        RetContentInLineBean palem = new RetContentInLineBean();
        palem.setFoldable( true );// 是否可以折叠标志
        palem.setIsShow( true ); // 是否显示标志
        palem.setName( getPropertiesVal( "eip2LeaveListGroupName" ) );// 名称
        palem.setType( Type.Table );// 类别，这里固定table

        List<Object> rkvList = new ArrayList<Object>();
        // 获取column字段
        String[] listField = getPropertiesVal( "eip2LeaveList" ).split( "," );
        // 枚举类型
        String enumId = "ATD_LEI_CATEGORY";
        List<AppEnum> emList = itcMvcService.getEnum( enumId );

        // 获取list数据
        for ( LeaveItemBean vo : leaveItemBeans ) {
            AttendanceEipList emfi = new AttendanceEipList();
            // 预设一个行的集合
            List<RetKeyValue> rows = new ArrayList<RetKeyValue>();
            for ( String lField : listField ) {
                String[] fieldContent = lField.split( "#@#" );
                String fieldKey = fieldContent[0];
                String fieldVal = CommonInvokeUtil.getFieldValueByFieldName( vo, fieldContent[1], "yyyy-MM-dd HH:mm" );
                // 如果是itemname的话就做特殊处理
                if ( "category".equals( fieldContent[1] ) ) {
                    fieldVal = getCategoryName( fieldVal, emList );
                }
                rows.add( new RetKeyValue( fieldKey, fieldVal ) );
            }
            emfi.setRows( rows );
            rkvList.add( emfi );
        }

        palem.setValue( rkvList );
        return palem;
    }

    /**
     * @description:考勤异常枚举code to name
     * @author: fengzt
     * @createDate: 2014年10月11日
     * @param flowCode
     * @return:
     */
    private String getCategoryName(String category, List<AppEnum> emList) {
        String categoryName = category;
        if ( StringUtils.isNotBlank( categoryName ) ) {
            if ( emList != null && emList.size() > 0 ) {
                for ( AppEnum appVo : emList ) {
                    if ( categoryName.equalsIgnoreCase( appVo.getCode() ) ) {
                        categoryName = appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return categoryName;
    }

    /**
     * @description:转换form数据信息到接口实体
     * @author: fengzt
     * @createDate: 2014-9-25
     * @param paList
     * @return:
     */
    private RetContentInLineBean settingFormData(Object vo) throws Exception {

        RetContentInLineBean pafem = new RetContentInLineBean();

        pafem.setFoldable( true );// 是否可以折叠标志
        pafem.setIsShow( true ); // 是否显示标志
        pafem.setName( getPropertiesVal( "eip2LeaveFormGroupName" ) );// 名称
        pafem.setType( Type.KeyValue );// 类别，这里固定keyValue

        // 通过properties文件中定义的字段反射获取价值
        List<Object> rkvList = new ArrayList<Object>();
        String[] formField = getPropertiesVal( "eip2LeaveForm" ).split( "," );
        for ( String ffield : formField ) {
            String[] fieldContent = ffield.split( "#@#" );
            String fieldKey = fieldContent[0];
            String fieldVal = CommonInvokeUtil.getFieldValueByFieldName( vo, fieldContent[1], "yyyy-MM-dd HH:mm" );

            rkvList.add( new RetKeyValue( fieldKey, fieldVal ) );
        }
        pafem.setValue( rkvList );
        return pafem;
    }

    /**
     * @description:获取properties文件中配置信息
     * @author: fengzt
     * @createDate: 2014-9-25
     * @param typeName
     * @return
     * @throws Exception:
     */
    private String getPropertiesVal(String key) {
        PropertyUtil propertyUtil = new PropertyUtil( "attendanceEip.properties" );
        return propertyUtil.getProperty( key );
    }

    /**
     * 处理流程
     * 
     * @description:
     * @author: fengzt
     * @createDate: 2014-10-15
     * @param bean
     * @return:
     */
    @Override
    public RetProcessBean processWorkflow(ParamProcessBean eipmobileparambean) {
        RetProcessBean emrb = new RetProcessBean();

        String flowId = eipmobileparambean.getFlowID();// 用户返回选择id
        String opinion = eipmobileparambean.getOpinion();// 用户填写意见
        String processId = eipmobileparambean.getProcessId();// 流程id

        List<String> nuser = eipmobileparambean.getNextUser();// 下一环节处理人员

        boolean flag = false;
        try {
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            if ( "next".equals( flowId ) ) {// 提交下一环节
                flag = emc.commitToNextLink( userInfo, opinion, nuser, processId );
            } else if ( "rollback".equals( flowId ) ) {// 回退
                flag = emc.returnToPreviousLink( userInfo, opinion, processId );
            } else {// 终止流程
                flag = emc.toStopCurProcess( userInfo, opinion, processId );
            }
            // 若流程提交操作成功，返回成功状态
            if ( flag ) {
                emrb.setRetcode( 1 );
                emrb.setRetmsg( "success" );
            } else {
                emrb.setRetcode( -1 );
            }
        } catch (Exception e) {
            log.error( e.getMessage(), e );
            emrb.setRetcode( -1 );
        }
        return emrb;
    }

}
