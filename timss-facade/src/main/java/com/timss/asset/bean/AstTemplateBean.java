package com.timss.asset.bean;

import java.util.Date;

public class AstTemplateBean {
	private int asset_template_id;
	private String asset_type;
	private String field_name;
	private String create_user;
	private Date create_date;
	private String modify_user;
	private Date modify_date;
	private String site_id;
	public int getAsset_template_id() {
		return asset_template_id;
	}
	public void setAsset_template_id(int asset_template_id) {
		this.asset_template_id = asset_template_id;
	}
	public String getAsset_type() {
		return asset_type;
	}
	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	
}
