package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;

/**
 * 招标详细信息
 * @ClassName:     BidDtlVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-4 上午8:34:46
 */
public class BidDtlVo extends BidVo{
	public List<BidMethod> bidMethod;
	
	public List<BidResult> bidResult;
	
	public List<BidConVo> bidConVos;
	
	private String projectName;
	
	private  ArrayList<HashMap<String,Object>> bidAttachMap;
	
	private  ArrayList<HashMap<String,Object>> bidMethodAttachMap;
	
	private  ArrayList<HashMap<String,Object>> bidResultAttachMap;

	/**
	 * 评标信息
	 * @Title: getBidMethod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public List<BidMethod> getBidMethod() {
		return bidMethod;
	}

	public void setBidMethod(List<BidMethod> bidMethod) {
		this.bidMethod = bidMethod;
	}

	/**
	 * 招标结果信息
	 * @Title: getBidResult
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public List<BidResult> getBidResult() {
		return bidResult;
	}

	public void setBidResult(List<BidResult> bidResult) {
		this.bidResult = bidResult;
	}

	public ArrayList<HashMap<String, Object>> getBidAttachMap() {
		return bidAttachMap;
	}

	public void setBidAttachMap(ArrayList<HashMap<String, Object>> bidAttachMap) {
		this.bidAttachMap = bidAttachMap;
	}

	public ArrayList<HashMap<String, Object>> getBidMethodAttachMap() {
		return bidMethodAttachMap;
	}

	public void setBidMethodAttachMap(
			ArrayList<HashMap<String, Object>> bidMethodAttachMap) {
		this.bidMethodAttachMap = bidMethodAttachMap;
	}

	public ArrayList<HashMap<String, Object>> getBidResultAttachMap() {
		return bidResultAttachMap;
	}

	public void setBidResultAttachMap(
			ArrayList<HashMap<String, Object>> bidResultAttachMap) {
		this.bidResultAttachMap = bidResultAttachMap;
	}

	public List<BidConVo> getBidConVos() {
		return bidConVos;
	}

	public void setBidConVos(List<BidConVo> bidConVos) {
		this.bidConVos = bidConVos;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
}
