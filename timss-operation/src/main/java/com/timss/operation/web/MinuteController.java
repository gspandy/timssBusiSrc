package com.timss.operation.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.MeetingMinute;
import com.yudean.itc.manager.support.IMeetingMinuteManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 生产碰头会
 * @description: {desc}
 * @company: gdyd
 * @className: MinuteController.java
 * @author: zhuw
 * @createDate: 2016年1月19日
 * @updateUser: zhuw
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/minute")
public class MinuteController {
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private IMeetingMinuteManager minuteMapper;
    
    /**
     * 
     * @description:生产碰头会表单页面跳转
     * @author: zhuw
     * @createDate: 2016年1月19日
     * @param minuteId
     * @return:
     */
    @RequestMapping("/minuteForm")
    public String minuteForm( String type, String id ){
    	return "/minute/meetingMinute.jsp?type=" + type + "&id=" + id;
    }
    
    /**
     * 
     * @description:生产碰头会列表数据
     * @author: zhuw
     * @createDate: 2016年1月19日
     * @return:Page
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping("/minuteList")
    public Page<MeetingMinute> queryMinuteList(String search,String sort,String order) throws JsonParseException, JsonMappingException{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<MeetingMinute> paramsPage = userInfoScope.getPage();
        paramsPage.setParameter( "siteId", userInfoScope.getSiteId() );

        // 获取表头搜索的参数，Dao的xml文件里面不用写 if is null这些方法了
        if ( StringUtils.isNotBlank( search ) ) {
            Map<String, Object> fuzzyParams = new HashMap<String, Object>();
            fuzzyParams.put("minute", search);
            paramsPage.setFuzzyParams( fuzzyParams );
        }
        
        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
        	if("issueDate".equals(sort)){
        		sort = "ISSUE_DATE";
        	}
        	paramsPage.setSortKey( sort );
        	paramsPage.setSortOrder( order );
        } else {
            // 设置默认的排序字段
        	paramsPage.setSortKey( "ISSUE_DATE" );
        	paramsPage.setSortOrder( "DESC" );
        }
        
        Page<MeetingMinute> minutePage = minuteMapper.retrieveMinute(paramsPage);

        
        
        return minutePage;
    }
    
    /**
     * 
     * @description:根据id查询生产碰头会详情
     * @author: zhuw
     * @createDate: 2016年1月19日
     * @return:MeetingMinuteVO
     * @throws Exception
     */
    @RequestMapping(value = "/queryMinuteById", method = RequestMethod.POST)
    public MeetingMinute queryMinuteById(String id) throws Exception {
        return minuteMapper.retrieveById(id);
    }
    
    /**
     * @description:根据id删除生产碰头会
     * @author: zhuw
     * @createDate: 2016年1月19日
     * @return:Map
     * @throws
     */
    @RequestMapping(value = "/deleteMinute", method = RequestMethod.POST)
    public Map<String, Object> deleteMinute(String id) {
      int count = minuteMapper.removeMinute(id);
      Map<String, Object> map = new HashMap<String, Object>();
      if (count > 0) {
        map.put("result", "success");
      } else {
        map.put("result", "false");
      }
      return map;
    }
    
    /**
     * @description:新增或修改生产碰头会信息
     * @author: zhuw
     * @createDate: 2016年1月19日
     * @return:Map
     * @throws
     */
    @RequestMapping(value = "/saveOrupdateMinute", method = RequestMethod.POST)
    public Map<String, Object> saveOrupdateMinute(String minute) {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	MeetingMinute meetingMinute = JsonHelper.fromJsonStringToBean(minute, MeetingMinute.class);
    	meetingMinute.setSiteId(userInfoScope.getSiteId());
  	    int count = 0;
  	    if (StringUtils.isEmpty(meetingMinute.getId())) {
  	    	meetingMinute.setCreateUser(userInfoScope.getUserId());
  	    	meetingMinute.setCreateDate(new Date());
  	    	count = minuteMapper.createMinute(meetingMinute);
  	    }else{
  	    	meetingMinute.setModifyUser(userInfoScope.getUserId());
  	    	meetingMinute.setModifyDate(new Date());
  	    	count = minuteMapper.editMinute(meetingMinute);
  	    }
  	    Map<String, Object> map = new HashMap<String, Object>();
  	    if (count > 0) {
  	      map.put("result", "success");
  	    } else {
  	      map.put("result", "false");
  	    }
  	    return map;
    }
    
    
}
