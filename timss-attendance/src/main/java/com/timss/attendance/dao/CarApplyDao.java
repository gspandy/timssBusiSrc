package com.timss.attendance.dao;

import java.util.List;

import com.timss.attendance.bean.CarApplyBean;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * @description:用车申请
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public interface CarApplyDao {
	
	/**插入用车申请
	 * @param bean
	 * @return
	 */
	int insertCarApply(CarApplyBean bean);
	
	/**修改
	 * @param bean
	 * @return
	 */
	int updateCarApply(CarApplyBean bean);
	
	/**删除
	 * @param id
	 * @return 
	 */
	int deleteCarApply(String id);
	
	/**列表查询
	 * @param page
	 * @return
	 */
	List<CarApplyBean> queryList(Page<CarApplyBean> page);
	
	/**根据id查找
	 * @param caId
	 * @return
	 */
	CarApplyBean queryById(String caId);
}
