package com.timss.operation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 运行管理用户和权限的通用函数
 */
@Service("oprUserPrivUtil")
public class OprUserPrivUtil {
	private Logger log = LoggerFactory.getLogger( OprUserPrivUtil.class );
	
	@Autowired
    private ItcMvcService itcMvcService;

	public UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }
}
