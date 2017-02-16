package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwPtoStepInfo;
import com.timss.ptw.bean.PtwPtoUserInfo;
import com.timss.ptw.bean.PtwPtoUserInfoConfig;
import com.yudean.itc.dto.Page;

/**
 * @title: 两票审批人员Dao
 * @description: 两票审批人员
 * @company: gdyd
 * @className: PtwPtoUserInfoDao.java
 * @author: gucw
 * @createDate: 2015-7-20
 * @updateUser:
 * @version: 1.0
 */
public interface PtwPtoUserInfoDao {

    List<PtwPtoUserInfoConfig> queryUserInfoConfig(Page<PtwPtoUserInfoConfig> page);
    
    List<PtwPtoUserInfoConfig> isUserInfoConfigConflict(PtwPtoUserInfoConfig config);
    
    PtwPtoUserInfoConfig queryUserInfoConfigById(String id);

    List<String> queryUserInfoListByConfigId(String id);

    List<PtwPtoStepInfo> queryStepInfo(PtwPtoStepInfo ptwPtoStepInfo);
    
    List<String> queryOrgCodeWithSiteId(String id);
    
    List<String> queryRelatedOrgListStartWithOrgCode(String id);
    
    List<String> queryRelatedOrgListEndWithOrgCode(String id);

    int insertUserInfoConfig(PtwPtoUserInfoConfig ptwPtoUserInfoConfig);

    int insertUserInfo(PtwPtoUserInfo ptwPtoUserInfo);

    int updateUserInfoConfig(@Param("config") PtwPtoUserInfoConfig config,@Param("params") String[] params);

    int deleteUserInfo(String id);
}

