package com.timss.workorder.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.Workapply;
import com.yudean.itc.dto.Page;



public interface WoWorkapplyDao {

    List<Workapply> queryAllWorkapply(Page<Workapply> page);

    Workapply queryWorkapplyById(String id);
    
    int insertWorkapply(Workapply workapply);

    int deleteWorkapply(@Param("woapplyId")String id);

    int updateWorkapply(Workapply workapply);

    /**
     * @description:修改工单申请状态
     * @author: 王中华
     * @createDate: 2015-12-21
     * @param woapplyId
     * @param status:
     */
    int updateWoaplyStatus(@Param("woapplyId")String woapplyId, @Param("status")String status);
	
   
    /**
     * @description:修改当前处理人 
     * @author: 王中华
     * @createDate: 2015-12-21
     * @param parmas:  woapplyId,currHandlerUser,currHandUserName
     * @return:
     */
    int updateCurrHander(Map<String, String> parmas);

    /**
     * @description:修改流程实例ID
     * @author: 王中华
     * @createDate: 2015-12-21
     * @param woapplyId
     * @param processInstId:
     */
    void updateWorkflowId(@Param("woapplyId")String woapplyId, @Param("workflowId")String processInstId);
}
