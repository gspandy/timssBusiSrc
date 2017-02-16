package com.timss.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.utils.GetAcceptDataInWorkflowUtil;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatAcceptDtlVo;
import com.timss.inventory.vo.InvMatAcceptVo;
import com.timss.inventory.vo.MTPurOrderVO;
import com.timss.inventory.workflow.IsAcceptToRK;
import com.timss.inventory.workflow.IsAcceptToZJYS;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptController.java
 * @author: 890145
 * @createDate: 2015-10-30
 * @updateUser: 890145
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmataccept")
public class InvMatAcceptController {

    /**
	 * 
	 */
    private static final String INV_MAT_ACCEPT_DETAILS_PARAMNAME = "invMatAcceptDetails";
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private InvMatAcceptService invMatAcceptService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    InvPubInterface invPubInterface;
    @Autowired
    IsAcceptToRK isAcceptToRK;
    @Autowired
    IsAcceptToZJYS isAcceptToZJYS;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    GetAcceptDataInWorkflowUtil getAcceptDataInWorkflowUtil;

    private static final String INV_MAT_ACCEPT_PARAMNAME = "invMatAccept";

    private static final Logger logger = Logger.getLogger( InvMatAcceptController.class );

    /**
     * 物资验收列表页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invMatAcceptListJsp", method = RequestMethod.GET)
    @ReturnEnumsBind("ACPT_STATUS,ACPT_CNLUS")
    public String invBinList() {
        return "/invmataccept/invMatAcceptList.jsp";
    }

    /**
     * @description:采购单编号页面跳转
     * @author: 890166
     * @createDate: 2014-7-23
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/purOrderNoList", method = RequestMethod.GET)
    public String purOrderNoList() {
        return "/invmataccept/purOrderNoList.jsp";
    }

    /**
     * 物资验收列表页面跳转
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invMatAcceptDetailListJsp", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_APPLY_TYPE,INV_SPMATERIAL,ACPT_SPECIAL_MATERIALS,ACPT_EXTERIOR,"
            + "ACPT_TECH_SPECIFICATIONS,ACPT_TECH_INFORMATION,ACPT_HSE_INDICATORS,ACPT_KS_MATERIAL,"
            + "ACPT_CNLUS,ACPT_PROC_RULT,ACPT_PROBLEMS,ACPT_STATUS")
    public ModelAndView invMatAcceptDetailListJsp(String poNo) throws Exception {
        ModelAndView modelAndView = new ModelAndView( "/invmataccept/invMatAcceptDetailList.jsp" );
        UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
        String poId = purOrderService.querySheetIdByFlowNo( poNo, userInfo.getSiteId() );

        List<InvMatAcceptDetailVO> invMatAcceptDetails = invPubInterface.queryItem2InvMatAcceptDetail( poId );
        modelAndView.addObject( "invMatAcceptDetails", JsonHelper.toJsonString( invMatAcceptDetails ) );
        modelAndView.addObject( "poNo", poNo );
        return modelAndView;
    }

    /**
     * 物资验收详情页面跳转
     * 
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
    @RequestMapping(value = "/invMatAcceptFormJsp", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_APPLY_TYPE,INV_SPMATERIAL,ACPT_SPECIAL_MATERIALS,ACPT_EXTERIOR,"
            + "ACPT_TECH_SPECIFICATIONS,ACPT_TECH_INFORMATION,ACPT_HSE_INDICATORS,ACPT_KS_MATERIAL,"
            + "ACPT_CNLUS,ACPT_PROC_RULT,ACPT_PROBLEMS,ACPT_STATUS,ITEMAPPLY_TYPE,ACPT_TYPE,ITEMORDER_TYPE")
    public ModelAndView invMatAcceptForm(String inacId, String poId) throws NumberFormatException, Exception {
        Map<String, Object> maps = new HashMap<String, Object>();
        boolean isEdit = false;
        InvMatAcceptDtlVo invMatAcceptDtlVo = new InvMatAcceptDtlVo();
        // 判断申请id是否存在
        if ( StringUtils.isNotBlank( inacId ) ) {

            invMatAcceptDtlVo = invMatAcceptService.queryInvMatAcceptById( inacId );
            // 查询出合同的信息，需要在前台显示合同的采购类型、名称
            List<PurOrderVO> purOrderVOs = purOrderService.queryPurOrderInfoBySheetId( invMatAcceptDtlVo.getPoId() );
            if ( purOrderVOs != null & !purOrderVOs.isEmpty() ) {
                invMatAcceptDtlVo.setSheetName( purOrderVOs.get( 0 ).getSheetname() );
                invMatAcceptDtlVo.setSheetType( purOrderVOs.get( 0 ).getSheetIType() );
            }

            maps.put( "invMatAcceptDetails", invMatAcceptDtlVo.getInvMatAcceptDetails() );
            // 获取流程信息
            String processId = invMatAcceptDtlVo.getInstanceid();
            if ( StringUtils.isNotBlank( processId ) ) {
                Map<String, Object> flowMap = invMatAcceptService.queryWorkflowMap( inacId, processId );
                maps.put( "flow", flowMap );

                // 20151127 JIRA ==> TIM221
                List<Task> activities = workflowService.getActiveTasks( processId );
                if ( null != activities && !activities.isEmpty() ) {
                    Task task = activities.get( 0 );
                    String curProcess = task.getTaskDefinitionKey();
                    maps.put( "curProcess", curProcess );
                }
            }
            maps.put( "curUserId", invMatAcceptDtlVo.getCreateuser() );
            maps.put( "inacId", inacId );
            maps.put( "status", invMatAcceptDtlVo.getStatus() );
            isEdit = true;
        }
        // 判断采购合同id是否存在
        if ( StringUtils.isNotBlank( poId ) ) {
            List<PurOrderVO> purOrderVOs = purOrderService.queryPurOrderInfoBySheetId( poId );
            if ( purOrderVOs != null & !purOrderVOs.isEmpty() ) {
                maps.put( "purOrder", purOrderVOs.get( 0 ) );
                List<InvMatAcceptDetailVO> invMatAcceptDetails = invPubInterface.queryItem2InvMatAcceptDetail( poId );
                maps.put( "invMatAcceptDetails", invMatAcceptDetails );
            }
            maps.put( "poId", poId );
        }
        //即收即发字段，即使果系统配置开关关了， 页面的那个“即收即发”字段依然显示
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        if( "SWF".equals(userInfoScope.getSiteId()) ){
        	//验收结束后，auto_delivery字段如果有值了，就以存储的值为准
            if(StringUtils.isNotBlank(inacId) && (invMatAcceptDtlVo.getAutoDelivery()==null)){
                Map<String, Object> autoDeliveryInfo = invMatAcceptService.getAutuDeleiveyInfo(userInfoScope, inacId);
                Boolean autoDelivery = (Boolean) autoDeliveryInfo.get("autoDelivery");
                if(autoDelivery){
                    invMatAcceptDtlVo.setAutoDelivery("Y");
                }
                else{
                    invMatAcceptDtlVo.setAutoDelivery("N");
                }
            }
        }
        
        maps.put( "invMatAccept", invMatAcceptDtlVo );
        maps.put( "isEdit", isEdit );

        UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
        maps.put( "createuseid", userInfo.getUserId() );
        maps.put( "createusername", userInfo.getUserName() );
        maps.put( "siteid", userInfo.getSiteId().toLowerCase() );

        ModelAndView modelAndView = new ModelAndView( "/invmataccept/invMatAcceptForm.jsp" );
        modelAndView.addObject( "data", JsonHelper.toJsonString( maps ) );
        return modelAndView;
    }

    /**
     * 根据采购合同编号查询采购合同详情
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-6
     * @param inacId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvMatAcceptByPoNo", method = RequestMethod.POST)
    public Map<String, Object> queryInvMatAcceptByPoSheetNo(String poNo) throws Exception {
        UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> reMap = new HashMap<String, Object>();
        String poId = purOrderService.querySheetIdByFlowNo( poNo, userInfo.getSiteId() );
        List<PurOrderVO> list = purOrderService.queryPurOrderInfoBySheetId(poId);
        if(list != null && list.size() != 0){
        	PurOrderVO purOrderVO = list.get(0);
        	reMap.put("companyNo", purOrderVO.getCompanyNo());
        	reMap.put("companyName", purOrderVO.getCompanyName());
        }
        List<InvMatAcceptDetailVO> invMatAcceptDetails = invPubInterface.queryItem2InvMatAcceptDetail( poId );
        reMap.put( "invMatAcceptDetails", invMatAcceptDetails );
        reMap.put( "poId", poId );
        reMap = getSuccessReturnMap( reMap );
        return reMap;
    }

    @RequestMapping(value = "/queryInvMatAcceptById", method = RequestMethod.POST)
    public Map<String, Object> queryInvMatAcceptById(String inacId) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        InvMatAcceptDtlVo invMatAcceptDtlVo = invMatAcceptService.queryInvMatAcceptById( inacId );
        reMap.put( "data", invMatAcceptDtlVo );
        return reMap;
    }

    @RequestMapping(value = "/queryInvMatAcceptList", method = RequestMethod.POST)
    public Page<InvMatAcceptVo> queryInvMatAcceptList(String search) throws Exception {
        InvMatAcceptVo imt = new InvMatAcceptVo();
        UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            imt = JsonHelper.fromJsonStringToBean( search, InvMatAcceptVo.class );
        }
        return invMatAcceptService.queryInvMatAcceptList( imt, userInfo );

    }

    @RequestMapping(value = "/insertInvMatAccept", method = RequestMethod.POST)
    public Map<String, Object> insertInvMatAccept(boolean startWorkflow) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        InvMatAccept invMatAccept = getAcceptDataInWorkflowUtil.getPoFromBrower( INV_MAT_ACCEPT_PARAMNAME,
                InvMatAccept.class );
        List<InvMatAcceptDetail> invMatAcceptDetails = getAcceptDataInWorkflowUtil.getListFromBrower(
                INV_MAT_ACCEPT_DETAILS_PARAMNAME, InvMatAcceptDetail.class );

        reMap = invMatAcceptService.insertInvMatAccept( invMatAccept, invMatAcceptDetails, startWorkflow );
        reMap = getSuccessReturnMap( reMap );
        return reMap;
    }

    private Map<String, Object> getSuccessReturnMap(Map<String, Object> reMap) {
        reMap.put( "result", "success" );
        return reMap;
    }

    @RequestMapping(value = "/updateInvMatAccept", method = RequestMethod.POST)
    public Map<String, Object> updateInvMatAccept(boolean startWorkflow) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        InvMatAccept invMatAccept = getAcceptDataInWorkflowUtil.getPoFromBrower( INV_MAT_ACCEPT_PARAMNAME,
                InvMatAccept.class );
        List<InvMatAcceptDetail> invMatAcceptDetails = getAcceptDataInWorkflowUtil.getListFromBrower(
                INV_MAT_ACCEPT_DETAILS_PARAMNAME, InvMatAcceptDetail.class );
        reMap = invMatAcceptService.updateInvMatAccept( invMatAccept, invMatAcceptDetails, startWorkflow );
        return reMap;
    }

    @RequestMapping(value = "/deleteInvMatAcceptById", method = RequestMethod.POST)
    public Map<String, Object> deleteInvMatAcceptById(String inacId) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        invMatAcceptService.deleteInvMatAccept( inacId );
        reMap = getSuccessReturnMap( reMap );
        return reMap;
    }

    @RequestMapping(value = "/voidFlow", method = RequestMethod.POST)
    public Map<String, Object> voidFlow(String inacId, String message) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        invMatAcceptService.voidFlow( inacId, message );
        reMap = getSuccessReturnMap( reMap );
        return reMap;
    }

    /**
     * 设置流程变量
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-9
     * @param inacId
     * @param message
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/setVariable", method = RequestMethod.POST)
    public Map<String, Object> setVariable(String processId, String sp_mate, String proc_result) throws Exception {

        Map<String, Object> reMap = new HashMap<String, Object>();
        ProcessInstance processInstance = workflowService.getProcessByProcessInstId( processId );
        if ( StringUtils.isNotBlank( sp_mate ) ) {
            workflowService.setVariable( processId, "SP_MATERIAL", sp_mate );
            workflowService.setVariable( processId, "isZJYS", isAcceptToZJYS.isApproval( processInstance, null ) );
        }

        if ( StringUtils.isNotBlank( "PROC_RESULT" ) ) {
            workflowService.setVariable( processId, "PROC_RESULT", proc_result );
            workflowService.setVariable( processId, "isRK", isAcceptToRK.isApproval( processInstance, null ) );
        }

        reMap = getSuccessReturnMap( reMap );
        return reMap;
    }

    /**
     * @description:获取采购单号列表方法
     * @author: 890166
     * @createDate: 2014-7-23
     * @param search
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryPurOrderList", method = RequestMethod.POST)
    public Page<MTPurOrderVO> queryPurOrderList(String search) throws Exception {
        MTPurOrderVO mtpo = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            mtpo = JsonHelper.fromJsonStringToBean( search, MTPurOrderVO.class );
        }
        return invMatAcceptService.queryPurOrderList( userInfo, mtpo );
    }
}
