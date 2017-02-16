package com.timss.finance.service.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceFlowMatch;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.service.FinanceAttachMatchService;
import com.timss.finance.service.FinanceFlowMatchService;
import com.timss.finance.service.FinanceMainDetailCostService;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceSummaryService;
import com.timss.finance.service.FlowService;
import com.timss.finance.util.FinStatusEnum;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.utils.WorkFlowConstants;

@Service
public class FinanceSummaryServiceImpl implements FinanceSummaryService {
	private Logger logger = Logger.getLogger(FinanceSummaryServiceImpl.class);

	private final String typeName = "财务报销";

	private final String viewBasePath = "finance/financeInfoController/viewFinanceInfo.do?";

	@Autowired
	FinanceMainService financeMainService;
	@Autowired
	FinanceFlowMatchService financeFlowMatchService;
	@Autowired
	FinanceMainDetailService financeMainDetailService;
	@Autowired
	FinanceMainDetailCostService financeMainDetailCostService;
	@Autowired
	FinanceAttachMatchService financeAttachMatchService;
	@Autowired
	FlowService flowService;
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
	WorkflowService wfs; // by type
	@Autowired
	IAuthorizationManager im;
	@Autowired
	HomepageService homepageService;

	@Override
	public void createDraft(String flow, String typeName, String name,
			String statusName, String url, UserInfo userInfo)  {
		
	}

	public void createProcess(String flow, String processInstId,
			String typeName, String name, String statusName,
			List<String> operUser, String url, UserInfo userInfo)
			 {
		
	}


	/** 
         * @description: 修改报销信息
         * @author: 890152
         * @createDate: 2015-1-5
         */
        @Override
        @Transactional(propagation = Propagation.REQUIRED)
        public Map<String, Object> updateFinanceByFid(FinInsertParams insertParams,String fid,String flowName)  {
                UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
                String uploadIds = insertParams.getUploadIds();
                String finNameEn = insertParams.getFinNameEn();
                String finTypeEn = insertParams.getFinTypeEn(); 
                String submitType = insertParams.getSubmitType();
                String beneficiaryid = insertParams.getBeneficiaryid();
                
                Map<String, Object> resultMap = new HashMap<String, Object>();
                
                List<FinanceMainDetail> mainDtlList = new LinkedList<FinanceMainDetail>();

                // 1.更新主表
                logger.info("更新主表");
                logger.info("fid==" + fid);
                financeMainService.updateFinanceMainByFid(insertParams,fid);

                // 2.更新明细表和明细费用表(先删后增)
                logger.info("更新子表");
                Map<String, Object> map = financeMainService.queryFinanceMainByFid(fid);
                FinanceMain financeMain = (FinanceMain) map.get("financeMain");
                //不删除，只能标识一下，防止从已办里面打开记录时报错
                //financeMainDetailCostService.deleteFinanceMainDetailCostByFid(financeMain.getFid()); //根据fid删除明细费用表
                financeMainDetailService.historyFinanceMainDetail(financeMain); //子表信息设置为历史记录
                
                mainDtlList = financeMainDetailService.insertFinanceMainDetail(financeMain, insertParams); //插入报销明细表
//                financeMainDetailCostService.insertFinanceMainDetailCost(mainDtlList, detail); //插入报销明细费用表
                
                financeAttachMatchService.updateFinanceAttachMatch(fid, uploadIds);//更新附件信息

                // 3.更新附件表--待补充
                logger.info("##获取明细表信息" + fid);
                List<FinanceMainDetail> fmdList = financeMainDetailService.queryFinanceMainDetailByFid(fid);
                
                //如果修改了报销人,则工作流需要修改(如果为草稿状态的单则不用修改)
                if( !financeMain.getStatus().equals(FinStatusEnum.FIN_DRAFT.toString()) ) { //如果不为草稿状态(退回到建单者时,才会到这个状态,即"申请开始"状态")
                        FinanceFlowMatch financeFlowMatch = financeFlowMatchService.queryFinanceFlowMatchByFid(financeMain.getFid());
                        String pid = financeFlowMatch.getPid();
                        if (financeFlowMatch != null) {
                                if( finTypeEn.equals("only") ) {
                                    double amount = financeMain.getTotal_amount(); //报销总金额
                                    String beneficiaryUserid = mainDtlList.get(0).getBeneficiaryid(); //实际报销人
                                    setFlowParamsOnOnly(fmdList,pid,amount,beneficiaryUserid);
                                } else if( finTypeEn.equals("other") ) {
                                    double amount = financeMain.getTotal_amount(); //报销总金额
                                    String beneficiaryUserid = mainDtlList.get(0).getBeneficiaryid(); //实际报销人
                                    setFlowParamsOnOther(beneficiaryid,fmdList,pid,amount,beneficiaryUserid);
                                } else if( finTypeEn.equals("more") ) {
                                    setFlowParamsOnMore(mainDtlList,pid,financeMain.getFname());
                                }
                        }
                } else { //如果报销单是草稿状态
                        if( submitType.equals("submit") ) { //如果是提交,则需要启动流程
                            //1.启动流程
                            Map<String, Object> flowMap = flowService.startWorkflow(financeMain,
                                            mainDtlList, finNameEn, finTypeEn, userInfoScope);
                            String pid = (String) flowMap.get("pid");
                            //2.存流程映射表(绑定报销编号和流程实例编号)
                            flowIdBindBusinessId(pid,financeMain.getFid());
                            //3 新增一条待办草稿
                            createHomepageRecord(financeMain,pid,typeName,viewBasePath,userInfoScope);
                            //4.返回值
                            resultMap.put("taskId", flowMap.get("taskId"));
                            resultMap.put("pid", pid);
                        } else if( submitType.equals("save") ) { //如果为草稿,需要重建草稿(为了便于草稿修改了报销单名称,而待办没有显示的情况)
                            //删掉启动流程的待办
                            homepageService.Delete(financeMain.getFid(), userInfoScope);
                            //新增一条待办草稿
                            createHomepageRecord(financeMain,financeMain.getFid(),typeName,viewBasePath,userInfoScope);
                        }
                        resultMap.put("fid", financeMain.getFid());
                }
                return resultMap;
        }
        
        /**
         * @description:多人报销流程参数设置
         * @author: 王中华
         * @createDate: 2015-8-22
         * @param mainDtlList
         * @param processId:
         */
        private void setFlowParamsOnMore(List<FinanceMainDetail> mainDtlList,String processId,String finName) {
            StringBuffer userIdsBuf = new StringBuffer();
            StringBuffer childBusinessIdsBuf = new StringBuffer();
            String userIds;
            String childBusinessIds;
            for (int i = 0; i < mainDtlList.size(); i++) {
                if(i != mainDtlList.size()-1) {
                        userIdsBuf.append(mainDtlList.get(i).getBeneficiaryid()).append(",");
                        childBusinessIdsBuf.append(mainDtlList.get(i).getId()).append(",");
                } else {
                        userIdsBuf.append(mainDtlList.get(i).getBeneficiaryid());
                        childBusinessIdsBuf.append(mainDtlList.get(i).getId());
                }
            }
            userIds = new String(userIdsBuf);
            childBusinessIds = new String(childBusinessIdsBuf);
            //设置用户编号
            wfs.setVariable( processId, WorkFlowConstants.SUB_PROCESS_USER_IDS,userIds );
            //设置子流程
            wfs.setVariable( processId, WorkFlowConstants.SUB_PROCESS_BUSINESS_IDS,childBusinessIds );
            //设置报销单名称
            wfs.setVariable( processId, WorkFlowConstants.BUSINESS_NAME,finName );
       }

    /**
     * @description:他人报销流程参数设置
     * @author: 王中华
     * @createDate: 2015-8-22
     * @param beneficiaryid
     * @param fmdList
     * @param processId
     * @param amount
     * @param beneficiaryUserid:
     */
    private void setFlowParamsOnOther(String beneficiaryid, List<FinanceMainDetail> fmdList, String processId, double amount,
            String beneficiaryUserid) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
	    if(beneficiaryid != null) {
                boolean checkIsLeader = false;
                String[] beneficiaryids = beneficiaryid.split("_");
                List<SecureUser> suList = im.retriveUsersWithSpecificGroup( siteid+"_INV_LEADER", null, true, true); //groupid,组织机构,是否包含下属机构，是否返回活动用户
                for(SecureUser su:suList){
                    if(su.getId().contains(fmdList.get(0).getBeneficiaryid())){
                       checkIsLeader = true;
                    }
                }
                wfs.setVariable(processId, "userId",beneficiaryids[0]);
                wfs.setVariable(processId, "amount", amount);
                wfs.setVariable(processId, "isLeader", checkIsLeader);
                wfs.setVariable(processId, "beneficiaryid",beneficiaryUserid); //保存实际报销人
                wfs.setVariable(processId, "finType", "other"); //保存报销类型
        }
        
    }

    /**
	 * @description:自己报销的流程参数设置
	 * @author: 王中华
	 * @createDate: 2015-8-22
	 * @param fmdList
	 * @param processId
	 * @param financeMain
	 * @param finTypeEn:
	 */
	private void setFlowParamsOnOnly(List<FinanceMainDetail> fmdList, String processId, double amount,String beneficiaryid ) {
	    boolean checkIsLeader = false;
	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	    String siteid = userInfoScope.getSiteId();
            //TODO 查找公司领导的组时，为了支持多站点，需要将站点变成一个动态的（最好不要用其他模块的角色或者用户组，防止其他站点不上你依赖的那么模块）
            List<SecureUser> suList = im.retriveUsersWithSpecificGroup( siteid+"_INV_LEADER", null, true, true); //groupid,组织机构,是否包含下属机构，是否返回活动用户
            for(SecureUser su:suList) {
                //TODO 判断报销人是否是公司领导，只需要通过工号判断就够了吧
                if(su.getId().contains(fmdList.get(0).getBeneficiaryid())){
                        checkIsLeader = true;
                }
            } 
            wfs.setVariable(processId, "amount", amount);
            wfs.setVariable(processId, "isLeader", checkIsLeader);
            wfs.setVariable(processId, "beneficiaryid",beneficiaryid); //保存实际报销人
            wfs.setVariable(processId, "finType", "only"); //保存报销类型
        }

  

	
        @Override
        @Transactional(propagation = Propagation.REQUIRED)
        public Map<String, Object> insertFinanceInfo(FinInsertParams insertParams)  {
//            String detail = insertParams.getDetail();
            String uploadIds = insertParams.getUploadIds();
            String finNameEn = insertParams.getFinNameEn();
            String finTypeEn = insertParams.getFinTypeEn(); 
            String submitType = insertParams.getSubmitType();
                
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            Map<String, Object> resultMap = new HashMap<String, Object>();

            Map<String, Object> mainMap = financeMainService.insertFinanceMain(insertParams);
            FinanceMain getMain = (FinanceMain) mainMap.get("financeMain");
         
            List<FinanceMainDetail> mainDtlList = financeMainDetailService
                    .insertFinanceMainDetail(getMain, insertParams);

            //3.存明细费用表信息
           // financeMainDetailCostService.insertFinanceMainDetailCost(mainDtlList,detail);

            //4.存附件表
            financeAttachMatchService.insertFinanceAttachMatch(getMain.getFid(),uploadIds);

            //5.流程相关操作
            if(submitType.equals("submit")) { //提交报销
                //5.1.启动流程
                Map<String, Object> flowMap = flowService.startWorkflow(getMain,
                                mainDtlList, finNameEn, finTypeEn, userInfoScope);
                String pid = (String) flowMap.get("pid");

                //5.2.存流程映射表(绑定报销编号和流程实例编号)
                flowIdBindBusinessId(pid,getMain.getFid());
                //5.3.新增一条待办草稿
                createHomepageRecord(getMain,pid,typeName,viewBasePath,userInfoScope);
                
                resultMap.put("taskId", flowMap.get("taskId"));
                resultMap.put("pid", pid);
            } else if(submitType.equals("save")) {
              //新增一条待办草稿
                createHomepageRecord(getMain,getMain.getFid(),typeName,viewBasePath,userInfoScope);
            }

            resultMap.put("financeMain", getMain);
            resultMap.put("fid", getMain.getFid());

            return resultMap;
    }
    
    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param pid
     * @param fid:
     */
    private void flowIdBindBusinessId(String pid, String fid) {
        FinanceFlowMatch financeFlowMatch = new FinanceFlowMatch();
        financeFlowMatch.setPid(pid);// 流程实例id
        financeFlowMatch.setFid(fid);// 业务id
        financeFlowMatchService.insertFinanceFlowMatch(financeFlowMatch);
    }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param getMain 业务bean
     * @param pid 启动的流程实例id
     * @param typeName2 类别名称（属于哪个模块的待办）
     * @param viewBasePath2 （双击调整的.do）
     * @param userInfoScope:
     */
    private void createHomepageRecord(FinanceMain getMain, String pid, String typeName2, String viewBasePath2,
            UserInfoScope userInfoScope) {
        
        HomepageWorkTask hwt = new HomepageWorkTask();
        hwt.setFlow(getMain.getFid()); //报销单号
        hwt.setProcessInstId(pid); //流程实例编号
        hwt.setTypeName(typeName2); //类别名称
        hwt.setName(getMain.getFname()); //任务名称
        hwt.setStatusName("草稿"); //状态名称
        hwt.setUrl(viewBasePath2 + "businessId=" + getMain.getFid()); //跳转地址
        
        homepageService.create(hwt, userInfoScope);
    }

    
}
