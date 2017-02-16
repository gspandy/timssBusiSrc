package com.timss.inventory.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.inventory.bean.InvMatReturns;
import com.timss.inventory.service.InvMatReturnsService;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.ReflectionUtil;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
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
@RequestMapping(value = "inventory/invmatrefund")
public class InvMatRefundController {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvPubInterface invPubInterface;

    @Autowired
    private InvMatReturnsService invMatReturnsService;

    /**
     * @description: 查看某个物资申请单的所有出库明细信息 或 某条退库记录的明细
     * @author: 890152
     * @createDate: 2015-9-24
     * @param imaid 申请单ID
     * @param imrid 退库记录id
     * @param embed 是否是嵌入页面 1：嵌入； 0:非嵌入
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatRefundDetailList", method = RequestMethod.POST)
    public Page<InvMatReturnsDetailVO> queryMatRefundDetailList(@RequestParam String imaid,
            @RequestParam String imrsid, @RequestParam String sheetno, @RequestParam String embed) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Page<InvMatReturnsDetailVO> page = userInfo.getPage();

        // 如果是查看详情
        if ( null != imrsid && !"".equals( imrsid ) ) {
            List<InvMatReturnsVO> templist = invMatReturnsService.queryMatReturnsForm( userInfo, imrsid );
            imaid = templist.get( 0 ).getImtid();
            sheetno = templist.get( 0 ).getImrsno();
        }

        List<InvMatReturnsDetailVO> imrdvList = queryRefundDetailList( imaid, imrsid, sheetno, embed );
        page.setResults( imrdvList );
        return page;
    }

    @RequestMapping(value = "/queryMatRefundDetailList2", method = RequestMethod.POST)
    public Map<String, Object> queryMatRefundDetailList2(@RequestParam String imaid, @RequestParam String imrsid,
            @RequestParam String sheetno, @RequestParam String embed) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String refundReason = null;
        // 如果是查看详情
        if ( null != imrsid && !"".equals( imrsid ) ) {
            List<InvMatReturnsVO> templist = invMatReturnsService.queryMatReturnsForm( userInfo, imrsid );
            imaid = templist.get( 0 ).getImtid();

            refundReason = templist.get( 0 ).getReturnReason();
            resultMap.put( "refundReason", refundReason );

            sheetno = templist.get( 0 ).getImrsno();
        }
        List<InvMatReturnsDetailVO> imrdvList = queryRefundDetailList( imaid, imrsid, sheetno, embed );
        resultMap.put( "imaid", imaid );
        resultMap.put( "list", imrdvList );
        return resultMap;
    }

    /**
     * @description: 获取退货详细信息
     * @author: yuanzh
     * @createDate: 2016-2-4
     * @param imaid
     * @param imrsid
     * @param sheetno
     * @param embed
     * @return:
     */
    private List<InvMatReturnsDetailVO> queryRefundDetailList(String imaid, String imrsid, String sheetno, String embed) {
        List<InvMatReturnsDetailVO> ret2 = new LinkedList<InvMatReturnsDetailVO>();
        if ( "1".equals( embed ) || (imrsid != null && !"".equals( imrsid )) ) { // 查看，从申请单下面的列表点击查看
            // 查询对应的退库单的明细
            List<InvMatReturnsDetailVO> ret = invPubInterface.queryRefundingDetailI( imaid, sheetno );
            for ( InvMatReturnsDetailVO tempbean : ret ) { // 查出来的退库数量，转变为前台的退库数量，字段不对应
                if ( tempbean.getReturnQty().compareTo( new BigDecimal( 0 ) ) == 1 ) {
                    tempbean.setRefundqty( tempbean.getReturnQty() );
                    ret2.add( tempbean );
                }
            }
        } else {
            List<InvMatReturnsDetailVO> ret = invPubInterface.queryRefundingDetailI( imaid, null );
            // 计算可退库量，并过滤掉已领数量为0的物资
            ret2 = filterItems( ret );
        }
        return ret2;
    }

    /**
     * @description:计算可退库量，并过滤掉已领数量为0的物资
     * @author: 890152
     * @createDate: 2015-9-24
     * @param ret
     * @return:
     */
    private List<InvMatReturnsDetailVO> filterItems(List<InvMatReturnsDetailVO> ret) {
        List<InvMatReturnsDetailVO> result = new LinkedList<InvMatReturnsDetailVO>();
        for ( InvMatReturnsDetailVO tempbean : ret ) {
            BigDecimal outQty = tempbean.getOutqty(); // 申请单的 已接收数量
            BigDecimal returnQty = tempbean.getReturnQty(); // 申请单对应的已退库数量
            BigDecimal assetQty = tempbean.getAssetQty(); //已资产化次数
            if ( returnQty.compareTo( outQty ) == 0 ) { // 接收的数量 = 已退库的数量
                continue;
            } else {
                tempbean.setRefundableqty( outQty.subtract( returnQty ).subtract(assetQty) );
                result.add( tempbean );
            }
        }
        return result;
    }

    /**
     * @description:保存信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param formData
     * @param listData
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/commitMatRefund", method = RequestMethod.POST)
    @ValidFormToken
    public Map<String, Object> commitMatRefund(String formData, String listData, String imrsid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>(); // 参数map
        Map<String, Object> reMap = new HashMap<String, Object>();
        boolean flag = true;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        paramMap.put( "imrsid", imrsid );
        paramMap.put( "rebackType", "refund" );

        InvMatReturnsVO imrv = JsonHelper.fromJsonStringToBean( formData, InvMatReturnsVO.class );
        paramMap.put( "sheetno", imrv.getSheetno() ); // 物资申请单code
        imrv.setImtid( imrv.getImaid() ); // 将物资申请单ID赋值给物资接交易单ID
                                          // imtid在退库时存储的是物资申请单id，在退货时存储的是物资接收单id
        imrv.setReturnReason( imrv.getRefundReason() ); // InvMatReturns是退库和退货公用的bean
                                                        // ,其中的returnReason、imtid在不同情况代表不同意思
        InvMatReturns imr = (InvMatReturns) ReflectionUtil.conventBean2Bean( imrv, new InvMatReturns() );
        List<InvMatReturnsDetailVO> imrdList = CommonUtil.conventToInvMatReturnsDetailVOList( listData );
        for ( InvMatReturnsDetailVO tempbean : imrdList ) {
            // 将退库数量的值赋值给returnQty，为了方便之后的vo转化为bean,因为InvMatReturnsDetail中returnQty在退库情况下，代表退库数量，在退货情况下代表退货数量
            tempbean.setReturnQty( tempbean.getRefundqty() );
            tempbean.setImtdid( tempbean.getImadid() ); // 数据库中的imtdid,在退库时对应的是申请单明细id
        }
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

    /**
     * @description: 根据sheetno查ID
     * @author: 890152
     * @createDate: 2015-9-28
     * @param sheetNo
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryRefundIdBySheetNo", method = RequestMethod.POST)
    public Map<String, Object> queryRefundIdBySheetNo(String sheetNo) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvMatReturns tempbean = invMatReturnsService.queryReturnsBySheetNo( userInfo, sheetNo );
        String imrid = tempbean.getImrsid();
        result.put( "imrid", imrid );
        return result;
    }

    @RequestMapping(value = "/invMatRefundForm", method = RequestMethod.GET)
    @ReturnEnumsBind ( "INV_APPLY_TYPE" )
    public ModelAndView invMatRefundForm(@RequestParam String imrsid, @RequestParam String refundReason,
            @RequestParam String sheetno, @RequestParam String imaid, @RequestParam String embed) throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatrefund/invMatRefundForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        mav.addObject( "imrsid", imrsid );
        mav.addObject( "refundReason", refundReason );
        mav.addObject( "sheetno", sheetno );
        mav.addObject( "embed", embed );
        mav.addObject( "siteid", siteId );
        return mav;
    }

    @RequestMapping(value = "/readInvMatRefundForm", method = RequestMethod.GET)
    public ModelAndView invMatRefundForm(@RequestParam String imrsid) throws Exception {
        ModelAndView mav = new ModelAndView( "/invmatrefund/invMatRefundForm.jsp" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfo.getSiteId();
        mav.addObject( "imrsid", imrsid );
        mav.addObject( "siteid", siteId );
        return mav;
    }

}
