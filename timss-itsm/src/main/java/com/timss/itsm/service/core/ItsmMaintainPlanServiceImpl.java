package com.timss.itsm.service.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWoAttachment;
import com.timss.itsm.dao.ItsmMaintainPlanDao;
import com.timss.itsm.dao.ItsmWoAttachmentDao;
import com.timss.itsm.service.ItsmJobPlanService;
import com.timss.itsm.service.ItsmMaintainPlanService;
import com.timss.itsm.service.ItsmWoUtilService;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmMaintainPlanServiceImpl implements ItsmMaintainPlanService {
    @Autowired
    private ItcMvcService ItcMvcService;
    @Autowired
    private ItsmMaintainPlanDao maintainPlanDao;
    @Autowired
    private ItsmWoAttachmentDao woAttachmentDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private ItsmWoUtilService woUtilService;
    @Autowired
    private ItsmJobPlanService jobPlanService;

    private static final Logger LOG = Logger.getLogger( ItsmMaintainPlanServiceImpl.class );

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public ItsmMaintainPlan insertMaintainPlan(Map<String, String> addMTPDataMap) throws Exception {
        UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        Integer jpId = null;
        jpId = jobPlanService.insertJobPlan( addMTPDataMap );

        /** 插入维护计划列表 */
        String mtpFormData = addMTPDataMap.get( "mtpFormData" ); // form表单数据
        ItsmMaintainPlan maintainPlan = JsonHelper.fromJsonStringToBean( mtpFormData, ItsmMaintainPlan.class );

        int mtpId = maintainPlanDao.getNextMTPId(); // 获取要插入维护计划的ID
        maintainPlan.setId( mtpId );
        maintainPlan.setJobPlanId( jpId );
        maintainPlan.setMaintainPlanFrom( "cycle_maintainPlan" );// cycle_maintainPlan:周期维护计划

        String workteam = maintainPlan.getWorkTeam();
        List<SecureUser> temp2 = authManager.retriveUsersWithSpecificGroup( workteam, null, false, true );
        String principal = "";
        String principalName = "";
        for ( int i = 0; i < temp2.size(); i++ ) {
            principal += temp2.get( i ).getId() + ",";
            principalName += temp2.get( i ).getName() + ",";
        }
        maintainPlan.setPrincipal( principal.substring( 0, principal.length() - 1 ) );
        maintainPlan.setPrincipalName( principalName.substring( 0, principalName.length() - 1 ) );

        maintainPlan.setCreateuser( userId );
        maintainPlan.setCreatedate( new Date() );
        maintainPlan.setModifydate( new Date() );
        maintainPlan.setModifyuser( userId );
        maintainPlan.setSiteid( siteId );
        maintainPlan.setDeptid( deptId );
        maintainPlan.setYxbz( 1 );

        maintainPlanDao.insertMaintainPlan( maintainPlan );

        // 给维护计划添加附件关联
        String uploadIds = addMTPDataMap.get( "uploadIds" ); // form表单数据
        insertAttachMatch( String.valueOf( mtpId ), uploadIds );

        return maintainPlan;
    }

    /**
     * @description:将附件与业务单的关系插入附件表中
     * @author: 王中华
     * @createDate: 2014-11-13
     * @param businessId
     * @param fileIds
     * @throws Exception:
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void insertAttachMatch(String businessId, String fileIds) throws Exception {
        if ( fileIds != null && fileIds != "" ) {
            String[] ids = fileIds.split( "," );
            for ( int i = 0; i < ids.length; i++ ) {
                ItsmWoAttachment woAttachment = new ItsmWoAttachment();
                woAttachment.setId( businessId );
                woAttachment.setType( "MTP" ); // "WO":工单;"MTP"：维护计划;"JP"：标准作业方案
                woAttachment.setAttachId( ids[i] );
                woAttachmentDao.insertWoAttachment( woAttachment );
            }
            insertAttachmentToServer( ids );
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void insertAttachmentToServer(String[] ids) throws Exception {
        attachmentMapper.setAttachmentsBinded( ids, 1 ); // 1表示绑定，0表示解除绑定
    }

    @Override
    public Page<ItsmMaintainPlan> queryAllMTP(Page<ItsmMaintainPlan> page) throws Exception {

        List<ItsmMaintainPlan> ret = maintainPlanDao.queryAllITMTP( page );

        page.setResults( ret );

        LOG.info( "查询维护计划列表信息" );

        return page;
    }

    @Override
    public Page<ItsmMaintainPlan> queryAllParentMTP(Page<ItsmMaintainPlan> page) {

        List<ItsmMaintainPlan> ret = maintainPlanDao.queryAllParentMTP( page );
        page.setResults( ret );
        LOG.info( "查询维护计划列表信息" );
        return page;
    }

    @Override
    public ItsmMaintainPlan queryMTPById(int id) {
        ItsmMaintainPlan result = maintainPlanDao.queryITMTPById( id );
        String principal = result.getPrincipal();
        String workTeams = result.getWorkTeam();
        String userIds = woUtilService.selectPrincipalList( principal, workTeams ).toString();
        result.setPrincipal( userIds );
        return result;
    }

    @Override
    public int getNextMTPId() {
        return maintainPlanDao.getNextMTPId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateMaintainPlan(Map<String, String> mtpDataMap) throws Exception {
        UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();

        String mtpFormData = mtpDataMap.get( "mtpFormData" );
        ItsmMaintainPlan maintainPlan = JsonHelper.fromJsonStringToBean( mtpFormData, ItsmMaintainPlan.class );

        // int mtpId = maintainPlan.getId(); //获取要修改的维护计划的ID
        maintainPlan.setModifydate( new Date() );
        maintainPlan.setModifyuser( userId );

        int jpId = jobPlanService.updateJobPlan( mtpDataMap ); // 更新维护计划对应的策划信息

        maintainPlan.setJobPlanId( jpId );
        maintainPlanDao.updateMaintainPlan( maintainPlan ); // 更新维护计划form信息

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateMTPToUnvailable(int maintainPlanId) {
        maintainPlanDao.updateMTPToUnvailable( maintainPlanId );
    }

    @Override
    public ItsmMaintainPlan queryITMTPById(Integer id) {
        // TODO Auto-generated method stub
        return maintainPlanDao.queryITMTPById( id );
    }

}
