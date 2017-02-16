package com.timss.inventory.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.dao.InvStocktakingDetailDao;
import com.timss.inventory.service.InvStocktakingDetailService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvStocktakingDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDetailServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvStocktakingDetailServiceImpl")
public class InvStocktakingDetailServiceImpl implements InvStocktakingDetailService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvStocktakingDetailDao invStocktakingDetailDao;

    /**
     * @description: 查询表单中列表的详细信息
     * @author: 890166
     * @createDate: 2014-10-8
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvStocktakingDetailVO> queryStocktakingDetailList(UserInfoScope userInfo, String istid)
            throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvStocktakingDetailVO> page = scope.getPage();

        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );

        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "istId", istid );
        List<InvStocktakingDetailVO> ret = invStocktakingDetailDao.queryStocktakingDetailList( page );
        page.setResults( ret );
        return page;
    }
}
