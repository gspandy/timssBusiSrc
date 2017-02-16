package com.timss.itsm.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmJPItems;
import com.timss.itsm.bean.ItsmJobPlan;
import com.timss.itsm.bean.ItsmMaintainPlan;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmJPItemsDao;
import com.timss.itsm.dao.ItsmJobPlanDao;
import com.timss.itsm.service.ItsmJobPlanService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmJobPlanServiceImpl implements ItsmJobPlanService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ItsmJobPlanDao jobPlanDao;
    @Autowired
    private ItsmJPItemsDao jpItemsDao;
    private static final Logger LOG = Logger.getLogger( ItsmJobPlanServiceImpl.class );

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int insertJobPlan(Map<String, String> jpDataMap) throws JsonParseException, JsonMappingException {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
     // (standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
        String jpSource = jpDataMap.get( "jpSource" ); 
        /** 插入作业方案列表 */
        String jpFormData = jpDataMap.get( "jpFormData" );
        String mtpFormData = jpDataMap.get( "mtpFormData" );
        String woFormData = jpDataMap.get( "woFormData" );
        String commitStyle = jpDataMap.get( "commitStyle" );

        String toolDataStr = jpDataMap.get( "toolDataStr" );
        JSONObject itemsJsonObj = JSONObject.fromObject( toolDataStr );
        int itemsDatagridNum = Integer.valueOf( itemsJsonObj.get( "total" ).toString() ); // 记录数

        ItsmJobPlan jobPlan = null;
        if ( jpFormData != null ) { // 从作业方案模块页面过来
            jobPlan = JsonHelper.toObject( jpFormData, ItsmJobPlan.class );
        } else if ( mtpFormData != null ) { // 若是从维护计划模块页面过来
            ItsmMaintainPlan maintainPlan = JsonHelper.toObject( mtpFormData, ItsmMaintainPlan.class );
            jobPlan = new ItsmJobPlan();
            jobPlan.setDescription( maintainPlan.getDescription() );
            jobPlan.setSpecialtyId( maintainPlan.getSpecialtyId() );
            jobPlan.setRemarks( maintainPlan.getRemarks() );
        } else if ( woFormData != null ) {
            // 从工单模块页面过来
            ItsmWorkOrder workOrder = JsonHelper.toObject( woFormData, ItsmWorkOrder.class );

            Integer jpId = workOrder.getJobPlanId();
            String woCurrStatus = workOrder.getCurrStatus();

            if ( jpId != null ) {
                jobPlan = jobPlanDao.queryJPById( jpId );
            }

            if ( jobPlan != null ) { // 是否有暂存的作业方案
                if ( "endWorkReport".equals( woCurrStatus ) && "actual".equals( jobPlan.getJobPlanType() ) ) {
                    jobPlan.setId( jpId );
                }
                if ( "workPlan".equals( woCurrStatus ) && "plan".equals( jobPlan.getJobPlanType() ) ) {
                    jobPlan.setId( jpId );
                } else {
                    jobPlan = new ItsmJobPlan();
                    if ( "endWorkReport".equals( woCurrStatus ) ) { // 完工汇报，没有暂存时
                        jobPlan.setJobPlanType( "actual" );
                    }
                }
            } else {
                jobPlan = new ItsmJobPlan();
            }

            jobPlan.setWorkOrderId( workOrder.getId() );
            jobPlan.setDescription( workOrder.getDescription() );
            jobPlan.setSpecialtyId( workOrder.getFaultTypeId() );
            jobPlan.setRemarks( workOrder.getRemarks() );
        }

        Integer jpId = jobPlan.getId();
        if ( jpId == null || jpId == 0 ) {
            jpId = jobPlanDao.getNextJPId(); // 获取要插入作业方案的ID
            jobPlan.setId( jpId );
            jobPlan.setCreateuser( userId );
            jobPlan.setCreatedate( new Date() );
            jobPlan.setModifydate( new Date() );
            jobPlan.setModifyuser( userId );
            jobPlan.setJobPlanType( jpSource );// (standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
            jobPlan.setSiteid( siteId );
            jobPlan.setDeptid( deptId );
            jobPlan.setYxbz( 1 );
            jobPlanDao.insertJobPlan( jobPlan );
        } else {
            jobPlan.setModifydate( new Date() );
            jobPlan.setModifyuser( userId );
            jobPlanDao.updateJobPlan( jobPlan );
        }

        // 插入工具表 (#{id},#{jobPlanId},#{itemsCode},#{count},#{siteId}
        JSONArray itemsJsonArray = itemsJsonObj.getJSONArray( "rows" ); // 记录数组
        // 直接调用库存接口，进行领料申请
        if ( "commit".equals( commitStyle ) ) {
            
        } else if ( "save".equals( commitStyle ) ) {
            for ( int i = 0; i < itemsDatagridNum; i++ ) {
                String itemsRecord = itemsJsonArray.get( i ).toString(); // 某条记录的字符串表示
                ItsmJPItems jpItems = JsonHelper.fromJsonStringToBean( itemsRecord, ItsmJPItems.class );
                String jpItemsCode = jpItems.getItemsCode();
                if ( "".equals( jpItemsCode ) ) { // 如果未空行，则直接跳出
                    continue;
                }
                int jpItemsId = jpItems.getId();
                jpItemsId = jpItemsDao.getNextJPItemsId(); // 获取要插入维护计划的ID
                jpItems.setId( jpItemsId );
                jpItems.setJobPlanId( jpId );
                jpItems.setSiteid( siteId );
                jpItems.setDeptid( deptId );
                jpItemsDao.insertJPItems( jpItems );
            }
        }

        return jpId;
    }

    @Override
    public Page<ItsmJobPlan> queryStandardJP(Page<ItsmJobPlan> page){

        List<ItsmJobPlan> ret = jobPlanDao.queryITStandardJP( page );
        page.setResults( ret );
        LOG.info( "查询作业方案列表信息" );

        return page;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int updateJobPlan(Map<String, String> jpDataMap) throws JsonParseException, JsonMappingException {

        String jpFormData = jpDataMap.get( "jpFormData" );
        String mtpFormData = jpDataMap.get( "mtpFormData" );
        String woFormData = jpDataMap.get( "woFormData" );
        Integer jpId = 0;
        if ( jpFormData != null ) { // 修改标准作业方案
            ItsmJobPlan jobPlan = JsonHelper.toObject( jpFormData, ItsmJobPlan.class );
            jpId = jobPlan.getId(); // 获取要修改的作业方案的ID
            jpDataMap.put( "jpSource", "standard" );// (standard：标准；plan：策划；actual：实际;maintainPlan:维护计划)
        } else if ( mtpFormData != null ) { // 修改维护计划的作业方案
            ItsmMaintainPlan maintainPlan = JsonHelper.toObject( mtpFormData, ItsmMaintainPlan.class );
            jpId = maintainPlan.getJobPlanId();
            jpDataMap.put( "jpSource", "maintainPlan" );
        } else if ( woFormData != null ) { // 修改工单的作业方案
            ItsmWorkOrder workOrder = JsonHelper.toObject( woFormData, ItsmWorkOrder.class );
            if ( workOrder.getJobPlanId() != null ) {
                jpId = workOrder.getJobPlanId();
            }

            // TODO 此处还需要分情况确定是plan（策划）或者actual（实际）
            jpDataMap.put( "jpSource", "unknown" );
        }

        // 删除所有之前的记录信息
        if ( jpId != null ) {
            jpItemsDao.deleteJPItemsByJPId( jpId );
        }

        // 插入新的记录信息，但是ID，jpID等信息要保持不变
        jpId = insertJobPlan( jpDataMap );
        return jpId;
    }

    @Override
    public HashMap<String, Object> queryJPById(Integer jpId) {
        if ( jpId == null ) {
            jpId = 0;
        }
        ItsmJobPlan jobPlan = jobPlanDao.queryITJPById( jpId ); // 查作业方法form表单内容
        String jobPlanStr = JsonHelper.toJsonString( jobPlan );

        List<ItsmJPItems> jpItemsList = jpItemsDao.queryJPItemsByJPId( jpId ); // 查工具信息

        HashMap<String, Object> jpFullData = new HashMap<String, Object>(); // 返回数据类型
        jpFullData.put( "jobPlanForm", jobPlanStr );
        jpFullData.put( "toolData", jpItemsList );

        return jpFullData;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteWOReportByWOId(String woId) {
        ItsmJobPlan jobPlan = jobPlanDao.queryReportJPByWOId( woId );
        if ( jobPlan != null ) {
            int jpId = jobPlan.getId();
            deleteJobPlanById( jpId );
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteJobPlanById(int jpId) {
        jpItemsDao.deleteJPItemsByJPId( jpId ); // 删除工具
        jobPlanDao.deleteJPById( jpId ); // 删除作业方案主表中信息
    }

    @Override
    public ItsmJobPlan queryPlanJPByWOId(String woId) {

        return jobPlanDao.queryPlanJPByWOId( woId );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateJPToUnvailable(int jobPlanId) {
        jobPlanDao.updateJPToUnvailable( jobPlanId );
    }

}
