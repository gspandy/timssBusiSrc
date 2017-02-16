package com.timss.attendance.service;

import java.util.List;

import com.timss.attendance.bean.DefinitionBean;

/**
 * 
 * @title: 考勤系统参数定义
 * @description: {desc}
 * @company: gdyd
 * @className: DefinitionService.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DefinitionService {

    /**
     * 
     * @description:插入休假规则
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @param definitionBean
     * @return:int
     */
    int insertDefinition(DefinitionBean definitionBean);

    /**
     * 
     * @description:通过站点来查找考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return:DefinitionBean
     */
    DefinitionBean queryDefinitionBySite();

    /**
     * 指定站点id检索考勤规则
     * @param siteId
     * @return
     */
    DefinitionBean queryDefinitionBySiteId(String siteId);
    
    /**
     * 
     * @description:更新考勤系统参数
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    int updateDefinition(DefinitionBean vo);

    /**
     * 更新LastCheck
     * @param date
     * @return
     */
    int updateDefinitionLastCheck(DefinitionBean vo);
    
    /**
     * 查询要更新考勤结果的考勤规则
     * 参数null查询所有站点
     * @return
     */
    List<DefinitionBean>queryCheckWorkstatusDefinition(String siteId);
}
