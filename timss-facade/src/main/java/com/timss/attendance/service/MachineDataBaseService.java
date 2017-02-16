package com.timss.attendance.service;

import java.util.List;
import java.util.Map;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.MachineBean;

/**
 * 考勤机数据服务的基类，提供共用的接口
 */
public interface MachineDataBaseService {
	
    /**
     * 获取打卡数据
     * 根据考勤机的配置自动调用获取数据的方法
     * @param machineBean
     * @param definitionBean
     * @param userMap 缓存的用户信息
     * @param oprPersons 运行人员字符串
     * @return
     * @throws Exception
     */
	List<CardDataBean> getAttdanceData(MachineBean machineBean,
			DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap,
			String oprPersons) throws Exception;
    
	/**
     * 从考勤机获取打卡数据
     * @param machineBean
     * @param definitionBean
     * @param userMap 缓存的用户信息
     * @param oprPersons 运行人员字符串
     * @return
     * @throws Exception
     */
	List<CardDataBean> getAttdanceMachineData(MachineBean machineBean,
			DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap,
			String oprPersons) throws Exception;
	
	/**
     * 从考勤文件获取打卡数据
     * 根据考勤机的配置自动调用获取数据的方法
     * @param machineBean
     * @param definitionBean
     * @param userMap 缓存的用户信息
     * @param oprPersons 运行人员字符串
     * @return
     * @throws Exception
     */
	List<CardDataBean> getAttdanceFileData(MachineBean machineBean,
			DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap,
			String oprPersons) throws Exception;
}
