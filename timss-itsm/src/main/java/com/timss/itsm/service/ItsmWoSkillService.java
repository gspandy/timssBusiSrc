package com.timss.itsm.service;

import java.util.Map;

import com.timss.itsm.bean.ItsmWoSkill;
import com.yudean.itc.dto.Page;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmWoSkillService {

	/**
	 * @description:修改技能
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param addWoSkillDataMap:
	 */
	void updateWoSkill(Map<String, String> addWoSkillDataMap);

	/**
	 * @description:添加技能
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param addWoSkillDataMap:
	 */
	void insertWoSkill(Map<String, String> addWoSkillDataMap);
	
	/**
	 * 根据ID 查询技能
	 * @param id
	 * @return
	 */
	ItsmWoSkill queryWoSkillById(int id);
	
	/**
	 * @description:删除技能
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param id:
	 */
	void deleteWoSkillById(int id);

	/**
	 * @description:查询技能列表
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param page
	 * @return:
	 */
	Page<ItsmWoSkill> queryWoSkillList(Page<ItsmWoSkill> page);
	
}
