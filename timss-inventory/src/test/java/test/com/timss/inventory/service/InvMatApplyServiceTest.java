package test.com.timss.inventory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyServiceTest.java
 * @author: 890166
 * @createDate: 2014-8-11
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatApplyServiceTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvMatApplyServiceTest.class );

    @Autowired
    InvMatApplyService invMatApplyService;

    public void workOrderTriggerProcessesTest() {
        TestUnitGolbalService.SetCurentUserById( "890135", "ITC" );
        HashMap<String, Object> hMap = new HashMap<String, Object>();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "III1088", 1 );
        jsonList.add( jsonObject );

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put( "III1089", 2 );
        jsonList.add( jsonObject1 );

        hMap.put( "items", jsonList );
        hMap.put( "applyUser", "890128" );
        hMap.put( "type", "out" );
        hMap.put( "woId", "113648" );
        hMap.put( "applyType", "itsm_picking" );
        hMap.put( "woCode", "WO20150209001" );
        hMap.put( "woName", "测试工单触发" );
        try {
            invMatApplyService.workOrderTriggerProcesses( hMap );
        } catch (Exception e) {
            throw new RuntimeException(
                    "---------InvMatApplyServiceTest 中的 workOrderTriggerProcessesTest 方法抛出异常---------：", e );
        }
    }

    public void queryMatApplyByWoIdTest() {
        TestUnitGolbalService.SetCurentUserById( "890135", "ITC" );
        Page<InvMatApplyToWorkOrder> ima2wo = new Page<InvMatApplyToWorkOrder>();
        try {
            ima2wo = invMatApplyService.queryMatApplyByWoId( "1084", "itsm_picking" );
            List<InvMatApplyToWorkOrder> list = ima2wo.getResults();
            for ( InvMatApplyToWorkOrder i : list ) {
                LOG.debug( i.getItemid() + "--------" + i.getItemcode() + "----" + i.getItemname() + "----"
                        + i.getOutQty() + "----" + i.getQtyApply() + "----" + i.getRefundQty() );
            }
        } catch (Exception e) {
            throw new RuntimeException( "---------InvMatApplyServiceTest 中的 queryMatApplyByWoIdTest 方法抛出异常---------：",
                    e );
        }
    }

    @Test
    public void testQueryMatApplyByOutterId() {
        TestUnitGolbalService.SetCurentUserById( "890135", "ITC" );
        try {
            List<InvMatApply> imaList = invMatApplyService.queryMatApplyByOutterId( "16765", "wo_picking" );
            for ( InvMatApply ima : imaList ) {
                LOG.info( " ------ 领料单号：" + ima.getSheetno() + " 领料单名称：" + ima.getSheetname() + " 领料id："
                        + ima.getImaid() + " ------" );
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
