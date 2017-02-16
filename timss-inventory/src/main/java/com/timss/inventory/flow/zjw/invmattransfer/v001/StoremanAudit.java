package com.timss.inventory.flow.zjw.invmattransfer.v001;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.bean.InvRealTimeData;
import com.timss.inventory.dao.InvMatTransferDao;
import com.timss.inventory.dao.InvMatTransferDetailDao;
import com.timss.inventory.dao.InvRealTimeDataDao;
import com.timss.inventory.exception.TransferException1;
import com.timss.inventory.exception.TransferException2;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.utils.InvMatTransferStatus;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class StoremanAudit  extends TaskHandlerBase{
	@Autowired
	private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private InvMatTransferDao invMatTransferDao;
    @Autowired
    private InvMatTransferDetailDao invMatTransferDetailDao;
    @Autowired
    private InvPubInterface invPubInterface;
    @Autowired
    private InvRealTimeDataDao invRealTimeDataDao;
    
    private static Logger logger = Logger.getLogger(StoremanAudit.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instanceId = taskInfo.getProcessInstanceId();
        String imtId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "StoremanAudit init --- instanceId = " + instanceId + "-- businessId = " + imtId );
        
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        InvMatTransfer invMatTransfer = new InvMatTransfer();
        invMatTransfer.setImtId(imtId);
        invMatTransfer.setStatus(InvMatTransferStatus.STOREMAN_AUDIT);
        invMatTransfer.setModifydate(new Date());
        invMatTransfer.setModifyuser(userInfoScope.getUserId());
    	invMatTransferDao.updateInvMatTransfer(invMatTransfer);  
    	
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo){
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String instanceId = taskInfo.getProcessInstanceId();
        String imtId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "StoremanAudit onComplete --- instanceId = " + instanceId + "-- businessId = " + imtId );
        
    	//获取物资数据
    	String businessData = null;
    	String invMatTransferData = null;
    	String invMatTransferDetailData = null;
    	InvMatTransfer invMatTransfer = null;
    	List<InvMatTransferDetail> imtdList = null;		
    	
		try {
			businessData = userInfoScope.getParam("businessData");
			JSONObject obj = JSONObject.fromObject(businessData);
			invMatTransferData = obj.getString("invMatTransferData");
	        invMatTransferDetailData = obj.getString( "invMatTransferDetailData" );
	        invMatTransfer = JsonHelper.toObject(invMatTransferData, InvMatTransfer.class);
	        imtdList = JsonHelper.toList( invMatTransferDetailData, InvMatTransferDetail.class );	
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	

        //校验是否填入移入货柜和物资类型，此处框架自定义异常不能包含在try catch中
        for ( InvMatTransferDetail invMatTransferDetail : imtdList ) {
        	if(StringUtils.isBlank( invMatTransferDetail.getToBinId() )
        			|| StringUtils.isBlank( invMatTransferDetail.getToCateTypeId() ) ){
    			logger.error("移库失败：未填写移入货柜或物资类型， 移库申请ID：" + imtId);
    			throw new TransferException2();
        	}else{
        		//校验移库数量是大于可出库数量
        		BigDecimal transferQty = invMatTransferDetail.getTransferQty();
        		String invcateId = invMatTransferDetail.getCateTypeId();
        		String itemId = invMatTransferDetail.getItemId();
        		String siteId = invMatTransferDetail.getSiteid();
        		InvRealTimeData irtd = invRealTimeDataDao.queryInvRealTimeDataByCompositeKey(itemId, invcateId, siteId);
        		BigDecimal nowqty = new BigDecimal(0);
        		BigDecimal actualQty = new BigDecimal(0);
        		if(irtd!=null){
        			nowqty = irtd.getCanUseQty();
        			actualQty = irtd.getActualQty();
        		}
        		if(transferQty.compareTo(nowqty)==1 || transferQty.compareTo(actualQty)==1 ){
        			logger.error("移库失败：移出数量不能超过可用库存和实际库存， 移库申请ID：" + imtId);
        			throw new TransferException1();
        		}
        	}
        }
        
		try {
	        //更新申请状态
	        invMatTransfer.setStatus(InvMatTransferStatus.DONE);
	        invMatTransfer.setModifydate(new Date());
	        invMatTransfer.setModifyuser(userInfoScope.getUserId());
	    	invMatTransferDao.updateInvMatTransfer(invMatTransfer);  
	    	
	        //删除旧物资
	    	invMatTransferDetailDao.deleteInvMatTransferDetailByImtId(imtId);
	    	
	        //插入物资
	        for ( InvMatTransferDetail invMatTransferDetail : imtdList ) {
	        	invMatTransferDetail.setImtdId(null);
	        	invMatTransferDetail.setImtId(imtId);
	        	invMatTransferDetail.setDeleted("0");
	        	invMatTransferDetail.setCreatedate(new Date());
	        	invMatTransferDetail.setCreateuser(userId);
	        	invMatTransferDetail.setSiteid(userInfoScope.getSiteId());
	        	invMatTransferDetail.setDeptid(userInfoScope.getOrgId());
	        	invMatTransferDetailDao.insertInvMatTransferDetail(invMatTransferDetail);
	        }
	        
	        //重新查询获取完整信息
	        invMatTransfer = invMatTransferDao.queryInvMatTransferById(imtId);
	        imtdList = invMatTransferDetailDao.queryInvMatTransferDetailList(imtId);
	        
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
        Map<String, String> transferResult = invPubInterface.saveTransfer2MatTran(invMatTransfer, imtdList);
        if(transferResult.containsKey("false")){
			logger.error("移库失败：" + transferResult.get("false") + "    移库申请ID：" + imtId);
			throw new TransferException1();
		}
        
        super.onComplete( taskInfo );
    }
    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        String instanceId = taskInfo.getProcessInstanceId();
        String imtId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "StoremanAudit beforeRollback --- instanceId = " + instanceId + "-- businessId = " + imtId );
        
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        InvMatTransfer invMatTransfer = new InvMatTransfer();
        invMatTransfer.setImtId(imtId);
        invMatTransfer.setStatus(InvMatTransferStatus.TRANSFER_APPLY_COMMIT);
        invMatTransfer.setModifydate(new Date());
        invMatTransfer.setModifyuser(userInfoScope.getUserId());
    	invMatTransferDao.updateInvMatTransfer(invMatTransfer);  
    	
	    super.beforeRollback(taskInfo, destTaskKey);		
    }
}
