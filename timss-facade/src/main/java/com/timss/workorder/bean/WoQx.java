package com.timss.workorder.bean;

import java.io.Serializable;
import java.util.Date;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;


public class WoQx extends ItcMvcBean implements Serializable{
	  
	  private static final long serialVersionUID = -5749120534735441428L;
	  @UUIDGen(requireType=GenerationType.REQUIRED_NEW)
	  private String id; //ID
	  private String defectCode;  //文件编号
	  private String monthCode;  //月度编号
	  private String equipId;  //设备ID
	  private String equipName;//设备名称
	  private Date defectTime;//缺陷时间
	  private String defectDes;//缺陷情况
	  private String onDutyUserId;//值班人员
	  private String onDutyUserName;
	  
	  private String defectSolveDes;//缺陷处理及消缺情况
	  private Date defectSolveTime;//消缺时间
	  private String defectSolveUserId;//消缺人员
	  private String defectSolveUserName;
	  private String runningUserId;//运行人员
	  private String runningUserName;
	  private String leftProblem;//遗留问题
	  private String leaderInstructions;//领导批示
	  private String instructionsUserId;//批示领导
	  private String instructionsUserName;
	  private int yxbz;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDefectCode() {
		return defectCode;
	}
	public void setDefectCode(String defectCode) {
		this.defectCode = defectCode;
	}
	public String getMonthCode() {
		return monthCode;
	}
	public void setMonthCode(String monthCode) {
		this.monthCode = monthCode;
	}
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public Date getDefectTime() {
		return defectTime;
	}
	public void setDefectTime(Date defectTime) {
		this.defectTime = defectTime;
	}
	public String getDefectDes() {
		return defectDes;
	}
	public void setDefectDes(String defectDes) {
		this.defectDes = defectDes;
	}
	public String getOnDutyUserId() {
		return onDutyUserId;
	}
	public void setOnDutyUserId(String onDutyUserId) {
		this.onDutyUserId = onDutyUserId;
	}
	public String getOnDutyUserName() {
		return onDutyUserName;
	}
	public void setOnDutyUserName(String onDutyUserName) {
		this.onDutyUserName = onDutyUserName;
	}
	public String getDefectSolveDes() {
		return defectSolveDes;
	}
	public void setDefectSolveDes(String defectSolveDes) {
		this.defectSolveDes = defectSolveDes;
	}
	public Date getDefectSolveTime() {
		return defectSolveTime;
	}
	public void setDefectSolveTime(Date defectSolveTime) {
		this.defectSolveTime = defectSolveTime;
	}
	public String getDefectSolveUserId() {
		return defectSolveUserId;
	}
	public void setDefectSolveUserId(String defectSolveUserId) {
		this.defectSolveUserId = defectSolveUserId;
	}
	public String getDefectSolveUserName() {
		return defectSolveUserName;
	}
	public void setDefectSolveUserName(String defectSolveUserName) {
		this.defectSolveUserName = defectSolveUserName;
	}
	public String getRunningUserId() {
		return runningUserId;
	}
	public void setRunningUserId(String runningUserId) {
		this.runningUserId = runningUserId;
	}
	public String getRunningUserName() {
		return runningUserName;
	}
	public void setRunningUserName(String runningUserName) {
		this.runningUserName = runningUserName;
	}
	public String getLeftProblem() {
		return leftProblem;
	}
	public void setLeftProblem(String leftProblem) {
		this.leftProblem = leftProblem;
	}
	public String getLeaderInstructions() {
		return leaderInstructions;
	}
	public void setLeaderInstructions(String leaderInstructions) {
		this.leaderInstructions = leaderInstructions;
	}
	public String getInstructionsUserId() {
		return instructionsUserId;
	}
	public void setInstructionsUserId(String instructionsUserId) {
		this.instructionsUserId = instructionsUserId;
	}
	public String getInstructionsUserName() {
		return instructionsUserName;
	}
	public void setInstructionsUserName(String instructionsUserName) {
		this.instructionsUserName = instructionsUserName;
	}
	public int getYxbz() {
		return yxbz;
	}
	public void setYxbz(int yxbz) {
		this.yxbz = yxbz;
	}

	

	
	
	  
}
