package com.timss.operation.service.core;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Rules;
import com.timss.operation.dao.RulesDao;
import com.timss.operation.service.RulesService;
import com.timss.operation.vo.RulesFormVo;
import com.yudean.itc.dto.Page;

/**
 * @title: 行列规则service
 * @description: {desc}
 * @company: gdyd
 * @className: RulesServiceImpl.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Service("rulesService")
public class RulesServiceImpl implements RulesService {

    @Autowired
    private RulesDao rulesDao;

    public RulesDao getRulesDao() {
        return rulesDao;
    }

    public void setRulesDao(RulesDao rulesDao) {

        this.rulesDao = rulesDao;
    }

    /**
     * @description:插入一条行列规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Rules 其中id为自增，不需要设置
     * @return:Rules
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Rules insertRules(Rules rules) {
        rules.setAvailable( "Y" );
        rulesDao.insertRules( rules );
        return rules;
    }

    /**
     * @description:更新行列规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Rules:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateRules(Rules rules) {
        return rulesDao.updateRules( rules );
    }

    /**
     * @description:通过Id拿到行列规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id
     * @return:Rules
     */
    public Rules queryRulesById(int id) {

        return rulesDao.queryRulesById( id );
    }

    /**
     * @description:通过ID 删除 Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteRulesById(int id) {

        return rulesDao.deleteRulesById( id );
    }

    /**
     * @description:更新Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param RulesMap:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRulesByMap(HashMap<?, ?> rulesMap) {

    }

    /**
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param list:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchInsertRules(List<Rules> list) {

    }

    /**
     * @description:Rules 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return:
     */
    public List<Rules> queryRulesByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );
        
        return rulesDao.queryRulesByPage( page );
    }

    /**
     * @description:Rules 分页 (返回hashmap)
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryRulesMapByPage(Page<HashMap<?, ?>> page) {
        return null;
    }

    /**
     * @description:拿出所有行列规则Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @return:
     */
    public List<Rules> queryAllRules() {
        return rulesDao.queryAllRules();
    }

    /**
     * @description:通过hashmap 拿到List
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @return:List<Rules>
     */
    public List<Rules> queryRulesByMap(HashMap<?, ?> map) {
        return null;
    }

    /**
     * @description:行列规则列表 高级搜索
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Rules>
     */
    public List<Rules> queryRulesBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "id", map.get( "id" ) );
        page.setParameter( "num", map.get( "num" ) );
        page.setParameter( "name", map.get( "name" ) );

        page.setParameter( "siteId", map.get( "siteId" ) );
        page.setParameter( "available", map.get( "available" ) );
        page.setParameter( "changeLimit", map.get( "changeLimit" ) );
        page.setParameter( "pollSequence", map.get( "pollSequence" ) );
        
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );

        List<Rules> rulesList = rulesDao.queryRulesBySearch( page );
        return rulesList;
    }

    /**
     * 
     * @description:插入by formVo
     * @author: fengzt
     * @createDate: 2014年6月26日
     * @param rulesFormVo
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public RulesFormVo insertRulesByFormVo(RulesFormVo rulesFormVo) {
        rulesFormVo.setAvailable( "Y" );
        rulesDao.insertRulesByFormVo( rulesFormVo );
        return rulesFormVo;
    }

    /**
     * 
     * @description:更新 by formVo
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param rulesFormVo
     * @return:int
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public int updateRulesByFormVo(RulesFormVo rulesFormVo) {
        return rulesDao.updateRulesByFormVo( rulesFormVo );
    }

}
