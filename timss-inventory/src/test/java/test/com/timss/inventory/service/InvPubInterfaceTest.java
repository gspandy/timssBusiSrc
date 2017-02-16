package test.com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatTransfer;
import com.timss.inventory.bean.InvMatTransferDetail;
import com.timss.inventory.service.InvMatTransferService;
import com.timss.inventory.service.InvPubInterface;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

public class InvPubInterfaceTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvPubInterfaceTest.class );

    @Autowired
    private InvPubInterface invPubInterface;

    @Autowired
    private InvMatTransferService invMatTransferService;

    @Autowired
    private ItcMvcService itcMvcService;

    public void testQueryAlreadyRefunding() {
        TestUnitGolbalService.SetCurentUserById( "950059", "ITC" );

        String imaid = "IMAI0719";
        Page<InvMatTranDetailVO> imtdPage = invPubInterface.queryAlreadyRefunding( imaid );
        List<InvMatTranDetailVO> imtdList = imtdPage.getResults();
        for ( InvMatTranDetailVO imtd : imtdList ) {
            LOG.info( "查询结果：" + imtd.getSheetno() );
        }

    }

    public void testQueryRefundingDetailI() {
        TestUnitGolbalService.SetCurentUserById( "950059", "ITC" );

        String imaid = "IMAI0719";
        String imrsno = null;
        List<InvMatReturnsDetailVO> imrdList = invPubInterface.queryRefundingDetailI( imaid, imrsno );
        for ( InvMatReturnsDetailVO imrd : imrdList ) {
            LOG.info( "查询结果：" + imrd.getItemname() );
        }
    }

    public void testQueryRefundingBtnIsHide() {
        TestUnitGolbalService.SetCurentUserById( "950059", "ITC" );

        String imaid = "IMAI0719";
        String flag = invPubInterface.queryRefundingBtnIsHide( imaid );
        LOG.info( "查询结果：" + flag );
    }

    public void testQueryItem2InvMatAcceptDetail() {
        TestUnitGolbalService.SetCurentUserById( "950059", "ITC" );

        String poId = "8139";
        List<InvMatAcceptDetailVO> imadList = invPubInterface.queryItem2InvMatAcceptDetail( poId );
        for ( InvMatAcceptDetailVO imad : imadList ) {
            LOG.info( "查询结果：" + imad.getInacId() );
        }

    }

    @Test
    public void testSaveTransfer2MatTran() throws Exception {
        TestUnitGolbalService.SetCurentUserById( "561131", "ZJW" );

        UserInfoScope ui = itcMvcService.getUserInfoScopeDatas();

        InvMatTransfer imtf = invMatTransferService.queryInvMatTransferById( ui, "IMTI0107" );
        List<InvMatTransferDetail> imtfdList = invMatTransferService.queryInvMatTransferDetailList( ui, "IMTI0107" );

        Map<String, String> reMap = invPubInterface.saveTransfer2MatTran( imtf, imtfdList );

        for ( Map.Entry<String, String> entry : reMap.entrySet() ) {
            LOG.info( ">>>>>>>>>>>>>>>返回状态：" + entry.getKey() + " | 返回信息：" + entry.getValue() + " ..." );
        }

    }
}
