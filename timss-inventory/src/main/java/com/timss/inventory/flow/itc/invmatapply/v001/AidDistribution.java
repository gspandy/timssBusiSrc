package com.timss.inventory.flow.itc.invmatapply.v001;

import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AidDistribution.java
 * @author: 890166
 * @createDate: 2014-7-29
 * @updateUser: 890166
 * @version: 1.0
 */
public class AidDistribution extends TaskHandlerBase {

    /**
     * 当完成了物资发放环节之后需要发送信息给用户，这时就需要这里的方法
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        super.onComplete( taskInfo );
    }
}
