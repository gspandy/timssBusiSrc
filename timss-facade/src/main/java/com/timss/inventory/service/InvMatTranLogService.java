package com.timss.inventory.service;

import com.timss.inventory.bean.InvMatTranLog;
import com.timss.inventory.bean.InvMatTranRec;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: 出库来源批次对应关系接口
 * @description: 出库来源批次对应关系接口
 * @company: gdyd
 * @className: InvMatTranLogService.java
 * @author: 890151
 * @createDate: 2016-5-5
 * @updateUser: 890151
 * @version: 1.0
 */
public interface InvMatTranLogService {

    /**
     * @description:分页查询出库来源批次对应关系列表
     * @author: 890151
     * @createDate: 2016-5-5
     * @return
     * @throws Exception :
     */
	Page<InvMatTranLog> queryInvMatTranLogList(UserInfoScope userInfo, InvMatTranLog imtl) throws Exception;

    /**
     * @description:查询关联的批次信息
     * @author: 890151
     * @createDate: 2016-06-03
     * @return
     * @throws Exception :
     */
	Page<InvMatTranRec> queryRelateTranRecByLog(UserInfoScope userInfo, InvMatTranLog imtl) throws Exception;

    /**
     * @description:保存一条出库来源批次对应关系
     * @author: 890151
     * @createDate: 2016-5-5
     * @return
     * @throws Exception :
     */
    int saveInvMatTranLog(UserInfoScope userInfoScope, InvMatTranLog invMatTranLog) throws Exception;

}
