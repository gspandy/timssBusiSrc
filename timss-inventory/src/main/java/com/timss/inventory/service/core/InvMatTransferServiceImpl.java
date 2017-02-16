package com.timss.inventory.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvMatTransferDao;
import com.timss.inventory.dao.InvMatTransferDetailDao;
import com.timss.inventory.service.InvMatTransferService;
import com.timss.inventory.utils.InvMatTransferStatus;
import com.timss.inventory.vo.InvItemVO;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTransferServiceImpl.java
 * @author: 890151
 * @createDate: 2016-1-8
 * @updateUser: 890151
 * @version: 1.0
 */
@Service("InvMatTransferServiceImpl")
public class InvMatTransferServiceImpl implements InvMatTransferService {

    @Autowired
    private InvMatTransferDao invMatTransferDao;

    @Autowired
    private InvMatTransferDetailDao invMatTransferDetailDao;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private HomepageService homepageService;

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    private InvItemDao invItemDao;

    /**
     * log4j输出
     */
    private static final Logger logger = Logger.getLogger( InvMatTransferServiceImpl.class );

    /**
     * @description:物资领料列表数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @param userInfo
     * @param ima
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvMatTransfer> queryInvMatTransferList(UserInfoScope userInfo, InvMatTransfer imt) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatTransfer> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "userId", userInfo.getUserId() );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "createdate" );
            page.setSortOrder( "desc" );
        }

        if ( null != imt ) {
            page.setParameter( "imtCode", imt.getImtCode() );
            page.setParameter( "name", imt.getName() );
            page.setParameter( "createuser", imt.getCreateuser() );
            page.setParameter( "createdate", imt.getCreatedate() );
            page.setParameter( "status", imt.getStatus() );
        }
        List<InvMatTransfer> ret = invMatTransferDao.queryInvMatTransferList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 查询表单数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    @Override
    public InvMatTransfer queryInvMatTransferById(UserInfoScope userInfo, String imtId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "imtId", imtId );
        return invMatTransferDao.queryInvMatTransferById( imtId );
    }

    /**
     * @description: 查询表单数据
     * @author: 890151
     * @createDate: 2016-1-8
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    @Override
    public InvMatTransfer queryInvMatTransferByCode(UserInfoScope userInfo, String imtCode) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "imtCode", imtCode );
        return invMatTransferDao.queryInvMatTransferById( imtCode );
    }

    /**
     * @description: 根据申请查询物资列表
     * @author: 890151
     * @createDate: 2016-1-8
     * @param imtId
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvMatTransferDetail> queryInvMatTransferDetailList(UserInfoScope userInfo, String imtId)
            throws Exception {
        return invMatTransferDetailDao.queryInvMatTransferDetailList( imtId );
    }

    /**
     * @description:暂存
     * @author: 890151
     * @createDate: 2016-1-8
     * @return:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveInvMatTransfer(UserInfoScope userInfoScope, String invMatTransferData,
            String invMatTransferDetailData, Map<String, Object> paramMap) throws Exception {
        // 数据转换
        String userId = userInfoScope.getUserId();
        InvMatTransfer invMatTransfer = JsonHelper.toObject( invMatTransferData, InvMatTransfer.class );
        List<InvMatTransferDetail> imtdList = JsonHelper.toList( invMatTransferDetailData, InvMatTransferDetail.class );

        boolean insertFlag;
        // 插入申请
        if ( invMatTransfer.getImtId().isEmpty() ) {
            invMatTransfer.setImtId( null );
            invMatTransfer.setImtCode( null );
            invMatTransfer.setDeleted( "0" );
            invMatTransfer.setCreatedate( new Date() );
            invMatTransfer.setCreateuser( userId );
            invMatTransfer.setSiteid( userInfoScope.getSiteId() );
            invMatTransfer.setDeptid( userInfoScope.getOrgId() );
            invMatTransfer.setStatus( InvMatTransferStatus.DRAFT ); // 设置为草稿状态
            invMatTransferDao.insertInvMatTransfer( invMatTransfer );
            insertFlag = true;
        } else {
            invMatTransfer.setModifydate( new Date() );
            invMatTransfer.setModifyuser( userId );
            invMatTransferDao.updateInvMatTransfer( invMatTransfer );
            insertFlag = false;
        }

        // 获取申请ID
        String imtId = invMatTransfer.getImtId();

        // 删除旧物资
        invMatTransferDetailDao.deleteInvMatTransferDetailByImtId( imtId );

        // 插入物资
        for ( InvMatTransferDetail invMatTransferDetail : imtdList ) {
            invMatTransferDetail.setImtdId( null );
            invMatTransferDetail.setImtId( imtId );
            invMatTransferDetail.setDeleted( "0" );
            invMatTransferDetail.setToWareHouseId( invMatTransfer.getWareHouseToId() );
            invMatTransferDetail.setToWareHouseName( invMatTransfer.getWareHouseToName() );
            invMatTransferDetail.setCreatedate( new Date() );
            invMatTransferDetail.setCreateuser( userId );
            invMatTransferDetail.setSiteid( userInfoScope.getSiteId() );
            invMatTransferDetail.setDeptid( userInfoScope.getOrgId() );
            invMatTransferDetailDao.insertInvMatTransferDetail( invMatTransferDetail );
        }

        // 没有流程实例ID则插入草稿，有则更新待办
        invMatTransfer = invMatTransferDao.queryInvMatTransferById( imtId );// 重新查询获取code
        HomepageWorkTask homeworkTask = new HomepageWorkTask();
        String flowCode = invMatTransfer.getImtCode();
        String jumpPath = "inventory/invmattransfer/invMatTransferInfo.do?imtId=" + imtId;

        if ( invMatTransfer.getInstanceId() == null || invMatTransfer.getInstanceId().isEmpty() ) {
            homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
            homeworkTask.setName( invMatTransfer.getName() ); // 名称
            homeworkTask.setStatusName( InvMatTransferStatus.getEnumName( itcMvcService, "INV_MAT_TRANSFER_STATUS",
                    InvMatTransferStatus.DRAFT ) ); // 流程状态名称
            homeworkTask.setProcessInstId( null ); // 草稿时流程实例ID可以不用设置
            homeworkTask.setType( HomepageWorkTask.TaskType.Draft );
            homeworkTask.setTypeName( "移库申请" ); // 类别
            homeworkTask.setUrl( jumpPath );// 跳转的URL
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿
        } else if ( insertFlag ) { // 有流程实例ID后再暂存调用create会把待办销掉
            homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
            homeworkTask.setName( invMatTransfer.getName() ); // 名称
            homeworkTask.setStatusName( InvMatTransferStatus.getEnumName( itcMvcService, "INV_MAT_TRANSFER_STATUS",
                    InvMatTransferStatus.TRANSFER_APPLY_COMMIT ) ); // 流程状态名称
            homeworkTask.setProcessInstId( invMatTransfer.getInstanceId() );
            homeworkTask.setType( HomepageWorkTask.TaskType.Process );
            homeworkTask.setTypeName( "移库申请" ); // 类别
            homeworkTask.setUrl( jumpPath );// 跳转的URL
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿
        }

        // 设置返回值
        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "imtId", imtId );
        resultHashMap.put( "instanceId", invMatTransfer.getInstanceId() );
        resultHashMap.put( "taskId", invMatTransfer.getTaskId() );
        return resultHashMap;
    }

    /**
     * @description:提交
     * @author: 890151
     * @createDate: 2016-1-8
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> commitInvMatTransfer(UserInfoScope userInfoScope, String invMatTransferData,
            String invMatTransferDetailData, Map<String, Object> paramMap) throws Exception {
        // 数据转换
        String userId = userInfoScope.getUserId();
        String siteId = userInfoScope.getSiteId();
        InvMatTransfer invMatTransfer = JsonHelper.toObject( invMatTransferData, InvMatTransfer.class );
        List<InvMatTransferDetail> imtdList = JsonHelper.toList( invMatTransferDetailData, InvMatTransferDetail.class );

        // 插入申请
        if ( invMatTransfer.getImtId().isEmpty() ) {
            invMatTransfer.setImtId( null );
            invMatTransfer.setImtCode( null );
            invMatTransfer.setDeleted( "0" );
            invMatTransfer.setCreatedate( new Date() );
            invMatTransfer.setCreateuser( userId );
            invMatTransfer.setSiteid( userInfoScope.getSiteId() );
            invMatTransfer.setDeptid( userInfoScope.getOrgId() );
            invMatTransfer.setStatus( InvMatTransferStatus.TRANSFER_APPLY_COMMIT ); // 设置为提交状态
            invMatTransferDao.insertInvMatTransfer( invMatTransfer );
        } else {
            invMatTransfer.setStatus( InvMatTransferStatus.TRANSFER_APPLY_COMMIT ); // 设置为提交状态
            invMatTransfer.setModifydate( new Date() );
            invMatTransfer.setModifyuser( userId );
            invMatTransferDao.updateInvMatTransfer( invMatTransfer );
        }

        // 获取申请ID
        String imtId = invMatTransfer.getImtId();

        // 删除旧物资
        invMatTransferDetailDao.deleteInvMatTransferDetailByImtId( imtId );

        // 插入物资
        for ( InvMatTransferDetail invMatTransferDetail : imtdList ) {
            invMatTransferDetail.setImtdId( null );
            invMatTransferDetail.setImtId( imtId );
            invMatTransferDetail.setDeleted( "0" );
            invMatTransferDetail.setToWareHouseId( invMatTransfer.getWareHouseToId() );
            invMatTransferDetail.setToWareHouseName( invMatTransfer.getWareHouseToName() );
            invMatTransferDetail.setCreatedate( new Date() );
            invMatTransferDetail.setCreateuser( userId );
            invMatTransferDetail.setSiteid( userInfoScope.getSiteId() );
            invMatTransferDetail.setDeptid( userInfoScope.getOrgId() );
            invMatTransferDetailDao.insertInvMatTransferDetail( invMatTransferDetail );
        }

        // 获取最新流程定义版本
        String invMatTransferKey = "inventory_[@@@]_invmattransfer".replace( "[@@@]", siteId.toLowerCase() );
        String defkey = workflowService.queryForLatestProcessDefKey( invMatTransferKey );

        // 如果没有流程实例ID则启动新流程，如果有则获取任务ID
        String instanceId = null;
        String taskId = null;
        invMatTransfer = invMatTransferDao.queryInvMatTransferById( imtId );// 重新查询获取code
        String flowCode = invMatTransfer.getImtCode();
        String jumpPath = "inventory/invmattransfer/invMatTransferInfo.do?imtId=" + imtId;
        if ( invMatTransfer.getInstanceId() == null || invMatTransfer.getInstanceId().isEmpty() ) {
            // 启动流程
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "businessId", imtId );
            logger.info( "-------------CORE 启动流程开始，移库申请ID：" + imtId + "---------------" );
            ProcessInstance processInstance = workflowService.startLatestProcessInstanceByDefKey( defkey, userId, map );
            // 获取流程实例ID
            instanceId = processInstance.getProcessInstanceId();
            logger.info( "-------------CORE 启动流程结束，申移库请ID：" + imtId + "  流程实例ID：" + instanceId + "---------------" );
            // 获取当前活动节点 刚启动流程，第一个活动节点肯定是属于当前登录人的
            List<Task> activities = workflowService.getActiveTasks( instanceId );
            Task task = activities.get( 0 );
            taskId = task.getId();

            // 添加“待办”
            HomepageWorkTask homeworkTask = new HomepageWorkTask();
            homeworkTask.setFlow( flowCode );// 编号，如采购申请 WO20140902001
            homeworkTask.setName( invMatTransfer.getName() ); // 名称
            homeworkTask.setProcessInstId( instanceId ); // 草稿时流程实例ID可以不用设置
            homeworkTask.setStatusName( InvMatTransferStatus.getEnumName( itcMvcService, "INV_MAT_TRANSFER_STATUS",
                    InvMatTransferStatus.TRANSFER_APPLY_COMMIT ) ); // 流程状态名称
            homeworkTask.setType( HomepageWorkTask.TaskType.Process );
            homeworkTask.setTypeName( "移库申请" ); // 类别
            homeworkTask.setUrl( jumpPath );// 扭转的URL
            homepageService.create( homeworkTask, userInfoScope ); // 调用接口创建草稿

            // 更新申请流程、状态
            invMatTransfer.setInstanceId( instanceId );
            invMatTransfer.setModifydate( new Date() );
            invMatTransfer.setModifyuser( userId );
            invMatTransferDao.updateInvMatTransfer( invMatTransfer );
        } else {
            instanceId = invMatTransfer.getInstanceId();
            List<Task> activities = workflowService.getActiveTasks( instanceId );
            if ( activities.size() != 0 ) {
                // 刚启动流程，第一个活动节点肯定是属于当前登录人的
                Task task = activities.get( 0 );
                if ( task != null ) {
                    taskId = task.getId();
                }
            }
        }

        // 设置返回值
        Map<String, Object> resultHashMap = new HashMap<String, Object>();
        resultHashMap.put( "imtId", imtId );
        resultHashMap.put( "instanceId", instanceId );
        resultHashMap.put( "taskId", taskId );
        return resultHashMap;
    }

    /**
     * @description:删除
     * @author: 890151
     * @createDate: 2016-1-9
     * @return
     * @throws Exception:
     */
    @Override
    public int deleteInvMatTransfer(UserInfoScope userInfo, String imtId) {
        InvMatTransfer invMatTransfer = invMatTransferDao.queryInvMatTransferById( imtId );
        // 删除申请和物资
        invMatTransferDao.deleteInvMatTransfer( imtId );
        invMatTransferDetailDao.deleteInvMatTransferDetailByImtId( imtId );
        // 删除草稿
        homepageService.Delete( invMatTransfer.getImtCode(), userInfo );
        return 1;
    }

    /**
     * @description:作废
     * @author: 890151
     * @createDate: 2016-1-9
     * @return
     * @throws Exception:
     */
    @Override
    public int obsoleteInvMatTransfer(UserInfoScope userInfoScope, String imtId) {
        String userId = userInfoScope.getUserId();

        // 更新申请状态
        InvMatTransfer invMatTransfer = new InvMatTransfer();
        invMatTransfer.setImtId( imtId );
        invMatTransfer.setStatus( InvMatTransferStatus.OBSOLETE );
        invMatTransfer.setModifydate( new Date() );
        invMatTransfer.setModifyuser( userId );
        invMatTransferDao.updateInvMatTransfer( invMatTransfer );

        // 重新查询申请获取流程信息
        invMatTransfer = invMatTransferDao.queryInvMatTransferById( imtId );
        String instanceId = invMatTransfer.getInstanceId();
        // 获取当前活动节点
        List<Task> activities = workflowService.getActiveTasks( invMatTransfer.getInstanceId() );
        // 刚启动流程，第一个活动节点肯定是属于当前登录人的
        Task task = activities.get( 0 );
        String taskId = task.getId();

        String statusName = InvMatTransferStatus.getEnumName( itcMvcService, "INV_MAT_TRANSFER_STATUS",
                InvMatTransferStatus.OBSOLETE );
        logger.info( "-------------作废移库申请处理开始,ID：" + imtId );
        // 终止流程
        workflowService.stopProcess( taskId, userId, userId, statusName );
        // 删掉对应的待办
        homepageService.complete( instanceId, userInfoScope, statusName );
        return 1;
    }

    /**
     * @description:通过物资查询页面列表带过来的参数查询物资信息
     * @author: 890151
     * @createDate: 2016-1-9
     * @param userInfo
     * @param paramMap（包含移出仓库wareHouseId，多个物资itemIds，多个物资类型cateTypeIds）
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvItemVO> queryItemInfoToMatTransfer(UserInfoScope userInfo, Map<String, Object> paramMap)
            throws Exception {
        List<InvItemVO> itemList = new ArrayList<InvItemVO>();
        if ( paramMap.containsKey( "itemIds" ) ) {
            String wareHouseId = paramMap.get( "wareHouseId" ).toString();
            String itemIds = paramMap.get( "itemIds" ).toString();
            String cateTypeIds = paramMap.get( "cateTypeIds" ).toString();

            if ( !"".equals( itemIds ) ) {
                String[] itemIdsArray = itemIds.split( "," );
                String[] cateTypeIdsArray = cateTypeIds.split( "," );
                InvItemVO itemVO = null;
                for ( int i = 0; i < itemIdsArray.length; i++ ) {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put( "siteId", userInfo.getSiteId() );
                    map.put( "wareHouseId", wareHouseId );
                    map.put( "itemId", itemIdsArray[i] );
                    map.put( "cateTypeId", cateTypeIdsArray[i] );
                    itemVO = invMatTransferDetailDao.queryItemInfoToMatTransfer( map );
                    itemList.add( itemVO );
                }
            }
        }
        return itemList;
    }
}
