package com.timss.operation.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.Note;
import com.timss.operation.vo.NoteVo;
import com.yudean.itc.dto.Page;


/**
 * 
 * @title: 运行记事Service
 * @description: 
 * @company: gdyd
 * @className: NoteService.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface NoteService {
    
    /**
     * 
     * @description:插入一条运行记事
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param 运行记事bean note
     * @return:
     */
    Note insertNote(Note note);

    /**
     * 
     * @description:通过高级搜索查询运行记事
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<Note> queryNoteBySearch(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询运行记事
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    Note queryNoteById(int id);
    
    /**
     * 
     * @description:更新运行记事
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param note
     * @return:
     */
    int updateNote(Note note);

    /**
     * 
     * @description:删除运行记事
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deleteNoteById(int id);

    /**
     * 
     * @description:获取当值的值别、班次、值别人员、上一班情况,若没有数据则根据当前时间初始化
     * @author: huanglw
     * @param jobsId 
     * @createDate: 2014年7月15日
     * @return:
     */
    Map<String, Object> queryOnDutyBaseInfo(String stationId, String jobsId)throws Exception;
    
    /**
     * 
     * @description:通过员工号检测这个人是否在值别
     * @author: fengzt
     * @createDate: 2014年7月21日
     * @param userId
     * @return boolean
     * @throws ParseException:
     */
    boolean isOnDuty( String userId ) throws ParseException;
    
    /**
     * 
     * @description:登录用户是否在值班
     * @author: fengzt
     * @createDate: 2014年7月21日
     * @return boolean
     * @throws ParseException:
     */
    boolean isOnDutyByLoginUser() throws ParseException;

    /**
     * 
     * @description:查询运行日志时获取基本信息和交接班信息
     * @author: huanglw
     * @createDate: 2014年7月21日
     * @param stationId
     * @param jobsId 工种id
     * @param shiftId 班次id
     * @param date 日期
     * @return:Map<String, Object>
     * @throws Exception 
     */
    Map<String, Object> queryNoteBaseInfoBySearch(String stationId, String jobsId, String shiftId, String date) throws Exception;

}
