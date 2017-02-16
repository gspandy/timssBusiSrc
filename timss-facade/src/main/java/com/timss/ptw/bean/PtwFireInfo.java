package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 工作票动火信息
 * @description: {desc}
 * @company: gdyd
 * @className: PtwFireInfo.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwFireInfo implements Serializable {

    private static final long serialVersionUID = -5822438040690150139L;
    private int wtId;
    private int attachWtId;
    private String fireWorkPic;
    private String attachWtNo;
    private String fireUnit;
    private String fireWc;
    private String fireWpExec;
    private String fireWpExecNo;
    private String guardXmCo;
    private String guardXmCoNo;
    private String appvCj;
    private String appvCjNo;
    private Date appvCjTime;
    private String appvBm;
    private String appvBmNo;
    private Date appvBmTime;
    private String appvAj;
    private String appvAjNo;
    private Date appvAjTime;
    private String appvXf;
    private String appvXfNo;
    private Date appvXfTime;
    private String appvSecuCo;
    private String appvSecuCoNo;
    private Date appvSecuCoTime;
    private String cfmApprv;
    private String cfmApprvNo;
    private Date cfmApprvTime;
    private String cfmWpic;
    private String cfmWpicNo;
    private Date cfmWpicTime;
    private String cfmGuardXf;
    private String cfmGuardXfNo;
    private Date cfmGuardXfTime;    
    private String licWpExec;
    private String licWpExecNo;
    private Date licWpExecTime;
    private String licSecu;
    private String licSecuNo;
    private Date licSecuTime;
    private String licPicAj;
    private String licPicAjNo;
    private Date licPicAjTime;
    private String licPicCj;
    private String licPicCjNo;
    private Date licPicCjTime;
    private String finExec;
    private String finExecNo;
    private Date finExecTime;
    private String finGuardXf;
    private String finGuardXfNo;
    private Date finGuardXfTime;
    private String finApprv;
    private String finApprvNo;
    private Date finApprvTime;
    private String finWpic;
    private String finWpicNo;
    private Date finWpicTime;
    /**
     * @return 工作票_id
     */
    public int getWtId() {
        return wtId;
    }
    /**
     * @param 工作票_id
     */
    public void setWtId(int wtId) {
        this.wtId = wtId;
    }
    /**
     * @return 对应工作票_id
     */
    public int getAttachWtId() {
        return attachWtId;
    }
    /**
     * @param 对应工作票_id
     */
    public void setAttachWtId(int attachWtId) {
        this.attachWtId = attachWtId;
    }
    /**
     * @return 对应工作票_No
     */
    public String getAttachWtNo() {
        return attachWtNo;
    }
    /**
     * @param 对应工作票_No
     */
    public void setAttachWtNo(String attachWtNo) {
        this.attachWtNo = attachWtNo;
    }
    /**
     * @return 动火单位
     */
    public String getFireUnit() {
        return fireUnit;
    }
    /**
     * @param 动火单位
     */
    public void setFireUnit(String fireUnit) {
        this.fireUnit = fireUnit;
    }
    /**
     * @return 动火部门(公司/部门/机组)
     */
    public String getFireWc() {
        return fireWc;
    }
    /**
     * @param 动火部门(公司/部门/机组)
     */
    public void setFireWc(String fireWc) {
        this.fireWc = fireWc;
    }
    /**
     * @return 动火工作执行人
     */
    public String getFireWpExec() {
        return fireWpExec;
    }
    /**
     * @param 动火工作执行人
     */
    public void setFireWpExec(String fireWpExec) {
        this.fireWpExec = fireWpExec;
    }
    /**
     * @return 公司消防监护人
     */
    public String getGuardXmCo() {
        return guardXmCo;
    }
    /**
     * @param 公司消防监护人
     */
    public void setGuardXmCo(String guardXmCo) {
        this.guardXmCo = guardXmCo;
    }
    /**
     * @return 审批_厂级审批人
     */
    public String getAppvCj() {
        return appvCj;
    }
    /**
     * @param 审批_厂级审批人
     */
    public void setAppvCj(String appvCj) {
        this.appvCj = appvCj;
    }
    /**
     * @return 审批_厂级审批时间
     */
    public Date getAppvCjTime() {
        return appvCjTime;
    }
    /**
     * @param 审批_厂级审批时间
     */
    public void setAppvCjTime(Date appvCjTime) {
        this.appvCjTime = appvCjTime;
    }
    /**
     * @return 审批_部门审批人
     */
    public String getAppvBm() {
        return appvBm;
    }
    /**
     * @param 审批_部门审批人
     */
    public void setAppvBm(String appvBm) {
        this.appvBm = appvBm;
    }
    /**
     * @return 审批_部门审批时间
     */
    public Date getAppvBmTime() {
        return appvBmTime;
    }
    /**
     * @param 审批_部门审批时间
     */
    public void setAppvBmTime(Date appvBmTime) {
        this.appvBmTime = appvBmTime;
    }
    /**
     * @return 审批_安监审批人
     */
    public String getAppvAj() {
        return appvAj;
    }
    /**
     * @param 审批_安监审批人
     */
    public void setAppvAj(String appvAj) {
        this.appvAj = appvAj;
    }
    /**
     * @return 审批_安监审批时间
     */
    public Date getAppvAjTime() {
        return appvAjTime;
    }
    /**
     * @param 审批_安监审批时间
     */
    public void setAppvAjTime(Date appvAjTime) {
        this.appvAjTime = appvAjTime;
    }
    /**
     * @return 审批_消防审批人
     */
    public String getAppvXf() {
        return appvXf;
    }
    /**
     * @param 审批_消防审批人
     */
    public void setAppvXf(String appvXf) {
        this.appvXf = appvXf;
    }
    /**
     * @return 审批_消防审批时间
     */
    public Date getAppvXfTime() {
        return appvXfTime;
    }
    /**
     * @param 审批_消防审批时间
     */
    public void setAppvXfTime(Date appvXfTime) {
        this.appvXfTime = appvXfTime;
    }
    /**
     * @return 审批_公司保卫负责人
     */
    public String getAppvSecuCo() {
        return appvSecuCo;
    }
    /**
     * @param 审批_公司保卫负责人
     */
    public void setAppvSecuCo(String appvSecuCo) {
        this.appvSecuCo = appvSecuCo;
    }
    /**
     * @return 审批_公司保卫审批时间
     */
    public Date getAppvSecuCoTime() {
        return appvSecuCoTime;
    }
    /**
     * @param 审批_公司保卫审批时间
     */
    public void setAppvSecuCoTime(Date appvSecuCoTime) {
        this.appvSecuCoTime = appvSecuCoTime;
    }
    /**
     * @return 确认_工作许可人
     */
    public String getCfmApprv() {
        return cfmApprv;
    }
    /**
     * @param 确认_工作许可人
     */
    public void setCfmApprv(String cfmApprv) {
        this.cfmApprv = cfmApprv;
    }
    /**
     * @return 确认_工作许可确认时间
     */
    public Date getCfmApprvTime() {
        return cfmApprvTime;
    }
    /**
     * @param 确认_工作许可确认时间
     */
    public void setCfmApprvTime(Date cfmApprvTime) {
        this.cfmApprvTime = cfmApprvTime;
    }
    /**
     * @return 确认_工作负责人
     */
    public String getCfmWpic() {
        return cfmWpic;
    }
    /**
     * @param 确认_工作负责人
     */
    public void setCfmWpic(String cfmWpic) {
        this.cfmWpic = cfmWpic;
    }
    /**
     * @return 确认_工作负责确认时间
     */
    public Date getCfmWpicTime() {
        return cfmWpicTime;
    }
    /**
     * @param 确认_工作负责确认时间
     */
    public void setCfmWpicTime(Date cfmWpicTime) {
        this.cfmWpicTime = cfmWpicTime;
    }
    /**
     * @return 确认_消防监护人
     */
    public String getCfmGuardXf() {
        return cfmGuardXf;
    }
    /**
     * @param 确认_消防监护人
     */
    public void setCfmGuardXf(String cfmGuardXf) {
        this.cfmGuardXf = cfmGuardXf;
    }
    /**
     * @return 确认_消防监护确认时间
     */
    public Date getCfmGuardXfTime() {
        return cfmGuardXfTime;
    }
    /**
     * @param 确认_消防监护确认时间
     */
    public void setCfmGuardXfTime(Date cfmGuardXfTime) {
        this.cfmGuardXfTime = cfmGuardXfTime;
    }
    /**
     * @return 许可_动火工作执行人
     */
    public String getLicWpExec() {
        return licWpExec;
    }
    /**
     * @param 许可_动火工作执行人
     */
    public void setLicWpExec(String licWpExec) {
        this.licWpExec = licWpExec;
    }
    /**
     * @return 许可_动火工作执行许可时间
     */
    public Date getLicWpExecTime() {
        return licWpExecTime;
    }
    /**
     * @param 许可_动火工作执行许可时间
     */
    public void setLicWpExecTime(Date licWpExecTime) {
        this.licWpExecTime = licWpExecTime;
    }
    /**
     * @return 许可_公司保卫负责人
     */
    public String getLicSecu() {
        return licSecu;
    }
    /**
     * @param 许可_公司保卫负责人
     */
    public void setLicSecu(String licSecu) {
        this.licSecu = licSecu;
    }
    /**
     * @return 许可_保卫负责人许可时间
     */
    public Date getLicSecuTime() {
        return licSecuTime;
    }
    /**
     * @param 许可_保卫负责人许可时间
     */
    public void setLicSecuTime(Date licSecuTime) {
        this.licSecuTime = licSecuTime;
    }
    /**
     * @return 许可_公司安监负责人
     */
    public String getLicPicAj() {
        return licPicAj;
    }
    /**
     * @param 许可_公司安监负责人
     */
    public void setLicPicAj(String licPicAj) {
        this.licPicAj = licPicAj;
    }
    /**
     * @return 许可_公司安监负责许可时间
     */
    public Date getLicPicAjTime() {
        return licPicAjTime;
    }
    /**
     * @param 许可_公司安监负责许可时间
     */
    public void setLicPicAjTime(Date licPicAjTime) {
        this.licPicAjTime = licPicAjTime;
    }
    /**
     * @return 许可_厂级负责人
     */
    public String getLicPicCj() {
        return licPicCj;
    }
    /**
     * @param 许可_厂级负责人
     */
    public void setLicPicCj(String licPicCj) {
        this.licPicCj = licPicCj;
    }
    /**
     * @return 许可_厂级负责许可时间
     */
    public Date getLicPicCjTime() {
        return licPicCjTime;
    }
    /**
     * @param 许可_厂级负责许可时间
     */
    public void setLicPicCjTime(Date licPicCjTime) {
        this.licPicCjTime = licPicCjTime;
    }
    /**
     * @return 结束_工作执行人
     */
    public String getFinExec() {
        return finExec;
    }
    /**
     * @param 结束_工作执行人
     */
    public void setFinExec(String finExec) {
        this.finExec = finExec;
    }
    /**
     * @return 结束_工作执行确认时间
     */
    public Date getFinExecTime() {
        return finExecTime;
    }
    /**
     * @param 结束_工作执行确认时间
     */
    public void setFinExecTime(Date finExecTime) {
        this.finExecTime = finExecTime;
    }
    /**
     * @return the 结束_消防监护人
     */
    public String getFinGuardXf() {
        return finGuardXf;
    }
    /**
     * @param 结束_消防监护人
     */
    public void setFinGuardXf(String finGuardXf) {
        this.finGuardXf = finGuardXf;
    }
    /**
     * @return 结束_消防监护确认时间
     */
    public Date getFinGuardXfTime() {
        return finGuardXfTime;
    }
    /**
     * @param 结束_消防监护确认时间
     */
    public void setFinGuardXfTime(Date finGuardXfTime) {
        this.finGuardXfTime = finGuardXfTime;
    }
    /**
     * @return 动火工作票示意图
     */
    public String getFireWorkPic() {
        return fireWorkPic;
    }
    /**
     * @param 动火工作票示意图
     */
    public void setFireWorkPic(String fireWorkPic) {
        this.fireWorkPic = fireWorkPic;
    }
    /**
     * @return 结束工作许可人
     */
    public String getFinApprv() {
        return finApprv;
    }
    /**
     * @param 结束工作许可人
     */
    public void setFinApprv(String finApprv) {
        this.finApprv = finApprv;
    }
    public String getFinApprvNo() {
        return finApprvNo;
    }
    public void setFinApprvNo(String finApprvNo) {
        this.finApprvNo = finApprvNo;
    }
    /**
     * @return 结束工作许可人确认时间
     */
    public Date getFinApprvTime() {
        return finApprvTime;
    }
    /**
     * @param 结束工作许可人确认时间
     */
    public void setFinApprvTime(Date finApprvTime) {
        this.finApprvTime = finApprvTime;
    }
    /**
     * @return 结束工作负责人
     */
    public String getFinWpic() {
        return finWpic;
    }
    /**
     * @param 结束工作负责人
     */
    public void setFinWpic(String finWpic) {
        this.finWpic = finWpic;
    }
    public String getFinWpicNo() {
        return finWpicNo;
    }
    public void setFinWpicNo(String finWpicNo) {
        this.finWpicNo = finWpicNo;
    }
    /**
     * @return 结束工作负责人确认时间
     */
    public Date getFinWpicTime() {
        return finWpicTime;
    }
    /**
     * @param 结束工作负责人确认时间
     */
    public void setFinWpicTime(Date finWpicTime) {
        this.finWpicTime = finWpicTime;
    }
    
    public String getFireWpExecNo() {
        return fireWpExecNo;
    }
    public void setFireWpExecNo(String fireWpExecNo) {
        this.fireWpExecNo = fireWpExecNo;
    }
    public String getGuardXmCoNo() {
        return guardXmCoNo;
    }
    public void setGuardXmCoNo(String guardXmCoNo) {
        this.guardXmCoNo = guardXmCoNo;
    }
    public String getAppvCjNo() {
        return appvCjNo;
    }
    public void setAppvCjNo(String appvCjNo) {
        this.appvCjNo = appvCjNo;
    }
    public String getAppvBmNo() {
        return appvBmNo;
    }
    public void setAppvBmNo(String appvBmNo) {
        this.appvBmNo = appvBmNo;
    }
    public String getAppvAjNo() {
        return appvAjNo;
    }
    public void setAppvAjNo(String appvAjNo) {
        this.appvAjNo = appvAjNo;
    }
    public String getAppvXfNo() {
        return appvXfNo;
    }
    public void setAppvXfNo(String appvXfNo) {
        this.appvXfNo = appvXfNo;
    }
    public String getAppvSecuCoNo() {
        return appvSecuCoNo;
    }
    public void setAppvSecuCoNo(String appvSecuCoNo) {
        this.appvSecuCoNo = appvSecuCoNo;
    }
    public String getFinGuardXfNo() {
        return finGuardXfNo;
    }
    public void setFinGuardXfNo(String finGuardXfNo) {
        this.finGuardXfNo = finGuardXfNo;
    }
    
    public String getCfmApprvNo() {
        return cfmApprvNo;
    }
    public void setCfmApprvNo(String cfmApprvNo) {
        this.cfmApprvNo = cfmApprvNo;
    }
    public String getCfmWpicNo() {
        return cfmWpicNo;
    }
    public void setCfmWpicNo(String cfmWpicNo) {
        this.cfmWpicNo = cfmWpicNo;
    }
    public String getCfmGuardXfNo() {
        return cfmGuardXfNo;
    }
    public void setCfmGuardXfNo(String cfmGuardXfNo) {
        this.cfmGuardXfNo = cfmGuardXfNo;
    }
    public String getLicWpExecNo() {
        return licWpExecNo;
    }
    public void setLicWpExecNo(String licWpExecNo) {
        this.licWpExecNo = licWpExecNo;
    }
    public String getLicSecuNo() {
        return licSecuNo;
    }
    public void setLicSecuNo(String licSecuNo) {
        this.licSecuNo = licSecuNo;
    }
    public String getLicPicAjNo() {
        return licPicAjNo;
    }
    public void setLicPicAjNo(String licPicAjNo) {
        this.licPicAjNo = licPicAjNo;
    }
    public String getLicPicCjNo() {
        return licPicCjNo;
    }
    public void setLicPicCjNo(String licPicCjNo) {
        this.licPicCjNo = licPicCjNo;
    }
    public String getFinExecNo() {
        return finExecNo;
    }
    public void setFinExecNo(String finExecNo) {
        this.finExecNo = finExecNo;
    }
    public PtwFireInfo() {
        super();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + wtId;
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
        PtwFireInfo other = (PtwFireInfo) obj;
        if ( wtId != other.wtId )
            return false;
        return true;
    }
	@Override
	public String toString() {
		return "PtwFireInfo [wtId=" + wtId + ", attachWtId=" + attachWtId
				+ ", fireWorkPic=" + fireWorkPic + ", attachWtNo=" + attachWtNo
				+ ", fireUnit=" + fireUnit + ", fireWc=" + fireWc
				+ ", fireWpExec=" + fireWpExec + ", fireWpExecNo="
				+ fireWpExecNo + ", guardXmCo=" + guardXmCo + ", guardXmCoNo="
				+ guardXmCoNo + ", appvCj=" + appvCj + ", appvCjNo=" + appvCjNo
				+ ", appvCjTime=" + appvCjTime + ", appvBm=" + appvBm
				+ ", appvBmNo=" + appvBmNo + ", appvBmTime=" + appvBmTime
				+ ", appvAj=" + appvAj + ", appvAjNo=" + appvAjNo
				+ ", appvAjTime=" + appvAjTime + ", appvXf=" + appvXf
				+ ", appvXfNo=" + appvXfNo + ", appvXfTime=" + appvXfTime
				+ ", appvSecuCo=" + appvSecuCo + ", appvSecuCoNo="
				+ appvSecuCoNo + ", appvSecuCoTime=" + appvSecuCoTime
				+ ", cfmApprv=" + cfmApprv + ", cfmApprvNo=" + cfmApprvNo
				+ ", cfmApprvTime=" + cfmApprvTime + ", cfmWpic=" + cfmWpic
				+ ", cfmWpicNo=" + cfmWpicNo + ", cfmWpicTime=" + cfmWpicTime
				+ ", cfmGuardXf=" + cfmGuardXf + ", cfmGuardXfNo="
				+ cfmGuardXfNo + ", cfmGuardXfTime=" + cfmGuardXfTime
				+ ", licWpExec=" + licWpExec + ", licWpExecNo=" + licWpExecNo
				+ ", licWpExecTime=" + licWpExecTime + ", licSecu=" + licSecu
				+ ", licSecuNo=" + licSecuNo + ", licSecuTime=" + licSecuTime
				+ ", licPicAj=" + licPicAj + ", licPicAjNo=" + licPicAjNo
				+ ", licPicAjTime=" + licPicAjTime + ", licPicCj=" + licPicCj
				+ ", licPicCjNo=" + licPicCjNo + ", licPicCjTime="
				+ licPicCjTime + ", finExec=" + finExec + ", finExecNo="
				+ finExecNo + ", finExecTime=" + finExecTime + ", finGuardXf="
				+ finGuardXf + ", finGuardXfNo=" + finGuardXfNo
				+ ", finGuardXfTime=" + finGuardXfTime + ", finApprv="
				+ finApprv + ", finApprvNo=" + finApprvNo + ", finApprvTime="
				+ finApprvTime + ", finWpic=" + finWpic + ", finWpicNo="
				+ finWpicNo + ", finWpicTime=" + finWpicTime + "]";
	}
    
    
    
}
