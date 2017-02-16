package com.timss.itsm.flow.dpp.infowo.v001;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmInfoWoEquipment;
import com.timss.itsm.bean.ItsmWoPriConfig;
import com.timss.itsm.service.ItsmInfoWoEquipmentService;
import com.timss.itsm.service.ItsmInfoWoService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ItcenterLend extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmWoUtilService itsmWoUtilService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmInfoWoService itsmInfoWoService;
    @Autowired
    private ItsmInfoWoEquipmentService itsmInfoWoEquipmentService;
    
    private static final Logger LOG = Logger.getLogger( ItcenterLend.class );

    public void init(TaskInfo taskInfo) {

        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        String woStatus = "itCenterLend";
        itsmWoUtilService.updateBusinessStatus(itsmInfoWoService, infoWoId, woStatus );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘信息中心借出’的onComplete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();

        String businessData = null;
        try {
            businessData = userInfoScope.getParam( "businessData" );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }
        JSONObject businessDataobj = JSONObject.fromObject( businessData );
        String equipmentData = businessDataobj.getString( "equipmentData" );
        JSONObject equipmentJsonObj = JSONObject.fromObject(equipmentData);
        int equipmentDatagridNum =Integer.valueOf(equipmentJsonObj.get("total").toString());  //记录数
        JSONArray equipmentJsonArray = equipmentJsonObj.getJSONArray("rows"); //记录数组
        for(int i=0; i<equipmentDatagridNum; i++){
            String itemsRecord = equipmentJsonArray.get(i).toString();  //某条记录的字符串表示
            ItsmInfoWoEquipment infoWoEquipment = JsonHelper.fromJsonStringToBean(itemsRecord, ItsmInfoWoEquipment.class);
            infoWoEquipment.setInfoWoId( infoWoId );
            String infoWoEquipmentId = infoWoEquipment.getId();
            if(infoWoEquipmentId!=null && !"".equals(infoWoEquipmentId)){
                itsmInfoWoEquipmentService.updateItsmInfoWoEquipment( infoWoEquipment );
            }else{
                itsmInfoWoEquipmentService.insertItsmInfoWoEquipment( infoWoEquipment );
            }
            
        }
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "normal" );
        
        LOG.debug( "-------------进入‘信息中心借出’的onComplete(),业务逻辑处理结束-----------------" );

    }

    public void beforeRollback(TaskInfo taskInfo, String destTaskKey) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        workflowService.setVariable( taskInfo.getProcessInstanceId(), "rollbackFlag", "Y" );
        String infoWoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        itsmWoUtilService.updateBusinessCurrHandlerUser(itsmInfoWoService, infoWoId, userInfoScope, "rollback" );
    }

}
