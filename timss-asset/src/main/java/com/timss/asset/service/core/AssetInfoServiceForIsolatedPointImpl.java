package com.timss.asset.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.timss.asset.bean.AssetBean;
import com.timss.asset.dao.AssetInfoDao;
import com.yudean.itc.dto.Page;

/**
 * 资产台账用于隔离点的实现类
 * @author 890147
 *
 */
@Service
public class AssetInfoServiceForIsolatedPointImpl extends AssetInfoServiceImpl{

    @Autowired
    private AssetInfoDao infoDao;
    
    static Logger logger = Logger.getLogger( AssetInfoServiceForIsolatedPointImpl.class );

	@Override
	public List<Map<String, Object>> queryAssetForHint(String site, String keyWord) throws Exception{
		List<Map<String, Object>>result=infoDao.queryAssetForHint(site,keyWord,true);
		return result;
	}

	@Override
	public List<HashMap<String,Object>> queryChildrenNode(String parentId)
			throws Exception {
		List<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
        List<AssetBean> childrenNodes = infoDao.queryChildrenNode( parentId,true );
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
		page.setParameter("isShowAll", true);
		List<AssetBean> list = infoDao.queryAssetListByParentId( page );
        page.setResults( list );
        return page;
	}
}
