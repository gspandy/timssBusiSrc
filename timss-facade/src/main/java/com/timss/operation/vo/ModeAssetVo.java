package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
 * @title: 设备
 * @description: {desc}
 * @company: gdyd
 * @className: ModeAssetVo.java
 * @author: fengzt
 * @createDate: 2015年10月30日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ModeAssetVo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2187170313313227297L;
    
    /**
     * 设备树Id
     */
    private String assetId;
    
    /**
     * 名称
     */
    private String assetName;
    
    /**
     * 类型
     */
    private String assetType;
    
    /**
     * 专业
     */
    private String spec;
    
    /**
     * 可选项
     */
    private String modeVal;
    /**
     * 当前运行方式取值
     */
    private String nowModeVal;
    /**
     * 工种ID
     */
    private int jobId;
    
    /**
     * 组
     */
    private String team;
    
    /**
     * 排序
     */
    private int sortNum;
    
    public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getNowModeVal() {
		return nowModeVal;
	}

	public void setNowModeVal(String nowModeVal) {
		this.nowModeVal = nowModeVal;
	}

	public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModeVal() {
        return modeVal;
    }

    public void setModeVal(String modeVal) {
        this.modeVal = modeVal;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    
    
}
