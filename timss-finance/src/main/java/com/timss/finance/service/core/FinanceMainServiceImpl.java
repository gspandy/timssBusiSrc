package com.timss.finance.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceFlowMatch;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinanceMainDao;
import com.timss.finance.service.FinanceAttachMatchService;
import com.timss.finance.service.FinanceFlowMatchService;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.util.FinStatusEnum;
import com.timss.finance.util.FinanceUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Service
public class FinanceMainServiceImpl implements FinanceMainService {
        private Logger logger = Logger.getLogger(FinanceMainService.class);

	@Autowired
	private FinanceMainDao financeMainDao;	
	@Autowired
	WorkflowService wfs;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
        private IEnumerationManager iEnumManager;
	@Autowired
	private FinanceMainDetailService financeMainDetailService;
	@Autowired
	private HomepageService homepageService;
	@Autowired
	private FinanceFlowMatchService financeFlowMatchService;
	@Autowired
        FinanceAttachMatchService financeAttachMatchService;
	@Override
	public Page<FinanceMain> queryFinanceMainList(Page<FinanceMain> page,UserInfoScope userInfoScope, String search)  {
            String siteid = userInfoScope.getSiteId();
	    if (!StringUtil.isBlank(search)) {
			FinanceMain financeMain=JsonHelper.fromJsonStringToBean(search, FinanceMain.class);
			page.setParameter("fid", financeMain.getFid());
			page.setParameter("fname", financeMain.getFname());
			page.setParameter("creatorname", financeMain.getCreatorname());
			page.setParameter("department", financeMain.getDepartment());
			//page.setParameter("createdate", financeMain.getCreatedate());
			page.setParameter("finance_flow", financeMain.getFinance_flow());
			page.setParameter("finance_type", financeMain.getFinance_type());
			page.setParameter("statusName", financeMain.getStatusName());
		}

		page.setParameter("loginUserId", userInfoScope.getUserId());
        
		page.setParameter("siteid", userInfoScope.getSiteId());
		List<FinanceMain> list;
		
		List<Role> roleList = userInfoScope.getRoles();
		boolean isZJL = false;
		
		String roleId;
		for( Role role : roleList ) {
			roleId = role.getId();
			if(roleId.equals(siteid+"_ZJL")) {
				isZJL = true;
				break;
			}
		}
		
		if( isZJL ) { //是总经理角色,则可以看到所有报销单
			list = financeMainDao.queryAllFinanceMainList(page);
		} else {
			list = financeMainDao.queryFinanceMainList(page);
		}

		page.setResults(list);
		return page;
	}

	 @Override
	 public List<FinanceMain> queryFinanceMainListByApplyId(String applyId, String siteid) {
	     List<FinanceMain> list = financeMainDao.queryFinanceMainListByApplyId(applyId,siteid);
	     return list;
	 }
	 
	public Map<String, Object> queryFinanceMainByFid(String fid)  {
		FinanceMain financeMain = financeMainDao.queryFinanceMainByFid(fid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("financeMain", financeMain);
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		map.put("userId", userInfoScope.getUserId());
		map.put("userName", userInfoScope.getUserName());
		// 通过contains判断流程，以及报销类型
		return map;
	}



	/** 
         * @description: 修改报销单信息(草稿状态下)
         * @author: 890170
         * @createDate: 2015-1-4
         */
        @Override
        @Transactional(propagation = Propagation.REQUIRED) //事务控制要加这个注解
        public FinanceMain updateFinanceMainByFid(FinInsertParams insertParams, String fid)  {
            FinanceMain financeMain = financeMainDao.queryFinanceMainByFid(fid);

            String formData = insertParams.getFormData();
            
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            financeMain.setUpdateid(userInfoScope.getUserId());
            double pric = 0;
            pric = Double.parseDouble(FinanceUtil.getJsonFieldString(formData, "total_amount"));
            
            String name = FinanceUtil.getJsonFieldString(formData, "fname");
            String description = FinanceUtil.getJsonFieldString(formData, "description");
            financeMain.setFname(name);
            financeMain.setTotal_amount(pric);
            financeMain.setDescription( description );
            financeMainDao.updateFinanceMainByFid(financeMain);
            return financeMain;
        }
      //更新报销单处理状态
	@Override
	@Transactional(propagation = Propagation.REQUIRED) //事务控制要加这个注解
	public FinanceMain updateFinanceMainStatus(Task task,String processInstanceId, String flowStatus)  {
		FinanceFlowMatch finFlowMatch = financeFlowMatchService
				.queryFinanceFlowMatchByPid(processInstanceId);
		Map<String, Object> map = queryFinanceMainByFid(finFlowMatch.getFid());
		FinanceMain finMain = (FinanceMain) map.get("financeMain");

		finMain = updateFinanceMainStatusByFid( flowStatus, finMain.getFid());
		
		return finMain;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) //事务控制要加这个注解
	//根据报销单编号,更新报销单处理状态
	public FinanceMain updateFinanceMainStatusByFid(String flowStatus,
			String fid)  {
		Map<String, Object> map = queryFinanceMainByFid(fid);
		FinanceMain fm = (FinanceMain) map.get("financeMain");
		AppEnum appEnum = new AppEnum();
		List<AppEnum> flowENum = itcMvcService.getEnum("FIN_WORKFLOW_APPROVE_STATUS");
		
		for( int i=0; i<flowENum.size(); i++ ) {
			appEnum = flowENum.get(i);
			if( appEnum.getCode().equals(flowStatus) ) {
				fm.setStatus(flowStatus);
				fm.setStatusName(appEnum.getLabel());
				financeMainDao.updateFinanceMainByFid(fm);
				break;
			}
		}
		
		return fm;
	}

	/** 
	 * @description: 逻辑删除报销单(更改报销单显示状态)
	 * @author: 890170
	 * @createDate: 2014-12-23
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED) //事务控制要加这个注解
	public String deleteAndUpdateFinanceMain(String fid)  {
	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		FinanceMain fm = financeMainDao.queryFinanceMainByFid(fid);
		fm.setIs_show("N");
		financeMainDao.updateFinanceMainByFid(fm);
		homepageService.Delete( fid, userInfoScope );
		// wfs.delete(arg0, arg1);//实例id,流程删除意见
		return "success";
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public FinanceMain deleteFinanceMain(FinanceMain financeMain){
		financeMainDao.deleteFinanceMain(financeMain.getFid());// 删除主表
		financeMainDetailService.deleteFinanceMainDetail(financeMain);// 删除主表同时删除detail表
		return null;
	}

	@Override
	public String queryTaskIdByPid(String pid)  {
		logger.info("pid: " + pid);
		
		List<Task> activities = wfs.getActiveTasks(pid);
		if( !activities.isEmpty() ) {
			Task task = activities.get(0);
			return task.getId();
		} else {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) //事务控制要加这个注解
	public FinanceMain updateFinanceMainStatusByDid(String flowStatus, String did)  {
		FinanceMainDetail fmd = financeMainDetailService.queryFinanceMainDetailById(did);
		FinanceMain finMain = updateFinanceMainStatusByFid( flowStatus, fmd.getFid() );
		
		return finMain;
	}
	

    /*
     * TODO 此处可以利用工具类直接将form表单的内容生产一个bean,然后再做微调（后续重构，等前台传值方式重构后执行）
     * 重构插入报销 主表方法 by ahua
     * 
     * */
	@Override
        @Transactional(propagation = Propagation.REQUIRED) //为方便控制事务，使用统一实现类处理
        public Map<String, Object> insertFinanceMain(FinInsertParams insertParams)  {
	    
	    String formData = insertParams.getFormData();
            String submitType = insertParams.getSubmitType();
            String finNameEn = insertParams.getFinNameEn();
            String finTypeEn = insertParams.getFinTypeEn();
            
            logger.info("into FinanceMainServiceImpl insertFinanceMain start");
            
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String siteid = userInfoScope.getSiteId();
            FinanceMain financeMain = JsonHelper.fromJsonStringToBean( formData, FinanceMain.class );


            if(submitType.equals("save")) {
               String statusKey = FinStatusEnum.FIN_DRAFT.toString();
              // String statusName = iEnumManager.retrieveEnumeration("FIN_WORKFLOW_APPROVE_STATUS",statusKey).getLabel();
               AppEnum tempAppEnum = getAppEnumObj("FIN_WORKFLOW_APPROVE_STATUS",statusKey,siteid);
               
               String statusName = tempAppEnum.getLabel();
               financeMain.setStatus(statusKey); //草稿
               financeMain.setStatusName( statusName );
            } else if( submitType.equals("submit") ) {
                String statusKey = FinStatusEnum.FIN_FLOW_STR.toString();
                AppEnum tempAppEnum = getAppEnumObj("FIN_WORKFLOW_APPROVE_STATUS",statusKey,siteid);
//                List<AppEnum> tempList = iEnumManager.retrieveEnumeration("FIN_WORKFLOW_APPROVE_STATUS",statusKey);
               
                String statusName = tempAppEnum.getLabel();
                financeMain.setStatus(statusKey); //申请开始
                financeMain.setStatusName( statusName );
            }
            
            financeMain.setDepartmentid(userInfoScope.getOrgId());
            financeMain.setDepartment(userInfoScope.getOrgName());
            financeMain.setCreateid( userInfoScope.getUserId() );
            financeMain.setCreatedate(new Date());

           // AppEnum appEnum = iEnumManager.retrieveEnumeration("FIN_FLOW_TYPE",finNameEn);
            AppEnum appEnum = getAppEnumObj("FIN_FLOW_TYPE",finNameEn,siteid);
            
            String financeFlowNameCn = appEnum.getLabel();
            String financeFlowTypeCn = FinanceUtil.genFinTypeCn(finTypeEn) + "报销";
            financeMain.setFinance_flowid(finNameEn);
            financeMain.setFinance_typeid(finTypeEn);
            financeMain.setFinance_flow(financeFlowNameCn);
            financeMain.setFinance_type(financeFlowTypeCn);
            
            financeMain.setIs_show("Y"); //初始为显示报销单
            financeMain.setFlag_item("0"); //初始为"未生成ERP凭证"
            
            financeMain.setSiteid(userInfoScope.getSiteId());

            financeMainDao.insertFinanceMain(financeMain);//finance_flow_str

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("financeMain", financeMain);
            return map;
        }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-9-21
     * @param cat 枚举类型
     * @param statusKey 枚举值
     * @param siteid 站点
     * @return:
     */
    private AppEnum getAppEnumObj(String cat, String statusKey, String siteid) {
        List<AppEnum> tempList = iEnumManager.retrieveEnumeration( cat, statusKey );
        AppEnum tempAppEnum = null;
        for ( AppEnum temp : tempList ) {
            if ( siteid.equals( temp.getSiteId() ) ) {
                tempAppEnum = temp;
            }
        }
        if ( tempAppEnum == null ) {
            for ( AppEnum temp : tempList ) {
                if ( "NaN".equals( temp.getSiteId() ) ) {
                    tempAppEnum = temp;
                }
            }
        }
        return tempAppEnum;
    }

    /** 
	 * @description: 更新呢报销主表
	 * @author: 890170
	 * @createDate: 2015-3-30
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED) //为方便控制事务，使用统一实现类处理
	public FinanceMain updateFinanceMainByFid(FinanceMain fm) {
		financeMainDao.updateFinanceMainByFid(fm);
		return fm;
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED) 
    public void updateFinInfoByApplicant(FinInsertParams insertParams) {
        String formData = insertParams.getFormData();
        FinanceMain financeMain = JsonHelper.fromJsonStringToBean( formData, FinanceMain.class );
       //原信息
        FinanceMain financeMain2 = financeMainDao.queryFinanceMainByFid(financeMain.getFid());
        List<FinanceMainDetail> details = financeMainDetailService.queryFinanceMainDetailByFid( financeMain2.getFid() );
        insertParams.setBeneficiaryid( details.get( 0 ).getBeneficiaryid() );
        financeMain.setCreateid( financeMain2.getCreateid() );
        financeMain.setCreatorname( financeMain2.getCreatorname() );
        financeMain.setSiteid( financeMain2.getSiteid() );
        financeMain.setDeptid( financeMain2.getDeptid() );
        // 更新报销主表的报销层面
        updateFinanceMainByFid( financeMain );
        //TODO 修改待办里面的名称
//        FinanceFlowMatch financeFlowMatch = financeFlowMatchService.queryFinanceFlowMatchByFid( financeMain.getFid() );
//        if(financeFlowMatch!=null && StringUtils.isBlank( financeFlowMatch.getPid() )){
          homepageService.modify( null, financeMain.getFid(), null, financeMain.getFname(), null, null, null, null );
//        }
        //不删除，只能标识一下，防止从已办里面打开记录时报错
        financeMainDetailService.historyFinanceMainDetail(financeMain); //子表信息设置为历史记录
        financeMainDetailService.insertFinanceMainDetail(financeMain, insertParams); //插入报销明细表
        //附件更新
        financeAttachMatchService.deleteFinanceAttachMatch( financeMain.getFid() );
        financeAttachMatchService.insertFinanceAttachMatch(financeMain.getFid(),insertParams.getUploadIds());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) 
    public void updateFinAllowanceType(FinInsertParams insertParams) {
        //更新主表的报销金额
        String formData = insertParams.getFormData();
        FinanceMain financeMain = JsonHelper.fromJsonStringToBean( formData, FinanceMain.class );
        String fid = financeMain.getFid();
        financeMainDao.updateFinanceTotalAmount(fid,financeMain.getTotal_amount());
        
        String detail = insertParams.getDetail();  //报销明细
        JSONArray jsonArr = JSONArray.fromObject(detail);  //报销明细
        //更新明细
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject aDetailJson  = jsonArr.getJSONObject(i);//一条明细的json格式数据
            //id
            String finDetailId = aDetailJson.getString("id"); 
            //总额
            String amountString = aDetailJson.getString( "amount" );
            double amount = 0.0;
            if(!"".equals( amountString )){
                amount =  Double.parseDouble( amountString );
            }
            //报销标准
            String allowanceType = aDetailJson.getString("allowanceType"); 
            if("null".equals( allowanceType )){
                allowanceType = null;
            }
            //补贴金额
            String allowancecostString = aDetailJson.getString( "allowancecost" );
            double allowancecost = 0.0;
            if(!"".equals( allowancecostString )){
                allowancecost =  Double.parseDouble( allowancecostString );
            }
            
            financeMainDetailService.updateFinDetailAllowanceType( finDetailId, allowanceType,amount,allowancecost );
            
          
        }
        
    }

   
}
