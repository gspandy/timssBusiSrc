package com.timss.pms.service;

import java.util.Map;

/**
 * 根据流程节点配置更新流程变量类
 * @ClassName:     WorkflowVariableUpdateService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-25 上午10:58:25
 */
public interface WorkflowVariableUpdateService {
	boolean setWFVariable(String taskId,String processInstId,Map data);
}
