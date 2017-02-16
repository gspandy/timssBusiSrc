package com.timss.asset.service.itc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.HwLedgerDeviceBean;
import com.timss.asset.bean.HwModelBean;
import com.timss.asset.dao.HwModelDao;
import com.timss.asset.service.HwModelService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class HwModelServiceImpl implements HwModelService {
    private Logger log = Logger.getLogger( HwModelServiceImpl.class );
    
    
    @Autowired
    private HwModelDao modelDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    static Logger logger = Logger.getLogger( HwModelServiceImpl.class );
	
	@Override
	public Page<HwModelBean> queryList(Page<HwModelBean> page)
			throws Exception {
		List<HwModelBean> list=modelDao.queryList(page);
		page.setResults(list);
		return page;
	}
	
	@Override
	public HwModelBean queryDetail(String modelId) throws Exception {
		HwModelBean bean=modelDao.queryDetail(modelId);
		List<HwLedgerDeviceBean>deviceList=modelDao.queryDeviceByModelId(modelId, bean.getModelType());
		bean.setDeviceList(deviceList);
		return bean;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int insert(@CUDTarget HwModelBean bean) throws Exception {
		return modelDao.insert(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int update(@CUDTarget HwModelBean bean) throws Exception {
		return modelDao.update(bean);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int deleteById(String id) throws Exception {
		return modelDao.deleteById(id);
	}

	@Override
	public HwModelBean queryHwModelByNameAndType(String modelType,
			String modelName, String siteId) throws Exception {
		HwModelBean bean=modelDao.queryHwModelByNameAndType(modelType,modelName,siteId);
		return bean;
	}
	
	    /**
	     * 
	     * @description:拿到登录用户信息
	     * @author: fengzt
	     * @createDate: 2014年8月26日
	     * @return:String
	     */
	    private UserInfoScope getUserInfoScope(){
	        UserInfoScope userInfoScope = null;
	        try {
	            userInfoScope = itcMvcService.getUserInfoScopeDatas();
	        } catch (Exception e) {
	            log.error( e.getMessage() );
	        }
	        return userInfoScope;
	    }
	    

	/**
         * 
         * @description:通过硬件类型查询型号
         * @author: fengzt
         * @createDate: 2014年12月18日
         * @param pageVo
         * @param modelType 
         * @return:List<HwModelBean>
         */
        @Override
        public List<HwModelBean> queryHwModelByType(Page<HashMap<?, ?>> pageVo, String modelType) {
            pageVo.setParameter( "modelType", modelType );
            String siteId = getUserInfoScope().getSiteId();
            pageVo.setParameter( "siteId", siteId );
            
            return modelDao.queryHwModelByType( pageVo );
        }

        /**
         * 
         * @description:通过硬件类型查询型号-高级查询
         * @author: fengzt
         * @createDate: 2014年12月18日
         * @param map
         * @param pageVo
         * @param modelType
         * @return:
         */
        @Override
        public List<HwModelBean> queryHwModelByTypeSearch(Map<String, Object> map, Page<HashMap<?, ?>> pageVo,
                String modelType) {
            //高级搜索参数
            pageVo.setParams( map );
            pageVo.setParameter( "modelType", modelType );
            String siteId = getUserInfoScope().getSiteId();
            pageVo.setParameter( "siteId", siteId );
            
            return modelDao.queryHwModelByTypeSearch( pageVo );
        }
        
}