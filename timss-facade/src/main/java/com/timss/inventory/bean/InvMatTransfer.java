package com.timss.inventory.bean;

import java.util.Date;
import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资移库流程信息主表
 * @description: 物资移库流程信息主表
 * @company: gdyd
 * @className: InvMatTransfer.java
 * @author: 890151
 * @createDate: 2016-1-7
 * @updateUser: 890151
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class InvMatTransfer extends ItcMvcBean {

    @AutoGen(value = "INV_MATTRANSFER_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imtId; // 移库ID
    @AutoGen(value = "INV_MATTRANSFER_CODE", requireType = GenerationType.REQUIRED_NULL)
    private String imtCode; // 移库申请单号
    private String name; // 名称
    private String status; // 状态
    private String instanceId; // 流程实例ID
    private String taskId; // 任务id
    private String wareHouseFromId;// 移出仓库ID
    private String wareHouseFromName;// 移出仓库名称
    private String wareHouseToId;// 移入仓库ID
    private String wareHouseToName;// 移入仓库名称
    private String remark; // 备注
    private String deleted;//删除标志位
    
    private String createuser; // 创建人
    private Date createdate; // 创建时间
    private String modifyuser; // 修改人
    private Date modifydate; // 修改时间
    private String siteid; // 站点ID
    private String deptid; // 申请部门id
	public String getImtId() {
		return imtId;
	}
	public void setImtId(String imtId) {
		this.imtId = imtId;
	}
	public String getImtCode() {
		return imtCode;
	}
	public void setImtCode(String imtCode) {
		this.imtCode = imtCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWareHouseFromId() {
		return wareHouseFromId;
	}
	public void setWareHouseFromId(String wareHouseFromId) {
		this.wareHouseFromId = wareHouseFromId;
	}
	public String getWareHouseFromName() {
		return wareHouseFromName;
	}
	public void setWareHouseFromName(String wareHouseFromName) {
		this.wareHouseFromName = wareHouseFromName;
	}
	public String getWareHouseToId() {
		return wareHouseToId;
	}
	public void setWareHouseToId(String wareHouseToId) {
		this.wareHouseToId = wareHouseToId;
	}
	public String getWareHouseToName() {
		return wareHouseToName;
	}
	public void setWareHouseToName(String wareHouseToName) {
		this.wareHouseToName = wareHouseToName;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}



}
