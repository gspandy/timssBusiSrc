package com.timss.purchase.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.service.PurInvoiceService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * 
 * @title: 发票
 * @description: {desc}
 * @company: gdyd
 * @className: PurInvoiceController.java
 * @author: fengzt
 * @createDate: 2015年9月17日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("purchase/purInvoice")
public class PurInvoiceController {
    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger(PurInvoiceController.class);

    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PurInvoiceService purInvoiceService;
    
    @Autowired
    private PurPubInterface pubInterface;
    
    /**
     * 
     * @description:发票列表页面
     * @author: fengzt
     * @createDate: 2015年9月17日
     * @return:ModelAndView
     */
    @RequestMapping("/queryAllInvoiceListMenu")
    @ReturnEnumsBind("PUR_INVOICE_STATUS")
    public ModelAndView queryAllInvoiceListMenu(){
        return new ModelAndView("purinvoice/PurInvoice-queryAllInvoiceList.jsp");
    }
    
    /**
     * 
     * @description:发票插入页面
     * @author: fengzt
     * @createDate: 2015年9月17日
     * @return:ModelAndView
     */
    @RequestMapping("/insertInvoiceToPage")
    @ReturnEnumsBind("PUR_INVOICE_STATUS,PUR_ORDER_TAXRATE")
    public ModelAndView insertInvoiceToPage(String contractId, String contractNo ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "contractId", contractId );
        map.put( "contractNo", contractNo );
        return new ModelAndView("purinvoice/PurInvoice-insertInvoice.jsp", map);
    }
    
    /**
     * 
     * @description:跳转到合同物资清单页面
     * @author: fengzt
     * @createDate: 2015年9月17日
     * @return:ModelAndView
     */
    @RequestMapping("/queryInvoiceItemToPage")
    @ReturnEnumsBind("PUR_INVOICE_STATUS,PUR_ORDER_TAXRATE")
    public ModelAndView queryInvoiceItemToPage( String contractId ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "contractId", contractId );
        return new ModelAndView("purinvoice/PurInvoice-queryInvoiceItemList.jsp", map);
    }
    
    /**
     * 
     * @description:发票更新页面
     * @author: fengzt
     * @createDate: 2015年9月17日
     * @return:ModelAndView
     */
    @RequestMapping("/updateInvoiceToPage")
    @ReturnEnumsBind("PUR_INVOICE_STATUS,PUR_ORDER_TAXRATE")
    public ModelAndView updateInvoiceToPage(String id ){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "id", id );
        return new ModelAndView("purinvoice/PurInvoice-updateInvoice.jsp", map);
    }
    
    /**
     * 
     * @description:通过站点查询发票
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @return: Map<String, Object> 
     * @throws Exception 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryInvoiceBySiteId")
    public Page<PurInvoiceBean> queryInvoiceBySiteId( int rows, int page, String search,
            String sort, String order ) throws Exception{
        LOG.debug( "[PurInvoiceController-queryInvoiceBySiteId]查询参数：" + search );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PurInvoiceBean> paramsPage = userInfoScope.getPage();
        
        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
            paramsPage.setSortKey( sort );
            paramsPage.setSortOrder( order );
        } else {
            // 设置默认的排序字段
            paramsPage.setSortKey( "sheetNo" );
            paramsPage.setSortOrder( "desc" );
        }
        // 获取表头搜索
        Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
        if ( fuzzyParams.size() > 0 ) {
            paramsPage.setParams( fuzzyParams );
            //根据发票号或者合同号 （忽略大小写）
            paramsPage = purInvoiceService.queryInvoiceBySearch( paramsPage );
        }else{
            paramsPage = purInvoiceService.queryInvoiceBySiteId( paramsPage );
        }
        return paramsPage;
    }
    
    /**
     * 
     * @description:通过合同ID查询物资清单
     * @author: fengzt
     * @createDate: 2015年9月18日
     * @return: Map<String, Object> 
     * @throws Exception 
     * @throws JsonParseException 
     */
    @RequestMapping("/queryWuziByContractId")
    public Page<PurInvoiceAssetVo> queryWuziByContractId( int rows, int page, String search,
            String sort, String order, String contractId ) throws Exception{
        LOG.debug( "[PurInvoiceController-queryInvoiceBySiteId]查询参数：" + search );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PurInvoiceAssetVo> paramsPage = userInfoScope.getPage();
        paramsPage.setPageSize( 1000 );
        
        // 设置排序内容
        if ( StringUtils.isNotBlank( sort ) ) {
            paramsPage.setSortKey( sort );
            paramsPage.setSortOrder( order );
        } else {
            // 设置默认的排序字段
            paramsPage.setSortKey( "ITEM_CODE" );
            paramsPage.setSortOrder( "desc" );
        }
        // 获取表头搜索
        if ( StringUtils.isNotBlank( search ) ) {
            Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
            paramsPage.setFuzzyParams( fuzzyParams );
        }
        // 加入合同编号
        if ( StringUtils.isNotBlank( contractId ) ) {
            paramsPage.setParameter( "sheetId", contractId );
        }
        
        paramsPage = purInvoiceService.queryWuziByContractId( paramsPage );
        return paramsPage;
    }
    
    /**
     * 
     * @description:插入、更新发票
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param formData
     * @param rowData
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/insertOrUpdateInvoice",method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> insertOrUpdateInvoice( String formData, String rowData ){
        int count = purInvoiceService.insertOrUpdateInvoice( formData, rowData );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    /**
     * 
     * @description:删除发票
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param id
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/deleteInvoiceById")
    public @ResponseBody Map<String, Object> deleteInvoiceById( String id ){
        int count = purInvoiceService.deleteInvoiceById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:发票报账
     * @author: fengzt
     * @createDate: 2015年9月22日
     * @param id
     * @return:Map<String, Object>
     */
    @RequestMapping(value="/updateInvoiceStatus")
    public @ResponseBody Map<String, Object> updateInvoiceStatus( String id ){
        int count = purInvoiceService.updateInvoiceStatus( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if ( count > 0 ) {
            map.put( "result", "success" );
        } else {
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:通过合同编号或合同名称模糊搜索
     * @author: fengzt
     * @createDate: 2015年8月26日
     * @param kw
     * @param ciId
     * @param ciType
     * @return:ModelAndViewAjax
     */
    @RequestMapping(value = "/queryHintContract")
    public ModelAndViewAjax queryHintContract(String kw ) {
        LOG.debug( "[queryHintContract]传入参数 kw = " + kw );
        List<PurOrderVO> result = new ArrayList<PurOrderVO>();
        // 可性能优化，前台只展示前十条，可考虑只查询20条
        if( StringUtils.isNotBlank( kw ) ){
            kw = kw.trim().toUpperCase();
        }
        result = pubInterface.queryPurOrderByIHint( kw );
        
        //list bean transfer map
        List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
        for( PurOrderVO bean : result ){
            String value = bean.getSheetno() + "/" + bean.getSheetname();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "id", bean.getSheetId() );
            map.put( "name", value );
            resultMap.add( map );
        }
        
        return itcMvcService.jsons( resultMap );
    }
    
    /**
     * 
     * @description:根据采购合同id查询采购合同主单信息
     * @author: fengzt
     * @createDate: 2015年9月23日
     * @param contractId
     * @return:PurOrderVO
     */
    @RequestMapping("/queryVoiceAssetByContractId")
    public PurOrderVO queryVoiceAssetByContractId(String contractId){
        PurOrderVO purOrderVO = pubInterface.queryPurOrderVOBySheetId( contractId );
        return purOrderVO;
    }
    
    /**
     * 
     * @description:根据采购合同id查询采购合同物资列表信息
     * @author: fengzt
     * @createDate: 2015年9月23日
     * @param contractId
     * @return:Map
     */
    @RequestMapping("/queryWuziDatagridByContractId")
    public @ResponseBody Map<String, Object> queryWuziDatagridByContractId(String contractId){
        List<PurInvoiceAssetVo> result = purInvoiceService.queryWuziByContractId( contractId );
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", result );
        if( ! result.isEmpty() ){
            dataMap.put( "total", result.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:根据id查询发票主信息
     * @author: fengzt
     * @createDate: 2015年9月23日
     * @param id
     * @return:Map
     */
    @RequestMapping("/queryInvoiceById")
    public @ResponseBody Map<String, Object> queryInvoiceById(String id){
        PurInvoiceBean bean = purInvoiceService.queryInvoiceById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( bean.getId() )){
            map.put( "result", "success" );
            map.put( "bean", bean );
        }else{
            map.put( "result", "fail" );
        }
        return map;
    }
    
    /**
     * 
     * @description:根据发票id查询物资明细
     * @author: fengzt
     * @createDate: 2015年9月23日
     * @param inVoiceId
     * @return:Map
     */
    @RequestMapping("/queryInvoiceItemById")
    public @ResponseBody Map<String, Object> queryInvoiceItemById(String invoiceId){
        List<PurInvoiceAssetVo> vos = purInvoiceService.queryInvoiceItemById( invoiceId );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", vos );
        if( ! vos.isEmpty() ){
            dataMap.put( "total", vos.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:检查发票号在同一个站点下是否存在
     * @author: fengzt
     * @createDate: 
     * @return:BOOLEAN
     */
    @RequestMapping(value = "/queryCheckInvoiceNo")
    public @ResponseBody Boolean queryCheckInvoiceNo( String paramsMap ) {
        JSONObject object = JSONObject.fromObject(paramsMap);
        String invoiceNo = object.get("invoiceNo").toString();
        String id = object.get("id").toString().trim();
        
        List<PurInvoiceBean> result = purInvoiceService.queryCheckInvoiceNo( invoiceNo, id );
        
        if( result != null && result.size() > 0  ){
            return false;
        }
        return true ;    
    }
}
