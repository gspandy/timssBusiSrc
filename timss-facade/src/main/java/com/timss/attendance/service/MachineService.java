package com.timss.attendance.service;

import java.util.List;

import com.timss.attendance.bean.MachineBean;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 考勤机的service
 * @author 890147
 *
 */
public interface MachineService {

	/**
	 * 分页查询列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page<MachineBean> queryList(Page<MachineBean> page) throws Exception;

	/**
	 * 查询所有的考勤机
	 * 用于定时任务
	 * @return
	 * @throws Exception
	 */
	List<MachineBean>queryAll() throws Exception;
	
	/**
	 * 查询站点所有的考勤机
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	List<MachineBean>queryBySiteId(String siteId) throws Exception;
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	MachineBean queryDetail(String id) throws Exception;  
	
	/**
	 * 新建
	 * @param bean
	 * @return
	 */
	int insert(@CUDTarget MachineBean bean) throws Exception;
	
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	int update(@CUDTarget MachineBean bean) throws Exception;
	
	/**
	 * 更新数据同步信息
	 * @param bean
	 * @return
	 */
	int updateSync(@CUDTarget MachineBean bean) throws Exception;
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int deleteById(String id) throws Exception;
	
}