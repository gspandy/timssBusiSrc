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

import com.timss.operation.bean.PersonJobs;
import com.timss.operation.dao.PersonJobsDao;
import com.timss.operation.service.PersonJobsService;
import com.timss.operation.vo.PersonDutyVo;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.security.service.impl.BaseService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 人员值别service Implements
 * @description: 
 * @company: gdyd
 * @className: PersonJobsServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("personJobsService")
@Transactional(propagation=Propagation.SUPPORTS)
public class PersonJobsServiceImpl implements PersonJobsService {
    private Logger log = Logger.getLogger( PersonJobsServiceImpl.class );
    
    @Autowired
    private PersonJobsDao personJobsDao;
    
    @Autowired
    private OrganizationMapper orgMapper;
    
    @Autowired
    private ISecurityMaintenanceManager secManager;
    
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:批量插入人员值别
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param personJobs 其中id为自增，不需要设置
     * @param treeMap < userId, userName> 
     * @return:PersonJobs
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertPersonJobs(PersonJobs personJobs, HashMap<String, Object> treeMap) {
        int count = 0;
        
        if( !treeMap.isEmpty() ){
        	List<PersonJobs> perJobsList = new ArrayList<PersonJobs>();
            Set<String> userIdSet = treeMap.keySet();
            for( String userId : userIdSet ){
                PersonJobs vo = new PersonJobs();
                
                vo.setDutyId( personJobs.getDutyId() );
                vo.setJobsId( personJobs.getJobsId() );
                vo.setSiteId( personJobs.getSiteId() );
                
                vo.setStationId( personJobs.getStationId() );
                vo.setUserId( userId );
                vo.setUserName( treeMap.get( userId ).toString() );
                
                perJobsList.add( vo );
            }
            count = personJobsDao.batchInsertPersonJobs( perJobsList );
        }
        
        return count;
    }
    
    /**
     * 
     * @description:当前登录人站点
     * @author: fengzt
     * @createDate: 2015年1月20日
     * @return:String
     */
    private String getSiteId( ){
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        return siteId;
    }

    /**
     * 
     * @description:更新人员值别
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param personDutyVo
     * @param userAdd 
     * @param userDel 
     * @return:int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updatePersonJobs(PersonDutyVo personDutyVo, String userDel, String userAdd){
        //用userId in ( '890128','890101')方式删除
        String[] userDelArray = userDel.split( "," );
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put( "personJobs", personDutyVo );
        paramsMap.put( "userDelArray", userDelArray );
        
        int delCount = personJobsDao.deletePersonJobsByUserId( paramsMap );
        
        //构建增加的用户
        List<PersonJobs> personjobsList = new ArrayList<PersonJobs>();
        String[] userAddArry = userAdd.split( "," );
        if( StringUtils.isNotBlank( userAdd ) && userAddArry != null && userAddArry.length > 0 ){
            for( String userId : userAddArry ){
                PersonJobs vo = new PersonJobs();
                vo.setDutyId( personDutyVo.getDutyId() );
                vo.setJobsId( personDutyVo.getJobsId() );
                
                vo.setStationId( personDutyVo.getStationId() );
                vo.setUserId( userId );
                vo.setUserName( "" );
                vo.setSiteId( getSiteId() );
                personjobsList.add( vo );
            }
        }
        
        int addCount = 0;
        if( !personjobsList.isEmpty() ){
            addCount = personJobsDao.batchInsertPersonJobs( personjobsList );
        }
        
        //有通过一系列操作，用户没有改变 =0
        if( delCount + addCount >= 0 ){
            return 1;
        }
        return 0;
    }

    /**
     * @description:通过Id拿到人员值别表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:PersonJobs
     */
    public PersonJobs queryPersonJobsById(int id) {

        return personJobsDao.queryPersonJobsById( id );
    }

    /**
     * @description:通过ID 删除 personJobs
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param personDutyVo:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deletePersonJobs(PersonDutyVo personDutyVo) {

        return personJobsDao.deletePersonJobs( personDutyVo );
    }

    /**
     * @description:personJobs分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<PersonDutyVo> queryPersonJobsByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "stationName" );
        page.setSortOrder( "asc" );
        
        return personJobsDao.queryPersonJobsByPage( page );
    }

    /**
     * @description:人员值别列表 高级搜索
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<PersonJobs>
     * 
     */
    public List<PersonDutyVo> queryPersonJobsBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "jobsName", map.get( "jobsName" ) );
        page.setParameter( "dutyName", map.get( "dutyName" ) );
        page.setParameter( "stationName", map.get( "stationName" ) );
        page.setSortKey( "stationName" );
        page.setSortOrder( "asc" );

        List<PersonDutyVo> personJobsList = personJobsDao.queryPersonJobsBySearch( page );
        return personJobsList;
    }

    /**
     * 
     * @description:查找详细通过 jobsId dutyId stationId
     * @author: fengzt
     * @createDate: 2014年7月14日
     * @param vo
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryOrgsRelatedToUsers(PersonDutyVo vo) {
        List<PersonJobs> personJobsList = personJobsDao.queryAllPersonJobsByPersonDutyVo( vo );
        
        //用户列表
        List<String> userArr = new ArrayList<String>();
        Map<String, Boolean> userMap = new HashMap<String, Boolean>();
        for( PersonJobs tempVo : personJobsList ){
            String userId = tempVo.getUserId();
            userArr.add( userId );
            userMap.put( userId, true );
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        //String[] arr = new String[]{"890157","890128","113489"};
        
        if( !userArr.isEmpty() ){
            String[] arr =  userArr.toArray(new String[]{});
            List<Map> result = orgMapper.selectOrgsRelatedToUsers( arr );
            HashMap<String, Boolean> baseMap = BaseService.getParentOrgs(result, secManager);
            
            map.put("orgArr", JSONObject.fromObject( baseMap ).toString() );
        }
        
        map.put( "userArr", JSONObject.fromObject( userMap ).toString() );
        
        return map;
    }

    /**
     * 
     * @description: 通过userId找personJobs
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param String userId
     * @return:List<PersonJobs>
     */
    public List<PersonJobs> queryPersonJobsByUserId(String userId) {
        
        return personJobsDao.queryPersonJobsByUserId( userId );
    }

    /**
     * 
     * @description: 通过dutyId找personJobs
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param  int dutyId
     * @return:List<PersonJobs>
     */
    public List<PersonJobs> queryPersonJobsByDutyId(int dutyId) {
        
        return personJobsDao.queryPersonJobsByDutyId(dutyId);
    }

    /**
     * 
     * @description:查询某个岗位下的所有人员
     * @author: huanglw
     * @createDate: 2014年7月31日
     * @param stationId
     * @return:List<PersonJobs>
     */
    public List<PersonJobs> queryPersonJobsByStationId(String stationId) {
         return personJobsDao.queryPersonJobsByStationId(stationId);
    }

}