package com.timss.attendance.vo;

import java.io.Serializable;

import com.timss.attendance.bean.CarApplyBean;

/**
 * @description:用车申请VO
 * @author: 
 * @createDate: 
 * @param 
 * @return:
 */
public class CarApplyVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3849752327298318685L;
	
	private CarApplyBean carApplyBean;
	private String taskId;
	private String caId;
	public CarApplyBean getCarApplyBean() {
		return carApplyBean;
	}
	public void setCarApplyBean(CarApplyBean carApplyBean) {
		this.carApplyBean = carApplyBean;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	

}
