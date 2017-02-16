package com.timss.pms.service;

import com.timss.pms.bean.FlowVoidParamBean;

/**
 * 流程作废接口
 * @ClassName:     FlowVoidService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-12-23 上午10:35:07
 */
public interface FlowVoidService {
	boolean voidFlow(FlowVoidParamBean flowVoidParamBean);
}
