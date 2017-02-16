package com.timss.ptw.vo;

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
public class PtwIsolationPointVo extends ItcMvcBean{
    
	 
	
	private static final long serialVersionUID = 8384629378336859106L;
	private int id;
    private String pointNo; //实际上就是assetId
    private String pointName;  //隔离点名称
    private int methodId;  //隔离方法Id
    private String no; //隔离方法编号
    private String method;  //隔离方法描述
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
	
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
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
	@Override
	public String toString() {
		return "PtwIsolationPointVo [id=" + id + ", pointNo=" + pointNo
				+ ", pointName=" + pointName + ", methodId=" + methodId
				+ ", no=" + no + ", method=" + method + "]";
	}
	
	
    

}
