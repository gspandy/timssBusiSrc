package com.timss.inventory.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemVO.java
 * @author: 890166
 * @createDate: 2014-7-11
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvItemVO extends InvItemBaseField {

    private String     listId;
    private String     siteId;	   // 站点ID
    private String     isspare;	  // 是否备件，Y是N否
    private String     sparecode;	// 备件编码
    private String     img;	      // 图片
    private String     unit1;	    // 单位1
    private String     unitname;	 // 单位名称
    private String     unit2;	    // 单位2(经济订购数量)
    private String     bin;	      // 货柜
    private String     binid;	    // 货柜id
    private String     warehouse;	// 仓库
    private String     cateId;	   // 物资类型id
    private String     cateId2;	  // 物资类型id
    private String     cateName;	 // 物资类型名称 cate_name
    private String     cateType;	 // 物资类型
    private String     totalQty;	 // 库存总数据 total_qty
    private String     prepareQty;       // 预领用数量 prepare_qty
    private String     lateInPrice;      // 最近入库价格 late_in_price
    private Date       lateInDate;       // 最近入库时间 late_in_date
    private String     lateInQty;	// 最近入库数量 late_in_qty
    private String     blgEquip;	 // 所属设备 blg_equip
    private String     manufacturer;     // 生产厂商
    private String     secQty;	   // 安全库存量 sec_qty
    private String     purorderno;       // 采购单号
    private String     imrsno;	   // 退货单号、退库单号
    private String     imrsreason;       // 退货、退库原因
    private String     lotno;	    // 批次
    private String     remark;	   // 备注信息
    private String     outterid;	 // 外部关联id
    private String     embbed;
    private String     opentype;
    private String     invmatapplyStatus;
    private BigDecimal qtyUnit1;	 // 单位数量1
    private BigDecimal itemnum;	  // 采购数量
    private BigDecimal stockqty;	 // 已入库数量（实际库存）
    private BigDecimal bestockqty;       // 入库数量
    private BigDecimal returnableqty;    // 可退货数量
    private BigDecimal returnQty;	// 退货数量
    private BigDecimal totalprice;       // 单元成本（元）
    private BigDecimal laststockqty;
    private BigDecimal qtyUnit2;	 // 单位数量2
    private BigDecimal qtyStock;	 // 库存数量
    private BigDecimal price;	    // 平均成本
    private BigDecimal batchstockqty;    // 本次入库数量

    private String     issafety;	 // 是否启用安全库存
    private BigDecimal qtyEconomic;      // 经济订购量
    private BigDecimal qtyLowInv;	// 最低库存量

    private int	ishis;	    // 是否历史库存

    private String     searchType;       // 安全库存查询方式
    private String     differ;	   // 差异数据

    private BigDecimal nowqty;	   // 可用库存

    private String     imtdid;	   // 物资接收表具体字段

    private String     projectAscription; // 项目名称

    private String     attr2;	    // 保留字段1
    private String     puraId;	   // 采购申请ID
    private String     puraNo;     //采购申请单编号
    private BigDecimal tax;	      // 税额
    private BigDecimal noTaxPrice;	      // 不含税单价
    
    private String active;	      // 是否启用
    private BigDecimal invNum;    //只计算了物资接收的入库数量
    
    

	public String getPuraNo() {
        return puraNo;
    }

    public void setPuraNo(String puraNo) {
        this.puraNo = puraNo;
    }

    /**
     * @return the tax
     */
    public BigDecimal getTax () {
	return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax ( BigDecimal tax ) {
	this.tax = tax;
    }

    /**
     * @return the attr2
     */
    public String getAttr2 () {
	return attr2;
    }

    /**
     * @param attr2 the attr2 to set
     */
    public void setAttr2 ( String attr2 ) {
	this.attr2 = attr2;
    }

    /**
     * @return the listId
     */
    public String getListId () {
	return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId ( String listId ) {
	this.listId = listId;
    }

    /**
     * @return the cateId2
     */
    public String getCateId2 () {
	return cateId2;
    }

    /**
     * @param cateId2 the cateId2 to set
     */
    public void setCateId2 ( String cateId2 ) {
	this.cateId2 = cateId2;
    }

    /**
     * @return the projectAscription
     */
    public String getProjectAscription () {
	return projectAscription;
    }

    /**
     * @param projectAscription the projectAscription to set
     */
    public void setProjectAscription ( String projectAscription ) {
	this.projectAscription = projectAscription;
    }

    /**
     * @return the imtdid
     */
    public String getImtdid () {
	return imtdid;
    }

    /**
     * @param imtdid the imtdid to set
     */
    public void setImtdid ( String imtdid ) {
	this.imtdid = imtdid;
    }

    /**
     * @return the nowqty
     */
    public BigDecimal getNowqty () {
	return nowqty;
    }

    /**
     * @param nowqty the nowqty to set
     */
    public void setNowqty ( BigDecimal nowqty ) {
	this.nowqty = nowqty;
    }

    /**
     * @return the differ
     */
    public String getDiffer () {
	return differ;
    }

    /**
     * @param differ the differ to set
     */
    public void setDiffer ( String differ ) {
	this.differ = differ;
    }

    /**
     * @return the searchType
     */
    public String getSearchType () {
	return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType ( String searchType ) {
	this.searchType = searchType;
    }

    /**
     * @return the cateType
     */
    public String getCateType () {
	return cateType;
    }

    /**
     * @param cateType the cateType to set
     */
    public void setCateType ( String cateType ) {
	this.cateType = cateType;
    }

    /**
     * @return the ishis
     */
    public int getIshis () {
	return ishis;
    }

    /**
     * @param ishis the ishis to set
     */
    public void setIshis ( int ishis ) {
	this.ishis = ishis;
    }

    public String getIssafety () {
	return issafety;
    }

    public void setIssafety ( String issafety ) {
	this.issafety = issafety;
    }

    public BigDecimal getQtyEconomic () {
	return qtyEconomic;
    }

    public void setQtyEconomic ( BigDecimal qtyEconomic ) {
	this.qtyEconomic = qtyEconomic;
    }

    public BigDecimal getQtyLowInv () {
	return qtyLowInv;
    }

    public void setQtyLowInv ( BigDecimal qtyLowInv ) {
	this.qtyLowInv = qtyLowInv;
    }

    /**
     * @return the unitname
     */
    public String getUnitname () {
	return unitname;
    }

    /**
     * @param unitname the unitname to set
     */
    public void setUnitname ( String unitname ) {
	this.unitname = unitname;
    }

    /**
     * @return the batchstockqty
     */
    public BigDecimal getBatchstockqty () {
	return batchstockqty;
    }

    /**
     * @param batchstockqty the batchstockqty to set
     */
    public void setBatchstockqty ( BigDecimal batchstockqty ) {
	this.batchstockqty = batchstockqty;
    }

    /**
     * @return the invmatapplyStatus
     */
    public String getInvmatapplyStatus () {
	return invmatapplyStatus;
    }

    /**
     * @param invmatapplyStatus the invmatapplyStatus to set
     */
    public void setInvmatapplyStatus ( String invmatapplyStatus ) {
	this.invmatapplyStatus = invmatapplyStatus;
    }

    /**
     * @return the opentype
     */
    public String getOpentype () {
	return opentype;
    }

    /**
     * @param opentype the opentype to set
     */
    public void setOpentype ( String opentype ) {
	this.opentype = opentype;
    }

    /**
     * @return the lateInDate
     */
    public Date getLateInDate () {
	return lateInDate;
    }

    /**
     * @param lateInDate the lateInDate to set
     */
    public void setLateInDate ( Date lateInDate ) {
	this.lateInDate = lateInDate;
    }

    /**
     * @return the laststockqty
     */
    public BigDecimal getLaststockqty () {
	return laststockqty;
    }

    /**
     * @param laststockqty the laststockqty to set
     */
    public void setLaststockqty ( BigDecimal laststockqty ) {
	this.laststockqty = laststockqty;
    }

    /**
     * @return the embbed
     */
    public String getEmbbed () {
	return embbed;
    }

    /**
     * @param embbed the embbed to set
     */
    public void setEmbbed ( String embbed ) {
	this.embbed = embbed;
    }

    /**
     * @return the outterid
     */
    public String getOutterid () {
	return outterid;
    }

    /**
     * @param outterid the outterid to set
     */
    public void setOutterid ( String outterid ) {
	this.outterid = outterid;
    }

    /**
     * @return the binid
     */
    public String getBinid () {
	return binid;
    }

    /**
     * @param binid the binid to set
     */
    public void setBinid ( String binid ) {
	this.binid = binid;
    }

    /**
     * @return the purorderno
     */
    public String getPurorderno () {
	return purorderno;
    }

    /**
     * @param purorderno the purorderno to set
     */
    public void setPurorderno ( String purorderno ) {
	this.purorderno = purorderno;
    }

    /**
     * @return the itemnum
     */
    public BigDecimal getItemnum () {
	return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum ( BigDecimal itemnum ) {
	this.itemnum = itemnum;
    }

    /**
     * @return the stockqty
     */
    public BigDecimal getStockqty () {
	return stockqty;
    }

    /**
     * @param stockqty the stockqty to set
     */
    public void setStockqty ( BigDecimal stockqty ) {
	this.stockqty = stockqty;
    }

    /**
     * @return the bestockqty
     */
    public BigDecimal getBestockqty () {
	return bestockqty;
    }

    /**
     * @param bestockqty the bestockqty to set
     */
    public void setBestockqty ( BigDecimal bestockqty ) {
	this.bestockqty = bestockqty;
    }

    /**
     * @return the totalprice
     */
    public BigDecimal getTotalprice () {
	return totalprice;
    }

    /**
     * @param totalprice the totalprice to set
     */
    public void setTotalprice ( BigDecimal totalprice ) {
	this.totalprice = totalprice;
    }

    /**
     * @return the lotno
     */
    public String getLotno () {
	return lotno;
    }

    /**
     * @param lotno the lotno to set
     */
    public void setLotno ( String lotno ) {
	this.lotno = lotno;
    }

    /**
     * @return the remark
     */
    public String getRemark () {
	return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark ( String remark ) {
	this.remark = remark;
    }

    /**
     * @return the cateName
     */
    public String getCateName () {
	return cateName;
    }

    /**
     * @param cateName the cateName to set
     */
    public void setCateName ( String cateName ) {
	this.cateName = cateName;
    }

    /**
     * @return the totalQty
     */
    public String getTotalQty () {
	return totalQty;
    }

    /**
     * @param totalQty the totalQty to set
     */
    public void setTotalQty ( String totalQty ) {
	this.totalQty = totalQty;
    }

    /**
     * @return the prepareQty
     */
    public String getPrepareQty () {
	return prepareQty;
    }

    /**
     * @param prepareQty the prepareQty to set
     */
    public void setPrepareQty ( String prepareQty ) {
	this.prepareQty = prepareQty;
    }

    /**
     * @return the lateInPrice
     */
    public String getLateInPrice () {
	return lateInPrice;
    }

    /**
     * @param lateInPrice the lateInPrice to set
     */
    public void setLateInPrice ( String lateInPrice ) {
	this.lateInPrice = lateInPrice;
    }

    /**
     * @return the lateInQty
     */
    public String getLateInQty () {
	return lateInQty;
    }

    /**
     * @param lateInQty the lateInQty to set
     */
    public void setLateInQty ( String lateInQty ) {
	this.lateInQty = lateInQty;
    }

    /**
     * @return the blgEquip
     */
    public String getBlgEquip () {
	return blgEquip;
    }

    /**
     * @param blgEquip the blgEquip to set
     */
    public void setBlgEquip ( String blgEquip ) {
	this.blgEquip = blgEquip;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer () {
	return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer ( String manufacturer ) {
	this.manufacturer = manufacturer;
    }

    /**
     * @return the secQty
     */
    public String getSecQty () {
	return secQty;
    }

    /**
     * @param secQty the secQty to set
     */
    public void setSecQty ( String secQty ) {
	this.secQty = secQty;
    }

    /**
     * @return the unit2
     */
    public String getUnit2 () {
	return unit2;
    }

    /**
     * @param unit2 the unit2 to set
     */
    public void setUnit2 ( String unit2 ) {
	this.unit2 = unit2;
    }

    /**
     * @return the qtyUnit2
     */
    public BigDecimal getQtyUnit2 () {
	return qtyUnit2;
    }

    /**
     * @param qtyUnit2 the qtyUnit2 to set
     */
    public void setQtyUnit2 ( BigDecimal qtyUnit2 ) {
	this.qtyUnit2 = qtyUnit2;
    }

    /**
     * @return the qtyStock
     */
    public BigDecimal getQtyStock () {
	return qtyStock;
    }

    /**
     * @param qtyStock the qtyStock to set
     */
    public void setQtyStock ( BigDecimal qtyStock ) {
	this.qtyStock = qtyStock;
    }

    /**
     * @return the cateId
     */
    public String getCateId () {
	return cateId;
    }

    /**
     * @param cateId the cateId to set
     */
    public void setCateId ( String cateId ) {
	this.cateId = cateId;
    }

    /**
     * @return the siteId
     */
    public String getSiteId () {
	return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId ( String siteId ) {
	this.siteId = siteId;
    }

    /**
     * @return the isspare
     */
    public String getIsspare () {
	return isspare;
    }

    /**
     * @param isspare the isspare to set
     */
    public void setIsspare ( String isspare ) {
	this.isspare = isspare;
    }

    /**
     * @return the sparecode
     */
    public String getSparecode () {
	return sparecode;
    }

    /**
     * @param sparecode the sparecode to set
     */
    public void setSparecode ( String sparecode ) {
	this.sparecode = sparecode;
    }

    /**
     * @return the img
     */
    public String getImg () {
	return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg ( String img ) {
	this.img = img;
    }

    /**
     * @return the unit1
     */
    public String getUnit1 () {
	return unit1;
    }

    /**
     * @param unit1 the unit1 to set
     */
    public void setUnit1 ( String unit1 ) {
	this.unit1 = unit1;
    }

    /**
     * @return the qtyUnit1
     */
    public BigDecimal getQtyUnit1 () {
	return qtyUnit1;
    }

    /**
     * @param qtyUnit1 the qtyUnit1 to set
     */
    public void setQtyUnit1 ( BigDecimal qtyUnit1 ) {
	this.qtyUnit1 = qtyUnit1;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice () {
	return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice ( BigDecimal price ) {
	this.price = price;
    }

    /**
     * @return the bin
     */
    public String getBin () {
	return bin;
    }

    /**
     * @param bin the bin to set
     */
    public void setBin ( String bin ) {
	this.bin = bin;
    }

    /**
     * @return the warehouse
     */
    public String getWarehouse () {
	return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse ( String warehouse ) {
	this.warehouse = warehouse;
    }

    /**
     * @return 退货数量
     */
    public BigDecimal getReturnQty () {
	return returnQty;
    }

    /**
     * @param returnqty 退货数量
     */
    public void setReturnQty ( BigDecimal returnQty ) {
	this.returnQty = returnQty;
    }

    /**
     * @return 退货单号
     */
    public String getImrsno () {
	return imrsno;
    }

    /**
     * @param imrsno 退货单号
     */
    public void setImrsno ( String imrsno ) {
	this.imrsno = imrsno;
    }

    /**
     * @return 可退货数量
     */
    public BigDecimal getReturnableqty () {
	return returnableqty;
    }

    /**
     * @param returnableqty 可退货数量
     */
    public void setReturnableqty ( BigDecimal returnableqty ) {
	this.returnableqty = returnableqty;
    }

    /**
     * @return imrsreason 退库、退货原因
     */
    public String getImrsreason () {
	return imrsreason;
    }

    /**
     * @param imrsreason 退库、退货原因
     */
    public void setImrsreason ( String imrsreason ) {
	this.imrsreason = imrsreason;
    }

    /**
     * @return the puraId
     */
    public String getPuraId () {
	return puraId;
    }

    /**
     * @param puraId the puraId to set
     */
    public void setPuraId ( String puraId ) {
	this.puraId = puraId;
    }

	public BigDecimal getNoTaxPrice() {
		return noTaxPrice;
	}

	public void setNoTaxPrice(BigDecimal noTaxPrice) {
		this.noTaxPrice = noTaxPrice;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
    public BigDecimal getInvNum () {
        return invNum;
    }

    public void setInvNum ( BigDecimal invNum ) {
        this.invNum = invNum;
    }
}
