package com.timss.pms.bean;

import java.math.BigDecimal;

/**
 * BPrifixSequence entity. @author MyEclipse Persistence Tools
 */

public class PrifixSequence implements java.io.Serializable {

	// Fields

	private String type;
	private String prifix;
	private BigDecimal nextVal;
	private BigDecimal step;
	private String siteid;
	private String pattern;
	private String attribute1;

	// Constructors

	/** default constructor */
	public PrifixSequence() {
	}

	/** minimal constructor */
	public PrifixSequence(String SId) {
		this.type = SId;
	}

	/** full constructor */
	public PrifixSequence(String SId, String prifix, BigDecimal nextVal,
			BigDecimal step, String siteid, String pattern, String attribute1) {
		this.type = SId;
		this.prifix = prifix;
		this.nextVal = nextVal;
		this.step = step;
		this.siteid = siteid;
		this.pattern = pattern;
		this.attribute1 = attribute1;
	}

	public String getPrifix() {
		return this.prifix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPrifix(String prifix) {
		this.prifix = prifix;
	}

	public BigDecimal getNextVal() {
		return this.nextVal;
	}

	public void setNextVal(BigDecimal nextVal) {
		this.nextVal = nextVal;
	}

	public BigDecimal getStep() {
		return this.step;
	}

	public void setStep(BigDecimal step) {
		this.step = step;
	}

	public String getSiteid() {
		return this.siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getAttribute1() {
		return this.attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

}