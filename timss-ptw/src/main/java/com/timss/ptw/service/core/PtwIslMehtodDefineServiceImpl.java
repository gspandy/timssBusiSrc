package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwIslMethodDefine;
import com.timss.ptw.dao.PtwIslMethodDefineDao;
import com.timss.ptw.service.PtwIslMethodDefineService;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwIslMehtodDefineServiceImpl implements PtwIslMethodDefineService {
    private static final Logger log = Logger.getLogger(PtwIslMehtodDefineServiceImpl.class);

    @Autowired
    PtwIslMethodDefineDao  ptwIslMethodDefineDao;
    @Autowired
    ItcMvcService itcMvcService;
    
	@Override
	public Page<PtwIslMethodDefine> queryPtwIsLMethDefList(
			Page<PtwIslMethodDefine> page) {
		List<PtwIslMethodDefine> ret = ptwIslMethodDefineDao.queryPtwIsLMethDefList(page);
		page.setResults(ret);
		return page;
	}

	@Override
	public PtwIslMethodDefine queryPtwIsLMethDefByPtwId(int id) {
		return ptwIslMethodDefineDao.queryPtwIsLMethDefById(id);
	}

	@Override
	public int insertPtwIsLMethDef( HashMap<String,String> paramsDataMap) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteId = userInfoScope.getSiteId();
		String islMethDefForm = paramsDataMap.get("islMethDefForm");
		
		PtwIslMethodDefine ptwIslMethodDefine = JsonHelper.fromJsonStringToBean(islMethDefForm, PtwIslMethodDefine.class);
		ptwIslMethodDefine.setSiteid(siteId);
		ptwIslMethodDefine.setCreatedate(new Date());
		ptwIslMethodDefine.setModifydate(new Date());
		ptwIslMethodDefine.setCreateuser(userId);
		ptwIslMethodDefine.setModifyuser(userId);
		ptwIslMethodDefine.setYxbz(1);
		
		return ptwIslMethodDefineDao.insertPtwIsLMethDef(ptwIslMethodDefine);
	}

	@Override
	public int updatePtwIsLMethDef(PtwIslMethodDefine ptwInfo) {
		return ptwIslMethodDefineDao.updatePtwIsLMethDef(ptwInfo);
	}

	@Override
	public int deletePtwIsLMethDefById(int id) {
		return ptwIslMethodDefineDao.deletePtwIsLMethDefById(id);
	}

	@Override
	public Boolean checkIslMethNo(String islMethDefNo) {
		boolean result = false;
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		int count = ptwIslMethodDefineDao.queryPtwIsLMethDefByNo(islMethDefNo,siteId);
		if(count == 0){  
			result= true;
		}
		return result;
	}


	/**
         * 
         * @description:添加隔离证弹出页--高级查询
         * @author: fengzt
         * @createDate: 2014年10月27日
         * @param map
         * @param pageVo
         * @return:List<IsMethodPointVo>
         */
        @Override
        public List<IsMethodPointVo> queryIsMethodBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo) {
            pageVo.setParams( map );
            String siteId = getUserInfoScope().getSiteId();
            pageVo.setParameter( "siteId", siteId );
            String sort = (String) pageVo.getParams().get( "sort" );
            String order = (String) pageVo.getParams().get( "order" );
            
            if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
                pageVo.setSortKey( sort );
                pageVo.setSortOrder( order );
            }else{
                pageVo.setSortKey( "pointName" );
                pageVo.setSortOrder( "DESC" );
            }
            return ptwIslMethodDefineDao.queryIsMethodBySiteId( pageVo );
        }

        /**
         * 
         * @description:添加隔离证弹出页列表
         * @author: fengzt
         * @createDate: 2014年10月27日
         * @param pageVo
         * @return:List<IsMethodPointVo>
         */
        @Override
        public List<IsMethodPointVo> queryIsMethodBySiteId(Page<HashMap<?, ?>> pageVo) {
            
            String siteId = getUserInfoScope().getSiteId();
            pageVo.setParameter( "siteId", siteId );
            String sort = (String) pageVo.getParams().get( "sort" );
            String order = (String) pageVo.getParams().get( "order" );
            
            if( StringUtils.isNotBlank( sort ) && StringUtils.isNotBlank( order ) ){
                pageVo.setSortKey( sort );
                pageVo.setSortOrder( order );
            }else{
                pageVo.setSortKey( "pointName" );
                pageVo.setSortOrder( "DESC" );
            }
            return ptwIslMethodDefineDao.queryIsMethodBySiteId( pageVo );
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
            	log.error(e);
            }
            return userInfoScope;
        }

   

}
