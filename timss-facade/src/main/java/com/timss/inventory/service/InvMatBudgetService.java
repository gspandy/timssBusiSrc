package com.timss.inventory.service;

import java.util.List;

import com.timss.inventory.bean.InvMatBudget;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatBudgetService.java
 * @author: 890166
 * @createDate: 2014-9-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatBudgetService {

    /**
     * @description:查询每月预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @param map
     * @return
     * @throws Exception:
     */
    List<InvMatBudget> queryInvMatBudget(String deptId, String setDate, String siteId) throws Exception;

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
    Double queryBudgetGrandTotalLastMonth(String deptId, String lastMonth, String siteId) throws Exception;

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
