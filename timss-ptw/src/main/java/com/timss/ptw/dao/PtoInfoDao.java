package com.timss.ptw.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.itc.dto.Page;


/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtoInfoDao.java
 * @author: 王中华
 * @createDate: 2015-7-29
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface PtoInfoDao {
   
    /**
     * @description: 查询列表
     * @author: 王中华
     * @createDate: 2015-7-29
     * @param page
     * @return:
     */
    List<PtoInfo> queryPtoListInfo(Page<PtoInfo> page);
    
    /**
     * @description: 插入
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoInfo
     * @return:
     */
    int insertPtoInfo(PtoInfo ptoInfo);
    
    /**
     * @description: 更新
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoInfo
     * @return:
     */
    int updatePtoInfo(@Param("ptoInfo") PtoInfo ptoInfo, @Param("params") String[] params);

    /**
     * @description: 查询操作票基本信息
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    PtoInfoVo queryPtoInfoById(@Param("ptoId") String ptoId);

    /**
     * @description: 作废
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    int updatePtoStatusById(@Param("ptoId") String ptoId,@Param("status") String status,
            @Param("modifyuserId") String modifyuserId,@Param("modifydate") Date modifydate);

    /**
     * @description: 删除
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    int deletePtoInfoById(@Param("ptoId") String ptoId);

    void updateCurrHandUserById(Map<String, String> parmas);

    void updatePtoInfoOnCheck(Map<String, Object> params);

    

}
