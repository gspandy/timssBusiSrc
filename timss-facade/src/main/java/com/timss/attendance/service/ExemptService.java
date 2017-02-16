package com.timss.attendance.service;

import java.util.Map;

/**
 * 
 * @title: 免考勤和免打卡Service
 * @description: 
 * @company: gdyd
 * @className: ExemptService.java
 * @author: zhuw
 * @createDate: 2016年1月22日
 * @updateUser: zhuw
 * @version: 2.0
 */
public interface ExemptService {
    
    /**
     * 
     * @description:查询关系用户或部门id
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @param siteId menuId
     * @return:Map
     */
    public Map<String, Object> queryRelatedToUsersOrOrgs(String siteId, String menuId);
    
    /**
     * 
     * @description:更新免考勤名单
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @param userDel userAdd orgDel orgAdd menuId
     * @return int
     */
    public int updateExempt(String userDel, String userAdd, String orgDel, String orgAdd, String menuId);
    
    /**
     * 查询站点的免考勤信息，返回类型-id字符串的map
     * @param siteId menuId
     * @return
     * @throws Exception
     */
    Map<String, String> queryAtdExcludeMapBySiteId(String siteId, String menuId);
}
