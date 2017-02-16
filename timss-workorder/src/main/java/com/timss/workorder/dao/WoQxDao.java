package com.timss.workorder.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WoQx;
import com.timss.workorder.vo.WoQxVo;
import com.yudean.itc.dto.Page;
/**
 * 缺陷
 * @author 王中华
 * 2014-6-11
 */
public interface WoQxDao {

	int insertWoQx(WoQx woQx);
	
	int updateWoQx(WoQx woQx);
	
	WoQx queryWoQxById(String id);
	/**
	 * @description: 删除某个缺陷记录
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param id
	 * @return:
	 */
	int deleteWoQxById(String id); 
	
	List<WoQx> queryAllWoQx(Page<WoQx> page);

	/**
	 * @description:根据设备ID 和站点ID，查询关联的缺陷单列表
	 * @author: 王中华
	 * @createDate: 2015-6-4
	 * @param assetId
	 * @param siteId
	 * @return:
	 */
	List<WoQx> queryQxByAssetId(@Param("assetId")String assetId, @Param("siteid")String siteid);
	/**
         * @description:查询缺陷统计列表
         * @author: 890162
         * @createDate: 2015-12-22
         * @return:
         */
	List<WoQxVo> queryWoQxVoStat(Page<WoQxVo> page);
}
