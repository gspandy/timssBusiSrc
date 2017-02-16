package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwKeyBox;
import com.timss.ptw.util.PtwConstants.KeyBoxStatus;
import com.yudean.itc.dto.Page;

public interface PtwKeyBoxDao {
	
	List<PtwKeyBox> queryPtwKeyBoxList(Page<PtwKeyBox> page);
	
	PtwKeyBox queryPtwKeyBoxById(int id);
	
	int insertPtwKeyBox( PtwKeyBox ptwKeyBox);
	
	int updatePtwKeyBox( PtwKeyBox  ptwKeyBox);
	
	int deletePtwKeyBoxById(int id);
	
	int queryPtwKeyBoxByNo(@Param("keyBoxNo") String keyBoxNo,@Param("siteId") String siteId);
	
	List<PtwKeyBox> queryByIds(@Param("ids")String ids);
	
	int updateKeyBoxStatus(@Param("id")int id, @Param("status")KeyBoxStatus status) ;
	 
}
