package com.timss.itsm.flow.core.question.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.itsm.bean.ItsmQuestionRd;
import com.timss.itsm.dao.ItsmQuestionRdDao;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class CreateQuestion extends TaskHandlerBase {

//    @Autowired
//    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItsmQuestionRdDao questionRdDao;

    private static final Logger LOG = Logger.getLogger( CreateQuestion.class );

    public void init(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘新建问题’的init(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        ItsmQuestionRd questionRd = new ItsmQuestionRd();
        questionRd.setId( Integer.valueOf( id ) );
        questionRd.setStatus( "new" );
        String[] params = new String[] { "status" };
        questionRdDao.updateQuestionRd( questionRd, params );
        LOG.debug( "-------------进入‘新建问题’的init(),处理业务逻辑结束-----------------" );
    }

    public void onComplete(TaskInfo taskInfo) {
    }
}
