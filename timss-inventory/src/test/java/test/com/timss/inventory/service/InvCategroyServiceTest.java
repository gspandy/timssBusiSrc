package test.com.timss.inventory.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.service.InvCategroyService;
import com.timss.inventory.vo.InvCategoryParam;
import com.yudean.mvc.testunit.TestUnit;
import com.yudean.mvc.testunit.TestUnitGolbalService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvCategroyServiceTest.java
 * @author: user
 * @createDate: 2016-1-8
 * @updateUser: user
 * @version: 1.0
 */
public class InvCategroyServiceTest extends TestUnit {

    private static final Logger LOG = Logger.getLogger( InvCategroyServiceTest.class );

    @Autowired
    public InvCategroyService invCategroyService;

    @Test
    public void queryCategroyTest() {

        TestUnitGolbalService.SetCurentUserById( "326519", "ZJW" );

        InvCategoryParam icParam = new InvCategoryParam();
        icParam.setLevel( "2" );
        icParam.setSiteId( "ZJW" );
        icParam.setWarehouseId( "IWI3439" );

        InvCategory ic = new InvCategory();
        ic.setInvcatename( "备品" );
        icParam.setInvCategory( ic );

        List<InvCategory> icList = invCategroyService.queryCategroy( icParam );

        if ( null != icList && !icList.isEmpty() ) {
            for ( InvCategory icate : icList ) {
                LOG.info( ">>>>>>>>>>>> 物资类型名称：" + icate.getInvcatename() + " | 物资类型id：" + icate.getInvcateid()
                        + " | 物资类型描述：" + icate.getDescriptions() + " | 物资类型父节点id：" + icate.getParentid() + " | 物资类型状态："
                        + icate.getStatus() );
            }
        }

    }
}
