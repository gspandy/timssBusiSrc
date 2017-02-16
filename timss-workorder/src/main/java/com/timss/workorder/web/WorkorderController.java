package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.workorder.bean.JPItems;
import com.timss.workorder.bean.MaintainPlan;
import com.timss.workorder.bean.WoAttachment;
import com.timss.workorder.bean.WoPriority;
import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.JobPlanService;
import com.timss.workorder.service.MaintainPlanService;
import com.timss.workorder.service.WoAttachmentService;
import com.timss.workorder.service.WoFaultTypeService;
import com.timss.workorder.service.WoPriorityService;
import com.timss.workorder.service.WoQxService;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.util.InvItemVOtoJPItemsUtil;
import com.timss.workorder.util.WoProcessStatusUtil;
import com.timss.workorder.util.WoProcessStatusUtilZJW;
import com.timss.workorder.vo.WoQxVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.MvcJsonUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
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
@RequestMapping(value = "/workorder/workorder")
public class WorkorderController {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    @Qualifier("JobPlanServiceImpl")
    private JobPlanService jobPlanService;
    @Autowired
    private MaintainPlanService maintainPlanService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoPriorityService woPriorityService;
    @Autowired
    private WoFaultTypeService woFaultTypeService;
    @Autowired
    private WoAttachmentService woAttachmentService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private WoQxService woQxService;

    private static Logger logger = Logger.getLogger( WorkorderController.class );

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
    @ReturnEnumsBind("WO_SPEC,WO_URGENCY_DEGREE,WO_STATUS,WO_WIND_STATION,WO_WIND_STATION,WO_COMMIT_HANDLE_STYLE,WO_FAULT_TYPE")
    public String workOrderList() throws Exception {
        return "/workOrderList.jsp";
    }

    /**
     * @description:选择父工单
     * @author: 王中华
     * @createDate: 2014-8-13
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/parentWOList", method = RequestMethod.GET)
    @ReturnEnumsBind("WO_SPEC")
    public String parentWOList() throws Exception {
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
    public String faultTypeTree() throws Exception {
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
    @ReturnEnumsBind("WO_SPEC")
    public String woPlanStandJPList() throws Exception {
        return "/operationWO/woPlanStandJPList.jsp";
    }

    /**
     * @description:统计报表（超时工单统计）
     * @author: 890151
     * @createDate: 2015年11月23日
     * @return
     */
    @RequestMapping(value = "/woReport", method = RequestMethod.GET)
    @ReturnEnumsBind("WO_SPEC,WO_URGENCY_DEGREE,WO_STATUS")
    public ModelAndView workOrderReport() throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //查询优先级
        List<ArrayList<Object>> priorityList = getPriorityList(userInfoScope, null);
        String priorityArrayStr = JsonHelper.toJsonString( priorityList );
        dataMap.put( "priorityArray", priorityArrayStr );
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        return new ModelAndView( "workOrderReport.jsp", dataMap );
    }

    /**
     * @description:缺陷报表页面的跳转
     * @author: 890162 cp from 890151
     * @createDate: 2015年12月21日
     * @return
     */
    @RequestMapping(value = "/woBugReport", method = RequestMethod.GET)
    @ReturnEnumsBind("WO_WIND_STATION")
    public ModelAndView workOrderBugReport() throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        return new ModelAndView( "workOrderBugReport.jsp", dataMap );
    }

    /**
     * @description:缺陷报表页面的查询
     * @author: 890162
     * @createDate: 2015年12月22日
     * @return
     */
    @RequestMapping(value = "/woBugReportList", method = RequestMethod.POST)
    public Page<WoQxVo> workOrderBugReportList() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<WoQxVo> page = userInfoScope.getPage();
        String woYear = userInfoScope.getParam( "woYear" );
        String woMonth = userInfoScope.getParam( "woMonth" );
        String woWindStation = userInfoScope.getParam( "woWindStation" );
        String siteid = userInfoScope.getSiteId();
        if ( "-1".equals( woYear ) ) {
            woYear = null;
        }
        if ( "-1".equals( woMonth ) ) {
            woMonth = null;
        }
        page.setParameter( "woYear", woYear );
        page.setParameter( "woMonth", woMonth );
        page.setParameter( "woWindStation", woWindStation );
        page.setParameter( "siteid", siteid );
        page = woQxService.queryWoQxVoStat( page );
        return page;
    }

    /**
     * @description:工单缺陷优先级设置
     * @author: 890151
     * @createDate: 2015年11月23日
     * @return
     */
    @RequestMapping(value = "/woPriorityConfig", method = RequestMethod.GET)
    public String workOrderPriorityReport() throws Exception {
        return "/workOrderPriorityConfig.jsp";
    }

    /**
     * @description:判断登录用户是否是客服
     * @author: 王中华
     * @createDate: 2014-9-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/loginUserIsCusSer")
    public Map<String, Boolean> loginUserIsCusSer() throws Exception {
        boolean flag = workOrderService.loginUserIsCusSer();
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put( "result", flag );
        return result;
    }

    /**
     * @description: 查询工单列表数据
     * @author: 王中华
     * @createDate: 2014-7-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/workorderListdata", method = RequestMethod.POST)
    public Page<WorkOrder> workOrderListData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<WorkOrder> page = userInfoScope.getPage();
        String selectTreeId = userInfoScope.getParam( "selectTreeId" );
        String rowFilterFlag = userInfoScope.getParam( "rowFilterFlag" ); // 此处1代表前台选中，0代表前台没选中
        String unEndFilterFlag = userInfoScope.getParam( "unEndFilterFlag" );
        //add by yangk 增加超时工单的过滤条件   未完成单过滤
        String overWoFilterFlag = userInfoScope.getParam( "overWoFilterFlag" );
        String unDoneFilterFlag = userInfoScope.getParam( "unDoneFilterFlag" );
        // String embbed = userInfoScope.getParam("embbed");
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "workOrderMap", "workorder",
                "WorkOrderDao" );

        if ( fuzzySearchParams != null ) {
            HashMap<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            if ( fuzzyParams.get( "selectTreeId" ) != null ) {
                selectTreeId = fuzzyParams.get( "selectTreeId" ).toString();
                fuzzyParams.remove( "selectTreeId" ); //因为选择左边树的查询不同与表头查询，所有要移除
            }
            if( fuzzyParams.get( "createdate" ) != null ){
            	fuzzyParams.put("CREATEDATE_STR", fuzzyParams.get( "createdate" ));
                fuzzyParams.remove( "createdate" ); //如果写在fuzzyparam里，最后生成的sql会是CREATEDATE LIKE '%2016-02-23%' ESCAPE '\'，改为按时间字符串形式模糊匹配
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
            page.setSortKey( "CREATEDATE" );
            page.setSortOrder( "desc" );

        }
        page.setParameter( "siteid", siteId );
        if("SWF".equals(siteId)){//只有SWF站点有超时的工单概念
        	page.setParameter("overWoFilterFlag", overWoFilterFlag);
        	page.setParameter("unDoneFilterFlag", unDoneFilterFlag);
        }
        if ( selectTreeId != null && !"null".equals( selectTreeId ) ) {
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
    @ReturnEnumsBind("WO_SPEC,WO_FB_TYPE,WO_STATUS,WO_TYPE,WO_FAULT_DEGREE,WO_URGENCY_DEGREE,WO_INFLUENCE_SCOPE,WO_NOW_HANDLER_MONITOR,WO_NOW_HANDLER_ASSISTANT,WO_NOW_HANDLER_PLANTLEADER")
    public String openNewWOPage() {
        return "/operationWO/newWO.jsp";
    }

    /**
     * @description: 跳转到新建工单页面（湛江生物质）
     * @author: 890151
     * @createDate: 2015年11月23日
     * @return:
     */
    @RequestMapping(value = "/openWorkOrderAddPage")
    @ReturnEnumsBind("WO_TYPE,WO_STATUS,WO_SPEC,WO_FAULT_TYPE,WO_URGENCY_DEGREE,WO_FAULT_DEGREE,WO_EXPERT_HANDLE_STYLE,WO_IS_REASSIGN,WO_IS_DELAY,WO_DELAY_TYPE,WO_DELAY_LEN")
    public ModelAndView openWorkOrderAddPage() {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        setWoAddPageInfo(userInfoScope,dataMap);
        return new ModelAndView( "workOrderAdd.jsp", dataMap );
    }


	/**
     * @description: 跳转到新建工单页面（湛江风电）
     * @author: 890151
     * @createDate: 2015年12月14日
     * @return:
     */
    @RequestMapping(value = "/openWorkOrderAddPageZJW")
    @ReturnEnumsBind("WO_WIND_STATION,WO_ISTOPTW,WO_SPEC,WO_COMMIT_HANDLE_STYLE,WO_FAULT_TYPE,WO_URGENCY_DEGREE,WO_STATUS")
    public ModelAndView openWorkOrderAddPageZJW() {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        setWoAddPageInfo(userInfoScope,dataMap);
        return new ModelAndView( "workOrderAdd.jsp", dataMap );
    }

    /**
     * @description: "待办"中途跳转到
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return:
     */
    @RequestMapping(value = "/todolistTOWOPage")
    @ReturnEnumsBind("WO_SPEC,WO_FB_TYPE,WO_STATUS,WO_TYPE,WO_HAS_REMAINFAULT,WO_ISTOPTW,WO_FAULT_DEGREE,WO_INFLUENCE_SCOPE,WO_URGENCY_DEGREE,WO_NOW_HANDLER_MONITOR,WO_NOW_HANDLER_ASSISTANT,WO_NOW_HANDLER_PLANTLEADER")
    public String todolistTOWOPage() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woId = userInfoScope.getParam( "woId" );// 获取前台传过来的form表单数据
        Map<String, Object> workOrderHashMap = workOrderService.queryWOById( Integer.valueOf( woId ) );
        WorkOrder workOrder = (WorkOrder) workOrderHashMap.get( "bean" );
        String jumpUrl = "";
        String woStatus = workOrder.getCurrStatus();
        boolean flag = "draft".equals( woStatus ) || "monitorAudit".equals( woStatus )
                || "assistantAudit".equals( woStatus ) || "plantLeaderAudit".equals( woStatus )
                || "assistantToTeamleader".equals( woStatus ) || "sendWO".equals( woStatus )
                || "newWo".equals( woStatus ) || "newWO".equals( woStatus );
        if ( flag ) {
            jumpUrl = "/operationWO/newWO.jsp?woId=" + woId + "&woStatus=" + woStatus;
        } else {
            jumpUrl = "/operationWO/woPlan.jsp?woId=" + woId + "&woStatus=" + woStatus;
        }
        return jumpUrl;
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
        WorkOrder workOrder = (WorkOrder) workOrderHashMap.get( "workOrder" );

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
        logger.info( "---------------进入提交工单-------------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderFormDate = userInfoScope.getParam( "workOrderForm" );// 获取前台传过来的form表单数据
        WorkOrder workOrder = JsonHelper.toObject( workOrderFormDate, WorkOrder.class );
        int woId = workOrder.getId();
        String workOrderCodeString = workOrder.getWorkOrderCode();
        String workflowId = workOrder.getWorkflowId();
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号

        Map<String, String> addWODataMap = new HashMap<String, String>();
        addWODataMap.put( "woFormData", workOrderFormDate );
        // plan：工单策划；actual:实际
        addWODataMap.put( "commitStyle", commitStyle );
        addWODataMap.put( "uploadIds", uploadIds ); // 附件ID

        String taskId = "noFlow";
        if ( workflowId == "" || workflowId == null ) { // 确定不是因为回退的节点
            if ( woId == 0 ) { // 初次提交或暂存
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    Map<String, Object> insertResultMap = workOrderService.insertWorkOrder( addWODataMap );
                    taskId = insertResultMap.get( "taskId" ).toString();
                    woId = Integer.valueOf( insertResultMap.get( "woId" ).toString() );
                    workOrderCodeString = insertResultMap.get( "workOrderCode" ).toString();
                    workflowId = insertResultMap.get( "workflowId" ).toString();
                    logger.info( "-------------web层：工单提交完成,工单编号：" + workOrderCodeString + "工单流程：" + workflowId
                            + "-------------" );
                } else if ( "save".equals( commitStyle ) ) { // 暂存，不启动流程
                    Map<String, Object> saveResultMap = workOrderService.saveWorkOrder( addWODataMap );
                    woId = Integer.valueOf( saveResultMap.get( "woId" ).toString() );
                    workOrderCodeString = saveResultMap.get( "workOrderCode" ).toString();
                    logger.info( "-------------web层：工单暂存完成,工单编号：" + workOrderCodeString + "-------------" );
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
                logger.info( "-------------web层：暂存的工单再次提交或暂存完成,工单流程：" + workflowId + "-------------" );
            }
        } else {
            logger.info( "-------------web层：回退的工单再次提交或暂存开始,工单编号：" + workOrderCodeString + "-------------" );
            workOrderService.rollbackCommitWo( addWODataMap );
            logger.info( "-------------web层：回退的工单再次提交或暂存完成,工单编号：" + workOrderCodeString + "-------------" );
            // 获取当前活动节点
            List<Task> activities = workflowService.getActiveTasks( workflowId );
            if ( activities.size() != 0 ) {
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get( 0 );
                if ( task != null ) {
                    taskId = task.getId();
                }
            }
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        if ( commitStyle == "save" ) {
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
        int woId = Integer.valueOf( woIdString );

        workOrderService.deleteWorkOrder( woId );

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
     * @description:跳转到工单策划页面
     * @author: 王中华
     * @createDate: 2014-6-23
     * @return:
     */
    @RequestMapping(value = "/openWOPlanInfoPage")
    @ReturnEnumsBind("WO_FB_TYPE,WO_TYPE,WO_STATUS,WO_URGENCY_DEGREE,WO_INFLUENCE_SCOPE,WO_SPEC,WO_ISTOPTW,WO_HAS_REMAINFAULT,WO_FAULT_DEGREE")
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
        int woId = Integer.valueOf( userInfoScope.getParam( "workOrderId" ) );
        Map<String, Object> map = workOrderService.queryWOById( woId );
        WorkOrder workOrder = (WorkOrder) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        String approveFlag = (String) map.get( "approveFlag" );
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

            // TODO 查询策划或者汇报时领取的物资
            List<JPItems> jpPlanItemsList = new ArrayList<JPItems>();
            Page<InvMatApplyToWorkOrder> invItemVOList = invMatApplyService.queryMatApplyByWoId(
                    String.valueOf( woId ), "wo_picking" );
            List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();
            jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );

            JSONArray jpItemsListStr = new JSONArray();
            if ( jpPlanItemsList.size() > 0 ) { // 有申请领取的物资
                resultMap.remove( "toolData" ); // 先将作业方案中的物资剔除
                jpItemsListStr = MvcJsonUtil.JSONArrayFromList( jpPlanItemsList );
                resultMap.put( "toolData", jpItemsListStr.toString() );
            }
        }

        resultMap.put( "workOrderForm", JsonHelper.toJsonString( workOrder ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "candidateUsers", candidateUsers );
        if ( userInfoScope.getSiteId() != null && userInfoScope.getSiteId().equals( "SWF" ) ) {
            String isHighLevel = (String) map.get( "isHighLevel" );
            List<ArrayList<Object>> executorsArray = (List<ArrayList<Object>>) map.get( "executorsArray" );
            resultMap.put( "executorsArray", executorsArray );
            resultMap.put( "approveFlag", approveFlag );
            resultMap.put( "isHighLevel", isHighLevel );
        } else if ( userInfoScope.getSiteId() != null && userInfoScope.getSiteId().equals( "ZJW" ) ) {
            resultMap.put( "approveFlag", approveFlag );
            resultMap.put( "hasValidPtw", map.get( "hasValidPtw" ) );
            String toolData = (String) map.get( "toolData" );
            resultMap.put( "toolData", toolData );
            String relPtwIds = (String) map.get( "relPtwIds" );
            resultMap.put( "relPtwIds", relPtwIds );
            String relMatApplyIds = (String) map.get( "relMatApplyIds" );
            resultMap.put( "relMatApplyIds", relMatApplyIds );
        }
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
        int woId = Integer.valueOf( userInfoScope.getParam( "workOrderId" ) );
        Map<String, Object> map = workOrderService.queryItWOById( woId );
        WorkOrder workOrder = (WorkOrder) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        // TODO 节点的候选人 (此处有两种方法，从流程中取值有问题，从自己的业务表中取值可以)
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

        // TODO 领料单数据查询
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
            List<JPItems> jpPlanItemsList = new ArrayList<JPItems>();
            Page<InvMatApplyToWorkOrder> invItemVOList = invMatApplyService.queryMatApplyByWoId(
                    String.valueOf( woId ), "wo_picking" );
            List<InvMatApplyToWorkOrder> itemsList = invItemVOList.getResults();

            jpPlanItemsList = InvItemVOtoJPItemsUtil.invItemVOtoJPItem( itemsList );
            // JSONArray jpToolListStr =
            // MvcJsonUtil.JSONArrayFromList(jpPlanItemsList);

            resultMap.put( "toolData", jpPlanItemsList );
        }

        List<WoAttachment> attachList = woAttachmentService.queryWoAttachmentById( String.valueOf(woId), "WO" );
        // TODO 根据附件id，转化数据传前台，前台显示附件数据
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

    /**
     * @description:stopWorkOrder
     * @author:
     * @createDate:
     * @return
     * @throws
     */
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

        MaintainPlan tempMTP = maintainPlanService.queryMTPById( Integer.valueOf( mtpId ) );
        String principal = tempMTP.getPrincipal(); // 维护计划负责人

        // 周期性维护计划生成工单并启动流程
        if ( !principal.contains( userId ) ) {
            resultMap.put( "result", "notyours" );
            return resultMap;
        }

        workOrderService.cycMtpObjToWo( mtpId, todoId );
        // //删掉提醒待办
        // if((!"null".equals(todoId))&&(todoId != null)){
        // homepageService.Delete(todoId, userInfoScope);
        // }

        resultMap.put( "result", "success" );
        return resultMap;

    }

    /**
     * 用于搜索框的查询
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/faultTypeHint", method = RequestMethod.POST)
    public ModelAndViewAjax assetSearchHint() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String keyWord = userInfo.getParam( "kw" ).trim();
        keyWord = keyWord.toLowerCase();
        if ( keyWord != null && keyWord.length() > 0 ) {
            result = woFaultTypeService.queryFaultTypeForHint( keyWord, userInfo.getSiteId() );// 可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return itcMvcService.jsons( result );
    }

    //工单页面信息赋值
    private void setWoAddPageInfo(UserInfoScope userInfoScope, Map<String, Object> dataMap) {
    	//工单状态赋值给前台页面，使前后台保持一致
        setWoAddPageStatus(userInfoScope,dataMap);

    	//查询已选优先级项
    	WoPriority currentPriority = null;
		try {
			String woId = userInfoScope.getParam("woId");
			String woCode = userInfoScope.getParam("woCode");
			Map<String,Object> workOrderHashMap = null;
			if(woCode!=null){
				Map<String, Object> workOrderInfo = workOrderService.queryWOBaseInfoByWOCode(woCode, userInfoScope.getSiteId());
				WorkOrder workOrderTemp = (WorkOrder) workOrderInfo.get("workOrder");
				workOrderHashMap = workOrderService.queryWOById(workOrderTemp.getId());
		        dataMap.put( "woId", String.valueOf(workOrderTemp.getId()) );
			}
			else if(woId!=null){
				workOrderHashMap = workOrderService.queryWOById(Integer.valueOf(woId));
		        dataMap.put( "woId", woId );
			}
			if(workOrderHashMap!=null){
				WorkOrder workOrder = (WorkOrder) workOrderHashMap.get("bean");
				Integer priorityId = workOrder.getPriorityId();
				if(priorityId!=null){
					Map<String, Object> resultMap = woPriorityService.queryWoPriorityById(priorityId, userInfoScope.getSiteId());
					currentPriority = (WoPriority)resultMap.get("baseData");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		//查询可选优先级
        List<ArrayList<Object>> priorityList = getPriorityList(userInfoScope, currentPriority);
        String priorityArrayStr = JsonHelper.toJsonString( priorityList );
        dataMap.put( "priorityArray", priorityArrayStr );
	}
    
    //工单状态赋值给前台页面，使前后台保持一致
	private void setWoAddPageStatus(UserInfoScope userInfoScope, Map<String, Object> dataMap) {
		String siteId = userInfoScope.getSiteId();
		if("SWF".equals(siteId)){
	        dataMap.put( "draftStr", WoProcessStatusUtil.DRAFT_STR );
	        dataMap.put( "woCommitStr", WoProcessStatusUtil.WO_COMMIT_STR );
	        dataMap.put( "dutyConfirmDefectStr", WoProcessStatusUtil.DUTY_CONFIRM_DEFECT_STR );
	        dataMap.put( "expertDealStr", WoProcessStatusUtil.EXPERT_DEAL_STR );
	        dataMap.put( "planningGroupAuditStr", WoProcessStatusUtil.PLANNING_GROUP_AUDIT_STR );
	        dataMap.put( "safeClearAuditStr", WoProcessStatusUtil.SAFE_CLEAR_AUDIT_STR );//生安分部清除缺陷
	        dataMap.put( "dutyClearAuditStr", WoProcessStatusUtil.DUTY_CLEAR_AUDIT_STR );//值长清除缺陷缺陷
	        dataMap.put( "supervisorPlanStr", WoProcessStatusUtil.SUPERVISOR_PLAN_STR );
	        dataMap.put( "supervisorReportStr", WoProcessStatusUtil.SUPERVISOR_REPORT_STR );
	        dataMap.put( "delayEquipAuditStr", WoProcessStatusUtil.DEALY_EQUIP_AUDIT_STR );
	        dataMap.put( "delaySafeAuditStr", WoProcessStatusUtil.DELAY_SAFE_AUDIT_STR );
	        dataMap.put( "delayOperationAuditStr", WoProcessStatusUtil.DELAY_OPERATION_AUDIT_STR );
	        dataMap.put( "delayLeaderAuditStr", WoProcessStatusUtil.DELAY_LEADER_AUDIT_STR );
	        dataMap.put( "delayDutyAuditStr", WoProcessStatusUtil.DELAY_DUTY_AUDIT_STR );
	        dataMap.put( "delayDutyRestartStr", WoProcessStatusUtil.DELAY_DUTY_RESTART_STR );
	        dataMap.put( "delayConfirmOverStr", WoProcessStatusUtil.DUTY_CONFIRM_OVER_STR );//值长确认结束
	        dataMap.put( "obseleteStr", WoProcessStatusUtil.OBSELETE_STR );
	        dataMap.put( "doneStr", WoProcessStatusUtil.DONE_STR );
		}
		else if ("ZJW".equals(siteId)) {
	        dataMap.put( "DRAFT", WoProcessStatusUtilZJW.DRAFT );//0草稿
	        dataMap.put( "WORK_ORDER_COMMIT", WoProcessStatusUtilZJW.WORK_ORDER_COMMIT );//1工单提交
	        dataMap.put( "MONITOR_AUDIT", WoProcessStatusUtilZJW.MONITOR_AUDIT );//2班长审核
	        dataMap.put( "CHAIRMAN_AUDIT", WoProcessStatusUtilZJW.CHAIRMAN_AUDIT );//3场长(助理)审核
	        dataMap.put( "MONITOR_DISTRIBUTE", WoProcessStatusUtilZJW.MONITOR_DISTRIBUTE );//4班长分发
	        dataMap.put( "WORK_ORDER_PLAN", WoProcessStatusUtilZJW.WORK_ORDER_PLAN );//工作策划
	        dataMap.put( "WORK_TICKET_PROCEDURE", WoProcessStatusUtilZJW.WORK_TICKET_PROCEDURE );//6工作票流程
	        dataMap.put( "WORK_ORDER_REPORT", WoProcessStatusUtilZJW.WORK_ORDER_REPORT );//7填写故障缺陷处理单
	        dataMap.put( "WORK_ORDER_CHECK", WoProcessStatusUtilZJW.WORK_ORDER_CHECK );//8验收
	        dataMap.put( "SHIELD_OPR_DIRECTOR_AUDIT", WoProcessStatusUtilZJW.SHIELD_OPR_DIRECTOR_AUDIT );//9运检主管审批
	        dataMap.put( "SHIELD_OPR_MINISTER_AUDIT", WoProcessStatusUtilZJW.SHIELD_OPR_MINISTER_AUDIT );//10运检部长审批
	        dataMap.put( "SHIELD_SAFE_CLERK_AUDIT", WoProcessStatusUtilZJW.SHIELD_SAFE_CLERK_AUDIT );//11生安专责审批
	        dataMap.put( "SHIELD_SAFE_MINISTER_AUDIT", WoProcessStatusUtilZJW.SHIELD_SAFE_MINISTER_AUDIT );//12生安部部长审批
	        dataMap.put( "SHIELD_DUTY_RESET", WoProcessStatusUtilZJW.SHIELD_DUTY_RESET );//13值长复位
	        dataMap.put( "SUSPEND", WoProcessStatusUtilZJW.SUSPEND );//14挂起
	        dataMap.put( "OBSELETE", WoProcessStatusUtilZJW.OBSELETE );//15作废
	        dataMap.put( "DONE", WoProcessStatusUtilZJW.DONE );//16完成
		}
	}

	//查询工单优先级信息
    private List<ArrayList<Object>> getPriorityList(UserInfoScope userInfoScope, WoPriority currentPriority) {
        //查询可选有效优先级
    	String siteId = userInfoScope.getSiteId();
        Page<WoPriority> page = userInfoScope.getPage();
        page.setPageSize( 1000 );
        page.setSortKey( "SORT_NUM" );
        page.setSortOrder( "ASC" );
        page.setParameter( "siteid", siteId );
        List<WoPriority> priorityList = woPriorityService.queryWoPriorityList( page ).getResults();
		
        //已选优先级项如果无效则加入作为数据源，保证查看时显示正常
		if(currentPriority!=null && currentPriority.getYxbz()==0){
			priorityList.add(currentPriority);
		}
		
        //将优先级数组转换为各站点下的combobox数据
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        for ( WoPriority priority : priorityList ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add( priority.getId() );
            double solveLength = priority.getSolveLength();
            if ( solveLength == -1 ){
            	if("SWF".equals(siteId)){
                    row.add( priority.getName() + "    (解决时长无穷大)" );
            	}
            	else{
                    row.add( priority.getName());
            	}
            }
            else{
            	if("SWF".equals(siteId)){
                    row.add( priority.getName() + "    (解决时长" + (int) solveLength + "小时)" );
            	}
            	else{
                    row.add( priority.getName());
            	}
            }
            result.add( row );
        }
        return result;
    }

}
