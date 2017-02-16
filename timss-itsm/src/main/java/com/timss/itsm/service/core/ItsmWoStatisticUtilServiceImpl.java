package com.timss.itsm.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.service.ItsmWoStatisticUtilService;
import com.timss.itsm.util.ItsmCommonUtil;
import com.timss.itsm.vo.ItsmWorkTimeVo;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.sec.SecureUserGroup;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class ItsmWoStatisticUtilServiceImpl implements ItsmWoStatisticUtilService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private ISecurityMaintenanceManager iSecManager;

    private static final Logger LOG = Logger.getLogger( ItsmWoStatisticUtilServiceImpl.class );

    @Override
    public List<SecureUserGroup> retrieveGroupsByKeyword(String keyword) {
        LOG.info( "------------客户联想查询-------------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        // 获取要统计的用户组， 此处需要模糊查询，以"xxx"开头的用户组
        Page<SecureUserGroup> page = new Page<SecureUserGroup>();
        page = new Page<SecureUserGroup>();
        page.setPageSize( 100 );
        Map<String, Object> searchBy = new HashMap<String, Object>();
        searchBy.put( "searchBy", "itc_itsm_wt" );// 以xxx开头的用户组标识(此处需要将用户组中的ID变成小写)
        page.setParams( searchBy );
        SecureUser operator = itcMvcService.getUserInfo( "898000", "ITC" ).getSecureUser();
        Page<SecureUserGroup> qResult = iSecManager.retrieveGroups( page, operator );
        List<SecureUserGroup> UserGroupList = qResult.getResults();
        return UserGroupList;
    }

    @Override
    public ItsmWorkTimeVo setWorkTimeVo(Date startDate, Date endDate) {
        ItsmWorkTimeVo result = new ItsmWorkTimeVo();
        result.setStart( startDate );
        result.setEnd( endDate );
        result.setSiteId( "ITC" );
        result.setMorning( ItsmCommonUtil.getProperties( "morningBegin" ) );
        result.setForenoon( ItsmCommonUtil.getProperties( "morningEnd" ) );
        result.setNoon( ItsmCommonUtil.getProperties( "afternoonBegin" ) );
        result.setAfternoon( ItsmCommonUtil.getProperties( "afternoonEnd" ) );
        result.setWorkTime( Integer.valueOf( ItsmCommonUtil.getProperties( "workTimeLen" ) ) );
        result.setFlag( false );
        return result;
    }
}
