package com.timss.attendance.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.attendance.dao.StatDao;
import com.timss.attendance.service.StatService;
import com.timss.attendance.vo.StatVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 考勤统计定时任务
 * @description: {desc}
 * @company: gdyd
 * @className: StatScheduler.java
 * @author: fengzt
 * @createDate: 2014年10月17日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Component
@Lazy(false)
public class StatScheduler {
    private Logger log = LoggerFactory.getLogger( StatScheduler.class );
    
    @Autowired
    private StatService statService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private StatDao statDao;
    
    @Autowired
    private ISecurityMaintenanceManager iSecurityMaintenanceManager;
    
    /**
     * 
     * @description:每年定时任务更新整个考勤统计--ITC
     * @author: fengzt
     * @createDate: 2014年10月17日:
     */
    //@Scheduled(cron = " 2  1  0  1  1  ?  ")
    //@Scheduled(cron = "2/30 * * * * ? ")
    public void countStatData() throws Exception{
        
        UserInfo userInfo = itcMvcService.getUserInfo( "890147", "ITC" );
        ThreadLocalHandler.createNewVarableOweUserInfo( userInfo );

        statService.countStatData( );
    }
    
    /**
     * 
     * @description:每年定时任务更新整个考勤统计--湛江风电
     * @author: fengzt
     * @createDate: 2014年10月17日:
     */
    //@Scheduled(cron = " 2  1  0  1  1  ?  ")
    //@Scheduled(cron = "2/30 * * * * ? ")
    public void countZJWStatData() throws Exception{
        
        UserInfo userInfo = itcMvcService.getUserInfo( "180811", "ZJW" );
        ThreadLocalHandler.createNewVarableOweUserInfo( userInfo );
        
        statService.countStatData( );
    }
    
    //@Scheduled(cron = " 2  1  0  1  1  ?  ")
    //@Scheduled(cron = "0 8 16 * * ? ")
    public void countSWFStatData() throws Exception{
        
        UserInfo userInfo = itcMvcService.getUserInfo( "880040", "SWF" );
        ThreadLocalHandler.createNewVarableOweUserInfo( userInfo );
        
        statService.countStatData( );
    }
    
    /**
     * 
     * @description:人员不存在更新人员考勤统计状态
     * @author: fengzt
     * @createDate: 2014年10月17日:
     */
    //@Scheduled(cron = "2/30 * * * * ? ")
	//@Scheduled(cron = "0 0 3 * * ?")
    //@Scheduled(cron = "0 50 9 * * ?")
    public void updateITCStatStatus() throws Exception{
        //需要更新人员状态的列表
        Map<String, String> map = new HashMap<String, String>();
        map.put( "ITC", "890147" );
        map.put( "ZJW", "180811" );
        map.put( "SWF", "880040" );
        
        Set<String> siteSet = map.keySet();
        //需要更新用户状态的列表
        List<StatVo> uList = new ArrayList<StatVo>();
        
        for( String siteId : siteSet ){
            UserInfo userInfo = itcMvcService.getUserInfo( map.get( siteId ), siteId );
            ThreadLocalHandler.createNewVarableOweUserInfo( userInfo );
            
            Map<String, Object> paramMap = new HashMap<String, Object>();
            int year = Calendar.getInstance().get( Calendar.YEAR );
            paramMap.put( "year", year );
            paramMap.put( "siteId", userInfo.getSiteId() );
            
            //考勤统计人员数据
            List<StatVo> statList = statDao.queryAllStat( paramMap );
            
            //站点下所有用户信息u
            Page<SecureUser> pageVo = new Page<SecureUser>( 1, 99999 );
            SecureUser secureUser = new SecureUser();
            secureUser.setCurrentSite( siteId );
            secureUser.setId( map.get( siteId ) );
            pageVo.setParameter( "userStatus", "Y" );
            Page<SecureUser> userList = iSecurityMaintenanceManager.retrieveUniqueUsers( pageVo, secureUser );
            List<SecureUser> personList = userList.getResults();
            
            //比对用户
            for( StatVo vo : statList ){
                String userId = vo.getUserId();
                boolean flag = false;
                for( SecureUser u : personList ){
                    if( StringUtils.equals( u.getId(), userId )){
                        flag = true;
                        break;
                    }
                }
                //加入离职列表
                if( !flag ){
                    StatVo temp = new StatVo();
                    temp.setUserId( userId );
                    temp.setYearLeave( year );
                    temp.setUserStatus( "离职" );
                    uList.add( temp );
                }
            }
        }
        
        //批量更新
        if( uList.size() > 0 ){
           int count = statService.updateBatchStatStatus( uList );
           log.info( "每日定时更新考勤统计的用户数量是：" + count + " ; 明细：" + uList.toString() );
        }
        
    }
    
	@Scheduled(cron = "0 10 0 * * ?")
    public void checkStat() throws Exception{
		log.info("statScheduler.checkStat start");
    	String[]checkSites=new String[]{"SWF","ITC","ZJW","DPP"};
    	for (String siteId : checkSites) {
    		log.info("statScheduler.checkStat->"+siteId);
			statService.checkPersonStat(false,false,siteId, null,null,null);
		}
    	log.info("statScheduler.checkStat finish");
    }
    
    

}
