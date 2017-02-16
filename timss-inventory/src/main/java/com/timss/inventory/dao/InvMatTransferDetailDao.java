package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.vo.InvItemVO;
import com.yudean.itc.annotation.DynamicFormBind;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferDetailDao.java
 * @author: 890151
 * @createDate: 2016-1-8
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatTransferDetailDao {

    /**
     * @description:查询物资列表信息
     * @author: 890151
     * @createDate: 2016-1-8
     * @param page
     * @return:
     */
    List<InvMatTransferDetail> queryInvMatTransferDetailList(@Param("imtId") String imtId);

    /**
     * @description:插入物资
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imad
     * @return:
     */
    @DynamicFormBind(masterKey = "imtdId")
    int insertInvMatTransferDetail(InvMatTransferDetail imtd);

    /**
     * @description: 更新物资
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imad
     * @return:
     */
    int updateInvMatTransferDetail(InvMatTransferDetail imtd);

    /**
     * @description:根据申请id查询物资信息
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtid
     * @return:
     */
    int deleteInvMatTransferDetailByImtId(@Param("imtId") String imtId);

    /**
     * @description:根据物资编码查询物资信息 map含codes，可以多个编码
     * @author: 890151
     * @createDate: 2016-1-9
     * @param codes
     * @return:
     */
    InvItemVO queryItemInfoToMatTransfer(Map<String, Object> map);
}
