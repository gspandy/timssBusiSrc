package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwChangeWpic;
import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwKeyBox;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.bean.PtwTypeDefine;
import com.timss.ptw.bean.PtwWaitRestore;
import com.timss.ptw.dao.PtwInfoDao;
import com.timss.ptw.service.PtwChangeWpicService;
import com.timss.ptw.service.PtwFireInfoService;
import com.timss.ptw.service.PtwInfoNoService;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwInfoServiceDef;
import com.timss.ptw.service.PtwKeyBoxService;
import com.timss.ptw.service.PtwProcessService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.PtwSafeService;
import com.timss.ptw.service.PtwTypeDefineService;
import com.timss.ptw.service.PtwTypeService;
import com.timss.ptw.service.PtwWaitRestoreService;
import com.timss.ptw.util.PtwAuditPrivUtil;
import com.timss.ptw.vo.PtwInfoVoList;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.dto.support.Attachment;
import com.yudean.itc.exception.homepage.homePageServiceException;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.support.IAttachmentManager;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.itc.util.Constant;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMsgService;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwInfoServiceDefImpl implements PtwInfoServiceDef {
	private static final Logger log = Logger.getLogger(PtwInfoService.class);

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
	private AttachmentMapper attachmentMapper;
	@Autowired
	private IAttachmentManager attachmentManager;
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
	@Autowired
	private PtwWaitRestoreService ptwWaitRestoreService;
	@Autowired
        private ITaskFacade iTaskFacade;
	@Autowired
	private PtwPtoSelectUserService ptwPtoSelectUserService;
	@Autowired 
	private IEnumerationManager iEnumerationManager;
	@Autowired 
	private PtwChangeWpicService ptwChangeWpicService;

	@Override
	public Page<PtwInfoVoList> queryPtwInfoVoList(Page<PtwInfoVoList> page) throws Exception {
		List<PtwInfoVoList> list = ptwInfoDao.queryPtwInfoVoList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
	public Page<PtwInfoVoList> queryHisPtwList(Page<PtwInfoVoList> page) throws Exception {
		List<PtwInfoVoList> list = ptwInfoDao.queryHisPtwList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
    public List<PtwInfo> queryPtwInfoListByWoId(String woId, String siteid) throws Exception {
            List<PtwInfo> list = ptwInfoDao.queryPtwInfoVoListByWoId(woId,siteid);
            return list;
    }

	@Override
	public PtwInfo queryPtwInfoById(int id) {
		return ptwInfoDao.queryPtwInfoById(id);
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
        String typeName = "工作票";
		String statusName = "待签发";
        
        //用户信息，更新信息等的更新
        ptwInfo.setDeptId( userInfoScope.getOrgId() );
        ptwInfo.setSiteId( siteid );
        ptwInfo.setCreateUserName( userInfoScope.getUserName() );
        ptwInfo.setCreateUser( userInfoScope.getUserId() );
        ptwInfo.setCreateDate( now );
        ptwInfo.setModifyUserName( userInfoScope.getUserName() );
        ptwInfo.setModifyUser( userInfoScope.getUserId() );
        ptwInfo.setModifyDate( now );
        if("RY".equalsIgnoreCase(ptwTypeCode)){
        	//设置为部长审批
        	statusName = "审批中";
        	ptwInfo.setWtStatus( ptwInfo.getIsStdWt() == 1 ? 1000 : 320 );
        }else{
        	//设置为未签发
        	ptwInfo.setWtStatus( ptwInfo.getIsStdWt() == 1 ? 1000 : 300 );
        }
        
       //TODO 设置工作票编号
        boolean fixedCodeflag = hasEnumValueInEnumcat("fixedPtwNo","PTW_SINGLE_VALUE",siteid) ;
        if(!fixedCodeflag){  //签发时候才产生编号
            ptwInfo.setWtNo( ptwInfoNoService.genPtwNo( ptwInfo, ptwTypeCode) );
        }else{  //SWF,新建时就产生一个固定编号
            ptwInfo.setWtNo( ptwInfoNoService.genFixedPtwNo( ptwInfo, ptwTypeCode) );
        }
            
        int reuslt = ptwInfoDao.insertPtwInfo( ptwInfo);
        
        
        ptwSafeService.batchInsertPtwSafe( ptwInfo.getId(),safeItems );
        //插入动火信息
        if ( ptwFireInfo != null ) {
        	typeName = "动火票";
            ptwFireInfo.setWtId( ptwInfo.getId() );
            ptwFireInfoService.insertPtwFireBaseInfo( ptwFireInfo );
        }
        
        updatePtwAttachInfo( ptwInfo, ptwFireInfo );
        String flowNo = ptwInfo.getWtNo();
        String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
        List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
        UserInfo userInfo = itcMvcService.getUserInfo(userInfoScope.getUserId(), siteid);
        nextUserList.remove(userInfo);
        //创建待办
        List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
        for ( AppEnum appEnum : enumNewTodoList ) {
            if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
                iTaskFacade.createTask(flowNo, flowNo, typeName, ptwInfo.getWorkContent(), statusName, 
                        jumpPath, nextUserList, userInfoScope, null);
            }
        }
        
        //更新关联钥匙箱号
        this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
        if (ptwInfo.getIsStdWt() != 1) {
        	ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(),ptwInfo.getWtStatus());
		}
        
        //发短信端口
//        String roleId = userInfoScope.getSiteId() + "_PTWISSUSE";
//
//    	List<UserInfo> userList;
//	try {
//		userList = UserInfoUtil.castUserInfo((SecureUser[])authManager.retriveUsersWithSpecificRole( roleId, null, true, true ).toArray());
//	        itcMsgService.SendSms("签发工作票", StringHelper.concat("需签发工作票：", ptwInfo.getWtNo(), ".工作票内容：", ptwInfo.getWorkContent()), userList, userInfoScope);
//	} catch (Exception e) {
//		log.error("签发工作票通知短信发送异常");
//	}
        
        return reuslt;
    }

    	private boolean hasEnumValueInEnumcat(String enumCode, String enumCate, String siteid) {
              List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( enumCate );
              for ( AppEnum appEnum : enumNewTodoList ) {
                  if(appEnum.getCode().equalsIgnoreCase( enumCode ) && siteid.equals( appEnum.getSiteId() )){
                      return true;
                  }
              }
            return false;
        }

    /**
	 * 更新普通票和动火票的附加文件信息
	 * 
	 * @description:
	 * @author: 周保康
	 * @createDate: 2014-8-12
	 * @param ptwInfo
	 * @param ptwFireInfo
	 * @return:
	 */
	@Override
	public int updatePtwAttachInfo(PtwInfo ptwInfo, PtwFireInfo ptwFireInfo) {
		// 更新附加文件信息
		log.info("开始更新工作票的附件信息");
		List<String> fileIds = new ArrayList<String>();
		if (ptwInfo.getAddFile1() != null && !ptwInfo.getAddFile1().equals("")) {
			fileIds.add(ptwInfo.getAddFile1());
		}
		if (ptwInfo.getAddFile2() != null && !ptwInfo.getAddFile2().equals("")) {
			fileIds.add(ptwInfo.getAddFile2());
		}
		if (ptwInfo.getAddFile3() != null && !ptwInfo.getAddFile3().equals("")) {
			fileIds.add(ptwInfo.getAddFile3());
		}
		if (ptwInfo.getAddFile4() != null && !ptwInfo.getAddFile4().equals("")) {
			fileIds.add(ptwInfo.getAddFile4());
		}
		if (ptwInfo.getAddFile5() != null && !ptwInfo.getAddFile5().equals("")) {
			fileIds.add(ptwInfo.getAddFile5());
		}
		if (ptwInfo.getAddFileOtherNo() != null && !ptwInfo.getAddFileOtherNo().equals("")) {
			fileIds.add(ptwInfo.getAddFileOtherNo());
			Attachment attachment = attachmentManager.retrieveAttachment(Constant.basePath, ptwInfo.getAddFileOtherNo());
			if (attachment != null) {
				String fileName = attachment.getOriginalFileName();
				ptwInfo.setAddFileOtherName(fileName.substring(0, fileName.lastIndexOf(".")));
			} else {
				log.error("没有找到工作票ID为" + ptwInfo.getId() + "中，附件Id为：" + ptwInfo.getAddFileOtherNo() + "的文件");
			}
		}
		int result = 0;
		//if (fileIds.size() > 0) {
		result = ptwInfoDao.updatePtwAttachFiles(ptwInfo);
		//}

		if (ptwFireInfo != null && ptwFireInfo.getFireWorkPic() != null && !ptwFireInfo.getFireWorkPic().equals("")) {
			fileIds.add(ptwFireInfo.getFireWorkPic());
		}
		if(ptwFireInfo != null){
			ptwFireInfoService.updatePtwFirePic(ptwFireInfo);
		}
		// 更新到文件中
		if (fileIds.size() > 0) {
			attachmentMapper.setAttachmentsBinded((String[]) fileIds.toArray(new String[fileIds.size()]), 1);
		}
		log.info("完成更新工作票的附件信息");
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwStatusInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		if (password == null || password.trim().equals("")) {
			return -1;
		}
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		// 校验密码
		if (!authManager.verifyPassword(userId, password.trim())) {
			return -1;
		}
		ptwInfo.setModifyUserName(userInfoScope.getUserName());
		ptwInfo.setModifyUser(userId);
		ptwInfo.setModifyDate(modifyDate);
		return ptwInfoDao.updatePtwStatusInfo(ptwInfo);
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

		ptwInfoDao.updatePtwStatusInfo(ptwInfo);
		ptwInfoDao.updatePtwLicInfo(ptwInfo);

		ptwSafeService.deletePtwSafeByWtId(ptwInfo.getId());
		ptwSafeService.batchInsertPtwSafe(ptwInfo.getId(), safeItems);
		if (ptwFireInfo != null) {
			ptwFireInfoService.updatePtwFireBaseInfo(ptwFireInfo);
		}
		int result = ptwInfoDao.updatePtwBaseInfo(ptwInfo);
		updatePtwAttachInfo(ptwInfo, ptwFireInfo);
		this.updateRelateKeyBox(ptwInfo.getId(), relateKeyBoxId);
		log.info("完成更新工作票");
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwIssueInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo, boolean hasProcess, boolean isProcess)
			throws Exception {
		log.info("签发工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return "passwordError";
		}
		if (isEdit) {
			log.info("工作票处于编辑状态,更新基本信息");
			updatePtwBaseAndLicInfo(ptwInfo, ptwTypeCode, safeItems, modifyDate, ptwFireInfo);
		}
		ptwInfoDao.updatePtwIssueInfo(ptwInfo);
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		String flowNo = ptwInfo.getWtNo();
		//TODO 判断工单编号是否是固定编号
		boolean fixedCodeflag = hasEnumValueInEnumcat("fixedPtwNo","PTW_SINGLE_VALUE",siteid) ;
		if( !fixedCodeflag ){ //若是含有临时编号，则更新，如果是手动编号则不更新编号
		    ptwInfoNoService.updatePtwNoAfterIssue(ptwInfo, ptwTypeCode);
		}
		
		String taskId = "";
		if(hasProcess){
			PtwType ptwType = ptwTypeService.queryPtwTypeById(ptwInfo.getWtTypeId());
			PtwTypeDefine ptwTypeDefine = ptwTypeDefineService.queryPtwTypeDefineById(ptwType.getWtTypeDefineId());
			if (ptwTypeDefine.getRemark1() == 1) {
				log.info("启动签发动火票的流程");
				taskId = ptwProcessService.startNewProcess(ptwInfo, userInfoScope, "动火票签发", ptwType.getTypeCode().toLowerCase() + "issue");
				log.info("签发动火票的流程启动成功");
			}
		}
		if(isProcess){
			String typeName = "工作票";
			String statusName = "待许可";
			if ("HY".equals(ptwTypeCode) || "HE".equals(ptwTypeCode)) {
				typeName = "动火票";
				statusName = "审核中";
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
			//创建待办
			 List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
			 for ( AppEnum appEnum : enumNewTodoList ) {
			     if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
			         iTaskFacade.createTask(flowNo, flowNo, typeName, ptwInfo.getWorkContent(), statusName, 
                                     jumpPath, nextUserList, userInfoScope, null); 
			     }
			 }
		}
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("完成签发工作票");
		return taskId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwFinInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean hasProcess, boolean isProcess) throws Exception {
		log.info("结束工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return "passwordError";
		}
		ptwInfoDao.updatePtwFinInfo(ptwInfo);
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		String taskId = "";
		if(hasProcess){
			PtwType ptwType = ptwTypeService.queryPtwTypeById(ptwInfo.getWtTypeId());
			PtwTypeDefine ptwTypeDefine = ptwTypeDefineService.queryPtwTypeDefineById(ptwType.getWtTypeDefineId());
			if (ptwTypeDefine.getRemark3() == 1) {
				log.info("启动结束动火票的流程");
				taskId = ptwProcessService.startNewProcess(ptwInfo, userInfoScope, "动火票结束", "dh" + "finish");
				log.info("结束动火票的流程启动成功");
			}
		}
		if(isProcess){
			String flowNo = ptwInfo.getWtNo();
			try{
				//删除待办
				iTaskFacade.deleteTask(flowNo, userInfoScope);
			}catch(homePageServiceException e){
				log.error("根据实例或流水号：" + flowNo + "获取任务失败");
			}
			
			PtwType ptwType = ptwTypeService.queryPtwTypeById(ptwInfo.getWtTypeId());
			String ptwTypeCode = ptwType.getTypeCode();
			if(!"HY".equals(ptwTypeCode)&&!"HE".equals(ptwTypeCode)){
				String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ ptwTypeCode +"',id:" + ptwInfo.getId() + "}";
				
				List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
				//创建待办
				 List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
				 for ( AppEnum appEnum : enumNewTodoList ) {
				            if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
				                iTaskFacade.createTask(flowNo, flowNo, "工作票", ptwInfo.getWorkContent(), "待终结", 
		                                             jumpPath, nextUserList, userInfoScope, null); 
				            }
				        }
			}
		}
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("完成结束工作票");
		return taskId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwLicInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		log.info("许可工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		String typeName = "工作票";
		String statusName = "待结束";
		if(ptwFireInfo != null){
			typeName = "动火票";
			statusName = "待终结";
			ptwInfo.setWtStatus(600);
		}
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
			if(ptwFireInfo!=null){
				ptwFireInfoService.updatePtwFireBaseInfo(ptwFireInfo);
			}
		}
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		String flowNo = ptwInfo.getWtNo();
		try{
			//删除待办
			iTaskFacade.deleteTask(flowNo, userInfoScope);
		}catch(homePageServiceException e){
			log.error("根据实例或流水号：" + flowNo + "获取任务失败");
		}
		
		String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
	
		List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
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
		log.info("许可结束");
		return taskId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwEndInfo(PtwInfo ptwInfo, String password, Date modifyDate, String safeItems, boolean hasWorkOrder, boolean isProcess) {
		log.info("终结工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return updated;
		}
		
		ptwSafeService.batchUpdatePtwSafeRemover(ptwInfo.getId(), safeItems);
		updated = ptwInfoDao.updatePtwEndInfo(ptwInfo);
		if(hasWorkOrder){
			// 终结工作票后，需要人工的将工单走向下一个节点
			// 查找最新的工作票负责人
			if (ptwInfo.getWorkOrderId() != 0) {
				log.info("终结工作票时，自动让工单走向下一个节点:" + ptwInfo.getId() + ",woId  = " + ptwInfo.getWorkOrderId() + ",下一个人：" + ptwInfo.getLicWpicNo());
				workOrderService.inPtwToNextStep(ptwInfo.getWorkOrderId(), ptwInfo.getLicWpicNo());
				log.info("工单流程执行成功");
			} else {
				log.info("不存在工单Id,不能自动走工单的下一步");
			}
		}
		if(isProcess){
			PtwInfo ptwInfoById = ptwInfoDao.queryPtwInfoById(ptwInfo.getId());
			String extCode = ptwInfoById.getWtNo();
			try{
				//删除待办
				iTaskFacade.deleteTask(extCode, userInfoScope);
			}catch(homePageServiceException e){
				log.error("根据实例或流水号：" + extCode + "获取任务失败");
			}
		}
		ptwKeyBoxService.queryPtwKeyBoxById(1);
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("终结成功");
		return updated;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwCancelInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean hasWorkOrder, boolean isProcess) {
		log.info("作废工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return updated;
		}
		int result = ptwInfoDao.updatePtwCancelInfo(ptwInfo);
		// 标准票在作废的同时，逻辑删除
		if (ptwInfo.getIsStdWt() == 1) {
			ptwInfoDao.updatePtwNotInUse(ptwInfo.getId());
		}
		ptwInfo = queryPtwInfoById(ptwInfo.getId());
		if(hasWorkOrder){
			// 同时将工单的工作票ID清空
			if (ptwInfo.getWorkOrderId() != 0) {
				log.info("开始更新工单的关联工作票信息");
				workOrderService.updateWOAddPTWId(ptwInfo.getWorkOrderId(), 0);
				log.info("完成更新工单的关联工作票信息");
			}
		}
		if(isProcess){
			try{
				iTaskFacade.deleteTask(ptwInfo.getWtNo(), userInfoScope);
			}catch(homePageServiceException e){
				log.error("根据实例或流水号：" + ptwInfo.getWtNo() + "获取任务失败");
			}
		}
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), ptwInfo.getWtStatus());
		log.info("作废成功");
		return result;
	}

	@Override
	public PtwInfo queryPtwInfoByNo(String wtNo, String siteId) {
		PtwInfo ptwInfo = new PtwInfo();
		ptwInfo.setWtNo(wtNo);
		ptwInfo.setSiteId(siteId);
		return ptwInfoDao.queryPtwInfoByNo(ptwInfo);
	}

	@Override
	public int updatePtwRemark(int ptwId, String remark) {
		if (remark == null || remark.trim().equals("")) {
			return 1;
		}
		PtwInfo ptwInfo = new PtwInfo();
		ptwInfo.setId(ptwId);
		ptwInfo.setRemark(remark);
		return ptwInfoDao.updatePtwRemark(ptwInfo);
	}

	@Override
	public int deletePtwInfo(int id, boolean hasWorkOrder, boolean isProcess) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		PtwInfo ptwInfo = ptwInfoDao.queryPtwInfoById(id);
		if(hasWorkOrder){
			if (ptwInfo.getWorkOrderId() != 0) {
				log.info("开始更新工单的关联工作票信息");
				workOrderService.updateWOAddPTWId(ptwInfo.getWorkOrderId(), 0);
				log.info("完成更新工单的关联工作票信息");
			}
		}
		int deleted = ptwInfoDao.deletePtwInfoById(id);
		ptwKeyBoxService.updateKeyBoxStatusByPtwOrIslStatus(ptwInfo.getKeyBoxId(), 800);
		if(isProcess){
			try{
				//删除待办
				iTaskFacade.deleteTask(ptwInfo.getWtNo(), userInfoScope);
			}catch(homePageServiceException e){
				log.error("根据实例或流水号：" + ptwInfo.getWtNo() + "获取任务失败");
			}
		}
		return deleted;
	}

	@Override
	public int updateRelateKeyBox(int id, String relateKeyBoxId) {
		log.info("更新工作票" + id + "的关联钥匙箱" + relateKeyBoxId);
		return ptwInfoDao.updateRelateKeyBox(id, relateKeyBoxId);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId) {
		String status = "300,400,500,600";
		return queryByKeyBoxId(keyBoxId, status);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId, String status) {
		List<PtwInfo> list = ptwInfoDao.queryByKeyBoxId(keyBoxId, status);
		for (PtwInfo ptwInfo : list) {
			if (ptwInfo.getRelateKeyBoxId() != null && !ptwInfo.getRelateKeyBoxId().equals("")) {
				List<PtwKeyBox> keyBoxes = ptwKeyBoxService.queryByIds(ptwInfo.getRelateKeyBoxId());
				StringBuffer keyBoxNos = new StringBuffer();
				for (PtwKeyBox ptwKeyBox : keyBoxes) {
					keyBoxNos.append(ptwKeyBox.getKeyBoxNo()).append(",");
				}
				ptwInfo.setKeyBoxNo(keyBoxNos.substring(0, keyBoxNos.length() - 1).toString());
			}
		}
		return list;
	}

	@Override
	public List<PtwInfo> queryByRelateKeyBoxId(int keyBoxId, String status) {
		List<PtwInfo> list = ptwInfoDao.queryByRelateKeyBoxId(keyBoxId, status);
		return list;
	}

	@Override
	public HashMap<String, Object> validWpicAvailable(String userId) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		//第一种情况 直接查询相同工作负责人是否手持工作票
		List<PtwInfo> list = ptwInfoDao.queryPtwInfoByWpicAndStatus(userId, "500");
		// 检查每张工作票是否被收回
		// 如果已经收回了，也可以开新的工作票
		if (list != null && !list.isEmpty()) {
			List<PtwInfo> toRemove = new ArrayList<PtwInfo>();
			for (PtwInfo ptwInfo : list) {
				PtwChangeWpic pcw = ptwChangeWpicService.queryPtwChangeWpicByPtwId(ptwInfo.getId());
				if( pcw != null ){
					String chaNewWpicNo = pcw.getChaNewWpicNo();
					if(chaNewWpicNo!=null && !chaNewWpicNo.equals(userId)){
						toRemove.add(ptwInfo);
					}
				}
				List<PtwWaitRestore> waits = ptwWaitRestoreService.queryPtwWaitRestoreByPtwId(ptwInfo.getId());
				if (waits != null && !waits.isEmpty()) {
					if (waits.get(waits.size() - 1).getResTime() == null) {
						toRemove.add(ptwInfo);
					}
				}
				
			}
			list.removeAll(toRemove);
		}
		List<PtwInfo> list2 = new ArrayList<PtwInfo>();
		if(list == null || list.isEmpty()){
			//第二中情况 变更过的工作负责人是否手持工作票
			if(!StringUtils.isEmpty(userId)){
				List<PtwChangeWpic> changeList = ptwChangeWpicService.queryPtwChangeWpicByNewNo(userId);
				if (changeList != null && !changeList.isEmpty()) {
					for (PtwChangeWpic ptwChange : changeList) {
						list2 = ptwInfoDao.queryPtwInfoByWpicAndStatus(ptwChange.getChaOldWpicNo(), "500");
						if (list2 != null && !list2.isEmpty()) {
							List<PtwInfo> toRemove = new ArrayList<PtwInfo>();
							for (PtwInfo ptwInfo : list2) {
								PtwChangeWpic pcw = ptwChangeWpicService.queryPtwChangeWpicByPtwId(ptwInfo.getId());
								if( pcw != null ){
									String chaNewWpicNo = pcw.getChaNewWpicNo();
									if(chaNewWpicNo!=null && !chaNewWpicNo.equals(userId)){
										toRemove.add(ptwInfo);
									}
								}else{
									toRemove.add(ptwInfo);
								}
								List<PtwWaitRestore> waits = ptwWaitRestoreService.queryPtwWaitRestoreByPtwId(ptwInfo.getId());
								if (waits != null && !waits.isEmpty()) {
									if (waits.get(waits.size() - 1).getResTime() == null) {
										toRemove.add(ptwInfo);
									}
								}
							}
							list2.removeAll(toRemove);
						}
					}
				}
			}
			result.put("valid", list2 == null || list2.isEmpty() ? true : false);
			result.put("list", list2);
		}else{
			result.put("valid", list == null || list.isEmpty() ? true : false);
			result.put("list", list);
		}
		
		
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwAuditInfo(PtwInfo ptwInfo, String password,
			Date modifyDate,String ptwTypeCode) {
		log.info("审核工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		boolean isFire = true;
		if(ptwInfo.getWtStatus()==300){
			ptwInfo.setApprover(userInfoScope.getUserName());
			ptwInfo.setApproverNo(userInfoScope.getUserId());
			ptwInfo.setApproveTime(modifyDate);
			isFire = false;
		}
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return updated;
		}
		
		if(isFire){
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
		return updated;
	}

	@Override
	public int updatePtwDepartAuditInfo(PtwInfo ptwInfo, String password,
			boolean isEdit, Date modifyDate, String ptwTypeCode,
			String safeItems) {
		log.info("审批工作票:" + ptwInfo.toString());
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteid = userInfoScope.getSiteId();
		if(ptwInfo.getWtStatus()==300){
			ptwInfo.setApprover(userInfoScope.getUserName());
			ptwInfo.setApproverNo(userInfoScope.getUserId());
			ptwInfo.setApproveTime(modifyDate);
		}
		int updated = updatePtwStatusInfo(ptwInfo, password, modifyDate);
		if (updated == -1) {
			return updated;
		}
		
		String flowNo = ptwInfo.getWtNo();
		try{
			//删除待办
			iTaskFacade.deleteTask(flowNo, userInfoScope);
		}catch(homePageServiceException e){
			log.error("根据实例或流水号：" + flowNo + "获取任务失败");
		}
		
		String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',ptwTypeCode:'"+ptwTypeCode+"',id:" + ptwInfo.getId() + "}";
	
		List<UserInfo> nextUserList = PtwAuditPrivUtil.queryNextUserList(itcMvcService, ptwPtoSelectUserService, ptwTypeCode, ptwInfo.getWtStatus(), userInfoScope.getSiteId());
		//创建待办
		 List<AppEnum> enumNewTodoList = iEnumerationManager.retriveEnumerationsByCat( "PTW_NEED_TODO" );
		 for ( AppEnum appEnum : enumNewTodoList ) {
                     if(appEnum.getCode().toUpperCase().equals( "Y" ) && siteid.equals( appEnum.getSiteId() )){
                         iTaskFacade.createTask(flowNo, flowNo, "工作票", ptwInfo.getWorkContent(), "待签发", 
                                 jumpPath, nextUserList, userInfoScope, null); 
                     }
                 }
		
		if (isEdit) {
			log.info("工作票处于编辑状态,更新基本信息");
			updatePtwBaseAndLicInfo(ptwInfo, ptwTypeCode, safeItems, modifyDate, null);
		}
		log.info("完成审批工作票");
		return updated;
	}
}
