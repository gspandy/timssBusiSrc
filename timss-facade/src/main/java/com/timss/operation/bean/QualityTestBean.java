package com.timss.operation.bean;

import java.util.Date;

import com.yudean.itc.annotation.EntityID;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 质检日志
 * @description: {desc}
 * @company: gdyd
 * @className: QualityTestBean.java
 * @author: yyn
 * @createDate: 2016年8月10日
 * @updateUser: yyn
 * @version: 1.0
 */
public class QualityTestBean extends ItcMvcBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6334517987196047584L;
	/**
     * 使用组件的uuid
     */
    @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
    @EntityID
    private String qtId;
    /**
     * 质检日期
     */
    private Date qtDate;
    /**
     * 质检情况
     */
    private String content;
    /**
     * 质检记录人部门
     */
    private String deptName;
    /**
     * 质检记录人姓名
     */
    private String userName;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getQtId() {
		return qtId;
	}
	public void setQtId(String qtId) {
		this.qtId = qtId;
	}
	public Date getQtDate() {
		return qtDate;
	}
	public void setQtDate(Date qtDate) {
		this.qtDate = qtDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
