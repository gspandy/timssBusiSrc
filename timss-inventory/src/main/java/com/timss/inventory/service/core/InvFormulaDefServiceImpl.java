package com.timss.inventory.service.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.bean.InvFormulaDef;
import com.timss.inventory.bean.InvItemBaseField;
import com.timss.inventory.dao.InvFormulaDefDao;
import com.timss.inventory.service.InvFormulaDefService;

/**
 * @title: 计算实时库存数据接口实现类
 * @description: 计算实时库存数据接口实现类
 * @company: gdyd
 * @className: InvFormulaDefServiceImpl.java
 * @author: yuanzh
 * @createDate: 2016-5-9
 * @updateUser: yuanzh
 * @version: 1.0
 */

@Service ( "InvFormulaDefServiceImpl" )
public class InvFormulaDefServiceImpl implements InvFormulaDefService {

    @Autowired
    private InvFormulaDefDao invFormulaDefDao;

    /**
     * @description: 通过查询公式表得到各字段对应的公式
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param type 查询字段类型
     * @param siteId 站点id
     * @return:
     */
    public String queryAndReturnFormulaByType ( String type , String siteId ) {
	return queryFormulaByEntity( type , siteId );
    }

    /**
     * @description: 通过参数查询公式表中的公式
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param type 查询字段类型
     * @param siteId 站点id
     * @return:
     */
    private String queryFormulaByEntity ( String type , String siteId ) {
	// 作为参数传入
	InvFormulaDef ifd = null;
	// 返回列表，由于查询语句写成公用的，所以返回的是一个集合
	List< InvFormulaDef > ifdList = new ArrayList< InvFormulaDef >();
	// 返回公式数据
	StringBuffer reBuffer = new StringBuffer();
	String reStr = null;
	// 常量切割，得出需要查询的字段
	String[] typeFields = type.split( "," );
	// 遍历循环
	for ( String fieldCode : typeFields ) {
	    ifd = new InvFormulaDef();
	    ifd.setFieldCode( fieldCode );
	    // ifd.setSiteid( siteId == null || siteId.equals( "" ) ? "NaN" :
	    // siteId );
	    ifd.setSiteid( "NaN" );
	    // 查询出来字段
	    ifdList = invFormulaDefDao.queryInvFormulaDefByEntity( ifd );
	    reBuffer = returnFormulaContent( ifdList , reBuffer , fieldCode );
	}
	if ( null != reBuffer ) {
	    reStr = reBuffer.substring( 0 , reBuffer.length() - 2 );
	}
	// 返回之前先做去掉最后逗号处理
	return reStr;
    }

    /**
     * @description: 将查询出来的公式拼装成数据库脚本
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param ifdList 公式集合
     * @param reBuffer 返回字符串
     * @param fieldCode 字段编码
     * @return:
     */
    private StringBuffer returnFormulaContent ( List< InvFormulaDef > ifdList , StringBuffer reBuffer , String fieldCode ) {
	if ( null != ifdList && !ifdList.isEmpty() ) {
	    // 通过唯一索引查询出来的数据仅有一条，所以直接get(0)就可以了
	    InvFormulaDef ifdRe = ifdList.get( 0 );
	    StringBuffer sBuffer = new StringBuffer( "( " );
	    sBuffer.append( ifdRe.getFormulaContent() ).append( " ) " ).append( " AS " ).append( fieldCode )
		    .append( "," ).append( "\r" );
	    reBuffer.append( sBuffer );
	}
	return reBuffer;
    }

    /**
     * @description: 将脚本中需要替换的地方用真实的物资数据替换掉
     * @author: yuanzh
     * @createDate: 2016-5-9
     * @param caluItem 物资数据
     * @param formulaSql 查询脚本
     * @return:
     */
    @Override
    public String setRealData2TheScript ( InvItemBaseField caluItem , String formulaSql ) {
	String itemid = caluItem.getItemid();
	String invcateid = caluItem.getInvcateid();
	String siteid = caluItem.getSiteid();
	String warehosueid = caluItem.getWarehouseid();
	String reSql = formulaSql.replace( "[itemid]" , "'" + itemid + "'" )
		.replace( "[invcateid]" , "'" + invcateid + "'" ).replace( "[siteid]" , "'" + siteid + "'" )
		.replace( "[warehosueid]" , "'" + warehosueid + "'" );

	if ( null != invcateid && !"".equals( invcateid ) ) {
	    reSql = reSql.replace( "--" , "" );
	}

	return reSql;
    }

}
