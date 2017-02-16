package com.timss.inventory.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvMatReturns;
import com.timss.inventory.service.InvMatReturnsService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.ReflectionUtil;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsController.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmatreturns")
public class InvMatReturnsController {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvMatReturnsService invMatReturnsService;

    /**
     * @description: 列表页面跳转
     * @author: 890166
     * @createDate: 2015-3-12
     * @return:
     */
    @RequestMapping(value = "/invMatReturnsList", method = RequestMethod.GET)
    public String invMatReturnsList() {
        return "/invmatreturns/invMatReturnsList.jsp";
    }

    /**
     * @description: 表单页面跳转
     * @author: 890166
     * @createDate: 2015-3-12
     * @return:
     */
    @RequestMapping(value = "/invMatReturnsForm", method = RequestMethod.GET)
    @ReturnEnumsBind("INV_RETURNOPER_TYPE")
    public ModelAndView invMatReturnsForm() throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatreturns/invMatReturnsForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        mav.addObject( "siteid", userInfo.getSiteId() );
        return mav;
    }

    /**
     * @description: 选取退换货物资
     * @author: 890166
     * @createDate: 2015-3-18
     * @return:
     */
    @RequestMapping(value = "/invMatReturnsItemList", method = RequestMethod.GET)
    public String invMatReturnsItemList() {
        return "/invmatreturns/invMatReturnsItemList.jsp";
    }

    /**
     * @description:查询全部被退货的物资
     * @author: 890166
     * @createDate: 2015-3-12
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryAllReturnItem", method = RequestMethod.POST)
    public Page<InvMatReturnsVO> queryAllReturnItem(String schfield) throws Exception {
        String schField = URLDecoder.decode( schfield, "UTF-8" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatReturnsService.queryAllReturnItem( userInfo, schField );
    }

    /**
     * @description: 表单页面列表查询
     * @author: 890166
     * @createDate: 2015-3-16
     * @param imrsid
     * @return
     * @throws Exception :(这个请求，好像没有地方调用过啊？wzh)
     */
    @RequestMapping(value = "/queryMatReturnsDetailList", method = RequestMethod.POST)
    public Page<InvMatReturnsDetailVO> queryMatReturnsDetailList(String imrsid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatReturnsService.queryMatReturnsDetailList( userInfo, imrsid );
    }

    /**
     * @description: 查询表单详细信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param imrsid
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryMatReturnsForm", method = RequestMethod.POST)
    public InvMatReturnsVO queryMatReturnsForm(String imrsid) throws Exception {
        InvMatReturnsVO imrv = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvMatReturnsVO> imrvList = invMatReturnsService.queryMatReturnsForm( userInfo, imrsid );
        if ( null != imrvList && !imrvList.isEmpty() ) {
            imrv = imrvList.get( 0 );
        }
        return imrv;
    }

    /**
     * @description: 弹出页面查询可退回物资
     * @author: 890166
     * @createDate: 2015-3-18
     * @param userInfo
     * @param invMatReturnsDetailVO
     * @return
     * @throws Exception :(这个请求，好像没有地方调用过啊？wzh)
     */
    @RequestMapping(value = "/queryMatReturnsItemList", method = RequestMethod.POST)
    public Page<InvMatReturnsDetailVO> queryMatReturnsItemList(String search) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvMatReturnsDetailVO invMatReturnsDetailVO = null;
        if ( StringUtils.isNotBlank( search ) ) {
            invMatReturnsDetailVO = JsonHelper.fromJsonStringToBean( search, InvMatReturnsDetailVO.class );
        }
        return invMatReturnsService.queryMatReturnsItemList( userInfo, invMatReturnsDetailVO );
    }

    /**
     * @description:删除退换货记录
     * @author: 890166
     * @createDate: 2015-3-12
     * @param listData
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/deleteMatReturns", method = RequestMethod.POST)
    public Map<String, Object> deleteMatReturns(String listData) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvMatReturnsVO> imrvList = JsonHelper.toList( listData, InvMatReturnsVO.class );
        boolean flag = invMatReturnsService.deleteMatReturns( userInfo, imrvList );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description:保存退换货信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @param formData
     * @param listData
     * @param imrsid
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/saveMatReturns", method = RequestMethod.POST)
    public Map<String, Object> saveMatReturns(String formData, String listData, String imrsid,
            String pruorderno) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        Map<String, Object> reMap = new HashMap<String, Object>();

        boolean flag = true;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "imrsid", imrsid );
        paramMap.put( "pruorderno", pruorderno );  //对应的采购单的编号
        paramMap.put( "rebackType", "returns" );
        
        InvMatReturnsVO imrv = JsonHelper.fromJsonStringToBean( formData, InvMatReturnsVO.class );
        paramMap.put( "sheetno", imrv.getSheetno() );  //物资接收单code
        InvMatReturns imr = (InvMatReturns) ReflectionUtil.conventBean2Bean( imrv, new InvMatReturns() );
        List<InvMatReturnsDetailVO> imrdList = CommonUtil.conventToInvMatReturnsDetailVOList( listData );
        reMap = invMatReturnsService.saveMatReturns( userInfo, imr, imrdList, paramMap );

        flag = Boolean.valueOf( reMap.get( "flag" ) == null ? "false" : String.valueOf( reMap.get( "flag" ) ) );
        String imrsId = String.valueOf( reMap.get( "imrsid" ) == null ? "" : String.valueOf( reMap.get( "imrsid" ) ) );
        result.put( "imrsid", imrsId );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }
}
