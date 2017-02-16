package com.timss.itsm.service;

import java.util.Map;

import com.timss.itsm.bean.ItsmComplainTypeConf;
import com.yudean.itc.dto.Page;

public interface ItsmComplainTypeConfService {
	
	void updateComplainTypeConf(Map<String, String> addComplainTypeConfDataMap);//跟新投诉列别配置	
	
	void insertComplainTypeConf(Map<String, String> addComplainTypeConfDataMap);//插入
	
	ItsmComplainTypeConf queryComplainTypeConfById(String id);//根据id查找
	
	void deleteComplainTypeConfById(String id);//删除投诉列表配置
	
	Page<ItsmComplainTypeConf> queryComplainTypeConfList(Page<ItsmComplainTypeConf> page);//列表页面查询
}
