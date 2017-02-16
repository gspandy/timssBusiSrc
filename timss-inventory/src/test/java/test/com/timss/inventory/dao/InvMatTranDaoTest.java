package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvMatTranDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatTranDaoTest.class );

    @Autowired
    InvMatTranDao invMatTranDao;

    @Test
    public void testInsertInvMatTran() {
        InvMatTran imt = new InvMatTran();
        imt.setSiteId( "ITC" );
        imt.setRemark( "1111" );
        int count = invMatTranDao.insertInvMatTran( imt );
        LOG.debug( "插入条数==========>" + count );
    }

    @Test
    public void testQueryInvMatTranById() {
        List<InvMatTran> imtList = invMatTranDao.queryInvMatTranById( "IMI0109" );
        for ( InvMatTran imt : imtList ) {
            LOG.debug( "物资备注信息==========>" + imt.getRemark() );
        }
    }

    @Test
    public void testQueryMatTranList() {
        Page<InvMatTranVO> page = new Page<InvMatTranVO>();
        page.setParameter( "siteId", "ITC" );
        List<InvMatTranVO> imtvList = invMatTranDao.queryMatTranList( page );
        for ( InvMatTranVO imtv : imtvList ) {
            LOG.debug( "物资接收中采购合同号为==========>" + imtv.getPruorderno() );
        }
    }

    @Test
    public void testQueryMatTranForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", "ITC" );
        map.put( "imtid", "IMI0113" );
        List<InvMatTranVO> imtList = invMatTranDao.queryMatTranForm( map );
        for ( InvMatTranVO imtv : imtList ) {
            LOG.debug( "物资接收中采购合同号为==========>" + imtv.getPruorderno() );
        }
    }

    @Test
    public void testQueryPurOrderList() {
        Page<MTPurOrderVO> page = new Page<MTPurOrderVO>();
        page.setParameter( "siteId", "ITC" );
        List<MTPurOrderVO> mpoList = invMatTranDao.queryPurOrderList( page );
        for ( MTPurOrderVO mt : mpoList ) {
            LOG.debug( "审批单名称==========>" + mt.getSheetname() );
        }
    }

    @Test
    public void testUpdateMatTran() {
        InvMatTran imt = new InvMatTran();
        imt.setImtid( "IMI0111" );
        imt.setWarehouseid( "IWHI3522" );
        int count = invMatTranDao.updateMatTran( imt );
        LOG.debug( "更新条数==========>" + count );
    }

    @Test
    public void testGetUserHint() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "keyWord", "周" );
        List<Map<String, Object>> mapList = invMatTranDao.getUserHint( map );
        for ( Map<String, Object> hmap : mapList ) {
            LOG.debug( "查询到人名为==========>" + hmap.get( "NAME" ) );
        }
    }
}
