package com.timss.asset.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.asset.bean.HwLedgerAddressBean;
import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerCabinetBean;
import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwLedgerRoomBean;
import com.timss.asset.bean.HwLedgerServerBean;
import com.timss.asset.bean.HwLedgerVMBean;
import com.timss.asset.vo.MultiSearchVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 硬件台账
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerDao.java
 * @author: fengzt
 * @createDate: 2014年11月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface HwLedgerDao {

    /**
     * 
     * @description:插入硬件台账
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @param hwLedgerBean
     * @return:int
     */
    int insertHwLedger(HwLedgerBean hwLedgerBean);

    /**
     * 
     * @description:通过站点查找根节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param siteId
     * @return:String
     */
    String queryHwLedgerRootIdBySite(String siteId);

    /**
     * 
     * @description:通过ID查找硬件台账
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:HwLedgerBean
     */
    HwLedgerBean queryHwLedgerDetail(String id);

    /**
     * 
     * @description:通过id找到子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<HwLedgerBean>
     */
    List<HwLedgerBean> queryHwLedgerChildren(String id);

    /**
     * 
     * @description:用于搜索框的查询
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param map
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> searchHwLedgerHint(Map<String, Object> map);

    /**
     * 
     * @description:搜索子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<String>
     */
    List<String> searchHintHwLedgerParentIds(String id);

    /**
     * 
     * @description:查询根节点信息
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param siteId
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> queryHwLedgerByRoot(String siteId);

    /**
     * 
     * @description:更新硬件台账
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwLedgerBean
     * @return:int
     */
    int updateHwLedger(HwLedgerBean hwLedgerBean);

    /**
     * 
     * @description:删除硬件台账及其子节点
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwId
     * @return:int
     */
    int deleteHwLedger(String hwId);

    /**
     * 
     * @description:删除服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:int
     */
    int delelteHwLedgerServer(String hwId);

    /**
     * 
     * @description:设备
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:int
     */
    int delelteHwLedgerDevice(String hwId);

    /**
     * 
     * @description:插入服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param serverBean
     * @return:int
     */
    int insertHwLedgerServer(HwLedgerServerBean serverBean);

    /**
     * 
     * @description:插入设备
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param deviceBean
     * @return:int 
     */
    int insertHwLedgerDevice(HwLedgerDeviceBean deviceBean);

    /**
     * 
     * @description:查询硬件类型iHint
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param params
     * @return:
     */
    List<Map<String, Object>> searchHwModelHint(Map<String, Object> params);

    /**
     * 
     * @description:查询设备
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:HwLedgerDeviceBean
     */
    HwLedgerDeviceBean queryHwLedgerDeviceById(String hwId);

    /**
     * 
     * @description:通过hwId查找服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:HwLedgerServerBean
     */
    HwLedgerServerBean queryHwLedgerServerById(String hwId);

    /**
     * 
     * @description:拿到站点下所有硬件 
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param siteId
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> queryHwModelNameBySiteId(String siteId);

    /**
     * 
     * @description:删除物理地址
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:int
     */
    int deleteAddress(String hwId);

    /**
     * 
     * @description:删除机房
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:int
     */
    int deleteRoom(String hwId);

    /**
     * 
     * @description:删除硬件台账的机柜
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:
     */
    int deleteCabinet(String hwId);

    /**
     * 
     * @description:插入物理地址
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param addressBean
     * @return:int
     */
    int insertAddress(HwLedgerAddressBean addressBean);

    /**
     * 
     * @description:机房
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param roomBean
     * @return:int
     */
    int insertRoom(HwLedgerRoomBean roomBean);

    /**
     * 
     * @description:插入机柜
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param cabinetBean
     * @return:int
     */
    int insertCabinet(HwLedgerCabinetBean cabinetBean);

    /**
     * 
     * @description:删除虚机By hwId
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:int
     */
    int delelteHwLedgerVM(String hwId);

    /**
     * 
     * @description:插入虚机
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param vmBean
     * @return:int
     */
    int insertHwLedgerVM(HwLedgerVMBean vmBean);

    /**
     * 
     * @description:通过hwId 查询虚机信息
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:HwLedgerVMBean
     */
    HwLedgerVMBean queryHwLedgerVMById(String hwId);

    /**
     * 
     * @description:检查硬件台账名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2014年11月28日
     * @param params
     * @return:List<HwLedgerBean>
     */
    List<HwLedgerBean> queryCheckHwLedgerName(Map<String, Object> params);

    /**
     * 
     * @description:通过HwId查询物理地址
     * @author: fengzt
     * @createDate: 2014年11月29日
     * @param hwId
     * @return:HwLedgerAddressBean
     */
    HwLedgerAddressBean queryHwAddressByHwId(String hwId);

    /**
     * 
     * @description:通过HwId查询机房
     * @author: fengzt
     * @createDate: 2014年11月29日
     * @param hwId
     * @return:HwLedgerRoomBean
     */
    HwLedgerRoomBean queryHwRoomByHwId(String hwId);

    /**
     * 
     * @description:通过HwId查询机柜
     * @author: fengzt
     * @createDate: 2014年11月29日
     * @param hwId
     * @return:HwLedgerCabinetBean
     */
    HwLedgerCabinetBean queryHwCabinetByHwId(String hwId);

    /**
     * 
     * @description:通过Id查询HwModel
     * @author: fengzt
     * @param params 
     * @createDate: 2014年12月3日
     * @return: List<Map<String, Object>>
     */
    List<Map<String, Object>> queryHwModelNameById(Map<String, Object> params);

    /**
     * 
     * @description:更新设备
     * @author: fengzt
     * @createDate: 2014年12月4日
     * @param deviceBean
     * @return:int
     */
    int updateHwLedgerDevice(HwLedgerDeviceBean deviceBean);

    /**
     * 
     * @description:更新服务器
     * @author: fengzt
     * @createDate: 2014年12月4日
     * @param serverBean
     * @return:int
     */
    int updateHwLedgerServer(HwLedgerServerBean serverBean);

    /**
     * 
     * @description:更新虚机
     * @author: fengzt
     * @createDate: 2014年12月4日
     * @param vmBean
     * @return:int
     */
    int updateHwLedgerVM(HwLedgerVMBean vmBean);

    /**
     * 
     * @description:拖动硬件台账树节点
     * @author: fengzt
     * @createDate: 2014年12月15日
     * @param map
     * @return:int
     */
    int updateDropHwlTreeNode(Map<String, Object> map);

    /**
     * 
     * @description:硬件台账多条件查询
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param pageVo
     * @return: List<MultiSearchVo>
     */
    List<MultiSearchVo> queryHwLedgerMulti(Page<HashMap<?, ?>> pageVo);

}
