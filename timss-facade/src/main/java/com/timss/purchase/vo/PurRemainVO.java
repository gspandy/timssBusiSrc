package com.timss.purchase.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurRemainVO.java
 * @author: 890166
 * @createDate: 2014-10-16
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurRemainVO {

    private int remainnum;
    private String sheetid;
    private String itemid;

    /**
     * @return the remainnum
     */
    public int getRemainnum() {
        return remainnum;
    }

    /**
     * @param remainnum the remainnum to set
     */
    public void setRemainnum(int remainnum) {
        this.remainnum = remainnum;
    }

    /**
     * @return the sheetid
     */
    public String getSheetid() {
        return sheetid;
    }

    /**
     * @param sheetid the sheetid to set
     */
    public void setSheetid(String sheetid) {
        this.sheetid = sheetid;
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

}
