package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资移库明细表
 * @description: 物资移库明细表
 * @company: gdyd
 * @className: InvMatTransferDetail.java
 * @author: 890151
 * @createDate: 2016-1-7
 * @updateUser: 890151
 * @version: 1.0
 */
public class InvMatTransferDetail extends ItcMvcBean {

    private static final long serialVersionUID = 8529154253687082914L;

    @AutoGen(value = "INV_MATTRANSFERD_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imtdId; // 移库明细ID
    private String imtId; // 移库申请ID
    
    private String itemId; // 物资ID
    private String itemCode; // 物资编码
    private String itemName; // 物资名称
    private BigDecimal price; // 单价（元）
    private String cusModel; // 型号规格
    private BigDecimal nowqty; // 库存可用数量
    private BigDecimal stockQty; // 库存实际数量
    private BigDecimal transferQty; // 申请移库数量
    private String unitId; // 单位id
    private String unitName; // 单位名称    
    
    private String wareHouseId;//所属仓库ID
    private String wareHouseName;//所属仓库名称
    private String binId;//货柜ID
    private String binName;//货柜名称
    private String cateTypeId;//物资类型ID
    private String cateTypeName;//物资类型名称
    
    private String toWareHouseId;//移入仓库ID
    private String toWareHouseName;//移入仓库名称
    private String toBinId;//移入仓库货柜ID
    private String toBinName;//移入仓库货柜名称
    private String toCateTypeId;//移入仓库物资类型ID
    private String toCateTypeName;//移入仓库物资类型名称
    
    private String deleted;//删除标志位 0/1
   
    private String createuser; // 创建人
    private Date createdate; // 创建时间
    private String modifyuser; // 修改人
    private Date modifydate; // 修改时间
    private String siteid; // 站点ID
    private String deptid; // 申请部门id
    
    
	public String getImtdId() {
		return imtdId;
	}
	public void setImtdId(String imtdId) {
		this.imtdId = imtdId;
	}
	public String getImtId() {
		return imtId;
	}
	public void setImtId(String imtId) {
		this.imtId = imtId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getNowqty() {
		return nowqty;
	}
	public void setNowqty(BigDecimal nowqty) {
		this.nowqty = nowqty;
	}
	public BigDecimal getStockQty() {
		return stockQty;
	}
	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
	}

	public BigDecimal getTransferQty() {
		return transferQty;
	}
	public void setTransferQty(BigDecimal transferQty) {
		this.transferQty = transferQty;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}
	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	public String getBinId() {
		return binId;
	}
	public void setBinId(String binId) {
		this.binId = binId;
	}
	public String getBinName() {
		return binName;
	}
	public void setBinName(String binName) {
		this.binName = binName;
	}
	public String getCateTypeId() {
		return cateTypeId;
	}
	public void setCateTypeId(String cateTypeId) {
		this.cateTypeId = cateTypeId;
	}
	public String getCateTypeName() {
		return cateTypeName;
	}
	public void setCateTypeName(String cateTypeName) {
		this.cateTypeName = cateTypeName;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getCusModel() {
		return cusModel;
	}
	public void setCusModel(String cusModel) {
		this.cusModel = cusModel;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getToWareHouseId() {
		return toWareHouseId;
	}
	public void setToWareHouseId(String toWareHouseId) {
		this.toWareHouseId = toWareHouseId;
	}
	public String getToWareHouseName() {
		return toWareHouseName;
	}
	public void setToWareHouseName(String toWareHouseName) {
		this.toWareHouseName = toWareHouseName;
	}
	public String getToBinId() {
		return toBinId;
	}
	public void setToBinId(String toBinId) {
		this.toBinId = toBinId;
	}
	public String getToBinName() {
		return toBinName;
	}
	public void setToBinName(String toBinName) {
		this.toBinName = toBinName;
	}
	public String getToCateTypeId() {
		return toCateTypeId;
	}
	public void setToCateTypeId(String toCateTypeId) {
		this.toCateTypeId = toCateTypeId;
	}
	public String getToCateTypeName() {
		return toCateTypeName;
	}
	public void setToCateTypeName(String toCateTypeName) {
		this.toCateTypeName = toCateTypeName;
	}

 

}
