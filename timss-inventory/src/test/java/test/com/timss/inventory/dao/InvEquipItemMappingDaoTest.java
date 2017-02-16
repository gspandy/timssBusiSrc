package test.com.timss.inventory.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.inventory.bean.InvEquipItemMapping;
import com.timss.inventory.dao.InvEquipItemMappingDao;
import com.timss.inventory.service.InvEquipItemMappingService;
import com.timss.inventory.vo.SpareBean;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvEquipItemMappingDaoTest.java
 * @author: 890166
 * @createDate: 2014-7-19
 * @updateUser: 890166
 * @version: 1.0
 */
@ContextConfiguration(locations = { "classpath:config/context/applicationContext-workflow.xml" })
public class InvEquipItemMappingDaoTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvEquipItemMappingDaoTest.class );

    @Autowired
    InvEquipItemMappingDao invEquipItemMappingDao;

    @Autowired
    InvEquipItemMappingService invEquipItemMappingService;

    @Test
    public void testInsertMappingInfoByItemInfo() {
        InvEquipItemMapping iem = new InvEquipItemMapping();
        iem.setItemid( "1" );
        iem.setEquipid( "AST-9992" );
        iem.setEquipname( "测试" );
        iem.setSiteId( "ITC" );
        invEquipItemMappingDao.insertMappingInfoByItemInfo( iem );
        LOG.debug( "插入的设备名称========>" + iem.getEquipname() );
    }

    @Test
    public void testDeleteMappingInfoByItemInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemid", "1" );
        map.put( "siteid", "ITC" );
        map.put( "equipid", "AST-9992" );
        int count = invEquipItemMappingDao.deleteMappingInfoByItemInfo( map );
        LOG.debug( "删除数据条数========>" + count );
    }

    @Test
    public void testGetApareByAssetId() {
        try {
            Page<SpareBean> page = new Page<SpareBean>();
            page.setParameter( "siteId", "ITC" );
            page.setParameter( "assetId", "AST-00010701" );

            List<SpareBean> abList = invEquipItemMappingDao.getSpareByAssetId( page );
            for ( SpareBean ab : abList ) {
                LOG.debug( "设备代码为========>" + ab.getSparecode() );
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "---------InvEquipItemMappingDaoTest 中的 testGetApareByAssetId 方法抛出异常---------：", e );
        }
    }
}
