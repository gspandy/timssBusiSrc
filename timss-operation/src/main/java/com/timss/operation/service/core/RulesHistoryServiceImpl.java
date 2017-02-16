package com.timss.operation.service.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.RulesHistory;
import com.timss.operation.dao.RulesHistoryDao;
import com.timss.operation.service.RulesHistoryService;
import com.timss.operation.vo.RulesHistoryVo;

/**
 * 
 * @title: 规则历史
 * @description: {desc}
 * @company: gdyd
 * @className: RulesHistoryServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("rulesHistoryService")
@Transactional(propagation=Propagation.SUPPORTS)
public class RulesHistoryServiceImpl implements RulesHistoryService {
    
    @Autowired
    private RulesHistoryDao rulesHistoryDao;
    

    public RulesHistoryDao getRulesHistoryDao() {
        return rulesHistoryDao;
    }

    public void setRulesHistoryDao(RulesHistoryDao rulesHistoryDao) {
        this.rulesHistoryDao = rulesHistoryDao;
    }

    /**
     * 
     * @description:生成日历 插入规则历史
     * @author: fengzt
     * @createDate: 2014年6月13日
     * @param rulesHistory
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertRulesHistory(RulesHistory rulesHistory) {
        rulesHistoryDao.insertRulesHistory( rulesHistory );
        
        if( rulesHistory.getId() > 0 ){
            return 1;
        }
        
        return 0;
    }

    /**
     * 
     * @description:确定dategrid生成日期的时候下标
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param map
     * @return:RulesHistoryVo
     */
    @Override
    public List<RulesHistoryVo> queryRulesHistoryByMap(Map<String, Object> map) {
        return rulesHistoryDao.queryRulesHistoryByMap( map );
    }
    
    
}
