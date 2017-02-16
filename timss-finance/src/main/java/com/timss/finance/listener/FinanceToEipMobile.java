package com.timss.finance.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.facade.util.EipBranchFlowProcessUtil;
import com.timss.facade.util.FlowEipUtil;
import com.timss.facade.util.InitVoEnumUtil;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.exception.FinanceBaseException;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceManagementPayService;
import com.timss.finance.util.CommonUtil;
import com.timss.finance.util.EipMobileCommon;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.timss.finance.vo.FinanceManagementPayDtlVo;
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
import com.yudean.itc.util.ReflectionUtils;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: FinanceToEipMobile.java
 * @author: 890170
 * @createDate: 2014-11-4
 * @updateUser: 890170
 * @version: 1.0
 */
@EipAnnotation("finance")
@Component
public class FinanceToEipMobile implements EipMobileInterface {
	private Logger logger = Logger.getLogger(FinanceToEipMobile.class);


	@Autowired
	private FinanceMainService financeMainService;

	@Autowired
	private FinanceMainDetailService financeMainDetailService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private EipMobileCommon emc;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private AttachmentMapper attachmentMapper;
	@Autowired
	private FinanceManagementPayService financeManagementPayService;
	
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
			AttachmentMapper attachmentMapper2) throws Exception {
		//收集主单信息
		Map<String, Object> fmMap = financeMainService.queryFinanceMainByFid(flowNo);
		List<RetAttachmentBean> retAttachmentBeans = new ArrayList<RetAttachmentBean>();
		//报销类型
		String fmType=(fmMap.get("financeMain")==null)?"":((FinanceMain)fmMap.get("financeMain")).getFinance_flow();
		if("行政报销".equals(fmType)){
			FinanceManagementPayDtlVo financeManagementPayDtlVos=financeManagementPayService.queryFMPtByMainId(flowNo);
			String attach=financeManagementPayDtlVos.getAttach();
			if(StringUtils.isNotBlank(attach)){
				retAttachmentBeans=FlowEipUtil.assembleAttachements(attach, attachmentMapper2);
			}
		}else{
			retAttachmentBeans=emc.assembleAttachements(flowNo, attachmentMapper2);
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
			    
			   // FinanceMain financeMain = financeMainService.queryFinanceMainByProcessid(processId);
			    //预先设置流程变量
				setFlowVariables(processId,taskKey,null);
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
	private void setFlowVariables(String processId, String taskKey,Object businessObj) throws Exception {
		EipBranchFlowProcessUtil eipBranchFlowProcessUtil=new EipBranchFlowProcessUtil(workflowService);
		eipBranchFlowProcessUtil.setFlowVariablesBeforeCommit(processId, taskKey,businessObj);
	}

	/** 
	 * @description: 组织表单和表格数据
	 * @author: 890170
	 * @createDate: 2014-11-11
	 */
	private List<RetContentInLineBean> assembleContent(UserInfoScope userInfo,String flowNo,String processId) throws Exception {
		List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
		RetContentInLineBean rcilbForm = new RetContentInLineBean();
		RetContentInLineBean rcilbList = new RetContentInLineBean();
		RetContentInLineBean rcilbOpi = null;
		String fid;
		String id;

		//多人报销时,flowNo为FINDTL带头,为明细ID
		if(flowNo.contains("FINDTL")) {
			id = flowNo;
			FinanceMainDetail finMainDetail = financeMainDetailService.queryFinanceMainDetailById(id);
			fid = finMainDetail.getFid();
		} else { //自己/他人报销是,flowNo为FIN带头,为报销单ID
			fid = flowNo;
		}
		
		//收集主单信息
		Map<String, Object> fmMap = financeMainService.queryFinanceMainByFid(fid);
		//收集子单信息
		//List<FinanceMainDetail> fmdList = financeMainDetailService.queryFinanceMainDetailByFid(flowNo);
		List<FinanceMainDetailCostVo> fmdCostVoList = financeMainDetailService.queryFinanceMainDetailCostListByFid(fid);
		
		//报销类型
		String fmType=(fmMap.get("financeMain")==null)?"":((FinanceMain)fmMap.get("financeMain")).getFinance_flow();
		if("行政报销".equals(fmType)){
			assembleFormAndDetailList(flowNo,rcilbForm,rcilbList);
			epbList.add(rcilbForm);
			epbList.add(rcilbList);
		}else{
			if( null != fmMap && fmMap.size() > 0 && null != fmdCostVoList && fmdCostVoList.size() > 0 ) {
				emc.configFormListData( processId, fmMap, fmdCostVoList, rcilbForm, rcilbList );
				
				epbList.add(rcilbForm);
				epbList.add(rcilbList);
			}
		}
		
		//组装opions中数据
		rcilbOpi = emc.assembleOpinions(processId);
		epbList.add(rcilbOpi);
		
		return epbList;
	}
	
	private void assembleFormAndDetailList(String flowNo,
			RetContentInLineBean rcilbForm, RetContentInLineBean rcilbList) {
		assembleFMPForm(flowNo,rcilbForm);
		assembleFMPDetaiList(flowNo,rcilbList);
	}

	private void assembleFMPDetaiList(String flowNo,
			RetContentInLineBean rcilbList) {
		rcilbList.setFoldable(true);
		rcilbList.setIsShow(true);
		rcilbList.setType(Type.Table);
		rcilbList.setName("费用明细");
		List<Object> results=new ArrayList<Object>();
		
		List<FinanceMainDetail> financeMainDetailVos=financeMainDetailService.queryFinanceMainDetailByFid(flowNo);
		for(int i=0;i<financeMainDetailVos.size();i++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			FinanceMainDetail financeMainDetail=financeMainDetailVos.get(i);
			
			map.put("title", "金额"+financeMainDetail.getAmount()+"元");
			map.put("rows", toFMPDetailListMap(financeMainDetail));
			
			results.add(map);
		}
		rcilbList.setValue(results);
		
	}

	private Object toFMPDetailListMap(FinanceMainDetail financeMainDetail) {
		List<Object> resultList=new ArrayList<Object>();
		String projectEipForm="单据张数##join_nbr,报销金额##amount,报销事由##description,备注##remark";
		
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

	private void assembleFMPForm(String flowNo, RetContentInLineBean rcilbForm) {
		rcilbForm.setFoldable(true);
		rcilbForm.setIsShow(true);
		rcilbForm.setType(Type.KeyValue);
		rcilbForm.setName("行政付款信息");
		List<Object> resultList=new ArrayList<Object>();
		
		FinanceManagementPayDtlVo financeManagementPayDtlVo=financeManagementPayService.queryFMPtByMainId(flowNo);
		financeManagementPayDtlVo.setType(getTypeValue(financeManagementPayDtlVo.getType()));
		
		List<AppEnum> ptypes=itcMvcService.getEnum("FIN_SUBJECT");
		financeManagementPayDtlVo.setSubject(InitVoEnumUtil.getEnumVal(financeManagementPayDtlVo.getSubject(),  ptypes));
		
		//收集主单信息
		Map<String, Object> fmMap = financeMainService.queryFinanceMainByFid(financeManagementPayDtlVo.getMainId());
		financeManagementPayDtlVo.setCreateuser((String) fmMap.get("userName"));
		
		String projectEipForm="类型##type,报销单名称##name,申请单##applyName,报销金额##price,批复金额##budget,预支科目##subject,填单人##createuser,收款方##payeeName";
		
	    //解析表单数据为一个表单字段
		String projectProperties[]=projectEipForm.split(",");
		for(int i=0;i<projectProperties.length;i++){
			 //解析表单字段，获取表单字段名称，以及字段值
			 String formField=projectProperties[i];
			 String []formFieldList=formField.split("##");
			 String formFieldName=formFieldList[0];
			 String formFieldValue=formFieldList[1];
			 Object valueObject=ReflectionUtils.obtainFieldValue(financeManagementPayDtlVo, formFieldValue);
			 if(valueObject!=null){
				 RetKeyValue retKeyValue=new RetKeyValue(formFieldName, String.valueOf(valueObject));
				 resultList.add(retKeyValue);
			 }
			 
		}
		rcilbForm.setValue(resultList);
		
		
	}
	
	private String  getTypeValue(String key){
		String result="";
		if("only".equals(key)){
			result="自己报销";
		}else if("other".equals(key)){
			result="他人报销";
		}else if("external".equals(key)){
			result="对外报销";
		}
		return result;
	}

	/** 
	 * @description: 获取properties文件中配置信息
	 * @author: 890170
	 * @createDate: 2014-11-11
	 */
	public String getPropertiesVal(String typeName) throws Exception{
		return CommonUtil.getProperties(typeName);
	}
}
