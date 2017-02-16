package com.timss.purchase.service.abstr;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvWarehouse;
import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.bean.PurAttachMapping;
import com.timss.purchase.bean.PurHhcMaxnum;
import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.bean.PurPaSyncHhc;
import com.timss.purchase.bean.PurProcessMapping;
import com.timss.purchase.dao.PurApplyDao;
import com.timss.purchase.dao.PurProcessMappingDao;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.service.PurAttachMappingService;
import com.timss.purchase.service.PurHhcMaxnumService;
import com.timss.purchase.service.PurPaSyncHhcService;
import com.timss.purchase.service.PurProcessMappingService;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.vo.EamItemVO;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurApplyVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.homepage.bean.WorktaskBean;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.LogParam;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.code.ActiveStatus;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.itc.webservice.HhcWebEamItemService;
import com.yudean.itc.webservice.HhcWebEamPrService;
import com.yudean.itc.webservice.bean.EamItem;
import com.yudean.itc.webservice.bean.EamPR;
import com.yudean.itc.webservice.bean.EamPRCom;
import com.yudean.itc.webservice.bean.EamPRLine;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyServiceDefImpl.java
 * @author: user
 * @createDate: 2016-2-2
 * @updateUser: user
 * @version: 1.0
 */
public abstract class PurApplyServiceDefImpl implements PurApplyService {
  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurApplyServiceDefImpl.class);

  /**
   * 采购申请Dao
   */
  @Autowired
  private PurApplyDao purApplyDao;

  /**
   * 上传附件
   */
  @Autowired
  private PurAttachMappingService purAttachMappingService;
  @Autowired
  private AttachmentMapper attachmentMapper;

  /**
   * 商务网接口调用
   */
  @Autowired
  private HhcWebEamItemService eamItemService;// 物资主项目接口
  @Autowired
  private HhcWebEamPrService eamPrService;// 采购订单接口
  @Autowired
  private PurPaSyncHhcService purPaSyncHhcService;
  @Autowired
  private PurProcessMappingDao purProcessMappingDao;
  @Autowired
  private PurHhcMaxnumService purHhcMaxnumService;

  /**
   * 系统接口
   */
  @Autowired
  private ItcMvcService itcMvcService;
  @Autowired
  private HomepageService homepageService;

  /**
   * 工作流相关接口
   */
  @Autowired
  private WorkflowService workflowService;
  @Autowired
  private PurProcessMappingService purProcessMappingService;

  /**
   * 查询采购申请列表
   */
  @Override
  public Page<PurApplyVO> queryPurApply(UserInfo userinfo, PurApplyVO purApplyVO) throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;

    Page<PurApplyVO> page = scope.getPage();
    page.setParameter("siteid", userinfo.getSiteId());
    page.setParameter("userId", userinfo.getUserId());

    String sort = String.valueOf(scope.getParam("sort") == null ? "" : scope.getParam("sort"));
    String search = String.valueOf(scope.getParam("search") == null ? "" : scope.getParam( "search" ) );
    String searchStr = String.valueOf( scope.getParam("searchStr") == null ? "" : scope.getParam( "searchStr" ) );
    sort = sort.replace( "totalcost", " cast (totalcost as decimal )" );
    String order = String.valueOf(scope.getParam("order") == null ? "" : scope.getParam("order"));
    if (!"".equals(sort) && !"".equals(order)) {
      page.setSortKey(sort);
      page.setSortOrder(order);
    } else {
      page.setSortKey("createdatesec");
      page.setSortOrder("desc");
    }
    if ( StringUtils.isNotBlank( searchStr ) ) {
        Map<String, Object> spParams = MapHelper.jsonToHashMap( searchStr );
        if ( spParams.containsKey( "item" ) ) {
            page.setParameter( "item", spParams.get( "item" ) );
        }
    }
    if ( StringUtils.isNotBlank( search ) ) {
        Map<String, Object> fuzzyParams = MapHelper.jsonToHashMap( search );
        if ( fuzzyParams.containsKey( "curHandler" ) ) {
            fuzzyParams.put( "cur_handler", fuzzyParams.get( "curHandler" ) );
            fuzzyParams.remove( "curHandler" );
        }
        if ( fuzzyParams.containsKey( "curLink" ) ) {
            fuzzyParams.put( "cur_link", fuzzyParams.get( "curLink" ) );
            fuzzyParams.remove( "curLink" );
        }
        if ( fuzzyParams.containsKey( "_projectAscription" ) ) {
            fuzzyParams.put( "project_Ascription", fuzzyParams.get( "_projectAscription" ) );
            fuzzyParams.remove( "_projectAscription" );
        }
        page.setFuzzyParams( fuzzyParams );
    }
    List<PurApplyVO> ret = purApplyDao.queryPurApply(page);
    page.setResults(ret);
    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 queryPurApply 方法，" + " 操作人：" + userinfo.getUserName()
        + " | 时间为：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " | 站点编码为："
        + userinfo.getSiteId());
    return page;
  }

  /**
   * 根据sheetid查询出采购申请表单详细信息
   */
  @Override
  public List<PurApply> queryPurApplyInfoBySheetId(UserInfo userinfo, String sheetId)
      throws Exception {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurApply> page = scope.getPage();

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", scope.getSiteId());

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 queryPurApplyInfoBySheetId 方法，操作人：" + userinfo.getUserName()
        + " | 时间为：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " | 站点编码为："
        + userinfo.getSiteId() + " | sheetId为：" + sheetId);

    return purApplyDao.queryPurApplyInfoBySheetId(page);
  }

  /**
   * @description:查询不带中文的采购申请信息
   * @author: yuanzh
   * @param userinfo
   * @param sheetId
   * @return:
   */
  private List<PurApply> queryPurApplyInfoBySheetIdNoZh(UserInfo userinfo, String sheetId) {
    UserInfoScope scope = (UserInfoScope) userinfo;
    Page<PurApply> page = scope.getPage();

    page.setParameter("sheetId", sheetId);
    page.setParameter("siteId", scope.getSiteId());

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 queryPurApplyInfoBySheetIdNoZh 方法，操作人："
        + userinfo.getUserName() + " | 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " | 站点编码为："
        + userinfo.getSiteId() + " | sheetId为：" + sheetId);

    return purApplyDao.queryPurApplyInfoBySheetIdNoZh(page);
  }

  /**
   * 根据sheetid查询采购申请物资列表
   */
  @Override
  public Page<PurApplyItemVO> queryPurApplyItemList(UserInfoScope userInfo, String sheetId,
      String sendType) throws Exception {
    UserInfoScope scope = userInfo;
    Page<PurApplyItemVO> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", sheetId);
    page.setParameter("commitcommecnetwk", sendType);
    List<PurApplyItemVO> ret = purApplyDao.queryPurApplyItemList(page);
    page.setResults(ret);

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 queryPurApplyItemList 方法，操作人：" + userInfo.getUserName()
        + " | 时间为：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " | 站点编码为："
        + userInfo.getSiteId() + " | sheetId为：" + sheetId);

    return page;
  }

  @Override
  public List<InvWarehouse> queryPurApplyWarehouse(String siteId){
      List<InvWarehouse> result = new ArrayList<InvWarehouse>(0);
      result = purApplyDao.queryPurApplyWarehouse( siteId );
      return result;
  }
  
  /**
   * 更新PurApply信息
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurApplyInfo(PurApply purApply) {

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 updatePurApplyInfo 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    return purApplyDao.updatePurApplyInfo(purApply);
  }

  /**
   * 新建PurApply信息
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurApplyInfo(PurApply purApply) {
    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 insertPurApplyInfo 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    return purApplyDao.insertPurApplyInfo(purApply);
  }

  /**
   * 根据sheetid删除PurApplyItem信息
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int deletePurApplyItemBySheetId(String sheetId) {
    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 deletePurApplyItemBySheetId 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    return purApplyDao.deletePurApplyItemBySheetId(sheetId);
  }

  /**
   * 批量插入PurApplyItem信息(页面数据直接插入)
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int insertPurApplyItemWithList(List<PurApplyItem> paiList) {
    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 insertPurApplyItemWithList 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    return purApplyDao.insertPurApplyItemWithList(paiList);
  }

  /**
   * @description:更新状态
   * @author: 890166
   * @createDate: 2014-8-8
   * @param status
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurApplyItemStatus(String status, String sheetId, String itemId ,String categoryId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", status);
    map.put("sheetId", sheetId);
    map.put("itemId", itemId);
    map.put("invcateId", categoryId);

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 updatePurApplyItemStatus 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    return purApplyDao.updatePurApplyItemStatus(map);
  }

  /**
   * @description:批量更新采购申请明细是否已经提交商务网状态
   * @author: 890166
   * @createDate: 2014-7-3
   * @param itemIds
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurApplyItemBusinessStatus(String sheetId, String[] itemIds, int busiStatus,
      String itemStatus) throws Exception {
    PurApply purApply = new PurApply();
    purApply.setSheetId(sheetId);
    purApply.setModifydate(new Date());
    purApplyDao.updatePurApplyInfo(purApply);

    StringBuilder itemSB = new StringBuilder("");
    for (int i = 0; i < itemIds.length; i++) {
        if ( itemIds[i].split( "_" ).length==2 ) {
            itemSB.append(" t.itemid = '").append(itemIds[i].split( "_" )[0]).append("' and t.invcateid = '").append( itemIds[i].split( "_" )[1] ).append( "' or" );
        }
    }
    itemSB.append( " 1 = 0 " );
    String itemId = itemSB.toString();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", sheetId);
    map.put("itemId", itemId);
    map.put("busiStatus", busiStatus);
    map.put("itemStatus", itemStatus);

    LOG.info(">>>>>>>>>>>>>>>> 系统调用了 updatePurApplyItemBusinessStatus 方法， 时间为："
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    return purApplyDao.updatePurApplyItemBusinessStatus(map);

  }

  /**
   * @description:采购申请统一保存方法
   * @author: 890166
   * @createDate: 2014-7-4
   * @param pa
   * @param paiList
   * @param paramMap
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean saveOrUpdatePurApply(@Operator UserInfoScope userInfo,
      @LogParam("purApply") PurApply pa, List<PurApplyItem> paiList, Map<String, Object> paramMap)
      throws Exception {
    boolean flag = true; // 默认值为false
    try {
      if (null != pa) {
        int counter = savePuraApplyInfo(userInfo, pa, paramMap);

        // 判断主单信息不为空
        if (counter > 0) {
          savePuraApplyItem(userInfo, pa, paiList, paramMap);
        }

        // 20160107 add by yuanzh采购申请附件上传功能
        String uploadIds = paramMap.get("uploadIds") == null ? "" : String.valueOf(paramMap
            .get("uploadIds"));
        purAttachMappingService.deletePurAttachMappingBySheetno(pa.getSheetno());
        if (!"".equals(uploadIds)) {
          String[] ids = uploadIds.split(",");
          // 先删除后插入
          for (int i = 0; i < ids.length; i++) {
            PurAttachMapping pam = new PurAttachMapping();
            pam.setSheetno(pa.getSheetno());
            pam.setAttachid(ids[i]);
            purAttachMappingService.insertPurAttachMapping(pam);
          }
          attachmentMapper.setAttachmentsBinded(ids, 1);
        }
      }
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyServiceImpl 中的 saveOrUpdatePurApply 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      flag = false;
    }
    return flag;
  }

  /**
   * @description:采购申请单主单信息保存
   * @author: yuanzh
   * @createDate: 2015-10-21
   * @param userInfo
   * @param pa
   * @param paramMap
   * @return:
   */
  private int savePuraApplyInfo(UserInfoScope userInfo, PurApply pa, Map<String, Object> paramMap) {
    String type = paramMap.get("type") == null ? "" : String.valueOf(paramMap.get("type"));
    String status = paramMap.get("status") == null ? "0" : String.valueOf(paramMap.get("status")); // 状态
    String sheetId = paramMap.get("sheetId") == null ? "" : String.valueOf(paramMap.get("sheetId"));

    int recounter = 0;
    pa.setSiteid(userInfo.getSiteId()); // 站点id

    if ("0".equals(pa.getPurchstatus()) || "".equals(pa.getPurchstatus())) {
      if ("save".equals(type)) {
        pa.setPurchstatus("0"); // 通过暂存进入，保存为草稿
      } else {
        pa.setPurchstatus(status); // 审批完成
        // 如果是草稿以非暂存形式保存，就把状态设为1。这个purchasestatus除了主表本身状态，下面还会用来更新物资列表状态。
        if ("0".equals(pa.getPurchstatus())) {
          pa.setPurchstatus("1");
        }
      }
    }
    // 先通过sheetid查询看是否存在记录
    if (!"".equals(sheetId)) {
      if ("3".equals(status)) {
        pa.setPurchstatus(status); // 审批完成
      }
      pa.setSheetId(sheetId);
      pa.setModifyaccount(userInfo.getUserId());
      pa.setModifydate(new Date());
      recounter = updatePurApplyInfo(pa);
    } else { // PurApply的insert操作
      pa.setCreateaccount(userInfo.getUserId()); // 创建人账号
      pa.setCreatedate(new Date()); // 创建时间
      pa.setSiteid(userInfo.getSiteId()); // 站点id
      pa.setSheetno(null);
      pa.setSumflag("0"); // 未标记汇总单
      pa.setPurchtype("0"); // 普通申请
      pa.setDeptid(userInfo.getOrgId());
      pa.setDept(userInfo.getOrgName());
      recounter = insertPurApplyInfo(pa);
    }

    if (recounter > 0) {
      if (null != pa.getIsauth() && !"".equals(pa.getIsauth())) {
        workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "isauth",
            pa.getIsauth());
      }
      if (null != pa.getTatolcost() && !"".equals(pa.getTatolcost())) {
        workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "tatolcost",
            pa.getTatolcost());
      }
      workflowService.setVariable(String.valueOf(paramMap.get("processInstanceId")), "sheetId",
          pa.getSheetId());
    }

    return recounter;
  }

  /**
   * @description: 保存采购申请列表信息
   * @author: yuanzh
   * @createDate: 2015-10-20
   * @param userInfo
   * @param pa
   * @param paiList
   * @param paramMap
   *          :
   */
  private void savePuraApplyItem(UserInfoScope userInfo, PurApply pa, List<PurApplyItem> paiList,
      Map<String, Object> paramMap) {
    String type = paramMap.get("type") == null ? "" : String.valueOf(paramMap.get("type"));
    String proStatus = paramMap.get("proStatus") == null ? "" : String.valueOf(paramMap
        .get("proStatus")); // 状态
    paramMap.put("sheetId", pa.getSheetId());
    paramMap.put("sheetNo", pa.getSheetno());
    String status = pa.getPurchstatus();
    paramMap.put("status", status);

    // 保存物资信息
    if (null != paiList && !paiList.isEmpty()) {
      // 若不是最后的环节
      if (!"3".equals(status)) {
        // 先根据sheetid将子单中的所有信息清空
        deletePurApplyItemBySheetId(pa.getSheetId());

        long totalItemnum = 0L; // 默认累加采购数量
        double costCalu = 0D; // 默认累加总价

        // 通过遍历子表list做一些前期的工作
        for (PurApplyItem pai : paiList) {
          pai.setSheetId(pa.getSheetId()); // 将sheetid重新绑定到子单信息中
          if ("submit".equals(type)) {
            pai.setStatus(status); // 提交采购申请
          } else {
            pai.setStatus("0"); // 草稿
          }
          pai.setCommitcommecnetwk("0"); // 默认不提交商务网
          if ("draft".equals(proStatus)) {
              pai.setRepliednum(pai.getItemnum());
          }
          totalItemnum += pai.getRepliednum(); // 累计采购数量
          double averDouble = pai.getAverprice() == null ? 0D : pai.getAverprice().doubleValue();
          costCalu = costCalu + (averDouble * pai.getRepliednum()); // 累计成本
        }
        // 重新插入子单列表信息
        insertPurApplyItemWithList(paiList);

        // 更新主单信息
        pa.setItemnum(totalItemnum);
        pa.setTatolcost(new BigDecimal(costCalu));
        updatePurApplyInfo(pa);
      }else if ("3".equals(status)&&"SWF".equals( userInfo.getSiteId())) {
          //原来的设定最后一个环节不更新明细表，只更新状态。生物质例外，最后一个环节还可以修改，这部分代码要重构
          // 先根据sheetid将子单中的所有信息清空
          deletePurApplyItemBySheetId(pa.getSheetId());

          long totalItemnum = 0L; // 默认累加采购数量
          double costCalu = 0D; // 默认累加总价

          // 通过遍历子表list做一些前期的工作
          for (PurApplyItem pai : paiList) {
            pai.setSheetId(pa.getSheetId()); // 将sheetid重新绑定到子单信息中
            if ("submit".equals(type)) {
              pai.setStatus(status); // 提交采购申请
            } else {
              pai.setStatus("0"); // 草稿
            }
            pai.setCommitcommecnetwk("0"); // 默认不提交商务网
            if ("draft".equals(proStatus)) {
                pai.setRepliednum(pai.getItemnum());
            }
            totalItemnum += pai.getRepliednum(); // 累计采购数量
            double averDouble = pai.getAverprice() == null ? 0D : pai.getAverprice().doubleValue();
            costCalu = costCalu + (averDouble * pai.getRepliednum()); // 累计成本
          }
          // 重新插入子单列表信息
          insertPurApplyItemWithList(paiList);

          // 更新主单信息
          pa.setItemnum(totalItemnum);
          pa.setTatolcost(new BigDecimal(costCalu));
          updatePurApplyInfo(pa);
        }
    }else if("0".equals( status )){//暂存时清空物资明细
     // 先根据sheetid将子单中的所有信息清空
        deletePurApplyItemBySheetId(pa.getSheetId());
        long totalItemnum = 0L; // 默认累加采购数量
        double costCalu = 0D; // 默认累加总价
        // 更新主单信息
        pa.setItemnum(totalItemnum);
        pa.setTatolcost(new BigDecimal(costCalu));
        updatePurApplyInfo(pa);
    }
    // 保存映射表信息
    savePurProcessMappingInfo(userInfo, pa, paramMap);
  }

  /**
   * @description: 保存采购与流程之间的映射关系
   * @author: yuanzh
   * @createDate: 2015-10-20
   * @param userInfo
   * @param pa
   * @param paramMap
   *          :
   */
  private void savePurProcessMappingInfo(UserInfoScope userInfo, PurApply pa,
      Map<String, Object> paramMap) {
    String taskName = paramMap.get("taskName") == null ? "" : String.valueOf(paramMap
        .get("taskName")); // 任务名称
    String processInstanceId = paramMap.get("processInstanceId") == null ? "" : String
        .valueOf(paramMap.get("processInstanceId")); // 流程实例

    // 当前任务id为空且流程实例不为空的时候，保存流程实例信息
    PurProcessMapping ppm = new PurProcessMapping();
    ppm.setMasterkey(String.valueOf(pa.getSheetId()));
    ppm.setModeltype("purapply");
    ppm.setSiteid(pa.getSiteid());
    ppm.setCurHandler(userInfo.getUserName());
    ppm.setCurLink(taskName);

    String processId = purProcessMappingDao.queryProcessIdByEntity(ppm);

    if (!"".equals(processId) && null != processId) {
      ppm.setProcessid(processId);
      purProcessMappingDao.updatePurProcessMapping(ppm);
    } else {
      ppm.setProcessid(processInstanceId);
      purProcessMappingDao.insertPurProcessMapping(ppm);
    }
  }

  /**
   * @description:查询生成的采购单
   * @author: 890166
   * @createDate: 2014-7-7
   * @param appSheetId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public Map<String, Object> queryGenerateOrder(@Operator UserInfoScope userInfo,
      @LogParam("appSheetId") String appSheetId, String itemIds) throws Exception {
    UserInfo uinfo = null;
    PurOrder purOrder = null;
    PurOrderItemVO poi = null;
    String sheetIType = null;
    String projectAscription = null;
    double totalPriceDob = 0.0;
    List<PurOrderItemVO> poiList = new ArrayList<PurOrderItemVO>();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    UserInfoScope scope = userInfo;

    Page<PurApply> page = scope.getPage();

    String pageSize = CommonUtil.getProperties("pageSize");
    page.setPageSize(Integer.valueOf(pageSize));

    page.setParameter("sheetId", appSheetId);
    page.setParameter("siteId", scope.getSiteId());

    List<PurApply> paList = purApplyDao.queryPurApplyInfoBySheetId(page);
    if (null != paList && !paList.isEmpty()) {
      PurApply purApply = paList.get(0);

      purOrder = new PurOrder();
      List<AppEnum> aeList = itcMvcService.getEnum("ITEMORDER_TYPE");
      for (AppEnum ae : aeList) {
        if (ae.getLabel().equals(purApply.getSheetclassid())) {
          sheetIType = ae.getCode();
          break;
        }
      }

      uinfo = itcMvcService.getUserInfoById(purApply.getOrderaccount());

      purOrder.setSheetIType(sheetIType);
      purOrder.setDhdate(purApply.getDhdate());
      purOrder.setRemark(purApply.getRemark() == null ? "" : purApply.getRemark());
      purOrder.setCreateaccount(userInfo.getUserId());
      purOrder.setCreatedate(new Date());
      purOrder.setSiteid(userInfo.getSiteId());
      purOrder.setTaxRate(new BigDecimal("0.00"));
      purOrder.setStatus("0");
    }

    Page<PurApplyItemVO> pageVO = scope.getPage();
    pageVO.setPageSize(100);
    pageVO.setParameter("sheetId", appSheetId);
    pageVO.setParameter("commitcommecnetwk", "0");
    pageVO.setParameter("status", "1");
    if (null != itemIds && !"".equals(itemIds)) {
      pageVO.setParameter("itemIds", itemIds);
    }
    List<PurApplyItemVO> paiList = purApplyDao.queryPurApplyItemList(pageVO);

    if (null != paiList && !paiList.isEmpty()) {
      for (PurApplyItemVO paiv : paiList) {
        poi = new PurOrderItemVO();
        poi.setApplySheetId(String.valueOf(appSheetId));
        poi.setCost(paiv.getAverprice() == null ? "0" : String.valueOf(paiv.getAverprice()));
        poi.setItemid(paiv.getItemid());
        poi.setItemnum(paiv.getRepliednum() == null ? "0" : paiv.getRepliednum());
        poi.setAverprice(paiv.getAverprice() == null ? "0" : String.valueOf(paiv.getAverprice()));
        poi.setRemark(paiv.getRemark());
        poi.setItemcus(paiv.getCusmodel());
        poi.setItemname(paiv.getItemname());
        poi.setOrderunitid(paiv.getOrderunitid());
        poi.setOrderunitname(paiv.getOrderunitname());
        poi.setPriceTotal(paiv.getPriceTotal());
        poi.setTax("0.00");
        poi.setTaxRate(new BigDecimal("0.00"));
        poi.setCreateUserName(uinfo.getUserName());
        poi.setApplyDept(uinfo.getOrgName());
        poi.setProjectAscription(projectAscription);
        poi.setWarehouseid(paiv.getWarehouseid());
        poi.setWarehouse(paiv.getWarehouse());
        poi.setListId(paiv.getListId());
        poi.setInvcateid( paiv.getInvcateid() );
        poiList.add(poi);

        totalPriceDob += Double.parseDouble(paiv.getPriceTotal());
      }
      purOrder.setTotalPrice(new BigDecimal(totalPriceDob));
    }

    resultMap.put("purOrder", purOrder);
    resultMap.put("poiList", poiList);

    return resultMap;
  }

  /**
   * @description:删除采购申请信息
   * @author: 890166
   * @createDate: 2014-8-12
   * @param sheetId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean deletePurApplyData(String sheetId) throws Exception {
    return true;
  }

  /**
   * @description: 通过sheetNo和站点id找到申请表id
   * @author: 890166
   * @createDate: 2014-9-23
   * @param sheetNo
   * @param siteId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public String querySheetIdByFlowNo(String sheetNo, String siteId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetNo", sheetNo);
    map.put("siteId", siteId);
    return purApplyDao.querySheetIdByFlowNo(map);
  }

  /**
   * @description: 通过sheetid查询flowno
   * @author: 890166
   * @createDate: 2015-2-26
   * @param sheetId
   * @param siteId
   * @return
   * @throws Exception
   *           :
   */
  @Override
  public String queryFlowNoBySheetId(String sheetId, String siteId) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", sheetId);
    map.put("siteId", siteId);
    return purApplyDao.queryFlowNoBySheetId(map);
  }

  /**
   * @description: 发送数据到商务网接口入口
   * @author: 890166
   * @createDate: 2014-10-29
   * @return
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean sendDataToBussiness(UserInfoScope userInfo, String sheetId, String[] itemidArr)
      throws Exception {
    boolean flag = true;
    EamPRCom eamPrCom = new EamPRCom();
    String parent = null;
    String child = null;
    try {
      List<AppEnum> aeList = itcMvcService.getEnum("SITE_IN_BUSI_MAPPING");
      for (AppEnum ae : aeList) {
        String[] siteInfo = ae.getCode().toLowerCase().split("_");
        if ("parent".equals(siteInfo[1])) {
          parent = siteInfo[0].toUpperCase();
          eamPrCom.setSiteId(parent);
        } else {
          child = siteInfo[0].toUpperCase();
          eamPrCom.setSubSiteId(child);
        }
      }

      // 查询采购申请单的主表数据
      List<PurApply> paList = queryPurApplyInfoBySheetIdNoZh(userInfo, sheetId);
      if (null != paList && !paList.isEmpty()) {
        PurApply pa = paList.get(0);
        // 转换主表信息到中间表
        eamPrCom = conventEamPR(eamPrCom, pa, userInfo);
        if (!"".equals(eamPrCom.getEamPR().getPrnum())) {
          // 转换子表信息到中间表
          eamPrCom = conventEamPrLine(userInfo, eamPrCom, sheetId, itemidArr);
          // 保存转换后数据
          eamPrService.addEamPRWithLog(eamPrCom);
        }

      }
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyServiceImpl 中的 sendDataToBussiness 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
      flag = false;
    }
    return flag;
  }

  /**
   * @description:转换主单信息
   * @author: 890166
   * @createDate: 2014-10-29
   * @param pa
   * @return:
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  private EamPRCom conventEamPR(EamPRCom eamPrCom, PurApply pa, UserInfoScope userInfo) {
    // 先保存中间表数据
    PurPaSyncHhc ppsh = purPaSyncHhcService.savePurPaSyncHhc(pa, userInfo);
    UserInfo ui = itcMvcService.getUserInfoById(pa.getCreateaccount());
    EamPR eamPrInsert = new EamPR();
    eamPrInsert.setPrnum(pa.getSiteid() + ppsh.getHhcPano()); // 采购申请编号
    eamPrInsert.setRequireddate(pa.getDhdate()); // 到货日期
    eamPrInsert.setRequestedby(ui.getUserName()); // 申请人工号
    eamPrInsert.setDescription(pa.getSheetname()); // 采购申请名称
    eamPrInsert.setChangeby(ui.getUserName()); // 处理人工号
    eamPrInsert.setChangedate(new Date()); // 处理时间
    eamPrInsert.setSiteid(pa.getSiteid()); // 站点id
    eamPrInsert.setStatusdate(new Date()); // 状态时间
    eamPrInsert.setPr4(pa.getDept());
    eamPrCom.setEamPR(eamPrInsert);
    return eamPrCom;
  }

  /**
   * @description:转换子单列表信息
   * @author: 890166
   * @createDate: 2014-10-29
   * @param sheetId
   * @param itemidArr
   * @return:
   */
  private EamPRCom conventEamPrLine(UserInfoScope userInfo, EamPRCom eamPrCom, String sheetId,
      String[] itemidArr) {

    PurApplyItemVO paiv = null;

    // 收集到物资编号信息，并转换成字符串
    StringBuilder itemSB = new StringBuilder("");
    for (int i = 0; i < itemidArr.length; i++) {
        if ( itemidArr[i].split( "_" ).length==2 ) {
            itemSB.append(" t.itemid = '").append(itemidArr[i].split( "_" )[0]).append("' and t.invcateid = '").append( itemidArr[i].split( "_" )[1] ).append( "' or" );
        }
    }
    itemSB.append( " 1 = 0 " );
    String itemId = itemSB.toString();
    // 将sheetid和itemid信息同时传入采购申请子表查询选中的物资
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sheetId", sheetId);
    map.put("itemId", itemId);
    map.put("siteId", userInfo.getSiteId());
    List<PurApplyItemVO> paivList = purApplyDao.queryPurApplyItemBySheetIdAndItems(map);
    // 通过循环将数据遍历放入中间表子表并保存
    for (int i = 0; i < paivList.size(); i++) {
      paiv = paivList.get(i);

      EamPRLine eamPRLine = new EamPRLine();
      eamPRLine.setLoadedcost(0D); // 行合计价格
      eamPRLine.setDescription(paiv.getItemname()); // 物资内容

      eamPRLine.setPrlineid(paiv.getEamPrlineId());// 表主键ID
      eamPRLine.setPrlinenum(Long.valueOf(i + 1));// 行计数，第几项物资
      eamPRLine.setPrnum(eamPrCom.getEamPR().getPrnum());// 主表单编号

      // 20151224 modify by yuanzh 商务网要求物资编码前加上站点的三字码
      // 20160224 modify by yuanzh 追加仓库id在后面以便分割
      eamPRLine.setItemnum(paiv.getSiteid() + paiv.getItemid() + paiv.getWarehouseid()+paiv.getInvcateid());

      eamPRLine.setOrderqty(Double.valueOf(paiv.getRepliednum()));// 采购数量
      eamPRLine.setOrderunit(paiv.getOrderunitname());// 计量单位
      eamPRLine.setModelnum(paiv.getCusmodel());// 规格型号
      eamPRLine.setEnterdate(new Date());// 提交日期
      eamPRLine.setEnterby(eamPrCom.getEamPR().getRequestedby());// 申请人
      eamPRLine.setRequestedby(eamPrCom.getEamPR().getRequestedby());// 提交人
      eamPRLine.setLinecost(0D);// 单价
      eamPRLine.setTax1(0D);// 税费
      eamPRLine.setSiteid(paiv.getSiteid());// 站点
      eamPRLine.setOrgid("YUDEAN");
      eamPRLine.setRemark(paiv.getRemark());

      eamPrCom.addEamPRLines(eamPRLine);
    }

    return eamPrCom;
  }

  /**
   * @description: 发送商务网同步数据接口(异步接口供定时器你调用)
   * @author: 890166
   * @createDate: 2014-10-29
   * @param pai
   * @return:
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void syncDataToEamItem(Date syncDate) throws Exception {
    // 获取所有使用商务网的站点
    String[] siteIds = CommonUtil.getProperties("hhcSite").split(",");

    for (String siteId : siteIds) {
      // 设定查询条件
      Map<String, Object> paramMap = new HashMap<String, Object>();
      paramMap.put("siteId", siteId);
      paramMap.put("syncDate", syncDate);
      // 根据站点和日期查询需要同步的数据(这个没有物资信息)
      List<EamItemVO> eivList = purApplyDao.queryHhcSyncDataInTimss(paramMap);

      LOG.info("--------------------------------------------");
      LOG.info("- " + siteId + "站点今天有" + eivList.size() + "条数据需要同步！-");
      syncData2EamItem(eivList, siteId);
      LOG.info("--------------------------------------------");
    }
  }

  /**
   * @description: 实时同步物资信息到商务网
   * @author: 890166
   * @createDate: 2015-3-20
   * @param itemidArr
   * @param siteId
   * @throws Exception
   *           :
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void syncDataToEamItem4RealTime(String[] itemidArr, String siteId) throws Exception {
    // 收集到含物资分类编号的物资编号信息，并转换成字符
    StringBuilder itemSB = new StringBuilder("");
    for (int i = 0; i < itemidArr.length; i++) {
        if ( itemidArr[i].split( "_" ).length==2 ) {
            itemSB.append(" a.itemid = '").append(itemidArr[i].split( "_" )[0]).append("' and a.invcateid = '").append( itemidArr[i].split( "_" )[1] ).append( "' or" );
        }
    }
    itemSB.append( " 1 = 0 " );
    String itemId = itemSB.toString();

    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("siteId", siteId);
    paramMap.put("itemCodes", itemId);
    // 根据站点和日期以及含物资分类信息的物资信息查询需要同步的数据
    List<EamItemVO> eivList = purApplyDao.queryHhcSyncDataInTimss(paramMap);

    LOG.info("--------------------------------------------");
    LOG.info("- " + siteId + "本批次有" + eivList.size() + "条数据需要同步！-");
    syncData2EamItem(eivList, siteId);
    LOG.info("--------------------------------------------");
  }

  /**
   * @description:同步数据到商务网接口（主入口）
   * @author: 890166
   * @createDate: 2015-3-20
   * @param eivList
   * @param siteId
   * @throws Exception
   *           :
   */
  private void syncData2EamItem(List<EamItemVO> eivList, String siteId) throws Exception {
    for (EamItemVO eiv : eivList) {
      LOG.info("- 查询的物资编码是： " + eiv.getItemnum() + " -");
      // 根据物资编码和站点到中间表中查询物资
      EamItem eamItem = eamItemService.getEamItem(
          eiv.getSiteid() + eiv.getItemnum() + eiv.getWarehouseid()+eiv.getInvcateid(), eiv.getSiteid());
      // 若已经存在更新物资信息
      if (null != eamItem) {
        LOG.info(">>>>>>>>>>>>>>>>> 物资编码为：" + eamItem.getItemnum() + " 的物资已存在，现在更新数据！ ");
        modifyEamItem(eamItem, eiv);
      } else {
        LOG.info(">>>>>>>>>>>>>>>>> 物资编码为：" + eiv.getSiteid() + eiv.getItemnum()
            + " 的物资不存在，现在插入数据！ ");
        addEamItem(eamItem, eiv, siteId);
      }
    }
    // 20151229 喻经理要求：将插入后的数据再重新查询一遍并写入到log文件中
    LOG.info(">>>>>>>>>>>>>>>>> 本轮插入、更新到中间库数据如下：");
    for (EamItemVO eiv : eivList) {
      EamItem eamItem = eamItemService.getEamItem(eiv.getSiteid() + eiv.getItemnum(),
          eiv.getSiteid());
      if (null != eamItem) {
        LOG.info(">>>>>>>>>>>>>>>>> 物资编码为：" + eamItem.getItemnum() + " | 物资型号为："
            + eamItem.getCumodel() + " | 物资名称为：" + eamItem.getDescription() + " | 物资id为："
            + eamItem.getItemid() + "...");
      }
    }
  }

  /**
   * @description: 发送更新数据到商务网
   * @author: 890166
   * @createDate: 2015-3-20
   * @param eamItem
   * @param eiv
   * @return:
   */
  private int modifyEamItem(EamItem eamItem, EamItemVO eiv) {
    eamItem.setClaes(eiv.getClaes());// 物资类别
    eamItem.setCumodel(eiv.getCumodel());// 规格型号
    eamItem.setDescription(eiv.getDescription());// 物资内容、名称
    eamItem.setOrderunit(eiv.getOrderunit());// 物资计量单位 个、辆、块 等
    eamItem.setIssueunit(eiv.getIssueunit());// 物资计量单位 个、辆、块 等，和orderunit一样
    eamItem.setStatus(ActiveStatus.ACTIVE);// 启用标志
    eamItem.setStatusdate(new Date());// 状态日期
    return eamItemService.modifyEamItem(eamItem);
  }

  /**
   * @description:发送插入数据到商务网
   * @author: 890166
   * @createDate: 2015-3-20
   * @param eamItem
   * @param eiv
   * @param siteId
   * @return
   * @throws Exception
   *           :
   */
  private int addEamItem(EamItem eamItem, EamItemVO eiv, String siteId) throws Exception {
    int maxnum = 0;// 中间表中最大itemid
    int rowstamp = 0;// 行标识
    // 若是新增，获取随机号
    double random = 89999999 * Math.random() + 10000000;
    DecimalFormat format = new DecimalFormat("###");
    rowstamp = Integer.valueOf(String.valueOf(format.format(random)));

    // 若PurHhcMaxnum表中是否存在数据
    List<PurHhcMaxnum> phmList = purHhcMaxnumService.queryPurHhcMaxnum(siteId);
    if (null != phmList && !phmList.isEmpty()) {
      PurHhcMaxnum phm = phmList.get(0);
      maxnum = phm.getMaxnum().intValue() + 1;// 新增+1
      phm.setMaxnum(new BigDecimal(maxnum));
      purHhcMaxnumService.updatePurHhcMaxnum(phm);

      LOG.info("- 更新编号记录表PurHhcMaxnum，最新编码是：  " + maxnum + " -");

    } else {
      PurHhcMaxnum phm = new PurHhcMaxnum();
      maxnum = Integer.valueOf(CommonUtil.getProperties(siteId + "_Itemid")) + 1;
      // 完全新增情况
      phm.setMaxnum(new BigDecimal(maxnum));
      phm.setSiteid(siteId);
      purHhcMaxnumService.insertPurHhcMaxnum(phm);
      LOG.info("- 编号记录表PurHhcMaxnum没有任何记录，最新编码是：  " + maxnum + " -");
    }

    // 测试新增物资主表
    eamItem = new EamItem();
    eamItem.setClaes(eiv.getClaes());// 物资类别
    eamItem.setCumodel(eiv.getCumodel());// 规格型号
    eamItem.setDescription(eiv.getDescription());// 物资内容、名称
    eamItem.setOrderunit(eiv.getOrderunit());// 物资计量单位 个、辆、块 等
    eamItem.setIssueunit(eiv.getIssueunit());// 物资计量单位 个、辆、块 等，和orderunit一样
    eamItem.setItemid(maxnum);// 中间库编码ID，石碑山由800000000开始

    // 20151225 modify by yuanzh 商务网要求增加站点三字码到字段前面
    eamItem.setItemnum(siteId + eiv.getItemnum() + eiv.getWarehouseid()+eiv.getInvcateid());// 物编码资
    eamItem.setRowstamp(siteId + String.valueOf(rowstamp));// format
    eamItem.setYudeannum(siteId + eiv.getItemnum() + eiv.getWarehouseid());// 粤电编码

    // 99999999
    // 格式的数字，唯一
    eamItem.setStatus(ActiveStatus.ACTIVE);// 启用标志
    eamItem.setStatusdate(eiv.getStatusdate());// 状态日期
    eamItem.setCusiteid(eiv.getSiteid());// 站點信息
    return eamItemService.addEamItem(eamItem);
  }

  @Override
  public int updatePurApplyInfoTransactor(String sheetId, String transactor) throws Exception {
    PurApply purApply = new PurApply();
    purApply.setSheetId(sheetId);
    purApply.setTransactor(transactor);
    int result = purApplyDao.updatePurApplyInfoTransactor(purApply);
    LOG.info("- 更新采购申请待办人：  " + result + " -");
    return result;
  }

  @Override
  public List<PurApplyItem> queryPurApplyItemListBySheetId(String sheetId) {
    List<PurApplyItem> purApplyItems = purApplyDao.queryPurApplyItemListBySheetId(sheetId);
    LOG.info("- 根据sheetId查询物资列表，返回条数：  " + purApplyItems.size() + " -");
    return purApplyItems;
  }

  @Override
  public PurApply queryPurApplyBySheetId(String sheetId) {
    PurApply purApply = purApplyDao.queryPurApplyBySheetId(sheetId);
    LOG.info("- 根据sheetId查询采购申请，返回purApplyid：  " + purApply.getSheetId() + " -");
    return purApply;
  }

  @Override
  public int updatePurApplyInfoIsToBusiness(String sheetId, String sendToBusiness) {
    int result = 0;
    PurApply purApply = new PurApply();
    purApply.setSheetId(sheetId);
    purApply.setIsToBusiness(sendToBusiness);
    if (null != purApply) {
      result = purApplyDao.updatePurApplyInfoIsToBusiness(purApply);
    }
    LOG.info("- 更新采购申请是否提交商务网状态，更新条数：  " + result + " -");
    return result;
  }

  /**
   * 根据sheetid查询采购申请物资列表 返回List
   */
  @Override
  public List<PurApplyItemVO> queryPurApplyItemListAsList(@Operator UserInfoScope userInfo,
      @LogParam("sheetId") String sheetId,
      @LogParam("purApplyItemVOCon") PurApplyItemVO purApplyItemVOCon) throws Exception {
    // UserInfoScope scope = userInfo;
    List<PurApplyItemVO> list = purApplyDao.queryPurApplyItemListAsList(sheetId, purApplyItemVOCon);
    LOG.info("- 根据sheetId查询物资VO列表，返回条数:  " + list.size() + " -");
    return list;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public int updatePurApplyToPass(String sheetId, String taskName) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    PurApply purApply = this.queryPurApplyBySheetId(sheetId);
    // 更新状态与待办人
    purApply.setSheetId(sheetId);
    purApply.setPurchstatus("3"); // 审批完成
    purApply.setModifyaccount(userInfo.getUserId());
    purApply.setModifydate(new Date());
    purApply.setTransactor(null);
    try {
      // 更新物资
      long totalItemnum = 0L; // 默认累加采购数量
      double costCalu = 0D; // 默认累加总价
      List<PurApplyItem> applyItemList = this.queryPurApplyItemListBySheetId(sheetId);
      for (PurApplyItem pai : applyItemList) {
        totalItemnum += pai.getRepliednum(); // 累计采购数量
        double averDouble = pai.getAverprice() == null ? 0D : pai.getAverprice().doubleValue();
        costCalu = costCalu + (averDouble * pai.getRepliednum()); // 累计成本

        this.updatePurApplyItemStatus("3", sheetId, pai.getItemid(),pai.getInvcateid());
      }
      purApply.setItemnum(totalItemnum);
      purApply.setTatolcost(new BigDecimal(costCalu));
      this.updatePurApplyInfo(purApply);
      // 当前任务id为空且流程实例不为空的时候，保存流程实例信息
      PurProcessMapping ppm = new PurProcessMapping();
      ppm.setMasterkey(String.valueOf(purApply.getSheetId()));
      ppm.setModeltype("purapply");
      ppm.setSiteid(purApply.getSiteid());
      ppm.setCurHandler(userInfo.getUserName());
      ppm.setCurLink(taskName);
      String processId = purProcessMappingDao.queryProcessIdByEntity(ppm);
      if (!"".equals(processId) && null != processId) {
        ppm.setProcessid(processId);
        purProcessMappingDao.updatePurProcessMapping(ppm);
      }
    } catch (Exception e) {
      LOG.info("- 审批通过更新采购申请状态待办人出现异常", e);
      throw new Exception();
    }

    updatePurApplyInfoTransactor(sheetId, "");
    return 1;
  }

  /**
   * @description:生成采购申请表单数据[refactor]
   * @author: 890162
   * @createDate: 2015-10-23
   * @param UserInfoScope
   * @param type
   * @param sheetId
   * @return
   * @throws Exception
   */
  @Override
  public Map<String, Object> initPurApplyForm(String type, String sheetId) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>(0);
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    String siteId = userInfo.getSiteId();
    result.put("siteId", siteId);
    result.put("orgName", userInfo.getOrgName());

    String defKey = "purchase_[@@@]_purapply".replace("[@@@]", siteId.toLowerCase());
    String stopDefKey = "purchase_[@@@]_purapplystop".replace("[@@@]", siteId.toLowerCase());
    
    result.put("defKey", defKey);
    result.put( "stopDefKey", stopDefKey );
    String sheetNo = userInfo.getParam("sheetNo");
    if (StringUtils.isEmpty(sheetNo)) {
      sheetNo = queryFlowNoBySheetId(sheetId, siteId);
    }

    if ("null".equals(sheetId) && StringUtils.isNotEmpty(sheetNo)) {
      sheetId = querySheetIdByFlowNo(sheetNo, siteId);
    }

    // 如果是新建的话就创建一个流程实例
    String[] types = type.split("_");
    if (!"new".equals(types[0])) {
      PurProcessMapping ppm = new PurProcessMapping();
      ppm.setMasterkey(sheetId);
      ppm.setSiteid(siteId);
      ppm.setModeltype("purapply");
      String processInst = purProcessMappingService.queryProcessIdByParams(ppm);
      result.put("processInstId", processInst);

      // modify by yuanzh 20160104 将查询流程环节的动作放在有sheetNo的地方
      WorktaskBean wtBean = homepageService.getOneTaskByFlowNo(sheetNo, userInfo);
      if (null != wtBean) {
        result.put("classType", wtBean.getClasstype().toString());
      }

      if (null != processInst && !"".equals(processInst)) {
        List<HistoricTask> htList = workflowService.getPreviousTasks(processInst);
        if (null != htList && !htList.isEmpty()) {
          result.put("isRollBack", "Y");
        }

        List<Task> activities = workflowService.getActiveTasks(processInst);
        if (null != activities && !activities.isEmpty()) {
          Task task = activities.get(0);
          List<String> userIdList = workflowService.getCandidateUsers(task.getId());
          for (String userId : userIdList) {
            if (userId.equals(userInfo.getUserId())) {
              result.put("isEdit", "editable");
              break;
            }
          }

          Map<String, String> processAttr = workflowService.getElementInfo(task.getId());
          result.put("oper", processAttr.get("modifiable"));
          result.put("taskId", task.getId());
          result.put("process", task.getTaskDefinitionKey());
          result.put("processName", task.getName());

          boolean isLastStep = workflowService.getVariable(processInst, "isLastStep") == null ? false
              : (Boolean) workflowService.getVariable(processInst, "isLastStep");
          result.put("isLastStep", isLastStep);

        }
      } else {
        result.put("isEdit", "editable");
      }

      // 20160107 add by yuanzh 增加附件上传功能
      List<Map<String, Object>> fileMap = purAttachMappingService.queryPurAttach(sheetNo);
      JSONArray jsonArray = JSONArray.fromObject(fileMap);
      result.put("uploadFiles", jsonArray);

    } else {
      result.put("isEdit", "editable");
    }
    return result;
  }
  
  /**
   * @description:查询采购申请的物资是否分类启用
   * @author: 890191
   * @createDate: 2016-8-12
   * @param paiList
   * @return map
   * @throws Exception :
   */
    @Override
	public String queryActiveByPurApply(List<PurApplyItem> paiList)
			throws Exception {
		return purApplyDao.queryActiveByPurApply(paiList);
	}
    /**
     * 回退采购申请明细状态为申请采购 
     */
    @Override
    public int revertPurApplyItemToApplyStatus(String sheetId) throws Exception {
        purApplyDao.updatePurApplyItemStatusByApplyId(sheetId,"3","1");
        return 0;
    }
    @Override
    public Boolean allItemsApplying(String sheetId) {
       Boolean result = true;
       List<PurApplyItem> purApplyItemList = purApplyDao.queryPurApplyItemListBySheetId( sheetId );       
       for ( PurApplyItem item : purApplyItemList ) {
          if ( !"1".equals( item.getStatus() ) ) {
              result = false;
              LOG.info("--发现"+item.getItemcode()+"-"+item.getItemname()+" status:"+item.getStatus());
              break;
          }
        }
        return result;
    }
    @Override
    public Boolean hasItemsBusinessApplying(String sheetId) {
       Boolean result = false;
       List<PurApplyItem> purApplyItemList = purApplyDao.queryPurApplyItemListBySheetId( sheetId );       
       for ( PurApplyItem item : purApplyItemList ) {
          if ( "2".equals( item.getStatus() ) ) {
              result = true;
              LOG.info("--发现"+item.getItemcode()+"-"+item.getItemname()+" status:"+item.getStatus());
              break;
          }
        }
        return result;
    }
    @Override
    public Boolean hasItemApplying(String sheetId) {
        Boolean result = false;
        List<PurApplyItem> purApplyItemList = purApplyDao.queryPurApplyItemListBySheetId( sheetId );       
        for ( PurApplyItem item : purApplyItemList ) {
           if ( "1".equals( item.getStatus() ) ) {
               result = true;
               LOG.info("--发现"+item.getItemcode()+"-"+item.getItemname()+" status:"+item.getStatus());
               break;
           }
         }
         return result;
     }
}
