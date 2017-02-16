package com.timss.attendance.service.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.LeaveItemBean;
import com.timss.attendance.bean.WorkStatusBean;
import com.timss.attendance.dao.WorkStatusDao;
import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.service.CardDataService;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.ExemptService;
import com.timss.attendance.service.LeaveService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.vo.LeaveContainItemVo;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.manager.support.IDateManager;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 打卡管理
 * @author: yyn
 * @createDate: 2016年4月5日
 * @version: 1.0
 */
@Service("workStatusService")
public class WorkStatusServiceImpl implements WorkStatusService {

    private Logger log = Logger.getLogger( WorkStatusServiceImpl.class ); 
    
    @Autowired
    private WorkStatusDao workStatusDao;
    
    @Autowired
    private LeaveService leaveService;
    
    @Autowired
    private DutyService dutyService;
    
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
    @Autowired
    private ExemptService exemptService;
    
    @Autowired
    private AbnormityService abnormityService;
    
    @Autowired
    private CardDataService cardDataService;
    
    @Autowired
    private DefinitionService definitionService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private ISecurityMaintenanceManager iSecurityMaintenanceManager;
    
    @Autowired
    private AtdUserPrivUtil privUtil;
    
    @Autowired
    private IDateManager iDateManager;
    
    @Transactional(propagation=Propagation.REQUIRED)
    private Integer insertWorkStatusList(List<WorkStatusBean>insertData){
    	Integer num=0;//计算实际插入条数
        if( insertData != null && insertData.size() > 0 ){
        	//数据量过大，考虑分批插入
        	int count=0;//统计批量插入条数
        	int batchNum=100;//批量插入的条数
        	int insertIndex=0;//开始插入的序号
        	while ((insertIndex+batchNum)<=insertData.size()) {
        		count = batchInsertWorkStatus( insertData.subList(insertIndex, insertIndex+batchNum) );
                log.info( "插入条数 ：" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<insertData.size()){
        		count = batchInsertWorkStatus( insertData.subList(insertIndex, insertData.size()) );
                log.info( "插入条数 ：" + count );
                num+=count;
        	}
        }
        log.info( "插入完成，共插入条数：" + num );
        return num;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    private Integer batchInsertWorkStatus(List<WorkStatusBean> list){
    	int num=0;//插入条数
    	try {
    		//尝试批量插入，如果出错则单条插入
			num=workStatusDao.insertBatchWorkStatus(list);
		} catch (Exception e) {
			log.error("批量插入考勤数据失败---->",e);
			for (WorkStatusBean bean : list) {
				try {
					num+=workStatusDao.insertWorkStatus(bean);
				} catch (Exception e2) {
					log.error("单条插入考勤数据失败---->"+bean.getWorkDate()+" "+bean.getUserId()+" "+bean.getSiteId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
    
    @Transactional(propagation=Propagation.REQUIRED)
    private Integer updateWorkStatusList(List<WorkStatusBean>list){
    	Integer num=0;//计算实际更新条数
        if( list != null && list.size() > 0 ){
        	//数据量过大，考虑分批更新
        	int count=0;//统计批量更新条数
        	int batchNum=100;//批量更新的条数
        	int insertIndex=0;//开始更新的序号
        	while ((insertIndex+batchNum)<=list.size()) {
        		count = batchUpdateWorkStatus( list.subList(insertIndex, insertIndex+batchNum) );
                log.info( "更新条数 ：" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<list.size()){
        		count = batchUpdateWorkStatus( list.subList(insertIndex, list.size()) );
                log.info( "更新条数 ：" + count );
                num+=count;
        	}
        }
        log.info( "更新完成，共更新条数：" + num );
        return num;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    private Integer batchUpdateWorkStatus(List<WorkStatusBean> list){
    	int num=0;//更新条数
    	try {
    		//尝试批量更新，如果出错则单条更新
			num=workStatusDao.updateBatchWorkStatus(list);
			//批量update不会有返回值
			num=list.size();
		} catch (Exception e) {
			log.error("批量更新考勤数据失败---->",e);
			for (WorkStatusBean bean : list) {
				try {
					num+=workStatusDao.updateWorkStatus(bean);
				} catch (Exception e2) {
					log.error("单条更新考勤数据失败---->"+bean.getWorkDate()+" "+bean.getUserId()+" "+bean.getSiteId(),e2);
				}
			}
		}
        return num<0?0:num;
    }
    
    @Transactional(propagation=Propagation.REQUIRED)
    private Integer deleteWorkStatusList(List<WorkStatusBean>list){
    	Integer num=0;//计算实际删除条数
        if( list != null && list.size() > 0 ){
        	//数据量过大，考虑分批删除
        	int count=0;//统计批量删除条数
        	int batchNum=100;//批量删除的条数
        	int insertIndex=0;//开始删除的序号
        	while ((insertIndex+batchNum)<=list.size()) {
        		count = batchDeleteWorkStatus( list.subList(insertIndex, insertIndex+batchNum) );
                log.info( "删除条数 ：" + count );
                num+=count;
        		insertIndex+=batchNum;
			}
        	if(insertIndex<list.size()){
        		count = batchDeleteWorkStatus( list.subList(insertIndex, list.size()) );
                log.info( "删除条数 ：" + count );
                num+=count;
        	}
        }
        log.info( "删除完成，共删除条数：" + num );
        return num;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    private Integer batchDeleteWorkStatus(List<WorkStatusBean> list){
    	int num=0;//删除条数
    	try {
    		//尝试批量删除，如果出错则单条删除
			num=workStatusDao.deleteBatchWorkStatus(list);
		} catch (Exception e) {
			log.error("批量删除考勤数据失败---->",e);
			for (WorkStatusBean bean : list) {
				try {
					num+=workStatusDao.deleteWorkstatus(bean);
				} catch (Exception e2) {
					log.error("单条删除考勤数据失败---->"+bean.getWorkDate()+" "+bean.getUserId()+" "+bean.getSiteId(),e2);
				}
			}
		}
        return num<0?0:num;
    }

    /**
     * 
     * @description:出勤情况列表--高级查询
     * @author: fengzt
     * @createDate: 2015年6月8日
     * @param map
     * @param pageVo
     * @return:List<WorkStatusBean>
     * @update:yyn 160201 优化和特殊处理
     */
    @Override
    public List<WorkStatusBean> queryWorkStatusListBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
        String sort = (String) pageVo.getParams().get( "sort" );
        String order = (String) pageVo.getParams().get( "order" );
        if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
            pageVo.setSortKey( sort );
            pageVo.setSortOrder( order );
        }else{
            pageVo.setSortKey( "workdate desc, username" );
            pageVo.setSortOrder( "asc" );
        }
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        String siteId = userInfo.getSiteId();
        pageVo.setParameter( "siteId", siteId );
        String currentDate = DateFormatUtil.formatDate( DateFormatUtil.addDate(new Date(), "d", -1), "yyyy-MM-dd" );//仅显示昨天及之前的
        pageVo.setParameter( "currentDate", currentDate );
        
        List<String>checkInvalidSqlList=new ArrayList<String>();//存放sql片段最后拼sql
        String checkExclude=pageVo.getParams().get("checkExclude").toString();//检查例外的请求
        Boolean isOpr="Y".equals(pageVo.getParams().get("isOpr").toString());
        String checkInvalid=pageVo.getParams().get("checkInvalid").toString();//检查异常的请求
        String[] checkType=isOpr?new String[]{"mornStart","mornEnd"}:new String[]{"mornStart","mornEnd","noonStart","noonEnd"};
        if(checkInvalid!=null&&!"no".equals(checkInvalid)){//非查询全部
        	if("all".equals(checkInvalid)){//查询所有异常
        		String checkInvalidStr="('valid','rest')";
        		for (String check : checkType) {
        			if(!checkExclude.contains(check)){//非检查例外
        				checkInvalidSqlList.add(check+"CheckType not in "+checkInvalidStr);
        			}
        		}
        	}else if("invalid,overtime,leave,abnormity,late,early,".contains(checkInvalid+",")){//查询某类异常
        		for (String check : checkType) {
        			if(!checkExclude.contains(check)){//非检查例外
        				checkInvalidSqlList.add(check+"CheckType='"+checkInvalid+"'");
        			}
        		}
        	}
        }
        String checkInvalidSql="";//拼接的sql片段
        for (int i = 0; i < checkInvalidSqlList.size(); i++) {
			if(i==0)checkInvalidSql+="( ";
			checkInvalidSql+=checkInvalidSqlList.get(i);
			if(i==checkInvalidSqlList.size()-1){
				checkInvalidSql+=" )";
			}else{
				checkInvalidSql+=" or ";
			}
		}
        pageVo.setParameter( "checkInvalidSql", checkInvalidSql );
        if(map!=null&&map.size()>0){
			if(map.containsKey("deptName")){
				if(map.get("deptName")!=null && StringUtil.isNotBlank(map.get("deptName").toString())){
					String deptName = map.get("deptName").toString();
					pageVo.setParameter("deptName", deptName);
					pageVo.setParameter("siteId", siteId);
				}
				//将deptName设为null(890199 2016/11/8)
				map.remove("deptName");
        	}
            pageVo.setFuzzyParams(map);
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
        return  workStatusDao.queryWorkStatusListBySearch( pageVo );
    }

    @Override
	public String checkWorkStatusByCardData(CardDataBean cardData,
			DefinitionBean definitionBean) {
		if(definitionBean==null){
			definitionBean = definitionService.queryDefinitionBySiteId(cardData.getSiteId());
		}
		
		//获得并转化要校验的时间
		Date checkDate = DateFormatUtil.parseDate(cardData.getCheckDate().substring( 0, 16 ), "yyyy-MM-dd HH:mm");
		//获得可用打卡结果的map，用于判断打卡结果是否可用
		List<AppEnum> enumList = itcMvcService.getEnum("ATD_MACHINE_WORK_STATUS");
		Map<String, AppEnum>enumMap=new HashMap<String, AppEnum>();
		if(enumList!=null){
			for (AppEnum appEnum : enumList) {
				enumMap.put(appEnum.getCode(), appEnum);
			}
		}
		
        String result=null;
        //运行人员
        if(cardData.getIsOpr()){
        	if(cardData.getOprDutyDate()!=null){//有排班
        		//取校验的日期，截取yyyy-MM-dd
                String dateCheck = cardData.getOprDutyDate().substring( 0, 10 );
            	String start=cardData.getOprStartTime();
            	if(start!=null){
            		//上班打卡
            		Date startDate = DateFormatUtil.parseDate( dateCheck + " " + start, "yyyy-MM-dd HHmm" ); 
                	result=checkCardDataByTime(checkDate,startDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"opr","startWork",enumMap);
                	if(result==null){
                		//下班打卡
                		Date endDate = DateFormatUtil.addDate(startDate, "H", cardData.getOprLongTime()); 
                    	result=checkCardDataByTime(checkDate,endDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"opr","endWork",enumMap);
                	}
            	}
        	}
        }else{
        	//取校验的日期，截取yyyy-MM-dd
            String dateCheck = cardData.getCheckDate().substring( 0, 10 );
        	//依次判断上午上班，上午下班，下午上班，下午下班
            if(result==null){
            	String morning = definitionBean.getForeStartDate();
            	Date morningstartDate = DateFormatUtil.parseDate( dateCheck + " " + morning, "yyyy-MM-dd HHmm" ); 
            	result=checkCardDataByTime(checkDate,morningstartDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"am","startWork",enumMap);
            }
            if(result==null){
            	String forenoon = definitionBean.getForeEndDate();
            	Date morningEndDate = DateFormatUtil.parseDate( dateCheck + " " + forenoon, "yyyy-MM-dd HHmm" ); 
            	result=checkCardDataByTime(checkDate,morningEndDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"am","endWork",enumMap);
            }
            if(result==null){
            	String noon = definitionBean.getAfterStartDate();
            	Date afternoonStartDate = DateFormatUtil.parseDate( dateCheck + " " + noon, "yyyy-MM-dd HHmm" ); 
            	result=checkCardDataByTime(checkDate,afternoonStartDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"pm","startWork",enumMap);
            }
            if(result==null){
            	String afternoon = definitionBean.getAfterEndDate();
            	Date afternoonEndDate = DateFormatUtil.parseDate( dateCheck + " " + afternoon, "yyyy-MM-dd HHmm" );
            	result=checkCardDataByTime(checkDate,afternoonEndDate,definitionBean.getValidMin(),definitionBean.getToleranceMin(),"pm","endWork",enumMap);
            }
        }
        
        if(result==null){//如果没有结果，那就是无效打卡
        	result="invalid";
        }

		return result;
	}
	
	/**
	 * 判断一个打卡时间是否在指定时间内
	 * @param checkDate 打卡时间
	 * @param workDate 要校验的时间
	 * @param validMin 有效打卡时间
	 * @param toleranceMin 宽容时间
	 * @param time 上午(am)还是下午(pm)
	 * @param work 上班(startWork)还是下班(endWork)，判断是迟到还是早退
	 * @param enumMap 可用的打卡结果的枚举
	 * @return
	 */
	private String checkCardDataByTime(Date checkDate,Date workDate,Integer validMin,Integer toleranceMin,String time,String work,Map<String, AppEnum>enumMap){
		double mmDiff = DateFormatUtil.dateDiff( "mm", checkDate, workDate );
		double absMmDiff=Math.abs( mmDiff );
		//先判断是否有效
		if( absMmDiff>validMin){//打卡点和校验点的分钟差超过有效打卡时间分钟数，则返回无效打卡
			return null;
		}
		//判断迟到还是早退
		if(mmDiff>=0){//校验点之前打卡，可能是早退，否则是正常
			if("endWork".equals(work)&&absMmDiff>toleranceMin){//下班且提前打卡时间超过宽容时间，则是早退
				String result=time+"_"+work+"_early";
				return enumMap.get(result)==null?null:result;//检验站点有该打卡结果才是有效打卡
			}
		}else{//校验点之后打卡，可能是迟到，否则是正常
			if("startWork".equals(work)&&absMmDiff>toleranceMin){//上班且延后打卡时间超过宽容时间，则是迟到
				String result=time+"_"+work+"_late";
				return enumMap.get(result)==null?null:result;//检验站点有该打卡结果才是有效打卡
			}
		}
		String result=time+"_"+work+"_valid";
		return enumMap.get(result)==null?null:result;//检验站点有该打卡结果才是有效打卡
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer deleteWorkStatusByEndTime(String endTimeStr, String siteId)
			throws Exception {
		Integer result=workStatusDao.deleteWorkStatusByEndTime(endTimeStr,siteId);
		return result;
	}
	
	@Override
	public void checkWorkStatus(String siteId) throws Exception{
		//获取需要更新考勤结果的站点
		List<DefinitionBean>definitionBeanList=definitionService.queryCheckWorkstatusDefinition(siteId);
		if(definitionBeanList!=null){
			log.info("size of site to check work status:"+definitionBeanList.size());
			
			for (DefinitionBean definitionBean : definitionBeanList) {
				checkSiteWorkStatus(definitionBean,null,null,true);
			}
		}
		log.info("check work status finish");
	}
	
	@Override
	public void checkCardDataWorkStatus(String siteId,String startTimeStr,String endTimeStr)throws Exception{
		List<DefinitionBean>definitionBeanList=definitionService.queryCheckWorkstatusDefinition(siteId);
		if(definitionBeanList!=null){
			log.info("checkCardDataWorkStatus "+siteId+":"+startTimeStr+" - "+endTimeStr);
			Date startDate=DateFormatUtil.getFormatDate(DateFormatUtil.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd");
			Date endDate=DateFormatUtil.getFormatDate(DateFormatUtil.parseDate(endTimeStr, "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd");
    		Date prevDate=DateFormatUtil.addDate(startDate, "d", -1);//开始时间的前一天
    		Date nextDate=DateFormatUtil.addDate(endDate, "d", 1);//结束时间的后一天
    		Date nowDate=DateFormatUtil.getCurrentDate();//今天
			for (DefinitionBean definitionBean : definitionBeanList) {
				//打卡记录可能会影响前一天和后一天的打卡统计
				//只更新到今天
				checkSiteWorkStatus(definitionBean,prevDate,DateFormatUtil.compareDate(nextDate, nowDate)?nowDate:nextDate,false);
			}
		}
		log.info("finish checkCardDataWorkStatus "+siteId+":"+startTimeStr+" - "+endTimeStr);
	}
	
	@Override
	public void checkPersonsWorkStatus(String siteId,Date startDate,Date endDate,Set<String>userIdSet)throws Exception{
		String dateFormat="yyyy-MM-dd";
		String startDateStr=DateFormatUtil.dateToString(startDate,dateFormat);
		String endDateStr=DateFormatUtil.dateToString(endDate,dateFormat);
		log.info("start checkPersonsWorkStatus->startDate:"+startDateStr+" endDate:"+endDateStr+
				" userIdSet:"+(userIdSet==null?"null":userIdSet.toString()));
		if(userIdSet!=null&&userIdSet.size()>0){
	        Date nowDate=DateFormatUtil.getFormatDate(new Date(),dateFormat);
	        //可能会影响开始时间的前一天（跨天结束的班次属于前一天）和结束时间之间的workstatus，因此需要更新这些workstatus
    		Date prevDate=DateFormatUtil.getFormatDate(DateFormatUtil.addDate(startDate, "d", -1),dateFormat);//开始时间的前一天
    		//查询开始时间的前一天和结束时间（如果在今天之后，则是今天）之间的workstatus
        	if(DateFormatUtil.compareDate(nowDate,prevDate)){
        		List<WorkStatusBean>addList=new ArrayList<WorkStatusBean>();//存储最后要新建的结果
    	        List<WorkStatusBean>updateList=new ArrayList<WorkStatusBean>();//存储最后要更新的结果
    	        String oprPersons=dutyService.queryOprPersonsBySite(siteId);
    	        DefinitionBean definitionBean=null;
    	        List<DefinitionBean>definitionBeanList=definitionService.queryCheckWorkstatusDefinition(siteId);
		        if(definitionBeanList!=null&&definitionBeanList.size()>0){
		        	definitionBean=definitionBeanList.get(0);
		        }	
		        if(definitionBean==null){
		        	log.error("find no definitionBean for siteId:"+siteId);
		        }else{
		        	for(String userId:userIdSet){
	    	        	SecureUser user=iSecurityMaintenanceManager.retrieveUserWithDetails(userId,privUtil.getUserInfoScope().getSecureUser());

    		        	Organization org=new Organization();
    		        	if(user.getOrganizations()!=null&&user.getOrganizations().size()>0){
    		        		org=user.getOrganizations().get(0);
    		        	}
    		        	checkPersonWorkStatus(prevDate,
                				//只处理今天和今天之前的，明天及之后的由定时任务做
                				DateFormatUtil.compareDate(endDate, nowDate)?nowDate:endDate,
                				definitionBean,addList,updateList,
                				userId,user.getName(),org.getCode(),org.getName(),siteId,oprPersons);
	    		        
	    	        }
		        }
    	        
    	        //更新数据
    	        Integer insertNum=insertWorkStatusList(addList);
    	        Integer updateNum=updateWorkStatusList(updateList);
    	        log.info("finish checkPersonsWorkStatus->startDate:"+startDateStr+" endDate:"+endDateStr+
    	        		" add:"+insertNum+"/"+addList.size()+" update:"+updateNum+"/"+updateList.size());
        	}
		}
	}
	
	@Override
	public void checkLeaveWorkStatus(LeaveBean leave,DefinitionBean definitionBean)throws Exception{
		List<LeaveItemBean>itemList=leave.getItemList();
		if(itemList!=null&&itemList.size()>0){
			List<WorkStatusBean>addList=new ArrayList<WorkStatusBean>();//存储最后要新建的结果
	        List<WorkStatusBean>updateList=new ArrayList<WorkStatusBean>();//存储最后要更新的结果
	        Date now=new Date();
	        String dateFormat="yyyy-MM-dd";
	        String siteId=definitionBean.getSiteId();
	        String oprPersons=dutyService.queryOprPersonsBySite(siteId);
	        for(LeaveItemBean item:itemList){
	        	//每个申请项可能会影响开始时间的前一天（跨天结束的班次属于前一天）和结束时间之间的workstatus，因此需要更新这些workstatus
        		Date prevDate=DateFormatUtil.addDate(item.getStartDate(), "d", -1);//开始时间的前一天
	        	//查询每个请假项的开始时间的前一天和结束时间（如果在今天之后，则是今天）之间的workstatus
	        	if(DateFormatUtil.compareDate(DateFormatUtil.getFormatDate(now, dateFormat), DateFormatUtil.getFormatDate(prevDate, dateFormat))){
	        		checkPersonWorkStatus(prevDate,
	        				//只处理今天和今天之前的，明天及之后的由定时任务做
	        				DateFormatUtil.compareDate(item.getEndDate(), now)?now:item.getEndDate(),
	        				definitionBean,addList,updateList,
	        				leave.getCreateBy(),leave.getUserName(),leave.getDeptId(),leave.getDeptName(),siteId,oprPersons);
	        	}
	        }
	        
	        //更新数据
	        Integer insertNum=insertWorkStatusList(addList);
	        Integer updateNum=updateWorkStatusList(updateList);
	        log.info("finish check work status of leave:"+leave.getId()+
	        		" --------add:"+insertNum+"/"+addList.size()+" update:"+updateNum+"/"+updateList.size());     
		}
	}
	
	@Override
	public void checkAbnormityWorkStatus(AbnormityBean abnormity,DefinitionBean definitionBean)throws Exception{
		if(abnormity!=null){
			List<WorkStatusBean>addList=new ArrayList<WorkStatusBean>();//存储最后要新建的结果
	        List<WorkStatusBean>updateList=new ArrayList<WorkStatusBean>();//存储最后要更新的结果
	        Date now=new Date();
	        String dateFormat="yyyy-MM-dd";
	        String siteId=definitionBean.getSiteId();
	        String oprPersons=dutyService.queryOprPersonsBySite(siteId);
	        if(abnormity.getItemList()!=null&&abnormity.getItemList().size()>0){
	        	for(AbnormityItemBean item:abnormity.getItemList()){
	        		//每个申请项可能会影响开始时间的前一天（跨天结束的班次属于前一天）和结束时间之间的workstatus，因此需要更新这些workstatus
	        		Date prevDate=DateFormatUtil.addDate(item.getStartDate(), "d", -1);//开始时间的前一天
	    	        //查询更新每个申请项的开始时间的前一天和结束时间（如果在今天之后，则是今天）之间的workstatus
	            	if(DateFormatUtil.compareDate(DateFormatUtil.getFormatDate(now, dateFormat), DateFormatUtil.getFormatDate(prevDate, dateFormat))){
	            		checkPersonWorkStatus(prevDate,
	            				//只处理今天和今天之前的，明天及之后的由定时任务做
	            				DateFormatUtil.compareDate(item.getEndDate(), now)?now:item.getEndDate(),
	            				definitionBean,addList,updateList,
	            				item.getUserId(),item.getUserName(),item.getDeptId(),item.getDeptName(),siteId,oprPersons);
	            	}
	        	}
	        }
	        
	        //更新数据
	        Integer insertNum=insertWorkStatusList(addList);
	        Integer updateNum=updateWorkStatusList(updateList);
	        log.info("finish check work status of abnormity:"+abnormity.getId()+
	        		" --------add:"+insertNum+"/"+addList.size()+" update:"+updateNum+"/"+updateList.size()); 
		}
	}
	
	private void checkPersonWorkStatus(Date startDate,Date endDate,DefinitionBean definitionBean,
			List<WorkStatusBean>addList,List<WorkStatusBean>updateList,
			String userId,String userName,String deptId,String deptName,String siteId,String oprPersons)throws Exception{
		//结束时间
		endDate=DateFormatUtil.getFormatDate(endDate,"yyyy-MM-dd");
		String endDateStr=DateFormatUtil.dateToString(endDate,"yyyy-MM-dd");
		String endTimeStr=endDateStr+" 23:59";
		//开始时间
		startDate=DateFormatUtil.getFormatDate(startDate,"yyyy-MM-dd");
		String startDateStr=DateFormatUtil.dateToString(startDate,"yyyy-MM-dd");
		String startTimeStr=startDateStr+" 00:00";
		//开始时间前一天
		Date prevDate=DateFormatUtil.addDate(startDate, "d", -1);
		String prevDateStr=DateFormatUtil.dateToString(prevDate, "yyyy-MM-dd");
		String prevTimeStr=prevDateStr+" 00:00";
		//结束时间后一天
		Date nextDate=DateFormatUtil.addDate(endDate, "d", 1);
		String nextDateStr=DateFormatUtil.dateToString(nextDate, "yyyy-MM-dd");
		String nextTimeStr=nextDateStr+" 23:59";
		
		log.info("start check work status of person:"+siteId+" "+userId+" start:"+startDateStr+" end:"+endDateStr);
		
		Boolean isOpr=checkIsOpr(userId,oprPersons);
		String isOprStr=isOpr?"Y":"N";
		Map<String, DutyPersonShiftVo>dutyPersonShiftMap=null;//人员的排班情况
		if(isOpr){
			//dutyPersonShiftMap=dutyService.queryDutyPersonAndShiftBySiteAndTime(siteId,userId, prevDateStr, nextDateStr);
			dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(siteId,new String[]{userId}, prevDateStr, nextDateStr);
		}
		
		//已有的考勤结果记录
		Map<String,WorkStatusBean>existWorkStatus=queryExistWorkStatusBySiteAndTime(siteId,userId,startDateStr,endDateStr);
		//找出会影响到startDate和endDate直接的请假（后一天）、考勤异常（后一天）、打卡（前一天和后一天）
		//请假申请记录
		Map<String,WorkStatusBean>leaveWorkStatus=queryLeaveWorkStatusBySiteAndTime(siteId,userId,startTimeStr,nextTimeStr,definitionBean,oprPersons,dutyPersonShiftMap);
		//考勤异常申请记录
		Map<String,WorkStatusBean>abnormityWorkStatus=queryAbnormityWorkStatusBySiteAndTime(siteId,userId,startTimeStr,nextTimeStr,definitionBean,oprPersons,dutyPersonShiftMap);
		//打卡记录
		Map<String,WorkStatusBean>carddataWorkStatus=queryCardDataWorkStatusBySiteAndTime(siteId,userId,prevTimeStr,nextTimeStr,definitionBean);
		
		Date checkDate=startDate;
    	//循环开始到结束的每个日期
    	while(DateFormatUtil.compareDate(endDate, checkDate)){
    		String checkDateStr=DateFormatUtil.dateToString(checkDate,"yyyy-MM-dd");
    		Boolean toUpdate=false;//是否更新的标志位
    		Boolean toAdd=false;//是否插入的标志位
    		String flag=checkDateStr+"_"+userId;//获取数据的标识，日期_用户id，用于map的key
    		
    		//获取已有的workstatus，否则就新建
    		WorkStatusBean workStatus=existWorkStatus==null?null:existWorkStatus.get(flag);
    		if(workStatus==null){
    			workStatus=new WorkStatusBean();
    			workStatus.setId(UUIDGenerator.getUUID());
    			workStatus.setWorkDate(checkDateStr);
    			workStatus.setUserId(userId);
    			workStatus.setUserName(userName);
    			workStatus.setSiteId(siteId);
    			workStatus.setDeptId(deptId);
    			workStatus.setDeptName(deptName);
    			toAdd=true;
    		}else{
    			existWorkStatus.remove(flag);//最后剩下的表示不需考勤管理，要删除
    		}
    		
    		//设置是否运行人员
    		if(!isOprStr.equals(workStatus.getIsOpr())){
    			workStatus.setIsOpr(isOprStr);
    			toUpdate=true;
    		}
    		//设置是否工作
    		Boolean isWorkDay=true;
    		if(isOpr){//运行人员判断是否排班
    			if(dutyPersonShiftMap.get(flag)==null||"rest".equals(dutyPersonShiftMap.get(flag).getShiftType())){
    				isWorkDay=false;
    			}
    			if(dutyPersonShiftMap.get(flag)!=null){//加入排班id
    				Integer oprScheduleId=dutyPersonShiftMap.get(flag).getScheduleId();
    				if((oprScheduleId!=null&&oprScheduleId!=workStatus.getOprScheduleId())||(workStatus.getOprScheduleId()!=null&&workStatus.getOprScheduleId()!=oprScheduleId)){
    					workStatus.setOprScheduleId(oprScheduleId);
    					toUpdate=true;
    				}
    			}
    		}else{
    			isWorkDay=iDateManager.checkIsWorkDate(checkDate,siteId);
    		}
    		String isWorkDayStr=isWorkDay?"1":"0";
    		if(!isWorkDayStr.equals(workStatus.getStatus())){
    			workStatus.setStatus(isWorkDayStr);
    			toUpdate=true;
    		}
    		
    		
    		//检查并更新上下班的考勤结果
    		if(checkToUpdateWorkStatus(workStatus, leaveWorkStatus.get(flag), abnormityWorkStatus.get(flag), carddataWorkStatus.get(flag))){
    			toUpdate=true;
    		}
    		
    		//处理结果
    		Date date=new Date();
    		if(toAdd){
    			workStatus.setCreateDate(date);
    			addList.add(workStatus);
    		}else if(toUpdate){
    			workStatus.setUpdateDate(date);
    			updateList.add(workStatus);
    		}
    		
    		checkDate=DateFormatUtil.addDate(checkDate, "d", 1);
    	}
    	
    	log.info("finish check work status of person:"+siteId+" "+userId+" start:"+startDateStr+" end:"+endDateStr+
        		" --------add:"+addList.size()+" update:"+updateList.size()+" delete:"+existWorkStatus.size());
	}
	
	/**
	 * 更新站点的打卡统计
	 * @param definitionBean
	 * @param startDate yyyy-MM-dd格式，为空则获取规则中设定的更新时间戳，仍为空则取endDate往前30天
	 * @param endDate yyyy-MM-dd格式，为空则获取当前时间
	 * @param isChangeTimestamp 是否更新打卡管理开始日期（用于定时任务，定时任务要更新，打卡记录触发不更新）
	 * @throws Exception
	 */
	private void checkSiteWorkStatus(DefinitionBean definitionBean,Date startDate,Date endDate,Boolean isChangeTimestamp)throws Exception{
		//结束时间
		if(endDate==null)
			endDate=DateFormatUtil.getFormatDate(new Date(),"yyyy-MM-dd");
		String endDateStr=DateFormatUtil.dateToString(endDate,"yyyy-MM-dd");
		String endTimeStr=endDateStr+" 23:59";
		//开始时间
		if(startDate==null)
			startDate=DateFormatUtil.getFormatDate(definitionBean.getLastCheck(),"yyyy-MM-dd");
		if(startDate==null){//默认30天前
			startDate=DateFormatUtil.addDate(endDate, "d", -30);
		}
		String startDateStr=DateFormatUtil.dateToString(startDate,"yyyy-MM-dd");
		String startTimeStr=startDateStr+" 00:00";
		//开始时间前一天
		Date prevDate=DateFormatUtil.addDate(startDate, "d", -1);
		String prevDateStr=DateFormatUtil.dateToString(prevDate, "yyyy-MM-dd");
		String prevTimeStr=prevDateStr+" 00:00";
		//结束时间后一天
		Date nextDate=DateFormatUtil.addDate(endDate, "d", 1);
		String nextDateStr=DateFormatUtil.dateToString(nextDate, "yyyy-MM-dd");
		String nextTimeStr=nextDateStr+" 23:59";
		
	    String siteId=definitionBean.getSiteId();
		log.info("start check work status of site:"+siteId+" start:"+startDateStr+" end:"+endDateStr);
		
		//初始化人员值别map和人员排班map
		String oprPersons=dutyService.queryOprPersonsBySite(siteId);
		Map<String, DutyPersonShiftVo>dutyPersonShiftMap;//人员的排班情况
		//dutyPersonShiftMap=dutyService.queryDutyPersonAndShiftBySiteAndTime(siteId,null, prevDateStr, nextDateStr);
		dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(siteId,null, prevDateStr, nextDateStr);
				
		//已有的考勤结果记录
		Map<String,WorkStatusBean>existWorkStatus=queryExistWorkStatusBySiteAndTime(siteId,null,startDateStr,endDateStr);
		//找出会影响到startDate和endDate直接的请假（后一天）、考勤异常（后一天）、打卡（前一天和后一天）
		//请假申请记录
		Map<String,WorkStatusBean>leaveWorkStatus=queryLeaveWorkStatusBySiteAndTime(siteId,null,startTimeStr,nextTimeStr,definitionBean,oprPersons,dutyPersonShiftMap);
		//考勤异常申请记录
		Map<String,WorkStatusBean>abnormityWorkStatus=queryAbnormityWorkStatusBySiteAndTime(siteId,null,startTimeStr,nextTimeStr,definitionBean,oprPersons,dutyPersonShiftMap);
		//打卡记录
		Map<String,WorkStatusBean>carddataWorkStatus=queryCardDataWorkStatusBySiteAndTime(siteId,null,prevTimeStr,nextTimeStr,definitionBean);
		
		//查询站点人员
		//站点下所有用户信息
        Page<SecureUser>pageVo=new Page<SecureUser>(1,99999);
        pageVo.setParameter("userStatus","Y");
        pageVo.setParameter("site",siteId);
        SecureUser tmpUser=new SecureUser();
        tmpUser.setCurrentSite(siteId);
        tmpUser.setId("blankUser");
        Page<SecureUser>userList=iSecurityMaintenanceManager.retrieveUniqueUsers(pageVo,tmpUser);
        List<SecureUser>personList=userList.getResults();
        
        List<WorkStatusBean>addList=new ArrayList<WorkStatusBean>();//存储最后要新建的结果
        List<WorkStatusBean>updateList=new ArrayList<WorkStatusBean>();//存储最后要更新的结果
        
        for(SecureUser user:personList){
        	String userId=user.getId();
        	
        	//判断是否考勤例外
        	//修改成考勤例外也产生数据
        	/*if(checkIsAtdExclude(user)){//考勤例外的数据在existWorkStatus剩下，最后会被清除
        		continue;
        	}*/
        	
        	Date checkDate=startDate;
        	//循环开始到结束的每个日期
        	while(DateFormatUtil.compareDate(endDate, checkDate)){
        		String checkDateStr=DateFormatUtil.dateToString(checkDate,"yyyy-MM-dd");
        		Boolean toUpdate=false;//是否更新的标志位
        		Boolean toAdd=false;//是否插入的标志位
        		String flag=checkDateStr+"_"+userId;//获取数据的标识，日期_用户id，用于map的key
        		//debug here
            	if("2016-07-17_880034".equals(flag)){
            		System.out.println("aa");
            	}
        		//获取已有的workstatus，否则就新建
        		WorkStatusBean workStatus=existWorkStatus==null?null:existWorkStatus.get(flag);
        		if(workStatus==null){
        			workStatus=new WorkStatusBean();
        			workStatus.setId(UUIDGenerator.getUUID());
        			workStatus.setWorkDate(checkDateStr);
        			workStatus.setUserId(userId);
        			workStatus.setUserName(user.getName());
        			workStatus.setSiteId(siteId);
        			workStatus.setDeptId(user.getCurrOrgCode());
        			workStatus.setDeptName(user.getCurrOrgName());
        			toAdd=true;
        		}else{
        			existWorkStatus.remove(flag);//最后剩下的表示不需考勤管理，要删除
        		}
        		
        		//设置是否运行人员
        		Boolean isOpr=checkIsOpr(userId,oprPersons);
        		String isOprStr=isOpr?"Y":"N";
        		if(!isOprStr.equals(workStatus.getIsOpr())){
        			workStatus.setIsOpr(isOprStr);
        			toUpdate=true;
        		}
        		
        		//设置是否工作
        		Boolean isWorkDay=true;
        		if(isOpr){//运行人员判断是否排班
        			if(dutyPersonShiftMap.get(flag)==null||"rest".equals(dutyPersonShiftMap.get(flag).getShiftType())){//判断是否休息的班次
        				isWorkDay=false;
        			}
        			if(dutyPersonShiftMap.get(flag)!=null){//加入排班id
        				Integer oprScheduleId=dutyPersonShiftMap.get(flag).getScheduleId();
        				if((oprScheduleId!=null&&oprScheduleId!=workStatus.getOprScheduleId())||(workStatus.getOprScheduleId()!=null&&workStatus.getOprScheduleId()!=oprScheduleId)){
        					workStatus.setOprScheduleId(oprScheduleId);
        					toUpdate=true;
        				}
        			}
        		}else{
        			isWorkDay=iDateManager.checkIsWorkDate(checkDate,siteId);
        		}
        		String isWorkDayStr=isWorkDay?"1":"0";
        		if(!isWorkDayStr.equals(workStatus.getStatus())){
        			workStatus.setStatus(isWorkDayStr);
        			toUpdate=true;
        		}
        		
        		//检查并更新上下班的考勤结果
        		if(checkToUpdateWorkStatus(workStatus, leaveWorkStatus.get(flag), abnormityWorkStatus.get(flag), carddataWorkStatus.get(flag))){
        			toUpdate=true;
        		}
        		
        		//处理结果
        		Date date=new Date();
        		if(toAdd){
        			workStatus.setCreateDate(date);
        			addList.add(workStatus);
        		}else if(toUpdate){
        			workStatus.setUpdateDate(date);
        			updateList.add(workStatus);
        		}
        		
        		checkDate=DateFormatUtil.addDate(checkDate, "d", 1);
        	}
        }
        
        //更新数据
        Integer insertNum=insertWorkStatusList(addList);
        Integer updateNum=updateWorkStatusList(updateList);
        List<WorkStatusBean>existWorkStatusList=new ArrayList<WorkStatusBean>();
        for (WorkStatusBean tmp : existWorkStatus.values()) {
        	existWorkStatusList.add(tmp);
		}
        Integer deleteNum=deleteWorkStatusList(existWorkStatusList);
        
        if(isChangeTimestamp){
        	//设置新的导入时间戳
            definitionBean.setLastCheck(endDate);
            definitionService.updateDefinitionLastCheck(definitionBean);
        }
        
        log.info("finish check work status of site:"+siteId+" start:"+startDateStr+" end:"+endDateStr+
        		" --------add:"+insertNum+"/"+addList.size()+" update:"+updateNum+"/"+updateList.size()+" delete:"+deleteNum+"/"+existWorkStatus.size());
    }
		
	private Boolean checkToUpdateWorkStatus(WorkStatusBean bean,WorkStatusBean leave,WorkStatusBean abnormity,WorkStatusBean cardData)throws Exception{
		Boolean isChanged=false;
		Boolean isWork="1".equals(bean.getStatus());//是否工作
		
		String[]fields=new String[]{"MornStart","MornEnd","NoonStart","NoonEnd"};
		for (String field : fields) {
			Method getCheckMethod = WorkStatusBean.class.getMethod( "get"+field+"Check", new Class[]{} );
			Method setCheckMethod = WorkStatusBean.class.getMethod( "set"+field+"Check", new Class[]{String.class} );
			Method getCheckTypeMethod = WorkStatusBean.class.getMethod( "get"+field+"CheckType", new Class[]{} );
			Method setCheckTypeMethod = WorkStatusBean.class.getMethod( "set"+field+"CheckType", new Class[]{String.class} );
			Method getCheckSrcMethod = WorkStatusBean.class.getMethod( "get"+field+"CheckSrc", new Class[]{} );
			Method setCheckSrcMethod = WorkStatusBean.class.getMethod( "set"+field+"CheckSrc", new Class[]{String.class} );
			Method getCheckTimeMethod = WorkStatusBean.class.getMethod( "get"+field+"Time", new Class[]{} );
			Method setCheckTimeMethod = WorkStatusBean.class.getMethod( "set"+field+"Time", new Class[]{String.class} );
			
			if(isWork&&leave!=null&&getCheckTypeMethod.invoke( leave, new Object[]{} )!=null){
				if(!getCheckMethod.invoke( leave, new Object[]{} ).equals(getCheckMethod.invoke( bean, new Object[]{} ))){
					setCheckMethod.invoke( bean, getCheckMethod.invoke( leave, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckTypeMethod.invoke( leave, new Object[]{} ).equals(getCheckTypeMethod.invoke( bean, new Object[]{} ))){
					setCheckTypeMethod.invoke( bean, getCheckTypeMethod.invoke( leave, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckSrcMethod.invoke( leave, new Object[]{} ).equals(getCheckSrcMethod.invoke( bean, new Object[]{} ))){
					setCheckSrcMethod.invoke( bean, getCheckSrcMethod.invoke( leave, new Object[]{} ) );
					isChanged=true;
				}
				if(getCheckTimeMethod.invoke( bean, new Object[]{} )!=null){
					setCheckTimeMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
			}else if(isWork&&abnormity!=null&&getCheckTypeMethod.invoke( abnormity, new Object[]{} )!=null){
				if(!getCheckMethod.invoke( abnormity, new Object[]{} ).equals(getCheckMethod.invoke( bean, new Object[]{} ))){
					setCheckMethod.invoke( bean, getCheckMethod.invoke( abnormity, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckTypeMethod.invoke( abnormity, new Object[]{} ).equals(getCheckTypeMethod.invoke( bean, new Object[]{} ))){
					setCheckTypeMethod.invoke( bean, getCheckTypeMethod.invoke( abnormity, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckSrcMethod.invoke( abnormity, new Object[]{} ).equals(getCheckSrcMethod.invoke( bean, new Object[]{} ))){
					setCheckSrcMethod.invoke( bean, getCheckSrcMethod.invoke( abnormity, new Object[]{} ) );
					isChanged=true;
				}
				if(getCheckTimeMethod.invoke( bean, new Object[]{} )!=null){
					setCheckTimeMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
			}else if(isWork&&cardData!=null&&getCheckTypeMethod.invoke( cardData, new Object[]{} )!=null){
				if(getCheckMethod.invoke( bean, new Object[]{} )!=null){
					setCheckMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
				if(!getCheckTypeMethod.invoke( cardData, new Object[]{} ).equals(getCheckTypeMethod.invoke( bean, new Object[]{} ))){
					setCheckTypeMethod.invoke( bean, getCheckTypeMethod.invoke( cardData, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckSrcMethod.invoke( cardData, new Object[]{} ).equals(getCheckSrcMethod.invoke( bean, new Object[]{} ))){
					setCheckSrcMethod.invoke( bean, getCheckSrcMethod.invoke( cardData, new Object[]{} ) );
					isChanged=true;
				}
				if(!getCheckTimeMethod.invoke( cardData, new Object[]{} ).equals(getCheckTimeMethod.invoke( bean, new Object[]{} ))){
					setCheckTimeMethod.invoke( bean, getCheckTimeMethod.invoke( cardData, new Object[]{} ) );
					isChanged=true;
				}
			}else{//清空掉之前的结果
				if(getCheckMethod.invoke( bean, new Object[]{} )!=null){
					setCheckMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
				String checkType=isWork?"invalid":"rest";
				if(!checkType.equals(getCheckTypeMethod.invoke( bean, new Object[]{} ))){
					setCheckTypeMethod.invoke( bean, checkType );
					isChanged=true;
				}
				if(getCheckSrcMethod.invoke( bean, new Object[]{} )!=null){
					setCheckSrcMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
				if(getCheckTimeMethod.invoke( bean, new Object[]{} )!=null){
					setCheckTimeMethod.invoke( bean, new Object[]{null} );
					isChanged=true;
				}
			}
		}
		
		return isChanged;
	}
	
	/**
	 * 查询指定时间和站点的已存在的考勤结果，以日期+"_"+用户id为key返回map
	 * @param siteId
	 * @param userId
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	private Map<String, WorkStatusBean>queryExistWorkStatusBySiteAndTime(String siteId,String userId,String startDateStr,String endDateStr){
		Map<String, WorkStatusBean>result=workStatusDao.queryExistWorkStatusBySiteAndTime(siteId,userId,startDateStr,endDateStr);
		return result;
	}
		
	/**
	 * 查询指定时间和站点的打卡记录，以日期+"_"+用户id为key返回map
	 * @param siteId
	 * @param userIdToQuery
	 * @param startTimeStr
	 * @param endTimeStr
	 * @param definitionBean
	 * @return
	 * @throws Exception
	 */
	private Map<String, WorkStatusBean>queryCardDataWorkStatusBySiteAndTime(String siteId,String userIdToQuery,String startTimeStr,String endTimeStr,
			DefinitionBean definitionBean) throws Exception{
		//查询有效的打卡记录
		List<CardDataBean> cardDataList = cardDataService.queryValidCardDataListByDiffDate( startTimeStr, endTimeStr, siteId,userIdToQuery );
        
        //处理每个请假项，按日期分割成workstatus
        Map<String, WorkStatusBean>result=new HashMap<String, WorkStatusBean>();
        for(CardDataBean cardData:cardDataList){
        	//运行人员取打卡所属班次的日期
    		String workDateStr=(cardData.getOprDutyDate()!=null?cardData.getOprDutyDate():cardData.getCheckDate()).substring(0, 10);
    		String userId=cardData.getUserId();
    		String flag=workDateStr+"_"+userId;
    		WorkStatusBean bean=result.get(flag);
    		if(bean==null){//没有workstatus则新建
    			bean = new WorkStatusBean();
        		bean.setUserId(userId);
        		bean.setWorkDate(workDateStr);
        		bean.setFlag(flag);
    		}
    		
    		//将打卡结果转化成考勤结果
    		String[]params=cardData.getWorkStatus().split("_");
    		if(params.length!=3){//打卡记录中的打卡结果异常退出
    			log.error("fail to convert carddata to workstatus because of carddata's workstatus invalid-------carddata's id:"+cardData.getId());
    			continue;
    		}
    		String check=null;//考勤结果，不用保存check字段了
    		String checkType=params[2];//考勤结果所属类型，不会进入未打卡，因为查找的是有效的打卡记录
    		
    		if("opr".equals(params[0])||"am".equals(params[0])){//运行人员打卡或行政人员上午打卡
    			if("startWork".equals(params[1])){
    				setWorkStatusBeanByParams(bean, "MornStart", check, cardData.getId(), checkType, cardData.getCheckDate());
    			}else{
    				setWorkStatusBeanByParams(bean, "MornEnd", check, cardData.getId(), checkType, cardData.getCheckDate());
    			}
    		}else{//行政人员下午打卡
    			if("startWork".equals(params[1])){
    				setWorkStatusBeanByParams(bean, "NoonStart", check, cardData.getId(), checkType, cardData.getCheckDate());
    			}else{
    				setWorkStatusBeanByParams(bean, "NoonEnd", check, cardData.getId(), checkType, cardData.getCheckDate());
    			}
    		}
    		
    		result.put(bean.getFlag(), bean);
        }
        
		return result;
	}
	
	private Boolean checkIsOpr(String userId,String oprPersons){
		return oprPersons!=null&&oprPersons.contains(userId+",");
	}
	
	/**
	 * 查询指定时间和站点的请假申请，以日期+"_"+用户id为key返回map
	 * @param siteId
	 * @param userId
	 * @param startTimeStr
	 * @param endTimeStr
	 * @param definitionBean
	 * @return
	 * @throws Exception
	 */
	private Map<String, WorkStatusBean>queryLeaveWorkStatusBySiteAndTime(String siteId,String userId,String startTimeStr,String endTimeStr,
			DefinitionBean definitionBean,String oprPersons,Map<String, DutyPersonShiftVo>dutyPersonShiftMap) throws Exception{
		//查询请假内容
        List<LeaveContainItemVo> leaveList = leaveService.queryLeaveByDiffDay( siteId,(userId==null?null:new String[]{userId}), startTimeStr, endTimeStr,null );
        
        //处理每个请假项，按日期分割成workstatus
        Map<String, WorkStatusBean>result=new HashMap<String, WorkStatusBean>();
        for(LeaveContainItemVo vo:leaveList){
        	splitToConvertToWorkStatusBean(vo,"leave",definitionBean,result,oprPersons,dutyPersonShiftMap);
        }
        
		return result;
	}
	
	/**
	 * 查询指定时间和站点的考勤异常申请，以日期+"_"+用户id为key返回map
	 * @param siteId
	 * @param userId
	 * @param startTimeStr
	 * @param endTimeStr
	 * @param definitionBean
	 * @return
	 * @throws Exception
	 */
	private Map<String, WorkStatusBean>queryAbnormityWorkStatusBySiteAndTime(String siteId,String userId,String startTimeStr,String endTimeStr,
			DefinitionBean definitionBean,String oprPersons,Map<String, DutyPersonShiftVo>dutyPersonShiftMap) throws Exception{
		//查询考勤异常内容
        List<LeaveContainItemVo> abnormityList = abnormityService.queryAbnormityByDiffDay( siteId,userId, startTimeStr, endTimeStr );
        
        //处理每个请假项，按日期分割成workstatus
        Map<String, WorkStatusBean>result=new HashMap<String, WorkStatusBean>();
        for(LeaveContainItemVo vo:abnormityList){
        	splitToConvertToWorkStatusBean(vo,"abnormity",definitionBean,result,oprPersons,dutyPersonShiftMap);
        }
        
		return result;
	}
	
	/**
	 * 将一个包含多个日期的LeaveContainItemVo切割为单个日期再转化为WorkStatusBean
	 * @param vo
	 * @param type 请假（leave）、考勤异常（abnormity）
	 * @param definitionBean
	 * @return
	 * @throws Exception
	 */
	private Map<String, WorkStatusBean>splitToConvertToWorkStatusBean(LeaveContainItemVo vo,String type,DefinitionBean definitionBean,
			Map<String, WorkStatusBean>result,String oprPersons,Map<String, DutyPersonShiftVo>dutyPersonShiftMap)throws Exception{
		if(result==null){
			result=new HashMap<String, WorkStatusBean>();
		}
		//判断是否运行人员
        Boolean isOpr=checkIsOpr(vo.getCreateBy(),oprPersons);
        //起止日期，截取掉时间
        Date startTemp=DateFormatUtil.getFormatDate(vo.getStartDate(),"yyyy-MM-dd");
        Date endTemp=DateFormatUtil.getFormatDate(vo.getEndDate(),"yyyy-MM-dd");
        //间隔多少天，创建多少个workstatus
        int diffDays=(int)DateFormatUtil.dateDiff("dd",startTemp,endTemp);
        for(int i=0;i<=diffDays;i++){
        	Date temp = DateFormatUtil.addDate( startTemp, "d", i );
        	//对当天的影响
        	convertToWorkStatusBean(vo,type,definitionBean,isOpr,
        			temp,(i==0?vo.getStartDate():null),(i==diffDays?vo.getEndDate():null),result,dutyPersonShiftMap);
        }
        //如果是运行人员，有可能跨天对startDate的前一天有影响
        if(isOpr){
        	Date temp = DateFormatUtil.addDate( startTemp, "d", -1 );
        	//对当天的影响
        	convertToWorkStatusBean(vo,type,definitionBean,isOpr,
        			temp,vo.getStartDate(),vo.getEndDate(),result,dutyPersonShiftMap);
        }
        return result;
	}
	
	/**
	 * 将一个指定日期的LeaveContainItemVo转化成WorkStatusBean
	 * @param vo
	 * @param type 请假（leave）、考勤异常（abnormity）
	 * @param definitionBean
	 * @param isOpr 是否运行人员
	 * @param workDate 日期
	 * @param startTime 开始时间，为空则取一天开始工作的时间
	 * @param endTime 结束时间，为空则取一天结束工作的时间
	 * @param dutyPersonShiftMap 人员的排班情况
	 * @return
	 * @throws Exception 
	 */
	private WorkStatusBean convertToWorkStatusBean(LeaveContainItemVo vo,String type,DefinitionBean definitionBean,Boolean isOpr,
			Date workDate,Date startTime,Date endTime,Map<String, WorkStatusBean>result,Map<String, DutyPersonShiftVo>dutyPersonShiftMap) throws Exception{
		String workDateStr=DateFormatUtil.dateToString( workDate, "yyyy-MM-dd");
		
		WorkStatusBean bean = new WorkStatusBean();
		bean.setUserId(vo.getCreateBy());
		bean.setWorkDate(workDateStr);
		String flag=bean.getWorkDate()+"_"+bean.getUserId();
		if(result.get(flag)!=null){
			bean=result.get(flag);
			//因为根据详细项id倒序，同一天有多个详细项时，只要这一天的考勤结果都已算出，就不需再算
			if(bean.getMornStartCheck()!=null&&bean.getMornEndCheck()!=null&&(
					isOpr||(!isOpr&&bean.getNoonStartCheck()!=null&&bean.getNoonEndCheck()!=null))){
				return bean;
			}
		}else{
			bean.setFlag(flag);
		}
		
		//debug here
		/*if("2016-04-07_880040".equals(flag)){
			System.out.println("WorkStatusServiceImpl.convertToWorkStatusBean()");
		}*/
		
		//生成考勤结果
        String check=vo.getCategory();
        
        Boolean isChanged=false;//标记是否修改需要放入result
		if(isOpr){//运行人员需要去拿排班信息和班次时间，设置上午开始和下午结束两个时间即可
			DutyPersonShiftVo shiftVo=dutyPersonShiftMap.get(flag);
			if(shiftVo!=null&&!"rest".equals(shiftVo.getShiftType())){//有排班才需要更新
				Date shiftStartDate = DateFormatUtil.parseDate( workDateStr + " " + shiftVo.getStartTime(), "yyyy-MM-dd HHmm" );
				Date shiftEndDate=DateFormatUtil.addDate(shiftStartDate, "H", shiftVo.getLongTime());
				//当天的请假时间
	            Date start=startTime==null?shiftStartDate:startTime;
	            Date end=endTime==null?(
	            		//请假结束时间有可能是跨天但不跨班的，即属于下一天，但是要统计到当天的班次中的，应排除这种情况
	            		//即：请假结束时间<=当班下班时间，直接取请假结束时间
	            		DateFormatUtil.compareDate(shiftEndDate, vo.getEndDate())?vo.getEndDate():shiftEndDate
	            	):endTime;
	            
	            if(DateFormatUtil.compareDateNoEqual(end, start)){//请假开始时间<请假结束时间		
		            if(bean.getMornStartCheck()==null&&//根据详细项id倒序，后面的考勤结果不能覆盖前面的
		            		DateFormatUtil.compareDate(end, shiftStartDate)&&DateFormatUtil.compareDate(shiftStartDate, start)){
		            	//请假开始时间<=上班时间<=请假结束时间
		            	setWorkStatusBeanByParams(bean,"MornStart",check,vo.getId()+"",type,null);
		            	isChanged=true;
		            }
		            if(bean.getMornEndCheck()==null&&
		            		DateFormatUtil.compareDate(end, shiftEndDate)&&DateFormatUtil.compareDate(shiftEndDate, start)){
		            	//请假开始时间<=下班时间<=请假结束时间
		            	setWorkStatusBeanByParams(bean,"MornEnd",check,vo.getId()+"",type,null);
		            	isChanged=true;
		            }
	            }
			}
		}else{//非运行人员拿系统定义的上下班时间，需要设置上午和下午开始和结束四个时间
			//获得工作时间
			String morning = definitionBean.getForeStartDate();
	        String forenoon = definitionBean.getForeEndDate();
	        String noon = definitionBean.getAfterStartDate();
	        String afternoon = definitionBean.getAfterEndDate();
	        Date morningDate = DateFormatUtil.parseDate( workDateStr + " " + morning, "yyyy-MM-dd HHmm" ); 
            Date forenoonDate = DateFormatUtil.parseDate( workDateStr + " " + forenoon, "yyyy-MM-dd HHmm" ); 
            Date noonDate = DateFormatUtil.parseDate( workDateStr + " " + noon, "yyyy-MM-dd HHmm" ); 
            Date afternoonDate = DateFormatUtil.parseDate( workDateStr + " " + afternoon, "yyyy-MM-dd HHmm" );
            //当天的请假时间
            Date start=startTime==null?morningDate:startTime;
            Date end=endTime==null?afternoonDate:endTime;
            
            if(DateFormatUtil.compareDateNoEqual(end, start)){//请假开始时间<请假结束时间
            	if(bean.getMornStartCheck()==null&&
                		DateFormatUtil.compareDate(end, morningDate)&&DateFormatUtil.compareDate(morningDate, start)){
                	//请假开始时间<=上午上班时间<=请假结束时间
            		setWorkStatusBeanByParams(bean,"MornStart",check,vo.getId()+"",type,null);
            		isChanged=true;
                }
                if(bean.getMornEndCheck()==null&&
                		DateFormatUtil.compareDate(end, forenoonDate)&&DateFormatUtil.compareDate(forenoonDate, start)){
                	//请假开始时间<=上午下班时间<=请假结束时间
                    setWorkStatusBeanByParams(bean,"MornEnd",check,vo.getId()+"",type,null);
                    isChanged=true;
                }
                if(bean.getNoonStartCheck()==null&&
                		DateFormatUtil.compareDate(end, noonDate)&&DateFormatUtil.compareDate(noonDate, start)){
                	//请假开始时间<=下午上班时间<=请假结束时间
                	setWorkStatusBeanByParams(bean,"NoonStart",check,vo.getId()+"",type,null);
                	isChanged=true;
                }
                if(bean.getNoonEndCheck()==null&&
                		DateFormatUtil.compareDate(end, afternoonDate)&&DateFormatUtil.compareDate(afternoonDate, start)){
                	//请假开始时间<=下午下班时间<=请假结束时间
                	setWorkStatusBeanByParams(bean,"NoonEnd",check,vo.getId()+"",type,null);
                	isChanged=true;
                }
            }
		}
		if(isChanged){
			result.put(bean.getFlag(), bean);
		}
        return bean;
	}
	
	/**
	 * 设置workstatusbean的参数
	 * @param bean
	 * @param fieldType 取值 MornStart、MornEnd、NoonStart、NoonEnd
	 * @param check
	 * @param checkSrc
	 * @param checkType
	 * @param checkTime
	 * @throws Exception
	 */
	private void setWorkStatusBeanByParams(WorkStatusBean bean,String fieldType,
			String check,String checkSrc,String checkType,String checkTime) throws Exception{
		String methodName="get"+fieldType+"CheckType";
		Method method = WorkStatusBean.class.getMethod( methodName, new Class[]{} );
		Object originalCheckType=method.invoke( bean, new Object[]{} );
		if(originalCheckType!=null){//非空时考虑多条打卡记录的覆盖
			if(checkType.equals(originalCheckType)//打卡结果不变
				||"valid".equals(originalCheckType)){//已有已打卡的结果
				return;
			}
		}
				
		methodName="set"+fieldType+"Check";
		method = WorkStatusBean.class.getMethod( methodName, new Class[]{String.class} );
		method.invoke( bean, check );
		
		methodName="set"+fieldType+"CheckSrc";
		method = WorkStatusBean.class.getMethod( methodName, new Class[]{String.class} );
		method.invoke( bean, checkSrc );

		methodName="set"+fieldType+"CheckType";
		method = WorkStatusBean.class.getMethod( methodName, new Class[]{String.class} );
		method.invoke( bean, checkType );

		methodName="set"+fieldType+"Time";
		method = WorkStatusBean.class.getMethod( methodName, new Class[]{String.class} );
		method.invoke( bean, checkTime );
	}
}
