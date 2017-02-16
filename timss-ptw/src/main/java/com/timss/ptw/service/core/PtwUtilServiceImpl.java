package com.timss.ptw.service.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.dao.PtoInfoDao;
import com.timss.ptw.dao.PtwUtilDao;
import com.timss.ptw.service.PtwUtilService;
import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwUtilServiceImpl implements PtwUtilService {
    private static final Logger log = Logger.getLogger( PtwUtilService.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwUtilDao ptwUtilDao;
    @Autowired
    private PtoInfoDao ptoInfoDao;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private IAuthorizationManager authorizationManager;
    @Override
    public List<String> enumValueInSomeSites(String ecatCode, String searchSiteid) {
        List<String> result = ptwUtilDao.queryEnumValueByEcatcode( ecatCode, searchSiteid );
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updatePtoCurrHandlerUser(String ptoId, UserInfoScope userInfoScope, String flag) {
        try {
            String userIds = "";
            if ( "normal".equals( flag ) ) {
                userIds = userInfoScope.getParam( "userIds" );
                if ( userIds == null ) {
                    return;
                }
                Map<String, List<String>> userIdsMap = (Map<String, List<String>>) new ObjectMapper()
                        .readValue( userIds, Map.class );
                Iterator iterator = userIdsMap.keySet().iterator();
                while (iterator.hasNext()) {
                    List<String> auditUserId = userIdsMap.get( iterator.next() );
                    String nextAuditUserIds = "";
                    String nextAuditUserNames = "";
                    for ( int i = 0; i < auditUserId.size(); i++ ) {
                        nextAuditUserIds = auditUserId.get( i ) + ",";
                        String tempUserIds = auditUserId.get( i );
                        if ( tempUserIds.indexOf( "," ) > 0 ) {
                            String[] auditUserNames = tempUserIds.split( "," );
                            for ( int j = 0; j < auditUserNames.length; j++ ) {
                                nextAuditUserNames += itcMvcService.getUserInfoById( auditUserNames[j] ).getUserName()
                                        + ",";
                            }
                        } else {
                            nextAuditUserNames = itcMvcService.getUserInfoById( auditUserId.get( i ) ).getUserName()
                                    + ",";
                        }
                    }
                    nextAuditUserIds = nextAuditUserIds.substring( 0, nextAuditUserIds.length() - 1 );
                    nextAuditUserNames = nextAuditUserNames.substring( 0, nextAuditUserNames.length() - 1 );

                    Map<String, String> parmas = new HashMap<String, String>();
                    parmas.put( "ptoId", ptoId );
                    parmas.put( "currHandlerUser", nextAuditUserIds );
                    parmas.put( "currHandUserName", nextAuditUserNames );
                    ptoInfoDao.updateCurrHandUserById( parmas );
                }
            } else if ( "rollback".equals( flag ) ) { // 回退的时候是单个人
                String nextAuditUserId = "";
                String nextAuditUserName = "";
                nextAuditUserId = userInfoScope.getParam( "userId" );
                nextAuditUserName = itcMvcService.getUserInfoById( nextAuditUserId ).getUserName();

                Map<String, String> params = new HashMap<String, String>();
                params.put( "currHandlerUser", nextAuditUserId );
                params.put( "currHandUserName", nextAuditUserName );
                params.put( "ptoId", ptoId );
                
                ptoInfoDao.updateCurrHandUserById( params );
            }
        } catch (Exception e) {
            log.error( e.getMessage() );
            throw new RuntimeException( e );
        }

    }

    
    /**
     * @description: 将bean中相关的人员的名字根据工号查询然后赋值进去
     * @author: 王中华
     * @createDate: 2015-8-2
     * @param ptoInfoVo:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void setRelatePersonName(PtoInfoVo ptoInfoVo ,String siteid) {
        String operator = ptoInfoVo.getOperator();
        String guardian = ptoInfoVo.getGuardian();
        String commander = ptoInfoVo.getCommander();
        String ondutyPrincipal = ptoInfoVo.getOndutyPrincipal();
        String ondutyMonitor = ptoInfoVo.getOndutyMonitor();
        
        if(!StringUtils.isEmpty( operator )){
           String tempNameString = authorizationManager.retriveUserById(operator ,siteid).getName();
           ptoInfoVo.setOperatorName( tempNameString );
        }
        if(!StringUtils.isEmpty( guardian )){
            //String tempNameString = authorizationManager.retriveUserById(guardian ,siteid).getName();
        	String tempNameString = getPersonName(guardian, siteid);
            ptoInfoVo.setGuardianName( tempNameString );     
        }
        if(!StringUtils.isEmpty( commander )){
            //String tempNameString = authorizationManager. retriveUserById(commander ,siteid).getName();
        	String tempNameString = getPersonName(commander, siteid);
            ptoInfoVo.setCommanderName( tempNameString );
        }
        if(!StringUtils.isEmpty( ondutyPrincipal )){
            String tempNameString = authorizationManager. retriveUserById(ondutyPrincipal ,siteid).getName();
            ptoInfoVo.setOndutyPrincipalName( tempNameString );
        }
        if(!StringUtils.isEmpty( ondutyMonitor )){
            String tempNameString = authorizationManager.retriveUserById(ondutyMonitor ,siteid).getName();
            ptoInfoVo.setOndutyMonitorName( tempNameString );
        }
        
    }
    
    private String getPersonName(String personIds, String siteid){
    	String[] personArr = personIds.split(",");
    	String personNames = "";
    	for(String personId : personArr){
    		String tempNameString = authorizationManager.retriveUserById(personId ,siteid).getName();
    		personNames += tempNameString + ",";
    	}
    	if(!StringUtils.isEmpty( personNames )){
    		personNames = personNames.substring(0, personNames.length()-1);
    	}
    	return personNames;
    }

    @Override
    public void modifyHomepageTodoName(String todoFlowNo, String newName) {
        homepageService.modify( null, todoFlowNo, null, newName, null, null, null, null );
    }
}
