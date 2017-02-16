package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.itc.util.json.JsonHelper;
import com.timss.ptw.bean.PtwSafe;
import com.timss.ptw.dao.PtwSafeDao;
import com.timss.ptw.service.PtwInfoService;
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
@Service
public class PtwSafeServiceImpl implements PtwSafeService{
    
    private static final Logger log = Logger.getLogger(PtwSafeService.class);
    
    @Autowired
    PtwSafeDao ptwSafeDao;
    
    @Override
    public List<PtwSafe> queryPtwSafeListByWtId(int wtId) {
        return ptwSafeDao.queryPtwSafeListByWtId( wtId );
    }

    @Override
    @Transactional
    public int insertPtwSafe(PtwSafe ptwSafe) {
        return ptwSafeDao.insertPtwSafe( ptwSafe );
    }

    @Override
    @Transactional
    public int batchInsertPtwSafe(int ptwId,String safeItems) {
        List<PtwSafe> ptwSafes = this.fromSafeItemsToList( safeItems, ptwId );
        log.info("工作票隔离措施:" + ptwSafes);
        if ( ptwSafes == null || ptwSafes.size() == 0 ) {
            return 0;
        }
        return ptwSafeDao.batchInsertPtwSafe( ptwSafes );
    }
    
    @Override
    public List<PtwSafe> fromSafeItemsToList(String safeItems,int ptwId) {
        JSONArray safeArray = JSONArray.fromObject( safeItems );
        List<PtwSafe> ptwSafes = new ArrayList<PtwSafe>(safeArray.size());
        for ( Object object : safeArray ) {
            PtwSafe ptwSafe = JsonHelper.fromJsonStringToBean( object.toString(), PtwSafe.class );
            ptwSafe.setWtId( ptwId );
            ptwSafes.add( ptwSafe );
        }      
        return ptwSafes;
    }

    @Override
    @Transactional
    public int deletePtwSafeByWtId(int wtId) {
        return ptwSafeDao.deletePtwSafeByWtId( wtId );
    }

    @Override
    public int batchUpdatePtwSafeRemover(int ptwId,String safeItems) {
        List<PtwSafe> ptwSafes = this.fromSafeItemsToList( safeItems, ptwId );
        return ptwSafeDao.batchUpdatePtwSafeRemover( ptwSafes );
    }
    
    /***
     * 将安全措施的内容转换为map，同时计算有多少种种类
     * @description:
     * @author: 周保康
     * @createDate: 2014-8-14
     * @param ptwSafes
     * @return:
     */
    @Override
    public HashMap<String, Object> genPtwSafeMap(List<PtwSafe> ptwSafes){
        HashMap<String, Object> ptwSafeMap = new HashMap<String, Object>();
        HashMap<Integer, Integer> ptwTypes = new HashMap<Integer, Integer>();
        for ( PtwSafe ptwSafe : ptwSafes ) {
            ptwTypes.put( ptwSafe.getSafeType(), ptwSafe.getSafeType() );
        }
        int[] ptwTypeArray = new int[ptwTypes.size()];
        Iterator<Entry<Integer, Integer>> iter = ptwTypes.entrySet().iterator();
        
        int ptwIndex = 0;
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Entry<Integer, Integer>) iter.next();
            ptwTypeArray[ptwIndex++] = entry.getKey();
        }
        ptwSafeMap.put( "safeDatas", ptwSafes );
        ptwSafeMap.put( "ptwTypes", ptwTypeArray );
        return ptwSafeMap;
    }

}
