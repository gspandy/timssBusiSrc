package com.timss.itsm.vo;

import com.yudean.mvc.bean.ItcMvcBean;


public class ItsmWoStatisticVO extends ItcMvcBean{
	
	  private static final long serialVersionUID = -4543451885820576605L;
	  
	  private int id;
	  private String statisticObj; //统计对象
	  private String statisticType; //统计类型（事件、请求、个人、团队……）
	  private String serLevel;  //服务级别、工程师、运维团队
	  private String eventType;//事件分类
	  private int  sum;// 事件/请求总数(新建数量)
	  private int solveSum; //成功解决数量
	  private double solveRatio; //成功解决率
	  private int overTimeSolveSum; //超时解决数量
	  private double overTimeSolveRatio;  //超时解决率
	  private int overTimeRespondSum;  //超时响应数量
	  private double overTimeRespondRatio;  //超时响应比率
	  private int csOnTimeSolveSum;  //服务台及时解决数量
	  private double csOnTimeSolveRatio;  //服务台及时解决率
	  private int teamOnTimeSolveSum;  //二线团队及时解决数量
	  private double teamOnTimeSolveRatio;  //二线团队及时解决率
	  private double solveTimeSum; //解决时间总和
	  private double avgSolveTime; //平均解决时间
	  
	  private int statisticNum; //统计次数（用于获取某次统计的所有记录）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatisticObj() {
		return statisticObj;
	}

	public void setStatisticObj(String statisticObj) {
		this.statisticObj = statisticObj;
	}

	public String getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}

	public String getSerLevel() {
		return serLevel;
	}

	public void setSerLevel(String serLevel) {
		this.serLevel = serLevel;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public int getSolveSum() {
		return solveSum;
	}

	public void setSolveSum(int solveSum) {
		this.solveSum = solveSum;
	}

	public double getSolveRatio() {
		return solveRatio;
	}

	public void setSolveRatio(double solveRatio) {
		this.solveRatio = solveRatio;
	}

	public int getOverTimeSolveSum() {
		return overTimeSolveSum;
	}

	public void setOverTimeSolveSum(int overTimeSolveSum) {
		this.overTimeSolveSum = overTimeSolveSum;
	}

	public double getOverTimeSolveRatio() {
		return overTimeSolveRatio;
	}

	public void setOverTimeSolveRatio(double overTimeSolveRatio) {
		this.overTimeSolveRatio = overTimeSolveRatio;
	}

	public int getOverTimeRespondSum() {
		return overTimeRespondSum;
	}

	public void setOverTimeRespondSum(int overTimeRespondSum) {
		this.overTimeRespondSum = overTimeRespondSum;
	}

	public double getOverTimeRespondRatio() {
		return overTimeRespondRatio;
	}

	public void setOverTimeRespondRatio(double overTimeRespondRatio) {
		this.overTimeRespondRatio = overTimeRespondRatio;
	}

	public int getCsOnTimeSolveSum() {
		return csOnTimeSolveSum;
	}

	public void setCsOnTimeSolveSum(int csOnTimeSolveSum) {
		this.csOnTimeSolveSum = csOnTimeSolveSum;
	}

	public double getCsOnTimeSolveRatio() {
		return csOnTimeSolveRatio;
	}

	public void setCsOnTimeSolveRatio(double csOnTimeSolveRatio) {
		this.csOnTimeSolveRatio = csOnTimeSolveRatio;
	}

	public int getTeamOnTimeSolveSum() {
		return teamOnTimeSolveSum;
	}

	public void setTeamOnTimeSolveSum(int teamOnTimeSolveSum) {
		this.teamOnTimeSolveSum = teamOnTimeSolveSum;
	}

	public double getTeamOnTimeSolveRatio() {
		return teamOnTimeSolveRatio;
	}

	public void setTeamOnTimeSolveRatio(double teamOnTimeSolveRatio) {
		this.teamOnTimeSolveRatio = teamOnTimeSolveRatio;
	}

	public double getSolveTimeSum() {
		return solveTimeSum;
	}

	public void setSolveTimeSum(double solveTimeSum) {
		this.solveTimeSum = solveTimeSum;
	}

	public double getAvgSolveTime() {
		return avgSolveTime;
	}

	public void setAvgSolveTime(double avgSolveTime) {
		this.avgSolveTime = avgSolveTime;
	}

	public int getStatisticNum() {
		return statisticNum;
	}

	public void setStatisticNum(int statisticNum) {
		this.statisticNum = statisticNum;
	}

	@Override
	public String toString() {
		return "ItWoStatisticVO [id=" + id + ", statisticObj=" + statisticObj
				+ ", statisticType=" + statisticType + ", serLevel=" + serLevel
				+ ", eventType=" + eventType + ", sum=" + sum + ", solveSum="
				+ solveSum + ", solveRatio=" + solveRatio
				+ ", overTimeSolveSum=" + overTimeSolveSum
				+ ", overTimeSolveRatio=" + overTimeSolveRatio
				+ ", overTimeRespondSum=" + overTimeRespondSum
				+ ", overTimeRespondRatio=" + overTimeRespondRatio
				+ ", csOnTimeSolveSum=" + csOnTimeSolveSum
				+ ", csOnTimeSolveRatio=" + csOnTimeSolveRatio
				+ ", teamOnTimeSolveSum=" + teamOnTimeSolveSum
				+ ", teamOnTimeSolveRatio=" + teamOnTimeSolveRatio
				+ ", solveTimeSum=" + solveTimeSum + ", avgSolveTime="
				+ avgSolveTime + ", statisticNum=" + statisticNum + "]";
	}

	
	
	  
	  
	  
	  
}
