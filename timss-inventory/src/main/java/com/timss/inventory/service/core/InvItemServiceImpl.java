package com.timss.inventory.service.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvItem;
import com.timss.inventory.bean.InvUnit;
import com.timss.inventory.bean.InvWarehouseItem;
import com.timss.inventory.dao.InvItemDao;
import com.timss.inventory.dao.InvUnitDao;
import com.timss.inventory.dao.InvWarehouseItemDao;
import com.timss.inventory.service.InvItemService;
import com.timss.inventory.service.InvWarehouseItemService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvSafetyStockVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvItemServiceImpl")
public class InvItemServiceImpl implements InvItemService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvItemDao invItemDao;

    @Autowired
    private InvWarehouseItemDao invWarehouseItemDao;

    @Autowired
    private InvUnitDao invUnitDao;

    @Autowired
    private HomepageService homepageService;
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private InvWarehouseItemService invWarehouseItemService;
    

    /**
     * log4j输出
     */
    private static final Logger LOG = Logger.getLogger( InvItemServiceImpl.class );

    /**
     * @description:查詢物資列表
     * @author: 890166
     * @createDate: 2014-7-11
     * @param userInfo
     * @param iiv
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvItemVO> queryItemsList(@Operator UserInfoScope userInfo, @LogParam("iiv") InvItemVO iiv)
            throws Exception {
        int pageSize = Integer.valueOf( userInfo.getParam( "rows" ) == null ? "15" : userInfo.getParam( "rows" ) );
        int pageNo = Integer.valueOf( userInfo.getParam( "page" ) == null ? "1" : userInfo.getParam( "page" ) );

        UserInfoScope scope = userInfo;
        String active = userInfo.getParam( "active" );
        Page<InvItemVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "active", active );
        page.setPageNo( pageNo );
        page.setPageSize( pageSize );

        String sort = String.valueOf( scope.getParam( "sort" ) == null ? "" : scope.getParam( "sort" ) );
        String order = String.valueOf( scope.getParam( "order" ) == null ? "" : scope.getParam( "order" ) );
        if ( !"".equals( sort ) && !"".equals( order ) ) {
            page.setSortKey( sort );
            page.setSortOrder( order );
        } else {
            page.setSortKey( "itemcode" );
            page.setSortOrder( "asc" );
        }

        if ( null != iiv ) {
            page.setParameter( "itemcode", iiv.getItemcode() );
            page.setParameter( "itemname", iiv.getItemname() );
            page.setParameter( "cusmodel", iiv.getCusmodel() );
            page.setParameter( "stockqty", iiv.getStockqty() == null ? "" : String.valueOf( iiv.getStockqty() ) );
            page.setParameter( "nowqty", iiv.getNowqty() == null ? "" : String.valueOf( iiv.getNowqty() ) );
            page.setParameter( "qtyLowInv", iiv.getQtyLowInv() == null ? "" : String.valueOf( iiv.getQtyLowInv() ) );
            page.setParameter( "unitname", iiv.getUnitname() );
            page.setParameter( "unit2", iiv.getUnit2() );
            page.setParameter( "price", iiv.getPrice() == null ? "" : String.valueOf( iiv.getPrice() ) );
            page.setParameter( "noTaxPrice", iiv.getNoTaxPrice() == null ? "" : String.valueOf( iiv.getNoTaxPrice() ) );
            page.setParameter( "bin", iiv.getBin() );
            page.setParameter( "ishis", iiv.getIshis() );
            page.setParameter( "cateType", iiv.getCateType() );
            page.setParameter( "warehouse", iiv.getWarehouse() );
            if ( null != iiv.getWarehouseid() && !"".equals( iiv.getWarehouseid() ) ) {
                page.setParameter( "warehouseid", iiv.getWarehouseid() );
            }
            if ( null != iiv.getCateId() && iiv.getCateId().indexOf( "ICI" ) > -1 ) {
                page.setParameter( "cateId", iiv.getCateId() );
            }
            if ( null != iiv.getEmbbed() && !"".equals( iiv.getEmbbed() ) ) {
                page.setParameter( "embbed", iiv.getEmbbed() );
            }
            if ( null != iiv.getOpentype() && !"".equals( iiv.getOpentype() ) ) {
                page.setParameter( "opentype", iiv.getOpentype() );
            }
            if ( null != iiv.getIsspare() && !"".equals( iiv.getIsspare() ) ) {
                page.setParameter( "isspare", iiv.getIsspare() );
            }
            if ( null != iiv.getInvmatapplyStatus() && !"".equals( iiv.getInvmatapplyStatus() ) ) {
                page.setParameter( "invmatapply", iiv.getInvmatapplyStatus() );
            }
        }
        List<InvItemVO> ret = invItemDao.queryItemsList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:通过id查询site
     * @author: 890166
     * @createDate: 2014-7-11
     * @param siteId
     * @return
     * @throws Exception :
     */
    @Override
    public List<Map<String, String>> querySiteById(String siteId) throws Exception {
        return invItemDao.querySiteById( siteId );
    }

    /**
     * @description:查询InvItem详细信息
     * @author: 890166
     * @createDate: 2014-7-14
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvItemVO> queryInvItemDetail(Map<String, Object> map) throws Exception {
        return invItemDao.queryInvItemDetail( map );
    }

    /**
     * @description:查询详细主项目信息
     * @author: 890166
     * @createDate: 2014-7-16
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvItemVO> queryInvMainItemDetail(@Operator UserInfoScope userInfo, @LogParam("itemId") String itemId)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "itemId", itemId );
        return invItemDao.queryInvMainItemDetail( map );
    }

    /**
     * @description:更新主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public InvItem updateInvItemInfo(InvItem ii) throws Exception {
        InvItem newInvItem = null;
        // 做更新操作
        int count = invItemDao.updateInvItemInfo( ii );
        if ( count > 0 ) {
            newInvItem = ii;
        }
        return newInvItem;
    }

    /**
     * @description:插入主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return
     * @throws Exception :
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public InvItem insertInvItemInfo(InvItem ii) throws Exception {
        InvItem newInvItem = null;
        // 做更新操作
        int count = invItemDao.insertInvItemInfo( ii );
        if ( count > 0 ) {
            newInvItem = ii;
        }
        return newInvItem;
    }

    /**
     * 保存主项目
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveItem(@Operator UserInfoScope userInfo, @LogParam("ii") InvItem ii,
            List<InvWarehouseItemVO> iwiList, Map<String, Object> paramMap) throws Exception {
        boolean flag = true; // 默认值为false
        Map<String, Object> reMap = new HashMap<String, Object>();
        String itemId = null;
        String isspare = null;
        String siteId = userInfo.getSiteId();
        try {
            if ( "".equals( ii.getItemid() ) ) {
                ii.setItemid( null );
                ii.setItemcode( null );
            }

            // 2015-5-28 若用户自己填写单位的话就直接帮他生成一个新的单位 begin
            if ( !"".equals( ii.getAttr1() ) ) {
                String unitId = null;
                InvUnit iu = new InvUnit();

                iu.setSiteId( userInfo.getSiteId() );
                iu.setUnitname( ii.getAttr1() );
                List<InvUnit> iuList = invUnitDao.queryUnitByUnitname( iu );

                if ( iuList.isEmpty() ) {
                    iu.setActive( "Y" );
                    iu.setDescriptions( ii.getAttr1() );
                    iu.setCreatedate( new Date() );
                    iu.setCreateuser( userInfo.getUserId() );
                    iu.setModifydate( new Date() );
                    iu.setModifyuser( userInfo.getUserId() );
                    invUnitDao.insertUnitInfo( iu );
                    unitId = iu.getUnitid();
                } else {
                    unitId = iuList.get( 0 ).getUnitid();
                }
                ii.setUnit1( unitId );
            }
            // 2015-5-28 end

            InvItem newIi = null;
            if ( null != ii.getItemid() ) {
                ii.setModifyuser( userInfo.getUserId() );
                ii.setModifydate( new Date() );
                newIi = this.updateInvItemInfo( ii );
            } else {
                ii.setSiteId( siteId );
                ii.setCreateuser( userInfo.getUserId() );
                ii.setCreatedate( new Date() );
                newIi = this.insertInvItemInfo( ii );
            }
            // 判断主单信息不为空
            if ( null != newIi ) {
                itemId = newIi.getItemid();
                isspare = newIi.getIsspare();
                if ( null != iwiList && !iwiList.isEmpty() ) {
                    InvWarehouseItem iwi = null;
                    for ( InvWarehouseItemVO iwiv : iwiList ) {
                        iwi = new InvWarehouseItem();
                        String iwiid = iwiv.getIwiid();
                        if(!"".equals(iwiid)){                        	
                        	iwi.setIwiid( iwiv.getIwiid() );
                        	iwi.setActive( iwiv.getActive() );
                        	iwi.setQtyLowInv( iwiv.getQtyLowInv() );
                        	iwi.setQtyEconomic( iwiv.getQtyEconomic() );
                        	iwi.setIssafety( iwiv.getIssafety() );
                        	if(StringUtils.isBlank(iwiv.getDefBinid())){
                            	iwi.setDefBinid( "-1" );
                        	}
                        	else{
                            	iwi.setDefBinid( iwiv.getDefBinid() );
                        	}
                        	iwi.setInvcateid(iwiv.getInvcateid());
                        	invWarehouseItemDao.updateInvWarehouseItem( iwi );
                        }else{
                        	iwiv.setItemid(itemId);
                            Map<String, Object> pMap = new HashMap<String, Object>(); 
                            pMap.put( "item", itemId );
                            flag = invWarehouseItemService.saveInvWarehouseItem( userInfo, iwiv, pMap );
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.info( "--------------------------------------------" );
            LOG.info( "- InvItemServiceImpl 中的 saveItem 方法抛出异常：" + e.getMessage() + " - " );
            LOG.info( "--------------------------------------------" );
            flag = false;
        }
        reMap.put( "flag", flag );
        reMap.put( "itemid", itemId );
        reMap.put( "isspare", isspare );
        return reMap;
    }

    /**
     * @description:查询到货物资列表
     * @author: 890166
     * @createDate: 2014-7-24
     * @param userInfo
     * @param ii
     * @return
     * @throws Exception :
     */
    @Override
    public Page<InvItemVO> queryArrivalItem(@Operator UserInfoScope userInfo, @LogParam("iiv") InvItemVO iiv,
            Map<String, Object> paramMap) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvItemVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );

        String pruorderno = String.valueOf( paramMap.get( "pruordernoVal" ) );
        StringBuilder paramOrderNo = new StringBuilder();
        String paramNo = null;
        String[] orderNos = pruorderno.split( "," );

        if ( orderNos.length > 1 ) {
            for ( String no : orderNos ) {
                paramOrderNo.append( "'" ).append( no ).append( "'," );
            }
            paramNo = paramOrderNo.toString().substring( 0, paramOrderNo.toString().length() - 1 );
        } else {
            paramNo = "'" + pruorderno + "'";
        }

        page.setParameter( "pruorderno", paramNo );
        page.setParameter( "warehouseid", paramMap.get( "warehouseVal" ) );

        if ( null != iiv ) {
            page.setParameter( "itemid", iiv.getItemid() );
            page.setParameter( "itemcode", iiv.getItemcode() );
            page.setParameter( "itemname", iiv.getItemname() );
            page.setParameter( "cusmodel", iiv.getCusmodel() );
            page.setParameter( "unit1", iiv.getUnit1() );
            page.setParameter( "itemnum", iiv.getItemnum() );
            page.setParameter( "stockqty", iiv.getStockqty() );
            page.setParameter( "price", iiv.getPrice() );
            page.setParameter( "bin", iiv.getBin() );
        }
        List<InvItemVO> ret = invItemDao.queryArrivalItem( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:查询物资详细信息
     * @author: 890166
     * @createDate: 2014-8-6
     * @param itemid
     * @return
     * @throws Exception :
     */
    @Override
    public List<InvItemVO> queryItemInfo(@Operator UserInfoScope userInfo, @LogParam("itemcode") String itemcode,
            @LogParam("warehouseid") String warehouseid, @LogParam("invcateid") String invcateid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemcode", itemcode );
        map.put( "siteid", userInfo.getSiteId() );
        map.put( "warehouseid", warehouseid );
        map.put( "cateid", invcateid );
        return invItemDao.queryItemInfo( map );
    }

    @Override
    public Page<InvItemVO> queryInvItemSafetyStock(Page<InvItemVO> page) throws Exception {
        List<InvItemVO> rst = invItemDao.queryInvItemSafetyStock( page );
        page.setResults( rst );
        return page;
    }

    @Override
    public int checkInvItemSafetyStock(UserInfo userInfo) throws Exception {
        int num = invItemDao.queryInvItemSafetyStockNum( userInfo.getSiteId() );

        // 查询仓管员
        String imtRoleId = CommonUtil.getProperties( "imtRoleId" );
        List<SecureUser> us = new ArrayList<SecureUser>();
        List<String> userIds = new ArrayList<String>();
        String[] imtRoleIds = imtRoleId.split( "," );
        IAuthorizationManager am = (SecurityBeanHelper.getInstance()).getBean( IAuthorizationManager.class );
        for ( String roleId : imtRoleIds ) {
            String[] siteArr = roleId.split( "_" );
            if ( siteArr[0].equals( userInfo.getSiteId() ) ) {
                us = am.retriveUsersWithSpecificRole( roleId, null, true, true );
                if ( us.isEmpty() ) {
                    us = am.retriveUsersWithSpecificGroup( roleId, null, true, true );
                }
                if ( !us.isEmpty() ) {
                    for ( SecureUser tmp : us ) {
                        userIds.add( tmp.getId() );
                    }
                }
                break;
            }
        }

        HomepageWorkTask task = new HomepageWorkTask();
        if ( num > 0 ) {
            // 创建待办
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );
            String flowNo = "SS" + sdf.format( new Date() ) + String.valueOf( 100 + new Random().nextInt( 900 ) );
            task.setFlow( flowNo );
            task.setProcessInstId( "invitem-safetystock-todo_" + userInfo.getSiteId() );
            task.setName( "部分物资库存数量低，需要进行采购" );
            task.setStatusName( "安全库存提醒" );
            task.setTypeName( "安全库存提醒" );
            task.setUrl( "inventory/invitem/invItemSafetyStock.do" );
            homepageService.createNoticeWithOutWorkflow( task, userIds, userInfo, "INFO" );// 创建并转派给仓管员
        } else {
            homepageService.deleteNoticeWithOutWorkflow( "invitem-safetystock-todo_" + userInfo.getSiteId(), userInfo );
        }
        return num;
    }

    /**
     * @description:转为历史库存
     * @author: fengzt
     * @createDate: 2014年10月20日
     * @param itemCodes
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> updateTurnToHistory(String itemCodes) {
        if ( StringUtils.isNotBlank( itemCodes ) ) {
            String[] itemArr = itemCodes.split( "," );

            List<String> itemCodeList = new ArrayList<String>();
            Map<String, Object> map = new HashMap<String, Object>();
            StringBuilder itemBuffer = new StringBuilder();
            
            boolean isZero =  true;
            String msg = "";
            int count = 0;
            for ( String itemCode : itemArr ) {
                // 1.对于当前库存数量不为0的物资不可以转为历史库存物资。by zhuw
            	String hasNum = curItemNum( itemCode );
                if ( "N".equals(hasNum) ) {
                	isZero = false;
                	msg = "请检查所选物资，至少一项当前库存数量不为0";
                	break;
                } else if( "-1".equals(hasNum) ){
                	isZero = false;
                	msg = "所选物资包含未初始化数据的物资，请联系系统管理员";
                	break;
                }
            }
            
            //当前库存数量为0则继续做验证
            if(isZero){
	            for ( String itemCode : itemArr ) {
	                // 2.对于那些正在采购中，待入库，待出库的物资不可以转为历史库存物资。
	                if ( isBusyInvItem( itemCode ) ) {
	                    itemBuffer.append( itemCode );
	                }
	                itemCodeList.add( itemCode );
	            }
	            if ( StringUtils.isBlank( itemBuffer ) ) {
	                Map<String, Object> params = new HashMap<String, Object>();
	                // 1 为历史 0 为正常
	                params.put( "isHis", 1 );
	                params.put( "list", itemCodeList );
	                count = invItemDao.batchUpdateInvItemHistory( params );
	            } else {
	                map.put( "isBusyCode", itemBuffer.toString() );
	            }
            }
            map.put( "isZero", isZero );
            map.put( "msg", msg );
            map.put( "count", count );
            return map;
        }

        return null;
    }
    
    /**
     * @description:检查--当前库存数量不为0的物资不可以转为历史库存物资
     * @author: 890191
     * @createDate: 2016年7月22日
     * @param itemCode
     * @return:boolean
     */
    private String curItemNum(String itemCode) {
        String flag = "Y";
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "itemCode", itemCode );

        int counter = invItemDao.queryCurItemNum( map );
        if ( counter > 0 ) {
            flag = "N";
        }else if( counter<0 ){
        	flag = "-1";
        }
        return flag;
    }

    /**
     * @description:检查--在采购中，待入库，待出库的物资不可以转为历史库存物资
     * @author: 890167
     * @createDate: 2014年10月20日
     * @param itemCode
     * @return:boolean
     */
    private boolean isBusyInvItem(String itemCode) {
        boolean flag = false;
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "siteId", userInfo.getSiteId() );
        map.put( "itemCode", itemCode );

        int counter = invItemDao.queryIsBusyInvItem( map );
        if ( counter > 0 ) {
            flag = true;
        }
        return flag;
    }

    /**
     * @description:转为物资库存
     * @author: fengzt
     * @createDate: 2014年10月20日
     * @param itemCodes
     * @return:Map<String, Object>
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> updateTurnToWuzi(String itemCodes) {
        if ( StringUtils.isNotBlank( itemCodes ) ) {
            String[] itemArr = itemCodes.split( "," );

            List<String> itemCodeList = new ArrayList<String>();
            Map<String, Object> map = new HashMap<String, Object>();

            for ( String itemCode : itemArr ) {
                itemCodeList.add( itemCode );
            }

            Map<String, Object> params = new HashMap<String, Object>();
            // 1 为历史 0 为正常
            params.put( "isHis", 0 );
            params.put( "list", itemCodeList );
            int count = invItemDao.batchUpdateInvItemHistory( params );
            map.put( "count", count );
            return map;
        }
        return null;
    }

    /**
     * @description: 查询当前安全库存给首页展示
     * @author: 890166
     * @createDate: 2015-2-15
     * @param siteId
     * @return:
     */
    @Override
    public List<InvSafetyStockVO> querySafetyStockNow(String siteId) {
        return invItemDao.querySafetyStockNow( siteId );
    }
    
    @Override
    public String getSheetIdByImtId(String imtId,String type) {
        String result = "";
        if("receivingmaterial".equals( type )){//物资接收
            result = imtId;
        }else if("pickingmaterials".equals( type )){//根据imtid（如IMI3369）查出imrid
            result = invItemDao.queryImridByImtId(imtId);
        }else if("materialscounting".equals( type )){//物资盘点
            result = invItemDao.queryIstidByImtId(imtId);
        }else if("materialsrefunding".equals( type )){//物资退库
            result = invItemDao.queryImrsidByImtId( imtId, type );
        }else if("strippermaterials".equals( type )){//物资退货
            result = invItemDao.queryImrsidByImtId( imtId, type );
        }else if("shiftmaterialsOut".equals( type )  || "shiftmaterialsIn".equals( type )){//物资移库
            result = invItemDao.queryImtidByImtId( imtId );
        }else if("acceptancematerial".equals( type )){//物资验收
            result = invItemDao.queryInacidByImtId(imtId);
        }
        return result;
    }
    
    /**
     * 
     * @description:通过itemcode,warehouseid查询启用的分类
     * @author: zhuw
     * @createDate: 2016-7-25
     * @param itemcode
     * @param warehouseid
     * @return
     */
	@Override
	public List<InvItemVO> queryInvCategory(String itemcode, String warehouseid)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put( "itemcode", itemcode );
        map.put( "warehouseid", warehouseid );
        return invItemDao.queryInvCategory( map );
	}
}
