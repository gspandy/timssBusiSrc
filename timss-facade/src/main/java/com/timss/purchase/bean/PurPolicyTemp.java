package com.timss.purchase.bean;

import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurPolicyTemp
 * @description: 标准条款bean
 * @company: gdyd
 * @className: PurPolicyTemp.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: 890162
 * @version: 1.0
 */
public class PurPolicyTemp extends ItcMvcBean {

    private static final long serialVersionUID = 6289118899588547861L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String policyId; // 条款ID
    private String policyContent; // 条款内容
    private Integer sort; // 排序

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
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
