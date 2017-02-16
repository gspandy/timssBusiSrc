package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwIsolationArea;
import com.yudean.itc.dto.Page;


public interface PtwIsolationAreaDao {
	
	List<PtwIsolationArea> queryPtwIsolationAreaList(Page<PtwIsolationArea> page);
	
	PtwIsolationArea queryPtwIsolationAreaById(int id);
	
	int insertPtwIsolationArea( PtwIsolationArea ptwIsolationArea);
	 
	
	int updatePtwIsolationArea( PtwIsolationArea  ptwIsolationArea);
	 
	
	int deletePtwIsolationAreaById(int id);

	
	int queryPtwIsolationAreaByNo(@Param("no") String no,@Param("siteId") String siteId);
	
	 
}
