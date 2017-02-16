package com.timss.inventory.dao;

import java.util.List;

import com.timss.inventory.bean.InvStocktakingDetail;
import com.timss.inventory.vo.InvStocktakingDetailVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDetailDao.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvStocktakingDetailDao {

    /**
     * @description: 查询表单中列表的详细信息
     * @author: 890166
     * @createDate: 2014-10-8
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    List<InvStocktakingDetailVO> queryStocktakingDetailList(Page<?> page);

    /**
     * @description:通过istid删除
     * @author: 890166
     * @createDate: 2014-11-12
     * @param istId
     * @return:
     */
    int deleteInvStocktakingDetailByIstId(String istId);

    /**
     * @description: 插入InvStocktakingDetail信息
     * @author: 890166
     * @createDate: 2014-11-12
     * @param istd
     * @return:
     */
    int insertInvStocktakingDetail(InvStocktakingDetail istd);
}
