package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatBudget;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatBudgetDao.java
 * @author: 890166
 * @createDate: 2014-9-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatBudgetDao {

    /**
     * @description:查询每月预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @param map
     * @return
     * @throws Exception:
     */
    List<InvMatBudget> queryInvMatBudget(Map<String, Object> map) throws Exception;

    /**
     * @description:查询直到上月为止的的累计预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @param deptId
     * @param lastMonth
     * @param siteId
     * @return
     * @throws Exception:
     */
    Double queryBudgetGrandTotalLastMonth(Map<String, Object> map) throws Exception;

    /**
     * @description:插入每月成本预算
     * @author: 890166
     * @createDate: 2014-9-18
     * @param invMatBudget
     * @return
     * @throws Exception:
     */
    int insertInvMatBudget(InvMatBudget invMatBudget) throws Exception;

    /**
     * @description:更新每月成本预算
     * @author: 890166
     * @createDate: 2014-9-18
     * @param invBinInfo
     * @return
     * @throws Exception:
     */
    int updateInvMatBudget(InvMatBudget invMatBudget) throws Exception;
}
