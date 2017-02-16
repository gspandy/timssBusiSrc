package com.timss.inventory.web;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvMatRecipientsService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.ReflectionUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.homepage.bean.WorktaskBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyController.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping ( value = "inventory/invmatapply" )
public class InvMatApplyController {

    /**
     * service 注入
     */
    @Autowired
    private InvMatApplyService      invMatApplyService;
    
    @Autowired
    private ItcMvcService	   itcMvcService;

    @Autowired
    private WorkflowService	 workflowService;

    @Autowired
    private HomepageService	 homepageService;

    @Autowired
    private WorkOrderService	workOrderService;

    @Autowired
    private InvMatRecipientsService invMatRecipientsService;

    @Autowired
    public IAuthorizationManager    authManager;

    @Autowired
    public PurPubInterface purPubInterface;                                                                                                                                                                                                     
    /**
     * log4j输出
     */
    private static final Logger     LOG = Logger.getLogger( InvMatApplyController.class );

    /**
     * @description:物资领料列表跳转
     * @author: 890166
     * @createDate: 2014-7-27
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/invMatApplyList" , method = RequestMethod.GET )
    public String invMatApplyList () {
	return "/invmatapply/invMatApplyList.jsp";
    }

    /**
     * @description:采购申请列表跳转
     * @author: 890162
     * @createDate: 2016-8-19
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/invMatApplyPurApplyList" , method = RequestMethod.GET )
    public String invMatApplyPurApplyList () {
        return "/invmatapply/invMatApplyPurApplyList.jsp";
    }
    
    @RequestMapping ( value = "/queryPurApplyList" )
    public Page<PurApplyVO> queryPurApplyList ( ) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return purPubInterface.queryPurApplyList(userInfo);
    }
    
    /**
     * @description: 物资领料表单跳转
     * @author: 890166
     * @createDate: 2014-7-27
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/invMatApplyForm" , method = RequestMethod.GET )
    @ReturnEnumsBind ( "INV_APPLY_TYPE,INV_SPMATERIAL" )
    public ModelAndView invMatApplyForm ( @RequestParam String imaid ) throws Exception {
	String jumpPage = "/invmatapply/invMatApplyForm.jsp";
	ModelAndView mav = new ModelAndView( jumpPage );
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	String siteId = userInfo.getSiteId();
	String defKey = "inventory_[@@@]_invmatapply".replace( "[@@@]" , siteId.toLowerCase() );
	String purApplyId = userInfo.getParam( "purApplyId" );
	mav.addObject( "siteid" , siteId );
	mav.addObject( "defKey" , defKey );
	mav.addObject( "purApplyId",purApplyId );
	if ( null != imaid && !"".equals( imaid ) ) {

	    // modify by yuanzh 20160104 将查询流程环节的动作放在有sheetNo的地方
	    String sheetNo = invMatApplyService.queryFlowNoByImaId( imaid , siteId );
	    WorktaskBean wtBean = homepageService.getOneTaskByFlowNo( sheetNo , userInfo );
	    if ( null != wtBean ) {
		mav.addObject( "classType" , wtBean.getClasstype().toString() );
	    }

	    List< InvMatApplyVO > imaList = invMatApplyService.queryMatApplyForm( userInfo , imaid , "1" );
	    if ( !imaList.isEmpty() ) {
		InvMatApplyVO ima = imaList.get( 0 );
		String instanceid = ima.getInstanceid();
		mav.addObject( "processInstId" , instanceid );
		if ( null == instanceid || "".equals( instanceid ) ) {
		    mav.addObject( "isEdit" , "editable" );
		}

		if ( !"".equals( instanceid ) && null != instanceid ) {
		    List< Task > activities = workflowService.getActiveTasks( instanceid );
		    if ( null != activities && !activities.isEmpty() ) {
			Task task = activities.get( 0 );

			String curProcess = task.getTaskDefinitionKey();
			curProcess = curProcess.replaceAll( "\\d+" , "" );
			mav.addObject( "process" , curProcess );
			mav.addObject( "processName" , task.getName() );

			List< String > userIdList = workflowService.getCandidateUsers( task.getId() );
			for ( String userId : userIdList ) {
			    if ( userId.equals( userInfo.getUserId() ) ) {
				mav.addObject( "isEdit" , "editable" );
				break;
			    }
			}
			mav.addObject( "taskId" , task.getId() );

			Map< String , String > processAttr = workflowService.getElementInfo( task.getId() );
			mav.addObject( "oper" , processAttr.get( "modifiable" ) );

			/********************************* 判断当前用户是否符合综合部经理条件 *********************************/
			if ( "dep_manager".equals( task.getTaskDefinitionKey() ) ) {
			    SecureUser su = authManager.retriveUserById( userInfo.getUserId() , siteId );
			    boolean inteMgrFlag = CommonUtil.isJumpTask( instanceid , su );
			    if ( inteMgrFlag ) {
				workflowService.setVariable( instanceid , "isInteMgr" , "Y" );
			    } else {
				workflowService.setVariable( instanceid , "isInteMgr" , "N" );
			    }
			}
			/********************************* 判断当前用户是否符合综合部经理条件 *********************************/
		    }
		}

		List< InvMatRecipientsVO > imrvList = invMatRecipientsService.queryRecipientsByImaId( userInfo ,
			ima.getImaid() );
		if ( !imrvList.isEmpty() ) {
		    mav.addObject( "hasRpts" , "Y" );
		    String imrArrayStr = JsonHelper.fromBeanToJsonString( imrvList );
		    mav.addObject( "imrArrayStr" , imrArrayStr );
		}
	    }
	} else {
	    mav.addObject( "isEdit" , "editable" );
	}
	return mav;
    }

    /**
     * @description: 物资退库单跳转
     * @author: 890152
     * @createDate: 2014-9-23
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/invMatRefundForm" , method = RequestMethod.GET )
    @ReturnEnumsBind ( "INV_APPLY_TYPE" )
    public ModelAndView invMatRefundForm ( @RequestParam String imaid ) throws Exception {
	ModelAndView mav = new ModelAndView( "/invmatrefund/invMatRefundForm.jsp" );
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	List< InvMatRecipientsVO > imrvList = invMatRecipientsService.queryRecipientsByImaId( userInfo , imaid );
	if ( null != imrvList && !imrvList.isEmpty() ) {
	    mav.addObject( "hasRpts" , "Y" );
	}
	return mav;
    }

    /**
     * @description:通知领料弹出框跳转
     * @author: 890166
     * @createDate: 2014-9-26
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/invMatConsuming" , method = RequestMethod.GET )
    public String invMatConsuming () {
	return "/invmatapply/invMatConsuming.jsp";
    }

    /**
     * @description:物资领料列表数据
     * @author: 890166
     * @createDate: 2014-7-27
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/queryMatApplyList" , method = RequestMethod.POST )
    public Page< InvMatApplyVO > queryMatApplyList ( String search ) throws Exception {
	InvMatApplyVO ima = new InvMatApplyVO();
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	// 若表头查询参数不为空
	if ( StringUtils.isNotBlank( search ) ) {
	    ima = JsonHelper.fromJsonStringToBean( search , InvMatApplyVO.class );
	}
	return invMatApplyService.queryMatApplyList( userInfo , ima );
    }

    /**
     * @description:查询表单数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param imaid
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/queryMatApplyForm" , method = RequestMethod.POST )
    public InvMatApplyVO queryMatApplyForm ( String imaid ) throws Exception {
	InvMatApplyVO ima = null;
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	List< InvMatApplyVO > imaList = invMatApplyService.queryMatApplyForm( userInfo , imaid , "1" );
	if ( null != imaList && !imaList.isEmpty() ) {
	    ima = imaList.get( 0 );
	    List< String > relatePurApplyIds = invMatApplyService.queryPurApplyOfMatApply( userInfo, imaid, "1" );
	    //注意:目前只支持对应单条采购申请
	    if ( 0 < relatePurApplyIds.size() ) {
	        String relatePurApplyId = relatePurApplyIds.get( 0 );
	        ima.setRelatePurApplyIdsList( relatePurApplyId );
            }
	}
	return ima;
    }

    @RequestMapping ( value = "/invMatApplyPurApplyItemList" , method = RequestMethod.POST )
    public Map<String, Object> invMatApplyPurApplyItemList ( String sheetId,String siteid ) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>(0);
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        siteid = userInfo.getSiteId();
        List<InvItemVO> list = purPubInterface.queryPurApplyItemListByIdInIMA( sheetId,siteid );
        result.put( "data", list);
        return result;
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
    @RequestMapping ( value = "/saveMatApply" , method = RequestMethod.POST )
    @ValidFormToken
    public Map< String , Object > saveMatApply ( String formData , String listData , String type , String taskId ,
	    String imaid ) throws Exception {
	Map< String , Object > result = new HashMap< String , Object >();
	Map< String , Object > paramMap = new HashMap< String , Object >(); // 参数map
	Map< String , Object > reMap = new HashMap< String , Object >();

	paramMap.put( "saveType" , type );
	boolean flag = true;
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	paramMap.put( "imaid" , imaid );
	if ( null != taskId && !"".equals( taskId ) ) {
	    paramMap.put( "taskId" , taskId );
	}

	InvMatApplyVO imav = JsonHelper.fromJsonStringToBean( formData , InvMatApplyVO.class );
	paramMap.put( "woNo" , imav.getWorkOrderNo() );

	InvMatApply ima = ( InvMatApply ) ReflectionUtil.conventBean2Bean( imav , new InvMatApply() );

	if ( !"picksubmit".equals( type ) ) {
	    List< InvMatApplyDetail > imaList = CommonUtil.conventJsonToInvMatApplyDetailList( listData );
	    reMap = commitApply( userInfo , ima , imaList , paramMap );
	}

	flag = Boolean.valueOf( reMap.get( "flag" ) == null ? "false" : String.valueOf( reMap.get( "flag" ) ) );
	imaid = String.valueOf( reMap.get( "imaid" ) == null ? "" : String.valueOf( reMap.get( "imaid" ) ) );
	result.put( "imaid" , imaid );
	result.put( "taskId" , paramMap.get( "taskId" ) );
	result.put( "status" , reMap.get( "status" ) );
	if ( flag ) {
	    result.put( "result" , "success" );
	} else {
	    result.put( "result" , "false" );
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
    @RequestMapping ( value = "/commitApply" , method = RequestMethod.POST )
    private Map< String , Object > commitApply ( UserInfoScope userInfo , InvMatApply ima ,
	    List< InvMatApplyDetail > imaList , Map< String , Object > paramMap ) throws Exception {
	ProcessInstance p = null;
	Task task = null;
	String processId = null;
	String siteId = userInfo.getSiteId();
	Map< String , Object > reMap = new HashMap< String , Object >();
	String taskId = paramMap.get( "taskId" ) == null ? "" : String.valueOf( paramMap.get( "taskId" ) );
	if ( "".equals( taskId ) ) {
	    String defKey = "inventory_[@@@]_invmatapply".replace( "[@@@]" , siteId.toLowerCase() );
	    String processKey = workflowService.queryForLatestProcessDefKey( defKey );
	    p = workflowService.startLatestProcessInstanceByDefKey( processKey , userInfo.getUserId() , null );
	    processId = p.getProcessInstanceId();
	    //根据流程实例id调用获取当前活动任务，task对应一个节点
	    List< Task > taskList = workflowService.getActiveTasks( processId );
	    if ( null != taskList && !taskList.isEmpty() ) {
		task = taskList.get( 0 );
		paramMap.put( "taskId" , task.getId() );
	    }
	    paramMap.put( "processId" , processId );

	} else {
	    task = workflowService.getTaskByTaskId( taskId );
	    processId = task.getProcessInstanceId();
	    paramMap.put( "processId" , processId );

	    // 经营部经理审批，根据流程实例id，将变量值赋值到变量名之下
	    if ( CommonUtil.getProperties( "operManagerProcess" ).equals( task.getTaskDefinitionKey() ) ) {
		workflowService.setVariable( processId , "spmaterial" , ima.getSpmaterial() );
	    }

	    // 行政部经理审批
	    if ( CommonUtil.getProperties( "adminManagerProcess" ).equals( task.getTaskDefinitionKey() ) ) {
		workflowService.setVariable( processId , "isOver" , ima.getIsOver() );
	    }
	}

	// 插入下一环节的keyid
	String type = paramMap.get( "saveType" ) == null ? "" : String.valueOf( paramMap.get( "saveType" ) );
	String status = "";
	//新建时点提交和审批时点审批都是对应的提交按钮，进入此处
	if ( "submit".equals( type ) ) {
	    List< String > taskNextList = workflowService.getNextTaskDefKeys( processId , task.getTaskDefinitionKey() );
	    if ( null != taskNextList && !taskNextList.isEmpty() ) {
	    	status = taskNextList.get( 0 );//审批通过后再发起请求进行状态更新
	    }
	}else if( "save".equals( type ) ){
		paramMap.put( "status" , "draft" );
	}
	

	reMap = invMatApplyService.saveMatApply( userInfo , ima , imaList , paramMap );
	workflowService.setVariable( processId , "totalPrice" ,
		new BigDecimal( String.valueOf( reMap.get( "totalPrice" ) ) ) );
	workflowService.setVariable( processId , "applyType" , ima.getApplyType() );
	workflowService.setVariable( processId , "imaList" , imaList );
	
	if ( "".equals( taskId ) ) {
	    UserInfo ui = itcMvcService.getUserInfoById( ima.getCreateuser() );
	    workflowService.setVariable( processId , "dept" , ui.getOrgId() );

	    workflowService.setVariable( processId , "imaid" , ima.getImaid() );
	    homepageService.createProcess(
		    ima.getSheetno() ,
		    task.getProcessInstanceId() ,
		    task.getName() ,
		    ima.getSheetname() ,
		    task.getName() ,
		    "/inventory/invmatapply/invMatApplyForm.do?taskId=" + task.getId() + "&processInstId="
			    + task.getProcessInstanceId() + "&imaid=" + ima.getImaid() , userInfo , null );
	}
	String name = ima.getSheetname();
	homepageService.modify(null, ima.getSheetno(), null, name, null, null, null, null);
	reMap.put("status", status);
	return reMap;
    }

    /**
     * @description:结束流程
     * @author: 890166
     * @createDate: 2014-10-25
     * @param taskId
     * @param processId
     * @param message
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/stopProcess" , method = RequestMethod.POST )
    public Map< String , Object > stopProcess ( @RequestParam ( "taskId" ) String taskId ,
	    @RequestParam ( "message" ) String message , @RequestParam ( "sheetId" ) String sheetId ) throws Exception {
	Map< String , Object > result = new HashMap< String , Object >();
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String classType = userInfo.getParam( "classType" );
	String curUser = userInfo.getUserId();
	try {
	    InvMatApply ima = new InvMatApply();
	    ima.setImaid( sheetId );
	    if("Processed".equals(classType)){
	    	//更新状态为作废
		    ima.setStatus( "stop" );
	    }
	    else{
	    	//更新状态为删除
		    ima.setStatus( "deleted" );
	    }
	    //更新状态
	    invMatApplyService.updateMatApply( ima );
	    //流程中止
	    workflowService.stopProcess( taskId , curUser , curUser , message );
	    result.put( "result" , "success" );
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatApplyController 中的 stopProcess 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    result.put( "result" , "error" );
	}
	return result;
    }
    
    
    /**
     * @description:提交确定
     * @author: zhuw
     * @createDate: 2016-07-06
     * @param sheetId
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/updateMatApply" , method = RequestMethod.POST )
    public Map< String , Object > updateMatApply (String taskId, String status,
    		 String imaid, String listData ) throws Exception {
    	Map< String , Object > result = new HashMap< String , Object >();
	    InvMatApply ima = new InvMatApply();
	    int	count = 0;
	    ima.setImaid( imaid );
	    if(StringUtils.isNotEmpty(taskId)){
	    	List<Task> activities = workflowService.getActiveTasks(taskId);
	    	if(null != activities && !activities.isEmpty()){
	    		Task task = activities.get( 0 );
		    	String curProcess = task.getTaskDefinitionKey();
		    	curProcess = curProcess.replaceAll( "\\d+" , "" );
		    	status = curProcess;
	    	}
	    }
	    ima.setStatus(status);
	    if(!"".equals(status)){
	    	List< InvMatApplyDetail > imaList = CommonUtil.conventJsonToInvMatApplyDetailList( listData );
	    	count = invMatApplyService.updateInvMatApply( imaList,ima );
	    }
	    if(count>0){
	    	result.put( "result" , "success" );
	    }else{
	    	result.put( "result", "error");
	    }
	    return result;
    }

    /**
     * @description:判断工单是否存在
     * @author: 890166
     * @createDate: 2014-11-10
     * @return
     * @throws Exception:
     */
    @RequestMapping ( value = "/verifyWorkOrderNo" )
    @ResponseBody
    public Boolean verifyWorkOrderNo () throws Exception {
	Map< String , Object > result = new HashMap< String , Object >();
	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	String workOrderNo = userInfoScope.getParam( "workOrderNo" );
	result = workOrderService.queryWOBaseInfoByWOCode( workOrderNo , userInfoScope.getSiteId() );
	boolean woExist = ( Boolean ) result.get( "woExist" );
	if ( woExist ) {
	    return true;
	}
	return false;
    }

    @RequestMapping ( value = "/stopSend" , method = RequestMethod.POST )
    public Map< String , Object > stopSend (String imaId, String taskId) throws Exception {
		Map< String , Object > result = new HashMap< String , Object >();
	    //终止领料
		result = invMatApplyService.stopSend(imaId, taskId);
		
		return result;
    }
}
