package com.timss.asset.service.scc;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.PropertyBean;
import com.timss.asset.dao.PropertyDao;
import com.timss.asset.service.PropertyService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;

/**
 * 物业管理的service的实现类
 * @author 890147
 *
 */
@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyDao propertyDao;
    static Logger logger = Logger.getLogger( PropertyServiceImpl.class );
    
	@Override
	public String queryPropertyRootIdBySite(String siteId) throws Exception {
		String rootId=propertyDao.queryPropertyRootIdBySite(siteId);
		return rootId;
	}
	
	@Override
	public Page<PropertyBean> queryList(Page<PropertyBean> page)
			throws Exception {
		List<PropertyBean> list=propertyDao.queryList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
	public PropertyBean queryDetail(String propertyId) throws Exception {
		PropertyBean bean=propertyDao.queryDetail(propertyId);
		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insert(@CUDTarget PropertyBean bean) throws Exception {
		return propertyDao.insert(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int update(@CUDTarget PropertyBean bean) throws Exception {
		return propertyDao.update(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteById(String id) throws Exception {
		return propertyDao.deleteById(id);
	}

	@Override
	public List<PropertyBean> queryChildren(String parentId) throws Exception {
		List<PropertyBean> result=propertyDao.queryChildren(parentId);
		return result;
	}

	@Override
	public List<Map<String, Object>> queryForHint(String site, String keyWord)
			throws Exception {
		List<Map<String, Object>>result=propertyDao.queryForHint(site,keyWord);
		return result;
	}

	@Override
	public List<String> queryParents(String id) throws Exception {
		List<String>result=propertyDao.queryParents(id); 
		return result;
	}
    
}