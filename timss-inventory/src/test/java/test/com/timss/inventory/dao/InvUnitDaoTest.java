package test.com.timss.inventory.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvUnit;
import com.timss.inventory.dao.InvUnitDao;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvUnitDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvUnitDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvUnitDaoTest.class );

    @Autowired
    InvUnitDao invUnitDao;

    @Test
    public void testUnitCRUD() {
        String code = "code";
        String site = "ITC";
        InvUnit prev = invUnitDao.queryUnitByCodeAndSiteId( site, code );
        if ( prev == null ) {
            LOG.debug( "InvUnitDaoTest.isUnitCodeExist()->N" );
        } else {
            LOG.debug( "InvUnitDaoTest.isUnitCodeExist()->Y" );
        }

        InvUnit wh = new InvUnit();
        wh.setActive( "Y" );
        wh.setUnitcode( code );
        wh.setSiteId( site );
        wh.setUnitname( "这是一个测试计量单位" );
        Integer result = invUnitDao.insertUnitInfo( wh );
        LOG.debug( "InvUnitDaoTest.insertUnitInfo()->" + result );

        Page<InvUnit> page = new Page<InvUnit>();
        page.setParameter( "siteId", site );
        List<InvUnit> list = invUnitDao.queryUnitListBySiteId( page );
        LOG.debug( "InvUnitDaoTest.queryUnitListBySiteId()->" + list.size() );

        LOG.debug( "InvUnitDaoTest.queryUnitDetail()->"
                + invUnitDao.queryUnitDetail( wh.getUnitid(), "ITC" ).getUnitname() );

        wh.setDescriptions( "这是用来测试更新方法" );
        result = invUnitDao.updateUnitInfo( wh );
        LOG.debug( "InvUnitDaoTest.updateUnitInfo()->" + result );

        result = invUnitDao.updateUnitState( wh.getUnitid(), "N", "ITC" );
        LOG.debug( "InvUnitDaoTest.updateUnitState()->" + result );

        result = invUnitDao.deleteUnit( wh.getUnitid(), "ITC" );
        LOG.debug( "InvUnitDaoTest.deleteUnit()->" + result );
    }
}