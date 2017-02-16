package com.timss.asset.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.dao.AssetInfoDao;
import com.timss.asset.service.AssetInfoService;
import com.timss.asset.service.AttachmentService;
import com.timss.asset.util.UserPrivUtil;
import com.timss.asset.vo.AssetVo;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AssetInfoServiceImpl.java
 * @author: 890165
 * @createDate: 2014-6-20
 * @updateUser: 890147(将老的资产bean替换为新的资产bean)
 * @version: 1.1
 */
@Service
public class AssetInfoServiceImpl implements AssetInfoService {
	@Autowired
    private UserPrivUtil userPrivUtil;
    @Autowired
    private AssetInfoDao infoDao;
    @Autowired
    private AttachmentService attachmentService;
    
    static Logger logger = Logger.getLogger( AssetInfoServiceImpl.class );

    public AssetBean queryAssetDetail(String assetId) throws Exception {
    	AssetBean ret = infoDao.queryAssetInfoById( assetId );
    	AssetBean assetItem = infoDao.queryItemInfoById(assetId);
    	if(assetItem!=null && assetItem.getItemCode()!=null){
    		ret.setItemCode(assetItem.getItemCode());
    	}
    	if(assetItem!=null && assetItem.getItemName()!=null){
    		ret.setItemName(assetItem.getItemName());
    	}
    	
        ret.setAttachments(attachmentService.queryAttachmentInfo( assetId, ret.getSite() ));
        return ret;
    }
    
    /*public AssetBean queryItemInfoById(String assetId) throws Exception {
    	return infoDao.queryItemInfoById(assetId);
    }*/

    @Transactional(propagation = Propagation.REQUIRED)   // 事物控制要加这个注解
    public String insertAssetInfo(@CUDTarget AssetBean assetInfo) throws Exception {
    	UserInfoScope currUser=userPrivUtil.getUserInfoScope();
    	assetInfo.setCreateuser(currUser.getUserId());
        infoDao.insertAssetInfo( assetInfo );
        return assetInfo.getAssetId();
    }     
    
    @Transactional(propagation = Propagation.REQUIRED) 
    public int updateAssetInfo(@CUDTarget AssetBean assetInfo) throws Exception{
    	UserInfoScope currUser=userPrivUtil.getUserInfoScope();
    	assetInfo.setModifyuser(currUser.getUserId());
        return infoDao.updateAssetInfo( assetInfo );
    }
    
    @Transactional(propagation = Propagation.REQUIRED) 
    public int updateAssetLocation(String assetId,String location) throws Exception{
        return infoDao.updateAssetLocation( assetId, location );
    }

	@Override
	public List<AssetBean> queryAssetByCodeAndSite(String assetCode, String siteId)  throws Exception{
		return infoDao.queryAssetByCodeAndSite(assetCode, siteId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteAsset(String assetId) throws Exception {		
		AssetBean delNode=new AssetBean();
		delNode.setAssetId(assetId);
		List<AssetBean>list=new ArrayList<AssetBean>();
		list.add(delNode);
		UserInfoScope currUser=userPrivUtil.getUserInfoScope();
		
		//查询所有的子资产
		for(Integer i=0;i<list.size();i++){
			list.addAll(infoDao.queryChildrenNode(list.get(i).getAssetId(),true));
		}
		//删除
		for(Integer i=0;i<list.size();i++){
			infoDao.deleteAssetById(list.get(i).getAssetId(),currUser.getUserId());
		}
		logger.debug("删除节点"+assetId+"及其子节点共"+list.size()+"个");
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryAssetForHint(String site, String keyWord) throws Exception{
		List<Map<String, Object>>result=infoDao.queryAssetForHint(site,keyWord,null);
		return result;
	}

	@Override
	public List<Map<String, Object>> queryAssetForHintByCode(String site, String keyWord) throws Exception{
		List<Map<String, Object>> result = infoDao.queryAssetForHintByCode(site,keyWord);
		return result;
	}
	
	@Override
	public List<String> queryAssetParents(String assetId)throws Exception {
		List<String>result=infoDao.queryAssetParents(assetId); 
		return result;
	}

	@Override
	public AssetBean queryAssetTreeRootBySiteId(String siteId) throws Exception {
		AssetBean bean=infoDao.queryAssetTreeRootBySiteId(siteId);
		return bean;
	}

	@Override
	public List<HashMap<String,Object>> queryChildrenNode(String parentId)
			throws Exception {
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
        List<AssetBean> childrenNodes = infoDao.queryChildrenNode( parentId,null );
        Iterator<AssetBean> beans = childrenNodes.iterator();
        while(beans.hasNext()){
            AssetBean bean = beans.next();
            HashMap<String,Object> pNode = new HashMap<String, Object>();
            pNode.put("id", bean.getAssetId());
            pNode.put("text", bean.getAssetName());
            pNode.put("state","closed");
            pNode.put("type", "system");
            pNode.put("assetType", bean.getAssetType());
            pNode.put("assetCode", bean.getAssetCode());
            pNode.put("forbidDelete", bean.getForbidDelete());
            pNode.put("forbidMove", bean.getForbidMove());
            pNode.put("forbidUpdate", bean.getForbidUpdate());
            
            //是否还有子节点
            List<AssetBean> childs = infoDao.queryChildrenNode( bean.getAssetId(),null );
            if( childs != null && childs.size() > 0 ){
                pNode.put("state","closed");
            }else{
                pNode.put("state","open");
                pNode.put("children", childs );
            }
            
            ret.add( pNode );
        }
        return ret;
	}

	@Override
	public Page<AssetBean> queryAssetListByParentId(Page<AssetBean> page) throws Exception {
		List<AssetBean> list = infoDao.queryAssetListByParentId( page );
        page.setResults( list );
        return page;
	}

	@Override
	public List<AssetBean> queryByName(String siteId, String assetName,
			String parentId) throws Exception {
		return infoDao.queryByName(siteId, assetName, parentId);
	}
	
	/**
     * @description:根据astApplyId更新资产申请表的状态 
     * @author: 890199
     * @createDate: 2016-12-05
     * @param astApplyId
     * @return:受影响的行数
     */
	@Override
	public int updateAssetApply(String astApplyId){
		return infoDao.updateAssetApply(astApplyId);
	}
	
	/**
     * @description:根据assetId获取采购合同编号，物资接收单编号
     * @author: 890199
     * @createDate: 2016-12-05
     * @param assetId
     */
	@Override
	public List<AssetVo> getPurchaseList(String assetId){
		return infoDao.getPurchaseList(assetId);
	}
	
	/**
     * @description:根据站点获取关联主项目列表
     * @author: 890199
     * @createDate: 2016-12-14
     * @param siteId
     */
	@Override
	public List<AssetVo> getInvItemList(Page<AssetVo> page){
		return infoDao.getInvItemList(page);
	}
}
