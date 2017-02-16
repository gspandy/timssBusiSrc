package com.timss.purchase.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.service.PurApplyService;
import com.timss.purchase.utils.CommonPurToEipMobile;
import com.timss.purchase.utils.CommonUtil;
import com.timss.purchase.utils.ReflectionUtil;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurEipListEip;
import com.yudean.interfaces.interfaces.EipMobileInterface;
import com.yudean.itc.annotation.EipAnnotation;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamDetailBean;
import com.yudean.itc.dto.interfaces.eip.mobile.ParamProcessBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean;
import com.yudean.itc.dto.interfaces.eip.mobile.RetContentInLineBean.Type;
import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;
import com.yudean.itc.dto.interfaces.eip.mobile.RetProcessBean;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;

/*
 * @description: {desc}
 * @company: gdyd
 * @className: PurchaseToEipMobile.java
 * @author: 890166
 * @createDate: 2014-9-22
 * @updateUser: 890166
 * @version: 1.0
 */
@EipAnnotation("purchApply")
@Component
public class PurApplyToEipMobile implements EipMobileInterface {

  @Autowired
  private PurApplyService purApplyService;
  @Autowired
  private ItcMvcService itcMvcService;
  @Autowired
  private CommonPurToEipMobile emc;
  @Autowired
  private WorkflowService workflowService;

  private static final Logger LOG = Logger.getLogger(PurApplyToEipMobile.class);

  @Override
  public RetProcessBean processWorkflow(ParamProcessBean eipmobileparambean) {
    return emc.getProcessWorkflow(eipmobileparambean);
  }

  /**
   * eip移动端界面展示
   */
  @Override
  public RetContentBean retrieveWorkflowFormDetails(ParamDetailBean eipmobileparambean) {
    RetContentBean efdb = new RetContentBean();

    // 表单的sheetNo
    String flowNo = eipmobileparambean.getFlowNo();
    // 流程实例id
    String processId = eipmobileparambean.getProcessId();
    try {
      UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
      // 通过flowNo找到对应的sheetId
      String sheetId = purApplyService.querySheetIdByFlowNo(flowNo, userInfo.getSiteId());

      // 组装forms中数据
      List forms = assembleContent(userInfo, sheetId, processId);
      efdb.setContent(forms);

      // 组装attachements中数据
      List attachements = emc.assembleAttachements();
      efdb.setAttachments(attachements);

      // 组装flows中数据
      String processDefKey = "purchase_[@@@]_purapply".replace("[@@@]", userInfo.getSiteId()
          .toLowerCase());

      List flows = emc.assembleFlows(userInfo, processId, processDefKey);
      efdb.setFlows(flows);
    } catch (Exception e) {
      LOG.info("--------------------------------------------");
      LOG.info("- PurApplyToEipMobile 中的 retrieveWorkflowFormDetails 方法抛出异常：", e);
      LOG.info("--------------------------------------------");
    }
    return efdb;
  }

  /*
   * @description:组织form中的数据
   * 
   * @author: 890166
   * 
   * @createDate: 2014-9-23
   * 
   * @param formId
   * 
   * @return:
   */
  private List<RetContentInLineBean> assembleContent(UserInfoScope userInfo, String sheetId,
      String processId) throws Exception {
    List<RetContentInLineBean> epbList = new ArrayList<RetContentInLineBean>();
    RetContentInLineBean rcilb = null;

    // 收集主单信息
    List<PurApply> paList = purApplyService.queryPurApplyInfoBySheetId(userInfo, sheetId);
    if (null != paList && !paList.isEmpty()) {
      rcilb = settingFormData(paList, processId);
      epbList.add(rcilb);
    }

    // 收集子单信息
    List<PurApplyItemVO> paiList = purApplyService.queryPurApplyItemList(userInfo, sheetId, "0")
        .getResults();
    if (null != paiList && !paiList.isEmpty()) {
      rcilb = settingListData(paiList);
      epbList.add(rcilb);
    }

    // 组装opions中数据
    rcilb = emc.assembleOpinions(processId);
    epbList.add(rcilb);

    return epbList;
  }

  /*
   * @description:转换form数据信息到接口实体
   * 
   * @author: 890166
   * 
   * @createDate: 2014-9-25
   * 
   * @param paList
   * 
   * @return:
   */
  private RetContentInLineBean settingFormData(List<PurApply> paList, String processId)
      throws Exception {

    RetContentInLineBean pafem = new RetContentInLineBean();
    PurApply pa = paList.get(0); // 获取到的表单数据

    pafem.setFoldable(true);// 是否可以折叠标志
    pafem.setIsShow(true); // 是否显示标志
    pafem.setName(getPropertiesVal("eip2purApplyFormGroupName"));// 名称
    pafem.setType(Type.KeyValue);// 类别，这里固定keyValue

    // 通过properties文件中定义的字段反射获取价值
    List<Object> rkvList = new ArrayList<Object>();
    String[] formField = getPropertiesVal("eip2purApplyForm").split(",");
    for (String ffield : formField) {
      String[] fieldContent = ffield.split("#@#");
      String fieldKey = fieldContent[0];
      String fieldVal = ReflectionUtil.getFieldValueByFieldName(pa, fieldContent[1]);

      if ("tatolcost".equals(fieldContent[1])) {
        workflowService.setVariable(processId, "tatolcost", new BigDecimal(fieldVal));
      }

      if ("sheetclassid".equals(fieldContent[1])) {
        workflowService.setVariable(processId, "sheetclassid", fieldVal);
      }

      rkvList.add(new RetKeyValue(fieldKey, fieldVal));
    }
    pafem.setValue(rkvList);
    return pafem;
  }

  /*
   * @description:转换list数据信息到接口实体
   * 
   * @author: 890166
   * 
   * @createDate: 2014-9-25
   * 
   * @param paiList
   * 
   * @return
   * 
   * @throws Exception :
   */
  private RetContentInLineBean settingListData(List<PurApplyItemVO> paiList) throws Exception {
    RetContentInLineBean palem = new RetContentInLineBean();
    palem.setFoldable(true);// 是否可以折叠标志
    palem.setIsShow(false); // 是否显示标志
    palem.setName(getPropertiesVal("eip2purApplyListGroupName"));// 名称
    palem.setType(Type.Table);// 类别，这里固定table

    List<Object> rkvList = new ArrayList<Object>();
    // 获取column字段
    String[] listField = getPropertiesVal("eip2purApplyList").split(",");
    // 获取list数据
    for (PurApplyItemVO pav : paiList) {
      PurEipListEip emfi = new PurEipListEip();
      // 预设一个行的集合
      List<RetKeyValue> rows = new ArrayList<RetKeyValue>();
      for (String lField : listField) {
        String[] fieldContent = lField.split("#@#");
        String fieldKey = fieldContent[0];
        String fieldVal = ReflectionUtil.getFieldValueByFieldName(pav, fieldContent[1]);
        // 如果是itemname的话就做特殊处理
        if ("itemname".equals(fieldContent[1])) {
          emfi.setTitle(fieldVal);
        }
        rows.add(new RetKeyValue(fieldKey, fieldVal));
      }
      emfi.setRows(rows);
      rkvList.add(emfi);
    }
    palem.setValue(rkvList);
    return palem;
  }

  /*
   * @description:获取properties文件中配置信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-9-25
   * 
   * @param typeName
   * 
   * @return
   * 
   * @throws Exception :
   */
  private String getPropertiesVal(String typeName) throws Exception {
    return new String(CommonUtil.getProperties(typeName).getBytes("ISO-8859-1"), "UTF-8");
  }

}
