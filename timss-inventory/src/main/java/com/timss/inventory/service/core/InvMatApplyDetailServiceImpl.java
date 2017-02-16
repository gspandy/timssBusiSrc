package com.timss.inventory.service.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.asset.bean.AssetBean;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.dao.InvMatApplyDetailDao;
import com.timss.inventory.dao.InvMatTranRecDao;
import com.timss.inventory.service.InvMatApplyDetailService;
import com.timss.inventory.utils.CommonUtil;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatTranRecVO;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.service.PurOrderService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.supplier.bean.SupBaseInfo;
import com.yudean.supplier.service.SupplierForModelService;
import com.yudean.supplier.vo.SupBaseInfoVo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyDetailServiceImpl.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
@Service("InvMatApplyDetailServiceImpl")
public class InvMatApplyDetailServiceImpl implements InvMatApplyDetailService {

    /**
     * 注入Dao
     */
    @Autowired
    private InvMatApplyDetailDao invMatApplyDetailDao;
    
    @Autowired
    private InvMatTranRecDao invMatTranRecDao;

    @Autowired
    SupplierForModelService supplierForModelService;
    
    @Autowired
    PurOrderService purOrderService; 
    /**
     * @description:查询列表信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatApplyDetailVO> queryMatApplyDetailList(@Operator UserInfoScope userInfo,
            @LogParam("imaid") String imaid) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatApplyDetailVO> page = scope.getPage();

        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );

        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "imaid", imaid );

        List<InvMatApplyDetailVO> ret = invMatApplyDetailDao.queryMatApplyDetailList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:弹出窗口中列表数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imad
     * @return
     * @throws Exception:
     */
    @Override
    public Page<InvMatApplyDetailVO> queryConsumingList(@Operator UserInfoScope userInfo,
            @LogParam("imad") InvMatApplyDetailVO imad) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatApplyDetailVO> page = scope.getPage();
        page.setParameter( "siteId", userInfo.getSiteId() );

        if ( null != imad ) {
            page.setParameter( "itemcode", imad.getItemcode() );
            page.setParameter( "itemname", imad.getItemname() );
            page.setParameter( "cusmodel", imad.getCusmodel() );
            page.setParameter( "stockqty", imad.getStockqty() );
            page.setParameter( "precollarqty", imad.getPrecollarqty() );
            page.setParameter( "price", imad.getPrice() );
            page.setParameter( "unit1", imad.getUnit1() );
        }
        List<InvMatApplyDetailVO> ret = invMatApplyDetailDao.queryConsumingList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * 领料列表详细信息
     */
    @Override
    public Page<InvMatApplyDetailVO> queryMatApplyDetailCSList(@Operator UserInfoScope userInfo,
            @LogParam("imaid") String imaid, String embed) throws Exception {
        UserInfoScope scope = userInfo;
        Page<InvMatApplyDetailVO> page = scope.getPage();

        String pageSize = CommonUtil.getProperties( "pageSize" );
        page.setPageSize( Integer.valueOf( pageSize ) );

        page.setParameter( "siteId", userInfo.getSiteId() );
        page.setParameter( "imaid", imaid );
        page.setParameter( "embed", embed );
        List<InvMatApplyDetailVO> ret = invMatApplyDetailDao.queryMatApplyDetailCSList( page );
        page.setResults( ret );
        return page;
    }

    /**
     * @description:通过列表生成物资领料
     * @author: 890166
     * @createDate: 2014-8-26
     * @param codes
     * @return
     * @throws Exception:
     */
    @Override
    public List<InvMatApplyDetailVO> queryItemInfoToMatApply(String codes) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "codes", codes );
        return invMatApplyDetailDao.queryItemInfoToMatApply( map );
    }

    /**
     * @description:查询今年内至今为止同部门的人员所有申请单总金额
     * @author: 890166
     * @createDate: 2014-9-18
     * @param deptId
     * @param nowMonth
     * @param siteId
     * @return
     * @throws Exception:
     */
    @Override
    public Double queryApplyBudget(String userIds, String nowMonth, String siteId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "userIds", userIds );
        map.put( "nowMonth", nowMonth );
        map.put( "siteId", siteId );
        return invMatApplyDetailDao.queryApplyBudget( map );
    }

    /**
     * @description:更新最新库存数量情况
     * @author: 890166
     * @createDate: 2014-10-28
     * @param imaid
     * @throws Exception:
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateNewStockQtyByImaid(UserInfoScope userInfo, String imaid) throws Exception {
        int counter = 0;
        List<InvMatApplyDetailVO> voList = queryMatApplyDetailList( userInfo, imaid ).getResults();
        for ( InvMatApplyDetailVO imadVO : voList ) {
            InvMatApplyDetail imad = new InvMatApplyDetail();
            imad.setImadid( imadVO.getImadid() );
            imad.setStockqty( imadVO.getStockqty() );
            invMatApplyDetailDao.updateInvMatApplyDetail( imad );
            counter++;
        }
        return counter;
    }

    /**
     * @description:查询领料单某个物资详情
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
	@Override
	public InvMatApplyDetailVO queryInvMatApplyDetailByImadId(String imadId) throws Exception {
		InvMatApplyDetailVO invMatApplyDetail = invMatApplyDetailDao.queryInvMatApplyDetailByImadId(imadId);
		return invMatApplyDetail;
	}
	
    /**
     * @description:查询领料单详情某个物资的发料总数
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
	@Override
	public int queryMatApplyDetailSendTotal(String imadId) throws Exception {
		int count = invMatApplyDetailDao.queryMatApplyDetailSendTotal(imadId);
		return count;
	}
	

    /**
     * @description:查询领料单详情某个物资相关联的资产
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    public List<AssetBean> queryRelateAssetByImadId(String imadId) throws Exception{
		List<AssetBean> assetList = invMatApplyDetailDao.queryRelateAssetByImadId(imadId);
		return assetList;
    }
    
    /**
     * @description:查询领料单详情某个物资相关联的发料单
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    public List<InvMatRecipientsDetailVO> queryRelateRecipientsByImadId(String imadId) throws Exception{
		List<InvMatRecipientsDetailVO> recipientsList = invMatApplyDetailDao.queryRelateRecipientsByImadId(imadId);
		return recipientsList;
    }

    /**
     * @description:查询领料单详情某个物资相关联的交易流水信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
	@Override
	public List<InvMatTranRecVO> queryRelateTranRecByImadId(String imadId) throws Exception {
		List<InvMatTranRecVO> tranList = invMatTranRecDao.queryRelateTranRecByImadId(imadId);
		return tranList;
	}
	
    /**
     * @description:查询合同相关信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
    public Map<String,Object> queryPoInfoByPoId(UserInfoScope userInfo, String poId) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
    	PurOrder purOrderInfo = purOrderService.queryPurOrderBySheetId(poId);
    	if(purOrderInfo != null){
			resultMap.put("purchaseDate", purOrderInfo.getCreatedate());
    		//初始化参数purVendor
    		SupBaseInfo supBaseInfo = new SupBaseInfo();
    		supBaseInfo.setCode(purOrderInfo.getCompanyNo());
    		//返回结果
    		List<SupBaseInfoVo> purVendors=null;
    		purVendors = supplierForModelService.querySupplierList(userInfo, supBaseInfo);
    		//调整获取的供应商数据格式，使其满足前端的需求
    		if(purVendors!=null && !purVendors.isEmpty()){
    			SupBaseInfoVo supBaseInfoVo=purVendors.get(0);
    			resultMap.put("companyNo", supBaseInfoVo.getCode());
    			resultMap.put("companyContact", supBaseInfoVo.getSpiName());
    			resultMap.put("companyTel", supBaseInfoVo.getSpiCellphone());
    			resultMap.put("companyName", supBaseInfoVo.getName());
    		}
    	}
		return resultMap;
    }

}
