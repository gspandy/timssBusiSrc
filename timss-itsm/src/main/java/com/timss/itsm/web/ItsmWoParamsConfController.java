package com.timss.itsm.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.itsm.bean.ItsmComplainTypeConf;
import com.timss.itsm.bean.ItsmCustomerConf;
import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWoPriConfig;
import com.timss.itsm.bean.ItsmWoPriority;
import com.timss.itsm.bean.ItsmWoSkill;
import com.timss.itsm.service.ItsmComplainTypeConfService;
import com.timss.itsm.service.ItsmCustomerConfService;
import com.timss.itsm.service.ItsmWoFaultTypeService;
import com.timss.itsm.service.ItsmWoPriorityService;
import com.timss.itsm.service.ItsmWoSkillService;
import com.timss.itsm.util.ItsmConstant;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

@Controller
@RequestMapping(value = "/itsm/woParamsConf")
public class ItsmWoParamsConfController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmWoSkillService woSkillService;
    @Autowired
    private ItsmWoPriorityService woPriorityService;
    @Autowired
    private ItsmCustomerConfService itsmCustomerConfService;
    
    @Autowired
    private ItsmComplainTypeConfService compTypeConfService;
    

    /**
     * @description:工单标识列表页面
     * @author: 王中华
     * @createDate: 2014-8-22
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/woLabelList")
    public String woLabelList() {
        return "/woLabelList.jsp";
    }

    @RequestMapping(value = "/openWoLabelPage")
    public String openWoLabelPage() {
        return "/woParamsConf/newWoLabel.jsp";
    }

    /**
     * @description:工单优先级列表
     * @author: 王中华
     * @createDate: 2014-8-25
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/PriorityList")
    @ReturnEnumsBind("ITSM_URGENCY_DEGREE,ITSM_INFLUENCE_SCOPE")
    public String woPriorityList() {
        return "/woPriorityList.jsp";
    }

    @RequestMapping(value = "/openWoPriorityPage")
    @ReturnEnumsBind("ITSM_URGENCY_DEGREE,ITSM_INFLUENCE_SCOPE")
    public String openWoPriorityPage() {
        return "/woParamsConf/newWoPriority.jsp";
    }

    /**
     * @description:新建工单优先级
     * @author: 王中华
     * @createDate: 2014-8-30
     * @return
     * @throws NumberFormatException
     * @throws Exception:
     */
    @RequestMapping(value = "/commitWoPriority")
    public Map<String, String> commitWoPriority() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int woPriorityId = Integer.valueOf( userInfoScope.getParam( "woPriorityId" ) );

        String woPriorityForm = userInfoScope.getParam( "woPriorityForm" );// 获取前台传过来的form表单数据
        String woPriConfData = userInfoScope.getParam( "woPriConfData" );// 获取前台传过来的form表单数据

        Map<String, String> addWoPriorityDataMap = new HashMap<String, String>();
        addWoPriorityDataMap.put( "woPriorityForm", woPriorityForm );
        addWoPriorityDataMap.put( "woPriConfData", woPriConfData );

        if ( woPriorityId == 0 ) {
            woPriorityService.insertWoPriority( addWoPriorityDataMap );
        } else {
            woPriorityService.updateWoPriority( addWoPriorityDataMap );
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:优先级数据传回前台，填充combobox
     * @author: 王中华
     * @createDate: 2014-10-27
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/comboboxPriority")
    public @ResponseBody
    List<List<Object>> comboboxPriority() {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        Page<ItsmWoPriority> page = userInfoScope.getPage();
        page.setPageSize( 100 );
        page.setSortKey( "RESPOND_LEN" );
        page.setSortOrder( "DESC" );

        List<List<Object>> result = new ArrayList<List<Object>>();
        List<ItsmWoPriority> priorityList = woPriorityService.queryWoPriorityList( page ).getResults();

        for ( ItsmWoPriority priority : priorityList ) {
            List<Object> row = new ArrayList<Object>();
            row.add( priority.getId() );
            double hour1 = priority.getRespondLength();
            double hour2 = priority.getSolveLength();
            int a = (int) hour1;
            int b = (int) hour2;
            // 控制显示时，小数点后为0时隐藏
            String timeString1 = "";
            String timeString2 = "";
            if ( (int) hour1 == a ) {
                timeString1 = String.valueOf( a );
            } else {
                timeString1 = String.valueOf( hour1 );
            }
            if ( (int) hour2 == b ) {
                timeString2 = String.valueOf( b );
            } else {
                timeString2 = String.valueOf( hour2 );
            }

            row.add( priority.getName() + "    (响应" + timeString1 + "分钟、解决" + timeString2 + "小时)" );
            result.add( row );
        }

        return result;
    }

    /**
     * @description:优先级列表
     * @author: 王中华
     * @createDate: 2014-8-30
     * @return
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws Exception:
     */
    @RequestMapping(value = "/woPriorityListData")
    public Page<ItsmWoPriority> woPriorityListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<ItsmWoPriority> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "priorityMap", ItsmConstant.MODULENAME,
                "ItsmWoPriorityDao" );

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
            page.setSortKey( "RESPOND_LEN" );
            page.setSortOrder( "DESC" );
            page.setParameter( "siteid", siteId );
        }

        page = woPriorityService.queryWoPriorityList( page );

        return page;
    }

    @RequestMapping(value = "/queryWoPriorityDataById")
    public Map<String, String> queryWoPriorityDataById() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        int woPriorityId = Integer.valueOf( userInfoScope.getParam( "woPriorityId" ) );
        Map<String, Object> resultMap = woPriorityService.queryWoPriorityById( woPriorityId, siteid );
        String woPriorityJsonStr = JsonHelper.toJsonString( resultMap.get( "baseData" ) );
        String woPriConfigJsonStr = JsonHelper.toJsonString( resultMap.get( "datagridData" ) );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "baseData", woPriorityJsonStr );
        returnData.put( "datagridData", woPriConfigJsonStr );
        return returnData;
    }

    @RequestMapping(value = "/deletePriority")
    public Map<String, String> deletePriority() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        int woPriorityId = Integer.valueOf( userInfoScope.getParam( "priId" ) );
        woPriorityService.deleteWoPriority( woPriorityId, siteid );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "result", "success" );
        return returnData;
    }

    /**
     * @description: 根据影响度和紧急度，查询对应的服务级别ID
     * @author: 王中华
     * @createDate: 2014-12-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/getPriIdValByUrgentInfluence")
    public Map<String, String> getPriIdValByUrgentInfluence() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String urgentVal = userInfoScope.getParam( "urgentVal" );
        String influenceVal = userInfoScope.getParam( "influenceVal" );

        String priId = woPriorityService.getPriIdValByUrgentInfluence( urgentVal, influenceVal ).get( "priId" );

        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "woPriorityId", String.valueOf( priId ) );

        return returnData;
    }

    /**
     * @description: 根据影响度和紧急度，查询同一个站点下是否有相同的配置，如果有，则将相同配置记录返回
     * @author: 王中华
     * @createDate: 2014-12-9
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/checkHasConfigData")
    public Map<String, String> checkHasConfigData() throws Exception {
        Map<String, String> returnData = new HashMap<String, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woPriConfData = userInfoScope.getParam( "woPriConfData" );
        String priId = userInfoScope.getParam( "priId" );
        JSONObject woPriConfJsonObj = JSONObject.fromObject( woPriConfData );
        int woPriConfDatagridNum = Integer.valueOf( woPriConfJsonObj.get( "total" ).toString() ); // 记录数
        JSONArray woPriConfJsonArray = woPriConfJsonObj.getJSONArray( "rows" ); // 记录数组

        for ( int i = 0; i < woPriConfDatagridNum; i++ ) {
            String itemsRecord = woPriConfJsonArray.get( i ).toString(); // 某条记录的字符串表示
            ItsmWoPriConfig woPriConfig = JsonHelper.fromJsonStringToBean( itemsRecord, ItsmWoPriConfig.class );
            String urgentVal = woPriConfig.getUrgentDegree();
            String influenceVal = woPriConfig.getInfluenceScope();
            Map<String, String> priConfHashMap = woPriorityService.getPriIdValByUrgentInfluence( urgentVal,
                    influenceVal );
            String priIdInDb = priConfHashMap.get( "priId" );
            String priName = priConfHashMap.get( "priName" );
            if ( !("0".equals( priId ) && "".equals( priName )) ) { // 如果查到有重复的配置，直接返回
                returnData.put( "urgentVal", urgentVal );
                returnData.put( "influenceVal", influenceVal );
                if ( "0".equals( priId ) ) { // 新建时的验证
                    returnData.put( "woPriId", priIdInDb );
                    returnData.put( "woPriName", priName );
                    return returnData;
                } else { // 编辑修改状态的验证
                    if ( !priId.equals( priIdInDb ) ) {
                        returnData.put( "woPriId", priIdInDb );
                        returnData.put( "woPriName", priName );
                        return returnData;
                    }
                }

            }
        }

        returnData.put( "woPriId", "0" );
        returnData.put( "woPriName", "" );
        return returnData;
    }

    /**
     * @description:技能列表
     * @author: 王中华
     * @createDate: 2014-8-25
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/skillList")
    public String skillList() {
        return "/woSkillList.jsp";
    }

    @RequestMapping(value = "/openSkillPage")
    public String openSkillPage() {
        return "/woParamsConf/newSkill.jsp";
    }

    @RequestMapping(value = "/skillListData")
    public Page<ItsmWoSkill> skillListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ItsmWoSkill> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "skillMap", ItsmConstant.MODULENAME,
                "ItsmWoSkillDao" );

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
        }

        page = woSkillService.queryWoSkillList( page );

        return page;
    }

    /**
     * @description:新建技能的保存
     * @author: 王中华
     * @createDate: 2014-8-27
     * @return:
     * @throws Exception
     * @throws NumberFormatException
     */
    @RequestMapping(value = "/commitWoSkill")
    public Map<String, String> commitWoSkill() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int woSkillId = Integer.valueOf( userInfoScope.getParam( "woSkillId" ) ); // 获取作业方案的ID，若是新建的，则为0

        String skillForm = userInfoScope.getParam( "skillForm" );// 获取前台传过来的form表单数据

        Map<String, String> addWoSkillDataMap = new HashMap<String, String>();
        addWoSkillDataMap.put( "skillForm", skillForm );

        if ( woSkillId == 0 ) {
            woSkillService.insertWoSkill( addWoSkillDataMap );
        } else {
            woSkillService.updateWoSkill( addWoSkillDataMap );
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    /**
     * @description:根据ID查询技能
     * @author: 王中华
     * @createDate: 2014-8-30
     * @return
     * @throws NumberFormatException
     * @throws Exception:
     */
    @RequestMapping(value = "/queryWoSkillDataById")
    public Map<String, String> queryWoSkillDataById() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int woSkillId = Integer.valueOf( userInfoScope.getParam( "woSkillId" ) );
        ItsmWoSkill woSkillObj = woSkillService.queryWoSkillById( woSkillId );
        String woSkillJsonStr = JsonHelper.toJsonString( woSkillObj );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "result", woSkillJsonStr );
        return returnData;
    }

    @RequestMapping(value = "/customerList")
    public String customerList() {
        return "/customerList.jsp";
    }

    @RequestMapping(value = "/customerConfListData")
    @ResponseBody
    public Map<String, Object> customerConfListData() throws JsonParseException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ItsmCustomerConf> page = userInfoScope.getPage();
        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "ItsmCustomerConfMap",
                ItsmConstant.MODULENAME, "ItsmCustomerConfDao" );

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
        }
        page = itsmCustomerConfService.queryAllCustomerConf( page );

        Map<String, Object> result = new HashMap<String, Object>();
        result.put( "rows", page.getResults() == null ? new String[0] : page.getResults() );
        result.put( "total", page.getTotalRecord() );
        return result;
    }

    @RequestMapping(value = "/openCustoerConfPage")
    public String openCustoerConfPage() {
        return "/woParamsConf/customerConf.jsp";
    }

    @RequestMapping(value = "/commitCustomerConf")
    public Map<String, String> commitCustomerConf() throws JsonMappingException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String CustomerConfForm;
        // 获取前台传过来的form表单数据
        CustomerConfForm = userInfoScope.getParam( "customerConfForm" );

        ItsmCustomerConf customerConf = JsonHelper.toObject( CustomerConfForm, ItsmCustomerConf.class );
        int id = customerConf.getId();
        if ( id == 0 ) {
            customerConf.setCreatedate( new Date() );
            customerConf.setCreateuser( userId );
            customerConf.setYxbz( 1 );
            itsmCustomerConfService.insertCustomerConf( customerConf );
        } else {
            customerConf.setModifydate( new Date() );
            customerConf.setModifyuser( userId );
            itsmCustomerConfService.updateCustomerConf( customerConf );
        }

        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }

    @RequestMapping(value = "/queryCustomerConfDataById")
    public Map<String, String> queryCustomerConfDataById() throws NumberFormatException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int customerConfId = Integer.valueOf( userInfoScope.getParam( "customerConfId" ) );
        ItsmCustomerConf result = itsmCustomerConfService.queryCustomerConfById( customerConfId );
        String customerConfJsonStr = JsonHelper.toJsonString( result );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "baseData", customerConfJsonStr );
        return returnData;
    }

    @RequestMapping(value = "/deleteCustomerConf")
    public Map<String, String> deleteCustomerConf() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        int customerConfId = Integer.valueOf( userInfoScope.getParam( "id" ) );
        itsmCustomerConfService.deleteCustomerConf( customerConfId );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "result", "success" );
        return returnData;
    }

    @RequestMapping(value = "/judgeRepeatCustomerConf")
    public Map<String, String> judgeRepeatCustomerConf() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String customerCode = userInfoScope.getParam( "customerCode" );
        int num = itsmCustomerConfService.judgeRepeatCustomerConf( customerCode );
        Map<String, String> returnData = new HashMap<String, String>();
        if ( num != 0 ) {
            returnData.put( "result", "fail" );
        } else {
            returnData.put( "result", "success" );
        }
        return returnData;
    }

    @RequestMapping(value = "/getInitPriority")
    public Map<String, String> getInitPriority() throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String customerCode = userInfoScope.getParam( "customerCode" );// 获取库存传过来的数据
        String faultTypeId = userInfoScope.getParam( "faultTypeId" );
        ItsmCustomerConf result = itsmCustomerConfService.getInitPriority( customerCode, faultTypeId );

        Map<String, String> mav = new HashMap<String, String>();
        if ( result == null ) {
            mav.put( "initPriorityId", null );
        } else {
            mav.put( "initPriorityId", result.getInitPriorityId() );
        }

        return mav;
    }

    @RequestMapping(value="/complainTypeList",method=RequestMethod.GET)
	public String complainTypeList() throws Exception{
		return "itsmComplainTypeList.jsp";
	}
    /*新建投诉类别*/
    @RequestMapping(value="/openComplainTypePage",method=RequestMethod.GET)
	public String newComplainType() throws Exception{
		return "/woParamsConf/complainTypeConf.jsp";
	}
    /*保存投诉类别配置*/
    @RequestMapping(value = "/commitComplainTypeConf", method = RequestMethod.POST)
    public Map<String, String> commitComplainTypeConf() throws NumberFormatException, Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String complainTypeConfForm = userInfoScope.getParam( "complainTypeConfForm" );// 获取前台传过来的form表单数据
        String complainTypeConfId =userInfoScope.getParam( "complainTypeConfId" );
        Map<String, String> addcomplainTypeConfDataMap = new HashMap<String, String>();
        addcomplainTypeConfDataMap.put( "complainTypeConfForm", complainTypeConfForm );
       
        if(complainTypeConfId.equals("0")){
        	compTypeConfService.insertComplainTypeConf( addcomplainTypeConfDataMap );
        }else{
        	compTypeConfService.updateComplainTypeConf(addcomplainTypeConfDataMap);
        }
        Map<String, String> mav = new HashMap<String, String>();
        mav.put( "result", "success" );
        return mav;
    }
    
    @RequestMapping(value = "/queryComplainTypeConfDataById", method = RequestMethod.POST)
    public Map<String, String> queryComplainTypeConfDataById() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String complainTypeConfId =userInfoScope.getParam( "complainTypeConfId" );
        ItsmComplainTypeConf conf=compTypeConfService.queryComplainTypeConfById(complainTypeConfId);
        String complTypeConfJsonStr = JsonHelper.toJsonString( conf );
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "result", complTypeConfJsonStr );
        return returnData;
    }
    
    /*投诉类别列表*/
    @RequestMapping(value = "/complainTypeListData", method = RequestMethod.POST)
    public Page<ItsmComplainTypeConf> complainTypeListData() throws  Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        Page<ItsmComplainTypeConf> page = userInfoScope.getPage();

        String fuzzySearchParams = userInfoScope.getParam( "search" );
        Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "complainTypeConfMap", ItsmConstant.MODULENAME,
                "ItsmComplainTypeConfDao" );

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
            page.setParameter( "siteid", siteId );
        }
        page=compTypeConfService.queryComplainTypeConfList(page);
		return page;
    }
    /*进行逻辑删除*/
    @RequestMapping(value = "/delComplainType")
    public Map<String, String> delComplainType() throws NumberFormatException, Exception {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        String complainTypeConfId =userInfoScope.getParam( "complainTypeConfId" );
        compTypeConfService.deleteComplainTypeConfById(complainTypeConfId);
        Map<String, String> returnData = new HashMap<String, String>();
        returnData.put( "result", "success" );
        return returnData;
    }
    
    /**获取投诉类别的值**/
    @RequestMapping(value = "/getComplainType")
    public @ResponseBody
    List<List<Object>> getComplainType() {

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();

        Page<ItsmComplainTypeConf> page = userInfoScope.getPage();

        List<List<Object>> result = new ArrayList<List<Object>>();
        List<ItsmComplainTypeConf> complainTypeList=compTypeConfService.queryComplainTypeConfList(page).getResults();

        for ( ItsmComplainTypeConf type : complainTypeList ) {
            List<Object> row = new ArrayList<Object>();
            row.add( type.getId() );
            row.add( type.getTypename());
            result.add( row );
        }
        return result;
    }

	/** 获取投诉内容的值 **/
	@RequestMapping(value = "/getComplainReamrks")
	@ResponseBody
	public ItsmComplainTypeConf getComplainReamrks(String complainTypeConfId) {

		ItsmComplainTypeConf type = compTypeConfService
				.queryComplainTypeConfById(complainTypeConfId);
		return type;
	}

}
