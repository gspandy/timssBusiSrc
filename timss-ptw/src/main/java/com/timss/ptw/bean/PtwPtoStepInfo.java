package com.timss.ptw.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 两票环节信息bean
 * @description: 两票环节信息
 * @company: gdyd
 * @className: PtwPtoStepInfo.java
 * @author: gucw
 * @createDate: 2015年7月28日
 * @updateUser:
 * @version: 1.0
 */
public class PtwPtoStepInfo extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -8863725330500507359L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id;
    private String category;
    private String stepName;
    private String step;
    private String type;
    

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    @Override
    public String toString() {
        return "PtwPtoStepInfo [id=" + id + ", category=" + category + ", stepName=" + stepName + ", step=" + step + ", type="
                + type + "]";
    }
    
}
