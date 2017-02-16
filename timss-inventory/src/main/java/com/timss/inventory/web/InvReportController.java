package com.timss.inventory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyController.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invreport")
public class InvReportController {

    @Autowired
    private ItcMvcService itcMvcService;

    /**
     * 查询采购申请列表数据
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryInvOutInReport", method = RequestMethod.GET)
    public ModelAndView queryInvOutInReport() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        ModelAndView mav = new ModelAndView( "/invreport/invOutInReport.jsp" );
        mav.addObject( "siteId", userInfo.getSiteId() );
        mav.addObject( "type", userInfo.getParam( "type" ) );
        return mav;
    }

    /**
     * @description:跳转到库存ABC报表
     * @author: yuanzh
     * @createDate: 2016-1-5
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryAbcInReport", method = RequestMethod.GET)
    public ModelAndView queryAbcInReport() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        ModelAndView mav = new ModelAndView( "/invreport/invAbcReport.jsp" );
        mav.addObject( "siteId", userInfo.getSiteId() );
        return mav;
    }

}
