package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.vo.RulesDetailForListVo;
import com.timss.operation.vo.RulesFormVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 排班规则详情 service
 * @description: {desc}
 * @company: gdyd
 * @className: RulesDetailService.java
 * @author: fengzt
 * @createDate: 2014年6月11日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface RulesDetailService {

    /**
     * 
     * @description:批量插入排版规则详情
     * @author: fengzt
     * @createDate: 2014年6月11日
     * @param maps
     * @param rulesId
     * @param deptId
     * @param rulesDetailName
     * @return:int
     */
    public Map<String, Object> batchInsertRulesDetail(List<HashMap<String, Object>> maps, int rulesId, String stationId, String rulesDetailName);

    /**
     * 
     * @description:高级查询
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param search
     * @param pageVo
     * @return:List<RulesDetailForListVo>
     */
    public List<RulesDetailForListVo> queryRulesDetailBySearch(String search, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:获取所有RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param pageVo
     * @return:List<RulesDetailForListVo>
     */
    public List<RulesDetailForListVo> queryAllRulesDetailByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过UUID查找RulesDetail
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:List<HashMap<String, Object>>
     */
    public List<HashMap<String, Object>> queryRulesDetailByUuid(String uuid);

    /**
     * 
     * @description:通过uuid查找staionid
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:String
     */
    public String queryStationIdByUuid(String uuid);

    /**
     * 
     * @description:更新
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param rulesFormVo
     * @param maps
     * @param uuid
     * @return:int
     */
    public Map<String, Object> updateRuleDetail(RulesFormVo rulesFormVo, List<HashMap<String, Object>> maps, String uuid );
    
    /**
     * 
     * @description:通过uuid删除datagrid
     * @author: fengzt
     * @createDate: 2014年6月27日
     * @param uuid
     * @return:int
     */
    public int deleteRulesDetail(String uuid);

   /**
    * 
    * @description:删除排班规则详情和行列表
    * @author: fengzt
    * @createDate: 2014年7月2日
    * @param id
    * @param uuid
    * @return:boolean
    */
    public boolean deleteRulesDetailByUuid(int id, String uuid);
    
}
