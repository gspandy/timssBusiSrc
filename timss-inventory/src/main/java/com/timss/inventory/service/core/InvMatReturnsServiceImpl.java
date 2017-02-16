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

import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatReturns;
import com.timss.inventory.bean.InvMatReturnsDetail;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.dao.InvMatMapDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatReturnsDao;
import com.timss.inventory.dao.InvMatReturnsDetailDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.dao.InvOutterMappingDao;
import com.timss.inventory.exception.InsufficientException;
import com.timss.inventory.service.InvMatRecipientsService;
import com.timss.inventory.service.InvMatReturnsService;
import com.timss.inventory.service.InvMatTranRecService;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.utils.ReflectionUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.exception.RunException;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsServiceImpl.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvMatReturnsServiceImpl")
public class InvMatReturnsServiceImpl implements InvMatReturnsService {

    private static final Logger LOG = Logger.getLogger( InvMatReturnsServiceImpl.class );

    @Autowired
    private InvMatReturnsDao invMatReturnsDao;
    @Autowired
    private InvMatReturnsDetailDao invMatReturnsDetailDao;
    @Autowired
    private InvMatMappingDao invMatMappingDao;
    @Autowired
    private InvMatMapDao invMatMapDao;
    @Autowired
    private InvOutterMappingDao invOutterMappingDao;
    @Autowired
    private InvMatTranDao invMatTranDao;
    @Autowired
    private InvMatTranRecService invMatTranRecService;
    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;
    @Autowired
    private InvMatTranRecDao invMatTranRecDao;
    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    @Autowired
    private InvMatRecipientsService  invMatRecipientsService;
    
    /**
     * @description:查询全部被退货的物资
     * @author: 890166
     * @createDate: 2015-3-12
     * @param userInfo
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvMatReturnsVO> queryAllReturnItem(UserInfoScope userInfo, String schfield) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatReturnsVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "userId", userInfo.getUserId() );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "return_date" );
            page.setSortOrder( "desc" );
        }

        schfield = schfield == null ? "" : schfield;
        if ( !"".equals( schfield ) ) {
            page.setParameter( "schfield", schfield );
        }

        List<InvMatReturnsVO> ret = invMatReturnsDao.queryAllReturnItem( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:删除退换货记录
     * @author: 890166
     * @createDate: 2015-3-12
     * @param imrvList
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteMatReturns(UserInfoScope userInfo, List<InvMatReturnsVO> imrvList) {
        UserInfoScope scope = userInfo;
        String siteId = scope.getSiteId();
        String imrsid = null;
        boolean flag = false;
        try {
            for ( InvMatReturnsVO imrVO : imrvList ) {
                imrsid = imrVO.getImrsid();
                // 删除外部关联表
                deleteMatMappingByReturns( imrsid );
                deleteOutterMappingByReturns( imrsid, siteId );

                invMatReturnsDetailDao.deleteMatReturnsDetailByImrsid( imrsid );
                invMatReturnsDao.deleteMatReturnsByImrsid( imrsid );
            }
            flag = true;
        } catch (Exception e) {
            throw new RuntimeException( "---------InvMatReturnsServiceImpl 中的 deleteMatReturns 方法抛出异常---------：", e );
        }
        return flag;
    }

    /**
     * @description:在物资退换货中删除交易表映射
     * @author: 890166
     * @createDate: 2015-3-13
     * @param imrsid :
     */
    private void deleteMatMappingByReturns(String imrsid) {
        String imtdid = null;
        String imtid = null;
        boolean flag = false;
        Map<String, Object> immMap = new HashMap<String, Object>();
        immMap.put( "outterid", imrsid );
        immMap.put( "type", "strippermaterials" );
        List<InvMatMapping> immList = invMatMappingDao.queryInvMatMappingInfo( immMap );
        for ( InvMatMapping imm : immList ) {
            imtdid = imm.getImtdid();
            invMatMappingDao.deleteMatMappingByImtdid( imtdid );
            invMatMapDao.deleteInvMatMapByImtdid( imtdid );
            if ( !flag ) {
                List<InvMatTranDetail> imtdList = invMatTranDetailDao.queryInvMatTranDetailByImtdid( imtdid );
                if ( !imtdList.isEmpty() ) {
                    imtid = imtdList.get( 0 ).getImtid();
                    flag = true;
                }
            }
        }
        invMatTranDetailDao.deleteMatTranDetailByImtid( imtid );
        invMatTranRecDao.deleteMatTranRecByImtid( imtid );
        invMatTranDao.deleteInvMatTranById( imtid );
    }

    /**
     * @description: 删除外部引用信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @param imrsid
     * @param siteId:
     */
    private void deleteOutterMappingByReturns(String imrsid, String siteId) {
        Map<String, Object> immMap = new HashMap<String, Object>();
        immMap.put( "outterId", imrsid );
        immMap.put( "siteid", siteId );
        invOutterMappingDao.deleteOutterMappingByMap( immMap );
    }

    /**
     * @description: 查询表单详细信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param userInfo
     * @param imrsid
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvMatReturnsVO> queryMatReturnsForm(UserInfoScope userInfo, String imrsid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteid", userInfo.getSiteId() );
        map.put( "imrsid", imrsid );
        return invMatReturnsDao.queryMatReturnsForm( map );
    }

    /**
     * @description:表单列表查询
     * @author: 890166
     * @createDate: 2015-3-16
     * @param userInfo
     * @param imrsid
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvMatReturnsDetailVO> queryMatReturnsDetailList(UserInfoScope userInfo, String imrsid)
            throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatReturnsDetailVO> page = scope.getPage();

        if ( !"".equals( imrsid ) ) {
            page.setParameter( "siteId", userInfo.getSiteId() );
            page.setParameter( "imrsid", imrsid );

            page.setSortKey( "itemid" );
            page.setSortOrder( "asc" );
            List<InvMatReturnsDetailVO> ret = invMatReturnsDetailDao.queryMatReturnsDetailList( page );
            page.setResults( ret );
        }
        return page;
    }

    /**
     * @description: 保存退货物资信息 步骤 a.向退货主表插入form表单信息，同时向交易表中插入一条出库信息
     *               b.插入退货明细信息，同时向交易明细表中插入对应的信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @param userInfo
     * @param imr
     * @param imrdList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveMatReturns(UserInfoScope userInfo, InvMatReturns imr,
            List<InvMatReturnsDetailVO> imrdList, Map<String, Object> paramMap) throws Exception {
        Map<String, Object> reMap = new HashMap<String, Object>();
        boolean isReceiveAll = false; // 退库都是false

        /*String returnType = (String) paramMap.get( "rebackType" );
        // TODO 退库之前，先判断是否是完全接收，如果是完全接收，则在退货之后要改为部分接受，同时要将待办打开
        if ( "returns".equals( returnType ) ) {
            String pruorderno = (String) paramMap.get( "pruorderno" ); // 采购单的Sheetno
            isReceiveAll = isReceiveAllItem( pruorderno );
        }*/

        // 再保存流水表数据
        boolean flag = saveMatTranInfo( userInfo, imr, imrdList, paramMap, isReceiveAll );
        reMap.put( "flag", flag );
        return reMap;
    }

    /**
     * @description: 保存业务数据信息
     * @author: yuanzh
     * @createDate: 2015-10-10
     * @param userInfo
     * @param imr
     * @param imrdList
     * @param paramMap
     * @throws Exception:
     */
    private void saveMatReturnsBusi(UserInfoScope userInfo, InvMatReturns imr, List<InvMatReturnsDetailVO> imrdList,
            Map<String, Object> paramMap) throws Exception {

        int count = 0;
        InvMatReturnsDetail imrd = null;
        List<InvMatReturnsDetailVO> imrdvList = null;
        String imrsid = paramMap.get( "imrsid" ) == null ? "" : String.valueOf( paramMap.get( "imrsid" ) );
        if ( "".equals( imrsid ) ) {
            imr.setImrsid( null );
            imr.setImrsno( null );
        } else {
            imr.setImrsid( imrsid );
        }

        try {
            // 插入、修改退货主表信息
            if ( !"".equals( imr.getImrsid() ) && null != imr.getImrsid() ) {
                imr.setModifydate( new Date() );
                imr.setModifyuser( userInfo.getUserId() );
                count = invMatReturnsDao.updateMatReturns( imr );
            } else {
                imr.setOperType( (String) paramMap.get( "rebackType" ) );
                imr.setCreatedate( new Date() );
                imr.setCreateuser( userInfo.getUserId() );
                imr.setModifydate( new Date() );
                imr.setModifyuser( userInfo.getUserId() );
                imr.setDeptid( userInfo.getOrgId() );
                imr.setSiteid( userInfo.getSiteId() );
                count = invMatReturnsDao.insertMatReturns( imr );
            }

            // 插入明细信息
            if ( count > 0 ) {
                imrdvList = new ArrayList<InvMatReturnsDetailVO>();
                paramMap.put( "imrsid", imr.getImrsid() );
                invMatReturnsDetailDao.deleteMatReturnsDetailByImrsid( imr.getImrsid() );
                for ( InvMatReturnsDetailVO imrdv : imrdList ) {
                    // TODO 根据对应的接收单明细，填充对应的退货单明细
                    imrdv.setImtno( (String) paramMap.get( "sheetno" ) );
                    imrdv.setImrsid( imr.getImrsid() );
                    imrdv.setSiteid( imr.getSiteid() );
                    imrdv.setCreateuser( imr.getCreateuser() );
                    imrdv.setModifyuser( imr.getModifyuser() );
                    imrdv.setCreatedate( new Date() );
                    imrdv.setModifydate( new Date() );

                    imrd = (InvMatReturnsDetail) ReflectionUtil.conventBean2Bean( imrdv, new InvMatReturnsDetail() );
                    imrd.setImrsdid( null );

                    imrd.setImtno( imrdv.getImtno() );
                    imrd.setSiteid( imrdv.getSiteid() );
                    imrd.setCreateuser( imrdv.getCreateuser() );
                    imrd.setModifyuser( imrdv.getModifyuser() );
                    imrd.setCreatedate( imrdv.getCreatedate() );
                    imrd.setModifydate( imrdv.getModifydate() );

                    if ( imrd.getReturnQty().compareTo( new BigDecimal( 0 ) ) == 1 ) { // 若果退货数量大于0
                        invMatReturnsDetailDao.insertInvMatReturnsDetail( imrd );
                        imrdv.setImrsdid( imrd.getImrsdid() );
                        imrdv.setImtdid( imrd.getImtdid() );
                        imrdvList.add( imrdv );
                    }
                }
                paramMap.put( "imrdvList", imrdvList );
            }
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvMatReturnsServiceImpl 中的 saveMatReturnsBusi 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
        }
    }

    /**
     * @description:保存流水表信息
     * @author: yuanzh
     * @createDate: 2015-10-10
     * @param userInfo
     * @param imr
     * @param paramMap
     * @return:
     */
    private boolean saveMatTranInfo(UserInfoScope userInfo, InvMatReturns imr, List<InvMatReturnsDetailVO> imrdList,
            Map<String, Object> paramMap, boolean isReceiveAll) throws Exception{
        boolean flag = false;

        try {

            // 先保存业务数据
            saveMatReturnsBusi( userInfo, imr, imrdList, paramMap );

            List<InvMatReturnsDetailVO> imrdvList = (List<InvMatReturnsDetailVO>) paramMap.get( "imrdvList" );
            // 保存交易表映射关系
            saveMatTranInReturns( imr, imrdvList, userInfo );

            /*if ( isReceiveAll ) { // 如果退货之前是完全接收，那么退货完了之后就是未完全接收，需要打开待办
                Map<String, Object> msgMap = new HashMap<String, Object>();
                msgMap.put( "msg", "退货编号为[ " + imr.getImrsno() + " ]的物资产生了接收信息" );

                String pruorderno = (String) paramMap.get( "pruorderno" ); // 采购单的Sheetno
                String sheetid = purOrderService.querySheetIdByFlowNo( pruorderno, userInfo.getSiteId() );

                invMatTranService.autoGenerateMatTran( sheetid, msgMap );
            }*/
            flag = true;
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvMatReturnsServiceImpl 中的 saveMatTranInfo 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
            if(e instanceof InsufficientException){
                throw new InsufficientException();
            }else{
                throw new RuntimeException( "---------InvMatReturnsServiceImpl 中的 saveMatTranInfo 方法抛出异常---------：", e );
            }
        }
        return flag;
    }

    /**
     * @description: 根据物资接收单id判断此接收单是否完全接收
     * @author: 890152
     * @createDate: 2015-10-9
     * @param imtidString 物资接收单ID
     * @return:
     */
    @Override
    public boolean isReceiveAllItem(String sheetNo) {
        boolean result = false;
        int num = invMatTranDetailDao.queryIsReviceAllItemBySheetNo( sheetNo );
        if ( num == 0 ) {
            result = true;
        }
        return result;
    }

    /**
     * @description: 插入库存交易表映射
     * @author: 890166
     * @createDate: 2015-3-18
     * @param imr
     * @param imrdList
     * @param userInfo:
     */
    private void saveMatTranInReturns(InvMatReturns imr, List<InvMatReturnsDetailVO> imrdList,
            UserInfoScope userInfo) throws Exception{
        InvMatTranDetail imtd = null;
        InvMatMapping imm = null;
        String tranTypeString = "strippermaterials"; // 默认为退货、退料
        if ( "refund".equals( imr.getOperType() ) ) {
            tranTypeString = "materialsrefunding";
        }

        InvMatTran imt = new InvMatTran();
        // 将领用信息转换成出库交易信息（主单）
        imt.setSheetno( imr.getImrsno() );
        imt.setTranType( tranTypeString );// 退库materialsrefunding
                                          // ，退货、退料strippermaterials
        imt.setOperuser( userInfo.getUserId() );
        imt.setCheckuser( userInfo.getUserId() );
        imt.setCreateuser( userInfo.getUserId() );
        imt.setCreatedate( new Date() );
        imt.setLotno( new BigDecimal( "1" ) );
        imt.setSiteId( userInfo.getSiteId() );
        imt.setRemark( imr.getReturnReason() );
        int count = invMatTranDao.insertInvMatTran( imt );
        // 若主单信息添加成功，则开始转换子单信息
        if ( count > 0 ) {
            for ( InvMatReturnsDetailVO imrd : imrdList ) {
                imtd = new InvMatTranDetail();
                imm = new InvMatMapping();
                String imtdId = CommonUtil.getUUID();
                imtd.setImtdid( imtdId );
                imtd.setImtid( imt.getImtid() );
                imtd.setItemid( imrd.getItemid() );
                imtd.setBinid( imrd.getBinid() );
                imtd.setWarehouseid( imrd.getWarehouseid() );
                imtd.setLotno( new BigDecimal( "1" ) );
                if ( "refund".equals( imr.getOperType() ) ) {
                    imtd.setInQty( imrd.getReturnQty() );
                    imtd.setCanOutQty( imrd.getReturnQty()  );  //入库数量和可出库数量一样，就是退库数量
                } else {
                    imtd.setOutQty( imrd.getReturnQty() );
                }
                imtd.setInUnitid( imrd.getUnitid() );
                imtd.setOutUnitid( imrd.getUnitid() );
                imtd.setCreatedate( new Date() );
                imtd.setCreateuser( userInfo.getUserId() );
                imtd.setSiteId( userInfo.getSiteId() );
                imtd.setPrice( imrd.getPrice() == null ? new BigDecimal( 0 ) : imrd.getPrice() );
                imtd.setItemcode( imrd.getItemcode() );
                imtd.setPuraId(imrd.getPuraId());
//                imtd.setTax( imrd.getTax() );
//                imtd.setNoTaxPrice( imrd.getNoTaxPrice() );
                imtd.setInvcateid( imrd.getInvcateid() );
                invMatTranDetailDao.insertInvMatTranDetail( imtd );

                imm.setImtdid( imtdId );
                imm.setOutterid( imrd.getImrsid() );
                imm.setTranType( tranTypeString );
                imm.setItemcode( imrd.getItemcode() );
                invMatMappingDao.insertInvMatMapping( imm );
                
                
                //TODO 插入新的流水和映射关系
                if ( "refund".equals( imr.getOperType() ) ) {  //退库
                    //1.查出这张领料单的此项物资的多次发料的明细记录
                    // 领料单id，站点id，物资id，
                      List< InvMatRecipientsDetailVO> recipientsDetailVOs = invMatRecipientsService.queryInvMatRecipListByItem( imr.getImtid(), userInfo.getSiteId() , imrd.getItemid() ,imrd.getWarehouseid());
                      //2.查询这张领料单的此项物资的已退库数量(此处的查询，一定要在此次退库之前查，已经提取到if外面了)
                      BigDecimal curr_returnNum = imrd.getReturnQty();//当前退货数量
                      BigDecimal returnNum = invMatReturnsDao.queryReturnsNumByImaid(imr.getImtid(),imrd.getItemid(),userInfo.getSiteId(),imrd.getWarehouseid());
                      returnNum = returnNum.subtract( curr_returnNum );  //从查询结果中减除此处退库的数量，因为在调用此函数前已经入库了
                      
                      //3.根据此次的退库数量确定价格、税、以及插入几条记录，准备好接口需要的参数bean
                      BigDecimal temp_returnNum = returnNum;
                      BigDecimal temp_curr_returnNum = curr_returnNum;
                     
                      for ( InvMatRecipientsDetailVO invMatRecipientsDetailVO : recipientsDetailVOs ) {
                          if(temp_curr_returnNum.compareTo( new BigDecimal( "0" ) ) > 0){    
                              BigDecimal outNum = invMatRecipientsDetailVO.getOutstockqty();//发料数量
                              InvMatTranDetail newImtd = new InvMatTranDetail();
                              InvMatMapping newImm = new InvMatMapping();
                              
                              String newimtdId = CommonUtil.getUUID();
                              newImtd.setImtdid( newimtdId );
                              newImtd.setImtid( imt.getImtid() );
                              newImtd.setLotno( new BigDecimal( "1" ) );
                              newImtd.setCreatedate( new Date() );
                              newImtd.setCreateuser( userInfo.getUserId() );
                              newImtd.setSiteId( userInfo.getSiteId() );
                              
                              if(temp_returnNum.compareTo( outNum ) >= 0){  //如果是已退货数量大于或等于这个批次的发料数量
                                  temp_returnNum = temp_returnNum.subtract( outNum );
                                  continue ;
                              }else{
                                 if( outNum.subtract( temp_returnNum ).compareTo( temp_curr_returnNum )>=0){
                                     // 当前批次可退库的数量大于等于当前要退库的数量，当前要退库数量作为退库数量
                                     newImtd.setInQty( temp_curr_returnNum );
                                     newImtd.setCanOutQty(  temp_curr_returnNum );  //入库数量和可出库数量一样，就是退库数量
                                     temp_curr_returnNum = new BigDecimal("0");
                                 }else{ // 当前批次可退库的数量小于当前要退库的数量，当前批次可退库数量作退库数量
                                     newImtd.setInQty( outNum.subtract( temp_returnNum ) );
                                     newImtd.setCanOutQty( outNum.subtract( temp_returnNum ) );
                                     temp_curr_returnNum = temp_curr_returnNum.subtract( outNum.subtract( temp_returnNum ) );
                                 }
                                 newImtd.setItemid( invMatRecipientsDetailVO.getItemid() );
                                 newImtd.setBinid( invMatRecipientsDetailVO.getBinid() );
                                 newImtd.setWarehouseid( invMatRecipientsDetailVO.getWarehouseid() );
                                 newImtd.setInUnitid( invMatRecipientsDetailVO.getUnitCode1());
                                 newImtd.setOutUnitid( invMatRecipientsDetailVO.getUnitCode1() );
                                 newImtd.setPrice( invMatRecipientsDetailVO.getPrice() == null ? new BigDecimal( 0 ) : invMatRecipientsDetailVO.getPrice() );
                                 newImtd.setItemcode( invMatRecipientsDetailVO.getItemcode() );
                                 newImtd.setTax( invMatRecipientsDetailVO.getTax() );
                                 newImtd.setNoTaxPrice( invMatRecipientsDetailVO.getNoTaxPrice() );
                                 newImtd.setInvcateid( invMatRecipientsDetailVO.getInvcateid() );
                              }
                              
                              newImm.setImtdid( newimtdId );
                              newImm.setOutterid( imrd.getImrsid() );
                              newImm.setTranType( tranTypeString );
                              newImm.setItemcode( newImtd.getItemcode() );
                              //插入新的流水和映射关系
                              LOG.info( "----------------插入新的流水和映射调用接口realTimeCaluAndUpdateTran----------------------------" );
                              LOG.info( "---------- 物资明细记录：-------------" +newImtd.toString());
                              LOG.info( "-----------映射关系记录：-------------" + newImm.toString() );
                              invRealTimeDataService.insertNewRecAndMap( newImtd, newImm, "3" );
                              
                          }
                      }
                } else {//退货
                    // 找到接收的新的流水表三个价格设置进去, 接口参数imtdid从物资接收列表带过来
                    List<InvMatTranRec> invMatTranRecList =  invMatTranRecService.queryInvMatTranRecByImtdId( imrd.getImtdid() );
                    InvMatTranRec tempInvMatTranRec = invMatTranRecList.get( 0 );
                    imtd.setTax( tempInvMatTranRec.getTax() );
                    imtd.setNoTaxPrice( tempInvMatTranRec.getNoTaxPrice() );
                    imtd.setPrice( tempInvMatTranRec.getPrice() );
                    
                    Map<String, Object> retMap = invRealTimeDataService.insertNewRecAndMap( imtd, imm, "3" );
                    if(!"success".equals( retMap.get( "result" ) )){
                        //throw new RuntimeException( "---------InvMatReturnsServiceImpl 中的 “退货”出库 方法抛出异常---------：");
                        throw new InsufficientException();
                    }
                }
            }
        }
    }

    /**
     * @description: 弹出页面查询可退回物资
     * @author: 890166
     * @createDate: 2015-3-18
     * @param userInfo
     * @param invMatReturnsDetailVO
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatReturnsDetailVO> queryMatReturnsItemList(UserInfoScope userInfo, InvMatReturnsDetailVO imrdv)
            throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatReturnsDetailVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "imtno" );
            page.setSortOrder( "desc" );
        }

        if ( null != imrdv ) {
            page.setParameter( "imtno", imrdv.getImtno() );
            page.setParameter( "purchorderNo", imrdv.getPurchorderNo() );
            page.setParameter( "itemcode", imrdv.getItemcode() );
            page.setParameter( "itemname", imrdv.getItemname() );
            page.setParameter( "cusmodel", imrdv.getCusmodel() );
            page.setParameter( "curReturnQty", imrdv.getCurReturnQty() );
        }
        List<InvMatReturnsDetailVO> ret = invMatReturnsDetailDao.queryMatDetailList( page );
        page.setResults( ret );
        return page;
    }

    @Override
    public Page<InvItemVO> queryMatReturnsDetailListByImtId(UserInfoScope userInfo, String imtid, String imrsid) {
        Page<InvItemVO> page = userInfo.getPage();
        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );
        page.setParameter( "siteid", userInfo.getSiteId() );
        page.setParameter( "imtid", imtid );
        page.setParameter( "imrsid", imrsid );

        List<InvItemVO> ret = invMatReturnsDetailDao.queryMatDetailListByImtid( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description: 根据sheetno找到退库信息
     * @author: yuanzh
     * @createDate: 2015-9-28
     * @param userInfo
     * @param sheetNo
     * @return:
     */
    @Override
    public InvMatReturns queryReturnsBySheetNo(UserInfoScope userInfo, String sheetNo) {
        InvMatReturns imr = null;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "siteId", userInfo.getSiteId() );
        paramMap.put( "sheetNo", sheetNo );
        List<InvMatReturns> ret = invMatReturnsDao.queryReturnsBySheetNo( paramMap );
        if ( null != ret && !ret.isEmpty() ) {
            imr = ret.get( 0 );
        }
        return imr;
    }
}
