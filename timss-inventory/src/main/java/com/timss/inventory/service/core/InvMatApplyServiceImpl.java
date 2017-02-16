package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvOutterMapping;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvMatApplyDao;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvOutterMappingDao;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvMatRecipientsService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.yudean.homepage.bean.ProcessFucExtParam;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.ClassCastUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service ( "InvMatApplyServiceImpl" )
public class InvMatApplyServiceImpl implements InvMatApplyService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvMatApplyDao	   invMatApplyDao;

    @Autowired
    private InvMatApplyDetailDao     invMatApplyDetailDao;

    @Autowired
    private InvMatTranDao	    invMatTranDao;

    @Autowired
    private InvMatTranDetailDao      invMatTranDetailDao;

    @Autowired
    private InvMatMappingDao	 invMatMappingDao;

    @Autowired
    private InvItemDao	       invItemDao;

    @Autowired
    private InvOutterMappingDao      invOutterMappingDao;

    @Autowired
    private ItcMvcService	    itcMvcService;
    @Autowired
    private WorkflowService	  workflowService;
    @Autowired
    private HomepageService	  homepageService;
    @Autowired
    private InvMatRecipientsService  invMatRecipientsService;
    @Autowired
    private InvMatApplyDetailService invMatApplyDetailService;
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    /**
     * log4j输出
     */
    private static final Logger      LOG = Logger.getLogger( InvMatApplyServiceImpl.class );

    /**
     * @description:物资领料列表数据
     * @author: 890166
     * @createDate: 2014-7-27
     * @param userInfo
     * @param ima
     * @return
     * @throws Exception :
     */
    @Override
    public Page< InvMatApplyVO > queryMatApplyList ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "ima" ) InvMatApplyVO ima ) throws Exception {
	UserInfoScope scope = userInfo;
	Page< InvMatApplyVO > page = scope.getPage();
	page.setParameter( "siteId" , userInfo.getSiteId() );
	page.setParameter( "userId" , userInfo.getUserId() );

	String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	if ( !"".equals( sort ) && !"".equals( order ) ) {
		if("applyType".equals(sort)){
			page.setSortKey( "apply_type" );
		}else{
			page.setSortKey( sort );
		}
	    page.setSortOrder( order );
	} else {
	    page.setSortKey( "createdate" );
	    page.setSortOrder( "desc" );
	}

	if ( null != ima ) {
	    page.setParameter( "sheetno" , ima.getSheetno() );
	    page.setParameter( "sheetname" , ima.getSheetname() );
	    page.setParameter( "applyType" , ima.getApplyType() );
	    page.setParameter( "createuser" , ima.getCreateuser() );
	    page.setParameter( "dept" , ima.getDept() );
	    page.setParameter( "createdate" , ima.getCreatedate() );
	    page.setParameter( "price" , ima.getPrice() );
	    page.setParameter( "status" , ima.getStatus() );
	    page.setParameter( "itemCode" , ima.getItemCode() );
	    page.setParameter( "itemName" , ima.getItemName() );
	}
	List< InvMatApplyVO > ret = invMatApplyDao.queryMatApplyList( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description: 查询表单数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    @Override
    public List< InvMatApplyVO > queryMatApplyForm ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "imaid" ) String imaid , String type ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "imaid" , imaid );
	map.put( "type" , type );
	return invMatApplyDao.queryMatApplyForm( map );
    }
    
    /**
     * @description: 查询物资领料关联的采购申请
     * @author: 890162
     * @createDate: 2016-8-24
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    @Override
    public List<String> queryPurApplyOfMatApply (@Operator UserInfoScope userInfo ,@LogParam("imaid") String imaid ,String type ) throws Exception {
        Map< String , Object > map = new HashMap< String , Object >();
        map.put( "siteId" , userInfo.getSiteId() );
        map.put( "imaid" , imaid );
        map.put( "type" , type );
        return invMatApplyDao.queryPurApplyOfMatApply( map );
    }
    
    /**
     * @description: 通过sheetNo和站点id找到申请表id
     * @author: 890166
     * @createDate: 2014-9-23
     * @param sheetNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    @Override
    public String queryImaIddByFlowNo ( String sheetNo , String siteId ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "sheetNo" , sheetNo );
	map.put( "siteId" , siteId );
	return invMatApplyDao.queryImaIddByFlowNo( map );
    }

    /**
     * @description:保存信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > saveMatApply ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "ima" ) InvMatApply ima , List< InvMatApplyDetail > imaList , Map< String , Object > paramMap )
	    throws Exception {
	Map< String , Object > reMap = new HashMap< String , Object >();
	boolean flag = true;
	int count = 0;
	// 环节id
	String taskId = paramMap.get( "taskId" ) == null ? "" : String.valueOf( paramMap.get( "taskId" ) );
	ima.setTaskid( taskId );
	// 流程实例id
	String processId = paramMap.get( "processId" ) == null ? "" : String.valueOf( paramMap.get( "processId" ) );
	ima.setInstanceid( processId );
	// 状态
	String status = paramMap.get( "status" ) == null ? "" : String.valueOf( paramMap.get( "status" ) );
	ima.setStatus( status );
	// 工单编码
	String woNo = paramMap.get( "woNo" ) == null ? "" : String.valueOf( paramMap.get( "woNo" ) );
	// 关联的采购申请id
	String purApplyIds = userInfo.getParam( "purApplyIds" );
	
	String imaid = String.valueOf( paramMap.get( "imaid" ) );
	if ( "".equals( imaid ) ) {
	    ima.setImaid( null );
	    ima.setSheetno( null );
	} else {
	    ima.setImaid( imaid );
	}

	try {
	    if ( !"".equals( ima.getImaid() ) && null != ima.getImaid() ) {
		ima.setModifydate( new Date() );
		ima.setModifyuser( userInfo.getUserId() );
		ima.setTaskid( taskId );
		count = invMatApplyDao.updateInvMatApply( ima );
		invMatApplyDao.deletePurApplyMatApplyMapByMatApplyId( ima.getImaid(), userInfo.getSiteId());
		if ( !"null".equals( purApplyIds ) && StringUtils.isNotEmpty( purApplyIds ) ) {
                    invMatApplyDao.insertPurApplyMatApplyMap( purApplyIds, ima.getImaid(), userInfo.getSiteId() );
                }
	    } else {
		ima.setCreatedate( new Date() );
		ima.setCreateuser( userInfo.getUserId() );
		ima.setDeptid( userInfo.getOrgId() );
		ima.setSiteId( userInfo.getSiteId() );
		ima.setTaskid( taskId );
		ima.setInstanceid( processId );
		count = invMatApplyDao.insertInvMatApply( ima );
		if ( !"null".equals( purApplyIds ) && StringUtils.isNotEmpty( purApplyIds ) ) {
                    invMatApplyDao.insertPurApplyMatApplyMap( purApplyIds, ima.getImaid(), userInfo.getSiteId() );
                }
		// 若工单编码不为空
		if ( !"".equals( woNo ) ) {
		    InvOutterMapping iom = new InvOutterMapping();
		    iom.setInvId( ima.getImaid() );
		    iom.setInvType( "pickingmaterials" );
		    iom.setOutterId( "WO_" + CommonUtil.getRandom() );
		    iom.setOutterNo( woNo );
		    iom.setOutterType( "out" );
		    iom.setSiteid( userInfo.getSiteId() );
		    invOutterMappingDao.insertInvOutterMapping( iom );
		}
	    }

	    if ( count > 0 ) {
		double totalPrice = 0.0;
		reMap.put( "imaid" , ima.getImaid() );
		invMatApplyDetailDao.deleteInvMatApplyDetailByImaId( ima.getImaid() );
		for ( InvMatApplyDetail imad : imaList ) {
		    imad.setImaid( ima.getImaid() );
		    imad.setCreatedate( new Date() );
		    imad.setCreateuser( ima.getCreateuser() );
		    imad.setModifydate( new Date() );
		    imad.setModifyuser( userInfo.getUserId() );
		    imad.setSiteId( userInfo.getSiteId() );
		    imad.setSiteid( imad.getSiteId() );// 更新库存实时数据时用到siteid
		    totalPrice += imad.getQtyApply().doubleValue() * imad.getPrice().doubleValue();

		    invMatApplyDetailDao.insertInvMatApplyDetail( imad );
		}
		reMap.put( "totalPrice" , totalPrice );
	    }
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatApplyServiceImpl 中的 saveMatApply 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    flag = false;
	}
	reMap.put( "flag" , flag );
	return reMap;
    }

    /**
     * @description:保存交易信息
     * @author: 890166
     * @createDate: 2014-7-30
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > savePickingTran ( @Operator UserInfoScope userInfo ,
	    @LogParam ( "ima" ) InvMatApply ima , List< InvMatApplyDetailVO > imaList ,
	    HashMap< String , Object > paramMap ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	boolean flag = true;
	try {
	    InvMatTranDetail imtd = null;
	    InvMatMapping imm = null;
	    InvMatTran imt = new InvMatTran();

	    String outSheetNo = ima.getSheetno().replaceAll( "SA" , "SO" );
	    imt.setSheetno( outSheetNo );
	    imt.setTranType( "pickingmaterials" );
	    imt.setOperuser( userInfo.getUserId() );
	    imt.setCheckuser( userInfo.getUserId() );
	    imt.setCreateuser( userInfo.getUserId() );
	    imt.setCreatedate( new Date() );
	    imt.setRemark( ima.getRemark() );
	    imt.setLotno( new BigDecimal( "1" ) );
	    imt.setSiteId( userInfo.getSiteId() );

	    InvMatApply imaIn = new InvMatApply();
	    imaIn.setImaid( ima.getImaid() );
	    imaIn.setStatus( "approval_complete" );
	    invMatApplyDao.updateInvMatApply( imaIn );

	    int count = invMatTranDao.insertInvMatTran( imt );
	    if ( count > 0 ) {
		for ( InvMatApplyDetailVO imad : imaList ) {
		    imtd = new InvMatTranDetail();
		    imm = new InvMatMapping();

		    String imtdId = CommonUtil.getUUID();

		    imtd.setImtdid( imtdId );
		    imtd.setImtid( imt.getImtid() );
		    imtd.setItemid( imad.getItemid() );
		    imtd.setBinid( imad.getBinid() );
		    imtd.setWarehouseid( imad.getWarehouseid() );
		    imtd.setLotno( new BigDecimal( imad.getLotno() ) );
		    imtd.setOutQty( imad.getOutstockqty() );
		    imtd.setOutUnitid( imad.getUnitCode1() );
		    imtd.setCreatedate( new Date() );
		    imtd.setCreateuser( userInfo.getUserId() );
		    imtd.setSiteId( userInfo.getSiteId() );
		    imtd.setPrice( imad.getPrice() );
		    imtd.setItemcode( imad.getItemcode() );
		    invMatTranDetailDao.insertInvMatTranDetail( imtd );

		    imm.setImtdid( imtdId );
		    imm.setOutterid( imad.getImadid() );
		    imm.setTranType( "pickingmaterials" );
		    invMatMappingDao.insertInvMatMapping( imm );
		}
	    }
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatApplyServiceImpl 中的 savePickingTran 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    flag = false;
	}
	map.put( "flag" , flag );
	return map;
    }

    /**
     * @description:补全invmatapply数据
     * @author: 890166
     * @createDate: 2014-10-16
     * @param dataMap
     * @return:
     */
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    private void saveAndCompletionData ( Map< String , Object > dataMap ) {
	// 保存物资领料主数据
	InvMatApply ima = ( InvMatApply ) dataMap.get( "invMatApply" );
	ima.setStatus( "collect_supplies" );// 完成审批
	ima.setInstanceid( ima.getApplyType() + "_" + CommonUtil.getRandom() );
	int count = invMatApplyDao.insertInvMatApply( ima );

	// 领料信息保存成功
	if ( count > 0 ) {
	    dataMap.put( "invMatApply" , ima );

	    List< InvMatApplyDetail > imadList = ( List< InvMatApplyDetail > ) dataMap.get( "imadList" );
	    // 通过循环遍历保存物资领料数据
	    String itemCodes = null;
	    for ( InvMatApplyDetail imad : imadList ) {
	    	if(imad.getItemcode()!=null){
		    	itemCodes = imad.getItemcode() + ",";
	    	}
	    	imad.setImaid( ima.getImaid() );
	    	invMatApplyDetailDao.insertInvMatApplyDetail( imad );
	    }
	    //更新物资可用库存
	    if(itemCodes!=null){
	    	itemCodes = itemCodes.substring(0,itemCodes.length()-1);
	    	invRealTimeDataService.caluSiteInvData(ima.getSiteId(), itemCodes);
	    }

	    // 保存工单与物资领料之间的映射关系
	    InvOutterMapping iom = ( InvOutterMapping ) dataMap.get( "invOutterMapping" );
	    iom.setInvId( ima.getImaid() );
	    invOutterMappingDao.insertInvOutterMapping( iom );
	}
    }

    /**
     * @description:更新主单信息
     * @author: 890166
     * @createDate: 2014-9-18
     * @param ima
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public int updateMatApply ( InvMatApply ima ) throws Exception {
	return invMatApplyDao.updateInvMatApply( ima );
    }
    
    /**
     * @description:更新主单信息(触发实时表更新)
     * @author: zhuw
     * @createDate: 2016-07-07
     * @param ima
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public int updateInvMatApply ( List< InvMatApplyDetail > imaList, InvMatApply ima ) throws Exception {
    	return invMatApplyDao.updateInvMatApply( ima );
    }

    /**
     * @description:工单接口，操作库存数据
     * @author: 890166
     * @createDate: 2014-7-16
     * @param json
     * @return
     * @throws Exception
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public void workOrderTriggerProcesses ( HashMap< String , Object > hMap ) throws Exception {
	// 获取传递参数内数据
	List< JSONObject > itemList = ( List< JSONObject > ) hMap.get( "items" );// 获取物资信息
	String applyUser = hMap.get( "applyUser" ) == null ? "" : String.valueOf( hMap.get( "applyUser" ) );// 申请用户
	String woName = hMap.get( "woName" ) == null ? "" : String.valueOf( hMap.get( "woName" ) );// 申请用户
	String type = hMap.get( "type" ) == null ? "" : String.valueOf( hMap.get( "type" ) );// 触发类型
	int woId = hMap.get( "woId" ) == null ? 0 : Integer.valueOf( String.valueOf( hMap.get( "woId" ) ) );// 获取工单id
	String applyType = hMap.get( "applyType" ) == null ? "" : String.valueOf( hMap.get( "applyType" ) );// 获取申请类型
	String woCode = hMap.get( "woCode" ) == null ? "" : String.valueOf( hMap.get( "woCode" ) );// 获取工单编码

	// 获取申请人信息
	UserInfo user = itcMvcService.getUserInfoById( applyUser );
	String siteid = user.getSiteId(); // 获取站点id

	// 将主表信息整理出来
	Map< String , Object > paramMap = new HashMap< String , Object >();
	paramMap.put( "type" , type );
	paramMap.put( "woId" , woId );
	paramMap.put( "woCode" , woCode );
	paramMap.put( "user" , user );
	paramMap.put( "siteid" , siteid );
	paramMap.put( "applyUser" , applyUser );
	paramMap.put( "woName" , woName );
	paramMap.put( "applyType" , applyType );
	// 主单信息整理
	Map< String , Object > dataMap = conventWorkOrderToInvProcess( paramMap );
	// 子单信息整理
	List< InvMatApplyDetail > imadList = conventWorkOrderToItemList( itemList , paramMap );
	dataMap.put( "imadList" , imadList );

	// 触发领料流程
	triggerInvMatApply( dataMap , user );
    }

    /**
     * @description: 将工单传过来的数据转换成领料单物资列表数据
     * @author: 890166
     * @createDate: 2014-10-15
     * @param itemList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @SuppressWarnings ( "unchecked" )
    private List< InvMatApplyDetail > conventWorkOrderToItemList ( List< JSONObject > itemList ,
	    Map< String , Object > paramMap ) throws Exception {
	InvItemVO iiv = null;
	InvMatApplyDetail imad = null;// 领料明细实体

	List< InvMatApplyDetail > imadList = new ArrayList< InvMatApplyDetail >();

	String type = String.valueOf( paramMap.get( "type" ) );
	String siteid = String.valueOf( paramMap.get( "siteid" ) );
	String applyUser = String.valueOf( paramMap.get( "applyUser" ) );

	Map< String , Object > map = null;
	String id = null;
	String itemId = null;
	String itemnum = null;
	String warehouseId = null;
	// 遍历在工单中选择的物资信息（整理触发过来的物资信息清单）
	for ( JSONObject jObject : itemList ) {
	    map = new HashMap< String , Object >();

	    Iterator< Object > it = jObject.keys();
	    while (it.hasNext()) {
		// 获取到物资的id和申请的数量
		id = ( String ) it.next();

		String[] ids = id.split( "_" );

		itemId = ids[0];// 获取物资id

		if ( "applyCount".equals( ids[1] ) ) {
		    itemnum = jObject.getString( id );// 获取申请数量
		} else if ( "wareHouseId".equals( ids[1] ) ) {
		    warehouseId = jObject.getString( id );// 获取仓库id
		}

	    }

	    map.put( "warehouseid" , warehouseId );// 增加了仓库id来确定物资的唯一
	    map.put( "itemid" , itemId );
	    map.put( "siteid" , siteid );
	    List< InvItemVO > iivList = invItemDao.queryItemInfo( map );

	    if ( null != iivList && !iivList.isEmpty() ) {
		iiv = iivList.get( 0 );
		// 判断触发类型 in是退料 out是领料
		if ( "in".equals( type ) ) {
		    // 由于现在没有退料所以这里没有返回
		} else if ( "out".equals( type ) ) {
		    imad = CommonUtil.conventInvItemVOToInvMatApplyDetail( iiv );
		    imad.setCreateuser( applyUser );
		    imad.setQtyApply( new BigDecimal( itemnum ) );
		    imad.setSiteId( siteid );
		    imadList.add( imad );
		}
	    }

	}
	return imadList;
    }

    /**
     * @description: 将物资转换成领料物资
     * @author: 890166
     * @createDate: 2014-8-11
     * @param iiv
     * @return
     * @throws Exception:
     */
    private Map< String , Object > conventWorkOrderToInvProcess ( Map< String , Object > paramMap ) throws Exception {

	Map< String , Object > reMap = new HashMap< String , Object >();
	InvMatApply ima = new InvMatApply();
	InvOutterMapping iom = new InvOutterMapping();

	// 将传递过来的参数获取
	UserInfo user = ( UserInfo ) paramMap.get( "user" );// 用户信息

	int woId = Integer.valueOf( String.valueOf( paramMap.get( "woId" ) ) );// 获取工单id

	String woCode = String.valueOf( paramMap.get( "woCode" ) );// 获取工单编码
	String type = String.valueOf( paramMap.get( "type" ) );// 触发类型
	String deptid = user.getOrgId();// 获取用户部门id
	String woName = String.valueOf( paramMap.get( "woName" ) );// 触发类型
	String applyType = String.valueOf( paramMap.get( "applyType" ) );// 触发类型
	if ( "out".equals( type ) ) {
	    // 物资领料主单信息整理
	    ima.setCreatedate( new Date() );
	    ima.setCreateuser( user.getUserId() );
	    ima.setDeptid( deptid );

	    // 时间流水号
	    ima.setSheetname( woName );
	    ima.setSiteId( user.getSiteId() );
	    ima.setApplyType( applyType );
	    reMap.put( "invMatApply" , ima );

	    // 通过if判断类型，在这里就先将物资领料设进去
	    iom.setInvType( "pickingmaterials" );

	} else {
	    // in
	}

	// 外部映射信息整理
	iom.setOutterId( String.valueOf( woId ) );
	iom.setOutterType( type );
	iom.setOutterNo( woCode );
	iom.setSiteid( user.getSiteId() );
	iom.setApplyType( applyType );
	reMap.put( "invOutterMapping" , iom );

	return reMap;
    }

    /**
     * @description: 工单触发领料流程
     * @author: 890166
     * @createDate: 2014-8-11
     * @param dataMap
     * @param user
     * @throws Exception:
     */
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    private void triggerInvMatApply ( Map< String , Object > dataMap , UserInfo user ) throws Exception {
	Task task = null;

	Map< String , Object > paramMap = new HashMap< String , Object >(); // 参数map
	Map< String , Object > reMap = new HashMap< String , Object >();// 保存操作后返回map

	LOG.debug( "======================>将UserInfo转换为合适的格式608行" );
	UserInfoScopeImpl userInfoScope = ClassCastUtil.castAllField2Class( UserInfoScopeImpl.class , user );
	LOG.debug( "======================>将UserInfo转换为合适的格式610行结束" );

	LOG.debug( "======================>保存转换数据612行" );
	// 补全和保存InvMatApply数据
	saveAndCompletionData( dataMap );
	LOG.debug( "======================>保存转换数据615行结束" );

	LOG.debug( "======================>查询领料申请单数据617行" );
	// 获取InvMatApply保存后主单信息
	InvMatApply ima = ( InvMatApply ) dataMap.get( "invMatApply" );
	// 获取InvMatApplyDetailVO保存后子单信息
	List< InvMatApplyDetailVO > imadList = invMatApplyDetailService.queryMatApplyDetailList( userInfoScope ,
		ima.getImaid() ).getResults();
	LOG.debug( "======================>查询领料申请单数据631行结束" );

	/****************************************** 创建物资发料流程 ******************************************/
	String siteId = user.getSiteId();
	String defKey = "inventory_[@@@]_invmatrecipients".replace( "[@@@]" , siteId.toLowerCase() );
	LOG.debug( "======================>创建流程644行" );
	// 创建流程
	String processKey = workflowService.queryForLatestProcessDefKey( defKey );
	ProcessInstance p = workflowService.startLatestProcessInstanceByDefKey( processKey , user.getUserId() , null );
	String processId = p.getProcessInstanceId();
	LOG.debug( "======================>创建流程649行结束" );

	paramMap.put( "processId" , processId );
	paramMap.put( "type" , "interface" );

	List< Task > taskList = workflowService.getActiveTasks( processId );
	if ( null != taskList && !taskList.isEmpty() ) {
	    task = taskList.get( 0 );
	}
	/****************************************** 创建物资发料流程 ******************************************/

	LOG.debug( "======================>自动触发领料流程660行" );
	// 保存InvMatRecipients信息
	reMap = invMatRecipientsService.autoGenerateConsuming( userInfoScope , ima , imadList , paramMap );
	LOG.debug( "======================>自动触发领料流程666行结束" );

	LOG.debug( "======================>领料流程待办创建668行" );
	// 或保存成功将会获取到领用主信息
	InvMatRecipients imr = ( InvMatRecipients ) reMap.get( "imr" );
	if ( null != imr ) {
	    UserInfo uiCreate = itcMvcService.getUserInfoById( ima.getCreateuser() );
	    homepageService.createProcess( imr.getSheetno() , task.getProcessInstanceId() , task.getName() ,
		    task.getName() + "(" + imr.getSheetname() + ")" , task.getName() ,
		    "/inventory/invmatrecipients/invMatRecipientsForm.do?taskId=" + task.getId() + "&processInstId="
			    + task.getProcessInstanceId() + "&imrid=" + imr.getImrid() , uiCreate , null );

	    List< String > list = new ArrayList< String >();
	    list.add( ima.getCreateuser() );

	    String imtRoleId = CommonUtil.getProperties( "imtRoleOutId" );
	    LOG.info( "角色id============>" + imtRoleId );

	    String[] imtRoleIds = imtRoleId.split( "," );
	    IAuthorizationManager am = ( SecurityBeanHelper.getInstance() ).getBean( IAuthorizationManager.class );
	    for ( String roleId : imtRoleIds ) {
		String[] siteArr = roleId.split( "_" );
		if ( siteArr[0].equals( siteId ) ) {
		    List< SecureUser > usList = am.retriveUsersWithSpecificRole( roleId , null , true , true );
		    if ( usList.isEmpty() ) {
			usList = am.retriveUsersWithSpecificGroup( roleId , null , true , true );
		    }
		    if ( !usList.isEmpty() ) {
			for ( SecureUser suser : usList ) {
			    list.add( suser.getId() ); // 仓管员
			}
		    }
		}
	    }

	    workflowService.addCandidateUsers( task.getId() , list );

	    homepageService.Process( task.getId() , processId , task.getName() , list , user , new ProcessFucExtParam() );
	    LOG.debug( "======================>领料流程待办创建704行结束" );
	}
    }

    /**
     * @description:与工单接口，通过工单id查询领料申请单
     * @author: 890166
     * @createDate: 2014-9-30
     * @param woId
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvMatApplyToWorkOrder > queryMatApplyByWoId ( String woId , String applyType ) throws Exception {
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

	Page< InvMatApplyToWorkOrder > page = new Page< InvMatApplyToWorkOrder >();

	String pageSize = CommonUtil.getProperties( "pageSize" );
	page.setPageSize( Integer.valueOf( pageSize ) );

	page.setParameter( "woSheetno" , woId );
	page.setParameter( "applyType" , applyType );
	page.setParameter( "siteId" , userInfo.getSiteId() );
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "outterId" , woId );
	map.put( "siteId" , userInfo.getSiteId() );
	List< InvOutterMapping > iomList = invOutterMappingDao.queryInvOutterMappingByOutterNo( map );

	if ( null != iomList && iomList.size() == 1 ) {
	    InvOutterMapping iom = iomList.get( 0 );
	    String type = iom.getOutterType();
	    if ( "out".equals( type ) ) {
		List< InvMatApplyToWorkOrder > ret = invMatApplyDetailDao.queryMatApplyByWoId( page );
		page.setResults( ret );
	    } else {
		// 退料单没有做
	    }
	}
	return page;
    }

    /**
     * @description: 通过申请表id和站点id找到sheetNo
     * @author: 890166
     * @createDate: 2014-9-23
     * @param imaId
     * @param siteId
     * @return
     * @throws Exception:
     */
    @Override
    public String queryFlowNoByImaId ( String imaId , String siteId ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "imaId" , imaId );
	map.put( "siteId" , siteId );
	return invMatApplyDao.queryFlowNoByImaId( map );
    }

    /**
     * @description: 通过外部ID找到领料申请信息
     * @author: yuanzh
     * @createDate: 2015-12-17
     * @param woId
     * @param applyType
     * @return
     * @throws Exception:
     */
    @Override
    public List< InvMatApply > queryMatApplyByOutterId ( String woId , String applyType ) throws Exception {
	return invMatApplyDao.queryMatApplyByOutterId( woId , applyType );
    }


    /**
     * @description: 终止领料
     * @author: 890151
     * @createDate: 2016年9月12日
     * @param imaId
     * @param taskId
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
	public Map< String , Object > stopSend(String imaId, String taskId) throws Exception {
		Map< String , Object > result = new HashMap< String , Object >();
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfo.getSiteId();
		String userId = userInfo.getUserId();
		
		//查询领料单相关联的发料单
		List<InvMatRecipientsVO> recipientsList = invMatRecipientsService.queryRecipientsByImaId(userInfo, imaId);
		for (InvMatRecipientsVO invMatRecipientsVO : recipientsList) {
			String instanceid = invMatRecipientsVO.getInstanceid();
			String imrid = invMatRecipientsVO.getImrid();
			//获取每个发料单下相关联的物资
			Page<InvMatRecipientsDetailVO> imrdPage = invMatRecipientsService.queryInvMatRecipientsList(userInfo, imrid);
			List<InvMatRecipientsDetailVO> imrdList = imrdPage.getResults();
			//通过size判断发料单下是否所有物资都未发出去
			int size = imrdList.size();
			for (InvMatRecipientsDetailVO imrd : imrdList) {
				if("Y".equals(imrd.getIsled())){
					break;
				}
				else{
					size--;
				}
			}
			//size=0表示所有物资都未发出，才可以删除发料单，中止发料流程
			if(size==0){
				//删除发料单主表和子表记录
		        LOG.info( "删除未发料的发料单，imrid=" + imrid );
				invMatRecipientsService.deleteInvMatRecipientsByImrId(imrid, siteId);
				//中止发料单流程，消除涉及的待办
				if ( !"".equals( instanceid ) && null != instanceid ) {
				    List< Task > activities = workflowService.getActiveTasks( instanceid );
				    if ( null != activities && !activities.isEmpty() ) {
				    	Task task = activities.get( 0 );
				        LOG.info( "终止发料单流程，imrid=" + imrid + ", 发料单当前节点taskId=" + task.getId() );
					    workflowService.stopProcess( task.getId() , userId , userId , "终止领料" );
				    }
				}
			}
		}
		
		//更新领料单状态为终止领料状态
		InvMatApply ima = new InvMatApply();
		ima.setImaid(imaId);
		ima.setStatus("stopsend");
		updateMatApply(ima);
		
		//领料流程结束，并且该节点的审批意见设置为“终止领料”
		if(StringUtils.isNotBlank(taskId)){
	        LOG.info( "终止领料单流程，imaid=" + imaId + ", 领料单当前节点taskId=" + taskId );
		    workflowService.stopProcess( taskId , userId , userId , "终止领料" );
		}
		
		//更新可用库存
		Page<InvMatApplyDetailVO> imadResult = invMatApplyDetailService.queryMatApplyDetailList(userInfo, imaId);
		List<InvMatApplyDetailVO> imadList = imadResult.getResults();
        StringBuilder itemCodesBuilder = new StringBuilder("");
		for (InvMatApplyDetailVO invMatApplyDetailVO : imadList) {
			itemCodesBuilder.append(invMatApplyDetailVO.getItemcode()).append(",");
		}
		String itemCodes = itemCodesBuilder.toString();
		if(itemCodes.length()>0){
			itemCodes = itemCodes.substring(0, itemCodes.length()-1);
		}
    	invRealTimeDataService.caluSiteInvData(ima.getSiteId(), itemCodes);

	    result.put( "result" , "success" );
	    return result;
	}
}
