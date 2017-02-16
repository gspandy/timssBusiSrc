package com.timss.asset.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.timss.asset.bean.AstOrganizeData;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AstOrganizeDataService.java
 * @author: 890166
 * @createDate: 2015-5-29
 * @updateUser: 890166
 * @version: 1.0
 */
public interface AstOrganizeDataService {

    /**
     * @description: 插入AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int insertAstOrganizeData(AstOrganizeData aod);

    /**
     * @description: 删除AstOrganizeData数据
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    int deleteAstOrganizeData(AstOrganizeData aod);

    /**
     * @description: 查询AstOrganizeData信息
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @return:
     */
    Page<AstOrganizeData> queryAstOrganizeData(UserInfoScope userInfo, AstOrganizeData aod);

    /**
     * @description: 触发调用oracle的存储过程整理数据
     * @author: 890166
     * @createDate: 2015-6-2
     * @param paramMap
     * @return:
     */
    boolean callAstOrganizeDataInit(Map<String, Object> paramMap);

    /**
     * @description: 上传excel方法
     * @author: 890166
     * @createDate: 2015-11-11
     * @param excelFiles:
     */
    void uploadExcel(MultipartFile[] excelFiles, UserInfoScope userInfo) throws Exception;
}
