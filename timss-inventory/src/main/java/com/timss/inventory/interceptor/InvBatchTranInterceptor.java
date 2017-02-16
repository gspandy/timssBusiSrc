package com.timss.inventory.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.service.InvRealTimeDataService;
import com.yudean.itc.annotation.CalulateItemTran;
import com.yudean.itc.interceptor.SpringIntercepter;

/**
 * @title: 物资出库时更新流水表中字段
 * @description: 物资出库时更新流水表中字段
 * @company: gdyd
 * @className: InvBatchTranInterceptor.java
 * @author: yuanzh
 * @createDate: 2016-5-4
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Aspect
@Component
public class InvBatchTranInterceptor extends SpringIntercepter {

    private static final Logger    LOG = Logger.getLogger( InvBatchTranInterceptor.class );

    // 具体执行实时计算的接口
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;

    @Pointcut ( "execution(* com.timss.inventory.dao.InvMatTranDetailDao.insertInvMatTranDetail(..))" )
    public void updateBatchTranData () {
    }

    /**
     * @description:切面植入操作(由于是使用了AfterReturning的注入，所以在调用这个方法的时候，已经完成了业务数据的保存)
     * @author: yuanzh
     * @createDate: 2016-3-31
     * @param joinPoint : 切面切点
     * @throws Throwable:
     */
    @AfterReturning ( value = "updateBatchTranData()" )
    public void doBatchUpdateAfterReturning ( JoinPoint joinPoint ) throws Throwable {
	LOG.debug( ">>>>>>>>>>>>>> 开始执行切面更新流水物资数据" );

	// 当前执行方法名
	String currentFunctionName = joinPoint.getSignature().getName();

	// 获取符合命名要求的方法对象
	Method method = joinPoint.getSignature().getDeclaringType()
		.getDeclaredMethod( currentFunctionName , InvMatTranDetail.class );

	// 获取切面传入参数
	Object[] paramData = joinPoint.getArgs();
	locate2TranEntry( method , currentFunctionName , paramData );

	LOG.debug( ">>>>>>>>>>>>>> 完成执行切面更新流水物资数据" );
    }

    /**
     * @description:定位到流水表更新物资条目
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @param methods 切面中含有的方法
     * @param currentFunctionName 当前方法名称
     * @param paramData: 方法中自带的参数
     */
    private void locate2TranEntry ( Method method , String currentFunctionName , Object[] paramData ) {
	LOG.debug( ">>>>>>>>>>>>>>  开始执行locate2TranEntry方法 ..." );
	long startTime = System.currentTimeMillis();// 执行开始时间

	// 得到当前执行方法与切面方法命名一致的方法实现
	Annotation[] anns = method.getDeclaredAnnotations();
	getAnnotion2Update( anns , paramData );

	long endTime = System.currentTimeMillis();// 执行结束时间
	LOG.debug( ">>>>>>>>>>>>>>  完成执行locate2TranEntry方法，用时：" + String.valueOf( ( endTime - startTime ) / 1000 )
		+ " s" );
    }

    /**
     * @description:反射获取到传入参数后调用realTimeCaluAndUpdateTran方法
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @param anns 注解s
     * @param paramData: 方法中自带的参数
     */
    private void getAnnotion2Update ( Annotation[] anns , Object[] paramData ) {
	LOG.debug( ">>>>>>>>>>>>>>  开始执行getAnnotion2Update方法 ..." );
	try {
	    // 若存在注入
	    if ( null != anns && anns.length > 0 ) {
		// 遍历注入内容
		for ( Annotation anno : anns ) {
		    // 找到对应的注入
		    if ( anno instanceof CalulateItemTran ) {
			InvMatTranDetail imtd = ( InvMatTranDetail ) paramData[0];
			//invRealTimeDataService.realTimeCaluAndUpdateTran( imtd);
			break;
		    }
		}
	    }
	} catch ( Exception e ) {
	    LOG.debug( ">>>>>>>>>>>>>> 调用getAnnotion2Update方法出现异常 ..." + e );
	}

	LOG.debug( ">>>>>>>>>>>>>>  完成执行getAnnotion2Update方法 ..." );
    }

}
