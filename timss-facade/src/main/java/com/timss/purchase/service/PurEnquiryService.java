package com.timss.purchase.service;

import java.util.List;

import com.timss.purchase.bean.PurEnquiry;
import com.timss.purchase.bean.PurEnquiryItem;
import com.timss.purchase.vo.PurEnquiryItemVO;
import com.timss.purchase.vo.PurEnquiryVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;

public interface PurEnquiryService {

    /**
     * @description:根据当前人的id查询他自己的询价单列表
     * @author: 890166
     * @createDate: 2014-8-15
     * @param userinfo
     * @return
     * @throws Exception :
     */
    Page<PurEnquiryVO> queryPurEnquiry(UserInfo userinfo) throws Exception;

    /**
     * @description:根据当前人的id和条件查询他自己的询价单列表
     * @author: 890166
     * @createDate: 2014-8-15
     * @param userinfo
     * @param purEnquiry
     * @return
     * @throws Exception :
     */
    Page<PurEnquiryVO> queryPurEnquiry(UserInfo userinfo, PurEnquiryVO purEnquiry) throws Exception;

    /**
     * @description:通过询价单id删除询价单
     * @author: 890166
     * @createDate: 2014-8-15
     * @param rowId
     * @return
     * @throws Exception :
     */
    int deletePurEnquiryDataByrowId(String rowId) throws Exception;

    /**
     * @description:通过rowid查询enquiry的详细信息
     * @author: 890166
     * @createDate: 2014-8-15
     * @param rowId
     * @return
     * @throws Exception :
     */
    List<PurEnquiryVO> queryPurEnquiryByrowId(String rowId) throws Exception;

    /**
     * @description:通过id找到enquiry明细列表
     * @author: 890166
     * @createDate: 2014-8-15
     * @param userinfo
     * @param id
     * @return
     * @throws Exception :
     */
    Page<PurEnquiryItemVO> queryPurEnquiryItemsById(UserInfo userinfo, String id) throws Exception;

    /**
     * @description:新增询价单
     * @author: 890166
     * @createDate: 2014-8-15
     * @param purEnquiry
     * @return
     * @throws Exception :
     */
    PurEnquiry insertPurEnquiry(PurEnquiry purEnquiry) throws Exception;

    /**
     * @description:新增询价单明细
     * @author: 890166
     * @createDate: 2014-8-15
     * @param purEnquiryItem
     * @return
     * @throws Exception :
     */
    int insertPurEnquiryItem(PurEnquiryItem purEnquiryItem) throws Exception;

}
