package com.timss.inventory.listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.service.core.InvMatAcceptDetailService;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.utils.CommonInvToEipMobile;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.ReflectionUtil;
import com.timss.inventory.vo.InvEipListEip;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptToEipMobile.java
 * @author: 890151
 * @createDate: 2016-8-8
 * @updateUser: 890151
 * @version: 1.0
 */
@EipAnnotation("invMatAccept")
@Component
public class InvMatAcceptToEipMobile implements EipMobileInterface {

    @Autowired
    private InvMatAcceptService invMatAcceptService;
    @Autowired
    private InvMatAcceptDetailService invMatAcceptDetailService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private CommonInvToEipMobile emc;

    private static final Logger LOG = Logger.getLogger( InvMatAcceptToEipMobile.class );

    @Override
    public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
        RetContentBean efdb = new RetContentBean();

        // 表单的sheetNo
        String flowNo = eipmobileparambean.getFlowNo();
        // 流程实例id
        String processId = eipmobileparambean.getProcessId();
        try {
            UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
            // 通过flowNo找到对应的sheetId
            String inacId = invMatAcceptService.queryInvMatAcceptIdByFlowNo( flowNo, userInfo.getSiteId() );

            // 组装forms中数据
            List forms = assembleContent( userInfo, inacId, processId );
            efdb.setContent( forms );

            // 组装flows中数据
            List flows = emc.assembleFlows( userInfo, processId, null );
            efdb.setFlows( flows );
        } catch (Exception e) {
            throw new RuntimeException(
                    "---------InvMatAcceptToEipMobile 中的 retrieveWorkflowFormDetails 方法抛出异常---------：", e );
        }
        return efdb;
    }

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
                emc.setFlowVariables( processId, eipmobileparambean.getTaskKey() );
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
            LOG.info( e.getMessage() );
            emrb.setRetcode( -1 );
        }
        return emrb;
    }

    /**
     * @description:组织form中的数据
     * @author: 890151
     * @createDate: 2016-8-8
     * @param formId
     * @return:
     */
    private List<RetContentInLineBean> assembleContent(UserInfoScope userInfo, String inacId, String processId)
            throws Exception {
        List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
        RetContentInLineBean rcilbForm = null;
        RetContentInLineBean rcilbList = null;
        RetContentInLineBean rcilbOpi = null;

        // 收集主单信息
        InvMatAccept invMatAcceptBasicInfo = invMatAcceptService.queryInvMatAcceptBasicInfoById( inacId );
        if ( null != invMatAcceptBasicInfo) {
            rcilbForm = settingFormData( invMatAcceptBasicInfo );
            epbList.add( rcilbForm );
       }

        // 收集子单信息
        List<InvMatAcceptDetailVO> invMatAcceptDetailList = invMatAcceptDetailService.queryInvMatAcceptDetailListByInacId( inacId );
        if ( null != invMatAcceptDetailList && !invMatAcceptDetailList.isEmpty() ) {
            rcilbList = settingListData( invMatAcceptDetailList );
            epbList.add( rcilbList );
        }

        // 组装opions中数据
        rcilbOpi = emc.assembleOpinions( processId );
        epbList.add( rcilbOpi );

        return epbList;
    }

    /**
     * @description:转换form数据信息到接口实体
     * @author: 890151
     * @createDate: 2016-8-8
     * @param paList
     * @return:
     */
    private RetContentInLineBean settingFormData(InvMatAccept invMatAccept) throws Exception {

        RetContentInLineBean pafem = new RetContentInLineBean();

        pafem.setFoldable( true );// 是否可以折叠标志
        pafem.setIsShow( true ); // 是否显示标志
        pafem.setName( getPropertiesVal( "eip2invMatAcceptFormGroupName" ) );// 名称
        pafem.setType( Type.KeyValue );// 类别，这里固定keyValue

        // 通过properties文件中定义的字段反射获取价值
        List<Object> rkvList = new ArrayList<Object>();
        String[] formField = getPropertiesVal( "eip2invMatAcceptForm" ).split( "," );
        for ( String ffield : formField ) {
            String[] fieldContent = ffield.split( "#@#" );
            String fieldKey = fieldContent[0];
            String fieldVal = ReflectionUtil.getFieldValueByFieldName( invMatAccept, fieldContent[1] );
            rkvList.add( new RetKeyValue( fieldKey, fieldVal ) );
        }
        pafem.setValue( rkvList );
        return pafem;
    }

    /**
     * @description:转换list数据信息到接口实体
     * @author: 890151
     * @createDate: 2016-8-8
     * @param paiList
     * @return
     * @throws Exception:
     */
    private RetContentInLineBean settingListData(List<InvMatAcceptDetailVO> inacList)
            throws Exception {
        RetContentInLineBean palem = new RetContentInLineBean();
        palem.setFoldable( true );// 是否可以折叠标志
        palem.setIsShow( false ); // 是否显示标志
        palem.setName( getPropertiesVal( "eip2invMatAcceptListGroupName" ) );// 名称
        palem.setType( Type.Table );// 类别，这里固定table

        List<Object> rkvList = new ArrayList<Object>();
        // 获取column字段
        String[] listField = getPropertiesVal( "eip2invMatAcceptList" ).split( "," );
        // 获取list数据
        for ( InvMatAcceptDetailVO inac : inacList ) {
            InvEipListEip emfi = new InvEipListEip();
            // 预设一个行的集合
            List<RetKeyValue> rows = new ArrayList<RetKeyValue>();
            for ( String lField : listField ) {
                String[] fieldContent = lField.split( "#@#" );
                String fieldKey = fieldContent[0];
                String fieldVal = ReflectionUtil.getFieldValueByFieldName( inac, fieldContent[1] );
                // 如果是itemname的话就做特殊处理
                if ( "itemname".equals( fieldContent[1] ) ) {
                    emfi.setTitle( fieldVal );
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
     * @description:获取properties文件中配置信息
     * @author: 890151
     * @createDate: 2016-8-8
     * @param typeName
     * @return
     * @throws Exception:
     */
    private String getPropertiesVal(String typeName) throws Exception {
        return CommonUtil.getProperties( typeName );
    }

}
