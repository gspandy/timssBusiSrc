package com.timss.attendance.service.core;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.dao.DefinitionDao;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 考勤系统参数定义
 * @description: {desc}
 * @company: gdyd
 * @className: DefinitionServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("definitionService")
@Transactional(propagation=Propagation.SUPPORTS)
public class DefinitionServiceImpl implements DefinitionService {
    
    private Logger log = Logger.getLogger( DefinitionServiceImpl.class );

    @Autowired
    private DefinitionDao definitionDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private StatService statService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    /**
     * 
     * @description:拿到站点信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private String getSiteId(){
        return privUtil.getUserInfoScope().getSecureUser().getCurrentSite();
    }
    
    /**
     * 
     * @description:拿到登录工号信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private String getUserId(){
        return privUtil.getUserInfoScope().getUserId();
    }
    
    /**
     * 
     * @description:插入休假规则
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param definitionBean
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertDefinition(DefinitionBean definitionBean) {
        //判断站点在有效期内是否有站点了，如果有就不能添加
        DefinitionBean vo = queryDefinitionBySite();
        if( vo != null ){
            return 10000000;
        }
        
        definitionBean.setSiteId( getSiteId() );
        definitionBean.setCreateBy( getUserId() );
        definitionBean.setCreateDate( new Date() );
        
        definitionBean.setUpdateBy( getUserId() );
        definitionBean.setUpdateDate( new Date() );
        
        Date yyyyMMddDate =  DateFormatUtil.getCurrentDate();
        Date endYear = DateFormatUtil.addDate( yyyyMMddDate, "y", 20 );
        definitionBean.setStartYear( yyyyMMddDate );
        definitionBean.setEndYear( endYear );
        
        return definitionDao.insertDefinition( definitionBean );
    }

    /**
     * 
     * @description:通过站点来查找考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return:DefinitionBean
     */
    @Override
    public DefinitionBean queryDefinitionBySite() {
        List<DefinitionBean> deList = definitionDao.queryDefinitionBySite( getSiteId() );
        
        if( !deList.isEmpty() ){
            Date today = DateFormatUtil.getCurrentDate();
            //有效时间区间内的系统参数
            for( DefinitionBean vo : deList ){
                boolean flagMin = DateFormatUtil.compareDate( today, vo.getStartYear() );
                boolean flagMax = DateFormatUtil.compareDate( vo.getEndYear(), today );
                
                if( flagMin && flagMax ){
                    return vo;
                }
            }
        }
        return null;
    }

    /**
     * 
     * @description:更新考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateDefinition(DefinitionBean vo) {
        vo.setUpdateBy( getUserId() );
        vo.setUpdateDate( new Date() );
        
        int count = definitionDao.updateDefinition( vo );
        
        //更新统计信息
        //statService.updateCurrentYearStat();
        
        return count;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateDefinitionLastCheck(DefinitionBean vo) {
        vo.setUpdateDate( new Date() );
        
        int count = definitionDao.updateDefinitionLastCheck( vo );
        
        return count;
    }
    
	@Override
	public DefinitionBean queryDefinitionBySiteId(String siteId) {
		List<DefinitionBean> deList = definitionDao.queryDefinitionBySite( siteId );
        
        if( !deList.isEmpty() ){
            Date today = DateFormatUtil.getCurrentDate();
            //有效时间区间内的系统参数
            for( DefinitionBean vo : deList ){
                boolean flagMin = DateFormatUtil.compareDate( today, vo.getStartYear() );
                boolean flagMax = DateFormatUtil.compareDate( vo.getEndYear(), today );
                
                if( flagMin && flagMax ){
                    return vo;
                }
            }
        }
        return null;
	}

	@Override
	public List<DefinitionBean>queryCheckWorkstatusDefinition(String siteId){
		List<DefinitionBean> deList = definitionDao.queryCheckWorkstatusDefinition(siteId);
		return deList;
	}
}
