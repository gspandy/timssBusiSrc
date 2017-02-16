package com.timss.workorder.service;

import java.util.List;
import java.util.Map;

import com.timss.workorder.bean.WoQx;
import com.timss.workorder.vo.WoQxVo;
import com.timss.workorder.vo.WorkOrderVO;
import com.yudean.itc.dto.Page;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface WoQxService {
	/**
	 * 添加 安全事项
	 * @param WoBdzqx
	 * @return
	 */
	Map<String, String> insertUpdateWoQx(WoQx woQx);
	/**
	 * 更新 安全事项
	 * @param harzard
	 */
	void updateWoQx(WoQx woQx);
	/**
	 * 根据ID 查询安全事项
	 * @param id
	 * @return
	 */
	WoQx queryWoQxById(String id);
	
	/**
	 * @description:删除缺陷记录
	 * @author: 王中华
	 * @createDate: 2015-5-28
	 * @param id
	 * @return:
	 */
	Map<String, String> deleteWoQxById(String id);
	
	Page<WoQx> queryAllWoQx(Page<WoQx> page);
	
	/**
	 * @description:根据设备id和站点，查询关联的缺陷单记录
	 * @author: 王中华
	 * @createDate: 2015-6-4
	 * @param assetId
	 * @param siteId
	 * @return:
	 */
	List<WoQx> queryQxByAssetId(String assetId, String siteId);
	/**
         * @description:查询缺陷统计
         * @author: 890162
         * @createDate: 2015-12-22
         * @param page
         * @return:Page<WoQxVo>
         */
	Page<WoQxVo> queryWoQxVoStat(Page<WoQxVo> page);
}
