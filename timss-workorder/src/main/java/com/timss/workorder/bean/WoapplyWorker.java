package com.timss.workorder.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class WoapplyWorker extends ItcMvcBean{
    
    private static final long serialVersionUID = -4649115948573168717L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id; // ID
    private String workapplyId; // 开工申请ID
    private String name; // 名字
    private String sex; // 性别
    private String certificate; // 持有特种作业证书
    private String ppe; //PPE情况
    private String score; // 安规考试成绩
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
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }
    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
    /**
     * @return the certificate
     */
    public String getCertificate() {
        return certificate;
    }
    /**
     * @param certificate the certificate to set
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
    /**
     * @return the ppe
     */
    public String getPpe() {
        return ppe;
    }
    /**
     * @param ppe the ppe to set
     */
    public void setPpe(String ppe) {
        this.ppe = ppe;
    }
    /**
     * @return the score
     */
    public String getScore() {
        return score;
    }
    /**
     * @param score the score to set
     */
    public void setScore(String score) {
        this.score = score;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WoapplyWorker [id=" + id + ", workapplyId=" + workapplyId + ", name=" + name + ", sex=" + sex
                + ", certificate=" + certificate + ", ppe=" + ppe + ", score=" + score + "]";
    }
	
	

}
