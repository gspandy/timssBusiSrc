package com.timss.ptw.service.core;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwProcess;
import com.timss.ptw.dao.PtwProcessDao;
import com.timss.ptw.service.PtwProcessService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.workflow.service.WorkflowService;

@Service
public class PtwProcessServiceImpl implements PtwProcessService {
    private static final Logger log = Logger.getLogger( PtwProcessService.class );

    @Autowired
    private PtwProcessDao ptwProcessDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private HomepageService homepageService;

    @Override
    public List<PtwProcess> queryPtwProcessByPtwId(int wtId) {
        return ptwProcessDao.queryPtwProcessByPtwId( wtId );
    }

    @Override
    public PtwProcess queryPtwProcessByWtIdAndStatus(int wtId, int wtStatus) {
        PtwProcess ptwProcess = new PtwProcess();
        ptwProcess.setWtId( wtId );
        ptwProcess.setWtStatus( wtStatus );
        return ptwProcessDao.queryPtwProcessByWtIdAndStatus( ptwProcess );
    }

    @Override
    public int insertPtwProcess(PtwProcess ptwProcess) {
        return ptwProcessDao.insertPtwProcess( ptwProcess );
    }

    @Override
    public String startNewProcess(PtwInfo ptwInfo, UserInfoScope userInfoScope, String processName, String processCode)
            throws Exception {
        // 启动会签流程
        String defkey = workflowService.queryForLatestProcessDefKey( "ptw_" + userInfoScope.getSiteId().toLowerCase()
                + "_" + processCode );// 获取最新流程定义版本
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put( "businessId", ptwInfo.getId() );

        // 启动新流程
        ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey,
                userInfoScope.getUserId(), map );
        // 获取流程实例ID
        String processInstId = processInstance.getProcessInstanceId();

        PtwProcess ptwProcess = new PtwProcess();
        ptwProcess.setProcessId( processInstId );
        ptwProcess.setWtId( ptwInfo.getId() );
        ptwProcess.setWtStatus( ptwInfo.getWtStatus() );
        insertPtwProcess( ptwProcess );

        // 第一次启动流程 添加“待办”(未测试)
        // 待办列表中的编号
        String flowCode = ptwInfo.getWtNo();
        String jumpPath = "ptw/ptwInfo/preQueryPtwInfo.do?params={opType:'handlePtw',id:" + ptwInfo.getId() + "}";

        // kchen 2015-05-05 modify， 修改流程调用接口
        homepageService.createProcess( flowCode, processInstId, "动火票会签", processName, processName, jumpPath,
                userInfoScope, null );
        // kchen 2015-05-05

        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();
        return taskId;
    }

}
