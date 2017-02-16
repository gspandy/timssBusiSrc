package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.Note;
import com.timss.operation.bean.Shift;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: NoteMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: NoteMapper.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface NoteDao {
	
    /**
     * @description:插入一条运行记事
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param note  其中id为自增，不需要设置
     */
    public int insertNote(Note note );
    
    /**
     * 
     * @description:更新运行记事表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param Note:
     * @return int 更新个数
     */
    public int updateNote(Note Note);
    
    /**
     * 
     * @description:通过Id拿到运行记事表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:Note
     */
    public Note queryNoteById(int id);
    
    /**
     * 
     * @description:通过ID 删除 note
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteNoteById(int id);
    
    /**
     * 
     * @description:高级搜索 查询运行记事列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<Note>
     */
    public List<Note> queryNoteBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有运行记事note
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<Note> queryAllNote();

    /**
     * 
     * @description:现在用户是否值班
     * @author: fengzt
     * @createDate: 2014年7月21日
     * @param map
     * @return:List<Shift>
     */
    public List<Shift> isOnDuty(Map<String, Object> map);

}
