package com.timss.ptw.vo;

import java.util.List;
import com.timss.ptw.bean.PtoInfo;
import com.timss.ptw.bean.PtoOperItem;


public class PtoInfoVo extends PtoInfo {

    private static final long serialVersionUID = 1656401501335366863L;

    private List<PtoOperItem> ptoOperItemList;
   
    private String commitStyle;
    private String attach;
    private String taskId;
   
    public List<PtoOperItem> getPtoOperItemList() {
        return ptoOperItemList;
    }

    public void setPtoOperItemList(List<PtoOperItem> ptoOperItemList) {
        this.ptoOperItemList = ptoOperItemList;
    }

    public String getCommitStyle() {
        return commitStyle;
    }

    public void setCommitStyle(String commitStyle) {
        this.commitStyle = commitStyle;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "PtoInfoVo [ptoOperItemList=" + ptoOperItemList + ", commitStyle=" + commitStyle + ", attach=" + attach
                + "]";
    }

   

    
}
