package com.timss.pms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.PayplanStatusUtil;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BiddingDtlVo;
import com.timss.pms.vo.CheckoutDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.InvoiceDtlVo;
import com.timss.pms.vo.PayDtlVo;
import com.timss.pms.vo.PayplanVo;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PrivilegeService {
	private static String tmpSaveButton="b-tmp-save";
	private static String saveButton="b-save";
	private static String deleteButton="b-del";
	private static String WORKFLOW="workflow";
	private static String WORKFLOW_SEC="workflowSec";
	private static String NULLIFY_WORKFLOW="nullifyworkflow";
	@Autowired
	WFService wfService;
	@Autowired
	private IAuthorizationManager authorizationManager;
	/**
	 * 草稿状态下设置权限
	 * @Title: createDraftMap
	 * @param map
	 */
	private  void createDraftMap(Map<String,Object> map){
		map.put(tmpSaveButton, true);
		map.put(saveButton, true);
		map.put(deleteButton, true);
		map.put("readOnly", false);
	}
	
	/**
	 * 获取年度计划页面的权限
	 * @Title: createPlanPrivilege
	 * @param planDtlVo
	 * @param itcMvcService
	 * @return
	 */
	public  Map<String,Object> createPlanPrivilege(PlanDtlVo planDtlVo,ItcMvcService itcMvcService){
		if(planDtlVo==null){
			return null;
		}
		Map<String,Object> priMap=getHashMapPrivilege(itcMvcService);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("readOnly", true);
		String status=planDtlVo.getStatus();
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			if( priMap.containsKey("pms-b-project-add")){
				map.put("pms-b-project-add", true);
			}
			if( priMap.containsKey("pms-b-plan-change")){
				map.put("pms-b-plan-change", true);
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(planDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		return map;
	}
	
	/**
	 * 创建返回的项目立项页面的权限信息
	 * @Title: createProjectPrivilege
	 * @param projectDtlVo
	 * @param itcMvcService
	 * @return
	 */
	public  Map<String,Object> createProjectPrivilege(ProjectDtlVo projectDtlVo,ItcMvcService itcMvcService){
		if(projectDtlVo==null){
			return null;
		}
		//获取用户权限
		Map<String,Object> priMap=getHashMapPrivilege(itcMvcService);
		//存放返回的权限信息
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("readOnly", true);
		String status=projectDtlVo.getStatus();
		Map<String,Object> workflowMap=wfService.createProjectMap(String.valueOf(projectDtlVo.getId()), null, null);
		map.put(WORKFLOW, workflowMap);
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
		        //审批中且退回了首环节，显示作废按钮
		        Object elementsObj= workflowMap.get( "elements" );
		        if ( null!=elementsObj ) {
		            Map<String, String> elements = (Map<String, String>)elementsObj;
		            
		            if (null!=elements && null!=elements.get( "__elementKey__" ) && StringUtils.equals( "Apply", String.valueOf( elements.get( "__elementKey__" ) ) ) ) {
		                map.put("b-workflow-del", true);
                            } 
                        }
			if("true".equals(workflowMap.get("isCandidate"))){
				workflowMap.put("approve", true);
			}
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			if(priMap.containsKey("pms-b-br-add") && priMap.containsKey("pms-b-contract-add")){
				map.put("pms-project-complex-add", true);
			}else if(priMap.containsKey("pms-b-br-add")){
				map.put("pms-b-br-add", true);
			}else if(priMap.containsKey("pms-b-contract-add")){
				map.put("pms-b-contract-add", true);
			}
			if(priMap.containsKey("pms-milestone-edit-actualtime")){
				map.put("pms-milestone-edit-actualtime", true);
			}
			if(priMap.containsKey("pms-milestone-change")){
				map.put("pms-milestone-change", true);
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(projectDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		
		
		return map;
	}
	
	/**
	 * 创建返回给合同详细信息页面的权限信息
	 * @Title: createContractPrivilege
	 * @param contractDtlVo
	 * @param itcMvcService
	 * @return
	 */
	public  Map<String,Object> createContractPrivilege(ContractDtlVo contractDtlVo,ItcMvcService itcMvcService){
		if(contractDtlVo==null){
			return null;
		}
		//获取用户权限
		Map<String,Object> priMap=getHashMapPrivilege(itcMvcService);
		//存放返回的权限信息
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("readOnly", true);
		
		String status=contractDtlVo.getStatus();
		//合同审批状态
		String statusApp=contractDtlVo.getStatusApp();
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		Map<String,Object> workflowMap=null;
		workflowMap=wfService.createContractMap(contractDtlVo, contractDtlVo.getProcessInstId(), null);
		
		map.put(WORKFLOW, workflowMap);
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			if("true".equals(workflowMap.get("isCandidate"))){
				workflowMap.put("approve", true);
				map.put("b-contract-change-workflow", true);
			}
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			//如何可以新建两个，则合并
			if(priMap.containsKey("pms-b-checkout-add") && priMap.containsKey("pms-b-pay-add")){
				map.put("pms-b-contract-complex", true);
			}else if(priMap.containsKey("pms-b-checkout-add")){
				map.put("pms-b-checkout-add", true);
			}else if(priMap.containsKey("pms-b-pay-add")){
				map.put("pms-b-pay-add", true);
			}
			if(priMap.containsKey("pms-b-checkout-query") || priMap.containsKey("pms-b-pay-query")|| priMap.containsKey("pms-b-project-query")){
			    map.put("pms-b-contract-complex2", true);
			}
			if(priMap.containsKey("pms-b-project-query")){
                            map.put("pms-b-project-query", true);
                        }
			if(priMap.containsKey("pms-b-checkout-query")){
				map.put("pms-b-checkout-query", true);
			}
			if(priMap.containsKey("pms-b-pay-query")){
				map.put("pms-b-pay-query", true);
			}
			if(priMap.containsKey("pms-b-contract-change-payplan")){
				map.put("pms-b-contract-change-payplan", true);
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(contractDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		Map<String,Object> appNullifyWorkflowMap=wfService.createContractAppNullifyMap(contractDtlVo);
                map.put(NULLIFY_WORKFLOW, appNullifyWorkflowMap);
		Map<String,Object> appWorkflowMap=null;
                appWorkflowMap=wfService.createContractAppMap(contractDtlVo,null,null);
		map.put(WORKFLOW_SEC, appWorkflowMap);
		//显示结算计划的权限
		map.put("pms-payPlanList-show", false);
		//显示添加结算计划按钮的权限
		map.put("pms-payPlanList-edit", false);
		//如果是审批中状态(合同审批)
		if(ChangeStatusUtil.approvingCode.equals(statusApp)){
			//如果是审核中，且当前用户为当前流程节点的候选人时，需要通知前端，并赋予显示审批按钮的权限
			if("true".equals(appWorkflowMap.get("isCandidate"))){
				appWorkflowMap.put("approve", true);
				//审批按钮
				map.put("pms-contract-approve", true);
				//审批中-客户经理提交 显示作废
				if(null!=appWorkflowMap.get("elements")&&("account_manager_apply".equals(((Map)appWorkflowMap.get("elements")).get("__elementKey__"))||
				        "apply".equals(((Map)appWorkflowMap.get("elements")).get("__elementKey__"))
				        )){
					map.put("pms-workflwo-del", true);
					map.put("readOnly", false);
				}
				//审批中-合同结算 显示结算计划 以及添加结算计划的按钮
				if(null!=appWorkflowMap.get("elements")&&"settle_account".equals(((Map)appWorkflowMap.get("elements")).get("__elementKey__"))){
					map.put("pms-payPlanList-show", true);
					map.put("pms-payPlanList-edit", true);
				}
			}
			
		}else if(ChangeStatusUtil.approvalCode.equals(statusApp)){
			//审批通过 显示结算计划
			map.put("pms-payPlanList-show", true);
			//审批完成-商务专责发起作废流程
                        Boolean isSWZZ = false;
                        List<Role> roles = userInfo.getRoles();
                        for ( Role role : roles ) {
                            if ( "ITC_SWZZ".equals( role.getName() ) ) {
                                isSWZZ = true;
                                break;
                            }
                        }
			if(isSWZZ){
                                map.put("pms-workflow-nullify", true);
                        }
			//如果合同审批完成，且有申请作废权限
			if(StringUtils.isEmpty( contractDtlVo.getNullifyProcInstId() )&&priMap.containsKey("pms-workflow-nullify")){
                            map.put("pms-workflow-nullify", true);
                        }
			//如果合同审批完成，且已在进行作废流程
			if(StringUtils.isNotEmpty( contractDtlVo.getNullifyProcInstId() )){
			    map.put("b-contract-nullify-workflow", true);
			    //如果还是待办人
			    if ( "true".equals(appNullifyWorkflowMap.get("isCandidate")) ) {
			        map.put("pms-workflow-nullify-audit", true);
                            }
                        }
		}else if(ChangeStatusUtil.draftCode.equals(statusApp)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			//如果是草稿的话，如果当前用户是创建人，按照草稿的状态分配权限
			if(userId.equals(contractDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}else{
			//没有流程信息的老合同数据
			map.put("pms-payPlanList-show", true);
		}
		
		return map;
	}
	public  Map<String,Object> createBidPrivilege(BidDtlVo bidDtlVo,ItcMvcService itcMvcService){
		if(bidDtlVo==null){
			return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("readOnly", true);
		String status=bidDtlVo.getStatus();
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			if (bidDtlVo.getBidMethod() == null) {
				map.put("b-add-bidmethod", true);
			}else{
				BidMethod bidMethod = bidDtlVo.getBidMethod().get(0);
				if (bidMethod.getBidMethodId() == null) {
					map.put("b-add-bidmethod", true);
				} else if (bidMethod.getBidMethodId() != null
						&& ChangeStatusUtil.approvalCode.equals(bidMethod
								.getStatus())) {
					if (bidDtlVo.getBidResult() == null) {
						map.put("b-add-bidresult", true);
					} else {
						BidResult bidResult = bidDtlVo.getBidResult().get(0);
						if (bidResult.getBidResultId() == null) {
							map.put("b-add-bidresult", true);
						}else{
							if(ChangeStatusUtil.approvalCode.equals(bidResult.getStatus())){
								map.put("b-add-contract", true);
							}
						}
					}
				}
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(bidDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		return map;
	}
	
	public  Map<String,Object> createBidResultPrivilege(BidResultDtlVo bidResultDtlVo,ItcMvcService itcMvcService) {
		if(bidResultDtlVo==null){
			return null;
		}
		//权限信息
		Map<String,Object> priMap=getHashMapPrivilege(itcMvcService);
		//返回的权限map
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("readOnly", true);
		String status=bidResultDtlVo.getStatus();
		Map<String,Object> workflowMap=wfService.createBidResultMap(bidResultDtlVo);
		map.put(WORKFLOW, workflowMap);
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			if("true".equals(workflowMap.get("isCandidate"))){
				workflowMap.put("approve", true);
			}
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			if(priMap.containsKey("pms-b-contract-add")){
				map.put("pms-b-contract-add", true);
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(bidResultDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		return map;
	}
	
	public  Map<String,Object> createCheckoutPrivilege(CheckoutDtlVo checkoutDtlVo,ItcMvcService itcMvcService){
		if(checkoutDtlVo==null){
			return null;
		}
		//权限信息
		Map<String,Object> priMap=getHashMapPrivilege(itcMvcService);
		//返回的权限map
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> workflowMap=wfService.createCheckoutMap(String.valueOf(checkoutDtlVo.getId()), checkoutDtlVo.getProcessInstId(), null);
		map.put(WORKFLOW, workflowMap);
		map.put("readOnly", true);
		String status=checkoutDtlVo.getStatus();
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			if("true".equals(workflowMap.get("isCandidate"))){
				workflowMap.put("approve", true);
			}
			Object elements = workflowMap.get("elements");
                        if ( null!=elements ) {
                            if("apply".equals(((Map)elements).get("__elementKey__"))&&"true".equals(workflowMap.get("isCandidate"))){
                                map.put("pms-flow-void", true);
                            }    
                        }
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			if(priMap.containsKey("pms-b-pay-add")){
				List<PayplanVo> payplanVos=checkoutDtlVo.getPayplanVos();
				if(payplanVos!=null && payplanVos.size()!=0&& PayplanStatusUtil.isPayplanPayable(payplanVos.get(0).getPayStatus())){
					map.put("pms-b-pay-add", true);
				}
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(checkoutDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}
		return map;
	}
	private boolean hasRole(List<Role> roles,String roleCode){
	    boolean result = false;
	    if(StringUtils.isNotEmpty( roleCode )){
	        for ( Role role : roles ) {
	            if(roleCode.equals( role.getId() )){
	                result = true;
	            }
	        }   
	    }
	    return result ;
	}
	
	public  Map<String,Object> createPayPrivilege(PayDtlVo payDtlVo,ItcMvcService itcMvcService){
		if(payDtlVo==null){
			return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> workflowMap=wfService.createPayMap(payDtlVo, null, null);
		Map<String,Object> undoWorkflowMap=wfService.createPayUndoMap(payDtlVo, null, null);
		map.put(WORKFLOW, workflowMap);
		map.put("readOnly", true);
		map.put("beginEdit", "");    
		String status=payDtlVo.getStatus();
		String unflowStatus = payDtlVo.getUndoStatus();
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			if("true".equals(workflowMap.get("isCandidate"))){
				workflowMap.put("approve", true);
			}
			Object elements = workflowMap.get("elements");
			if ( null!=elements ) {
			    if("apply".equals(((Map)elements).get("__elementKey__"))&&"true".equals(workflowMap.get("isCandidate"))){
			        map.put("pms-flow-void", true);
	                    }    
                        }
			if ( null!=elements){
			     Object modifiable = ((Map<String,Object>)elements).get( "modifiable" );
			     if(null!=modifiable){
			         if(modifiable.toString().contains( "\"beginEdit\":\"all\"" )){
			             map.put("beginEdit", "all");                                        
			         }
			     }
			 }
			//既可以审批又是可编辑环节
			 if ( "true".equals( String.valueOf(workflowMap.get("approve")) ) 
			         &&!"".equals(String.valueOf(map.get("beginEdit"))) ) {
			     map.put("readOnly", false);
                        }
		}else if(ChangeStatusUtil.approvalCode.equals(status)&&"income".equals( payDtlVo.getType() )){
			if(hasRole(itcMvcService.getUserInfoScopeDatas().getRoles(),"ITC_SWZZ")){
			    workflowMap.put( "undoApply", true );
			}
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(payDtlVo.getCreateUser())){
				createDraftMap(map);
			}
		}else if("undoing".equals( status )){
		    if ( "true".equals( undoWorkflowMap.get( "isCandidate" ) ) ) {
                        //退票审批按钮
		        workflowMap.put( "undoApprove", true );
		        if("businessTaskApply".equals( payDtlVo.getUndoStatus() )){
		            workflowMap.put( "undoApproveRollBack", true );
		        }
                    }
		    //退票审批信息按钮
		    workflowMap.put( "undoApproveInfo", true );
		}else if(ChangeStatusUtil.voidedCode.equals( status )&&"pass".equals( unflowStatus )){
		    workflowMap.put( "undoApproveInfo", true );
		}
		workflowMap.put( "undotaskId", undoWorkflowMap.get( "taskId" ) );
		return map;
	}
	
	public Map<String,Object> createInvoicePrivilege(InvoiceDtlVo invoiceDtlVo,ItcMvcService itcMvcService){
		if(invoiceDtlVo==null){
			return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		if(!"Y".equals(invoiceDtlVo.getIscheck())){
			map.put("b-confirm", true);
		}
		return map;
	}
	
	/**
	 * 获得hashMap类型的权限信息，方便比较。
	 * @Title: getHashMapPrivilege
	 * @param itcMvcService
	 * @return
	 */
	private Map<String,Object> getHashMapPrivilege(ItcMvcService itcMvcService){
		List<String> list=itcMvcService.getUserInfoScopeDatas().getSecureUser().getPrivileges();
		Map<String,Object> map=new HashMap<String,Object>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				map.put(list.get(i), true);
			}
		}
		return map;
	}
	
	public Map<String,Object> createBiddingPrivilege(BiddingDtlVo biddingDtlVo,ItcMvcService itcMvcService){
		if(biddingDtlVo==null){
			return null;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> workflowMap=wfService.createBiddingMap(biddingDtlVo);
		map.put(WORKFLOW, workflowMap);
		map.put("readOnly", true);
		String status=biddingDtlVo.getStatus();
		//如果是审批中状态
		if(ChangeStatusUtil.approvingCode.equals(status)){
			
		}else if(ChangeStatusUtil.approvalCode.equals(status)){
			
		}else if(ChangeStatusUtil.draftCode.equals(status)){
			String userId=itcMvcService.getUserInfoScopeDatas().getUserId();
			if(userId.equals(biddingDtlVo.getCreateuser())){
				createDraftMap(map);
			}
		}
		return map;
	}
	
	public List<SecureUser> getUserByRoleAndSite(String roleId,String siteId){
	    List<SecureUser> result = authorizationManager.retriveUsersWithSpecificRoleAndSite( roleId, siteId );
	    return result;
	}
	
	public SecureUser getManagerInfo(UserInfo userInfo){
	    String siteId = userInfo.getSiteId();
	    List<SecureUser> managerList = getUserByRoleAndSite( "SJW_ZJL", siteId );
            SecureUser manager = null;
            if ( 0 < managerList.size() ) {
                manager = managerList.get( 0 );
            }
            return manager;
	}
}
