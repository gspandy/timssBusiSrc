package com.timss.attendance.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 系统参数
 * @description: {desc}
 * @company: gdyd
 * @className: Definition.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class DefinitionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6586869903332939588L;
    
    /**
     * id
     */
    private int id;
    
    /**
     * 享受年假工龄(年)
     */
    private int serviceYear;
    
    /**
     * 年假结余折算比率
     */
    private double yearRatio;
    
    /**
     * 补休假结余折算比率
     */
    private double compensateRatio;
    
    /**
     * 补假有效期限天数
     */
    private int effectiveDays;
    
    /**
     * 1至9年可享受的年假天数
     */
    private int firstLevelDays;
    
    /**
     * 10至19年可享受的年假天数
     */
    private int secondLevelDays;
    
    /**
     * 20年以上可享受的年假天数
     */
    private int thirdLevelDays;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 启用时间
     */
    private Date startYear;
    
    /**
     * 失效时间
     */
    private Date endYear;
    
    /**
     * 上午上班开始时间
     */
    private String foreStartDate;
    
    /**
     * 上午下班结束时间
     */
    private String foreEndDate;
    
    /**
     * 下午上班开始时间
     */
    private String afterStartDate;
    
    /**
     * 下午下班结束时间
     */
    private String afterEndDate;

    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateDate;
    
    /**
     * 有效打卡时间的允许分钟差
     */
    private Integer validMin;
    /**
     * 正常打卡的宽容分钟差
     */
    private Integer toleranceMin;
    /**
     * 彻底清空重建考勤结果
     */
    private Boolean isRebuild=false;
    
    private String isCheckWorkstatus;//是否让定时任务更新考勤结果
    private Date lastCheck;//下次更新考勤结果的开始日期（上次的结束日期，默认更新昨天和今天）
    
    private double workHours;//行政人员一天的工作时长
    private double oprWorkHours;//运行人员一天的工作时长
    
    public double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(double workHours) {
		this.workHours = workHours;
	}

	public double getOprWorkHours() {
		return oprWorkHours;
	}

	public void setOprWorkHours(double oprWorkHours) {
		this.oprWorkHours = oprWorkHours;
	}

	public Boolean getIsRebuild() {
		return isRebuild;
	}

	public void setIsRebuild(Boolean isRebuild) {
		this.isRebuild = isRebuild;
	}

	public String getIsCheckWorkstatus() {
		return isCheckWorkstatus;
	}

	public void setIsCheckWorkstatus(String isCheckWorkstatus) {
		this.isCheckWorkstatus = isCheckWorkstatus;
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

	public Integer getValidMin() {
		return validMin;
	}

	public void setValidMin(Integer validMin) {
		this.validMin = validMin;
	}

	public Integer getToleranceMin() {
		return toleranceMin;
	}

	public void setToleranceMin(Integer toleranceMin) {
		this.toleranceMin = toleranceMin;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceYear() {
        return serviceYear;
    }

    public void setServiceYear(int serviceYear) {
        this.serviceYear = serviceYear;
    }

    public double getYearRatio() {
        return yearRatio;
    }

    public void setYearRatio(double yearRatio) {
        this.yearRatio = yearRatio;
    }

    public double getCompensateRatio() {
        return compensateRatio;
    }

    public void setCompensateRatio(double compensateRatio) {
        this.compensateRatio = compensateRatio;
    }

    public int getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(int effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public int getFirstLevelDays() {
        return firstLevelDays;
    }

    public void setFirstLevelDays(int firstLevelDays) {
        this.firstLevelDays = firstLevelDays;
    }

    public int getSecondLevelDays() {
        return secondLevelDays;
    }

    public void setSecondLevelDays(int secondLevelDays) {
        this.secondLevelDays = secondLevelDays;
    }

    public int getThirdLevelDays() {
        return thirdLevelDays;
    }

    public void setThirdLevelDays(int thirdLevelDays) {
        this.thirdLevelDays = thirdLevelDays;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Date getStartYear() {
        return startYear;
    }

    public void setStartYear(Date startYear) {
        this.startYear = startYear;
    }

    public Date getEndYear() {
        return endYear;
    }

    public void setEndYear(Date endYear) {
        this.endYear = endYear;
    }

    public String getForeStartDate() {
        return foreStartDate;
    }

    public void setForeStartDate(String foreStartDate) {
        this.foreStartDate = foreStartDate;
    }

    public String getForeEndDate() {
        return foreEndDate;
    }

    public void setForeEndDate(String foreEndDate) {
        this.foreEndDate = foreEndDate;
    }

    public String getAfterStartDate() {
        return afterStartDate;
    }

    public void setAfterStartDate(String afterStartDate) {
        this.afterStartDate = afterStartDate;
    }

    public String getAfterEndDate() {
        return afterEndDate;
    }

    public void setAfterEndDate(String afterEndDate) {
        this.afterEndDate = afterEndDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((afterEndDate == null) ? 0 : afterEndDate.hashCode());
        result = prime * result + ((afterStartDate == null) ? 0 : afterStartDate.hashCode());
        long temp;
        temp = Double.doubleToLongBits( compensateRatio );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + effectiveDays;
        result = prime * result + ((endYear == null) ? 0 : endYear.hashCode());
        result = prime * result + firstLevelDays;
        result = prime * result + ((foreEndDate == null) ? 0 : foreEndDate.hashCode());
        result = prime * result + ((foreStartDate == null) ? 0 : foreStartDate.hashCode());
        result = prime * result + id;
        result = prime * result + secondLevelDays;
        result = prime * result + serviceYear;
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startYear == null) ? 0 : startYear.hashCode());
        result = prime * result + thirdLevelDays;
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        temp = Double.doubleToLongBits( yearRatio );
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        DefinitionBean other = (DefinitionBean) obj;
        if ( afterEndDate == null ) {
            if ( other.afterEndDate != null )
                return false;
        } else if ( !afterEndDate.equals( other.afterEndDate ) )
            return false;
        if ( afterStartDate == null ) {
            if ( other.afterStartDate != null )
                return false;
        } else if ( !afterStartDate.equals( other.afterStartDate ) )
            return false;
        if ( Double.doubleToLongBits( compensateRatio ) != Double.doubleToLongBits( other.compensateRatio ) )
            return false;
        if ( createBy == null ) {
            if ( other.createBy != null )
                return false;
        } else if ( !createBy.equals( other.createBy ) )
            return false;
        if ( createDate == null ) {
            if ( other.createDate != null )
                return false;
        } else if ( !createDate.equals( other.createDate ) )
            return false;
        if ( effectiveDays != other.effectiveDays )
            return false;
        if ( endYear == null ) {
            if ( other.endYear != null )
                return false;
        } else if ( !endYear.equals( other.endYear ) )
            return false;
        if ( firstLevelDays != other.firstLevelDays )
            return false;
        if ( foreEndDate == null ) {
            if ( other.foreEndDate != null )
                return false;
        } else if ( !foreEndDate.equals( other.foreEndDate ) )
            return false;
        if ( foreStartDate == null ) {
            if ( other.foreStartDate != null )
                return false;
        } else if ( !foreStartDate.equals( other.foreStartDate ) )
            return false;
        if ( id != other.id )
            return false;
        if ( secondLevelDays != other.secondLevelDays )
            return false;
        if ( serviceYear != other.serviceYear )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( startYear == null ) {
            if ( other.startYear != null )
                return false;
        } else if ( !startYear.equals( other.startYear ) )
            return false;
        if ( thirdLevelDays != other.thirdLevelDays )
            return false;
        if ( updateBy == null ) {
            if ( other.updateBy != null )
                return false;
        } else if ( !updateBy.equals( other.updateBy ) )
            return false;
        if ( updateDate == null ) {
            if ( other.updateDate != null )
                return false;
        } else if ( !updateDate.equals( other.updateDate ) )
            return false;
        if ( Double.doubleToLongBits( yearRatio ) != Double.doubleToLongBits( other.yearRatio ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DefinitionBean [id=" + id + ", serviceYear=" + serviceYear + ", yearRatio=" + yearRatio
                + ", compensateRatio=" + compensateRatio + ", effectiveDays=" + effectiveDays + ", firstLevelDays="
                + firstLevelDays + ", secondLevelDays=" + secondLevelDays + ", thirdLevelDays=" + thirdLevelDays
                + ", siteId=" + siteId + ", startYear=" + startYear + ", endYear=" + endYear + ", foreStartDate="
                + foreStartDate + ", foreEndDate=" + foreEndDate + ", afterStartDate=" + afterStartDate
                + ", afterEndDate=" + afterEndDate + ", createBy=" + createBy + ", createDate=" + createDate
                + ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
    }
    
    
    

}
