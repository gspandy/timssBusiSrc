package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;



/**
 * @title: {title}
 * @description: {desc} 页面配置bean
 * @company: gdyd
 * @className: FinancePageConfig.java
 * @author: 王中华
 * @createDate: 2015-8-24
 * @updateUser: 王中华
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class FinancePageConfig extends ItcMvcBean{
	
    String siteid; //站点
	String flow; //流程类型
	String flowKey; //流程Key
	String reimburseType; //报销类型
	String formConf; // 表单配置（新建页面）
	String formInfoConf;//表单配置（详情页面）
	String datagridConf; //表格配置
	String dgDetailConf; // 表格弹出框的详情配置
	int dilogHeight; //报销明细弹出窗的高度
	
    public String getFormInfoConf() {
        return formInfoConf;
    }
    public void setFormInfoConf(String formInfoConf) {
        this.formInfoConf = formInfoConf;
    }
    public int getDilogHeight() {
        return dilogHeight;
    }
    public void setDilogHeight(int dilogHeight) {
        this.dilogHeight = dilogHeight;
    }
    public String getSiteid() {
        return siteid;
    }
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    public String getFlow() {
        return flow;
    }
    public void setFlow(String flow) {
        this.flow = flow;
    }
    public String getFlowKey() {
        return flowKey;
    }
    public void setFlowKey(String flowKey) {
        this.flowKey = flowKey;
    }
    public String getReimburseType() {
        return reimburseType;
    }
    public void setReimburseType(String reimburseType) {
        this.reimburseType = reimburseType;
    }
    public String getFormConf() {
        return formConf;
    }
    public void setFormConf(String formConf) {
        this.formConf = formConf;
    }
    public String getDatagridConf() {
        return datagridConf;
    }
    public void setDatagridConf(String datagridConf) {
        this.datagridConf = datagridConf;
    }
    public String getDgDetailConf() {
        return dgDetailConf;
    }
    public void setDgDetailConf(String dgDetailConf) {
        this.dgDetailConf = dgDetailConf;
    }
    @Override
    public String toString() {
        return "FinancePageConfig [siteid=" + siteid + ", flow=" + flow + ", flowKey=" + flowKey + ", reimburseType="
                + reimburseType + ", formConf=" + formConf + ", datagridConf=" + datagridConf + ", dgDetailConf="
                + dgDetailConf + "]";
    }
	
	
	
}
