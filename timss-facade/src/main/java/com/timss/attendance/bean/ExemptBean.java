package com.timss.attendance.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title:免考勤名单表
 * @description: {desc}
 * @company: gdyd
 * @className: ExemptBean.java
 * @author: zhuw
 * @createDate: 2016年1月22日
 * @updateUser: 
 * @version: 1.0
 */
public class ExemptBean extends ItcMvcBean{


    /**
	 * 
	 */
	private static final long serialVersionUID = -2112138692929956873L;

	/**
     * id
     */
    private String id;
    
    /**
     * 关联用户或部门id
     */
    private String relationId;
    
    
    /**
     * 关联类型
     */
    private String type;
    
    /**
     * 站点id
     */
    private String siteId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Override
	public String toString() {
		return "ExemptBean [id=" + id + ", relationId=" + relationId
				+ ", type=" + type + ", siteId=" + siteId + "]";
	}
    
    

    
}
