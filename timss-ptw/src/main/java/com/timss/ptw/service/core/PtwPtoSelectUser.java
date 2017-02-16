package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.ptw.bean.PtwType;
import com.timss.ptw.dao.PtwPtoSelectUserDao;
import com.timss.ptw.service.PtwTypeService;
import com.timss.ptw.vo.PtwPtoSelectUserInfoVo;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.SelectUserInfo;
import com.yudean.workflow.interfaces.SelectUserInterface;
import com.yudean.workflow.service.WorkflowService;

@Component("PtwPtoSelectUser")
public class PtwPtoSelectUser implements SelectUserInterface {

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwTypeService ptwTypeService;
    @Autowired
    private PtwPtoSelectUserDao ptwPtoSelectUserDao;
    @Autowired
    private IAuthorizationManager authManager;
    private static final Logger LOG = Logger.getLogger( PtwPtoSelectUser.class );

    @Override
    public List<SecureUser> selectUsers(SelectUserInfo selectUserInfo) {
        LOG.info( "-------------进入两票选人接口实现 工作流选人-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String type = workflowService.getVariable( selectUserInfo.getProcessInstId(), "type" ).toString();
        String category = workflowService.getVariable( selectUserInfo.getProcessInstId(), "category" ).toString();
        
        if("sptw".equals( category)){  //如果是标准工作票需要转换，因为类型和数据库中配置的不同
            int indexOfLine = Integer.valueOf( type.lastIndexOf( "_" ) );
            String typeIdString = type.substring( indexOfLine + 1 );
            PtwType  ptwType = ptwTypeService.queryPtwTypeById( Integer.valueOf( typeIdString ) );
            type = ptwType.getTypeCode();
        }
        
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        String processInstId = selectUserInfo.getProcessInstId();
        //获取活动环节
        List<Task> activities = workflowService.getActiveTasks( processInstId );
        //当前环节key
        String taskDefKey  = activities.get( 0 ).getTaskDefinitionKey();
        //获取之后环节key getNextTaskDefKeys方法，如果以当前环节为起点，只有一条流程，那么返回下一个环节的节点key；
        //如果以当前环节为起点有多条流程，若下一个节点是一个用户任务，那么返回多个环节的节点key(目前尚未遇到这种情况的流程),
        //若下一个节点是一个排他门（条件分支），会根据流程条件，取下一个环节的节点key
        List<String> nextTaskKeyList = workflowService.getNextTaskDefKeys( processInstId, taskDefKey );
        //获取接下来第一个环节key
        String step = nextTaskKeyList.get( 0 );
        String siteId  = userInfoScope.getSiteId();
        PtwPtoSelectUserInfoVo ptwPtoSelectUserInfoVo = new PtwPtoSelectUserInfoVo();
        ptwPtoSelectUserInfoVo.setType( type );
        ptwPtoSelectUserInfoVo.setStep( step );
        ptwPtoSelectUserInfoVo.setCategory( category );
        ptwPtoSelectUserInfoVo.setSiteid( siteId );
        ptwPtoSelectUserInfoVo.setCurDate( new Date() );
        List<PtwPtoSelectUserInfoVo> userInfos = ptwPtoSelectUserDao.queryPtwPtoUserInfo( ptwPtoSelectUserInfoVo );
        for ( PtwPtoSelectUserInfoVo ptwPtoUserInfo : userInfos ) {
            SecureUser user = new SecureUser();
            user.setId( ptwPtoUserInfo.getUserId() );
            user.setName( ptwPtoUserInfo.getUserName() );
            resultList.add( user );
        }

        return resultList;
    }
}

