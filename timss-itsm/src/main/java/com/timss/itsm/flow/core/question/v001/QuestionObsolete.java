package com.timss.itsm.flow.core.question.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.dao.ItsmQuestionRdDao;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class QuestionObsolete extends TaskHandlerBase {

    // @Autowired
    // private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmQuestionRdDao questionRdDao;

    private static final Logger LOG = Logger.getLogger( QuestionObsolete.class );

    public void init(TaskInfo taskInfo) {
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        ItsmQuestionRd questionRd = new ItsmQuestionRd();
        questionRd.setId( Integer.valueOf( id ) );
        questionRd.setStatus( "suspended" );
        String[] params = new String[] { "status" };
        questionRdDao.updateQuestionRd( questionRd, params );
    }

    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘问题作废’的onComplete(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        ItsmQuestionRd questionRd = new ItsmQuestionRd();
        questionRd.setId( Integer.valueOf( id ) );
        questionRd.setStatus( "closed" );
        String[] params = new String[] { "status" };
        questionRdDao.updateQuestionRd( questionRd, params );
    }
}
