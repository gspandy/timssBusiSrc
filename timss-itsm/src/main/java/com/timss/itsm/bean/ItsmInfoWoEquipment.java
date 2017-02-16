package com.timss.itsm.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmInfoWoEquipment extends ItcMvcBean {

    private static final long serialVersionUID = -3793640858475678071L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL) // 自动生成id
    private String id; // ID
    private String name; // 物资名称（型号）
    private Double num; //数量    
    private String lendStatus; // 借出状态
    private String returnStatus; // 归还状态
    private String infoWoId;  //关联信息工单ID
    
    public String getInfoWoId() {
        return infoWoId;
    }
    public void setInfoWoId(String infoWoId) {
        this.infoWoId = infoWoId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getNum() {
        return num;
    }
    public void setNum(Double num) {
        this.num = num;
    }
    public String getLendStatus() {
        return lendStatus;
    }
    public void setLendStatus(String lendStatus) {
        this.lendStatus = lendStatus;
    }
    public String getReturnStatus() {
        return returnStatus;
    }
    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }
    @Override
    public String toString() {
        return "ItsmInfoWoEquipment [id=" + id + ", name=" + name + ", num=" + num + ", lendStatus=" + lendStatus
                + ", returnStatus=" + returnStatus + "]";
    }
   

}
