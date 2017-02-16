package com.timss.asset.bean;

import java.io.Serializable;

/**
 * 
 * @title: 导入到资产设备树中间表
 * @description: {desc}
 * @company: gdyd
 * @className: AssetTempBean.java
 * @author: fengzt
 * @createDate: 2015年6月10日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class AssetTempBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 标识
     */
    private String biaoshi;
    
    /**
     * 资产编码1
     */
    private String code1;
    
    /**
     * 资产名称1
     */
    private String name1;
    
    
    /**
     * 资产编码2
     */
    private String code2;
    
    /**
     * 资产名称2
     */
    private String name2;
    
    /**
     * 资产编码3
     */
    private String code3;
    
    /**
     * 资产名称3
     */
    private String name3;
    
    /**
     * 资产编码4
     */
    private String code4;
    
    /**
     * 资产名称4
     */
    private String name4;
    
    /**
     * 资产编码5
     */
    private String code5;
    
    /**
     * 资产名称5
     */
    private String name5;
    
    /**
     * 型号
     */
    private String modelType;
    
    /**
     * 生产日期
     */
    private String proDate;
    
    /**
     * 生产厂家
     */
    private String proName;
    
    /**
     * 供应商
     */
    private String saleInfo;
    
    /**
     * 采购价格
     */
    private String price;
    
    /**
     * 采购日期
     */
    private String buyDate;
    
    /**
     * 使用年限
     */
    private String useYear;
    
    /**
     * 设备参数
     */
    private String device;

    public String getBiaoshi() {
        return biaoshi;
    }

    public void setBiaoshi(String biaoshi) {
        this.biaoshi = biaoshi;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }


    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getCode4() {
        return code4;
    }

    public void setCode4(String code4) {
        this.code4 = code4;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getCode5() {
        return code5;
    }

    public void setCode5(String code5) {
        this.code5 = code5;
    }

    public String getName5() {
        return name5;
    }

    public void setName5(String name5) {
        this.name5 = name5;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getProDate() {
        return proDate;
    }

    public void setProDate(String proDate) {
        this.proDate = proDate;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(String saleInfo) {
        this.saleInfo = saleInfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getUseYear() {
        return useYear;
    }

    public void setUseYear(String useYear) {
        this.useYear = useYear;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((biaoshi == null) ? 0 : biaoshi.hashCode());
        result = prime * result + ((buyDate == null) ? 0 : buyDate.hashCode());
        result = prime * result + ((code1 == null) ? 0 : code1.hashCode());
        result = prime * result + ((code2 == null) ? 0 : code2.hashCode());
        result = prime * result + ((code3 == null) ? 0 : code3.hashCode());
        result = prime * result + ((code4 == null) ? 0 : code4.hashCode());
        result = prime * result + ((code5 == null) ? 0 : code5.hashCode());
        result = prime * result + ((device == null) ? 0 : device.hashCode());
        result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
        result = prime * result + ((name1 == null) ? 0 : name1.hashCode());
        result = prime * result + ((name2 == null) ? 0 : name2.hashCode());
        result = prime * result + ((name3 == null) ? 0 : name3.hashCode());
        result = prime * result + ((name4 == null) ? 0 : name4.hashCode());
        result = prime * result + ((name5 == null) ? 0 : name5.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((proDate == null) ? 0 : proDate.hashCode());
        result = prime * result + ((proName == null) ? 0 : proName.hashCode());
        result = prime * result + ((saleInfo == null) ? 0 : saleInfo.hashCode());
        result = prime * result + ((useYear == null) ? 0 : useYear.hashCode());
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
        AssetTempBean other = (AssetTempBean) obj;
        if ( biaoshi == null ) {
            if ( other.biaoshi != null )
                return false;
        } else if ( !biaoshi.equals( other.biaoshi ) )
            return false;
        if ( buyDate == null ) {
            if ( other.buyDate != null )
                return false;
        } else if ( !buyDate.equals( other.buyDate ) )
            return false;
        if ( code1 == null ) {
            if ( other.code1 != null )
                return false;
        } else if ( !code1.equals( other.code1 ) )
            return false;
        if ( code2 == null ) {
            if ( other.code2 != null )
                return false;
        } else if ( !code2.equals( other.code2 ) )
            return false;
        if ( code3 == null ) {
            if ( other.code3 != null )
                return false;
        } else if ( !code3.equals( other.code3 ) )
            return false;
        if ( code4 == null ) {
            if ( other.code4 != null )
                return false;
        } else if ( !code4.equals( other.code4 ) )
            return false;
        if ( code5 == null ) {
            if ( other.code5 != null )
                return false;
        } else if ( !code5.equals( other.code5 ) )
            return false;
        if ( device == null ) {
            if ( other.device != null )
                return false;
        } else if ( !device.equals( other.device ) )
            return false;
        if ( modelType == null ) {
            if ( other.modelType != null )
                return false;
        } else if ( !modelType.equals( other.modelType ) )
            return false;
        if ( name1 == null ) {
            if ( other.name1 != null )
                return false;
        } else if ( !name1.equals( other.name1 ) )
            return false;
        if ( name2 == null ) {
            if ( other.name2 != null )
                return false;
        } else if ( !name2.equals( other.name2 ) )
            return false;
        if ( name3 == null ) {
            if ( other.name3 != null )
                return false;
        } else if ( !name3.equals( other.name3 ) )
            return false;
        if ( name4 == null ) {
            if ( other.name4 != null )
                return false;
        } else if ( !name4.equals( other.name4 ) )
            return false;
        if ( name5 == null ) {
            if ( other.name5 != null )
                return false;
        } else if ( !name5.equals( other.name5 ) )
            return false;
        if ( price == null ) {
            if ( other.price != null )
                return false;
        } else if ( !price.equals( other.price ) )
            return false;
        if ( proDate == null ) {
            if ( other.proDate != null )
                return false;
        } else if ( !proDate.equals( other.proDate ) )
            return false;
        if ( proName == null ) {
            if ( other.proName != null )
                return false;
        } else if ( !proName.equals( other.proName ) )
            return false;
        if ( saleInfo == null ) {
            if ( other.saleInfo != null )
                return false;
        } else if ( !saleInfo.equals( other.saleInfo ) )
            return false;
        if ( useYear == null ) {
            if ( other.useYear != null )
                return false;
        } else if ( !useYear.equals( other.useYear ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AssetTempBean [biaoshi=" + biaoshi + ", code1=" + code1 + ", name1=" + name1 + ", code2=" + code2
                + ", name2=" + name2 + ", code3=" + code3 + ", name3=" + name3 + ", code4=" + code4 + ", name4="
                + name4 + ", code5=" + code5 + ", name5=" + name5 + ", modelType=" + modelType + ", proDate=" + proDate
                + ", proName=" + proName + ", saleInfo=" + saleInfo + ", price=" + price + ", buyDate=" + buyDate
                + ", useYear=" + useYear + ", device=" + device + "]";
    }
    

    
}
