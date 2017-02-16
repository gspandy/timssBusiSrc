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
public class PtwIsolationAreaMethod extends ItcMvcBean{
   
	private static final long serialVersionUID = -992527300296946792L;
	private int id;
    private int areaId; //隔离证Id
    private int methodId;  //隔离点与隔离方法关联表Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	@Override
	public String toString() {
		return "PtwIsolationAreaMethod [id=" + id + ", areaId=" + areaId
				+ ", methodId=" + methodId + "]";
	}

    
}
