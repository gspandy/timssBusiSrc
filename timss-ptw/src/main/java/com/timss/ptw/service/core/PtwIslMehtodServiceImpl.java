package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwIsolationMethodBean;
import com.timss.ptw.dao.PtwIsolationDao;
import com.timss.ptw.service.PtwIslMethodService;
import com.timss.ptw.vo.IsolationVo;
import com.yudean.itc.util.json.JsonHelper;

@Service
public class PtwIslMehtodServiceImpl implements PtwIslMethodService {
    private static final Logger log = Logger.getLogger(PtwIslMehtodServiceImpl.class);
    
    @Autowired
    private PtwIsolationDao ptwIsolationDao;
    
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public int deletePtwIsolationItem(int islId,int wtId) {
        if ( islId == 0 && wtId == 0 ) {
            log.debug( "删除安全措施时未设置隔离证或工作票id" );
            return 0;
        }
        int delCount = ptwIsolationDao.deletePtwIsolationItem( islId,wtId );
        return delCount;
    }
    
    /**
     * 
     * @description:更新
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param ptwIsolationBean
     * @param safeDatas
     * @param elecDatas
     * @param compSafeDatas
     * @return:
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public int insertOrUpdatePtwIsolationItem( int islId,int wtId, String safeDatas, String elecDatas,String jxDatas, String compSafeDatas ) {
        List<IsolationVo> safeVos = new ArrayList<IsolationVo>();
        if( StringUtils.isNotBlank( safeDatas ) && !safeDatas.equals( "null" ) ){
            safeVos = JsonHelper.toList(safeDatas, IsolationVo.class); 
        }
        
        List<IsolationVo> jxVos = new ArrayList<IsolationVo>();
        //如果有检修
        if( StringUtils.isNotBlank( jxDatas ) && !jxDatas.equals( "null" ) ){
            jxVos = JsonHelper.toList(jxDatas, IsolationVo.class );
            safeVos.addAll( jxVos );
        }
        
        List<IsolationVo> elecVos = new ArrayList<IsolationVo>();
        //如果有地线
        if( StringUtils.isNotBlank( elecDatas ) && !elecDatas.equals( "null" ) ){
            elecVos = JsonHelper.toList( elecDatas, IsolationVo.class );
            safeVos.addAll( elecVos );
        }
        
        List<IsolationVo> compSafeVos = new ArrayList<IsolationVo>();
        //如果有补充安全措施
        if( StringUtils.isNotBlank( compSafeDatas ) && !compSafeDatas.equals( "null" ) ){
            compSafeVos = JsonHelper.toList( compSafeDatas, IsolationVo.class );
            safeVos.addAll( compSafeVos );
        }
        
        List<PtwIsolationMethodBean> vos = isolationVoToPtwIsolationMethodBean( islId,wtId, safeVos );
        if (vos == null || vos.size() == 0) {
			return 0;
		}
        //批量插入
        int itemCount = ptwIsolationDao.insertBatchPtwIsolationItem( vos );
        
        return itemCount;
    }
    
    /**
     * 
     * @description:页面Vo转换成bean
     * @author: fengzt
     * @createDate: 2014年11月3日
     * @param ptwIsolationBean
     * @param safeVos
     * @return:List<PtwIsolationMethodBean>
     */
    private List<PtwIsolationMethodBean> isolationVoToPtwIsolationMethodBean(int islId,int wtId,List<IsolationVo> safeVos) {
        List<PtwIsolationMethodBean> voBeans = new ArrayList<PtwIsolationMethodBean>();
        
        for( IsolationVo vo : safeVos ){
            PtwIsolationMethodBean bean = new PtwIsolationMethodBean();
            if ( wtId > 0 ) {
                bean.setWtId( wtId );
            }
            if(islId > 0){
                bean.setIslId( islId );
            }
            bean.setPointMethodId( vo.getId() );
            bean.setElecFloorNo( vo.getElecFloorNo() );
            
            bean.setSafeType( vo.getSafeType() );
            bean.setSafeOrder( vo.getSafeOrder() );
            bean.setExecuterNo( vo.getExecuterNo() );
            
            bean.setExecuter( vo.getExecuter() );
            bean.setRemover( vo.getRemover() );
            bean.setRemoverNo( vo.getRemoverNo() );
            voBeans.add( bean );
        }
        
        return voBeans;
    }
    
    /**
     * 
     * @description:加载隔离证子项 datagrid
     * @author: fengzt
     * @createDate: 2014年11月4日
     * @param safeType
     * @param islId
     * @return:Map<String, Object>
     */
    @Override
    public Map<String, Object> querySafeDatagridByWtOrIslId(int safeType, int islId,int wtId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "safeType", safeType );
        map.put( "islId", islId );
        map.put( "wtId", wtId );
        
        List<IsolationVo> ptwList = new ArrayList<IsolationVo>();
        if ( islId != 0 || wtId != 0 ) {
            ptwList = ptwIsolationDao.querySafeDatagridByWtOrIslId( map );
        }
        
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put( "rows", ptwList );
        if( !ptwList.isEmpty() ){
            dataMap.put( "total", ptwList.size() );
        }else{
            dataMap.put( "total", 0 );
        }
        
        return dataMap;
    }
}
