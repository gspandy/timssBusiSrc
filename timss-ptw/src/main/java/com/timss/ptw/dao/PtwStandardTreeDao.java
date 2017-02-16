package com.timss.ptw.dao;

import java.util.List;
import java.util.Map;

import com.timss.ptw.vo.PtwStdTreeVo;

/**
 * 
 * @title: 标准树dao
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardTreeDao.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwStandardTreeDao {

    /**
     * 
     * @description:插入标准树
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param ptwStdTreeVo
     * @return:int
     */
    public int insertStandardTree(PtwStdTreeVo ptwStdTreeVo);

    /**
     * 
     * @description:更新标准树
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param ptwStdTreeVo
     * @return:int
     */
    public int updateStandardTree(PtwStdTreeVo ptwStdTreeVo);

    /**
     * 
     * @description:通过ID查询PtwStdTree
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param id
     * @return:PtwStdTreeVo
     */
    public PtwStdTreeVo queryStandardTreeById(int id);

    /**
     * 
     * @description:通过parentId查找子节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param params
     * @return:List<PtwStdTreeVo>
     */
    public List<PtwStdTreeVo> queryPtwStdByParentId(Map<String, Object> params);

    /**
     * 
     * @description:通过名字/编码查询标准树
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param map
     * @return:List<Map<String, Object>>
     */
    public List<Map<String, Object>> queryPtwStdSearch(Map<String, Object> map);

    /**
     * 
     * @description:用于搜索框的选中
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:List<String>
     */
    public List<String> searchHintPtwStdParentIds(int id);

    /**
     * 
     * @description:拖拽标准树节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param map
     * @return:int
     */
    public int updateDropStdTreeNode(Map<String, Object> map);

    /**
     * 
     * @description:通过ID删除本节点及其下属子节点
     * @author: fengzt
     * @createDate: 2014年12月29日
     * @param id
     * @return:int
     */
    public int deleteStandardTree(int id);

}
