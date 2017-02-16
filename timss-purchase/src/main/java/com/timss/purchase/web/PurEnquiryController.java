package com.timss.purchase.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.purchase.bean.PurEnquiry;
import com.timss.purchase.service.PurEnquiryService;
import com.timss.purchase.vo.PurEnquiryItemVO;
import com.timss.purchase.vo.PurEnquiryVO;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Controller
@RequestMapping(value = "purchase/purenquiry")
public class PurEnquiryController {

  /**
   * service注入
   */
  @Autowired
  private PurEnquiryService purEnquiryService;

  @Autowired
  private ItcMvcService itcMvcService;

  /**
   * @description:页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purEnquiryList", method = RequestMethod.GET)
  public String purEnquiryList() {
    return "/purenquiry/purEnquiryList.jsp";
  }

  /**
   * @description:页面列双击后跳转到详细信息页面
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purEnquiryForm", method = RequestMethod.GET)
  public String purEnquiryForm() {
    return "/purenquiry/purEnquiryForm.jsp";
  }

  /**
   * @description:异步查询列表数据
   * @author: 890166
   * @createDate: 2014-8-16
   * @param search
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurEnquiry", method = RequestMethod.POST)
  public Page<PurEnquiryVO> queryPurEnquiry(String search) throws Exception {
    Page<PurEnquiryVO> page = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if (StringUtils.isNotBlank(search)) {
      PurEnquiryVO purEnquiry = JsonHelper.fromJsonStringToBean(search, PurEnquiryVO.class);
      page = purEnquiryService.queryPurEnquiry(userInfo, purEnquiry);
    } else {
      page = purEnquiryService.queryPurEnquiry(userInfo);
    }
    return page;
  }

  /**
   * @description:查询询价单中物资列表数据
   * @author: 890166
   * @createDate: 2014-8-16
   * @param id
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurEnquiryItems", method = RequestMethod.POST)
  public Page<PurEnquiryItemVO> queryPurEnquiryItems(String id) throws Exception {
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    return purEnquiryService.queryPurEnquiryItemsById(userInfo, id);
  }

  /**
   * @description:查询详细信息页面的时候表单信息获取
   * @author: 890166
   * @createDate: 2014-8-16
   * @param rowId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurEnquiryDetail", method = RequestMethod.POST)
  public PurEnquiryVO queryPurEnquiryDetail(String rowId) throws Exception {
    PurEnquiryVO purEnquiry = null;
    List<PurEnquiryVO> list = purEnquiryService.queryPurEnquiryByrowId(rowId);
    if (null != list && !list.isEmpty()) {
      purEnquiry = list.get(0);
    }
    return purEnquiry;
  }

  /**
   * @description:根据rowid删除询价单
   * @author: 890166
   * @createDate: 2014-8-16
   * @param rowId
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/deletePurEnquiry", method = RequestMethod.POST)
  public Map<String, Object> deletePurEnquiry(String rowId) throws Exception {
    int count = purEnquiryService.deletePurEnquiryDataByrowId(rowId);
    Map<String, Object> map = new HashMap<String, Object>();
    if (count > 0) {
      map.put("result", "success");
    } else {
      map.put("result", "false");
    }
    return map;
  }

  /**
   * @description:提交操作（未完成）
   * @author: 890166
   * @createDate: 2014-6-20
   * @param formData
   * @param type
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/commitEnquiry", method = RequestMethod.POST)
  public Map<String, Object> commitEnquiry(String formData, String type) throws Exception {
    PurEnquiry purEnquiry = JsonHelper.fromJsonStringToBean(formData, PurEnquiry.class);
    int count = 0;
    if ("save".equals(type)) {
      purEnquiry = purEnquiryService.insertPurEnquiry(purEnquiry);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    if (count > 0) {
      map.put("result", "success");
    } else {
      map.put("result", "false");
    }
    return map;
  }

}
