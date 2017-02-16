package com.timss.itsm.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.bean.ItsmWoPriority;
import com.timss.itsm.dao.ItsmHomepageCardDao;
import com.timss.itsm.dao.ItsmWoFaultTypeDao;
import com.timss.itsm.dao.ItsmWoPriorityDao;
import com.timss.itsm.dao.ItsmWorkOrderDao;
import com.timss.itsm.service.ItsmHomePageCardService;
import com.timss.itsm.service.ItsmWoStatisticUtilService;
import com.timss.itsm.service.ItsmWorkTimeService;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;

@Service
public class ItsmHomePageCardServiceImpl implements ItsmHomePageCardService {

    @Autowired
    ItsmWoFaultTypeDao woFaultTypeDao;
    @Autowired
    ItsmWoPriorityDao woPriorityDao;
    @Autowired
    ItsmWorkOrderDao workOrderDao;
    @Autowired
    ISecurityMaintenanceManager iSecManager;
    @Autowired
    ItsmWoStatisticUtilService woStatisticUtilService;
    @Autowired
    ItsmWorkTimeService workTimeService;
    @Autowired
    private ItsmHomepageCardDao itsmHomepageCardDao;

    private static final Logger LOG = Logger.getLogger( ItsmHomePageCardServiceImpl.class );

    private Object[][] genWoSdSeries(Map<String, Double> faultTypeRatioMap, List<ItsmWoFaultType> woFTList) {

        ItsmWoFaultType[] ftArray = new ItsmWoFaultType[woFTList.size()];
        double[] ratioArray = new double[woFTList.size()];
        double ratioSum = 0.0;
        int index = 0;
        for ( int i = 0; i < woFTList.size(); i++ ) {
            int tempFtId = woFTList.get( i ).getId();
            if ( faultTypeRatioMap.containsKey( String.valueOf( tempFtId ) ) ) {
                double tempRatio = faultTypeRatioMap.get( String.valueOf( tempFtId ) );
                if ( tempRatio >= 10 ) {
                    ftArray[i] = woFTList.get( i );
                    ratioArray[i] = tempRatio;
                    ratioSum += tempRatio; // 已占的百分比
                    index++;
                }
            }

        }
        // 排序并返回排序后的Object result[][]
        Object result[][] = new Object[index][2];
        result = sortMergeArray( ftArray, ratioArray );

        Object[] sx = new Object[2];
        sx[0] = "其它";
        sx[1] = 100 - ratioSum;
        if ( 100 - ratioSum > 0 ) {
            result[index] = sx;
        }

        return result;
    }

    private Object[][] sortMergeArray(ItsmWoFaultType[] ftArray, double[] ratioArray) {
        Object result[][] = new Object[ratioArray.length][2];
        for ( int i = 0; i < ratioArray.length; i++ ) {
            int maxItemindex = getMaxItemIndx( ratioArray );
            if ( maxItemindex >= 0 ) {
                Object[] s = new Object[2];
                s[0] = ftArray[maxItemindex].getName();
                s[1] = ratioArray[maxItemindex];
                result[i] = s;
                ratioArray[maxItemindex] = 0;
            }
        }
        return result;
    }

    /**
     * @description: 获取数组中最大值的序号
     * @author: 王中华
     * @createDate: 2015-2-27
     * @param ratioArray 百分比的数组
     * @return:
     */
    private int getMaxItemIndx(double[] ratioArray) {
        int index = -1;
        double value = 0;
        for ( int i = 0; i < ratioArray.length; i++ ) {
            double tempValue = ratioArray[i];
            if ( tempValue > value ) {
                value = tempValue;
                index = i;
            }
        }
        return index;
    }

    /**
     * @description:计算各类以及服务目录的工单所占比例
     * @author: 王中华
     * @createDate: 2015-2-15
     * @param woFtSumHashMap 各类一级服务目录所占的工单数量（分子）
     * @param woFTList 工单总数量（分母）
     * @return:
     */
    private Map<String, Double> caculateFtRatio(Map<Integer, Integer> woFtSumHashMap, List<ItsmWoFaultType> woFTList) {
        Map<String, Double> resultRatioMap = new HashMap<String, Double>();
        int sum = 0;
        for ( int i = 0; i < woFTList.size(); i++ ) {
            if ( woFtSumHashMap.containsKey( woFTList.get( i ).getId() ) ) {
                sum += woFtSumHashMap.get( woFTList.get( i ).getId() );
            }
        }
        for ( int i = 0; i < woFTList.size(); i++ ) {
            if ( woFtSumHashMap.containsKey( woFTList.get( i ).getId() ) ) {
                int faultTypeSum = woFtSumHashMap.get( woFTList.get( i ).getId() );
                double tempRatio = (double) faultTypeSum / sum * 100;
                tempRatio = (double) (Math.round( tempRatio * 100 ) / 100.0);
                resultRatioMap.put( String.valueOf( woFTList.get( i ).getId() ), tempRatio );
            }
        }
        return resultRatioMap;
    }

    /**
     * @description:计算个维护团队的及时响应率与及时解决率
     * @author: 王中华
     * @createDate: 2015-2-15
     * @param teamOnTimeWoSumHashMap 各团队的及时响应数量和及时解决数量（分子）
     * @param teamWoSumHashMap 各团队的所有工单总数（分母）
     * @param userGroupList 有哪些团队参与统计
     * @return:
     */
    private Map<String, Double> caculateRatio(Map<String, Integer> teamOnTimeWoSumHashMap,
            Map<String, Integer> teamWoSumHashMap, List<SecureUserGroup> userGroupList) {
        Map<String, Double> resultRatioMap = new HashMap<String, Double>();
        for ( int i = 0; i < userGroupList.size(); i++ ) {
            if ( teamWoSumHashMap.containsKey( userGroupList.get( i ).getId() ) ) { // 如果有对应的团队的工单总数
                if ( teamOnTimeWoSumHashMap.containsKey( userGroupList.get( i ).getId() ) ) { // 如果有对应团队的及时响应后解决的工单数
                    int teamOnTimeNum = teamOnTimeWoSumHashMap.get( userGroupList.get( i ).getId() );
                    int teamSum = teamWoSumHashMap.get( userGroupList.get( i ).getId() );
                    double tempRatio = (double) teamOnTimeNum / teamSum * 100;
                    tempRatio = (double) (Math.round( tempRatio * 100 ) / 100.0);
                    resultRatioMap.put( userGroupList.get( i ).getId(), tempRatio ); // 设置维护组的及时率（响应率或解决率）
                } else { // 团队有工单，但是没有及时响应后解决的工单，即所以的都超时
                    resultRatioMap.put( userGroupList.get( i ).getId(), 0.0 );
                }
            } else { // 对应团队的工单总数为0，及时响应和解决率都为100%
                resultRatioMap.put( userGroupList.get( i ).getId(), 100.0 ); // 设置维护组的及时率（响应率或解决率）
            }
        }
        return resultRatioMap;
    }

    @Override
    public Map<String, Object> sdCardStatistic() {
        LOG.info( "----------首页卡片：服务目录占比，统计开始-------------------" );
        // 查询服务目录的一级类型
        List<ItsmWoFaultType> woFTList = woFaultTypeDao.queryOneLevelFTBySiteId( "ITC" );

        // 获取当前年的第一天
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        calendar.clear();
        calendar.set( Calendar.YEAR, year );
        Date currYearFirst = calendar.getTime(); // 当前年的第一天

        List<Map<String, BigDecimal>> resultHashMapList = itsmHomepageCardDao.queryOneLevlFtWoSum( currYearFirst,
                new Date() );
        // 用于存放个服务目录对应的工单数量
        Map<Integer, Integer> resultSumHashMap = new HashMap<Integer, Integer>();
        List<Integer> oneLevelFtId = new ArrayList<Integer>();
        for ( int i = 0; i < resultHashMapList.size(); i++ ) {
            Map<String, BigDecimal> temp = resultHashMapList.get( i );
            int ftId = temp.get( "FTID" ).intValue();
            int woSum = temp.get( "WOSUM" ).intValue();
            oneLevelFtId.add( ftId );
            resultSumHashMap.put( ftId, woSum );
        }

        // 计算各级服务目录占的比率
        Map<String, Double> faultTypeRatioMap = caculateFtRatio( resultSumHashMap, woFTList );

        // 排序，将不满10%合并
        Object[][] woSDSeries = genWoSdSeries( faultTypeRatioMap, woFTList );

        Map<String, Object> resultRatioMap = new HashMap<String, Object>();
        resultRatioMap.put( "woSDstatistic", woSDSeries );
        LOG.info( "----------首页卡片：服务目录占比，统计结束-------------------" );
        return resultRatioMap;
    }

    @Override
    public Map<String, Object> itsmUnOkWostatistic() {
        LOG.info( "----------首页卡片：服务不满意率，统计开始-------------------" );
        // 获取当前年的第一天
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        calendar.clear();
        calendar.set( Calendar.YEAR, year );
        Date currYearFirst = calendar.getTime(); // 当前年的第一天

        int allWoSum = itsmHomepageCardDao.queryFbWoSum( currYearFirst, new Date() );
        int allUnOkWoSum = itsmHomepageCardDao.queryUnOkWoSum( currYearFirst, new Date() );

        double unOKwoRatio = Math.round( (double) allUnOkWoSum * 100 / allWoSum );

        Map<String, Object> unOKwoStatisticMap = new HashMap<String, Object>();
        unOKwoStatisticMap.put( "sum", (double) allUnOkWoSum );
        unOKwoStatisticMap.put( "ratio", unOKwoRatio ); // 四舍五入取整

        Map<String, Object> resultRatioMap = new HashMap<String, Object>();
        resultRatioMap.put( "unOKwoStatistic", unOKwoStatisticMap );
        LOG.info( "----------首页卡片：服务不满意率，统计结束-------------------" );
        return resultRatioMap;
    }

    @Override
    public Map<String, Object> itsmTeamRespondSolvestatistic() {
        LOG.info( "----------首页卡片：团队及时响应率与解决率，统计开始-------------------" );
        // 获取当前年的第一天
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        calendar.clear();
        calendar.set( Calendar.YEAR, year );
        Date currYearFirst = calendar.getTime(); // 当前年的第一天

        List<Map<String, Object>> teamWoSum = itsmHomepageCardDao.queryteamWoSum( currYearFirst, new Date() );
        List<Map<String, Object>> teamOnTimeRespondWoSum = itsmHomepageCardDao.queryteamOnTimeRespondWoSum(
                currYearFirst, new Date() );
        List<Map<String, Object>> teamOnTimeSolveWoSum = itsmHomepageCardDao.queryteamOnTimeSolveWoSum( currYearFirst,
                new Date() );

        Map<String, Integer> teamWoSumMap = listMapToMap( teamWoSum );
        Map<String, Integer> teamOnTimeRespondWoSumMap = listMapToMap( teamOnTimeRespondWoSum );
        Map<String, Integer> teamOnTimeSolveWoSumMap = listMapToMap( teamOnTimeSolveWoSum );

        // 维护组查询
        List<SecureUserGroup> userGroupList = woStatisticUtilService.retrieveGroupsByKeyword( "itc_itsm_wt" );

        Map<String, Double> respondRatioMap = caculateRatio( teamOnTimeRespondWoSumMap, teamWoSumMap, userGroupList );
        Map<String, Double> solveRatioMap = caculateRatio( teamOnTimeSolveWoSumMap, teamWoSumMap, userGroupList );

        Map<String, Object> resultRatioMap = new HashMap<String, Object>();
        resultRatioMap.put( "teamOnTimeRespondRatio", respondRatioMap );
        resultRatioMap.put( "teamOnTimeSolveRatio", solveRatioMap );
        LOG.info( "----------首页卡片：团队及时响应率与解决率，统计结束-------------------" );
        return resultRatioMap;
    }

    /**
     * @description:listMapData其中map数据一个是string，一个是int
     * @author: 王中华
     * @createDate: 2015-3-13
     * @param listMapData
     * @return:
     */
    private Map<String, Integer> listMapToMap(List<Map<String, Object>> listMapData) {
        Map<String, Integer> resultSumHashMap = new HashMap<String, Integer>();
        for ( int i = 0; i < listMapData.size(); i++ ) {
            Map<String, Object> temp = listMapData.get( i );
            String teamId = (String) temp.get( "TEAMID" );
            int woSum = ((BigDecimal) temp.get( "WOSUM" )).intValue();
            resultSumHashMap.put( teamId, woSum );
        }
        return resultSumHashMap;
    }

    @Override
    public Map<String, Object> itsmWoSolveAbilitystatistic() {
        LOG.info( "----------首页卡片：工单解决能力，统计开始-------------------" );
        Map<String, Object> result = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        calendar.clear();
        calendar.set( Calendar.YEAR, year );
        calendar.set( Calendar.MONTH, month );
        calendar.set( Calendar.DAY_OF_MONTH, 1 );// 设置为1号,当前日期既为本月第一天
        Date firstDayOfCurrMonth = calendar.getTime(); // 当月第一天
        calendar.add( Calendar.MONTH, -1 );
        Date firstDayOfLastMonth = calendar.getTime(); // 上月第一天

        // 参加统计的工单数量
        int woSumLastMonth = itsmHomepageCardDao.queryWoSolveAbilitySum( firstDayOfLastMonth, firstDayOfCurrMonth );
        int woSumCurrMonth = itsmHomepageCardDao.queryWoSolveAbilitySum( firstDayOfCurrMonth, new Date() );
        // 超时响应的工单数量
        int woOverTimeRespondSumLastMonth = itsmHomepageCardDao.queryOverTimeRespondSum( firstDayOfLastMonth,
                firstDayOfCurrMonth );
        int woOverTimeRespondSumCurrMonth = itsmHomepageCardDao.queryOverTimeRespondSum( firstDayOfCurrMonth,
                new Date() );
        // 超时解决的工单数量
        int woOverTimeSolveSumLastMonth = itsmHomepageCardDao.queryOverTimeSolveSum( firstDayOfLastMonth,
                firstDayOfCurrMonth );
        int woOverTimeSolveSumCurrMonth = itsmHomepageCardDao.queryOverTimeSolveSum( firstDayOfCurrMonth, new Date() );

        double overTimeRespondLastMonth = (double) woOverTimeRespondSumLastMonth * 100 / woSumLastMonth;
        double overTimeRespondCurrMonth = (double) woOverTimeRespondSumCurrMonth * 100 / woSumCurrMonth;
        double overTimeSolveLastMonth = (double) woOverTimeSolveSumLastMonth * 100 / woSumLastMonth;
        double overTimeSolveCurrMonth = (double) woOverTimeSolveSumCurrMonth * 100 / woSumCurrMonth;

        Map<String, Object> abilityResultOfCurrMonth = new HashMap<String, Object>();
        Map<String, Integer> respondResultMap = new HashMap<String, Integer>();
        respondResultMap.put( "ratio", (int) overTimeRespondCurrMonth );
        Map<String, Integer> solveResultMap = new HashMap<String, Integer>();
        solveResultMap.put( "ratio", (int) overTimeSolveCurrMonth );
        if ( overTimeRespondLastMonth >= overTimeRespondCurrMonth ) {
            respondResultMap.put( "respondFlag", -1 ); // -1 代表降 1待办升
        } else {
            respondResultMap.put( "respondFlag", 1 );
        }
        if ( overTimeSolveLastMonth >= overTimeSolveCurrMonth ) {
            solveResultMap.put( "solveFlag", -1 );
        } else {
            solveResultMap.put( "solveFlag", 1 );
        }
        abilityResultOfCurrMonth.put( "respondResult", respondResultMap );
        abilityResultOfCurrMonth.put( "solveResult", solveResultMap );
        result.put( "abilityResult", abilityResultOfCurrMonth );
        LOG.info( "----------首页卡片：工单解决能力，统计结束-------------------" );
        return result;
    }

    @Override
    public Map<String, Object> woAvgRespondTimesCard() {
        LOG.info( "----------首页卡片：各服务级别平均响应时间，统计开始-------------------" );
        Map<String, Object> result = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        calendar.clear();
        calendar.set( Calendar.YEAR, year );
        calendar.set( Calendar.MONTH, month );
        calendar.set( Calendar.DAY_OF_MONTH, 1 );// 设置为1号,当前日期既为本月第一天
        Date firstDayOfCurrMonth = calendar.getTime(); // 当月第一天

        List<ItsmWoPriority> woPriorityList = woPriorityDao.queryWoPriorityListBySiteId( "ITC" );

        // 上月和当月的平均响应时间
        List<Map<String, BigDecimal>> everyFtAvgRespondTimeListCurrMonth = itsmHomepageCardDao
                .queryeveryFtAvgRespondTime( firstDayOfCurrMonth, new Date() );
        Map<Integer, Double> respondLenOfCurrMonth = respondTimeListMapToMap( everyFtAvgRespondTimeListCurrMonth );

        Map<String, Map<String, Double>> respondCarddataMap = new HashMap<String, Map<String, Double>>();
        for ( int i = 0; i < woPriorityList.size(); i++ ) {
            ItsmWoPriority tempPriority = woPriorityList.get( i );
            int priorityId = tempPriority.getId();
            double currAvgLen = respondLenOfCurrMonth.containsKey( priorityId ) ? respondLenOfCurrMonth
                    .get( priorityId ) : 0;

            Map<String, Double> tempStatisticmMap = new HashMap<String, Double>();
            tempStatisticmMap.put( "len", currAvgLen );
            tempStatisticmMap.put( "standardLen", tempPriority.getRespondLength() );
            respondCarddataMap.put( String.valueOf( priorityId ), tempStatisticmMap );
        }
        result.put( "respondCard", respondCarddataMap );
        result.put( "woPriorityList", woPriorityList );
        LOG.info( "----------首页卡片：各服务级别平均响应时间，统计结束-------------------" );
        return result;
    }

    private Map<Integer, Double> respondTimeListMapToMap(List<Map<String, BigDecimal>> respondTimeMapList) {
        Map<Integer, Double> resultSumHashMap = new HashMap<Integer, Double>();
        for ( int i = 0; i < respondTimeMapList.size(); i++ ) {
            Map<String, BigDecimal> temp = respondTimeMapList.get( i );
            int prioridId = temp.get( "PRIORIDID" ).intValue();
            double avgRespondLen = temp.get( "RESPONDLEN" ).intValue();
            resultSumHashMap.put( prioridId, avgRespondLen );
        }
        return resultSumHashMap;
    }

}
