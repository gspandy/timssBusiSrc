package com.timss.attendance.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.service.AtdAttachService;
import com.timss.attendance.service.SealApplyService;
import com.timss.attendance.vo.SealApplyVo;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.VaildParam;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 用章申请
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-08-31
 * @updateUser: 890199
 * @version:1.0
 */
@Controller
@RequestMapping(value = "attendance/sealApply")
public class SealApplyController{
	private Logger log = Logger.getLogger( SealApplyController.class );
	@Autowired
    private ItcMvcService timssService;
	@Autowired
	private SealApplyService sealApplyService;
	@Autowired
	private HomepageService homepageService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private AtdAttachService attachService;
   
	/**
     * 跳转到列表页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sealApplyList")
    @ReturnEnumsBind("ATD_SEALAPPLY_STATUS")
    public String sealApplyList() throws Exception {
        return "sealapply/Seallist.jsp";
    }
 
    /**
	 * @description: 查看用章申请列表
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/getList")
    public Page<SealApplyVo> getSealApplyList() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Page<SealApplyVo> page = userInfoScope.getPage();
        Map<String, String[]> params = userInfoScope.getParamMap();
        page.setParameter("siteId", userInfoScope.getSiteId());
        HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "SealApplyMap", "attendance", "SealApplyDao" );
        if(params.containsKey("search")){
        	String fuzzySearchParams = userInfoScope.getParam("search");
        	Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
			fuzzyParams = MapHelper.fromPropertyToColumnMap( fuzzyParams, propertyColumnMap );
			if(fuzzyParams.containsKey("CREATE_DATE")){
				fuzzyParams.put("to_char(create_date,'yyyy-mm-dd')", fuzzyParams.get("CREATE_DATE"));
				fuzzyParams.remove("CREATE_DATE");
			}
			page.setFuzzyParams(fuzzyParams);
		}
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            sortKey = propertyColumnMap.get( sortKey );
            page.setSortKey( sortKey );
            page.setSortOrder( userInfoScope.getParam( "order" ) );
        }
        else{
            page.setSortKey( "create_date" );
            page.setSortOrder( "desc" );
        }
        List<SealApplyVo> list = sealApplyService.queryAllSealApply(page);
        page.setResults(list);
        return page;
    }
    
    /**
     * 跳转到用章申请详情页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value= "/detailPage")
    public ModelAndView detailPage() throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        dataMap.put( "siteId", siteId );
        dataMap.put("userId", userId);
        return new ModelAndView( "sealapply/Sealdetail.jsp", dataMap );
    }
    
    /**
	 * @description: 查看用章申请详情
	 * @author: 890199
	 * @createDate: 2016-09-01
	 * @param: saId
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/getDetail")
    public ModelAndViewAjax getSealApplyDetail(String saId) throws Exception {
        UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        //获取附件信息
        List<Map<String,Object>> fileMaps = attachService.queryAll("sealApply", saId);
        if(fileMaps.size()>0){
        	map.put("fileMaps", fileMaps);
        }
        //查询用章申请详情
    	List<SealApplyVo> list = sealApplyService.querySealApplyById(saId);
    	SealApplyVo sav=list.get(0);
    	map.put( "bean", sav );
        //判断当前登录人是否有审批权限
        String currentUser = userInfoScope.getUserId();
        List<Task> activities = new ArrayList<Task>();
        if( StringUtils.isNotBlank( sav.getInstanceId() ) ){
            activities = workflowService.getActiveTasks( sav.getInstanceId() );
        }
        if( !activities.isEmpty() ){
            Task task = activities.get(0);
            map.put( "taskId", task.getId() );
            //拿到审批人的列表
            List<String> userList = workflowService.getCandidateUsers( task.getId() );
            //判断是否具有审批状态
            String applyFlag = null;
            if( userList.contains( currentUser )){
                applyFlag ="approver";
            }else{
                applyFlag = "others";
            }
            map.put( "applyFlag", applyFlag );
        }
        //判断当前审批人是否为创建人，而使用的标志位editFlag
        int editFlag = 0;
        String createUser = sav.getCreateuser();
        if(currentUser.equals(createUser)){
        	editFlag = 1;
        }
        map.put("editFlag", editFlag);
        return timssService.jsons(map);       
    }
    
    /**
	 * @description: 新建用章申请
	 * @author: 890199
	 * @createDate: 2016-09-02
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/insertSealApply")
    public ModelAndViewAjax insertSealApply() throws Exception{
    	UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	String jsonData = userInfoScope.getParam("jsonData");
    	SealApplyBean sab = JsonHelper.fromJsonStringToBean( jsonData, SealApplyBean.class );
    	String[] fileIds = null;
    	if(!"".equals(userInfoScope.getParam("fileIds"))&& userInfoScope.getParam("fileIds")!= null){
    		fileIds = userInfoScope.getParam("fileIds").split(",");
		}
    	//插入业务数据
    	int res = -1;
    	sab.setFileIds(fileIds);
    	if(sab.getSaId()==null || sab.getSaId()==""){
        	sab.setSaId(UUIDGenerator.getUUID());
        	sab.setSaNo(null);
        	sab.setStatus("draft");
        	sab.setCreatedate(new Date());
        	sab.setCreateuser(userInfoScope.getUserId());
        	sab.setSiteid(userInfoScope.getSiteId());
        	sab.setDeptid(userInfoScope.getOrgId());
        	sab.setDelInd("N");
        	res = sealApplyService.saveSealApply(sab);
    	}
    	else{
    		sab.setModifydate(new Date());
    		sab.setModifyuser(userInfoScope.getUserId());
        	res = sealApplyService.updateSealApply(sab);
        	List<SealApplyVo> sabList = sealApplyService.querySealApplyById(sab.getSaId());
        	sab = sabList.get(0);
    	}
    	//第一次暂存则需要创建草稿待办，后面退回再暂存不需要再创建草稿待办
		//加入待办-草稿 列表
        if(StringUtils.isBlank( sab.getInstanceId() )){
        	//有流程id说明是被退回，此时暂存就不用创建草稿待办了，否则需要创建或更新
        	//生成待办标题
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            String saNo = sab.getSaNo();
            //生产待办标题
            String saTitle = sab.getTitle();
        	String title = null;
            if(StringUtil.isNotBlank(saTitle)){
            	title = "《" + saTitle + "》用章申请";
            }
            else{
            	title = "用章申请";
            }
            String jumpPath = "attendance/sealApply/detailPage.do?mode=view&saId=" + sab.getSaId() + "&fromDraftBox=true";
            homeworkTask.setFlow(saNo);// 编号，如采购申请 WO20140902001
            //homeworkTask.setProcessInstId(""); // 草稿时流程实例ID可以不用设置
            homeworkTask.setTypeName("用章申请");// 类别名称
            homeworkTask.setName(title); // 任务名称
            homeworkTask.setStatusName("草稿"); // 流程状态名称
            homeworkTask.setUrl(jumpPath);//跳转的URL
            homeworkTask.setType(HomepageWorkTask.TaskType.Draft); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿 
        }else{//退回修改了title从而更新代办名称    add by 890200 2016-12-01
        	String flowCode = sab.getSaNo();
        	String flowName = "《"+sab.getTitle()+"》用章申请";
        	if(flowName.length()>50){
           	 flowName = flowName.substring(0, 47)+"...";
            }
        	homepageService.modify(null, flowCode, null, flowName, null, null, null, null);
        }		
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	if(res==1&&sab.getSaId()!=null){
    		result.put("result", "success");
    		result.put("bean", sab);
    	}else{
    		result.put("result", "fail");
    	}
    	return timssService.jsons(result);
    }
    
    /**
	 * @description: 提交用章申请
	 * @author: 890199
	 * @createDate: 2016-09-07
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/commitSealApply")
    public ModelAndViewAjax commitSealApply() throws Exception{
    	UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	String jsonData = userInfoScope.getParam("formData");
    	SealApplyBean sab = JsonHelper.fromJsonStringToBean( jsonData, SealApplyBean.class );
    	String[] fileIds = null;
    	if(!"".equals(userInfoScope.getParam("fileIds"))&& userInfoScope.getParam("fileIds")!= null){
    		fileIds = userInfoScope.getParam("fileIds").split(",");
		}
    	//插入业务数据
    	int res = -1;
    	sab.setFileIds(fileIds);
    	if(sab.getSaId()==null || sab.getSaId()==""){
        	sab.setSaId(UUIDGenerator.getUUID());
        	sab.setSaNo(null);
        	sab.setStatus("draft");
        	sab.setCreatedate(new Date());
        	sab.setCreateuser(userInfoScope.getUserId());
        	sab.setDeptid(userInfoScope.getOrgId());
        	sab.setSiteid(userInfoScope.getSiteId());
        	sab.setDelInd("N");
        	res = sealApplyService.saveSealApply(sab);
    	}
    	else{
    		sab.setModifydate(new Date());
    		sab.setModifyuser(userInfoScope.getUserId());
        	res = sealApplyService.updateSealApply(sab);
    	}
    	Map<String,Object> result = new HashMap<String,Object>();
    	//获取最新流程定义版本
    	String processKey = "atd_" + userInfoScope.getSiteId().toLowerCase() + "_sealapply";
    	String defkey = workflowService.queryForLatestProcessDefKey(processKey);
    	log.info( "processKey = " + processKey + "----- defkey = "  + defkey + " ----sealApplyBean.toString = " + sab.toString() );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", sab.getSaId() );
        ProcessInstance processInstance;
        String saNo = sab.getSaNo();
    	try{
        	//启动工作流
    		processInstance = workflowService.startLatestProcessInstanceByDefKey(defkey, userInfoScope.getUserId(), map);
    		String processInstId = processInstance.getProcessInstanceId();
    		workflowService.setVariable(processInstId, "orgId", userInfoScope.getOrgId());
    		result.put( "processInstId", processInstId );
            log.info( "流程实例ID processInstId=" + processInstId + " --- businessId = " +  sab.getSaId() );
            //生产待办标题
            String saTitle = sab.getTitle();
        	String title = null;
            if(StringUtil.isNotBlank(saTitle)){
            	title = "《" + saTitle + "》用章申请";
            }
            else{
            	title = "用章申请";
            }
            try{
            	homepageService.Delete(saNo,userInfoScope);
            	}catch(Exception e){
            		log.error( e.getMessage(), e );
            	}
        	//加入待办-草稿 列表
        	HomepageWorkTask homeworkTask = new HomepageWorkTask();
            String jumpPath = "attendance/sealApply/detailPage.do?mode=view&saId=" + sab.getSaId() + "&fromDraftBox=true";
            homeworkTask.setFlow(saNo);// 编号，如采购申请 WO20140902001
            homeworkTask.setProcessInstId(processInstId); // 草稿时流程实例ID可以不用设置
            homeworkTask.setTypeName("用章申请");// 类别名称
            homeworkTask.setName(title); // 任务名称（点检情况）
            homeworkTask.setStatusName("填写用章申请"); // 流程状态名称
            homeworkTask.setUrl(jumpPath);//跳转的URL
            homeworkTask.setType(HomepageWorkTask.TaskType.Process); // 枚举类型定义是草稿还是流程,Draft 草稿;Process 流程实例
            //homepageService.create( homeworkTask, userInfoScope ); 
            homepageService.createProcess(saNo, processInstId, "用章申请", title, "填写用章申请", jumpPath, userInfoScope, null);
            //获取当前活动节点，刚启动流程，第一个活动节点肯定是属于当前登录人的
            List<Task> activities = workflowService.getActiveTasks(processInstId);
            Task task = activities.get(0);
            result.put( "taskId", task.getId() );
    	}catch(Exception e){
    		log.error( e.getMessage(), e );
    	}
    	if(res==1&&sab.getSaId()!=null){
    		result.put("result", "success");
    		result.put("bean", sab);
    	}else{
    		result.put("result", "fail");
    	}
    	return timssService.jsons(result);
    }
    
    /**
	 * @description: 更新申请单
	 * @author: 890199
	 * @createDate: 2016-09-02
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value="/updateSealApply")
    public ModelAndViewAjax updateSealApply() throws Exception{
    	UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	String jsonData = userInfoScope.getParam("jsonData");
    	SealApplyBean sab = JsonHelper.fromJsonStringToBean(jsonData, SealApplyBean.class);
    	sab.setModifydate(new Date());
    	sab.setModifyuser(userInfoScope.getUserId());
    	int res = -1;
    	//状态，删除标志位 未设置
    	res = sealApplyService.updateSealApply(sab);
    	Map<String,Object> result = new HashMap<String,Object>();
    	if(res==1){
    		result.put("result", "success");
    	}else{
    		result.put("result", "fail");
    	}
    	return timssService.jsons(result);
    }
    
    /**
	 * @description: 删除申请单(物理删除)
	 * @author: 890199
	 * @createDate: 2016-09-05
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/removeSealApply")
    @VaildParam(paramName = "sealApplyBean")
    public ModelAndViewAjax removeSealApply() throws Exception{
    	HashMap<String,Object> result = new HashMap<String,Object>();
    	UserInfoScope userInfoScope = timssService.getUserInfoScopeDatas();
    	String jsonData = userInfoScope.getParam("jsonData");
    	SealApplyBean sab = JsonHelper.fromJsonStringToBean(jsonData, SealApplyBean.class);
    	String saId = sab.getSaId();
    	int res = 0;
    	res = sealApplyService.removeSealApply(saId);
    	//删除待办
        homepageService.Delete( sab.getSaNo(), userInfoScope ); 
    	if(res != 0){
    		result.put("result", "success");
    	}else{
    		result.put("result", "fail");
    	}
    	return ViewUtil.Json(result);
    }
    
    /**
	 * @description: 查询申请单
	 * @author: 890199
	 * @createDate: 2016-09-03
	 * @param: 
	 * @return:
	 * @throws Exception:
	 */
    @RequestMapping(value = "/searchSealApply")
    public ModelAndViewAjax searchSealApplyDetail(String saId) throws Exception{
    	HashMap<String,Object> result = new HashMap<String,Object>();
    	List<SealApplyVo> list = sealApplyService.querySealApplyById(saId);
    	SealApplyBean sealApplyBean = list.get(0);
    	result.put("bean", sealApplyBean);
    	return timssService.jsons(result);
    }
    
    /**
     * 
     * @description:作废流程
     * @author: 890199
     * @createDate: 2016-09-09
     * @param 
     * @return: 
     */
    @RequestMapping(value="/invalidSealApply")
    public ModelAndViewAjax invalidSealApply(String saId) throws Exception{
    	//状态，删除标志位 未设置
    	Map<String,Object> result = new HashMap<String,Object>();
    	result	= sealApplyService.invalidSealApply(saId);
    	return timssService.jsons(result);
    }
}
