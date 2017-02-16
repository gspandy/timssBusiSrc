package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceMain;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.itc.dto.Page;


/**
 * 
 * @title: 值别Service
 * @description: 
 * @company: gdyd
 * @className: FinanceService.java
 * @author: wus
 * @createDate: 2014年6月13日
 * @updateUser: wus
 * @version: 1.0
 */
public interface FinanceService {
	
	
	
	 /**
     * 
     * @description:查询结果中返回FinanceMainList的方法
     * @author: wus
     * @createDate: 2014年6月17日
     * @param 
     * @return:List<FinanceMain>
	 * @ 
     */
	 Page<FinanceMain> queryFinanceMainList(Page<FinanceMain> page,UserInfoScope userInfoScope);
    /**
     * 
     * @description:查询结果中返回主键方法
     * @author: wus
     * @createDate: 2014年6月4日
     * @param fid  其中id为自增，不需要设置
     * @return:FinanceMain
     */
	 FinanceMain insertFinanceMain(FinanceMain financemain);

	
}
