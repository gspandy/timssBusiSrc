package com.timss.asset.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetLayoutBean;
import com.timss.asset.dao.AssetLayoutDao;
import com.timss.asset.service.AssetLayoutService;
import com.timss.asset.util.UserPrivUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

@Service
public class AssetLayoutServiceImpl implements AssetLayoutService {
    @Autowired
    private AssetLayoutDao layoutDao;
    static Logger logger = Logger.getLogger( AssetLayoutServiceImpl.class );
    @Autowired
    private UserPrivUtil userPrivUtil;

	@Override
	public List<AssetLayoutBean> queryAllByAssetId(String assetId)
			throws Exception {
		return layoutDao.queryAllByAssetId(assetId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer batchInsert(List<AssetLayoutBean> addList, String assetId)
			throws Exception {
		UserInfoScope currUser=userPrivUtil.getUserInfoScope();
		return layoutDao.batchInsert(addList, assetId, currUser.getUserId(), currUser.getSiteId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer batchUpdate(List<AssetLayoutBean> updateList)
			throws Exception {
		UserInfoScope currUser=userPrivUtil.getUserInfoScope();
		return layoutDao.batchUpdate(updateList, currUser.getUserId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public Integer batchDelete(List<AssetLayoutBean> deleteList)
			throws Exception {
		return layoutDao.batchDelete(deleteList);
	}
}