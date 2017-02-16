package com.timss.purchase.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.timss.purchase.bean.PurVendor;
import com.timss.purchase.service.PurVendorService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.supplier.bean.SupBaseInfo;
import com.yudean.supplier.service.SupplierForModelService;
import com.yudean.supplier.vo.SupBaseInfoDtlVo;
import com.yudean.supplier.vo.SupBaseInfoVo;

/**
 * @title: 供应商
 * @description: 供应商controller
 * @company: gdyd
 * @className: PurVendorController.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "purchase/purvendor")
public class PurVendorController {

  /**
   * service注入
   */
  @Autowired
  private PurVendorService purVendorService;

  @Autowired
  private ItcMvcService itcMvcService;

  @Autowired
  private SupplierForModelService supplierForModelService;

  /**
   * log4j输出
   */
  private static final Logger LOG = Logger.getLogger(PurVendorController.class);

  /**
   * @description:列表页面跳转
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purVendorList", method = RequestMethod.GET)
  public String purVendorList() {
    return "/purvendor/purVendorList.jsp";
  }

  /**
   * @description:页面列双击后跳转到详细信息页面
   * @author: 890166
   * @createDate: 2014-8-16
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/purVendorForm", method = RequestMethod.GET)
  @ReturnEnumsBind("PUR_COMPANYTYPE")
  public String purVendorForm() {
    return "/purvendor/purVendorForm.jsp";
  }

  /**
   * @description:获取供应商列表
   * @author: 890166
   * @createDate: 2014-8-16
   * @param search
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurVendor", method = RequestMethod.POST)
  public Page<PurVendor> queryPurVendor(String search) throws Exception {
    PurVendor purVendor = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if (StringUtils.isNotBlank(search)) {
      purVendor = JsonHelper.fromJsonStringToBean(search, PurVendor.class);
    }
    return purVendorService.queryPurVendor(userInfo, purVendor);
  }

  /**
   * @description:查询详细信息页面的时候表单信息获取
   * @author: 890166
   * @createDate: 2014-8-16
   * @param companyNo
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryPurVendorDetail", method = RequestMethod.POST)
  public PurVendor queryPurVendorDetail(String companyNo) throws Exception {
    PurVendor purVendor = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    List<PurVendor> list = purVendorService.queryPurVendorDetailByCompanyNo(companyNo,
        userInfo.getSiteId());
    if (null != list && !list.isEmpty()) {
      purVendor = list.get(0);
    }
    return purVendor;
  }

  /**
   * @description:提交操作
   * @author: 890166
   * @createDate: 2014-6-20
   * @param formData
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/commitVendor", method = RequestMethod.POST)
  public Map<String, Object> commitVendor(String formData) throws Exception {
    PurVendor purVendor = JsonHelper.fromJsonStringToBean(formData, PurVendor.class);
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    int count = purVendorService.purVendorSaveOrUpdate(userInfo, purVendor);
    Map<String, Object> map = new HashMap<String, Object>();
    if (count > 0) {
      map.put("result", "success");
    } else {
      map.put("result", "false");
    }
    return map;
  }

  /**
   * @description:查询供应商信息
   * @author: 890166
   * @createDate: 2014-7-1
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryCompanyList", method = RequestMethod.POST)
  public Page<SupBaseInfoVo> queryCompanyList(String search) throws Exception {
    Page<SupBaseInfoVo> page = new Page<SupBaseInfoVo>();
    SupBaseInfo supBaseInfo = null;
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
    if (StringUtils.isNotBlank(search)) {
      supBaseInfo = JsonHelper.fromJsonStringToBean(search, SupBaseInfo.class);
    }
    if(null == supBaseInfo){
    	supBaseInfo = new SupBaseInfoVo();
    }
    supBaseInfo.setBlklstCl("N");
    supBaseInfo.setBlklstSup("N");
    List<SupBaseInfoVo> sbivList = supplierForModelService.querySupplierList(userInfo, supBaseInfo);
    LOG.info( "--" );
    page.setResults(sbivList);
    return page;
  }

  /**
   * @description:查询供应商详细信息
   * @author: 890166
   * @createDate: 2014-8-6
   * @param itemid
   * @return
   * @throws Exception
   *           :
   */
  @RequestMapping(value = "/queryVendorInfo", method = RequestMethod.GET)
  public ModelAndView queryVendorInfo(@RequestParam("companyNo") String companyNo) throws Exception {
    ModelAndView mav = new ModelAndView("/purvendor/purVendorInfo.jsp");
    UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();

    SupBaseInfoDtlVo sbidv = supplierForModelService.queryBaseInfoDtlVoByCode(companyNo,
        userInfo.getSiteId());
    String pvStr = JsonHelper.fromBeanToJsonString(sbidv);
    mav.addObject("pvData", pvStr);
    return mav;
  }
}
