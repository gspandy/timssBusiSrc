package com.timss.asset.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwModelBean;
import com.yudean.itc.dto.Page;

/**
 * 硬件类型维护的dao
 * @author 890147
 *
 */
public interface HwModelDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	List<HwModelBean> queryList(Page<HwModelBean> page);
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	HwModelBean queryDetail(@Param("id")String id);
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(HwModelBean bean);
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(HwModelBean bean);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(@Param("id")String id);
	
	/**
	 * 查询使用了该硬件类型的硬件台账
	 * @param id
	 * @return
	 */
	List<HwLedgerDeviceBean> queryDeviceByModelId(@Param("id")String id,@Param("type")String type);

	/**
	 * 查询指定站点、类型和名称的硬件类型
	 * @param modelType
	 * @param modelName
	 * @param siteId
	 * @return
	 */
	HwModelBean queryHwModelByNameAndType(@Param("type")String modelType, @Param("name")String modelName,
			@Param("siteId")String siteId);

	/**
	 * 
	 * @description:通过硬件类型查询型号
	 * @author: fengzt
	 * @createDate: 2014年12月18日
	 * @param pageVo
	 * @return:
	 */
	List<HwModelBean> queryHwModelByType(Page<HashMap<?, ?>> pageVo);

	/**
	 * 
	 * @description:通过硬件类型查询型号-高级查询
	 * @author: fengzt
	 * @createDate: 2014年12月18日
	 * @param pageVo
	 * @return:List<HwModelBean> 
	 */
        List<HwModelBean> queryHwModelByTypeSearch(Page<HashMap<?, ?>> pageVo);
}