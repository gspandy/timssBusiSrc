package com.timss.ptw.service;

import com.timss.ptw.bean.PtwInfo;


/**
 * 工作票编号生成接口
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoNoService.java
 * @author: 周保康
 * @createDate: 2014-12-2
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwInfoNoService {
    /**
     * 生成临时的工作票编号
     * @description:
     * @author: 周保康
     * @createDate: 2014-12-2
     * @param ptwInfo
     * @param ptwTypeCode
     * @return:
     */
    String genPtwNo(PtwInfo ptwInfo,String ptwTypeCode);
    
    /**
     * @description:生成固定的工作票编号（生物质要求编号不可变，新建的时候就确定下来）
     * @author: 王中华
     * @createDate: 2016-5-24
     * @param ptwInfo
     * @param ptwTypeCode
     * @return:
     */
    String genFixedPtwNo(PtwInfo ptwInfo,String ptwTypeCode);
    
    /**
     * 签发工作票后生成正式的工作票编号
     * @description:
     * @author: 周保康
     * @createDate: 2014-12-2
     * @param ptwInfo
     * @return:更新的条数
     */
    int updatePtwNoAfterIssue(PtwInfo ptwInfo,String ptwTypeCode);
}
