package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Duty;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.dao.DutyDao;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.timss.operation.vo.RoleVo;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.security.service.impl.BaseService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 值别service
 * @description: {desc}
 * @company: gdyd
 * @className: DutyServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("dutyService")
public class DutyServiceImpl implements DutyService {
	
	private Logger log = LoggerFactory.getLogger( DutyServiceImpl.class );
	
    @Autowired
    private DutyDao dutyDao;
    
    @Autowired
    private OrganizationMapper orgMapper;

    @Autowired
    private ISecurityMaintenanceManager secManager;
    
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
    /**
     * @description:查询结果中返回主键id的方法
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param duty 其中id为自增，不需要设置
     * @return:Duty
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> insertDuty(Duty duty, HashMap<String, Object> treeMap) throws Exception {
    	Map<String, Object>  map = new HashMap<String, Object>();
    	//判断选的用户是否已经在其他值别
    	boolean hasDuty = false;
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String siteId = userInfoScope.getSiteId();
    	String insertInfo = "";
    	if( !treeMap.isEmpty() ){
            Set<String> userIdSet = treeMap.keySet();
            for( String userId : userIdSet ){
            	String dutyName = dutyDao.hasDutyPerson(userId, siteId);
            	if(StringUtils.isNotEmpty(dutyName)){
            		hasDuty = true;
            		String userName = secManager.retrieveUserById(userId).getName();
            		insertInfo = userId+","+userName+","+dutyName;
            		break;
            	}
            }
    	}
    	
    	if(hasDuty){
    		map.put("fail", insertInfo);
    		return map;
    	}
    	
        duty.setState( "Y" );
        dutyDao.insertDuty( duty );
        
        if( !treeMap.isEmpty() ){
            Set<String> userIdSet = treeMap.keySet();
            List<Object> userIdList = new ArrayList<Object>();
            for( String userId : userIdSet ){
            	Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
            	dutyPersonMap.put("dutyId", duty.getId());
            	dutyPersonMap.put("userId", userId);
            	userIdList.add(dutyPersonMap);
            }
            dutyDao.batchInsertDutyPerson( userIdList );
            //给该值别的明天开始的排班添加值班人员
            //新建值别的时候，该值别无排班，所以不需要
            /*Date today=DateFormatUtil.getCurrentDate();
            Date tomorrow=DateFormatUtil.addDate(today, "d", 1);
            scheduleDetailService.insertSchedulePersonFromScheduleDutyAndUserIds(null, tomorrow, null, 
            		new Integer[]{duty.getId()}, null, null, null, userIdSet.toArray(new String[userIdSet.size()]));*/
    	}
        map.put("success", duty);
        return map;
    }

    /**
     * @description:更新值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Duty:
     * @return String
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String updateDuty(Duty duty, String userDel, String userAdd) throws Exception {
    	//判断选的用户是否已经在其他值别
    	boolean hasDuty = false;
        String[] userAddArry = userAdd.split( "," );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String siteId = userInfoScope.getSiteId();
    	String updateInfo = "";
        if( StringUtils.isNotBlank( userAdd ) && userAddArry != null && userAddArry.length > 0 ){
            for( String userId : userAddArry ){
            	String dutyName = dutyDao.hasDutyPerson(userId, siteId);
            	if(StringUtils.isNotEmpty(dutyName)){
            		hasDuty = true;
            		String userName = secManager.retrieveUserById(userId).getName();
            		updateInfo = userId+","+userName+","+dutyName;
            		break;
            	}
            }
        }
        
        if(hasDuty){
        	return updateInfo;
        }
        
        Date today=DateFormatUtil.getCurrentDate();
        Date tomorrow=DateFormatUtil.addDate(today, "d", 1);
    	//用户删除
    	List<Object> userDelList = new ArrayList<Object>();
    	String[] userDelArray = userDel.split( "," );
    	if( StringUtils.isNotBlank( userDel ) && userDelArray != null && userDelArray.length > 0 ){
    		for( String userId : userDelArray ){
    			Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
    			dutyPersonMap.put("dutyId", duty.getId());
    			dutyPersonMap.put("userId", userId);
    			userDelList.add( dutyPersonMap );
         	}
    		dutyDao.batchDeleteDutyPerson( userDelList );
    		//给该值别的明天开始的排班删除值班人员
            scheduleDetailService.deleteSchedulePersonFromScheduleDutyAndUserIds(null, tomorrow, null, 
            		new Integer[]{duty.getId()}, null, null, null, userDelArray);
    	}
        //用户增加
        List<Object> userAddList = new ArrayList<Object>();
        if( StringUtils.isNotBlank( userAdd ) && userAddArry != null && userAddArry.length > 0 ){
            for( String userId : userAddArry ){
            	Map<String, Object> dutyPersonMap = new HashMap<String, Object>();
            	dutyPersonMap.put("dutyId", duty.getId());
            	dutyPersonMap.put("userId", userId);
            	userAddList.add( dutyPersonMap );
            }
            dutyDao.batchInsertDutyPerson( userAddList );
            //给该值别的明天开始的该值别的排班添加值班人员
            //先删除要添加的人明天开始的所有排班
            scheduleDetailService.deleteSchedulePersonFromScheduleDutyAndUserIds(null, tomorrow, null, 
            		null, null, null, null, userAddArry);
            scheduleDetailService.insertSchedulePersonFromScheduleDutyAndUserIds(null, tomorrow, null, 
            		new Integer[]{duty.getId()}, null, null, null, userAddArry);
        }
    	
        int count = dutyDao.updateDuty( duty );
        if(count>0){
        	return "success";
        }
        return "fail";
        
    }

    /**
     * @description:通过Id拿到值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id
     * @return:Duty
     */
    public Duty queryDutyById(int id) {

        return dutyDao.queryDutyById( id );
    }

    /**
     * @description:通过ID 删除 duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteDutyById(int id) {

        return dutyDao.deleteDutyById( id );
    }

    /**
     * @description:更新duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param DutyMap:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDutyByMap(HashMap<?, ?> DutyMap) {

    }

    /**
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param list:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchInsertDuty(List<Duty> list) {

    }

    /**
     * @description:duty 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return:
     */
    public List<Duty> queryDutyByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "stationId,sortType" );
        page.setSortOrder( "asc" );
        
        return dutyDao.queryDutyByPage( page );
    }

    /**
     * @description:duty 分页 (返回hashmap)
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryDutyMapByPage(Page<HashMap<?, ?>> page) {
        return null;
    }

    /**
     * @description:拿出所有值别duty
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @return:
     */
    public List<Duty> queryAllDuty() {
        return dutyDao.queryAllDuty();
    }

    /**
     * @description:通过hashmap 拿到List
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @return:List<Duty>
     */
    public List<Duty> queryDutyByMap(HashMap<?, ?> map) {
        return null;
    }

    /**
     * @description:值别列表 高级搜索
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param duty 查询form vo
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:List<Duty>
     */
    public List<Duty> queryDutyBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "id", map.get( "id" ) );
        page.setParameter( "deptId", map.get( "deptId" ) );
        page.setParameter( "stationId", map.get( "stationId" ) );
        page.setParameter( "name", map.get( "name" ) );

        page.setParameter( "num", map.get( "num" ) );
        page.setParameter( "sortType", map.get( "sortType" ) );
        page.setParameter( "state", map.get( "state" ) );
        
        page.setSortKey( "stationId,sortType" );
        page.setSortOrder( "asc" );

        List<Duty> dutyList = dutyDao.queryDutyBySearch( page );
        return dutyList;
    }

    /**
     * 
     * @description:通过岗位ID查找值别
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param stationId
     * @return:List<Duty>
     */
    public List<Duty> queryDutyByStationId(String stationId) {
            
        return dutyDao.queryDutyByStationId( stationId );
    }

    /**
     * 
     * @description:通过当站点取岗位信息
     * @author: fengzt
     * @createDate: 2014年6月24日
     * @param siteId
     * @return: List<RoleVo>
     */
    @Override
    public List<RoleVo> queryStationInfoBySitId(String siteId) {
        //加上"_opr" 代表 运行管理这个模块的,所以添加角色的时候要遵循规范
        String roleId = "%" + siteId.toUpperCase() + "_opr%";
        return dutyDao.queryStationInfoBySitId( roleId );
    }

    /**
     * 
     * @description:通过UUID查找dutyList
     * @author: fengzt
     * @createDate: 2014年7月28日
     * @param uuid
     * @return:List<Duty>
     */
    @Override
    public List<Duty> queryDutyByUuid(String uuid) {
        return dutyDao.queryDutyByUuid(uuid);
    }

    /**
     * 
     * @description:通过岗位拿到sortType
     * @author: fengzt
     * @createDate: 2014年7月29日
     * @param stationId
     * @return:int
     */
    @Override
    public int querySortTypeByStationId(String stationId) {
        String sortType = dutyDao.querySortTypeByStationId( stationId );
        
        if( StringUtils.isBlank( sortType ) ){
            return 100;
        }else{
            return Integer.parseInt( sortType ) + 100;
        }
    }

    @Override
    public Map<String, DutyPersonShiftVo>queryDutyPersonAndShiftBySiteAndTime(String siteId,String userId,String startDateStr,String endDateStr)throws Exception{
    	Map<String, DutyPersonShiftVo>result=dutyDao.queryDutyPersonAndShiftBySiteAndTime(siteId,userId,startDateStr,endDateStr);
    	log.info("dutyService.queryDutyPersonAndShiftBySiteAndTime---->"+siteId+" userId:"+userId+" "+startDateStr+" "+endDateStr+" size:"+result.size());
		return result;
    }
    
    @Override
    public String queryOprPersonsBySite(String siteId)throws Exception{
    	String result="";
    	List<String>oprPersonList=dutyDao.queryOprPersonIdBySite(siteId);
    	if(oprPersonList!=null&&oprPersonList.size()>0){
    		for (String userId : oprPersonList) {
    			result+=userId+",";
			}
    	}
    	return result;
    }

	@Override
	public Map<String, Object> queryOrgsRelatedToUsers(int id) {
		 List<String> userList = dutyDao.queryOrgsRelatedToUsers( id );
	        
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
	public List<PersonJobs> queryDutyPersons(Integer dutyId,String siteId,String userId,String userKw)throws Exception{
        return dutyDao.queryDutyPersons(dutyId,siteId,userId,userKw);
    }

	@Override
	public List<Map<String, Object>> queryDutyPersonsForHint(Integer dutyId,
			String siteId, String userKw) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<PersonJobs>dutyPersons=queryDutyPersons(dutyId,siteId,null,userKw);
        for (PersonJobs personJobs : dutyPersons) {
        	Map<String, Object>map=new HashMap<String, Object>();
        	map.put("id", personJobs.getUserId());
        	map.put("name", personJobs.getUserName());
        	result.add(map);
		}
        return result;
	}
}
