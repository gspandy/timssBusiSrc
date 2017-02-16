package com.timss.purchase.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ErpIdsControlVo implements Serializable {
	  	private BigDecimal controlId;
	    private String sourceCode;
	    private String sourceCompCode;
	    private String targetCode;
	    private String targetCompCode;
	    private String targetModel;
	    private String processStatus;
	    private String receiveStatus;
	    private int recordCount;
	    private String errorMessage;
	    private String creationDate;
	    private BigDecimal createdBy;
	    private String lastUpdateDate;
	    private BigDecimal lastUpdatedBy;
	    private String attribute1;
	    private String attribute2;
	    private String attribute3;
	    private String attribute4;
	    private String attribute5;
	    private String attribute6;
	    private String attribute7;
	    private String attribute8;
	    private String attribute9;
	    private String attribute10;
		public BigDecimal getControlId() {
			return controlId;
		}
		public void setControlId(BigDecimal controlId) {
			this.controlId = controlId;
		}
		public String getSourceCode() {
			return sourceCode;
		}
		public void setSourceCode(String sourceCode) {
			this.sourceCode = sourceCode;
		}
		public String getSourceCompCode() {
			return sourceCompCode;
		}
		public void setSourceCompCode(String sourceCompCode) {
			this.sourceCompCode = sourceCompCode;
		}
		public String getTargetCode() {
			return targetCode;
		}
		public void setTargetCode(String targetCode) {
			this.targetCode = targetCode;
		}
		public String getTargetCompCode() {
			return targetCompCode;
		}
		public void setTargetCompCode(String targetCompCode) {
			this.targetCompCode = targetCompCode;
		}
		public String getTargetModel() {
			return targetModel;
		}
		public void setTargetModel(String targetModel) {
			this.targetModel = targetModel;
		}
		public String getProcessStatus() {
			return processStatus;
		}
		public void setProcessStatus(String processStatus) {
			this.processStatus = processStatus;
		}
		public String getReceiveStatus() {
			return receiveStatus;
		}
		public void setReceiveStatus(String receiveStatus) {
			this.receiveStatus = receiveStatus;
		}
		public int getRecordCount() {
			return recordCount;
		}
		public void setRecordCount(int recordCount) {
			this.recordCount = recordCount;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public String getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(String creationDate) {
			this.creationDate = creationDate;
		}
		public BigDecimal getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(BigDecimal createdBy) {
			this.createdBy = createdBy;
		}
		public String getLastUpdateDate() {
			return lastUpdateDate;
		}
		public void setLastUpdateDate(String lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
		}
		public BigDecimal getLastUpdatedBy() {
			return lastUpdatedBy;
		}
		public void setLastUpdatedBy(BigDecimal lastUpdatedBy) {
			this.lastUpdatedBy = lastUpdatedBy;
		}
		public String getAttribute1() {
			return attribute1;
		}
		public void setAttribute1(String attribute1) {
			this.attribute1 = attribute1;
		}
		public String getAttribute2() {
			return attribute2;
		}
		public void setAttribute2(String attribute2) {
			this.attribute2 = attribute2;
		}
		public String getAttribute3() {
			return attribute3;
		}
		public void setAttribute3(String attribute3) {
			this.attribute3 = attribute3;
		}
		public String getAttribute4() {
			return attribute4;
		}
		public void setAttribute4(String attribute4) {
			this.attribute4 = attribute4;
		}
		public String getAttribute5() {
			return attribute5;
		}
		public void setAttribute5(String attribute5) {
			this.attribute5 = attribute5;
		}
		public String getAttribute6() {
			return attribute6;
		}
		public void setAttribute6(String attribute6) {
			this.attribute6 = attribute6;
		}
		public String getAttribute7() {
			return attribute7;
		}
		public void setAttribute7(String attribute7) {
			this.attribute7 = attribute7;
		}
		public String getAttribute8() {
			return attribute8;
		}
		public void setAttribute8(String attribute8) {
			this.attribute8 = attribute8;
		}
		public String getAttribute9() {
			return attribute9;
		}
		public void setAttribute9(String attribute9) {
			this.attribute9 = attribute9;
		}
		public String getAttribute10() {
			return attribute10;
		}
		public void setAttribute10(String attribute10) {
			this.attribute10 = attribute10;
		}
		@Override
		public String toString() {
			return "ErpIdsControlVo [controlId=" + controlId + ", sourceCode="
					+ sourceCode + ", sourceCompCode=" + sourceCompCode
					+ ", targetCode=" + targetCode + ", targetCompCode="
					+ targetCompCode + ", targetModel=" + targetModel
					+ ", processStatus=" + processStatus + ", receiveStatus="
					+ receiveStatus + ", recordCount=" + recordCount
					+ ", errorMessage=" + errorMessage + ", creationDate="
					+ creationDate + ", createdBy=" + createdBy
					+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
					+ lastUpdatedBy + ", attribute1=" + attribute1
					+ ", attribute2=" + attribute2 + ", attribute3="
					+ attribute3 + ", attribute4=" + attribute4
					+ ", attribute5=" + attribute5 + ", attribute6="
					+ attribute6 + ", attribute7=" + attribute7
					+ ", attribute8=" + attribute8 + ", attribute9="
					+ attribute9 + ", attribute10=" + attribute10 + "]";
		}
	    
}
