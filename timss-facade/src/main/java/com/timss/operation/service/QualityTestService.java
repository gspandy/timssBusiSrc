package com.timss.operation.service;

import com.timss.operation.bean.QualityTestBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 质检日志的service
 * @author 890147
 *
 */
public interface QualityTestService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<QualityTestBean> queryList(Page<QualityTestBean> page) throws Exception;

	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	QualityTestBean queryDetail(String id) throws Exception;  
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(@CUDTarget QualityTestBean bean) throws Exception;
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(@CUDTarget QualityTestBean bean) throws Exception;
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;
	
}