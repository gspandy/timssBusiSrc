package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Jobs;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.dao.JobsDao;
import com.timss.operation.service.JobsService;
import com.timss.operation.service.PersonJobsService;
import com.timss.operation.vo.ModeListVo;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.security.service.impl.BaseService;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 工种service Implements
 * @description: 
 * @company: gdyd
 * @className: JobsServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("jobsService")
@Transactional(propagation=Propagation.SUPPORTS)
public class JobsServiceImpl implements JobsService {
    private Logger log = Logger.getLogger( JobsServiceImpl.class );
    
    @Autowired
    private JobsDao jobsDao;
    
    @Autowired
    private OrganizationMapper orgMapper;
    
    @Autowired
    private ISecurityMaintenanceManager secManager;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PersonJobsService personJobsService;
    
    /**
     * @description:插入一条工种
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param jobs 其中id为自增，不需要设置
     * @return:Jobs
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> insertJobs(Jobs jobs, HashMap<String, Object> treeMap) {
    	Map<String, Object>  map = new HashMap<String, Object>();
    	
    	jobsDao.insertJobs( jobs );
        
        if( !treeMap.isEmpty() ){
            Set<String> userIdSet = treeMap.keySet();
            List<Object> userIdList = new ArrayList<Object>();
            for( String userId : userIdSet ){
            	Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
            	dutyPersonMap.put("jobsId", jobs.getId());
            	dutyPersonMap.put("userId", userId);
            	userIdList.add(dutyPersonMap);
            }
            int jobsPersonNum=jobsDao.batchInsertJobsPerson( userIdList );
    	}
        map.put("success", jobs);
        return map;
    }

    /**
     * @description:更新工种表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param jobs:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String updateJobs(Jobs jobs, String userDel, String userAdd) {
    	String[] userAddArry = userAdd.split( "," );
    	//用户删除
    	List<Object> userDelList = new ArrayList<Object>();
    	String[] userDelArray = userDel.split( "," );
    	if( StringUtils.isNotBlank( userDel ) && userDelArray != null && userDelArray.length > 0 ){
    		for( String userId : userDelArray ){
    			Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
    			dutyPersonMap.put("jobsId", jobs.getId());
    			dutyPersonMap.put("userId", userId);
    			userDelList.add( dutyPersonMap );
         	}
    		if(userDelList.size()>0){
    			jobsDao.batchDeleteJobsPerson( userDelList );
    		}
    	}
        //用户增加
        List<Object> userAddList = new ArrayList<Object>();
        if( StringUtils.isNotBlank( userAdd ) && userAddArry != null && userAddArry.length > 0 ){
            for( String userId : userAddArry ){
            	Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
            	dutyPersonMap.put("jobsId", jobs.getId());
            	dutyPersonMap.put("userId", userId);
            	userAddList.add( dutyPersonMap );
            }
            jobsDao.batchInsertJobsPerson( userAddList );
        }
    	
        int count = jobsDao.updateJobs( jobs );
        if(count>0){
        	return "success";
        }
        return "fail";
    }

    /**
     * @description:通过Id拿到工种表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:Jobs
     */
    public Jobs queryJobsById(int id) {

        return jobsDao.queryJobsById( id );
    }

    /**
     * @description:通过ID 删除 jobs
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteJobsById(int id) {

        return jobsDao.deleteJobsById( id );
    }

    /**
     * @description:jobs分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Jobs> queryJobsByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );
        
        return jobsDao.queryJobsByPage( page );
    }

    /**
     * @description:工种列表 高级搜索
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Jobs>
     */
    public List<Jobs> queryJobsBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "name", map.get( "name" ) );
        page.setParameter( "stationId", map.get( "stationId" ) );
        page.setParameter( "sortType", map.get( "sortType" ) );
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );

        List<Jobs> jobsList = jobsDao.queryJobsBySearch( page );
        return jobsList;
    }

    /**
     * 
     * @description:通过岗位ID拿到工种
     * @author: fengzt
     * @createDate: 2014年7月10日
     * @param stationId
     * @return:List<Jobs> 
     */
    @Override
    public List<Jobs> queryJobsByStationId(String stationId) {
        return jobsDao.queryJobsByStationId( stationId );
    }

    /**
     * 
     * @description:通过jobId查询工种、岗位信息
     * @author: fengzt
     * @createDate: 2015年10月30日
     * @param jobId
     * @return:ModeListVo
     */
    @Override
    public ModeListVo queryJobDeptByJobId(int jobId) {
        return jobsDao.queryJobDeptByJobId( jobId );
    }

    @Override
	public Map<String, Object> queryOrgsRelatedToUsers(int id) {
	 	List<String> userList = jobsDao.queryOrgsRelatedToUsers( id );
	        
        //用户列表
        List<String> userArr = new ArrayList<String>();
        Map<String, Boolean> userMap = new HashMap<String, Boolean>();
        for( String userId : userList ){
            userArr.add( userId );
            userMap.put( userId, true );
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( !userArr.isEmpty() ){
            String[] arr =  userArr.toArray(new String[]{});
            List<Map> result = orgMapper.selectOrgsRelatedToUsers( arr );
            HashMap<String, Boolean> baseMap = BaseService.getParentOrgs(result, secManager);
            
            map.put("orgArr", JSONObject.fromObject( baseMap ).toString() );
        }else{
        	map.put("orgArr", "{}" );
        }
        
        
        map.put( "userArr", JSONObject.fromObject( userMap ).toString() );
        
        return map;
	}
    
    @Override
	public String queryJobsIdStrByUserId(String userId)throws Exception{
    	String result="";
    	List<Jobs>jobsList=jobsDao.queryJobsByUserId(userId);
    	if(jobsList!=null&&jobsList.size()>0){
    		for (Jobs jobs : jobsList) {
    			result+=jobs.getId()+",";
			}
    	}
    	return result;
    }
    
    @Override
	public List<PersonJobs> queryJobsPersons(int jobsId) {
        return jobsDao.queryJobsPersons(jobsId);
    }
}