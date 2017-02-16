package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.ExemptBean;
import com.timss.attendance.dao.ExemptDao;
import com.timss.attendance.service.ExemptService;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.security.service.impl.BaseService;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 免考勤和免打卡service
 * @description: {desc}
 * @company: gdyd
 * @className: ExemptServiceImpl.java
 * @author: zhuw
 * @createDate: 2016年1月22日
 * @updateUser: zhuw
 * @version: 2.0
 */
@Service("exemptService")
public class ExemptServiceImpl implements ExemptService {
	
	@Autowired
    private ItcMvcService itcMvcService;
	
	@Autowired
    private ExemptDao extemptDao;
	
	@Autowired
    private OrganizationMapper orgMapper;
	
	@Autowired
    private ISecurityMaintenanceManager secManager;
	 
	/**
     * 
     * @description:查询关系用户或部门id
     * @author: zhuw
     * @createDate: 2016年1月22日
     * @param id
     * @return:Map
     */
	@Override
	public Map<String, Object> queryRelatedToUsersOrOrgs(String siteId, String menuId) {
		
		List<ExemptBean> exemptList = extemptDao.queryRelatedToUsersOrOrgs( siteId, menuId );

        //用户列表
        List<String> userArr = new ArrayList<String>();
        Map<String, Boolean> userMap = new HashMap<String, Boolean>();
        //部门
        List<String> orgList = new ArrayList<String>();
        for( ExemptBean exempt : exemptList ){
        	String relationId = exempt.getRelationId();
        	String type = exempt.getType();
        	//判断relationId是用户id还是部门code
        	if("user_id".equals(type)){
        		userArr.add( relationId );
                userMap.put( relationId, true );
        	}else if("org_code".equals(type)){
        		orgList.add( relationId );
        	}
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        //免考勤人员初始化数据
        if( !userArr.isEmpty() ){
            String[] arr =  userArr.toArray(new String[]{});
            List<Map> result = orgMapper.selectOrgsRelatedToUsers( arr );
            Map<String, Boolean> baseMap = BaseService.getParentOrgs(result, secManager);
            map.put("orgArr", JSONObject.fromObject( baseMap ).toString() );
        }else{
        	map.put("orgArr", "{}" );
        }
        map.put( "userArr", JSONObject.fromObject( userMap ).toString() );
        
        //免考勤部门初始化数据
        if( !orgList.isEmpty() ){
        	Map<String, Object> orgMap = new HashMap<String, Object>();
            List<Map> result = orgMapper.selectOrgsParents(orgList);
            for(Map resultMap:result){
            	orgMap.put(resultMap.get("ORGCODE").toString(),resultMap.get("ORGNAME").toString());
            }
            map.put("orgs", JSONObject.fromObject( orgMap ).toString() );
        }else{
        	map.put("orgs", "{}" );
        }
        return map;
	}
	
	/**
     * @description:更新免考勤名单
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @param userDel userAdd orgDel orgAdd
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateExempt(String userDel, String userAdd, String orgDel, String orgAdd, String menuId) {
    	int delCount = 0;
    	int addCount = 0;
    	//免考勤名单删除
    	List<Object> exemptDelList = new ArrayList<Object>();
    	//删除免考勤人员
    	exemptDelList.addAll(addOrDelExemptList(userDel,"user_id", menuId));
    	//删除免考勤部门
    	exemptDelList.addAll(addOrDelExemptList(orgDel,"org_code", menuId));
    	
    	if(!exemptDelList.isEmpty()){
    		delCount = extemptDao.batchDeleteExempt( exemptDelList );
    	}
    	
        //考勤名单增加
        List<Object> exemptAddList = new ArrayList<Object>();
        //增加免考勤人员
        exemptAddList.addAll(addOrDelExemptList(userAdd,"user_id", menuId));
        //增加免考勤部门
        exemptAddList.addAll(addOrDelExemptList(orgAdd,"org_code", menuId));
        
        if(!exemptAddList.isEmpty()){
        	addCount = extemptDao.batchInsertExempt( exemptAddList );
        }
        
        //有通过一系列操作，用户没有改变 =0
        if( delCount + addCount >= 0 ){
            return 1;
        }
        return 0;
    }
    
    
    /**
     * @description:获取免考勤增删的数据方法
     * @author: zhuw
     * @createDate: 2016年2月1日
     * @param addOrDel type
     * @return List
     */
    public List<Object> addOrDelExemptList(String addOrDel, String type, String menuId) {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String siteId = userInfoScope.getSiteId();
    	List<Object> addOrDelExemptList = new ArrayList<Object>();
    	if(StringUtils.isNotBlank( addOrDel )){
    		String[] addOrDelArr = addOrDel.split( "," );
	    	if( addOrDelArr != null && addOrDelArr.length > 0 ){
	            for( String userId : addOrDelArr ){
	            	Map<String, Object> exemptMap = new HashMap<String, Object>();
	            	exemptMap.put("id", UUIDGenerator.getUUID());
	    			exemptMap.put("siteId", siteId);
	    			exemptMap.put("type", type);
	    			exemptMap.put("relationId", userId);
	    			exemptMap.put("menuId", menuId);
	    			addOrDelExemptList.add( exemptMap );
	            }
	        }
    	}
    	return addOrDelExemptList;
    	
    }
    
    
    @Override
    public Map<String, String> queryAtdExcludeMapBySiteId(String siteId, String menuId){
    	List<ExemptBean> excludeList = extemptDao.queryRelatedToUsersOrOrgs(siteId, menuId);
    	Map<String, String> result = new HashMap<String, String>();
    	if( excludeList != null ){
    		for (ExemptBean exemptBean : excludeList) {
    			String str = result.get(exemptBean.getType());
    			if(str==null){
    				str="";
    			}
    			str += exemptBean.getRelationId()+",";
    			result.put(exemptBean.getType(), str);
			}
    	}
    	return result;
    }
    
    
}
