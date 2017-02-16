package com.timss.purchase.service;

import java.util.Date;

import com.timss.purchase.bean.PurPoSyncHhc;
import com.yudean.mvc.bean.userinfo.UserInfo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPoSyncHhcService.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurPoSyncHhcService {

    /**
     * @description: 查询最后一次同步信息
     * @author: 890166
     * @createDate: 2015-3-24
     * @return:
     */
    Date queryLastTimeGetData(UserInfo uInfo);

    /**
     * @description: 插入中间表数据
     * @author: 890166
     * @createDate: 2015-3-24
     * @param PurPoSyncHhc
     * @return:
     */
    int insertPurPoSyncHhcInfo(PurPoSyncHhc purPoSyncHhc);
}
