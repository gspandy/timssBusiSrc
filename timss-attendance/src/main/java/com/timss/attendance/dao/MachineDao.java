package com.timss.attendance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.attendance.bean.MachineBean;
import com.yudean.itc.dto.Page;

/**
 * 考勤机的dao
 * @author 890147
 *
 */
public interface MachineDao {
	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 */
	List<MachineBean> queryList(Page<MachineBean> page);
	
	/**
	 * 查询全部
	 * @return
	 */
	List<MachineBean> queryAll();
	
	/**
	 * 查询站点的全部
	 * @return
	 */
	List<MachineBean> queryBySiteId(@Param("siteId")String siteId);
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	MachineBean queryDetail(@Param("id")String id);
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(MachineBean bean);
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(MachineBean bean);
	
	/**
	 * 更新同步数据信息
	 * @param bean
	 * @return
	 */
	int updateSync(MachineBean bean);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(@Param("id")String id);
	
}