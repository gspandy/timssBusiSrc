package com.timss.asset.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 物业管理房产收费的bean
 * @author 890147
 *
 */
public class PropertyChargeBean extends ItcMvcBean{
	private static final long serialVersionUID = 3576930238863577407L;
	
	@AutoGen("AST_CHARGEID_SEQ")
	@EntityID
	private String chargeId;
	private String houseId;//所属房产id,关联ast_house_property的house_id
	private Date chargeDate;//收费日期
	private String chargeType;//收费类型,取值electric/water,在b_enum(charge_type)中增加该枚举
	private Double prevCount;//上次读数
	private Double currentCount;//本次读数
	private String unit;//计费用量,关联inv_unit的unitid
	private Double unitPrice;//标准单价
	private Double otherCharge1;//计外价1
	private Double otherCharge2;//计外价2
	private Double currentCharge;//本月费用
	private Double debts;//往月欠费
	private Double lateFee;//滞纳金
	private Double subTotal;//应交小计
	
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public Double getPrevCount() {
		return prevCount;
	}
	public void setPrevCount(Double prevCount) {
		this.prevCount = prevCount;
	}
	public Double getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(Double currentCount) {
		this.currentCount = currentCount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getOtherCharge1() {
		return otherCharge1;
	}
	public void setOtherCharge1(Double otherCharge1) {
		this.otherCharge1 = otherCharge1;
	}
	public Double getOtherCharge2() {
		return otherCharge2;
	}
	public void setOtherCharge2(Double otherCharge2) {
		this.otherCharge2 = otherCharge2;
	}
	public Double getCurrentCharge() {
		return currentCharge;
	}
	public void setCurrentCharge(Double currentCharge) {
		this.currentCharge = currentCharge;
	}
	public Double getDebts() {
		return debts;
	}
	public void setDebts(Double debts) {
		this.debts = debts;
	}
	public Double getLateFee() {
		return lateFee;
	}
	public void setLateFee(Double lateFee) {
		this.lateFee = lateFee;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}