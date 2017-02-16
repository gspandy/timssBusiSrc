package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvAttachMapping;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvRealTimeData;
import com.timss.inventory.bean.InvStocktaking;
import com.timss.inventory.bean.InvStocktakingDetail;
import com.timss.inventory.dao.InvAttachMappingDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvRealTimeDataDao;
import com.timss.inventory.dao.InvStocktakingDao;
import com.timss.inventory.dao.InvStocktakingDetailDao;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.service.InvStocktakingService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvStocktakingDetailVO;
import com.timss.inventory.vo.InvStocktakingVO;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvStocktakingServiceImpl")
public class InvStocktakingServiceImpl implements InvStocktakingService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvStocktakingDao invStocktakingDao;
    @Autowired
    private InvStocktakingDetailDao invStocktakingDetailDao;

    @Autowired
    private InvMatTranDao invMatTranDao;
    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;
    @Autowired
    private InvMatMappingDao invMatMappingDao;
    @Autowired
    private InvAttachMappingDao invAttachMappingDao;
    @Autowired
    private InvRealTimeDataDao invRealTimeDataDao;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvStocktakingServiceImpl.class );

    /**
     * @description:查询列表功能
     * @author: 890166
     * @createDate: 2014-9-30
     * @param userInfo
     * @param isv
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvStocktakingVO> queryStocktakingList(UserInfoScope userInfo, InvStocktakingVO isv) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvStocktakingVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "modifydate" );
            page.setSortOrder( "desc" );
        }

        if ( null != isv ) {
            page.setParameter( "sheetno", isv.getSheetno() );
            page.setParameter( "sheetname", isv.getSheetname() );
            page.setParameter( "createusername", isv.getCreateusername() );
            page.setParameter( "createdate", isv.getCreatedate() );
            page.setParameter( "warehousename", isv.getWarehousename() );
            page.setParameter( "status", isv.getStatus() );
        }
        List<InvStocktakingVO> ret = invStocktakingDao.queryStocktakingList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:直接查询表单数据
     * @author: 890166
     * @createDate: 2014-9-30
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvStocktakingVO> queryStocktakingForm(UserInfoScope userInfo, String istid) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvStocktakingVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "istId", istid );
        return invStocktakingDao.queryStocktakingList( page );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateStocktaking(UserInfoScope userInfo, InvStocktaking ist) throws Exception {
        ist.setModifydate( new Date() );
        ist.setModifyuser( userInfo.getUserId() );
        invStocktakingDao.updateInvStocktaking( ist );
    }

    /**
     * @description:保存盘点信息
     * @author: 890166
     * @createDate: 2014-11-10
     * @param userInfo
     * @param ist
     * @param istdList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveStocktaking(UserInfoScope userInfo, InvStocktaking ist,
            List<InvStocktakingDetail> istdList, Map<String, Object> paramMap) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        boolean flag = true;
        int count = 0;
        // 流程实例id
        String processId = paramMap.get( "processId" ) == null ? "" : String.valueOf( paramMap.get( "processId" ) );
        ist.setInstanceid( processId );
        reMap.put( "processId", processId );
        // 状态
        String status = paramMap.get( "status" ) == null ? "" : String.valueOf( paramMap.get( "status" ) );
        ist.setStatus( status );

        String istid = String.valueOf( paramMap.get( "istid" ) );
        if ( "".equals( istid ) ) {
            ist.setIstid( null );
            ist.setSheetno( null );
        } else {
            ist.setIstid( istid );
        }

        if ( null != ist.getCheckuser() ) {
            String[] checkUser = ist.getCheckuser().split( "_" );
            ist.setCheckuser( checkUser[0] );
        }

        try {
            if ( !"".equals( ist.getIstid() ) && null != ist.getIstid() ) {
                ist.setModifydate( new Date() );
                ist.setModifyuser( userInfo.getUserId() );
                count = invStocktakingDao.updateInvStocktaking( ist );
            } else {
                ist.setCreatedate( new Date() );
                ist.setCreateuser( userInfo.getUserId() );
                ist.setSiteId( userInfo.getSiteId() );
                count = invStocktakingDao.insertInvStocktaking( ist );
            }

            if ( count > 0 ) {
                reMap.put( "istid", ist.getIstid() );
                double totalPrice = 0.00D;
                double factor = 0.00D;
                // 子表数据重新插入
                invStocktakingDetailDao.deleteInvStocktakingDetailByIstId( ist.getIstid() );
                for ( InvStocktakingDetail istd : istdList ) {
                    factor = (istd.getQtyAfter().subtract( istd.getQtyBefore() ).abs()).doubleValue();
                    totalPrice += factor * istd.getPrice().doubleValue();
                    istd.setIstid( ist.getIstid() );
                    istd.setCreatedate( new Date() );
                    istd.setCreateuser( userInfo.getUserId() );
                    istd.setModifydate( new Date() );
                    istd.setModifyuser( userInfo.getUserId() );
                    istd.setSiteId( userInfo.getSiteId() );
                    istd.setInvcateid( istd.getInvcateid() );
                    invStocktakingDetailDao.insertInvStocktakingDetail( istd );
                }
                reMap.put( "totalPrice", totalPrice );
                // 附件上传
                String uploadIds = paramMap.get( "uploadIds" ) == null ? "" : String.valueOf( paramMap
                        .get( "uploadIds" ) );
                invAttachMappingDao.deleteInvAttachMappingBySheetId( ist.getIstid() );
                if ( !"".equals( uploadIds ) ) {
                    String[] ids = uploadIds.split( "," );
                    // 先删除后插入
                    for ( int i = 0; i < ids.length; i++ ) {
                        InvAttachMapping iam = new InvAttachMapping();
                        iam.setSheetid( ist.getIstid() );
                        iam.setAttachid( ids[i] );
                        invAttachMappingDao.insertInvAttachMapping( iam );
                    }
                    attachmentMapper.setAttachmentsBinded( ids, 1 );
                }
            }
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvStocktakingServiceImpl 中的 saveStocktaking 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
            flag = false;
        }
        reMap.put( "flag", flag );
        return reMap;
    }

    /**
     * @description:更新当前库存数量
     * @author: 890166
     * @createDate: 2014-11-17
     * @param listData
     * @param istid
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> changeInvStock(UserInfoScope userInfo, Map<String, Object> paramMap) {
        Map<String, Object> reMap = new HashMap<String, Object>();

        String istid = String.valueOf( paramMap.get( "istid" ) );
        String siteId = userInfo.getSiteId();

        boolean flag = true;// 返回状态

        InvMatTran imt = null;

        // 根据id查询主表单信息
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put( "siteId", siteId );
        queryMap.put( "istId", istid );
        List<InvStocktaking> istList = invStocktakingDao.queryStocktakingForm( queryMap );

        try {
            if ( null != istList && !istList.isEmpty() ) {
                // 获取数据后整理数据
                InvStocktaking ist = istList.get( 0 );

                ist.setStatus( "approval_complete" );
                invStocktakingDao.updateInvStocktaking( ist );

                paramMap.put( "ist", ist );
                paramMap.put( "siteId", siteId );
                // 转换数据
                imt = conventStocktaking2MatTran( paramMap );
                // 插入交易表
                int count = invMatTranDao.insertInvMatTran( imt );

                // 若插入成功
                if ( count > 0 ) {
                    // 查询子单的数据
                    Page<InvStocktakingDetailVO> page = new Page<InvStocktakingDetailVO>();
                    String pageSize = CommonUtil.getProperties( "pageSize" );
                    page.setPageSize( Integer.valueOf( pageSize ) );
                    page.setParameter( "siteId", siteId );
                    page.setParameter( "istId", istid );
                    List<InvStocktakingDetailVO> istdList = invStocktakingDetailDao.queryStocktakingDetailList( page );

                    // 收集并保存数据
                    paramMap.put( "istdList", istdList );
                    paramMap.put( "imt", imt );
                }
            }
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvStocktakingServiceImpl 中的 changeInvStock 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
            flag = false;
        }
        conventStocktaking2MatTranInList( paramMap );
        reMap.put( "flag", flag );
        return reMap;
    }

    /**
     * @description:转换数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param imt
     * @param ist
     * @param siteId
     * @return:
     */
    private InvMatTran conventStocktaking2MatTran(Map<String, Object> paramMap) {
        InvStocktaking ist = (InvStocktaking) paramMap.get( "ist" );
        String siteId = String.valueOf( paramMap.get( "siteId" ) );

        InvMatTran imt = new InvMatTran();
        imt.setCheckuser( ist.getCheckuser() );
        imt.setCreatedate( new Date() );
        imt.setCreateuser( ist.getCreateuser() );
        imt.setLotno( new BigDecimal( "1" ) );
        imt.setModifydate( new Date() );
        imt.setModifyuser( ist.getModifyuser() );
        imt.setOperuser( ist.getCreateuser() );
        imt.setRemark( ist.getRemark() );
        imt.setSiteId( siteId );
        imt.setTranType( "materialscounting" );
        imt.setSheetno( ist.getSheetno() );
        return imt;
    }

    /**
     * @description: 转换并保存数据
     * @author: 890166
     * @createDate: 2014-11-18
     * @param paramMap:
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void conventStocktaking2MatTranInList(Map<String, Object> paramMap){
        // 获取并转换成需要的格式
        InvMatTran imt = (InvMatTran) paramMap.get( "imt" );
        InvStocktaking ist = (InvStocktaking) paramMap.get( "ist" );
        List<InvStocktakingDetailVO> istdList = (List<InvStocktakingDetailVO>) paramMap.get( "istdList" );
        // 遍历保存数据
        for ( InvStocktakingDetailVO istdv : istdList ) {
            InvMatTranDetail imtd = new InvMatTranDetail();
            imtd.setBinid( istdv.getBinid() );
            imtd.setCreatedate( new Date() );
            imtd.setCreateuser( istdv.getCreateuser() );
            imtd.setImtid( imt.getImtid() );

            double qtyBefore = istdv.getQtyBefore().doubleValue();// 之前的数据
            double qtyAfter = istdv.getQtyAfter().doubleValue();// 之后的数据
            double remainQty = qtyBefore - qtyAfter;// 数据相减后判断是增加还是减去
            if ( remainQty > 0 ) {
                // 减去数据
                imtd.setInQty( new BigDecimal( "0" ) );
                imtd.setOutQty( new BigDecimal( remainQty ) );
            } else if ( remainQty < 0 ) {
                // 增加数据
                imtd.setInQty( new BigDecimal( remainQty ).abs() );
                imtd.setOutQty( new BigDecimal( "0" ) );
            }

            imtd.setInUnitid( istdv.getUnitid() );
            imtd.setImtdid( CommonUtil.getUUID() );
            imtd.setItemcode( istdv.getItemcode() );
            imtd.setItemid( istdv.getItemid() );
            imtd.setLotno( new BigDecimal( "1" ) );
            imtd.setModifydate( new Date() );
            imtd.setModifyuser( istdv.getModifyuser() );
            imtd.setOutUnitid( istdv.getUnitid() );
            imtd.setPrice( istdv.getPrice() );
            imtd.setRemark( istdv.getRemark() );
            imtd.setSiteId( istdv.getSiteId() );
            imtd.setWarehouseid( ist.getWarehouseid() );
            imtd.setInvcateid( istdv.getInvcateid() );
            // 保存交易表明细
            invMatTranDetailDao.insertInvMatTranDetail( imtd );

            // 保存映射表
            InvMatMapping imm = new InvMatMapping();
            imm.setImtdid( imtd.getImtdid() );
            imm.setOutterid( istdv.getIstdid() );
            imm.setItemcode( istdv.getItemcode() );
            imm.setTranType( "materialscounting" );
            invMatMappingDao.insertInvMatMapping( imm );
            //盘盈
            if ( remainQty < 0 ) {
            	imtd.setCanOutQty(new BigDecimal( remainQty ).abs());
                //如果实时表实际库存为0，则取最近一次入库的价格作为盘入价格，最近入库价格没有则置为0
                InvRealTimeData invRealTimeData = invRealTimeDataDao.queryInvRealTimeDataByCompositeKey(istdv.getItemid(), istdv.getInvcateid(), istdv.getSiteId());
            	if(invRealTimeData!=null && 
            			invRealTimeData.getActualQty()!=null &&
            			invRealTimeData.getActualQty().compareTo(BigDecimal.ZERO)==0 &&
            			invRealTimeData.getLastInTime()!=null){  
	            	imtd.setTax( invRealTimeData.getLastInTax() );//有最近入库时间，就会有最近入库三个价格
	            	imtd.setNoTaxPrice( invRealTimeData.getLastInNoTaxPrice() );
	            	imtd.setPrice(invRealTimeData.getLastInPrice() );
            	}
            	else if(invRealTimeData!=null && 
            			invRealTimeData.getActualQty()!=null &&
            			invRealTimeData.getActualQty().compareTo(BigDecimal.ZERO)>0){
	            	imtd.setTax( invRealTimeData.getTax() );
	            	imtd.setNoTaxPrice( invRealTimeData.getNoTaxPrice() );
	            	imtd.setPrice(invRealTimeData.getWithTaxPrice());
            	}
            	else{
            		imtd.setTax( new BigDecimal(0) );
	            	imtd.setNoTaxPrice( new BigDecimal(0) );
	            	imtd.setPrice( new BigDecimal(0) );
            	}
            }
            //插入新的流水和映射关系
        	invRealTimeDataService.insertNewRecAndMap( imtd, imm, remainQty < 0 ? "3" : "2");
        }
    }

    /**
     * @description: 查询并获取盘点附件
     * @author: 890166
     * @createDate: 2014-11-18
     * @param istid
     * @return
     * @throws Exception:
     */
    @Override
    public List<Map<String, Object>> queryStocktakingAttach(String istid) throws Exception {
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
    public String queryFlowNoByIstid(String istId, String siteId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "istId", istId );
        map.put( "siteId", siteId );
        return invStocktakingDao.queryFlowNoByIstid( map );
    }

    /**
     * @description: 通过sheetNo和站点id找到id
     * @author: 890166
     * @createDate: 2015-3-2
     * @param flowNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    @Override
    public String queryIstidByFlowNo(String sheetNo, String siteId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "sheetNo", sheetNo );
        map.put( "siteId", siteId );
        return invStocktakingDao.queryIstidByFlowNo( map );
    }

    /**
     * @description: 获取表单主信息
     * @author: 890166
     * @createDate: 2015-3-2
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvStocktaking> queryStocktakingInfo(UserInfoScope userInfo, String istid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "istId", istid );
        return invStocktakingDao.queryStocktakingForm( map );
    }
}
