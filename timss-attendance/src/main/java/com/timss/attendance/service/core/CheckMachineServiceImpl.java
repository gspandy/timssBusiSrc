package com.timss.attendance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.MachineBean;
import com.timss.attendance.service.CardDataService;
import com.timss.attendance.service.CheckMachineService;
import com.timss.attendance.service.DefinitionService;
import com.timss.attendance.service.MachineDataService;
import com.timss.attendance.service.MachineService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.code.NotificationType;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.manager.support.IConfigurationManager;
import com.yudean.itc.manager.support.INotificationManager;
import com.yudean.mvc.bean.userinfo.impl.UserInfoImpl;
import com.yudean.mvc.handler.ThreadLocalHandler;

@Service("checkMachineService")
public class CheckMachineServiceImpl implements CheckMachineService {
    private Logger log = Logger.getLogger( CheckMachineServiceImpl.class );
    
    @Autowired
    private CardDataService cardDataService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private MachineDataService machineDataService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    @Autowired
    private DefinitionService definitionService;
    @Autowired
    private WorkStatusService workStatusService;
    @Autowired
    private IConfigurationManager confManager;
    @Autowired
    private INotificationManager notifyManager;
    @Autowired
    private AtdUserPrivUtil userPrivUtil;
    
    @Override
    public void checkMachineStatusAndNotify(String siteId) throws Exception{
    	String nowDateStr=DateFormatUtil.dateToString(new Date(), "yyyy-MM-dd");
    	//检查并返回检查失败的考勤机
    	Map<String,List<MachineBean>>result=checkMachineStatus(siteId,"fail");
    	//如果需要发送通知
    	if(!result.isEmpty()){
    		for (String site: result.keySet()) {
    			notifyMachineStatus(site,result.get(site),nowDateStr);
			}
    	}
    }
    /**
     * 从系统配置中获取通知方式，将考勤机的状态发送给配置中的用户
     * 配置包括：
     * atd_notify_machine_status_type 配置通知方式，区分站点，可取值email/sms，多个值;分隔
     * atd_notify_machine_status_site_${type} 配置通知方式的站点通知人员，区分站点，多个值;分隔
     * atd_notify_machine_status_superadmin_${type} 配置通知方式的超管的通知人员，不区分站点，多个值;分隔
     * @param siteId
     * @param machines
     * @throws Exception 
     */
    private void notifyMachineStatus(String siteId,List<MachineBean>machines,String nowDateStr) throws Exception{
    	Set<String>typeList=getValidValFromConf("atd_notify_machine_status_type", siteId);
    	log.info("notifyMachineStatus->siteId:"+siteId+" machinesSize:"+machines.size()+" notifyTypeSize:"+typeList.size());
    	if(machines.size()>0&&typeList.size()>0){
    		for (String type : typeList) {
    			Set<String>recipients=getValidValFromConf("atd_notify_machine_status_site_"+type, siteId);
    			recipients.addAll(getValidValFromConf("atd_notify_machine_status_superadmin_"+type, "NaN"));
    			if(recipients.size()>0){
    				Map<String, Object>params=new HashMap<String, Object>();
    				params.put("siteId", siteId);
    				params.put("machines", machines);
    				params.put("nowDate", nowDateStr);
    				notifyManager.notify(recipients.toArray(new String[recipients.size()]), 
    						"email".equals(type)?NotificationType.EMAIL:NotificationType.SMS, "atd_notify_machine_status", params);
    			}
    			log.info("notifyMachineStatus->siteId:"+siteId+" notifyType:"+type+" recipientsSize:"+recipients.size());
			}
    	}
    }
    /**
     * 从系统配置中取出参数并;分隔成字符串列表
     * @param confName
     * @param siteId
     * @return
     */
    private Set<String>getValidValFromConf(String confName,String siteId){
    	Configuration configuration = confManager.query( confName, siteId, "NaN" );
    	Set<String>result=new HashSet<String>();
    	if(configuration!=null){
    		String valStr=configuration.getVal();
    		log.info("getValidValFromConf->confName:"+confName+" siteId:"+siteId+" val:"+valStr);
    		if(valStr!=null&&!"".equals(valStr)){
    			String[]vals=valStr.split(";");
    			for(String val:vals){
    				if(!"".equals(val)){
    					result.add(val);
    				}
    			}
    		}
    	}
    	return result;
    }
    /**
     * 检查指定站点的考勤机状态，可指定返回指定结果的考勤机
     * @param siteId 可为空，为空查询所有站点
     * @param checkType 指定返回的考勤机结果的类型，取值all/fail/success 为空默认all
     * @return
     * @throws Exception
     */
    private Map<String,List<MachineBean>>checkMachineStatus(String siteId,String checkType) throws Exception{
    	List<MachineBean>machineList=new ArrayList<MachineBean>();
    	if(siteId==null||"".equals(siteId)){
    		//获取所有考勤机
        	machineList=machineService.queryAll();
    	}else{
    		//获取站点的考勤机
        	machineList=machineService.queryBySiteId(siteId);
    	}
    	log.info("checkMachineStatus->machineSize:"+machineList.size());
    	
    	Map<String,List<MachineBean>>result=new HashMap<String, List<MachineBean>>();
    	String dateFormat="yyyy-MM-dd";
    	String nowStr=DateFormatUtil.dateToString(new Date(),dateFormat );
    	//检查每台考勤机
    	for (MachineBean machineBean : machineList) {
    		String status="success";
    		String site=machineBean.getSiteid();
    		Date importDate=machineBean.getLastImport();
    		if(importDate==null){//空导入成功的时间
    			status="empty";
    		}else{
    			String importStr=DateFormatUtil.dateToString(importDate, dateFormat);
    			if(importStr==null||"".equals(importStr)){//时间转换不成功
    				status="error format";
    			}else if(!nowStr.equals(importStr)){//不是同一天
    				status="not today";
    			}
    			machineBean.setLastImportStr(DateFormatUtil.dateToString(importDate, "yyyy-MM-dd HH:mm"));
    		}
    		machineBean.setStatus(status);
    		
    		if(checkType==null)checkType="all";
    		if("all".equals(checkType)//根据要返回的结果类型决定是否记录下这台考勤机
    				||(!"success".equals(status)&&"fail".equals(checkType))//记录不成功的
    				||("success".equals(status)&&"success".equals(checkType))){//记录成功的
    			List<MachineBean>machines=result.get(site);
        		if(machines==null){
        			machines=new ArrayList<MachineBean>();
        		}
        		machines.add(machineBean);
        		result.put(site, machines);
    		}
		}
    	
    	return result;
    }
    
	@Override
	public void importCheckMachineData(String site) throws Exception{
		Map<String,Map<String, Integer>>result=new HashMap<String, Map<String, Integer>>();//统计站点的数据
    	//缓存站点的考勤规则
    	Map<String,DefinitionBean>definitionMap=new HashMap<String,DefinitionBean>();
    	//缓存人员的部门信息
        Map<String, Map<String, String>>userMap=new HashMap<String, Map<String, String>>();
        //缓存站点的运行人员
        Map<String, String>oprPersonsMap=new HashMap<String, String>();
        
    	List<MachineBean>machineList=new ArrayList<MachineBean>();
    	if(site==null||"".equals(site)){
    		//获取所有考勤机
        	machineList=machineService.queryAll();
    	}else{
    		//获取站点的考勤机
        	machineList=machineService.queryBySiteId(site);
    	}
    	
    	Integer machineNum=machineList.size();
    	log.info("size of machine to importCheckMachineData："+machineNum);
    	
    	CountDownLatch latch = new CountDownLatch(machineNum);//线程同步器
    	//等待线程执行时间，从系统配置中获取，超时且无结果的线程将被结束，为0时不结束超时线程
        Configuration configuration = confManager.query( "atd_check_machine_data_wait_time", "", "NaN" );
        Integer defaultWaitTime=60*60*machineNum;//默认等待单个任务时间乘以任务个数，保证即使任务串行执行，也给每个任务足够的指定时间
    	Integer waitTime=defaultWaitTime;
    	if(configuration!=null&&!StringUtils.isEmpty(configuration.getVal())){
    		waitTime=Integer.parseInt(configuration.getVal());
    		if(waitTime<0)waitTime=defaultWaitTime;
    	}
    	log.info("waitTime for importCheckMachineData："+waitTime);
    	
    	ExecutorService executor = Executors.newFixedThreadPool(machineNum);
    	List<Future<Map<String, Object>>>taskList=new ArrayList<Future<Map<String,Object>>>();
    	for (MachineBean machineBean : machineList) {
    		String siteId=machineBean.getSiteid();
    		DefinitionBean definitionBean=definitionMap.get(siteId);
    		if(definitionBean==null){
    			definitionBean=definitionService.queryDefinitionBySiteId(siteId);
    			definitionMap.put(siteId, definitionBean);
    		}
    		//初始化站点运行人员
    		String oprPersons=oprPersonsMap.get(siteId);
    		if(oprPersons==null){
    			oprPersons=dutyService.queryOprPersonsBySite(siteId);
    			oprPersonsMap.put(siteId, oprPersons);
    		}
    		
    		ImportCheckMachineDataTask task = new ImportCheckMachineDataTask(machineBean, definitionBean, userMap, oprPersons,latch);
    		taskList.add(executor.submit(task));
		}
        executor.shutdown();
        
        try {
        	//等待任务完成
        	latch.await(waitTime, TimeUnit.SECONDS);
        	//Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            log.error("latch.await:"+waitTime+" s interrupted",e);
        }
        
        for (int i=0;i<taskList.size();i++) {
        	Future<Map<String, Object>> task=taskList.get(i);
        	Map<String, Object>taskResult=null;
        	String taskIndex=(i+1)+"/"+taskList.size();
        	
        	try {
        		if(task.isDone()||waitTime==0){//任务完成或无超时时间
        			taskResult=task.get();
    				log.debug("task "+taskIndex+" is finished in importCheckMachineData");
    			}else{//取消任务
    				Boolean cancelResult=task.cancel(true);
    				if(cancelResult){//正在执行或未执行的任务被取消成功
    					log.debug("task "+taskIndex+" is canceled in importCheckMachineData");
    				}else{//执行完成的
    					taskResult=task.get();
    					log.debug("task "+taskIndex+" is finished while cancel in importCheckMachineData");
    				}
    			}
            } catch (Exception e) {
            	log.error("check and get task result failed:"+taskIndex,e);
            }
			
        	if(taskResult!=null){//记录统计信息
        		String siteId=taskResult.get("siteId").toString();
        		if(!StringUtils.isEmpty(siteId)){
        			Map<String, Integer>siteResult=result.get(siteId);
        			if(siteResult==null){
        				siteResult=new HashMap<String, Integer>();
        				siteResult.put("getNum", 0);
        				siteResult.put("saveNum", 0);
        			}
        			siteResult.put("getNum", siteResult.get("getNum")+Integer.parseInt(taskResult.get("getNum").toString()));
        			siteResult.put("saveNum", siteResult.get("saveNum")+Integer.parseInt(taskResult.get("saveNum").toString()));
        			result.put(siteId, siteResult);
        		}
        	}else{
        		log.debug("task "+taskIndex+" is error and return null in importCheckMachineData");
        	}
		}
        
    	//打印统计信息
    	Integer totalGetNum=0,totalSaveNum=0;
    	for (String siteId : result.keySet()) {
    		Map<String, Integer>siteResult=result.get(siteId);
			log.info("importCheckMachineData->site:"+siteId+" "+siteResult.get("saveNum")+"/"+siteResult.get("getNum"));
			totalGetNum+=siteResult.get("getNum");
			totalSaveNum+=siteResult.get("saveNum");
		}
    	log.info("finish importCheckMachineData->site:"+site+" "+totalSaveNum+"/"+totalGetNum);
	}
	
	class ImportCheckMachineDataTask implements Callable<Map<String, Object>>{
		MachineBean machineBean;
		DefinitionBean definitionBean;
		Map<String, Map<String, String>> userMap;
		String oprPersons;
		CountDownLatch latch;
		
		public ImportCheckMachineDataTask(MachineBean machineBean,
				DefinitionBean definitionBean,
				Map<String, Map<String, String>> userMap,
				String oprPersons,CountDownLatch latch){
			this.machineBean=machineBean;
			this.definitionBean=definitionBean;
			this.userMap=userMap;
			this.oprPersons=oprPersons;
			this.latch=latch;
		}
		
	    @Override
	    public Map<String, Object> call() throws Exception {
	    	//给线程带上用户信息
	    	UserInfoImpl userInfo=new UserInfoImpl();
	    	userInfo.setCurrentSite(definitionBean.getSiteId());
	    	ThreadLocalHandler.createNewVarableOweUserInfo(userInfo);
	    	String taskInfo=machineBean.getSiteid()+" "+machineBean.getAmIp();
	    	log.info("start call ImportCheckMachineDataTask:"+taskInfo);
	    	Map<String, Object>result=null;
	    	try {
	    		//获取数据
	    		List<CardDataBean> resultList = machineDataService.getAttdanceData(machineBean, definitionBean, userMap, oprPersons);
	    		machineBean.setCardDataList(resultList);
	    		Integer saveNum=cardDataService.saveMachineCardData(machineBean);
	    		Map<String, Object>siteResult=new HashMap<String, Object>();
	    		siteResult.put("getNum", resultList.size());
				siteResult.put("saveNum", saveNum);
				siteResult.put("siteId", definitionBean.getSiteId());
				siteResult.put("machineBean", machineBean);
				result=siteResult;
				log.info("finish call ImportCheckMachineDataTask:"+taskInfo);
			} catch (Exception e) {
				log.error("fail to call ImportCheckMachineDataTask:"+taskInfo+" oprPersons:"+oprPersons,e);
			}
	    	latch.countDown();
	    	return result;
	    }
	}
	
	private Boolean checkIsOpr(String userId,String oprPersons){
		return oprPersons!=null&&oprPersons.contains(userId+",");
	}

	@Override
	public void refreshCheckMachineResult(String siteId, String[]userIds, Date startTime,
			Date endTime) throws Exception {
		log.info("start refreshCheckMachineResult->siteId:"+siteId+" userIds:"+userIds+" startTime:"+startTime+" endTime:"+endTime);
		//开始时间前一天
		Date prevDate=startTime==null?null:DateFormatUtil.addDate(startTime, "d", -1);
		String prevDateStr=prevDate==null?null:DateFormatUtil.dateToString(prevDate, "yyyy-MM-dd");
		String prevTimeStr=prevDateStr==null?null:(prevDateStr+" 00:00");
		//结束时间后一天
		Date nextDate=endTime==null?null:DateFormatUtil.addDate(endTime, "d", 1);
		String nextDateStr=nextDate==null?null:DateFormatUtil.dateToString(nextDate, "yyyy-MM-dd");
		String nextTimeStr=nextDateStr==null?null:(nextDateStr+" 23:59");
		//获取符合条件的打卡记录
		List<CardDataBean>cardDataBeans=cardDataService.queryAll(siteId, userIds, prevTimeStr, nextTimeStr);
		//获取排班
		Map<String, DutyPersonShiftVo>dutyPersonShiftMap;//人员的排班情况
		dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(siteId,userIds, prevTimeStr, nextTimeStr);
		List<CardDataBean>updateBeans=new ArrayList<CardDataBean>();
		//缓存站点的考勤规则
    	Map<String,DefinitionBean>definitionMap=new HashMap<String,DefinitionBean>();
    	//缓存站点的运行人员
        Map<String, String>oprPersonsMap=new HashMap<String, String>();
        //当前登录人
        String updateBy=userPrivUtil.getUserInfoScope().getUserId();
        Date now=DateFormatUtil.getCurrentTime();
        
		for (CardDataBean bean : cardDataBeans) {
			DefinitionBean definitionBean=definitionMap.get(bean.getSiteId());
    		if(definitionBean==null){
    			definitionBean=definitionService.queryDefinitionBySiteId(bean.getSiteId());
    			definitionMap.put(bean.getSiteId(), definitionBean);
    		}
    		//初始化站点运行人员
    		String oprPersons=oprPersonsMap.get(bean.getSiteId());
    		if(oprPersons==null){
    			oprPersons=dutyService.queryOprPersonsBySite(bean.getSiteId());
    			oprPersonsMap.put(bean.getSiteId(), oprPersons);
    		}
    		
    		//保留之前的结果用于比较是否需要更新
    		String oprDutyDate=bean.getOprDutyDate();
    		String workStatus=bean.getWorkStatus();
    		Integer oprScheduleId=bean.getOprScheduleId();
    		
    		bean.setIsOpr(checkIsOpr(bean.getUserId(),oprPersons));
	        if(bean.getIsOpr()){//设置运行人员的排班时间
	        	cardDataService.setOprPersonDutyToCardDataBean(bean,definitionBean.getValidMin(),dutyPersonShiftMap);
	        }
	        //刷新考勤结果
	        bean.setWorkStatus(workStatusService.checkWorkStatusByCardData(bean, definitionBean));
	        
	        if((oprDutyDate==null?(bean.getOprDutyDate()!=null):(!oprDutyDate.equals(bean.getOprDutyDate())))||
	        		(workStatus==null?(bean.getWorkStatus()!=null):(!workStatus.equals(bean.getWorkStatus())))||
	        		(oprScheduleId==null?(bean.getOprScheduleId()!=null):(!oprScheduleId.equals(bean.getOprScheduleId())))){
	        	bean.setUpdateBy(updateBy);
	        	bean.setUpdateDate(now);
	        	updateBeans.add(bean);
	        }
		}
		
		//更新操作
		Integer result=0;
		if(updateBeans.size()>0){
			result=cardDataService.updateCardDataList(updateBeans);
		}
		log.info("finish refreshCheckMachineResult->siteId:"+siteId+" userIds:"+userIds+" startTime:"+startTime+" endTime:"+endTime+" result:"+result+"/"+updateBeans.size());
	}
}
