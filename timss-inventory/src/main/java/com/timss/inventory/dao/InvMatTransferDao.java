package com.timss.inventory.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.timss.inventory.bean.InvMatTransfer;
import com.yudean.itc.annotation.DynamicFormBind;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferDao.java
 * @author: 890151
 * @createDate: 2016-1-7
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatTransferDao {

    /**
     * @description:移库申请列表数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @param userInfo
     * @param imt
     * @return
     * @throws Exception:
     */
    List<InvMatTransfer> queryInvMatTransferList(Page<?> page);

    /**
     * @description:根据ID查询申请
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtId
     * @return:
     */
    InvMatTransfer queryInvMatTransferById(@Param("imtId") String imtId);

    /**
     * @description:根据CODE查询申请
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtCode
     * @return:
     */
    InvMatTransfer queryInvMatTransferByCode(@Param("imtCode") String imtCode);
    
    /**
     * @description:插入领料信息
     * @author: 890151
     * @createDate: 2014-7-28
     * @param imt
     * @return:
     */
    @DynamicFormBind(masterKey = "imtId")
    int insertInvMatTransfer(InvMatTransfer imt);
    
    /**
     * @description:更新领料信息
     * @author: 890151
     * @createDate: 2014-7-28
     * @param imt
     * @return:
     */
    @DynamicFormBind(masterKey = "imtId")
    int updateInvMatTransfer(InvMatTransfer imt);

    /**
     * @description:删除申请
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtId
     * @return:
     */
    int deleteInvMatTransfer(@Param("imtId") String imtId);
    
}
