package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.dao.InvMatAcceptDao;
import com.timss.inventory.service.InvMatTranService;
import com.timss.inventory.utils.AcceptStatusEnum;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.timss.inventory.vo.InvMatAcceptDtlVo;
import com.timss.inventory.vo.InvMatAcceptVo;
import com.timss.inventory.vo.InvMatTranVO;
import com.timss.inventory.vo.MTPurOrderVO;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurOrderService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.Configuration;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptServiceImpl.java
 * @author: 890145
 * @createDate: 2015-11-2
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class InvMatAcceptServiceImpl implements InvMatAcceptService {
    @Autowired
    InvMatAcceptDao	     invMatAcceptDao;

    @Autowired
    InvMatAcceptDetailService   invMatAcceptDetailService;

    @Autowired
    ItcMvcService	       itcMvcService;

    @Autowired
    WorkflowService	     workflowService;

    @Autowired
    HomepageService	     homepageService;

    @Autowired
    InvMatTranService	   invMatTranService;
    
    @Autowired
    PurOrderService purOrderService;

    @Autowired
    PurApplyService purApplyService;
    
    private static final Logger LOGGER = Logger.getLogger( InvMatAcceptServiceImpl.class );

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#insertInvMatAccept
     * (com.timss.inventory.bean.InvMatAccept, java.util.List, boolean)
     */
    @Override
    @Transactional
    public Map< String , Object > insertInvMatAccept ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails , boolean startWorkflow ) {

	return insertOrUpdateInvMatAccept( invMatAccept , invMatAcceptDetails , startWorkflow );
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#updateInvMatAccept
     * (com.timss.inventory.bean.InvMatAccept, java.util.List, boolean)
     */
    @Override
    @Transactional
    public Map< String , Object > updateInvMatAccept ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails , boolean startWorkflow ) {
	return insertOrUpdateInvMatAccept( invMatAccept , invMatAcceptDetails , startWorkflow );
    }

    /**
     * 插入或者更新验收申请信息，如果需要进行物资申请人有多个需要进行流程拆分
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @param invMatAcceptDetails
     * @param startWorkflow
     * @return:
     */
    @Transactional
    private Map< String , Object > insertOrUpdateInvMatAccept ( InvMatAccept invMatAccept ,
	    List< InvMatAcceptDetail > invMatAcceptDetails , boolean startWorkflow ) {
	Map< String , Object > results = new HashMap< String , Object >();
	String inacId = invMatAccept.getInacId();
	UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
	if ( StringUtils.isBlank( inacId ) ) {
	    // 处理插入情况
	    invMatAccept.setInacId( null );
	    invMatAccept.setInacNo( null );
	    insertInvMatAccept( invMatAccept );
	    invMatAcceptDetailService.insertInvMatAcceptDetail( invMatAccept , invMatAcceptDetails );
	} else {
	    // 处理更新情况
	    updateInvMatAccept( invMatAccept );
	    invMatAcceptDetailService.updateInvMatAcceptDetail( invMatAccept , invMatAcceptDetails );
	}
	String processId = invMatAccept.getInstanceid();
	InvMatAccept inAccept = invMatAcceptDao.queryByPrimaryKey( invMatAccept.getInacId() );
	processId = inAccept.getInstanceid();
	// 处理流程启动情况
	if ( startWorkflow ) {
	    if ( StringUtils.isBlank( processId ) ) {
		// 当需要启动流程，同时流程还未启动时，启动流程
		processId = startWorkflow( invMatAccept );
		invMatAccept.setInstanceid( processId );
		updateInvMatProcessId( invMatAccept.getInacId() , processId );
	    }
	}
	if ( StringUtils.isNotBlank( processId ) ) {
	    workflowService.setVariable( processId , "SP_MATERIAL" , invMatAccept.getSpecialMaterials() );
	}

	String url = "inventory/invmataccept/invMatAcceptFormJsp.do?inacId=" + invMatAccept.getInacId();
	homepageService.createProcess( invMatAccept.getInacNo() , processId , "物资验收" ,
		"采购单号[ " + invMatAccept.getPoSheetno() + " ] -- " + invMatAccept.getPoName() , "草稿" , url , userInfo ,
		null );

	String taskId = null;
	if ( StringUtils.isNotBlank( processId ) ) {
	    // 获取当前的流程对应的正在执行的节点id
	    taskId = getTaskId( processId );

	    // 20151201 JIRA ==>TIM221
	    String curProcess = workflowService.getTaskByTaskId( taskId ).getTaskDefinitionKey();
	    results.put( "curProcess" , curProcess );
	}

	results.put( "taskId" , taskId );
	results.put( "instanceId" , processId );
	results.put( "inacId" , invMatAccept.getInacId() );
	results.put( "inacNo" , invMatAccept.getInacNo() );
	return results;
    }

    /**
     * 根据流程id，查询当前任务节点id
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param processId
     * @return:
     */
    private String getTaskId ( String processId ) {
	String taskId = null;
	List< Task > taskList = workflowService.getActiveTasks( processId );
	if ( null != taskList && !taskList.isEmpty() ) {
	    Task task = taskList.get( 0 );
	    taskId = task.getId();
	}

	return taskId;
    }

    private boolean insertInvMatAccept ( InvMatAccept invMatAccept ) {
	UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
	invMatAccept.setCreatedate( new Date() );
	invMatAccept.setCreateuser( userInfo.getUserId() );
	invMatAccept.setSiteid( userInfo.getSiteId() );
	invMatAccept.setDeptid( userInfo.getOrgId() );
	invMatAcceptDao.insert( invMatAccept );
	return true;
    }

    /**
     * 更新物资接收申请单的流程id和验收单状态
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param inacId
     * @param processId:
     */
    private boolean updateInvMatProcessId ( String inacId , String processId ) {
	InvMatAccept invMatAccept = new InvMatAccept();
	invMatAccept.setInacId( inacId );
	invMatAccept.setInstanceid( processId );

	return updateInvMatAccept( invMatAccept );
    }

    /**
     * 启动工作流并返回流程实例id
     * 
     * @description:
     * @author: 890145
     * @createDate: 2015-11-2
     * @param invMatAccept
     * @return:
     */
    private String startWorkflow ( InvMatAccept invMatAccept ) {
	UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();

	String defKey = "inventory_" + userInfo.getSiteId().toLowerCase() + "_accept";

	// 创建流程
	String processKey = workflowService.queryForLatestProcessDefKey( defKey );

	Map< String , Object > paramMap = new HashMap< String , Object >();
	paramMap.put( "inacId" , invMatAccept.getInacId() );

	paramMap.put( "processKey" , processKey );
	paramMap.put( "SP_MATERIAL" , invMatAccept.getSpecialMaterials() );
	ProcessInstance p;

	LOGGER.info( "startWorkflow>>>>>>>>>>>>> processKey:" + processKey + " | userId:" + userInfo.getUserId()
		+ " | SP_MATERIAL:" + invMatAccept.getSpecialMaterials() + " | inacId:" + invMatAccept.getInacId()
		+ " | defKey" + defKey );
	try {
	    p = workflowService.startLatestProcessInstanceByDefKey( processKey , userInfo.getUserId() , paramMap );
	} catch ( Exception e ) {
	    throw new RuntimeException( "启动工作流" + processKey + "失败" , e );

	}
	String processId = p.getProcessInstanceId();
	return processId;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#voidFlow(java.lang
     * .String)
     */
    @Override
    @Transactional
    public boolean voidFlow ( String inacId , String message ) {
	InvMatAccept invMatAccept = invMatAcceptDao.queryByPrimaryKey( inacId );
	String processId = invMatAccept.getInstanceid();
	if ( StringUtils.isNotBlank( processId ) ) {
	    String taskId = getTaskId( processId );
	    if ( StringUtils.isNotBlank( taskId ) ) {
		UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
		// 终止流程
		workflowService.stopProcess( taskId , userInfo.getUserId() , userInfo.getUserId() , message );
		// 首页信息变为办毕
		homepageService.complete( processId , userInfo , "已作废" );
	    }
	}
	invMatAccept = new InvMatAccept();
	invMatAccept.setInacId( inacId );
	invMatAccept.setStatus( AcceptStatusEnum.VOIDED.toString() );
	updateInvMatAccept( invMatAccept );
	return true;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#updateInvMatAccept
     * (com.timss.inventory.bean.InvMatAccept)
     */
    @Override
    @Transactional
    public boolean updateInvMatAccept ( InvMatAccept invMatAccept ) {
	UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
	invMatAccept.setModifydate( new Date() );
	invMatAccept.setModifyuser( userInfo.getUserId() );
	invMatAcceptDao.updateByPrimaryKeySelective( invMatAccept );
	return true;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#deleteInvMatAccept
     * (java.lang.String)
     */
    @Override
    @Transactional
    public boolean deleteInvMatAccept ( String inacId ) {
	InvMatAccept invMatAccept = invMatAcceptDao.queryByPrimaryKey( inacId );
	invMatAcceptDao.deleteByPrimaryKey( inacId );

	// 删除草稿
	String processInstId = invMatAccept.getInstanceid();
	if ( StringUtils.isNotBlank( processInstId ) ) {
	    workflowService.delete( processInstId , "" );
	}
	return true;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#queryInvMatAcceptById
     * (java.lang.String)
     */
    @Override
    @Transactional
    public InvMatAcceptDtlVo queryInvMatAcceptById ( String inacId ) {
	InvMatAccept invMatAccept = invMatAcceptDao.queryByPrimaryKey( inacId );
	InvMatAcceptDtlVo invMatAcceptDtlVo = new InvMatAcceptDtlVo();
	try {
	    Date deliverDay = invMatAccept.getDeliveryDate();
	    invMatAccept.setDeliveryDate( new Date() );
	    BeanUtils.copyProperties( invMatAcceptDtlVo , invMatAccept );
	    invMatAcceptDtlVo.setDeliveryDate( deliverDay );
	} catch ( Exception e ) {
	    throw new RuntimeException( "bean复制失败，目标对象为" + invMatAcceptDtlVo + ",原始对象为" + invMatAccept , e );
	}
	// 查询物资详细列表
	List< InvMatAcceptDetailVO > invMatAcceptDetails = invMatAcceptDetailService
		.queryInvMatAcceptDetailListByInacId( inacId );
	invMatAcceptDtlVo.setInvMatAcceptDetails( invMatAcceptDetails );
	return invMatAcceptDtlVo;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#queryInvMatAcceptById
     * (java.lang.String)
     */
    @Override
    @Transactional
    public InvMatAccept queryInvMatAcceptBasicInfoById ( String inacId ) {
	InvMatAccept invMatAccept = invMatAcceptDao.queryByPrimaryKey( inacId );
	return invMatAccept;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#queryInvMatAcceptList
     * (com.timss.inventory.bean.InvMatAccept)
     */
    @Override
    @Transactional
    public Page< InvMatAcceptVo > queryInvMatAcceptList ( InvMatAcceptVo imVo , UserInfo userInfo ) {

	imVo.setSiteid( userInfo.getSiteId() );
	UserInfoScope scope = ( UserInfoScope ) userInfo;
	Page< InvMatAcceptVo > page = scope.getPage();
	page.setParameter( "siteid" , userInfo.getSiteId() );

	try {
	    String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
	    String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
	    if ( "deliveryDataString".equals( sort ) ) {
		sort = "createdate";
	    }
	    sort = sort.replaceAll( "([A-Z])" , "_$1" );

	    if ( !"".equals( sort ) && !"".equals( order ) ) {
		page.setSortKey( sort );
		page.setSortOrder( order );
	    } else {
		page.setSortKey( "createdate" );
		page.setSortOrder( "desc" );
	    }
	} catch ( Exception e ) {
	    throw new RuntimeException( "无法获取物资接收分页器参数" , e );
	}

	if ( null != imVo ) {
	    if ( !"null".equals( imVo.get_acptCnlus() ) ) {
		page.setParameter( "acptCnlus" , imVo.get_acptCnlus() );
	    }

	    page.setParameter( "applyUser" , imVo.getApplyUser() );
	    page.setParameter( "poSheetno" , imVo.getPoSheetno() );
	    if ( !"null".equals( imVo.get_status() ) ) {
		page.setParameter( "status" , imVo.get_status() );
	    }

	    page.setParameter( "poName" , imVo.getPoName() );
	    page.setParameter( "inacNo" , imVo.getInacNo() );
	    page.setParameter( "createdate" , imVo.getDeliveryDataString() );

	    page.setParameter( "itemCode" , imVo.getItemCode() );
	    page.setParameter( "itemName" , imVo.getItemName() );

	}
	List< InvMatAcceptVo > ret = invMatAcceptDao.queryInvMatAcceptList( page );
	page.setResults( ret );
	return page;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#queryWorkflowMap
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Map< String , Object > queryWorkflowMap ( String inacId , String processId ) {
	UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
	Map< String , Object > maps = new HashMap< String , Object >();
	String taskId = null;
	Task task = null;
	List< Task > taskList = workflowService.getActiveTasks( processId );
	if ( null != taskList && !taskList.isEmpty() ) {
	    task = taskList.get( 0 );
	    taskId = task.getId();
	}
	if ( StringUtils.isNotBlank( taskId ) ) {
	    List< String > users = workflowService.getCandidateUsers( taskId );
	    if ( users != null && !users.isEmpty() ) {
		for ( int i = 0; i < users.size(); i++ ) {
		    if ( users.get( i ).equals( userInfo.getUserId() ) ) {
			maps.put( "isApproval" , true );
			break;
		    }
		}
	    }
	    String processKey = ( String ) workflowService.getVariable( processId , "processKey" );
	    Map< String , String > eMap = workflowService.getElementInfo( processKey , task.getTaskDefinitionKey() );
	    maps.put( "element" , eMap );
	}

	maps.put( "taskId" , taskId );
	maps.put( "processId" , processId );
	return maps;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#saveMatTran(com.
     * timss.inventory.bean.InvMatAccept, java.util.List)
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public boolean saveMatTran ( InvMatAccept invMatAccept , List< InvMatAcceptDetailVO > details , String checkUser ) throws Exception {
	UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	InvMatTranVO imtv = new InvMatTranVO();
	imtv.setSiteId( userInfo.getSiteId() );
	imtv.setCheckuser( userInfo.getUserId() );
	imtv.setCreateuser( userInfo.getUserId() );
	imtv.setModifyuser( userInfo.getUserId() );
	imtv.setProcessinstid( invMatAccept.getInstanceid() );
	imtv.setWarehouseid( details.get( 0 ).getWarehouseid() );
	imtv.setRemark( "物资验收单编号为: " + invMatAccept.getInacNo() + ",采购合同名称为: " + invMatAccept.getPoName() );
	imtv.setCheckuser( checkUser );// 取“采购申请人验收”节点的审批人为验收人 TIM-773
	// 获取仓管员信息
	String processId = invMatAccept.getInstanceid();
	List< HistoricTask > historicTasks = workflowService.getFakePreviousTasks( processId );
	if ( historicTasks != null && !historicTasks.isEmpty() ) {
	    for ( int i = 0; i < historicTasks.size(); i++ ) {
		if ( "cgyqrsl".equals( historicTasks.get( i ).getTaskDefinitionKey() ) ) {
		    imtv.setOperuser( historicTasks.get( i ).getAssignee() );
		    break;
		}
	    }
	}
	List< InvMatTranDetail > tranDetails = new ArrayList< InvMatTranDetail >();
	List< InvMatMapping > immList = new ArrayList< InvMatMapping >();
	for ( int i = 0; i < details.size(); i++ ) {
	    String imidId = CommonUtil.getUUID();
	    InvMatTranDetail tranDetail = new InvMatTranDetail();
	    tranDetail.setItemid( details.get( i ).getItemid() );
	    tranDetail.setBinid( details.get( i ).getBinid() );
	    tranDetail.setWarehouseid( details.get( i ).getWarehouseid() );
	    tranDetail.setInQty( details.get( i ).getAcceptnum() );
	    tranDetail.setInUnitid( details.get( i ).getUnit() );
	    tranDetail.setItemcode( details.get( i ).getItemcode() );
	    tranDetail.setLotno( new BigDecimal( 1 ) );
	    tranDetail.setPrice( details.get( i ).getPrice() );
	    tranDetail.setOutQty( new BigDecimal( 0 ) );
	    tranDetail.setImtdid( imidId );
	    tranDetail.setPuraId( details.get( i ).getPuraId() );
	    tranDetail.setCanOutQty( details.get( i ).getAcceptnum() );
	    tranDetail.setNoTaxPrice( details.get( i ).getPrice()
		    .divide( new BigDecimal( 1 ).add( details.get( i ).getTaxRate() ) , 2 , BigDecimal.ROUND_DOWN ) );
	    tranDetail.setTax( tranDetail.getPrice().subtract(tranDetail.getNoTaxPrice()) );
	    tranDetail.setInvcateid( details.get( i ).getInvcateid() );// 设置物资分类ID
	    tranDetails.add( tranDetail );

	    InvMatMapping invMatMapping = new InvMatMapping();
	    invMatMapping.setOutterid( invMatAccept.getPoId() );
	    invMatMapping.setItemcode( details.get( i ).getItemcode() );
	    invMatMapping.setTranType( "receivingmaterial" );
	    invMatMapping.setImtdid( imidId );
	    immList.add( invMatMapping );
	}

	Map< String , Object > mapData = new HashMap< String , Object >();
	mapData.put( "imm" , immList );
	mapData.put( "imtd" , tranDetails );

	try {
	    HashMap< String , Object > param = new HashMap< String , Object >();
	    param.put( "inacId" , invMatAccept.getInacId() );// 方便建立物资验收单和接收单之间的关联
	    invMatTranService.saveMatTran( userInfo , imtv , mapData , param );
	    
	    //TIM-2222 生物质，物资验收结束后，根据规则自动创建领料和发料单
        boolean autoDeliverySwitch = false;//系统配置表里自动领料开关是否打开
		boolean autoDeliveryCondition = false;//是否满足自动领料条件
        Configuration cf = itcMvcService.getConfiguration( "auto_delivery" );
        
        //记录即收即发情况，以后就以此字段值为准
        InvMatAccept invMatAcceptNew = new InvMatAccept();
        invMatAcceptNew.setInacId(invMatAccept.getInacId());
        invMatAcceptNew.setAutoDelivery("N");//默认为空
        invMatAcceptNew.setModifyuser(userInfo.getUserId());
        invMatAcceptNew.setModifydate(new Date());
        
        if ( null != cf ) {
        	autoDeliverySwitch = Boolean.valueOf( cf.getVal() == null ? "false" : cf.getVal() );
        }
		if(autoDeliverySwitch){
			Map<String, Object> autoDeliveryInfo = getAutuDeleiveyInfo(userInfo, invMatAccept.getInacId());
			autoDeliveryCondition = (Boolean) autoDeliveryInfo.get("autoDelivery");
			PurApply selectedPurApply = (PurApply) autoDeliveryInfo.get("selectedPurApply");
			//自动领料开关打开且满足条件才进行自动领料
		    if(autoDeliveryCondition){
		    	InvMatTran invMatTran = new InvMatTran();
		    	invMatTran.setOperuser(imtv.getOperuser());
			    invMatTranService.autoDelivery(userInfo, selectedPurApply, invMatTran, tranDetails);
		        invMatAcceptNew.setAutoDelivery("Y");
		    }
		}
        invMatAcceptDao.updateAutoDelivery(invMatAcceptNew);//更新验收表即收即发字段信息
	} catch ( Exception e ) {
	    LOGGER.error( e );
	    throw new RuntimeException( "物资入库时调用InvMatAcceptServiceImpl.saveMatTran方法失败" , e );
	}
	return true;
    }
    
    /**
     * 判断是否自动发料信息，并获取第一条满足规则的采购申请单名称
     * @description:验收单明细所关联的采购申请单的“采购类型”字段属于“劳保用品、福利设施、办公用品”或采购申请单的“资产性质”字段属于“固定资产”，则属于自动领料
     * @author: 890151
     * @createDate: 2016-11-14
     * @param userInfo
     * @param imacId
     * @return
     * @throws Exception:
     */
    @Override
    public Map<String, Object> getAutuDeleiveyInfo(UserInfoScope userInfo, String imacId) throws Exception{
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	boolean autoDelivery = false;
    	PurApply selectedPurApply = new PurApply();
    	//验收单明细所关联的采购申请单的“采购类型”字段属于“劳保用品、福利设施、办公用品”或采购申请单的“资产性质”字段属于“固定资产”，则属于自动领料
		InvMatAcceptDtlVo invMatAcceptVO = queryInvMatAcceptById(imacId);
    	if(invMatAcceptVO.getInvMatAcceptDetails()!=null){
    		//新建验收单时，选择了一个合同后，会获取合同的创建人（CREATEACCOUNT字段），提交验收单后存储到验收子表的purapply_usercode字段。
    		//“采购申请人执行验收”节点通过selectUserForZXYS接口选人，而SelectUserForZXYS则是将验收子表第一条记录的purapply_usercode作为候选人
    		//因此领料单的创建人填“采购申请人执行验收”节点办理人等同于验收子表第一条记录对应的采购申请创建人
        	List<InvMatAcceptDetailVO> invMatAcceptDetails = invMatAcceptVO.getInvMatAcceptDetails();
    	    InvMatAcceptDetail invMatAcceptDetail = invMatAcceptDetails.get(0);
			if( invMatAcceptDetail.getPuraId() != null ){
				PurApply purApply = purApplyService.queryPurApplyBySheetId(invMatAcceptDetail.getPuraId());
				if("ASSET".equals(purApply.getAssetNature()) || "劳保用品、福利设施、办公用品".indexOf(purApply.getSheetclassid())!=-1){
					selectedPurApply = purApply;
					//purApply的CREATEACCOUNT为中文，原来的CREATEACCOUNT转为了CREATEUSER
					selectedPurApply.setCreateaccount(purApply.getCreateuser());
					autoDelivery = true;
				}
			}
    	}
    	//构造返回对象
		resultMap.put("selectedPurApply", selectedPurApply);
		resultMap.put("autoDelivery", autoDelivery);
		return resultMap;
    }
    

    /*
     * (non-Javadoc)
     * @see
     * com.timss.inventory.service.core.InvMatAcceptService#departInvMat(com
     * .timss.inventory.bean.InvMatAccept, java.util.List)
     */
    @Override
    @Transactional
    public boolean departInvMat ( InvMatAccept invMatAccept , List< InvMatAcceptDetail > details ) {
	// 存放拆分后的详情
	Map< String , List< InvMatAcceptDetail >> allList = new HashMap< String , List< InvMatAcceptDetail >>();

	if ( details != null && !details.isEmpty() ) {
	    for ( int i = 0; i < details.size(); i++ ) {
		InvMatAcceptDetail detail = details.get( i );
		String userCode = detail.getPurapplyUsercode();
		Object list = allList.get( userCode );
		if ( list == null ) {
		    List< InvMatAcceptDetail > tmpList = new ArrayList< InvMatAcceptDetail >();
		    tmpList.add( detail );
		    allList.put( userCode , tmpList );
		} else {
		    List< InvMatAcceptDetail > tmpList = ( List< InvMatAcceptDetail > ) list;
		    tmpList.add( detail );
		}
	    }
	}

	// 发现多于1个时要进行处理。
	if ( allList.size() > 1 ) {
	    int i = 0;
	    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
	    String message = null;
	    Map< String , ArrayList > userId = null;

	    // 20151202 JIRA ==> TIM221
	    Map< String , List< String >> userIdInside = new HashMap< String , List< String >>();
	    List< String > ppList = new ArrayList< String >();
	    // 20151202 JIRA ==> TIM221 end

	    String assignee = null;
	    String owner = null;
	    try {
		message = userInfo.getParam( "message" );
		userId = JsonHelper.toObject( userInfo.getParam( "userIds" ) , Map.class );

		// 20151202 JIRA ==> TIM221 begin
		for ( Map.Entry< String , ArrayList > info : userId.entrySet() ) {
		    String processPerson = String.valueOf( info.getValue().get( 0 ) );
		    ppList = Arrays.asList( processPerson.split( "," ) );
		    userIdInside.put( info.getKey() , ppList );
		}
		// 20151202 JIRA ==> TIM221 end

		assignee = userInfo.getParam( "assignee" );
		owner = userInfo.getParam( "owner" );
	    } catch ( Exception e ) {
		throw new RuntimeException( "拆分验收流程是，无法获取流程变量" , e );
	    }
	    for ( Map.Entry< String , List< InvMatAcceptDetail >> entry : allList.entrySet() ) {
		i++;
		if ( i == 1 ) {
		    // 第一个拆分的流程只是更新数据。
		    List< InvMatAcceptDetail > tmpDetails = entry.getValue();
		    invMatAcceptDetailService.updateInvMatAcceptDetail( invMatAccept , tmpDetails );
		} else {
		    //
		    List< InvMatAcceptDetail > tmpDetails = entry.getValue();

		    invMatAccept.setInacId( null );
		    invMatAccept.setInacNo( null );
		    invMatAccept.setInstanceid( null );
		    insertInvMatAccept( invMatAccept , tmpDetails , true );
		    String curProcessId = invMatAccept.getInstanceid();
		    String taskId = getTaskId( curProcessId );
		    workflowService.setVariable( curProcessId , "isDeparted" , "Y" );
		    workflowService.complete( taskId , assignee , owner , userIdInside , message , false , null );
		}
	    }
	}

	return true;
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
	List< MTPurOrderVO > ret = invMatAcceptDao.queryPurOrderList( page );
	page.setResults( ret );
	return page;
    }

    /**
     * @description: 通过sheetNo和站点id找到申请表id
     * @author: 890151
     * @createDate: 2016-8-8
     * @param sheetNo
     * @param siteId
     * @return
     * @throws Exception:
     */
	@Override
	public String queryInvMatAcceptIdByFlowNo(String sheetNo, String siteId) throws Exception {
		Map< String , Object > map = new HashMap< String , Object >();
		map.put( "sheetNo" , sheetNo );
		map.put( "siteId" , siteId );
		String inacId = invMatAcceptDao.queryInvMatAcceptIdByFlowNo(map);
		return inacId;
	}

}
