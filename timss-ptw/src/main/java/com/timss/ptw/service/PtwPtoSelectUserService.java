package com.timss.ptw.service;

import java.util.List;

import com.yudean.itc.dto.sec.SecureUser;

/**
 * 
 * @title: 两票审批人员选人接口
 * @description: 两票审批人员选人接口
 * @company: gdyd
 * @className: PtwPtoSelectUserService.java
 * @author: gucw
 * @createDate: 2015-8-4
 * @updateUser: gucw
 * @version: 1.0
 */
public interface PtwPtoSelectUserService {

    /**
     * 根据专业 类型以及环节，返回用户列表
     * @description:
     * @author: gucw
     * @createDate: 2015-11-24
     * @param category type step
     * @return:
     */
    public List<SecureUser> selectUsersWithoutWorkFLow(String category,String type,String step)throws Exception;
    /**
     * 根据专业 类型以及环节，返回当前用户是否拥有处理权限
     * @description:如当前用户拥有新建 电气第一种 工作票时，就返回true
     * @author: gucw
     * @createDate: 2015-11-28
     * @param  category type step
     * @return:
     */
    public Boolean hasAuditPrivilege(String category,String type,String step)throws Exception;
    /**
     * 返回当前用户可处理指定类别、环节的类型集合
     * @description:如返回新建哪几个类型的工作票
     * @author: gucw
     * @createDate: 2015-11-30
     * @param  category step
     * @return:
     */
    public List<String> queryPrivilegeTypes(String category, String step) throws Exception;
}

