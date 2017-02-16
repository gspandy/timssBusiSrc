package test.com.timss.inventory.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvItem;
import com.timss.inventory.dao.InvEquipItemMappingDao;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvItemDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvItemDaoTest.class );

    @Autowired
    InvItemDao invItemDao;

    @Autowired
    InvEquipItemMappingDao invEquipItemMappingDao;

    public void testQueryItemsList() {
        Page<InvItemVO> page = new Page<InvItemVO>();
        page.setParameter( "siteId", "ITC" );

        List<InvItemVO> iivList = invItemDao.queryItemsList( page );
        for ( InvItemVO iiv : iivList ) {
            LOG.debug( "物资名称为===========>" + iiv.getItemname() );
        }
    }

    public void testQuerySiteById() {
        List<Map<String, String>> mList = invItemDao.querySiteById( "ITC" );
        for ( Map<String, String> m : mList ) {
            LOG.debug( "站点名称为===========>" + m.get( "SITE_NAME" ) );
        }
    }

    public void testQueryInvItemDetail() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemCode", "M200301" );
        map.put( "siteId", "ITC" );
        List<InvItemVO> iivList = invItemDao.queryInvItemDetail( map );
        for ( InvItemVO iiv : iivList ) {
            LOG.debug( "物资名称为===========>" + iiv.getItemname() );
        }
    }

    public void testQueryInvMainItemDetail() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemId", "37" );
        map.put( "siteId", "ITC" );

        List<InvItemVO> iivList = invItemDao.queryInvMainItemDetail( map );
        for ( InvItemVO iiv : iivList ) {
            LOG.debug( "物资名称为===========>" + iiv.getItemname() );
        }
    }

    public void testQueryEquipInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemId", "37" );
        map.put( "siteId", "ITC" );

        List<Map<String, Object>> mList = invEquipItemMappingDao.queryEquipInfo( map );
        for ( Map<String, Object> m : mList ) {
            LOG.debug( "设备ID为===========>" + m.get( "EQUIPID" ) );
        }
    }

    public void testQueryInvItemByItemid() {
        List<InvItem> iiList = invItemDao.queryInvItemByItemid( "37" );
        for ( InvItem ii : iiList ) {
            LOG.debug( "物资名称为===========>" + ii.getItemname() );
        }
    }

    public void testQueryArrivalItem() {
        Page<InvItemVO> page = new Page<InvItemVO>();
        page.setParameter( "siteId", "ITC" );
        page.setParameter( "pruorderno", "'PO201408057473'" );
        List<InvItemVO> iivList = invItemDao.queryArrivalItem( page );
        for ( InvItemVO iiv : iivList ) {
            LOG.debug( "物资名称为===========>" + iiv.getItemname() );
        }
    }

    public void testQueryItemInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteid", "ITC" );
        List<InvItemVO> iivList = invItemDao.queryItemInfo( map );
        for ( InvItemVO iiv : iivList ) {
            LOG.debug( "物资名称为===========>" + iiv.getItemname() );
        }
    }

    public void testUpdateInvItemInfo() {
        InvItem ii = new InvItem();
        ii.setItemid( "31" );
        ii.setModifydate( new Date() );
        int count = invItemDao.updateInvItemInfo( ii );
        LOG.debug( "物资更新条数为===========>" + count );
    }

    public void testInsertInvItemInfo() {
        InvItem ii = new InvItem();
        ii.setSiteId( "ITC" );
        ii.setCreatedate( new Date() );
        int count = invItemDao.insertInvItemInfo( ii );
        LOG.debug( "物资插入条数为===========>" + count );
    }

    public void testQueryInvItemSafetyStock() {
        String siteId = "ITC";
        Page<InvItemVO> page = new Page<InvItemVO>();

        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );

        page.setParameter( "siteid", siteId );
        List<InvItemVO> rst = invItemDao.queryInvItemSafetyStock( page );
        LOG.debug( "InvItemDaoTest.testQueryInvItemSafetyStock()------->size:" + rst.size() );
        int size = invItemDao.queryInvItemSafetyStockNum( siteId );
        LOG.debug( "InvItemDaoTest.testQueryInvItemSafetyStock()------->queryInvItemSafetyStockNum:" + size );
    }
}
