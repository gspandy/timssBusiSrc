package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.service.RulesDetailService;
import com.timss.operation.service.RulesService;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.RulesDetailForListVo;
import com.timss.operation.vo.RulesFormVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 排版规则详情
 * @description: {desc}
 * @company: gdyd
 * @className: RulesDetailController.java
 * @author: fengzt
 * @createDate: 2014年6月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/rulesDetail")
public class RulesDetailController {
    private Logger log = Logger.getLogger( RulesDetailController.class );
    
    @Autowired
    private RulesDetailService rulesDetailService;
    
    @Autowired
    private RulesService rulesService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param row
     * @param rulesId
     * @param deptId
     * @return:String
     */
    @RequestMapping(value="/batchInsertRulesDetail",method=RequestMethod.POST)
    public Map<String, Object> batchInsertRulesDetail( String row, String formData ){
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        
        List<HashMap<String, Object>> maps = VOUtil.fromJsonToHashMapList(row);
        RulesFormVo rulesFormVo = VOUtil.fromJsonToVoUtil( formData, RulesFormVo.class );
        rulesFormVo.setNum( "" );
        rulesFormVo.setSiteId( siteId );
        
        rulesFormVo = rulesService.insertRulesByFormVo( rulesFormVo );
        
        Map<String,Object>result = rulesDetailService.batchInsertRulesDetail( maps, rulesFormVo.getId(), rulesFormVo.getStationId(), rulesFormVo.getName() );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( Integer.parseInt(result.get("count").toString()) > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param row
     * @param rulesId
     * @param deptId
     * @return:String
     */
   @RequestMapping(value = "/updateRulesDetail",method=RequestMethod.POST)
    public Map<String, Object> updateRulesDetail( String row, String formData, String uuid ){
        
	   List<HashMap<String, Object>> maps = VOUtil.fromJsonToHashMapList(row);
        RulesFormVo rulesFormVo = VOUtil.fromJsonToVoUtil( formData, RulesFormVo.class );
        
        
        Map<String,Object>result = rulesDetailService.updateRuleDetail(rulesFormVo, maps, uuid );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( Integer.parseInt(result.get("count").toString()) > 0 ) {
            map.put( "result", "success" );
            map.put("uuid", result.get("uuid"));
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:拿到所有的rulesDetail列表
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param rows 每页条数
     * @param page 第几页
     * @param search 高级查询
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllRulesDetailList")
    public Map<String, Object> queryAllRulesDetailList( int rows, int page, String search ){
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        pageVo.setParameter( "siteId", siteId );
        
        List<RulesDetailForListVo> rulesDetailForListVos = new ArrayList<RulesDetailForListVo>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            rulesDetailForListVos = rulesDetailService.queryRulesDetailBySearch( search, pageVo );
            
        }else{
            //默认分页
            rulesDetailForListVos = rulesDetailService.queryAllRulesDetailByPage( pageVo );
        }

        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( rulesDetailForListVos.isEmpty() ){
            dataMap.put( "rows", "" );
            dataMap.put( "total", 0 );
        }else{
            dataMap.put( "rows", rulesDetailForListVos );
            dataMap.put( "total", pageVo.getTotalRecord() );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:通过uuid查找
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:
     */
    @RequestMapping("/queryRulesDetailByUuid")
    public Map<String, Object> queryRulesDetailByUuid( String uuid ){
        List<HashMap<String, Object>> result = rulesDetailService.queryRulesDetailByUuid( uuid );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", result );
        dataMap.put( "total", result.size() );
        return dataMap;
    }
    
    /**
     * 
     * @description:通过uuid查找staionId
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:
     */
    @RequestMapping("/queryStationIdByUuid")
    public @ResponseBody Map<String, Object> queryStationIdByUuid( String uuid ){
        //SELECT UUID,STATIONID FROM OPR_RULES_DETAIL WHERE UUID = 'b8d6cf49-288c-4ca4-ab9d-1da3ef64444a' 
        String stationId = rulesDetailService.queryStationIdByUuid( uuid );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "stationId", stationId );
        return dataMap;
    }
    
    /**
     * 
     * @description:删除排班规则详情和行列表
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param id
     * @param uuid:
     */
    @RequestMapping("/deleteRulesDetailByUuid")
    public @ResponseBody Map<String, Object> deleteRulesDetailByUuid( int id, String uuid ){
        boolean flag = rulesDetailService.deleteRulesDetailByUuid( id, uuid );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( flag ){
            dataMap.put( "result", "success" );
        }else{
            dataMap.put( "result", "fail" );
        }
        return dataMap;
    }
    
    
}
