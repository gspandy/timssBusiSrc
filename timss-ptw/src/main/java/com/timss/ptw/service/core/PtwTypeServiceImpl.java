package com.timss.ptw.service.core;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwType;
import com.timss.ptw.dao.PtwTypeDao;
import com.timss.ptw.service.PtwTypeService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class PtwTypeServiceImpl implements PtwTypeService {
    
    private static final Logger log = Logger.getLogger(PtwTypeService.class);
    
    @Autowired
    private PtwTypeDao ptwTypeDao;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Override
    public List<PtwType> queryTypesBySiteId(String siteId,Integer isFireWt) {
    	UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    	String ptwTypeCodes = "";
    	try {
			ptwTypeCodes = userInfoScope.getParam("ptwTypeCodes");	
		} catch (Exception e) {
			log.error( e.getMessage(), e );
		}
    	if(StringUtils.isEmpty(ptwTypeCodes)){
			return ptwTypeDao.queryTypesBySiteId( siteId,isFireWt );
		}else{
			String[] typeCodes = ptwTypeCodes.split(",");
			return ptwTypeDao.queryTypesByTypeCode( siteId, typeCodes, isFireWt );
		}
    }

    @Override
    public PtwType queryPtwTypeById(int id) {
        return ptwTypeDao.queryPtwTypeById(id);
    }

}
