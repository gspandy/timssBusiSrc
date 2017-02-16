package com.timss.ptw.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.ptw.bean.PtoAttachment;
import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.bean.SptoInfo;
import com.timss.ptw.service.AttachMatchService;
import com.timss.ptw.service.PtoInfoService;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.service.SptoInfoService;
import com.timss.ptw.vo.PtoInfoVo;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: 标准操作票信息的Controller
 * @description: {desc}
 * @company: gdyd
 * @className: SptoInfoController.java
 * @author: 谷传伟
 * @createDate: 2015-7-1
 * @updateUser: 谷传伟
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "ptw/sptoInfo")
public class SptoInfoController {
    private static final Logger LOG = Logger.getLogger( SptoInfoController.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private SptoInfoService sptoInfoService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private AttachMatchService attachMatchService;
    @Autowired
    private PtoInfoService ptoInfoService;
    @Autowired 
    private PtwPtoSelectUserService ptwPtoSelectUserService;
    
    
    @RequestMapping(value = "/sptoCodeMultiSearch")
    @ResponseBody
    public List<Object> sptoCodeMultiSearch() throws Exception {
        List<Object> resultMap = new ArrayList<Object>();

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String value = userInfoScope.getParam( "kw" );
        if("".equals( value )){
            value = "nullString";
        }
        Page<SptoInfoVo> page = new Page<SptoInfoVo>();
        page = new Page<SptoInfoVo>();
        page.setPageSize( 20 );
        page.setParameter( "sptoCode", value );
        page.setParameter( "siteid", userInfoScope.getSiteId() );
        page.setParameter( "nowTime", new Date() );
        page = sptoInfoService.querySptoInfoByCode( page );
        int size = page.getResults().size();
        if ( size > 11 ) {
            size = 11;
        }

        for ( int i = 0; i < size; i++ ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "id", page.getResults().get( i ).getId() );
            jsonObject.put( "code", page.getResults().get( i ).getCode() );
            jsonObject.put( "beginTime", page.getResults().get( i ).getBeginTime() );
            jsonObject.put( "endTime", page.getResults().get( i ).getEndTime() );
            jsonObject.put( "mission", page.getResults().get( i ).getMission() );
            jsonObject.put( "equipment", page.getResults().get( i ).getEquipment() );
            jsonObject.put( "equipmentName", page.getResults().get( i ).getEquipmentName() );
            jsonObject.put( "type", page.getResults().get( i ).getType() );
            jsonObject.put( "operItemRemarks", page.getResults().get( i ).getOperItemRemarks() );
            resultMap.add( jsonObject );
        }

        return resultMap;
    }
    
    
    /**
     * @description:跳转至标准操作票列表
     * @author: 谷传伟
     * @createDate: 2015-7-1
     * @return:
     * @throws Exception
     * @throws RuntimeException
     */
    @RequestMapping(value = "/preQuerySptoInfoVoList")
    @ReturnEnumsBind("PTW_SPTO_STATUS,PTW_SPTO_TYPE")
    public ModelAndView preQuerySptoInfoVoList() throws Exception {
        List<String> privTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "spto", "dev_dept_write" );
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        map.put( "privTypes", JsonHelper.fromBeanToJsonString( privTypes ) );
        String jumpUrl =  "pto/sptoList.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    
    
    /**
     * @description:弹出设置有效时间页面
     * @author: 王中华
     * @createDate: 2016-4-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/setValidTimePage")
    public ModelAndView setValidTimePage() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        String jumpUrl =  "pto/setSptoValidTime.jsp";
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }
    
    
    /**
     * @description:设置有效时间
     * @author: 王中华
     * @createDate: 2016-4-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/setValidTimeData")
    public  Map<String, Object>  setValidTimeData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单
        String beginTimeStr = userInfoScope.getParam( "beginTime" );
        String endTimeStr = userInfoScope.getParam( "endTime" );
        Date beginTime = new Date(  Long.parseLong(beginTimeStr) );
        Date endTime = new Date(  Long.parseLong(endTimeStr) );
        Map<String, Object> mav = new HashMap<String, Object>();
        int flag = sptoInfoService.updateValidTime(id,beginTime,endTime);
        if(flag > 0){
            mav.put( "result", "success" );
        }else{
            mav.put( "result", "fail" );
        }
        return mav;
    }
    
    
    /**
     * @description:检查设置的有效时间是否合法
     * @author: 王中华
     * @createDate: 2016-4-18
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkValidTimeData")
    public  Map<String, Object>  checkValidTimeData() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String sptoId = userInfoScope.getParam( "id" );
        if("".equals( sptoId )){
            sptoId = null;
        }
        String sptoCode = userInfoScope.getParam( "code" );// 获取前台传过来的form表单
        String beginTimeStr = userInfoScope.getParam( "beginTime" );
        String endTimeStr = userInfoScope.getParam( "endTime" );
        Date beginTime = new Date(  Long.parseLong(beginTimeStr) );
        Date endTime = new Date(  Long.parseLong(endTimeStr) );
        Map<String, Object> mav = new HashMap<String, Object>();
        boolean flag = sptoInfoService.checkValidTimeData(sptoId,sptoCode,beginTime,endTime);
        mav.put( "result", flag );
        return mav;
    }
    
    
    /**
     * @description: 打开读取标准操作票
     * @author: gucw
     * @createDate: 2015-7-10
     * @return:
     */
    @RequestMapping(value = "/todolistTOSptoPage")
    @ReturnEnumsBind("PTW_SPTO_STATUS,PTW_SPTO_TYPE")
    public ModelAndView todolistTOSptoPage() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单
        map.put( "sptoId", id );
        map.put( "defKey", "ptw_core_spto" );
        //其值是在setFormVal的方法中设置
        map.put( "sptoItems", JsonHelper.fromBeanToJsonString( new ArrayList<Object>(0) ) );
        map.put( "sptoData", JsonHelper.fromBeanToJsonString( new HashMap<Object, Object>(0) ) );
        map.put( "sptoAttachment", JsonHelper.fromBeanToJsonString( new HashMap<Object, Object>(0) ) );
        map.put( "title", "" );
        String jumpUrl = "pto/sptoInfo.jsp";
        List<String> privTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "spto", "dev_dept_write" );
        map.put( "privTypes", JsonHelper.fromBeanToJsonString( privTypes ) );
        List<String> privTypes_pto = ptwPtoSelectUserService.queryPrivilegeTypes( "pto", "xj" );
        map.put( "privTypes_pto", JsonHelper.fromBeanToJsonString( privTypes_pto ) );
        ModelAndView modelAndView = new ModelAndView( jumpUrl, map );
        return modelAndView;
    }

    /**
     * @description:打开新增标准操作票
     * @author: 谷传伟
     * @createDate: 2015-7-8
     * @return:
     * @throws Exception
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/newSpto")
    @ReturnEnumsBind("PTW_SPTO_TYPE")
    public ModelAndView addSptoInfo() throws Exception  {
        Map<String, Object> map = new HashMap<String, Object>( 0 );
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        SecureUser user = infoScope.getSecureUser();
        String userId = user.getId();
        String userName = user.getName();
        String deptName = infoScope.getOrgName();
        String categoryParams = "";
        String categoryId = "";
        String categoryName = "";
        //修改标准操作票
        String sptoInfoId = "";
        //从操作票创建标准操作票
        String ptoInfoId = "";
        String sptoData = "";
        String sptoItems = "";
        String sptoAttachment = "";
        String title = "";
        //是否是修改操作票
        String modify = "";
        try {
            modify = infoScope.getParam( "modify" );
            title = infoScope.getParam( "title" );
            ptoInfoId = infoScope.getParam( "ptoInfoId" );
            sptoInfoId = infoScope.getParam( "sptoInfoId" );
            if ( StringUtils.isNotEmpty(sptoInfoId) ) {
                Map<String, Object> sptoMap = sptoInfoService.querySptoInfoById( sptoInfoId );
                SptoInfoVo sptoInfoVoTmp = (SptoInfoVo) sptoMap.get( "bean" );
                SptoInfoVo sptoInfoVo = new SptoInfoVo();
                sptoInfoVo.setId( sptoInfoVoTmp.getId());
                sptoInfoVo.setMission( sptoInfoVoTmp.getMission());
                sptoInfoVo.setType( sptoInfoVoTmp.getType() );
                sptoInfoVo.setEquipment( sptoInfoVoTmp.getEquipment() );
                sptoInfoVo.setEquipmentName( sptoInfoVoTmp.getEquipmentName() );
                sptoInfoVo.setCode( sptoInfoVoTmp.getCode() );
                sptoInfoVo.setBeginTime( sptoInfoVoTmp.getBeginTime() );
                sptoInfoVo.setEndTime( sptoInfoVoTmp.getEndTime() );
                sptoInfoVo.setOperItemRemarks( sptoInfoVoTmp.getOperItemRemarks() );
                List<PtoOperItem> sptoItemsList  = (List<PtoOperItem>) sptoMap.get( "list" );
                List<PtoAttachment> attachList = attachMatchService.queryPtoAttachmentById( sptoInfoId, "SPTO" );
                // 根据附件id，转化数据传前台，前台显示附件数据
                List<String> aList = new ArrayList<String>();
                for ( int i = 0; i < attachList.size(); i++ ) {
                    aList.add( attachList.get( i ).getAttachId() );
                }
                List<Map<String, Object>> sptoAttachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
                sptoData = JsonHelper.fromBeanToJsonString( sptoInfoVo );
                sptoItems = JsonHelper.fromBeanToJsonString( sptoItemsList );
                sptoAttachment = JsonHelper.fromBeanToJsonString( sptoAttachmentMap );
            }
            if(StringUtils.isNotEmpty(ptoInfoId)){
                PtoInfoVo ptoInfoVo = ptoInfoService.queryPtoInfoVoById( ptoInfoId );
                List<PtoOperItem> sptoItemsMap = ptoInfoVo.getPtoOperItemList();
                SptoInfoVo sptoInfoVo = new SptoInfoVo();
                sptoInfoVo.setMission( ptoInfoVo.getTask());
                sptoInfoVo.setType( ptoInfoVo.getType() );
                sptoInfoVo.setEquipment( ptoInfoVo.getAssetId() );
                sptoInfoVo.setEquipmentName( ptoInfoVo.getAssetName() );
                sptoInfoVo.setOperItemRemarks( ptoInfoVo.getOperItemRemarks() );
                sptoData = JsonHelper.fromBeanToJsonString( sptoInfoVo );
                sptoItems = JsonHelper.fromBeanToJsonString( sptoItemsMap );
            }
            categoryParams = infoScope.getParam( "categoryParams" );
            if(StringUtils.isNotEmpty(categoryParams)){
                Map<String, Object> categoryParamsMap = JsonHelper.fromJsonStringToBean( categoryParams, HashMap.class );
                categoryId = categoryParamsMap.get( "categoryId" ).toString();
                categoryName = categoryParamsMap.get( "categoryName" ).toString();   
            }
        } catch (Exception e) {
            LOG.warn( "addSptoInfo获取参数异常", e );
        }
        map.put( "userId", userId );
        map.put( "userName", userName );
        map.put( "deptName", deptName );
        map.put( "createUserInfo", userName + "/" + deptName );
        map.put( "sptoId", "null" );
        map.put( "categoryId", categoryId );
        map.put( "categoryName", categoryName );
        map.put( "modify", modify );
        map.put( "defKey", "ptw_core_spto" );
        if(StringUtils.isEmpty( sptoData )){
            sptoData = JsonHelper.fromBeanToJsonString( new HashMap<Object, Object>(0) );
        }
        if(StringUtils.isEmpty( sptoItems )){
            sptoItems = JsonHelper.fromBeanToJsonString( new ArrayList<Object>(0) );
        }
        if(StringUtils.isEmpty( sptoAttachment )){
            sptoAttachment = JsonHelper.fromBeanToJsonString( new ArrayList<Object>(0) );
        }
        map.put( "sptoItems", sptoItems );
        map.put( "sptoData", sptoData );
        map.put( "sptoAttachment",sptoAttachment );
        map.put( "title", title );
        List<String> privTypes = ptwPtoSelectUserService.queryPrivilegeTypes( "spto", "dev_dept_write" );
        map.put( "privTypes", JsonHelper.fromBeanToJsonString( privTypes ) );
        List<String> privTypes_pto = ptwPtoSelectUserService.queryPrivilegeTypes( "pto", "xj" );
        map.put( "privTypes_pto", JsonHelper.fromBeanToJsonString( privTypes_pto ) );
        ModelAndView modelAndView = new ModelAndView( "pto/sptoInfo.jsp", map );
        return modelAndView;
    }

    /**
     * @description:跳转至标准操作票列表对话框-选择参考标准操作票(旧)
     * @author: 谷传伟
     * @createDate: 2015-7-13
     * @return:
     * @throws Exception
     * @throws RuntimeException
     */
    @RequestMapping(value = "/preQuerySptoInfoVoListDlg")
    @ReturnEnumsBind("PTW_SPTO_STATUS,PTW_SPTO_TYPE")
    public String preQuerySptoInfoVoListDlg() {
        return "pto/sptoListDlg.jsp";
    }
    /**
     * @description:跳转至标准操作票列表对话框-选择标准操作票(操作票选择标准操作票)
     * @author: 谷传伟
     * @createDate: 2015-8-6
     * @return:
     * @throws Exception
     * @throws RuntimeException
     */
    @RequestMapping(value = "/sptoListDlgOth")
    @ReturnEnumsBind("PTW_SPTO_STATUS,PTW_SPTO_TYPE")
    public String sptoListDlgOth() {
        return "pto/sptoListDlgOth.jsp";
    }
    /**
     * @description: 查询标准操作票列表数据
     * @author: gucw
     * @createDate: 2015-7-8
     * @return Page
     * @throws Exception
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/sptoRdListData", method = RequestMethod.POST)
    public Page<SptoInfo> sptoRdListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        Page<SptoInfo> page = userInfoScope.getPage();
        String selectTreeId = userInfoScope.getParam( "selectTreeId" );
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        String showHis = userInfoScope.getParam( "showHis" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "sptoInfoMap", "ptw", "SptoInfoDao" );
        if ( fuzzySearchParams != null ) {
            Map<String, Object> fuzzyParams = (HashMap<String, Object>) MapHelper.jsonToHashMap( fuzzySearchParams );
            if ( fuzzyParams.get( "selectTreeId" ) != null ) {
                selectTreeId = fuzzyParams.get( "selectTreeId" ).toString();
                fuzzyParams.remove( "selectTreeId" ); // 因为选择左边树的查询不同与表头查询，所有要移除
            }
            if ( fuzzyParams.get( "onRunningSpto" ) != null ) {
                String onRunningSpto = fuzzyParams.get( "onRunningSpto" ).toString();
                if("true".equals( onRunningSpto )){
                    page.setParameter( "onRunningSpto", "true" );
                }
                fuzzyParams.remove( "onRunningSpto" );
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
        page = sptoInfoService.querySptoInfo( page );
        return page;
    }

    
    
    @RequestMapping(value = "/sameCodeSptoListData", method = RequestMethod.POST)
    public Page<SptoInfoVo> sameCodeSptoListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String sptoCode = userInfoScope.getParam( "code" );
        String sptoId = userInfoScope.getParam( "id" );
        Page<SptoInfoVo> page = userInfoScope.getPage();
        page.setParameter( "code", sptoCode );
        page.setParameter( "id", sptoId );
        page.setParameter( "siteid", siteid );
        Page<SptoInfoVo> sameCodeSptoList = sptoInfoService.querySameCodeSptoList(page );
        return sameCodeSptoList;
    }
    
    @RequestMapping(value = "/hasSameCodeSptoInAudit", method = RequestMethod.POST)
    public Map<String, Object> hasSameCodeSptoInAudit() throws JsonParseException, Exception {
        Map<String, Object> mav = new HashMap<String, Object>();
        mav.put( "result", false );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String sptoCode = userInfoScope.getParam( "sptoCode" );
        String sptoId = userInfoScope.getParam( "id" );
        if("".equals( sptoId )){
            sptoId = null ;
        }
        Page<SptoInfoVo> page = userInfoScope.getPage();
        int pageSize = page.getPageSize();
        page.setPageSize( 100 );  //设置100条记录
        page.setParameter( "code", sptoCode );
        page.setParameter( "id", sptoId );
        page.setParameter( "siteid", siteid );
        Page<SptoInfoVo> sameCodeSptoList = sptoInfoService.querySameCodeSptoList(page );
        page.setPageSize( pageSize );  //恢复数据
        List<SptoInfoVo> sptoList = sameCodeSptoList.getResults();
        for ( SptoInfoVo sptoInfoVo : sptoList ) {
            String sptoStatus = sptoInfoVo.getStatus();
            if("firststep,secondstep,thirdstep".indexOf( sptoStatus ) >= 0 ){
                //非 已通过、作废 状态，即没有在审批中的同编号的标准票
                mav.put( "result", true );
                return mav;
            }
            
        }
        
        return mav;
    }
    
    
    
    
    /**
     * @description: 提交标注操作票数据
     * @author: gucw
     * @createDate: 2015-7-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitSptodata", method = RequestMethod.POST)
    public Map<String, Object> commitSptodata() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String sptoFormData = userInfoScope.getParam( "sptoForm" );// 获取前台传过来的form表单数据 
        String itemListString = userInfoScope.getParam( "itemList" );//获取前台传过来的数据表格数据
        SptoInfoVo sptoInfoVo = JsonHelper.toObject( sptoFormData, SptoInfoVo.class );
        List<PtoOperItem> itemList = JsonHelper.toList( itemListString, PtoOperItem.class );
        sptoInfoVo.setPtoOperItemList( itemList );
        String commitStyle = userInfoScope.getParam( "commitStyle" );// 提交方式（用于确定是点击的“提交”还是“暂存”）
        String uploadIds = userInfoScope.getParam( "uploadIds" );
        boolean startWorkFlow = false;
        if ( !"save".equals( commitStyle ) ) {
            startWorkFlow = true;
        }
        if ( "modifyCommit".equals( commitStyle ) ) {
            sptoInfoVo.setId( null );
            sptoInfoVo.setProcinstId( null );
            sptoInfoVo.setCommitStyle( commitStyle );
        }
        Map<String, Object> mav = new HashMap<String, Object>( 0 );
        sptoInfoVo.setAttach( uploadIds );
        mav = sptoInfoService.saveOrUpdateSptoInfo( sptoInfoVo, startWorkFlow );
        mav.put( "result", "success" );
        return mav;
    }
    
    /**
     * @description:查询标准操作票数据
     * @author: gchw
     * @createDate: 2015-7-10
     * @return
     * @throws Exception:
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/queryItSptoDataById")
    public Map<String, Object> queryItSptoDataById() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id =  userInfoScope.getParam( "id" );
        Map<String, Object> map = sptoInfoService.querySptoInfoById( id );
        SptoInfoVo sptoInfoVo = (SptoInfoVo) map.get( "bean" );
        List<PtoOperItem> sptoItems  = (List<PtoOperItem>) map.get( "list" );
        String taskId = (String) map.get( "taskId" );
        // 节点的候选人
        List<String> candidateUsers = new ArrayList<String>( 0 );
        if ( StringUtils.isNotEmpty( taskId ) ) {
            candidateUsers = workflowService.getCandidateUsers( taskId );
        }
        List<SecureUserGroup> groups = userInfoScope.getSecureUser().getGroups();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<PtoAttachment> attachList = attachMatchService.queryPtoAttachmentById( id, "SPTO" );
        // 根据附件id，转化数据传前台，前台显示附件数据
        List<String> aList = new ArrayList<String>();
        for ( int i = 0; i < attachList.size(); i++ ) {
            aList.add( attachList.get( i ).getAttachId() );
        }
        boolean newThisSptoPriv = ptwPtoSelectUserService.hasAuditPrivilege( "spto", sptoInfoVo.getType() , "dev_dept_write");
        List<Map<String, Object>> attachmentMap = FileUploadUtil.getJsonFileList( Constant.basePath, aList );
        resultMap.put( "attachmentMap", attachmentMap );
        resultMap.put( "sptoForm", JsonHelper.toJsonString( sptoInfoVo ) );
        resultMap.put( "taskId", taskId );
        resultMap.put( "list", sptoItems );
        resultMap.put( "candidateUsers", candidateUsers );
        resultMap.put( "groups", groups );
        resultMap.put( "newThisSptoPriv", newThisSptoPriv );
        return resultMap;
    }
    /**
     * @description: 查询标准操作票操作项
     * @author: gucw
     * @createDate: 2015-7-13
     * @return
     * @throws Exception:
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getSptoItems", method = RequestMethod.POST)
    public Map<String, Object> getSptoItems() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = userInfoScope.getParam( "id" );// 获取前台传过来的form表单数据 
        Map<String, Object> sptoInfoVoMap = sptoInfoService.querySptoInfoById( id ); 
        List<PtoOperItem> itemList = new ArrayList<PtoOperItem>(0);
        if(null!=sptoInfoVoMap){
            itemList = (List<PtoOperItem>)sptoInfoVoMap.get( "list" );
        }
        Map<String, Object> mav = new HashMap<String, Object>( 0 );
        mav.put( "result", itemList );
        return mav;
    }
    /**
     * @description:删除草稿
     * @author: gchw
     * @createDate: 2015-7-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteSptoDraft", method = RequestMethod.POST)
    public Map<String, String> deleteSptoDraft() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        sptoInfoService.deleteSptoInfo( userInfoScope.getParam( "id" ) );
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description: 作废
     * @author: gchw
     * @createDate: 2015-7-14
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/obsoleteSpto", method = RequestMethod.POST)
    public Map<String, String> obsoleteSpto() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        sptoInfoService.obsoleteSptoInfo( userInfoScope.getParam( "id" )  );// 获取前台传过来的form表单数据
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
   
}
