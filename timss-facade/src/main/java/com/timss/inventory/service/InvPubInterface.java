package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvPubInterface.java
 * @author: user
 * @createDate: 2015-9-24
 * @updateUser: user
 * @version: 1.0
 */
public interface InvPubInterface {

    /**
     * @description: 物资领料页面查询退库列表
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param imaid
     * @return:
     */
    public Page<InvMatTranDetailVO> queryAlreadyRefunding(String imaid);

    /**
     * @description:查询退库单页面明细
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param imaid ：领料单id
     * @param imrsno：退库单编码
     * @return:
     */
    public List<InvMatReturnsDetailVO> queryRefundingDetailI(String imaid, String imrsno);

    /**
     * @description: 判断是否符合显示按钮要求
     * @author: yuanzh
     * @createDate: 2015-9-25
     * @return:返回Y代表可以隐藏，N代表要展示
     */
    public String queryRefundingBtnIsHide(String imaid);

    /**
     * @description: 根据采购合同查询到验收记录明细
     * @author: yuanzh
     * @createDate: 2015-10-30
     * @param poId 采购合同Id
     * @return:
     */
    public List<InvMatAcceptDetailVO> queryItem2InvMatAcceptDetail(String poId);

    /**
     * @description: 保存移库数据到流水表
     * @author: yuanzh
     * @createDate: 2016-1-12
     * @param imTranferDetail
     * @return:
     */
    public Map<String, String> saveTransfer2MatTran(InvMatTransfer imTransfer, List<InvMatTransferDetail> imTfList);

}
