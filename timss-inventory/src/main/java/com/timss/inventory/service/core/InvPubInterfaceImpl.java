package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.dao.InvMatAcceptDetailDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatReturnsDetailDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 库存对外接口
 * @description: 库存对外接口
 * @company: gdyd
 * @className: InvPubInterfaceImpl.java
 * @author: yuanzh
 * @createDate: 2015-9-24
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Service("InvPubInterfaceImpl")
public class InvPubInterfaceImpl implements InvPubInterface {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvMatTranDao invMatTranDao;

    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;

    @Autowired
    private InvMatReturnsDetailDao invMatReturnsDetailDao;

    @Autowired
    private InvMatAcceptDetailDao invMatAcceptDetailDao;

    @Autowired
    private InvMatMappingDao invMatMappingDao;

    @Autowired
    private InvWarehouseItemDao invWarehouseItemDao;
    
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;

    /**
     * @description: 物资领料页面查询退库列表
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param imaid
     * @return:
     */
    @Override
    public Page<InvMatTranDetailVO> queryAlreadyRefunding(String imaid) {
        UserInfoScope scope = itcMvcService.getUserInfoScopeDatas();
        Page<InvMatTranDetailVO> page = scope.getPage();

        page.setParameter( "siteId", scope.getSiteId() );
        page.setParameter( "imaid", imaid );

        List<InvMatTranDetailVO> ret = invMatTranDetailDao.queryAlreadyRefunding( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:查询退库单页面明细
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param imaid ：领料单id
     * @param imrsno：退库单编码
     * @return:
     */
    @Override
    public List<InvMatReturnsDetailVO> queryRefundingDetailI(String imaid, String imrsno) {
        UserInfoScope scope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "imaid", imaid );
        paramMap.put( "imrsno", imrsno );
        paramMap.put( "siteId", scope.getSiteId() );
        return invMatReturnsDetailDao.queryRefundingDetailI( paramMap );
    }

    /**
     * @description: 判断是否符合显示按钮要求
     * @author: yuanzh
     * @createDate: 2015-9-25
     * @return: 返回Y代表可以隐藏，N代表要展示
     */
    @Override
    public String queryRefundingBtnIsHide(String imaid) {
        UserInfoScope scope = itcMvcService.getUserInfoScopeDatas();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "imaid", imaid );
        paramMap.put( "siteId", scope.getSiteId() );
        return invMatReturnsDetailDao.queryRefundingBtnIsHide( paramMap );
    }

    /**
     * @description: 根据采购合同查询到验收记录明细
     * @author: yuanzh
     * @createDate: 2015-10-30
     * @param poId 采购合同Id
     * @return:
     */
    @Override
    public List<InvMatAcceptDetailVO> queryItem2InvMatAcceptDetail(String poId) {
        UserInfoScope scope = itcMvcService.getUserInfoScopeDatas();
        return invMatAcceptDetailDao.queryItem2InvMatAcceptDetail( scope.getSiteId(), poId );
    }

    /**
     * @description: 保存移库数据到流水表
     * @author: yuanzh
     * @createDate: 2016-1-12
     * @param imTransfer 移库业务表主表信息
     * @param imTfList 移库业务表子表信息
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> saveTransfer2MatTran(InvMatTransfer imTransfer, List<InvMatTransferDetail> imTfList) {
        Map<String, String> reMap = new HashMap<String, String>();

        if ( null != imTfList && !imTfList.isEmpty() ) {
            Map<String, Object> paramMap = null;
            for ( InvMatTransferDetail imTf : imTfList ) {
                // 根据传入的itemcode和siteid查询当前的库存数量
                paramMap = new HashMap<String, Object>();
                paramMap.put( "itemCode", imTf.getItemCode() );
                paramMap.put( "siteId", imTf.getSiteid() );
                paramMap.put( "warehouseId", imTf.getWareHouseId() );
                BigDecimal stockQty = imTf.getStockQty();
                BigDecimal nowQty = imTf.getNowqty();

                // 用当前库存量跟申请转移的库存量经行计较 大于0
                if ( stockQty.compareTo( imTf.getTransferQty() ) >= 0 && nowQty.compareTo( imTf.getTransferQty() ) >= 0) {
                    // 绑定物资的物资类型
                    bindItem2Warehouse( imTf, stockQty );

                    // 先插入出库记录
                    Map<String, String> outMap = trans2Entity( imTransfer, imTf, "Out", null );
                    reMap.putAll( outMap );
                    
                    Map<String, String> pMap = new HashMap<String, String>();
                	pMap.put( "price" , outMap.get("price") );
                	pMap.put( "noTaxPrice" , outMap.get("noTaxPrice") );
                	pMap.put( "tax" , outMap.get("tax") );
                    
                    // 再插入入库记录
                    Map<String, String> inMap = trans2Entity( imTransfer, imTf, "In", pMap );
                    reMap.putAll( inMap );
                } else {
                    reMap.put( "false", "物资编码为：" + imTf.getItemCode() + " 的物资当前库存量小于移库申请的库存量" );
                }
            }
        } else {
            reMap.put( "false", "没有找到任何数据" );
        }
        return reMap;
    }

    /**
     * @description: 将transferDetail实体转换成流水表中数据并保存
     * @author: yuanzh
     * @createDate: 2016-1-12
     * @param imTransfer 移库表主表信息
     * @param imTranferDetail 移库表子表信息
     * @param type In：入库操作 / Out：出库操作
     * @return:
     */
    private Map<String, String> trans2Entity(InvMatTransfer imTransfer, InvMatTransferDetail imTranferDetail,
            String type, Map<String, String> pMap) {
        // 流水表主表
        InvMatTran imTran = null;

        // 执行状态位
        boolean flag = true;

        // 返回信息
        Map<String, String> reMap = new HashMap<String, String>();
        Map<String, String> paramMap = new HashMap<String, String>();
        // 通过移库唯一编码查询流水表看看是否已经存在主表信息
        paramMap.put( "tranType", type == "In" ? "shiftmaterialsIn" : "shiftmaterialsOut" );
        List<InvMatTran> imtList = invMatTranDao.queryInvMatTranBySheetNoAndParam( imTransfer.getImtCode(), paramMap );
        if ( null != imtList && !imtList.isEmpty() ) {
            imTran = imtList.get( 0 );
        } else {
            imTran = new InvMatTran();
            imTran.setSheetno( imTransfer.getImtCode() );

            imTran.setTranType( type == "In" ? "shiftmaterialsIn" : "shiftmaterialsOut" );
            imTran.setWarehouseid( type == "In" ? imTransfer.getWareHouseToId() : imTransfer.getWareHouseFromId() );
            imTran.setOperuser( imTransfer.getModifyuser() );
            imTran.setCheckuser( imTransfer.getModifyuser() );
            imTran.setCreateuser( imTransfer.getModifyuser() );
            imTran.setCreatedate( new Date() );
            imTran.setLotno( new BigDecimal( 0 ) );
            imTran.setSiteId( imTransfer.getSiteid() );
            imTran.setRemark( imTransfer.getRemark() );
            invMatTranDao.insertInvMatTran( imTran );
        }
        // 若主表信息已存在，则插入子表信息
        if ( null != imTran ) {

            InvMatTranDetail imtDetail = new InvMatTranDetail();
            imtDetail.setImtdid( CommonUtil.getUUID() );
            imtDetail.setImtid( imTran.getImtid() );
            imtDetail.setItemid( imTranferDetail.getItemId() );
            imtDetail.setWarehouseid( type == "In" ? imTranferDetail.getToWareHouseId() : imTranferDetail
                    .getWareHouseId() );
            imtDetail.setBinid( type == "In" ? imTranferDetail.getToBinId() : imTranferDetail.getBinId() );
            imtDetail.setLotno( new BigDecimal( 1 ) );
            imtDetail.setInQty( type == "In" ? imTranferDetail.getTransferQty() : new BigDecimal( 0 ) );
            imtDetail.setInUnitid( imTranferDetail.getUnitId() );
            imtDetail.setOutQty( type == "In" ? new BigDecimal( 0 ) : imTranferDetail.getTransferQty() );
            imtDetail.setOutUnitid( imTranferDetail.getUnitId() );
            imtDetail.setCreateuser( imTranferDetail.getCreateuser() );
            imtDetail.setCreatedate( new Date() );
            imtDetail.setSiteId( imTranferDetail.getSiteid() );
            imtDetail.setPrice( imTranferDetail.getPrice() );
            imtDetail.setItemcode( imTranferDetail.getItemCode() );
            imtDetail.setInvcateid( type=="In" ? imTranferDetail.getToCateTypeId() : imTranferDetail.getCateTypeId());
            // 插入子表信息
            int counter = invMatTranDetailDao.insertInvMatTranDetail( imtDetail );
            
            imtDetail.setCanOutQty( type=="In" ? imTranferDetail.getTransferQty() : new BigDecimal( 0 ));
            if(type=="In"){
            	if(pMap.get("price")!=null){
	            	imtDetail.setPrice(new BigDecimal(pMap.get("price")));
	            	imtDetail.setNoTaxPrice( new BigDecimal(pMap.get("noTaxPrice")) );
	            	imtDetail.setTax( new BigDecimal(pMap.get("tax")) );
            	}else{
            		imtDetail.setPrice( new BigDecimal(0) );
	            	imtDetail.setNoTaxPrice( new BigDecimal(0) );
	            	imtDetail.setTax( new BigDecimal(0) );
            	}
            }
            
            // 若插入成功，则插入映射表信息
            if ( counter > 0 ) {
                InvMatMapping imMapping = new InvMatMapping();
                imMapping.setImtdid( imtDetail.getImtdid() );
                imMapping.setOutterid( imTranferDetail.getImtdId() );
                imMapping.setTranType( type == "In" ? "shiftmaterialsIn" : "shiftmaterialsOut" );

                invMatMappingDao.insertInvMatMapping( imMapping );
                
                //插入新的流水和映射关系
                Map<String, Object> outInfo = invRealTimeDataService.insertNewRecAndMap( imtDetail, imMapping, 
                		type=="In" ? "3" : "2" );
                if(type=="Out"){
                	if(outInfo.get("price")!=null){                		
                		reMap.put( "price" , outInfo.get("price").toString() );
                		reMap.put( "noTaxPrice" , outInfo.get("noTaxPrice").toString() );
                		reMap.put( "tax" , outInfo.get("tax").toString() );
                	}
                }
            }
            
        } else {
            reMap.put( "false", "无法生成移出主单数据" );
            flag = false;
        }

        if ( flag ) {
            reMap.put( "success", "成功生成移库流水" );
        }

        return reMap;
    }

    /**
     * @description: 绑定物资的物资类型和仓库
     * @author: user
     * @createDate: 2016-1-13
     * @param imTranferDetail
     * @param stockQty:
     */
    private void bindItem2Warehouse(InvMatTransferDetail imTranferDetail, BigDecimal stockQty) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        List<InvWarehouseItemVO> iwivList = null;

        paramMap.put( "itemid", imTranferDetail.getItemId() );
        paramMap.put( "warehouseid", imTranferDetail.getToWareHouseId() );
        paramMap.put( "siteid", imTranferDetail.getSiteid() );
        iwivList = invWarehouseItemDao.queryInvWarehouseItem( paramMap );

        if ( null == iwivList || iwivList.isEmpty() ) {
            InvWarehouseItem iwi = new InvWarehouseItem();
            iwi.setCreatedate( new Date() );
            iwi.setCreateuser( imTranferDetail.getCreateuser() );
            iwi.setDefBinid( imTranferDetail.getToBinId() );
            iwi.setInvcateid( imTranferDetail.getToCateTypeId() );
            iwi.setItemid( imTranferDetail.getItemId() );
            iwi.setSiteId( imTranferDetail.getSiteid() );
            iwi.setIssafety( "0" );
            iwi.setQtyLowInv( new BigDecimal( "0" ) );
            iwi.setQtyEconomic( new BigDecimal( "0" ) );
            iwi.setWarehouseid( imTranferDetail.getToWareHouseId() );

            invWarehouseItemDao.insertInvWarehouseItem( iwi );
        }

        /*
         * InvItemVO iiv = new InvItemVO(); InvItem ii = null; iiv.setItemname(
         * imTranferDetail.getItemName() ); iiv.setCateId(
         * imTranferDetail.getToCateTypeId() ); iiv.setCusmodel(
         * imTranferDetail.getCusModel() ); iiv.setSiteId(
         * imTranferDetail.getSiteid() ); List<InvItem> iiList =
         * invItemDao.queryInvItem( iiv ); if ( null == iiList ||
         * iiList.isEmpty() ) { ii = new InvItem(); ii.setCreatedate( new Date()
         * ); ii.setCreateuser( imTranferDetail.getCreateuser() );
         * ii.setCusmodel( imTranferDetail.getCusModel() ); ii.setIshis( 0 );
         * ii.setIssafety( "0" ); ii.setItemname( imTranferDetail.getItemName()
         * ); ii.setQtyUnit1( new BigDecimal( 1 ) ); ii.setUnit1(
         * imTranferDetail.getUnitId() ); ii.setSiteId(
         * imTranferDetail.getSiteid() ); invItemDao.insertInvItemInfo( ii );
         * InvWarehouseItem iwi = new InvWarehouseItem(); iwi.setCreatedate( new
         * Date() ); iwi.setCreateuser( imTranferDetail.getCreateuser() );
         * iwi.setDefBinid( imTranferDetail.getToBinId() ); iwi.setInvcateid(
         * imTranferDetail.getToCateTypeId() ); iwi.setItemid( ii.getItemid() );
         * iwi.setSiteId( imTranferDetail.getSiteid() ); iwi.setWarehouseid(
         * imTranferDetail.getToWareHouseId() );
         * invWarehouseItemDao.insertInvWarehouseItem( iwi ); } else { ii =
         * iiList.get( 0 ); } reMap.put( "itemId", ii.getItemid() ); reMap.put(
         * "itemCode", ii.getItemcode() ); return reMap;
         */
    }
}
