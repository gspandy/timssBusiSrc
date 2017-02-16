package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvMatTranDetailDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatTranDetailDaoTest.class );

    @Autowired
    InvMatTranDetailDao invMatTranDetailDao;

    @Autowired
    InvMatMappingDao invMatMappingDao;

    @Test
    public void testQueryStockInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemCode", "M200316" );
        map.put( "siteId", "ITC" );
        List<InvMatTranDetailVO> imtdList = invMatTranDetailDao.queryStockInfo( map );
        for ( InvMatTranDetailVO imtd : imtdList ) {
            LOG.debug( "审批单号为===========>" + imtd.getSheetno() );
        }
    }

    /*
     * @Test public void testQueryMatTranDetail(){ Page<InvMatTranDetailVO> page
     * = new Page<InvMatTranDetailVO>(); page.setParameter("imtid", "IMI0114");
     * page.setParameter("siteId", "ITC"); List<InvMatTranDetailVO> imtdList =
     * invMatTranDetailDao.queryMatTranDetail(page); for(InvMatTranDetailVO
     * imtd:imtdList){ LOG.debug("审批单号为===========>"+imtd.getSheetno()); } }
     */

    @Test
    public void testQueryInvMatMappingByOutterInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "outterId", "IMADI0512" );
        map.put( "type", "pickingmaterials" );
        List<InvMatMapping> immList = invMatMappingDao.queryInvMatMappingByOutterInfo( map );
        for ( InvMatMapping imm : immList ) {
            LOG.debug( "接收单id为===========>" + imm.getImtdid() );
        }
    }

    @Test
    public void testQueryInvMatTranDetailByImtdid() {
        List<InvMatTranDetail> imtdList = invMatTranDetailDao.queryInvMatTranDetailByImtdid( "IMDI0007" );
        for ( InvMatTranDetail imtd : imtdList ) {
            LOG.debug( "物资编号为===========>" + imtd.getItemcode() );
        }
    }

    @Test
    public void testQueryInvMatTranDetailByItemInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemId", "1" );
        map.put( "itemCode", "M200299" );
        map.put( "siteId", "ITC" );
        List<InvMatTranDetail> imtdList = invMatTranDetailDao.queryInvMatTranDetailByItemInfo( map );
        for ( InvMatTranDetail imtd : imtdList ) {
            LOG.debug( "物资编号为===========>" + imtd.getItemcode() );
        }
    }

    @Test
    public void testInsertInvMatTranDetail() {
        InvMatTranDetail imtd = new InvMatTranDetail();
        imtd.setImtdid( "12" );
        imtd.setSiteId( "ITC" );
        int count = invMatTranDetailDao.insertInvMatTranDetail( imtd );
        LOG.debug( "插入数据条数为===========>" + count );
    }

    @Test
    public void testDeleteMatTranDetailByImtid() {
        int count = invMatTranDetailDao.deleteMatTranDetailByImtid( "IMAI0050" );
        LOG.debug( "删除数据条数为===========>" + count );
    }

    @Test
    public void testDeleteMatTranDetailById() {
        int count = invMatTranDetailDao.deleteMatTranDetailById( "IMADI0325" );
        LOG.debug( "删除数据条数为===========>" + count );
    }

    @Test
    public void testQueryStockOperInfo() {
        Page<InvMatTranDetailVO> page = new Page<InvMatTranDetailVO>();
        page.setParameter( "itemCode", "I200002" );
        page.setParameter( "siteId", "ITC" );
        List<InvMatTranDetailVO> imtdList = invMatTranDetailDao.queryStockOperInfo( page );
        for ( InvMatTranDetailVO imtd : imtdList ) {
            LOG.debug( "审批单号为===========>" + imtd.getSheetno() );
        }
    }

    @Test
    public void testQueryMatTranDetailByImtid() {
        List<InvMatTranDetail> imtdList = invMatTranDetailDao.queryMatTranDetailByImtid( "IMAI0064" );
        for ( InvMatTranDetail imtd : imtdList ) {
            LOG.debug( "物资编号为===========>" + imtd.getItemcode() );
        }
    }
}
