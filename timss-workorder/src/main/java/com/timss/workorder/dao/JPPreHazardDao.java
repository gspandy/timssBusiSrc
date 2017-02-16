package com.timss.workorder.dao;

import java.util.List;

import com.timss.workorder.bean.JPPreHazard;

/**
 * 作业方案中 安全事项与预控措施关联关系 DAO 操作接口
 * @author 王中华
 * 2014-6-11
 */
public interface JPPreHazardDao {

	void insertJPPreHazard(JPPreHazard jpPreHazard);
	
	List<JPPreHazard> queryJPPreHazardByJPId(int jpId);
	
	/**
	 * @description: 删除某个作业方案对应的安全注意事项关联表信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param jpId
	 * @return:
	 */
	int deleteJPPreHazardByJPId(int jpId);
	
	/**
	 * @description: 删除某个安全注意事项关联表信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param preHazardId
	 * @return:
	 */
	int deleteJPPreHazardById(int preHazardId);
	
	JPPreHazard queryJPPreHazardById(int id);

	
}
