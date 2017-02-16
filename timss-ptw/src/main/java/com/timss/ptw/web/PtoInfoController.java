package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtoAttachment;
import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.service.AttachMatchService;
import com.timss.ptw.service.PtoInfoService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.SptoInfoService;
import com.timss.ptw.util.CommonUtil;
import com.timss.ptw.vo.PtoInfoVo;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.support.IEnumerationManager;
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
 * @className: PtoInfoController.java
 * @author: 王中华
 * @createDate: 2015-7-27
 * @updateUser: 王中华
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/ptoInfo")
public class PtoInfoController {
    private static final Logger log = Logger.getLogger(PtoInfoController.class);
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtoInfoService ptoInfoService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private AttachMatchService attachMatchService;
    @Autowired
    private SptoInfoService sptoInfoService;
    @Autowired
    private PtwPtoSelectUserService ptwPtoSelectUserService;
    @Autowired 
    private IEnumerationManager iEnumerationManager;
    
    @RequestMapping(value = "/queryPtoList")
    @ReturnEnumsBind("PTW_PTO_STATUS,PTW_SPTO_TYPE,WO_WIND_STATION")
    public ModelAndView queryPtoList() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        //新建按钮是否显示
        boolean newBtnShowFlag = ptwPtoSelectUserService.hasAuditPrivilege( "pto", null , "xj");
      //TODO 是否需要启动流程（例如：SWF只要新建、然后确定是执行了还是作废了的操作，并没有汇报操作，也就是做完之后回来点击下鼠标，单就结束了）
        boolean isNeedFlow = CommonUtil.hasEnumValue( iEnumerationManager, "PTO_NEED_FLOW", "Y", siteid );
        
        String jumpUrl = "/pto/ptoList.jsp";  //跳转页面
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "newBtnShowFlag", newBtnShowFlag );
        map.put("isNeedFlow",isNeedFlow);
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView ;
    }
    
    private Map<String, Object> getPtoRoleUserList(String[] args, UserInfoScope userInfoScope){
        Map<String,Object> resultMap = new HashMap<String,  Object>();
        
        for ( int i = 0; i < args.length; i++ ) {
            List<List<Object>> result1 = new ArrayList<List<Object>>();
            String roleId = args[i];
            List<SecureUser> roleUserList = authManager.retriveUsersWithSpecificRole( roleId, null, false, true );
            for ( SecureUser secureUser : roleUserList ) {
                List<Object> row = new ArrayList<Object>();
                row.add( secureUser.getId() );
                row.add( secureUser.getName() );
                result1.add( row );
            }
            resultMap.put( roleId.substring( 4 ), result1 );  //KEY不带站点信息
        }
        return resultMap;
    }
    
    @RequestMapping(value = "/newPto")
    @ReturnEnumsBind("PTW_PTO_STATUS,PTW_SPTO_TYPE,WO_WIND_STATION")
    public ModelAndView newPto() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        
        String sptoInfoId = userInfoScope.getParam( "sptoInfoId" );
        //复制操作票传入的操作票id 用于查询出操作票信息赋值给新建操作票
        String ptoId = userInfoScope.getParam("ptoId");
        PtoInfoVo ptoInfoVo = null;
        if(!StringUtils.isEmpty(sptoInfoId)){
            Map<String, Object> sptoInfoMap = sptoInfoService.querySptoInfoById( sptoInfoId );
            SptoInfoVo sptoInfoVo = (SptoInfoVo) sptoInfoMap.get( "bean" );
            List<PtoOperItem> operItems = (List<PtoOperItem>) sptoInfoMap.get( "list" );
            ptoInfoVo = sptoToPtoInfoVo(sptoInfoVo,operItems);  //转化为ptoInfoVo
        }
        if(!StringUtils.isEmpty(ptoId) && StringUtils.isEmpty(sptoInfoId)){
        	PtoInfoVo ptoInfo = ptoInfoService.queryPtoInfoVoById(ptoId);
        	ptoInfoVo = ptoToPtoInfoVo(ptoInfo);
        }
        
        String[] args = new String[]{siteid+"_PTOGUARDIAN",siteid+"_PTOCOMMANDER"};
        Map<String, Object> roleMap = getPtoRoleUserList(args,userInfoScope);
         
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "ptoInfoVo", JsonHelper.fromBeanToJsonString( ptoInfoVo==null?"":ptoInfoVo ) );
        map.put( "attachmentMap", JsonHelper.fromBeanToJsonString( "" ) );
        map.put( "roleUserMap", JSONArray.fromObject(roleMap) );
        map.put( "taskId", JsonHelper.fromBeanToJsonString( "" ) );
        List<String> privTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "pto", "xj" ); //新建权限
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( privTypes ));
      //TODO 是否需要启动流程（例如：SWF只要新建、然后确定是执行了还是作废了的操作，并没有汇报操作，也就是做完之后回来点击下鼠标，单就结束了）
        boolean isNeedFlow = CommonUtil.hasEnumValue( iEnumerationManager, "PTO_NEED_FLOW", "Y", siteid );
        map.put("isNeedFlow",isNeedFlow);
        
        ModelAndView modelAndView = new ModelAndView( "pto/newPto.jsp", map );
        return modelAndView;
    }
    
    
    private PtoInfoVo sptoToPtoInfoVo(SptoInfoVo sptoInfo, List<PtoOperItem> operItems) {
        PtoInfoVo ptoInfoVo = new PtoInfoVo();
        ptoInfoVo.setTask( sptoInfo.getMission() );
        ptoInfoVo.setType( sptoInfo.getType() );
        ptoInfoVo.setAssetId( sptoInfo.getEquipment() ); 
        ptoInfoVo.setAssetName( sptoInfo.getEquipmentName() );
        ptoInfoVo.setSptoId( sptoInfo.getId() ) ;
        ptoInfoVo.setPtoOperItemList( operItems );
        ptoInfoVo.setOperItemRemarks( sptoInfo.getOperItemRemarks() );
        return ptoInfoVo;
    }
    
    private PtoInfoVo ptoToPtoInfoVo(PtoInfoVo ptoInfo) {
        PtoInfoVo ptoInfoVo = new PtoInfoVo();
        ptoInfoVo.setWindStation( ptoInfo.getWindStation() );
        ptoInfoVo.setTask( ptoInfo.getTask() );
        ptoInfoVo.setType( ptoInfo.getType() );
        ptoInfoVo.setAssetId( ptoInfo.getAssetId() ); 
        ptoInfoVo.setAssetName( ptoInfo.getAssetName() );
        ptoInfoVo.setGuardian( ptoInfo.getGuardian() );
        ptoInfoVo.setCommander( ptoInfo.getCommander() );
        ptoInfoVo.setPreBeginOperTime( ptoInfo.getPreBeginOperTime() );
        ptoInfoVo.setPreEndOperTime( ptoInfo.getPreEndOperTime() );
        ptoInfoVo.setPtoOperItemList( ptoInfo.getPtoOperItemList() );
        ptoInfoVo.setOperItemRemarks( ptoInfo.getOperItemRemarks() );
        return ptoInfoVo;
    }
    
    @RequestMapping(value = "/todolistToPtoPage")
    @ReturnEnumsBind("PTW_PTO_STATUS,PTW_SPTO_TYPE,WO_WIND_STATION")
    public ModelAndView todolistToPtoPage() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String ptoId = userInfoScope.getParam( "id" );
      //TODO 是否需要启动流程（例如：SWF只要新建、然后确定是执行了还是作废了的操作，并没有汇报操作，也就是做完之后回来点击下鼠标，单就结束了）
        boolean isNeedFlow = CommonUtil.hasEnumValue( iEnumerationManager, "PTO_NEED_FLOW", "Y", siteid );
        
        String[] args = new String[]{siteid+"_PTOGUARDIAN",siteid+"_PTOCOMMANDER",siteid+"_PTOOPERATOR",
                                     siteid+"_PTOONDUTYP",siteid+"_PTOONDUTYM"};
        Map<String, Object> roleMap = getPtoRoleUserList(args,userInfoScope);
        
        PtoInfoVo ptoInfoVo = ptoInfoService.queryPtoInfoVoById( ptoId );
        String currStatus = ptoInfoVo.getCurrStatus();
        String workflowId = ptoInfoVo.getWorkflowId();
        String taskId = ptoInfoVo.getTaskId();
        
        String jumpUrl = "pto/newPto.jsp";
        if("report,obsolete".indexOf( currStatus ) >= 0 && isNeedFlow){
            jumpUrl = "pto/reportPto.jsp";
        }else if("end,obsolete".indexOf( currStatus ) >= 0 ){
        	jumpUrl = "pto/ptoDetail.jsp";
        }
        List<PtoAttachment> attachList = attachMatchService.queryPtoAttachmentById( ptoInfoVo.getId(), "PTO" );
        ArrayList<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "ptoInfoVo", JsonHelper.fromBeanToJsonString( ptoInfoVo ) );
        map.put( "ptoId", ptoId );
        map.put( "workflowKey", "ptw_"+siteid.toLowerCase()+"_pto" );
        map.put( "processInstId", workflowId==null?"":workflowId);
        map.put( "taskId", JsonHelper.fromBeanToJsonString( taskId==null?"":taskId ));
        map.put( "roleUserMap", JSONArray.fromObject(roleMap) );
        map.put( "attachmentMap", JsonHelper.fromBeanToJsonString(attachmentMap) );
        List<String> privTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "pto", "xj" ); //新建权限
        boolean newSptoFlag = false;  //是否有新建对应标准操作票权限
        List<String> sptoPrivTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "spto", "dev_dept_write" ); //新建标准工作票权限
        if(sptoPrivTypes.contains( ptoInfoVo.getType() )){
            newSptoFlag = true;
        }
        map.put( "privTypes",JsonHelper.fromBeanToJsonString( privTypes ));
        map.put( "newSptoPrivTypes",newSptoFlag);
        
        //新建按钮是否显示
        boolean newBtnShowFlag = ptwPtoSelectUserService.hasAuditPrivilege( "pto", null , "xj");
        map.put( "newBtnShowFlag", newBtnShowFlag );
        
        map.put("isNeedFlow",isNeedFlow);
        
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        
        return modelAndView ;
    }
    
    
    @RequestMapping(value = "/ptoListData", method = RequestMethod.POST)
    public Page<PtoInfo> ptoListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        Page<PtoInfo> page = userInfoScope.getPage();
        String selectTreeId = userInfoScope.getParam( "selectTreeId" );
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "ptoInfoMap", "ptw", "PtoInfoDao" );
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
        page.setParameter( "siteid", siteId );
        page.setParameter( "loginUser", userId );
        if ( selectTreeId != null && !"null".equals( selectTreeId ) ) {
            page.setParameter( "selectTreeId", selectTreeId ); // 设置树查询参数
        }
        page = ptoInfoService.queryPtoListInfo( page );
        return page;
    }
    
    
    @RequestMapping(value = "/commitPtodata", method = RequestMethod.POST)
    public Map<String, Object> commitPtodata() throws Exception {
        log.info( "--------------进入操作票提交功能commitPtodata（）-------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String ptoFormData = userInfoScope.getParam( "ptoForm" );// 获取前台传过来的form表单数据 
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" );
        String itemListString = userInfoScope.getParam( "itemList" );
        List<PtoOperItem> itemList = JsonHelper.toList( itemListString, PtoOperItem.class );
        
        PtoInfoVo ptoInfoVo = JsonHelper.toObject( ptoFormData, PtoInfoVo.class );
        ptoInfoVo.setPtoOperItemList( itemList );
        ptoInfoVo.setAttach( uploadIds );
        ptoInfoVo.setCommitStyle( commitStyle );
        ptoInfoVo.setDelFlag( "N" );
        
        boolean startWorkFlow = false;
        if ( !"save".equals( commitStyle ) ) {
            startWorkFlow = true;
        }
        
        Map<String, Object> mav = new HashMap<String, Object>( 0 );
        mav = ptoInfoService.saveOrUpdatePtoInfo( ptoInfoVo, startWorkFlow );
        mav.put( "result", "success" );
        return mav;
    }
    
    
    /**
     * @description:删除
     * @author: 王中华
     * @createDate: 2015-8-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deletePtoDraft", method = RequestMethod.POST)
    public Map<String, String> deletePtoDraft() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ptoInfoService.deletePtoInfo( userInfoScope.getParam( "id" ));
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
    /**
     * @description:作废
     * @author: 王中华
     * @createDate: 2015-8-4
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoletePtoInfo", method = RequestMethod.POST)
    public Map<String, String> obsoletePtoInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ptoInfoService.obsoletePtoInfo( userInfoScope.getParam( "id" ));// 获取前台传过来的form表单数据
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
    /**
     * @description:已执行，用户SWF不启动流程的情况
     * @author: 王中华
     * @createDate: 2016-5-19
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/hasDonePtoInfo", method = RequestMethod.POST)
    public Map<String, String> hasDonePtoInfo() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        ptoInfoService.hasDonePtoInfo( userInfoScope.getParam( "id" ));// 获取前台传过来的form表单数据
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
    @RequestMapping(value = "/queryInitdataFromSpto", method = RequestMethod.POST)
    public Map<String, String>  queryInitdataFromSpto() throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        
        String sptoInfoId = userInfoScope.getParam( "sptoId" );
        PtoInfoVo ptoInfoVo = null;
        if(!StringUtils.isEmpty(sptoInfoId)){
            Map<String, Object> sptoInfoMap = sptoInfoService.querySptoInfoById( sptoInfoId );
            SptoInfoVo sptoInfoVo = (SptoInfoVo) sptoInfoMap.get( "bean" );
            List<PtoOperItem> operItems = (List<PtoOperItem>) sptoInfoMap.get( "list" );
            ptoInfoVo = sptoToPtoInfoVo(sptoInfoVo,operItems);  //转化为ptoInfoVo
        }
        String resultData = JsonHelper.fromBeanToJsonString(ptoInfoVo==null?"":ptoInfoVo );
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        mav.put( "data", resultData );
        return mav;
    }
        
        
    
    /**
     * @description: 抽查操作票
     * @author: 王中华
     * @createDate: 2015-8-7
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkPto", method = RequestMethod.POST)
    public Map<String, String> checkPto() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String ptoId = userInfoScope.getParam( "ptoId" );
        String ptoCheckForm = userInfoScope.getParam( "ptoCheckForm" );
        JSONObject jsonObject = JSONObject.fromObject(ptoCheckForm);
        String isProper = (String)jsonObject.get( "isProper" );
        String problem =  (String)jsonObject.get( "problem" );
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "ptoId", ptoId );
        params.put( "isProper", isProper );
        params.put( "problem", problem );
        params.put( "modifyUser", userId );
        params.put( "modifyDate", new Date() );
        
        ptoInfoService.updatePtoInfoOnCheck(params);
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }   
    
    
   
}
