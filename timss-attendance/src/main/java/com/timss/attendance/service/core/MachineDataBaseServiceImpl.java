package com.timss.attendance.service.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFilenameFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanvon.faceid.sdk.FaceId;
import com.hanvon.faceid.sdk.FaceIdAnswer;
import com.hanvon.faceid.sdk.FaceId_ErrorCode;
import com.timss.attendance.bean.CardDataBean;
import com.timss.attendance.bean.DefinitionBean;
import com.timss.attendance.bean.MachineBean;
import com.timss.attendance.service.CardDataService;
import com.timss.attendance.service.MachineDataBaseService;
import com.timss.attendance.util.DateFormatUtil;
import com.timss.attendance.vo.CardResultVo;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.ScheduleDetailService;
import com.timss.operation.vo.DutyPersonShiftVo;
import com.yudean.itc.util.json.JsonHelper;

@Service("machineDataBaseService")
public class MachineDataBaseServiceImpl implements MachineDataBaseService{
    private Logger log = Logger.getLogger( MachineDataBaseServiceImpl.class );
    
    @Autowired
    private CardDataService cardDataService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ScheduleDetailService scheduleDetailService;
    
	@Override
    public List<CardDataBean> getAttdanceMachineData(MachineBean machineBean,DefinitionBean definitionBean,
    		Map<String, Map<String, String>>userMap,String oprPersons) throws Exception{
        String deviceCharset = "GBK";
        List<CardDataBean> result = new ArrayList<CardDataBean>();
        String ip=machineBean.getAmIp();
        Integer port=machineBean.getAmPort();
        String siteId=machineBean.getSiteid();
        Date startTime=machineBean.getLastImport();//抓取数据开始的时间点
        Date endTime=new Date();
        String startTimeStr=startTime==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        String endTimeStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
        machineBean.setStartTime(startTimeStr);
        machineBean.setEndTime(endTimeStr);
        
        String beanInfo="machine siteId:"+siteId+" ip:" + ip +":"+port + " start:"+startTimeStr+" end:"+endTimeStr;
        log.info( beanInfo+" ----start getAttdanceMachineData");
        
        try {
        	//连接考勤机
            InetAddress addr = InetAddress.getByName( ip );
            FaceId tcpClient=null;
            try {
            	tcpClient = new FaceId( addr,  port );
            	log.info("execute new FaceId(' "+addr+" ',' "+port+" ')");
			} catch (Exception e) {
				log.error("execute new FaceId(' "+addr+" ',' "+port+" ') error，error info-->"+e.getMessage(),e);
			}
            if(tcpClient!=null){
            	// 获取考勤记录
                String command = "GetRecord("+
                				(startTime==null?"":("start_time=\"" + startTimeStr + "\" "))+
                				"end_time=\"" + endTimeStr + "\")";
                FaceIdAnswer output = new FaceIdAnswer();
                
                for(int j=0;j<5;j++){//为了增加连接成功率，重试5次
                	FaceId_ErrorCode errorCode = tcpClient.Execute(command, output, deviceCharset);
                    log.info(j+" execute to get data:"+command+" -----FaceId_ErrorCode:"+errorCode);
                    
                    if (errorCode.equals(FaceId_ErrorCode.Success)) {   //无数据也会执行失败
                        String[] strList = output.answer.split ("\r\n");
                        
                        //初始化人员排班map
                        //多查询前后一天用于处理跨天的班次
                        String plusStartTimeStr=startTime==null?"":new SimpleDateFormat("yyyy-MM-dd").format(DateFormatUtil.addDate(startTime,"d",-1));
                        String plusEndTimeStr=new SimpleDateFormat("yyyy-MM-dd").format(DateFormatUtil.addDate(endTime,"d",1));
                    	//Map<String, DutyPersonShiftVo>dutyPersonShiftMap=dutyService.queryDutyPersonAndShiftBySiteAndTime(siteId,null, plusStartTimeStr, plusEndTimeStr);
                        Map<String, DutyPersonShiftVo>dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(siteId,null, plusStartTimeStr, plusEndTimeStr);
                        
                        //去掉开头、末尾
                        for( int i = 1; i < strList.length - 1; i++ ){
                        	//log.debug(i+" "+strList[i]);
                        	String str = (strList[i]+" ").replaceAll( "=\"([^\\f\\n\\r\\t\\v]*?)\" ", "\":\"$1\",\"" );
                            str = "{\"" + str.substring(0,str.length()-2) + "}";
                            //log.debug(i+" "+str);
                            CardResultVo vo = JsonHelper.fromJsonStringToBean( str, CardResultVo.class );
                            //转换成cardDataBean
                            CardDataBean cardDataBean = cardDataService.setupCardDataBean(vo.getId(), StringUtils.trim(vo.getName()), 
                            		vo.getTime(), definitionBean, userMap, checkIsOpr(vo.getId(),oprPersons), dutyPersonShiftMap);
                            //设置数据来源的考勤机
                            cardDataBean.setAmId(machineBean.getAmId());
                            
                            result.add( cardDataBean );
                            
                            //将第一条数据的时间设置成考勤机的开始时间，用于更新打卡统计
                            if("".equals(machineBean.getStartTime())){
                            	machineBean.setStartTime(cardDataBean.getCheckDate());
                            }
                        }
                        //使用获取考勤记录命令的结束时间加一秒作为考勤机的最后打卡时间戳，用作下次取数的开始时间
                        //考勤数据插入成功才能更新
                        machineBean.setLastImport(DateFormatUtil.addDate(endTime, "s", 1));
                        
                        break;//成功则退出重试
                     }
                }
            }
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        
        log.info( beanInfo+" ----finish getAttdanceMachineData size of data get：" + result.size() );
        
        return result;
    }
	
	private Boolean checkIsOpr(String userId,String oprPersons){
		return oprPersons!=null&&oprPersons.contains(userId+",");
	}

	@Override
	public List<CardDataBean> getAttdanceData(MachineBean machineBean,
			DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap, String oprPersons)
			throws Exception {
		if("smb".equals(machineBean.getType())){
			return getAttdanceFileData(machineBean, definitionBean, userMap, oprPersons);
		}else{
			return getAttdanceMachineData(machineBean, definitionBean, userMap, oprPersons);
		}
	}

	@Override
	public List<CardDataBean> getAttdanceFileData(MachineBean machineBean,
			DefinitionBean definitionBean,
			Map<String, Map<String, String>> userMap, String oprPersons)
			throws Exception {
		String deviceCharset = "GBK";
        List<CardDataBean> result = new ArrayList<CardDataBean>();
        String ip=machineBean.getAmIp();
        String siteId=machineBean.getSiteid();
        Date startTime=machineBean.getLastImport();//抓取数据开始的时间点
        Date endTime=new Date();
        String startTimeStr=startTime==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        String endTimeStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
        machineBean.setStartTime(startTimeStr);
        machineBean.setEndTime(endTimeStr);
        String filePath="smb://"+ip+(!StringUtils.isBlank(machineBean.getPath())?machineBean.getPath():"");
        
        String loginInfo=(!StringUtils.isBlank(machineBean.getLoginName())?machineBean.getLoginName():"")+"/"+
        		(!StringUtils.isBlank(machineBean.getPassword())?machineBean.getPassword():"");
        String beanInfo="machine siteId:"+siteId+" path:"+filePath+" login:"+loginInfo+" start:"+startTimeStr+" end:"+endTimeStr;
        log.info( beanInfo+" ----start getAttdanceFileData");
        
		try{
	    	NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ip, machineBean.getLoginName(), machineBean.getPassword());  //先登录验证  
	        SmbFile smbPath = new SmbFile(filePath,auth); //路径指向的文件夹，也可能是文件 
	        if(smbPath.exists()){
	        	//初始化人员排班map
	            //多查询前后一天用于处理跨天的班次
	            String plusStartTimeStr=startTime==null?"":new SimpleDateFormat("yyyy-MM-dd").format(DateFormatUtil.addDate(startTime,"d",-1));
	            String plusEndTimeStr=new SimpleDateFormat("yyyy-MM-dd").format(DateFormatUtil.addDate(endTime,"d",1));
	        	//Map<String, DutyPersonShiftVo>dutyPersonShiftMap=dutyService.queryDutyPersonAndShiftBySiteAndTime(siteId,null, plusStartTimeStr, plusEndTimeStr);
	            Map<String, DutyPersonShiftVo>dutyPersonShiftMap=scheduleDetailService.querySchedulePersonAndShiftBySiteAndTime(siteId,null, plusStartTimeStr, plusEndTimeStr);
	            
	        	if(smbPath.isFile()){//如果是文件，直接读
	        		result.addAll(getCardDataFromSmbFile(smbPath, deviceCharset,machineBean,definitionBean,userMap,dutyPersonShiftMap,oprPersons));
	        	}else{//如果是文件夹，根据日期过滤出匹配的文件列表
	        		//处理不是以/结尾的文件夹
	        		if(!"/".equals(filePath.substring(filePath.length()-1))){
	        			smbPath=new SmbFile(filePath+"/",auth);
	        		}
	        		SmbFile[] files=smbPath.listFiles(new AttendanceSmbFilenameFilter(machineBean));//用类内实现的过滤器
	        		for (SmbFile smbFile : files) {
	        			result.addAll(getCardDataFromSmbFile(smbFile, deviceCharset,machineBean,definitionBean,userMap,dutyPersonShiftMap,oprPersons));
					}
	        	}
	        	
	        	//使用获取考勤记录命令的结束时间加一秒作为考勤机的最后打卡时间戳，用作下次取数的开始时间
	            //考勤数据插入成功才能更新
	            machineBean.setLastImport(DateFormatUtil.addDate(endTime, "s", 1));
	            
	        }else{
	        	log.error(filePath+" is not exist");
	        }
	    }catch(Exception e){
	    	log.error("error in getAttdanceFileData of "+beanInfo,e);
	    }
		
		log.info( beanInfo+" ----finish getAttdanceFileData size of data get：" + result.size() );
        
		return result;
	}

	/**
	 * 从smb文件中获取并组织cardData
	 * @throws Exception
	 */
	private List<CardDataBean> getCardDataFromSmbFile(SmbFile smbFile,String charset,
			MachineBean machineBean,DefinitionBean definitionBean,
    		Map<String, Map<String, String>>userMap,Map<String, DutyPersonShiftVo>dutyPersonShiftMap, String oprPersons) throws Exception{
		BufferedReader br=new BufferedReader(new InputStreamReader(new SmbFileInputStream(smbFile),charset));
        String line;
        Integer lineNo=0;
        List<CardDataBean> result = new ArrayList<CardDataBean>();
        try {
        	while((line=br.readLine())!=null){
            	lineNo++;
            	log.debug("line "+lineNo+":"+line);
            	if(lineNo==1)continue;
            	if(!StringUtils.isBlank(line)){
            		String[]str=line.split(",");//日期和时间,事件信息,卡号,卡用户名,用户信息userId,门
            		if(StringUtils.isBlank(str[4]))continue;//没有userId则跳过
            		String checkTime=DateFormatUtil.fromatDateString(str[0], "y/M/d H:m:s", "yyyy-MM-dd HH:mm:ss");
            		CardDataBean cardDataBean = cardDataService.setupCardDataBean(str[4], StringUtils.trim(str[3]), 
            				checkTime, definitionBean, userMap, checkIsOpr(str[4],oprPersons), dutyPersonShiftMap);
                    //设置数据来源的考勤机
                    cardDataBean.setAmId(machineBean.getAmId());
                    
                    result.add( cardDataBean );
                    
                    //将最早的打卡时间设置成考勤机的开始时间，用于更新打卡统计
                    if("".equals(machineBean.getStartTime())||
                    		cardDataBean.getCheckDate().compareTo(machineBean.getStartTime())<0){
                    	machineBean.setStartTime(cardDataBean.getCheckDate());
                    }
            	}
            }
		} catch (Exception e) {
			log.error("error in getCardDataFromSmbFile:"+smbFile.getName(), e);
		}
        br.close();
		return result;
	}
	
}

class AttendanceSmbFilenameFilter implements SmbFilenameFilter {
    private MachineBean machineBean;//smb文件名过滤接口需要使用
    
    AttendanceSmbFilenameFilter(MachineBean machineBean){
    	this.machineBean=machineBean;
    }
	/**
	 * smb文件名过滤器
	 */
	@Override
	public boolean accept(SmbFile dir, String name) throws SmbException {
		String fileDateStr=name.substring(0,10).replace("_", "-");
		String startDateStr=StringUtils.isBlank(machineBean.getStartTime())?"":machineBean.getStartTime().substring(0,10>machineBean.getStartTime().length()?machineBean.getStartTime().length():10);
		String endDateStr=machineBean.getEndTime().substring(0,10);
		if(startDateStr.compareTo(fileDateStr)<=0&&fileDateStr.compareTo(endDateStr)<=0){
			return true;
		}
		return false;
	}
}
