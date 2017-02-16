package com.timss.itsm.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWoPriority;
import com.timss.itsm.bean.ItsmWorkOrder;
import com.timss.itsm.dao.ItsmWoFaultTypeDao;
import com.timss.itsm.dao.ItsmWoPriorityDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmWoStatisService;
import com.timss.itsm.service.ItsmWoStatisticVOService;
import com.timss.itsm.service.ItsmWorkTimeService;
import com.timss.itsm.util.ItsmCommonUtil;
import com.timss.itsm.util.ItsmConstant;
import com.timss.itsm.vo.ItsmWoStatisticVO;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmWoStatisServiceImpl implements ItsmWoStatisService {
    @Autowired
    ItsmWoFaultTypeDao woFaultTypeDao;
    @Autowired
    ItsmWoPriorityDao woPriorityDao;
    @Autowired
    ItsmWorkOrderDao workOrderDao;
    @Autowired
    ItsmWoStatisticVOService itWoStatisticVOService;
    @Autowired
    private IAuthorizationManager authManager;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    ItsmWorkTimeService workTimeService;
    @Autowired
    ISecurityMaintenanceManager iSecManager;

    private static final Logger LOG = Logger.getLogger( ItsmWoStatisServiceImpl.class );
    private static final int ONEHUNDRED = 100;

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> queryItWoStatisticVO(Date beginTime, Date endTime, String siteid, String statisticType)
            throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        List<ItsmWoStatisticVO> insertObjList = new ArrayList<ItsmWoStatisticVO>(); // 插入到统计表中的对象

        // 查询服务目录的一级类型(添加一个“小计”)
        List<ItsmWoFaultType> woFTList = woFaultTypeDao.queryOneLevelFTBySiteId( "ITC" );
        ItsmWoFaultType sumWoFaultType = new ItsmWoFaultType(); // 添加一个“小计”
        sumWoFaultType.setSiteid( siteid );
        sumWoFaultType.setFaultTypeCode( "MIN_SUM" );
        sumWoFaultType.setName( "小计" );
        woFTList.add( sumWoFaultType );

        // 获取要统计的用户组， 此处需要模糊查询，以"xxx"开头的用户组
        Page<SecureUserGroup> page = new Page<SecureUserGroup>();
        page = new Page<SecureUserGroup>();
        int pageSize = 100;
        page.setPageSize( pageSize );
        Map<String, Object> searchBy = new HashMap<String, Object>();
        searchBy.put( "searchBy", siteid.toLowerCase() + "_itsm_wt" );// 以xxx开头的用户组标识(此处需要将用户组中的ID变成小写)
        page.setParams( searchBy );
        SecureUser operator = userInfoScope.getSecureUser();
        Page<SecureUserGroup> qResult = iSecManager.retrieveGroups( page, operator );
        List<SecureUserGroup> userGroupList = qResult.getResults();

        // 获取要查询的所有Bean,这里只是初始化bean，并没有给它赋值
        insertObjList = getInsertObjBeanList( siteid, statisticType, woFTList, result, userGroupList );
        List<Integer> mergeNumList = new ArrayList<Integer>(); // 用户个人、团队运维KPI统计，合并行
        // 客服列表
        List<SecureUser> kfUserList = authManager.retriveUsersWithSpecificRole( siteid + "_ITSM_KF", null, false, true );
        ItsmWoFaultType ftRoot = woFaultTypeDao.queryFTRootBySiteId( "ITC" );
        Map<Integer, String> serCharactMap = getSerCharactMap( siteid ); // 用于判断工单是“请求”还是“事件”

        // 设置分段查询统计的参数
        int recordSum = workOrderDao.queryNewWoSize( beginTime, endTime, siteid );
        int selectSize = ItsmConstant.selectSize; // 每次查询出selectSize条记录
        int selectNum = (recordSum + selectSize - 1) / selectSize; // 需要查询多少次

        // 草稿、作废、任务型、周期性工单不参与统计，需要排除
        for ( int n = 0; n < selectNum; n++ ) {
            List<ItsmWorkOrder> newWorkOrderList = workOrderDao.queryNewWoList( beginTime, endTime, siteid, n,
                    selectSize );// 参加统计的工单
            // 计算统计数据，并赋值给bean
            if ( "requstStatistic".equals( statisticType ) || "eventStatistic".equals( statisticType ) ) { // 请求、事件管理KPI统计
                for ( int i = 0; i < newWorkOrderList.size(); i++ ) {
                    manageStatistic( insertObjList, newWorkOrderList.get( i ), statisticType, kfUserList, ftRoot,
                            serCharactMap );
                }
            } else if ( "personStatistic".equals( statisticType ) || "teamStatistic".equals( statisticType ) ) { // 跟人、
                                                                                                                 // 团队运维KPI统计
                // 过滤掉工单在“工程师处理”环节前的工单
                List<ItsmWorkOrder> newWorkOrderFilterList = new ArrayList<ItsmWorkOrder>();
                for ( int i = 0; i < newWorkOrderList.size(); i++ ) {
                    String woStatus = newWorkOrderList.get( i ).getCurrStatus();
                    boolean flag = "sendWO".equals( woStatus ) || "newWO".equals( woStatus ); // 新建、派单状态的工单不参与统计
                    if ( !flag ) {
                        newWorkOrderFilterList.add( newWorkOrderList.get( i ) );
                    }
                }

                for ( int i = 0; i < newWorkOrderFilterList.size(); i++ ) {
                    itTeamPersonStatistic( insertObjList, newWorkOrderFilterList.get( i ), statisticType, ftRoot,
                            userGroupList );
                }
                // 过滤掉所有值都为0的项
                insertObjList = filterZeroBean( insertObjList, mergeNumList );
            }
        }

        // 计算比率，向临时表中插入数据
        int printNum = itWoStatisticVOService.getNextPrintNum();
        for ( int i = 0; i < insertObjList.size(); i++ ) {
            ItsmWoStatisticVO tempInsertObj = insertObjList.get( i );
            calculateRatio( tempInsertObj, statisticType );
            tempInsertObj.setStatisticNum( printNum );
        }

        itWoStatisticVOService.insertBatchItWoStatisticVO( insertObjList );

        result.put( "printNum", printNum );
        result.put( "data", insertObjList );
        result.put( "mergeNumList", mergeNumList );
        result.put( "oneLevFTSum", woFTList.size() ); // 一级服务目录的数量，包括“小计”

        return result;

    }

    /**
     * @description:过滤掉insertObjList中值为0的bean,并计算出在前台合并时，每组合并的行数
     * @author: 王中华
     * @createDate: 2014-12-25
     * @param insertObjList
     * @param mergNumList:
     */
    private List<ItsmWoStatisticVO> filterZeroBean(List<ItsmWoStatisticVO> insertObjList, List<Integer> mergNumList) {
        List<ItsmWoStatisticVO> tempObjList = new ArrayList<ItsmWoStatisticVO>();
        for ( int i = 0; i < insertObjList.size(); i++ ) {
            ItsmWoStatisticVO bean = insertObjList.get( i );
            int sum = bean.getSum();
            String statisticObj = bean.getStatisticObj();
            boolean flag = "MAX_SUM".equals( statisticObj ) || "MIN_SUM".equals( statisticObj ) || (sum != 0);
            if ( flag ) {
                tempObjList.add( bean );
            }
        }

        int index = -1;
        for ( int i = 0; i < tempObjList.size(); i++ ) {
            String statisticObj = tempObjList.get( i ).getStatisticObj();
            if ( "MIN_SUM".equals( statisticObj ) ) {
                mergNumList.add( i - index );
                index = i;
            }
        }

        return tempObjList;
    }

    /**
     * @description:根据不同的查询类型，查询统计列表的第一列
     * @author: 王中华
     * @createDate: 2014-12-23
     * @param siteid 站点
     * @param statisticType 统计类型
     * @param woFTList 服务目录
     * @param result 这个参数是上层函数返回的结果，但是需要在这里面给其中一张参数赋值
     * @param UserGroupList 需要统计的维护组列表（不包括服务台）
     * @return:
     */
    private List<ItsmWoStatisticVO> getInsertObjBeanList(String siteid, String statisticType,
            List<ItsmWoFaultType> woFTList, Map<String, Object> result, List<SecureUserGroup> userGroupList) {

        List<ItsmWoStatisticVO> insertObjList = new ArrayList<ItsmWoStatisticVO>();
        
        if ( "requstStatistic".equals( statisticType ) || "eventStatistic".equals( statisticType ) ) {
            List<ItsmWoPriority> woPriorityList = woPriorityDao.queryWoPriorityListBySiteId( siteid );
            result.put( "serLevelSum", woPriorityList.size() ); // 服务级别的数量
            // 构建插入的bean
            for ( int i = 0; i < woPriorityList.size(); i++ ) {
                for ( int j = 0; j < woFTList.size(); j++ ) {
                    ItsmWoStatisticVO temp = new ItsmWoStatisticVO();
                    temp.setStatisticType( statisticType );
                    temp.setSerLevel( woPriorityList.get( i ).getName() );
                    temp.setEventType( woFTList.get( j ).getName() );
                    if ( "MIN_SUM".equals( woFTList.get( j ).getFaultTypeCode() ) ) {
                        temp.setStatisticObj( "MIN_SUM" ); // 小计
                    }
                    insertObjList.add( temp );
                }
            }
        } else if ( "personStatistic".equals( statisticType ) ) {

            // 维护主管组(要从统计中排除)
            List<SecureUser> temp2 = authManager.retriveUsersWithSpecificGroup( siteid + "_ITSM_WHZG", null, false,
                    true );
            String removeUserListStr = "";
            for ( int i = 0; i < temp2.size(); i++ ) {
                removeUserListStr += temp2.get( i ).getId() + ",";
            }
            int usergroupSize = userGroupList.size();
            List<SecureUser> engineerUserList = new ArrayList<SecureUser>();
            for ( int i = 0; i < usergroupSize; i++ ) {
                String groupId = userGroupList.get( i ).getId();
                List<SecureUser> temp1 = authManager.retriveUsersWithSpecificGroup( groupId, null, false, true );
                for ( int j = 0; j < temp1.size(); j++ ) {
                    SecureUser addSecureUser = temp1.get( j );
                    if ( removeUserListStr.indexOf( addSecureUser.getId() ) == -1 ) { // 如果不是维护主管组里面的人
                        engineerUserList.add( addSecureUser );
                    }
                }
            }

            for ( int i = 0, j = engineerUserList.size() - 1; i < j; i++, j-- ) { // 删掉重复的记录
                SecureUser secureUserInList1 = engineerUserList.get( i );
                SecureUser secureUserInList2 = engineerUserList.get( j );
                if ( secureUserInList1.getId().equals( secureUserInList2.getId() ) ) {
                    engineerUserList.remove( j );
                    j = engineerUserList.size();
                }
            }

            result.put( "serLevelSum", engineerUserList.size() ); // 工程师的数量

            // 构建插入的bean
            for ( int i = 0; i < engineerUserList.size(); i++ ) {
                for ( int j = 0; j < woFTList.size(); j++ ) {
                    ItsmWoStatisticVO temp = new ItsmWoStatisticVO();
                    temp.setStatisticType( statisticType );
                    temp.setSerLevel( engineerUserList.get( i ).getName() );
                    temp.setEventType( woFTList.get( j ).getName() );
                    if ( "MIN_SUM".equals( woFTList.get( j ).getFaultTypeCode() ) ) {
                        temp.setStatisticObj( "MIN_SUM" ); // 小计
                    }
                    insertObjList.add( temp );
                }
            }
        } else if ( "teamStatistic".equals( statisticType ) ) {
            result.put( "serLevelSum", userGroupList.size() ); // 工程师的数量
            // 构建插入的bean
            for ( int i = 0; i < userGroupList.size(); i++ ) {
                for ( int j = 0; j < woFTList.size(); j++ ) {
                    ItsmWoStatisticVO temp = new ItsmWoStatisticVO();
                    temp.setStatisticType( statisticType );
                    temp.setSerLevel( userGroupList.get( i ).getName() );
                    temp.setEventType( woFTList.get( j ).getName() );
                    if ( "MIN_SUM".equals( woFTList.get( j ).getFaultTypeCode() ) ) {
                        temp.setStatisticObj( "MIN_SUM" ); // 小计
                    }
                    insertObjList.add( temp );
                }
            }
        }

        ItsmWoStatisticVO allSumBean = new ItsmWoStatisticVO(); // 所有工单的统计
        allSumBean.setSerLevel( "合计" );
        allSumBean.setStatisticObj( "MAX_SUM" );
        insertObjList.add( allSumBean );
        return insertObjList;
    }

    /**
     * @description:获取对应站点下的服务性质id与服务性质code的对应关系
     * @author: 王中华
     * @createDate: 2014-12-1
     * @param siteid
     * @return:
     */
    private Map<Integer, String> getSerCharactMap(String siteid) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<ItsmWoFaultType> page = userInfoScope.getPage();
        page.setPageSize( 10000 );
        page.setParameter( "siteId", "ITC" );
        List<ItsmWoFaultType> faultTypeList = woFaultTypeDao.queryWoFaultTypeList( page );
        for ( int i = 0; i < faultTypeList.size(); i++ ) {
            ItsmWoFaultType tempObj = faultTypeList.get( i );
            String serCharacterCode = tempObj.getFaultTypeCode();
            if ( serCharacterCode.startsWith( "SC" ) ) { // 如果是以“SC”开头
                result.put( tempObj.getId(), serCharacterCode );
            }
        }
        return result;
    }

    /**
     * @description:计算百分比
     * @author: 王中华
     * @createDate: 2014-11-26
     * @param tempInsertObj:
     */
    private void calculateRatio(ItsmWoStatisticVO tempInsertObj, String statisticType) {
        int sum = tempInsertObj.getSum();
        if ( sum != 0 ) {
            double solveRatio = 0, overTimeSolveRatio = 0, overTimeRespondRatio = 0, csOnTimeSolveRatio = 0, teamOnTimeSolveRatio = 0;
            // 成功解决率
            solveRatio = (double) tempInsertObj.getSolveSum() / sum * ONEHUNDRED;
            solveRatio = (double) (Math.round( solveRatio * ONEHUNDRED ) / 100.0);
            if ( "requstStatistic".equals( statisticType ) || "eventStatistic".equals( statisticType ) ) {
                // 超时解决率
                overTimeSolveRatio = (double) tempInsertObj.getOverTimeSolveSum() / sum * ONEHUNDRED;
                overTimeSolveRatio = (double) (Math.round( overTimeSolveRatio * ONEHUNDRED ) / 100.0);
                // 超时响应率
                overTimeRespondRatio = (double) tempInsertObj.getOverTimeRespondSum() / sum * ONEHUNDRED;
                overTimeRespondRatio = (double) (Math.round( overTimeRespondRatio * ONEHUNDRED ) / 100.0);
                // 服务台及时解决率
                csOnTimeSolveRatio = (double) tempInsertObj.getCsOnTimeSolveSum() / sum * ONEHUNDRED;
                csOnTimeSolveRatio = (double) (Math.round( csOnTimeSolveRatio * ONEHUNDRED ) / 100.0);
                // 二线团队及时解决率
                teamOnTimeSolveRatio = (double) tempInsertObj.getTeamOnTimeSolveSum() / sum * ONEHUNDRED;
                teamOnTimeSolveRatio = (double) (Math.round( teamOnTimeSolveRatio * ONEHUNDRED ) / 100.0);
            } else if ( "personStatistic".equals( statisticType ) || "teamStatistic".equals( statisticType ) ) {
                // 及时解决率
                overTimeSolveRatio = (double) (tempInsertObj.getSolveSum() - tempInsertObj.getOverTimeSolveSum()) / sum
                        * 100;
                overTimeSolveRatio = (double) (Math.round( overTimeSolveRatio * ONEHUNDRED ) / 100.0);
                // 及时响应率
                overTimeRespondRatio = (double) (tempInsertObj.getSolveSum() - tempInsertObj.getOverTimeRespondSum())
                        / sum * 100;
                overTimeRespondRatio = (double) (Math.round( overTimeRespondRatio * ONEHUNDRED ) / 100.0);
            }

            double avgSolveTime = 0;
            if ( tempInsertObj.getSolveSum() != 0 ) {
                avgSolveTime = tempInsertObj.getSolveTimeSum() / (tempInsertObj.getSolveSum() * 60 * 60);
            }
            avgSolveTime = (double) (Math.round( avgSolveTime * ONEHUNDRED ) / 100.0);

            tempInsertObj.setSolveRatio( solveRatio );
            tempInsertObj.setOverTimeSolveRatio( overTimeSolveRatio );
            tempInsertObj.setOverTimeRespondRatio( overTimeRespondRatio );
            tempInsertObj.setCsOnTimeSolveRatio( csOnTimeSolveRatio );
            tempInsertObj.setTeamOnTimeSolveRatio( teamOnTimeSolveRatio );
            tempInsertObj.setAvgSolveTime( avgSolveTime );
        } else {
            tempInsertObj.setSolveRatio( ONEHUNDRED );
            if ( "requstStatistic".equals( statisticType ) || "eventStatistic".equals( statisticType ) ) {
                tempInsertObj.setOverTimeSolveRatio( 0 );
                tempInsertObj.setOverTimeRespondRatio( 0 );
            } else if ( "personStatistic".equals( statisticType ) || "teamStatistic".equals( statisticType ) ) {
                // 在这两种统计里面OverTimeSolveRatio，OverTimeRespondRatio的值分别代表及时解决率和及时响应率
                tempInsertObj.setOverTimeSolveRatio( ONEHUNDRED );
                tempInsertObj.setOverTimeRespondRatio( ONEHUNDRED );
            }
            tempInsertObj.setCsOnTimeSolveRatio( ONEHUNDRED );
            tempInsertObj.setTeamOnTimeSolveRatio( ONEHUNDRED );
            tempInsertObj.setAvgSolveTime( 0 );
        }
    }

    /**
     * @description: 请求管理KPI、事件管理KPI统计
     * @author: 王中华
     * @createDate: 2014-12-1
     * @param voList
     * @param workOrder 统计的工单
     * @param statisticType 统计类型
     * @param kfUserList 客服列表
     * @param ftRoot 服务目录的根
     * @param serCharactMap: 服务性质的id与code的对应关系map
     * @throws Exception
     */
    private void manageStatistic(List<ItsmWoStatisticVO> voList, ItsmWorkOrder workOrder, String statisticType,
            List<SecureUser> kfUserList, ItsmWoFaultType ftRoot, Map<Integer, String> serCharactMap) throws Exception {
        LOG.info( "-----------------------开始统计,统计类型为：" + statisticType + "-----------------------" );
        String preCode = "";// SCR:代表请求，单指服务性质为“咨询”或“服务请求";SCE：代表事件,单指服务性质为“故障”或“其它”
        if ( "requstStatistic".equals( statisticType ) ) {
            preCode = "SCCONSULT,SCREQUEST,SCOTHER";
        } else if ( "eventStatistic".equals( statisticType ) ) {
            preCode = "SCFAULT";
        }

        // 计算工单的服务级别和事件分类
        Map<String, Object> serLevelAndEventTypeObj = getSerLevelAndEventType( workOrder, ftRoot.getId() );
        ItsmWoPriority woPriority = (ItsmWoPriority) serLevelAndEventTypeObj.get( "serLevel" );
        ItsmWoFaultType woFaultType = (ItsmWoFaultType) serLevelAndEventTypeObj.get( "evenType" );

        // 根据计算的服务级别和事件分类，取出List中对应的Bean
        Map<String, String> serLevelAndEventTypeStr = new HashMap<String, String>();
        serLevelAndEventTypeStr.put( "serLevelName", woPriority.getName() );
        serLevelAndEventTypeStr.put( "evenTypeName", woFaultType.getName() );
        ItsmWoStatisticVO tempBean = getVOBean( voList, serLevelAndEventTypeStr );

        Integer serCharacterId = Integer.valueOf( workOrder.getSerCharacterId() );
        String serCharacterCode = serCharactMap.get( serCharacterId );
        if ( preCode.contains( serCharacterCode ) ) { // 根据查询类型，赛选对应的工单进行统计

            ItsmWoStatisticVO oneLevelSumBean = new ItsmWoStatisticVO(); // 一级服务的“小计”
            ItsmWoStatisticVO allLevelSumBean = new ItsmWoStatisticVO(); // 所有服务级别的“合计”
            // 一级服务级别的“小计” 与总的 “合计”
            for ( int i = 0; i < voList.size(); i++ ) {
                ItsmWoStatisticVO temp = voList.get( i );
                String serLevel = temp.getSerLevel();
                String statisticObj = temp.getStatisticObj();
                if ( null != statisticObj && serLevel.equals( woPriority.getName() ) && "MIN_SUM".equals( statisticObj ) ) { // 小计
                    temp.setSum( temp.getSum() + 1 );
                    oneLevelSumBean = temp;
                }
                if ( null != statisticObj && "MAX_SUM".equals( statisticObj ) ) { // 合计
                    temp.setSum( temp.getSum() + 1 );
                    allLevelSumBean = temp;
                }
            }

            int sum = tempBean.getSum() + 1;
            tempBean.setSum( sum );
            // 完工了的工单
            String woStatus = workOrder.getCurrStatus();
            if ( "woFeedback".equals( woStatus ) || "woFiling".equals( woStatus )
                    || "applicantConfirm".equals( woStatus ) ) {
                int solveSum = tempBean.getSolveSum();
                tempBean.setSolveSum( solveSum + 1 );// 完工了的工单总数

                int oneLevelSumsolveSum = oneLevelSumBean.getSolveSum(); // “小计”修改
                oneLevelSumBean.setSolveSum( oneLevelSumsolveSum + 1 );
                int allLevelSumsolveSum = allLevelSumBean.getSolveSum(); // “总计”修改
                allLevelSumBean.setSolveSum( allLevelSumsolveSum + 1 );

                // 统计超时的工单总数量,并对Bean做数据修改
                double solvetimeLength = woPriority.getSolveLength() * 60 * 60;
                double respondtimeLength = woPriority.getRespondLength() * 60;

                // 实际解决时间
                double solveTimeRealLen = workOrder.getSolveLen();
                // 实际响应时间
                double respondTimeRealLen = 0;
                if ( "qxWoType".equals( workOrder.getWorkOrderTypeCode() ) ) {
                    respondTimeRealLen = workOrder.getRespondLen();
                }

                String endReportUserId = workOrder.getEndReportUser(); // 汇报人ID

                double newSolveTimeSum = tempBean.getSolveTimeSum() + solveTimeRealLen;
                tempBean.setSolveTimeSum( newSolveTimeSum );
                double oneLevelnewSolveTimeSum = oneLevelSumBean.getSolveTimeSum() + solveTimeRealLen; // “小计”修改
                oneLevelSumBean.setSolveTimeSum( oneLevelnewSolveTimeSum );
                double allLevelnewSolveTimeSum = allLevelSumBean.getSolveTimeSum() + solveTimeRealLen; // “合计”修改
                allLevelSumBean.setSolveTimeSum( allLevelnewSolveTimeSum );

                // 解决超时工单
                if ( solveTimeRealLen > solvetimeLength ) {
                    int overtimeSolveNum = tempBean.getOverTimeSolveSum();
                    tempBean.setOverTimeSolveSum( overtimeSolveNum + 1 );
                    int oneLevelovertimeSolveNum = oneLevelSumBean.getOverTimeSolveSum();// “小计”修改
                    oneLevelSumBean.setOverTimeSolveSum( oneLevelovertimeSolveNum + 1 );
                    int allLevelovertimeSolveNum = allLevelSumBean.getOverTimeSolveSum();// “合计”修改
                    allLevelSumBean.setOverTimeSolveSum( allLevelovertimeSolveNum + 1 );
                } else { // 解决未超时
                    boolean isCusSer = isCusSer( endReportUserId, kfUserList ); // 判断是否是客服
                    if ( isCusSer ) { // 一线客服解决问题并且未超时的工单数量,并对Bean做数据修改
                        int csOnTimeSolveNum = tempBean.getCsOnTimeSolveSum();
                        tempBean.setCsOnTimeSolveSum( csOnTimeSolveNum + 1 );
                        int oneLevelSumcsOnTimeSolveNum = oneLevelSumBean.getCsOnTimeSolveSum();// “小计”修改
                        oneLevelSumBean.setCsOnTimeSolveSum( oneLevelSumcsOnTimeSolveNum + 1 );
                        int allLevelSumcsOnTimeSolveNum = allLevelSumBean.getCsOnTimeSolveSum();// “合计”修改
                        allLevelSumBean.setCsOnTimeSolveSum( allLevelSumcsOnTimeSolveNum + 1 );
                    } else { // 二线工程师（非客服的工程师）解决问题且未超时工单数量,并对Bean做数据修改
                        int teamOnTimeSolveSum = tempBean.getTeamOnTimeSolveSum();
                        tempBean.setTeamOnTimeSolveSum( teamOnTimeSolveSum + 1 );
                        int oneLevelSumteamOnTimeSolveSum = oneLevelSumBean.getTeamOnTimeSolveSum();// “小计”修改
                        oneLevelSumBean.setTeamOnTimeSolveSum( oneLevelSumteamOnTimeSolveSum + 1 );
                        int allLevelSumteamOnTimeSolveSum = allLevelSumBean.getTeamOnTimeSolveSum();// “合计”修改
                        allLevelSumBean.setTeamOnTimeSolveSum( allLevelSumteamOnTimeSolveSum + 1 );
                    }
                }

                // 响应超时工单
                if ( respondTimeRealLen >= respondtimeLength ) {
                    int overtimeRespondNum = tempBean.getOverTimeRespondSum();
                    tempBean.setOverTimeRespondSum( overtimeRespondNum + 1 );
                    int oneLevelSumOvertimeRespondNum = oneLevelSumBean.getOverTimeRespondSum();// “小计”修改
                    oneLevelSumBean.setOverTimeRespondSum( oneLevelSumOvertimeRespondNum + 1 );
                    int allLevelSumOvertimeRespondNum = allLevelSumBean.getOverTimeRespondSum();// “小计”修改
                    allLevelSumBean.setOverTimeRespondSum( allLevelSumOvertimeRespondNum + 1 );
                }

            }

        }

    }

    /**
     * @description:个人运维KPI统计(事件分类、 事件（请求）总数、成功解决比率、及时解决比率、及时响应比率、平均解决时间（h）)
     * @author: 王中华
     * @createDate: 2014-12-24
     * @param engHandlerAuditUserMap "工程师处理"环节的审批人map,
     * @param voList 要插入的bean
     * @param workOrder 要统计的工单
     * @param statisticType 统计类型
     * @param ftRoot 服务目录的根
     * @param UserGroupList 要统计的维护组列表
     * @throws Exception:
     */
    private void itTeamPersonStatistic(List<ItsmWoStatisticVO> voList, ItsmWorkOrder workOrder, String statisticType,
            ItsmWoFaultType ftRoot, List<SecureUserGroup> UserGroupList) throws Exception {
        LOG.info( "----------------------开始统计,统计类型为：" + statisticType + "-----------------------" );

        // 计算工单的服务级别和事件分类
        Map<String, Object> serLevelAndEventTypeObj = getSerLevelAndEventType( workOrder, ftRoot.getId() );
        ItsmWoPriority woPriority = (ItsmWoPriority) serLevelAndEventTypeObj.get( "serLevel" );
        ItsmWoFaultType woFaultType = (ItsmWoFaultType) serLevelAndEventTypeObj.get( "evenType" );

        // 获得此工单是哪位工程师的工单
        String woStatus = workOrder.getCurrStatus();
        String serLevelName = null;

        // 获取工单对应的统计列表中第一列的值
        serLevelName = getSerLevelName( statisticType, workOrder, UserGroupList );

        if ( serLevelName == null ) { // 如果不属于任何班组 或 不属于任何工程师，则不参与统计
            return;
        }
        // 根据计算的服务级别和事件分类，取出List中对应的Bean
        Map<String, String> serLevelAndEventTypeStr = new HashMap<String, String>();
        serLevelAndEventTypeStr.put( "serLevelName", serLevelName );// 工程师名字 或
                                                                    // 运维组名
        serLevelAndEventTypeStr.put( "evenTypeName", woFaultType.getName() );
        ItsmWoStatisticVO tempBean = getVOBean( voList, serLevelAndEventTypeStr );
        if ( tempBean == null ) { // 如果不属于任何班组 或 不属于任何工程师，则不参与统计
            LOG.info( "------------------工单" + workOrder.getWorkOrderCode() + "没有找到对应的统计bean----------------" );
            return;
        }

        // 个人运维KPI统计(事件（请求）总数、成功解决比率、及时解决比率、及时响应比率、平均解决时间（h）)
        ItsmWoStatisticVO oneLevelSumBean = new ItsmWoStatisticVO(); // 工程师的“小计”
        ItsmWoStatisticVO allLevelSumBean = new ItsmWoStatisticVO(); // 所有服务级别的“合计”
        for ( int i = 0; i < voList.size(); i++ ) {
            ItsmWoStatisticVO temp = voList.get( i );
            String serLevel = temp.getSerLevel();
            String statisticObj = temp.getStatisticObj();
            if ( null != statisticObj && serLevel.equals( serLevelName ) && "MIN_SUM".equals( statisticObj ) ) { // 小计
                temp.setSum( temp.getSum() + 1 );
                oneLevelSumBean = temp;
            }
            if ( null != statisticObj && "MAX_SUM".equals( statisticObj ) ) { // 合计
                temp.setSum( temp.getSum() + 1 );
                allLevelSumBean = temp;
            }

        }
        // 事件（请求）总数
        int sum = tempBean.getSum() + 1;
        tempBean.setSum( sum );
        // 完工了的工单
        if ( "woFeedback".equals( woStatus ) || "woFiling".equals( woStatus ) || "applicantConfirm".equals( woStatus ) ) {
            // 成功解决数量
            int solveSum = tempBean.getSolveSum();
            tempBean.setSolveSum( solveSum + 1 );// 完工了的工单总数

            int oneLevelSumsolveSum = oneLevelSumBean.getSolveSum(); // “小计”修改
            oneLevelSumBean.setSolveSum( oneLevelSumsolveSum + 1 );
            int allLevelSumsolveSum = allLevelSumBean.getSolveSum(); // “总计”修改
            allLevelSumBean.setSolveSum( allLevelSumsolveSum + 1 );

            // 统计超时的工单总数量,并对Bean做数据修改
            double solvetimeLength = woPriority.getSolveLength() * 60 * 60;
            double respondtimeLength = woPriority.getRespondLength() * 60;

            // 实际解决时间
            double solveTimeRealLen = workOrder.getSolveLen();
            // 实际响应时间
            double respondTimeRealLen = 0;
            if ( "qxWoType".equals( workOrder.getWorkOrderTypeCode() ) ) {
                respondTimeRealLen = workOrder.getRespondLen();
            }

            // 解决时间求和
            double newSolveTimeSum = tempBean.getSolveTimeSum() + solveTimeRealLen;
            tempBean.setSolveTimeSum( newSolveTimeSum );
            double oneLevelnewSolveTimeSum = oneLevelSumBean.getSolveTimeSum() + solveTimeRealLen; // “小计”修改
            oneLevelSumBean.setSolveTimeSum( oneLevelnewSolveTimeSum );
            double allLevelnewSolveTimeSum = allLevelSumBean.getSolveTimeSum() + solveTimeRealLen; // “合计”修改
            allLevelSumBean.setSolveTimeSum( allLevelnewSolveTimeSum );

            // 解决超时工单
            if ( solveTimeRealLen > solvetimeLength ) {
                int overtimeSolveNum = tempBean.getOverTimeSolveSum();
                tempBean.setOverTimeSolveSum( overtimeSolveNum + 1 );
                int oneLevelovertimeSolveNum = oneLevelSumBean.getOverTimeSolveSum();// “小计”修改
                oneLevelSumBean.setOverTimeSolveSum( oneLevelovertimeSolveNum + 1 );
                int allLevelovertimeSolveNum = allLevelSumBean.getOverTimeSolveSum();// “合计”修改
                allLevelSumBean.setOverTimeSolveSum( allLevelovertimeSolveNum + 1 );
            } else { // 解决未超时

            }

            // 响应超时工单
            if ( respondTimeRealLen >= respondtimeLength ) {
                int overtimeRespondNum = tempBean.getOverTimeRespondSum();
                tempBean.setOverTimeRespondSum( overtimeRespondNum + 1 );
                int oneLevelSumOvertimeRespondNum = oneLevelSumBean.getOverTimeRespondSum();// “小计”修改
                oneLevelSumBean.setOverTimeRespondSum( oneLevelSumOvertimeRespondNum + 1 );
                int allLevelSumOvertimeRespondNum = allLevelSumBean.getOverTimeRespondSum();// “小计”修改
                allLevelSumBean.setOverTimeRespondSum( allLevelSumOvertimeRespondNum + 1 );
            }

        }

    }

    /**
     * @description: 查询某工单属于统计中的那一组（例如：张三(个人运维KPI)、软件组（团队运维KPI））
     * @author: 王中华
     * @createDate: 2014-12-24
     * @param statisticType 统计类型（个人、团队运维KPI）
     * @param engineerSecureUser 工程师处理环节的审批人对象
     * @param workOrder 要统计的工单
     * @param statisticUserGroupList 要统计的运维团队用户组对象
     * @return:
     */
    private String getSerLevelName(String statisticType, ItsmWorkOrder workOrder,
            List<SecureUserGroup> statisticUserGroupList) {
        String result = null;
        String woStatus = workOrder.getCurrStatus();
        String principalEngeerId = workOrder.getPrincipalEngeer();
        String principalEngeerTeamId = workOrder.getPrincipalEngeerTeam();
        String siteid = workOrder.getSiteid();

        if ( "personStatistic".equals( statisticType ) ) {
            if ( principalEngeerId == null ) {
                if ( "workPlan".equals( woStatus ) ) { // 如果当前状态是“工程师处理”，此时是活动节点，则当前处理人为此单的工程师
                    result = workOrder.getCurrHandUserName();
                }
            } else {
                SecureUser engineerSecureUser = authManager.retriveUserById( principalEngeerId, siteid );
                result = engineerSecureUser.getName();
            }
        } else if ( "teamStatistic".equals( statisticType ) ) {
            if ( principalEngeerTeamId == null ) {
                if ( "workPlan".equals( woStatus ) ) { // 如果当前状态是“工程师处理”，此时是活动节点，则当前处理人为此单的工程师
                    String handlerUserId = workOrder.getCurrHandlerUser();
                    SecureUser tempSecureUser = authManager.retriveUserById( handlerUserId, siteid );
                    result = getMaintainTeamName( tempSecureUser, statisticUserGroupList );
                }
            } else {
                for ( int i = 0; i < statisticUserGroupList.size(); i++ ) {
                    SecureUserGroup tempUserGroup = statisticUserGroupList.get( i );
                    if ( principalEngeerTeamId.equals( tempUserGroup.getId() ) ) {
                        result = tempUserGroup.getName();
                    }
                }
            }
        }
        return result;
    }

    /**
     * @description:如果是客服则返回“服务台”，如果是属于统计用户组其中的一个用户组，则返回对应用户组的名字，否则返回null
     * @author: 王中华
     * @createDate: 2014-12-24
     * @param engineerSecureUser
     * @param statisticUserGroupList
     * @return:
     */
    private String getMaintainTeamName(SecureUser engineerSecureUser, List<SecureUserGroup> statisticUserGroupList) {
        List<SecureUserGroup> userGroupsList = engineerSecureUser.getGroups(); // 当前处理人所在的用户组
        List<Role> userRoleList = engineerSecureUser.getRoles(); // 当前处理人拥有的角色
        for ( int i = 0; i < userRoleList.size(); i++ ) { // 如果拥有客服角色，则统计到“服务台”类型里去
            if ( "ITC_ITSM_KF".equals( userRoleList.get( i ).getId() ) ) {
                return "服务台";
            }
        }
        for ( int i = 0; i < statisticUserGroupList.size(); i++ ) { // 如果拥有某个维护组，则统计到对应的维护组类型中去
            String statisticUGroupID = statisticUserGroupList.get( i ).getId();
            for ( int j = 0; j < userGroupsList.size(); j++ ) {
                String userGroupId = userGroupsList.get( j ).getId();
                if ( userGroupId.equals( statisticUGroupID ) ) {
                    return statisticUserGroupList.get( i ).getName();
                }
            }
        }
        return null;
    }

    private ItsmWorkTimeVo setWorkTimeVo(Date startDate, Date endDate) {
        ItsmWorkTimeVo result = new ItsmWorkTimeVo();
        result.setStart( startDate );
        result.setEnd( endDate );
        result.setSiteId( "ITC" );
        result.setMorning( ItsmCommonUtil.getProperties( "morningBegin" ) );
        result.setForenoon( ItsmCommonUtil.getProperties( "morningEnd" ) );
        result.setNoon( ItsmCommonUtil.getProperties( "afternoonBegin" ) );
        result.setAfternoon( ItsmCommonUtil.getProperties( "afternoonEnd" ) );
        result.setWorkTime( Integer.valueOf( ItsmCommonUtil.getProperties( "workTimeLen" ) ) );
        result.setFlag( false );
        return result;
    }

    /**
     * @description: 判断是否是客服
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param endReportUserId
     * @param siteid
     * @return:
     */
    private boolean isCusSer(String endReportUserId, List<SecureUser> tempList) {
        for ( int j = 0; j < tempList.size(); j++ ) {
            if ( tempList.get( j ).getId() == endReportUserId ) {
                return true;
            }
        }
        return false;
    }

    /**
     * @description: 根据工单性质获取其对应的服务级别和事件分类信息
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param workOrder
     * @return:
     */
    private Map<String, Object> getSerLevelAndEventType(ItsmWorkOrder workOrder, int rootId) {
        Map<String, Object> result = new HashMap<String, Object>();
        int priorityId = workOrder.getPriorityId();
        ItsmWoPriority tempPriority = woPriorityDao.queryWoPriorityById( priorityId );

        String faultTypeId = workOrder.getFaultTypeId();
        ItsmWoFaultType woFaultType = getOneLevelFTById( Integer.valueOf( faultTypeId ), rootId ); // 获取一级服务目录

        result.put( "serLevel", tempPriority );
        result.put( "evenType", woFaultType );

        return result;

    }

    private ItsmWoFaultType getOneLevelFTById(int id, int rootId) {
        ItsmWoFaultType temp = woFaultTypeDao.queryFaultTypeById( id );
        if ( temp.getParentId() != rootId ) {
            temp = getOneLevelFTById( temp.getParentId(), rootId );
        }
        return temp;
    }

    /**
     * @description: 根据服务级别和事件分类返回对应的Bean
     * @author: 王中华
     * @createDate: 2014-11-25
     * @param voList
     * @param serLevelAndEventType
     * @return:
     */
    private ItsmWoStatisticVO getVOBean(List<ItsmWoStatisticVO> voList, Map<String, String> serLevelAndEventType) {

        String serLevel = serLevelAndEventType.get( "serLevelName" );
        String evenType = serLevelAndEventType.get( "evenTypeName" );

        for ( int i = 0; i < voList.size(); i++ ) {
            ItsmWoStatisticVO temp = voList.get( i );

            String tempSerLevel = temp.getSerLevel();
            String tempEventTyoeString = temp.getEventType();

            if ( tempSerLevel.equals( serLevel ) && tempEventTyoeString.equals( evenType ) ) {
                return temp;
            }
        }
        return null;
    }

}
