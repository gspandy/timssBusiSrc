package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;


public class FinInsertParams extends ItcMvcBean{
	
    private static final long serialVersionUID = -60088489703120440L;
        String formData;  // 报销表单数据
	String detail;  // 报销明细数据
	String submitType;  // 提交方式,"暂存/提交"
	String finNameEn; // 报销流程名称,如"travelcost"
	String finTypeEn; // 报销名称,如"only"
	String uploadIds; // 附件编号
	String beneficiaryid; // 报销人信息,目前只有"他人报销"有值
	
    public String getFormData() {
        return formData;
    }
    public void setFormData(String formData) {
        this.formData = formData;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getSubmitType() {
        return submitType;
    }
    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }
    public String getFinNameEn() {
        return finNameEn;
    }
    public void setFinNameEn(String finNameEn) {
        this.finNameEn = finNameEn;
    }
    public String getFinTypeEn() {
        return finTypeEn;
    }
    public void setFinTypeEn(String finTypeEn) {
        this.finTypeEn = finTypeEn;
    }
    public String getUploadIds() {
        return uploadIds;
    }
    public void setUploadIds(String uploadIds) {
        this.uploadIds = uploadIds;
    }
    public String getBeneficiaryid() {
        return beneficiaryid;
    }
    public void setBeneficiaryid(String beneficiaryid) {
        this.beneficiaryid = beneficiaryid;
    }
    @Override
    public String toString() {
        return "FinInsertParams [formData=" + formData + ", detail=" + detail + ", submitType=" + submitType
                + ", finNameEn=" + finNameEn + ", finTypeEn=" + finTypeEn + ", uploadIds=" + uploadIds
                + ", beneficiaryid=" + beneficiaryid + "]";
    }
	

	
	
}
