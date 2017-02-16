package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.RulesDetail;
import com.timss.operation.vo.RulesDetailForListVo;
import com.timss.operation.vo.RulesDetailVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: RulesDetailMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: RulesDetailMapper.java
 * @author: fengzt
 * @createDate: 2014年6月4日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface RulesDetailDao {
        
    /**
     * 
     * @description:插入一条排班规则
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param RulesDetail  其中id为自增，不需要设置
     * @return:RulesDetail
     */
    public void insertRulesDetail(RulesDetail rulesDetail );
    
    /**
     * 
     * @description:更新值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param RulesDetail:
     * @return int 更新个数
     */
    public int updateRulesDetail(RulesDetail rulesDetail);
    
    /**
     * 
     * @description:通过Id拿到值别表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id
     * @return:RulesDetail
     */
    public RulesDetail queryRulesDetailById(int id);
    
    /**
     * 
     * @description:通过ID 删除 RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param id:
     * @return 
     */
    public int deleteRulesDetailById(int id);

    /**
     * 
     * @description:更新RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param RulesDetailMap:
     */
    public void updateRulesDetailByMap(HashMap<?, ?> rulesDetailMap);
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param list:
     */
    public void batchInsertRulesDetail(List<RulesDetail> list);   
    
    /**
     * 
     * @description:RulesDetail 分页
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return:
     */
    public List<RulesDetailForListVo> queryRulesDetailByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询值别列表
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<RulesDetail>
     */
    public List<RulesDetail> queryRulesDetailBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:RulesDetail 分页 (返回hashmap)
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryRulesDetailMapByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:拿出所有值别RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @return:
     */
    public List<RulesDetail> queryAllRulesDetail();
    
    /**
     * 
     * @description:通过hashmap 拿到List
     * @author: fengzt
     * @createDate: 2014年6月4日
     * @param map
     * @return:List<RulesDetail>
     */
    public List<RulesDetail> queryRulesDetailByMap(HashMap<?, ?> map);
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param rulesDetails
     */
    public int batchInsert( Map<String, Object> rulesDetails );

    /**
     * 
     * @description:通过UUID查找
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:List<RulesDetailVo> 
     */
    public List<RulesDetailVo> queryRulesDetailByUuid(String uuid);

    /**
     * 
     * @description:通过uuid查找stationid
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:List<RulesDetailVo>
     */
    public List<RulesDetailVo> queryStationIdByUuid(String uuid);

    /**
     * 
     * @description:通过uuid删除datagrid
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:int
     */
    public int deleteRulesDetailByUuid(String uuid);

}
