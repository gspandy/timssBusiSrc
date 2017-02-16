package com.timss.purchase.service;

import java.util.List;

import com.timss.purchase.bean.PurHhcMaxnum;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurHhcMaxnumService.java
 * @author: 890166
 * @createDate: 2014-10-31
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurHhcMaxnumService {

    /**
     * @description: 通过站点查询最大数记录
     * @author: 890166
     * @createDate: 2014-10-31
     * @param siteid
     * @return
     * @throws Exception:
     */
    List<PurHhcMaxnum> queryPurHhcMaxnum(String siteid) throws Exception;

    /**
     * @description:插入中间表数据
     * @author: 890166
     * @createDate: 2014-11-3
     * @param phm
     * @return
     * @throws Exception:
     */
    int insertPurHhcMaxnum(PurHhcMaxnum phm) throws Exception;

    /**
     * @description:更新中间表数据
     * @author: 890166
     * @createDate: 2014-11-3
     * @param phm
     * @return
     * @throws Exception:
     */
    int updatePurHhcMaxnum(PurHhcMaxnum phm) throws Exception;

}
