package com.timss.ptw.service;

import java.util.List;
import java.util.Map;

import com.timss.ptw.vo.PtwStdTreeVo;

/**
 * 
 * @title: 标准树service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardTreeService.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface PtwStandardTreeService {

    /**
     * 
     * @description:插入或者更新标准树
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param ptwStdTreeVo
     * @return: Map<String, Object>
     */
    Map<String, Object> insertOrUpdateStandard(PtwStdTreeVo ptwStdTreeVo);

    /**
     * 
     * @description:通过ID查询PtwStdTree
     * @author: fengzt
     * @createDate: 2014年12月25日
     * @param id
     * @return:PtwStdTreeVo
     */
    PtwStdTreeVo queryStandardTreeById( int id );

    /**
     * 
     * @description:查找根节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @return:PtwStdTreeVo
     */
    PtwStdTreeVo queryPtwStdRootIdBySite();

    /**
     * 
     * @description:通过parentId查找子节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param params
     * @return:List<PtwStdTreeVo>
     */
    List<PtwStdTreeVo> queryPtwStdByParentId(Map<String, Object> params);


    /**
     * 
     * @description:通过名字/编码查询标准树
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param kw
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> queryPtwStdSearch(String kw);

    /**
     * 
     * @description:用于搜索框的选中
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @return:List<String>
     */
    List<String> searchHintPtwStdParentIds(int id);

    /**
     * 
     * @description:拖拽标准树节点
     * @author: fengzt
     * @createDate: 2014年12月26日
     * @param id
     * @param parentId
     * @return:int
     */
    int updateDropStdTreeNode(int id, int parentId);

    /**
     * 
     * @description:通过ID删除本节点及其下属子节点
     * @author: fengzt
     * @createDate: 2014年12月29日
     * @param id
     * @return:int
     */
    int deleteStandardTree(int id);
}
