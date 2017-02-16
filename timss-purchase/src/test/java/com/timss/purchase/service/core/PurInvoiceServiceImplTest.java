package com.timss.purchase.service.core;

import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.com.timss.purchase.dao.PurVendorDaoTest;

import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.dao.PurInvoiceDao;
import com.timss.purchase.service.PurInvoiceService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
public class PurInvoiceServiceImplTest  extends TestUnit{

    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PurInvoiceService purInvoiceService;
    @Autowired
    private PurInvoiceDao purInvoiceDao;
    @Autowired
    private PurPubInterface purPubInterface;
    private static final Logger log = Logger.getLogger(PurVendorDaoTest.class);
    @Test
    public void testQueryInvoiceBySiteId() {
        TestUnitGolbalService.SetCurentUserById("890166", "ITC");
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PurInvoiceBean> paramsPage = userInfoScope.getPage();
        paramsPage.setSortKey( "sheetNo" );
        paramsPage.setSortOrder( "desc" );
        
        String siteId = userInfoScope.getSiteId();
        
        paramsPage.setParameter( "siteId", siteId );
        paramsPage.setParameter( "isDelete", "N" );
        
        List<PurInvoiceBean> invoiceBeans = purInvoiceDao.queryInvoiceBySiteId( paramsPage );
        log.info( invoiceBeans );
    }
    
    @Test
    public void testQueryPurOrderVOBySheetId() {
        TestUnitGolbalService.SetCurentUserById("890166", "ITC");
        PurOrderVO result = purPubInterface.queryPurOrderVOBySheetId( "8020" );
        log.info( result.toString() );
    }
    
    @Test
    public void testqueryInvoiceBaseInfoByContractId() {
        TestUnitGolbalService.SetCurentUserById("890127", "ITC");
         List<PurInvoiceBean> result = purInvoiceService.queryInvoiceBaseInfoByContractId( "8017" );
         log.info( result.toString() );
    }
    @Test
    public void testremindInvoice() {
        TestUnitGolbalService.SetCurentUserById("890127", "ITC");
        List<String> sites = purInvoiceService.queryAllSite();
        for( String siteId : sites ){
            TestUnitGolbalService.SetCurentUserById("superadmin", siteId);
            purInvoiceService.remindInvoice( siteId );
        }
    }
    
    

    @Test
    public void testQueryInvoiceBySearch() {
        fail( "Not yet implemented" );
    }
    @Test
    public void testQueryInvoiceById() {
        TestUnitGolbalService.SetCurentUserById("890166", "ITC");
        PurInvoiceBean reuslt = purInvoiceService.queryInvoiceById( "5jas1ddv62bqxwt8" );
        log.info( reuslt.toString() );
    }
    @Test
    public void testQueryInvoiceItemById() {
        TestUnitGolbalService.SetCurentUserById("890166", "ITC");
        List<PurInvoiceAssetVo> reuslt = purInvoiceService.queryInvoiceItemById( "5jas1ddv62bqxwt8" );
        log.info( reuslt.toString() );
    }

    @Test
    public void testQueryWuziByContractId() {
        
        TestUnitGolbalService.SetCurentUserById("890166", "ITC");
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Page<PurInvoiceAssetVo> paramsPage = userInfoScope.getPage();
        String siteId = userInfoScope.getSiteId();
        
        paramsPage.setParameter( "siteId", siteId );
        paramsPage.setParameter( "sheetId", "8020" );
        Page<PurInvoiceAssetVo> result = purInvoiceService.queryWuziByContractId( paramsPage );
        log.info( result );
    }

    @Test
    public void testInsertOrUpdateInvoice() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testDeleteInvoiceByInvoiceId() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testUpdateInvoice() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testInsertWuziItem() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testInsertInvoice() {
        fail( "Not yet implemented" );
    }

}
