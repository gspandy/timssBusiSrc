package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.dao.PtwPtoSelectUserDao;
import com.timss.ptw.service.PtwPtoSelectUserService;
import com.timss.ptw.vo.PtwPtoSelectUserInfoVo;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwPtoSelectUserServiceImpl implements PtwPtoSelectUserService {

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwPtoSelectUserDao ptwPtoSelectUserDao;
    private static final Logger LOG = Logger.getLogger( PtwPtoSelectUserServiceImpl.class );

    
    @Override
    public List<SecureUser> selectUsersWithoutWorkFLow(String category,String type,String step) {
        LOG.info( "-------------进入两票选人接口实现 非工作流选人-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        List<SecureUser> resultList = new ArrayList<SecureUser>();
        String siteId  = userInfoScope.getSiteId();
        PtwPtoSelectUserInfoVo ptwPtoSelectUserInfoVo = new PtwPtoSelectUserInfoVo();
        ptwPtoSelectUserInfoVo.setType( type );
        ptwPtoSelectUserInfoVo.setStep( step );
        ptwPtoSelectUserInfoVo.setCategory( category );
        ptwPtoSelectUserInfoVo.setSiteid( siteId );
        ptwPtoSelectUserInfoVo.setCurDate( new Date() );
        List<PtwPtoSelectUserInfoVo> userInfos = ptwPtoSelectUserDao.queryPtwPtoUserInfo( ptwPtoSelectUserInfoVo );
        List<String> userList = new ArrayList<String>(0);
        for ( PtwPtoSelectUserInfoVo ptwPtoUserInfo : userInfos ) {
            String userId =  ptwPtoUserInfo.getUserId();
            String userName =  ptwPtoUserInfo.getUserName();        
            if ( !userList.contains( userId ) ) {
                SecureUser user = new SecureUser();
                user.setId( userId );
                user.setName( userName );
                resultList.add( user );
                userList.add( userId );
            }
        }
        return resultList;
    }


    @Override
    public Boolean hasAuditPrivilege(String category, String type, String step) throws Exception {
        LOG.info( "-------------进入两票选人接口实现 非工作流选人-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        List<String> resultList = new ArrayList<String>();
        String siteId  = userInfoScope.getSiteId();
        PtwPtoSelectUserInfoVo ptwPtoSelectUserInfoVo = new PtwPtoSelectUserInfoVo();
        ptwPtoSelectUserInfoVo.setType( type );
        ptwPtoSelectUserInfoVo.setStep( step );
        ptwPtoSelectUserInfoVo.setCategory( category );
        ptwPtoSelectUserInfoVo.setSiteid( siteId );
        ptwPtoSelectUserInfoVo.setCurDate( new Date() );
        List<PtwPtoSelectUserInfoVo> userInfos = ptwPtoSelectUserDao.queryPtwPtoUserInfo( ptwPtoSelectUserInfoVo );
        for ( PtwPtoSelectUserInfoVo ptwPtoUserInfo : userInfos ) {
            resultList.add( ptwPtoUserInfo.getUserId() );
        }
        String userId = userInfoScope.getUserId();
        Boolean result = resultList.contains( userId );
        return result;
    }
    @Override
    public List<String> queryPrivilegeTypes(String category, String step) throws Exception {
        LOG.info( "-------------进入两票选人接口实现 非工作流 类型集合-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        List<String> resultList = new ArrayList<String>();
        String siteId  = userInfoScope.getSiteId();
        PtwPtoSelectUserInfoVo ptwPtoSelectUserInfoVo = new PtwPtoSelectUserInfoVo();
        ptwPtoSelectUserInfoVo.setType( null );
        ptwPtoSelectUserInfoVo.setStep( step );
        ptwPtoSelectUserInfoVo.setCategory( category );
        ptwPtoSelectUserInfoVo.setSiteid( siteId );
        ptwPtoSelectUserInfoVo.setCurDate( new Date() );
        String userId = userInfoScope.getUserId();
        List<PtwPtoSelectUserInfoVo> userInfos = ptwPtoSelectUserDao.queryPtwPtoUserInfo( ptwPtoSelectUserInfoVo );
        for ( PtwPtoSelectUserInfoVo ptwPtoUserInfo : userInfos ) {
            if(userId.equals( ptwPtoUserInfo.getUserId() )){
                resultList.add( ptwPtoUserInfo.getType() );   
            }
        }
        return resultList;
    }
}

