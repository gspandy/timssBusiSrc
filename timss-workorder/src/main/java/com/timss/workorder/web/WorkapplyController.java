package com.timss.workorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.workorder.bean.WoAttachment;
import com.timss.workorder.bean.WoapplyRisk;
import com.timss.workorder.bean.WoapplySafeInform;
import com.timss.workorder.bean.WoapplyWorker;
import com.timss.workorder.bean.Workapply;
import com.timss.workorder.service.WoAttachmentService;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WoWorkapplyService;
import com.timss.workorder.service.WoapplyRiskService;
import com.timss.workorder.service.WoapplySafeInformService;
import com.timss.workorder.service.WoapplyWorkerService;
import com.timss.workorder.util.WoapplyUtil;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: WorkapplyController.java
 * @author: 王中华
 * @createDate: 2014-6-23
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/workorder/workapply")
public class WorkapplyController {
	@Autowired
	private ItcMvcService itcMvcService;
	@Autowired
    private WoWorkapplyService woWorkapplyService;
	@Autowired
	private WoapplyWorkerService woapplyWorkerService;
	@Autowired
    private WoapplyRiskService woapplyRiskService;
	@Autowired
    private WoapplySafeInformService woapplySafeInformService;
	@Autowired
    private WoUtilService woUtilService;
	@Autowired
    private WoAttachmentService woAttachmentService;
	
	private static Logger logger = Logger.getLogger(WorkapplyController.class);

	/**
	 * @description: 打开工单申请列表页面
	 * @author: 王中华
	 * @createDate: 2015-12-18
	 * @return
	 * @throws Exception:
	 * nullMode = NULLMODE.Exception 表示你传入的枚举“enum1,enum2”返回的数据为空会抛出异常。
	 *	nullMode = NULLMODE.EachException  表示你传入的枚举“enum1,enum2”其中有一个为空就会抛出异常。
	 */
	@RequestMapping(value="/workapplyList",method=RequestMethod.GET)
	@ReturnEnumsBind("WO_WOAPPLY_STATUS,WO_CHECK_LEVEL")
	public ModelAndView workOrderList() throws Exception{
//	    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
	    Map<String, Object> map = new HashMap<String, Object>();
//	    boolean newBtnShowFlag = hasNewWoapplyPriv(userInfoScope);
//	    map.put( "newBtnShowFlag",  newBtnShowFlag);
	    
	    ModelAndView modelAndView = new ModelAndView( "/workapply/workapplyList.jsp", map );
	    return modelAndView;
	}
	
 
    /**
     * @description:判断登录用户是否有新建开工申请的权限
     * @author: 王中华
     * @createDate: 2015-12-22
     * @param userInfoScope
     * @return:
     */
//    private boolean hasNewWoapplyPriv(UserInfoScope userInfoScope) {
//        String loginUserId = userInfoScope.getUserId();
//        String siteId = userInfoScope.getSiteId();
//        boolean result = false;
//        List<SecureUser> resultList = new ArrayList<SecureUser>();
//        resultList = authManager.retriveUsersWithSpecificGroup(siteId+"_WO_YXJXZ", null, false, true);
//        
//        for (SecureUser temp :resultList ) {
//            if(loginUserId.equals( temp.getId() )){
//                return true;
//            }
//        }
//        
//        return result;
//    }


    /**
	 * @description: 查询工单列表数据
	 * @author: 王中华
	 * @createDate: 2014-7-4
	 * @return
	 * @throws Exception:
	 */
	@RequestMapping(value = "/workapplyListdata",method=RequestMethod.POST)
	public Page<Workapply> workapplyListdata() throws Exception {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		Page<Workapply> page = userInfoScope.getPage();
		String fuzzySearchParams = userInfoScope.getParam("search");
                HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "workapplyMap", "workorder","WoWorkapplyDao");
                
                if(fuzzySearchParams!=null){
                    HashMap<String, Object> fuzzyParams = (HashMap<String, Object>)MapHelper.jsonToHashMap(fuzzySearchParams );
                    fuzzyParams = MapHelper.fromPropertyToColumnMap(fuzzyParams, propertyColumnMap);
                	page.setFuzzyParams(fuzzyParams);
                } 
        
		page.setParameter("siteid", siteId);
		page = woWorkapplyService.queryAllWorkapply(page);
		
		return page;
	}
	
	 /**
     * @description: 跳转到新建工单申请页面
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return:
     */
    @RequestMapping(value = "/openNewWoapplyPage" )
    @ReturnEnumsBind("WO_WOAPPLY_STATUS,WO_CHECK_LEVEL")
    public String openNewWOPage(){
        System.out.println("sdfsdfs");
	return "/workapply/workapply.jsp";
    }
    
    /**
     * @description: 提交“工单”数据
     * @author: 王中华
     * @createDate: 2014-6-19
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitWoapplydata",method=RequestMethod.POST)
    @ValidFormToken
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,String> commitWoapplydata() throws Exception {
            logger.info("---------------进入提交开工申请-------------------------");
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String woapplyFormDate = userInfoScope.getParam("workapplyForm");//获取前台传过来的form表单数据
            Workapply workapply = JsonHelper.toObject(woapplyFormDate, Workapply.class);
            
            String woapplyId= workapply.getId();
            String workflowId = workapply.getWorkflowId();
            String commitStyle = userInfoScope.getParam("commitStyle");//提交方式（用于确定是点击的“提交”还是“暂存”）
            
            String safeInformUser = userInfoScope.getParam("safeInformUser");//安全交底人
            workapply.setSafeInformUser( safeInformUser );
            String safeinform = userInfoScope.getParam("safeinform");  //安全交底内容
            List<WoapplySafeInform> woapplySafeInformList = WoapplyUtil.converToWoapplySafeInform(safeinform);
            
            String workerData = userInfoScope.getParam("workerData");  //外来队伍施工人员
            ArrayList<WoapplyWorker> woapplyWorkerList = WoapplyUtil.convertToWoapplyWorkerList(workerData);
            
            String riskAssessmentData = userInfoScope.getParam("riskAssessmentData");  //风险评估
            ArrayList<WoapplyRisk> riskAssessmentList = WoapplyUtil.convertToRiskAssessmentList(riskAssessmentData);
            
            String uploadIds = userInfoScope.getParam("uploadIds");  //附件
            
            Map<String,Object> addWODataMap = new HashMap<String, Object>();
            addWODataMap.put("woapplyFormData", woapplyFormDate);
            addWODataMap.put("commitStyle", commitStyle);
            addWODataMap.put( "safeInformUser", safeInformUser );
            addWODataMap.put( "safeinformList", woapplySafeInformList );
            addWODataMap.put( "workerList", woapplyWorkerList );
            addWODataMap.put( "riskAssessmentList", riskAssessmentList );
            
            String taskId = "noFlow";
            String loadPhase = "txkgsq";
            if(workflowId == "" || workflowId == null){  //确定不是因为回退的节点
                    if(woapplyId == null || "".equals( woapplyId )){  //初次提交或暂存
                        workapply.setId( null );
                        if("commit".equals(commitStyle)){  //提交，启动流程
                                Map<String, Object> insertResultMap = woWorkapplyService.insertWorkapply( workapply );
                                taskId = insertResultMap.get("taskId").toString();
                                woapplyId = insertResultMap.get("woapplyId").toString();
                                workflowId = insertResultMap.get("workflowId").toString();
                        }else if("save".equals(commitStyle)){  //暂存，不启动流程
                                    Map<String, Object> saveResultMap = woWorkapplyService.saveWorkapply(workapply);
                                    woapplyId = saveResultMap.get("woapplyId").toString();
//                                    String workapplyCode = saveResultMap.get("workapplyCode").toString();
                                    loadPhase = "draft";
                        }
                        WoapplyUtil.setWoapplyIdInList(woapplySafeInformList,woapplyWorkerList,riskAssessmentList,woapplyId);
                        //添加安全交底内容
                        woapplySafeInformService.insertWoapplySafeInformList( woapplySafeInformList );
                        //添加施工人员信息
                        woapplyWorkerService.insertWoapplyWorkerList( woapplyWorkerList );
                        //添加风险评估信息
                        woapplyRiskService.insertWorkapplyList( riskAssessmentList );
                        
                    }else{  //对暂存的工单提交 或 再次暂存  或其他状态下的提交
                        if("commit".equals(commitStyle)){  //提交，启动流程
                                addWODataMap.put("updateStyle", "commit");
                        }else if("save".equals(commitStyle)){  //再次暂存，不启动流程
                                addWODataMap.put("updateStyle", "save");
                                loadPhase = "draft";
                        }
                        Map<String, String> flowIdAndTaskId = woWorkapplyService.updateWorkapply(addWODataMap);
                        workflowId  = flowIdAndTaskId.get("workflowId");
                        taskId = flowIdAndTaskId.get("taskId");
                        logger.info("-------------web层：暂存的工单再次提交或暂存完成,工单流程："+workflowId+"-------------");
                    }
                    //添加附件信息，包含删除附件信息
                    woUtilService.insertAttachMatch(woapplyId, uploadIds, "WORKAPPLY", loadPhase);
            }else{
//                    logger.info("-------------web层：回退的工单再次提交或暂存开始,工单编号："+workOrderCodeString+"-------------");
//                    workOrderService.rollbackCommitWo(addWODataMap);
//                    logger.info("-------------web层：回退的工单再次提交或暂存完成,工单编号："+workOrderCodeString+"-------------");
//                    //获取当前活动节点
//                    List<Task> activities = workflowService.getActiveTasks(workflowId);
//                    if(activities.size()!=0){
//                            //刚启动流程，第一个活动节点肯定是属于当前登录人的
//                            Task task = activities.get(0);
//                            if(task!=null){
//                                    taskId = task.getId();
//                            }
//                    }
            }
            
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            if(commitStyle == "save"){
                    taskId = "noFlow";
            }
            mav.put("taskId", taskId);
            mav.put("woapplyId", woapplyId);
            mav.put("workflowId", workflowId);
            return mav;
    }

	
    
//    private void setWoapplyIdInList(List<WoapplySafeInform> woapplySafeInformList,
//            List<WoapplyWorker> woapplyWorkerList, List<WoapplyRisk> riskAssessmentList, String woapplyId) {
//        for ( WoapplySafeInform woapplySafeInform : woapplySafeInformList ) {
//            woapplySafeInform.setWorkapplyId( woapplyId );
//        }
//        for ( WoapplyWorker woapplyWorker : woapplyWorkerList ) {
//            woapplyWorker.setWorkapplyId( woapplyId );
//        }
//        for ( WoapplyRisk woapplyRisk : riskAssessmentList ) {
//            woapplyRisk.setWorkapplyId( woapplyId );
//        }
//    }


//    private List<WoapplySafeInform> converToWoapplySafeInform(String safeinform) {
//        //[{"content":"电饭锅","showOrder":1},{"content":"反攻倒算","showOrder":2},{"content":"法国和","showOrder":3}]
//        List<WoapplySafeInform> itemList = JsonHelper.toList( safeinform, WoapplySafeInform.class );
//        return itemList;
//    }


//    private ArrayList<WoapplyRisk> convertToRiskAssessmentList(String riskAssessmentData) {
//        ArrayList<WoapplyRisk> riskAssessmentList =  new ArrayList<WoapplyRisk>();
//          JSONObject riskAssessmentJsonObj = JSONObject.fromObject(riskAssessmentData);
//          int riskAssessmentDatagridNum =Integer.valueOf(riskAssessmentJsonObj.get("total").toString());  //记录数
//        //插入风险评估
//          JSONArray riskAssessmentJsonArray = riskAssessmentJsonObj.getJSONArray("rows"); //记录数组
//          for(int i=0; i<riskAssessmentDatagridNum; i++){
//                  String riskAssessmentRecord = riskAssessmentJsonArray.get(i).toString();  //某条记录的字符串表示
//                  WoapplyRisk woapplyRisk = JsonHelper.fromJsonStringToBean(riskAssessmentRecord, WoapplyRisk.class);
//                  riskAssessmentList.add( woapplyRisk );
//          }   
//        return riskAssessmentList;
//    }


//    private ArrayList<WoapplyWorker> convertToWoapplyWorkerList(String workerData) {
//        ArrayList<WoapplyWorker> woapplyWorkerList = new ArrayList<WoapplyWorker>();
//        JSONObject workerJsonObj = JSONObject.fromObject(workerData);
//          int workerDatagridNum =Integer.valueOf(workerJsonObj.get("total").toString());  //记录数
//        //插入外来施工人员
//          JSONArray workerJsonArray = workerJsonObj.getJSONArray("rows"); //记录数组
//          for(int i=0; i<workerDatagridNum; i++){
//                  String workerRecord = workerJsonArray.get(i).toString();  //某条记录的字符串表示
//                  WoapplyWorker woapplyWorker = JsonHelper.fromJsonStringToBean(workerRecord, WoapplyWorker.class);
//                  woapplyWorkerList.add( woapplyWorker );
//          }   
//        return woapplyWorkerList;
//    }


    @RequestMapping(value = "/queryWoapplyDataById" )
    public Map<String, Object> queryWoapplyDataById() throws Exception{
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woapplyId = userInfoScope.getParam("woapplyId");
        //表单内容查询
        Map<String, Object> map = woWorkapplyService.queryWorkapplyById( woapplyId );
        Workapply workapply = (Workapply) map.get("bean");
        String taskId = (String) map.get("taskId");
        //安全交底查询
        List<WoapplySafeInform>  safeInformdata = woapplySafeInformService.queryWoapplySafeInformList( woapplyId );
        //外来队伍施工人员查询
        List<WoapplyWorker> workerdata = woapplyWorkerService.queryWoapplyWorkerList( woapplyId );
        //风险评估查询
        List<WoapplyRisk> riskdata = woapplyRiskService.queryWoapplyRiskList( woapplyId );
        //附件
        List<WoAttachment> attachList = woAttachmentService.queryWoAttachmentById( woapplyId, "WORKAPPLY" );
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("workapplyForm", JsonHelper.toJsonString(workapply));
        resultMap.put( "safeInform", JsonHelper.fromBeanToJsonString( safeInformdata ) );
        resultMap.put( "worker", JsonHelper.toJsonString(workerdata) );
        resultMap.put( "risk", JsonHelper.toJsonString(riskdata) );
        resultMap.put("taskId", taskId);
        resultMap.put("attachmentMap", attachmentMap);
        return resultMap;
    } 
   
    /**
     * @description: 作废开工申请，仅工单发起人可以作废
     * @author: 王中华
     * @createDate: 2014-12-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteWorkapply",method=RequestMethod.POST)
    public Map<String,String> obsoleteWorkapply() throws Exception {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String woapplyId = userInfoScope.getParam("woapplyId");//获取前台传过来的form表单数据
            
            woWorkapplyService.obsoleteWorkapply(woapplyId);
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            return mav;
    }
    
    /**
     * @description:删除草稿
     * @author: 王中华
     * @createDate: 2014-7-31
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteWorkapplyDraft",method=RequestMethod.POST)
    public Map<String,String> deleteWorkapplyDraft() throws Exception {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            String woapplyId = userInfoScope.getParam("woapplyId");//获取前台传过来的form表单数据
            woWorkapplyService.deleteWorkapply( woapplyId );
            //删除附件 第二个参数为null的话表示删除附件
            woUtilService.insertAttachMatch(woapplyId, null, "WORKAPPLY", "draft");
            Map<String,String> mav = new HashMap<String,String>();
            mav.put("result", "success");
            return mav;
    }
    
    
}
