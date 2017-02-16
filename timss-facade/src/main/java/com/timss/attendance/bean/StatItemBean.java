package com.timss.attendance.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 月份休假统计
 */
public class StatItemBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 8040636901824518094L;
    /**
     * 主表数据
     */
    private StatBean stat;
    /**
     * id
     */
    private int id;
    /**
     * 主表id
     */
    private int statId;
    /**
     * year-month_userid
     */
    private String flag;
    /**
     * 统计年份
     */
    private int yearLeave;
    /**
     * 统计月份
     */
    private int monthLeave;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 年假
     */
    private Double annualLevel;
    /**
     * 加班天数合计
     */
    private Double overTime;
    /**
     * 转补休天数
     */
    private Double transferCompensate;
    /**
     * 已补休
     */
    private Double compensateLeave;
    /**
     * 事假
     */
    private Double enventLeave;
    /**
     * 病假
     */
    private Double sickLeave;
    /**
     * 婚假
     */
    private Double marryLeave;
    /**
     * 产假
     */
    private Double birthLeave;
    /**
     * 其他
     */
    private Double otherLeave;
    
    /**
     * 请假天数合计
     */
    private Double countDays;
    
    /**
     * 请假类型8
     */
    private Double category_8;
    
    /**
     * 请假类型9
     */
    private Double category_9;
    
    /**
     * 请假类型10
     */
    private Double category_10;
    
    /**
     * 请假类型11
     */
    private Double category_11;
    
    /**
     * 请假类型12
     */
    private Double category_12;
    
    /**
     * 请假类型13
     */
    private Double category_13;
    
    /**
     * 请假类型14
     */
    private Double category_14;
    
    /**
     * 请假类型15
     */
    private Double category_15;
    
    /**
     * 请假类型16
     */
    private Double category_16;
    
    /**
     * 请假类型17
     */
    private Double category_17;
    
    public Double getTransferCompensate() {
		return transferCompensate;
	}

	public void setTransferCompensate(Double transferCompensate) {
		this.transferCompensate = transferCompensate;
	}

	public StatBean getStat() {
		return stat;
	}

	public void setStat(StatBean stat) {
		this.stat = stat;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public int getMonthLeave() {
		return monthLeave;
	}

	public void setMonthLeave(int monthLeave) {
		this.monthLeave = monthLeave;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Double getCountDays() {
		return countDays;
	}

	public void setCountDays(Double countDays) {
		this.countDays = countDays;
	}

	public Double getCategory_8() {
		return category_8;
	}

	public void setCategory_8(Double category_8) {
		this.category_8 = category_8;
	}

	public Double getCategory_9() {
		return category_9;
	}

	public void setCategory_9(Double category_9) {
		this.category_9 = category_9;
	}

	public Double getCategory_10() {
		return category_10;
	}

	public void setCategory_10(Double category_10) {
		this.category_10 = category_10;
	}

	public Double getCategory_11() {
		return category_11;
	}

	public void setCategory_11(Double category_11) {
		this.category_11 = category_11;
	}

	public Double getCategory_12() {
		return category_12;
	}

	public void setCategory_12(Double category_12) {
		this.category_12 = category_12;
	}

	public Double getCategory_13() {
		return category_13;
	}

	public void setCategory_13(Double category_13) {
		this.category_13 = category_13;
	}

	public Double getCategory_14() {
		return category_14;
	}

	public void setCategory_14(Double category_14) {
		this.category_14 = category_14;
	}

	public Double getCategory_15() {
		return category_15;
	}

	public void setCategory_15(Double category_15) {
		this.category_15 = category_15;
	}

	public Double getCategory_16() {
		return category_16;
	}

	public void setCategory_16(Double category_16) {
		this.category_16 = category_16;
	}

	public Double getCategory_17() {
		return category_17;
	}

	public void setCategory_17(Double category_17) {
		this.category_17 = category_17;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYearLeave() {
        return yearLeave;
    }

    public void setYearLeave(int yearLeave) {
        this.yearLeave = yearLeave;
    }

    public Double getAnnualLevel() {
        return annualLevel;
    }

    public void setAnnualLevel(Double annualLevel) {
        this.annualLevel = annualLevel;
    }

    public Double getEnventLeave() {
        return enventLeave;
    }

    public void setEnventLeave(Double enventLeave) {
        this.enventLeave = enventLeave;
    }

    public Double getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(Double sickLeave) {
        this.sickLeave = sickLeave;
    }

    public Double getMarryLeave() {
        return marryLeave;
    }

    public void setMarryLeave(Double marryLeave) {
        this.marryLeave = marryLeave;
    }

    public Double getBirthLeave() {
        return birthLeave;
    }

    public void setBirthLeave(Double birthLeave) {
        this.birthLeave = birthLeave;
    }

    public Double getOtherLeave() {
        return otherLeave;
    }

    public void setOtherLeave(Double otherLeave) {
        this.otherLeave = otherLeave;
    }

    public Double getOverTime() {
        return overTime;
    }

    public void setOverTime(Double overTime) {
        this.overTime = overTime;
    }

    public Double getCompensateLeave() {
        return compensateLeave;
    }

    public void setCompensateLeave(Double compensateLeave) {
        this.compensateLeave = compensateLeave;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
