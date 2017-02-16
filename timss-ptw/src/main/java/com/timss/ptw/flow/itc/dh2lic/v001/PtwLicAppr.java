package com.timss.ptw.flow.itc.dh2lic.v001;

import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.service.PtwFireInfoService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class PtwLicAppr extends TaskHandlerBase {
    @Autowired
    private ItcMvcService ItcMvcService;
    @Autowired
    private PtwFireInfoService ptwFireInfoService;
    
    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String userName = userInfoScope.getUserName();
        try {
            String businessData = userInfoScope.getParam( "businessData" );
            JSONObject obj = JSONObject.fromObject(businessData);
            String wtId = obj.getString( "ptwId" );
            ptwFireInfoService.updatePtwFireLicAppr( Integer.parseInt( wtId ), userId, userName, new Date() );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
        
    }
}
