package com.timss.inventory.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.timss.asset.vo.AssetVo;
import com.timss.inventory.bean.InvWarehouse;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.service.InvMatReturnsService;
import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.service.InvWarehouseService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.timss.purchase.service.PurApplyService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailController.java
 * @author: 890166
 * @createDate: 2014-7-15
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "inventory/invmattrandetail")
public class InvMatTranDetailController {

    @Autowired
    private InvMatTranDetailService invMatTranDetailService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvWarehouseService invWarehouseService;
    @Autowired
    private InvMatReturnsService invMatReturnsService;
    @Autowired
    private InvPubInterface invPubInterface;
    @Autowired
    private PurApplyService purApplyService;
    @Autowired
    private InvMatApplyDetailService invMatApplyDetailService;
    /**
     * 库存查询中查询仓库情况
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-15
     * @param itemCode
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryStockInfo", method = RequestMethod.POST)
    public Page<InvMatTranDetailVO> queryStockInfo(String itemCode) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        InvWarehouse iw = null;
        String wareId = null;
        String cateId = userInfo.getParam( "cateId" );
        if ( cateId.indexOf( "ICI" ) > -1 ) {
            iw = invWarehouseService.queryWarehouseByCategoryId( userInfo, cateId ).get( 0 );
            wareId = iw.getWarehouseid();
        }
        Page<InvMatTranDetailVO> page = invMatTranDetailService.queryStockInfo( userInfo, itemCode, wareId, cateId );
        return page;
    }

    @RequestMapping(value = "/queryAlreadyOut", method = RequestMethod.POST)
    public Page<InvMatTranDetailVO> queryAlreadyOut(String imaid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Page<InvMatTranDetailVO> page = invMatTranDetailService.queryAlreadyOut( userInfo, imaid );
        return page;
    }

    // TODO BY AHUA 已经退库的记录列表,
    @RequestMapping(value = "/queryRefundDetail", method = RequestMethod.POST)
    public Map<String, Object> queryRefundDetail(String imaid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<InvMatTranDetailVO> list = invPubInterface.queryAlreadyRefunding( imaid ).getResults();
        StringBuilder jsonStr = new StringBuilder();
        if ( null != list && !list.isEmpty() ) {
            for ( InvMatTranDetailVO imrs : list ) {
                jsonStr.append( JsonHelper.fromBeanToJsonString( imrs ) ).append( ";" );
            }
            String json = jsonStr.toString();
            if ( json.length() > 0 ) {
                json = json.substring( 0, json.length() - 1 );
            }
            resultMap.put( "listdata", json );
        }

        String flag = invPubInterface.queryRefundingBtnIsHide( imaid );
        resultMap.put( "refundable", flag );
        return resultMap;
    }

    /**
     * 查看具体仓库操作信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-15
     * @param itemCode
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryStockOperInfo", method = RequestMethod.POST)
    public Page<InvMatTranDetailVO> queryStockOperInfo(String search, String itemCode) throws Exception {
        InvMatTranDetailVO imtdv = null;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        // 若表头查询参数不为空
        if ( StringUtils.isNotBlank( search ) ) {
            imtdv = JsonHelper.fromJsonStringToBean( search, InvMatTranDetailVO.class );
        }

        String cateId = userInfo.getParam( "cateId" );
        if ( cateId.indexOf( "ICI" ) > -1 ) {
            InvWarehouse iw = invWarehouseService.queryWarehouseByCategoryId( userInfo, cateId ).get( 0 );
            if ( null == imtdv ) {
                imtdv = new InvMatTranDetailVO();
            }
            imtdv.setWarehouseid( iw.getWarehouseid() );
            imtdv.setInvcateid( cateId );
        }

        return invMatTranDetailService.queryStockOperInfo( userInfo, itemCode, imtdv );
    }

    /**
     * 表单中列表数据查询
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-23
     * @param search
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryMatTranDetail", method = RequestMethod.POST)
    public Page<InvItemVO> queryMatTranDetail(String imtid, String openType) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatTranDetailService.queryMatTranDetail( userInfo, imtid, openType );
    }

    /**
     * @description:查询可以退货的明细 （过滤掉货退完了的明细记录）或者查询退库记录（imrsid不为空时）
     * @author: 890152
     * @createDate: 2015-9-23
     * @param imtid
     * @param openType
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatReturnAbleDetail", method = RequestMethod.POST)
    public Map<String, Object> queryMatReturnAbleDetail(@RequestParam String imtid, @RequestParam String imrsid,
            @RequestParam String openType) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Page<InvItemVO> result = new Page<InvItemVO>();
        List<InvItemVO> resultlist = new LinkedList<InvItemVO>();
        String returnReason = "";
        // 如果是查看详情
        if ( imrsid != null && !"".equals( imrsid ) ) {
            List<InvMatReturnsVO> templist = invMatReturnsService.queryMatReturnsForm( userInfo, imrsid );
            imtid = templist.get( 0 ).getImtid(); // 接收单id
            returnReason = templist.get( 0 ).getReturnReason(); // 退货原因
            // 已退货明细
            resultlist = invMatReturnsService.queryMatReturnsDetailListByImtId( userInfo, imtid,
                    templist.get( 0 ).getImrsid() ).getResults();
            setPurchaseApplySheetno(resultlist);
        } else {
            // 物资接收明细
            result = invMatTranDetailService.queryMatTranDetail( userInfo, imtid, openType );
            List<InvItemVO> imtDetailList = result.getResults();
            setPurchaseApplySheetno(imtDetailList);
            // 已退货明细
            List<InvItemVO> imrsDetailList = invMatReturnsService.queryMatReturnsDetailListByImtId( userInfo, imtid,
                    null ).getResults();
            // 过滤后的结果
            resultlist = filterDetail( imtDetailList, imrsDetailList );
        }
        resultMap.put( "list", resultlist );
        resultMap.put( "imtid", imtid );
        resultMap.put( "returnReason", returnReason );
        return resultMap;
    }

    /**
     * @description: 根据采购申请单Id设置采购申请编号
     * @author: 890152
     * @createDate: 2016-6-28
     * @param imtDetailList:
     */
    private void setPurchaseApplySheetno(List<InvItemVO> imtDetailList) {
        for ( InvItemVO invItemVO : imtDetailList ) {
            String applyIdString = invItemVO.getPuraId();
            String applySheetNo = purApplyService.queryPurApplyBySheetId(applyIdString).getSheetno();
            invItemVO.setPuraNo( applySheetNo );
        }
        
    }

    /**
     * @description:
     * @author: 890152
     * @createDate: 2015-9-23
     * @param imtDetailList 物资接收单的明细列表
     * @param imrsDetailList 物资接收单对应的物资退货明细列表
     * @return:
     */
    private List<InvItemVO> filterDetail(List<InvItemVO> imtDetailList, List<InvItemVO> imrsDetailList) {
        List<InvItemVO> ret = new LinkedList<InvItemVO>();
        for ( InvItemVO imtBean : imtDetailList ) {
            String imtItemCode = imtBean.getItemcode(); // 物资编码
            String imtWarehouseId = imtBean.getWarehouseid();// 仓库id
            BigDecimal imtItemNum = imtBean.getBatchstockqty(); // 此接收单对应的 入库数量
                                                                // ；退货只能退这张接收单接收的货
            String puraId1 = imtBean.getPuraId();//对应的采购申请单id
            imtBean.setReturnableqty( imtItemNum ); // 默认设置可退货数量为已入库数量
            BigDecimal imrsItemNum = new BigDecimal( 0 ); // 退货物资数量
            boolean flag = false; // 对应的物资是否有退货记录
            // 没有退货记录
            if ( imrsDetailList.size() == 0 ) {
                ret.add( imtBean );
            } else { // 有退货记录
                for ( int i = 0; i < imrsDetailList.size(); i++ ) {
                    InvItemVO imrsBean = imrsDetailList.get( i );
                    String imrsItemCode = imrsBean.getItemcode(); // 物资编码
                    String imrsWarehouseId = imrsBean.getWarehouseid();// 仓库id
                    String puraId2 = imrsBean.getPuraId();  //对应的采购申请单id
                    boolean flag3 = puraId2.equals( puraId1 ) && imrsItemCode.equals( imtItemCode ) && imrsWarehouseId.equals( imtWarehouseId ) ;
                    if (flag3 ) { // 物资编码相同 && 对应同一个采购申请
                        flag = true;
                        imrsItemNum = imrsItemNum.add( imrsBean.getReturnQty() ); // 同类物资、对应同一申请单
                                                                                  // 退货数量累加
                    } else {
//                        if ( i == imrsDetailList.size()-1 && flag == false ) { //比较完， 物资接收单里有的记录，退货单里面没有
//                            ret.add( imtBean );
//                        }
                    }
                    // 同一类物资的所有退货统计完毕，还小于接收的数量，则可以退货
                    boolean flag2 = (i == imrsDetailList.size() - 1) && (imrsItemNum.compareTo( imtItemNum ) == -1);
                    // imrsItemNum.compareTo( new BigDecimal(0) ) == 1

                    if ( flag2 ) {
                        imtBean.setReturnableqty( imtItemNum.subtract( imrsItemNum ) );
                        ret.add( imtBean );
                    }
                }
            }

        }
        return ret;
    }
    
//    private List<InvItemVO> filterDetail(List<InvItemVO> imtDetailList, List<InvItemVO> imrsDetailList) {
//        List<InvItemVO> ret = new LinkedList<InvItemVO>();
//        for ( InvItemVO imtBean : imtDetailList ) {
////            BigDecimal imtItemNum = imtBean.getBatchstockqty(); // 此接收单对应的 入库数量
//            BigDecimal imtIteminNum = imtBean.getLaststockqty(); // 此接收单对应的已 入库数量       
//            if(imtIteminNum.compareTo( BigDecimal.valueOf(0) ) != 0){
//                imtBean.setReturnableqty( imtIteminNum ); // 设置可退货数量
//                ret.add( imtBean );
//            }
//            
//        }
//        return ret;
//    }
    

    /**
     * @description:查询退货的明细
     * @author: 890152
     * @createDate: 2015-9-23
     * @param imtid
     * @return
     * @throws Exception:
     */
    @RequestMapping(value = "/queryMatReturnDetail", method = RequestMethod.POST)
    public Page<InvItemVO> queryMatReturnDetail(String imtid) throws Exception {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        return invMatReturnsService.queryMatReturnsDetailListByImtId( userInfo, imtid, null );
    }

    /**
     * @description: 解除绑定物资类型
     * @author: 890166
     * @createDate: 2015-1-4
     * @param search
     * @param itemCode
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/deleteBindWarehouse", method = RequestMethod.POST)
    public Map<String, Object> deleteBindWarehouse(String iwiid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean flag = true;
        flag = invMatTranDetailService.deleteBindWarehouse( iwiid );
        if ( flag ) {
            result.put( "result", "success" );
        } else {
            result.put( "result", "false" );
        }
        return result;
    }
    
    /**
     * @description: 获取物资资产化信息
     * @author: 890199
     * @createDate: 2016-11-28
     * @param imtdId
     * @return
     * @throws Exception :
     */
    @RequestMapping(value = "/queryMatTranDetailAssetInfo", method = RequestMethod.POST)
    public Map<String, Object>queryMatApplyDetailAssetInfo(String imtdId, String batchstockqty, String outterid) throws Exception{
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    	Map<String, Object> map = new HashMap<String, Object>();
    	int assetTimes = invMatTranDetailService.queryAssetApplyByImtdId(imtdId);
    	float batchStockqty = Float.parseFloat(batchstockqty) ;
    	
    	//校验选中物资的资产化次数是否超过接收数量
    	if(assetTimes+1 > (int)batchStockqty){
    		map.put("result", "error");
    		map.put( "msg", "该物资资产化申请次数不能超过接收数量" );
            return map;
    	}
    	
    	if(outterid != null){
    		Map<String, Object> poInfo = invMatApplyDetailService.queryPoInfoByPoId(userInfo, outterid);
            map.put( "companyName", poInfo.get("companyName") );
            map.put( "companyTel", poInfo.get("companyTel") );
            map.put( "companyContact", poInfo.get("companyContact") );
            //map.put( "purchaseDate", poInfo.get("purchaseDate") );
    	}
    	
    	map.put("result","success");
    	return map;
    	
    }
    
    /**
     * @description:获取物资资产化记录
     * @author: 890199
     * @createDate: 2016-12-01
     * @param imtdId
     * @return
     * @throws Exception:
     */
    @ReturnEnumsBind("AST_APPLY_STATUS") 
    @RequestMapping(value = "/queryMatTranAssetRecord", method = RequestMethod.POST)
    public Page<AssetVo> queryMatTranAssetRecord(String imtid) throws Exception{
    	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    	Page<AssetVo> page = userInfo.getPage();
    	String pageSize = CommonUtil.getProperties("pageSize");
    	page.setPageSize(Integer.valueOf(pageSize));
    	List<AssetVo> assetList = invMatTranDetailService.queryRelateAssetByImtId(imtid);
    	//String assetListData = JsonHelper.fromBeanToJsonString(assetList);
    	page.setResults(assetList);
    	page.setParameter("result","success");
    	page.setTotalRecord(assetList.size());
    	return page;
    }
}
