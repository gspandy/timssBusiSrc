package com.timss.asset.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.asset.bean.HwModelBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 硬件类型维护的service
 * @author 890147
 *
 */
public interface HwModelService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<HwModelBean> queryList(Page<HwModelBean> page) throws Exception;

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	HwModelBean queryDetail(String id) throws Exception;  
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(@CUDTarget HwModelBean bean) throws Exception;
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(@CUDTarget HwModelBean bean) throws Exception;
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;

	/**
	 * 查询指定站点、类型和名称的硬件类型
	 * @param modelType
	 * @param modelName
	 * @param siteId
	 * @return
	 */
	HwModelBean queryHwModelByNameAndType(String modelType, String modelName,
			String siteId) throws Exception;

	/**
	 * 
	 * @description:通过硬件类型查询型号
	 * @author: fengzt
	 * @createDate: 2014年12月18日
	 * @param pageVo
	 * @param modelType 
	 * @return:List<HwModelBean>
	 */
        List<HwModelBean> queryHwModelByType(Page<HashMap<?, ?>> pageVo, String modelType);

        /**
         * 
         * @description:通过硬件类型查询型号-高级查询
         * @author: fengzt
         * @createDate: 2014年12月18日
         * @param map
         * @param pageVo
         * @param modelType
         * @return:
         */
        List<HwModelBean> queryHwModelByTypeSearch(Map<String, Object> map, Page<HashMap<?, ?>> pageVo, String modelType);
}