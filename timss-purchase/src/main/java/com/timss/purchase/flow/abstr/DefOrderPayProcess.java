package com.timss.purchase.flow.abstr;

import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public abstract class DefOrderPayProcess extends TaskHandlerBase {
   
	@Override
    public void init(TaskInfo taskInfo) {
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo) {
        super.onComplete( taskInfo );
    }
}
