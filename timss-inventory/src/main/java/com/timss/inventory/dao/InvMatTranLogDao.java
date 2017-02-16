package com.timss.inventory.dao;

import java.util.List;
import com.timss.inventory.bean.InvMatTranLog;
import com.timss.inventory.bean.InvMatTranRec;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranLogDao.java
 * @author: 890151
 * @createDate: 2016-5-5
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatTranLogDao {

    /**
     * @description:查询出库来源批次对应关系记录列表
     * @author: 890151
     * @createDate: 2016-5-5
     * @param page
     * @return:
     */
    List<InvMatTranLog> queryInvMatTranLogList(Page<?> page);

    /**
     * @description:查询出库来源批次对应关系记录列表
     * @author: 890151
     * @createDate: 2016-5-5
     * @param page
     * @return:
     */
    List<InvMatTranRec> queryRelateTranRecByLog(Page<?> page);

    /**
     * @description:插入出库来源批次对应关系记录
     * @author: 890151
     * @createDate: 2016-5-5
     * @param invMatTranLog
     * @return:
     */
    int insertInvMatTranLog(InvMatTranLog invMatTranLog);

}
