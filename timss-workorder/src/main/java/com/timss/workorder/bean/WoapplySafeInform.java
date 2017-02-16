package com.timss.workorder.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class WoapplySafeInform extends ItcMvcBean{
    
    private static final long serialVersionUID = -5010628356448895344L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id; // ID
    private String workapplyId; // 申请编号
    private String content; // 安全交底内容
    private String showOrder; //  顺序
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
     * @return the workapplyId
     */
    public String getWorkapplyId() {
        return workapplyId;
    }
    /**
     * @param workapplyId the workapplyId to set
     */
    public void setWorkapplyId(String workapplyId) {
        this.workapplyId = workapplyId;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * @return the showOrder
     */
    public String getShowOrder() {
        return showOrder;
    }
    /**
     * @param showOrder the showOrder to set
     */
    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WoapplySafeInform [id=" + id + ", workapplyId=" + workapplyId + ", content=" + content + ", orderNum="
                + showOrder + "]";
    }
	
	

}
