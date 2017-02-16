package com.timss.finance.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.facade.util.EipBranchFlowProcessUtil;
import com.timss.facade.util.FlowEipUtil;
import com.timss.facade.util.InitVoEnumUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.exception.FinanceBaseException;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.util.EipMobileCommon;
import com.timss.finance.vo.FinanceMainDetailVo;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dao.support.AttachmentMapper;
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
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.itc.util.ReflectionUtils;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
@EipAnnotation("fin_ma")
@Component
public class FMAToEipMobileListen implements EipMobileInterface {

	private static final Logger logger = Logger.getLogger(FMAToEipMobileListen.class);
	@Autowired
	FinanceManagementApplyService financeManagementApplyService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private EipMobileCommon emc;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
	private IEnumerationManager iEnumerationManager;
	
	/* (non-Javadoc)向业务模块获取待办详情。这个接口需要接入EIPM的每个流程去实现，框架根据EIPM传递的参数来调用指定的模块
	 * @see com.yudean.interfaces.interfaces.EipMobileInterface#retrieveWorkflowFormDetails(com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean)
	 */
	@Override
	public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
		RetContentBean efdb = new RetContentBean();
		
		//表单的sheetNo
		String flowNo = eipmobileparambean.getFlowNo();
		//流程实例id
		String processId = eipmobileparambean.getProcessId();
		
		logger.info("flowNo: " + flowNo);
		logger.info("processId: " + processId);
		
		try {
			UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
			
			//1.组装表单和表格数据
			List<RetContentInLineBean> forms = assembleContent(userInfo, flowNo, processId);
			efdb.setContent(forms);
			
			//2.组装附件数据
			List<RetAttachmentBean> attachements = assembleAttachements(flowNo, attachmentMapper);
			efdb.setAttachments(attachements);
			
			List<RetFlowsBean> flows = emc.assembleFlows(userInfo, processId);
			efdb.setFlows(flows);
		} catch (Exception e) {
			throw new FinanceBaseException("财务eip接口获取数据失败，对应flowno为"+flowNo, e);
		}
		return efdb;
	}

	private List<RetAttachmentBean> assembleAttachements(String flowNo,
			AttachmentMapper attachmentMapper2) {
		
		FinanceManagementApplyDtlVo financeManagementApplyDtlVos=financeManagementApplyService.queryFinanceManagementApplyById(flowNo);
		String attach=financeManagementApplyDtlVos.getAttach();
		
		List<RetAttachmentBean> retAttachmentBeans = new ArrayList<RetAttachmentBean>();
		if(StringUtils.isNotBlank(attach)){
			retAttachmentBeans=FlowEipUtil.assembleAttachements(attach, attachmentMapper2);
		}
		return retAttachmentBeans;
	}
	
	

	/* (non-Javadoc)向业务模块传递办理信息，这个接口需要接入EIPM的每个流程去实现，框架根据EIPM传递的参数来调用指定的模块。当EIPM传递某个流程的处理信息时，接口被触发
	 * @see com.yudean.interfaces.interfaces.EipMobileInterface#processWorkflow(com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean)
	 */
	@Override
	public RetProcessBean processWorkflow(ParamProcessBean eipmobileparambean) {
		RetProcessBean emrb = new RetProcessBean();
		String flowId = eipmobileparambean.getFlowID();//用户返回选择id
		String opinion = eipmobileparambean.getOpinion();//用户填写意见
		String processId = eipmobileparambean.getProcessId();//流程id
		String taskKey=eipmobileparambean.getTaskKey();
		List<String> nuser = eipmobileparambean.getNextUser();//下一环节处理人员
		
		boolean flag = false;
		try {
			UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
			if("next".equals(flowId)){//提交下一环节
			  FinanceManagementApply financeManagementApplyDtlVo = financeManagementApplyService.queryFinanceManagementApplyByProcessId( processId );
				//预先设置流程变量
				setFlowVariables(processId,taskKey,financeManagementApplyDtlVo);
				flag = emc.commitToNextLink(userInfo,opinion,nuser,processId);
			}else if("rollback".equals(flowId)){//回退
				flag = emc.returnToPreviousLink(userInfo,opinion,processId);
			}else{//终止流程
				flag = emc.toStopCurProcess(userInfo, opinion, processId);
			}
			//若流程提交操作成功，返回成功状态
			if(flag){
				emrb.setRetcode(1);
				emrb.setRetmsg("success");
			}else{
				emrb.setRetcode(-1);
			}
		}catch(Exception e){
			logger.error("eip接口执行流程流转命令失败，flowno为"+flowId+" ;opinion为"+opinion,e);
			emrb.setRetcode(-1);
		}
		return emrb;
	}
	//设置流程变量
	private void setFlowVariables(String processId, String taskKey,FinanceManagementApply financeManagementApply) throws Exception {
		EipBranchFlowProcessUtil eipBranchFlowProcessUtil=new EipBranchFlowProcessUtil(workflowService);
		eipBranchFlowProcessUtil.setFlowVariablesBeforeCommit(processId, taskKey,financeManagementApply);
	}

	/** 
	 * @description: 组织表单和表格数据
	 * @author: 890170
	 * @createDate: 2014-11-11
	 */
	private List<RetContentInLineBean> assembleContent(UserInfoScope userInfo,String flowNo,String processId) throws Exception {
		String siteid = userInfo.getSiteId();
	    List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
		RetContentInLineBean rcilbOpi = null;
		
		FinanceManagementApplyDtlVo financeManagementApplyDtlVos=financeManagementApplyService.queryFinanceManagementApplyById(flowNo);
		List<AppEnum> applyTypeAppEnum = iEnumerationManager.retrieveEnumeration("FIN_APPLY_TYPE" , financeManagementApplyDtlVos.getApplyType());
		String toEipBeanName="信息";
		for ( AppEnum appEnum : applyTypeAppEnum ) {
                    if(appEnum.getSiteId().equals( siteid )){
                        toEipBeanName = appEnum.getLabel()+ toEipBeanName;
                    }
                }
//		List<RetContentInLineBean> contentInLineBeans=new ArrayList<RetContentInLineBean>();
		//组装基本信息
		RetContentInLineBean retContentInLineBean=new RetContentInLineBean();
		retContentInLineBean.setFoldable(true);
		retContentInLineBean.setIsShow(true);
		retContentInLineBean.setType(Type.KeyValue);
		
		retContentInLineBean.setName(toEipBeanName);
		retContentInLineBean.setValue(getFMAMessage(financeManagementApplyDtlVos));
		
		epbList.add(retContentInLineBean);
		
		retContentInLineBean=new RetContentInLineBean();
		retContentInLineBean.setFoldable(true);
		retContentInLineBean.setIsShow(true);
		retContentInLineBean.setType(Type.Table);
		retContentInLineBean.setName("申请明细");
		retContentInLineBean.setValue(getFMAListMessage(financeManagementApplyDtlVos));
		
		epbList.add(retContentInLineBean);
		//组装opions中数据
		rcilbOpi = emc.assembleOpinions(processId);
		
		epbList.add(rcilbOpi);
		
		return epbList;
	}
	
	private List<Object> getFMAListMessage(
			FinanceManagementApplyDtlVo financeManagementApplyDtlVos) {
		List<Object> results=new ArrayList<Object>();
		List<FinanceMainDetailVo> financeMainDetailVos=financeManagementApplyDtlVos.getFinanceMainDetailVos();
		for(int i=0;i<financeMainDetailVos.size();i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			FinanceMainDetailVo financeMainDetail=financeMainDetailVos.get(i);
			
			map.put("title", "金额"+financeMainDetail.getAmount()+"元");
			map.put("rows", toFMDplanMap(financeMainDetail));
			
			results.add(map);
		}
		return results;
	}

	private Object toFMDplanMap(FinanceMainDetailVo financeMainDetail) {
		List<Object> resultList=new ArrayList<Object>();
		String projectEipForm="申请金额##amount,申请事由##description,备注##remark";
		
	    //解析表单数据为一个表单字段
		String projectProperties[]=projectEipForm.split(",");
		for(int i=0;i<projectProperties.length;i++){
			 //解析表单字段，获取表单字段名称，以及字段值
			 String formField=projectProperties[i];
			 String []formFieldList=formField.split("##");
			 String formFieldName=formFieldList[0];
			 String formFieldValue=formFieldList[1];
			 Object valueObject=ReflectionUtils.obtainFieldValue(financeMainDetail, formFieldValue);
			 if(valueObject!=null){
				 RetKeyValue retKeyValue=new RetKeyValue(formFieldName, String.valueOf(valueObject));
				 resultList.add(retKeyValue);
			 }
			
		}
		return resultList;
	}

	private List<Object> getFMAMessage(
			FinanceManagementApplyDtlVo financeManagementApplyDtlVos) {
		List<Object> resultList=new ArrayList<Object>();
		String projectEipForm="项目名称##name,预支科目##subject,申请预算##budget,申请人##applyUsername,申请部门##deptname,事由说明##description";
		
		List<AppEnum> ptypes=itcMvcService.getEnum("FIN_SUBJECT");
		financeManagementApplyDtlVos.setSubject(InitVoEnumUtil.getEnumVal(financeManagementApplyDtlVos.getSubject(),  ptypes));
	    //解析表单数据为一个表单字段
		String projectProperties[]=projectEipForm.split(",");
		for(int i=0;i<projectProperties.length;i++){
			 //解析表单字段，获取表单字段名称，以及字段值
			 String formField=projectProperties[i];
			 String []formFieldList=formField.split("##");
			 String formFieldName=formFieldList[0];
			 String formFieldValue=formFieldList[1];
			 Object valueObject=ReflectionUtils.obtainFieldValue(financeManagementApplyDtlVos, formFieldValue);
			 if(valueObject!=null){
				 RetKeyValue retKeyValue=new RetKeyValue(formFieldName, String.valueOf(valueObject));
				 resultList.add(retKeyValue);
			 }
			 
			 
		}
		return resultList;
	}


}
