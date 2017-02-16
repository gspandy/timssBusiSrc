package com.timss.operation.service.core;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.operation.bean.Dept;
import com.timss.operation.dao.DeptDao;
import com.timss.operation.service.DeptService;
import com.yudean.itc.dto.Page;

/**
 * @title: 部门service Implements
 * @description: 
 * @company: gdyd
 * @className: DeptServiceImpl.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
@Service("deptService")
@Transactional(propagation=Propagation.SUPPORTS)
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    /**
     * @description:插入一条部门
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param dept 其中id为自增，不需要设置
     * @return:Dept
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertDept(Dept dept) {
        int count = deptDao.insertDept( dept );
        return count;
    }

    /**
     * @description:更新部门表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param dept:
     * @return int
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateDept(Dept dept) {
        
        return deptDao.updateDept( dept );
    }

    /**
     * @description:通过Id拿到部门表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:Dept
     */
    public Dept queryDeptById(String id) {

        return deptDao.queryDeptById( id );
    }

    /**
     * @description:通过ID 删除 dept
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteDeptById(String id) {

        return deptDao.deleteDeptById( id );
    }

    /**
     * @description:dept分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Dept> queryDeptByPage(Page<HashMap<?, ?>> page) {
        page.setSortKey( "name" );
        page.setSortOrder( "asc" );
        
        return deptDao.queryDeptByPage( page );
    }

    /**
     * @description:部门列表 高级搜索
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param map HashMap
     * @param page HashMap
     * @return:List<Dept>
     */
    public List<Dept> queryDeptBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> page) {
        page.setParameter( "name", map.get( "name" ) );
        page.setParameter( "active", map.get( "active" ) );
        page.setParameter( "updateTime", map.get( "updateTime" ) );
        page.setParameter( "updateBy", map.get( "updateBy" ) );
        page.setParameter( "siteId", map.get( "siteId" ) );
        page.setSortKey( "name" );
        page.setSortOrder( "asc" );

        List<Dept> deptList = deptDao.queryDeptBySearch( page );
        return deptList;
    }

    @Override
    public List<Dept>queryDeptWithJobsBySiteId(String siteId)throws Exception{
    	List<Dept>result=deptDao.queryDeptWithJobsBySiteId(siteId);
    	return result;
    }
}