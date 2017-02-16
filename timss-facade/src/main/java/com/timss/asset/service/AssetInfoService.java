package com.timss.asset.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.vo.AssetVo;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AssetInfoService.java
 * @author: 890165
 * @createDate: 2014-6-17
 * @updateUser: 890147(将老的资产bean替换为新的资产bean)
 * @version: 1.1
 */
public interface AssetInfoService {

    /**
     * 
     * @description:根据资产ID获取资产详细信息
     * @author: 890165
     * @createDate: 2014-6-18
     * @param assetId
     * @param userInfoScope
     * @return
     * @throws Exception:
     */
    AssetBean queryAssetDetail(String assetId) throws Exception;
    
    /**
     * @description:通过ID查询资产相关联的主项目信息
     * @author: 890199
     * @createDate: 2016-12-7
     * @param id
     * @return:
     */
    //AssetBean queryItemInfoById(String assetId) throws Exception;
  
    /**
     * 
     * @description:插入成功后自动返回主键
     * @author: 890165
     * @createDate: 2014-6-24
     * @param assetInfo
     * @return:
     */
    String insertAssetInfo(@CUDTarget AssetBean assetInfo ) throws Exception;
        
    /**
     * 
     * @description:更新整个资产设备的信息
     * @author: 890165
     * @createDate: 2014-6-25
     * @param asestInfo
     * @return:更新成功的行数
     */
    int updateAssetInfo(@CUDTarget AssetBean assetInfo) throws Exception;
    
    /**
     * 
     * @description:更新资产的位置（拖拉拽设备树的设备之后的操作），有用
     * @author: 890165
     * @createDate: 2014-7-2
     * @param assetId
     * @param location
     * @return 更新成功的行数
     * @throws Exception:
     */
    int updateAssetLocation(String assetId,String location) throws Exception;

    /**
     * 根据编码查询site内的资产
     * @param assetCode
     * @param siteId
     * @return
     * @author 890147
     */
	List<AssetBean> queryAssetByCodeAndSite(String assetCode,String siteId) throws Exception;

	/**
	 * 根据id删除资产及其子资产
	 * @param assetId
	 * @return
	 */
	int deleteAsset(String assetId) throws Exception;

	/**
	 * 查询指定站点的资产用于模糊搜索
	 * @param site
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> queryAssetForHint(String site, String keyWord) throws Exception;

	/**
	 * 查询指定站点的资产用于模糊搜索
	 * @param site
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> queryAssetForHintByCode(String site, String keyWord) throws Exception;

	/**
	 * 查询给定id的资产的所有父资产，直到根节点
	 * @param assetId
	 * @return
	 */
	List<String> queryAssetParents(String assetId) throws Exception;   
	
	/**
	 * 查询站点的资产树的根节点
	 * @param siteId
	 * @return
	 */
	AssetBean queryAssetTreeRootBySiteId(String siteId) throws Exception;
	
	/**
     * 根据给定id查树节点的所有的直接子节点
     * @param parentId
     * @param isShowAll 是否查询所有节点，默认只查询资产，非所有隔离点
     * @return
     */
	List<HashMap<String,Object>> queryChildrenNode(String parentId) throws Exception;
    
    /**
     * 
     * @description:查询父资产下的子资产，分页的方式给出
     * @author: 890165
     * @createDate: 2014-6-27
     * @param page
     * @param isShowAll 是否查询所有节点，默认只查询资产，而不是所有隔离点
     * @return:
     */
	Page<AssetBean> queryAssetListByParentId(Page<AssetBean> page) throws Exception;
	
	/**
	 * 查询指定资产名称的资产卡片
	 * 可指定所属父资产
	 * @param siteId
	 * @param assetName
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<AssetBean>queryByName(String siteId,String assetName,String parentId)throws Exception;

	/**
     * @description:根据astApplyId更新资产申请表的状态 
     * @author: 890199
     * @createDate: 2016-12-05
     * @param astApplyId
     * @return:受影响的行数
     */
	int updateAssetApply(String astApplyId);
	
	/**
     * @description:根据assetId获取采购合同编号，物资接收单编号
     * @author: 890199
     * @createDate: 2016-12-05
     * @param assetId
     */
	List<AssetVo> getPurchaseList(String assetId);
	
	/**
     * @description:根据站点获取关联主项目列表
     * @author: 890199
     * @createDate: 2016-12-14
     * @param siteId
     */
	List<AssetVo> getInvItemList(Page<AssetVo> page);
}
