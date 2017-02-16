package com.timss.workorder.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.WorkOrderService;
import com.timss.workorder.service.WorkOrderServiceDef;
import com.yudean.itc.dto.Page;

@Service("WorkOrderServiceImpl")
public class WorkOrderServiceImpl implements WorkOrderService{
    @Autowired
    private WorkOrderServiceDef workOrderServiceDef;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> insertWorkOrder(Map<String, String> WODataMap) throws Exception {
        return  workOrderServiceDef.insertWorkOrder( WODataMap );
    }
    
    @Override
    public Map<String, String> cancelCommitWO(String woId) {
        return workOrderServiceDef.cancelCommitWO( woId );
    }

    @Override
    public void deleteWorkOrder(int woId) {
        workOrderServiceDef.deleteWorkOrder( woId );
    }

    @Override
    public void deleteWorkOrderByWoCode(String woCode, String siteid) {
        workOrderServiceDef.deleteWorkOrderByWoCode( woCode, siteid );
    }

    @Override
    public int getNextWOId() {
        return workOrderServiceDef.getNextWOId();
    }

    @Override
    public int getUserWoSum(String userId, String siteId) {
        return workOrderServiceDef.getUserWoSum( userId, siteId );
    }

    @Override
    public boolean loginUserIsCusSer() {
        return false;
    }

    @Override
    public void obsoleteWorkOrder(String woIdString) {
        workOrderServiceDef.obsoleteWorkOrder( woIdString );
    }


    @Override
    public Page<WorkOrder> queryAllRelateWoOfQx(Page<WorkOrder> page) {
        return workOrderServiceDef.queryAllRelateWoOfQx( page );
    }

    @Override
    public Page<WorkOrder> queryAllWO(Page<WorkOrder> page) throws Exception {
        return workOrderServiceDef.queryAllWO( page );
    }

    @Override
    public Map<String, Object> queryItWOById(int woId) {
        return workOrderServiceDef.queryItWOById( woId );
    }

    @Override
    public Map<String, Object> queryWOBaseInfoByWOCode(String woCode, String siteId) {
        return workOrderServiceDef.queryWOBaseInfoByWOCode( woCode, siteId );
    }

    @Override
    public Map<String, String> saveWOOnPlan() throws Exception {
        return workOrderServiceDef.saveWOOnPlan();
    }

    @Override
    public Map<String, Object> saveWorkOrder(Map<String, String> addWODataMap) throws Exception {
        return workOrderServiceDef.saveWorkOrder( addWODataMap );
    }

    @Override
    public void stopWorkOrder(Map<String, Object> parmas) {
        workOrderServiceDef.stopWorkOrder( parmas );
    }


    @Override
    public void updateOperUserById(Map<String, String> parmas) {
        workOrderServiceDef.updateOperUserById( parmas );
    }

    @Override
    public void updateWOAddPTWId(int woId, int ptwId) {
        workOrderServiceDef.updateWOAddPTWId( woId, ptwId );
    }

    @Override
    public void updateWOHandlerStyle(Map<String, Object> handStyleMap) {
        workOrderServiceDef.updateWOHandlerStyle( handStyleMap );
    }

    @Override
    public void updateWOOnAcceptance(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnAcceptance( parmas );
    }

    @Override
    public void updateWOOnPlan(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnPlan( parmas );
    }

    @Override
    public void updateWOOnReport(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOOnReport( parmas );
    }

    @Override
    public void updateWOStatus(Map<String, Object> parmas) {
        workOrderServiceDef.updateWOStatus( parmas );
    }

    @Override
    public Map<String, String> updateWoBaseInfo() throws Exception {
        return workOrderServiceDef.updateWoBaseInfo();
    }

    @Override
    public Map<String, String> updateWorkOrder(Map<String, String> addWODataMap) throws Exception {
        return workOrderServiceDef.updateWorkOrder( addWODataMap );
    }

    @Override
    public void updateWorkflowId(String workflowId, String woId) {
        workOrderServiceDef.updateWorkflowId( workflowId, woId );
    }

    @Override
    public Map<String, String> wobackToSomeStep(String woId, String woStepFlag) throws Exception {
        return workOrderServiceDef.wobackToSomeStep( woId, woStepFlag );
    }

    @Override
    public void cycMtpObjToWo(String mtpId, String todoId) throws Exception {
        workOrderServiceDef.cycMtpObjToWo( mtpId, todoId );
        
    }

    @Override
    public void inPtwToNextStep(int woId, String userId) {
        workOrderServiceDef.inPtwToNextStep( woId, userId );
    }

    @Override
    public Map<String, Object> queryWOById(int woId) {
        return workOrderServiceDef.queryItWOById( woId );
    }

    @Override
    public void rollbackCommitWo(Map<String, String> addWODataMap) throws Exception {
        workOrderServiceDef.rollbackCommitWo( addWODataMap );
    }

    @Override
    public List<WorkOrder> queryAllDelayWoNoSiteId() {
        Map<String, String> queryParams = new HashMap<String, String>();
        //查询条件WO_DELAY_LEN不为空，WO_IS_DELAY值为Y,CURR_STATUS的值为值长启动工单（DELAY_DUTY_RESTART）
        queryParams.put( "woIsDelay", "Y" );  //是延时工单
        queryParams.put( "currStatus", "DELAY_DUTY_RESTART" );  //状态正处于“值长启动工单”
        queryParams.put( "woDelayLen", "Y" ); //有延时长度值
        return workOrderServiceDef.queryAllDelayWoNoSiteId(queryParams);
    }

    @Override
    public String getDelayRestartNextTaskKey() {
        return null;
    }

    @Override
    public void updateCurrHandUserById(Map<String, String> parmas) {
        workOrderServiceDef.updateCurrHandUserById(parmas);
    }

    
}
