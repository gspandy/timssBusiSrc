package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.Shift;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 班次Service
 * @description: 
 * @company: gdyd
 * @className: ShiftService.java
 * @author: huanglw
 * @createDate: 2014年6月11日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface ShiftService {

    /**
     * 
     * @description:新建一个班次
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param Shift  其中id为自增，不需要设置
     * @return:Shift
     */
    public Shift insertShift(Shift Shift );
    
    /**
     * 
     * @description:更新班次表
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param Shift:
     * @return int
     */
    public int updateShift(Shift Shift);
    
    /**
     * 
     * @description:通过Id拿到班次表
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param id
     * @return:Shift
     */
    public Shift queryShiftById(int id);
    
    /**
     * 
     * @description:通过ID 删除 Shift
     * @author:huanglw
     * @createDate:2014年6月11日
     * @param id:int
     * @return int
     */
    public int deleteShiftById(int id);

    /**
     * 
     * @description:通过Map更新Shift
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param ShiftMap:HashMap
     */
    public void updateShiftByMap(HashMap<?, ?> ShiftMap);
    
    /**
     * 
     * @description:批量插入
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param list:保存Shift的List
     */
    public void batchInsertShift(List<Shift> list);   
    
    /**
     * 
     * @description:Shift 分页
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param page:Page<HashMap<?, ?>>
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:Shift 分页 (返回hashmap)
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param page:Page<HashMap<?, ?>>
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryShiftMapByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:拿出所有Shift
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @return:List<Shift>
     */
    public List<Shift> queryAllShift();
    
    /**
     * 
     * @description:通过hashmap 拿到List
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param map:HashMap<?, ?>
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByMap(HashMap<?, ?> map);

    /**
     * 
     * @description:班次列表 高级搜索
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param map
     * @param pageVo
     * @return:
     */
    public List<Shift> queryShiftBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过岗位ID拿班次
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param stationId
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByStationId(String stationId);

    /**
     * 
     * @description:通过岗位拿到排序值
     * @author: fengzt
     * @createDate: 2014年7月29日
     * @param stationId
     * @return:int
     */
    public int querySortTypeByStationId(String stationId);
}
