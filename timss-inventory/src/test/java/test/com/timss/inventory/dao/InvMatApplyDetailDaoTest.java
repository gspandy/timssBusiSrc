package test.com.timss.inventory.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailDaoTest.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvMatApplyDetailDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatApplyDetailDaoTest.class );

    @Autowired
    InvMatApplyDetailDao invMatApplyDetailDao;

    public void testQueryMatApplyDetailList() {
        Page<InvMatApplyDetailVO> page = new Page<InvMatApplyDetailVO>();
        page.setParameter( "imaid", "IMAI0051" );
        page.setParameter( "siteId", "ITC" );
        List<InvMatApplyDetailVO> imadList = invMatApplyDetailDao.queryMatApplyDetailList( page );
        for ( InvMatApplyDetailVO imadv : imadList ) {
            LOG.debug( "领料物资名称为==============>" + imadv.getItemname() );
        }
    }

    public void testQueryMatApplyDetailCSList() {
        Page<InvMatApplyDetailVO> page = new Page<InvMatApplyDetailVO>();
        page.setParameter( "imaid", "IMAI0051" );
        page.setParameter( "siteId", "ITC" );
        List<InvMatApplyDetailVO> imadList = invMatApplyDetailDao.queryMatApplyDetailCSList( page );
        for ( InvMatApplyDetailVO imadv : imadList ) {
            LOG.debug( "领料物资名称为==============>" + imadv.getItemname() );
        }
    }

    public void testQueryConsumingList() {
        Page<InvMatApplyDetailVO> page = new Page<InvMatApplyDetailVO>();
        page.setParameter( "siteId", "ITC" );
        List<InvMatApplyDetailVO> imadList = invMatApplyDetailDao.queryConsumingList( page );
        for ( InvMatApplyDetailVO imadv : imadList ) {
            LOG.debug( "领料物资名称为==============>" + imadv.getItemname() );
        }
    }

    public void testDeleteInvMatApplyDetailByImaId() {
        int count = invMatApplyDetailDao.deleteInvMatApplyDetailByImaId( "IMAI0068" );
        LOG.debug( "删除条数为==============>" + count );
    }

    public void testInsertInvMatApplyDetail() {
        InvMatApplyDetail imad = new InvMatApplyDetail();
        imad.setCreateuser( "890166" );
        imad.setCreatedate( new Date() );
        int count = invMatApplyDetailDao.insertInvMatApplyDetail( imad );
        LOG.debug( "插入条数为==============>" + count );
    }
}
