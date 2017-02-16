package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.itsm.bean.ItsmJPItems;
import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.service.ItsmJobPlanService;
import com.timss.itsm.service.ItsmMaintainPlanService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.service.ItsmWorkOrderService;
import com.timss.itsm.util.InvItemVOtoJPItemsUtil;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.MvcJsonUtil;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: WorkorderController.java
 * @author: 王中华
 * @createDate: 2014-6-23
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/itsm/workorder")
public class ItsmWorkorderController {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWorkOrderService workOrderService;
    @Autowired
    // @Qualifier("JobPlanServiceImpl")
    private ItsmJobPlanService jobPlanService;
    @Autowired
    private ItsmMaintainPlanService maintainPlanService;
    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private ItsmWoAttachmentService woAttachmentService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private ISecurityMaintenanceManager iSecurityMaintenanceManager;
    private static final Logger LOG = Logger.getLogger( ItsmWorkorderController.class );

    
    @RequestMapping(value = "/sysUserhelpList", method = RequestMethod.GET)
    public String sysUserhelpList() {
        return "/sysUserhelpList.jsp";
    }
    /**
     * @description: 打开工单列表页面
     * @author: 王中华
     * @createDate: 2014-7-4
     * @return
     * @throws Exception: nullMode = NULLMODE.Exception
     *             表示你传入的枚举“enum1,enum2”返回的数据为空会抛出异常。 nullMode =
     *             NULLMODE.EachException 表示你传入的枚举“enum1,enum2”其中有一个为空就会抛出异常。
     */
    @RequestMapping(value = "/workorderList", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_SPEC,ITSM_URGENCY_DEGREE,ITSM_STATUS")
    public String workOrderList() {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        
        boolean flag1 = workOrderService.userInGroupOrRole( "ITC_ITSM_KF", "role", userId, siteId ); // 是否是客服
        boolean flag2 = workOrderService.userInGroupOrRole( "ITC_ITSM_WHGCS", "role", userId, siteId ); // 是否是工程师
        return "/workOrderList.jsp?isITCSer=" + flag1 + "&isEngineer=" + flag2;
    }

    /**
     * @description:选择父工单
     * @author: 王中华
     * @createDate: 2014-8-13
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/parentWOList", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_SPEC")
    public String parentWOList() {
        return "/operationWO/parentWOList.jsp";
    }

    /**
     * @description:故障类型树
     * @author: 王中华
     * @createDate: 2014-9-19
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/faultTypeTree", method = RequestMethod.GET)
    public String faultTypeTree() {
        return "/woParamsConf/faultTypeTree.jsp";
    }

    /**
     * @description:选择标准作业方案
     * @author: 王中华
     * @createDate: 2014-8-13
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/woPlanStandJPList", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_SPEC")
    public String woPlanStandJPList() {
        return "/operationWO/woPlanStandJPList.jsp";
    }

    /**
     * @description:判断登录用户是否是客服
     * @author: 王中华
     * @createDate: 2014-9-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/loginUserIsCusSer")
    public Map<String, Boolean> loginUserIsCusSer() {
        // 获取用户所有的部门信息
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        boolean flag1 = workOrderService.userInGroupOrRole( "ITC_ITSM_KF", "role", userId, siteId );
        boolean flag2 = workOrderService.userInGroupOrRole( "ITC_ITSM_WHGCS", "role", userId, siteId );
        boolean flag3 = workOrderService.userInGroupOrRole( "ITC_ITSM_WHZG", "usergroup", userId, siteId );
        boolean flag4 = workOrderService.userInGroupOrRole( "ITC_ITSM_ADMIN", "role", userId, siteId );

        boolean flag5 = false;
        List<Organization> userOrgs = organizationMapper.selectOrgUserBelongsTo( userId );
        for ( int i = 0; i < userOrgs.size(); i++ ) {
            String orgId = userOrgs.get( i ).getCode();
            if ( ItsmConstant.INFOCENTERDEPTID.equals( orgId ) ) {
                flag5 = true;
                break;
            }
        }

        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put( "isCuser", flag1 );
        result.put( "isEngineer", flag2 );
        result.put( "isMtpCharge", flag3 );
        result.put( "isItsmAdmin", flag4 );
        result.put( "isInfoCenterUser", flag5 );

        return result;
    }

    /**
     * @description: 查询工单列表数据
     * @author: 王中华
     * @createDate: 2014-7-4
     * @return
     * @throws Exception
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/workorderListdata", method = RequestMethod.POST)
    public Page<ItsmWorkOrder> workOrderListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        Page<ItsmWorkOrder> page = userInfoScope.getPage();
        String selectTreeId = userInfoScope.getParam( "selectTreeId" );
        String rowFilterFlag = userInfoScope.getParam( "rowFilterFlag" ); // 此处1代表前台选中，0代表前台没选中
        String unEndFilterFlag = userInfoScope.getParam( "unEndFilterFlag" );
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "workOrderMap",
                ItsmConstant.MODULENAME, "ItsmWorkOrderDao" );

        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            if ( fuzzyParams.get( "selectTreeId" ) != null ) {
                selectTreeId = fuzzyParams.get( "selectTreeId" ).toString();
                fuzzyParams.remove( "selectTreeId" ); // 因为选择左边树的查询不同与表头查询，所有要移除
            }

            fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
            page.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( userInfoScope.getParamMap().containsKey( "sort" ) ) {
            String sortKey = userInfoScope.getParam( "sort" );
            // 如果Dao中使用resultMap来将查询结果转化为bean，则需要将Map中的key做转换，变成数据库可以识别的列名
            sortKey = propertyColumnMap.get( sortKey );

            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        } else {
            // 设置默认的排序字段
            page.setSortKey( "MODIFYDATE" );
            page.setSortOrder( "desc" );

        }
        page.setParameter( "loginSiteid", siteId );
        page.setParameter( "loginUser", userId );
        if ( selectTreeId != null && !"null".equals( selectTreeId ) && !"".equals( rowFilterFlag ) ) {
            page.setParameter( "selectTreeId", selectTreeId ); // 设置树查询参数
        }
        if ( rowFilterFlag != null && !"null".equals( rowFilterFlag ) ) {
            page.setParameter( "rowFilterFlag", rowFilterFlag );
            page.setParameter( "unEndFilterFlag", unEndFilterFlag );

        }

        page = workOrderService.queryAllWO( page );

        return page;
    }

    /**
     * @description: 跳转到新建工单页面
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return:
     */
    @RequestMapping(value = "/openNewWOPage")
    @ReturnEnumsBind("ITSM_SPEC,ITSM_FB_TYPE,ITSM_STATUS,ITSM_TYPE,ITSM_FAULT_DEGREE,ITSM_URGENCY_DEGREE,ITSM_INFLUENCE_SCOPE")
    public String openNewWOPage() {
        return "/operationWO/newWO.jsp";
    }

    @RequestMapping(value = "/openInitNewWOPage")
    @ReturnEnumsBind("ITSM_STATUS,ITSM_TYPE,ITSM_MAINTTYPE")
    public ModelAndView openInitNewWOPage() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String woId = userInfoScope.getParam( "woId" );
        ItsmWorkOrder workOrder = new ItsmWorkOrder();
        if(woId == null){
            workOrder = null;
        }else{
            Map<String, Object> workOrderHashMap = workOrderService.queryWOById(  woId  );
            workOrder = (ItsmWorkOrder) workOrderHashMap.get( "bean" );
        }
       
        String siteid = userInfoScope.getSiteId();
        String deptid = userInfoScope.getOrgId();
        List<List<Object>> comboboxResult = new ArrayList<List<Object>>();
        comboboxResult = getComboboxData( siteid, deptid,workOrder );

        map.put( "assistDeptResult", JsonHelper.toJsonString( comboboxResult ) );
        ModelAndView modelAndView = new ModelAndView( "/operationWO/initNewWO.jsp", map );
        return modelAndView;
    }

    @RequestMapping(value = "/homepageOpenNewWOPage")
    @ReturnEnumsBind("ITSM_SPEC,ITSM_FB_TYPE,ITSM_STATUS,ITSM_TYPE,ITSM_FAULT_DEGREE," +
    		"ITSM_URGENCY_DEGREE,ITSM_INFLUENCE_SCOPE,ITSM_MAINTTYPE")
    public ModelAndView homepageOpenNewWOPage() {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteid = userInfoScope.getSiteId();
        String deptid = userInfoScope.getOrgId();
        
        boolean flag = workOrderService.userInGroupOrRole( "ITC_ITSM_WHGCS", "role", userId, siteid );
        ModelAndView modelAndView = new ModelAndView();
        
        if(flag){
            modelAndView = new ModelAndView( "/operationWO/newWO.jsp", null );
        }else{
            List<List<Object>> comboboxResult = new ArrayList<List<Object>>();
            comboboxResult = getComboboxData( siteid, deptid, null );
            map.put( "assistDeptResult", JsonHelper.toJsonString( comboboxResult ) );
            modelAndView = new ModelAndView( "/operationWO/initNewWO.jsp", map );
        }
        return modelAndView;
    }
    
    /**
     * @description:获取下一环节审批单位的数据
     * @author: 王中华
     * @createDate: 2015-6-24
     * @param siteid
     * @param deptid
     * @return:
     */
    private List<List<Object>> getComboboxData(String siteid, String deptid,ItsmWorkOrder itsmWorkOrder) {
        List<List<Object>> comboboxResult = new ArrayList<List<Object>>();
        if ( (ItsmConstant.INFOCENTERSITEID).equals( siteid ) && (ItsmConstant.INFOCENTERDEPTID).equals( deptid ) ) { 
            boolean flag = itsmWorkOrder!= null && "applicantAudit".equals( itsmWorkOrder.getCurrStatus() ) 
                            && itsmWorkOrder.getSiteid().equals( siteid );
            if( flag ){ //如果是管理员流转环节,且是粤电总部建单
                ArrayList<Object> row0 = new ArrayList<Object>();
                row0.add( ItsmConstant.INFOCENTERSITEID );
                row0.add( "集团总部" );
                comboboxResult.add( row0 );
            }else{
             // 如果是信息中心用户，获取其对应的“下一环节审批单位”的combobox值（集团总部和科技公司）
                List<String> ydzIdList = new ArrayList<String>();
                ydzIdList.add( ItsmConstant.YDZID );
                List<Organization> deptOfYDZList = iSecurityMaintenanceManager.selectOrgsByParentIds( ydzIdList );

                for ( Organization deptObj : deptOfYDZList ) {
                    ArrayList<Object> row = new ArrayList<Object>();
                    row.add( deptObj.getCode() );
                    row.add( deptObj.getName() );
                    if ( deptObj.getCode().equals( ItsmConstant.INFOCENTERDEPTID ) ) {
                        row.add( true );
                    }
                    comboboxResult.add( row );
                }
            }

            List<Object> row1 = new ArrayList<Object>();
            row1.add( ItsmConstant.ITCSITEID );
            row1.add( "科技公司" );
            comboboxResult.add( row1 );
            List<Object> row2 = new ArrayList<Object>();
            row2.add( "endWo" );
            row2.add( "工单结束" );
            comboboxResult.add( row2 );
        } else {// 如果是非信息中心用户，获取其对应的“下一环节审批单位”的combobox值（本单位和信息中心）
            List<Object> row1 = new ArrayList<Object>();
            row1.add( "own" );
            row1.add( "本单位" );
            comboboxResult.add( row1 );
            ArrayList<Object> row2 = new ArrayList<Object>();
            row2.add( ItsmConstant.INFOCENTERSITEID );
            row2.add( "集团信息中心" );
            comboboxResult.add( row2 );
            ArrayList<Object> row3 = new ArrayList<Object>();
            row3.add( ItsmConstant.ITCSITEID );
            row3.add( "科技公司" );
            comboboxResult.add( row3 );
            ArrayList<Object> row4 = new ArrayList<Object>();
            row4.add( "endWo" );
            row4.add( "工单结束" );
            comboboxResult.add( row4 );
        }
        return comboboxResult;
    }

    /**
     * @description: "待办"中途跳转到
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return:
     */
    @RequestMapping(value = "/todolistTOWOPage")
    @ReturnEnumsBind("ITSM_SPEC,ITSM_FB_TYPE,ITSM_STATUS,ITSM_TYPE,ITSM_HAS_REMAINFAULT,"
            + "ITSM_FAULT_DEGREE,ITSM_INFLUENCE_SCOPE,ITSM_URGENCY_DEGREE,ITSM_MAINTTYPE")
    public ModelAndView todolistTOWOPage() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据
        Map<String, Object> workOrderHashMap = workOrderService.queryWOById(  woId  );
        ItsmWorkOrder workOrder = (ItsmWorkOrder) workOrderHashMap.get( "bean" );
        String maintType = workOrder.getMaintType();
        String jumpUrl = "";
        String woStatus = workOrder.getCurrStatus();
        boolean flag = "draft".equals( woStatus ) || "sendWO".equals( woStatus ) || "newWO".equals( woStatus );
        boolean flag2 = "initComAudit".equals( woStatus ) || "infoCenterAudit".equals( woStatus )
                || "applicantAudit".equals( woStatus ) || "groupDeptAudit".equals( woStatus );
        if ( flag ) {
            jumpUrl = "/operationWO/newWO.jsp";
            if ( maintType != null && !"sendWO".equals( woStatus ) ) { // 如果是非专业客户的报障
                jumpUrl = "/operationWO/initNewWO.jsp";
            }
        } else if ( flag2 ) {
            jumpUrl = "/operationWO/initNewWO.jsp";
        } else {
            jumpUrl = "/operationWO/woPlan.jsp";
        }

        Map<String, Object> map = new HashMap<String, Object>( 0 );
        String siteid = userInfoScope.getSiteId();
        String deptid = userInfoScope.getOrgId();
        List<List<Object>> comboboxResult = new ArrayList<List<Object>>();
        comboboxResult = getComboboxData( siteid, deptid, workOrder );
        map.put( "assistDeptResult", JsonHelper.toJsonString( comboboxResult ) );
        map.put( "woId", woId );
        map.put( "woStatus", woStatus );
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );

        return modelAndView;
    }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2014-11-10
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/openPageByWoCode")
    public Map<String, String> invOpenPageByWoCode() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woCode = userInfoScope.getParam( "woCode" );// 获取库存传过来的数据
        String siteId = userInfoScope.getParam( "siteid" );
        Map<String, Object> workOrderHashMap = workOrderService.queryWOBaseInfoByWOCode( woCode, siteId );
        ItsmWorkOrder workOrder = (ItsmWorkOrder) workOrderHashMap.get( "workOrder" );

        String woId = String.valueOf( workOrder.getId() );
        String woStatus = workOrder.getCurrStatus();

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "woId", woId );
        mav.put( "currStatus", woStatus );

        return mav;
    }

    /**
     * @description: 提交“工单”数据
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitWorkOrderdata", method = RequestMethod.POST)
    @ValidFormToken
    public Map<String, String> commitWorkOrder() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderFormDate = userInfoScope.getParam( "workOrderForm" );// 获取前台传过来的form表单数据
        ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
        String woId = workOrder.getId();
        String workOrderCodeString = workOrder.getWorkOrderCode();
        String workflowId = workOrder.getWorkflowId();
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号
        String isEngineer = userInfoScope.getParam( "isEngineer" ); // 是否是工程师
        String needInfoCenterAudit = userInfoScope.getParam( "needInfoCenterAudit" ); 
        String sendWoNow = userInfoScope.getParam( "sendWoNow" ); //是否立即派单
        
        Map<String, String> addWODataMap = new HashMap<String, String>();
        addWODataMap.put( "woFormData", workOrderFormDate );
        addWODataMap.put( "itcWhbWo", isEngineer );
        addWODataMap.put( "needInfoCenterAudit", needInfoCenterAudit );
        addWODataMap.put( "commitStyle", commitStyle );
        addWODataMap.put( "uploadIds", uploadIds ); // 附件ID
        addWODataMap.put( "sendWoNow", sendWoNow ); 

        String taskId = "noFlow";
        if ( "".equals( workflowId ) || workflowId == null ) { // 确定不是因为回退的节点
            if ( "".equals( woId ) ) { // 初次提交或暂存
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    Map<String, Object> insertResultMap = workOrderService.insertWorkOrder( addWODataMap );
                    taskId = insertResultMap.get( "taskId" ).toString();
                    woId = (String) insertResultMap.get( "woId" );
                    workOrderCodeString = insertResultMap.get( "workOrderCode" ).toString();
                    workflowId = insertResultMap.get( "workflowId" ).toString();
                    LOG.info( "-------------web层：工单提交完成,工单编号：" + workOrderCodeString + "工单流程：" + workflowId
                            + "-------------" );
                } else if ( "save".equals( commitStyle ) ) { // 暂存，不启动流程
                    Map<String, Object> saveResultMap = workOrderService.saveWorkOrder( addWODataMap );
                    woId =  (String) saveResultMap.get( "woId" );
                    workOrderCodeString = saveResultMap.get( "workOrderCode" ).toString();
                    LOG.info( "-------------web层：工单暂存完成,工单编号：" + workOrderCodeString + "-------------" );
                }
            } else { // 对暂存的工单提交 或 再次暂存 或其他状态下的提交
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    addWODataMap.put( "updateStyle", "commit" );
                } else if ( "save".equals( commitStyle ) ) { // 再次暂存，不启动流程
                    addWODataMap.put( "updateStyle", "save" );
                }
                Map<String, String> flowIdAndTaskId = workOrderService.updateWorkOrder( addWODataMap );
                workflowId = flowIdAndTaskId.get( "workflowId" );
                taskId = flowIdAndTaskId.get( "taskId" );
                LOG.info( "-------------web层：暂存的工单再次提交或暂存完成,工单流程：" + workflowId + "-------------" );
            }
        } else {
            LOG.info( "-------------web层：回退的工单再次提交或暂存开始,工单编号：" + workOrderCodeString + "-------------" );
            workOrderService.rollbackCommitWo( addWODataMap );
            LOG.info( "-------------web层：回退的工单再次提交或暂存完成,工单编号：" + workOrderCodeString + "-------------" );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workflowId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            taskId = task.getId();
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        if ( "save".equals( commitStyle ) ) {
            taskId = "noFlow";
        }
        mav.put( "taskId", taskId );
        mav.put( "woId", String.valueOf( woId ) );
        mav.put( "workOrderCode", workOrderCodeString );
        mav.put( "workflowId", workflowId );
        return mav;
    }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-3-20
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitInitWodata", method = RequestMethod.POST)
    @ValidFormToken
    public Map<String, String> commitInitWodata() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderFormDate = userInfoScope.getParam( "initWorkOrderForm" );// 获取前台传过来的form表单数据
        ItsmWorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, ItsmWorkOrder.class );
        String woId = workOrder.getId();
        String workOrderCodeString = workOrder.getWorkOrderCode();
        String workflowId = workOrder.getWorkflowId();
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号
        String itcWhbWo = userInfoScope.getParam( "itcWhbWo" ); // 是否是科技公司维护部建单
        String infoCenterWo = userInfoScope.getParam( "infoCenterWo" ); // 是否是信息中心人员检点
        String comNextAudit = userInfoScope.getParam( "comNextAudit" ); // 单位审批下一步环节审批单位
        String centerNextAudit = userInfoScope.getParam( "centerNextAudit" ); // 信息中心审批的下一步审批单位

        Map<String, String> addWODataMap = new HashMap<String, String>();
        addWODataMap.put( "woFormData", workOrderFormDate );
        // plan：工单策划；actual:实际
        addWODataMap.put( "commitStyle", commitStyle );
        addWODataMap.put( "uploadIds", uploadIds ); // 附件ID
        addWODataMap.put( "infoCenterWo", infoCenterWo );
        addWODataMap.put( "itcWhbWo", itcWhbWo );
        addWODataMap.put( "comNextAudit", comNextAudit );
        addWODataMap.put( "centerNextAudit", centerNextAudit );
       
        String taskId = "noFlow";
        if ( "".equals( workflowId ) || workflowId == null ) { // 确定不是因为回退的节点
            if ( "".equals( woId )  ) { // 初次提交或暂存
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    Map<String, Object> insertResultMap = workOrderService.insertInitWorkOrder( addWODataMap );
                    taskId = insertResultMap.get( "taskId" ).toString();
                    woId =  (String) insertResultMap.get( "woId" );
                    workOrderCodeString = insertResultMap.get( "workOrderCode" ).toString();
                    workflowId = insertResultMap.get( "workflowId" ).toString();
                    LOG.info( "-------------web层：工单提交完成,工单编号：" + workOrderCodeString + "工单流程：" + workflowId
                            + "-------------" );
                } else if ( "save".equals( commitStyle ) ) { // 暂存，不启动流程
                    Map<String, Object> saveResultMap = workOrderService.saveInitWorkOrder( addWODataMap );
                    woId =  (String) saveResultMap.get( "woId" );
                    workOrderCodeString = saveResultMap.get( "workOrderCode" ).toString();
                    LOG.info( "-------------web层：工单暂存完成,工单编号：" + workOrderCodeString + "-------------" );
                }
            } else { // 对暂存的工单提交 或 再次暂存 或其他状态下的提交
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    addWODataMap.put( "updateStyle", "commit" );
                } else if ( "save".equals( commitStyle ) ) { // 再次暂存，不启动流程
                    addWODataMap.put( "updateStyle", "save" );
                }
                Map<String, String> flowIdAndTaskId = workOrderService.updateInitWorkOrder( addWODataMap );
                workflowId = flowIdAndTaskId.get( "workflowId" );
                taskId = flowIdAndTaskId.get( "taskId" );
                LOG.info( "-------------web层：暂存的工单再次提交或暂存完成,工单流程：" + workflowId + "-------------" );
            }
        } else {
            LOG.info( "-------------web层：回退的工单再次提交或暂存开始,工单编号：" + workOrderCodeString + "-------------" );
            workOrderService.rollbackCommitWo( addWODataMap );
            LOG.info( "-------------web层：回退的工单再次提交或暂存完成,工单编号：" + workOrderCodeString + "-------------" );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workflowId );
            // 刚启动流程，第一个活动节点肯定是属于当前登录人的
            Task task = activities.get( 0 );
            taskId = task.getId();
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        if ( "save".equals( commitStyle ) ) {
            taskId = "noFlow";
        }
        mav.put( "taskId", taskId );
        mav.put( "woId", String.valueOf( woId ) );
        mav.put( "workOrderCode", workOrderCodeString );
        mav.put( "workflowId", workflowId );
        return mav;
    }

    /**
     * @description: 提交工单后，启动了流程，弹出审批框，点击取消 1.先消掉启动流程产生的待办 2.加入草稿待办 3.删掉流程
     *               4.清空工单信息中流程ID 5.修改工单的状态为“草稿”
     * @author: 王中华
     * @createDate: 2014-9-19
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/cancelCommitWO", method = RequestMethod.POST)
    public Map<String, String> cancelCommitWO() throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据
        String hasRollback = userInfoScope.getParam( "hasRollback" );
        if ( "yes".equals( hasRollback ) ) { // 是回退的工单,什么也不用做
            Map<String, String> mav = new HashMap<String, String>();
            mav.put( "result", "success" );
            result = mav;
        } else { // 不是回退的工单，要删待办，删流程，清空工单中流程ID，修改状态为草稿，加入到首页草稿里面
            result = workOrderService.cancelCommitWO( woId );
        }
        return result;
    }

    @RequestMapping(value = "/wobackToPlan", method = RequestMethod.POST)
    public Map<String, String> wobackToPlan() throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据
        String woStepFlag = userInfoScope.getParam( "woStepFlag" );
        result = workOrderService.wobackToSomeStep( woId, woStepFlag );
        return result;
    }

    /**
     * @description:删除草稿
     * @author: 王中华
     * @createDate: 2014-7-31
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteWorkOrderDraft", method = RequestMethod.POST)
    public Map<String, String> deleteWorkOrderDraft() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woIdString = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据

        workOrderService.deleteWorkOrder( woIdString );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description: 作废工单，仅工单发起人可以作废
     * @author: 王中华
     * @createDate: 2014-12-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteWorkOrder", method = RequestMethod.POST)
    public Map<String, String> obsoleteWorkOrder() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woIdString = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据

        workOrderService.obsoleteWorkOrder( woIdString );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:暂存 工作策划或汇报 时的数据
     * @author: 王中华
     * @createDate: 2014-7-16
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveWOOnPlandata", method = RequestMethod.POST)
    public Map<String, String> saveWOOnPlan() throws Exception {
        return workOrderService.saveWOOnPlan();
    }

    /**
     * 更新工单的基本信息
     */
    @RequestMapping(value = "/updateWoBaseInfo", method = RequestMethod.POST)
    public Map<String, String> updateWoBaseInfo() throws Exception {
        return workOrderService.updateWoBaseInfo();
    }

    /**
     * 更新工单的基本信息
     */
    @RequestMapping(value = "/updateWoOnSendWo", method = RequestMethod.POST)
    public Map<String, String> updateWoOnSendWo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        Map<String, String> params = new HashMap<String, String>();
        params.put( "woId", userInfoScope.getParam( "woId" ) );
        params.put( "faultTypeId", userInfoScope.getParam( "faultTypeId" ) );
        params.put( "serCharacterId", userInfoScope.getParam( "serCharacterId" ) );
        params.put( "priorityId", userInfoScope.getParam( "priorityId" ) );

        workOrderService.updateWoOnSendWo( params );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:跳转到工单策划页面
     * @author: 王中华
     * @createDate: 2014-6-23
     * @return:
     */
    @RequestMapping(value = "/openWOPlanInfoPage")
    @ReturnEnumsBind("ITSM_FB_TYPE,ITSM_TYPE,ITSM_STATUS,ITSM_URGENCY_DEGREE,ITSM_INFLUENCE_SCOPE,ITSM_SPEC,ITSM_ISTOPTW,ITSM_HAS_REMAINFAULT,ITSM_FAULT_DEGREE")
    public String openWOPlanInfoPage() {
        return "/operationWO/woPlan.jsp";
    }

    /**
     * @description: 查询工单信息(电厂)
     * @author: 王中华
     * @createDate: 2014-7-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryWODataById")
    public Map<String, Object> queryWODataById() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = userInfoScope.getParam( "workOrderId" ) ;
        Map<String, Object> map = workOrderService.queryWOById( woId );
        ItsmWorkOrder workOrder = (ItsmWorkOrder) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        // 节点的候选人
        // TODO 以后改为调用供应商模块的数据
        List<String> candidateUsers = (List<String>) map.get( "candidateUsers" );

        Integer jpId = workOrder.getJobPlanId();
        Integer mtpId = workOrder.getMaintainPlanId();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if ( mtpId != null && jpId == null && mtpId != 0 ) { // 从周期性维护计划过来，并且在策划阶段未暂存也未提交
            jpId = maintainPlanService.queryMTPById( mtpId ).getJobPlanId();
            resultMap = jobPlanService.queryJPById( jpId );
        } else {
            resultMap = jobPlanService.queryJPById( jpId );

            // 查询策划或者汇报时领取的物资
            List<ItsmJPItems> jpPlanItemsList = new ArrayList<ItsmJPItems>();
            // 此处需要多传一个参数过去
            Page<InvMatApplyToWorkOrder> invItemVOList = invMatApplyService.queryMatApplyByWoId(
                    String.valueOf( woId ), "itsm_picking" );
            List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();
            jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );

            JSONArray jpItemsListStr = new JSONArray();
            if ( !jpPlanItemsList.isEmpty() && jpPlanItemsList.size() > 0 ) { // 有申请领取的物资
                resultMap.remove( "toolData" ); // 先将作业方案中的物资剔除
                jpItemsListStr = MvcJsonUtil.JSONArrayFromList( jpPlanItemsList );
                resultMap.put( "toolData", jpItemsListStr.toString() );
            }
        }

        resultMap.put( "workOrderForm", JsonHelper.toJsonString( workOrder ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "candidateUsers", candidateUsers );
        return resultMap;

    }

    /**
     * @description:查询工单数据（ITC）
     * @author: 王中华
     * @createDate: 2014-9-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryItWODataById")
    public @ResponseBody
    Map<String, Object> queryItWODataById() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId =  userInfoScope.getParam( "workOrderId" ) ;
        Map<String, Object> map = workOrderService.queryItWOById( woId );
        ItsmWorkOrder workOrder = (ItsmWorkOrder) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        // 节点的候选人 (此处有两种方法，从流程中取值有问题，从自己的业务表中取值可以)
        List<String> candidateUsers = (List<String>) map.get( "candidateUsers" );
        String currhandUsersId = workOrder.getCurrHandlerUser();
        if ( currhandUsersId != null ) {
            String[] usersIdArray = currhandUsersId.split( "," );
            candidateUsers = (List<String>) Arrays.asList( usersIdArray );
        }

        Integer jpId = workOrder.getJobPlanId();
        Integer mtpId = workOrder.getMaintainPlanId();
        String woStatus = workOrder.getCurrStatus();

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 领料单数据查询
        if ( "woPlan".equals( woStatus ) ) { // 策划
            if ( jpId != 0 && jpId != null ) { // 有了暂存
                resultMap = jobPlanService.queryJPById( jpId );
            } else if ( mtpId != null && mtpId != 0 ) { // 没有暂存，但是从维护计划过来
                jpId = maintainPlanService.queryMTPById( mtpId ).getJobPlanId();
                resultMap = jobPlanService.queryJPById( jpId );
            }
        } else if ( "endWorkReport".equals( woStatus ) && jpId != null && jpId != 0 ) { // 汇报阶段，有暂存时
            resultMap = jobPlanService.queryJPById( jpId );
        } else {
            List<ItsmJPItems> jpPlanItemsList = new ArrayList<ItsmJPItems>();
            Page<InvMatApplyToWorkOrder> invItemVOList = invMatApplyService.queryMatApplyByWoId(
                    String.valueOf( woId ), "itsm_picking" );
            List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();

            jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );

            resultMap.put( "toolData", jpPlanItemsList );
        }

        List<ItsmWoAttachment> attachList = woAttachmentService.queryWoAttachmentById( woId, "WO" );
        // 根据附件id，转化数据传前台，前台显示附件数据
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        resultMap.put( "attachmentMap", attachmentMap );

        resultMap.put( "workOrderForm", JsonHelper.toJsonString( workOrder ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "candidateUsers", candidateUsers );
        return resultMap;

    }

    @RequestMapping(value = "/stopWorkOrder", method = RequestMethod.POST)
    public Map<String, String> stopWorkOrder(@RequestParam("taskId") String taskId,
            @RequestParam("assignee") String assignee, @RequestParam("owner") String owner,
            @RequestParam("message") String message, @RequestParam("businessId") String businessId) throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "woId", businessId );
        params.put( "modifyDate", new Date() );
        params.put( "modifyUser", userId );
        params.put( "woStatus", "woFiling" );
        params.put( "currHandlerUser", "" );
        params.put( "currHandUserName", "" );
        params.put( "reason", message );
        params.put( "taskId", taskId );
        params.put( "assignee", assignee );
        params.put( "owner", owner );
        params.put( "message", message );

        workOrderService.stopWorkOrder( params );
        // 终止流程后需要对流程进行处理
        // workflowService.stopProcess(taskId, assignee, owner, message);

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description: 周期性维护计划手动生成工单
     * @author: 王中华
     * @createDate: 2014-10-10
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/cycMtpToWo")
    public @ResponseBody
    Map<String, Object> cycMtpToWo() throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String mtpId = userInfoScope.getParam( "mtpId" );
        String todoId = userInfoScope.getParam( "todoId" );

        ItsmMaintainPlan tempMTP = maintainPlanService.queryMTPById( Integer.valueOf( mtpId ) );
        String principal = tempMTP.getPrincipal(); // 维护计划负责人

        // 周期性维护计划生成工单并启动流程
        if ( !principal.contains( userId ) ) {
            resultMap.put( "result", "notyours" );
            return resultMap;
        }

        workOrderService.cycMtpObjToWo( mtpId, todoId );

        resultMap.put( "result", "success" );
        return resultMap;

    }

}
