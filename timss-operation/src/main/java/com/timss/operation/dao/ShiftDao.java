package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.Shift;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: ShiftMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: ShiftMapper.java
 * @author: huanglw
 * @createDate: 2014年6月10日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface ShiftDao {
	
    /**
     * 
     * @description:新建一个班次
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param shift  其中id为自增，不需要设置
     */
    public void insertShift(Shift shift );
    
    /**
     * 
     * @description:更新班次表
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param Shift:
     * @return int 更新个数
     */
    public int updateShift(Shift Shift);
    
    /**
     * 
     * @description:通过Id拿到班次表
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param id
     * @return:Shift
     */
    public Shift queryShiftById(int id);
    
    /**
     * 
     * @description:通过ID 删除 shift
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param id:
     * @return 
     */
    public int deleteShiftById(int id);

    /**
     * 
     * @description:更新shift
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param ShiftMap:
     */
    public void updateShiftByMap(HashMap<?, ?> ShiftMap);
    
    /**
     * 
     * @description:批量插入
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param list:
     */
    public void batchInsertShift(List<Shift> list);   
    
    /**
     * 
     * @description:shift 分页
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param page
     * @return:
     */
    public List<Shift> queryShiftByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询班次列表
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param page
     * @return: List<Shift>
     */
    public List<Shift> queryShiftBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:shift 分页 (返回hashmap)
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryShiftMapByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:拿出所有班次shift
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @return:
     */
    public List<Shift> queryAllShift();
    
    /**
     * 
     * @description:通过hashmap 拿到班次List
     * @author: huanglw
     * @createDate: 2014年6月10日
     * @param map
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByMap(HashMap<?, ?> map);

    /**
     * 
     * @description:通过岗位ID拿到班次
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
    public String querySortTypeByStationId(String stationId);
}
