package com.timss.purchase.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.bean.PurOrderItem;
import com.timss.purchase.vo.PurApplyItemVO;
import com.timss.purchase.vo.PurOrderItemVO;
import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.itc.util.json.JsonHelper;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: CommonUtil.java
 * @author: 890166
 * @createDate: 2014-6-25
 * @updateUser: 890166
 * @version: 1.0
 */
public class CommonUtil {

  private CommonUtil() {

  }

  /**
   * @description:能够高效找出两个list中相同和不同的的数据
   * @author: 890166
   * @createDate: 2014-6-25
   * @param minList
   *          数量较小的list
   * @param maxList
   *          数量较大的list
   * @return:
   */
  public static Map<String, List<String>> getListDifferAndSimilar(List<String> minList,
      List<String> maxList) {
    // 返回的map
    Map<String, List<String>> reMap = new HashMap<String, List<String>>();

    // 预设一个用来比较的map
    Map<String, Integer> map = new HashMap<String, Integer>(maxList.size() + minList.size());

    List<String> diffList = new ArrayList<String>();
    List<String> sameList = new ArrayList<String>();

    // 先将比较大的list数据放入map中
    for (String string : maxList) {
      map.put(string, 1);
    }
    // 再将较小的list放入map中
    for (String string : minList) {
      Integer count = map.get(string);
      if (null != count) {
        map.put(string, ++count);
        continue;
      }
      map.put(string, 1);
    }
    // 找出不同
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      if (entry.getValue() == 1) {
        diffList.add(entry.getKey());
      } else {
        sameList.add(entry.getKey());
      }
    }
    // 通过map返回
    reMap.put("diff", diffList);
    reMap.put("same", sameList);
    return reMap;
  }

  /**
   * json数据转换成PurApplyItem
   * 
   * @description: 因为页面上需要展示多方面的数据，所以页面的数据是经过整理后放在vo实体传到页面的。
   *               这样我们在获取页面信息的时候为了方便，后台只能先将传过来的数据转换成vo，再由vo转换成我们真正需要的实体中。
   * @author: 890166
   * @createDate: 2014-6-25
   * @param listData
   * @return
   * @throws Exception
   */
  public static List<PurApplyItem> conventJsonToPurApplyItemList(String listData,Boolean isNew) throws Exception {
    List<PurApplyItem> paiList = new ArrayList<PurApplyItem>();
    PurApplyItem pai = null;
    List<PurApplyItemVO> paivList = JsonHelper.toList(listData, PurApplyItemVO.class);
    // 由于页面展示的是vo，但是我们后台保存需要用到的是标准的实体，所以需要将vo转换成规定的实体类
    for (PurApplyItemVO paiv : paivList) {
      pai = new PurApplyItem();
      pai.setItemid(paiv.getItemid());
      //新建的时候，批复数量的0 才会设为itemnum,因为表单字段设为了数字类型，当为空时，就会默认设为0
      pai.setRepliednum(new Double(isNew&&"0".equals(paiv.getRepliednum()) ? paiv.getItemnum() : paiv
          .getRepliednum()));
      pai.setItemnum(new Double(paiv.getItemnum()));
      pai.setAverprice(new BigDecimal(paiv.getAverprice()));
      pai.setCommitcommecnetwk(paiv.getCommitcommecnetwk());
      pai.setWarehouseid(paiv.getWarehouseid());
      pai.setInvcateid( paiv.getInvcateid() );
      pai.setRemark(paiv.getRemark());
      paiList.add(pai);
    }
    return paiList;
  }

  /**
   * @description:通过适配器方法将json传过来的vo信息转换成实体类信息
   * @author: 890166
   * @createDate: 2014-7-1
   * @param listData
   * @return
   * @throws Exception
   */
  public static List<PurOrderItem> conventJsonToPurOrderItemList(String listData) throws Exception {
    List<PurOrderItem> poiList = new ArrayList<PurOrderItem>();
    PurOrderItem poi = null;
    List<PurOrderItemVO> poivList = JsonHelper.toList(listData, PurOrderItemVO.class);
    // 由于页面展示的是vo，但是我们后台保存需要用到的是标准的实体，所以需要将vo转换成规定的实体类
    for (PurOrderItemVO poiv : poivList) {
      poi = new PurOrderItem();
      poi.setApplySheetId(poiv.getApplySheetId());
      poi.setCost(new BigDecimal(null == poiv.getCost() ? "0" : poiv.getCost()));
      poi.setItemid(poiv.getItemid());
      poi.setItemnum(new BigDecimal(poiv.getItemnum()));
      poi.setPrice(new BigDecimal("".equals(poiv.getAverprice()) ? "0" : poiv.getAverprice()));
      poi.setReceivenum(new BigDecimal("0"));
      poi.setWarehouseid(poiv.getWarehouseid());
      poi.setInvcateid( poiv.getInvcateid() );
      poi.setRemark(poiv.getRemark());
      poi.setTax(new BigDecimal("".equals(poiv.getTax()) ? "0" : poiv.getTax()));
      poi.setTaxRate(poiv.getTaxRate() == null ? new BigDecimal("0") : poiv.getTaxRate());
      poiList.add(poi);
    }
    return poiList;
  }

  /**
   * @description:获取properties文件
   * @author: 890166
   * @createDate: 2014-8-16
   * @param key
   * @return:
   */
  public static String getProperties(String key) {
    Properties p = null;
    InputStream in = null;
    String reStr = null;
    try {
      in = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("purchase.properties");
      p = new Properties();
      p.load(in);
      reStr = p.getProperty(key);
    } catch (Exception e) {
      throw new RuntimeException("---------CommonUtil 中的 getProperties 方法抛出异常---------：", e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        throw new RuntimeException("---------CommonUtil 中的 getProperties 方法抛出异常---------：", e);
      }
    }
    return reStr;
  }

  /**
   * @description:返回随机数
   * @author: 890166
   * @createDate: 2014-8-19
   * @return:
   */
  public static String getRandom() {
    long mill = System.currentTimeMillis();
    String random = String.valueOf(mill);
    random = random.substring(random.length() - 5);
    return random;
  }

  /**
   * @description: 通过角色找到相关的人员
   * @author: 890166
   * @createDate: 2015-4-7
   * @param roleCode
   * @param siteId
   * @return:
   */
  @SuppressWarnings("deprecation")
  public static List<String> returnUserIdInRole(String roleCode, String siteId, String userName) {
    siteId = siteId.equals("SFC") ? "SBS" : siteId;
    List<String> list = new ArrayList<String>();
    String roleId = CommonUtil.getProperties(roleCode);
    String[] roleIds = roleId.split(",");
    IAuthorizationManager am = (SecurityBeanHelper.getInstance())
        .getBean(IAuthorizationManager.class);
    for (String rId : roleIds) {
      String[] siteArr = rId.split("_");
      if (siteArr[0].equals(siteId)) {
        List<SecureUser> usList = am.retriveUsersWithSpecificRole(rId, null, true, true);
        if (null == usList || usList.isEmpty()) {
          usList = am.retriveUsersWithSpecificGroup(rId, null, true, true);
        }

        if (null != userName) {
          for (SecureUser us : usList) {
            if (userName.equals(us.getName())) {
              list.add(us.getId()); // 仓管员
              break;
            }
          }
        } else {
          for (SecureUser us : usList) {
            list.add(us.getId()); // 仓管员
          }
        }
      }
    }
    return list;
  }

  /**
   * 计算两个日期之间的相隔的年、月、日。注意：只有计算相隔天数是准确的，相隔年和月都是 近似值，按一年365天，一月30天计算，忽略闰年和闰月的差别。
   * 
   * @param datepart
   *          两位的格式字符串，yy表示年，MM表示月，dd表示日
   * @param startdate
   *          开始日期
   * @param enddate
   *          结束日期
   * @return double 如果enddate>startdate，返回一个大于0的实数，否则返回一个小于等于0的实数
   */
  public static double dateDiff(String datepart, Date startdate, Date enddate) {
    if (datepart == null || datepart.equals("")) {
      throw new IllegalArgumentException("DateUtil.dateDiff()方法非法参数值：" + datepart);
    }

    double distance = (double) (enddate.getTime() - startdate.getTime())
        / ((double) 60 * 60 * 24 * 1000);
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(enddate.getTime() - startdate.getTime());
    if (datepart.equals("yy")) {
      distance = distance / 365;
    } else if (datepart.equals("MM")) {
      distance = distance / 30;
    } else if (datepart.equals("dd")) {
      distance = (enddate.getTime() - startdate.getTime()) / (double) (60 * 60 * 24 * 1000);
    } else if (datepart.equals("hh")) {
      distance = (enddate.getTime() - startdate.getTime()) / (double) (60.0 * 60 * 1000);
    } else if (datepart.equals("ss")) { // 得到秒
      distance = (enddate.getTime() - startdate.getTime()) / (double) 1000;
    } else if (datepart.equals("mm")) {
      distance = (enddate.getTime() - startdate.getTime()) / (double) 1000 / (double) 60;
    } else {
      throw new IllegalArgumentException("DateUtil.dateDiff()方法非法参数值：" + datepart);
    }
    return distance;
  }

  /**
   * 按指定格式格式化日期
   * 
   * @param date
   *          待格式化的日期
   * @param formatString
   *          　 日期格式 　取值为： 1、 yyyy-MM-dd hhmm(例：2013-07-17 1400) 2、yyyy-MM-dd hh:mm(例：2013-07-17
   *          14:00) 3、 yyyy/MM/dd hhmm 4、dd hhmm(例：17 1400) 5、 hhmm(例：1400) 6、 yyyy-MM-dd 7、 其它
   *          格式　　　　　　　　　　　　　　　结果 "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
   *          "EEE, MMM d, ''yy" Wed, Jul 4, '01 "h:mm a" 12:08 PM "hh 'o''clock' a, zzzz" 12
   *          o'clock PM, Pacific Daylight Time "K:mm a, z" 0:08 PM, PDT
   *          "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM "EEE, d MMM yyyy HH:mm:ss Z"
   *          Wed, 4 Jul 2001 12:08:56 -0700 "yyMMddHHmmssZ" 010704120856-0700
   *          "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
   * @return String
   */
  public static String formatDate(Date date, String formatString) {
    String result = null;
    try {
      if (date != null) {
        DateFormat df = new SimpleDateFormat(formatString);
        result = df.format(date);
      }
    } catch (Exception e) {
      result = "";
    }// END TRY-CATCH

    return result;
  }
}
