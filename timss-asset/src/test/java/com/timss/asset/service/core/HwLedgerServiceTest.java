package com.timss.asset.service.core;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.asset.bean.HwLedgerBean;
import com.timss.asset.service.HwLedgerService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * 
 * @title: 硬件总账测试
 * @description: {desc}
 * @company: gdyd
 * @className: HwLedgerServiceImplTest.java
 * @author: fengzt
 * @createDate: 2014年11月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
@ContextConfiguration(locations={"classpath:config/context/applicationContext-webserviceClient-config.xml",
"classpath:config/context/applicationContext-workflow.xml"})
public class HwLedgerServiceTest  extends TestUnit {
    
    @Autowired
    private HwLedgerService hwLedgerService;
    
    @Test
    public void testInsertHwLedger() {
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        
        HwLedgerBean hwLedgerBean = new HwLedgerBean();
        hwLedgerBean.setHwName( "信息科技公司" );
        hwLedgerBean.setIsRoot( "Y" );
        hwLedgerBean.setHwType( "ast_hwl_type_0" );
        hwLedgerBean.setParentId( "0" );
        
        int count = hwLedgerService.insertHwLedger( hwLedgerBean  );
        Assert.assertTrue( count == 1 );
        
    }
    
    /**
     * 
     * @description:插入子节点
     * @author: fengzt
     * @createDate: 2014年11月24日:
     */
    @Test
    public void testInsertHwLedger2() {
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        
        HwLedgerBean hwLedgerBean = new HwLedgerBean();
        hwLedgerBean.setHwName( "物理地点" );
        hwLedgerBean.setIsRoot( "N" );
        hwLedgerBean.setHwType( "HW_L_ROOM" );
        hwLedgerBean.setParentId( "HW-00000002" );
        
        int count = hwLedgerService.insertHwLedger( hwLedgerBean  );
        Assert.assertTrue( count == 1 );
        
    }
    
    /**
     * 
     * @description:插入子节点
     * @author: fengzt
     * @createDate: 2014年11月24日:
     */
    @Test
    public void testInsertHwLedger3() {
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        
        HwLedgerBean hwLedgerBean = new HwLedgerBean();
        hwLedgerBean.setHwName( "机房" );
        hwLedgerBean.setIsRoot( "N" );
        hwLedgerBean.setHwType( "HW_L_ROOM" );
        hwLedgerBean.setParentId( "HW-00000002" );
        
        int count = hwLedgerService.insertHwLedger( hwLedgerBean  );
        Assert.assertTrue( count == 1 );
        
    }

    @Test
    public void testQueryHwLedgerBySite() {
    }

    @Test
    public void testUpdateHwLedger() {
    }
    
    @Test
    public void testDeleteHwLedger() {
        
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        Map<String, Object> dataMap = hwLedgerService.deleteHwLedger( "HW_L_ROOM", "HW-00000004" );
        Assert.assertEquals( dataMap.get( "result" ), "success" );
    }
    
    @Test
    public void testQueryHwLedgerServiceById() {
        
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        Map<String, Object> dataMap = hwLedgerService.queryHwLedgerServiceById( "HW-00000016" );
        
    }
    
    @Test
    public void testQueryHwLedgerByTypeAndId() {
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        String hwId = "HW-00000019";
        Map<String, Object> dataMap = hwLedgerService.queryHwLedgerByTypeAndId( hwId, "HW_L_SERVER" );
        
        System.out.println( dataMap.size() );
    }
    
    @Test
    public void testQueryHwLedgerByTypeAndIdVm() {
        TestUnitGolbalService.SetCurentUserById("890128", "ITC");
        String hwId = "HW-00000020";
        Map<String, Object> dataMap = hwLedgerService.queryHwLedgerByTypeAndId( hwId, "HW_L_VM" );
        
        System.out.println( dataMap.size() );
    }

}
