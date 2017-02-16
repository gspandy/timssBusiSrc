package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;


/**
 * @title: {title} 隔离点与隔离方法关联
 * @description: {desc} 隔离点的维护（关联隔离点和隔离方法）
 * @company: gdyd
 * @className: PtwIslMethodDefine.java
 * @author: 王中华
 * @createDate: 2014-10-17
 * @updateUser: 王中华
 * @version: 1.0
 */
public class PtwIsolationPoint extends ItcMvcBean{
    
	 
	private static final long serialVersionUID = -3192184472518004102L;
	private int id;
    private String pointNo; //实际上就是assetId
    private int methodId;  //隔离方法Id
    private Integer yxbz;
    

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPointNo() {
		return pointNo;
	}
	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public Integer getYxbz() {
		return yxbz;
	}
	public void setYxbz(Integer yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "PtwIsolationPoint [id=" + id + ", pointNo=" + pointNo
				+ ", methodId=" + methodId + ", yxbz=" + yxbz + "]";
	}
	
    
}
