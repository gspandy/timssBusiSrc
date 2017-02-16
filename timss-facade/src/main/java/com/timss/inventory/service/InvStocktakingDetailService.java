package com.timss.inventory.service;

import com.timss.inventory.vo.InvStocktakingDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDetailService.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvStocktakingDetailService {

    /**
     * @description: 查询表单中列表的详细信息
     * @author: 890166
     * @createDate: 2014-10-8
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    Page<InvStocktakingDetailVO> queryStocktakingDetailList(UserInfoScope userInfo, String istid) throws Exception;

}
