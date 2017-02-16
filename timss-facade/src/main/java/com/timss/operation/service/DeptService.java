package com.timss.operation.service;

import java.util.HashMap;
import java.util.List;

import com.timss.operation.bean.Dept;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 部门service
 * @description: {desc}
 * @company: gdyd
 * @className: DeptService.java
 * @author: fengzt
 * @createDate: 2014年7月7日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DeptService {

    /**
     * 
     * @description:插入部门
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param 部门bean dept
     * @return:
     */
    int insertDept(Dept dept);

    /**
     * 
     * @description:通过高级搜索查询部门
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<Dept> queryDeptBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:部门分页
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param pageVo
     * @return:
     */
    List<Dept> queryDeptByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询部门
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    Dept queryDeptById(String id);
    
    /**
     * 
     * @description:更新部门
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param dept
     * @return:
     */
    int updateDept(Dept dept);

    /**
     * 
     * @description:删除部门
     * @author: fengzt
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deleteDeptById(String id);
    
    /**
     * 查询站点的工种及属于工种的岗位
     * @param siteId
     * @return
     * @throws Exception
     */
    List<Dept>queryDeptWithJobsBySiteId(String siteId)throws Exception;
}
