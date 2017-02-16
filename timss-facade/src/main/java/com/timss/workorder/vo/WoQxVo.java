package com.timss.workorder.vo;

import java.io.Serializable;
import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;


public class WoQxVo extends ItcMvcBean implements Serializable{

        private static final long serialVersionUID = -4376648599598633381L;
        private String workOrder; //工单编号
	private String equipName;  //设备
	private String woSepcCode;  //分类编号
	private String woSepcCodeName;//分类名称
	private String description;//名称
	private Date beginTime;//开始时间
	private Date endTime;//结束时间
	private String currWindSpeed;//当前风速
	private String usedTime;//使用时间
	private String loseElecticPower;//损失电量
	private String createUser;//创建人员
	private String defectSolveUser;//消缺人员
	private String faultConfirmUser;//确定人员
        public String getWorkOrder() {
            return workOrder;
        }
        public void setWorkOrder(String workOrder) {
            this.workOrder = workOrder;
        }
        public String getEquipName() {
            return equipName;
        }
        public void setEquipName(String equipName) {
            this.equipName = equipName;
        }
        public String getWoSepcCode() {
            return woSepcCode;
        }
        public void setWoSepcCode(String woSepcCode) {
            this.woSepcCode = woSepcCode;
        }
        public String getWoSepcCodeName() {
            return woSepcCodeName;
        }
        public void setWoSepcCodeName(String woSepcCodeName) {
            this.woSepcCodeName = woSepcCodeName;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
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
        public String getCurrWindSpeed() {
            return currWindSpeed;
        }
        public void setCurrWindSpeed(String currWindSpeed) {
            this.currWindSpeed = currWindSpeed;
        }
        public String getUsedTime() {
            return usedTime;
        }
        public void setUsedTime(String usedTime) {
            this.usedTime = usedTime;
        }
        public String getLoseElecticPower() {
            return loseElecticPower;
        }
        public void setLoseElecticPower(String loseElecticPower) {
            this.loseElecticPower = loseElecticPower;
        }
        public String getCreateUser() {
            return createUser;
        }
        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }
        public String getDefectSolveUser() {
            return defectSolveUser;
        }
        public void setDefectSolveUser(String defectSolveUser) {
            this.defectSolveUser = defectSolveUser;
        }
        public String getFaultConfirmUser() {
            return faultConfirmUser;
        }
        public void setFaultConfirmUser(String faultConfirmUser) {
            this.faultConfirmUser = faultConfirmUser;
        }
	  	  
}
