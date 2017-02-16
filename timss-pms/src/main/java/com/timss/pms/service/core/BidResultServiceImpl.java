package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.BidResult;
import com.timss.pms.bean.FlowVoidParamBean;
import com.timss.pms.bean.WorkflowBusiness;
import com.timss.pms.dao.BidResultDao;
import com.timss.pms.dao.ProjectDao;
import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.BidResultService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.FlowVoidUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class BidResultServiceImpl implements BidResultService{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	BidResultDao bidResultDao;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	HomepageService homepageService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowBusinessDao wfBusinessDao;
	@Autowired
	ProjectDao projectDao;
	Logger LOGGER=Logger.getLogger(this.getClass());
	@Override
	@Transactional
	public Map tmpInsertBidResult(BidResult bidResult) {
		
		//TODO 特殊处理包括附件
		LOGGER.info("开始暂存招标结果信息");
		InitUserAndSiteIdUtil.initCreate(bidResult, itcMvcService);
		ChangeStatusUtil.changeSToValue(bidResult, ChangeStatusUtil.draftCode);
		bidResultDao.insertBidResult(bidResult);
		AttachUtil.bindAttach(attachmentMapper, null, bidResult.getAttach());
		Map map=startWorkflow(bidResult);
		LOGGER.info("完成暂存招标结果信息");
		return map;
	}

	@Override
	public List<BidResultVo> queryBidResultListByProjectId(int projectId) {
		List<BidResultVo> list=bidResultDao.queryBidResultListByProjectId(projectId);
		InitVoEnumUtil.initBidResultVoList(list,itcMvcService);
		return list;
	}

	@Override
	public BidResultDtlVo queryBidResultById(int id) {
		BidResultDtlVo bidResultDtlVo=bidResultDao.queryBidResultById(id);
		
		ProjectDtlVo projectDtlVo=projectDao.queryProjectById(bidResultDtlVo.getProjectId());
		bidResultDtlVo.setProjectName(projectDtlVo.getProjectName());
		bidResultDtlVo.setProjectProperty(projectDtlVo.getProperty());
		
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(bidResultDtlVo.getAttach());
		bidResultDtlVo.setAttachMap(attachMap);
		return bidResultDtlVo;
	}

	@Override
	@Transactional
	public int deleteBidResult(int id) {
		LOGGER.info("删除招标结果信息，id为"+id);
		BidResultDtlVo bidResultDtlVo=bidResultDao.queryBidResultById(id);
		AttachUtil.bindAttach(attachmentMapper, bidResultDtlVo.getAttach(), null);
		bidResultDao.deleteBidResult(id);
		
		String processInstId=wfBusinessDao.queryWorkflowIdByBusinessId(WFServiceImpl.bidResultPrefix+id);
		if(processInstId!=null && !"".equals(processInstId)){
			
			//删除保存的业务与流程关联数据
			wfBusinessDao.deleteWorkflwoBusinessByWFId(processInstId);
			//删除流程，并且删除流程在首页的记录
			workflowService.delete(processInstId, "");
		}
		return 0;
	}

	@Override
	@Transactional
	public int tmpUpdateBidResult(BidResult bidResult) {
		LOGGER.info("开始修改招标结果");
		BidResultDtlVo bidResultDtlVo=bidResultDao.queryBidResultById(bidResult.getBidResultId());
		AttachUtil.bindAttach(attachmentMapper, bidResultDtlVo.getAttach(), bidResult.getAttach());
		InitUserAndSiteIdUtil.initUpdate(bidResult, itcMvcService);
		ChangeStatusUtil.changeSToValue(bidResult,ChangeStatusUtil.draftCode);
		bidResultDao.updateBidResult(bidResult);
		LOGGER.info("完成修改招标结果");
		
		return 0;
	}

	
	private Map startWorkflow(BidResult bidResult ){
		UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
		//TODO 动态生成

		String processKey="pms_"+infoScope.getSiteId().toLowerCase()+"_bidresult";
		String prefix=WFServiceImpl.bidResultPrefix;
		
		
		String defkey = workflowService.queryForLatestProcessDefKey(processKey);
	
		HashMap map=new HashMap();
		//map.put("isRs", project.getIsRs());
		//存入业务id，移动eip端要使用
		map.put("businessId", bidResult.getBidResultId());
		map.put("needFZSH", "N");
		ProcessInstance processInstance=null;
		
		try{
			processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey,infoScope.getUserId(),map);
		}catch(Exception e){
			throw new PmsBasicException(processKey+"流程启动失败", e);
		}
		String processInstId = processInstance.getProcessInstanceId();
		//保存流程实例id与businessid的关联
		WorkflowBusiness wBusiness=new WorkflowBusiness();
		wBusiness.setBusinessId(prefix+String.valueOf(bidResult.getBidResultId()));
		wBusiness.setInstanceId(processInstId);
		wfBusinessDao.insertWorkflowBusiness(wBusiness);
		
		//绑定合同到待办页面
		String url="pms/bid/editBidResultJsp.do?id="+bidResult.getBidResultId()+"&processInstId="
				+processInstId;
		homepageService.createProcess(wBusiness.getId(), processInstId, "招标结果审批", 
				"招标-" + bidResult.getName(), "草稿", url, infoScope, null);
		
		
		//获取当前活动节点，根据当前活动节点获取下一个任务节点、以及执行人，并返回给前台
		List<Task> activities = workflowService.getActiveTasks(processInstId);
		//刚启动流程，第一个活动节点肯定是属于当前登录人的
		Task task = activities.get(0);

		map = new HashMap();
		map.put("taskId", task.getId());
		map.put("bidResultId",bidResult.getBidResultId());
		
		return map;
	}

	@Override
	@Transactional
	public int updateBidResultApproving(BidResult bidResult) {
		LOGGER.info("开始修改招标为审批中");
		BidResultDtlVo bidResultDtlVo=bidResultDao.queryBidResultById(bidResult.getBidResultId());
		AttachUtil.bindAttach(attachmentMapper, bidResultDtlVo.getAttach(), bidResult.getAttach());
		InitUserAndSiteIdUtil.initUpdate(bidResult, itcMvcService);
		ChangeStatusUtil.changeSToValue(bidResult,ChangeStatusUtil.approvingCode);
		bidResultDao.updateBidResult(bidResult);
		LOGGER.info("完成修改招标为审批中");
		return 0;

	}

	@Override
	@Transactional
	public int updateBidResultApproved(BidResult bidResult) {
		LOGGER.info("开始修改招标为审批完成");
		BidResultDtlVo bidResultDtlVo=bidResultDao.queryBidResultById(bidResult.getBidResultId());
		AttachUtil.bindAttach(attachmentMapper, bidResultDtlVo.getAttach(), bidResult.getAttach());
		InitUserAndSiteIdUtil.initUpdate(bidResult, itcMvcService);
		ChangeStatusUtil.changeSToValue(bidResult,ChangeStatusUtil.approvalCode);
		bidResultDao.updateBidResult(bidResult);
		LOGGER.info("完成修改招标为审批完成");
		return 0;

	}

	@Override
	@Deprecated
	public int stopWorkflow(BidResult bidresult, String processInstId,
			String reason) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<BidResultVo> queryBidResultList(Page<BidResultVo> page,
			UserInfoScope userInfo) {
		LOGGER.info("开始查询招标结果数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		List<BidResultVo> bidResults = bidResultDao.queryBidResultList(page);
		InitVoEnumUtil.initBidResultVoList(bidResults,itcMvcService);
		page.setResults(bidResults);
		LOGGER.info("查询招标结果数据成功");
		
		return page;
	}

	@Override
	@Transactional
	public int updateBidResultApproving(int bidResultId) {
		BidResultDtlVo bidResultDtlVo=queryBidResultById(bidResultId);
		
		updateBidResultApproving(bidResultDtlVo);
		return 0;
	}

	@Override
	@Transactional
	public int updateBidResultApproved(int bidResultId) {
		BidResultDtlVo bidResultDtlVo=queryBidResultById(bidResultId);
		updateBidResultApproved(bidResultDtlVo);
		return 0;
	}

	@Override
	@Transactional
	public boolean voidFlow(FlowVoidParamBean params) {
		FlowVoidUtil flowVoidUtil=new FlowVoidUtil(workflowService, homepageService);
		flowVoidUtil.initFlowVoid(params);
		String processInstId=flowVoidUtil.getProcessInstId();
		//更新招标结果状态为作废
		updateBidResultToVoided(params.getBusinessId(), processInstId);
		return true;
	}

	private boolean updateBidResultToVoided(String businessId, String processInstId) {
		LOGGER.info("更新招标为作废状态，id为"+businessId);
		BidResultDtlVo bidResultDtlVo=queryBidResultById(Integer.valueOf(businessId));
		ChangeStatusUtil.changeToVoidedStatus(bidResultDtlVo);
		bidResultDao.updateBidResult(bidResultDtlVo);
		
		return true;
		
	}
}
