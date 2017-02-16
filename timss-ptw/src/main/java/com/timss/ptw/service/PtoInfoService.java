package com.timss.ptw.service;

import java.util.Map;

import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.itc.dto.Page;


/**
 * @title: {title}操作票
 * @description: {desc}  操作票接口
 * @company: gdyd
 * @className: PtoInfoService.java
 * @author: 王中华
 * @createDate: 2015-7-29
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface PtoInfoService {
   
    /**
     * @description: 查询操作票列表
     * @author: 王中华
     * @createDate: 2015-7-29
     * @param page
     * @return
     * @throws Exception:
     */
    Page<PtoInfo> queryPtoListInfo(Page<PtoInfo> page) throws Exception;
    
    /**
     * @description:保存和更新操作票信息
     * @author: 王中华
     * @createDate: 2015-7-30 
     * @param ptoInfoVo
     * @param startWorkFlow
     * @return:
     */
    Map<String, Object> saveOrUpdatePtoInfo(PtoInfoVo ptoInfoVo, boolean startWorkFlow) throws Exception;

    /**
     * @description: 根据ID查找工作票的基本信息
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    PtoInfoVo queryPtoInfoById(String ptoId);
    
    /**
     * @description:作废
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    int obsoletePtoInfoById(String ptoId);
    
    /**
     * @description: 删除
     * @author: 王中华
     * @createDate: 2015-7-31 
     * @param ptoId
     * @return:
     */
    int deletePtoInfoById(String ptoId);

    /**
     * @description: 查询操作票的详情信息
     * @author: 王中华
     * @createDate: 2015-7-31
     * @param ptoId
     * @return:
     */
    PtoInfoVo queryPtoInfoVoById(String ptoId);

    
    /**
     * @description:更新操作票信息，此更新与工作流无关
     * @author: 王中华
     * @createDate: 2015-8-3
     * @param ptoInfoVo
     * @param params 需要修改的字段
     * @param currstatus  当前状态，存储附件的时候需要
     * @throws Exception:
     */
    void updatePtoInfoNoWithWorkflow(PtoInfoVo ptoInfoVo, String[] params, String currstatus) throws Exception;

    /**
     * @description: 删除 
     * @author: 王中华
     * @createDate: 2015-8-4
     * @param param:
     */
    void deletePtoInfo(String id);

    /**
     * @description:作废
     * @author: 王中华
     * @createDate: 2015-8-4
     * @param param:
     */
    void obsoletePtoInfo(String id);

    /**
     * @description: 操作票抽查更新
     * @author: 王中华
     * @createDate: 2015-8-7
     * @param params:
     */
    void updatePtoInfoOnCheck(Map<String, Object> params);

    /**
     * @description:确定操作票已执行
     * @author: 王中华
     * @createDate: 2016-5-19
     * @param param:
     */
    void hasDonePtoInfo(String param);


    
}
