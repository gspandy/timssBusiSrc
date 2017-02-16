package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.pms.bean.Checkout;

/**
 * 项目验收详细信息vo类
 * @ClassName:     CheckoutDtlVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-22 上午10:07:23
 */
public class CheckoutDtlVo extends Checkout{
	
	private String xmhzf;
	private String projectLeader;
	private String projectName;
	private String hzffzr;
	private String projectId;
	private List<PayplanVo> payplanVos;
	
	private  ArrayList<HashMap<String,Object>> attachMap;
	public String getXmhzf() {
		return xmhzf;
	}
	public void setXmhzf(String xmhzf) {
		this.xmhzf = xmhzf;
	}
	public String getProjectLeader() {
		return projectLeader;
	}
	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getHzffzr() {
		return hzffzr;
	}
	public void setHzffzr(String hzffzr) {
		this.hzffzr = hzffzr;
	}
	public List<PayplanVo> getPayplanVos() {
		return payplanVos;
	}
	public void setPayplanVos(List<PayplanVo> payplanVos) {
		this.payplanVos = payplanVos;
	}
	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}
	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}
        public String getProjectId() {
            return projectId;
        }
        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
}
