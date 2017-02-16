package com.timss.inventory.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.service.InvRealTimeDataService;
import com.yudean.itc.annotation.CalulateItem;
import com.yudean.itc.annotation.CalulateItemEntry;
import com.yudean.itc.interceptor.SpringIntercepter;

/**
 * @title: 实时计算库存数量AOP切面拦截器
 * @description: 实时计算库存数量AOP切面拦截器
 * @company: gdyd
 * @className: InvRealTimeDataInterceptor.java
 * @author: yuanzh
 * @createDate: 2015-7-17
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Aspect
@Component
public class InvRealTimeDataInterceptor extends SpringIntercepter {
    private static final Logger    LOG = Logger.getLogger( InvRealTimeDataInterceptor.class );

    // 具体执行实时计算的接口
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;

    @Pointcut ( "execution(* com.timss.inventory.service.*.*.insertNewRecAndMap*(..)) ||"
	    + " execution(* com.timss.inventory.service.*.*.update*(..)) ||"
	    + " execution(* com.timss.inventory.service.*.*.save*(..)) ||"
	    + " execution(* com.timss.inventory.service.*.*.delete*(..)) ||"
	    + " execution(* com.timss.inventory.service.*.*.insert*(..))" )
    public void caluRealTimeData () {
    }

    /**
     * @description:切面植入操作(由于是使用了AfterReturning的注入，所以在调用这个方法的时候，已经完成了业务数据的保存)
     * @author: yuanzh
     * @createDate: 2016-3-31
     * @param joinPoint : 切面切点
     * @throws Throwable:
     */
    @AfterReturning ( value = "caluRealTimeData()" )
    public void doFuncAfterReturning ( JoinPoint joinPoint ) throws Throwable {
    LOG.info( ">>>>>>>>>>>>>>>>>>> 开始执行doFuncAfterReturning... " );
    long startTime = System.currentTimeMillis();// 执行开始时间
        
	LOG.info( ">>>>>>>>>>>>>> 开始执行切面计算实时物资数据" );

	// 获取符合命名要求的方法对象
	Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
	// 当前执行方法名
	String currentFunctionName = joinPoint.getSignature().getName();
	// 获取切面方法的参数
	Object[] paramDatas = joinPoint.getArgs();

	LOG.debug( ">>>>>>>>>>>>>> 获取切面方法中的参数， 符合命名规则方法数量为：" + methods.length + " | 当前执行的方法名为：" + currentFunctionName
		+ " | 当前方法参数数量为：" + paramDatas.length );

	// joinPoint传入参数
	Map< String , Object > joinPointParam = new HashMap< String , Object >();
	joinPointParam.put( "paramDatas" , paramDatas );
	joinPointParam.put( "methods" , methods );
	joinPointParam.put( "currentFunctionName" , currentFunctionName );

	// 获取切面中的参数的方法
	LOG.info( ">>>>>>>>>>>>>> 开始执行getJoinPointDatas方法 ..." );
	getJoinPointDatas( joinPointParam );
	LOG.info( ">>>>>>>>>>>>>> 完成执行getJoinPointDatas方法 ..." );
	
    long endTime = System.currentTimeMillis();// 执行结束时间
    LOG.info( ">>>>>>>>>>>>>>  完成执行doFuncAfterReturning方法，用时：" + String.valueOf( ( endTime - startTime ) / 1000 ) + " s" );
    }

    /**
     * @description:获取切面中的参数的方法
     * @author: yuanzh
     * @createDate: 2016-4-22
     * @param joinPointParam: 切面获取参数
     */
    private void getJoinPointDatas ( Map< String , Object > joinPointParam ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行getJoinPointDatas方法 ..." );
	long startTime = System.currentTimeMillis();// 执行开始时间

	// 将传入的参数还原
	Object[] paramDatas = ( Object[] ) joinPointParam.get( "paramDatas" );
	Method[] methods = ( Method[] ) joinPointParam.get( "methods" );
	String currentFunctionName = ( String ) joinPointParam.get( "currentFunctionName" );

	// 遍历方法
	for ( Method mth : methods ) {
	    // 得到当前执行方法与切面方法命名一致的方法实现
	    if ( currentFunctionName.equals( mth.getName() ) ) {
		Map< String , Object > paramMap = getAnnotationDatas( mth , paramDatas );
		// 获取了注解参数信息后开始计算物资数量
		organizeData2Calu( paramMap );
		break;
	    }
	}

	long endTime = System.currentTimeMillis();// 执行结束时间
	LOG.info( ">>>>>>>>>>>>>>  完成执行getJoinPointDatas方法，用时：" + String.valueOf( ( endTime - startTime ) / 1000 )
		+ " s" );
    }

    /**
     * @description:获取注解中的数据
     * @author: yuanzh
     * @createDate: 2016-4-22
     * @param mth: 方法对象
     * @param paramDatas: 传入参数数组
     */
    private Map< String , Object > getAnnotationDatas ( Method mth , Object[] paramDatas ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行getAnnotationDatas方法 ..." );
	// 注解参数变量
	String caluTypeVal = null;
	List< InvItemBaseField > caluItemEntry = new ArrayList< InvItemBaseField >();
	Map< String , Object > dataReturn = new HashMap< String , Object >();

	// 获取方法指定的注入对象
	Annotation[] anns = mth.getDeclaredAnnotations();
	// 若存在注入
	if ( null != anns && anns.length > 0 ) {
	    // 遍历注入内容
	    for ( Annotation anno : anns ) {
		// 找到对应的注入
		if ( anno instanceof CalulateItem ) {
		    // 获取注入中设定的内容
		    caluTypeVal = ( ( CalulateItem ) anno ).caluType();
		    // 由于参数的注解是可选的，所以这里的caluItemEntry有可能为空
		    caluItemEntry = getParamAnnotationDatas( mth , paramDatas );

		    // 将返回的数据传入变量
		    dataReturn.put( "caluTypeVal" , caluTypeVal );
		    dataReturn.put( "caluItemEntry" , caluItemEntry );

		    /*
		     * LOG.debug( ">>>>>>>>>>>>>>  参数caluTypeVal中的值为：" +
		     * caluTypeVal + " | 参数caluItemEntry中物资的条目为：" +
		     * caluItemEntry.size() + " 条" );
		     */

		    break;
		}
	    }
	}
	LOG.info( ">>>>>>>>>>>>>>  完成执行getAnnotationDatas方法 ..." );
	return dataReturn;
    }

    /**
     * @description: 获取参数中含有注解的内容的值
     * @author: yuanzh
     * @createDate: 2016-4-22
     * @param mth: 方法对象
     * @param paramDatas: 传入参数数组
     * @return:
     */
    private List< InvItemBaseField > getParamAnnotationDatas ( Method mth , Object[] paramDatas ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行getParamAnnotationDatas方法 ..." );
	LOG.info( ">>>>>>>>>>>>>>  paramDatas参数内有：" + paramDatas.length + " 条信息" );
	List< InvItemBaseField > iibfList = new ArrayList< InvItemBaseField >();
	// 获取参数的注入
	LOG.info( ">>>>>>>>>>>>>>  Method ：" + mth.getName() );
	Annotation[][] paramAnnotations = mth.getParameterAnnotations();
	LOG.info( ">>>>>>>>>>>>>>  paramAnnotations ：" + paramAnnotations.length + " 条信息" );
	if ( null != paramAnnotations && paramAnnotations.length > 0 ) {
	    // 遍历参数注入内容
	    for ( int i = 0; i < paramAnnotations.length; i++ ) {
		LOG.info( ">>>>>>>>>>>>>>  i ：" + i );
		Annotation[] annotations = paramAnnotations[i];
		LOG.info( ">>>>>>>>>>>>>>  annotations ：" + annotations.length + " 个" );
		for ( Annotation annotation : annotations ) {
		    LOG.info( ">>>>>>>>>>>>>>  annotation ：" + annotation.toString() );
		    // 若发现参数中存在CalulateItemEntry的注入
		    if ( annotation instanceof CalulateItemEntry ) {
			// 将注入的参数内容进行映射转换
			iibfList = conventObject2InvItemBaseField( paramDatas[i] );
			return iibfList;
		    }
		}
	    }
	}
	LOG.info( ">>>>>>>>>>>>>>  完成执行getParamAnnotationDatas方法 ..." );
	return null;
    }

    /**
     * @description: 将Object转换成InvItemBaseField数据
     * @author: yuanzh
     * @createDate: 2016-4-22
     * @param paramData: 传入的Object对象
     * @return:
     */
    private List< InvItemBaseField > conventObject2InvItemBaseField ( Object paramData ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行conventObject2InvItemBaseField方法 ..." );
	InvItemBaseField iibf = null;
	List< InvItemBaseField > iibfList = new ArrayList< InvItemBaseField >();

	List< Object > objList = new ArrayList< Object >();
	try {
	    // 因为有可能有一些参数不是以List的形式给出的
	    if ( paramData instanceof java.util.List ) {
		objList = ( List< Object > ) paramData;
	    } else {
		objList.add( paramData );
	    }

	    if ( null != objList && !objList.isEmpty() ) {
		for ( Object obj : objList ) {
		    iibf = ( InvItemBaseField ) obj;
		    iibfList.add( iibf );
		}
	    }
	} catch ( Exception e ) {
	    LOG.info( ">>>>>>>>>>>>>>  执行conventObject2InvItemBaseField方法时候出现错误：" + e );
	}
	LOG.info( ">>>>>>>>>>>>>>  完成执行conventObject2InvItemBaseField方法 ..." );
	return iibfList;
    }

    /**
     * @description:整理数据准备计算物资数据
     * @author: yuanzh
     * @createDate: 2016-4-22
     * @param paramMap: 传入参数集合
     */
    private void organizeData2Calu ( Map< String , Object > paramMap ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行organizeData2Calu方法 ..." );
	// 若参数不为空的时候
	if ( !paramMap.isEmpty() ) {
	    // 获取参数
	    String caluTypeVal = ( String ) paramMap.get( "caluTypeVal" );
	    List< InvItemBaseField > caluItemEntry = ( List< InvItemBaseField > ) paramMap.get( "caluItemEntry" );
	    // 若没有指定物资数据的话就全部计算
	    /*
	     * if ( null == caluItemEntry || caluItemEntry.isEmpty() ) { //
	     * 代表没有指定参数列表的时候就全部执行 caluTypeVal = "All"; caluItemEntry =
	     * invRealTimeDataService.queryInvItemBaseField4AllItem(); }
	     */
	    // 调用service中方法进行计算
	    caluAndSaveInvRealTimeData( caluTypeVal , caluItemEntry );
	}
	LOG.info( ">>>>>>>>>>>>>>  完成执行organizeData2Calu方法 ..." );
    }

    /**
     * @description:通过反射找到对应的计算和保存库存实时数量方法
     * @author: yuanzh
     * @createDate: 2016-3-31
     * @param caluTypeVal: 执行类型
     * @param caluItemEntry: 执行的批次对象
     */
    private void caluAndSaveInvRealTimeData ( String caluTypeVal , List< InvItemBaseField > caluItemEntry ) {
	LOG.info( ">>>>>>>>>>>>>>  开始执行caluAndSaveInvRealTimeData方法 ..." );
	try {
	    // 直接通过反射加载Spring容器中已经加载的类
	    invRealTimeDataService.realTimeCaluByType( caluItemEntry , caluTypeVal );
	} catch ( Exception e ) {
	    LOG.info( ">>>>>>>>>>>>>>  执行caluAndSaveInvRealTimeData方法时出现错误：" + e );
	}
	LOG.info( ">>>>>>>>>>>>>>  完成执行caluAndSaveInvRealTimeData方法 ..." );
    }

}
