package com.timss.ptw.service.zjw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.dao.PtwInfoDao;
import com.timss.ptw.service.PtwFireInfoService;
import com.timss.ptw.service.PtwInfoNoService;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwInfoServiceDef;
import com.timss.ptw.service.PtwKeyBoxService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.PtwSafeService;
import com.timss.ptw.util.PtwAuditPrivUtil;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.exception.homepage.homePageServiceException;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 工作票信息的Service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoServiceCore.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwInfoServiceImpl implements PtwInfoService {
	private static final Logger log = Logger.getLogger(PtwInfoService.class);
	
	@Autowired
        private PtwInfoServiceDef ptwInfoServiceDef;

	@Autowired
	private PtwInfoDao ptwInfoDao;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	private PtwSafeService ptwSafeService;
	@Autowired
	private PtwFireInfoService ptwFireInfoService;
	@Autowired
	private PtwInfoNoService ptwInfoNoService;
	@Autowired
	private PtwKeyBoxService ptwKeyBoxService;
	@Autowired
        private ITaskFacade iTaskFacade;
	@Autowired
        private PtwPtoSelectUserService ptwPtoSelectUserService;
	@Autowired 
        private IEnumerationManager iEnumerationManager;
	

	@Override
	public Page<PtwInfoVoList> queryPtwInfoVoList(Page<PtwInfoVoList> page) throws Exception {
		return ptwInfoServiceDef.queryPtwInfoVoList(page);
	}
	
	@Override
	public Page<PtwInfoVoList> queryHisPtwList(Page<PtwInfoVoList> page) throws Exception {
		return ptwInfoServiceDef.queryHisPtwList(page);
	}

	@Override
	public PtwInfo queryPtwInfoById(int id) {
		return ptwInfoServiceDef.queryPtwInfoById(id);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertPtwInfo(PtwInfo ptwInfo,String ptwTypeCode,String safeItems,PtwFireInfo ptwFireInfo) {
    	log.info("传入的工作票信息:"+ ptwInfo.toString());
    	log.info("工作票编号前缀:"+ ptwTypeCode);
    	log.info("隔离措施包含:"+ safeItems);
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        Date now = new Date();
        
        String relateKeyBoxId = ptwInfo.getRelateKeyBoxId();
        int isOutSourcing = ptwInfo.getIsOutSourcing();
        String statusName = "待签发";
        
        //用户信息，更新信息等的更新
        ptwInfo.setDeptId( userInfoScope.getOrgId() );
        ptwInfo.setSiteId( userInfoScope.getSiteId() );
        ptwInfo.setCreateUserName( userInfoScope.getUserName() );
        ptwInfo.setCreateUser( userInfoScope.getUserId() );
        ptwInfo.setCreateDate( now );
        ptwInfo.setModifyUserName( userInfoScope.getUserName() );
        ptwInfo.setModifyUser( userInfoScope.getUserId() );
        ptwInfo.setModifyDate( now );
        if(isOutSourcing == 1){
        	//委外单位签发
        	ptwInfo.setWtStatus( ptwInfo.getIsStdWt() == 1 ? 1000 : 310 );
        	statusName = "外委签发";
        }else{
        	//业主签发
        	ptwInfo.setWtStatus( ptwInfo.getIsStdWt() == 1 ? 1000 : 300 );
        	statusName = "业主签发";
        }
        
        
        ptwInfo.setWtNo( ptwInfoNoService.genPtwNo( ptwInfo, ptwTypeCode) );
        
        int reuslt = ptwInfoDao.insertPtwInfo( ptwInfo);
        
        List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
        UserInfo userInfo = itcMvcService.getUserInfo(userInfoScope.getUserId(), userInfoScope.getSiteId());
        nextUserList.remove(userInfo);
		String flowNo = ptwInfo.getWtNo();
		String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
		String typeName = "工作票";
		
		ptwSafeService.batchInsertPtwSafe( ptwInfo.getId(),safeItems );
        //插入动火信息
        if ( ptwFireInfo != null ) {
            ptwFireInfo.setWtId( ptwInfo.getId() );
            ptwFireInfoService.insertPtwFireBaseInfo( ptwFireInfo );
            typeName = "动火票";
        }
        
        ptwInfoServiceDef.updatePtwAttachInfo( ptwInfo, ptwFireInfo );
        
        //更新关联钥匙箱号
        this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
        if (ptwInfo.getIsStdWt() != 1) {
        	ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(),ptwInfo.getWtStatus());
		}
        
        //创建待办
        List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
        for ( AppEnum appEnum : enumNewTodoList ) {
            if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
                iTaskFacade.createTask(flowNo, flowNo, typeName, ptwInfo.getWorkContent(), statusName, 
                        jumpPath, nextUserList, userInfoScope, null);
            }
        }
        
        //发短信端口
//        String roleId = userInfoScope.getSiteId() + "_PTWISSUSE";
//
//    	List<UserInfo> userList;
//		try {
//			userList = UserInfoUtil.castUserInfo((SecureUser[])authManager.retriveUsersWithSpecificRole( roleId, null, true, true ).toArray());
//	        itcMsgService.SendSms("签发工作票", StringHelper.concat("需签发工作票：", ptwInfo.getWtNo(), ".工作票内容：", ptwInfo.getWorkContent()), userList, userInfoScope);
//		} catch (Exception e) {
//			log.error("签发工作票通知短信发送异常");
//		}
        
        return reuslt;
    }

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwStatusInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		return ptwInfoServiceDef.updatePtwStatusInfo(ptwInfo, password, modifyDate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwBaseAndLicInfo(PtwInfo ptwInfo, String ptwTypeCode, String safeItems, Date modifyDate, PtwFireInfo ptwFireInfo) {
		log.info("更新工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String relateKeyBoxId = ptwInfo.getRelateKeyBoxId();
		String userId = userInfoScope.getUserId();
		ptwInfo.setModifyUserName(userInfoScope.getUserName());
		ptwInfo.setModifyUser(userId);
		ptwInfo.setModifyDate(modifyDate);
		
		int isOutSourcing = ptwInfo.getIsOutSourcing();
		int wtStatus = ptwInfo.getWtStatus();
		//外委签发时修改是否外委 触发状态改变
		if(wtStatus == 310 && isOutSourcing == 0){
			ptwInfo.setWtStatus(300);
		}else if(wtStatus == 300 && isOutSourcing == 1){
			ptwInfo.setWtStatus(310);
		}

		ptwInfoDao.updatePtwStatusInfo(ptwInfo);
		ptwInfoDao.updatePtwLicInfo(ptwInfo);

		ptwSafeService.deletePtwSafeByWtId(ptwInfo.getId());
		ptwSafeService.batchInsertPtwSafe(ptwInfo.getId(), safeItems);
		if (ptwFireInfo != null) {
			ptwFireInfoService.updatePtwFireBaseInfo(ptwFireInfo);
		}
		int result = ptwInfoDao.updatePtwBaseInfo(ptwInfo);
		ptwInfoServiceDef.updatePtwAttachInfo(ptwInfo, ptwFireInfo);
		this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
		log.info("完成更新工作票");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwIssueInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		log.info("签发工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return "passwordError";
		}
		String typeName = "工作票";
		String statusName = "待许可";
		if ("HY".equals(ptwTypeCode) || "HE".equals(ptwTypeCode)) {
			typeName = "动火票";
			statusName = "审核中";
		}
		if(ptwInfo.getWtStatus() == 300){
			statusName = "业主签发";
			ptwInfoDao.updatePtwOutIssueInfo(ptwInfo);
		}else{
			ptwInfoDao.updatePtwIssueInfo(ptwInfo);
		}
		
		if (isEdit) {
			log.info("工作票处于编辑状态,更新基本信息");
			updatePtwBaseAndLicInfoByIsEdit(ptwInfo, ptwTypeCode, safeItems, modifyDate, ptwFireInfo);
		}
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		String flowNo = ptwInfo.getWtNo();
		//签发完后修改工作票编号
		if(ptwInfo.getWtStatus() == 410 || ptwInfo.getWtStatus() == 400){
			ptwInfoNoService.updatePtwNoAfterIssue(ptwInfo, ptwTypeCode);
		}
		try{
			//删除待办
			iTaskFacade.deleteTask(flowNo, userInfoScope);
		}catch(homePageServiceException e){
			log.error("根据实例或流水号：" + flowNo + "获取任务失败");
		}
		
		flowNo = ptwInfo.getWtNo();
		
		String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
	
		List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
		if(ptwInfo.getWtStatus() == 300){
			UserInfo userInfo = itcMvcService.getUserInfo(ptwInfo.getCreateUser(), ptwInfo.getSiteId());
	        nextUserList.remove(userInfo);
		}
		//创建待办
		List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
		for ( AppEnum appEnum : enumNewTodoList ) {
                    if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
                        iTaskFacade.createTask(flowNo, flowNo, typeName, ptwInfo.getWorkContent(), statusName, 
                                jumpPath, nextUserList, userInfoScope, null); 
                    }
                }
		
		String taskId = "";
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("完成签发工作票");
		return taskId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwFinInfo(PtwInfo ptwInfo, String password, Date modifyDate) throws Exception {
		return ptwInfoServiceDef.updatePtwFinInfo(ptwInfo, password, modifyDate, false, true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwLicInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		return ptwInfoServiceDef.updatePtwLicInfo(ptwInfo, password, modifyDate, isEdit, ptwTypeCode, safeItems, ptwFireInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwEndInfo(PtwInfo ptwInfo, String password, Date modifyDate, String safeItems) {
		return ptwInfoServiceDef.updatePtwEndInfo(ptwInfo, password, modifyDate, safeItems, true, true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwCancelInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		return ptwInfoServiceDef.updatePtwCancelInfo(ptwInfo, password, modifyDate, false, true);
	}

	@Override
	public PtwInfo queryPtwInfoByNo(String wtNo, String siteId) {
		return ptwInfoServiceDef.queryPtwInfoByNo(wtNo, siteId);
	}

	@Override
	public int updatePtwRemark(int ptwId, String remark) {
		return ptwInfoServiceDef.updatePtwRemark(ptwId, remark);
	}

	@Override
	public int deletePtwInfo(int id) {
		return ptwInfoServiceDef.deletePtwInfo(id, false, true);
	}

	@Override
	public int updateRelateKeyBox(int id, String relateKeyBoxId) {
		return ptwInfoServiceDef.updateRelateKeyBox(id, relateKeyBoxId);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId) {
		return ptwInfoServiceDef.queryByKeyBoxId(keyBoxId);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId, String status) {
		return ptwInfoServiceDef.queryByKeyBoxId(keyBoxId, status);
	}

	@Override
	public List<PtwInfo> queryByRelateKeyBoxId(int keyBoxId, String status) {
		return ptwInfoServiceDef.queryByRelateKeyBoxId(keyBoxId, status);
	}

	@Override
	public HashMap<String, Object> validWpicAvailable(String userId) {
		return ptwInfoServiceDef.validWpicAvailable(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwAuditInfo(PtwInfo ptwInfo, String password,
			Date modifyDate,String ptwTypeCode) {
		log.info("审核工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return updated;
		}
		PtwFireInfo ptwFireInfo = new PtwFireInfo();
		String statusName = "审核中";
		ptwFireInfo.setWtId(ptwInfo.getId());
		if(ptwInfo.getWtStatus()==420){
			ptwFireInfo.setAppvXf(userInfoScope.getUserName());
			ptwFireInfo.setAppvXfNo(userInfoScope.getUserId());
			ptwFireInfo.setAppvXfTime(modifyDate);
		}else if(ptwInfo.getWtStatus()==430){
			ptwFireInfo.setAppvAj(userInfoScope.getUserName());
			ptwFireInfo.setAppvAjNo(userInfoScope.getUserId());
			ptwFireInfo.setAppvAjTime(modifyDate);
		}else if(ptwInfo.getWtStatus()==400){
			statusName = "待许可";
			if("HY".equals(ptwTypeCode)){
				ptwFireInfo.setAppvCj(userInfoScope.getUserName());
				ptwFireInfo.setAppvCjNo(userInfoScope.getUserId());
				ptwFireInfo.setAppvCjTime(modifyDate);
			}else if("HE".equals(ptwTypeCode)){
				ptwFireInfo.setAppvBm(userInfoScope.getUserName());
				ptwFireInfo.setAppvBmNo(userInfoScope.getUserId());
				ptwFireInfo.setAppvBmTime(modifyDate);
			}
		}
		int result = ptwFireInfoService.updatePtwFireFlowApprInfo(ptwFireInfo);
		
		PtwInfo ptwInfoById = queryPtwInfoById(ptwInfo.getId());
		String flowNo = ptwInfoById.getWtNo();
		try{
			//删除待办
			iTaskFacade.deleteTask(flowNo, userInfoScope);
		}catch(homePageServiceException e){
			log.error("根据实例或流水号：" + flowNo + "获取任务失败");
		}
		
		String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
	
		List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfoById.getWtStatus(), userInfoScope.getSiteId());
		//创建待办
		List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
		for ( AppEnum appEnum : enumNewTodoList ) {
	            if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
	                iTaskFacade.createTask(flowNo, flowNo, "动火票", ptwInfoById.getWorkContent(), statusName, 
	                            jumpPath, nextUserList, userInfoScope, null); 
	            }
	        }
  		
		  		
		log.info("审核完成");
		return result;
		
	}

	@Override
	public int updatePtwDepartAuditInfo(PtwInfo ptwInfo, String password,
			boolean isEdit, Date modifyDate, String ptwTypeCode,
			String safeItems) {
		return ptwInfoServiceDef.updatePtwDepartAuditInfo(ptwInfo, password, isEdit, modifyDate, ptwTypeCode, safeItems);
	}
	@Override
    public List<PtwInfo> queryPtwInfoListByWoId(String woId, String siteid) throws Exception {
           return ptwInfoServiceDef.queryPtwInfoListByWoId(woId, siteid);
    }
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwBaseAndLicInfoByIsEdit(PtwInfo ptwInfo, String ptwTypeCode, String safeItems, Date modifyDate, PtwFireInfo ptwFireInfo) {
		log.info("签发时更新工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String relateKeyBoxId = ptwInfo.getRelateKeyBoxId();
		String userId = userInfoScope.getUserId();
		ptwInfo.setModifyUserName(userInfoScope.getUserName());
		ptwInfo.setModifyUser(userId);
		ptwInfo.setModifyDate(modifyDate);
		
		ptwInfoDao.updatePtwStatusInfo(ptwInfo);
		ptwInfoDao.updatePtwLicInfo(ptwInfo);

		ptwSafeService.deletePtwSafeByWtId(ptwInfo.getId());
		ptwSafeService.batchInsertPtwSafe(ptwInfo.getId(), safeItems);
		if (ptwFireInfo != null) {
			ptwFireInfoService.updatePtwFireBaseInfo(ptwFireInfo);
		}
		int result = ptwInfoDao.updatePtwBaseInfo(ptwInfo);
		ptwInfoServiceDef.updatePtwAttachInfo(ptwInfo, ptwFireInfo);
		this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
		log.info("完成更新工作票");
		return result;
	}
}
