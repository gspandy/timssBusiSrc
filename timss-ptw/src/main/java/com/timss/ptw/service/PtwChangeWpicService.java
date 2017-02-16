package com.timss.ptw.service;

import java.util.List;

import com.timss.ptw.bean.PtwChangeWpic;

/**
 * 变更工作负责人
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwChangeWpicService.java
 * @author: 周保康
 * @createDate: 2014-8-21
 * @updateUser: 周保康
 * @version: 1.0
 */
public interface PtwChangeWpicService {
    /**
     * 根据工作票编号查询
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-21
     * @param wtId
     * @return:
     */
    PtwChangeWpic queryPtwChangeWpicByPtwId(int wtId);
    
    /**
     * 根据变更后工作负责人查询
     * @description:
     * @author: zhuw
     * @createDate: 2016-6-30
     * @param chaNewWpicNo
     * @return:
     */
    List<PtwChangeWpic> queryPtwChangeWpicByNewNo(String chaNewWpicNo);
    
    /**
     * 插入工作负责人变更信息
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-21
     * @param ptwChangeWpic
     * @return:
     */
    int insertPtwChangeWpic(PtwChangeWpic ptwChangeWpic);
}
