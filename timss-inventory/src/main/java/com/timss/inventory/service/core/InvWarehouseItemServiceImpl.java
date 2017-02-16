package com.timss.inventory.service.core;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.timss.inventory.bean.InvBin;
import com.timss.inventory.bean.InvItem;
import com.timss.inventory.bean.InvMatMapping;
import com.timss.inventory.bean.InvMatTran;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.dao.InvBinDao;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvMatMappingDao;
import com.timss.inventory.dao.InvMatTranDao;
import com.timss.inventory.dao.InvMatTranDetailDao;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.service.InvRealTimeDataService;
import com.timss.inventory.service.InvWarehouseItemService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dao.support.LogMapper;
import com.yudean.itc.dto.support.AppLog;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvWarehouseItemServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-17
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvWarehouseItemServiceImpl")
public class InvWarehouseItemServiceImpl implements InvWarehouseItemService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvWarehouseItemDao invWarehouseItemDao;

    @Autowired
    private InvMatTranDao invMatTranDao;

    @Autowired
    private InvMatTranDetailDao invMatTranDetailDao;

    @Autowired
    private InvMatMappingDao invMatMappingDao;

    @Autowired
    private InvItemDao invItemDao;

    @Autowired
    private LogMapper lMapper;

    @Autowired
    private InvBinDao invBinDao;

    @Autowired
    private InvRealTimeDataService invRealTimeDataService;
    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvWarehouseItemServiceImpl.class );

    /**
     * @description:保存到仓库
     * @author: 890166
     * @createDate: 2014-7-17
     * @param userInfo
     * @param iwiv
     * @param paramMap
     * @return
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveInvWarehouseItem(@Operator UserInfoScope userInfo, @LogParam("iwiv") InvWarehouseItemVO iwiv,
            Map<String, Object> paramMap) throws Exception {
        boolean flag = true;
        int count = 0;
        String userId = userInfo.getUserId(); // 用户id
        String siteId = userInfo.getSiteId(); // 站点id

        try {
            if ( null != iwiv ) {
                String itemId = iwiv.getItemid(); // 物资id
                // 每次保存都会保存一条信息到仓库映射表
                InvWarehouseItem iwi = new InvWarehouseItem();
                iwi.setItemid( itemId );
                iwi.setCreatedate( new Date() );
                iwi.setCreateuser( userId );
                iwi.setDefBinid( iwiv.getDefBinid() );
                iwi.setInvcateid( iwiv.getInvcateid() );
                iwi.setManufacturer( iwiv.getManufacturer() == null ? "" : iwiv.getManufacturer() );
                iwi.setSiteId( siteId );
                iwi.setSparecode( iwiv.getSparecode() == null ? "" : iwiv.getSparecode() );
                iwi.setWarehouseid( iwiv.getWarehouseid() );
                iwi.setIssafety( iwiv.getIssafety() );
                iwi.setQtyEconomic( iwiv.getQtyEconomic() );
                iwi.setQtyLowInv( iwiv.getQtyLowInv() );
                iwi.setActive( iwiv.getActive() );
                count = invWarehouseItemDao.insertInvWarehouseItem( iwi );
                // 若添加到仓库成功
                if ( count > 0 ) {
                    // add by yuanzh 2015-08-25 添加仓库记录
                    logTheChange( userInfo, iwi );

                    InvMatTran imt = null;
                    int imtCount = 0;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put( "outterid", iwi.getIwiid() );
                    map.put( "type", "storagematerials" );
                    // 查询交易映射表
                    List<InvMatMapping> immList = invMatMappingDao.queryInvMatMappingInfo( map );
                    if ( null != immList && !immList.isEmpty() ) {// 若交易映射表中存在记录
                    	InvMatMapping imm = immList.get( 0 );
                        // 查询交易明细表
                        List<InvMatTranDetail> imtdList = invMatTranDetailDao.queryInvMatTranDetailByImtdid( imm
                                .getImtdid() );
                        if ( null != imtdList && !imtdList.isEmpty() ) {// 若明细表也存在记录
                            InvMatTranDetail imtd = imtdList.get( 0 );
                            // 找到交易主表信息
                            List<InvMatTran> imtList = invMatTranDao.queryInvMatTranById( imtd.getImtid() );
                            if ( null != imtList && !imtList.isEmpty() ) {
                                imtCount = imtList.size();
                                imt = imtList.get( 0 );
                            }
                        }
                    } else {// 若映射表中不存在记录，则可以判断该物资没有添加过到仓库，在下面将需要进行一次交易记录
                        imt = new InvMatTran();
                        imt.setCheckuser( userId );
                        imt.setCreatedate( new Date() );
                        imt.setCreateuser( userId );
                        imt.setLotno( new BigDecimal( "0" ) );
                        imt.setOperuser( userId );
                        imt.setSiteId( siteId );
                        imt.setTranType( "storagematerials" );
                        imt.setWarehouseid( iwiv.getWarehouseid() );
                        imtCount = invMatTranDao.insertInvMatTran( imt );
                    }
                    // 无论是否存在交易主表信息，下面的代码都会执行，在交易明细表中添加一条入库的记录
                    if ( imtCount > 0 ) {
                        // 根据传递过来的物资id重新找回物资信息
                        List<InvItem> iiList = invItemDao.queryInvItemByItemid( itemId );
                        if ( null != iiList && !iiList.isEmpty() ) {
                            String uuid = CommonUtil.getUUID();
                            InvMatTranDetail imtd = new InvMatTranDetail();
                            InvItem ii = iiList.get( 0 );
                            // 转换物资信息到明细信息中
                            imtd.setImtdid( uuid );
                            imtd.setBinid( iwiv.getDefBinid() );
                            imtd.setCreatedate( new Date() );
                            imtd.setCreateuser( userId );
                            imtd.setImtid( imt.getImtid() );
                            imtd.setInQty( iwiv.getQty() );
                            imtd.setInUnitid( ii.getUnit1() );
                            imtd.setItemcode( ii.getItemcode() );
                            imtd.setItemid( itemId );
                            imtd.setLotno( imt.getLotno() );
                            imtd.setPrice( iwiv.getPrice() );
                            imtd.setSiteId( siteId );
                            imtd.setWarehouseid( imt.getWarehouseid() );
                            imtd.setInvcateid(iwiv.getInvcateid());
                            invMatTranDetailDao.insertInvMatTranDetail( imtd );
                            
                            imtd.setNoTaxPrice(new BigDecimal(0));
                            imtd.setTax(new BigDecimal(0));
                            imtd.setPrice(new BigDecimal(0));
                            
                            // 添加一条记录到映射表中
                            InvMatMapping imm = new InvMatMapping();
                            imm.setImtdid( uuid );
                            imm.setOutterid( iwi.getIwiid() );
                            imm.setTranType( "storagematerials" );
                            invMatMappingDao.insertInvMatMapping( imm );
                            
                            //插入新的流水和映射关系
                            invRealTimeDataService.insertNewRecAndMap( imtd, imm, "3" );
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.debug( "---------InvWarehouseItemServiceImpl 中的 saveInvWarehouseItem 方法抛出异常---------：" + e.getMessage(),e);
            flag = false;
        }
        return flag;
    }

    /**
     * @description:通过itemid查询所在仓库
     * @author: 890166
     * @createDate: 2014-7-18
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvWarehouseItemVO> queryInvWarehouseItem(@Operator UserInfoScope userInfo,
            @LogParam("itemId") String itemId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteid", userInfo.getSiteId() );
        map.put( "itemid", itemId );
        return invWarehouseItemDao.queryInvWarehouseItem( map );
    }

    /**
     * @description: 添加仓库时记录操作
     * @author: user
     * @createDate: 2015-8-25
     * @param userInfo
     * @param iwi:
     */
    private void logTheChange(UserInfoScope userInfo, InvWarehouseItem iwi) {
        String binAddTip = CommonUtil.getProperties( "binAddTip" );

        AppLog al = new AppLog();
        al.setCategoryId( 1 );
        al.setAttr1( iwi.getSiteId() );

        String binName = "";
        if(StringUtils.isNotBlank(iwi.getDefBinid())){
            InvBin ib = invBinDao.queryBinDetail( iwi.getDefBinid() );
            if( ib != null ){
                binName = ib.getBinname();
            }
        }

        String operater = userInfo.getUserName();

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        String dateTime = sdf.format( iwi.getCreatedate() );

        String itemName = iwi.getItemid();
        List<InvItem> iiList = invItemDao.queryInvItemByItemid( iwi.getItemid() );
        if ( null != iiList && !iiList.isEmpty() ) {
            itemName = iiList.get( 0 ).getItemname();
        }

        binAddTip = binAddTip.replace( "{itemName}", itemName ).replace( "{binName}", binName )
                .replace( "{dateTime}", dateTime ).replace( "{operater}", operater );
        al.setDescription( binAddTip );
        al.setOperator( userInfo.getUserId() );
        al.setOperationTime( new Date() );
        lMapper.insert( al );
    }
    
    /**
     * 批量保存物资安全库存
     * @description:
     * @author: 890151
     * @createDate: 2016-9-22
     * @param userInfoScope 
     * @param invWarehouseItemList
     * @return
     * @throws Exception :
     */    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> batchUpdateSafeQty(UserInfoScope userInfoScope, List<InvWarehouseItem> invWarehouseItemList) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        String siteId = userInfoScope.getSiteId();
        for ( InvWarehouseItem invWarehouseItem : invWarehouseItemList ) {
        	if( invWarehouseItem.getItemid() != null && invWarehouseItem.getInvcateid()!=null &&
        		invWarehouseItem.getWarehouseid() != null && invWarehouseItem.getQtyLowInv() != null ){
            	invWarehouseItem.setSiteId(siteId);
    			invWarehouseItemDao.updateSafeQty(invWarehouseItem);
        	}
		}
    	result.put( "result", "success" );
        return result;
    }
}
