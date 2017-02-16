package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.dao.InvMatApplyDao;
import com.timss.inventory.vo.InvMatApplyVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDaoTest.java
 * @author: 890166
 * @createDate: 2014-8-12
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvMatApplyDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatApplyDaoTest.class );

    @Autowired
    InvMatApplyDao invMatApplyDao;

    @Test
    public void testQueryMatApplyList() {
        Page<InvMatApplyVO> page = new Page<InvMatApplyVO>();
        page.setParameter( "siteId", "ITC" );
        List<InvMatApplyVO> imavList = invMatApplyDao.queryMatApplyList( page );
        for ( InvMatApplyVO imav : imavList ) {
            LOG.debug( "物资领料单号为===========>" + imav.getSheetno() );
        }
    }

    public void testQueryMatApplyForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "imaid", "IMAI0051" );
        map.put( "siteId", "ITC" );
        List<InvMatApplyVO> imavList = invMatApplyDao.queryMatApplyForm( map );
        for ( InvMatApplyVO imav : imavList ) {
            LOG.debug( "物资领料单号为===========>" + imav.getSheetno() );
        }
    }

    public void testUpdateInvMatApply() {
        InvMatApply ima = new InvMatApply();
        ima.setSiteId( "ITC" );
        ima.setImaid( "IMAI0068" );
        ima.setApplyType( "test_type" );
        int count = invMatApplyDao.updateInvMatApply( ima );
        LOG.debug( "领料更新===========>" + count );
    }

    public void testInsertInvMatApply() {
        InvMatApply ima = new InvMatApply();
        ima.setSheetname( "单元测试生成" );
        ima.setSiteId( "ITC" );
        int count = invMatApplyDao.insertInvMatApply( ima );
        LOG.debug( "领料插入===========>" + count );
    }
}
