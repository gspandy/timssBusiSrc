package com.timss.inventory.listener;

import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvStocktaking;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvStocktakingService;
import com.timss.inventory.vo.InvMatApplyVO;
import com.yudean.homepage.bean.DeleteDraftParam;
import com.yudean.itc.annotation.HopAnnotation;
import com.yudean.itc.annotation.HopAnnotation.ProType;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InventoryListener.java
 * @author: 890166
 * @createDate: 2015-3-2
 * @updateUser: 890166
 * @version: 1.0
 */

@Component
public class InventoryListener {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private InvStocktakingService invStocktakingService;

    /**
     * @description:领料删除草稿
     * @author: 890166
     * @createDate: 2015-3-2
     * @param param :
     */
    @HopAnnotation(value = "invMatApply", type = ProType.DeleteDraft, Sync = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void invMatApplyDeleteDraft(DeleteDraftParam param) {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String curUser = userInfo.getUserId();

        String flowId = param.getFlowId();
        String siteId = param.getSiteid();

        try {
            String imaid = invMatApplyService.queryImaIddByFlowNo( flowId, siteId );

            // 更新业务表状态
            InvMatApply ima = new InvMatApply();
            ima.setImaid( imaid );
            ima.setStatus( "deleted" );
            invMatApplyService.updateMatApply( ima );

            // 通过业务表获取instanceid，然后用于终止流程
            List<InvMatApplyVO> imavList = invMatApplyService.queryMatApplyForm( userInfo, imaid, "1" );
            if ( null != imavList && !imavList.isEmpty() ) {
                InvMatApplyVO imaVO = imavList.get( 0 );
                List<Task> tasks = workflowService.getActiveTasks( imaVO.getInstanceid() );
                if ( null != tasks && !tasks.isEmpty() ) {
                    for ( Task task : tasks ) {
                        workflowService.stopProcess( task.getId(), curUser, curUser, "删除草稿" );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException( "---------InventoryListener 中的 invMatApplyDeleteDraft 方法抛出异常---------：", e );
        }
    }

    /**
     * @description:库存盘点草稿删除
     * @author: 890166
     * @createDate: 2015-3-2
     * @param param :
     */
    @HopAnnotation(value = "invStocktaking", type = ProType.DeleteDraft, Sync = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void invStocktakingDeleteDraft(DeleteDraftParam param) {
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String curUser = userInfo.getUserId();

        String flowId = param.getFlowId();
        String siteId = param.getSiteid();

        try {
            String istid = invStocktakingService.queryIstidByFlowNo( flowId, siteId );

            // 更新业务表状态
            InvStocktaking ist = new InvStocktaking();
            ist.setIstid( istid );
            ist.setStatus( "stop" );
            invStocktakingService.updateStocktaking( userInfo, ist );

            // 通过业务表获取instanceid，然后用于终止流程
            List<InvStocktaking> isvList = invStocktakingService.queryStocktakingInfo( userInfo, istid );
            if ( null != isvList && !isvList.isEmpty() ) {
                InvStocktaking istaking = isvList.get( 0 );
                List<Task> tasks = workflowService.getActiveTasks( istaking.getInstanceid() );
                if ( null != tasks && !tasks.isEmpty() ) {
                    for ( Task task : tasks ) {
                        workflowService.stopProcess( task.getId(), curUser, curUser, "此流程已被中止" );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException( "---------InventoryListener 中的 invStocktakingDeleteDraft 方法抛出异常---------：", e );
        }
    }
}
