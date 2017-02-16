package com.timss.inventory.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvStocktaking;
import com.timss.inventory.bean.InvStocktakingDetail;
import com.timss.inventory.service.InvStocktakingService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvStocktakingVO;
import com.yudean.homepage.bean.WorktaskBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingController.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invstocktaking")
public class InvStocktakingController {

    /**
     * service 注入
     */
    @Autowired
    private InvStocktakingService invStocktakingService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private HomepageService homepageService;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvStocktakingController.class );

    /**
     * @description:列表页面跳转
     * @author: 890166
     * @createDate: 2014-9-30
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/invStocktakingList", method = RequestMethod.GET)
    public ModelAndView invStocktakingList() {
        ModelAndView mav = new ModelAndView( "/invstocktaking/invStocktakingList.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        mav.addObject( "siteid", siteId );
        return mav;
    }

    /**
     * @description:表单页面跳转
     * @author: 890166
     * @createDate: 2014-9-30
     * @param imaid
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/invStocktakingForm", method = RequestMethod.GET)
    public ModelAndView invStocktakingForm(@RequestParam String istid) throws Exception {
        ModelAndView mav = new ModelAndView( "/invstocktaking/invStocktakingForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        String defKey = "inventory_[@@@]_invstocktaking".replace( "[@@@]", siteId.toLowerCase() );
        mav.addObject( "defKey", defKey );
        mav.addObject( "siteid", siteId );

        // 查询库存盘点开关
        boolean switchFlag = false;
        Configuration cf = itcMvcService.getConfiguration( "store_check_switch" );
        if ( null != cf ) {
            switchFlag = Boolean.valueOf( cf.getVal() == null ? "false" : cf.getVal() );
        }
        mav.addObject( "switchFlag", switchFlag );

        if ( null != istid && !"".equals( istid ) ) {

            // modify by yuanzh 20160104 将查询流程环节的动作放在有sheetNo的地方
            String sheetNo = invStocktakingService.queryFlowNoByIstid( istid, siteId );
            WorktaskBean wtBean = homepageService.getOneTaskByFlowNo( sheetNo, userInfo );
            if ( null != wtBean ) {
                mav.addObject( "classType", wtBean.getClasstype().toString() );
            }

            List<InvStocktakingVO> isvList = invStocktakingService.queryStocktakingForm( userInfo, istid );
            if ( !isvList.isEmpty() ) {
                InvStocktakingVO isv = isvList.get( 0 );

                String status = isv.getEnStatus();
                mav.addObject( "status", status );

                if ( switchFlag ) {
                    String instanceid = isv.getInstanceid();
                    mav.addObject( "processInstId", instanceid );
                    if ( null == instanceid || "".equals( instanceid ) ) {
                        mav.addObject( "isEdit", "editable" );
                    }
                    if ( !"".equals( instanceid ) && null != instanceid ) {
                        List<Task> activities = workflowService.getActiveTasks( instanceid );
                        if ( null != activities && !activities.isEmpty() ) {
                            Task task = activities.get( 0 );

                            mav.addObject( "process", task.getTaskDefinitionKey() );
                            mav.addObject( "processName", task.getName() );

                            List<String> userIdList = workflowService.getCandidateUsers( task.getId() );
                            for ( String userId : userIdList ) {
                                if ( userId.equals( userInfo.getUserId() ) ) {
                                    mav.addObject( "isEdit", "editable" );
                                    break;
                                }
                            }
                            mav.addObject( "taskId", task.getId() );

                            Map<String, String> processAttr = workflowService.getElementInfo( task.getId() );
                            mav.addObject( "oper", processAttr.get( "modifiable" ) );
                        }
                    }
                } else {
                    mav.addObject( "isEdit", "editable" );
                }
            }

            List<Map<String, Object>> fileMap = invStocktakingService.queryStocktakingAttach( istid );
            JSONArray jsonArray = JSONArray.fromObject( fileMap );
            mav.addObject( "uploadFiles", jsonArray );
        } else {
            mav.addObject( "isEdit", "editable" );
        }
        return mav;
    }

    /**
     * @description:列表查询
     * @author: 890166
     * @createDate: 2014-9-30
     * @param search
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryStocktakingList", method = RequestMethod.POST)
    public Page<InvStocktakingVO> queryStocktakingList(String search) throws Exception {
        InvStocktakingVO isv = new InvStocktakingVO();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            isv = JsonHelper.fromJsonStringToBean( search, InvStocktakingVO.class );
        }
        return invStocktakingService.queryStocktakingList( userInfo, isv );
    }

    /**
     * @description:查询盘点表单数据
     * @author: 890166
     * @createDate: 2014-10-8
     * @param imaid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryStocktakingForm", method = RequestMethod.POST)
    public InvStocktakingVO queryStocktakingForm(String istid) throws Exception {
        InvStocktakingVO ist = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvStocktakingVO> istList = invStocktakingService.queryStocktakingForm( userInfo, istid );
        if ( null != istList && !istList.isEmpty() ) {
            ist = istList.get( 0 );
        }
        return ist;
    }

    /**
     * @description:保存信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param formData
     * @param listData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveStocktaking", method = RequestMethod.POST)
    public Map<String, Object> saveStocktaking(String formData, String listData, String taskId, String istid,
            String uploadIds) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<InvStocktakingDetail> istdList = new ArrayList<InvStocktakingDetail>();

        boolean flag = true;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "istid", istid );
        paramMap.put( "taskId", taskId );

        uploadIds = uploadIds.replace( "\"", "" );
        paramMap.put( "uploadIds", uploadIds );

        // 表单数据转换
        InvStocktaking ist = JsonHelper.fromJsonStringToBean( formData, InvStocktaking.class );
        if ( null != listData ) {
            // 列表数据转换
            istdList = CommonUtil.conventJsonToInvStocktakingDetailList( listData );
        }
        // 保存操作
        reMap = commitApply( userInfo, ist, istdList, paramMap );

        flag = Boolean.valueOf( reMap.get( "flag" ) == null ? "false" : String.valueOf( reMap.get( "flag" ) ) );
        istid = String.valueOf( reMap.get( "istid" ) == null ? "" : String.valueOf( reMap.get( "istid" ) ) );
        String processId = String.valueOf( reMap.get( "processId" ) == null ? "" : String.valueOf( reMap
                .get( "processId" ) ) );

        result.put( "istid", istid );
        result.put( "processInstanceId", processId );
        result.put( "taskId", paramMap.get( "taskId" ) );
        result.put( "switchFlag", reMap.get( "switchFlag" ) );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description:流程提交
     * @author: 890166
     * @createDate: 2014-7-29
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitApply", method = RequestMethod.POST)
    private Map<String, Object> commitApply(UserInfoScope userInfo, InvStocktaking ist,
            List<InvStocktakingDetail> istdList, Map<String, Object> paramMap) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        ProcessInstance p = null;
        String processId = null;
        Task task = null;
        String taskId = paramMap.get( "taskId" ) == null ? "" : String.valueOf( paramMap.get( "taskId" ) );
        String siteId = userInfo.getSiteId();

        // 查询库存盘点开关
        boolean switchFlag = false;
        Configuration cf = itcMvcService.getConfiguration( "store_check_switch" );
        if ( null != cf ) {
            switchFlag = Boolean.valueOf( cf.getVal() == null ? "false" : cf.getVal() );
        }

        if ( switchFlag ) {
            if ( "".equals( taskId ) ) {
                String defKey = "inventory_[@@@]_invstocktaking".replace( "[@@@]", siteId.toLowerCase() );
                String processKey = workflowService.queryForLatestProcessDefKey( defKey );
                p = workflowService.startLatestProcessInstanceByDefKey( processKey, userInfo.getUserId(), null );

                processId = p.getProcessInstanceId();
                List<Task> taskList = workflowService.getActiveTasks( processId );
                if ( null != taskList && !taskList.isEmpty() ) {
                    task = taskList.get( 0 );
                    paramMap.put( "taskId", task.getId() );
                }
                paramMap.put( "status", CommonUtil.getProperties( "draftProcess" ) );
            } else {
                task = workflowService.getTaskByTaskId( taskId );
                processId = task.getProcessInstanceId();
            }
            paramMap.put( "processId", processId );
        }

        reMap = invStocktakingService.saveStocktaking( userInfo, ist, istdList, paramMap );

        if ( switchFlag ) {
            workflowService.setVariable( processId, "totalPrice",
                    new BigDecimal( String.valueOf( reMap.get( "totalPrice" ) ) ) );
            if ( "".equals( taskId ) ) {
                homepageService.createProcess( ist.getSheetno(), task.getProcessInstanceId(), task.getName(),
                        ist.getSheetname(), task.getName(),
                        "/inventory/invstocktaking/invStocktakingForm.do?taskId=" + task.getId() + "&processInstId="
                                + task.getProcessInstanceId() + "&istid=" + ist.getIstid(), userInfo, null );
            }
        }
        reMap.put( "switchFlag", switchFlag );
        return reMap;
    }

    /**
     * @description:更新当前库存数量
     * @author: 890166
     * @createDate: 2014-11-17
     * @param listData
     * @param istid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/changeInvStock", method = RequestMethod.POST)
    public Map<String, Object> changeInvStock(String istid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        Map<String, Object> reMap = new HashMap<String, Object>();

        boolean flag = true;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "istid", istid );
        // 列表数据转换
        // 保存操作
        reMap = invStocktakingService.changeInvStock( userInfo, paramMap );
        flag = Boolean.valueOf( reMap.get( "flag" ) == null ? "false" : String.valueOf( reMap.get( "flag" ) ) );

        boolean switchFlag = false;
        Configuration cf = itcMvcService.getConfiguration( "store_check_switch" );
        if ( null != cf ) {
            switchFlag = Boolean.valueOf( cf.getVal() == null ? "false" : cf.getVal() );
        }
        result.put( "switchFlag", switchFlag );

        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description: 终止流程
     * @author: 890166
     * @createDate: 2015-2-28
     * @param taskId
     * @param message
     * @param sheetId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/stopProcess", method = RequestMethod.POST)
    public Map<String, Object> stopProcess(@RequestParam("taskId") String taskId,
            @RequestParam("message") String message, @RequestParam("sheetId") String sheetId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String curUser = userInfo.getUserId();
        try {
            InvStocktaking ist = new InvStocktaking();
            ist.setIstid( sheetId );
            ist.setStatus( "stop" );
            invStocktakingService.updateStocktaking( userInfo, ist );

            Configuration cf = itcMvcService.getConfiguration( "store_check_switch" );
            if ( null != cf ) {
                boolean switchFlag = Boolean.valueOf( cf.getVal() == null ? "false" : cf.getVal() );
                if ( switchFlag ) {
                    workflowService.stopProcess( taskId, curUser, curUser, message );
                }
            }
            result.put( "result", "success" );
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvStocktakingController 中的 stopProcess 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
            result.put( "result", "error" );
        }
        return result;
    }
}
