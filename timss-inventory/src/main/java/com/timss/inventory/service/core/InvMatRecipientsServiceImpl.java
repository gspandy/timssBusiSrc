package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvAttachMapping;
import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.bean.InvMatRecipientsDetail;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.dao.InvAttachMappingDao;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatRecipientsDao;
import com.timss.inventory.dao.InvMatRecipientsDetailDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.exception.InsufficientException;
import com.timss.inventory.exception.PriceNotExistException;
import com.timss.inventory.service.InvMatApplyService;
import com.timss.inventory.service.InvMatRecipientsService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsServiceImpl.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
@Service ( "InvMatRecipientsServiceImpl" )
public class InvMatRecipientsServiceImpl implements InvMatRecipientsService {

    @Autowired
    private InvMatRecipientsDao       invMatRecipientsDao;
    @Autowired
    private InvMatRecipientsDetailDao invMatRecipientsDetailDao;
    @Autowired
    private InvMatApplyDetailDao invMatApplyDetailDao;
    @Autowired
    private InvMatTranDao	     invMatTranDao;
    @Autowired
    private InvMatTranDetailDao       invMatTranDetailDao;
    @Autowired
    private InvMatMappingDao	  invMatMappingDao;
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    @Autowired
    private InvMatApplyService invMatApplyService;
    @Autowired
    private InvAttachMappingDao invAttachMappingDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    /**
     * log4j输出
     */
    private static final Logger       LOG = Logger.getLogger( InvMatRecipientsServiceImpl.class );

    /**
     * 生成领用信息前保存记录
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > autoGenerateConsuming ( UserInfoScope userInfo , InvMatApply ima ,
	    List< InvMatApplyDetailVO > imaList , Map< String , Object > paramMap ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	String flag = "success";

	String processId = paramMap.get( "processId" ) == null ? "" : String.valueOf( paramMap.get( "processId" ) );
	String type = paramMap.get( "type" ) == null ? "" : String.valueOf( paramMap.get( "type" ) );
	try {
	    InvMatRecipients imr = new InvMatRecipients();
	    int counter = 0;
	    if ( null != ima ) {
		imr.setApplyType( ima.getApplyType() );
		imr.setCreatedate( new Date() );
		imr.setCreateuser( userInfo.getUserId() );
		imr.setModifydate( new Date() );
		imr.setModifyuser( userInfo.getUserId() );
		imr.setSheetname( ima.getSheetname() );
		imr.setSiteId( userInfo.getSiteId() );
		imr.setInstanceid( processId );
		imr.setImaid( ima.getImaid() );
		imr.setRemark( ima.getRemark() );
		imr.setStatus("N");
		counter = invMatRecipientsDao.insertInvMatRecipients( imr );
	    }

	    if ( counter > 0 ) {
		map.put( "imr" , imr );
		if ( null != imaList && !imaList.isEmpty() ) {
		    for ( InvMatApplyDetailVO imad : imaList ) {
			InvMatRecipientsDetail imrd = new InvMatRecipientsDetail();

			imrd.setCreatedate( new Date() );
			imrd.setCreateuser( userInfo.getUserId() );
			imrd.setImrid( imr.getImrid() );
			imrd.setItemid( imad.getItemid() );
			imrd.setModifydate( new Date() );
			imrd.setModifyuser( userInfo.getUserId() );

			if ( "interface".equals( type ) ) {
			    imrd.setOutstockqty( imad.getQtyApply() );
			} else {
			    imrd.setOutstockqty( imad.getOutstockqty() );
			}

			imrd.setQtyApply( imad.getQtyApply() );
			imrd.setWarehouseid( imad.getWarehouseid() );
			imrd.setBinid( imad.getBinid() );
			imrd.setSiteId( userInfo.getSiteId() );
			imrd.setImadid( imad.getImadid() );
			imrd.setIsled( "N" );
			imrd.setInvcateid( imad.getInvcateid() );
			imrd.setPrice(imad.getPrice());
			imrd.setNoTaxPrice(imad.getNoTaxPrice());
			invMatRecipientsDetailDao.insertInvMatRecipientsDetail( imrd );
		    }
		}
	    }
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "---------InvMatRecipientsServiceImpl 中的 autoGenerateConsuming 方法抛出异常---------：" + e.getMessage() );
	    LOG.info( "--------------------------------------------" );
	    flag = "false";
	}
	map.put( "flag" , flag );
	return map;
    }

    /**
     * @description:查询表单信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    @Override
    public List< InvMatRecipientsVO > queryInvMatRecipientsForm ( UserInfoScope userInfo , String imrid )
	    throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "imrid" , imrid );
	List<InvMatRecipientsVO> imrList = invMatRecipientsDao.queryInvMatRecipientsForm( map );
	for (InvMatRecipientsVO imrVo : imrList) {
		//根据imaId查询相关领料单单号和领用部门
		if(StringUtils.isNotBlank(imrVo.getImaid())){
			List<InvMatApplyVO> imaList = invMatApplyService.queryMatApplyForm( userInfo , imrVo.getImaid() , "1" );
	        if ( null != imaList && !imaList.isEmpty() ) {
	            InvMatApplyVO ima = imaList.get( 0 );
	            imrVo.setApplySheetNo(ima.getSheetno());//领料单单号
	            imrVo.setApplyDept(ima.getDept());//领用部门
	            imrVo.setApplyUse(ima.getApplyUse());//用途说明
	        }
		}
	}
	return imrList;
    }

    /**
     * @description:查看列表信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    @Override
    public Page< InvMatRecipientsDetailVO > queryInvMatRecipientsList ( UserInfoScope userInfo , String imrid )
	    throws Exception {

	UserInfoScope scope = userInfo;
	Page< InvMatRecipientsDetailVO > page = scope.getPage();

	String pageSize = CommonUtil.getProperties( "pageSize" );
	page.setPageSize( Integer.valueOf( pageSize ) );

	page.setParameter( "siteId" , userInfo.getSiteId() );
	page.setParameter( "imrid" , imrid );
	List< InvMatRecipientsDetailVO > ret = invMatRecipientsDao.queryInvMatRecipientsList( page );
	page.setResults( ret );
	return page;
    }
    
    @Override
    public List< InvMatRecipientsDetailVO> queryInvMatRecipListByItem(String imaid,String siteid,String itemid,String warehouseid){
        List< InvMatRecipientsDetailVO> resultDetailVOs = null;
        resultDetailVOs = invMatRecipientsDao.queryInvMatRecipListByItem( imaid,siteid,itemid,warehouseid );
        return resultDetailVOs;
    }
    
    /**
     * @description:保存方法
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imr
     * @param imrList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional ( propagation = Propagation.REQUIRED , rollbackFor = Exception.class )
    public Map< String , Object > saveRecipientsTran ( UserInfoScope userInfo , InvMatRecipients imr ,
	    List< InvMatRecipientsDetailVO > imrList , String uploadIds) throws Exception {

	Map< String , Object > map = new HashMap< String , Object >();
	String flag = "success";
	String siteId = userInfo.getSiteId();
	try {

	    List< InvMatTran > imtList = invMatTranDao.queryInvMatTranBySheetNo( imr.getSheetno() );
	    if ( imtList.isEmpty() ) {
		InvMatTranDetail imtd = null;
		InvMatMapping imm = null;
		InvMatTran imt = new InvMatTran();
		// 将领用信息转换成出库交易信息（主单）
		imt.setSheetno( imr.getSheetno() );
		imt.setTranType( "pickingmaterials" );
		imt.setOperuser( userInfo.getUserId() );
		imt.setCheckuser( userInfo.getUserId() );
		imt.setCreateuser( userInfo.getUserId() );
		imt.setCreatedate( new Date() );
		imt.setLotno( new BigDecimal( "1" ) );
		imt.setSiteId( userInfo.getSiteId() );
		int count = invMatTranDao.insertInvMatTran( imt );
		// 若主单信息添加成功，则开始转换子单信息
		if ( count > 0 ) {
		    for ( InvMatRecipientsDetailVO imrd : imrList ) {
			imtd = new InvMatTranDetail();
			imm = new InvMatMapping();
			String imtdId = CommonUtil.getUUID();
			imtd.setImtdid( imtdId );
			imtd.setImtid( imt.getImtid() );
			imtd.setItemid( imrd.getItemid() );
			imtd.setBinid( imrd.getBinid() );
			imtd.setWarehouseid( imrd.getWarehouseid() );
			imtd.setLotno( new BigDecimal( "1" ) );
			imtd.setInQty( new BigDecimal( 0 ) );
			imtd.setOutQty( imrd.getOutstockqty() );
			imtd.setOutUnitid( imrd.getUnitCode1() );
			imtd.setCreatedate( new Date() );
			imtd.setCreateuser( userInfo.getUserId() );
			imtd.setSiteId( userInfo.getSiteId() );
			imtd.setPrice( imrd.getPrice() );
			imtd.setItemcode( imrd.getItemcode() );
			imtd.setInvcateid( imrd.getInvcateid() );
			invMatTranDetailDao.insertInvMatTranDetail( imtd );

			imm.setImtdid( imtdId );
			imm.setOutterid( imrd.getImadid() );
			imm.setTranType( "pickingmaterials" );
			invMatMappingDao.insertInvMatMapping( imm );

			//插入新的流水和映射关系（旧的流水price由业务模块传，新的流水由算法算出，此处imtd可以不设置price）
			Map<String, Object> outInfo = invRealTimeDataService.insertNewRecAndMap(imtd, imm, "2");
			if("success".equals(outInfo.get("result"))){
			    //领料子表更新isled和price字段信息
				InvMatRecipientsDetail imrD = new InvMatRecipientsDetail();
				String price = null;
				String noTaxPrice = null;
				if("SWF".equals(siteId)){
					if(outInfo.get("noTaxPrice")==null || outInfo.get("noTaxPrice").equals("")){
						throw new PriceNotExistException();
					}
					price = outInfo.get("noTaxPrice").toString();//生物质发料取不含税单价
				}
				else if("SJW".equals(siteId)){
					if(outInfo.get("price")==null || outInfo.get("price").equals("") ||
							outInfo.get("noTaxPrice")==null || outInfo.get("noTaxPrice").equals("") ){
						throw new PriceNotExistException();
					}
					price = outInfo.get("price").toString();//沙C多经价格严格区分
					noTaxPrice = outInfo.get("noTaxPrice").toString();
					imrD.setNoTaxPrice(new BigDecimal(noTaxPrice));
				}
				else{
					if(outInfo.get("price")==null || outInfo.get("price").equals("")){
						throw new PriceNotExistException();
					}
					price = outInfo.get("price").toString();//其他站点取含税单价
				}
				imrD.setImrdid( imrd.getImrdid() );
				imrD.setIsled( "Y" );
				imrD.setPrice(new BigDecimal(price));
				invMatRecipientsDetailDao.updateInvMatRecipientsDetail( imrD );
				
			    LOG.info( "InvMatRecipientsServiceImpl 中的 saveRecipientsTran 方法调用realTimeCaluAndUpdateTran成功！" 
			    		+ "新的流水price为：" + outInfo.get("price") + "; "
			    		+ "新的流水noTaxPrice为：" + outInfo.get("noTaxPrice") + "; "
			    		+ "新的流水tax为：" + outInfo.get("tax")
			    );
			}
			else{
				throw new InsufficientException();
			}
		    
		    }
		    //更新发料单的发料时间和状态
			InvMatRecipients newInvMatRecipients = new InvMatRecipients();
			newInvMatRecipients.setImrid(imr.getImrid());
			newInvMatRecipients.setDeliveryDate(imt.getCreatedate());
			newInvMatRecipients.setStatus("Y");
			newInvMatRecipients.setModifydate(new Date());
			newInvMatRecipients.setModifyuser(userInfo.getUserId());
			newInvMatRecipients.setSiteId(userInfo.getSiteId());
		    invMatRecipientsDao.updateInvMatRecipients(newInvMatRecipients);
		    // 获取领用详细信息
		    Map< String , Object > paramMap = new HashMap< String , Object >();
		    paramMap.put( "siteId" , userInfo.getSiteId() );
		    paramMap.put( "imaId" , imr.getImaid() );
		    List< InvMatRecipientsDetail > imrdList = invMatRecipientsDetailDao.queryIMRDQtyByImaId( paramMap );
		    boolean alreadyOut = true;
		    if(imrdList.size()<=0){
		    	alreadyOut = false;
		    }
		    //发料对应的领料单子表信息
		    List<InvMatApplyDetailVO> imardVOList = invMatApplyDetailDao.queryApplyDetailList(imr.getImaid());
		    for(InvMatApplyDetailVO imadVO : imardVOList){
		    	int outQty = 0;
		    	String itemId = imadVO.getItemid();
		    	String invcateId = imadVO.getInvcateid();
		    	int qtyApply = imadVO.getQtyApply().intValue();
		    	for ( int i = 0; i < imrdList.size(); i++ ) {
					InvMatRecipientsDetail imrd = imrdList.get( i );
					String imrdItemId = imrd.getItemid();
					String imrdInvcateId = imrd.getInvcateid();
					if(itemId.equals(imrdItemId) && invcateId.equals(imrdInvcateId)){
						outQty += imrd.getOutstockqty().intValue();
					}
	    		}
		    	if(qtyApply != outQty){
		    		alreadyOut = false;
		    		break;
		    	}
		    }
		    InvMatApply imaIn = new InvMatApply();
			imaIn.setImaid( imr.getImaid() );
		    if( alreadyOut){
		    	// 若申请数量等于入库数量，则领用申请设置状态为“审批完成”
				imaIn.setStatus( "approval_complete" );
		    }else{
		    	imaIn.setStatus( "collect_supplies" );
		    }
		    //提出调用update方法 方便发料后实时表更新
		    invMatApplyService.updateInvMatApply( CommonUtil.conventJsonToInvMatApplyDetailList(imardVOList), imaIn );
		    
		    Map<String, Object> passMap = new HashMap<String, Object>();
		    uploadIds = uploadIds.replace( "\"", "" );
		    if(uploadIds!=null&&uploadIds!=""){
		    	passMap.put("uploadIds", uploadIds);
			    passMap.put("imrid", imr.getImrid());
		        saveInvMatRecipientsAttach(passMap);
		    }
		    
		}
	    } else {
		flag = "same";
	    }
	} catch ( Exception e ) {
	    LOG.info( "--------------------------------------------" );
	    LOG.info( "- InvMatRecipientsServiceImpl 中的 saveRecipientsTran 方法抛出异常：" + e.getMessage() + " - " );
	    LOG.info( "--------------------------------------------" );
	    flag = "false";
	    if(e instanceof InsufficientException){
			throw new InsufficientException();
	    }
	    else if(e instanceof PriceNotExistException){
			throw new PriceNotExistException();
	    }
	    else{
	    	throw new Exception();
	    }
	}
	map.put( "flag" , flag );
	return map;
    }

    /**
     * @description:根据sheetno查询id
     * @author: 890166
     * @createDate: 2014-10-24
     * @param sheetNo
     * @return
     * @throws Exception:
     */
    @Override
    public String queryRecipientsIdBySheetNo ( UserInfoScope userInfo , String sheetNo ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "sheetNo" , sheetNo );
	return invMatRecipientsDao.queryRecipientsIdBySheetNo( map );
    }

    /**
     * @description: 通过imaid查询发料单数据
     * @author: 890166
     * @createDate: 2015-1-6
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    @Override
    public List< InvMatRecipientsVO > queryRecipientsByImaId ( UserInfoScope userInfo , String imaid ) throws Exception {
	Map< String , Object > map = new HashMap< String , Object >();
	map.put( "siteId" , userInfo.getSiteId() );
	map.put( "imaid" , imaid );
	List<InvMatRecipientsVO> imrList = invMatRecipientsDao.queryRecipientsByImaId( map );
	return imrList;
    }

    /**
     * @description:验证物资是否能够出库
     * @author: 890166
     * @createDate: 2015-6-17
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    @Override
    public Map< String , Object > validateCanOut ( UserInfoScope userInfo , List< InvMatRecipientsDetailVO > imrList )
	    throws Exception {
	BigDecimal stockQty = new BigDecimal( "0.00" );
	BigDecimal outStockQty = new BigDecimal( "0.00" );

	Map< String , Object > map = new HashMap< String , Object >();
	Map< String , Object > reMap = new HashMap< String , Object >();

	StringBuilder reMsg = new StringBuilder( "" );

	if ( null != imrList && !imrList.isEmpty() ) {
	    for ( InvMatRecipientsDetailVO imrdv : imrList ) {
		map.put( "itemCode" , imrdv.getItemcode() );
		map.put( "siteId" , userInfo.getSiteId() );
		stockQty = invMatTranDetailDao.queryInvStockQty( map );
		outStockQty = imrdv.getOutstockqty();

		if ( null != stockQty && null != outStockQty ) {
		    if ( stockQty.doubleValue() < outStockQty.doubleValue() ) {
			reMsg.append( imrdv.getItemcode() ).append( "," );
		    }
		}
	    }
	    if ( reMsg.length() > 0 ) {
		String reMsgStr = reMsg.substring( 0 , reMsg.length() - 1 );
		reMap.put( "flag" , "false" );
		reMap.put( "remark" , reMsgStr );
	    } else {
		reMap.put( "flag" , "success" );
	    }
	}
	return reMap;
    }

    /**
     * @description:根据发料单ID删除发料主表和子表记录
     * @author: 890151
     * @createDate: 2016-8-25
     * @param imrId
     * @return
     * @throws Exception:
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteInvMatRecipientsByImrId(String imrId, String siteId) throws Exception {
		Integer result1 = invMatRecipientsDao.deleteInvMatRecipients(imrId, siteId);
		Integer result2 = invMatRecipientsDetailDao.deleteInvMatRecipientsDetail(imrId, siteId);
		return true;
	}
	
	/**
     * @description:查看物资发料单列表
     * @author: 890199
     * @createDate: 2016-11-09
     * @param siteId
     * @return
     * @throws Exception:
     */
	public List<InvMatRecipientsVO> queryInvMatRecipientsApplyList(Page<InvMatRecipientsVO> page){
		List<InvMatRecipientsVO> result = null;
		if(page.getParams()!=null && page.getParams().containsKey("sdata")){
			result = invMatRecipientsDao.searchInvMatRecipientsApplyList(page);
		}else{
			result = invMatRecipientsDao.queryInvMatRecipientsApplyList(page);
		}
		return result;
	}
	
	/**
     * @description:保存附件功能
     * @author: 890199
     * @createDate: 2017-01-13
     * @param paramMap
     * @return
     * @throws Exception:
     */
	@Override
    public Map<String, Object> saveInvMatRecipientsAttach (Map<String, Object> paramMap) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	//获取附件id和imrid
        String uploadIds = paramMap.get( "uploadIds" ) == null ? "" : String.valueOf( paramMap.get( "uploadIds" ) );
        String imrid = paramMap.get("imrid") == null ? "" : String.valueOf(paramMap.get("imrid"));
        if(!"".equals( imrid )){
            // 先删除后插入
            invAttachMappingDao.deleteInvAttachMappingBySheetId( imrid );
            if ( !"".equals( uploadIds )) {
                String[] ids = uploadIds.split( "," );
                for ( int i = 0; i < ids.length; i++ ) {
                    InvAttachMapping iam = new InvAttachMapping();
                    iam.setSheetid( imrid );
                    iam.setAttachid( ids[i] );
                    invAttachMappingDao.insertInvAttachMapping( iam );
                }
                attachmentMapper.setAttachmentsBinded( ids, 1 );
            }
            result.put("result", "success");
        }
    	return result;
    }
    
    /**
     * @description:获取附件
     * @author: 890199
     * @createDate: 2017-01-16
     * @param paramMap
     * @return
     * @throws Exception:
     */
	@Override
	public List<Map<String, Object>> queryMatRecipientsAttach(String istid) throws Exception {
		List<InvAttachMapping> iamList = invAttachMappingDao.queryInvAttachMappingBysheetId( istid );
        ArrayList<String> ids = new ArrayList<String>();
        for ( int i = 0; i < iamList.size(); i++ ) {
            InvAttachMapping iam = iamList.get( i );
            ids.add( iam.getAttachid() );
        }
        List<Map<String, Object>> fileMap = new ArrayList<Map<String, Object>>();
        fileMap = FileUploadUtil.getJsonFileList( Constant.basePath, ids );
        return fileMap;
    }
}
