package com.timss.inventory.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.inventory.service.InvMatTranService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranController.java
 * @author: 890166
 * @createDate: 2014-7-18
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmattran")
public class InvMatTranController {

    @Autowired
    private InvMatTranService invMatTranService;

    @Autowired
    private InvMatTranDetailService invMatTranDetailService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private HomepageService homepageService;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvMatTranController.class );

    /**
     * @description:列表页面跳转
     * @author: 890166
     * @createDate: 2014-7-22
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/invMatTranList", method = RequestMethod.GET)
    public String invMatTranList() {
        return "/invmattran/invMatTranList.jsp";
    }

    /**
     * @description:跳转表单
     * @author: 890166 
     * @createDate: 2014-7-23
     * @return
     * @throws Exception
     */
    @ReturnEnumsBind("AST_APPLY_STATUS")
    @RequestMapping(value = "/invMatTranForm", method = RequestMethod.GET)
    public ModelAndView invMatTranForm() {
        ModelAndView mav = new ModelAndView( "/invmattran/invMatTranForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        mav.addObject( "siteid", userInfo.getSiteId() );
        return mav;
    }

    /**
     * @description: 退货 跳转页面
     * @author: 890152
     * @createDate: 2015-9-18
     * @return:
     */
    @RequestMapping(value = "/invMatReturnsForm", method = RequestMethod.GET)
    public ModelAndView invMatRefundForm() {
        ModelAndView mav = new ModelAndView( "/invmatreturns/invMatReturnsForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        mav.addObject( "siteid", userInfo.getSiteId() );
        return mav;
    }

    /**
     * @description:采购单编号页面跳转
     * @author: 890166
     * @createDate: 2014-7-23
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/purOrderNoList", method = RequestMethod.GET)
    public String purOrderNoList() {
        return "/invmattran/purOrderNoList.jsp";
    }

    /**
     * @description:物资出入库查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/invMatTranInfoList", method = RequestMethod.GET)
    public String invMatTranInfoList() {
        return "/invmattran/invMatTranInfoList.jsp";
    }

    /**
     * @description:查询列表数据
     * @author: 890166
     * @createDate: 2014-7-23
     * @param search
     * @param cateId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMatTranList", method = RequestMethod.POST)
    public Page<InvMatTranVO> queryMatTranList(String search) throws Exception {
        InvMatTranVO imt = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            imt = JsonHelper.fromJsonStringToBean( search, InvMatTranVO.class );
        }
        return invMatTranService.queryMatTranList( userInfo, imt );
    }

    /**
     * @description:物资接收列表快速查询
     * @author: 890166
     * @createDate: 2014-9-1
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/quickSearch", method = RequestMethod.POST)
    public Page<InvMatTranVO> quickSearch(String search, String schfield) throws Exception {
        InvMatTranVO imt = null;

        String schField = URLDecoder.decode( schfield, "UTF-8" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        if ( StringUtils.isNotBlank( search ) ) {
            imt = JsonHelper.fromJsonStringToBean( search, InvMatTranVO.class );
        }
        return invMatTranService.quickSearch( userInfo, imt, schField );
    }

    /**
     * @description:加载表单数据
     * @author: 890166
     * @createDate: 2014-7-23
     * @param imtid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryMatTranForm", method = RequestMethod.POST)
    public InvMatTranVO queryMatTranForm(String imtid) throws Exception {
        InvMatTranVO imtv = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<InvMatTranVO> imtList = invMatTranService.queryMatTranForm( userInfo, imtid );
        if ( null != imtList && !imtList.isEmpty() ) {
            imtv = imtList.get( 0 );
        }
        return imtv;
    }

    /**
     * @description:获取采购单号列表方法
     * @author: 890166
     * @createDate: 2014-7-23
     * @param search
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryPurOrderList", method = RequestMethod.POST)
    public Page<MTPurOrderVO> queryPurOrderList(String search) throws Exception {
        MTPurOrderVO mtpo = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            mtpo = JsonHelper.fromJsonStringToBean( search, MTPurOrderVO.class );
        }
        return invMatTranService.queryPurOrderList( userInfo, mtpo );
    }

    /**
     * @description:保存
     * @author: 890166
     * @createDate: 2014-7-24
     * @param formData
     * @param listData
     * @param type
     * @param oper
     * @param sheetId
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveMatTran", method = RequestMethod.POST)
    public Map<String, Object> saveMatTran(String formData, String listData) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        boolean flag = true;

        // 将表单的数据转换成实体类
        InvMatTranVO imtv = JsonHelper.fromJsonStringToBean( formData, InvMatTranVO.class );
        imtv.setCreateuser( userInfo.getUserId() );
        imtv.setModifyuser( userInfo.getUserId() );
        imtv.setOperuser( userInfo.getUserId() );

        Map<String, Object> mapData = CommonUtil.conventJsonToIMTList( listData );

        flag = invMatTranService.saveMatTran( userInfo, imtv, mapData, paramMap );
        if ( flag ) {
            result.put( "result", "success" );
            result.put( "imtid", imtv.getImtid() );
            result.put( "sheetno", imtv.getSheetno() );
            result.put( "newImtid", paramMap.get( "newImtid" ) );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description:物资接收后更改采购申请单中的采购状态
     * @author: 890166
     * @createDate: 2014-10-17
     * @param imtid
     * @param status
     * @return:
     */
    @RequestMapping(value = "/updatePurApplyStatus", method = RequestMethod.POST)
    public Map<String, Object> updatePurApplyStatus(String imtid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        boolean flag = invMatTranService.updatePurApplyStatus( imtid );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }

    /**
     * @description:自动触发物资接收
     * @author: 890166
     * @createDate: 2014-8-8
     * @param appSheetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/autoGenerateMatTran", method = RequestMethod.POST)
    public Map<String, Object> autoGenerateMatTran(String appSheetId) throws Exception {
        Map<String, Object> result = invMatTranService.autoGenerateMatTran( appSheetId, null );
        return result;
    }

    /**
     * @description:提示查询用户信息
     * @author: 890166
     * @createDate: 2014-8-5
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/getUserHint", method = RequestMethod.POST)
    public ModelAndViewAjax getUserHint() throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        String keyWord = userInfo.getParam( "kw" );
        if ( keyWord != null || !"".equals( keyWord ) ) {
            result = invMatTranService.getUserHint( userInfo, keyWord );// 可性能优化，前台只展示前十条，可考虑只查询10条，已优化
        }
        return itcMvcService.jsons( result );
    }

    /**
     * @description:删除待办
     * @author: 890166
     * @createDate: 2014-8-28
     * @param appSheetId
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/deleteMatTranDB", method = RequestMethod.POST)
    public void deleteMatTranDB(String dbId) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String procId = null;
        // 通过processid找到单条数据进行删除
        /*
         * invMatTranDetailService.deleteNoMainDetailDataBydbId( dbId );
         * homepageService.Delete( dbId, userInfo );
         */
        List<InvMatTran> imtList = invMatTranService.queryInvMatTranByPoNo( dbId, userInfo.getSiteId() );
        if ( null != imtList && !imtList.isEmpty() ) {
            for ( InvMatTran imt : imtList ) {
                procId = imt.getProcessinstid();
                invMatTranDetailService.deleteNoMainDetailDataBydbId( procId );
                homepageService.Delete( procId, userInfo );
            }
        }
    }

    /**
     * @description:物资出库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryOutMatTran", method = RequestMethod.POST)
    public Page<InvMatTranVO> queryOutMatTran(String search) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvMatTranVO imtv = null;
        if ( StringUtils.isNotBlank( search ) ) {
            imtv = JsonHelper.fromJsonStringToBean( search, InvMatTranVO.class );
        }
        return invMatTranService.queryOutMatTran( userInfo, imtv );
    }

    /**
     * @description: 物资入库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @param search
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryInMatTran", method = RequestMethod.POST)
    public Page<InvMatTranVO> queryInMatTran(String search) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvMatTranVO imtv = null;
        if ( StringUtils.isNotBlank( search ) ) {
            imtv = JsonHelper.fromJsonStringToBean( search, InvMatTranVO.class );
        }
        return invMatTranService.queryInMatTran( userInfo, imtv );
    }
}
