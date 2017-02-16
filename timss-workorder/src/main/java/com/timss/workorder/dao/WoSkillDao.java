package com.timss.workorder.dao;


import java.util.List;

import com.timss.workorder.bean.WoSkill;
import com.yudean.itc.dto.Page;
/**
 * 技能
 * @author 王中华
 * 2014-6-11
 */
public interface WoSkillDao {

	/**
	 * @description:插入技能
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int insertWoSkill(WoSkill woSkill);
	
	/**
	 * @description:修改技能
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int updateWoSkill(WoSkill woSkill);
	
	/**
	 * @description:删除技能
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	int deleteWoSkill(int id);
	
	/**
	 * @description:更加id查询
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	WoSkill queryWoSkillById(int id);
	
	/**
	 * @description:获取下一个插入的ID
	 * @author: 王中华
	 * @createDate: 2014-8-26 
	 * @return:
	 */
	int getNextParamsConfId();

	/**
	 * @description:列表页面查询
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param page
	 * @return:
	 */
	List<WoSkill> queryWoSkillList(Page<WoSkill> page);
}
