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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.Duty;
import com.timss.operation.bean.Jobs;
import com.timss.operation.service.JobsService;
import com.timss.operation.util.VOUtil;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 工种controller
 * @description:
 * @company: gdyd
 * @className: JobsController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/jobs")
public class JobsController {
    private Logger log = Logger.getLogger( JobsController.class );
    
    @Autowired
    private JobsService jobsService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:新建一条工种
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertJobs")
    public Map<String, Object> insertJobs(String formData, String treeData) {
        
        Jobs jobs = VOUtil.fromJsonToVoUtil( formData, Jobs.class );
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        jobs.setSiteId( siteId );
        
        HashMap<String, Object> treeMap = VOUtil.fromJsonToHashMap( treeData==null||"".equals(treeData)?"{}":treeData );
        
        Map<String, Object>  insertMap = jobsService.insertJobs( jobs, treeMap );

        Map<String, Object> map = new HashMap<String, Object>();
        if(insertMap.containsKey("success")){
        	jobs = (Jobs) insertMap.get("success");
        	 if ( jobs.getId() > 0 ) {
                 map.put( "result", "success" );
             } else {
                 map.put( "result", "fail" );
             }
        }else if(insertMap.containsKey("fail")){
        	map.put("result", insertMap.get("fail"));
        }else{
            map.put( "result", "fail" );
        }

        return map;

    }

    /**
     * @description:工种列表 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllJobsList")
    public Map<String, Object> queryAllJobsList(int rows, int page, String search ) {
        
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
        
        List<Jobs> jobsList = new ArrayList<Jobs>();
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            jobsList = jobsService.queryJobsBySearch( map, pageVo );
            
        }else{
            //默认分页
            jobsList = jobsService.queryJobsByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", jobsList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新工种
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateJobs") 
    public Map<String, Object> updateJobs(String formData, String userDel, String userAdd) {

        Jobs jobs = VOUtil.fromJsonToVoUtil( formData, Jobs.class );

        String updateInfo = jobsService.updateJobs( jobs, userDel, userAdd );

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", updateInfo);
        return map;

    }
    
    /**
     * @description:删除工种
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteJobs")
    public Map<String, Object> deleteJobs(String formData) {
        
        Jobs jobs = VOUtil.fromJsonToVoUtil( formData, Jobs.class );
        
        int count = 0 ;
        if( jobs.getId() > 0 ){
            count = jobsService.deleteJobsById( jobs.getId() );
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
     * @description:通过工种ID查找jobs
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param id
     * @return:map
     */
    @RequestMapping("/queryJobsById")
    public @ResponseBody Map<String, Object> queryJobsById( int id ){
        Jobs jobsVo = jobsService.queryJobsById( id );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "result", jobsVo );
        return map;
    }
    
    /**
     * @description:通过岗位ID获取工种
     * @author: fengzhutai
     * @createDate: 2014年6月18日
     * @param stationId
     * @return:
     */
    @RequestMapping("/queryJobsByStationId")
    public Map<String, Object> queryJobsByStationId(String stationId){
       
        List<Jobs> jobsList = new ArrayList<Jobs>();
        jobsList = jobsService.queryJobsByStationId( stationId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", jobsList );
        if( !jobsList.isEmpty() ){
            dataMap.put( "total", jobsList.size());
        }else{
            dataMap.put( "total", 0);
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:列表双击跳转修改页面查询关系用户
     * @author: yyn
     * @createDate: 2016年2月18日
     * @param formData
     * @return:
     */
    @RequestMapping("/updateJobsForm")
    public ModelAndView updateJobsForm(int id){
        Map<String, Object> dataMap = jobsService.queryOrgsRelatedToUsers( id );
        return new ModelAndView( "operationlog/Jobs-updateJobs.jsp", dataMap );
    }
}
