package com.timss.workorder.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class WoapplyRisk extends ItcMvcBean{
    
    private static final long serialVersionUID = 3798791068039510873L;
        @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
        private String id; // ID
	private String workapplyId; // 开工申请ID
	private String riskPoint; // 风险点
	private String riskSource; // 风险源
	private String safeItem; // 安全措施
	private String bearFlag; //可承受风险
	private String remarks; // 备注
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the workapplyI
     */
    public String getWorkapplyId() {
        return workapplyId;
    }
    /**
     * @param workapplyI the workapplyI to set
     */
    public void setWorkapplyId(String workapplyId) {
        this.workapplyId = workapplyId;
    }
    /**
     * @return the riskPoint
     */
    public String getRiskPoint() {
        return riskPoint;
    }
    /**
     * @param riskPoint the riskPoint to set
     */
    public void setRiskPoint(String riskPoint) {
        this.riskPoint = riskPoint;
    }
    /**
     * @return the riskSource
     */
    public String getRiskSource() {
        return riskSource;
    }
    /**
     * @param riskSource the riskSource to set
     */
    public void setRiskSource(String riskSource) {
        this.riskSource = riskSource;
    }
    /**
     * @return the safeItem
     */
    public String getSafeItem() {
        return safeItem;
    }
    /**
     * @param safeItem the safeItem to set
     */
    public void setSafeItem(String safeItem) {
        this.safeItem = safeItem;
    }
    /**
     * @return the bearFlag
     */
    public String getBearFlag() {
        return bearFlag;
    }
    /**
     * @param bearFlag the bearFlag to set
     */
    public void setBearFlag(String bearFlag) {
        this.bearFlag = bearFlag;
    }
    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }
    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WoapplyRisk [id=" + id + ", workapplyI=" + workapplyId + ", riskPoint=" + riskPoint + ", riskSource="
                + riskSource + ", safeItem=" + safeItem + ", bearFlag=" + bearFlag + ", remarks=" + remarks + "]";
    }
	
	

}
