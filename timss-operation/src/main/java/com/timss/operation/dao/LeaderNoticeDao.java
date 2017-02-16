package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.LeaderNotice;
import com.timss.operation.vo.LeaderNoticeVo;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: LeaderNoticeMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: LeaderNoticeMapper.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface LeaderNoticeDao {
        
    /**
     * @description:插入一条上级通知
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param leaderNoticeVo  其中id为自增，不需要设置
     */
    public void insertLeaderNotice(LeaderNoticeVo leaderNoticeVo );
    
    /**
     * 
     * @description:更新上级通知表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param LeaderNotice:
     * @return int 更新个数
     */
    public int updateLeaderNotice(LeaderNoticeVo leaderNoticeVo);
    
    /**
     * 
     * @description:通过Id拿到上级通知表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:LeaderNotice
     */
    public LeaderNoticeVo queryLeaderNoticeById(int id);
    
    /**
     * 
     * @description:通过ID 删除 leaderNotice
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteLeaderNoticeById(int id);

    /**
     * 
     * @description:leaderNotice 分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<LeaderNotice> queryLeaderNoticeByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询上级通知列表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<LeaderNotice>
     */
    public List<LeaderNotice> queryLeaderNoticeBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有上级通知leaderNotice
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<LeaderNotice> queryAllLeaderNotice();
}
