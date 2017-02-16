package com.timss.ptw.service;

import java.util.Map;



public interface PtwIslMethodService {
    
    /**
     * 
     * @description:
     * @author: 周保康
     * @createDate: 2014-11-26
     * @param islId
     * @param wtId
     * @return:
     */
    public int deletePtwIsolationItem(int islId,int wtId);
    
    /**
     * 
     * @description:
     * @author: 周保康
     * @createDate: 2014-11-26
     * @param islId
     * @param wtId
     * @param safeDatas
     * @param elecDatas
     * @param jxDatas
     * @param compSafeDatas
     * @return:
     */
    public int insertOrUpdatePtwIsolationItem( int islId,int wtId, String safeDatas, String elecDatas,String jxDatas, String compSafeDatas );
    
    /**
     * 
     * @description:加载隔离证子项 datagrid
     * @author: fengzt
     * @createDate: 2014年11月4日
     * @param safeType
     * @param islId
     * @return:Map<String, Object>
     */
    public Map<String, Object> querySafeDatagridByWtOrIslId(int safeType, int islId,int wtId);
}
