package com.timss.finance.web;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceFlowMatch;
import com.timss.finance.bean.FinanceGeneralLedgerInfo;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinancePageConfig;
import com.timss.finance.service.FinanceAttachMatchService;
import com.timss.finance.service.FinanceFlowMatchService;
import com.timss.finance.service.FinanceGeneralLedgerInfoService;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinancePageConfigService;
import com.timss.finance.service.FinanceSummaryService;
import com.timss.finance.service.FlowService;
import com.timss.finance.service.SubFlowService;
import com.timss.finance.util.FinStatusEnum;
import com.timss.finance.util.FinanceUtil;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.manager.support.IConfigurationManager;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

@Controller
@RequestMapping(value = "finance/financeInfoController")
public class FinanceInfoController {
    private Logger logger = Logger.getLogger( FinanceInfoController.class );

    @Autowired
    private WorkflowService wfs;
    @Autowired
    private IEnumerationManager iEnumManager;
    @Autowired
    private FinanceMainService financeMainService;
    
    @Autowired
    FinanceFlowMatchService financeFlowMatchService;
    @Autowired
    FinanceAttachMatchService financeAttachMatchService;

    @Autowired
    FlowService flowService;

    @Autowired
    SubFlowService subFlowService;

    @Autowired
    FinanceMainDetailService financeMainDetailService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    FinanceSummaryService financeSummaryService;
    @Autowired
    FinancePageConfigService finPageConfService;
    @Autowired
    FinanceGeneralLedgerInfoService financeGeneralLedgerInfoService;

    @Autowired
    IConfigurationManager iConfigurationManager;

    @RequestMapping(value = "/findFinanceInfoList", method = RequestMethod.GET)
    @ReturnEnumsBind("FIN_FLOW_TYPE,FIN_REIMBURSE_TYPE")
    public String findFinanceInfoList() throws Exception {
        return "/financeInfo/financeList.jsp";
    }

    @RequestMapping(value = "/financeListData", method = RequestMethod.POST)
    public Page<FinanceMain> financeListData(String search) throws Exception {
        logger.info( "####in ajax function financeListDate" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<FinanceMain> page = userInfoScope.getPage();
        page = financeMainService.queryFinanceMainList( page, userInfoScope, search );
        return page;
    }
    
    
    @RequestMapping(value = "/queryListByApplyId", method = RequestMethod.POST)
    public Map<String, Object> queryListByApplyId(String applyId) throws Exception {
        logger.info( "####in ajax function queryListByApplyId" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        Map<String, Object> restultMap = new HashMap<String, Object>();
        List<FinanceMain> result = financeMainService.queryFinanceMainListByApplyId( applyId ,siteid );
        restultMap.put( "rows", result );
        restultMap.put( "total", result.size() );
        return restultMap;
    }
    /**
     * @description: 新建财务报销
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param model
     * @param page
     * @param finNameEn
     * @param finTypeEn
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/createPage", method = RequestMethod.GET)
    @ReturnEnumsBind("FIN_REIMBURSE_TYPE,FIN_ALLOWANCE_TYPE")
    public ModelAndView createPage(ModelMap model, String page, String finNameEn, String finTypeEn) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        FinanceUtil myutil = new FinanceUtil();
        
//        AppEnum appEnum = iEnumManager.retrieveEnumeration( "FIN_FLOW_TYPE", finNameEn );
        AppEnum appEnum = getAppEnumObj("FIN_FLOW_TYPE", finNameEn,siteid);
        String finNameCn = appEnum.getLabel();
        String finTypeCn = FinanceUtil.genFinTypeCn( finTypeEn );
        ModelAndView modelandview = myutil.goToPage( userInfoScope, model, page, finNameEn, finTypeEn, finNameCn,
                finTypeCn );
        
        // 获取页面的配置参数 by ahua
        List<FinancePageConfig> pageConfData = finPageConfService.getFinPageConfByFlowType( finNameEn, siteid );
        modelandview.addObject( "pageConfData", JsonHelper.toJsonString( pageConfData ) );

        Configuration configuration = iConfigurationManager.query( "allowancePerDay", "NaN", "NaN" );
        modelandview.addObject( "allowancePerDay", configuration.getVal() );
        configuration = iConfigurationManager.query( "businessAllowancePerDay", "NaN", "NaN" );
        modelandview.addObject( "businessAllowancePerDay", configuration.getVal() );
        
        configuration = iConfigurationManager.query( "trainAllowancePerDay", "NaN", "NaN" );
        modelandview.addObject( "trainAllowancePerDay", configuration.getVal() );
        return modelandview;

    }

    @RequestMapping(value = "/showInDlgPage", method = RequestMethod.GET)
    public ModelAndView showInDlgPage(String page) throws Exception {
        page = "/financeInputForm/" + page + ".jsp";
        return new ModelAndView( page );
    }

    @RequestMapping(value = "/showInDetailDlgPage")
    public Map<String, Object> showInDetailDlgPage(String page, String strDate, String endDate, String amount,
            String description) throws Exception {
        page = "/financeInputForm/" + page + ".jsp";
        String url = page;
        HashMap<String, Object> map = new HashMap<String, Object>();
        logger.info( "事由==" + description );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String TestDesc = userInfoScope.getParam( "description" );
        logger.info( "TestDesc#######" + TestDesc );
        map.put( "page", url );
        map.put( "strDate", strDate );
        map.put( "endDate", endDate ); 
        map.put( "amount", amount );
        map.put( "description", description );
        map.put( "edit", "edit" );
        return map;
    }

    @RequestMapping(value = "/deleteFinanceMainAndDetail")
    public Map<String, Object> deleteFinanceMainAndDetail(String financeType, String formData, String detail,
            String submitType) throws Exception {
        return null;
    }

    /**
     * @description:待办或草稿打开页面
     * @author: 王中华
     * @createDate: 2015-8-22
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/viewFinanceInfo")
    @ReturnEnumsBind("FIN_REIMBURSE_TYPE,FIN_ALLOWANCE_TYPE")
    public ModelAndView viewFinanceInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();

        // businessId,taskId,processInstId由多人报销时的子流程给予,其他参数需自己传入
        String businessId = userInfoScope.getParam( "businessId" ); // 获取业务ID.如果是父流程(或没有子流程),则对应报销ID;如果是子流程,则对应明细ID.

        boolean subFlowFlag = false; // false-不在子流程里,true-在子流程里
        String isSubProcess = userInfoScope.getParam( "isSubProcess" ); // 子流程标记
        if ( isSubProcess != null && isSubProcess.equals( "1" ) ) {
            subFlowFlag = true;
        }

        // 识别真正的父业务ID
        Map<String, String> businessIdMap = getRealFid( userInfoScope, businessId, subFlowFlag );
        String childBusinessId = businessIdMap.get( "childBusinessId" ); // 子流程业务编号
        String parentBusinessId = businessIdMap.get( "parentBusinessId" ); // 父流程业务编号

        // 以报销编号查询报销信息
        Map<String, Object> map = financeMainService.queryFinanceMainByFid( parentBusinessId );
        FinanceMain fm = (FinanceMain) map.get( "financeMain" );
        String status = fm.getStatus();
        // 计算报销单的总金额
        caculateAmount( fm );

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        map.put( "createdate", sdf.format( fm.getCreatedate() ) );
        String url = "/financeDetail/financeNew.jsp";
        if(status!=null && !"finance_draft".equals( status )){
            url = "/financeDetail/financeInfo.jsp";
        }

        String finNameEn = fm.getFinance_flowid();
        String finTypeEn = fm.getFinance_typeid();
        // 获取页面的配置参数 by ahua
        List<FinancePageConfig> pageConfData = finPageConfService.getFinPageConfByFlowType( finNameEn, siteid );
        map.put( "pageConfData", JsonHelper.toJsonString( pageConfData ) );
        map.put( "description", fm.getDescription() );
        map.put( "finTypeEn", finTypeEn );
        map.put( "finNameEn", finNameEn );
        map.put( "finTypeCn", fm.getFinance_type() );
        map.put( "finNameCn", fm.getFinance_flow() );
        map.put( "oprModeEn", "edit" );
        map.put( "oprModeCn", "编辑" );
        //登录用户所拥有与的角色
        String roles = getLoginUserRoles(userInfoScope);
        map.put("loginRoleIds", roles);
        
        // 以报销编号查询报销明细信息(因对于同一张报销单,有些值相同的字段记录到明细表中,故需到明细表查)
        if ( finTypeEn.equals( "other" ) || finTypeEn.equals( "more" ) ) {
            List<FinanceMainDetail> list = financeMainDetailService.queryFinanceMainDetailByFid( parentBusinessId );
            // 获取一行数据以显示在form中
            if ( list.size() > 0 ) {
                FinanceMainDetail financeMainDetail = list.get( 0 );
                if ( finTypeEn.equals( "other" ) ) {
                    map.put( "beneficiary", financeMainDetail.getBeneficiary() );
                    map.put( "beneficiaryid", financeMainDetail.getBeneficiaryid() );
                } else if ( finTypeEn.equals( "more" ) ) {
                    if(StringUtils.isBlank( fm.getDescription() )){  //对应一些报销事由不在主单里面的情况
                        map.put( "description", financeMainDetail.getDescription() ); 
                    }
                    map.put( "join_boss", financeMainDetail.getJoin_boss() );
                    map.put( "join_bossid", financeMainDetail.getJoin_bossid() );
                    map.put( "join_nbr", financeMainDetail.getJoin_nbr() );
                    map.put( "strdate", financeMainDetail.getStrdate()==null?null:financeMainDetail.getStrdate().getTime() );
                    map.put( "enddate", financeMainDetail.getEnddate()==null?null: financeMainDetail.getEnddate().getTime() );
                }
            }
        }

        // 如果报销类型为多人报销,且在子流程中时,需要以明细id搜索
        FinanceFlowMatch ffm = new FinanceFlowMatch();
        if ( subFlowFlag ) {
            ffm = financeFlowMatchService.queryFinanceFlowMatchByFid( childBusinessId );
        } else {
            ffm = financeFlowMatchService.queryFinanceFlowMatchByFid( parentBusinessId );
        }

        List<Map<String, Object>> fileMap = financeAttachMatchService.queryFinanceAttachMatchByFid( parentBusinessId );
        JSONArray jsonArray = JSONArray.fromObject( fileMap );
        map.put( "uploadFiles", jsonArray );
        logger.info( "附件上传相关=====" + jsonArray );

        if ( ffm != null ) {// 流程已经开启的情况下
            String pid = ffm.getPid();
            map.put( "pid", pid );
            map.put( "status", fm.getStatus() );

            // 如果状态为"流程结束"则直接返回"非审批者"
            if ( fm.getStatus().equals( FinStatusEnum.FIN_FLWO_END.toString() ) ) { // 流程结束
                map.put( "checkApprover", "others" );
            } else { // 流程结束后就不能进入这里了,因为已查不到taskId
                String taskId = financeMainService.queryTaskIdByPid( pid );
                String checkApprover = "others"; // 默认设定为非审批者
                if ( taskId != null ) {
                    map.put( "taskId", taskId );
                    checkApprover = flowService.getCandidateUsers( taskId, userInfoScope.getUserId() );

                    Map<String, String> processAttr = wfs.getElementInfo( taskId );
                    map.put( "curTaskInputInfo", processAttr.get( "modifiable" ) ); // 当前流程节点输入信息
                }

                map.put( "checkApprover", checkApprover );
            }
        }

        // 附加补贴费计算规则 TODO 不理解怎么计算的
        Configuration configuration = iConfigurationManager.query( "allowancePerDay", "NaN", "NaN" );
        map.put( "allowancePerDay", configuration.getVal() );
        configuration = iConfigurationManager.query( "businessAllowancePerDay", "NaN", "NaN" );
        map.put( "businessAllowancePerDay", configuration.getVal() );

        configuration = iConfigurationManager.query( "trainAllowancePerDay", "NaN", "NaN" );
        map.put( "trainAllowancePerDay", configuration.getVal() );

        return new ModelAndView( url, map );
    }

    private String getLoginUserRoles(UserInfoScope userInfoScope) {
        String roleString = "";
        List<Role> roleList = userInfoScope.getRoles();
        for(Role temp : roleList){
            roleString += ","+  temp.getId() ;
        }
        return roleString.substring( 1 );
    }

    /**
     * @description:计算报销单的总金额，为各个报销明细的金额的总和
     * @author: 王中华
     * @createDate: 2015-9-11
     * @param fm
     * @throws Exception:
     */
    private void caculateAmount(FinanceMain fm) throws Exception {
        List<FinanceMainDetailCostVo> detailCostPage = viewFinanceDetail( fm.getFid() ).getResults();
        double amount = 0;
        for ( FinanceMainDetailCostVo temp : detailCostPage ) {
            amount = amount + temp.getAmount();
        }
        fm.setTotal_amount( amount );
    }

    /**
     * @description: 获取真正的业务ID
     * @author: 王中华
     * @createDate: 2015-9-10
     * @param userInfoScope
     * @param businessId
     * @param subFlowFlag
     * @return
     * @throws Exception:
     */
    private Map<String, String> getRealFid(UserInfoScope userInfoScope, String businessId, boolean subFlowFlag)
            throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        String parentBusinessId = null;
        String childBusinessId = null;

        if ( subFlowFlag ) { // 在多人报销的子流程里
            // 在子流程中时,businessId为明细ID,需要转换成FID
            childBusinessId = businessId;
            FinanceMainDetail finMainDetail = financeMainDetailService.queryFinanceMainDetailById( childBusinessId );
            parentBusinessId = finMainDetail.getFid();
        } else {
            parentBusinessId = businessId;
        }
        result.put( "parentBusinessId", parentBusinessId );
        result.put( "childBusinessId", childBusinessId );

        return result;
    }

    @RequestMapping(value = "/viewFinanceDetail")
    public Page<FinanceMainDetailCostVo> viewFinanceDetail(String fid) throws Exception {
        logger.info( "###########查询报销明细列表数据 ： FID==" + fid );
        UserInfoScope userInfoScope;
        userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<FinanceMainDetailCostVo> page = userInfoScope.getPage();
        int pageSize = page.getPageSize();
        page.setPageSize( 100 );
        financeMainDetailService.queryFinanceMainDetailList( page, userInfoScope, fid );
        page.setPageSize( pageSize );
        return page;
    }

    @RequestMapping(value = "/editFinanceInfo")
    @ValidFormToken
    public Map<String, Object> editFinanceInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String formData = userInfoScope.getParam( "formData" );
        String detail = userInfoScope.getParam( "detail" );
        String finNameEn = userInfoScope.getParam( "finNameEn" );
        String finTypeEn = userInfoScope.getParam( "finTypeEn" );
        String beneficiaryid = userInfoScope.getParam( "beneficiaryid" );
        String fid = userInfoScope.getParam( "fid" );
        String submitType = userInfoScope.getParam( "submitType" );
        String flowName = userInfoScope.getParam( "flowName" );

        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号
        logger.info( "*formData: " + formData );
        logger.info( "*detail: " + detail );
        logger.info( "*finNameEn: " + finNameEn );
        logger.info( "*finTypeEn: " + finTypeEn );
        logger.info( "*beneficiaryid: " + beneficiaryid );
        logger.info( "*fid: " + fid );
        logger.info( "*submitType: " + submitType );
        logger.info( "*flowName: " + flowName );

        JSONArray jsonArr = JSONArray.fromObject( detail );

        logger.info( "*jsonArr: " + jsonArr );

        logger.info( "###before updateFinanceByFid" );

        FinInsertParams insertParams = new FinInsertParams();
        insertParams.setSubmitType( submitType );
        insertParams.setFormData( formData );
        insertParams.setDetail( detail );
        insertParams.setFinNameEn( finNameEn );
        insertParams.setFinTypeEn( finTypeEn );
        insertParams.setBeneficiaryid( beneficiaryid );
        insertParams.setUploadIds( uploadIds );

        Map<String, Object> map = financeSummaryService.updateFinanceByFid( insertParams, fid, flowName );

        map.put( "result", "success" );

        return map;
    }

    /**
     * @description: 删除指定的报销信息
     * @author: 890170
     * @createDate: 2014-12-23
     */
    @RequestMapping(value = "/deleteFinanceInfo")
    public Map<String, Object> deleteFinanceInfo(String financeType, String formData, String detail, String submitType,
            String fid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String result = financeMainService.deleteAndUpdateFinanceMain( fid );
        map.put( "result", result );
        return map;
    }

    /**
     * @description: 插入报销信息
     * @author: 890170
     * @createDate: 2014-12-29
     */
    @RequestMapping(value = "/insertFinanceInfo")
    public Map<String, Object> insertFinanceInfo() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String formData = userInfoScope.getParam( "formData" ); // 报销表单数据
        String detail = userInfoScope.getParam( "detail" ); // 报销明细数据
        String submitType = userInfoScope.getParam( "submitType" ); // 提交方式,"暂存/提交"
        String finNameEn = userInfoScope.getParam( "finNameEn" ); // 报销名称,如"travelcost"
        String finTypeEn = userInfoScope.getParam( "finTypeEn" ); // 报销名称,如"only"
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号
        String beneficiaryid = userInfoScope.getParam( "beneficiaryid" ); // 报销人信息,目前只有"他人报销"有值

        Map<String, Object> resultMap = new HashMap<String, Object>();

        FinInsertParams insertParams = new FinInsertParams();
        insertParams.setFormData( formData );
        insertParams.setBeneficiaryid( beneficiaryid );
        insertParams.setDetail( detail );
        insertParams.setUploadIds( uploadIds );
        insertParams.setFinNameEn( finNameEn );
        insertParams.setFinTypeEn( finTypeEn );
        insertParams.setSubmitType( submitType );
        // 对于各种报销类型,走同一个service
        resultMap = financeSummaryService.insertFinanceInfo( insertParams );

        // 返回
        String taskId = (String) resultMap.get( "taskId" );
        String fid = (String) resultMap.get( "fid" );

        HashMap<String, Object> finInfoMap = new HashMap<String, Object>();
        finInfoMap.put( "result", "success" );
        finInfoMap.put( "taskId", taskId );
        finInfoMap.put( "fid", fid );

        return finInfoMap;
    }

    // 设置子流程变量
    @RequestMapping(value = "/setSubFlowVariables")
    public Map<String, Object> setSubFlowVariables() throws Exception {
        HashMap<String, Object> setVarMap = new HashMap<String, Object>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String pid = userInfoScope.getParam( "processInstId" ); // 流程实例ID
        subFlowService.setSubFlowVariables( pid );

        setVarMap.put( "result", "success" );
        return setVarMap;
    }

    /**
     * 终止流程
     * 
     * @param processInstId 流程实例id
     * @param reason 中止原因
     * @param businessId 业务主键
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/stopWorkFlow")
    public Map<String, Object> stopWorkFlow(@RequestParam("taskId") String taskId,
            @RequestParam("assignee") String assignee, @RequestParam("owner") String owner,
            @RequestParam("message") String message, @RequestParam("businessId") String businessId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        flowService.stopWorkFlow( taskId, assignee, owner, message, businessId );

        map.put( "result", "success" );

        return map;
    }

    /**
     * @Description: 提交审批流程信息
     * @author: 890170
     * @createDate: 2014-11-6
     */
    @RequestMapping(value = "/submitWorkFlowInfo")
    public Map<String, Object> submitWorkFlowInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String formData = userInfoScope.getParam( "formData" ); // 报销表单数据
        String detail = userInfoScope.getParam( "detail" );  //明细列表
        String pid = userInfoScope.getParam( "pid" ); // 流程实例id
        String fid = userInfoScope.getParam( "fid" ); // 报销编号
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号
        String accType;

        Map<String, Object> financeMainMap;
        financeMainMap = financeMainService.queryFinanceMainByFid( fid );
        FinanceMain fm = (FinanceMain) financeMainMap.get( "financeMain" );

        String financeFlowId = fm.getFinance_flowid();
        String status = fm.getStatus();
        String financeTypeId = fm.getFinance_typeid();
        
        if(status.equals( FinStatusEnum.MAIN_ACCOUNT_APPROVE.toString() )){
            //如果是主办会计复核环节，添加流程流转参数
            String needModifyParam = FinanceUtil.getJsonFieldString( formData, "needModify" );
            if(! "".equals( needModifyParam )){
                wfs.setVariable( pid, "needModify", needModifyParam );
            }
        }else if(status.equals( FinStatusEnum.FIN_APPLICANT_MODIFY.toString() )){
            FinInsertParams insertParams = new FinInsertParams();
            insertParams.setFormData( formData );
            insertParams.setDetail( detail );
            insertParams.setUploadIds( uploadIds );
            //申请人修改环节，修改业务数据
            financeMainService.updateFinInfoByApplicant(insertParams);
        }else if((status.equals( FinStatusEnum.WAIT_JYDEPTMGR_APPROVE.toString() ) ||
                status.equals( FinStatusEnum.WAIT_SUBMAINMGR_SPPROVE.toString() )) &&
                financeFlowId.equals( "travelcost" )){
            FinInsertParams insertParams = new FinInsertParams();
            insertParams.setFormData( formData );
            insertParams.setDetail( detail );
            //更新金额和报销标准
            financeMainService.updateFinAllowanceType(insertParams);
        }
        
        boolean tempflag = financeFlowId.equals( "businessentertainment" )
                && (financeTypeId.equals( "only" ) || financeTypeId.equals( "other" ))
                && status.equals( FinStatusEnum.WAIT_DEPTMGR_APPROVE.toString() );
        if ( tempflag ) { // 如果是业务招待费自己/他人报销且在部门经理审批环节,才需要设置记账类型
            accType = FinanceUtil.getJsonFieldString( formData, "accType" );
            if (! "".equals( accType )) {
                // 保存工作流流程属性
                wfs.setVariable( pid, "accType", accType );
                if ( accType.equals( "department" ) ) {
                    fm.setFin_level( "1" );
                } else if ( accType.equals( "company" ) ) {
                    fm.setFin_level( "2" );
                }
                // 更新报销主表的报销层面
                financeMainService.updateFinanceMainByFid( fm );
            }
        }

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "result", "success" );

        return resultMap;
    }

    @RequestMapping(value = "/queryFinanceGeneralLedgerInfoListByFid2")
    public Map<String, Object> queryFinanceGeneralLedgerInfoListByFid2() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String getReimbursementMan = userInfoScope.getParam( "getReimbursementMan" ); // 实际取报销款人
        String fid = userInfoScope.getParam( "fid" ); // 报销编号

        // 根据规则抓取科目关联记录,查询条件为报销类型和科目序号
        List<FinanceGeneralLedgerInfo> financeGeneralLedgerInfoList = financeGeneralLedgerInfoService
                .getFinanceGeneralLedgerInfoList( fid, getReimbursementMan );

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put( "rows", financeGeneralLedgerInfoList );
        resultMap.put( "total", financeGeneralLedgerInfoList.size() );

        logger.info( "===financeGeneralLedgerInfoList===: " + financeGeneralLedgerInfoList );

        return resultMap;
    }

    /**
     * @description: 推送ERP数据
     * @author: 890170
     * @createDate: 2014-12-2
     */
    @RequestMapping(value = "/putGeneralLedgerInfo")
    public Map<String, Object> putGeneralLedgerInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String erpForm = userInfoScope.getParam( "erpForm" ); // ERP表单数据
        String erpDetail = userInfoScope.getParam( "erpDetail" ); // ERP明细数据

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = financeGeneralLedgerInfoService.putGeneralLedgerInfo( erpForm, erpDetail );

        resultMap.put( "result", result );

        return resultMap;
    }

    /**
     * @description: 回退工作流操作
     * @author: 890170
     * @createDate: 2015-1-5
     */
    @RequestMapping(value = "/rollbackWorkFlowOpr")
    public Map<String, Object> rollbackWorkFlowOpr() throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String fid = userInfoScope.getParam( "fid" );
        String status = userInfoScope.getParam( "status" );

        if ( !status.equals( FinStatusEnum.FIN_FLOW_STR.toString() ) ) { // 如果不是"申请开始",即为回退的单,才需要回滚,否则不用做回滚
            String result = flowService.rollbackWorkFlowOpr( fid );

            resultMap.put( "result", result );
        } else {
            resultMap.put( "result", "success" );
        }

        return resultMap;
    }

    /**
     * @description: 废除报销单
     * @author: 890170
     * @createDate: 2015-1-7
     */
    @RequestMapping(value = "/abolishFinanceInfo")
    public Map<String, Object> abolishFinanceInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String fid = userInfoScope.getParam( "fid" );

        Map<String, Object> resultMap = flowService.abolishFinanceInfo( fid );

        return resultMap;
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
    
}
