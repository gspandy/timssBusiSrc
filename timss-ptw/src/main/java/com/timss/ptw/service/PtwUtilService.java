package com.timss.ptw.service;

import java.util.List;

import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;




public interface PtwUtilService {

	List<String> enumValueInSomeSites(String ecatCode, String searchSiteid);
    
	/**
         * @description:修改当前执行人信息
         * @author: 王中华
         * @createDate: 2015-8-2
         * @param woId
         * @param userInfoScope
         * @param flag: 正常流程时为“normal”，回退时为“rollback”
         */
        void updatePtoCurrHandlerUser(String woId,UserInfoScope userInfoScope, String flag);
        
        void setRelatePersonName(PtoInfoVo ptoInfoVo, String siteid);
        
        void modifyHomepageTodoName(String todoFlowNo, String newName);
    
}
