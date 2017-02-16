package com.timss.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.timss.operation.bean.Note;
import com.timss.operation.bean.ScheduleTask;
import com.timss.operation.service.HandoverService;
import com.timss.operation.service.ModeService;
import com.timss.operation.service.NoteService;
import com.timss.operation.service.ScheduleTaskService;
import com.timss.operation.util.DateFormatUtil;
import com.timss.operation.util.VOUtil;
import com.timss.operation.util.ReturnCodeUtil;
import com.timss.operation.vo.HandoverVo;
import com.timss.operation.vo.NoteVo;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;

/**
 * @title: 运行记事controller
 * @description:
 * @company: gdyd
 * @className: NoteController.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
@Controller
@RequestMapping("operation/note")
public class NoteController {

    private Logger log = Logger.getLogger( NoteController.class );
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private ModeService modeService;
    
    @Autowired
    private HandoverService handoverService;
    /**
     * @description:新建一条运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertNoteItemToPage")
    @ReturnEnumsBind("OPR_CREW_NUM,OPR_NOTE_TYPE")
    public ModelAndView insertNoteItemToPage( int noteId ) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        map.put( "noteId", noteId );
        map.put( "siteId", userInfoScope.getSiteId() );
        
        return new ModelAndView( "operationlog/Note-insertNoteItem.jsp", map );
    }
    
    /**
     * @description:查询一条运行记事详情
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/queryNoteDetailPage")
    @ReturnEnumsBind("OPR_CREW_NUM,OPR_NOTE_TYPE")
    public ModelAndView queryNoteDetailPage( int shiftId, int handoverId, String dateStr,
            String stationId, int jobsId, int dutyId ) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        //运行方式分组
        List<String> teams = modeService.queryModeTeamByJobsId( jobsId );
        
        map.put( "shiftId", shiftId );
        map.put( "handoverId", handoverId );
        map.put( "dateStr", dateStr );
        map.put( "stationId", stationId );
        map.put( "jobsId", jobsId );
        map.put( "dutyId", dutyId );
        map.put( "siteId", userInfoScope.getSiteId() );
        if( teams != null && teams.size() > 0 ){
            map.put( "teams", VOUtil.fromVoToJsonUtil( teams ) );
        }
        
        return new ModelAndView( "operationlog/Note-getNoteDetail.jsp", map );
    }
    /**
     * @description:新建一条运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     */
    @RequestMapping("/insertNote")
    public Map<String, Object> insertNote(String formData, int dutyId, int jobsId, int handoverId ) {
        
        Note note = VOUtil.fromJsonToVoUtil( formData, Note.class );
        note.setDutyId( dutyId );
        note.setJobsId( jobsId );
        note.setHandoverId( handoverId );
        if( StringUtils.isBlank( note.getType() ) ){
            note.setType( "" );
        }
        
        if( StringUtils.isBlank( note.getCrewNum() )){
            note.setCrewNum( "" );
        }
        
        note = noteService.insertNote( note );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( note != null && note.getId() > 0 ) {
            map.put( "result", "success" );
        } else {
            /*if( "您不是当值人员！".equals( note.getCreateBy() ) ){
                map.put( "reason", "您不是当值人员！" );
            }*/
            map.put( "result", "fail" );
        }
        return map;
        
    }

    /**
     * 引用运行记事
     * @param formData
     * @return
     */
    @RequestMapping("/useNote")
    public Map<String, Object> useNote(String formData) {
        Note note = VOUtil.fromJsonToVoUtil( formData, Note.class );
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object>handoverMap = handoverService.queryNowAndLastHandoverVo(""+note.getJobsId());
        HandoverVo currentHandoverVo = null;
        if( handoverMap.containsKey( "currentHandover" )){
            currentHandoverVo = ( HandoverVo )handoverMap.get( "currentHandover" );
            note.setDutyId( currentHandoverVo.getCurrentDutyId() );
            note.setHandoverId( currentHandoverVo.getId() );
            if( StringUtils.isBlank( note.getType() ) ){
                note.setType( "" );
            }
            if( StringUtils.isBlank( note.getCrewNum() )){
                note.setCrewNum( "" );
            }
            note = noteService.insertNote( note );
        }
        
        if ( currentHandoverVo!=null&& note != null && note.getId() > 0 ) {
        	resultMap.put( "result", "success" );
        } else {
        	resultMap.put( "result", "fail" );
        }
        return resultMap;
    }
    
    private void setNoteSearchParams(Page<HashMap<?, ?>>pageVo) throws Exception{
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	pageVo.setPageNo( 1 );
        pageVo.setPageSize( 9999 );
        
        Map<String, String[]> params = userInfoScope.getParamMap();
        if (params.containsKey("search")) {
			String fuzzySearchParams = userInfoScope.getParam("search");
			Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( fuzzySearchParams );
			pageVo.setFuzzyParams(fuzzyParams);
		}
        // 设置排序内容
        if ( params.containsKey( "sort" ) ) { 
            String sortKey = userInfoScope.getParam( "sort" );
            String sortOrder=userInfoScope.getParam( "order" );
            pageVo.setSortKey( sortKey+" "+sortOrder+",id " );
            pageVo.setSortOrder( sortOrder );
        } else {
            // 设置默认的排序字段
        	pageVo.setSortKey( "writeTime,id" );
        }
    }
    
    /**
     * @description:当前用户运行记事列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param rows 一页有多少条
     * @param page 第几页
     * @param jobsId 工种ID
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping("/queryNoteListByCurrentUser")
    public Map<String, Object> queryNoteListByCurrentUser(
            String jobsId,String handoverId ) throws Exception {
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        setNoteSearchParams(pageVo);
        
        pageVo.setParameter( "jobsId", jobsId );
        pageVo.setParameter( "handoverId", handoverId );
        
        List<Note> noteList = new ArrayList<Note>();
        //默认分页
        noteList = noteService.queryNoteBySearch( pageVo );

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", noteList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    /**
     * @description:更新运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/updateNote") 
    public Map<String, Object> updateNote(String formData, int jobsId ) {

        Note note = VOUtil.fromJsonToVoUtil( formData, Note.class );
        note.setJobsId( jobsId );
        
        if( StringUtils.isBlank( note.getType() ) ){
            note.setType( "" );
        }
        
        if( StringUtils.isBlank( note.getCrewNum() )){
            note.setCrewNum( "" );
        }

       int count = noteService.updateNote( note );

        Map<String, Object> map = new HashMap<String, Object>();

        if ( count > 0 && count != 10000 ) {
            map.put( "result", "success" );
        } else {
           /* if( count == 10000 ){
                map.put( "reason", "您不是当值人员！" );
            }*/
            map.put( "result", "fail" );
        }
        return map;

    }
    
    /**
     * @description:删除运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param formData 对象JSON
     * @return:Map<String, Object>
     */
    @RequestMapping("/deleteNote")
    public Map<String, Object> deleteNote(int id) {
        
        int count = noteService.deleteNoteById( id );
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        if ( count > 0 && count != 10000 ) {
            map.put( "result", "success" );
        } else {
           /* if( count == 10000 ){
                map.put( "reason", "您不是当值人员！" );
            }*/
            map.put( "result", "fail" );
        }
        return map;
        
    }
    /**
     * 
     * @description:查找运行记事by id
     * @author: fengzt
     * @createDate: 2014年7月8日
     * @param id
     * @return:Map
     */
    @RequestMapping("/queryNoteById")
    public @ResponseBody Map<String, Object> queryNoteById( int id ){
        Note note = noteService.queryNoteById( id );
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put( "result", note );
        return map;
    }
    
    
    /**
     * 
     * @description:通过岗位、工种、班次、时间拿到运行记事历史
     * @author: fengzt
     * @createDate: 2014年7月17日
     * @param stationId
     * @param jobsId
     * @param shiftId
     * @param date
     * @return:Map<String, Object>
     * @throws Exception 
     */
    @RequestMapping("/queryNoteHistory")
    public Map<String, Object> queryNoteHistory( String stationId ,String jobsId, String shiftId, String date ) throws Exception{
        Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        setNoteSearchParams(pageVo);
        pageVo.setParameter( "nowscheduleDate", DateFormatUtil.parseDate( date, "yyyy-MM-dd" ) );
        pageVo.setParameter( "deptId", stationId );
        pageVo.setParameter( "jobsId", jobsId );
        pageVo.setParameter( "shiftId", shiftId );
        //默认分页
        List<Note> noteList = noteService.queryNoteBySearch( pageVo );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", noteList );
        if( !noteList.isEmpty() ){
            dataMap.put( "total", noteList.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        return dataMap;
    }
    
    /**
     * 
     * @description:获取当前值班的值别、班次、人员、上一班情况等基本信息
     * @author: huanglw
     * @createDate: 2014年7月15日
     * @return:Map
     * @throws Exception 
     */
    @RequestMapping("/queryOnDutyBaseInfo")
    public ModelAndViewAjax queryOnDutyBaseInfo(String jobsId,String stationId) throws Exception{
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //当前登录用户的工号
        String userId = null;
        try {
            UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
            userId = userInfoScope.getUserId();
        } catch (Exception e) {
            log.error( e.getMessage() );
        }
        resultMap = noteService.queryOnDutyBaseInfo( stationId,jobsId );
        if( resultMap.get( "returnCode" ).equals( ReturnCodeUtil.SUCCESS ) ){
            resultMap.put( "loginUserId", userId );
            resultMap.put( "success", 1 );
        }else{
            resultMap.put( "success", 0 );
        }
        
        return itcMvcService.jsons( resultMap );
    }
    
    /**
     * 
     * @description:日志查询获取基本信息
     * @author: huanglw
     * @createDate: 2014年7月15日
     * @return:Map
     * @throws Exception 
     */
    @RequestMapping("/queryNoteBaseInfoBySearch")
    public @ResponseBody Map<String, Object> queryNoteBaseInfoBySearch(String stationId, String jobsId, String shiftId, String date) throws Exception{
        Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap = noteService.queryNoteBaseInfoBySearch( stationId,jobsId,shiftId,date );
            /*if( resultMap.get( "returnCode" ).equals( returnCodeUtil.SUCCESS ) ){
                resultMap.put( "success", 1 );
            }*/
        
        return resultMap;
    }
    
    @RequestMapping("/queryNoteListByContent")
    public Map<String, Object> queryNoteListByContent( String content ) throws Exception{
    	Page<HashMap<?, ?>> pageVo = new Page<HashMap<?, ?>>();
        setNoteSearchParams(pageVo);
        pageVo.setFuzzyParameter("content", content);
        //默认分页
        List<Note> noteList = noteService.queryNoteBySearch( pageVo );
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", noteList );
        dataMap.put( "total", pageVo.getTotalRecord() );
        return dataMap;
    }
    
    @RequestMapping("/queryNoteListByContentPage")
    @ReturnEnumsBind("OPR_CREW_NUM,OPR_NOTE_TYPE")
    public ModelAndView queryNoteListByContentPage() throws Exception{
        return  new ModelAndView( "operationlog/Note-getNoteList.jsp", new HashMap<String, Object>() );
    }
}
