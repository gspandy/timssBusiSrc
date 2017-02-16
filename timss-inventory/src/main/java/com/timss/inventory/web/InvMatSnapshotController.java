package com.timss.inventory.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.service.InvMatSnapshotService;
import com.timss.inventory.vo.InvMatSnapshotDetailVO;
import com.timss.inventory.vo.InvMatSnapshotVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotController.java
 * @author: 890166
 * @createDate: 2014-11-20
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmatsnapshot")
public class InvMatSnapshotController {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private InvMatSnapshotService invMatSnapshotService;

    /**
     * @description:跳转页面
     * @author: 890166
     * @createDate: 2014-11-24
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatSnapshotList", method = RequestMethod.GET)
    public String invMatApplyList() {
        return "/invmatsnapshot/invMatSnapshotList.jsp";
    }

    /**
     * @description:表单页面跳转
     * @author: 890166
     * @createDate: 2014-11-24
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatSnapshotDetailList", method = RequestMethod.GET)
    public ModelAndView invMatSnapshotDetailList() throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatsnapshot/invMatSnapshotDetailList.jsp" );

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        mav.addObject( "siteid", siteId );

        return mav;
    }

    /**
     * @description:查询主单数据
     * @author: 890166
     * @createDate: 2014-11-24
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatSnapshotList", method = RequestMethod.POST)
    public Page<InvMatSnapshotVO> queryMatSnapshotList(String search) throws Exception {
        InvMatSnapshotVO imsv = new InvMatSnapshotVO();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            imsv = JsonHelper.fromJsonStringToBean( search, InvMatSnapshotVO.class );
        }
        return invMatSnapshotService.queryMatSnapshotList( userInfo, imsv );
    }

    /**
     * @description:查询子单数据
     * @author: 890166
     * @createDate: 2014-11-24
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/quickMatSnapshotSearch", method = RequestMethod.POST)
    public Page<InvMatSnapshotDetailVO> quickMatSnapshotSearch(String search, String quickSearch, String imsid)
            throws Exception {
        quickSearch = URLDecoder.decode( quickSearch, "UTF-8" );

        InvMatSnapshotDetailVO imsdv = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        if ( StringUtils.isNotBlank( search ) ) {
            imsdv = JsonHelper.fromJsonStringToBean( search, InvMatSnapshotDetailVO.class );
        }

        return invMatSnapshotService.quickMatSnapshotSearch( userInfo, imsdv, quickSearch, imsid );
    }

    /**
     * @description: 保存库存快照
     * @author: 890166
     * @createDate: 2014-11-20
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/saveAsSnapshot", method = RequestMethod.POST)
    public Map<String, Object> saveAsSnapshot(String remark, String type) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        paramMap.put( "remark", remark );
        paramMap.put( "type", type );

        boolean flag = invMatSnapshotService.saveAsSnapshot( userInfo, paramMap );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

}
