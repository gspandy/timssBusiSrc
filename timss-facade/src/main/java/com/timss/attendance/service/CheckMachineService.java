package com.timss.attendance.service;

import java.util.Date;

/**
 * 
 * @title: 获取考勤机数据
 * @description: {desc}
 * @company: gdyd
 * @className: CheckMachineService.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface CheckMachineService {

	/**
	 * 刷新打卡记录的结果
	 * @param siteId 指定站点，为空刷新所有站点
	 * @param userIds 指定用户
	 * @param startTime
	 * @param endTime
	 * @throws Exception
	 */
    void refreshCheckMachineResult(String siteId,String[]userIds,Date startTime,Date endTime)throws Exception;
    
    /**
     * 执行抓取打卡机数据并导入系统
     * 参数null可查询所有站点
     */
    void importCheckMachineData(String siteId) throws Exception;
    
    /**
     * 检查考勤机抓取数据的结果，如果有异常则发送邮件通知
     * 参数null可查询所有站点
     */
    void checkMachineStatusAndNotify(String siteId) throws Exception;
    
}
