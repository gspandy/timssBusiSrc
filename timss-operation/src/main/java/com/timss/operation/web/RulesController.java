package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.bean.Rules;
import com.timss.operation.service.RulesService;
import com.timss.operation.util.VOUtil;
import com.yudean.itc.dto.Page;

/**
 * @title: 排班规则controller
 * @description: {desc}
 * @company: gdyd
 * @className: RulesController.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/rules")
public class RulesController {

    @Autowired
    private RulesService rulesService;

    public RulesService getRulesService() {
        return rulesService;
    }

    public void setRulesService(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    /**
     * @description:保存排班规则表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertRules")
    public Map<String, Object> insertRules(String formData) {

        Rules rules = VOUtil.fromJsonToVoUtil( formData, Rules.class );

        rules = rulesService.insertRules( rules );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( rules.getId() > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }

    /**
     * @description:排班规则列表 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllRulesList")
    public Map<String, Object> queryAllRulesList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        List<Rules> rulesList = new ArrayList<Rules>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            rulesList = rulesService.queryRulesBySearch( map, pageVo );
            
        }else{
            //默认分页
            rulesList = rulesService.queryRulesByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", rulesList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新排班规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateRules")
    public Map<String, Object> updateRules(String formData) {

        Rules rules = VOUtil.fromJsonToVoUtil( formData, Rules.class );

       int count = rulesService.updateRules( rules );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除排班规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteRules")
    public Map<String, Object> deleteRules(String formData) {
        
        Rules rules = VOUtil.fromJsonToVoUtil( formData, Rules.class );
        
        int count = 0 ;
        if( rules.getId() > 0 ){
            count = rulesService.deleteRulesById( rules.getId() );
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
     * @description:通过ID查找
     * @author: fengzt
     * @createDate: 2014年6月26日
     * @param rulesId
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryRulesById")
    public @ResponseBody HashMap<String, Object> queryRulesById( int rulesId ){
        Rules rules = rulesService.queryRulesById( rulesId );
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put( "result",  rules );
        
        return map;
        
    }

}
