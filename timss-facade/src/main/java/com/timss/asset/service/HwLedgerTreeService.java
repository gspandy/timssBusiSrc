package com.timss.asset.service;

import java.util.List;
import java.util.Map;

import com.timss.asset.bean.HwLedgerBean;

/**
 * 
 * @title: 硬件台账树
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerTreeService.java
 * @author: fengzt
 * @createDate: 2014年12月12日
 * @updateUser: fengzt
 * @version: 1.0
 */
public interface HwLedgerTreeService {

    /**
     * 
     * @description:通过站点查找根节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return:String
     */
    String queryHwLedgerRootIdBySite();
    
    /**
     * 
     * @description:通过id找到子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<HwLedgerBean>
     */
    List<HwLedgerBean> queryHwLedgerChildren(String id);
    
    /**
     * 
     * @description:用于搜索框的查询
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param kw
     * @return:List<Map<String, Object>>
     */
    List<Map<String, Object>> searchHwLedgerHint(String kw);
    
    /**
     * 
     * @description:搜索子节点
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @param id
     * @return:List<String>
     */
    List<String> searchHintHwLedgerParentIds(String id);

    /**
     * 
     * @description:查找根节点信息
     * @author: fengzt
     * @createDate: 2014年11月24日
     * @return: Map<String, Object>
     */
    List<Map<String, Object>> queryHwLedgerByRoot();

    /**
     * 
     * @description:拖动硬件台账树节点
     * @author: fengzt
     * @createDate: 2014年12月15日
     * @param id
     * @param parentId
     * @return:int
     */
    int updateDropHwlTreeNode(String id, String parentId);
    
    
}
