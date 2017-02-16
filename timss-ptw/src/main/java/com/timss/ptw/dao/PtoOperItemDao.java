package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtoOperItem;


/**
 * @title: {title}
 * @description: {desc} 操作项
 * @company: gdyd
 * @className: PtoOperItemDao.java
 * @author: 王中华
 * @createDate: 2015-7-31
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface PtoOperItemDao {
   

    /**
     * @description:查询
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    List<PtoOperItem> queryPtoOperItemByPtoId(@Param("ptoId") String ptoId);

    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param item
     * @return:
     */
    int insertPtoOperItem(PtoOperItem item);

    /**
     * @description:删除
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    int deletePtoOperItemsByPtoId(@Param("ptoId") String ptoId);
    
}
