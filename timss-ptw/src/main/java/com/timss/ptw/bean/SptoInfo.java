package com.timss.ptw.bean;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * '
 * 
 * @title: 标准操作票bean
 * @description: {desc}
 * @company: gdyd
 * @className: SptoInfo.java
 * @author: gucw
 * @createDate: 2015年7月2日
 * @updateUser:
 * @version: 1.0
 */
public class SptoInfo extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -4773707906715690646L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id;
    @AutoGen(requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW, value = "PTW_SPTO_ID")
    private String sheetNo;
    private String code;
    private String mission;
    private String equipment;
    private String type;
    private String version;
    private Date beginTime;
    private Date endTime;
    private String procinstId;
    private String status;
    private String delFlag;
    private String isDraft;
    private String operItemRemarks; //操作项备注

    public String getOperItemRemarks() {
        return operItemRemarks;
    }

    public void setOperItemRemarks(String operItemRemarks) {
        this.operItemRemarks = operItemRemarks;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getProcinstId() {
        return procinstId;
    }

    public void setProcinstId(String procinstId) {
        this.procinstId = procinstId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
    }

    @Override
    public String toString() {
        return "SptoInfo [id=" + id + ", code=" + code + ", mission=" + mission + ", equipment=" + equipment
                + ", type=" + type +  ", version=" + version + ", procinstId=" + procinstId
                + ", status=" + status + ", delFlag=" + delFlag + ", isDraft=" + isDraft + "]";
    }
}
