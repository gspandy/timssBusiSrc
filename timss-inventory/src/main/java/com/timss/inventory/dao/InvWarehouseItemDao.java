package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.vo.InvWarehouseItemVO;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseItemDao.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvWarehouseItemDao {

    /**
     * @description:插入添加到仓库数据
     * @author: 890166
     * @createDate: 2014-7-18
     * @param iwi
     * @return:
     */
    int insertInvWarehouseItem(InvWarehouseItem iwi);

    /**
     * @description:更新添加到仓库数据
     * @author: yuanzh
     * @createDate: 2016-2-1
     * @param iwi
     * @return:
     */
    int updateInvWarehouseItem(InvWarehouseItem iwi);

    /**
     * @description:通过itemid查询所在仓库
     * @author: 890166
     * @createDate: 2014-7-18
     * @param map
     * @return:
     */
    List<InvWarehouseItemVO> queryInvWarehouseItem(Map<String, Object> map);

    /**
     * @description: 通过物资类别查询是否已经绑定
     * @author: 890166
     * @createDate: 2014-8-23
     * @param categoryId
     * @return:
     */
    int queryInvWarehouseItemCounter(String categoryId);

	/**
	 * @description:更新安全库存值
	 * @author: 890151
	 * @createDate: 2016-9-22
	 * @param invWarehouseItem:
	 */
	void updateSafeQty(InvWarehouseItem invWarehouseItem);

}
