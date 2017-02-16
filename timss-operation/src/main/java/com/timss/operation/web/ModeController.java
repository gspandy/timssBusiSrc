package com.timss.operation.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.ModeBean;
import com.timss.operation.service.JobsService;
import com.timss.operation.service.ModeService;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.ModeAssetVo;
import com.timss.operation.vo.ModeListVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行方式设置controller
 * @description:
 * @company: gdyd
 * @className: ModeController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/mode")
public class ModeController {
    private Logger log = Logger.getLogger( ModeController.class );

    @Autowired
    private ModeService modeService;

    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private JobsService jobsService;
    
    
    /**
     * 
     * @description:跳转到新增or更新运行方式设置页面
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @return:
     */
    @RequestMapping("/insertOrUpdateModePage")
    @ReturnEnumsBind("AST_ASSET_TYPE,AST_ASSET_SPEC")
    public ModelAndView insertOrUpdateModePage( int jobId ){
        Map<String, Object> map = new HashMap<String, Object>();
        //通过JobId查询工种、岗位信息
        ModeListVo jobDeptVo = jobsService.queryJobDeptByJobId( jobId );
        map.put( "jobDeptVo", JsonHelper.fromBeanToJsonString( jobDeptVo ) );
        
        return new ModelAndView("schedule/Mode-insertMode.jsp", map);
    }
    

    /**
     * @description:运行方式设置列表 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryAllModeList")
    public Page<ModeBean> queryAllModeList(int rows, int page, String search, String sort, String order)
            throws JsonParseException, JsonMappingException {
        
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ModeBean> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );

        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        if ( StringUtils.isNotBlank( search ) ) {
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
            paramsPage.setFuzzyParams( fuzzyParams );
        }

        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
            paramsPage.setSortKey( sort );
            paramsPage.setSortOrder( order );
        } else {
            // 设置默认的排序字段
            paramsPage.setSortKey( "deptName, jobName" );
            paramsPage.setSortOrder( "ASC" );
        }

        paramsPage = modeService.queryAllModeList( paramsPage );
        return paramsPage;
    }
    
    /**
     * @description:运行方式设置列表 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryModeListByJobId")
    public Map<String, Object> queryModeListByJobId( int jobId, String team ) {
        log.debug( "queryModeListByJobId params jobId=" + jobId + "-- team = " + team );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //通过jobId查找资产项
        List<ModeAssetVo> modeAssetVos = modeService.queryModeAssetByJobId( jobId, team );
        
        dataMap.put( "rows", modeAssetVos );
        if( modeAssetVos != null && modeAssetVos.size() > 0 ){
            dataMap.put( "total",  modeAssetVos.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }
    
    /**
     * @description:更新运行方式设置
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateMode") 
    public Map<String, Object> updateMode(String formData) {

        ModeBean mode = VOUtil.fromJsonToVoUtil( formData, ModeBean.class );

       int count = modeService.updateMode( mode );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 保存运行方式的值
     * @param formData
     * @return
     */
    @RequestMapping("/updateNowModeVal") 
    public Map<String, Object> updateNowModeVal(String assetId,String team,String val,Integer jobId) {
       int count = modeService.updateNowModeVal( jobId,team,assetId,val );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * @description:删除运行方式设置
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteMode")
    public Map<String, Object> deleteMode(String formData) {
        
        ModeBean mode = VOUtil.fromJsonToVoUtil( formData, ModeBean.class );
        
        int count = 0 ;
        if(StringUtils.isNotBlank( mode.getModeId() ) ){
            count = modeService.deleteModeById( mode.getModeId() );
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
        
    }
    
    /**
     * 
     * @description:通过运行方式设置ID查找mode
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param modeId
     * @return:map
     */
    @RequestMapping("/queryModeByModeId")
    public @ResponseBody Map<String, Object> queryModeByModeId( String modeId ){
        ModeBean modeVo = modeService.queryModeById( modeId );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", modeVo );
        return map;
    }
    
    /**
     * 
     * @description:通过assetId 查找资产树信息
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param modeId
     * @return:map
     */
    @RequestMapping("/queryModeAssetByAssetId")
    public @ResponseBody Map<String, Object> queryModeAssetByAssetId( String assetId ){
        ModeAssetVo modeAssetVo = modeService.queryModeAssetByAssetId( assetId );
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( StringUtils.isNotBlank( modeAssetVo.getAssetId() )){
            map.put( "result", "success" );
            map.put( "modeAssetVo", JsonHelper.fromBeanToJsonString( modeAssetVo ) );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:插入资产设备
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param modeId
     * @return:map
     */
    @RequestMapping("/insertOrUpdateMode")
    public Map<String, Object> insertOrUpdateMode( String rowData,Integer jobId ){
        List<ModeAssetVo> modeAssetVos = VOUtil.fromJsonToListObject( rowData, ModeAssetVo.class );
        int count = modeService.insertOrUpdateMode( modeAssetVos,jobId );
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( count > 0 ){
            map.put( "result", "success" );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:通过工种查找Mode分组信息
     * @author: fengzt
     * @createDate: 2015年11月11日
     * @param jobsId
     * @return:List<String>
     */
    @RequestMapping("/queryModeTeamByJobsId")
    public @ResponseBody Map<String, Object> queryModeTeamByJobsId( int jobsId ){
        List<String> teams = modeService.queryModeTeamByJobsId( jobsId );
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( teams != null && teams.size() > 0 ){
            map.put( "result", "success" );
            map.put( "teams", VOUtil.fromVoToJsonUtil( teams ) );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    
}
