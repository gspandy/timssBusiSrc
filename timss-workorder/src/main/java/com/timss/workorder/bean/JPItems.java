package com.timss.workorder.bean;

import java.io.Serializable;

import com.yudean.mvc.bean.ItcMvcBean;


public class JPItems extends ItcMvcBean implements Serializable {
	 
	private static final long serialVersionUID = 8868008480516411876L;
	  private int id; //ID
	  private int jobPlanId;  //作业方案ID
	  private String itemsCode;//资产编码
	  private String  itemsId;// 物资ID
	  private String itemsName; //工具名称
	  private String itemsModel;  //工具型号
	  private String unit;  //单位
	  private String bin;  //货柜
	  private String warehouse;  //仓库
	  private double applyCount; //申请数量
	  private double getCount; //领取数量
	  private double usedCount; //使用数量
	  private String cateName; //物资类型 
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJobPlanId() {
		return jobPlanId;
	}
	public void setJobPlanId(int jobPlanID) {
		this.jobPlanId = jobPlanID;
	}
	
	public String getItemsCode() {
		return itemsCode;
	}
	public void setItemsCode(String itemsCode) {
		this.itemsCode = itemsCode;
	}
	
	public String getItemsId() {
		return itemsId;
	}
	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}
	
	public String getItemsName() {
		return itemsName;
	}
	public void setItemsName(String itemsName) {
		this.itemsName = itemsName;
	}
	
	public String getItemsModel() {
		return itemsModel;
	}
	public void setItemsModel(String itemsModel) {
		this.itemsModel = itemsModel;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	
	public double getApplyCount() {
        return applyCount;
    }
    public void setApplyCount(double applyCount) {
        this.applyCount = applyCount;
    }
    public double getGetCount() {
        return getCount;
    }
    public void setGetCount(double getCount) {
        this.getCount = getCount;
    }
    public double getUsedCount() {
        return usedCount;
    }
    public void setUsedCount(double usedCount) {
        this.usedCount = usedCount;
    }
    public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	  
	  
}
