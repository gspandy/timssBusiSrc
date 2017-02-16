package com.timss.purchase.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.purchase.service.ERPService;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.utils.WorkflowPurUtil;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.annotation.ValidFormToken;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: PurPayController
 * @description: 付款控制层
 * @company: gdyd
 * @className: PurPayController.java
 * @author: 890162
 * @createDate: 2016-3-25
 * @updateUser: 890162
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purpay")
public class PurPayController {
  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurPayController.class);

  @Autowired
  private PurPayService purPayService;
  @Autowired
  private ItcMvcService itcMvcService;
  @Autowired
  private WorkflowService workflowService;
  @Autowired
  private HomepageService homepageService;
  @Autowired
  private WorkflowPurUtil workflowPurUtil;
  @Autowired
  private ERPService erpService;
  

  /**
   * @Title:purPayForm
   * @Description:付款详细信息页面跳转
   * @param operType
   * @param payType
   * @param payId
   * @param sheetId
   * @throws Exception
   * @return ModelAndView
   * @throws
   */
  @RequestMapping(value = "/purPayForm", method = RequestMethod.GET)
  @ReturnEnumsBind("PUR_PAYTYPE,PAY_PAYSTATUS")
  public ModelAndView purPayForm(@RequestParam String operType,@RequestParam String payType,@RequestParam String payId,@RequestParam String sheetId) throws Exception {
    ModelAndView mav = new ModelAndView("/purpay/purPayForm.jsp");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    // 生成付款表单页面-- start
    if (StringUtils.isEmpty( payId )) {
      payId = userInfo.getParam("payId");
      mav.addObject("payId", payId);
    }
    //这个参数是给对审批通过的到货付款 生成质保金付款时传递刷新前一个页面的id
    String refreshTabId = userInfo.getParam( "refreshTabId" );
    //这个参数是给【生成质保金】付款时的必要参数
    String relatepayId = userInfo.getParam( "relatepayId" );
    mav.addObject( "refreshTabId", refreshTabId );
    mav.addObject( "relatepayId", relatepayId );
    Map<String, Object> result = purPayService.initPurPayForm( operType, payType, payId );
    Set<String> keySet = result.keySet();
    for (String key : keySet) {
      mav.addObject(key, result.get(key));
    }
    // 生成付款表单页面-- end
    return mav;
  }

  /**
   * @description:按照采购合同ID查询付款列表
   * @author: 890162
   * @createDate: 2016-03-25
   * @param sheetId
   * @return List<PurPayVO>
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurPayList", method = RequestMethod.POST)
  public Page<PurPayVO> queryPurPayList(String sheetId) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    Page<PurPayVO> resultPage = new Page<PurPayVO>();
    List<PurPayVO> purPayVOs = purPayService.queryPurPayVoListBySheetId( userInfo, sheetId  );
    resultPage.setResults( purPayVOs );
    resultPage.setTotalRecord( purPayVOs.size());
    return resultPage;
  }

  /**
   * @description:查询付款信息的基本信息
   * @author: 890162
   * @createDate: 2016-3-25
   * @param payId
   * @return PurPayVO
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurPayInfo", method = RequestMethod.POST)
  public PurPayVO queryPurPayInfo(String sheetId,String payId,String payType) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    PurPayVO purPayVO = new PurPayVO();
    if(StringUtils.isNotEmpty( payId )){
        //非新增
        purPayVO = purPayService.queryPurPayVoByPayId( userInfo, payId );    
    }else {
        //新增
        List<PurPayVO> purPayVOs =  purPayService.queryBlankPurPayVoListBySheetId( userInfo, sheetId );
        if ( 0<purPayVOs.size() ) {
            purPayVO = purPayVOs.get( 0 );
        }
        purPayVO.setPayType( payType );
    }
    //补充三个总金额(税前 税 总)以及对应给采购类型和物资类型的信息
    purPayService.queryPurPayVoAdditionalInfo( userInfo, purPayVO, payType );
    //生成质保付款独占方法--begin
    if (StringUtils.isEmpty( payId )&& "qualitypay".equals( payType ) ) {
        String relatepayId = userInfo.getParam( "relatepayId" );
        PurPayVO relatePayVO = purPayService.queryPurPayVoByPayId( userInfo, relatepayId );
        Date   excludeDate = relatePayVO.getAuditDate();
        String relatePayNo = relatePayVO.getPayNo();
        String relatePayId = relatePayVO.getPayId();
        Double qaPay = relatePayVO.getQaPay();
        purPayVO.setExcludeDate( excludeDate );
        purPayVO.setRelatePayId( relatePayId );
        purPayVO.setRelatePayNo( relatePayNo );
        purPayVO.setPay( qaPay );
        purPayVO.setActualPayTotal( purPayVO.getPay()-(null!=purPayVO.getRefusePay()?purPayVO.getRefusePay():0) );
    }
    //生成质保付款独占方法--end
    return purPayVO;
  }

  /**
   * @description:根据条件查询付款明细列表
   * @author: 890162
   * @createDate: 2016-3-25
   * @param payId
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryPurPayDtlList", method = RequestMethod.POST)
  public Page<PurPayDtlVO> queryPurPayDtlList(String payId,String sheetId,String payType) throws Exception {
      UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
      Page<PurPayDtlVO> result = new Page<PurPayDtlVO>();
      List<PurPayDtlVO> purPayDtlVos = new ArrayList<PurPayDtlVO>(0);
      purPayDtlVos = purPayService.queryPurPayDtlVoListByCondition( userInfo, payId ,sheetId ,payType); 
      result.setPageSize( purPayDtlVos.size() );
      result.setResults( purPayDtlVos );
      return result;
  }

  /**
   * @description:保存/提交付款信息方法
   * @author: 890162
   * @createDate: 2016-3-25
   * @param formData 主表数据
   * @param listData 子表数据
   * @param operType save/submit
   * @return Map
   * @throws Exception
   */
  @RequestMapping(value = "/commitApply", method = RequestMethod.POST)
  @ValidFormToken
  public Map<String, Object> commitApply(String formData, String listData, String saveType,
      String payId) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>(0);
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    boolean flag = true;
    // 将表单的数据转换成实体类
    PurPayVO purPayVo = JsonHelper.fromJsonStringToBean(formData, PurPayVO.class);
    // 将列表数据转换成List
    List<PurPayDtlVO> purPayDtlVoList = JsonHelper.toList(listData, PurPayDtlVO.class);
    // 参数map
    Map<String, Object> paramMap = new HashMap<String, Object>(0); 
    paramMap.put("saveType", saveType);
    paramMap.put("payId", payId);
    //附件上传的参数处理
    String uploadIds = userInfo.getParam("uploadIds");
    if (StringUtils.isNotEmpty(uploadIds)) {
      uploadIds = uploadIds.replace("\"", "");
      paramMap.put("uploadIds", uploadIds);
    }
    purPayService.saveOrUpdatePayInfo( userInfo, purPayVo, purPayDtlVoList, paramMap );
    result.putAll( paramMap );;
    if (flag) {
      result.put("result", "success");
    } else {
      result.put("result", "false");
    }
    return result;
  }

  /**
   * @description:删除、作废
   * @author: 890162
   * @createDate: 2016-3-30
   * @param taskId
   * @param processId
   * @param message
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/removePay", method = RequestMethod.POST)
  public Map<String, Object> stopProcess(@RequestParam("procInstId") String procInstId,@RequestParam("taskId") String taskId,
      @RequestParam("message") String message, @RequestParam("payId") String payId) {
    Map<String, Object> result = new HashMap<String, Object>();
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    
    String curUser = userInfo.getUserId();
    try {
        String deleteType = userInfo.getParam( "deleteType" );
        if ( "del".equals( deleteType ) ) {
            purPayService.updatePurPayStatus(payId, "deleted");
            purPayService.updatePurPayInfoTransactor( payId, "" );
            workflowService.stopProcess(taskId, curUser, curUser, message);
            homepageService.complete( procInstId, userInfo, message );
        }else if ( "revoke".equals( deleteType ) ) {
            purPayService.updatePurPayStatus(payId, "obsoleted");
            purPayService.updatePurPayInfoTransactor( payId, "" );
            workflowService.stopProcess(taskId, curUser, curUser, message);
            homepageService.complete( procInstId, userInfo, message );
        }
        result.put("result", "success");
    } catch (Exception e) {
        LOG.info("--------------------------------------------");
        LOG.info("- PurPayController 中的 stopProcess 方法抛出异常：", e);
        LOG.info("--------------------------------------------");
        result.put("result", "error");
    }
    return result;
  }
      @RequestMapping(value = "/sendToERP", method = RequestMethod.POST)
      public Map<String, Object> sendToERP(@RequestParam("payId") String payId,@RequestParam("invNum") String invNum,@RequestParam("invDesc") String invDesc) throws Throwable{

    	  Map<String, Object> result = new HashMap<String, Object>();
    	  String rsl = erpService.sendToERP(payId, invNum, invDesc);
    	  if ("1".equals(rsl)) {
    		  result.put("result", "success");
		  }else{

			  result.put("result", rsl);
		  }
    	  
    	  return result;
      }


//  @RequestMapping("/itemDetail")
//  public ModelAndView itemDetail(String sheetId, String itemid) throws Exception {
//    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
//    Map<String, Object> result = new HashMap<String, Object>(0);
//    PurApplyItemVO purApplyItemVOCon = new PurApplyItemVO();
//    purApplyItemVOCon.setItemid(itemid);
//    purApplyItemVOCon.setCommitcommecnetwk("0");
//    List<PurApplyItemVO> purApplyItems = purApplyService.queryPurApplyItemListAsList(userInfoScope,
//        sheetId, purApplyItemVOCon);
//    if (null != purApplyItems && !purApplyItems.isEmpty()) {
//      PurApplyItemVO purApplyItem = purApplyItems.get(0);
//      result.put("result", JsonHelper.fromBeanToJsonString(purApplyItem));
//    }
//    return new ModelAndView("purapply/itemDtl.jsp", result);
//  }

 
}
