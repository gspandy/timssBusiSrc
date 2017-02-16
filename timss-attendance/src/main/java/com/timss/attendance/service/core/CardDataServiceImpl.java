package com.timss.attendance.service.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.MachineBean;
import com.timss.attendance.dao.CardDataDao;
import com.timss.attendance.service.CardDataService;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.MachineService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.AtdDataBaseUtil;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 考勤机数据service
 * @description: {desc}
 * @company: gdyd
 * @className: CardDataServiceImpl.java
 * @author: fengzt
 * @createDate: 2015年6月3日
 * @updateUser: yyn
 * @version: 1.0
 */
@Service("cardDataService")
public class CardDataServiceImpl implements CardDataService {

    private Logger log = LoggerFactory.getLogger( CardDataServiceImpl.class );
    
    @Autowired
    private CardDataDao cardDataDao;
    
    @Autowired
    private WorkStatusService workStatusService;
    
    @Autowired
    private ItcMvcService itcMvcService;
 
    @Autowired
    private DefinitionService definitionService;
    
    @Autowired
    private MachineService machineService;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Autowired
    private AtdDataBaseUtil dbUtil;
    
    /**
     * 
     * @description:批量插入
     * @author: fengzt
     * @createDate: 2015年6月3日
     * @updateBy:yyn
     * @updateDate: 2016年1月5日
     * @param list
     * @return:int
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertBatchCardData(List<CardDataBean> list){
    	int num=0;//插入条数
    	try {
    		//尝试批量插入，如果出错则单条插入
			num=cardDataDao.insertBatchCardData( list );
		} catch (Exception e) {
			log.error("批量插入打卡数据失败---->",e);
			for (CardDataBean cardDataBean : list) {
				try {
					num+=cardDataDao.insertCardData(cardDataBean);
				} catch (Exception e2) {
					log.error("单条插入打卡数据失败---->"+cardDataBean.getCheckDate()+" "+cardDataBean.getUserId()+" "+cardDataBean.getSiteId(),e2);
					//尝试删除相同时间和用户的数据再插入
					try {
						Integer delNum=cardDataDao.deleteCardDataByDateAndUserId(cardDataBean.getCheckDate(), cardDataBean.getUserId());
						log.info("删除了打卡数据"+cardDataBean.getCheckDate()+" "+cardDataBean.getUserId()+" 后再插入，删除条数："+delNum);
						num+=cardDataDao.insertCardData(cardDataBean);
					} catch (Exception e3) {
						log.error("单条先删除后插入打卡数据失败---->"+cardDataBean.getCheckDate()+" "+cardDataBean.getUserId()+" "+cardDataBean.getSiteId(),e3);
					}
				}
			}
		}
        return num;
    }

    /**
     * 
     * @description:查询打卡记录列表
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param pageVo
     * @return:List<CardDataBean>
     */
    @Override
    public List<CardDataBean> queryCardDataList(Page<HashMap<?, ?>> pageVo) {
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "checkDate DESC, userName" );
            pageVo.setSortOrder( "ASC" );
        }
        
        //用户拥有的角色
        String roleFlag = privUtil.getStatPrivLevel();
        if( roleFlag.equals( "deptMgr" ) ){
        	String deptId = userInfo.getOrgId();
            pageVo.setParameter( "deptId", deptId );
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	String userId = userInfo.getUserId();
            pageVo.setParameter( "userId", userId );
        }
        
        return cardDataDao.queryCardDataListBySearch( pageVo );
    }
    
    /**
     * 
     * @description:查询打卡记录列表--高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param pageVo
     * @return:List<CardDataBean>
     */
    @Override
    public List<CardDataBean> queryCardDataListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "checkDate DESC, userName" );
            pageVo.setSortOrder( "ASC" );
        }
        
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("searchDateFrom", map.get("searchDateFrom"));
        map.put("searchDateFrom", null);
        params.put("searchDateEnd", map.get("searchDateEnd"));
        map.put("searchDateEnd", null);
        params.put("onStatus", "0".equals(""+map.get("onStatus"))?null:map.get("onStatus"));
        map.put("onStatus", null);
        pageVo.setParams(params);
        pageVo.setFuzzyParams(map);
        
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        //用户拥有的角色
        String roleFlag = privUtil.getStatPrivLevel();
        if( roleFlag.equals( "deptMgr" ) ){
        	String deptId = userInfo.getOrgId();
            pageVo.setParameter( "deptId", deptId );
        }else if( roleFlag.equals( "hrMgr" ) ){
            
        }else{
        	String userId = userInfo.getUserId();
            pageVo.setParameter( "userId", userId );
        }
        
        return cardDataDao.queryCardDataListBySearch( pageVo );
    }

    /**
     * 插入一个考勤机的数据
     * @param definitionBean
     * @param userMap
     * @param cardDataBeans
     * @return
     * @throws Exception
     */
    private Integer insertOrUpdateMachineCardData(List<CardDataBean>cardDataBeans) throws Exception{
    	//统计插入条数
        int count = 0;
        List<CardDataBean> insertData = cardDataBeans;
        log.info( "start insert machine data with size ：" + insertData.size() );
        Integer num=0;//计算实际插入条数
        if( insertData != null && insertData.size() > 0 ){
            //插入考勤机数据表
        	//数据量过大，考虑分批插入
        	int batchNum=100;//批量插入的条数
        	int insertIndex=0;//开始插入的序号
        	while ((insertIndex+batchNum)<=insertData.size()) {
        		count = insertBatchCardData( insertData.subList(insertIndex, insertIndex+batchNum) );
                log.info( "inserted ：" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<insertData.size()){
        		count = insertBatchCardData( insertData.subList(insertIndex, insertData.size()) );
                log.info( "inserted ：" + count );
                num+=count;
        	}
        }
        log.info( "finish insert-->total inserted：" + num );
        return num;
    }
    
    @Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer saveMachineCardData(MachineBean machineBean) throws Exception {
    	String siteId=machineBean.getSiteid();
    	Integer getNum=machineBean.getCardDataList().size();
    	Integer updateNum=0,delNum=0;
    	String machineInfo="machine:"+machineBean.getAmIp()+":"+machineBean.getAmPort()+"@site:"+siteId;
    	if(getNum>0){
    		//清除之前的打卡记录
    		delNum=deleteCardDataByMachine(machineBean);
        	updateNum=insertOrUpdateMachineCardData(machineBean.getCardDataList());
    	}
    	
    	//更新考勤机的时间戳
    	machineBean.setLastSync(new Date());
    	machineService.updateSync(machineBean);
    	
    	log.info( machineInfo+" data get："+ getNum+" del：" + delNum+" update：" + updateNum +
    			" next import time："+DateFormatUtil.formatDate(machineBean.getLastImport(), "yyyy-MM-dd HH:mm:ss"));
    	
        //更新打卡统计
    	String startTimeStr=machineBean.getStartTime();
    	String endTimeStr=machineBean.getEndTime();
        if(!StringUtils.isBlank(startTimeStr)&&!StringUtils.isBlank(endTimeStr)){
        	workStatusService.checkCardDataWorkStatus(siteId, startTimeStr, endTimeStr);
        }else{
        	log.info("skip checkCardDataWorkStatus of "+machineInfo);
        }
        
        return updateNum;
	}
    
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, Integer> insertOrUpdateMachineListCardData(List<MachineBean> machineBeans, String siteId) throws Exception {
		Integer getNum=0,updateNum=0,delNum=0;
		String startTimeStr="",endTimeStr="";//用于触发更新打卡统计的日期
        for (MachineBean machineBean : machineBeans) {
        	Integer getSize=machineBean.getCardDataList().size();
        	Integer updateSize=0;
        	if(getSize>0){
        		//清除之前的打卡记录
            	Integer del=deleteCardDataByMachine(machineBean);
            	delNum+=del;
            	log.info( "start insert machine data-->"+ machineBean.getAmIp()+":"+machineBean.getAmPort()+" clear "+
            			machineBean.getStartTime()+" to "+machineBean.getEndTime()+" with data "+del );
            	getNum+=getSize;
            	updateSize=insertOrUpdateMachineCardData(machineBean.getCardDataList());
            	updateNum+=updateSize;
            	
            	//找到最小的开始时间
            	startTimeStr="".equals(startTimeStr)?machineBean.getStartTime():(startTimeStr.compareTo(machineBean.getStartTime())>0?machineBean.getStartTime():startTimeStr);
            	//找到最大的结束时间
            	endTimeStr="".equals(endTimeStr)?machineBean.getEndTime():(endTimeStr.compareTo(machineBean.getEndTime())<0?machineBean.getEndTime():endTimeStr);
        	}
        	//更新考勤机的时间戳
        	machineBean.setLastSync(new Date());
        	machineService.updateSync(machineBean);
        	log.info( "finish insert machine data-->"+ machineBean.getAmIp()+":"+machineBean.getAmPort()+" "+updateSize+"/"+getSize+" next import timestamp："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(machineBean.getLastImport()) );
		}
        
        log.info( "site:"+siteId+" machine data get："+ getNum+" del：" + delNum+" update：" + updateNum );
        
        //更新打卡统计
        if(!"".equals(startTimeStr)&&!"".equals(endTimeStr)){
        	workStatusService.checkCardDataWorkStatus(siteId, startTimeStr, endTimeStr);
        }else{
        	log.info("skip checkCardDataWorkStatus of site "+siteId);
        }
        
        Map<String, Integer>result=new HashMap<String, Integer>();
        result.put("getNum", getNum);
        result.put("updateNum", updateNum);
        
        return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer deleteCardDataByEndTime(String endTimeStr, String siteId)
			throws Exception {
		Integer result=cardDataDao.deleteCardDataByEndTime(endTimeStr,siteId);
		return result;
	}
	
	@Override
    public List<CardDataBean> queryValidCardDataListByDiffDate(String startDate, String endDate, String siteId,String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "startDate", startDate );
        map.put( "endDate", endDate );
        map.put( "siteId", siteId );
        map.put( "onlyValid", true );
        map.put( "userId", userId );
        
        return cardDataDao.queryCardDataListByDiffDate( map );
    }
	
	@Override
	public Integer deleteCardDataByMachine(MachineBean machine)throws Exception{
		return cardDataDao.deleteCardDataByMachine( machine );
	}

	@Override
	public List<CardDataBean> queryAll(String siteId, String[] userIds,
			String startTimeStr, String endTimeStr) throws Exception {
		return cardDataDao.queryAll(siteId, userIds, startTimeStr, endTimeStr);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateCardDataList(List<CardDataBean> list) throws Exception {
		Integer result=dbUtil.limitedBatchOperateList(list, this.getClass(), "batchUpdateCardDataList",this);
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer updateCardData(CardDataBean bean) throws Exception {
		return cardDataDao.updateCardData(bean);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer batchUpdateCardDataList(List<CardDataBean> list)
			throws Exception {
    	int num=0;//更新条数
    	String name="打卡记录";
    	try {
    		//尝试批量更新，如果出错则单条更新
			num=cardDataDao.batchUpdateCardDataList(list);
		} catch (Exception e) {
			log.error("批量更新"+name+"数据失败---->",e);
			for (CardDataBean bean : list) {
				try {
					num+=updateCardData(bean);
				} catch (Exception e2) {
					log.error("单条更新"+name+"数据失败---->id:"+bean.getId()+" checkTime:"+bean.getCheckDate()+" userId:"+bean.getUserId()+" workstatus:"+bean.getWorkStatus(),e2);
				}
			}
		}
        return num<0?0:num;
	}

	@Override
	public CardDataBean setupCardDataBean(String userId, String userName,
			String checkTime, DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap, 
			Boolean isOpr,Map<String, DutyPersonShiftVo>dutyPersonShiftMap) {
		CardDataBean bean = new CardDataBean();
        
        bean.setUserId(userId);
        bean.setUserName(userName);
        bean.setCheckDate(checkTime);
        bean.setSiteId(definitionBean.getSiteId());
        bean.setCreateDate( new Date() );
        bean.setId( UUIDGenerator.getUUID() );//由数据库联合主键判重
        
        //添加部门信息
        String deptId="",deptName="";
        String userMapFlag=bean.getUserId()+"_"+bean.getSiteId();
        if(userMap.get(userMapFlag)==null){//如果没缓存，则去取人员信息，否则取缓存数据
        	//获得部门信息
            try {
            	UserInfo userInfo = itcMvcService.getUserInfo( bean.getUserId(), bean.getSiteId() );
                if(userInfo!=null){
                	deptId=userInfo.getOrgId();
                	deptName=userInfo.getOrgName();
                }
    		} catch (Exception e) {
    			log.debug("setupCardDataBean-->获取用户失败:"+bean.getUserId()+"@"+bean.getSiteId());
    		}
            //存入缓存，将出错的部门也存入缓存，防止重复获取错误部门
        	Map<String, String>dept=new HashMap<String, String>();
        	dept.put("deptId", deptId);
        	dept.put("deptName", deptName);
        	userMap.put(userMapFlag, dept);
        }else{
        	Map<String, String>dept=userMap.get(userMapFlag);
        	deptId=dept.get("deptId");
        	deptName=dept.get("deptName");
        }
        bean.setDeptId(deptId);
        bean.setDeptName(deptName);
        
        bean.setIsOpr(isOpr);
        if(bean.getIsOpr()){//设置运行人员的排班时间
        	setOprPersonDutyToCardDataBean(bean,definitionBean.getValidMin(),dutyPersonShiftMap);
        }
        //添加考勤结果
        bean.setWorkStatus(workStatusService.checkWorkStatusByCardData(bean, definitionBean));
        
        return bean;
	}
	
	@Override
    public void setOprPersonDutyToCardDataBean(CardDataBean bean,Integer validMin,Map<String, DutyPersonShiftVo>dutyPersonShiftMap){
    	//当天的
    	Date checkDate=DateFormatUtil.parseDate(bean.getCheckDate(), "yyyy-MM-dd HH:mm:ss");
    	if(!checkAndSetOprPersonDuty(bean,validMin,checkDate,checkDate,dutyPersonShiftMap)){
    		//前一天
    		Date prevDate=DateFormatUtil.addDate(checkDate, "d", -1);
    		if(!checkAndSetOprPersonDuty(bean,validMin,prevDate,checkDate,dutyPersonShiftMap)){
    			//后一天
        		Date nextDate=DateFormatUtil.addDate(checkDate, "d", 1);
        		checkAndSetOprPersonDuty(bean,validMin,nextDate,checkDate,dutyPersonShiftMap);
        	}
    	}
    }
    //检查dateToCompare是否在dateToCheck那一天的班次的有效打卡时间内，是则设置运行人员的班次信息
    private Boolean checkAndSetOprPersonDuty(CardDataBean bean,Integer validMin,Date dateToCheck,Date dateToCompare,
    		Map<String, DutyPersonShiftVo>dutyPersonShiftMap){
    	String checkDateStr;
		try {
			checkDateStr = DateFormatUtil.dateToString(dateToCheck, "yyyy-MM-dd");
		} catch (ParseException e) {
			log.debug("DateFormatUtil.dateToString error",e);
			return false;
		}
    	String flag=checkDateStr+"_"+bean.getUserId();
    	DutyPersonShiftVo shiftVo=dutyPersonShiftMap.get(flag);
		if(shiftVo!=null&&!"rest".equals(shiftVo.getShiftType())){
			Date startDate=DateFormatUtil.parseDate(checkDateStr+" "+shiftVo.getStartTime(), "yyyy-MM-dd HHmm");
			Date endDate=DateFormatUtil.addDate(startDate, "H", shiftVo.getLongTime());
			if(Math.abs(DateFormatUtil.dateDiff("mm", dateToCompare, startDate))<=validMin||
					Math.abs(DateFormatUtil.dateDiff("mm", dateToCompare, endDate))<=validMin){
				bean.setOprStartTime(shiftVo.getStartTime());
				bean.setOprLongTime(shiftVo.getLongTime());
				bean.setOprDutyDate(checkDateStr);
				bean.setOprScheduleId(shiftVo.getScheduleId());
				return true;
			}
		}
		return false;
    }

}
