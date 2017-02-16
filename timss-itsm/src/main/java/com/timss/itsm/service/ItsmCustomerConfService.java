package com.timss.itsm.service;

import com.timss.itsm.bean.ItsmCustomerConf;
import com.yudean.itc.dto.Page;



public interface ItsmCustomerConfService {

	/**
	 * @description:查询客服配置列表数据
	 * @author: 王中华
	 * @createDate: 2014-11-14
	 * @param principal
	 * @param workTeams
	 * @return:
	 */
	Page<ItsmCustomerConf>  queryAllCustomerConf(Page<ItsmCustomerConf> page);

	/**
	 * @description:插入
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param bean:
	 */
	void insertCustomerConf(ItsmCustomerConf bean);

	/**
	 * @description:更新
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param bean:
	 */
	void updateCustomerConf(ItsmCustomerConf bean);

	/**
	 * @description: 根据ID查询记录
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param customerConfId
	 * @return:
	 */
	ItsmCustomerConf queryCustomerConfById(int customerConfId);

	/**
	 * @description:删除客户配置
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param customerConfId:
	 */
	void deleteCustomerConf(int customerConfId);

	/**
	 * @description: 判断是否有重复的客户配置
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param customerCode
	 * @return:
	 */
	int judgeRepeatCustomerConf(String customerCode);

	/**
	 * @description: 根据服务目录和客户工号查找对应的客户配置
	 * @author: 王中华
	 * @createDate: 2015-4-8
	 * @param customerCode
	 * @param faultTypeId
	 * @return:
	 */
	ItsmCustomerConf getInitPriority(String customerCode, String faultTypeId);



	
	
	
	
	
}
