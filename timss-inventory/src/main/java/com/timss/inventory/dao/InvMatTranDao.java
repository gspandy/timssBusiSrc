package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDao.java
 * @author: 890166
 * @createDate: 2014-7-18
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatTranDao {

    /**
     * @description:插入交易信息
     * @author: 890166
     * @createDate: 2014-7-18
     * @param imt
     * @return:
     */
    int insertInvMatTran(InvMatTran imt);

    /**
     * @description:更新交易表
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imt
     * @return:
     */
    int updateMatTran(InvMatTran imt);

    /**
     * @description:通过id查询交易信息主表
     * @author: 890166
     * @createDate: 2014-7-18
     * @param imtId
     * @return:
     */
    List<InvMatTran> queryInvMatTranById(String imtId);

    /**
     * @description:查询物资接收列表
     * @author: 890166
     * @createDate: 2014-7-23
     * @param page
     * @return:
     */
    List<InvMatTranVO> queryMatTranList(Page<?> page);

    /**
     * @description:查询物资接收表单
     * @author: 890166
     * @createDate: 2014-7-23
     * @param map
     * @return:
     */
    List<InvMatTranVO> queryMatTranForm(Map<String, Object> map);

    /**
     * @description:获取采购单号列表方法
     * @author: 890166
     * @createDate: 2014-7-23
     * @param page
     * @return:
     */
    List<MTPurOrderVO> queryPurOrderList(Page<?> page);

    /**
     * @description:获取用户信息
     * @author: 890166
     * @createDate: 2014-8-5
     * @param map
     * @return
     * @throws Exception:
     */
    List<Map<String, Object>> getUserHint(Map<String, Object> map);

    /**
     * @description:根据id删除入库记录
     * @author: 890166
     * @createDate: 2014-8-28
     * @param imtid:
     */
    void deleteInvMatTranById(String imtid);

    /**
     * @description:快速查询
     * @author: 890166
     * @createDate: 2014-9-1
     * @param userInfo
     * @param schfield
     * @return
     * @throws Exception:
     */
    List<InvMatTranVO> quickSearch(Page<?> page);

    /**
     * @description:物资入库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @param page
     * @return:
     */
    List<InvMatTranVO> queryInMatTran(Page<?> page);

    /**
     * @description:物资出库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @param page
     * @return:
     */
    List<InvMatTranVO> queryOutMatTran(Page<?> page);

    /**
     * @description: 通过sheetNo查询InvMatTran
     * @author: 890166
     * @createDate: 2014-11-12
     * @param sheetNo
     * @return:
     */
    List<InvMatTran> queryInvMatTranBySheetNo(String sheetNo);

    /**
     * @description:根据sheetno查询流水表（带扩展字段）
     * @author: yuanzh
     * @createDate: 2016-1-12
     * @param sheetNo
     * @param paramMap
     * @return:
     */
    List<InvMatTran> queryInvMatTranBySheetNoAndParam(@Param("sheetNo") String sheetNo,
            @Param("paramMap") Map<String, String> paramMap);
}
