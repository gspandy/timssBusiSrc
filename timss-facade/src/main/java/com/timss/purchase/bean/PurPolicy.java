package com.timss.purchase.bean;

import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurPolicy
 * @description: 合同条款bean
 * @company: gdyd
 * @className: PurPolicy.java
 * @author: 890162
 * @createDate: 2015-9-24
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPolicy extends ItcMvcBean {
    
    private static final long serialVersionUID = 7450816434431728873L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String sheetId; // 合同ID
    private String policyContent; // 条款内容
    private Integer sort; // 排序

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public String getPolicyContent() {
        return policyContent;
    }

    public void setPolicyContent(String policyContent) {
        this.policyContent = policyContent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
