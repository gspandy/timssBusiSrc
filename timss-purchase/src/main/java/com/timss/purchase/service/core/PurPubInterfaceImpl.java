package com.timss.purchase.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatApplyVO;
import com.timss.purchase.bean.PurInvoiceBean;
import com.timss.purchase.bean.PurInvoiceItemBean;
import com.timss.purchase.dao.PurPubInterfaceDao;
import com.timss.purchase.service.PurPubInterface;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.vo.PurApplyStockItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurInvoiceAssetVo;
import com.timss.purchase.vo.PurOrderVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: 采购公开接口
 * @description: 采购公开接口
 * @company: gdyd
 * @className: PurPubInterfaceImpl.java
 * @author: yuanzh
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
@Service("purPubInterface")
public class PurPubInterfaceImpl implements PurPubInterface {

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private PurPubInterfaceDao purPubInterfaceDao;

  /**
   * @description: 使用ihint来查询采购合同信息
   * @author: yuanzh
   * @createDate: 2015-9-22
   * @param inputStr
   * @return:
   */
  @Override
  public List<PurOrderVO> queryPurOrderByIHint(String inputStr) {
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("keyWord", inputStr);
    paramMap.put("siteid", userInfo.getSiteId());
    return purPubInterfaceDao.queryPurOrderByIHint(paramMap);
  }

  /**
   * @description: 根据采购合同id查询采购合同主单信息
   * @author: yuanzh
   * @createDate: 2015-9-22
   * @param sheetId
   * @return:
   */
  @Override
  public PurOrderVO queryPurOrderVOBySheetId(String sheetId) {
    PurOrderVO pov = null;

    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("sheetId", sheetId);
    paramMap.put("siteid", userInfo.getSiteId());
    List<PurOrderVO> povList = purPubInterfaceDao.queryPurOrderVOBySheetId(paramMap);

    if (null != povList && !povList.isEmpty()) {
      pov = povList.get(0);
    }
    return pov;
  }

  /**
   * @description: 根据采购和同id查询物资信息
   * @author: yuanzh
   * @createDate: 2015-9-22
   * @param sheetId
   * @return:
   */
  @Override
  public List<PurInvoiceAssetVo> queryPurInvoiceAssetVoBySheetId(String sheetId) {
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();

    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("sheetId", sheetId);
    paramMap.put("siteid", userInfo.getSiteId());
    return purPubInterfaceDao.queryPurInvoiceAssetVoBySheetId(paramMap);
  }

  /**
   * @description: 根据发票信息更新交易表中入库价格
   * @author: yuanzh
   * @createDate: 2015-9-23
   * @param piibList
   * @return:
   */
  @Override
  public boolean updateTranDetailByInvoice(List<PurInvoiceItemBean> piibList, String purOrderId) {

    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();

    boolean flag = false;
    int counter = 0;
    if (null != piibList && !piibList.isEmpty()) {
      for (PurInvoiceItemBean piib : piibList) {
        purPubInterfaceDao.updateTranDetailByInvoice(piib, purOrderId, userInfo.getSiteId());
        counter++;
      }
      if (counter == piibList.size()) {
        flag = true;
      }
    }
    return flag;
  }

  /**
   * 
   * @description:根据采购和同id查询物资信息(datagrid列表)
   * @author: fengzt
   * @createDate: 2015年9月23日
   * @param paramsPage
   * @return:List<PurInvoiceAssetVo>
   */
  @Override
  public List<PurInvoiceAssetVo> queryWuziByContractId(Page<PurInvoiceAssetVo> paramsPage) {
    return purPubInterfaceDao.queryWuziByContractId(paramsPage);
  }

  /**
   * @description:通过采购合同id查询所有关联的发票
   * @author: yuanzh
   * @createDate: 2015-9-24
   * @param contractId
   * @return:
   */
  @Override
  public List<PurInvoiceBean> queryInvoiceRelationByContractId(String contractId) {
    UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("siteId", userInfo.getSiteId());
    paramMap.put("contractId", contractId);
    return purPubInterfaceDao.queryInvoiceRelationByContractId(paramMap);
  }

  /**
   * @description: 如果已接收数量等于采购数量，且有关联的未报账的发票记录，系统发送站内信息给这些发票的录入人
   * @author: yuanzh
   * @createDate: 2015-9-29
   * @param imtid
   *          物资接收id
   * @param siteId
   *          站点id
   * @return:
   */
  @Override
  public List<PurInvoiceBean> queryInvoice2SendNotice(String sheetNo, String siteId) {
    return purPubInterfaceDao.queryInvoice2SendNotice(sheetNo, siteId);
  }

  /**
   * @description:已入库物资列表
   * @author: user
   * @createDate: 2016-1-22
   * @param userInfo
   * @param sheetId
   * @return:
   */
  @Override
  public Page<PurApplyStockItemVO> queryPurApplyStockItemList(UserInfoScope userInfo, String sheetId) {
    UserInfoScope scope = userInfo;
    Page<PurApplyStockItemVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", userInfo.getSiteId());
    List<PurApplyStockItemVO> ret = purPubInterfaceDao.queryPurApplyStockItemList(page);
    page.setResults(ret);
    return page;
  }
  
  /**
   * @description:执行情况列表
   * @author: 890162
   * @createDate: 2016-7-1
   * @param userInfo
   * @param sheetId
   * @return:
   */
  @Override
  public Page<PurApplyStockItemVO> queryPurApplyImplemetationStatusList(UserInfoScope userInfo, String sheetId) {
    UserInfoScope scope = userInfo;
    Page<PurApplyStockItemVO> page = scope.getPage();
    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", userInfo.getSiteId());
    List<PurApplyStockItemVO> ret = purPubInterfaceDao.queryPurApplyImplemetationStatusList(page);
    page.setResults(ret);
    return page;
  }
  
  /**
   * @description:执行情况列表
   * @author: 890162
   * @createDate: 2016-7-11
   * @param userInfo
   * @param sheetId
   * @return:
   */
  @Override
  public List<PurApplyStockItemVO> queryPurApplyImplemetationStatusListAsList(UserInfoScope userInfo, String sheetId) {
    Page<PurApplyStockItemVO> page = new Page<PurApplyStockItemVO>();
    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", userInfo.getSiteId());
    List<PurApplyStockItemVO> list = purPubInterfaceDao.queryPurApplyImplemetationStatusList(page);
    return list;
  }

  /**
   * @description:相关领料列表
   * @author: 890162
   * @createDate: 2016-7-7
   * @param userInfo
   * @param sheetId
   * @return:
   */
  @Override
  public Page<InvMatApplyVO> queryRelateMatApplyList(UserInfoScope userInfo, String sheetId) {
    UserInfoScope scope = userInfo;
    Page<InvMatApplyVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", userInfo.getSiteId());
    List<InvMatApplyVO> ret = purPubInterfaceDao.queryRelateMatApplyList(page);
    page.setResults(ret);
    return page;
  }

  @Override
  public Boolean isContainsInQty(UserInfoScope userInfo,String sheetId) {
     Boolean result = false;
     Page<PurApplyStockItemVO> page = new Page<PurApplyStockItemVO>();
     page.setParameter("sheetId", sheetId);
     page.setParameter("siteId", userInfo.getSiteId());
     List<PurApplyStockItemVO> ret = purPubInterfaceDao.queryPurApplyImplemetationStatusList(page);
     for ( PurApplyStockItemVO purApplyStockItemVO : ret ) {
         if(null!=purApplyStockItemVO.getInvNum()){
             if(0<Double.valueOf(purApplyStockItemVO.getInvNum())){
                 result = true;
             }
         }
     }
     return result;
  }
  
  /**
   * @description:查询采购申请列表
   * @author: 890162
   * @createDate: 2016-7-8
   * @param userinfo
   * @return Page<PurApplyVO>
   * @throws Exception
   */
  @Override
  public Page<PurApplyVO> queryPurApplyList(UserInfo userinfo) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurApplyVO> page = scope.getPage();
    //创建人为当前用户
    page.setParameter( "creator", userinfo.getUserId() );
    Map<String, String[]> params = scope.getParamMap();
    Map<String, String> propertyColumnMap = MapHelper.getPropertyColumnMap( "PurApplyVOMap", "purchase","PurPubInterfaceDao" );
    if ( params.containsKey( "search" ) ) {
        String fuzzySearchParams = scope.getParam( "search" );
        Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
        fuzzyParams = MapHelper.fromPropertyToColumnMap( (HashMap<String,Object>) fuzzyParams,propertyColumnMap);
        if(fuzzyParams.containsKey( "createdate" )){
            String createdate = String.valueOf( fuzzyParams.get( "createdate" ) );
            fuzzyParams.remove( "createdate" );
            fuzzyParams.put( "createdatestr", createdate );
        }
        page.setFuzzyParams( fuzzyParams );
    }
    List<PurApplyVO> ret = purPubInterfaceDao.queryPurApplyListForMatApply(page);
    page.setResults(ret);
    return page;
  }
  @Override
  public List<InvItemVO> queryPurApplyItemListByIdInIMA(String sheetId,String siteid) throws Exception {
    List<InvItemVO> list = new ArrayList<InvItemVO>(0);
    list = purPubInterfaceDao.queryPurApplyItemListByIdInIMA( sheetId, siteid );    
    return list;
  }
}
