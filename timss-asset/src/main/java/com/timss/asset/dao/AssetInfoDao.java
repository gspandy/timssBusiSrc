package com.timss.asset.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.vo.AssetVo;
import com.yudean.itc.annotation.DynamicFormBind;
import com.yudean.itc.dto.Page;

/**
 * @title: 资产基本信息表的DAO
 * @description: {desc}
 * @company: gdyd
 * @className: AssetInfoDao.java
 * @author: 890165
 * @createDate: 2014-6-16
 * @updateUser: 890147(将老的资产bean替换为新的资产bean)
 * @version: 1.1
 */
public interface AssetInfoDao {

    /**
     * @description:插入资产数据
     * @author: 890165
     * @createDate: 2014-6-24
     * @param assetBean
     * @return:受影响的行数
     */
    @DynamicFormBind(masterKey = "assetId")
    int insertAssetInfo(AssetBean assetBean);

    /**
     * @description:更新资产数据
     * @author: 890165
     * @createDate: 2014-6-25
     * @param assetBean
     * @return:受影响的行数
     */
    @DynamicFormBind(masterKey = "assetId")
    int updateAssetInfo(AssetBean assetBean);

    /**
     * @description:更新资产的位置（拖拉拽设备树的设备到别的地方之后执行）
     * @author: 890165
     * @createDate: 2014-7-2
     * @param assetId
     * @param location
     * @return:
     */
    int updateAssetLocation(@Param("id") String assetId, @Param("parentId") String location);

    /**
     * @description:根据assetid删除资产
     * @author: 890165
     * @createDate: 2014-7-2
     * @param assetId
     * @return:受影响的行数
     */
    int deleteAssetById(@Param("assetId")String assetId,@Param("operator")String operator);

    /**
     * @description:通过ID查询资产信息
     * @author: 890165
     * @createDate: 2014-6-24
     * @param id
     * @return:
     */
    AssetBean queryAssetInfoById(String id);
    
    /**
     * @description:通过ID查询资产相关联的主项目信息
     * @author: 890199
     * @createDate: 2016-12-7
     * @param id
     * @return:
     */
    AssetBean queryItemInfoById(String id);
    
    /**
     * 根据编码查询site内的资产
     * 
     * @param assetCode
     * @param siteId
     * @return
     * @author 890147
     */
    List<AssetBean> queryAssetByCodeAndSite(@Param("code") String assetCode, @Param("siteId") String siteId);

    /**
     * 查询指定站点的资产用于模糊搜索
     * 
     * @param site
     * @param keyWord
     * @param isShowAll 是否查询所有节点，默认只查询资产，非所有隔离点
     * @return
     */
    List<Map<String, Object>> queryAssetForHint(@Param("site") String site, @Param("kw") String keyWord,
            @Param("isShowAll") Boolean isShowAll);

    /**
     * 根据物资编码查询资产卡片
     * 
     * @param site
     * @param keyWord
     * @author 890151
     * @return
     */
    List<Map<String, Object>> queryAssetForHintByCode(@Param("site") String site, @Param("kw") String keyWord);

    /**
     * 查询给定id的资产的所有父资产，直到根节点
     * 
     * @param assetId
     * @return
     */
    List<String> queryAssetParents(@Param("id") String assetId);

    /**
     * 查询站点的资产树的根节点
     * 
     * @param siteId
     * @return
     */
    AssetBean queryAssetTreeRootBySiteId(@Param("siteId") String siteId);

    /**
     * 根据给定id查树节点的所有的直接子节点
     * 
     * @param parentId
     * @param isShowAll 是否查询所有节点，默认只查询资产，非所有隔离点
     * @return
     */
    List<AssetBean> queryChildrenNode(@Param("parentId") String parentId, @Param("isShowAll") Boolean isShowAll);

    /**
     * @description:查询父资产下的子资产，分页的方式给出
     * @author: 890165
     * @createDate: 2014-6-27
     * @param page
     * @param isShowAll 是否查询所有节点，默认只查询资产，而不是所有隔离点
     * @return:
     */
    List<AssetBean> queryAssetListByParentId(Page<AssetBean> page);
    
    /**
	 * 查询指定资产名称的资产卡片
	 * 可指定所属父资产
	 * @param siteId
	 * @param assetName
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<AssetBean>queryByName(@Param("siteId")String siteId,@Param("assetName")String assetName,@Param("parentId")String parentId);
	
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
