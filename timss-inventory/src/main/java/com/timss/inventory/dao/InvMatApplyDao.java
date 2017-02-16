package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.vo.InvMatApplyVO;
import com.yudean.itc.annotation.CalulateItem;
import com.yudean.itc.annotation.CalulateItemEntry;
import com.yudean.itc.annotation.DynamicFormBind;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDao.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatApplyDao {

    /**
     * @description:物资领料列表数据
     * @author: 890166
     * @createDate: 2014-7-27
     * @param userInfo
     * @param ima
     * @return
     * @throws Exception:
     */
    List<InvMatApplyVO> queryMatApplyList(Page<?> page);

    /**
     * @description:查询表单数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param map
     * @return:
     */
    List<InvMatApplyVO> queryMatApplyForm(Map<String, Object> map);
    
    /**
     * @description:查询领料申请关联的采购申请
     * @author: 890162
     * @createDate: 2016-8-24
     * @param map
     * @return:
     */
    List<String> queryPurApplyOfMatApply(Map<String, Object> map);
    /**
     * @description:新增采购申请和物资领料关联
     * @author: 890162
     * @createDate: 2016-8-26
     * @param map
     * @return:
     */
    void insertPurApplyMatApplyMap(@Param("purApplyId")String purApplyId,@Param("matApplyId")String matApplyId,@Param("siteId")String siteId);
    /**
     * @description:新增采购申请和物资领料关联
     * @author: 890162
     * @createDate: 2016-8-26
     * @param map
     * @return:
     */
    void deletePurApplyMatApplyMapByMatApplyId(@Param("matApplyId")String matApplyId,@Param("siteId")String siteId);
    /**
     * @description:更新领料信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param ima
     * @return:
     */
    @DynamicFormBind(masterKey = "imaid")
    int updateInvMatApply(InvMatApply ima);

    /**
     * @description:插入领料信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param ima
     * @return:
     */
    @DynamicFormBind(masterKey = "imaid")
    int insertInvMatApply(InvMatApply ima);

    /**
     * @description:通过flowNo查询到sheetid
     * @author: 890166
     * @createDate: 2014-9-23
     * @param sheetNo
     * @param siteId
     * @return:
     */
    String queryImaIddByFlowNo(Map<String, Object> map);

    /**
     * @description: 通过申请表id和站点id找到sheetNo
     * @author: 890166
     * @createDate: 2014-9-23
     * @param imaId
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryFlowNoByImaId(Map<String, Object> map);

    /**
     * @description: 通过外部ID找到领料申请信息
     * @author: yuanzh
     * @createDate: 2015-12-17
     * @param woId
     * @param applyType
     * @return
     * @throws Exception:
     */
    List<InvMatApply> queryMatApplyByOutterId(@Param("woId") String woId, @Param("applyType") String applyType);
}
