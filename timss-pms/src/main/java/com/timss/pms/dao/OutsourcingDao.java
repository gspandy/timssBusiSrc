package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Outsourcing;
import com.timss.pms.vo.OutsourcingVo;

public interface OutsourcingDao {
	int insertOutsourcing(Outsourcing outsourcing);
	
	int updateOutsourcing(Outsourcing outsourcing);
	
	int deleteOutsourcing(String outsourcingId);
	
	int deleteOutsourcingByProjectId(Integer projectId);
	
	List<OutsourcingVo> queryOutsourcingListByProjectId(Integer projectId);
}
