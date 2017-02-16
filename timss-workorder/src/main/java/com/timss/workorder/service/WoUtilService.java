package com.timss.workorder.service;

import java.util.List;
import java.util.Map;

import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.WoFaultType;
import com.timss.workorder.bean.WorkOrder;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;


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
public interface WoUtilService {

	
	/**
	 * @description: 修改工单状态
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param woId
	 * @param woStatus:
	 */
	void updateWoStatus(String woId,String woStatus);
	
	
	
	/**
	 * @description:修改当前执行人信息
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param woId
	 * @param userInfoScope
	 * @param flag: 正常流程时为“normal”，回退时为“rollback”
	 */
	void updateWoCurrHandlerUser(String woId,UserInfoScope userInfoScope,String flag);
	
	/**
	 * @description: 清空当前执行人信息
	 * @author: 王中华
	 * @createDate: 2014-9-17
	 * @param userInfoScope:
	 */
	void clearWoCurrHandlerUser(String woId);



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
	void cycMtpStartFlow(MaintainPlan tempMTP, UserInfo userInfo, String string) throws Exception ;

	/**
	 * @description:获取操作人所在的维护班组（ITC）
	 * @author: 王中华
	 * @createDate: 2014-12-31
	 * @param userInfoScope
	 * @param workOrder
	 * @return:
	 */
	public String getOperUserTeam(UserInfoScope userInfoScope,WorkOrder workOrder);
	
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
	public WoFaultType getOneLevelFTById(int id ,int rootId) ;
	
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
	
	/**
	 * @description: 删除附件
	 * @author: 王中华
	 * @createDate: 2015-1-19
	 * @param businessId 业务Id
	 * @param attachId:  附件ID 
	 */
	public void deleteAttachMatch(String businessId,String attachId,String type);



        /**
         * @description:修改工单申请状态
         * @author: 王中华
         * @createDate: 2015-12-21 
         * @param woapplyId
         * @param fillApply:
         */
        void updateWoaplyStatus(String woapplyId, String fillApply);
        
        /**
         * @description:获取当前处理人信息 
         * @author: 王中华
         * @createDate: 2015-12-21
         * @param woapplyId
         * @param userInfoScope
         * @return:
         * @throws Exception 
         */
        Map<String, String> getWoapplyCurrHanderInfo(UserInfoScope userInfoScope) throws Exception;



        /**
         * @description:开工申请回退，更新当前处理人
         * @author: 王中华
         * @createDate: 2015-12-22
         * @param userInfoScope
         * @param woapplyId:
         */
        void rollbackUpdateWoapplyCurrHander(UserInfoScope userInfoScope, String woapplyId);
}
