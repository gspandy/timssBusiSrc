package com.timss.operation.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 运行方式设置（配置基本信息）
 * @description: {desc}
 * @company: gdyd
 * @className: ModeBean.java
 * @author: fengzt
 * @createDate: 2015年10月29日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ModeBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -6790112109525508239L;

    /**
     * 主键
     */
    private String modeId;
    
    /**
     * 工种ID
     */
    private int jobId;
    
    /**
     * 设备树ID
     */
    private String assetId;
    
    /**
     * 设备树名称
     */
    private String assetName;
    
    /**
     * 可选项值
     */
    private String modeVal;
    /**
     * 当前运行方式取值
     */
    private String nowModeVal;
    /**
     * 是否删除
     */
    private String isDelete;
    
    /**
     * 创建人名称
     */
    private String createUserName;
    
    /**
     * 最近更新人名称
     */
    private String modifyUserName;
    
    /**
     * 组
     */
    private String team;
    
    /**
     * 排序
     */
    private Integer sortNum;
    
    public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
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

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

    public String getModeVal() {
        return modeVal;
    }

    public void setModeVal(String modeVal) {
        this.modeVal = modeVal;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }
    
    
    
}
