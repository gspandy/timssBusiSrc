package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.itsm.bean.ItsmKnowledge;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.service.ItsmKnowledgeService;
import com.timss.itsm.service.ItsmQuestionRdService;
import com.timss.itsm.service.ItsmWoAttachmentService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "/itsm/knowledge")
public class ItsmKnowledgeController {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmKnowledgeService itsmKnowledgeService;
    @Autowired
    private ItsmWoAttachmentService woAttachmentService;
    @Autowired
    private ItsmQuestionRdService questionRdService;

    private static final Logger LOG = Logger.getLogger( ItsmKnowledgeController.class );

    /**
     * @description:标识知识单
     * @author: 王中华
     * @createDate: 2015-4-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/knowledgeList", method = RequestMethod.GET)
    @ReturnEnumsBind("ITSM_KL_SOURCE,ITSM_KL_STATUS")
    public String normKnowledgeList() throws Exception {
        return "/knowledgeBase/knowledgeList.jsp";
    }

    /**
     * @description:打开知识单页面
     * @author: 王中华
     * @createDate: 2015-4-21
     * @return:
     */
    @RequestMapping(value = "/openKnowledgePage")
    @ReturnEnumsBind("ITSM_KL_SOURCE,ITSM_KL_STATUS")
    public String openKnowledgePage() {
        return "/knowledgeBase/knowledge.jsp";
    }

    @RequestMapping(value = "/knowledgeListData")
    @ReturnEnumsBind("ITSM_KL_SOURCE,ITSM_KL_STATUS")
    public Page<ItsmKnowledge> knowledgeListData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ItsmKnowledge> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "itsmknowledgeMap",
                ItsmConstant.MODULENAME, "ItsmKnowledgeDao" );

        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
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

        page = itsmKnowledgeService.queryItsmKnowledgeList( page );

        return page;
    }

    @RequestMapping(value = "/commitKnowledgedata", method = RequestMethod.POST)
    public Map<String, String> commitKnowledge() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String knowledgeFormDate = userInfoScope.getParam( "knowledgeForm" );// 获取前台传过来的form表单数据
        ItsmKnowledge knowledge = JsonHelper.toObject( knowledgeFormDate, ItsmKnowledge.class );
        int klId = knowledge.getId();
        String knowledgeCodeString = knowledge.getKnowledgeCode();
        String workflowId = knowledge.getWorkflowId();
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" ); // 附件编号

        Map<String, String> addKLDataMap = new HashMap<String, String>();
        addKLDataMap.put( "knowledgeData", knowledgeFormDate );
        // plan：工单策划；actual:实际
        addKLDataMap.put( "commitStyle", commitStyle );
        addKLDataMap.put( "uploadIds", uploadIds ); // 附件ID

        String taskId = "noFlow";
        if ( workflowId == "" || workflowId == null ) { // 确定不是因为回退的节点
            if ( klId == 0 ) { // 初次提交或暂存
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    Map<String, Object> insertResultMap = itsmKnowledgeService.insertItsmKnowledge( addKLDataMap );
                    taskId = insertResultMap.get( "taskId" ).toString();
                    klId = Integer.valueOf( insertResultMap.get( "klId" ).toString() );
                    knowledgeCodeString = insertResultMap.get( "knowledgeCode" ).toString();
                    workflowId = insertResultMap.get( "workflowId" ).toString();
                    LOG.info( "-------------web层：工单提交完成,工单编号：" + "工单流程：" + workflowId + "-------------" );
                } else if ( "save".equals( commitStyle ) ) { // 暂存，不启动流程

                    LOG.info( "-------------web层：工单暂存完成,工单编号：" + "-------------" );
                }
            } else { // 对暂存的工单提交 或 再次暂存 或其他状态下的提交
                if ( "commit".equals( commitStyle ) ) { // 提交，启动流程
                    addKLDataMap.put( "updateStyle", "commit" );
                } else if ( "save".equals( commitStyle ) ) { // 再次暂存，不启动流程
                    addKLDataMap.put( "updateStyle", "save" );
                }

                LOG.info( "-------------web层：暂存的工单再次提交或暂存完成,工单流程：" + "-------------" );
            }
        } else {
            LOG.info( "-------------web层：回退的工单再次提交或暂存开始,工单编号：" + "-------------" );

            LOG.info( "-------------web层：回退的工单再次提交或暂存完成,工单编号：" + "-------------" );
            // 获取当前活动节点

        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        if ( "save".equals( commitStyle ) ) {
            taskId = "noFlow";
        }
        mav.put( "taskId", taskId );
        mav.put( "knowledgeId", String.valueOf( klId ) );
        mav.put( "knowledgeCode", knowledgeCodeString );
        mav.put( "workflowId", workflowId );
        return mav;
    }

    @RequestMapping(value = "/queryKnowledgeById")
    public @ResponseBody
    Map<String, Object> queryKnowledgeById() throws Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int knowledgeId = Integer.valueOf( userInfoScope.getParam( "itsmKnowledgeId" ) );
        Map<String, Object> map = itsmKnowledgeService.queryItsmKnowledgeById( knowledgeId );
        ItsmKnowledge itsmKnowledge = (ItsmKnowledge) map.get( "bean" );
        String taskId = (String) map.get( "taskId" );
        List<String> candidateUsers = (List<String>) map.get( "candidateUsers" );

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<ItsmWoAttachment> attachList = woAttachmentService.queryWoAttachmentById( String.valueOf( knowledgeId ), "KL" );
        // 根据附件id，转化数据传前台，前台显示附件数据
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        resultMap.put( "attachmentMap", attachmentMap );

        resultMap.put( "itsmKnowledgeForm", JsonHelper.toJsonString( itsmKnowledge ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "candidateUsers", candidateUsers );

        return resultMap;

    }

    @RequestMapping(value = "/obsoleteKnowledge", method = RequestMethod.POST)
    public Map<String, String> obsoleteKnowledge() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String klIdString = userInfoScope.getParam( "klId" );// 获取前台传过来的form表单数据
        int klId = Integer.valueOf( klIdString );

        itsmKnowledgeService.obsoleteWorkOrder( klId );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:将知识单的ID回填到问题单中
     * @author: 王中华
     * @createDate: 2015-6-11
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/knowledgeIdAddToPromble", method = RequestMethod.POST)
    public Map<String, String> knowledgeIdAddToPromble() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String klIdString = userInfoScope.getParam( "knowledgeId" );// 获取前台传过来的form表单数据
        String prombleIdString = userInfoScope.getParam( "knowledgeId" );
        int prombleId = Integer.valueOf( prombleIdString );
        int klId = Integer.valueOf( klIdString );

        questionRdService.knowledgeIdAddToPromble( prombleId, klId );

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
}
