package com.timss.purchase.service;

import java.util.List;
import java.util.Map;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurPaSyncHhc;
import com.timss.purchase.vo.PurBuisCalaVO;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPaSyncHhcService.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurPaSyncHhcService {

    /**
     * @description:数据查询是否存在中间记录表
     * @author: 890166
     * @createDate: 2015-3-24
     * @param map
     * @return:
     */
    List<PurPaSyncHhc> queryPurPaSyncHhcByMap(Map<String, Object> map);

    /**
     * @description: 保存中间表映射信息
     * @author: 890166
     * @createDate: 2015-3-24
     * @param pa
     * @param userInfo
     * @throws Exception:
     */
    PurPaSyncHhc savePurPaSyncHhc(PurApply pa, UserInfoScope userInfo);

    /**
     * @description:根据发送商务网流水号查询本地采购申请物资是否已经采购
     * @author: user
     * @createDate: 2016-1-20
     * @param map
     * @return:
     */
    PurBuisCalaVO queryPurBuisCala(Map<String, Object> map);

}
