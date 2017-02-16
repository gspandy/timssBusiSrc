package com.timss.pms.service.sfc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.WFService;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BiddingDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.PayDtlVo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

public class WFServiceImpl implements WFService{
	public static String projectPrefix="project_";
	public static String checkoutPrefix="checkout_";
	public static String payPrefix="pay_";
	public static String receiptPrefix="receipt_";
	public static String contractPrefix="contract_";//合同变更流程前缀
	public static String contractAppPrefix="contractApp_";//合同审批流程前缀
	public static String bidResultPrefix="bidResult_";
	public static String contractappPrefix="contractapp_";
	public static String biddingApplyPrefix="biddingapply_";
	
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	@Autowired
    ItcMvcService itcMvcService;
	
	private  boolean isExisted(String string){
		if(string==null || "".equals(string)){
			return false;
		}
		return true;
	}
	
	private  boolean inStringList(String string,List<String> list){
		boolean result=false;
		if(list!=null && string!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).equals(string)){
					result=true;
					break;
				}
			}
		}
		return result;
	}
	
	
	private Map<String,Object> createWFMap(String businessId,String processInstId,String taskId){
		Map<String,Object> map=new HashMap<String,Object>();
		String isCandidate="false";
		//获取节点信息
		Map<String,String> eMap=null;
		//对基础数据初始化
		//如果processInstId不存在，则根据businessId获取
		//如果taskId不存在，则根据processInstId获取
		if(isExisted(processInstId) && isExisted(taskId)){
			
		}else{
			if(StringUtils.isBlank(processInstId)){
				processInstId=workflowBusinessDao.queryWorkflowIdByBusinessId(businessId);
			}
			
			if(StringUtils.isNotBlank(processInstId)){
				List<Task> tasks=workflowService.getActiveTasks(processInstId);
				if(tasks==null || tasks.size()==0){
					taskId=null;
				}else{
					//当存在会签节点是，需要另外处理
					taskId=tasks.get(0).getId();
				}
			}else{
				return map;
			}
		}
		//判断是否需要显示审批按钮
		if(taskId==null){
			
		}else {
			List<String> users=workflowService.getCandidateUsers(taskId);
			String currentUser=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(inStringList(currentUser, users)){
				isCandidate="true";
				//获取节点信息
				eMap=workflowService.getElementInfo(taskId);
			}
			
		}
			
			
		map.put("processInstId", processInstId);
		map.put("taskId", taskId);
		map.put("isCandidate", isCandidate);
		map.put("elements", eMap);
		return map;
	}
	
	public  Map<String,Object> createProjectMap(String businessId,String processInstId,String taskId){
		Map<String,Object> map=createWFMap(projectPrefix+businessId, processInstId, taskId);
		return map;
	}

	@Override
	public Map<String,Object> createCheckoutMap(String businessId, String processInstId,
			String taskId) {
		Map<String,Object> map=createWFMap(checkoutPrefix+businessId, processInstId, taskId);
		return map;
	}


	@Override
	public Map<String,Object> createPayMap(PayDtlVo payDtlVo, String processInstId,
			String taskId) {
		Map<String,Object> map=null;
		Integer businessId=payDtlVo.getId();
		String prefix="";
		if("cost".equals(payDtlVo.getType())){
			prefix=payPrefix;
		}else if("income".equals(payDtlVo.getType())){
			prefix=receiptPrefix;
		}else{
			throw new PmsBasicException("结算中根据type解析为对应的流程时出现一个异常的type为"+payDtlVo.getType());
		}
		map=createWFMap(prefix+businessId, processInstId, taskId);
		return map;
	}

	@Override
        public Map<String,Object> createPayUndoMap(PayDtlVo payDtlVo, String processInstId,
                        String taskId) {
	        Map<String,Object> map=new HashMap<String,Object>();
	        String isCandidate="false";
                //获取节点信息
                Map<String,String> eMap=null;
                //对基础数据初始化
                //如果processInstId不存在，则根据businessId获取
                //如果taskId不存在，则根据processInstId获取
                if(isExisted(processInstId) && isExisted(taskId)){
                        
                }else{
                    processInstId=payDtlVo.getUndoFlowId();
                    if(StringUtils.isNotBlank(processInstId)){
                        List<Task> tasks=workflowService.getActiveTasks(processInstId);
                        if(tasks==null || tasks.size()==0){
                            taskId=null;
                        }else{
                            //当存在会签节点是，需要另外处理
                            taskId=tasks.get(0).getId();
                        }
                    }else{
                        return map;
                    }
                }
                //判断是否需要显示审批按钮
                if(taskId==null){
                        
                }else {
                    List<String> users=workflowService.getCandidateUsers(taskId);
                    String currentUser=itcMvcService.getUserInfoScopeDatas().getUserId();
                    if(inStringList(currentUser, users)){
                        isCandidate="true";
                        //获取节点信息
                        eMap=workflowService.getElementInfo(taskId);
                    }    
                }  
                map.put("processInstId", processInstId);
                map.put("taskId", taskId);
                map.put("isCandidate", isCandidate);
                map.put("elements", eMap);
                return map;
        }
	
	@Override
	public Map<String,Object> createContractMap(ContractDtlVo contractDtlVo,
			String processInstId, String taskId) {
		String businessId=String.valueOf(contractDtlVo.getId());
		Map<String,Object> map=createWFMap(businessId, processInstId, taskId);
		return map;
	}

	@Override
	public Map<String,Object> createContractAppMap(ContractDtlVo contractDtlVo,
			String processInstId, String taskId) {
		String businessId=String.valueOf(contractDtlVo.getId());
		Map<String,Object> map=createWFMap(contractAppPrefix+businessId, processInstId, taskId);
		return map;
	}
	
	@Override
	public Map<String,Object> createBidResultMap(BidResultDtlVo bidResultDtlVo) {
		Map<String,Object> map=createWFMap(WFServiceImpl.bidResultPrefix+bidResultDtlVo.getBidResultId(), bidResultDtlVo.getProcessInstId(), null);
		return map;
	}
	
	public String queryBusinessIdByFlowId(String proInstId){
		String bIdWithPrifix=workflowBusinessDao.queryBusinessIdByWorkflowId(proInstId);
		String bId=bIdWithPrifix.substring(bIdWithPrifix.lastIndexOf("_")+1);
		return bId;
	}
	
	public Map<String,Object> createBiddingMap(BiddingDtlVo biddingDtlVo){
		Map<String,Object> biddingApplyMap=createWFMap(WFServiceImpl.biddingApplyPrefix+biddingDtlVo.getId(), null, null);
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("biddingApply", biddingApplyMap);
		return resultMap;
	}
	@Override
	public Map<String, Object> createContractAppNullifyMap(ContractDtlVo contractDtlVo) {
	       return null;
	}
}
