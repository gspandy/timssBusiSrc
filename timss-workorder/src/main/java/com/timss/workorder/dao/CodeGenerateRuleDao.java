package com.timss.workorder.dao;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.CodeGenerateRule;

public interface CodeGenerateRuleDao {

    /**
     * @title: {title} 查询规则Bean
     * @description: {desc}
     * @company: gdyd
     * @className: CodeGenerateRoleDao.java
     * @author: 王中华
     * @createDate: 2016-5-20
     * @updateUser: 王中华
     * @version: 1.0
     */
    CodeGenerateRule queryCodeGenerateRule(@Param("siteid")String siteId, @Param("plantCode")String plantCode, 
            @Param("moduleCode")String moduleCode,@Param("subModule")String subModule);

}
