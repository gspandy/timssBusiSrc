package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.bean.AstBorrowRecordBean;
import com.timss.asset.service.AssetInfoService;
import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.bean.InvMatMap;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.bean.InvMatRecipientsDetail;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTranLog;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvMatApplyDao;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.dao.InvMatMapDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatRecipientsDao;
import com.timss.inventory.dao.InvMatRecipientsDetailDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvMatTranLogDao;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.service.InvMatTranService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurOrderService;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.vo.PurRemainVO;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-18
 * @updateUser: 890166
 * @version: 1.0
 */
@Service ( "InvMatTranServiceImpl" )
public class InvMatTranServiceImpl implements InvMatTranService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvMatTranDao       invMatTranDao;

    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;
    
    @Autowired
    private InvMatTranRecDao invMatTranRecDao;
    
    @Autowired
    private InvMatMappingDao    invMatMappingDao;

    @Autowired
    private InvMatMapDao    invMatMapDao;
    
    @Autowired
    private PurOrderService     purOrderService;

    @Autowired
    private PurApplyService     purApplyService;

    @Autowired
    private PurPubInterface     purPubInterface;

    @Autowired
    InvMatAcceptService	 invMatAcceptService;

    @Autowired
    private InvItemDao	  invItemDao;
   
    @Autowired
    private HomepageService     homepageService;
    
    @Autowired
    private ItcMvcService       itcMvcService;
    
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    
    @Autowired
    private InvMatApplyDao	   invMatApplyDao;
    
    @Autowired
    private InvMatApplyDetailDao     invMatApplyDetailDao;
    
    @Autowired
    private InvMatRecipientsDao       invMatRecipientsDao;
    
    @Autowired
    private InvMatRecipientsDetailDao invMatRecipientsDetailDao;
    
    @Autowired
    private InvMatTranLogDao     invMatTranLogDao;
    
    @Autowired
    @Qualifier("assetInfoServiceImpl")
    private AssetInfoService assetInfoService;    
    
    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvMatTranServiceImpl.class );

    /**
     * @description:查询物资接收列表
     * @author: 890166
     * @createDate: 2014-7-23
     * @param userInfo
     * @param imt
     * @return
     * @throws Exception
     */
    @Override
    public Page< InvMatTranVO > queryMatTranList ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "imt" ) InvMatTranVO imt ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvMatTranVO > page = scope.getPage();
	page.setParameter( "siteId" , userInfo.getSiteId() );

	if ( null != imt ) {
	    page.setParameter( "sheetno" , imt.getSheetno() );
	    page.setParameter( "tranTypeName" , imt.getTranTypeName() );
	    page.setParameter( "createdate" , imt.getCreatedate() );
	    page.setParameter( "operuser" , imt.getOperuser() );
	    page.setParameter( "warehouseName" , imt.getWarehouseName() );
	    page.setParameter( "totalPrice" , String.valueOf( imt.getTotalPrice() == null ? "" : imt.getTotalPrice() ) );
	    page.setParameter( "remark" , imt.getRemark() );
	}
	List< InvMatTranVO > ret = invMatTranDao.queryMatTranList( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:查询物资接收表单
     * @author: 890166
     * @createDate: 2014-7-23
     * @param userInfo
     * @param imtid
     * @return
     * @throws Exception
     */
    @Override
    public List< InvMatTranVO > queryMatTranForm ( @Operator UserInfoScope userInfo , @LogParam ( "imtid" ) String imtid )
	    throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "imtid" , imtid );
	return invMatTranDao.queryMatTranForm( map );
    }

    /**
     * @description:获取采购单号列表方法
     * @author: 890166
     * @createDate: 2014-7-23
     * @param userInfo
     * @param mtpo
     * @return
     * @throws Exception
     */
    @Override
    public Page< MTPurOrderVO > queryPurOrderList ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "mtpo" ) MTPurOrderVO mtpo ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< MTPurOrderVO > page = scope.getPage();
	page.setParameter( "siteId" , userInfo.getSiteId() );

	if ( null != mtpo ) {
	    page.setParameter( "sheetno" , mtpo.getSheetno() );
	    page.setParameter( "sheetname" , mtpo.getSheetname() );
	    page.setParameter( "companyname" , mtpo.getCompanyname() );
	    page.setParameter( "dhdate" , mtpo.getDhdate() );
	}
	List< MTPurOrderVO > ret = invMatTranDao.queryPurOrderList( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:保存方法
     * @author: 890166
     * @createDate: 2014-7-24
     * @param userInfo
     * @param imt
     * @param mapData
     * @param paramMap
     * @return
     * @throws Exception
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public boolean saveMatTran ( @Operator UserInfoScope userInfo , @LogParam ( "imtv" ) InvMatTranVO imtv ,
	    Map< String , Object > mapData , Map< String , Object > paramMap ) throws Exception {
	boolean flag = true;
	int count = 0;

	String siteId = userInfo.getSiteId();
	InvMatTran imt = new InvMatTran();

	String autoGenerate = paramMap.get( "auto" ) == null ? "" : String.valueOf( paramMap.get( "auto" ) );
	try {
	    if ( "auto".equals( autoGenerate ) ) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
		String sheetno = sdf.format( new Date() ) + CommonUtil.getRandom().substring( 0 , 3 );
		imt.setSheetno( "RS" + sheetno );
	    }
	    imt.setCheckuser( imtv.getCheckuser() );
	    imt.setCreatedate( new Date() );
	    if ( paramMap.containsKey( "inacId" ) ) {
		InvMatAccept invMatAccept = invMatAcceptService.queryInvMatAcceptBasicInfoById( paramMap.get( "inacId" )
			.toString() );
		imt.setCreateuser( invMatAccept.getWareHouseUser() );
	    } else {
		imt.setCreateuser( imtv.getCreateuser() );
	    }
	    imt.setModifydate( new Date() );
	    imt.setModifyuser( imtv.getModifyuser() );
	    imt.setSiteId( siteId );
	    imt.setLotno( new BigDecimal( "1" ) );
	    imt.setOperuser( imtv.getOperuser() );
	    imt.setProcessinstid( imtv.getProcessinstid() );
	    imt.setRemark( imtv.getRemark() );

	    if ( null != imtv.getTranType() && !"".equals( imtv.getTranType() ) ) {
		imt.setTranType( imtv.getTranType() );
	    } else {
		imt.setTranType( "receivingmaterial" );
	    }

	    if ( ( null != imtv.getSheetno() && !"".equals( imtv.getSheetno() ) )
		    && !( imtv.getSheetno().indexOf( "RS" ) > -1 ) ) {
		imt.setSheetno( imtv.getSheetno() );
	    }

	    imt.setWarehouseid( imtv.getWarehouseid() );
	    count = invMatTranDao.insertInvMatTran( imt );

	    paramMap.put( "newImtid" , imt.getImtid() );

	    imtv.setSheetno( imt.getSheetno() );
	    imtv.setImtid( imt.getImtid() );

	    // 保存主信息成功
	    if ( count > 0 ) {
	    	//流水信息和映射表信息获取
			List< InvMatTranDetail > imtdList = ( List< InvMatTranDetail > ) mapData.get( "imtd" );
			List< InvMatMapping > immList = ( List< InvMatMapping > ) mapData.get( "imm" );
			for (int i = 0; i < imtdList.size(); i++) {
				InvMatTranDetail imtd = imtdList.get(i);
				InvMatMapping imm = immList.get(i);
				
				//插入旧的流水
				imtd.setImtid( imt.getImtid() );
				imtd.setCreatedate( new Date() );
				imtd.setCreateuser( userInfo.getUserId() );
				imtd.setModifydate( new Date() );
				imtd.setModifyuser( userInfo.getUserId() );
				imtd.setSiteId( siteId );
				invMatTranDetailDao.insertInvMatTranDetail( imtd );
				
				//更新物资的批次
				InvMatTranDetail imtdTemp = new InvMatTranDetail();
				String lotno = invMatTranDetailDao.queryMatTranMaxLotno( imm.getOutterid() );
				imtdTemp.setLotno( new BigDecimal( lotno == null ? "0" : lotno ).add( new BigDecimal( "1" ) ) );
				imtdTemp.setImtdid( imm.getImtdid() );
				imtd.setLotno( new BigDecimal( lotno == null ? "0" : lotno ).add( new BigDecimal( "1" ) ) );//新流水用到
				invMatTranDetailDao.updateInvMatTranDetail( imtdTemp );
				
				//插入旧的映射关系
				invMatMappingDao.insertInvMatMapping( imm );
				
				//插入新的流水和映射关系
				invRealTimeDataService.insertNewRecAndMap(imtd, imm, "3");
			}
	    }
	    // 入库好物资接收的信息后，需要判断是否要发票录入人，通知他去报账
	    // 信息格式为“发票[发票编号]的物资已全部接收，请及时报账。”
	    List< PurInvoiceBean > purInvoiceList = purPubInterface.queryInvoice2SendNotice( imtv.getPruorderno() ,
		    siteId );// 某张物资全部接收的合同关联的未报账的发票列表
	    if ( null != purInvoiceList && !purInvoiceList.isEmpty() ) {
		HomepageWorkTask task = null;
		List< String > creatorList = new ArrayList< String >();
		String poCreateuser = null;
		for ( PurInvoiceBean tempBean : purInvoiceList ) {
		    task = new HomepageWorkTask();

		    poCreateuser = purOrderService.queryPurOrderBySheetId( tempBean.getContractId() )
			    .getCreateaccount();

		    task.setFlow( tempBean.getId() );
		    task.setProcessInstId( userInfo.getSiteId() + "_PIMONEYIN_" + task.getFlow() );
		    task.setName( "发票   [" + tempBean.getInvoiceNo() + "]  的物资已全部接收，请及时报账。" );
		    task.setStatusName( "报账提醒" );
		    task.setTypeName( "报账提醒" );
		    task.setUrl( "purchase/purInvoice/updateInvoiceToPage.do?id=" + tempBean.getId() );// 跳转到发票页面
		    creatorList.add( tempBean.getCreateuser() ); // 创建提醒信息给发票录入人
		    creatorList.add( poCreateuser ); // 创建提醒信息给发票录入人
		    homepageService.createNoticeWithOutWorkflow( task , creatorList , userInfo , "WARN" );
		}
	    }
	    // end 添加报账的消息提醒

	    // 将物资验收单相关联的物资接收单ID字段填入信息
	    if ( paramMap.containsKey( "inacId" ) ) {
		String inacId = paramMap.get( "inacId" ).toString();
		InvMatAccept invMatAccept = new InvMatAccept();
		invMatAccept.setInacId( inacId );
		invMatAccept.setImtId( imt.getImtid() );
		invMatAcceptService.updateInvMatAccept( invMatAccept );
	    }

	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatTranServiceImpl 中的 saveMatTran 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    flag = false;
	}
	return flag;
    }

    /**
     * @description:获取用户信息
     * @author: 890166
     * @createDate: 2014-8-5
     * @param userInfo
     * @param keyWord
     * @return
     * @throws Exception
     */
    @Override
    public List< Map< String , Object >> getUserHint ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "keyWord" ) String keyWord ) throws Exception {

	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "keyWord" , keyWord );
	List< Map< String , Object >> reList = null;
	List< Map< String , Object >> mList = invMatTranDao.getUserHint( map );
	if ( null != mList && !mList.isEmpty() ) {
	    reList = new ArrayList< Map< String , Object >>();
	    for ( Map< String , Object > dmap : mList ) {
		Map< String , Object > reMap = new HashMap< String , Object >();
		reMap.put( "id" , dmap.get( "ID" ) );
		reMap.put( "name" , dmap.get( "NAME" ) );
		reList.add( reMap );
	    }
	}
	return reList;
    }

    /**
     * @description:自动触发物资接收
     * @author: 890166
     * @createDate: 2014-8-8
     * @param appSheetId
     * @return
     * @throws Exception :
     */
    @Override
    public Map< String , Object > queryGenerateMatTran ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "appSheetId" ) String appSheetId , @LogParam ( "userId" ) String userId ) throws Exception {
	Map< String , Object > reMap = new HashMap< String , Object >();
	InvMatTranVO imtv = new InvMatTranVO();
	String paramIds = null;
	String[] sheetIds = appSheetId.split( "," );
	StringBuilder idBuffer = new StringBuilder();
	if ( sheetIds.length > 1 ) {
	    for ( String id : sheetIds ) {
		idBuffer.append( "'" ).append( id ).append( "'," );
	    }
	    paramIds = idBuffer.toString().substring( 0 , idBuffer.toString().length() - 1 );
	} else {
	    paramIds = "'" + appSheetId + "'";
	}

	StringBuilder sheetNoTempBuffer = new StringBuilder();
	StringBuilder remarkBuffer = new StringBuilder( "采购合同为：" );
	String sheetNoTemp = null;
	String remark = null;

	List< PurOrder > poList = purOrderService.queryPurOrderBySheetIds( paramIds );
	if ( null != poList && !poList.isEmpty() ) {

	    for ( PurOrder poTemp : poList ) {
		sheetNoTempBuffer.append( "'" ).append( poTemp.getSheetno() ).append( "'," );
		remarkBuffer.append( poTemp.getSheetname() ).append( "+" );
	    }
	    sheetNoTemp = sheetNoTempBuffer.toString().substring( 0 , sheetNoTempBuffer.toString().length() - 1 );
	    remark = remarkBuffer.toString().substring( 0 , remarkBuffer.toString().length() - 1 );

	    imtv.setLotno( new BigDecimal( "1" ) );
	    imtv.setRemark( remark );
	    imtv.setSiteId( userInfo.getSiteId() );
	    imtv.setCreateuser( userId );
	    imtv.setModifyuser( userId );
	    imtv.setOperuser( userId );
	    imtv.setCheckuser( userInfo.getUserId() );
	    imtv.setTranType( "receivingmaterial" );

	    UserInfoScope scope = userInfo;
	    Page< InvItemVO > page = scope.getPage();

	    String pageSize = CommonUtil.getProperties( "pageSize" );
	    page.setPageSize( Integer.valueOf( pageSize ) );

	    page.setParameter( "siteId" , userInfo.getSiteId() );
	    page.setParameter( "pruorderno" , sheetNoTemp );
	    List< InvItemVO > iivList = invItemDao.queryArrivalItem( page );

	    for ( InvItemVO iiv : iivList ) {
		iiv.setBestockqty( new BigDecimal( "0" ) );
		iiv.setLotno( "1" );
		iiv.setRemark( "" );
		iiv.setTotalprice( iiv.getPrice() );
	    }
	    reMap.put( "iivList" , iivList );
	    reMap.put( "invMatTranVO" , imtv );
	    reMap.put( "remark" , remark );
	}

	return reMap;
    }

    /**
     * @description:快速查询
     * @author: 890166
     * @createDate: 2014-9-1
     * @param userInfo
     * @param schfield
     * @return
     * @throws Exception :
     */
    @Override
    public Page< InvMatTranVO > quickSearch ( UserInfoScope userInfo , InvMatTranVO imt , String schfield )
	    throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvMatTranVO > page = scope.getPage();
	page.setParameter( "siteid" , userInfo.getSiteId() );

	String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	if ( !"".equals( sort ) && !"".equals( order ) ) {
	    page.setSortKey( sort );
	    page.setSortOrder( order );
	} else {
	    page.setSortKey( "createdate" );
	    page.setSortKey("sheetno");
	    page.setSortOrder( "desc" );
	}

	schfield = schfield == null ? "" : schfield;
	if ( !"".equals( schfield ) ) {
	    page.setParameter( "schfield" , schfield );
	}

	if ( null != imt ) {
	    page.setParameter( "sheetno" , imt.getSheetno() );
	    page.setParameter( "tranTypeName" , imt.getTranTypeName() );
	    page.setParameter( "createdate" , imt.getCreatedate() );
	    page.setParameter( "operuser" , imt.getOperuser() );
	    page.setParameter( "warehouseName" , imt.getWarehouseName() );
	    page.setParameter( "totalPrice" , String.valueOf( imt.getTotalPrice() == null ? "" : imt.getTotalPrice() ) );
	    page.setParameter( "remark" , imt.getRemark() );
	}

	List< InvMatTranVO > ret = invMatTranDao.quickSearch( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:物资入库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @param userInfo
     * @param imtv
     * @return
     * @throws Exception :
     */
    @Override
    public Page< InvMatTranVO > queryInMatTran ( UserInfoScope userInfo , InvMatTranVO imtv ) throws Exception {
	List< InvMatTranVO > ret = null;
	UserInfoScope scope = userInfo;
	Page< InvMatTranVO > page = scope.getPage();
	page.setParameter( "siteId" , userInfo.getSiteId() );

	String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "InvMatTranVOMap", "inventory", "InvMatTranDao" );
	if ( !"".equals( sort ) && !"createdate".equals(sort) && !"sheetno".equals(sort) && !"".equals( order ) ) {
		sort = propertyColumnMap.get( sort );
		page.setSortKey( sort );
	    page.setSortOrder( order );
	}else if("sheetno".equals(sort)){
		page.setSortKey( sort );
	    page.setSortOrder( order );
	}else if("createdate".equals(sort)){
		String sortRule = " createdatefororder "+order+" ,sheetno " ;
	    page.setSortKey( sortRule );
	    page.setSortOrder( " desc " );
	}else {
	    page.setSortKey( " createdatefororder desc,sheetno " );
	    page.setSortOrder( " desc " );
	}

	if ( null != imtv ) {
	    page.setParameter( "sheetno" , imtv.getSheetno() );
	    page.setParameter( "tranTypeName" , imtv.getTranTypeName() );
	    page.setParameter( "createdate" , imtv.getCreatedate() );
	    page.setParameter( "operuser" , imtv.getOperuser() );
	    page.setParameter( "warehouseName" , imtv.getWarehouseName() );
	    page.setParameter( "totalPrice" , imtv.getTotalPrice() );
	    page.setParameter( "remark" , imtv.getRemark() );
	}

	ret = invMatTranDao.queryInMatTran( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:物资出库列表查询
     * @author: 890166
     * @createDate: 2014-9-5
     * @param userInfo
     * @param imtv
     * @return
     * @throws Exception :
     */
    @Override
    public Page< InvMatTranVO > queryOutMatTran ( UserInfoScope userInfo , InvMatTranVO imtv ) throws Exception {
	List< InvMatTranVO > ret = null;
	UserInfoScope scope = userInfo;
	Page< InvMatTranVO > page = scope.getPage();
	page.setParameter( "siteId" , userInfo.getSiteId() );

	String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	HashMap<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "InvMatTranVOMap", "inventory", "InvMatTranDao" );
	if ( !"".equals( sort ) && !"createdate".equals(sort) && !"sheetno".equals(sort) && !"".equals( order ) ) {
		sort = propertyColumnMap.get( sort );
		page.setSortKey( sort );
	    page.setSortOrder( order );
	} else if("sheetno".equals(sort)){
		page.setSortKey( sort );
	    page.setSortOrder( order );
	} else if("createdate".equals(sort)){
		String sortRule = " createdatefororder "+order+" ,sheetno " ;
	    page.setSortKey( sortRule );
	    page.setSortOrder( " desc " );
	} else {
	    page.setSortKey( " createdatefororder desc, sheetno " );
	    page.setSortOrder( " desc " );
	}

	if ( null != imtv ) {
	    page.setParameter( "sheetno" , imtv.getSheetno() );
	    page.setParameter( "tranTypeName" , imtv.getTranTypeName() );
	    page.setParameter( "createdate" , imtv.getCreatedate() );
	    page.setParameter( "operuser" , imtv.getOperuser() );
	    page.setParameter( "warehouseName" , imtv.getWarehouseName() );
	    page.setParameter( "totalPrice" , imtv.getTotalPrice() );
	    page.setParameter( "remark" , imtv.getRemark() );
	}

	ret = invMatTranDao.queryOutMatTran( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description:查询还剩多少物资没有入库
     * @author: 890166
     * @createDate: 2014-10-16
     * @param imtdid
     * @return:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public boolean updatePurApplyStatus ( String imtid ) {
	boolean flag = true;

	String sheetId = null;
	String itemId = null;
	String status = null;

	try {
	    List< InvMatTranDetail > imtdList = invMatTranDetailDao.queryMatTranDetailByImtid( imtid );

	    for ( InvMatTranDetail imtd : imtdList ) {
		List< PurRemainVO > prvList = invMatTranDetailDao.queryRemainWarehouseNum( imtd.getImtdid() );
		if ( null != prvList ) {
		    PurRemainVO prv = prvList.get( 0 );
		    int num = prv.getRemainnum();
		    sheetId = prv.getSheetid();
		    itemId = prv.getItemid();

		    if ( num > 0 ) {
			status = "5";
		    } else {
			status = "6";
		    }
		    purApplyService.updatePurApplyItemStatus( status , sheetId , itemId ,imtd.getInvcateid());
		}
	    }
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatTranServiceImpl 中的 updatePurApplyStatus 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    flag = false;
	}
	return flag;
    }

    @Override
    public Map< String , Object > autoGenerateMatTran ( String appSheetId , Map< String , Object > msgMap )
	    throws Exception {
	Map< String , Object > paramMap = new HashMap< String , Object >(); // 参数map
	paramMap.put( "auto" , "auto" );

	Map< String , Object > result = new HashMap< String , Object >();

	List< SecureUser > us = new ArrayList< SecureUser >();
	SecureUser suser = null;
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	String siteId = userInfo.getSiteId();

	String random = CommonUtil.getRandom();
	boolean flag = true;
	LOG.info( "自动生成物资接收============>autoGenerateMatTran" );
	// 通过角色id找到其中的一个用户
	String imtRoleId = CommonUtil.getProperties( "imtRoleId" );
	LOG.info( "角色id============>" + imtRoleId );

	String[] imtRoleIds = imtRoleId.split( "," );
	IAuthorizationManager am = ( SecurityBeanHelper.getInstance() ).getBean( IAuthorizationManager.class );
	for ( String roleId : imtRoleIds ) {
	    String[] siteArr = roleId.split( "_" );
	    if ( siteArr[0].equals( siteId ) ) {
		us = am.retriveUsersWithSpecificRole( roleId , null , true , true );
		if ( us.isEmpty() ) {
		    us = am.retriveUsersWithSpecificGroup( roleId , null , true , true );
		}
		if ( !us.isEmpty() ) {
		    suser = us.get( 0 );
		}
		break;
	    }
	}

	if ( null != suser ) {
	    // 抽取采购申请中与采购单中公共信息
	    Map< String , Object > map = queryGenerateMatTran( userInfo , appSheetId , suser.getId() );
	    LOG.info( "用户id============>" + suser.getId() );

	    // 抽取主单信息
	    InvMatTranVO imtv = ( InvMatTranVO ) map.get( "invMatTranVO" );
	    imtv.setProcessinstid( random );

	    LOG.info( "物资接收id============>" + imtv.getImtid() );
	    // 抽取列表信息
	    List< InvItemVO > iivList = ( List< InvItemVO > ) map.get( "iivList" );
	    Map< String , Object > mapData = CommonUtil.conventInvItemVOToIMTList( iivList );

	    flag = saveMatTran( userInfo , imtv , mapData , paramMap );
	    if ( flag ) {
		String remark = map.get( "remark" ) == null ? "" : String.valueOf( map.get( "remark" ) );

		List< String > list = new ArrayList< String >();
		list.add( suser.getId() );
		UserInfo userinfo = itcMvcService.getUserInfoById( suser.getId() );

		String invmattranName = CommonUtil.getProperties( "imtName" );
		String invmattranType = CommonUtil.getProperties( "imtType" );

		String msg = null;
		if ( null == msgMap ) {
		    msg = remark;
		} else {
		    msg = String.valueOf( msgMap.get( "msg" ) );
		}

		homepageService.createProcess( imtv.getSheetno() , random , invmattranName ,
			invmattranName + " " + msg , invmattranType , "/inventory/invmattran/invMatTranForm.do?imtid="
				+ imtv.getImtid() + "&type=auto&openType=read" , userinfo , null );
		homepageService.Process( random , invmattranType , list , userinfo , null );
		LOG.info( "发送待办============>true" );
		result.put( "result" , "success" );
	    } else {
		result.put( "result" , "false" );
	    }
	}

	return result;
    }

    /**
     * @param PoSheetno : 根据采购合同单号找到所有接收单
     * @return
     */
    @Override
    public List< InvMatTran > queryInvMatTranByPoNo ( String poSheetno , String siteId ) throws Exception {
	List< InvMatTran > imtList = new ArrayList< InvMatTran >();

	String poSheetId = purOrderService.querySheetIdByFlowNo( poSheetno , siteId );

	List< String > strList = invMatTranDetailDao.queryMatTranSheetNoByOutterId( poSheetId );
	if ( null != strList && !strList.isEmpty() ) {
	    for ( String sheetNo : strList ) {
		List< InvMatTran > inList = invMatTranDao.queryInvMatTranBySheetNo( sheetNo );
		if ( null != inList && !inList.isEmpty() ) {
		    imtList.addAll( inList );
		}
	    }
	}
	return imtList;
    }

	/**
	 * @description:自动创建领料发料单，生成出库流水，和入库流水相对应
	 * @author: 890151
	 * @createDate: 2016-11-10
	 * @param userInfo
	 * @param purApply 采购申请，用于构造领料单
	 * @param invMatTranDetails 入库流水明细，用于构造领料详情、发料详情，出库明细
	 * @return
	 * @throws Exception
	 */
	@Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
	public void autoDelivery(UserInfoScope userInfo, PurApply purApply, InvMatTran invMatTran, List<InvMatTranDetail> invMatTranDetails) throws Exception {
		if(invMatTranDetails.size()>0){
			String siteId = userInfo.getSiteId();
			String deptId = userInfo.getOrgId();
			String createUserId = userInfo.getUserId();
			Date createDate = new Date();
			//1.1构造领料主表信息
			InvMatApply ima = new InvMatApply();
			ima.setSheetname("【系统】采购申请单“" + purApply.getSheetname() + "”即收即发");//领料单名称填“【系统】采购申请单“[对应采购申请单名称]”即收即发”
			ima.setApplyType("");//领料类型
			ima.setRemark("");//备注
			ima.setInstanceid("0");//不绑定流程实例（即不用走流程）
			ima.setStatus("auto_complete");//与正常结束的状态区别开来（approval_complete）
			ima.setApplyBudget(new BigDecimal(0));
			ima.setRemainBudget(new BigDecimal(0));
			if(purApply.getUsage()!=null){
				ima.setApplyUse( "采购申请单" + purApply.getSheetno() + "\n用途：" + purApply.getUsage() );//“采购申请单[编号]用途：[对应采购申请单的用途字段值]”
			}
			ima.setCreatedate( createDate );//创建时间填自动创建时的系统时间
			UserInfo purApplyUser = null;
			if( null!=purApply.getCreateaccount() ){
				purApplyUser = itcMvcService.getUserInfoById(purApply.getCreateaccount());
				ima.setCreateuser( purApplyUser.getUserId() );//领料人填采购申请人执行验收的节点的办理人
				ima.setSiteId( purApplyUser.getSiteId() );
				ima.setDeptid( purApplyUser.getOrgId() );
			}

			//1.2插入领料主表信息
			invMatApplyDao.insertInvMatApply( ima );
			String imaId = ima.getImaid();
			//1.3构造领料子表信息
			Map< String , Object > imadIdMap = new HashMap< String , Object >();
			for (InvMatTranDetail imtrIn : invMatTranDetails) {
				InvMatApplyDetail imad = new InvMatApplyDetail();
				imad.setImaid(imaId);
				imad.setItemid(imtrIn.getItemid());
				imad.setQtyApply(imtrIn.getInQty());
				//imad.setStockqty(new BigDecimal(0));
				if("SWF".equals(userInfo.getSiteId())){
					imad.setPrice(imtrIn.getNoTaxPrice());//领料时生物质发料用不含税单价，其余站点用含税单价
				}
				else{
					imad.setPrice(imtrIn.getPrice());
				}
				imad.setWarehouseid(imtrIn.getWarehouseid());
				imad.setInvcateid(imtrIn.getInvcateid());
				imad.setCreatedate( createDate );
				if( null != purApplyUser ){
					imad.setCreateuser( purApplyUser.getUserId() );
					imad.setSiteId( purApplyUser.getSiteId() );
					imad.setDeptid( purApplyUser.getOrgId() );
				    //imad.setSiteid( siteId );// 更新库存实时数据时用到siteid
				}
				//1.4插入领料子表信息
			    invMatApplyDetailDao.insertInvMatApplyDetail( imad );
			    //1.5保存入库流水和领料子表一一对应关系，构造出库流水时要用！！！
			    imadIdMap.put(imtrIn.getImtdid(), imad.getImadid());
			}
			
			//2.1构造发料主表信息（发料单在保存时，字段sheetname、applyType、imaid、remark都和领料单一致 TIM-1701）
		    InvMatRecipients imr = new InvMatRecipients();
			imr.setSheetname( ima.getSheetname() );
			imr.setApplyType( ima.getApplyType() );
			imr.setImaid( ima.getImaid() );
			imr.setRemark( ima.getRemark() );
			imr.setInstanceid("0");//不绑定流程实例
			imr.setDeliveryDate(new Date());//发料日期
			imr.setStatus("Y");//已发料
			imr.setCreatedate( createDate );
			imr.setCreateuser( createUserId );
			imr.setSiteId( siteId );
			imr.setDeptid( deptId );
			//2.2插入发料主表信息
			invMatRecipientsDao.insertInvMatRecipients( imr );
			String imrId = imr.getImrid();
			String imrSheetNo = imr.getSheetno();//出库流水主要要用
			//2.3构造发料子表信息
			for (InvMatTranDetail imtrIn : invMatTranDetails) {
				InvMatRecipientsDetail imrd = new InvMatRecipientsDetail();
				imrd.setImrid(imrId);
				imrd.setItemid(imtrIn.getItemid());
				imrd.setQtyApply(imtrIn.getInQty());
				imrd.setOutstockqty(imtrIn.getInQty());//出库量
				imrd.setWarehouseid(imtrIn.getWarehouseid());
				imrd.setInvcateid(imtrIn.getInvcateid());
				imrd.setBinid(imtrIn.getBinid());
				imrd.setIsled("Y");//是否已发料
				if("SWF".equals(userInfo.getSiteId())){
					imrd.setPrice(imtrIn.getNoTaxPrice());//领料时生物质发料用不含税单价，其余站点用含税单价
				}
				else{
					imrd.setPrice(imtrIn.getPrice());
				}
				if( imtrIn.getImtdid() != null ){
					imrd.setImadid( imadIdMap.get(imtrIn.getImtdid()).toString() );//1.5处保留了对应关系
				}
				imrd.setCreatedate( createDate );
				imrd.setCreateuser( createUserId );
				imrd.setSiteId( siteId );
				imrd.setDeptid( deptId );
				//2.4插入发料子表信息
				invMatRecipientsDetailDao.insertInvMatRecipientsDetail( imrd );
			}
			
			//3.1生成出库流水主表信息
			StringBuilder itemCodes = new StringBuilder();
			InvMatTran imt = new InvMatTran();
			imt.setSheetno( imrSheetNo );
			imt.setTranType( "pickingmaterials" );
			imt.setOperuser( invMatTran.getOperuser() );//出库办理人和入库办理人保持一致
			imt.setCheckuser( invMatTran.getOperuser() );
			imt.setCreateuser( createUserId );
			imt.setCreatedate( createDate );
			imt.setLotno( new BigDecimal( "1" ) );
			imt.setSiteId( siteId );
			invMatTranDao.insertInvMatTran( imt );
			String imtId = imt.getImtid();
			//3.2生成出库流水子表信息
			for (InvMatTranDetail imtrIn : invMatTranDetails) {
				itemCodes = itemCodes.append(imtrIn.getItemcode()).append(",");
				//出库流水子表信息
				InvMatTranRec imtrOut = new InvMatTranRec();
				String imtrOutId = CommonUtil.getUUID();
				imtrOut.setImtdid( imtrOutId );
				imtrOut.setImtid( imtId );//关联的流水主表ID
				imtrOut.setItemid( imtrIn.getItemid() );
				imtrOut.setWarehouseid( imtrIn.getWarehouseid() );
				imtrOut.setInvcateid( imtrIn.getInvcateid() );
				imtrOut.setBinid(imtrIn.getBinid());
				imtrOut.setLotno( imtrIn.getLotno() );
				imtrOut.setRemark( "" );
				imtrOut.setInQty( new BigDecimal( 0 ) );
				imtrOut.setInUnitid("");
				imtrOut.setOutQty( imtrIn.getInQty() );//出入相等！！
				imtrOut.setOutUnitid( imtrIn.getInUnitid() );//出入单位相同
				imtrOut.setCanOutQty(new BigDecimal(0));//可出库数量为0 ！！
				imtrOut.setPrice( imtrIn.getPrice() );
				imtrOut.setNoTaxPrice( imtrIn.getNoTaxPrice() );
				imtrOut.setTax( imtrIn.getTax() );
				//isrmbrs字段默认为N 已报账未报账
				imtrOut.setItemcode( imtrIn.getItemcode() );
				imtrOut.setPriceFlag( "2" ); //此处因为要和入库流水一一对应，所以价格先定好了，但是还是和其他发料一样保持价格标识为2（实时价，参考saveRecipientsTran方法）。
				imtrOut.setCreatedate( createDate );
				imtrOut.setCreateuser( createUserId );
				imtrOut.setSiteId( siteId );
				imtrOut.setDeptid( deptId );
				invMatTranRecDao.insertInvMatTranRec( imtrOut );

				//生成出库流水映射关系
				InvMatMap imm = new InvMatMap();
				if( imtrIn.getImtdid() != null ){
					imm.setOutterid(imadIdMap.get( imtrIn.getImtdid()).toString() );//1.5处保留了对应关系
				}
				imm.setItemcode(imtrIn.getItemcode());
				imm.setTranType( "pickingmaterials" );
				imm.setImtdid( imtrOutId );
				invMatMapDao.insertInvMatMap( imm );
			    
				//出入相抵消
	            InvMatTranRec imtrNew = new InvMatTranRec();
	            imtrNew.setImtdid(imtrIn.getImtdid());
	            imtrNew.setCanOutQty(new BigDecimal(0));
	            invMatTranRecDao.updateInvMatTranRec( imtrNew );
	            
	            // 记录出库来源批次对应关系
	            InvMatTranLog imtl = new InvMatTranLog();
	            String imtlId = UUIDGenerator.getUUID();
	            imtl.setImtlId( imtlId );// 主键id
	            imtl.setImtdId( imtrOut.getImtdid() );// 出库流水ID
	            imtl.setFromImtdId( imtrIn.getImtdid() );// 来自的入库记录流水ID
	            imtl.setTranCategory("OUT");
	            imtl.setTranQty(imtrIn.getInQty());
	            imtl.setCreatedate( createDate );
	            imtl.setCreateuser( createUserId );
	            imtl.setSiteid( siteId );
	            imtl.setDeptid( deptId );
	            invMatTranLogDao.insertInvMatTranLog(imtl);

	    	    //throw new RuntimeException( "物资入库失败" );
				//插入新的流水和映射关系（旧的流水price由业务模块传，新的流水由算法算出，此处imtd可以不设置price）
				//Map<String, Object> outInfo = invRealTimeDataService.insertNewRecAndMap(imtd, imm, "2");
			}			
			if(itemCodes.length()>0){
	        	invRealTimeDataService.caluSiteInvData(siteId, itemCodes.substring(0, itemCodes.length()-1));
			}
		}
	}

	/**
	 * @description:归还资产
	 * @author: 890151
	 * @createDate: 2016-12-9
	 * @param userInfo 用户信息
	 * @param abrb 归还记录
	 * @return
	 * @throws Exception
	 */
	//@Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
	public void returnAsset(UserInfoScope userInfoScope, AstBorrowRecordBean abrb) throws Exception {
		//查询资产卡片详情
		AssetBean assetDetailInfo = assetInfoService.queryAssetDetail(abrb.getAssetId());
		String imtdId = assetDetailInfo.getImtdId();
		//查询入库流水
        List<InvMatTranRec> imtrList = invMatTranRecDao.queryTranRecByimtdid( imtdId );
        String siteId = userInfoScope.getSiteId();
        String deptId = userInfoScope.getOrgId();
        String userId = userInfoScope.getUserId();
        
        if(imtrList!=null && imtrList.size()>0){
        	//获取物资接收入库流水
            InvMatTranRec oldRec = imtrList.get( 0 );
			//生成归还入库流水主表信息
			InvMatTran imt = new InvMatTran();
			imt.setSheetno( abrb.getAssetId() );
			imt.setTranType( "returnasset" );
			imt.setOperuser( userId );
			imt.setCreateuser( userId );
			imt.setCreatedate( new Date() );
			imt.setLotno( new BigDecimal( "1" ) );
			imt.setSiteId( siteId );
			imt.setRemark( "" );//资产卡片号：XXXX 界面每次都去动态获取
			invMatTranDao.insertInvMatTran( imt );
			String imtId = imt.getImtid();
			
			//生成归还入库流水子表信息
			InvMatTranRec imtr = new InvMatTranRec();
			String imtrInId = CommonUtil.getUUID();
			imtr.setImtdid( imtrInId );//主键ID
			imtr.setImtid( imtId );//关联的流水主表ID
			imtr.setItemid( oldRec.getItemid() );
			imtr.setWarehouseid( oldRec.getWarehouseid() );
			imtr.setInvcateid( oldRec.getInvcateid() );
			imtr.setBinid( oldRec.getBinid());
			imtr.setLotno( oldRec.getLotno() );
			imtr.setRemark( "" );
			imtr.setInQty( new BigDecimal( 1 ) );//入库数量1  !!
			imtr.setInUnitid( oldRec.getInUnitid() );
			imtr.setOutQty( new BigDecimal( 0 ) );//出库数量0  !!
			imtr.setOutUnitid( "" );
			imtr.setCanOutQty( new BigDecimal(0) );//可出库数量为0  !!
			imtr.setPrice( new BigDecimal( 0 ) );//三个价格都取0
			imtr.setNoTaxPrice( new BigDecimal( 0 ) );
			imtr.setTax( new BigDecimal( 0 ) );
			imtr.setItemcode( oldRec.getItemcode() );//物资编码
			imtr.setPriceFlag( "3" ); //flag=1批次价 flag=2实时价 flag=3自定义价格
			imtr.setCreatedate( new Date() );
			imtr.setCreateuser( userId );
			imtr.setSiteId( userInfoScope.getSiteId() );
			imtr.setDeptid( deptId );
			invMatTranRecDao.insertInvMatTranRec( imtr );

			//生成出库流水映射关系
			InvMatMap imm = new InvMatMap();
			imm.setOutterid( String.valueOf(abrb.getBorrowRecordId()) );//借用记录ID
			imm.setItemcode( oldRec.getItemcode() );
			imm.setTranType( "returnasset" );
			imm.setImtdid( imtrInId );
			invMatMapDao.insertInvMatMap( imm );
			
			//更新库存数量
			if( oldRec.getItemcode()!=null ){
	        	invRealTimeDataService.caluSiteInvData(userInfoScope.getSiteId(), oldRec.getItemcode());
			}
        }
	}
}
