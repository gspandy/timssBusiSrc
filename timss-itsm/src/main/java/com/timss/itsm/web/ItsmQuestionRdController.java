package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.service.ItsmJobPlanService;
import com.timss.itsm.service.ItsmMaintainPlanService;
import com.timss.itsm.service.ItsmQuestionRdService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.vo.ItsmQuestionRdVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ItsmQuestionRdController.java
 * @author: gucw
 * @createDate: 2015-6-1
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/itsm/questionrd")
public class ItsmQuestionRdController {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmQuestionRdService questionRdService;
    @Autowired
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

    private static final Logger LOG = Logger.getLogger( ItsmQuestionRdController.class );

    /**
     * @description: 打开问题列表页面
     * @author: gucw
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/questionrdList", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_URGENCY_DEGREE,ITSM_QUESTION_STATUS,ITSM_QUESTION_STEP,ITSM_QUESTION_SOURCE")
    public String questionRdList() {
        return "/questionBase/questionrdList.jsp";
    }

    /**
     * @Title: selectNextStep
     * @Description: 转发到下一步操作页面
     * @return
     */
    @RequestMapping(value = "/selectNextStep")
    public ModelAndView selectNextStep() {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        String index = "";
        try {
            index = infoScope.getParam( "index" );
        } catch (Exception e) {
            LOG.warn( "selectNextStep获取参数异常", e );
            throw new RuntimeException( e );
        }
        map.put( "index", index );
        ModelAndView modelAndView = new ModelAndView( "/questionBase/selectNextStep.jsp", map );
        return modelAndView;
    }

    /**
     * @description: 跳转到新建问题页面
     * @author: gucw
     * @createDate: 2015-6-1
     * @return:
     */
    @RequestMapping(value = "/openNewQuestionPage")
    @ReturnEnumsBind("ITSM_URGENCY_DEGREE,ITSM_QUESTION_STATUS,ITSM_QUESTION_STEP,ITSM_QUESTION_SOURCE")
    public ModelAndView openNewQuestionPage() {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        SecureUser user = infoScope.getSecureUser();
        String userId = user.getId();
        String userName = user.getName();
        String deptName = infoScope.getOrgName();
        String categoryParams = "";
        String categoryId = "";
        String categoryName = "";
        try {
            categoryParams = infoScope.getParam( "categoryParams" );
            Map<String, Object> categoryParamsMap = JsonHelper.fromJsonStringToBean( categoryParams, HashMap.class );
            categoryId = categoryParamsMap.get( "categoryId" ).toString();
            categoryName = categoryParamsMap.get( "categoryName" ).toString();
        } catch (Exception e) {
            LOG.warn( "openNewQuestionPage获取参数异常", e );
            throw new RuntimeException( e );
        }
        map.put( "userId", userId );
        map.put( "userName", userName );
        map.put( "deptName", deptName );
        map.put( "createUserInfo", userName + "/" + deptName );
        map.put( "questionId", "null" );
        map.put( "categoryId", categoryId );
        map.put( "categoryName", categoryName );
        ModelAndView modelAndView = new ModelAndView( "/questionBase/newQuestion.jsp", map );
        return modelAndView;
    }

    /**
     * @description: "待办"中途跳转到
     * @author: gucw
     * @createDate: 2015-6-1
     * @return:
     */
    @RequestMapping(value = "/todolistTOQuestionPage")
    @ReturnEnumsBind("ITSM_URGENCY_DEGREE,ITSM_QUESTION_STATUS,ITSM_QUESTION_STEP,ITSM_QUESTION_SOURCE")
    public ModelAndView todolistTOQuestionPage() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单
        map.put( "questionId", id );
        String jumpUrl = "/questionBase/newQuestion.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }

    /**
     * @description: 查询工单列表数据
     * @author: gucw
     * @createDate: 2015-6-1
     * @return
     * @throws Exception
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/questionRdListData", method = RequestMethod.POST)
    public Page<ItsmQuestionRd> workOrderListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        Page<ItsmQuestionRd> page = userInfoScope.getPage();
        String selectTreeId = userInfoScope.getParam( "selectTreeId" );
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "questionRdMap",
                ItsmConstant.MODULENAME, "ItsmQuestionRdDao" );
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
        if ( selectTreeId != null && !"null".equals( selectTreeId ) ) {
            page.setParameter( "selectTreeId", selectTreeId ); // 设置树查询参数
        }
        page = questionRdService.queryQuestionRd( page );

        return page;
    }

    /**
     * @description: 提交“问题”数据
     * @author: gucw
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitQuestiondata", method = RequestMethod.POST)
    public Map<String, Object> commitQuestion() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String questionFormDate = userInfoScope.getParam( "questionForm" );// 获取前台传过来的form表单数据
        ItsmQuestionRdVo questionRdVo = JsonHelper.toObject( questionFormDate, ItsmQuestionRdVo.class );
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" );
        boolean startWorkFlow = false;
        if ( !"save".equals( commitStyle ) ) {
            startWorkFlow = true;
        }
        Map<String, Object> mav = new HashMap<String, Object>( 0 );
        questionRdVo.setAttach( uploadIds );
        mav = questionRdService.saveOrUpdateQuestionRd( questionRdVo, startWorkFlow );
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description: 提交问题后，启动了流程，弹出审批框，点击取消 1.删掉流程 2.清空工单信息中流程ID
     * @author: gucw
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/cancelCommitQuestion", method = RequestMethod.POST)
    public Map<String, String> cancelCommitQuestion() throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单数据
        questionRdService.cancelCommitQuestionRd( Integer.valueOf( id ) );
        return result;
    }

    /**
     * @description:删除草稿
     * @author: 王中华
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteQuestionDraft", method = RequestMethod.POST)
    public Map<String, String> deleteQuestionDraft() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int id = Integer.valueOf( userInfoScope.getParam( "id" ) );// 获取前台传过来的form表单数据
        questionRdService.deleteQuestionRd( id );
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description: 作废工单，仅工单发起人可以作废
     * @author: 王中华
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteQuestion", method = RequestMethod.POST)
    public Map<String, String> obsoleteQuestion() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        questionRdService.obsoleteQuestionRd( Integer.valueOf( userInfoScope.getParam( "id" ) ) );// 获取前台传过来的form表单数据
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:查询问题数据
     * @author: 王中华
     * @createDate: 2015-6-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryItQuestionDataById")
    public @ResponseBody
    Map<String, Object> queryItQuestionDataById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int id = Integer.valueOf( userInfoScope.getParam( "id" ) );
        Map<String, Object> map = questionRdService.queryQuestionRdById( id );
        ItsmQuestionRd questionRd = (ItsmQuestionRd) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        // 节点的候选人
        List<String> candidateUsers = new ArrayList<String>( 0 );
        if ( StringUtils.isNotEmpty( taskId ) ) {
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ItsmWoAttachment> attachList = woAttachmentService.queryWoAttachmentById( String.valueOf( id ), "QUESTION" );
        // 根据附件id，转化数据传前台，前台显示附件数据
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        resultMap.put( "attachmentMap", attachmentMap );
        resultMap.put( "questionForm", JsonHelper.toJsonString( questionRd ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "candidateUsers", candidateUsers );
        return resultMap;
    }

    /**
     * @Title:stopQuestion
     * @Description:终止问题
     * @param taskId
     * @param assignee
     * @param owner
     * @param message
     * @param businessId
     * @throws Exception
     * @return Map<String,String>
     * @throws
     */
    @RequestMapping(value = "/stopQuestion", method = RequestMethod.POST)
    public Map<String, String> stopQuestion(@RequestParam("taskId") String taskId,
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
        questionRdService.stopQuestionRd( params );
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    // **--不清楚有没有对应的现成的类存放相关的方法 暂时寄放在本controller
    @RequestMapping("/queryWorkOrderByName")
    public ModelAndViewAjax querySupplierFuzzyByName(String kw) {
        LOG.info( "通过名称查询工单：" + kw );
        List<Map<String, Object>> result = questionRdService.queryWOFuzzyByName( kw );
        LOG.info( "成功通过名称查询工单：" + kw );
        return ViewUtil.Json( result );
    }

}
