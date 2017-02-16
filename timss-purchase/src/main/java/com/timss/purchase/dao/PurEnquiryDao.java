package com.timss.purchase.dao;

import java.util.List;

import com.timss.purchase.bean.PurEnquiry;
import com.timss.purchase.bean.PurEnquiryItem;
import com.timss.purchase.vo.PurEnquiryItemVO;
import com.timss.purchase.vo.PurEnquiryVO;
import com.yudean.itc.dto.Page;

public interface PurEnquiryDao {

  /*
   * @description:根据当前人的id查询他自己的询价单列表
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param page
   * 
   * @return:
   */
  List<PurEnquiryVO> queryPurEnquiry(Page<?> page);

  /*
   * @description:通过rowid查询enquiry的详细信息
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param rowId
   * 
   * @return:
   */
  List<PurEnquiryVO> queryPurEnquiryByrowId(String rowId);

  /*
   * @description:通过id找到enquiry明细列表
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param id
   * 
   * @return:
   */
  List<PurEnquiryItemVO> queryPurEnquiryItemsById(String id);

  /*
   * @description:新增询价单
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param purEnquiry
   * 
   * @return
   * 
   * @throws Exception :
   */
  PurEnquiry insertPurEnquiry(PurEnquiry purEnquiry) throws Exception;

  /*
   * @description:新增询价单明细
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param purEnquiryItem
   * 
   * @return
   * 
   * @throws Exception :
   */
  int insertPurEnquiryItem(PurEnquiryItem purEnquiryItem) throws Exception;

  /*
   * @description:根据rowid删除询价单
   * 
   * @author: 890166
   * 
   * @createDate: 2014-8-15
   * 
   * @param rowId
   * 
   * @return:
   */
  int deletePurEnquiryDataByrowId(String rowId);
}
