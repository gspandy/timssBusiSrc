package com.timss.itsm.service.core;

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

import com.timss.itsm.bean.ItsmWoFaultType;
import com.timss.itsm.dao.ItsmWoFaultTypeDao;
import com.timss.itsm.service.ItsmWoFaultTypeService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmWoFaultTypeServiceImpl implements ItsmWoFaultTypeService {
	@Autowired
	private ItcMvcService ItcMvcService;
	@Autowired
	private ItsmWoFaultTypeDao woFaultTypeDao;
	
	private static String DefaultSiteId = "ITC";
	private static final Logger LOG = Logger.getLogger(ItsmWoFaultTypeServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void insertWoFaultType(Map<String, String> addWoFaultTypeDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		String userId = userInfoScope.getUserId();
		String woFaultTypeForm = addWoFaultTypeDataMap.get("woFaultTypeForm");
		int id = woFaultTypeDao.getNextParamsConfId();
		
		ItsmWoFaultType woFaultType = JsonHelper.fromJsonStringToBean(woFaultTypeForm, ItsmWoFaultType.class);
		
		if(woFaultType.getParentId() != null){
			ItsmWoFaultType parentFaultType = woFaultTypeDao.queryOneLeveFTById(woFaultType.getParentId());
			if(parentFaultType.getParentId()!=null){
				woFaultType.setOneLevelId(parentFaultType.getOneLevelId());
			}
		}
		
		
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void updateWoFaultType(Map<String, String> addWoLabelDataMap) {
		UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		
		String woFaultTypeForm = addWoLabelDataMap.get("woFaultTypeForm");
		ItsmWoFaultType woFaultType;
		try {
			woFaultType = JsonHelper.toObject(woFaultTypeForm, ItsmWoFaultType.class);
			
			if(woFaultType.getParentId() != null){
				ItsmWoFaultType parentFaultType = woFaultTypeDao.queryOneLeveFTById(woFaultType.getParentId());
				if(parentFaultType.getParentId()!=null){
					woFaultType.setOneLevelId(parentFaultType.getOneLevelId());
				}
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		} 
		woFaultType.setModifyDate(new Date());
		woFaultType.setModifyUser(userId);
		woFaultTypeDao.updateFaultType(woFaultType);
	}

	@Override
	public ItsmWoFaultType queryWoFaultTypeById(int id) {
		return woFaultTypeDao.queryFaultTypeById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public void deleteWoFaultType(int id) {
		woFaultTypeDao.deleteFaultType(id);
	}

	@Override
	public Page<ItsmWoFaultType> queryAllFaultType(Page<ItsmWoFaultType> page) {
		page.setParameter("siteId", DefaultSiteId);
		List<ItsmWoFaultType> ret = woFaultTypeDao.queryWoFaultTypeList(page);
		page.setResults(ret);
		LOG.info("查询故障类型列表信息");
		return page;
	}

	@Override
	public ArrayList<Map<String, Object>> queryChildrenNodes(String faultTypeId,String treeType,String siteid) {
		
		ArrayList<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		//ITC站点的服务目录
                List<ItsmWoFaultType> woFaultTypeList = woFaultTypeDao.queryChildrenNodes(faultTypeId,DefaultSiteId,treeType);
		if(siteid != null){
		  //各单位站点的服务目录
	           woFaultTypeList = woFaultTypeDao.queryChildrenNodes(faultTypeId,siteid,treeType);
		}
		
		Iterator<ItsmWoFaultType> beans = woFaultTypeList.iterator();
		while(beans.hasNext()) {
			ItsmWoFaultType woFaultType = beans.next();
			HashMap<String, Object> pNode = new HashMap<String, Object>();
			pNode.put("id", woFaultType.getId());
			pNode.put("text",woFaultType.getName());
			pNode.put("state", "closed");
			pNode.put("type", "system");
			pNode.put("parentId", woFaultType.getParentId());
			pNode.put("oneLevenId", woFaultType.getOneLevelId());
			
			String childrenFTId = String.valueOf(woFaultType.getId());
			String querySiteid = DefaultSiteId;
			if(!woFaultType.getSiteid().equals( DefaultSiteId )){
			    querySiteid = woFaultType.getSiteid();
			}
			List<ItsmWoFaultType> woFaultTypeList2 = woFaultTypeDao.queryChildrenNodes(childrenFTId,querySiteid,treeType);
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
    	 public List<Map<String, Object>> queryOneLevelChildrenNodes(String parentId, String treeType) {
	     ArrayList<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
	     ret = queryChildrenNodes(parentId,treeType,DefaultSiteId);
             return ret;
    	 }
	 @Override
         public List<Map<String, Object>> queryOneLevelSerCataNodes(String parentId, String treeType,String siteId) {
             ArrayList<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
             ret = queryChildrenNodes(parentId,treeType,siteId);
             return ret;
         }       
	 
	@Override
	public ItsmWoFaultType queryFaultTypeRootBySiteId(String siteId) {
		siteId = DefaultSiteId ;
		return woFaultTypeDao.queryFTRootBySiteId(siteId);
	}

	@Override
        public ItsmWoFaultType querySerCatalogRootBySiteId(String siteId) {
                return woFaultTypeDao.queryFTRootBySiteId(siteId);
        }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor ={Exception.class})
	public int deleteFaultTypeById(int faultTypeId) {
		return woFaultTypeDao.deleteFaultTypeById(faultTypeId);
	}

	@Override
	public List<ItsmWoFaultType> queryOneLevelFTBySiteId(String siteid) {
		siteid = DefaultSiteId ;
		return woFaultTypeDao.queryOneLevelFTBySiteId(siteid);
	}

	@Override
	public ItsmWoFaultType queryFTRootBySiteId(String siteid) {
		siteid = DefaultSiteId ;
		return woFaultTypeDao.queryFTRootBySiteId(siteid);
	}

	@Override
	public List<ItsmWoFaultType> queryWoFaultTypeList(Page<ItsmWoFaultType> page) {
		page.setParameter("siteId", DefaultSiteId);
		return woFaultTypeDao.queryWoFaultTypeList(page);
	}

    @Override
    public List<String> queryFaultTypeParents(String faultTypeId) {
        List<String> result=woFaultTypeDao.queryWoFaultTypeParents(faultTypeId); 
        return result;
    }

    @Override
    public List<Map<String, Object>> queryFaultTypeForHint(String keyWord) {
        List<Map<String, Object>> result=woFaultTypeDao.queryFaultTypeForHint(keyWord);
        return result;
    }

    @Override
    public List<ItsmWoFaultType> querychildrenFtById(String parentId, String type) {
        UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
        String siteId = userInfoScope.getSiteId();
        List<ItsmWoFaultType> woFaultTypeList = woFaultTypeDao.queryChildrenNodes(parentId,siteId,type);
        return woFaultTypeList;
    }

   
}
