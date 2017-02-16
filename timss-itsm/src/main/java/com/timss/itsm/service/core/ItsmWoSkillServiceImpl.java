package com.timss.itsm.service.core;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.itsm.bean.ItsmWoSkill;
import com.timss.itsm.dao.ItsmWoSkillDao;
import com.timss.itsm.service.ItsmWoSkillService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;

@Service
public class ItsmWoSkillServiceImpl implements ItsmWoSkillService {
    // @Autowired
    // private ItcMvcService ItcMvcService;
    @Autowired
    private ItsmWoSkillDao woSkillDao;

    private static final Logger LOG = Logger.getLogger( ItsmWoSkillServiceImpl.class );

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void insertWoSkill(Map<String, String> addWoSkillDataMap) {
        // UserInfoScope userInfoScope = ItcMvcService.getUserInfoScopeDatas();
        // String siteId = userInfoScope.getSiteId();
        // String userId = userInfoScope.getUserId();
        String skillForm = addWoSkillDataMap.get( "skillForm" );
        int id = woSkillDao.getNextParamsConfId();
        ItsmWoSkill woSkill;
        try {
            woSkill = JsonHelper.toObject( skillForm, ItsmWoSkill.class );
            woSkill.setId( id );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        woSkillDao.insertWoSkill( woSkill );

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateWoSkill(Map<String, String> addWoSkillDataMap) {
        String skillForm = addWoSkillDataMap.get( "skillForm" );
        ItsmWoSkill woSkill;
        try {
            woSkill = JsonHelper.toObject( skillForm, ItsmWoSkill.class );
        } catch (Exception e) {
            LOG.error( e.getMessage() );
            throw new RuntimeException( e );
        }

        woSkillDao.updateWoSkill( woSkill );
    }

    @Override
    public ItsmWoSkill queryWoSkillById(int id) {
        return woSkillDao.queryWoSkillById( id );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteWoSkillById(int id) {
        woSkillDao.deleteWoSkill( id );
    }

    @Override
    public Page<ItsmWoSkill> queryWoSkillList(Page<ItsmWoSkill> page) {

        List<ItsmWoSkill> ret = woSkillDao.queryWoSkillList( page );
        page.setResults( ret );
        LOG.info( "查询技能列表信息" );

        return page;
    }

}
