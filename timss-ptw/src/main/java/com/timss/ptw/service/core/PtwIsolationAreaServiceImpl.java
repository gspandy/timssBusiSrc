package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwIsolationArea;
import com.timss.ptw.bean.PtwIsolationAreaMethod;
import com.timss.ptw.dao.PtwIsolationAreaDao;
import com.timss.ptw.dao.PtwIsolationAreaMethodDao;
import com.timss.ptw.service.PtwIsolationAreaService;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwIsolationAreaServiceImpl implements PtwIsolationAreaService {
    private static final Logger log = Logger.getLogger(PtwIsolationAreaServiceImpl.class);

    @Autowired
    PtwIsolationAreaDao  ptwIsolationAreaDao;
    @Autowired
    PtwIsolationAreaMethodDao ptwIsolationAreaMethodDao;
    @Autowired
    ItcMvcService itcMvcService;
    
	@Override
	public Page<PtwIsolationArea> queryPtwIsolationAreaList(Page<PtwIsolationArea> page) {
		List<PtwIsolationArea> ret = ptwIsolationAreaDao.queryPtwIsolationAreaList(page);
		page.setResults(ret);
		return page;
	}

	@Override
	public PtwIsolationArea queryPtwIsolationAreaById(int id) {
		return ptwIsolationAreaDao.queryPtwIsolationAreaById(id);
	}

	/**
	 * 插入隔离证
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertPtwIsolationArea( HashMap<String,String> paramsDataMap) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteId = userInfoScope.getSiteId();
		String isolationAreaForm = paramsDataMap.get("isolationAreaForm");
		String isolationAreaMethodDate = paramsDataMap.get("isolationAreaMethodDate");
	
		//组装隔离证的基本信息
		PtwIsolationArea ptwIsolationArea = JsonHelper.fromJsonStringToBean(isolationAreaForm, PtwIsolationArea.class);
		ptwIsolationArea.setSiteid(siteId);
		ptwIsolationArea.setCreatedate(new Date());
		ptwIsolationArea.setModifydate(new Date());
		ptwIsolationArea.setCreateuser(userId);
		ptwIsolationArea.setModifyuser(userId);
		ptwIsolationArea.setYxbz(1);
		int ptwIsolationAreaId = ptwIsolationAreaDao.insertPtwIsolationArea(ptwIsolationArea);
		
		//添加标准隔离证与隔离点的关联信息
		if( StringUtils.isNotBlank( isolationAreaMethodDate ) ){
        		List<IsMethodPointVo> isVos = JsonHelper.toList( isolationAreaMethodDate, IsMethodPointVo.class );
        		for(IsMethodPointVo vo : isVos ){
        			PtwIsolationAreaMethod ptwIsolationAreaMethod = new PtwIsolationAreaMethod();
        			//标准隔离证ID塞进去
        			ptwIsolationAreaMethod.setAreaId( ptwIsolationArea.getId() ); 
        			ptwIsolationAreaMethod.setMethodId( vo.getId() );; 
        			ptwIsolationAreaMethodDao.insertPtwIsolationAreaMethod(ptwIsolationAreaMethod);
        		}
		}
		
		return ptwIsolationAreaId;
	}

	/**
	 * 更新隔离证
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updatePtwIsolationArea(HashMap<String,String> paramsDataMap) throws Exception{
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String isolationAreaForm = paramsDataMap.get("isolationAreaForm");
		String isolationAreaMethodDate = paramsDataMap.get("isolationAreaMethodDate");
		PtwIsolationArea ptwIsolationArea = JsonHelper.fromJsonStringToBean(isolationAreaForm, PtwIsolationArea.class);
		ptwIsolationArea.setModifydate(new Date());
    	        ptwIsolationArea.setModifyuser(userId);
    	        //更新基本信息
		int result =  ptwIsolationAreaDao.updatePtwIsolationArea(ptwIsolationArea);
		
		//删除所有之前的记录信息
		int ptwIsolationAreaId = ptwIsolationArea.getId();
		if( ptwIsolationAreaId != 0){
			int delCount = ptwIsolationAreaMethodDao.deleteIslAreaMethodByIslAreaId(ptwIsolationAreaId);
			log.info( "修改标准隔离症与隔离方法之间的关系 --删除数据：" + delCount + " ;更新为：" + result  );
		}

                //添加标准隔离证与隔离点的关联信息
                if( StringUtils.isNotBlank( isolationAreaMethodDate ) ){
                        List<IsMethodPointVo> isVos = JsonHelper.toList( isolationAreaMethodDate, IsMethodPointVo.class );
                        for(IsMethodPointVo vo : isVos ){
                                PtwIsolationAreaMethod ptwIsolationAreaMethod = new PtwIsolationAreaMethod();
                                //标准隔离证ID塞进去
                                ptwIsolationAreaMethod.setAreaId( ptwIsolationAreaId ); 
                                ptwIsolationAreaMethod.setMethodId( vo.getId() );; 
                                ptwIsolationAreaMethodDao.insertPtwIsolationAreaMethod(ptwIsolationAreaMethod);
                        }
                }
		return result ;
	}
	
	/**
	 * 删除隔离证
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deletePtwIsolationAreaById(int id) {
	    return ptwIsolationAreaDao.deletePtwIsolationAreaById(id);
	}

	@Override
	public Boolean checkIsolationAreaNo(String isolationAreaNo) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		boolean result = false;
		int count = ptwIsolationAreaDao.queryPtwIsolationAreaByNo(isolationAreaNo,siteId);
		if(count == 0){  
			result= true;
		}
		return result;
	}

	/**
         * 
         * @description:通过隔离证ID查找列表
         * @author: fengzt
         * @createDate: 2014年10月28日
         * @param areaId
         * @return:List<IsMethodPointVo>
         */
        @Override
        public List<IsMethodPointVo> queryIsolationMethodList(String areaId) {
            return ptwIsolationAreaMethodDao.queryIsolationMethodList( areaId );
        }
    
   

}
