package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Dept;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: DeptMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: DeptMapper.java
 * @author: fengzt
 * @createDate: 2014年7月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface DeptDao {
	
    /**
     * @description:插入一条部门
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param dept  其中id为自增，不需要设置
     * @return 
     */
    public int insertDept(Dept dept );
    
    /**
     * 
     * @description:更新部门表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param Dept:
     * @return int 更新个数
     */
    public int updateDept(Dept Dept);
    
    /**
     * 
     * @description:通过Id拿到部门表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id
     * @return:Dept
     */
    public Dept queryDeptById(String id);
    
    /**
     * 
     * @description:通过ID 删除 dept
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteDeptById(String id);

    /**
     * 
     * @description:dept 分页
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Dept> queryDeptByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询部门列表
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<Dept>
     */
    public List<Dept> queryDeptBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有部门dept
     * @author: fengzt
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<Dept> queryAllDept();
    
    /**
     * 查询站点的工种及属于工种的岗位
     * @param siteId
     * @return
     */
    List<Dept>queryDeptWithJobsBySiteId(@Param("siteId")String siteId);
}
