package com.timss.inventory.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.timss.inventory.service.InvMatSnapshotService;
import com.timss.inventory.utils.CommonUtil;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.ClassCastUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: AutoSnapshotScheduler.java
 * @author: 890166
 * @createDate: 2014-10-30
 * @updateUser: 890166
 * @version: 1.0
 */
@Component
@Lazy(false)
public class AutoSnapshotScheduler {

    private static final Logger LOG = Logger.getLogger( AutoSnapshotScheduler.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private InvMatSnapshotService invMatSnapshotService;

    /**
     * @description: 定时自动生成快照数据
     * @author: 890166
     * @createDate: 2014-10-30
     * @throws Exception :
     */
    public void autoSyncSnapshot() throws Exception {
        LOG.info( "=============================> 开始自动生成快照数据" );

        Map<String, Object> paramMap = null;

        String sRole = CommonUtil.getProperties( "snapshotRole" );
        String[] sRoles = sRole.split( "," );
        IAuthorizationManager am = (SecurityBeanHelper.getInstance()).getBean( IAuthorizationManager.class );
        for ( String roleId : sRoles ) {
            LOG.info( "=============================> 当前的角色id为：" + roleId );
            paramMap = new HashMap<String, Object>();
            List<SecureUser> usList = am.retriveUsersWithSpecificRole( roleId, null, true, true );

            String[] rSites = roleId.split( "_" );
            LOG.info( "=============================> 当前的站点为：" + rSites[0] );

            if ( !usList.isEmpty() ) {
                for ( SecureUser su : usList ) {
                    su.setCurrentSite( rSites[0] );

                    LOG.info( "=============================> 当前的角色人员为：" + su.getName() );
                    UserInfo uInfo = itcMvcService.getUserInfo( su.getId(), su.getCurrentSite() );
                    UserInfoScopeImpl userInfo = ClassCastUtil.castAllField2Class( UserInfoScopeImpl.class, uInfo );

                    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
                    String curDate = sdf.format( new Date() );

                    paramMap.put( "remark", "定时器自动生成快照" + curDate );
                    paramMap.put( "type", "A" );

                    invMatSnapshotService.saveAsSnapshot( userInfo, paramMap );
                }
            }
        }

        LOG.info( "=============================> 自动生成快照数据结束" );
    }
}
