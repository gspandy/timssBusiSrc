package com.timss.inventory.flow.swf.accept.v001;

import java.math.BigDecimal;
import java.util.List;

import org.activiti.engine.runtime.Execution;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.core.InvMatAcceptDetailService;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.service.swf.SendReceiveAllEmailService;
import com.timss.inventory.utils.AcceptStatusEnum;
import com.timss.inventory.utils.GetAcceptDataInWorkflowUtil;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.workflow.IsAcceptToRK;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description: 专家验收
 * @company: gdyd
 * @className: AidDistribution.java
 * @author: 890166
 * @createDate: 2014-7-29
 * @updateUser: 890166
 * @version: 1.0
 */
public class Zjys extends TaskHandlerBase {

	private static final Logger LOGGER = Logger.getLogger(Zjys.class);
	@Autowired
    InvMatAcceptService invMatAcceptService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    GetAcceptDataInWorkflowUtil getAcceptDataInWorkflowUtil;
    @Autowired
    InvMatAcceptDetailService invMatAcceptDetailService;
    @Autowired
    IsAcceptToRK isAcceptToRK;
    @Autowired
    private SelectUserService selectUserService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    SendReceiveAllEmailService sendReceiveAllEmailService;
    
    @Override
    public void init(TaskInfo taskInfo) {
    	
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	
    	//更新
    	InvMatAccept invMatAccept=getAcceptDataInWorkflowUtil.getAccept();
    	List<InvMatAcceptDetail> details=getAcceptDataInWorkflowUtil.getDetails();
    	List<InvMatAcceptDetailVO> detailVOs=getAcceptDataInWorkflowUtil.getDetailVOs();
    	invMatAcceptService.updateInvMatAccept(invMatAccept);
		invMatAcceptDetailService.updateInvMatAcceptDetail(invMatAccept, details);
		
		Execution execution=workflowService.getProcessByProcessInstId(taskInfo.getProcessInstanceId());
		//如果需要入库，则进行入库操作
	    if(isAcceptToRK.isApproval(execution, null)){
	    	
	    	String poSheetno = invMatAccept.getPoSheetno();
	    	String poName = invMatAccept.getPoName();
	    	selectUserService.setProcessInstId(taskInfo.getProcessInstanceId());//一定要
	    	List<SecureUser> resultList = selectUserService.byHistroicAssignee("cgsqrzxys");//获取“采购申请人执行验收”节点审批人
	    	String checkUser = resultList.get(0).getId();//“采购申请人执行验收”节点审批人作为验收人
	    	invMatAccept.setInstanceid(taskInfo.getProcessInstanceId());
	    	try {
				invMatAcceptService.saveMatTran(invMatAccept, detailVOs, checkUser);
			} catch (Exception e) {
				LOGGER.error(e);
			    throw new RuntimeException( "流程审批结束时物资入库失败" , e );
			}
	    	//更新为入库状态
	    	String inacId=(String) workflowService.getVariable(taskInfo.getProcessInstanceId(), "inacId");
	        invMatAccept=new InvMatAccept();
	        invMatAccept.setInacId(inacId);
	        invMatAccept.setStatus(AcceptStatusEnum.INVIN.toString());
	        invMatAcceptService.updateInvMatAccept(invMatAccept);
	        
	        //检查物质是否全部入库
	        List<PurOrderItemVO> list = purOrderService.queryPurApplyOrderItemList(poSheetno,"SWF");
	        if(list!= null && list.size() != 0){
	        	for (PurOrderItemVO pur : list) {
	        	    BigDecimal intSum = pur.getInSum();
                            BigDecimal itemSum = BigDecimal.valueOf(Double.valueOf(pur.getItemnum()));
                                    if(!(intSum.compareTo(itemSum)==0 || intSum .compareTo(  itemSum)==1 ) && itemSum.compareTo( BigDecimal.valueOf(0L) )!=0 ){
                                            //跳出循环,因为还没全部入库
                                            return;
                                    }
                            }
	        	//发送邮件
	        	LOGGER.info("物资已全部入库,需发送邮件通知仓管员.");
	        	try {
					sendReceiveAllEmailService.sendEmail(poSheetno, poName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error(e);
					throw new RuntimeException(e);
				}
	        }
	    	
	    }
		
    }
}
