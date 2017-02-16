package com.timss.operation.service.core;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Shift;
import com.timss.operation.dao.ShiftDao;
import com.timss.operation.service.ShiftService;
import com.yudean.itc.dto.Page;

/**
 * @title: 班次service Implements
 * @description: 
 * @company: gdyd
 * @className: ShiftServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年6月11日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("shiftService")
@Transactional(propagation=Propagation.SUPPORTS)
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    private ShiftDao shiftDao;

    public ShiftDao getShiftDao() {
        return shiftDao;
    }

    public void setShiftDao(ShiftDao shiftDao) {

        this.shiftDao = shiftDao;
    }

    /**
     * @description:新建一个班次
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param shift 其中id为自增，不需要设置
     * @return:Shift
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Shift insertShift(Shift shift) {
        shift.setState( "Y" );
        shiftDao.insertShift( shift );
        return shift;
    }

    /**
     * @description:更新班次表
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param shift:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateShift(Shift shift) {
        
        return shiftDao.updateShift( shift );
    }

    /**
     * @description:通过Id拿到班次表
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param id
     * @return:Shift
     */
    public Shift queryShiftById(int id) {

        return shiftDao.queryShiftById( id );
    }

    /**
     * @description:通过ID 删除 shift
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteShiftById(int id) {

        return shiftDao.deleteShiftById( id );
    }

    /**
     * @description:更新shift
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param ShiftMap:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateShiftByMap(HashMap<?, ?> ShiftMap) {

    }

    /**
     * @description:批量插入
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param list:
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchInsertShift(List<Shift> list) {

    }

    /**
     * @description:shift 分页
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param page
     * @return:
     */
    public List<Shift> queryShiftByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "stationId,sortType" );
        page.setSortOrder( "asc" );
        
        return shiftDao.queryShiftByPage( page );
    }

    /**
     * @description:shift 分页 (返回hashmap)
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param page
     * @return: List<HashMap<?, ?>>
     */
    public List<HashMap<?, ?>> queryShiftMapByPage(Page<HashMap<?, ?>> page) {
        return null;
    }

    /**
     * @description:拿出所有班次shift
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @return:
     */
    public List<Shift> queryAllShift() {
        return shiftDao.queryAllShift();
    }

    /**
     * @description:通过hashmap 拿到List
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param map
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByMap(HashMap<?, ?> map) {
        return null;
    }

    /**
     * @description:班次列表 高级搜索
     * @author: huanglw
     * @createDate: 2014年6月11日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Shift>
     */
    public List<Shift> queryShiftBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "id", map.get( "id" ) );
        page.setParameter( "deptId", map.get( "deptId" ) );
        page.setParameter( "name", map.get( "name" ) );
        page.setParameter( "num", map.get( "num" ) );
        page.setParameter( "sortType", map.get( "sortType" ) );
        page.setParameter( "startTime", map.get( "startTime" ) );
        page.setParameter( "longTime", map.get( "longTime" ) );
        page.setParameter( "abbName", map.get( "abbName" ) );
        page.setParameter( "isProduct", map.get( "isProduct" ) );
        page.setParameter( "stationId", map.get( "stationId" ) );
        page.setParameter( "type", map.get( "type" ) );
        
        page.setSortKey( "stationId,sortType" );
        page.setSortOrder( "asc" );

        List<Shift> shiftList = shiftDao.queryShiftBySearch( page );
        return shiftList;
    }

    /**
     * 
     * @description:通过岗位ID拿班次
     * @author: fengzt
     * @createDate: 2014年6月12日
     * @param stationId
     * @return:List<Shift>
     */
    public List<Shift> queryShiftByStationId(String stationId) {
        return shiftDao.queryShiftByStationId( stationId );
    }

    /**
     * 
     * @description:通过岗位拿到排序值
     * @author: fengzt
     * @createDate: 2014年7月29日
     * @param stationId
     * @return:int
     */
    @Override
    public int querySortTypeByStationId(String stationId) {
        String sortType = shiftDao.querySortTypeByStationId( stationId );
        
        if( StringUtils.isBlank( sortType ) ){
            return 100;
        }else{
            return Integer.parseInt( sortType ) + 100;
        }
    }

}
