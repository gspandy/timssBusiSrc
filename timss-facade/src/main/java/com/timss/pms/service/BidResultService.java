package com.timss.pms.service;

import java.util.List;
import java.util.Map;

import com.timss.pms.bean.BidCon;
import com.timss.pms.bean.BidResult;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.esb.ws.sms.Init;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface BidResultService extends FlowVoidService{
	Map tmpInsertBidResult(BidResult bidResult);
	
	List<BidResultVo> queryBidResultListByProjectId(int projectId);
	
	BidResultDtlVo queryBidResultById(int id);
	
	int deleteBidResult(int id);
	
	int tmpUpdateBidResult(BidResult bidResult);
	
	int updateBidResultApproving(BidResult bidResult);
	
	int updateBidResultApproved(BidResult bidResult);
	
	int updateBidResultApproving(int bidResultId);
	
	int updateBidResultApproved(int bidResultId);
	
	/**
	 * 终止项目招标流程，并修改招标状态
	 * @Title: stopWorkflow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param project
	 * @param procInstId
	 * @return
	 */
	int stopWorkflow(BidResult bidresult,String processInstId,String reason);
	
	/**
	 * 
	 * @Title: queryBidResultList
	 * @Description: 查询招标信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<Project>  包含的分页信息的项目立项信息
	 * @throws
	 */
	Page<BidResultVo> queryBidResultList(Page<BidResultVo> page,UserInfoScope userInfo);
}
