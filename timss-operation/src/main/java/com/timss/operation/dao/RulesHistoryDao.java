package com.timss.operation.dao;

import java.util.List;
import java.util.Map;

import com.timss.operation.bean.RulesHistory;
import com.timss.operation.vo.RulesHistoryVo;

/**
 * 
 * @title: 规则历史
 * @description: {desc}
 * @company: gdyd
 * @className: RulesHistoryMapper.java
 * @author: fengzt
 * @createDate: 2014年6月13日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface RulesHistoryDao {

    /**
     * 
     * @description:插入规则历史
     * @author: fengzt
     * @createDate: 2014年6月13日
     * @param rulesHistory
     */
    public void insertRulesHistory( RulesHistory rulesHistory );

    /**
     * 
     * @description:确定dategrid生成日期的时候下标
     * @author: fengzt
     * @createDate: 2014年7月1日
     * @param map
     * @return:RulesHistoryVo
     */
    public List<RulesHistoryVo> queryRulesHistoryByMap(Map<String, Object> map);
}
