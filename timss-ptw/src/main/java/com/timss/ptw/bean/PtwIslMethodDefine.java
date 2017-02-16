package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;


/**
 * @title: {title} 隔离方法
 * @description: {desc} 隔离方法的维护
 * @company: gdyd
 * @className: PtwIslMethodDefine.java
 * @author: 王中华
 * @createDate: 2014-10-14
 * @updateUser: 王中华
 * @version: 1.0
 */
public class PtwIslMethodDefine extends ItcMvcBean{
    
    
	private static final long serialVersionUID = -3149090114951148035L;
	private int id;
    private String no;
    private String method;
    private Integer yxbz;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	public Integer getYxbz() {
		return yxbz;
	}
	public void setYxbz(Integer yxbz) {
		this.yxbz = yxbz;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "PtwIslMethodDefine [id=" + id + ", no=" + no + ", method="
				+ method + ", yxbz=" + yxbz + ", getSiteid()=" + getSiteid()
				+ ", getDeptid()=" + getDeptid() + "]";
	}
	
	
	
	
    
}
