package com.timss.inventory.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatBudget;
import com.timss.inventory.dao.InvMatBudgetDao;
import com.timss.inventory.service.InvMatBudgetService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatBudgetServiceImpl.java
 * @author: 890166
 * @createDate: 2014-9-18
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvMatBudgetServiceImpl")
public class InvMatBudgetServiceImpl implements InvMatBudgetService {

    @Autowired
    private InvMatBudgetDao invMatBudgetDao;

    /**
     * @description:查询每月预算成本
     * @author: 890166
     * @createDate: 2014-9-18
     * @param map
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvMatBudget> queryInvMatBudget(String deptId, String setDate, String siteId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "deptid", deptId );
        map.put( "setdate", setDate );
        map.put( "siteId", siteId );
        return invMatBudgetDao.queryInvMatBudget( map );
    }

    /**
     * @description:插入每月成本预算
     * @author: 890166
     * @createDate: 2014-9-18
     * @param invMatBudget
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public int insertInvMatBudget(InvMatBudget invMatBudget) throws Exception {
        return invMatBudgetDao.insertInvMatBudget( invMatBudget );
    }

    /**
     * @description:更新每月成本预算
     * @author: 890166
     * @createDate: 2014-9-18
     * @param invBinInfo
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public int updateInvMatBudget(InvMatBudget invMatBudget) throws Exception {
        return invMatBudgetDao.updateInvMatBudget( invMatBudget );
    }

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
    @Override
    public Double queryBudgetGrandTotalLastMonth(String deptId, String lastMonth, String siteId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "deptId", deptId );
        map.put( "lastMonth", lastMonth );
        map.put( "siteId", siteId );
        return invMatBudgetDao.queryBudgetGrandTotalLastMonth( map );
    }
}
