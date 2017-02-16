package com.timss.ptw.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwPtoStepInfo;
import com.timss.ptw.bean.PtwPtoUserInfo;
import com.timss.ptw.bean.PtwPtoUserInfoConfig;
import com.timss.ptw.dao.PtwPtoUserInfoDao;
import com.timss.ptw.service.PtwPtoUserInfoService;
import com.timss.ptw.vo.PtwPtoUserInfoVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwPtoUserInfoServiceImpl implements PtwPtoUserInfoService {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private PtwPtoUserInfoDao ptwPtoUserInfoDao;
    private static final Logger LOG = Logger.getLogger( PtwPtoUserInfoServiceImpl.class );

    @Override
    public Page<PtwPtoUserInfoConfig> queryUserInfoConfig(Page<PtwPtoUserInfoConfig> page) throws Exception {
        List<PtwPtoUserInfoConfig> list = ptwPtoUserInfoDao.queryUserInfoConfig( page );
        page.setResults( list );
        LOG.info( "查询两票审批人员配置列表信息" );
        return page;
    }
    @Override
    public boolean isUserInfoConfigConflict(PtwPtoUserInfoConfig config) throws Exception {
        boolean result = false;
        List<PtwPtoUserInfoConfig> list = ptwPtoUserInfoDao.isUserInfoConfigConflict( config );
        if(0<list.size()){
            result = true;
        }
        LOG.info( "查询两票审批人员配置是否冲突" );
        return result;
    }

    @Override
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public Map<String, Object> saveOrUpdateUserInfo(PtwPtoUserInfoVo ptwPtoUserInfoVo) throws Exception {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> resultMap = new HashMap<String, Object>( 0 );
        String id = ptwPtoUserInfoVo.getId();
        PtwPtoUserInfoConfig ptwPtoUserInfoConfig = new PtwPtoUserInfoConfig();
        ptwPtoUserInfoConfig.setBeginDate( ptwPtoUserInfoVo.getBeginDate() );
        ptwPtoUserInfoConfig.setEndDate( ptwPtoUserInfoVo.getEndDate() );
        ptwPtoUserInfoConfig.setCategory( ptwPtoUserInfoVo.getCategory() );
        ptwPtoUserInfoConfig.setType( ptwPtoUserInfoVo.getType() );
        ptwPtoUserInfoConfig.setStep( ptwPtoUserInfoVo.getStep() );
        if ( StringUtils.isEmpty( id ) ) { // save
            String siteid = userInfoScope.getSiteId();
            ptwPtoUserInfoConfig.setSiteid( siteid );
            ptwPtoUserInfoConfig.setDelFlag( "N" );
            try {
                ptwPtoUserInfoDao.insertUserInfoConfig( ptwPtoUserInfoConfig );
                List<String> ptoUserLists = ptwPtoUserInfoVo.getUserList();
                for ( String ptoUserId : ptoUserLists ) {
                    PtwPtoUserInfo ptwPtoUserInfo = new PtwPtoUserInfo();
                    ptwPtoUserInfo.setConfigId( ptwPtoUserInfoConfig.getId() );
                    ptwPtoUserInfo.setUserId( ptoUserId );
                    ptwPtoUserInfoDao.insertUserInfo( ptwPtoUserInfo );
                }
            } catch (Exception e) {
                LOG.warn( "新增两票审批人员异常", e );
                throw new Exception();
            }
        } else {
            // update
            ptwPtoUserInfoConfig.setId( id );
            ptwPtoUserInfoConfig.setModifyuser( userInfoScope.getUserId() );
            ptwPtoUserInfoConfig.setModifydate( new Date() );
            String[] params = new String[] { "beginDate", "endDate", "category", "type", "step" };
            ptwPtoUserInfoDao.updateUserInfoConfig( ptwPtoUserInfoConfig, params );
            ptwPtoUserInfoDao.deleteUserInfo( id );
            List<String> ptoUserLists = ptwPtoUserInfoVo.getUserList();
            for ( String ptoUserId : ptoUserLists ) {
                PtwPtoUserInfo ptwPtoUserInfo = new PtwPtoUserInfo();
                ptwPtoUserInfo.setConfigId( ptwPtoUserInfoConfig.getId() );
                ptwPtoUserInfo.setUserId( ptoUserId );
                ptwPtoUserInfoDao.insertUserInfo( ptwPtoUserInfo );
            }
        }
        resultMap.put( "id", id );
        return resultMap;
    }

    @Override
    public PtwPtoUserInfoVo queryUserInfoByConfigId(String id) {
        PtwPtoUserInfoConfig ptwPtoUserInfoConfig = ptwPtoUserInfoDao.queryUserInfoConfigById( id );
        List<String> ptwPtoUserIds = ptwPtoUserInfoDao.queryUserInfoListByConfigId( id );
        List<String> userList = new ArrayList<String>(0);
        for ( String ptwPtoUserId : ptwPtoUserIds ) {
            userList.add( ptwPtoUserId );
        }
        PtwPtoUserInfoVo ptwPtoUserInfoVo = new PtwPtoUserInfoVo();
        ptwPtoUserInfoVo.setUserList( userList );
        ptwPtoUserInfoVo.setId( ptwPtoUserInfoConfig.getId() );
        ptwPtoUserInfoVo.setBeginDate( ptwPtoUserInfoConfig.getBeginDate() );
        ptwPtoUserInfoVo.setEndDate( ptwPtoUserInfoConfig.getEndDate() );
        ptwPtoUserInfoVo.setCategory( ptwPtoUserInfoConfig.getCategory() );
        ptwPtoUserInfoVo.setStep( ptwPtoUserInfoConfig.getStep() );
        ptwPtoUserInfoVo.setType( ptwPtoUserInfoConfig.getType() );
        return ptwPtoUserInfoVo;
    }
    @Override
    public List<PtwPtoStepInfo> queryStepInfo(PtwPtoStepInfo ptwPtoStepInfo) {
        List<PtwPtoStepInfo> list = ptwPtoUserInfoDao.queryStepInfo(ptwPtoStepInfo);
        return list;
    }

    @Override
    public List<String> queryRelatedOrgListBySiteId(String siteId){
        List<String> orgCodes = ptwPtoUserInfoDao.queryOrgCodeWithSiteId( siteId );
        List<String> orgList = new ArrayList<String>(0);
        if(0<orgCodes.size()){
            String orgCode = orgCodes.get( 0 ); 
            orgList = ptwPtoUserInfoDao.queryRelatedOrgListStartWithOrgCode( orgCode );
            orgList.addAll( ptwPtoUserInfoDao.queryRelatedOrgListEndWithOrgCode( orgCode ));
        }
        return orgList;
    }
    @Override
    public void deleteUserInfoByConfigId(String id) {
        LOG.info( "-------------删除两票审批人员ID：" + id + "----------------------" );
        ptwPtoUserInfoDao.deleteUserInfo( id );
    }
    
   
}
