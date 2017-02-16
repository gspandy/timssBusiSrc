package com.timss.inventory.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvAttachMapping;
import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvMatRecipientsService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsController.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmatrecipients")
public class InvMatRecipientsController {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private HomepageService homepageService;

    @Autowired
    private InvMatRecipientsService invMatRecipientsService;

    @Autowired
    private InvMatApplyService invMatApplyService;
    
    @Autowired
    private InvMatApplyDetailService invMatApplyDetailService;

    /**
     * @description:通过领用申请自动生成领用待办（只做新建操作）
     * @author: 890166
     * @createDate: 2014-9-28
     * @param formData
     * @param listData
     * @param imaid
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/autoGenerateConsuming", method = RequestMethod.POST)
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map<String, Object> autoGenerateConsuming(String formData, String listData, String imaid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();// 方法返回map
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        Map<String, Object> reMap = new HashMap<String, Object>();// 保存操作后返回map

        // 获取页面传来的参数
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();

        paramMap.put( "imaid", imaid );

        // 先更新一下最新的库存数量
        invMatApplyDetailService.updateNewStockQtyByImaid( userInfo, imaid );

        // 表单信息转换成实体
        InvMatApply ima = JsonHelper.fromJsonStringToBean( formData, InvMatApply.class );
        // 表单list转成成javalist
        List<InvMatApplyDetailVO> imaList = CommonUtil.conventJsonToInvMatApplyDetailVOList( listData );

        // 设置流程候选人
        List<String> list = new ArrayList<String>();
        list.add( ima.getCreateuser() ); // 领料申请人
        IAuthorizationManager am = (SecurityBeanHelper.getInstance()).getBean( IAuthorizationManager.class );
        String candidateRole = null;
        List<AppEnum> aeList = itcMvcService.getEnum( "INV_LIMIT_BY_TYPE" );
        if ( null != aeList && !aeList.isEmpty() ) {
            for ( AppEnum ae : aeList ) {
                if ( "ALL".equals( ae.getCode() ) ) {
                    candidateRole = ae.getLabel();
                    break;
                } else if ( ima.getApplyType().equals( ae.getCode() ) ) {
                    candidateRole = ae.getLabel();
                    break;
                }
            }
        }
        List<SecureUser> usList = am.retriveUsersWithSpecificRole( candidateRole, null, true, true );
        if ( !usList.isEmpty() ) {
            for ( SecureUser us : usList ) {
                list.add( us.getId() ); // 仓管员
            }
        }

        //生物质站点根据类型生成多张发料单
        if("SWF".equals(userInfo.getSiteId())){
            List<String> invCateIdList = new ArrayList<String>();
            //查询提交的发料申请中的所有类型
            for (InvMatApplyDetailVO imad : imaList) {
            	if(!invCateIdList.contains(imad.getInvcateid())){
            		invCateIdList.add(imad.getInvcateid());
            	}
			}
        	//根据每个类型生成多张发料单
            List<InvMatApplyDetailVO> imadList = new ArrayList<InvMatApplyDetailVO>();
            for (int i = 0; i < invCateIdList.size(); i++) {
            	String outterInvCateId = invCateIdList.get(i);
            	imadList.clear();
            	//查询当前类型的所有物资
                for (InvMatApplyDetailVO imad : imaList) {
                	String innerInvCateId = imad.getInvcateid();
                	if(innerInvCateId!=null && innerInvCateId.equals(outterInvCateId) && (imad.getOutstockqty().floatValue()>0)){
                		imadList.add(imad);
                	}
    			}
                
                // 获取流程定义
                String defKey = "inventory_[@@@]_invmatrecipients".replace( "[@@@]", siteId.toLowerCase() );
                // 查询最新流程
                String processKey = workflowService.queryForLatestProcessDefKey( defKey );
                // 创建流程
                ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey( processKey, ima.getCreateuser(), null );
                // 获取新建流程实例id和环节信息
                String processId = p.getProcessInstanceId();
                paramMap.put( "processId", processId );
                Task task = null;
                List<Task> taskList = workflowService.getActiveTasks( processId );
                if ( null != taskList && !taskList.isEmpty() ) {
                    task = taskList.get( 0 );
                }
                
                //开始生成发料单和待办
                if ( null != task && !list.isEmpty() && !imadList.isEmpty()) {
                    workflowService.addCandidateUsers( task.getId(), list );
                    // 保存并新建领用信息
                    reMap = invMatRecipientsService.autoGenerateConsuming( userInfo, ima, imadList, paramMap );

                    // 或保存成功将会获取到领用主信息
                    InvMatRecipients imr = (InvMatRecipients) reMap.get( "imr" );
                    if ( null != imr ) {
                        UserInfo uiCreate = itcMvcService.getUserInfoById( ima.getCreateuser() );
                        homepageService.createProcess( imr.getSheetno(), task.getProcessInstanceId(), task.getName(),
                                task.getName() + "(" + imr.getSheetname() + ")", task.getName(),
                                "/inventory/invmatrecipients/invMatRecipientsForm.do?taskId=" + task.getId()
                                        + "&processInstId=" + task.getProcessInstanceId() + "&imrid=" + imr.getImrid(),
                                uiCreate, null );
                        homepageService.Process( task.getId(), processId, task.getName(), list, userInfo, new ProcessFucExtParam());
                        result.put( "result", "success" );
                    } else {
                        result.put( "result", "false" );
                    }
                }
			}
        }
        else{
            // 获取流程定义
            String defKey = "inventory_[@@@]_invmatrecipients".replace( "[@@@]", siteId.toLowerCase() );
            // 查询最新流程
            String processKey = workflowService.queryForLatestProcessDefKey( defKey );
            // 创建流程
            ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey( processKey, ima.getCreateuser(), null );
            // 获取新建流程实例id和环节信息
            String processId = p.getProcessInstanceId();
            paramMap.put( "processId", processId );
            Task task = null;
            List<Task> taskList = workflowService.getActiveTasks( processId );
            if ( null != taskList && !taskList.isEmpty() ) {
                task = taskList.get( 0 );
            }
            
            //开始生成发料单和待办
            if ( null != task && !list.isEmpty() ) {
                workflowService.addCandidateUsers( task.getId(), list );
                // 保存并新建领用信息
                reMap = invMatRecipientsService.autoGenerateConsuming( userInfo, ima, imaList, paramMap );
                
                // 或保存成功将会获取到领用主信息
                InvMatRecipients imr = (InvMatRecipients) reMap.get( "imr" );
                if ( null != imr ) {
                    UserInfo uiCreate = itcMvcService.getUserInfoById( ima.getCreateuser() );
                    homepageService.createProcess( imr.getSheetno(), task.getProcessInstanceId(), task.getName(),
                            task.getName() + "(" + imr.getSheetname() + ")", task.getName(),
                            "/inventory/invmatrecipients/invMatRecipientsForm.do?taskId=" + task.getId()
                                    + "&processInstId=" + task.getProcessInstanceId() + "&imrid=" + imr.getImrid(),
                            uiCreate, null );
                    homepageService.Process( task.getId(), processId, task.getName(), list, userInfo, new ProcessFucExtParam() );
                    result.put( "result", "success" );
                } else {
                    result.put( "result", "false" );
                }
            }
        }

        return result;
    }

    /**
     * @description: 物资领料表单跳转
     * @author: 890166
     * @createDate: 2014-7-27
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/invMatRecipientsForm", method = RequestMethod.GET)
    public ModelAndView invMatRecipientsForm(@RequestParam String imrid) throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatrecipients/invMatRecipientsForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        String defKey = "inventory_[@@@]_invmatrecipients".replace( "[@@@]", siteId.toLowerCase() );

        mav.addObject( "siteid", siteId );
        mav.addObject( "defKey", defKey );
        if ( null != imrid && !"".equals( imrid ) ) {
            List<InvMatRecipientsVO> imrList = invMatRecipientsService.queryInvMatRecipientsForm( userInfo, imrid );
            if ( !imrList.isEmpty() ) {
                InvMatRecipientsVO imr = imrList.get( 0 );
                String instanceid = imr.getInstanceid();
                mav.addObject( "processInstId", instanceid );

                String imaid = imr.getImaid();
                mav.addObject( "imaid", imaid );
                
                //查询领料申请人信息
                List<InvMatApplyVO> imaList = invMatApplyService.queryMatApplyForm(userInfo, imaid, "1");
        	    if ( !imaList.isEmpty() ) {
	        		InvMatApplyVO ima = imaList.get( 0 );
	        		String imaCreateUserId = ima.getCreateuser();
	        		UserInfo imaCreateUser = itcMvcService.getUserInfoById(imaCreateUserId);
	        		String imaCreateUserName = imaCreateUser.getUserName();
	        		String imaCreateUserDept = imaCreateUser.getOrgId();
	                mav.addObject( "imaCreateUserId", imaCreateUserId + "_" + imaCreateUserDept );
	                mav.addObject( "imaCreateUserName", imaCreateUserName );
        	    }
       	    
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
                            }
                        }
                        mav.addObject( "taskId", task.getId() );
                        Map<String, String> processAttr = workflowService.getElementInfo( task.getId() );
                        mav.addObject( "oper", processAttr.get( "modifiable" ) );
                    }
                    List<Map<String, Object>> fileMap = invMatRecipientsService.queryMatRecipientsAttach(imrid);
                    JSONArray jsonArray = JSONArray.fromObject( fileMap );
                    mav.addObject( "uploadFiles", jsonArray ); 
                }
            }
        } else {
            mav.addObject( "isEdit", "editable" );
        }
        return mav;
    }

    /**
     * @description:查询表单信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryInvMatRecipientsForm", method = RequestMethod.POST)
    public InvMatRecipientsVO queryInvMatRecipientsForm(String imrid) throws Exception {
        InvMatRecipientsVO imr = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvMatRecipientsVO> imrList = invMatRecipientsService.queryInvMatRecipientsForm( userInfo, imrid );
        if ( null != imrList && !imrList.isEmpty() ) {
            imr = imrList.get( 0 );
        }
        return imr;
    }

    /**
     * @description:查看列表信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvMatRecipientsList", method = RequestMethod.POST)
    public Page<InvMatRecipientsDetailVO> queryInvMatRecipientsList(String imrid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatRecipientsService.queryInvMatRecipientsList( userInfo, imrid );
    }

    /**
     * @description:保存方法
     * @author: 890166
     * @createDate: 2014-9-28
     * @param formData
     * @param listData
     * @param taskId
     * @param imrid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveMatRecipients", method = RequestMethod.POST)
    public Map<String, Object> saveMatRecipients(String formData, String listData, String taskId, String imrid,String uploadIds)
            throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> reMap = new HashMap<String, Object>();
        String flag = "false";
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        InvMatRecipients imr = JsonHelper.fromJsonStringToBean( formData, InvMatRecipients.class );
        List<InvMatRecipientsDetailVO> imrList = JsonHelper.toList( listData, InvMatRecipientsDetailVO.class );
        reMap = invMatRecipientsService.saveRecipientsTran( userInfo, imr, imrList, uploadIds);

        flag = String.valueOf( reMap.get( "flag" ) );
        if ( "success".equals( flag ) ) {
            // 更新一下最新的库存数量
            invMatApplyDetailService.updateNewStockQtyByImaid( userInfo, imr.getImaid() );
        }
        result.put( "result", flag );
        return result;
    }

    /**
     * @description:根据sheetno查询id
     * @author: 890166
     * @createDate: 2014-10-24
     * @param sheetNo
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryRecipientsIdBySheetNo", method = RequestMethod.POST)
    public Map<String, Object> queryRecipientsIdBySheetNo(String sheetNo) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String imrid = invMatRecipientsService.queryRecipientsIdBySheetNo( userInfo, sheetNo );
        result.put( "imrid", imrid );
        return result;
    }

    /**
     * @description: 真正发料之前先做验证
     * @author: 890166
     * @createDate: 2015-6-17
     * @param formData
     * @param listData
     * @param taskId
     * @param imrid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/validateCanOut", method = RequestMethod.POST)
    public Map<String, Object> validateCanOut(String listData) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> reMap = new HashMap<String, Object>();

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        List<InvMatRecipientsDetailVO> imrList = JsonHelper.toList( listData, InvMatRecipientsDetailVO.class );
        reMap = invMatRecipientsService.validateCanOut( userInfo, imrList );
        result.put( "result", String.valueOf( reMap.get( "flag" ) ) );
        result.put( "remark", String.valueOf( reMap.get( "remark" ) ) );
        return result;
    }
    
    /**
     * @description: 返回物资发料列表页面
     * @author: 890199
     * @createDate: 2016-11-09
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatRecipientsApplyList", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_RECIPIENTSAPPLY_STATUS")
    public ModelAndView invMatRecipientsApplyList() throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatrecipients/invMatRecipientsApplyList.jsp" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        mav.addObject( "siteid", siteId );
        return mav;
    }

    /**
     * @description: 发料申请列表
     * @author: 890199
     * @createDate: 2016-11-09
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInvMatRecipientsApplyList", method = RequestMethod.POST)
    public Page<InvMatRecipientsVO> queryInvMatRecipientsApplyList() throws Exception{
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    	Page<InvMatRecipientsVO> page = userInfo.getPage();
    	Map<String, String[]> params = userInfo.getParamMap();
    	String siteId = userInfo.getParam("siteId");
    	String sdata = userInfo.getParam("sdata");
    	String imaid = userInfo.getParam("imaid");
    	page.setParameter("siteId", siteId);
    	if(StringUtils.isNotBlank(sdata)){
        	page.setParameter("sdata", sdata);
    	}
    	if(params.containsKey("search")){
        	String fuzzySearchParams = userInfo.getParam("search");
        	Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
        	if(fuzzyParams.containsKey("deliveryDate")){
				fuzzyParams.put("DELIVERYDATE_STR", fuzzyParams.get("deliveryDate"));
				fuzzyParams.remove("deliveryDate");
			}
			page.setFuzzyParams(fuzzyParams);
		}
    	String sort = String.valueOf( userInfo.getParam( "sort" ) == null ? "" : userInfo.getParam( "sort" ) );
        String order = String.valueOf( userInfo.getParam( "order" ) == null ? "" : userInfo.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        }
        List<InvMatRecipientsVO> results = null;
    	if( imaid != null ){
        	results = invMatRecipientsService.queryRecipientsByImaId(userInfo, imaid);
    	}
    	else{
        	results = invMatRecipientsService.queryInvMatRecipientsApplyList(page);
    	}
    	page.setResults(results);
    	return page;
    }
    
    /**
     * @description: 物资发料保存附件功能
     * @author: 890199
     * @createDate: 2017-01-13
     * @param 
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveInvMatRecipientsAttach", method = RequestMethod.POST)
    public Map<String, Object> saveInvMatRecipientsAttach(String uploadIds, String imrid) throws Exception{
    	Map<String, Object> result = new HashMap<String, Object>();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	uploadIds = uploadIds.replace( "\"", "" );
        paramMap.put("uploadIds", uploadIds);
        paramMap.put("imrid", imrid);
        result = invMatRecipientsService.saveInvMatRecipientsAttach(paramMap);
    	return result;
    }
}
