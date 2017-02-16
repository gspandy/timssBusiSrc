package com.timss.asset.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 资产台账设备配置项
 * 固定资产管理使用
 * @author 890147
 */
public class AssetLayoutBean extends ItcMvcBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4848933128658424785L;
	/** 
	 * 设备配置项id
	 */
	@AutoGen("SEQ_AST_CONFIG")
	@EntityID
	private Integer layoutId;
	/**
	 * 所属资产台账
	 */
	private String assetId;
	/**
	 * 设备配置的类别
	 */
	private String type;
	/**
	 * 设备配置的值
	 */
	private String value;
	
	public Integer getLayoutId() {
		return layoutId;
	}
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}