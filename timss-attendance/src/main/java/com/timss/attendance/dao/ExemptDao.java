package com.timss.attendance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.ExemptBean;

/**
 * 
 * @title: ExemptDao 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: ExemptDao.java
 * @author: zhuw
 * @createDate: 2016年1月22日
 * @updateUser: zhuw
 * @version: 2.0
 */
public interface ExemptDao {
	
    /**
     * 查询关系用户或部门id
     * @param siteId
     * @return
     */
    public List<ExemptBean> queryRelatedToUsersOrOrgs(@Param("siteId")String siteId, @Param("menuId")String menuId);
    
    /**
     * 
     * @description:批量删除
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @param List
     * @return int
     */
    public int batchDeleteExempt(List<Object> exemptDelList);
    
    /**
     * 
     * @description:批量插入
     * @author: zhuw
     * @createDate: 2016年1月25日
     * @param List
     * @return int
     */
    public int batchInsertExempt(List<Object> exemptAddList);
}
