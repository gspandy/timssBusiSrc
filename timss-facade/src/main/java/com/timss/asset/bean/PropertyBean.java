package com.timss.asset.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 物业管理的房产bean
 * @author 890147
 *
 */
public class PropertyBean extends ItcMvcBean{
	private static final long serialVersionUID = 925184245076102353L;
	@AutoGen("AST_HOUSEID_SEQ")
	@EntityID
	private String houseId; 
	private String houseType;//房产类型,非房间/房间(house/room)
	private String houseName;
	private String parentId;
	private String isRoot;//取值Y/N
	private String ownerName;//业主姓名
	private String unit;//单位,关联inv_unit的unitid
	private Double area;//面积
	private String rightOwner;//权属
	private Double managementCost;//管理费(元)
	private String decoration;//室内装修
	private String officeSupplies;//办公用品
	private String electricalAppliances;//电器
	private String airConditioner;//空调
	private String fireControl;//消防
	
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIsRoot() {
		return isRoot;
	}
	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public String getRightOwner() {
		return rightOwner;
	}
	public void setRightOwner(String rightOwner) {
		this.rightOwner = rightOwner;
	}
	public Double getManagementCost() {
		return managementCost;
	}
	public void setManagementCost(Double managementCost) {
		this.managementCost = managementCost;
	}
	public String getDecoration() {
		return decoration;
	}
	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	public String getOfficeSupplies() {
		return officeSupplies;
	}
	public void setOfficeSupplies(String officeSupplies) {
		this.officeSupplies = officeSupplies;
	}
	public String getElectricalAppliances() {
		return electricalAppliances;
	}
	public void setElectricalAppliances(String electricalAppliances) {
		this.electricalAppliances = electricalAppliances;
	}
	public String getAirConditioner() {
		return airConditioner;
	}
	public void setAirConditioner(String airConditioner) {
		this.airConditioner = airConditioner;
	}
	public String getFireControl() {
		return fireControl;
	}
	public void setFireControl(String fireControl) {
		this.fireControl = fireControl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}