package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.WoapplyRisk;

public interface WoapplyRiskService {

      
        /**
         * @description:查询
         * @author: 王中华
         * @createDate: 2016-1-8
         * @param woapplyId
         * @return:
         */
        List<WoapplyRisk> queryWoapplyRiskList(String woapplyId) ;
        
        
       
        /**
         * @description:插入
         * @author: 王中华
         * @createDate: 2016-1-8
         * @param woapplyRiskList:
         */
        void insertWorkapplyList(List<WoapplyRisk> woapplyRiskList);
       
        
      
        /**
         * @description:删除
         * @author: 王中华
         * @createDate: 2016-1-8
         * @param woapplyId
         * @return:
         */
        int deleteRiskListByWoapplyId(String woapplyId);

        
        
        
}
