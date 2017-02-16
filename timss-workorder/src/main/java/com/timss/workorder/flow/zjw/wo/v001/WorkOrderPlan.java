package com.timss.workorder.flow.zjw.wo.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvItemVO;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.WoProcessStatusUtilZJW;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WorkOrderPlan  extends TaskHandlerBase {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkOrderDao workOrderDao;
    @Autowired
    private InvMatApplyService invMatApplyService;
	@Autowired
	private WoUtilService woUtilService;
	private static Logger logger = Logger.getLogger(WorkOrderPlan.class);
	
    @Override
	public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderPlan init --- instantId = " + instantId + "-- businessId = " + woId );
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtilZJW.WORK_ORDER_PLAN);
        super.init( taskInfo );
	}
	
    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderPlan onComplete --- instantId = " + instantId + "-- businessId = " + woId );
    	
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String businessData = null;
		try {
			businessData = userInfoScope.getParam("businessData");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		JSONObject obj = JSONObject.fromObject(businessData);
		String woFormDataStr = obj.getString("workOrderForm");
        String toolDataStr = obj.getString( "toolData" );
		WorkOrder workOrder;
		WorkOrder tempObject;
		try {
			tempObject = JsonHelper.toObject(woFormDataStr, WorkOrder.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 

		//查询已有的物资领料申请数量
		int matApplyCount = 0;
		List<InvMatApply> invApplyList;
		try {
			invApplyList = invMatApplyService.queryMatApplyByOutterId(woId,"wo_picking");
			matApplyCount = invApplyList.size();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
        //获取策划时 工具的内容列表
        JSONObject itemsJsonObj = JSONObject.fromObject( toolDataStr );
        int itemsDatagridNum = Integer.valueOf( itemsJsonObj.get( "total" ).toString() ); //记录数
        JSONArray itemsJsonArray = itemsJsonObj.getJSONArray( "rows" ); //记录数组
        List<JSONObject> jpPlanItemsObjList = new ArrayList<JSONObject>();
        for ( int i=0; i<itemsDatagridNum; i++ ){
            String itemsRecord = itemsJsonArray.get( i ).toString(); //某条记录的字符串表示
            JPItems jpItems;
            InvItemVO invMatApplyDetail;
            try {
                jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, JPItems.class );
            	invMatApplyDetail = JsonHelper.fromJsonStringToBean( itemsRecord, InvItemVO.class );
            } catch(Exception e){
                logger.error( e.getMessage() );
                throw new RuntimeException( e );
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( jpItems.getItemsId() + "_applyCount", jpItems.getApplyCount() );
            jsonObject.put( jpItems.getItemsId() + "_wareHouseId", invMatApplyDetail.getWarehouseid() );
            jsonObject.put( jpItems.getItemsId() + "_cateId", invMatApplyDetail.getCateId() );
            jpPlanItemsObjList.add( jsonObject );
        }
        HashMap<String, Object> dataToInventory = new HashMap<String, Object>();
        dataToInventory.put( "applyUser", userInfoScope.getUserId() );
        dataToInventory.put( "woName", "工单"+ tempObject.getWorkOrderCode() + "领料申请" + (matApplyCount+1));
        dataToInventory.put( "type", "out" );
        dataToInventory.put( "woId", woId );
        dataToInventory.put( "woCode", tempObject.getWorkOrderCode() );
        dataToInventory.put( "applyType", "wo_picking" );
        dataToInventory.put( "items", jpPlanItemsObjList );
        if ( itemsDatagridNum > 0 ) {
            //库存出库申请 提供数据是：dataToInventory ，格式是：
            //{woName=SBS_0706001, applyUser=962023, items=[{"III1259":1}], woId=2023, woCode=WO20150706002, applyType=wo_picking, type=out}
            try {
                invMatApplyService.workOrderTriggerProcesses( dataToInventory );
            } catch (Exception e) {
                logger.error( "工单调用库存的接口出错："+e.getMessage());
                throw new RuntimeException( e );
            }
        }
        
		try {
			//修改工单信息  
			workOrder = new WorkOrder();
			workOrder.setId(Integer.valueOf(woId));
			workOrder.setCycleBeginTime(new Date());//策划提交时间
	        workOrder.setModifydate(new Date());
	        workOrder.setModifyuser(userInfoScope.getUserId());
			workOrderDao.updateWoAuditInfoZJW(workOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		
		//修改处理人
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
	    super.onComplete( taskInfo );
    }
	    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WorkOrderPlan beforeRollback --- instantId = " + instantId + "-- businessId = " + woId );
		//修改处理人
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"rollback");
	    super.beforeRollback(taskInfo, destTaskKey);		
    }
}
