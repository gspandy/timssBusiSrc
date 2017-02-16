package com.timss.inventory.flow.zjw.invmattransfer.v001;

import org.apache.log4j.Logger;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class TransferApplyCommit  extends TaskHandlerBase{
    
    private static Logger logger = Logger.getLogger(TransferApplyCommit.class);

    @Override
    public void init(TaskInfo taskInfo){
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo){
        super.onComplete( taskInfo );
    }
}
