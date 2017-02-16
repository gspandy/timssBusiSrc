package com.timss.workorder.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoFaultType;
import com.timss.workorder.dao.WoFaultTypeDao;
import com.timss.workorder.service.WoFaultTypeService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class WoFaultTypeServiceImpl implements WoFaultTypeService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private WoFaultTypeDao woFaultTypeDao;
	
	private static Logger logger = Logger.getLogger(WoFaultTypeServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertWoFaultType(Map<String, String> addWoFaultTypeDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String userId = userInfoScope.getUserId();
		String woFaultTypeForm = addWoFaultTypeDataMap.get("woFaultTypeForm");
		int id = woFaultTypeDao.getNextParamsConfId();
		
		WoFaultType woFaultType = JsonHelper.fromJsonStringToBean(woFaultTypeForm, WoFaultType.class);
		woFaultType.setId(id);
		woFaultType.setCreateDate(new Date());
		woFaultType.setModifyDate(new Date());
		woFaultType.setCreateUser(userId);
		woFaultType.setModifyUser(userId);
		woFaultType.setSiteid(siteId);
		woFaultType.setYxbz(1);
		//插入数据信息
		woFaultTypeDao.insertFaultType(woFaultType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateWoFaultType(Map<String, String> addWoLabelDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		
		String woFaultTypeForm = addWoLabelDataMap.get("woFaultTypeForm");
		WoFaultType woFaultType;
		try {
			woFaultType = JsonHelper.toObject(woFaultTypeForm, WoFaultType.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		woFaultType.setModifyDate(new Date());
		woFaultType.setModifyUser(userId);
		woFaultTypeDao.updateFaultType(woFaultType);
	}

	@Override
	public WoFaultType queryWoFaultTypeById(int id) {
		return woFaultTypeDao.queryFaultTypeById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteWoFaultType(int id) {
		woFaultTypeDao.deleteFaultType(id);
	}

	@Override
	public Page<WoFaultType> queryAllFaultType(Page<WoFaultType> page) {
		List<WoFaultType> ret = woFaultTypeDao.queryWoFaultTypeList(page);
		page.setResults(ret);
		logger.info("查询故障类型列表信息");
		return page;
	}

	@Override
	public ArrayList<Map<String, Object>> queryChildrenNodes(String faultTypeId,String treeType) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		ArrayList<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		List<WoFaultType> woFaultTypeList = woFaultTypeDao.queryChildrenNodes(faultTypeId,siteId,treeType);
		Iterator<WoFaultType> beans = woFaultTypeList.iterator();
		while(beans.hasNext()) {
			WoFaultType woFaultType = beans.next();
			HashMap<String, Object> pNode = new HashMap<String, Object>();
			pNode.put("id", woFaultType.getId());
			pNode.put("text",woFaultType.getName());
			pNode.put("state", "closed");
			pNode.put("type", "system");
			pNode.put("parentId", woFaultType.getParentId());
			
			String childrenFTId = String.valueOf(woFaultType.getId());
			List<WoFaultType> woFaultTypeList2 = woFaultTypeDao.queryChildrenNodes(childrenFTId,siteId,treeType);
			if("SCROOT".equals(woFaultType.getFaultTypeCode())){
				pNode.put("iconCls", "tree-sercharacter"); 
				pNode.put("leaf", false); 
			}else{
				if(woFaultTypeList2.size()==0){  //没有子类型，显示文件图标
					pNode.put("iconCls", "tree-file"); 
					pNode.put("leaf", true); 
					pNode.put("state", "open");  //删掉叶子节点的展开三角形
				}else{   // 有子类型时，显示文件夹图标
					pNode.put("iconCls", "tree-folder"); 
					pNode.put("leaf", false); 
				}
			}
			
			
			pNode.put("faultTypeCode", woFaultType.getFaultTypeCode());
			ret.add(pNode);
		}
		return ret;
	}

	@Override
	public WoFaultType queryFaultTypeRootBySiteId(String siteId) {
		return woFaultTypeDao.queryFTRootBySiteId(siteId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int deleteFaultTypeById(int faultTypeId) {
		return woFaultTypeDao.deleteFaultTypeById(faultTypeId);
	}

	@Override
	public List<WoFaultType> queryOneLevelFTBySiteId(String siteid) {
		
		return woFaultTypeDao.queryOneLevelFTBySiteId(siteid);
	}
	 
    @Override
    public List<Map<String, Object>> queryFaultTypeForHint(String keyWord,String siteId) {
        List<Map<String, Object>> result=woFaultTypeDao.queryFaultTypeForHint(keyWord, siteId);
        return result;
    }
}
