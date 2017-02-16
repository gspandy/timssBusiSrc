package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatRecipientsDetail;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsDetailDao.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatRecipientsDetailDao {

    /**
     * @description:插入领用情况详细信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param imrd
     * @return:
     */
    int insertInvMatRecipientsDetail(InvMatRecipientsDetail imrd);

    /**
     * @description:更新领用情况详细信息
     * @author: 890166
     * @createDate: 2014-10-13
     * @param imrd
     * @return:
     */
    int updateInvMatRecipientsDetail(InvMatRecipientsDetail imrd);

    /**
     * @description:通过imaid查询详细信息
     * @author: 890166
     * @createDate: 2014-9-29
     * @param map
     * @return:
     */
    List<InvMatRecipientsDetail> queryinvMatRecipientsDetailByImaId(Map<String, Object> map);

    /**
     * @description:通过imaid查询数量
     * @author: 890166
     * @createDate: 2014-9-29
     * @param map
     * @return:
     */
    List<InvMatRecipientsDetail> queryIMRDQtyByImaId(Map<String, Object> map);
    
    /**
     * @description:根据领料单ID删除未发料的发料单子表信息
     * @author: 890151
     * @createDate: 2016-8-25
     * @param imrId
     * @param siteId
     * @return
     * @throws Exception:
     */
    int deleteInvMatRecipientsDetail(@Param("imrId") String imrId, @Param("siteId")String siteId);

}
