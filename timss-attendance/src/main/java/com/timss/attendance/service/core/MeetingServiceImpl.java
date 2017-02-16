package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.MeetingBean;
import com.timss.attendance.dao.MeetingDao;
import com.timss.attendance.service.MeetingService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.ColorUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.util.SortUtil;
import com.timss.attendance.vo.EventsCalenderVo;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 预定会议室
 * @description: {desc}
 * @company: gdyd
 * @className: MeetingServiceImpl.java
 * @author: fengzt
 * @createDate: 2015年3月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("meetingService")
public class MeetingServiceImpl implements MeetingService {
    
    private Logger log = LoggerFactory.getLogger( MeetingServiceImpl.class );

    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private MeetingDao meetingDao;

    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 
     * @description:插入或者更新 预定会议室
     * @author: fengzt
     * @createDate: 2015年3月4日
     * @param meeting
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertOrUpdateMeeting(MeetingBean meetingBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        int count = 0;
        
        //校验是否插入有相同会议室时间段是否有冲突
        map = isValidateMeeting( meetingBean );
        String result = (String)map.get( "result" );
       // String roleFlag = getLoginUserRole();
        //普通员工检查冲突
        if( /*StringUtils.equals( "normal", roleFlag ) &&*/ "fail".equalsIgnoreCase( result ) ){
            return map;
        }
        
        //插入or更新
        if( meetingBean.getId() > 0 ){
            meetingBean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
            meetingBean.setUpdateDate( new Date() );
            count = meetingDao.updateMeeting( meetingBean );
        }else{
            count = insertMeeting( meetingBean );
        }
        meetingBean = queryMeetingById( meetingBean.getId() );
        
        //领导直接作废其他冲突的
     /*   if( StringUtils.equals( "lingdao", roleFlag ) ){
            List<MeetingBean> beans = (List<MeetingBean>) map.get( "diffBeans" );
            for( MeetingBean vo : beans ){
                log.info( "领导角色--作废的会议室是：" + vo.getId() + "--" + vo.getUserName() + "--" + vo.getMeetingName() );
                deleteMeeting( vo.getId(), meetingBean.getMeetingName() + " 使用了会议室！" );
            }
        }
        */
        map.put( "meetingBean", meetingBean );
        if( count > 0 ){
            map.put( "result", "success" );
        }
        
        return map;
    }
    
    
    /**
     * 
     * @description:通过ID查询
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param id
     * @return:
     */
    @Override
    public MeetingBean queryMeetingById(int id) {
        return meetingDao.queryMeetingById(id);
    }

    /**
     * 
     * @description:校验是否同一个会议室在时间段是否有冲突
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> isValidateMeeting(MeetingBean meetingBean) {
        List<MeetingBean> beans = meetingDao.queryMeetingByDiffDate( meetingBean );
        Map<String, Object> map = new HashMap<String, Object>();
        
        if( beans != null && beans.size() > 0 ){
            //非自己冲突
            if( beans.size() == 1 && meetingBean.getId() == beans.get( 0 ).getId() ){
                map.put( "result", "valid" );
            }else{
                map.put( "result", "fail" );
                map.put( "diffBeans", beans );
            }
        }else{
            map.put( "result", "valid" );
        }
        
        return map;
    }

    /**
     * 
     * @description:插入
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param meetingBean
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    private int insertMeeting( MeetingBean meetingBean ){
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        meetingBean.setUserName( userInfo.getUserName() );
        meetingBean.setSiteId( userInfo.getSiteId() );
        meetingBean.setDeptId( userInfo.getOrgId() );
        
        meetingBean.setDeptName( userInfo.getOrgName() );
        meetingBean.setCreateBy( userInfo.getUserId() );
        meetingBean.setCreateDate( new Date() );
        
        meetingBean.setActivities( "Y" );
        int count = meetingDao.insertMeeting( meetingBean );
        return count;
    }

    /**
     * 
     * @description:日历时间渲染
     * @author: fengzt
     * @createDate: 2015年3月5日
     * @param start
     * @param end
     * @param meetingNo 
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> queryCalendarByDate(String start, String end, String meetingNo) {
        Date startDate = DateFormatUtil.parseDate( start + " 0000", "yyyy-MM-dd HHmm" );
        Date endDate = DateFormatUtil.parseDate( end + " 2359", "yyyy-MM-dd HHmm" );
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "meetingNo", meetingNo );
        params.put( "siteId", privUtil.getUserInfoScope().getSiteId() );
        
        List<MeetingBean> beans = meetingDao.queryCalendarByDate( params );
        List<EventsCalenderVo> eventsVos = new ArrayList<EventsCalenderVo>();
        
        //转化成日历所需要的对象
        List<AppEnum> emList = itcMvcService.getEnum( "ATD_MT_NO" );
        for( MeetingBean vo : beans ){
            String startStr = DateFormatUtil.formatDate( vo.getStartDate(), "yyyy-MM-dd'T'HH:mmZ" );
            String endStr = DateFormatUtil.formatDate( vo.getEndDate(), "yyyy-MM-dd'T'HH:mmZ" );
            //开始时间和结束时间
            int diffDays =  (int)DateFormatUtil.dateDiff( "dd", 
                    DateFormatUtil.getFormatDate( vo.getStartDate(), "yyyy-MM-dd"),
                    DateFormatUtil.getFormatDate( vo.getEndDate(), "yyyy-MM-dd") ) ;
            
            String meetingNum = getCategoryName( emList, vo.getMeetingNo() );
            String title = meetingNum + " " ;
            //String title = "" ;
            if( diffDays > 0 ){
                title += DateFormatUtil.formatDate( vo.getStartDate(), "MM月dd号 HH:mm" ) + " - " +  DateFormatUtil.formatDate( vo.getEndDate(), "MM月dd号 HH:mm" );
            }else {
                title += DateFormatUtil.formatDate( vo.getStartDate(), "HH:mm" ) + " - " +  DateFormatUtil.formatDate( vo.getEndDate(), "HH:mm" );
            }
            
            //title += " " + vo.getUserName();
            if( StringUtils.equals( vo.getIsLeader(), "Y" ) ){
                title += " ★";
            }
            
            EventsCalenderVo eVo = new EventsCalenderVo();
            eVo.setId( vo.getId() );
            eVo.setTitle( title );
            
            eVo.setStart( startStr );
            eVo.setEnd( endStr );
            eVo.setAllDay( true );
            eVo.setColor( ColorUtil.nameChangToColor( meetingNum ) );
            eventsVos.add( eVo );
        }
        
        //根据颜色排序
        SortUtil.sortList( eventsVos, "title", true );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if( eventsVos.size() > 0 ){
            dataMap.put( "result", "success" );
            dataMap.put( "eventsVos", eventsVos );
        }else{
            dataMap.put( "result", "fail" );
        }
        
        return dataMap;
    }
    
    /**
     * 
     * @description:枚举code to name
     * @author: fengzt
     * @createDate: 2014年10月11日
     * @param flowCode
     * @return:
     */
    private String getCategoryName(List<AppEnum> emList,String categoryId) {
        String categoryName = categoryId;
        if( StringUtils.isNotBlank( categoryId ) ){
            if( emList != null && emList.size() > 0 ){
                for( AppEnum appVo : emList ){
                    if( categoryName.equalsIgnoreCase( appVo.getCode() ) ){
                        categoryName =  appVo.getLabel();
                        break;
                    }
                }
            }
        }
        return categoryName;
    }

    /**
     * 
     * @description:删除
     * @author: fengzt
     * @createDate: 2015年3月6日
     * @param id
     * @param remark
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int deleteMeeting(int id, String remark) {
        MeetingBean meetingBean = new MeetingBean();
        meetingBean.setUpdateBy( privUtil.getUserInfoScope().getUserId() );
        meetingBean.setUpdateDate( new Date() );
        meetingBean.setId( id );
        meetingBean.setRemark( remark );
        meetingBean.setActivities( "N" );
        
        int count = meetingDao.updateMeetingForStatus( meetingBean );
        return count;
    }

    /**
     * 
     * @description:通过会议室编号来查询
     * @author: fengzt
     * @createDate: 2015年3月23日
     * @param meetingNo
     * @return:List<MeetingBean>
     */
    @Override
    public List<MeetingBean> queryMeetingByNo(String meetingNo, int amount, int rownum ) {
        String start = DateFormatUtil.formatDate( new Date(), "yyyy-MM-dd" );
        String end = DateFormatUtil.formatDate( DateFormatUtil.addDate( new Date(), "d", amount ), "yyyy-MM-dd" );
        Date startDate = DateFormatUtil.parseDate( start + " 0000", "yyyy-MM-dd HHmm" );
        Date endDate = DateFormatUtil.parseDate( end + " 2359", "yyyy-MM-dd HHmm" );
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "startDate", startDate );
        params.put( "endDate", endDate );
        params.put( "meetingNo", meetingNo );
        params.put( "siteId", privUtil.getUserInfoScope().getSiteId() );
        params.put( "rownum", rownum );
        
        List<MeetingBean> beans = meetingDao.queryCalendarByDateMore( params );
        return beans;
    }

}
