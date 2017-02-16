package com.timss.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ErpIdsControl implements Serializable {
    private BigDecimal controlId;
    private String sourceCode;
    private String sourceCompCode;
    private String targetCode;
    private String targetCompCode;
    private String targetModel;
    private String processStatus;
    private String receiveStatus;
    private BigDecimal recordCount;
    private String errorMessage;
    private Date creationDate;
    private BigDecimal createdBy;
    private Date lastUpdateDate;
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
        this.sourceCode = sourceCode == null ? null : sourceCode.trim();
    }

    public String getSourceCompCode() {
        return sourceCompCode;
    }

    public void setSourceCompCode(String sourceCompCode) {
        this.sourceCompCode = sourceCompCode == null ? null : sourceCompCode.trim();
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode == null ? null : targetCode.trim();
    }

    public String getTargetCompCode() {
        return targetCompCode;
    }

    public void setTargetCompCode(String targetCompCode) {
        this.targetCompCode = targetCompCode == null ? null : targetCompCode.trim();
    }

    public String getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(String targetModel) {
        this.targetModel = targetModel == null ? null : targetModel.trim();
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus == null ? null : processStatus.trim();
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus == null ? null : receiveStatus.trim();
    }

    public BigDecimal getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(BigDecimal recordCount) {
        this.recordCount = recordCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BigDecimal createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
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
        this.attribute1 = attribute1 == null ? null : attribute1.trim();
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2 == null ? null : attribute2.trim();
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3 == null ? null : attribute3.trim();
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4 == null ? null : attribute4.trim();
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5 == null ? null : attribute5.trim();
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6 == null ? null : attribute6.trim();
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7 == null ? null : attribute7.trim();
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8 == null ? null : attribute8.trim();
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9 == null ? null : attribute9.trim();
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10 == null ? null : attribute10.trim();
    }
}