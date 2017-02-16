package com.timss.operation.service.core;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.LeaderNotice;
import com.timss.operation.dao.LeaderNoticeDao;
import com.timss.operation.service.LeaderNoticeService;
import com.timss.operation.vo.LeaderNoticeVo;
import com.yudean.itc.dto.Page;

/**
 * @title: 上级通知service Implements
 * @description: 
 * @company: gdyd
 * @className: LeaderNoticeServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("leaderNoticeService")
@Transactional(propagation=Propagation.SUPPORTS)
public class LeaderNoticeServiceImpl implements LeaderNoticeService {

    @Autowired
    private LeaderNoticeDao leaderNoticeDao;

    public LeaderNoticeDao getLeaderNoticeDao() {
        return leaderNoticeDao;
    }

    public void setLeaderNoticeDao(LeaderNoticeDao leaderNoticeDao) {
        this.leaderNoticeDao = leaderNoticeDao;
    }

    /**
     * @description:插入一条上级通知
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param leaderNotice 其中id为自增，不需要设置
     * @return:LeaderNotice
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public LeaderNoticeVo insertLeaderNotice(LeaderNoticeVo leaderNoticeVo) {
        leaderNoticeDao.insertLeaderNotice( leaderNoticeVo );
        return leaderNoticeVo;
    }

    /**
     * @description:更新上级通知表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param leaderNotice:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateLeaderNotice(LeaderNoticeVo leaderNoticeVo) {
        
        return leaderNoticeDao.updateLeaderNotice( leaderNoticeVo );
    }

    /**
     * @description:通过Id拿到上级通知表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:LeaderNotice
     */
    public LeaderNoticeVo queryLeaderNoticeById(int id) {

        return leaderNoticeDao.queryLeaderNoticeById( id );
    }

    /**
     * @description:通过ID 删除 leaderNotice
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteLeaderNoticeById(int id) {

        return leaderNoticeDao.deleteLeaderNoticeById( id );
    }

    /**
     * @description:leaderNotice分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<LeaderNotice> queryLeaderNoticeByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );
        
        return leaderNoticeDao.queryLeaderNoticeByPage( page );
    }

    /**
     * @description:上级通知列表 高级搜索
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<LeaderNotice>
     */
    public List<LeaderNotice> queryLeaderNoticeBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "id", map.get( "id" ) );
        page.setParameter( "num", map.get( "num" ) );
        page.setParameter( "writeDate", map.get( "writeDate" ) );
        page.setParameter( "leaderUserId", map.get( "leaderUserName" ) );
        page.setParameter( "content", map.get( "content" ) );
        page.setParameter( "writeUserId", map.get( "writeUserName" ) );
        page.setParameter( "executeUserId", map.get( "executeUserName" ) );
        page.setParameter( "finishTime", map.get( "finishTime" ) );
        page.setParameter( "updateTime", map.get( "updateTime" ) );
        page.setParameter( "feedBackContent", map.get( "feedBackContent" ) );
        
        page.setSortKey( "id" );
        page.setSortOrder( "asc" );

        List<LeaderNotice> leaderNoticeList = leaderNoticeDao.queryLeaderNoticeBySearch( page );
        return leaderNoticeList;
    }

}