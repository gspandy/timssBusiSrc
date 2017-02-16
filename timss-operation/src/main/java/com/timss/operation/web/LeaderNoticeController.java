package com.timss.operation.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.timss.operation.bean.LeaderNotice;
import com.timss.operation.service.LeaderNoticeService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.LeaderNoticeVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 上级通知controller
 * @description:
 * @company: gdyd
 * @className: LeaderNoticeController.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/leaderNotice")
public class LeaderNoticeController {
    
    private Logger log = Logger.getLogger( LeaderNoticeController.class );            

    @Autowired
    private LeaderNoticeService leaderNoticeService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    public LeaderNoticeService getLeaderNoticeService() {
        return leaderNoticeService;
    }

    public void setLeaderNoticeService(LeaderNoticeService leaderNoticeService) {
        this.leaderNoticeService = leaderNoticeService;
    }

    /**
     * @description:新建上级通知
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertLeaderNotice")
    public Map<String, Object> insertLeaderNotice(String formData) {
        
        LeaderNoticeVo leaderNoticeVo = VOUtil.fromJsonToVoUtil( formData, LeaderNoticeVo.class );
        
        //用户登录的站点
        String siteId = null;
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
            userId = userInfoScope.getUserId();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        
        leaderNoticeVo.setSiteId( siteId );
        leaderNoticeVo.setWriteUserId( userId );
        leaderNoticeVo.setUpdateUserId( userId );
        leaderNoticeVo.setWriteDate( DateFormatUtil.getFormatDate(new Date(),"yyyy-MM-dd HH:mm:ss") );
        leaderNoticeVo.setUpdateTime( DateFormatUtil.getFormatDate(leaderNoticeVo.getWriteDate(),"yyyy-MM-dd HH:mm:ss"));
        
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        //系统自动生成的编号，由日期+时间毫米数+随机数组成
        String num = DateFormatUtil.formatDate( today, "yyyyMMdd" );
        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        int minute = calendar.get( Calendar.MINUTE );
        int seconds = calendar.get( Calendar.SECOND );
        int randomNum = (hour * 60 * 60 + minute * 60 + seconds ) * 1000 + (int)(Math.random()*1000);
        num += Integer.toString(randomNum);
        
        leaderNoticeVo.setNum( num );
        leaderNoticeVo.setFeedBackContent( "尚未落实" );
        leaderNoticeVo.setIsFinished( "N" );
       
        leaderNoticeVo = leaderNoticeService.insertLeaderNotice( leaderNoticeVo );
        Map<String, Object> map = new HashMap<String, Object>();

        if ( leaderNoticeVo.getId() > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }

    /**
     * @description:上级通知列表 分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryAllLeaderNoticeList")
    public Map<String, Object> queryAllLeaderNoticeList(int rows, int page, String search ) {
        
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        pageVo.setPageNo( page );
        pageVo.setPageSize( rows );
        
        List<LeaderNotice> leaderNoticeList = new ArrayList<LeaderNotice>();
        
        //用户登录的站点
        String siteId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            siteId = userInfoScope.getSecureUser().getCurrentSite();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        pageVo.setParameter( "siteId", siteId );
        
        //高级搜索
        if( StringUtils.isNotBlank( search ) ){
            HashMap<String, Object> map = VOUtil.fromJsonToHashMap( search);
            leaderNoticeList = leaderNoticeService.queryLeaderNoticeBySearch( map, pageVo );
            
        }else{
            //默认分页
            leaderNoticeList = leaderNoticeService.queryLeaderNoticeByPage( pageVo );
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", leaderNoticeList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新上级通知
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateLeaderNotice") 
    public Map<String, Object> updateLeaderNotice(String formData) {
        LeaderNoticeVo leaderNoticeVo = VOUtil.fromJsonToVoUtil( formData, LeaderNoticeVo.class );
        //用户登录的站点
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            userId = userInfoScope.getUserId();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        leaderNoticeVo.setUpdateUserId( userId );
        leaderNoticeVo.setUpdateTime( DateFormatUtil.getFormatDate( new Date(), "yyyy-MM-dd HH:mm:ss" ) );
        if(leaderNoticeVo.getIsFinished() == "N"){
            leaderNoticeVo.setFinishTime( null );
        }
        
        int count = leaderNoticeService.updateLeaderNotice( leaderNoticeVo );
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除上级通知
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteLeaderNotice")
    public Map<String, Object> deleteLeaderNotice(String formData) {
        
        LeaderNoticeVo leaderNoticeVo = VOUtil.fromJsonToVoUtil( formData, LeaderNoticeVo.class );
        
        int count = 0 ;
        if( leaderNoticeVo.getId() > 0 ){
            count = leaderNoticeService.deleteLeaderNoticeById( leaderNoticeVo.getId() );
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
     * @description:通过id查找leaderNotice
     * @author: huanglw
     * @createDate: 2014年8月3日
     * @param id
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryLeaderNoticeById")
    public @ResponseBody Map<String, Object> queryLeaderNoticeById(int id) {
        Map<String, Object> map = new HashMap<String, Object>();
        LeaderNoticeVo leaderNoticeVo = leaderNoticeService.queryLeaderNoticeById( id );
        map.put( "result", leaderNoticeVo );
        return map;
    }
    
}
