package com.timss.itsm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.timss.itsm.bean.ItsmCustomerLoc;
import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;


/**
 * @title: {title}
 * @description: {desc} 通用service
 * @company: gdyd
 * @className: WoUtilService.java
 * @author: 王中华
 * @createDate: 2014-9-17
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface ItsmWoUtilService {

	
	/**
	 * @description: 修改工单状态
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param woId
	 * @param woStatus:
	 */
	void updateBusinessStatus(ItsmBusinessPubService itsmBusinessPubService,String woId,String woStatus);
	
	/**
	 * @description:修改当前执行人信息
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param woId
	 * @param userInfoScope
	 * @param flag: 正常流程时为“normal”，回退时为“rollback”
	 */
	void updateBusinessCurrHandlerUser(ItsmBusinessPubService itsmBusinessPubService,String woId,UserInfoScope userInfoScope,String flag);
	
	void updateKlCurrHandlerUser(String klId,UserInfoScope userInfoScope,String flag);
	
	/**
	 * @description: 清空当前执行人信息
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param userInfoScope:
	 */
	void clearBusinessCurrHandlerUser(ItsmBusinessPubService itsmBusinessPubService,String bussinessId);



	/**
	 * @description:查询工作负责人（若是用户组，则返回负责人列表）
	 * @author: 王中华
	 * @createDate: 2014-11-14
	 * @param principal
	 * @param workTeams
	 * @return:
	 */
	List<String> selectPrincipalList(String principal, String workTeams);



	/**
	 * @description:维护计划生成工单
	 * @author: 王中华
	 * @createDate: 2014-11-14
	 * @param tempMTP： 维护计划对象
	 * @param userInfo：执行人（由谁生成的工单）
	 * @param string:给谁生成待办
	 */
	void cycMtpStartFlow(ItsmMaintainPlan tempMTP, UserInfo userInfo, String string) throws Exception ;

	/**
	 * @description:获取操作人所在的维护班组（ITC）
	 * @author: 王中华
	 * @createDate: 2014-12-31
	 * @param userInfoScope
	 * @param workOrder
	 * @return:
	 */
	public String getOperUserTeam(UserInfoScope userInfoScope,ItsmWorkOrder workOrder);
	
	/**
	 * @description:获取某个SecureUser 所属于的维护班组（ITC）
	 * @author: 王中华
	 * @createDate: 2014-12-31
	 * @param engineerSecureUser
	 * @param statisticUserGroupList
	 * @return:
	 */
	public String getMaintainTeamId(SecureUser engineerSecureUser,
			List<SecureUserGroup> statisticUserGroupList);
	
	
	/**
	 * @description:查询一级目录
	 * @author: 王中华
	 * @createDate: 2015-1-19
	 * @param id
	 * @param rootId
	 * @return:
	 */
	public ItsmWoFaultType getOneLevelFTById(int id ,int rootId) ;
	
	/**
	 * @description: 插入附件
	 * @author: 王中华
	 * @createDate: 2015-1-19
	 * @param businessId  业务单号ID
	 * @param fileIds  附件IDs
	 * @param type  插入类型（工单、维护计划……）
	 * @param loadPhase  上传环节（新建、策划、汇报……）
	 * @throws Exception:
	 */
	public  void insertAttachMatch(String businessId,String fileIds,String type,String loadPhase)throws Exception;
	public  void insertAttachment(String businessId,String fileIds,String type,String loadPhase)throws Exception;
	/**
	 * @description: 删除附件
	 * @author: 王中华
	 * @createDate: 2015-1-19
	 * @param businessId 业务Id
	 * @param attachId:  附件ID
	 */
	public void deleteAttachMatch(String businessId,String attachId,String type);
	public void deleteAttachment(String businessId,String attachId,String type);
	/**
	 * @description:修改客户的位置信息
	 * @author: 王中华
	 * @createDate: 2015-3-31
	 * @param userId
	 * @param location:
	 */
	public void updateCustomerLocInfo(String userId,String location);
	/**
	 * @description:查询客户的位置信息
	 * @author: 王中华
	 * @createDate: 2015-3-31
	 * @param userId
	 * @return:
	 */
	public ItsmCustomerLoc queryCustomerLocInfo(String userId);


	/**
	 * @description:获取审批过程中下一个环节的siteid集合
	 * @author: 王中华
	 * @createDate: 2015-4-9
	 * @param itcMvcService
	 * @return:
	 */
	public Set<String> getNextStepSiteIdSet(ItcMvcService itcMvcService);


	public void updateWoAndAttach(String woId,UserInfoScope userInfoScope,String woStatus) throws Exception;
	
	/**@description 加入代办 
	 * 
	 */
	public HomepageWorkTask joinHomepageWorkTask(String flowCode,String flowName,String processInstId,String statusName,
			String typeName,String url);
    /**
     * @description:修改子单的处理工程师
     * @author: 王中华
     * @createDate: 2016-10-11
     * @param itsmWorkOrderSubService
     * @param subWoId
     * @param userInfoScope:
     */
    Map<String, String> queryAuditSelectUser(UserInfoScope userInfoScope) throws Exception;
}
