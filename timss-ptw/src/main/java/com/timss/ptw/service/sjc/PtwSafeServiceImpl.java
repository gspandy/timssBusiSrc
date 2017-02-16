package com.timss.ptw.service.sjc;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.bean.PtwSafe;
import com.timss.ptw.service.PtwIslMethodService;
import com.timss.ptw.service.PtwSafeService;

/**
 * 安全措施Service核心实现包
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PtwSafeServiceCore.java
 * @author: 周保康
 * @createDate: 2014-7-18
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwSafeServiceImpl implements PtwSafeService{
    
    private static final Logger log = Logger.getLogger(PtwSafeService.class);
    @Autowired
    private PtwIslMethodService ptwIslMethodService;
    
    @Override
    public List<PtwSafe> queryPtwSafeListByWtId(int wtId) {
        return null;
    }
    @Override
    public int insertPtwSafe(PtwSafe ptwSafe) {
        return 0;
    }

    @Override
    public List<PtwSafe> fromSafeItemsToList(String safeItems,int ptwId) {
        return null;
    }

    @Override
    public int deletePtwSafeByWtId(int wtId) {
        return ptwIslMethodService.deletePtwIsolationItem( 0, wtId );
    }
   
    @Override
    public HashMap<String, Object> genPtwSafeMap(List<PtwSafe> ptwSafes){
        return null;
    }

    @Override
    public int batchInsertPtwSafe(int ptwId, String safeItems) {
        JSONObject safeObject = JSONObject.fromObject( safeItems );
        if ( safeObject == null || safeObject.isEmpty() ) {
            return 0;
        }
        return ptwIslMethodService.insertOrUpdatePtwIsolationItem( 0, ptwId, 
                safeObject.getString( "safe" ), 
                safeObject.getString( "elecSafe" ), 
                safeObject.getString( "jxSafe" ), 
                safeObject.getString( "compSafe" ) );
    }

    @Override
    public int batchUpdatePtwSafeRemover(int ptwId, String safeItems) {
        JSONObject safeObject = JSONObject.fromObject( safeItems );
        if ( safeObject == null || safeObject.isEmpty() ) {
            return 0;
        }
        this.deletePtwSafeByWtId( ptwId );
        return ptwIslMethodService.insertOrUpdatePtwIsolationItem( 0, ptwId, 
                safeObject.getString( "safe" ), 
                safeObject.getString( "elecSafe" ), 
                safeObject.getString( "jxSafe" ), 
                safeObject.getString( "compSafe" ) );
    }

}
