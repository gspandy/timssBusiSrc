package com.timss.asset.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.timss.inventory.bean.InvCategory;
import com.timss.inventory.bean.InvEquipItemMapping;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.bean.InvMatMap;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatReturnsDetail;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.bean.InvStocktakingDetail;
import com.timss.inventory.vo.InvCategoryVO;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.yudean.itc.dto.sec.Role;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.json.JsonHelper;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: CommonUtil.java
 * @author: 890166
 * @createDate: 2014-6-25
 * @updateUser: 890166
 * @version: 1.0
 */
public class CommonUtil {

    private CommonUtil () {

    }

    /**
     * 能够高效找出两个list中相同和不同的的数据
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-6-25
     * @param minList 数量较小的list
     * @param maxList 数量较大的list
     * @return:
     */
    public static Map< String , List< String >> getListDifferAndSimilar ( List< String > minList ,
	    List< String > maxList ) {
	// 返回的map
	Map< String , List< String >> reMap = new HashMap< String , List< String >>();

	// 预设一个用来比较的map
	Map< String , Integer > map = new HashMap< String , Integer >( maxList.size() + minList.size() );

	List< String > diffList = new ArrayList< String >();
	List< String > sameList = new ArrayList< String >();

	// 先将比较大的list数据放入map中
	for ( String string : maxList ) {
	    map.put( string , 1 );
	}
	// 再将较小的list放入map中
	for ( String string : minList ) {
	    Integer count = map.get( string );
	    if ( null != count ) {
		map.put( string , ++count );
		continue;
	    }
	    map.put( string , 1 );
	}
	// 找出不同
	for ( Map.Entry< String , Integer > entry : map.entrySet() ) {
	    if ( entry.getValue() == 1 ) {
		diffList.add( entry.getKey() );
	    } else {
		sameList.add( entry.getKey() );
	    }
	}
	// 通过map返回
	reMap.put( "diff" , diffList );
	reMap.put( "same" , sameList );
	return reMap;
    }

    /**
     * json数据转换成PurApplyItem
     * 
     * @description: 因为页面上需要展示多方面的数据，所以页面的数据是经过整理后放在vo实体传到页面的。
     *               这样我们在获取页面信息的时候为了方便，后台只能先将传过来的数据转换成vo，再由vo转换成我们真正需要的实体中。
     * @author: 890166
     * @createDate: 2014-6-25
     * @param listData
     * @return
     * @throws Exception :
     */
    public static List< InvEquipItemMapping > conventJsonToInvEquipItemMappingList ( String listData ) throws Exception {
	// 获取json对象并转换成list
	List< InvEquipItemMapping > iemList = JsonHelper.toList( listData , InvEquipItemMapping.class );
	return iemList;
    }

    public static InvCategory conventInvCategoryVOToBean ( InvCategoryVO icv ) throws Exception {
	InvCategory ic = new InvCategory();
	ic.setDescriptions( icv.getDescriptions() );
	ic.setInvcateid( icv.getInvcateid() );
	ic.setInvcatename( icv.getInvcatename() );
	ic.setParentid( icv.getParentid() );
	ic.setSiteId( icv.getSiteId() );
	ic.setStatus( icv.getStatus() );
	ic.setWarehouseid( icv.getWarehouseid() );
	return ic;
    }

    public static Map< String , Object > conventJsonToIMTList ( String listData ) throws Exception {
	List< InvItemVO > iiList = JsonHelper.toList( listData , InvItemVO.class );
	return conventInvItemVOToIMTList( iiList );
    }

    public static Map< String , Object > conventInvItemVOToIMTList ( List< InvItemVO > iiList ) throws Exception {
	List< InvMatTranDetail > imtdList = new ArrayList< InvMatTranDetail >();
	List< InvMatMapping > immList = new ArrayList< InvMatMapping >();

	Map< String , Object > map = new HashMap< String , Object >();

	for ( InvItemVO iiv : iiList ) {
	    InvMatTranDetail imtd = new InvMatTranDetail();
	    String uuid = getUUID();
	    imtd.setImtdid( uuid );
	    imtd.setItemid( iiv.getItemid() );
	    imtd.setBinid( iiv.getBinid() );
	    imtd.setLotno( new BigDecimal( "".equals( iiv.getLotno() ) ? "0" : iiv.getLotno() ) );
	    imtd.setRemark( iiv.getRemark() );

	    BigDecimal lastStockQty = null;

	    lastStockQty = iiv.getLaststockqty() == null ? new BigDecimal( "0" ) : iiv.getLaststockqty();

	    imtd.setInQty( lastStockQty );
	    imtd.setInUnitid( iiv.getUnit1() );
	    imtd.setPrice( iiv.getPrice() );
	    imtd.setItemcode( iiv.getItemcode() );
	    imtd.setWarehouseid( iiv.getWarehouseid() );
	    imtd.setPuraId( iiv.getPuraId() );// 采购申请ID
	    imtd.setInvcateid( iiv.getInvcateid() );// 物资类型ID
	    if(iiv.getTax()!=null && iiv.getPrice()!=null){
		    imtd.setNoTaxPrice( iiv.getPrice().divide( new BigDecimal( 1 ).add( iiv.getTax() ) , 2 ,
				    BigDecimal.ROUND_DOWN ) );// 不含税单价
			imtd.setTax( iiv.getPrice().subtract( iiv.getPrice().divide( new BigDecimal( 1 ).add( iiv.getTax() ) , 2 , 
				    BigDecimal.ROUND_DOWN ) ) );// 税额
	    }
	    imtd.setCanOutQty( lastStockQty );// 可出库数量
	    imtdList.add( imtd );

	    InvMatMapping imm = new InvMatMapping();
	    imm.setImtdid( uuid );
	    imm.setOutterid( iiv.getOutterid() );
	    imm.setItemcode( iiv.getItemcode() );
	    imm.setTranType( "receivingmaterial" );
	    immList.add( imm );
	}

	map.put( "imtd" , imtdList );
	map.put( "imm" , immList );
	return map;
    }

    public static List< InvMatApplyDetail > conventJsonToInvMatApplyDetailList ( String listData ) throws Exception {
	List< InvMatApplyDetail > imadList = new ArrayList< InvMatApplyDetail >();
	InvMatApplyDetail imad = null;
	// 获取json对象并转换成list
	List< InvMatApplyDetailVO > imadvList = conventJsonToInvMatApplyDetailVOList( listData );
	for ( InvMatApplyDetailVO imadv : imadvList ) {
	    imad = new InvMatApplyDetail();
	    imad.setCreatedate( imadv.getCreatedate() );
	    imad.setCreateuser( imadv.getCreateuser() );
	    imad.setItemid( imadv.getItemid() );
	    imad.setModifydate( imadv.getModifydate() );
	    imad.setModifyuser( imadv.getModifyuser() );
	    imad.setQtyApply( imadv.getQtyApply() );
	    imad.setSiteId( imadv.getSiteId() );
	    imad.setStockqty( imadv.getStockqty() );
	    imad.setPrice( imadv.getPrice() );
	    imad.setWarehouseid( imadv.getWarehouseid() );
	    imad.setInvcateid( imadv.getInvcateid() );
	    imad.setItemcode( imadv.getItemcode() );
	    imadList.add( imad );
	}
	return imadList;
    }
    
    public static List< InvMatApplyDetail > conventJsonToInvMatApplyDetailList ( List< InvMatApplyDetailVO > imadvList ) throws Exception {
    	List< InvMatApplyDetail > imadList = new ArrayList< InvMatApplyDetail >();
    	InvMatApplyDetail imad = null;
    	for ( InvMatApplyDetailVO imadv : imadvList ) {
    	    imad = new InvMatApplyDetail();
    	    imad.setCreatedate( imadv.getCreatedate() );
    	    imad.setCreateuser( imadv.getCreateuser() );
    	    imad.setItemid( imadv.getItemid() );
    	    imad.setModifydate( imadv.getModifydate() );
    	    imad.setModifyuser( imadv.getModifyuser() );
    	    imad.setQtyApply( imadv.getQtyApply() );
    	    imad.setSiteId( imadv.getSiteId() );
    	    imad.setStockqty( imadv.getStockqty() );
    	    imad.setPrice( imadv.getPrice() );
    	    imad.setWarehouseid( imadv.getWarehouseid() );
    	    imad.setInvcateid( imadv.getInvcateid() );
    	    imad.setItemcode( imadv.getItemcode() );
    	    imadList.add( imad );
    	}
    	return imadList;
        }

    public static List< InvMatApplyDetailVO > conventJsonToInvMatApplyDetailVOList ( String listData ) throws Exception {
	// 获取json对象并转换成list
	List< InvMatApplyDetailVO > imadvList = JsonHelper.toList( listData , InvMatApplyDetailVO.class );
	return imadvList;
    }

    public static List< InvStocktakingDetail > conventJsonToInvStocktakingDetailList ( String listData )
	    throws Exception {
	// 获取json对象并转换成list
	List< InvStocktakingDetail > istdList = JsonHelper.toList( listData , InvStocktakingDetail.class );
	return istdList;
    }

    /**
     * 某些情况需要自己生成uuid
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-25
     * @return:
     */
    public static String getUUID () {
	UUID uuid = UUID.randomUUID();
	String a = uuid.toString().toLowerCase();
	return a.replaceAll( "-" , "" );
    }

    /**
     * @description:返回随机数
     * @author: 890166
     * @createDate: 2014-8-19
     * @return:
     */
    public static String getRandom () {
	long mill = System.currentTimeMillis();
	String random = String.valueOf( mill );
	random = random.substring( random.length() - 5 );
	return random;
    }

    /**
     * @description: 物资总信息转换成领料物资
     * @author: 890166
     * @createDate: 2014-8-11
     * @param iiv
     * @return
     * @throws Exception :
     */
    public static InvMatApplyDetail conventInvItemVOToInvMatApplyDetail ( InvItemVO iiv ) throws Exception {
	InvMatApplyDetail imad = new InvMatApplyDetail();
	imad.setCreatedate( new Date() );
	imad.setItemid( iiv.getItemid() );
	imad.setPrice( iiv.getPrice() );
	imad.setStockqty( iiv.getQtyStock() );
	imad.setWarehouseid( iiv.getWarehouseid() );
	imad.setInvcateid( iiv.getCateId() );
	imad.setItemcode(iiv.getItemcode());
	return imad;
    }

    /**
     * @description:获取properties文件
     * @author: 890166
     * @createDate: 2014-8-16
     * @param key
     * @return:
     */
    public static String getProperties ( String key ) {
	Properties p = null;
	InputStream in = null;
	String reStr = null;
	try {
	    in = Thread.currentThread().getContextClassLoader().getResourceAsStream( "inventory.properties" );
	    p = new Properties();
	    p.load( in );
	    reStr = p.getProperty( key );
	    reStr = new String( reStr.getBytes( "ISO-8859-1" ) , "UTF-8" );
	} catch ( Exception e ) {
	    throw new RuntimeException( "---------CommonUtil 中的 getProperties 方法抛出异常---------：" , e );
	} finally {
	    try {
		if ( in != null ) {
		    in.close();
		}
	    } catch ( Exception e ) {
		throw new RuntimeException( "---------CommonUtil 中的 getProperties 方法抛出异常---------：" , e );
	    }
	}
	return reStr;
    }

    // 返回是否跳过环节
    public static boolean isJumpTask ( String processId , SecureUser su ) {
	boolean inteMgrFlag = false;
	String zhbMgr = CommonUtil.getProperties( "ZHBManager" );
	// 当前站点下的人员角色列表
	List< Role > roList = su.getRoles();
	if ( null != roList && !roList.isEmpty() ) {
	    for ( Role role : roList ) {
		if ( zhbMgr.indexOf( role.getId() ) > -1 && !inteMgrFlag ) {
		    inteMgrFlag = true;
		}
	    }
	}
	return inteMgrFlag;
    }

    public static List< InvMatReturnsDetailVO > conventToInvMatReturnsDetailVOList ( String listData ) {
	return JsonHelper.toList( listData , InvMatReturnsDetailVO.class );
    }

    public static List< InvMatReturnsDetail > conventJsonToInvMatReturnsDetailList (
	    List< InvMatReturnsDetailVO > imrdvList ) throws Exception {
	List< InvMatReturnsDetail > imrdList = new ArrayList< InvMatReturnsDetail >();
	InvMatReturnsDetail imrd = null;
	// 获取json对象并转换成list
	for ( InvMatReturnsDetailVO imrdv : imrdvList ) {
	    imrd = new InvMatReturnsDetail();
	    imrd.setCreatedate( new Date() );
	    imrd.setCreateuser( imrdv.getCreateuser() );
	    imrd.setCurReturnQty( imrdv.getCurReturnQty() );
	    imrd.setImrsid( imrdv.getImrsid() );
	    imrd.setImtno( imrdv.getImtno() );
	    imrd.setImtdid( imrdv.getImtdid() );
	    imrd.setModifydate( new Date() );
	    imrd.setModifyuser( imrdv.getModifyuser() );
	    imrd.setReturnQty( imrdv.getReturnQty() );
	    imrd.setSiteid( imrdv.getSiteid() );
	    imrdList.add( imrd );
	}
	return imrdList;
    }

    /**
     * @description: 旧的交易流水转新的交易流水
     * @author: 890151
     * @createDate: 2016-5-30
     * @param imtd
     * @return imtr
     * @throws Exception :
     */
    public static InvMatTranRec conventTranDetailToTranRec ( InvMatTranDetail imtd){
        InvMatTranRec imtr = new InvMatTranRec();
        imtr.setImtdid( imtd.getImtdid() );
        imtr.setImtid( imtd.getImtid() );
        imtr.setItemid( imtd.getItemid() );
        imtr.setWarehouseid( imtd.getWarehouseid() );
        imtr.setBinid( imtd.getBinid() );
        imtr.setLotno( imtd.getLotno() );
        imtr.setRemark(imtd.getRemark());
        imtr.setInQty( imtd.getInQty() );
        imtr.setInUnitid(imtd.getInUnitid());
        imtr.setOutQty(imtd.getOutQty());
        imtr.setOutUnitid(imtd.getOutUnitid());
        imtr.setCreateuser( imtd.getCreateuser() );
        imtr.setCreatedate(imtd.getCreatedate());
        imtr.setModifyuser(imtr.getModifyuser());
        imtr.setModifydate(imtd.getModifydate());
        imtr.setSiteId( imtd.getSiteId() );
        imtr.setPrice( imtd.getPrice() );
        imtr.setItemcode( imtd.getItemcode() );
        imtr.setPuraId(imtd.getPuraId());
        imtr.setTax(imtd.getTax());
        imtr.setNoTaxPrice(imtd.getNoTaxPrice());
        imtr.setInvcateid(imtd.getInvcateid());
        imtr.setCanOutQty(imtd.getCanOutQty());
        return imtr;
    }
    
    /**
     * @description: 旧的交易映射转新的交易映射
     * @author: 890151
     * @createDate: 2016-5-30
     * @param imm
     * @return imm
     * @throws Exception :
     */
    public static InvMatMap conventInvMatMappingToInvMatMap ( InvMatMapping immOld ){
    	InvMatMap immNew = new InvMatMap();
        immNew.setImtdid( immOld.getImtdid() );
        immNew.setOutterid( immOld.getOutterid() );
        immNew.setTranType( immOld.getTranType() );
        immNew.setItemcode( immOld.getItemcode() );
        return immNew;
    }
}
