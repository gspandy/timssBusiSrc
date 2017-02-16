package com.timss.workorder.bean;

import java.io.Serializable;

public class CodeGenerateRule implements Serializable {
    
    private static final long serialVersionUID = 4212392426841138178L;
    private String siteId; // 站点
    private String moduleCode; // 模块code
    private String subModule; // 子模块
    private String plantCode; // 电厂CODE
    private String plantName; // 电厂名
    private String dateFormat; // 日期格式
    private int numberBit; // 末尾自增长的流水号位数
    private int startNum; // 末尾自增长的流水号启始数字
    private String preCode; // 前缀
    private String example; // 例子
    
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getModuleCode() {
        return moduleCode;
    }
    public String getSubModule() {
        return subModule;
    }
    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    public String getPlantCode() {
        return plantCode;
    }
    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }
    public String getPlantName() {
        return plantName;
    }
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
    public String getDateFormat() {
        return dateFormat;
    }
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    public int getNumberBit() {
        return numberBit;
    }
    public void setNumberBit(int numberBit) {
        this.numberBit = numberBit;
    }
    public int getStartNum() {
        return startNum;
    }
    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }
    public String getPreCode() {
        return preCode;
    }
    public void setPreCode(String preCode) {
        this.preCode = preCode;
    }
    public String getExample() {
        return example;
    }
    public void setExample(String example) {
        this.example = example;
    }
    @Override
    public String toString() {
        return "CodeGenerateRole [siteId=" + siteId + ", moduleCode=" + moduleCode + ", subModule=" + subModule
                + ", plantCode=" + plantCode + ", plantName=" + plantName + ", dateFormat=" + dateFormat
                + ", numberBit=" + numberBit + ", startNum=" + startNum + ", preCode=" + preCode + ", example="
                + example + "]";
    }
    

}
