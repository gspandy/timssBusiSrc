package com.timss.inventory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.inventory.service.InvStocktakingDetailService;
import com.timss.inventory.vo.InvStocktakingDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDetailController.java
 * @author: 890166
 * @createDate: 2014-10-8
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invstocktakingdetail")
public class InvStocktakingDetailController {

    /**
     * service 注入
     */
    @Autowired
    private InvStocktakingDetailService invStocktakingDetailService;

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * @description:查询表单中列表的详细信息
     * @author: 890166
     * @createDate: 2014-10-8
     * @param istid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryStocktakingDetail", method = RequestMethod.POST)
    public Page<InvStocktakingDetailVO> queryStocktakingDetail(String istid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invStocktakingDetailService.queryStocktakingDetailList( userInfo, istid );
    }
}
