package com.timss.itsm.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmWoPriConfig;
/**
 * 技能
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmWoPriConfigDao {

	/**
	 * @description:插入优先级
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int insertWoPriConfig(ItsmWoPriConfig woPriConfig);
	
	
	
	/**
	 * @description:删除优先级
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	int deleteWoPriConfig(@Param("priId")int priId ,@Param("siteid")String siteid);
	
	
	/**
	 * @description: 根据站点ID查找优先级 
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param siteid
	 * @return:
	 */
	List<ItsmWoPriConfig> queryWoPriConfigListById(@Param("priId")int priId ,@Param("siteid") String siteid);



	/**
	 * @description:根据影响度和紧急度，查询对应的服务级别ID
	 * @author: 王中华
	 * @createDate: 2014-12-9
	 * @param urgentVal
	 * @param influenceVal
	 * @return:
	 */
	ItsmWoPriConfig queryWoPriConfigByOtherCode(@Param("urgentVal") String urgentVal,
			@Param("influenceVal") String influenceVal,@Param("siteid") String siteid);

	
}
