package com.timss.ptw.service.sbs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.bean.PtwTypeDefine;
import com.timss.ptw.dao.PtwInfoDao;
import com.timss.ptw.service.PtwFireInfoService;
import com.timss.ptw.service.PtwInfoNoService;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwInfoServiceDef;
import com.timss.ptw.service.PtwKeyBoxService;
import com.timss.ptw.service.PtwProcessService;
import com.timss.ptw.service.PtwSafeService;
import com.timss.ptw.service.PtwTypeDefineService;
import com.timss.ptw.service.PtwTypeService;
import com.timss.ptw.vo.PtwInfoVoList;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMsgService;
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
	private ItcMsgService itcMsgService;

	@Autowired
	private IAuthorizationManager authManager;
	@Autowired
	private PtwSafeService ptwSafeService;
	@Autowired
	@Qualifier("WorkOrderServiceImpl")
	private WorkOrderService workOrderService;
	@Autowired
	private PtwFireInfoService ptwFireInfoService;
	@Autowired
	private PtwProcessService ptwProcessService;
	@Autowired
	private PtwTypeService ptwTypeService;
	@Autowired
	private PtwTypeDefineService ptwTypeDefineService;
	@Autowired
	private PtwInfoNoService ptwInfoNoService;
	@Autowired
	private PtwKeyBoxService ptwKeyBoxService;

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
        Date now = new Date();
        
        String relateKeyBoxId = ptwInfo.getRelateKeyBoxId();
        
        //用户信息，更新信息等的更新
        ptwInfo.setDeptId( userInfoScope.getOrgId() );
        ptwInfo.setSiteId( userInfoScope.getSiteId() );
        ptwInfo.setCreateUserName( userInfoScope.getUserName() );
        ptwInfo.setCreateUser( userInfoScope.getUserId() );
        ptwInfo.setCreateDate( now );
        ptwInfo.setModifyUserName( userInfoScope.getUserName() );
        ptwInfo.setModifyUser( userInfoScope.getUserId() );
        ptwInfo.setModifyDate( now );
        //设置为未签发
        ptwInfo.setWtStatus( ptwInfo.getIsStdWt() == 1 ? 1000 : 300 );
        
        
        ptwInfo.setWtNo( ptwInfoNoService.genPtwNo( ptwInfo, ptwTypeCode) );
        
        int reuslt = ptwInfoDao.insertPtwInfo( ptwInfo);
        
        
        ptwSafeService.batchInsertPtwSafe( ptwInfo.getId(),safeItems );
        if ( ptwInfo.getWorkOrderId() != 0 ) {
        	log.info("开始更新工单"+ptwInfo.getWorkOrderId()+"的工作票Id为"+ptwInfo.getId());
            workOrderService.updateWOAddPTWId( ptwInfo.getWorkOrderId(), ptwInfo.getId() );
            log.info("成功更新工单"+ptwInfo.getWorkOrderId()+"的工作票Id为"+ptwInfo.getId());
        }else{
        	log.info("用户输入的"+ptwInfo.getWorkOrderNo()+"没有对应的工单Id");
        }
        //插入动火信息
        if ( ptwFireInfo != null ) {
            ptwFireInfo.setWtId( ptwInfo.getId() );
            ptwFireInfoService.insertPtwFireBaseInfo( ptwFireInfo );
        }
        
        ptwInfoServiceDef.updatePtwAttachInfo( ptwInfo, ptwFireInfo );
        
        //更新关联钥匙箱号
        this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
        if (ptwInfo.getIsStdWt() != 1) {
        	ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(),ptwInfo.getWtStatus());
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
		return ptwInfoServiceDef.updatePtwBaseAndLicInfo(ptwInfo, ptwTypeCode, safeItems, modifyDate, ptwFireInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwIssueInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		return ptwInfoServiceDef.updatePtwIssueInfo(ptwInfo, password, modifyDate, isEdit, ptwTypeCode, safeItems, ptwFireInfo, true, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwFinInfo(PtwInfo ptwInfo, String password, Date modifyDate) throws Exception {
		return ptwInfoServiceDef.updatePtwFinInfo(ptwInfo, password, modifyDate, true, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwLicInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		log.info("许可工作票:" + ptwInfo.toString());
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return "passwordError";
		}

		if (isEdit) {
			updatePtwBaseAndLicInfo(ptwInfo, ptwTypeCode, safeItems, modifyDate, ptwFireInfo);
		} else {
			ptwSafeService.deletePtwSafeByWtId(ptwInfo.getId());
			ptwSafeService.batchInsertPtwSafe(ptwInfo.getId(), safeItems);
			ptwInfoDao.updatePtwLicInfo(ptwInfo);
		}
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		PtwType ptwType = ptwTypeService.queryPtwTypeById(ptwInfo.getWtTypeId());
		PtwTypeDefine ptwTypeDefine = ptwTypeDefineService.queryPtwTypeDefineById(ptwType.getWtTypeDefineId());
		String taskId = "";
		if (ptwTypeDefine.getRemark2() == 1) {
			log.info("启动许可动火票的流程");
			taskId = ptwProcessService.startNewProcess(ptwInfo, userInfoScope, "动火票许可", ptwType.getTypeCode().toLowerCase() + "lic");
			log.info("许可动火票的流程启动成功");
		}
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("许可结束");
		return taskId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwEndInfo(PtwInfo ptwInfo, String password, Date modifyDate, String safeItems) {
		return ptwInfoServiceDef.updatePtwEndInfo(ptwInfo, password, modifyDate, safeItems, true, false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwCancelInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		return ptwInfoServiceDef.updatePtwCancelInfo(ptwInfo, password, modifyDate, true, false);
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
		return ptwInfoServiceDef.deletePtwInfo(id, true, false);
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
	public int updatePtwAuditInfo(PtwInfo ptwInfo, String password,
			Date modifyDate,String ptwTypeCode) {
		return ptwInfoServiceDef.updatePtwAuditInfo(ptwInfo, password, modifyDate, ptwTypeCode);
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
}
