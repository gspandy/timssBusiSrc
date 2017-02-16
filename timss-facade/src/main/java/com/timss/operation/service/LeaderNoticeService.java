package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.LeaderNotice;
import com.timss.operation.vo.LeaderNoticeVo;
import com.yudean.itc.dto.Page;


/**
 * 
 * @title: 上级通知Service
 * @description: 
 * @company: gdyd
 * @className: LeaderNoticeService.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface LeaderNoticeService {

    /**
     * 
     * @description:插入一条上级通知
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param 上级通知bean note
     * @return:
     */
    LeaderNoticeVo insertLeaderNotice(LeaderNoticeVo leaderNoticeVo);

    /**
     * 
     * @description:通过高级搜索查询上级通知
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<LeaderNotice> queryLeaderNoticeBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:上级通知分页
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param pageVo
     * @return:
     */
    List<LeaderNotice> queryLeaderNoticeByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询上级通知
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    LeaderNoticeVo queryLeaderNoticeById(int id);
    
    /**
     * 
     * @description:更新上级通知
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param leaderNoticeVo
     * @return:
     */
    int updateLeaderNotice(LeaderNoticeVo leaderNoticeVo);

    /**
     * 
     * @description:删除上级通知
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deleteLeaderNoticeById(int id);

}
