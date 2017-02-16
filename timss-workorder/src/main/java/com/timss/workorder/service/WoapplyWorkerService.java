package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.WoapplyWorker;

public interface WoapplyWorkerService {


    /**
     * @description:查询
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyId
     * @return:
     */
    List<WoapplyWorker> queryWoapplyWorkerList(String woapplyId) ;
    
   
    /**
     * @description:插入
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyRiskList:
     */
    void insertWoapplyWorkerList(List<WoapplyWorker> woapplyWorkerList);
   
    
  
    /**
     * @description:删除
     * @author: 王中华
     * @createDate: 2016-1-8
     * @param woapplyId
     * @return:
     */
    int deleteWoapplyWorkerByWoapplyId(String woapplyId);

        
        
}
