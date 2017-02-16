package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.WoapplySafeInform;

public interface WoapplySafeInformService {


    /**
     * @description:查询
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyId
     * @return:
     */
    List<WoapplySafeInform> queryWoapplySafeInformList(String woapplyId) ;
    
    
   
    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyRiskList:
     */
    void insertWoapplySafeInformList(List<WoapplySafeInform> woapplySafeInformList);
   
    
  
    /**
     * @description:删除
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyId
     * @return:
     */
    int deleteSafeInformByWoapplyId(String woapplyId);

    
    
        
        
}
