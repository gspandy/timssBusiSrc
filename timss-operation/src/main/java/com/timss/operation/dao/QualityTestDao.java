package com.timss.operation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.QualityTestBean;
import com.yudean.itc.dto.Page;

/**
 * 质检日志的dao
 * @author 890147
 *
 */
public interface QualityTestDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	List<QualityTestBean> queryList(Page<QualityTestBean> page);
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	QualityTestBean queryDetail(@Param("qtId")String qtId);
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(QualityTestBean bean);
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(QualityTestBean bean);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(@Param("qtId")String qtId);
	
}