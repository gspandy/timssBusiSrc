package com.timss.inventory.flow.zjw.accept.v001;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.core.InvMatAcceptDetailService;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.utils.AcceptStatusEnum;
import com.timss.inventory.utils.GetAcceptDataInWorkflowUtil;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.workflow.IsAcceptToRK;
import com.timss.inventory.workflow.IsAcceptToZJYS;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description:执行验收
 * @company: gdyd
 * @className: AidDistribution.java
 * @author: 890166
 * @createDate: 2014-7-29
 * @updateUser: 890166
 * @version: 1.0
 */
public class Cgsqrzxys extends TaskHandlerBase {

	private static final Logger LOGGER = Logger.getLogger(Cgsqrzxys.class);
    @Autowired
    InvMatAcceptService invMatAcceptService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    GetAcceptDataInWorkflowUtil getAcceptDataInWorkflowUtil;
    @Autowired
    InvMatAcceptDetailService invMatAcceptDetailService;
    @Autowired
    IsAcceptToRK isAcceptToRK;
    @Autowired
    IsAcceptToZJYS isAcceptToZJYS;

    @Override
    public void init(TaskInfo taskInfo) {
        // 更新状态
        String inacId = (String) workflowService.getVariable( taskInfo.getProcessInstanceId(), "inacId" );
        InvMatAccept invMatAccept = new InvMatAccept();
        invMatAccept.setInacId( inacId );
        invMatAccept.setStatus( AcceptStatusEnum.ACCEPTING.toString() );
        invMatAcceptService.updateInvMatAccept( invMatAccept );
    }

    @Override
    public void onComplete(TaskInfo taskInfo) {
        // 更新
        InvMatAccept invMatAccept = getAcceptDataInWorkflowUtil.getAccept();
        List<InvMatAcceptDetail> details = getAcceptDataInWorkflowUtil.getDetails();
        List<InvMatAcceptDetailVO> detailVOs = getAcceptDataInWorkflowUtil.getDetailVOs();
        invMatAcceptService.updateInvMatAccept( invMatAccept );
        invMatAcceptDetailService.updateInvMatAcceptDetail( invMatAccept, details );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

        String acptCnlus = (String) workflowService.getVariable( taskInfo.getProcessInstanceId(), "acptCnlus" );
        // 当验收结论不为"不合格"，自动入库
        if ( !"FAILURE".equals( acptCnlus ) ) {
            invMatAccept.setInstanceid( taskInfo.getProcessInstanceId() );
            try {
				invMatAcceptService.saveMatTran( invMatAccept, detailVOs, userInfo.getUserId());
			} catch (Exception e) {
				LOGGER.error(e);
			    throw new RuntimeException( "流程审批结束时物资入库失败" , e );
			}

            String inacId = (String) workflowService.getVariable( taskInfo.getProcessInstanceId(), "inacId" );
            invMatAccept = new InvMatAccept();
            invMatAccept.setInacId( inacId );
            invMatAccept.setStatus( AcceptStatusEnum.INVIN.toString() );
            invMatAcceptService.updateInvMatAccept( invMatAccept );

        } else {
            String inacId = (String) workflowService.getVariable( taskInfo.getProcessInstanceId(), "inacId" );
            invMatAccept = new InvMatAccept();
            invMatAccept.setInacId( inacId );
            invMatAccept.setStatus( AcceptStatusEnum.ACCEPTED.toString() );
            invMatAcceptService.updateInvMatAccept( invMatAccept );
        }
    }
}
