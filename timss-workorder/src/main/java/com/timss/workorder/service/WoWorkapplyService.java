package com.timss.workorder.service;

import java.util.Map;

import com.timss.workorder.bean.Workapply;
import com.yudean.itc.dto.Page;

public interface WoWorkapplyService {

        /***
         * 查询所有工单申请
         * @throws Exception 
         */
        Page<Workapply> queryAllWorkapply(Page<Workapply> page) throws Exception;
        
        /**
         * @description:查询
         * @author: 王中华
         * @createDate: 2015-12-18
         * @param id
         * @param siteid
         * @return:
         */
        Map<String, Object> queryWorkapplyById(String id);
        
        /**
         * @description:添加
         * @author: 王中华
         * @createDate: 2015-12-19
         * @param workapply
         * @return:
         * @throws Exception 
         */
        Map<String, Object> insertWorkapply(Workapply workapply) throws Exception;
        /**
         * @description:更新
         * @author: 王中华
         * @createDate: 2015-12-18
         * @param workapply
         * @return:
         */
        int updateWorkapply(Workapply workapply);
        
        /**
         * @description:删除
         * @author: 王中华
         * @createDate: 2015-12-18
         * @param id
         * @param siteid
         * @return:
         */
        int deleteWorkapply(String id );

        /**
         * @description:暂存开工申请
         * @author: 王中华
         * @createDate: 2015-12-22
         * @param workapply
         * @return:
         */
        Map<String, Object> saveWorkapply(Workapply workapply);

        /**
         * @description: 更新开工申请
         * @author: 王中华
         * @createDate: 2015-12-22 
         * @param addWODataMap
         * @return:
         * @throws Exception 
         */
        Map<String, String> updateWorkapply(Map<String, Object> addWODataMap) throws Exception;

        /**
         * @description: 作废开工申请
         * @author: 王中华
         * @createDate: 2015-12-22
         * @param woapplyId:
         */
        void obsoleteWorkapply(String woapplyId);
        
        
        
}
