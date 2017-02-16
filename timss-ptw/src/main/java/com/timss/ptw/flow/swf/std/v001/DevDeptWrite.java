package com.timss.ptw.flow.swf.std.v001;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.timss.ptw.service.PtwStandardService;
import com.timss.ptw.service.PtwUtilService;
import com.timss.ptw.vo.VOUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class DevDeptWrite extends TaskHandlerBase {
    private static final Logger log = Logger.getLogger( DevDeptWrite.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwStandardService ptwStandardService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PtwUtilService ptwUtilService;
    /**
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope() {
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }

    @Override
    public void init(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备编写’的init(),开始处理业务逻辑-----------------" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId" ).toString();
        String siteId = getUserInfoScope().getSiteId();

        PtwStandardBean bean = new PtwStandardBean();
        bean.setFlowStatus( siteId.toLowerCase() + "_flow_std_status_1" );
        bean.setInstantId( instanceId );
        bean.setId( id );

        int count = ptwStandardService.updatePtwStandardStatus( bean );
        log.debug( "-------------进入‘设备编写’的init(),处理业务逻辑结束,更新条数：" + count + "-----------------" );

    }

    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.debug( "-------------进入‘设备编写’的beforeRollback(),开始处理业务逻辑-----------------" );

        log.debug( "-------------进入‘设备编写’的beforeRollback(),处理业务逻辑结束-----------------" );
    }

    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备编写’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String businessId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        // update的时候拿出传入的参数
        String dataStr = null;
        try {
            dataStr = userInfo.getParam( "businessData" );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        log.info( "businessData=" + dataStr );

        if ( StringUtils.isNotBlank( dataStr ) ) {
            JSONObject jsonObject = JSONObject.fromObject( dataStr );
            String formData = jsonObject.getString( "formData" );
            String safeItems = jsonObject.getString( "safeItems" );
            // 是否在编辑模式（update时候"edit" 其他情况:other）
            String editFlag = jsonObject.getString( "editFlag" );

            PtwStandardBean bean = JsonHelper.fromJsonStringToBean( formData, PtwStandardBean.class );
            List<PtwStdSafeBean> items = VOUtil.fromJsonToListObject( safeItems, PtwStdSafeBean.class );
            // 编辑模式才更新
            if ( editFlag.equalsIgnoreCase( "edit" ) ) {
                // update
                ptwStandardService.updatePtwStandard( bean, items );
                
                //修改待办名称
                String todoNameString = bean.getWorkContent().length() > 100 ? bean.getWorkContent().substring(
                        0, 100 ) :  bean.getWorkContent();
                String sheetNo = ptwStandardService.queryPtwStandardById( businessId ).getSheetNo();
                ptwUtilService.modifyHomepageTodoName( sheetNo, todoNameString );
            }
        }
        log.debug( "-------------进入‘设备编写’的onComplete(),开始处理业务逻辑-----------------" );

    }
}
