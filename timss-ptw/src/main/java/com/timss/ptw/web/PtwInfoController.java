package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.ptw.bean.PtwChangeWpic;
import com.timss.ptw.bean.PtwExtand;
import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwProcess;
import com.timss.ptw.bean.PtwRemarkTask;
import com.timss.ptw.bean.PtwSafe;
import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.bean.PtwStdSafeBean;
import com.timss.ptw.bean.PtwType;
import com.timss.ptw.bean.PtwTypeDefine;
import com.timss.ptw.bean.PtwWaitRestore;
import com.timss.ptw.service.PtwChangeWpicService;
import com.timss.ptw.service.PtwExtandService;
import com.timss.ptw.service.PtwFireInfoService;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwProcessService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.PtwRemarkTaskService;
import com.timss.ptw.service.PtwSafeService;
import com.timss.ptw.service.PtwStandardService;
import com.timss.ptw.service.PtwTypeDefineService;
import com.timss.ptw.service.PtwTypeService;
import com.timss.ptw.service.PtwWaitRestoreService;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
/**
 * 
 * @title: 工作票信息的Controller
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoController.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptwInfo")
public class PtwInfoController {
    private static final Logger log = Logger.getLogger(PtwInfoController.class);
    
    @Autowired
    private PtwInfoService ptwInfoService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PtwTypeService ptwTypeService;
    
    @Autowired
    private PtwTypeDefineService ptwTypeDefineService;
    
    @Autowired
    private WorkOrderService workOrderService;
    
    @Autowired
    private PtwFireInfoService ptwFireInfoService;
    
    @Autowired
    private PtwChangeWpicService ptwChangeWpicService;
    
    @Autowired
    private PtwExtandService ptwExtandService;
    @Autowired
    private PtwSafeService ptwSafeService;
    
    @Autowired
    private PtwWaitRestoreService ptwWaitRestoreService;
    
    @Autowired
    private PtwRemarkTaskService ptwRemarkTaskService;
    
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private PtwProcessService ptwProcessService;
    
    @Autowired
    private PtwStandardService ptwStandardService;
    
    @Autowired
    PtwPtoSelectUserService ptwPtoSelectUserService;
    /**
     * 
     * @description:跳转至工作票列表
     * @author: 周保康
     * @createDate: 2014-6-27
     * @return:
     * @throws Exception 
     * @throws RuntimeException 
     */
    @RequestMapping(value = "/preQueryPtwInfo")
    @ReturnEnumsBind("PTW_KEYBOXSTATUS,PTW_KEYBOXTYPE,WO_WIND_STATION")
    public String preQueryPtwInfo() throws RuntimeException, Exception {
        return "/ptwInfo.jsp";
    }
    
    /**
     * 查询工作票的基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-10
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryBasePtwInfo")
    public @ResponseBody HashMap<String, Object> queryBasePtwInfo() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        HashMap<String, Object> result = new HashMap<String, Object>();
        int id = Integer.parseInt(userInfoScope.getParam( "id" ));
        String isNewFireWt = userInfoScope.getParam( "isNewFireWt" );
        
        PtwInfo ptwInfo = ptwInfoService.queryPtwInfoById( id );        
        result.put( "ptwInfo", ptwInfo );
        //新建动火票时，先查询相关信息出来
        if ( isNewFireWt != null && isNewFireWt.equals( "true" ) ) {
            return result;
        }
        
        PtwType ptwType = ptwTypeService.queryPtwTypeById( ptwInfo.getWtTypeId() );
        PtwTypeDefine ptwTypeDefine = ptwTypeDefineService.queryPtwTypeDefineById( ptwType.getWtTypeDefineId() );
        result.put( "ptwType", ptwType );
        result.put( "ptwTypeDefine", ptwTypeDefine );
        
       //附加文件信息
        HashMap<String, Object> fileMaps = new HashMap<String, Object>();
        
        //动火票信息
        if ( ptwTypeDefine.getIsFireWt() == 1 ) {
            PtwFireInfo ptwFireInfo = ptwFireInfoService.queryPtwFireInfoByWtId( ptwInfo.getId() );
            result.put( "ptwFireInfo", ptwFireInfo );
            
            if(ptwFireInfo != null ){
	            //动火票查询出对应工作票信息
	            PtwInfo attachPtwInfo = ptwInfoService.queryPtwInfoById( ptwFireInfo.getAttachWtId() ); 
	            PtwType attachPtwType = ptwTypeService.queryPtwTypeById( attachPtwInfo.getWtTypeId() );
	            result.put( "attachPtwType", attachPtwType);
            }
            
            if (ptwFireInfo !=null && ptwFireInfo.getFireWorkPic() != null  && !ptwFireInfo.getFireWorkPic().equals( "" )) {
                ArrayList<String> ids = getPtwFiles(ptwFireInfo.getFireWorkPic());
                fileMaps.put( "fireWorkPic", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
        }else {
            if ( ptwInfo.getAddFile1() != null && !ptwInfo.getAddFile1().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFile1());
                fileMaps.put( "addFile1", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
            if (  ptwInfo.getAddFile2() != null && !ptwInfo.getAddFile2().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFile2());
                fileMaps.put( "addFile2", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
            if (  ptwInfo.getAddFile3() != null && !ptwInfo.getAddFile3().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFile3());
                fileMaps.put( "addFile3", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
            if (  ptwInfo.getAddFile4() != null && !ptwInfo.getAddFile4().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFile4());
                fileMaps.put( "addFile4", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
            if (  ptwInfo.getAddFile5() != null && !ptwInfo.getAddFile5().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFile5());
                fileMaps.put( "addFile5", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
            if ( ptwInfo.getAddFileOtherNo() != null && !ptwInfo.getAddFileOtherNo().equals( "" ) ) {
                ArrayList<String> ids = getPtwFiles(ptwInfo.getAddFileOtherNo());
                fileMaps.put( "addFileOtherNo", FileUploadUtil.getJsonFileList( Constant.basePath,ids ));
            }
        }
        result.put( "fileMaps", fileMaps );
        
        //隔离措施的信息
        List<PtwSafe> ptwSafes = ptwSafeService.queryPtwSafeListByWtId( id );
        
        result.put( "ptwSafes", ptwSafeService.genPtwSafeMap( ptwSafes ) );
        
        //工作负责人变更信息
        PtwChangeWpic ptwChangeWpic = ptwChangeWpicService.queryPtwChangeWpicByPtwId( id );
        if ( ptwChangeWpic != null ) {
            result.put( "ptwChangeWpic", ptwChangeWpic );
        }
        //工作票延期信息
        PtwExtand ptwExtand = ptwExtandService.queryPtwExtandByPtwId( id );
        if ( ptwExtand != null ); {
            result.put( "ptwExtand", ptwExtand );
        }
        
        //间断转移
        List<PtwWaitRestore> ptwWaitRestores = ptwWaitRestoreService.queryPtwWaitRestoreByPtwId( id );
        result.put( "ptwNeedRestore", ptwWaitRestoreService.ptwNeedRestore( ptwWaitRestores ) );
        if ( ptwWaitRestores != null && ptwWaitRestores.size() > 0 ) {
            result.put( "ptwWaitRestores", ptwWaitRestores );
        }
        
        //新增任务
        PtwRemarkTask ptwRemarkTask = ptwRemarkTaskService.queryPtwRemarkTaskByPtwId( id );
        if ( ptwRemarkTask != null ) {
            result.put( "ptwRemarkTask", ptwRemarkTask );
        }
        
        //查询流程相关信息
        List<PtwProcess> ptwProcesses = ptwProcessService.queryPtwProcessByPtwId( id );
        if ( ptwProcesses != null && ptwProcesses.size() > 0 ) {
            result.put( "ptwProcesses", ptwProcesses );
            for ( PtwProcess ptwProcess : ptwProcesses ) {
                if ( ptwProcess.getWtStatus() == ptwInfo.getWtStatus() ) {
                    String processInstId = ptwProcess.getProcessId();
                    if ( processInstId != null ) {
                        result.put( "processInstId", processInstId );
                        List<Task> activities = workflowService.getActiveTasks(processInstId);
                        if ( activities != null && activities.size() > 0 ) {
                            Task task = activities.get(0);
                            result.put( "taskId", task.getId() );
                            List<String> userIds = new ArrayList<String>();
                            String position = workflowService.getElementInfo( workflowService.getDefKeyByProcessInstId(task.getProcessInstanceId()), task.getTaskDefinitionKey() ).get( "position" );
                            //如果为提交申请后，点击取消，会回到自己手上。
                            if ( null == position || "".equals( position ) ) {
                                //当前用户
                                userIds.add( userInfoScope.getUserId() );
                            }else{
                                List<String> users = workflowService.getCandidateUsers(task.getId());
                                for ( String secureUser : users ) {
                                    userIds.add( secureUser );
                                }
                            }
                            result.put( "auditUsers", userIds );
                        }
                    }
                    break;
                }
            }
        }
        
        //查询相关的动火票
        List<Integer> fireIds = ptwFireInfoService.queryFireIdsByAttachWtId(id);
        result.put("fireIds", fireIds);
        
        return result;
    }
    
    /**
     * 处理文件信息 
     * @description:
     * @author: 朱旺
     * @createDate: 2016-4-26
     * @return:
     */
    private ArrayList<String> getPtwFiles(String fileIds){
    	ArrayList<String> list = new ArrayList<String>();
    	if(StringUtils.isNotEmpty(fileIds)){
    		String[] ids = fileIds.split(",");
    		for(String id:ids){
    			list.add(id);
    		}
    	}
    	return list;
    }
    
    
    /**
     * 保存工作票信息 
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-21
     * @return:
     * @throws Exception 
     */
    @RequestMapping(value = "/savePtwInfo")
    public HashMap<String, Object> savePtwInfo() throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String baseInfo = userInfoScope.getParam( "baseInfo" );
        //工作票的前缀
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        PtwInfo ptwInfo = JsonHelper.fromJsonStringToBean( baseInfo, PtwInfo.class );
        
        String safeItems = userInfoScope.getParam( "safeItems" );
        
        String fireInfo = userInfoScope.getParam( "fireInfo" );
        PtwFireInfo ptwFireInfo;
        if ( fireInfo == null || fireInfo.equals( "" ) ) {            
            ptwFireInfo = null;
        }else{
            ptwFireInfo = JsonHelper.fromJsonStringToBean( fireInfo, PtwFireInfo.class );
        }
        
        ptwInfoService.insertPtwInfo( ptwInfo, ptwTypeCode, safeItems ,ptwFireInfo);
        
        result.put( "result", "ok" );
        result.put( "ptwId", ptwInfo.getId() );
        result.put( "ptwNo", ptwInfo.getWtNo() );
        result.put( "ptwStatus", ptwInfo.getWtStatus() );
        if ( ptwInfo.getWtStatus() == 300 ) {
            List<PtwSafe> ptwSafes = ptwSafeService.fromSafeItemsToList( safeItems, ptwInfo.getId() );
            result.put( "ptwSafes", ptwSafeService.genPtwSafeMap( ptwSafes ) );
        }
        return result;
    }
    
    /**
     * 更新工作票的基本信息和许可信息，暂存时使用
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/updatePtwBaseAndLicInfo")
    public HashMap<String, Object> updatePtwBaseAndLicInfo()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String baseInfo = userInfoScope.getParam( "baseInfo" );
        PtwInfo ptwInfo = JsonHelper.fromJsonStringToBean( baseInfo, PtwInfo.class );
        //更新工作票的许可
        updateLicInfo( baseInfo, ptwInfo );
        
        //工作票的前缀
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        
        String safeItems = userInfoScope.getParam( "safeItems" );
        
        Date modifyDate = new Date();
        
        String fireInfo = userInfoScope.getParam( "fireInfo" );
        PtwFireInfo ptwFireInfo;
        if ( fireInfo == null || fireInfo.equals( "" ) ) {            
            ptwFireInfo = null;
        }else{
            ptwFireInfo = JsonHelper.fromJsonStringToBean( fireInfo, PtwFireInfo.class );
        }
        
        int updated = ptwInfoService.updatePtwBaseAndLicInfo( ptwInfo,ptwTypeCode,safeItems,modifyDate,ptwFireInfo );
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else{
            result.put( "result", "error" );
        }
        return result;
    } 
    
    /**
     * 签发工作票
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/issuePtw")
    public HashMap<String, Object> issuePtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int wtStatus = Integer.parseInt( userInfoScope.getParam( "wtStatus" ));
        String password = userInfoScope.getParam( "password" );
        String wtNo = userInfoScope.getParam( "wtNo" );
        String isEditing = userInfoScope.getParam( "isEditing" );

        PtwInfo ptwInfo;
        boolean isEdit = false;
        //工作票的前缀
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        String safeItems = null;
        if ( isEditing != null && isEditing.equals( "true" ) ) {
            isEdit = true;
            String baseInfo = userInfoScope.getParam( "baseInfo" );
            ptwInfo = JsonHelper.fromJsonStringToBean( baseInfo, PtwInfo.class );
            safeItems = userInfoScope.getParam( "safeItems" );
        }else{
            ptwInfo = new PtwInfo();            
        }
        
        ptwInfo.setId( ptwId );
        ptwInfo.setWtStatus( wtStatus );
        Date now = new Date();
        ptwInfo.setWtNo( wtNo );
        if(wtStatus == 300){        	
        	ptwInfo.setOutIssuer( userInfoScope.getUserName() );
        	ptwInfo.setOutIssuerNo( userInfoScope.getUserId() );
        	ptwInfo.setOutIssuedTime( now );
        }else{
        	ptwInfo.setIssuer( userInfoScope.getUserName() );
        	ptwInfo.setIssuerNo( userInfoScope.getUserId() );
        	ptwInfo.setIssuedTime( now );
        }
        
        String fireInfo = userInfoScope.getParam( "fireInfo" );
        PtwFireInfo ptwFireInfo;
        if ( fireInfo == null || fireInfo.equals( "" ) ) {            
            ptwFireInfo = null;
        }else{
            ptwFireInfo = JsonHelper.fromJsonStringToBean( fireInfo, PtwFireInfo.class );
        }
        
        String taskId = ptwInfoService.updatePtwIssueInfo( ptwInfo,password,now,isEdit,ptwTypeCode,safeItems,ptwFireInfo);
        if ( taskId == null ) {
            result.put( "result", "出现错误" );
        }else if (taskId.equals( "passwordError" ) ) {
            result.put( "result", "密码输入错误" );
        }else {
            result.put( "taskId", taskId );
            result.put( "result", "ok" );
        } 
        return result;
    }
    
    
    
    /**
     * 许可工作票
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-29
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/licPtw")
    public HashMap<String, Object> licPtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int wtStatus = Integer.parseInt( userInfoScope.getParam( "wtStatus" ));
        String password = userInfoScope.getParam( "password" );
        String isEditing = userInfoScope.getParam( "isEditing" );
        String baseInfo = userInfoScope.getParam( "baseInfo" );
        boolean isEdit = false;
        
        if ( isEditing != null && isEditing.equals( "true" ) ) {
            isEdit = true;
        }
        PtwInfo ptwInfo = JsonHelper.fromJsonStringToBean( baseInfo, PtwInfo.class );
        //工作票的前缀
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );       
        
        updateLicInfo( baseInfo, ptwInfo );
        ptwInfo.setId( ptwId );
        ptwInfo.setWtStatus( wtStatus );
        
        
        Date now = new Date();
        ptwInfo.setLicWl( userInfoScope.getUserName() );
        ptwInfo.setLicWlNo( userInfoScope.getUserId() );
        ptwInfo.setLicTime( now );
        
        String safeItems = userInfoScope.getParam( "safeItems" );
        
        String fireInfo = userInfoScope.getParam( "fireInfo" );
        PtwFireInfo ptwFireInfo;
        if ( fireInfo == null || fireInfo.equals( "" ) ) {            
            ptwFireInfo = null;
        }else{
            ptwFireInfo = JsonHelper.fromJsonStringToBean( fireInfo, PtwFireInfo.class );
        }
        
        //动火票消防监护人确认时间
        String cfmGuardXf = userInfoScope.getParam( "cfmGuardXf" );
        if ( cfmGuardXf != null && !cfmGuardXf.equals( "" ) ) {
            if ( ptwFireInfo == null ) {
                ptwFireInfo = new PtwFireInfo();
                ptwFireInfo.setWtId( ptwInfo.getId() );
            }
            ptwFireInfo.setCfmGuardXf( cfmGuardXf );
            ptwFireInfo.setCfmGuardXfNo( userInfoScope.getParam( "cfmGuardXfNo" ) );
            ptwFireInfo.setCfmGuardXfTime( new Date(Long.parseLong( userInfoScope.getParam( "cfmGuardXfTime" ) )) );
        }
        
        String taskId = ptwInfoService.updatePtwLicInfo( ptwInfo, password, now, isEdit, ptwTypeCode, safeItems,ptwFireInfo );
        if ( taskId == null ) {
            result.put( "result", "出现错误" );
        }else if (taskId.equals( "passwordError" ) ) {
            result.put( "result", "密码输入错误" );
        }else {
            result.put( "taskId", taskId );
            result.put( "result", "ok" );
        } 
        return result;
    }
    
    
    /**
     * 结束工作票
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/finPtw")
    public HashMap<String, Object> finPtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String password = userInfoScope.getParam( "password" );
        String finParams = userInfoScope.getParam( "finParams" );
        
        PtwInfo ptwInfo = JsonHelper.fromJsonStringToBean( finParams, PtwInfo.class );
        Date now = new Date();
        ptwInfo.setFinWl( userInfoScope.getUserName() );
        ptwInfo.setFinWlNo( userInfoScope.getUserId() );
        ptwInfo.setFinWlTime( now );
        
        String taskId = ptwInfoService.updatePtwFinInfo( ptwInfo,password,now);
        if ( taskId == null ) {
            result.put( "result", "出现错误" );
        }else if (taskId.equals( "passwordError" ) ) {
            result.put( "result", "密码输入错误" );
        }else {
            result.put( "taskId", taskId );
            result.put( "result", "ok" );
        } 
        return result;
    }
    
    /**
     * 终结工作票
     * @description:
     * @author: 周保康
     * @createDate: 2014-7-23
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/endPtw")
    public HashMap<String, Object> endPtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String password = userInfoScope.getParam( "password" );
        String endParams = userInfoScope.getParam( "endParams" );
        String safeItems = userInfoScope.getParam( "safeItems" );
        
        PtwInfo ptwInfo = JsonHelper.fromJsonStringToBean( endParams, PtwInfo.class );
        ptwInfo.setEndWl( userInfoScope.getUserName() );
        ptwInfo.setEndWlNo( userInfoScope.getUserId() );        
        
        Date now = new Date();        
        int updated = ptwInfoService.updatePtwEndInfo( ptwInfo, password, now, safeItems );
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else if ( updated == -1 ) {
            result.put( "result", "密码输入错误" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
    /**
     * 作废工作票
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/cancelPtw")
    public HashMap<String, Object> cancelPtw()throws Exception{
        
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int wtStatus = Integer.parseInt( userInfoScope.getParam( "wtStatus" ));
        int isStdWt = Integer.parseInt( userInfoScope.getParam( "isStdWt" ));
        String password = userInfoScope.getParam( "password" );
        
        PtwInfo ptwInfo = new PtwInfo();
        ptwInfo.setId( ptwId );
        ptwInfo.setWtStatus( wtStatus );
        ptwInfo.setIsStdWt( isStdWt );
        Date now = new Date();
        ptwInfo.setCanceler( userInfoScope.getUserName() );
        ptwInfo.setCancelerNo( userInfoScope.getUserId() );
        ptwInfo.setCancelTime( now );
        int updated = ptwInfoService.updatePtwCancelInfo( ptwInfo,password,now);
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else if ( updated == -1 ) {
            result.put( "result", "密码输入错误" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
   /**
    * 备注工作票
    * @description:
    * @author: 周保康
    * @createDate: 2014-8-23
    * @return
    * @throws Exception:
    */
    @RequestMapping(value = "/remarkPtw")
    public HashMap<String, Object> remarkPtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        String remark = userInfoScope.getParam( "remark" );
        
        int updated = ptwInfoService.updatePtwRemark(ptwId,remark);
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
    @RequestMapping(value = "/deletePtw")
    public HashMap<String, Object> deletePtw()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int updated = ptwInfoService.deletePtwInfo( ptwId );
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
    /**
     * 工单的校验
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/verifyWorkOrderNo")
    public @ResponseBody Boolean verifyWorkOrderNo()throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderNo = userInfoScope.getParam( "workOrderNo" );
        result = workOrderService.queryWOBaseInfoByWOCode( workOrderNo, userInfoScope.getSiteId() );
        return validWorkOrderNo(result);
    }
    
    @RequestMapping(value = "/findWorkOrderInfo")
    public @ResponseBody Map<String, Object> findWorkOrderInfo()throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderNo = userInfoScope.getParam( "workOrderNo" );
        result = workOrderService.queryWOBaseInfoByWOCode( workOrderNo, userInfoScope.getSiteId() );
        return result;
    }
    
    @RequestMapping(value = "/findWorkOrderInfoById")
    public @ResponseBody Map<String, Object> findWorkOrderInfoById()throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String workOrderId = userInfoScope.getParam( "workOrderId" );
        result = workOrderService.queryWOById( Integer.parseInt( workOrderId) );
        return result;
    }
    
    @RequestMapping(value = "/findPtwInfoByKeyBoxId")
    public @ResponseBody HashMap<String, Object> findPtwInfoByKeyBoxId()throws Exception{
        HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String keyBoxId = userInfoScope.getParam( "keyBoxId" );
        List<PtwInfo> list = ptwInfoService.queryByKeyBoxId(Integer.parseInt( keyBoxId) );
        result.put("total", list == null ? 0: list.size());
        result.put("rows", list == null ? new String[0]: list);
        return result;
    }
    
    @RequestMapping(value = "/verifyPtwNo")
    public @ResponseBody Boolean verifyPtwNo()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String wtNo = userInfoScope.getParam( "attachWtNo" );
        PtwInfo ptwInfo = ptwInfoService.queryPtwInfoByNo( wtNo, userInfoScope.getSiteId() );
        if ( ptwInfo == null ) {
            return false;
        }
        return true;
    }
    
    @RequestMapping(value = "/findPtwInfoByNo")
    public HashMap<String, Object> findPtwInfoByNo()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String wtNo = userInfoScope.getParam( "attachWtNo" );
        PtwInfo ptwInfo = ptwInfoService.queryPtwInfoByNo( wtNo, userInfoScope.getSiteId() );
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put( "ptwInfo", ptwInfo );
        return result;       
    }
    
    private boolean validWorkOrderNo(Map<String, Object> resultMap){
        //TODO 工单的校验
        //是否开启工单校验
        boolean openWoValid = false;
        if ( !openWoValid ) {
            return true;
        }
        //{woExist=true,isInPTW=flase,workorder = new WorkOrder() };
        boolean woExist = (Boolean) resultMap.get( "woExist" );
        boolean isInPTW = (Boolean) resultMap.get( "isInPTW" );
        if ( woExist && isInPTW ) {
            return true;
        }
        return false;
    }
    
    /**
     * 将许可的风险信息更新到ptwinfo中
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-4
     * @param baseInfo
     * @param ptwInfo:
     */
    private void updateLicInfo(String baseInfo,PtwInfo ptwInfo){
        Map<String, Object> licInfoMap = new HashMap<String, Object>();
        try {
            licInfoMap = MapHelper.jsonToHashMap( baseInfo );
        } catch (Exception e) {
            log.error(e.getMessage());
        } 
        String licRisks = (String) licInfoMap.get( "licRisks" );
        if (licRisks != null && ! licRisks.equals( "" ) ) {
            String[] risks = licRisks.split( "," );
            for ( String risk : risks ) {
                if ( risk.equals( "lic1" ) ) {
                    ptwInfo.setLic1( 1 );
                }else if ( risk.equals( "lic2" ) ) {
                    ptwInfo.setLic2( 1 );
                }else if ( risk.equals( "lic3" ) ) {
                    ptwInfo.setLic3( 1 );
                }else if ( risk.equals( "lic4" ) ) {
                    ptwInfo.setLic4( 1 );
                }else if ( risk.equals( "lic5" ) ) {
                    ptwInfo.setLic5( 1 );
                }else if ( risk.equals( "lic6" ) ) {
                    ptwInfo.setLic6( 1 );
                }else if ( risk.equals( "lic7" ) ) {
                    ptwInfo.setLic7( 1 );
                }else if ( risk.equals( "lic8" ) ) {
                    ptwInfo.setLic8( 1 );
                }else if ( risk.equals( "lic9" ) ) {
                    ptwInfo.setLic9( 1 );
                }else if ( risk.equals( "lic10" ) ) {
                    ptwInfo.setLic10( 1 );
                }
            }
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/verifyWpic")
    public HashMap<String, Object> verifyPtwNo(String userId){
        return ptwInfoService.validWpicAvailable(userId);
    } 
    
    
    /**
     * @description:查询标准工作票并转化为工作票的信息
     * @author: 王中华
     * @createDate: 2015-8-13
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInitdataFromSptw", method = RequestMethod.POST)
    public Map<String, Object>  queryInitdataFromSptw() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String sptwInfoId = userInfoScope.getParam( "sptwId" );
        PtwInfo ptwBaseInfo = null;
        List<PtwSafe> ptwSafe = null;
        int ptwSafeType = -1;
        if(!StringUtils.isEmpty(sptwInfoId)){
            PtwStandardBean sptwBean = ptwStandardService.queryPtwStandardById( sptwInfoId );
            List<PtwStdSafeBean> sptwItems = ptwStandardService.queryPtwStdSafeByWtId( sptwInfoId );
            ptwBaseInfo = sptwToPtw(sptwBean);
            ptwSafe = sptwSafeToPtwSafe(sptwItems);
            if(sptwItems.size()>0){
	            //获取安全措施类型
	            int index = sptwItems.get(0).getSafeType().lastIndexOf( "_" );
	            ptwSafeType = Integer.parseInt(sptwItems.get(0).getSafeType().substring( index+1 ));
	            //对于热控一、热控二要将ptwSafeType变成2
	            String wtTypeId = sptwBean.getWtTypeId();
	            int wtTypeIdIndex = wtTypeId.length();
	            List<PtwType> ptwTypeList = ptwTypeService.queryTypesBySiteId( siteId, 0 );
	            String ryreTypeString = ""; //热控第一种、热控第二种的ID值
	            for ( int i = 0; i < ptwTypeList.size(); i++ ) {
	                String tempPtwTypeCode = ptwTypeList.get( i ).getTypeCode();
	                if("RY".equals( tempPtwTypeCode )||"RE".equals( tempPtwTypeCode )||"JB".equals( tempPtwTypeCode )){
	                    ryreTypeString = ryreTypeString + "," + ptwTypeList.get( i ).getId();
	                }
	            }
	            if(ryreTypeString.indexOf( wtTypeId.substring( wtTypeIdIndex-2 ) ) > 0){
	                ptwSafeType = 2;
	                for(int j = 0; j<ptwSafe.size(); j++){
	                	 ptwSafe.get(j).setSafeType(ptwSafeType);
	                }
	            }
            }
        }
        
        HashMap<String, Object> ptwSafeMap = new HashMap<String, Object>();
        ptwSafeMap.put( "safeDatas", ptwSafe );
        int[] ptwTypeArray = new int[1];
        ptwTypeArray[0] = ptwSafeType;
        ptwSafeMap.put( "ptwTypes", ptwTypeArray );
        
        Map<String, Object> mav = new HashMap<String, Object>();
        mav.put( "result", "success" );
        mav.put( "ptwBaseInfo", JsonHelper.fromBeanToJsonString(ptwBaseInfo) );
        mav.put( "ptwSafe",  ptwSafeMap);
        return mav;
    }
    
    /**
     * @description:查询历史票并转化为工作票或动火票的信息
     * @author: zhuw
     * @createDate: 2016-6-21
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInitdataFromPtw", method = RequestMethod.POST)
    public Map<String, Object>  queryInitdataFromPtw() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String ptwId = userInfoScope.getParam( "ptwId" );
        PtwInfo ptwInfo = null;
        List<PtwSafe> ptwSafes = null;
        if(!StringUtils.isEmpty(ptwId)){
        	int id = Integer.parseInt(ptwId);
            ptwInfo = ptwInfoService.queryPtwInfoById( id );
            ptwSafes = ptwSafeService.queryPtwSafeListByWtId( id );
        }
        
        Map<String, Object> mav = new HashMap<String, Object>();
        mav.put( "result", "success" );
        mav.put( "ptwInfo", JsonHelper.fromBeanToJsonString(ptwInfo) );
        mav.put( "ptwSafe",  ptwSafeService.genPtwSafeMap( ptwSafes ));
        return mav;
    }

    /**
     * @description:将标准工作票的安全措施转换为工作票的安全措施
     * @author: 王中华
     * @createDate: 2015-8-13
     * @param sptwItems
     * @return:
     */
    private List<PtwSafe> sptwSafeToPtwSafe(List<PtwStdSafeBean> sptwItems) {
        List<PtwSafe> result = new ArrayList<PtwSafe>();
        for ( PtwStdSafeBean temPtwStdSafeBean : sptwItems ) {
            PtwSafe temPtwSafe = new PtwSafe();
            temPtwSafe.setSafeOrder( temPtwStdSafeBean.getSafeOrder() );
            temPtwSafe.setSafeContent( temPtwStdSafeBean.getSafeContent() );
            int index = temPtwStdSafeBean.getSafeType().lastIndexOf( "_" );
            int safeTypeInt = Integer.parseInt(temPtwStdSafeBean.getSafeType().substring( index+1 ));
            temPtwSafe.setSafeType( safeTypeInt );
            result.add( temPtwSafe );
        }
        return result;
    }

    /**
     * @description:将标准工作票的基本信息转换为工作票的基本信息
     * @author: 王中华
     * @createDate: 2015-8-13
     * @param sptwBean
     * @return:
     */
    private PtwInfo sptwToPtw(PtwStandardBean sptwBean) {
        PtwInfo ptwInfo =new PtwInfo();
        String sptwTypeId = sptwBean.getWtTypeId();
        int index = sptwTypeId.lastIndexOf( "_" );
        int ptwTypeId = Integer.parseInt(sptwTypeId.substring( index+1 ));
        ptwInfo.setWtTypeId(ptwTypeId);
//        ptwInfo.setEqId( sptwBean.getEqNo() );
        ptwInfo.setEqName( sptwBean.getEqName() );
        ptwInfo.setEqNo( sptwBean.getEqNo() );
        ptwInfo.setWorkContent( sptwBean.getWorkContent() );
        ptwInfo.setWorkPlace( sptwBean.getWorkPlace() );
        return ptwInfo;
    }
    
    
    /**
     * 审核动火票
     * @description:
     * @author: 朱旺
     * @createDate: 2015-11-25
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/auditPtw")
    public HashMap<String, Object> auditPtw()throws Exception{
    	HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int wtStatus = Integer.parseInt( userInfoScope.getParam( "wtStatus" ));
        String password = userInfoScope.getParam( "password" );
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        
        PtwInfo ptwInfo = new PtwInfo(); 
        
        ptwInfo.setId( ptwId );
        ptwInfo.setWtStatus( wtStatus );
        Date now = new Date();
        
        int updated = ptwInfoService.updatePtwAuditInfo(ptwInfo,password,now,ptwTypeCode);
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else if ( updated == -1 ) {
            result.put( "result", "密码输入错误" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
    /**
     * 审批工作票
     * @description:
     * @author: 朱旺
     * @createDate: 2015-11-25
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/departAuditPtw")
    public HashMap<String, Object> departAuditPtw()throws Exception{
    	HashMap<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        int ptwId = Integer.parseInt( userInfoScope.getParam( "id" ));
        int wtStatus = Integer.parseInt( userInfoScope.getParam( "wtStatus" ));
        String password = userInfoScope.getParam( "password" );
        String wtNo = userInfoScope.getParam( "wtNo" );
        String isEditing = userInfoScope.getParam( "isEditing" );
        
        PtwInfo ptwInfo;
        boolean isEdit = false;
        //工作票的前缀
        String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
        String safeItems = null;
        if ( isEditing != null && isEditing.equals( "true" ) ) {
            isEdit = true;
            String baseInfo = userInfoScope.getParam( "baseInfo" );
            ptwInfo = JsonHelper.fromJsonStringToBean( baseInfo, PtwInfo.class );
            safeItems = userInfoScope.getParam( "safeItems" );
        }else{
            ptwInfo = new PtwInfo();            
        }
        
        ptwInfo.setId( ptwId );
        ptwInfo.setWtStatus( wtStatus );
        Date now = new Date();
        ptwInfo.setWtNo( wtNo );
        
        int updated = ptwInfoService.updatePtwDepartAuditInfo(ptwInfo,password,isEdit,now,ptwTypeCode,safeItems);
        if ( updated == 1 ) {
            result.put( "result", "ok" );
        }else if ( updated == -1 ) {
            result.put( "result", "密码输入错误" );
        }else{
            result.put( "result", "出现错误了" );
        }  
        return result;
    }
    
    /**
     * 两票配置接口调用
     * @description:
     * @author: 朱旺
     * @createDate: 2015-11-30
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/auditPriv")
    public HashMap<String, Object> auditPriv()throws Exception{
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String ptwStatus = userInfoScope.getParam( "ptwStatus" );
    	String ptwTypeCode = userInfoScope.getParam( "ptwTypeCode" );
    	
    	if(!StringUtils.isEmpty(ptwTypeCode)){
    		boolean hasPriv = ptwPtoSelectUserService.hasAuditPrivilege( "ptw", ptwTypeCode, ptwStatus );
    		result.put( "result", hasPriv );
    	}else{
    		List<String> list = ptwPtoSelectUserService.queryPrivilegeTypes("ptw", ptwStatus);
    		result.put("types", list);
    	}
    	
    	return result;
    }
    
    /**
     * 两票配置接口调用查询用户
     * @description:
     * @author: 朱旺
     * @createDate: 2015-11-30
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryPtwUsersByAuditPri")
    public @ResponseBody ArrayList<ArrayList<Object>> queryPtwUsersByAuditPri() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String type = userInfoScope.getParam( "type" );
        String step = userInfoScope.getParam( "step" );
        List<SecureUser> users = ptwPtoSelectUserService.selectUsersWithoutWorkFLow("ptw", type, step);
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> def = new ArrayList<Object>();
        String addDefaultAll = userInfoScope.getParam( "hasOther" );
        if ( addDefaultAll != null && addDefaultAll.equals( "all" ) ) {
            def.add(0);
            def.add("全部");
            def.add(true);
            result.add(def);
        }else if ( addDefaultAll != null && addDefaultAll.equals( "select" ) ) {
            def.add(-1);
            def.add("请选择");
            def.add(true);
            result.add(def);
        }
        for ( SecureUser secureUser : users ) {
            ArrayList<Object> row = new ArrayList<Object>();
            row.add(secureUser.getId());
            row.add(secureUser.getName());
            result.add(row);
        }
        return result;
    }
       
}
