package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.transport.http.HTTPSession;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.service.PersonJobsService;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.PersonDutyVo;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 人员值别controller
 * @description:
 * @company: gdyd
 * @className: PersonJobsController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/personJobs")
public class PersonJobsController {
    private Logger log = Logger.getLogger( PersonJobsController.class );
    
    @Autowired
    private PersonJobsService personJobsService;

    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private OrganizationMapper orgMapper;
    
    @Autowired
    private ISecurityMaintenanceManager secManager;
    

    /**
     * @description:新建人员值别
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/insertPersonJobs", method=RequestMethod.POST)
    public Map<String, Object> insertPersonJobs(String formData, String treeData ) {
        PersonJobs personJobs = VOUtil.fromJsonToVoUtil( formData, PersonJobs.class );
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        personJobs.setSiteId( siteId );
        
        HashMap<String, Object> treeMap = treeData!=null&&!"".equals(treeData)?VOUtil.fromJsonToHashMap( treeData ):new HashMap<String, Object>();
        int count = personJobsService.insertPersonJobs( personJobs, treeMap );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }

    /**
     * @description:人员值别列表 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllPersonJobsList")
    public Map<String, Object> queryAllPersonJobsList(int rows, int page, String search ) {
        
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
        
        List<PersonDutyVo> personJobsList = new ArrayList<PersonDutyVo>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            personJobsList = personJobsService.queryPersonJobsBySearch( map, pageVo );
            
        }else{
            //默认分页
            personJobsList = personJobsService.queryPersonJobsByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", personJobsList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新人员值别
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updatePersonJobs") 
    public Map<String, Object> updatePersonJobs(String formData, String userDel, String userAdd ) {

       PersonDutyVo personDutyVo = VOUtil.fromJsonToVoUtil( formData, PersonDutyVo.class );

        
       int count = personJobsService.updatePersonJobs( personDutyVo, userDel, userAdd );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除人员值别
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deletePersonJobs")
    public Map<String, Object> deletePersonJobs(String formData) {
        
        PersonDutyVo personDutyVo = VOUtil.fromJsonToVoUtil( formData, PersonDutyVo.class );
        
        int count = personJobsService.deletePersonJobs( personDutyVo );
        
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
     * @description:通过ID查找personJobs
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param id
     * @return:map
     */
    @RequestMapping("/queryPersonJobsById")
    public @ResponseBody Map<String, Object> queryPersonJobsById( int id ){
        PersonJobs personJobsVo = personJobsService.queryPersonJobsById( id );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", personJobsVo );
        return map;
    }
    
    /**
     * 
     * @description:列表双击
     * @author: fengzt
     * @createDate: 2014年7月14日
     * @param formData
     * @return:
     */
    @RequestMapping("/prepareToQueryOrgsRelatedToUsers")
    public Map<String, Object> prepareToQueryOrgsRelatedToUsers(HttpServletRequest rq, String formData ){
        PersonDutyVo vo = VOUtil.fromJsonToVoUtil( formData, PersonDutyVo.class );
        rq.getSession().setAttribute("operationRowData", vo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", "success" );
        return map;
    }
    /**
     * 
     * @description:列表双击
     * @author: fengzt
     * @createDate: 2014年7月14日
     * @param formData
     * @return:
     */
    @RequestMapping("/queryOrgsRelatedToUsers")
    public ModelAndView queryOrgsRelatedToUsers(HttpServletRequest rq ){
        PersonDutyVo vo = (PersonDutyVo)rq.getSession().getAttribute("operationRowData");
        Map<String, Object> dataMap = personJobsService.queryOrgsRelatedToUsers( vo );
        
        dataMap.put( "personDutyVo", JSONObject.fromObject( vo ).toString() );
        return new ModelAndView( "operationlog/PersonJobs-updatePersonJobs.jsp", dataMap );
        
    }
}
