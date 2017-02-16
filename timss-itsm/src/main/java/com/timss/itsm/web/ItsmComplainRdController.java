package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.itsm.bean.ItsmAttachment;
import com.timss.itsm.bean.ItsmComplainRd;
import com.timss.itsm.bean.ItsmHandleRd;
import com.timss.itsm.dao.ItsmComplainRdDao;
import com.timss.itsm.service.ItsmAttachmentService;
import com.timss.itsm.service.ItsmComplainRdService;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.vo.ItsmComplainRdVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppComment;
import com.yudean.itc.manager.support.ICommentManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/itsm/complainRecords")
public class ItsmComplainRdController {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ItsmComplainRdService complainRdService;
	@Autowired
	private ICommentManager iCommentManager;
	@Autowired
	private ItsmAttachmentService attachmentService;
	@Autowired
	private ItsmComplainRdDao complainRdDao;
	
	private static final Logger LOG = Logger
			.getLogger(ItsmWorkorderController.class);

	/**
	 * @return
	 */
	@RequestMapping(value = "/complainRecordsList")
	@ReturnEnumsBind("ITSM_COMPLAIN_STATUS")
	public String complainRecordsList() {
		return "/complainRecordsList.jsp";
	}
	/**新建投诉
	 * @return
	 */
	@RequestMapping(value = "/openComplainPage")
	@ReturnEnumsBind("ITSM_COMPLAIN_TYPE")
	public String newComplainType() {
		return "/operationComplain/newComplain.jsp";
	}

	/* 保存 */
	@RequestMapping(value = "/commitcomplainRd")
	@ValidFormToken
	public Map<String, String> commitcomplainRd() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String uploadIds = userInfoScope.getParam("uploadIds"); // 附件编号
		String complainRdForm = userInfoScope.getParam("complainRdForm");// 获取前台传过来的form表单数据
		ItsmComplainRd complainRd = JsonHelper.toObject(complainRdForm,ItsmComplainRd.class);
		String complainRdId = complainRd.getId();
		String compalinRdCode = complainRd.getCode();
		String workflowId = complainRd.getWorkflowId();
		String commitStyle = userInfoScope.getParam("commitStyle");// 提交方式（用于确定是点击的“提交”还是“暂存”）
		ItsmComplainRdVO complainRdVO = new ItsmComplainRdVO();
		complainRdVO.setComplainRds(complainRd);
		complainRdVO.setCommitStyle(commitStyle);
		complainRdVO.setUploadIds(uploadIds);

		String taskId = "noFlow";
		if ("".equals(workflowId) || workflowId == null) { // 确定不是因为回退的节点
			if ("".equals(complainRdId)) { // 初次提交或暂存
				if ("commit".equals(commitStyle)) { // 提交，启动流程
					ItsmComplainRdVO complainRdVO2 = complainRdService.insertComplainRd(complainRdVO);
					taskId = complainRdVO2.getTaskId();
					complainRdId = complainRdVO2.getComplainId();
					compalinRdCode = complainRdDao.queryCpRdById(complainRdId).getCode();
					workflowId = complainRdVO2.getWorkflowId();
					LOG.info("-------------初次新建提交完成-------------");
				} else if ("save".equals(commitStyle)) { // 暂存，不启动流程
					ItsmComplainRdVO complainRdVO2 = complainRdService.saveComplainRd(complainRdVO);
					complainRdId = complainRdVO2.getComplainId();
					compalinRdCode = complainRdDao.queryCpRdById(complainRdId).getCode();
					LOG.info("-------------初次暂存完成-------------");
				}
			} else { // 暂存的投诉单提交 或 再次暂存 
				if ("commit".equals(commitStyle)) {
					complainRdVO.setCommitStyle("commit");
				} else if ("save".equals(commitStyle)) {
					complainRdVO.setCommitStyle("save");
				}
				ItsmComplainRdVO complainRdVO2 =  complainRdService.updateComplainRd(complainRdVO);
				taskId = complainRdVO2.getTaskId();
				workflowId = complainRdVO2.getWorkflowId();
				complainRdId = complainRdVO2.getComplainId();
				compalinRdCode = complainRdDao.queryCpRdById(complainRdId).getCode();
				LOG.info("-------------新建后暂存的投诉单再次提交或暂存完成-----------");
			}
		} else {//启动流程回退后的
			LOG.info("-------------回退的投诉单再次提交或暂存开始-------------");
			complainRdService.rollbackCommit(complainRdVO);
			LOG.info("-------------回退的投诉单再次提交或暂存完成-------------");
			// 获取当前活动节点
			List<Task> activities = workflowService.getActiveTasks(workflowId);
			// 刚启动流程，第一个活动节点肯定是属于当前登录人的
			Task task = activities.get(0);
			taskId = task.getId();
		}
		Map<String, String> mav = new HashMap<String, String>();
		mav.put("result", "success");
		if ("save".equals(commitStyle)) {
			taskId = "noFlow";
		}
		mav.put("taskId", taskId);
		mav.put("complainRdId", complainRdId);
		mav.put("workflowId", workflowId);
		mav.put("compalinRdCode", compalinRdCode);
		return mav;
	}

	/* 投诉记录列表 */
	@RequestMapping(value = "/complainRecordsListData", method = RequestMethod.POST)
	public Page<ItsmComplainRd> complainRecordsListData() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		Page<ItsmComplainRd> page = userInfoScope.getPage();
		// 表头搜索相关的
		String fuzzySearchParams = userInfoScope.getParam("search");
		Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap(
				"complainRdMap", ItsmConstant.MODULENAME, "ItsmComplainRdDao");
		if (fuzzySearchParams != null) {
			Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper
					.jsonToHashMap(fuzzySearchParams);
			fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams,
					propertyColumnMap);
			if(fuzzyParams.containsKey("CREATEDATE")){
				fuzzyParams.put("to_char(createdate,'yyyy-mm-dd hh24:mi:ss')", fuzzyParams.get("CREATEDATE"));
			    fuzzyParams.remove("CREATEDATE");
			}
			page.setFuzzyParams(fuzzyParams);
		}
		// 设置排序内容
		if (userInfoScope.getParamMap().containsKey("sort")) {
			String sortKey = userInfoScope.getParam("sort");
			// 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
			sortKey = propertyColumnMap.get(sortKey);
			page.setSortKey(sortKey);
			page.setSortOrder(userInfoScope.getParam("order"));
		} else {
			// 设置默认的排序字段
			page.setSortKey("CREATEDATE");
			page.setSortOrder("desc");
		}
		page.setParameter("siteid", siteId);
		page = complainRdService.queryComplainRdList(page);
		return page;
	}

	/* 根据id查找投诉记录 */
	@RequestMapping(value = "/querycomplainRdById", method = RequestMethod.POST)
	public Map<String, Map<String, Object>> querycomplainRdDataById()throws Exception {
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String complainRdId = userInfoScope.getParam("complainRdId");
		ItsmComplainRdVO complainRdVO = complainRdService.queryCpRdById(complainRdId);
		ItsmComplainRd complainRd = complainRdVO.getComplainRds();
		String taskId = complainRdVO.getTaskId();
		List<ItsmAttachment> attachList = attachmentService.queryAttachmentById(complainRdId, "Cp");
		// 根据附件id，转化数据传前台，前台显示附件数据
		ArrayList<String> aList = new ArrayList<String>();
		for (int i = 0; i < attachList.size(); i++) {
			aList.add(attachList.get(i).getAttachId());
		}
		List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList(Constant.basePath, aList);
		List<AppComment> list = iCommentManager.retrieveComments(complainRdId,"N");// 查找处理记录
		if (list.size() == 0) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("processRd", null);
			resultMap.put("complainRdForm", JsonHelper.toJsonString(complainRd));
			resultMap.put("taskId", taskId);
			resultMap.put("complainRdId", complainRd.getId());
			resultMap.put("attachmentMap", attachmentMap);
			result.put("resultMap", resultMap);
		} else {
			List<ItsmHandleRd> itsmHandleRds = new ArrayList<ItsmHandleRd>();
			for (AppComment appComment2 : list) {
				ItsmHandleRd handleRd = new ItsmHandleRd();
				handleRd.setUserName(itcMvcService.getUserInfoById(appComment2.getUserId()).getUserName());
				handleRd.setCmtId(appComment2.getCmtId());
				handleRd.setFlwId(appComment2.getFlwId());
				handleRd.setCommentInfo(appComment2.getCommentInfo());
				handleRd.setCreationTime(appComment2.getCreationTime());
				itsmHandleRds.add(handleRd);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("processRd", JsonHelper.toJsonString(itsmHandleRds));
			resultMap.put("complainRdForm", JsonHelper.toJsonString(complainRd));
			resultMap.put("taskId", taskId);
			resultMap.put("complainRdId", complainRd.getId());
			resultMap.put("attachmentMap", attachmentMap);
			result.put("resultMap", resultMap);
		}
		return result;
	}

	/* 查找分析与处理记录 */
	@RequestMapping(value = "/handleInfo", method = RequestMethod.GET)
	public String HandleInfo() throws Exception {
		return "/operationComplain/HandleInfo.jsp";
	}

	@RequestMapping(value = "/queryHandleInfo", method = RequestMethod.POST)
	public Map<String, String> queryHandleInfo() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String flwId = userInfoScope.getParam("flwId");
		String cmtId = userInfoScope.getParam("cmtId");

		List<AppComment> list = iCommentManager.retrieveComments(flwId, "N");// 查找处理记录
		List<ItsmHandleRd> itsmHandleRds = new ArrayList<ItsmHandleRd>();
		for (AppComment appComment2 : list) {
			ItsmHandleRd handleRd = new ItsmHandleRd();
			if (appComment2.getCmtId().equals(cmtId)) {
				handleRd.setUserName(itcMvcService.getUserInfoById(appComment2.getUserId()).getUserName());
				handleRd.setCmtId(appComment2.getCmtId());
				handleRd.setFlwId(appComment2.getFlwId());
				handleRd.setCommentInfo(appComment2.getCommentInfo());
				handleRd.setCreationTime(appComment2.getCreationTime());
				itsmHandleRds.add(handleRd);
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("processRd", JsonHelper.toJsonString(itsmHandleRds));
		return result;
	}

	/**
	 * @description: 作废工单，仅工单发起人可以作废
	 */
	@RequestMapping(value = "/obsoleteWorkOrder", method = RequestMethod.POST)
	public Map<String, String> obsoleteWorkOrder() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String complainRdId = userInfoScope.getParam("complainRdId");// 获取前台传过来的form表单数据
		complainRdService.obsoleteWorkOrder(complainRdId);
		Map<String, String> mav = new HashMap<String, String>();
		mav.put("result", "success");
		return mav;
	}

	/**
	 * @description: 删除工单，仅草稿状态下可删除
	 */
	@RequestMapping(value = "/deleteWorkOrder", method = RequestMethod.POST)
	public Map<String, String> deleteWorkOrder() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String complainRdId = userInfoScope.getParam("complainRdId");// 获取前台传过来的form表单数据
		complainRdService.deleteWorkOrder(complainRdId);
		Map<String, String> mav = new HashMap<String, String>();
		mav.put("result", "success");
		return mav;
	}
}
