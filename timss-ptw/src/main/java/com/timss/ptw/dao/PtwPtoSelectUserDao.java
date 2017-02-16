package com.timss.ptw.dao;

import java.util.List;

import com.timss.ptw.vo.PtwPtoSelectUserInfoVo;

/**
 * @title: 两票选人Dao
 * @description: 两票选人Dao
 * @company: gdyd
 * @className: PtwPtoSelectUserDao.java
 * @author: gucw
 * @createDate: 2015-7-10
 * @updateUser: 2015-8-4
 * @version: 1.0
 */
public interface PtwPtoSelectUserDao {
    /**
     * @description:查询满足条件的两票审批人员
     * @author: gucw
     * @createDate: 2015-7-10
     * @param PtwPtoSelectUserInfoVo
     * @return:
     */
    List<PtwPtoSelectUserInfoVo> queryPtwPtoUserInfo(PtwPtoSelectUserInfoVo ptwPtoSelectUserInfoVo);

}
