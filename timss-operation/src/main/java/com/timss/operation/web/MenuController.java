package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.Dept;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.service.DeptService;
import com.timss.operation.service.DutyService;
import com.timss.operation.service.JobsService;
import com.timss.operation.util.VOUtil;
import com.timss.operation.vo.RoleVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @title: 运行模块目录
 * @description: {desc}
 * @company: gdyd
 * @className: MenuController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/menuController")
public class MenuController {
    
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private JobsService jobsService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private DutyService dutyService;
    
    /**
     * 
     * @description:值别管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryDutyListMenu")
    @ReturnEnumsBind("OPR_YES_NO")
    public String queryDutyListMenu(){
        return "schedule/Duty-getAllDutyList.jsp";
    }
    
    /**
     * 
     * @description:班次管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryShiftListMenu")
    @ReturnEnumsBind("OPR_YES_NO")
    public String queryShiftListMenu(){
        return "schedule/Shift-getAllShiftList.jsp";
    }
    
    /**
     * 
     * @description:排班规则详细管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryRulesDetailListMenu")
    public String queryRulesDetailListMenu(){
        return "schedule/RulesDetail-getAllRulesDetailList.jsp";
    }
    
    /**
     * 
     * @description:排版查询管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryCalendarMenu")
    public String queryCalendarMenu(){
        return "schedule/ScheduleDetail-getCalendar.jsp";
    }
    
    /**
     * 
     * @description:部门管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryDeptListMenu")
    @ReturnEnumsBind("OPR_YES_NO")
    public String queryDeptListMenu(){
        return "schedule/Dept-getAllDeptList.jsp";
    }
    
    /**
     * 
     * @description:运行记事管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryNoteListMenu")
    @ReturnEnumsBind("OPR_CREW_NUM,OPR_NOTE_TYPE")
    public ModelAndView queryNoteListMenu()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //拿到站点的所有工种和岗位
        List<Dept>deptList=deptService.queryDeptWithJobsBySiteId(userInfoScope.getSiteId());
        //拿到用户所属的岗位
        String userJobsStr=jobsService.queryJobsIdStrByUserId(userInfoScope.getUserId());
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        dataMap.put( "deptList", deptList==null?"[]":VOUtil.fromVoToJsonUtil( deptList ));
        dataMap.put( "userJobsStr", userJobsStr);
        
        return new ModelAndView( "operationlog/Note-getAllNoteList.jsp", dataMap );
    }
    
    /**
     * 运行方式
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryNoteModeListMenu")
    public ModelAndView queryNoteModeListMenu()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //拿到站点的所有工种和岗位
        List<Dept>deptList=deptService.queryDeptWithJobsBySiteId(userInfoScope.getSiteId());
        //拿到用户所属的岗位
        String userJobsStr=jobsService.queryJobsIdStrByUserId(userInfoScope.getUserId());
                
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        dataMap.put( "deptList", deptList==null?"[]":VOUtil.fromVoToJsonUtil( deptList ));
        dataMap.put( "userJobsStr", userJobsStr);
        
        return new ModelAndView( "operationlog/Mode-getAllNoteModeList.jsp", dataMap );
    }
    
    /**
     * 
     * @description:定期工作管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryWorkListMenu")
    public String queryWorkListMenu(){
        return "operationlog/Note-getQuartzWorkList.jsp";
    }
    
    /**
     * 
     * @description:上级领导管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryLeaderNoticeListMenu")
    @ReturnEnumsBind("OPR_YES_NO")
    public String queryLeaderNoticeListMenu(){
        return "operationlog/LeaderNotice-getAllLeaderNotice.jsp";
    }
    
    
    /**
     * 
     * @description:日志查询管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryRecordListMenu")
    @ReturnEnumsBind("OPR_CREW_NUM,OPR_NOTE_TYPE")
    public String queryRecordListMenu(){
        return "operationlog/Note-getNoteHistory.jsp";
    }
    
    
    /**
     * 
     * @description:工种管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryJobsListMenu")
    @ReturnEnumsBind("OPR_YES_NO")
    public String queryJobsListMenu(){
        return "operationlog/Jobs-getAllJobsList.jsp";
    }
    
    
    /**
     * 
     * @description:人员值别管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryPersonDutyListMenu")
    public String queryPersonDutyListMenu(){
        return "operationlog/PersonJobs-getAllPersonJobsList.jsp";
    }
    
    /**
     * 
     * @description:运行方式设置管理目录
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:String
     */
    @RequestMapping("/queryModeListMenu")
    public String queryModeListMenu(){
        return "schedule/Mode-queryAllModeList.jsp";
    }
    
    /**
     * 
     * @description:点检日志列表
     * @author: fengtw
     * @createDate: 2015年10月29日
     * @return:String
     */
    @RequestMapping("/queryPatrolListMenu")
    @ReturnEnumsBind("OPR_SPECIALITY,OPR_PATROL_STATUS")
    public String queryPatrolListMenu(){
        return "operationlog/Patrol-getAllPatrolList.jsp";
    }
    
    /**
     * 
     * @description: 生产碰头会列表
     * @author: zhuw
     * @createDate: 2016年1月18日
     * @return:String
     */
    @RequestMapping("/queryMinuteListMenu")
    public String queryMinuteListMenu(){
        return "minute/minuteList.jsp";
    }
    
    /**
     * 定期工作计划菜单
     * @return
     * @throws Exception
     */
    @RequestMapping("/schedulePlanList")
    @ReturnEnumsBind("OPR_SCHEDULE_TYPE,OPR_SCHEDULE_CYCLE")
    public ModelAndView schedulePlanListMenu()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        
        return new ModelAndView( "scheduleTask/scheduleTaskPlanList.jsp", dataMap );
    }
    
    
    
    /**
     * @description:定期工作查询菜单
     * @author: 王中华
     * @createDate: 2016-11-30
     * @return
     * @throws Exception:
     */
    @RequestMapping("/scheduleQueryList")
    @ReturnEnumsBind("OPR_SCHEDULE_TYPE,OPR_SCHEDULE_DOSTATUS")
    public ModelAndView scheduleQueryListMenu()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId(); 
        String siteId = userInfoScope.getSiteId();
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        //获取所属的工种
        List<PersonJobs> personJobs = dutyService.queryDutyPersons( null, siteId, userId, null );
        if(personJobs.size()>0){
            dataMap.put( "stationId", personJobs.get( 0 ).getStationId() );
        }else{
            List<RoleVo> rolesList = new ArrayList<RoleVo>();
            rolesList = dutyService.queryStationInfoBySitId( siteId );
            dataMap.put( "stationId", rolesList.get( 0 ).getRoleId() );
        }
        return new ModelAndView( "scheduleTask/scheduleTaskList.jsp", dataMap );
    }
    
    
    /**
     * @description:待执行定期工作菜单
     * @author: 王中华
     * @createDate: 2016-11-30
     * @return
     * @throws Exception:
     */
    @RequestMapping("/queryToDoTaskList")
    @ReturnEnumsBind("OPR_SCHEDULE_TYPE,OPR_SCHEDULE_DOSTATUS")
    public ModelAndView queryToDoTaskListMenu()throws Exception{
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId(); 
        String siteId = userInfoScope.getSiteId();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "siteId", userInfoScope.getSiteId() );
        //获取所属的工种
        List<PersonJobs> personJobs = dutyService.queryDutyPersons( null, siteId, userId, null );
        if(personJobs.size()>0){
            dataMap.put( "stationId", personJobs.get( 0 ).getStationId() );
        }else{
            List<RoleVo> rolesList = new ArrayList<RoleVo>();
            rolesList = dutyService.queryStationInfoBySitId( siteId );
            dataMap.put( "stationId", rolesList.get( 0 ).getRoleId() );
        }
        return new ModelAndView( "scheduleTask/todoScheduleTaskList.jsp", dataMap );
    }
    
}
