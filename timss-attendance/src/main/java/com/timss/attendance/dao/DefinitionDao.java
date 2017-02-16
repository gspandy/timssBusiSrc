package com.timss.attendance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.DefinitionBean;

/**
 * 
 * @title: 考勤系统参数定义DAO
 * @description: {desc}
 * @company: gdyd
 * @className: DefinitionDao.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DefinitionDao {

    /**
     * 
     * @description:插入休假规则
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param definitionBean
     * @return:int
     */
    public int insertDefinition(DefinitionBean definitionBean);

    /**
     * 
     * @description:通过站点信息来拿考勤系统参数定义
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param siteId
     * @return:List<DefinitionBean>
     */
    public List<DefinitionBean> queryDefinitionBySite(String siteId);

    /**
     * 
     * @description:更新考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param DefinitionBean
     * @return:int
     */
    public int updateDefinition(DefinitionBean vo);

    /**
     * 更新lastCheck
     * @param vo
     * @return
     */
    public int updateDefinitionLastCheck(DefinitionBean vo);
    
    /**
     * 查询要更新考勤结果的考勤规则
     * 参数null查询所有站点
     * @return
     */
    public List<DefinitionBean> queryCheckWorkstatusDefinition(@Param("siteId")String siteId);
}
