package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.Rules;
import com.timss.operation.vo.RulesFormVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: RulesDao 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: RulesMapper.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface RulesDao {
	
    /**
     * 
     * @description:插入一条行列规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Rules  其中id为自增，不需要设置
     * @return:Rules
     */
    public void insertRules(Rules rules );
    
    /**
     * 
     * @description:更新值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param Rules:
     * @return int 更新个数
     */
    public int updateRules(Rules rules);
    
    /**
     * 
     * @description:通过Id拿到值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id
     * @return:Rules
     */
    public Rules queryRulesById(int id);
    
    /**
     * 
     * @description:通过ID 删除 Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id:
     * @return 
     */
    public int deleteRulesById(int id);

    /**
     * 
     * @description:更新Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param RulesMap:
     */
    public void updateRulesByMap(HashMap<?, ?> rulesMap);
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param list:
     */
    public void batchInsertRules(List<Rules> list);   
    
    /**
     * 
     * @description:Rules 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return:
     */
    public List<Rules> queryRulesByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询值别列表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<Rules>
     */
    public List<Rules> queryRulesBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:Rules 分页 (返回hashmap)
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryRulesMapByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:拿出所有值别Rules
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @return:
     */
    public List<Rules> queryAllRules();
    
    /**
     * 
     * @description:通过hashmap 拿到List
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @return:List<Rules>
     */
    public List<Rules> queryRulesByMap(HashMap<?, ?> map);

    /**
     * 
     * @description:通过FormVo insert
     * @author: fengzt
     * @createDate: 2014年6月26日
     * @param rulesFormVo:
     */
    public void insertRulesByFormVo(RulesFormVo rulesFormVo);

    /**
     * 
     * @description:更新 by formVo todo
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param rulesFormVo
     * @return:int
     */
    public int updateRulesByFormVo(RulesFormVo rulesFormVo);
}
