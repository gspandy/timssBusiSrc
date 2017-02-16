package com.timss.ptw.bean;

import java.io.Serializable;


/**
 * 
 * @title: 工作票类型定义表
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTypeDefine.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwTypeDefine implements Serializable{

    private static final long serialVersionUID = -2533000713853421944L;
    private int id;
    private String typeName;
    private int hasPreTime;
    private int hasWorkCondition;
    private int hasElec;
    private int hasSafeDefault;
    private int hasSafeRepair;
    private int hasSafeOprate;
    private int hasChangeExt;
    private int hasRemarkWork;
    private int isFireWt;
    private int isFireChkBox;
    private int hasFireAppr;
    private String noToPrint;
    private int remark1;
    private int remark2;
    private int remark3;
    private int remark4;
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return 默认类型名称
     */
    public String getTypeName() {
        return typeName;
    }
    /**
     * @param 默认类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * @return 有预计开工时间
     */
    public int getHasPreTime() {
        return hasPreTime;
    }
    /**
     * @param 有预计开工时间
     */
    public void setHasPreTime(int hasPreTime) {
        this.hasPreTime = hasPreTime;
    }
    /**
     * @return 有作业条件
     */
    public int getHasWorkCondition() {
        return hasWorkCondition;
    }
    /**
     * @param 有作业条件
     */
    public void setHasWorkCondition(int hasWorkCondition) {
        this.hasWorkCondition = hasWorkCondition;
    }
    /**
     * @return 有带电部分
     */
    public int getHasElec() {
        return hasElec;
    }
    /**
     * @param 有带电部分
     */
    public void setHasElec(int hasElec) {
        this.hasElec = hasElec;
    }
    /**
     * @return 有默认安全措施
     */
    public int getHasSafeDefault() {
        return hasSafeDefault;
    }
    /**
     * @param 有默认安全措施
     */
    public void setHasSafeDefault(int hasSafeDefault) {
        this.hasSafeDefault = hasSafeDefault;
    }
    /**
     * @return 有检修安全措施
     */
    public int getHasSafeRepair() {
        return hasSafeRepair;
    }
    /**
     * @param 有检修安全措施
     */
    public void setHasSafeRepair(int hasSafeRepair) {
        this.hasSafeRepair = hasSafeRepair;
    }
    /**
     * @return 有运行安全措施
     */
    public int getHasSafeOprate() {
        return hasSafeOprate;
    }
    /**
     * @param 有运行安全措施
     */
    public void setHasSafeOprate(int hasSafeOprate) {
        this.hasSafeOprate = hasSafeOprate;
    }
    /**
     * @return 允许变更转移延期等
     */
    public int getHasChangeExt() {
        return hasChangeExt;
    }
    /**
     * @param 允许变更转移延期等
     */
    public void setHasChangeExt(int hasChangeExt) {
        this.hasChangeExt = hasChangeExt;
    }
    /**
     * @return 备注栏内可以增加新任务
     */
    public int getHasRemarkWork() {
        return hasRemarkWork;
    }
    /**
     * @param 备注栏内可以增加新任务
     */
    public void setHasRemarkWork(int hasRemarkWork) {
        this.hasRemarkWork = hasRemarkWork;
    }
    /**
     * @return 是否为动火票
     */
    public int getIsFireWt() {
        return isFireWt;
    }
    /**
     * @param 是否为动火票
     */
    public void setIsFireWt(int isFireWt) {
        this.isFireWt = isFireWt;
    }
    /**
     * @return 动火票内容是选项框
     */
    public int getIsFireChkBox() {
        return isFireChkBox;
    }
    /**
     * @param 动火票内容是选项框
     */
    public void setIsFireChkBox(int isFireChkBox) {
        this.isFireChkBox = isFireChkBox;
    }
    /**
     * @return 动火票有审批
     */
    public int getHasFireAppr() {
        return hasFireAppr;
    }
    /**
     * @param 动火票有审批
     */
    public void setHasFireAppr(int hasFireAppr) {
        this.hasFireAppr = hasFireAppr;
    }
    /**
     * @return 工作票打印模板名称
     */
    public String getNoToPrint() {
        return noToPrint;
    }
    /**
     * @param 工作票打印模板名称
     */
    public void setNoToPrint(String noToPrint) {
        this.noToPrint = noToPrint;
    }
    
    public int getRemark1() {
        return remark1;
    }
    public void setRemark1(int remark1) {
        this.remark1 = remark1;
    }
    public int getRemark2() {
        return remark2;
    }
    public void setRemark2(int remark2) {
        this.remark2 = remark2;
    }
    public int getRemark3() {
        return remark3;
    }
    public void setRemark3(int remark3) {
        this.remark3 = remark3;
    }
    public int getRemark4() {
        return remark4;
    }
    public void setRemark4(int remark4) {
        this.remark4 = remark4;
    }
    public PtwTypeDefine() {
        super();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        PtwTypeDefine other = (PtwTypeDefine) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    
    
}
