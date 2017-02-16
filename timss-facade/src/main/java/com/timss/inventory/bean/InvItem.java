package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资项目表
 * @description: 物资项目表
 * @company: gdyd
 * @className: InvItem.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvItem extends ItcMvcBean {

    private static final long serialVersionUID = -4439904691883734873L;

    @AutoGen(value = "INV_ITEM_ID", requireType = GenerationType.REQUIRED_NULL)
    private String itemid; // 物资ID

    @AutoGen(value = "INV_ITEM_CODE", requireType = GenerationType.REQUIRED_NULL)
    private String itemcode; // 物资编码
    private String itemname; // 物资名称
    private String cusmodel; // 物资参数
    private String siteId; // 站点ID
    private String isspare; // 是否备件，1是0否
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间
    private String img; // 图片
    private String unit1; // 单位1
    private String unit2; // 单位2
    private String unit3; // 单位3
    private String unit4; // 单位4
    private String unit5; // 单位5
    private BigDecimal qtyUnit1; // 单位数量1
    private BigDecimal qtyUnit2; // 单位数量2
    private BigDecimal qtyUnit3; // 单位数量3
    private BigDecimal qtyUnit4; // 单位数量4
    private BigDecimal qtyUnit5; // 单位数量5
    private String attr1; // 保留字段1
    private String attr2; // 保留字段2
    private String attr3; // 保留字段3
    private String attr4; // 保留字段4
    private String attr5; // 保留字段5
    private String attr6; // 保留字段6
    private String attr7; // 保留字段7
    private String attr8; // 保留字段8
    private String attr9; // 保留字段9
    private String attr10; // 保留字段10

    private int ishis;// 是否历史库存

    /**
     * @return the ishis
     */
    public int getIshis() {
        return ishis;
    }

    /**
     * @param ishis the ishis to set
     */
    public void setIshis(int ishis) {
        this.ishis = ishis;
    }

    /**
     * @return the itemid
     */
    public String getItemid() {
        return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     * @return the itemcode
     */
    public String getItemcode() {
        return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    /**
     * @return the itemname
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * @return the cusmodel
     */
    public String getCusmodel() {
        return cusmodel;
    }

    /**
     * @param cusmodel the cusmodel to set
     */
    public void setCusmodel(String cusmodel) {
        this.cusmodel = cusmodel;
    }

    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the isspare
     */
    public String getIsspare() {
        return isspare;
    }

    /**
     * @param isspare the isspare to set
     */
    public void setIsspare(String isspare) {
        this.isspare = isspare;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    /**
     * @return the modifyuser
     */
    public String getModifyuser() {
        return modifyuser;
    }

    /**
     * @param modifyuser the modifyuser to set
     */
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    /**
     * @return the createdate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the modifydate
     */
    public Date getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the unit1
     */
    public String getUnit1() {
        return unit1;
    }

    /**
     * @param unit1 the unit1 to set
     */
    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    /**
     * @return the unit2
     */
    public String getUnit2() {
        return unit2;
    }

    /**
     * @param unit2 the unit2 to set
     */
    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }

    /**
     * @return the unit3
     */
    public String getUnit3() {
        return unit3;
    }

    /**
     * @param unit3 the unit3 to set
     */
    public void setUnit3(String unit3) {
        this.unit3 = unit3;
    }

    /**
     * @return the unit4
     */
    public String getUnit4() {
        return unit4;
    }

    /**
     * @param unit4 the unit4 to set
     */
    public void setUnit4(String unit4) {
        this.unit4 = unit4;
    }

    /**
     * @return the unit5
     */
    public String getUnit5() {
        return unit5;
    }

    /**
     * @param unit5 the unit5 to set
     */
    public void setUnit5(String unit5) {
        this.unit5 = unit5;
    }

    /**
     * @return the qtyUnit1
     */
    public BigDecimal getQtyUnit1() {
        return qtyUnit1;
    }

    /**
     * @param qtyUnit1 the qtyUnit1 to set
     */
    public void setQtyUnit1(BigDecimal qtyUnit1) {
        this.qtyUnit1 = qtyUnit1;
    }

    /**
     * @return the qtyUnit2
     */
    public BigDecimal getQtyUnit2() {
        return qtyUnit2;
    }

    /**
     * @param qtyUnit2 the qtyUnit2 to set
     */
    public void setQtyUnit2(BigDecimal qtyUnit2) {
        this.qtyUnit2 = qtyUnit2;
    }

    /**
     * @return the qtyUnit3
     */
    public BigDecimal getQtyUnit3() {
        return qtyUnit3;
    }

    /**
     * @param qtyUnit3 the qtyUnit3 to set
     */
    public void setQtyUnit3(BigDecimal qtyUnit3) {
        this.qtyUnit3 = qtyUnit3;
    }

    /**
     * @return the qtyUnit4
     */
    public BigDecimal getQtyUnit4() {
        return qtyUnit4;
    }

    /**
     * @param qtyUnit4 the qtyUnit4 to set
     */
    public void setQtyUnit4(BigDecimal qtyUnit4) {
        this.qtyUnit4 = qtyUnit4;
    }

    /**
     * @return the qtyUnit5
     */
    public BigDecimal getQtyUnit5() {
        return qtyUnit5;
    }

    /**
     * @param qtyUnit5 the qtyUnit5 to set
     */
    public void setQtyUnit5(BigDecimal qtyUnit5) {
        this.qtyUnit5 = qtyUnit5;
    }

    /**
     * @return the attr1
     */
    public String getAttr1() {
        return attr1;
    }

    /**
     * @param attr1 the attr1 to set
     */
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    /**
     * @return the attr2
     */
    public String getAttr2() {
        return attr2;
    }

    /**
     * @param attr2 the attr2 to set
     */
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    /**
     * @return the attr3
     */
    public String getAttr3() {
        return attr3;
    }

    /**
     * @param attr3 the attr3 to set
     */
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    /**
     * @return the attr4
     */
    public String getAttr4() {
        return attr4;
    }

    /**
     * @param attr4 the attr4 to set
     */
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    /**
     * @return the attr5
     */
    public String getAttr5() {
        return attr5;
    }

    /**
     * @param attr5 the attr5 to set
     */
    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    /**
     * @return the attr6
     */
    public String getAttr6() {
        return attr6;
    }

    /**
     * @param attr6 the attr6 to set
     */
    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    /**
     * @return the attr8
     */
    public String getAttr8() {
        return attr8;
    }

    /**
     * @param attr8 the attr8 to set
     */
    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    /**
     * @return the attr7
     */
    public String getAttr7() {
        return attr7;
    }

    /**
     * @param attr7 the attr7 to set
     */
    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    /**
     * @return the attr9
     */
    public String getAttr9() {
        return attr9;
    }

    /**
     * @param attr9 the attr9 to set
     */
    public void setAttr9(String attr9) {
        this.attr9 = attr9;
    }

    /**
     * @return the attr10
     */
    public String getAttr10() {
        return attr10;
    }

    /**
     * @param attr10 the attr10 to set
     */
    public void setAttr10(String attr10) {
        this.attr10 = attr10;
    }

}
