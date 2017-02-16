package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;



/**
 * @title: {title} 标准隔离证
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationArea.java
 * @author: 王中华
 * @createDate: 2014-10-16
 * @updateUser: 王中华
 * @version: 1.0
 */
public class PtwIsolationArea extends ItcMvcBean{
    
	private static final long serialVersionUID = -1928400898814291666L;
	private int id;
	private int keyBoxId;
	private String keyBoxNo;
    private String no;
    private String name;
    private Integer yxbz;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getKeyBoxId() {
		return keyBoxId;
	}
	public void setKeyBoxId(int keyBoxId) {
		this.keyBoxId = keyBoxId;
	}
	public String getKeyBoxNo() {
		return keyBoxNo;
	}
	public void setKeyBoxNo(String keyBoxNo) {
		this.keyBoxNo = keyBoxNo;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getYxbz() {
		return yxbz;
	}
	public void setYxbz(Integer yxbz) {
		this.yxbz = yxbz;
	}
	@Override
	public String toString() {
		return "PtwIsolationArea [id=" + id + ", keyBoxId=" + keyBoxId
				+ ", keyBoxNo=" + keyBoxNo + ", no=" + no + ", name=" + name
				+ ", yxbz=" + yxbz + "]";
	}
	
	
    
    
}
