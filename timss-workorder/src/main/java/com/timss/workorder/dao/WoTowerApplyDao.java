package com.timss.workorder.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.TowerApply;
import com.yudean.itc.dto.Page;



public interface WoTowerApplyDao {
    
    int insertTowerApply(TowerApply towerApply);
    
    int deleteTowerApply(@Param("towerApplyId")String id);
    
    TowerApply queryTowerApplyById(String id);
    
    int updateTowerApply(TowerApply towerApply);
    
    /**
     * @description:查询所有
     * @author: 朱旺
     * @createDate: 2015-12-21
     * @param: page
     * @return:
     */
    List<TowerApply> queryAllTowerApply(Page<TowerApply> page);
    
    /**
     * @description:修改当前处理人 
     * @author: 朱旺
     * @createDate: 2015-12-22
     * @param parmas:  towerApplyId,currHandlerUser,currHandUserName
     * @return:
     */
    int updateCurrHander(Map<String, String> parmas);
    
    /**
     * @description:修改流程实例ID
     * @author: 朱旺
     * @createDate: 2015-12-22
     * @param woapplyId
     * @param processInstId:
     */
    void updateWorkflowId(@Param("towerApplyId")String towerApplyId, @Param("workflowId")String processInstId);
    
    /**
     * @description:修改登塔申请状态
     * @author: 朱旺
     * @createDate: 2015-12-22
     * @param woapplyId
     * @param status:
     */
    int updateTowerApplyStatus(@Param("towerApplyId")String towerApplyId, @Param("status")String status);
}
