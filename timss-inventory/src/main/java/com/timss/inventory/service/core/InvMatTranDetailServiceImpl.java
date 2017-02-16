package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.vo.AssetVo;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvMatMapDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.service.InvMatTranDetailService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dao.support.LogMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppLog;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-15
 * @updateUser: 890166
 * @version: 1.0
 */
@Service ( "InvMatTranDetailServiceImpl" )
public class InvMatTranDetailServiceImpl implements InvMatTranDetailService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;

    @Autowired
    private InvMatTranDao       invMatTranDao;

    @Autowired
    private InvMatMappingDao    invMatMappingDao;

    @Autowired
    private LogMapper	   lMapper;

    @Autowired
    private ItcMvcService       itcMvcService;

    @Autowired
    private InvWarehouseItemDao invWarehouseItemDao;

    @Autowired
    private InvItemDao	  invItemDao;
    
    @Autowired
    private InvMatTranRecDao       invMatTranRecDao;

    @Autowired
    private InvMatMapDao    invMatMapDao;

    /**
     * @description:查询库存情况
     * @author: 890166
     * @createDate: 2014-7-15
     * @param userInfo
     * @param itemCode
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvMatTranDetailVO > queryStockInfo ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "itemCode" ) String itemCode , String wareId, String invcateId ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvMatTranDetailVO > page = scope.getPage();

	Map< String , Object > map = new HashMap< String , Object >();

	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "itemCode" , itemCode );
	map.put( "wareId" , wareId );
	map.put( "invcateId" , invcateId );

	List< InvMatTranDetailVO > ret = invMatTranDetailDao.queryStockInfo( map );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:查询库存操作信息
     * @author: 890166
     * @createDate: 2014-7-15
     * @param userInfo
     * @param itemCode
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvMatTranDetailVO > queryStockOperInfo ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "itemCode" ) String itemCode , InvMatTranDetailVO imtdv ) throws Exception {

	UserInfoScope scope = userInfo;
	Page< InvMatTranDetailVO > page = scope.getPage();

	page.setParameter( "siteId" , userInfo.getSiteId() );
	page.setParameter( "itemCode" , itemCode );

	String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	if ( !"".equals( sort ) && !"".equals( order ) ) {
	    page.setSortKey( sort );
	    page.setSortOrder( order );
	} else {
	    page.setSortKey( "createdate" );
	    page.setSortOrder( "desc" );
	}

	if ( null != imtdv ) {
	    page.setParameter( "opertype" , imtdv.getOpertype() );
	    page.setParameter( "sheetno" , imtdv.getSheetno() );
	    page.setParameter( "createdate" , imtdv.getCreatedate() );
	    page.setParameter( "outterSheetno" , imtdv.getOutterSheetno() );
	    page.setParameter( "warehousename" , imtdv.getWarehousename() );
	    page.setParameter( "binname" , imtdv.getBinname() );
	    page.setParameter( "lotno" , imtdv.getLotno() );
	    page.setParameter( "stockQty" , imtdv.getStockQty() );
	    page.setParameter( "price" , imtdv.getPrice() );
	    page.setParameter( "totalPrice" , imtdv.getTotalPrice() );
	    page.setParameter( "remark" , imtdv.getRemark() );
	    page.setParameter( "warehouseid" , imtdv.getWarehouseid() );
	    page.setParameter( "invcateid" , imtdv.getInvcateid() );
	}
	List< InvMatTranDetailVO > ret = invMatTranDetailDao.queryStockOperInfo( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:通过Item信息查询InvMatTranDetail
     * @author: 890166
     * @createDate: 2014-7-16
     * @param itemId
     * @param itemCode
     * @param siteId
     * @return
     * @throws Exception:
     */
    public List< InvMatTranDetail > queryInvMatTranDetailByItemInfo ( String itemId , String itemCode , String siteId )
	    throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "itemId" , itemId );
	map.put( "itemCode" , itemCode );
	map.put( "siteId" , siteId );
	return invMatTranDetailDao.queryInvMatTranDetailByItemInfo( map );
    }

    /**
     * @description:表单中列表数据查询
     * @author: 890166
     * @createDate: 2014-7-23
     * @param userInfo
     * @param imtid
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvItemVO > queryMatTranDetail ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "imtid" ) String imtid , String openType ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvItemVO > page = scope.getPage();

	String pageSize = CommonUtil.getProperties( "pageSize" );
	page.setPageSize( Integer.valueOf( pageSize ) );

	page.setParameter( "siteId" , userInfo.getSiteId() );
	page.setParameter( "imtid" , imtid );
	page.setParameter( "openType" , openType );
	List< InvItemVO > ret = invMatTranDetailDao.queryMatTranDetail( page );

	page.setResults( ret );
	return page;
    }

    /**
     * @description:获取最新的批次号
     * @author: 890166
     * @createDate: 2014-8-20
     * @param outterid
     * @return
     * @throws Exception:
     */
    @Override
    public String queryMatTranMaxLotno ( String outterid ) {
	return invMatTranDetailDao.queryMatTranMaxLotno( outterid );
    }

    /**
     * @description:自动触发物资接收后会产生无效的初始化数据，获取出来将其删除
     * @author: 890166
     * @createDate: 2014-8-28
     * @param dbId
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public void deleteNoMainDetailDataBydbId ( String dbId ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	String imtid = null;
	if ( null != dbId ) {
	    map.put( "dbId" , dbId );

	    List< InvMatTranDetail > imtdList = invMatTranDetailDao.getNoMainDetailDataBydbId( map );
	    if ( null != imtdList && !imtdList.isEmpty() ) {
		for ( InvMatTranDetail imtd : imtdList ) {
		    invMatMappingDao.deleteMatMappingByImtdid( imtd.getImtdid() );
		    invMatTranDetailDao.deleteMatTranDetailById( imtd.getImtdid() );
		   
		    invMatMapDao.deleteInvMatMapByImtdid(imtd.getImtdid());
		    invMatTranRecDao.deleteMatTranRecById(imtd.getImtdid());
		    if ( null == imtid ) {
			imtid = imtd.getImtid();
		    }
		}
		invMatTranDao.deleteInvMatTranById( imtid );
	    }
	}
    }

    /**
     * @description:查询当前领料申请已经出库的记录
     * @author: 890166
     * @createDate: 2014-9-26
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvMatTranDetailVO > queryAlreadyOut ( UserInfoScope userInfo , String imaid ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvMatTranDetailVO > page = scope.getPage();

	page.setParameter( "siteId" , userInfo.getSiteId() );
	page.setParameter( "imaid" , imaid );

	List< InvMatTranDetailVO > ret = invMatTranDetailDao.queryAlreadyOut( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description: 解除绑定物资类型
     * @author: 890166
     * @createDate: 2015-1-4
     * @param iwiid
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public boolean deleteBindWarehouse ( String iwiid ) throws Exception {
	boolean flag = false;

	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "iwiid" , iwiid );
	map.put( "siteid" , userInfo.getSiteId() );
	List< InvWarehouseItemVO > iwivList = invWarehouseItemDao.queryInvWarehouseItem( map );

	int counter = invMatTranDetailDao.deleteBindWarehouse( iwiid );
	if ( counter > 0 ) {
	    if ( null != iwivList && !iwivList.isEmpty() ) {
		InvWarehouseItemVO iwiv = iwivList.get( 0 );

		/*
		 * InvMatTranDetailVO imtdv =
		 * invMatTranDetailDao.queryInvMatTranDetailUnionIMT(iwiv);
		 * if(null != imtdv){
		 * invMatTranDetailDao.deleteMatTranDetailById
		 * (imtdv.getImtdid()); List<InvMatTranDetail> imtdList =
		 * invMatTranDetailDao.queryMatTranDetailByImtid(
		 * imtdv.getImtid() ); if ( null == imtdList ||
		 * imtdList.isEmpty() ) { invMatTranDao.deleteInvMatTranById(
		 * imtdv.getImtid() ); } }
		 */

		logTheChange( userInfo , iwiv );
	    }
	    flag = true;
	}
	return flag;
    }

    /**
     * @description: 通过站点查询实时的库存金额
     * @param siteId
     * @return
     * @throws Exception
     */
    @Override
    public String queryInvPriceTotal ( String siteId ) throws Exception {
	String reStr = null;

	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , siteId );
	//TIM-1171 首页指标卡片中，统计库存金额，生物质用不含税单价进行统计，其余用含税单价
	//TIM-2087 所有站点的“库存金额”的卡片的计算方式修改，修改为从流水中根据物资可出库数量*单价求和。（用不含税）
	BigDecimal bdTotal = invMatTranDetailDao.queryStockMoneyNoTaxTotal(map);
	bdTotal = bdTotal.divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP);
	if ( null != bdTotal ) {
	    reStr = bdTotal.toString();
	}
	return reStr;
    }

    /**
     * @description: 通过站点根据新流水表查询库存总金额
     * @param siteId 
     * @param wareHouseId 仓库ID，如果不限，则传入null
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal queryStockMoneyTotal ( String siteId, String wareHouseId ) throws Exception {
		Map< String , Object > map = new HashMap< String , Object >();
		map.put( "siteId" , siteId );
		if(null != wareHouseId){
	        map.put("wareHouseId", wareHouseId);
		}
		BigDecimal stockMoneyTotal = invMatTranDetailDao.queryStockMoneyNoTaxTotal(map);
		return stockMoneyTotal;
    }
    
    /**
     * @description: 删除仓库时记录操作
     * @author: user
     * @createDate: 2015-8-25
     * @param userInfo
     * @param iwi:
     */
    private void logTheChange ( UserInfoScope userInfo , InvWarehouseItemVO iwiv ) {
	String binDelTip = CommonUtil.getProperties( "binDelTip" );

	AppLog al = new AppLog();
	al.setCategoryId( 3 );
	al.setAttr1( userInfo.getSiteId() );

	String operater = userInfo.getUserName();

	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
	String dateTime = sdf.format( new Date() );

	String itemName = iwiv.getItemid();
	List< InvItem > iiList = invItemDao.queryInvItemByItemid( iwiv.getItemid() );
	if ( null != iiList && !iiList.isEmpty() ) {
	    itemName = iiList.get( 0 ).getItemname();
	}

	binDelTip = binDelTip.replace( "{itemName}" , itemName ).replace( "{binName}" , iwiv.getBinname() )
		.replace( "{dateTime}" , dateTime ).replace( "{operater}" , operater );
	al.setDescription( binDelTip );
	al.setOperator( userInfo.getUserId() );
	al.setOperationTime( new Date() );
	lMapper.insert( al );
    }

    /**
     * @description:根据采购合同id获得物资领料id
     * @author: 890162
     * @createDate: 2015-10-08
     * @param outterid
     * @return List<String>
     */
    @Override
    public List< String > queryMatTranSheetNoByOutterId ( String outterid ) {
	List< String > matTranSheetNoList = new ArrayList< String >( 0 );
	matTranSheetNoList = invMatTranDetailDao.queryMatTranSheetNoByOutterId( outterid );
	return matTranSheetNoList;
    }

    /**
     * @description:逻辑是按照itemid、invcateid和siteid三个字段来查询可以出库的批次
     * @author: yuanzh
     * @param itemId 物资id
     * @param invcateid 物资分类id
     * @param siteId 站点id
     * @return
     */
    @Override
    public List< InvMatTranDetail > queryTranDetailByBatch ( String itemId , String invcateid , String siteId ) {
	return invMatTranDetailDao.queryTranDetailByBatch( itemId , invcateid , siteId );
    }
    
    /**
     * @description: 根据imtdid获取资产化记录次数
     * @author: yucz
     * @createDate: 2016-11-28
     * @param imtdId
     * @return:
     */
    public int queryAssetApplyByImtdId(String imtdId){
    	return invMatTranDetailDao.queryAssetApplyByImtdId(imtdId);
    }
    
    /**
     * @description: 根据imtdid获取资产化记录
     * @author: yucz
     * @createDate: 2016-12-01
     * @param imtdId
     * @return:
     */
    public List<AssetVo> queryRelateAssetByImtId(String imtId){
    	return invMatTranDetailDao.queryRelateAssetByImtId(imtId);
    }
}
