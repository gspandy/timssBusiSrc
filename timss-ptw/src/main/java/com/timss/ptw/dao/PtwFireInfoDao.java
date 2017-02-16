package com.timss.ptw.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwFireInfo;

/**
 * 
 * @title: 动火相关信息Dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwFireInfoDao.java
 * @author: 周保康
 * @createDate: 2014-8-5
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwFireInfoDao {
    
    /**
     * 根据主表的工作票Id查询动火信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param wtId
     * @return:
     */
    PtwFireInfo queryPtwFireInfoByWtId(int wtId);
    
    /**
     * 查找工作票所有附加的动火票Id
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param wtId
     * @return:
     */
    List<Integer> queryFireIdsByAttachWtId(int attachWtId);
    
    /**
     * 插入一条动火基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:插入成功的条数
     */
    int insertPtwFireBaseInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新一条动火基本信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:更新成功的条数
     */
    int updatePtwFireBaseInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票允许会签的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowApprInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票安全措施确认的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowConfirmInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新工作票确认结束的流程信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFireFlowFinInfo(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新动火票的动火工作示意图
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-5
     * @param ptwFireInfo
     * @return:
     */
    int updatePtwFirePic(PtwFireInfo ptwFireInfo);
    
    /**
     * 更新动火票审批人的信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-9-1
     * @param userId
     * @param userName
     * @param date
     * @return:
     */
    int updatePtwFireAuditInfo(@Param("wtId")int wtId,
            @Param("idColumn")String idColumn,
            @Param("nameColumn") String nameColumn,
            @Param("timeColumn")String timeColumn,
            @Param("userId") String userId, 
            @Param("userName") String userName,
            @Param("date") Date date);
    
}
