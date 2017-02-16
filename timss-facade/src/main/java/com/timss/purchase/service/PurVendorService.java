package com.timss.purchase.service;

import java.util.List;

import com.timss.purchase.bean.PurVendor;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendorService.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
public interface PurVendorService {
    /**
     * @description:获取供应商列表
     * @author: 890166
     * @createDate: 2014-6-17
     * @param userinfo
     * @param purVendor
     * @return
     * @throws Exception
     */
    Page<PurVendor> queryPurVendor(UserInfo userinfo, PurVendor purVendor) throws Exception;

    /**
     * @description:根据companyNo找到供应商
     * @author: 890166
     * @createDate: 2014-6-19
     * @param companyNo
     * @return
     * @throws Exception
     */
    List<PurVendor> queryPurVendorDetailByCompanyNo(String companyNo, String siteId) throws Exception;

    /**
     * @description:查询公司类型
     * @author: 890166
     * @createDate: 2014-6-19
     * @return
     * @throws Exception
     */
    /*
     * List<Map<String, String>> queryCompanyType(String siteId) throws
     * Exception;
     */

    /**
     * @description:提供过个companyNo查询返回多个供应商
     * @author: 890166
     * @createDate: 2014-7-22
     * @param companyNos
     * @param siteId
     * @return
     * @throws Exception :
     */
    List<PurVendor> queryPurVendorBycompanyNos(List<String> companyNos, String siteId) throws Exception;

    /**
     * @description:PurVendor的save和update操作
     * @author: 890166
     * @createDate: 2014-6-19
     * @param purVendor
     * @return
     * @throws Exception
     */
    int purVendorSaveOrUpdate(UserInfoScope userInfo, PurVendor purVendor) throws Exception;

    /**
     * @description:查询供应商信息
     * @author: 890166
     * @createDate: 2014-7-1
     * @param userInfo
     * @return
     * @throws Exception
     */
    Page<PurVendor> queryCompanyList(UserInfoScope userInfo, PurVendor purVendor) throws Exception;

}
