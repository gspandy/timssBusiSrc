package com.timss.workorder.service;

import java.util.Map;

import com.timss.workorder.bean.TowerApply;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface WoTowerApplyService {

        /***
         * 查询所有登塔申请
         * @throws Exception 
         */
        Page<TowerApply> queryAllTowerApply(TowerApply towerApply) throws Exception;
        
        /**
         * @description:查询
         * @author: 朱旺
         * @createDate: 2015-12-21
         * @param id
         * @param siteid
         * @return:
         */
        Map<String, Object> queryTowerApplyById(String id);
        
        /**
         * @description:添加
         * @author: 朱旺
         * @createDate: 2015-12-21
         * @param towerApply
         * @return:
         * @throws Exception 
         */
        Map<String, Object> insertTowerApply(TowerApply towerApply) throws Exception;
        
        
        /**
         * @description:保存
         * @author: 朱旺
         * @createDate: 2015-12-21
         * @param towerApply
         * @return:
         * @throws Exception 
         */
        Map<String, Object> saveTowerApply(TowerApply towerApply) throws Exception;
        
        /**
         * @description:更新
         * @author: 朱旺
         * @createDate: 2015-12-21
         * @param Map
         * @return:
         */
        Map<String, String> updateTowerApply(Map<String, String> addWODataMap)  throws Exception;
        
        /**
         * @description:删除
         * @author: 朱旺
         * @createDate: 2015-12-21
         * @param id
         * @param siteid
         * @return:
         */
        int deleteTowerApply(String id );
        
        /**
         * @description:获取当前处理人信息 
         * @author: 朱旺
         * @createDate: 2015-12-22
         * @param userInfoScope
         * @return:
         * @throws Exception 
         */
        Map<String, String> getTowerApplyCurrHanderInfo(UserInfoScope userInfoScope) throws Exception;
        
        /**
         * @description:登塔申请回退，更新当前处理人
         * @author: 朱旺
         * @createDate: 2015-12-23
         * @param userInfoScope
         * @param woapplyId:
         */
        void rollbackUpdateTowerApplyCurrHander(UserInfoScope userInfoScope, String towerApplyId);
        
        /**
         * @description: 作废登塔申请
         * @author: 朱旺
         * @createDate: 2015-12-23
         * @param woapplyId:
         */
        void obsoleteTowerApply(String towerApplyId);
        
}
