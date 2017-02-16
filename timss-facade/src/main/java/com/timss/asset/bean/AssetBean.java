package com.timss.asset.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AssetBean.java
 * @author: 890147
 * @createDate: 2014-7-10
 * @updateUser: 890147
 * @version: 1.0
 */
public class AssetBean extends ItcMvcBean{
	private static final long serialVersionUID = -6130142864056477300L;
	@AutoGen("ASSET_ID")
	@EntityID
	private String assetId; 
	private String assetName; 
	private String description; 
	private String assetType; 
	private String status; 
	private String site; 
	private String parentId; 
	private String spec; 
	private String assetCode; 
	private String modelDesc; 
	private String companyNo; 
	private String manufacturer; 
	private String poexcuteId; 
	private Double purchasePrice; 
	private Integer lifeTime; 
	private Date produceDate; 
	private Date purchaseDate; 
	private Double originalValue; 
	private Double ytdCost; 
	private String budgets; 
	private Date installDate; 
	private String serialNum;
	private String cuModel;
	
	private String locationName;
	private String isRoot;//是否为根的标识
	private String isOnlyPoint;//是否仅为隔离点，不在资产台账树中显示，存入Y/N
	private String position;//位置
	
	private String attachments;//绑定的文件信息
	
	private String poId;//关联采购单id
	private String imadId;//领料单详情ID
	private String allowBorrow;//是否允许领用
	private String forbidDelete;//是否禁止删除
	private String forbidMove;//是否禁止移动
	private String forbidUpdate;//是否禁止修改内容
	private String imtdId;
	private String itemId;
	private String astApplyId;
	private String itemCode;
	private String itemName;

	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAstApplyId() {
		return astApplyId;
	}
	public void setAstApplyId(String astApplyId) {
		this.astApplyId = astApplyId;
	}
	public String getImtdId() {
		return imtdId;
	}
	public void setImtdId(String imtdId) {
		this.imtdId = imtdId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getForbidDelete() {
		return forbidDelete;
	}
	public void setForbidDelete(String forbidDelete) {
		this.forbidDelete = forbidDelete;
	}
	public String getForbidMove() {
		return forbidMove;
	}
	public void setForbidMove(String forbidMove) {
		this.forbidMove = forbidMove;
	}
	public String getForbidUpdate() {
		return forbidUpdate;
	}
	public void setForbidUpdate(String forbidUpdate) {
		this.forbidUpdate = forbidUpdate;
	}
	public String getAllowBorrow() {
		return allowBorrow;
	}
	public void setAllowBorrow(String allowBorrow) {
		this.allowBorrow = allowBorrow;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public String getImadId() {
		return imadId;
	}
	public void setImadId(String imadId) {
		this.imadId = imadId;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	public String getIsOnlyPoint() {
		return isOnlyPoint;
	}
	public void setIsOnlyPoint(String isOnlyPoint) {
		this.isOnlyPoint = isOnlyPoint;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIsRoot() {
		return isRoot;
	}
	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}
	public String getCuModel() {
		return cuModel;
	}
	public void setCuModel(String cuModel) {
		this.cuModel = cuModel;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public String getModelDesc() {
		return modelDesc;
	}
	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getPoexcuteId() {
		return poexcuteId;
	}
	public void setPoexcuteId(String poexcuteId) {
		this.poexcuteId = poexcuteId;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Integer getLifeTime() {
		return lifeTime;
	}
	public void setLifeTime(Integer lifeTime) {
		this.lifeTime = lifeTime;
	}
	public Date getProduceDate() {
		return produceDate;
	}
	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Double getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(Double originalValue) {
		this.originalValue = originalValue;
	}
	public Double getYtdCost() {
		return ytdCost;
	}
	public void setYtdCost(Double ytdCost) {
		this.ytdCost = ytdCost;
	}
	public String getBudgets() {
		return budgets;
	}
	public void setBudgets(String budgets) {
		this.budgets = budgets;
	}
	public Date getInstallDate() {
		return installDate;
	}
	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
}