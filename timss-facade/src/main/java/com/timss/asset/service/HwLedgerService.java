package com.timss.asset.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwLedgerEqptBean;
import com.timss.asset.bean.HwLedgerServerBean;
import com.timss.asset.bean.HwLedgerVMBean;
import com.timss.asset.vo.MultiSearchVo;
import com.yudean.itc.dto.Page;

/**
 * @title:硬件台账service
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerService.java
 * @author: fengzt
 * @createDate: 2014年11月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface HwLedgerService {
    
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
     * @description:插入硬件台账
     * @author: fengzt
     * @createDate: 2014年11月21日
     * @param hwLedgerBean
     * @return:
     */
    int insertHwLedger(HwLedgerBean hwLedgerBean);
    
    /**
     * 
     * @description:通过站点来查找硬件台账
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @return:HwLedgerBean
     */
    HwLedgerBean queryHwLedgerBySite();

    /**
     * 
     * @description:更新硬件台账
     * @author: fengzt
     * @createDate: 2014年8月28日
     * @param vo
     * @return:int
     */
    int updateHwLedger(HwLedgerBean vo);

    /**
     * 
     * @description:插入或者更新 硬件台账
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwType
     * @param formData
     * @return:Map<String, Object>
     */
    Map<String, Object> insertOrUpdateHwLedger(String hwType, String formData);
    
    /**
     * 
     * @description:删除硬件台账
     * @author: fengzt
     * @createDate: 2014年11月25日
     * @param hwType
     * @param hwId 
     * @return:Map<String, Object>
     */
    Map<String, Object> deleteHwLedger(String hwType, String hwId);

    /**
     * 
     * @description:插入或者更新服务器
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwLedgerBean
     * @param serverBean
     * @param deviceBean
     * @param eqptBean 
     * @return: Map<String, Object>
     */
    Map<String, Object> insertOrUpdateServiceHw(HwLedgerBean hwLedgerBean, HwLedgerServerBean serverBean,
            HwLedgerDeviceBean deviceBean, HwLedgerEqptBean eqptBean);

    /**
     * 
     * @description:查询硬件类型iHint
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param kw
     * @param modelType
     * @return: List<Map<String, Object>> 
     */
    List<Map<String, Object>> searchHwModelHint(String kw, String modelType);

    /**
     * 
     * @description:通过HwId查询服务器相关
     * @author: fengzt
     * @createDate: 2014年11月26日
     * @param hwId
     * @return:Map
     */
    Map<String, Object> queryHwLedgerServiceById(String hwId);

    /**
     * 
     * @description:插入或者更新虚机
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwLedgerBean
     * @param vmBean
     * @param deviceBean
     * @param eqptBean 
     * @return:Map<String, Object>
     */
    Map<String, Object> insertOrUpdateVMHw(HwLedgerBean hwLedgerBean, HwLedgerVMBean vmBean,
            HwLedgerDeviceBean deviceBean, HwLedgerEqptBean eqptBean);

    /**
     * 
     * @description:通过ID查询虚机相关
     * @author: fengzt
     * @createDate: 2014年11月27日
     * @param hwId
     * @return:Map<String, Object> 
     */
    Map<String, Object> queryHwLedgerVMById(String hwId);

    /**
     * 
     * @description:检查硬件台账名称在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 2014年11月28日
     * @param hwName
     * @param hwId 
     * @return:List<HwLedgerBean>
     */
    List<HwLedgerBean> queryCheckHwLedgerName(String hwName, String hwId);

    /**
     * 
     * @description:查找机房、机柜、物理地点
     * @author: fengzt
     * @createDate: 2014年11月29日
     * @param id
     * @return:Map<String, Object>
     */
    Map<String, Object> queryHwLedgerByHwTypeAndId(String id);

    /**
     * 
     * @description:通过hwId查找HwLedgerDevice
     * @author: fengzt
     * @createDate: 2014年12月1日
     * @param hwId
     * @return:HwLedgerDeviceBean
     */
    HwLedgerDeviceBean queryHwLedgerDeviceById(String hwId);

    /**
     * 
     * @description:树上DIV,当鼠标放在书上服务器或者虚机
     * @author: fengzt
     * @createDate: 2014年12月2日
     * @param hwId
     * @param hwType
     * @return:Map<String, Object>
     */
    Map<String, Object> queryHwLedgerByTypeAndId(String hwId, String hwType);

    /**
     * 
     * @description:硬件台账多条件查询
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param vo
     * @param pageVo
     * @return:List<MultiSearchVo>
     */
    List<MultiSearchVo> queryHwLedgerMulti(MultiSearchVo vo, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:硬件台账多条件查询--结果页查询
     * @author: fengzt
     * @createDate: 2015年1月4日
     * @param vo
     * @param searchVo
     * @param pageVo
     * @return:List<MultiSearchVo>
     */
    List<MultiSearchVo> queryHwLedgerMultiBySearch(MultiSearchVo vo, MultiSearchVo searchVo,
            Page<HashMap<?, ?>> pageVo);
}