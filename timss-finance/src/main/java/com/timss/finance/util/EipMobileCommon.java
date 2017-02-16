package com.timss.finance.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.facade.util.EipBranchFlowProcessUtil;
import com.timss.finance.bean.FinanceAttachMatch;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinanceAttachMatchDao;
import com.timss.finance.exception.FinanceBaseException;
import com.timss.finance.listener.FinanceToEipMobile;
import com.timss.finance.service.FinanceAttachMatchService;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.vo.FinListEip;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.timss.inventory.vo.InvEipOpinionsEip;
import com.yudean.itc.code.UsualOpinionsType;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetFlowsBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetTask;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.Attachment;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.configs.MvcWebConfig;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: EipMobileCommon.java
 * @author: 890170
 * @createDate: 2014-9-24
 * @updateUser: 890170
 * @version: 1.0
 */
@Component
public class EipMobileCommon {
	private Logger logger = Logger.getLogger(EipMobileCommon.class);

	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private FinanceMainDetailService financeMainDetailService;
	@Autowired
	FinanceAttachMatchService financeAttachMatchService;
	@Autowired
	FinanceAttachMatchDao financeAttachMatchDao;
	@Autowired
	HistoryInfoService historyInfoService;
	@Autowired
	IAuthorizationManager im;
	
	/**
	 * @description:组织opinions中的数据
	 * @author: 890170
	 * @createDate: 2014-11-5
	 * @param userInfo
	 * @param sheetId
	 * @return:
	 */
	public RetContentInLineBean assembleOpinions(String processId) throws Exception {
//		HistoricTask hiTask;
//		FinOpinionsEip foe;
		
		RetContentInLineBean rcilb = new RetContentInLineBean(); 
		rcilb.setFoldable(true);
		rcilb.setIsShow(true);
		rcilb.setName("审批意见");
		rcilb.setType(Type.Approval);
		
		List<Object> rkvList = new ArrayList<Object>();
		if (null != processId && !"".equals(processId)) {
//			//获取流程历史信息
//			List<HistoricTask> hiTaskList = workflowService.getFakePreviousTasks(processId);
//			//for(HistoricTask hiTask : hiTaskList){
//			if( hiTaskList != null ) {
//				for( int i=hiTaskList.size()-1; i>=0; i-- ) {
//					foe = new FinOpinionsEip();
//					hiTask = hiTaskList.get(i);
//					//历史信息提供的是员工编码，要转换成中文
//					UserInfo ui = itcMvcService.getUserInfoById(hiTask.getAssignee());
//					foe.setWho(ui.getUserName());
//					//时间格式转换
//					foe.setWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(hiTask.getEndTime()));
//					//审批信息
//					String comment = workflowService.getComment(hiTask.getId());
//					foe.setWhat(comment);
//					rkvList.add(foe);
//				}
//			}
//			rcilb.setValue(rkvList);
			List<Map<String, Object>> list = historyInfoService.getHistoryComment(processId);
			for(Map<String, Object> map :list){
				InvEipOpinionsEip eob = new InvEipOpinionsEip();
				//历史信息提供的是员工编码，要转换成中文
				eob.setWho(String.valueOf(map.get("ASSIGNEENAME")));
				//时间格式转换
				eob.setWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)map.get("END")));
				//审批信息
				eob.setWhat(String.valueOf(map.get("COMMENT_")));
				rkvList.add(eob);
			}
			rcilb.setValue(rkvList);
		}
		return rcilb;
	}
	
	/**
	 * @description:组织usualOpinions中的数据
	 * @author: 890170
	 * @createDate: 2014-9-23
	 * @param userInfo
	 * @param sheetId
	 * @return
	 * @throws Exception:
	 */
	public List<UsualOpinionsType> assembleUsualOpinions() throws Exception {
		List<UsualOpinionsType> eobList = new ArrayList<UsualOpinionsType>();
		eobList.add(UsualOpinionsType.yes);//同意
		eobList.add(UsualOpinionsType.no);//不同意
		return eobList;
	}
	
	/**
	 * @description:组织attachement中的数据
	 * @author: 890170
	 * @createDate: 2014-11-5
	 * @return:
	 */
	public List<RetAttachmentBean> assembleAttachements(String flowNo, AttachmentMapper attachmentMapper) throws Exception {
		List<RetAttachmentBean> eabList = new ArrayList<RetAttachmentBean>();
		
		String id;
		String fid;
		String attachId;
		Attachment attachment;
		FinanceAttachMatch fam;
		
		//多人报销时,flowNo为FINDTL带头,为明细ID
		if(flowNo.contains("FINDTL")) {
			id = flowNo;
			FinanceMainDetail finMainDetail = financeMainDetailService.queryFinanceMainDetailById(id);
			fid = finMainDetail.getFid();
		} else { //自己/他人报销是,flowNo为FIN带头,为报销单ID
			fid = flowNo;
		}
		
		//查询附件信息
		List<FinanceAttachMatch> famList= financeAttachMatchDao.queryFinanceAttachMatchByFid(fid);
		
		for( int i=0; i<famList.size(); i++ ) {
			fam = (FinanceAttachMatch)(famList.get(i));
			attachId = fam.getAttachid();
			attachment = attachmentMapper.selectById(attachId);
			RetAttachmentBean retAttachmentBean = convertToRetAttachmentBean(attachment);
			eabList.add(retAttachmentBean);
		}
		
		return eabList;
	}
	
	//将附件转为eip需要的格式
	private static RetAttachmentBean convertToRetAttachmentBean(Attachment attachment) {
		RetAttachmentBean retAttachmentBean = new RetAttachmentBean();
		retAttachmentBean.setFileName(attachment.getOriginalFileName());
		retAttachmentBean.setFileSize(getRealFileSizeFromAttachment(attachment.getFilesize()));
		retAttachmentBean.setFileSufx(getRealFileTypeFromAttachment(attachment.getFileType()));
		
		//附件编码信息获取 同时处理url的域名信息
		String url = MvcWebConfig.serverBasePath+"upload?method=downloadFile&id="+attachment.getId();
		retAttachmentBean.setFileUrl(url);
		return retAttachmentBean;
	}
	
	/** 
	 * @description: 将字节转换为用户可识别的信息
	 * @author: 890170
	 * @createDate: 2015-1-15
	 */
	private static String getRealFileSizeFromAttachment(Integer size){
		String result="";
		int KUnit=1024;
		int MUnit=KUnit*1024;
		if(size<KUnit){
			result="不到1K";
		}else if(size<MUnit){
			result=size/KUnit+"K";
		}else{
			result=size/MUnit+"M";
		}
		return result;
	}
	
	/** 
	 * @description: 处理fileType,将类似“。txt”转换为“txt”
	 * @author: 890170
	 * @createDate: 2015-1-15
	 */
	private static String getRealFileTypeFromAttachment(String fileType){
		if(StringUtils.isNotBlank(fileType)){
			
			if(fileType.indexOf(".")==0){
				fileType=fileType.substring(1);
			}
		}else{
			fileType="";
		}
		return fileType;
	}
	
	/**
	 * @description:组织flows中的数据
	 * @author: 890170
	 * @createDate: 2014-9-23
	 * @param userInfo
	 * @param sheetId
	 * @return:
	 * @throws Exception 
	 */
	public List<RetFlowsBean> assembleFlows(UserInfoScope userInfo,String processId) throws Exception {
		Task task = null;
		List<RetFlowsBean> efbList = new ArrayList<RetFlowsBean>();
		
		List<Task> activities = workflowService.getActiveTasks(processId);
		if (null != activities && activities.size() > 0) {
			task = activities.get(0);
			
			//获取下一环节信息
			efbList.add(showNextLink(new RetFlowsBean(),processId,task));
			//获取上一环节信息,如果没有找到信息则不返回数据
			RetFlowsBean retFlowsBean=showPreviousLink(new RetFlowsBean(),processId);
			if(retFlowsBean!=null){
				efbList.add(retFlowsBean);
			}
			
		}
		return efbList;
	}
	
	/**
	 * @description:获取下一环节信息
	 * @author: 890170
	 * @createDate: 2014-9-25
	 * @param processId
	 * @param task
	 * @return:
	 * @throws Exception 
	 */
	private RetFlowsBean showNextLink(RetFlowsBean rfb,String processId,Task task) throws Exception{
		rfb.setId("next");
		rfb.setName("下一环节");
		List<RetTask> rtList = new ArrayList<RetTask>();
		//获取可能的下一节点信息，可能存在分支节点
		EipBranchFlowProcessUtil eipBranchFlowProcessUtil=new EipBranchFlowProcessUtil(workflowService);
		rtList=eipBranchFlowProcessUtil.getNextElementsByFlowProperty(processId, task);
		rfb.setTask(rtList);
		return rfb;
	}
    
	
	/**
	 * @description:展示上一环节信息
	 * @author: 890170
	 * @createDate: 2014-9-25
	 * @param processId
	 * @param task
	 * @return:
	 */
	private RetFlowsBean showPreviousLink(RetFlowsBean rfb,String processId){
		rfb.setId("rollback");
		rfb.setName("退回");
		List<RetTask> rtList = new ArrayList<RetTask>();
		
		List<HistoricTask> htList = workflowService.getFakePreviousTasks(processId);
		//处理有数据的情况，没有数据时则返回null
		if(null!=htList&&htList.size()>0){
			
			HistoricTask ht = htList.get(0);
			RetTask rt = new RetTask();
			rt.setTask(new RetKeyValue(ht.getTaskDefinitionKey(),ht.getName()));
			
			List<RetKeyValue> rkvList = new ArrayList<RetKeyValue>();
			UserInfo ui = itcMvcService.getUserInfoById(ht.getAssignee());
			rkvList.add(new RetKeyValue(ht.getAssignee(),ui.getUserName()));
			rt.setUser(rkvList);
			rtList.add(rt);
		
		    rfb.setTask(rtList);
		    return rfb;
		}
		return null;
	}
	
	
	/**
	 * @description:提交到下一环节
	 * @author: 890170
	 * @createDate: 2014-11-7
	 * @return:
	 */
	public boolean commitToNextLink(UserInfoScope userInfo,String opinion,List<String> nuser,String processId) throws Exception{
		Task task = null;
		String nextDefKey = null;
		String owner = null;
		String curUser = userInfo.getUserId();
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		
		boolean flag = false;
		if(null!=processId && !"".equals(processId)){
			//获取当前任务节点
			List<Task> activities = workflowService.getActiveTasks(processId);
			if (null != activities && activities.size() > 0) {
				task = activities.get(0);
				
				//获取下一节点定义id
				List<String> nkeyList = workflowService.getNextTaskDefKeys(processId, task.getTaskDefinitionKey());
				if(null!=nkeyList && nkeyList.size()>0) {
					nextDefKey = nkeyList.get(0);
					map.put(nextDefKey,nuser);
				}
				
				//隐式提交到下一环节
				flag = workflowService.complete(
										task.getId(), 
										curUser, 
										owner, 
										map, 
										opinion, 
										false);
			}
		}
		return flag;
	}
	
	/**
	 * @description:退回到首环节
	 * @author: 890170
	 * @createDate: 2014-9-23
	 * @return:
	 */
	public boolean returnToPreviousLink(UserInfoScope userInfo,String opinion,String processId){
		boolean flag = false;
		//获取历史环节集合
		List<HistoricTask> htList = workflowService.getPreviousTasks(processId);
		try{
			if (null != htList && htList.size() > 0) {
				//获取首环节
				HistoricTask ht= htList.get(0);
				String destTaskKey = ht.getTaskDefinitionKey();
				String userId = ht.getAssignee(); 
				String assignee = userInfo.getUserId();
				//使用回滚操作，将流程退回到首环节
				workflowService.rollback(processId, destTaskKey, opinion, assignee, assignee, userId);
				flag = true;
			}
		}catch(Exception e){
			throw new FinanceBaseException("流程回退失败,流程id为"+processId, e);
		}
		return flag;
	}
	
	/**
	 * @description:终止流程
	 * @author: 890170
	 * @createDate: 2014-9-25
	 * @param userInfo
	 * @param opinion
	 * @param processId
	 * @return:
	 */
	public boolean toStopCurProcess(UserInfoScope userInfo, String opinion,String processId) {
		boolean flag = false;
		Task task = null;
		String curUser = userInfo.getUserId();
		try {
			List<Task> activities = workflowService.getActiveTasks(processId);
			if (null != activities && activities.size() > 0) {
				task = activities.get(0);
				workflowService.stopProcess(task.getId(), curUser, curUser,opinion);
				flag = true;
			}
		} catch (Exception e) {
			throw new FinanceBaseException("流程终止失败,流程id为"+processId, e);
		}
		return flag;
	}

	/** 
	 * @description: 组装表单和列表数据
	 * @author: 890170
	 * @createDate: 2014-11-11
	 */
	public void configFormListData( String processId, Map<String, Object> fmMap,
			List<FinanceMainDetailCostVo> fmdList, RetContentInLineBean rcilbForm,
			RetContentInLineBean rcilbList) throws Exception {
		FinanceToEipMobile ftem = new FinanceToEipMobile(); 
		FinanceMainDetailCostVo fmdFirstDtl = fmdList.get(0);
		//RetContentInLineBean pafem = new RetContentInLineBean();
		
		FinanceMain fm = (FinanceMain) fmMap.get("financeMain");
		
		Map<String, String> finNameMap = FinanceUtil.genFinNameByFinFlow(fm.getFinance_flow());
		Map<String, String> finTypeMap = FinanceUtil.genFinTypeByFinType(fm.getFinance_type());
		
		String finNameEn = finNameMap.get("finNameEn");
		String finTypeEn = finTypeMap.get("finTypeEn");
		
		String propFormTitleName = ftem.getPropertiesVal("eip2FinFormName");
		
		String propFormName = "";
		if( finTypeEn.equals("only") ) {
			propFormName = "eip2FinForm_commononly";
		} else if( finTypeEn.equals("other") ) {
			propFormName = "eip2FinForm_commonother";
		} else if( finTypeEn.equals("more") ) {
			propFormName = "eip2FinForm_" + finNameEn + finTypeEn;
		}
		String propFormNameStr = ftem.getPropertiesVal(propFormName);
		
		String propListTitleName = ftem.getPropertiesVal("eip2FinListName");
		
		String propListName = "";
		if( ( finNameEn.equals("businessentertainment") || finNameEn.equals("carcost") 
				|| finNameEn.equals("officecost") || finNameEn.equals("welfarism") 
				|| finNameEn.equals("meetingcost") ) 
				&& (finTypeEn.equals("only") || finTypeEn.equals("other")) ) {
			propListName = "eip2FinList_commonone";
		} else if( ( finNameEn.equals("businessentertainment") || finNameEn.equals("carcost") 
				|| finNameEn.equals("officecost") || finNameEn.equals("welfarism") 
				|| finNameEn.equals("meetingcost") ) 
				&& finTypeEn.equals("more") ) {
			propListName = "eip2FinList_commonmore";
		} else if( finNameEn.equals("travelcost") && (finTypeEn.equals("only") || finTypeEn.equals("other")) ) {
			propListName = "eip2FinList_travelcostone";
		} else if( finNameEn.equals("travelcost") && finTypeEn.equals("more") ) {
			propListName = "eip2FinList_travelcostmore";
		} else if( finNameEn.equals("traincost") && (finTypeEn.equals("only") || finTypeEn.equals("other")) ) {
			propListName = "eip2FinList_traincostone";
		} else if( finNameEn.equals("traincost") && finTypeEn.equals("more") ) {
			propListName = "eip2FinList_traincostmore";
		}
		String propListNameStr = ftem.getPropertiesVal(propListName);
		
		//业务招待费自己/他人报销,设置账户类型为"公司",走公司层面
		if( finNameEn.equals("businessentertainment") 
				&& (finTypeEn.equals("only") || finTypeEn.equals("other")) ) {
			workflowService.setVariable( processId, "accType", "company" );
		}
		
		//多人报销,设置普通用户/领导分支
		if( finTypeEn.equals("more") ) {
			
			// 获取全局参数
			UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
			String siteid = userInfoScope.getSiteId();
			List<SecureUser> suList = im.retriveUsersWithSpecificGroup(
			        siteid+"_INV_LEADER", null, true, true);// groupid,组织机构,是否包含下属机构，是否返回活动用户

			// 判断"领导分支"
			boolean checkIsLeader = false;
			for (SecureUser su : suList) {
				if (su.getId().contains(userInfoScope.getUserId())
						&& su.getName().contains(userInfoScope.getUserName())) {
					checkIsLeader = true;
				}
			}

			logger.info("checkIsLeader: " + checkIsLeader);
			
			//设置流程节点中的分支判断
			workflowService.setVariable( processId, "isLeader", checkIsLeader );
			
		}
		
		rcilbForm.setFoldable(true);//是否可以折叠标志-否
		rcilbForm.setIsShow(true);	//是否显示标志
		rcilbForm.setName(propFormTitleName);//名称
		rcilbForm.setType(Type.KeyValue);//类别，这里固定keyValue
		
		//1.组装表单内容
		//获取表单域元素缩写
		String[] shortFormFieldArr = propFormNameStr.split("\\|");
		String[] fieldContent;
		String fieldType;
		String fieldKey;
		String fieldVal = "";
		String actFormField; //实际表单域元素
		
		//通过properties文件中定义的字段反射获取价值
		List<Object> rkvForm = new ArrayList<Object>();
		for(String shortFormField: shortFormFieldArr) {
			actFormField = ftem.getPropertiesVal(shortFormField);
			fieldContent = actFormField.split("#");
			fieldType = fieldContent[0];
			fieldKey = fieldContent[1];
			
			if( fieldType.equals("M") ) {
				fieldVal = ReflectionUtil.getFieldValueByFieldName(fm, fieldContent[2]);
			} else if( fieldType.equals("D") ) {
				fieldVal = ReflectionUtil.getFieldValueByFieldName(fmdFirstDtl, fieldContent[2]);
			}
			
			//记账类型
//			if("accType".equals(fieldContent[0])){
//				workflowService.setVariable(processId, "applyType", fieldVal);
//			}
			
			rkvForm.add(new RetKeyValue(fieldKey, fieldVal));
		}
		
		rcilbForm.setValue(rkvForm);
		
		//2.组装列表内容
		//String[] listFieldArr = ftem.getPropertiesVal("eip2FinList_travelcostonly").split("\\|");
		String[] listFieldArr = propListNameStr.split("\\|");

		rcilbList.setFoldable(true);//是否可以折叠标志
		rcilbList.setIsShow(false);	//是否显示标志
		//palem.setName(getPropertiesVal("eip2invMatApplyListGroupName"));//名称
		rcilbList.setName(propListTitleName);//名称
		rcilbList.setType(Type.Table);//类别，这里固定table
		
		int dtlTitleFlag = 0; //明细标题标志.0-不是明细标题,1-是明细标题
		String actListField; //实际列表元素
		String shortListField; //列表元素缩写
		
		//获取list数据
		List<Object> rkvList = new ArrayList<Object>();
		for (FinanceMainDetailCostVo fmd : fmdList) {
			FinListEip fle = new FinListEip();
			//预设一个行的集合
			List<RetKeyValue> rows = new ArrayList<RetKeyValue>();
			for(String shortListFieldWithTitle : listFieldArr) {
				dtlTitleFlag = 0;
				
				if( shortListFieldWithTitle.contains("@") ) { //设置明细的标题
					shortListField = shortListFieldWithTitle.split("@")[1];
					dtlTitleFlag = 1;
				} else {
					shortListField = shortListFieldWithTitle;
				}
				
				actListField = ftem.getPropertiesVal(shortListField);
				
				fieldContent = actListField.split("#");
				fieldType = fieldContent[0];
				//dtlTitleFlag = fieldContent[1];
				fieldKey = fieldContent[1];
				fieldVal = ReflectionUtil.getFieldValueByFieldName(fmd, fieldContent[2]);
				
				if( dtlTitleFlag == 1 ) {
					fle.setTitle(fieldVal);
				}
				//如果是itemname的话就做特殊处理
//				if("description".equals(fieldContent[0])){
//					fle.setTitle(fieldVal);
//				}
//						if("totalprice".equals(fieldContent[0])) {
//							totalName = fieldKey;
//							totalPrice += Double.valueOf(fieldVal);
//							totalPrice = Double.valueOf(df.format(totalPrice));
//						else {
				rows.add(new RetKeyValue(fieldKey, fieldVal));
//						}
			}
			fle.setRows(rows);
			rkvList.add(fle);
		}
		
		rcilbList.setValue(rkvList);
		
		return;
	}
}
