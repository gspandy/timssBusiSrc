package com.timss.purchase.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timss.purchase.bean.PurPurchaserBean;
import com.timss.purchase.service.PurPurchaserService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPurchaserController.java
 * @author: 890191
 * @createDate: 2015-9-24
 * @updateUser: 890191
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purPurchaser")
public class PurPurchaserController {

  @Autowired
  private PurPurchaserService purPurchaserService;

  @Autowired
  private ItcMvcService itcMvcService;

  /**
   * @description:买方资料页面
   * @author: 890191
   * @createDate: 2015-9-28
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/purPurchaserList", method = RequestMethod.GET)
  public String purPurchaserList() {
    return "/purPurchaser/purPurchaserForm.jsp";
  }

  /**
   * @description:通过站点查询买方资料
   * @author: 890191
   * @createDate: 2015-9-28
   * @return
   */
  @RequestMapping(value = "/queryDetailBySiteId", method = RequestMethod.POST)
  public PurPurchaserBean queryPurchaserDetailBySiteId() {
    UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
    return purPurchaserService.queryPurPurchaserBySiteId(userInfoScope);
  }

  /**
   * @description:新增编辑买方资料
   * @author: 890191
   * @createDate: 2015-9-24
   * @param rowId
   * @return
   */
  @RequestMapping(value = "/saveOrupdatePurPurchaser", method = RequestMethod.POST)
  public Map<String, Object> saveOrupdatePurPurchaser(String purPurchaserBean) {
    PurPurchaserBean purPurchaser = JsonHelper.fromJsonStringToBean(purPurchaserBean,
        PurPurchaserBean.class);
    int count = 0;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if (StringUtils.isEmpty(purPurchaser.getPurchaserId())) {
      count = purPurchaserService.insertPurPurchaser(userInfo, purPurchaser);
    } else {
      count = purPurchaserService.updatePurPurchaser(userInfo, purPurchaser);
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
