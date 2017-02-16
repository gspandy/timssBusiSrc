package com.timss.operation.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Duty;
import com.timss.operation.bean.Rules;
import com.timss.operation.bean.RulesDetail;
import com.timss.operation.bean.Shift;
import com.timss.operation.dao.RulesDetailDao;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.RulesDetailService;
import com.timss.operation.service.RulesService;
import com.timss.operation.service.ShiftService;
import com.timss.operation.vo.RulesDetailForListVo;
import com.timss.operation.vo.RulesDetailVo;
import com.timss.operation.vo.RulesFormVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 排班规则详情 serviceImpl
 * @description: {desc}
 * @company: gdyd
 * @className: RulesDetailServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年6月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("rulesDetailService")
public class RulesDetailServiceImpl implements RulesDetailService {
    private Logger log = Logger.getLogger( RulesDetailServiceImpl.class );
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private RulesDetailDao rulesDetailDao;
    
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private RulesService rulesService;
    
    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 
     * @description:批量插入排版规则详情
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param maps
     * @param rulesId
     * @param deptId
     * @param rulesDetailName
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> batchInsertRulesDetail(List<HashMap<String, Object>> maps, int rulesId, String deptId, String rulesDetailName) {
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        
        List<Duty> duties = dutyService.queryDutyByStationId( deptId );
        
       // Map<Integer, Integer> dutyMap = new HashMap<Integer, Integer>();
        ArrayList<Integer> dutyArr = new ArrayList<Integer>();
        //拿到岗位中所有的值别map <sortType, id>
        for( Duty duty : duties ){
           // dutyMap.put( duty.getSortType(), duty.getId() );
            dutyArr.add( duty.getId() );
        }
        
        //页面datagrid 一个cell 一个RulesDetail
        List<RulesDetail> rulesDetails = new ArrayList<RulesDetail>();
        
        //整个datagrid统一ID
        String uuid = UUID.randomUUID().toString();
        
        //按行取
        for( HashMap<String, Object> map : maps ){
            int dayTime = Integer.parseInt( (String) map.get( "dayTime" ).toString() );
            
            //每个cell 一个rulesDetail
            for(String key : map.keySet() ) {
                if( StringUtils.isNotEmpty( key ) && key.contains( "field" ) ){
                    RulesDetail rulesDetail = new RulesDetail();
                    
                    rulesDetail.setDayTime( dayTime );
                    rulesDetail.setName( rulesDetailName );
                    rulesDetail.setUuid( uuid );
                    rulesDetail.setSiteId( siteId );
                    
                    Duty duty = new Duty();
                    //截取field10 中的数字 10
                    String fieldWord = key.substring( 5, key.length() );
                    duty.setId( dutyArr.get( Integer.parseInt( fieldWord ) - 1 ) );
                    rulesDetail.setDuty( duty );
                    
                    Shift shift = new Shift();
                    shift.setId( Integer.parseInt( (String)map.get( key ).toString() ) );
                    rulesDetail.setShift( shift );
                    
                    Rules rules = new Rules();
                    rules.setId( rulesId );
                    rulesDetail.setRules( rules );
                    
                    rulesDetail.setStationId( deptId );
                    
                    rulesDetails.add( rulesDetail );
                }
            }
          
        }
        int count = 0;
        if(rulesDetails.size()>0){
        	Map<String, Object> rulesDetailsMap = new HashMap<String, Object>();
            rulesDetailsMap.put( "rulesDetails", rulesDetails );
            
            count = rulesDetailDao.batchInsert( rulesDetailsMap );
        }
        
        Map<String, Object>result=new HashMap<String, Object>();
        result.put("count", count);
        result.put("uuid", uuid);
        return result;
    }


    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param search
     * @param pageVo
     * @return:List<RulesDetailForListVo>
     */
    public List<RulesDetailForListVo> queryRulesDetailBySearch(String search, Page<HashMap<?, ?>> pageVo) {
        //按
        pageVo.setParameter( "name", search );
        List<RulesDetailForListVo> rulesDetails = queryAllRulesDetailByPage( pageVo );
        return rulesDetails;
    }

    /**
     * 
     * @description:获取所有RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param pageVo
     * @return:List<RulesDetailForListVo>
     */
    public List<RulesDetailForListVo> queryAllRulesDetailByPage(Page<HashMap<?, ?>> pageVo) {
        
        //按UUID name stationId group
        List<RulesDetailForListVo> rulesDetails = rulesDetailDao.queryRulesDetailByPage( pageVo );
        
        
        //set 值别 班次
        for( RulesDetailForListVo rulesDetail : rulesDetails ){
            String stationId = rulesDetail.getStationId();
            
            if( StringUtils.isNotBlank( stationId ) ){
                //设置值别
                List<Duty> duties = dutyService.queryDutyByStationId( stationId );
                rulesDetail.setDutyCount( duties.size() );
                
                if( duties != null && duties.size() > 0 ){
	                StringBuffer dutyString = new StringBuffer("");
	                for( Duty duty : duties ){
	                     dutyString.append( duty.getName() ).append( "," );
	                }
	                
	                rulesDetail.setDutyString( dutyString.toString().substring( 0, dutyString.length() - 1 ) );
                }
                
                //设置班次
                List<Shift> shifts = shiftService.queryShiftByStationId( stationId );
                rulesDetail.setShiftCount( shifts.size() );
                
                if( shifts != null && shifts.size() > 0 ){
                    StringBuffer shiftString = new StringBuffer("");
                    for( Shift shift : shifts ){
                        shiftString.append( shift.getName() ).append( "," );
                    }
                    
                    rulesDetail.setShiftString( shiftString.toString().substring( 0, shiftString.length() - 1 ) );
                }
            }
            
        }
        
        return rulesDetails;
    }

    /**
     * 
     * @description:通过UUID查找RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:List<HashMap<String, Object>>
     */
    @Override
    public List<HashMap<String, Object>> queryRulesDetailByUuid(String uuid) {
        List<RulesDetailVo> rulesDetailVos = rulesDetailDao.queryRulesDetailByUuid( uuid );
        
        //同一个uuid 岗位相同
        //String stationId = rulesDetailVos.get( 0 ).getStationId();
        List<Duty> duties = dutyService.queryDutyByUuid( uuid );
        
        Map<Integer, Integer> dutyMap = new HashMap<Integer, Integer>();
        //拿到岗位中所有的值别map <id, index>
        for( int index = 0; index < duties.size(); index++ ){
            Duty duty = duties.get( index );
            dutyMap.put( duty.getId(), index + 1 );
        }
        
        List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
        
        //按天次来构造map
        for( int i = 0; i < rulesDetailVos.size() ; i += duties.size() ){
            HashMap<String, Object> map = new HashMap<String, Object>();
            
            int dayTime = rulesDetailVos.get( i ).getDayTime();
            map.put( "dayTime", dayTime );
            
            //值别周期
            for( int j = 0; j < duties.size(); j ++  ){
            	if((i+j)>=rulesDetailVos.size()){//数组越界
            		break;
            	}
                int dutyId = rulesDetailVos.get( i + j ).getDutyId();
                int shiftId = rulesDetailVos.get( i + j ).getShiftId();
                
                String field = "field" + dutyMap.get( dutyId );
                map.put( field, shiftId );
            }
            result.add( map );
        }
        
        return result;
    }

    /**
     * 
     * @description:通过uuid查找staionid
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:String
     */
    @Override
    public String queryStationIdByUuid(String uuid) {

        List<RulesDetailVo> rulesDetailVos = rulesDetailDao.queryStationIdByUuid( uuid );
        
        //同一个uuid 岗位相同
        String stationId = rulesDetailVos.get( 0 ).getStationId();
        
        return stationId;
    }

    /**
     * 
     * @description:更新
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param rulesFormVo
     * @param maps
     * @param uuid
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public Map<String,Object> updateRuleDetail(RulesFormVo rulesFormVo, List<HashMap<String, Object>> maps, String uuid ) {

        rulesService.updateRulesByFormVo( rulesFormVo );
        
        deleteRulesDetail( uuid );
        Map<String,Object>result = batchInsertRulesDetail( maps, rulesFormVo.getId(), rulesFormVo.getStationId(), rulesFormVo.getName() );
        return result;
    }

    /**
     * 
     * @description:通过uuid删除
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public int deleteRulesDetail(String uuid) {
        return rulesDetailDao.deleteRulesDetailByUuid( uuid );
    }

    /**
     * 
     * @description:删除排班规则详情和行列表
     * @author: fengzt
     * @createDate: 2014年7月2日
     * @param id
     * @param uuid
     * @return:boolean
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean deleteRulesDetailByUuid(int id, String uuid) {
        int rulesCount = rulesService.deleteRulesById( id );
        int rdCount = deleteRulesDetail( uuid );
        if( rulesCount > 0 && rdCount > 0 ){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for( int i = 0 ; i < 100; i++ ){
            arr.add( i );
        }
        for( Integer j : arr ){
            System.out.println( j );
        }
    }
}
