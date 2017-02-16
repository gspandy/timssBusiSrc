package com.timss.workorder.service.swf;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.util.WoProcessStatusUtil;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("WoSelectReportUser")
public class WoSelectReportUser implements SelectUserInterface {
    @Autowired
    private SelectUserService selectUserService;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger(WoSelectReportUser.class);

    public WoSelectReportUser() {
    }

    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info("-------------进入湛江生物质选择完工汇报人接口-----------------");
        
        String woId = workflowService.getVariable(selectUserInfo.getProcessInstId(), "businessId").toString();
        WorkOrder wo = workOrderDao.queryWOById(Integer.valueOf(woId));
        
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        if(wo.getCurrStatus().equals(WoProcessStatusUtil.DELAY_DUTY_RESTART_STR)){
        	//resultList = selectUserService.byHistroicAssignee("supervisor_report");
        	resultList = selectUserService.byUserId(wo.getEndReportUser());
        }
        else {
        	//resultList = selectUserService.byPreTaskCandidate();
        	resultList = selectUserService.byLoginUser();
		}
        return resultList;
    }  
}
