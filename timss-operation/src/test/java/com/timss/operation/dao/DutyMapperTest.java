package com.timss.operation.dao;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.timss.operation.bean.Duty;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.testunit.TestUnit;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-mybatis.xml" })*/
@TransactionConfiguration( defaultRollback = true )
public class DutyMapperTest extends TestUnit{

    //@Autowired
  //  DutyDao dutyMapper = applicationContext.getBean( DutyDao.class );

    /**
     * 测试插入 作者: fengzt 创建日期:2014年5月29日
     */
   // @Test
    public void insertDutyTest() {
        Duty duty = new Duty();
        duty.setDeptId( 1 );

        duty.setName( "test1" );
        duty.setNum( "num10101" );
        duty.setSortType( 0 );

        // dutyService.insertDuty(duty);
        System.out.println( new Date() );
    }

    /**
     * 更新值别 作者: fengzt 创建日期:2014年5月29日
     */
   // @Test
    public void updateDutyTest() {
        
        Duty duty = new Duty();
        duty.setDeptId( 1 );
        duty.setId( 2 );
        duty.setName( "test2" + new Date().toString() );
        duty.setNum( "num10101" );
        duty.setSortType( 1 );

     //   int count = dutyMapper.updateDuty( duty );
       // System.out.println( count );
    }

    /**
     * 删除值别 作者: fengzt 创建日期:2014年5月29日
     */
    ///@Test
    public void deleteDutyTest() {
        Duty duty = new Duty();
        duty.setDeptId( 1 );
        duty.setId( 1 );
        duty.setName( "test2" + new Date().toString() );
        duty.setNum( "num10101" );
        duty.setSortType( 0 );

       // dutyMapper.deleteDutyById( duty.getId() );
        System.out.println( new Date() );
        System.out.println( new Date() );
    }


    
    /**
     * 取到分页值别 作者: fengzt 创建日期:2014年5月29日
     */
   /// @Test
    public void getDutyBySearchTest() {
        Duty duty = new Duty();
        duty.setId( 1 );
        
        Page<HashMap<?, ?>> page = new Page<HashMap<?, ?>>();
        page.setPageNo( 1 );
        page.setPageSize( 10 );
        
      //  List<Duty> dutyMap = dutyService.getDutyBySearch( duty, page );
        //dutyMap.size();
        System.out.println( new Date() );
    }

}
