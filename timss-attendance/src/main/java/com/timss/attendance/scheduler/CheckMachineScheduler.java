package com.timss.attendance.scheduler;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.timss.attendance.service.CardDataService;
import com.timss.attendance.service.CheckMachineService;
import com.timss.attendance.service.WorkStatusService;
import com.timss.attendance.util.DateFormatUtil;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.manager.support.IConfigurationManager;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 考勤机相关定时任务
 */
@Component
@Lazy(false)
public class CheckMachineScheduler {

    private Logger log = LoggerFactory.getLogger( CheckMachineScheduler.class );
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private CheckMachineService checkMachineService;
    
    @Autowired
    private WorkStatusService workStatusService;
    
    @Autowired
    private CardDataService cardDataService;
    
    @Autowired
    private IConfigurationManager confManager;
    
    /**
     * 清除过期的考勤机打卡数据
     * @throws Exception
     */
    @Scheduled(cron = "0 30 2 * * ?")
    public void clearCheckMachineData() throws Exception{
    	Configuration configuration = confManager.query( "num_of_retain_atd_machine_data_years", "NaN", "NaN" );
    	Date now=new Date();
    	Date clearDate=DateFormatUtil.addDate(now, "y", -(Integer.parseInt(configuration.getVal())));//清理该日期前的数据
    	String clearDateStr = DateFormatUtil.formatDate( clearDate, "yyyy-MM-dd HH:mm:ss" );
    	log.info("Atd scheduled--clearCheckMachineData--begin");
    	log.info("开始清理日期"+clearDateStr+"之前的打卡记录");
    	Integer cardDataNum=cardDataService.deleteCardDataByEndTime(clearDateStr,null);
    	Integer workStatusNum=workStatusService.deleteWorkStatusByEndTime(clearDateStr,null);
    	log.info("清理日期"+clearDateStr+"之前的打卡记录完成，共清理打卡记录："+cardDataNum+"，共清理打卡管理："+workStatusNum);
    	log.info("Atd scheduled--clearCheckMachineData--end");
    }
    
    /**
     * @description:导入考勤机数据 更新或插入打卡信息
     * @author: yyn
     * @createDate: 2015年12月28日
     * @throws Exception:
     */
    @Scheduled(cron = "0 0/30 3,5 * * ?")//能触发更新打卡统计
    //@Scheduled(cron = "0 45 15 * * ?")
    public void importCheckMachineData() throws Exception{
    	log.info("Atd scheduled--importCheckMachineData--begin");
        //UserInfo userInfo = itcMvcService.getUserInfo( "180811", "ZJW" );
        //ThreadLocalHandler.createNewVarableOweUserInfo( userInfo );

        checkMachineService.importCheckMachineData(null);
        log.info("Atd scheduled--importCheckMachineData--end");
    }
    
    /**
     * 更新考勤结果（打卡管理数据）
     * @throws Exception
     */
    @Scheduled(cron = "0 5 0 * * ?")//用于生成当天数据
    //@Scheduled(cron = "0 55 15 * * ?")
    public void checkWorkStatus() throws Exception{
    	log.info("Atd scheduled--checkWorkStatus--begin");
        workStatusService.checkWorkStatus(null);
        log.info("Atd scheduled--checkWorkStatus--end");
    }
    
    /**
     * 检查考勤机的状态，如果异常则发送通知
     * @throws Exception
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkMachineStatus() throws Exception {
    	log.info("Atd scheduled--checkMachineStatus--begin");
        checkMachineService.checkMachineStatusAndNotify(null);
        log.info("Atd scheduled--checkMachineStatus--end");
    }
    
    //@Scheduled(cron = "0/5 40 10 * * ?")
    public void testScheduled() throws Exception {
    	log.info("定时任务--测试:"+DateFormatUtil.dateToString( new Date(), "yyyy-MM-dd hhmmss" ));
    }
}
