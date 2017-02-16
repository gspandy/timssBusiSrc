package com.timss.itsm.dao;

import com.timss.itsm.bean.ItsmComplainTypeConf;
import com.yudean.itc.dto.Page;
import java.util.List;

public interface ItsmComplainTypeConfDao {
	
	int insertComplTypeConf(ItsmComplainTypeConf complainTypeConf);// 插入投诉类别
	
	int updateComplTypeConf(ItsmComplainTypeConf complainTypeConf);//跟新投诉类别
	
	void deleteComplTypeConf(String id);//删除
	
	ItsmComplainTypeConf queryComplTypeConfById(String id);//根据id查找
	
	
	List<ItsmComplainTypeConf> queryComplTypeConfList(Page<ItsmComplainTypeConf> page);//列表页面查询
	
}
