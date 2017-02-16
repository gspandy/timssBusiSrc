package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatReturnsDetail;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsDetailDao.java
 * @author: 890166
 * @createDate: 2015-3-13
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatReturnsDetailDao {

    /**
     * @description:表单列表查询
     * @author: 890166
     * @createDate: 2015-3-16
     * @param page
     * @return:
     */
    List<InvMatReturnsDetailVO> queryMatReturnsDetailList(Page<?> page);

    /**
     * @description:查看接收物资
     * @author: 890166
     * @createDate: 2015-3-18
     * @param page
     * @return:
     */
    List<InvMatReturnsDetailVO> queryMatDetailList(Page<?> page);

    /**
     * @description: 删除物资退换货子单信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param imrsdid
     * @return
     * @throws Exception:
     */
    int deleteMatReturnsDetailByImrsdid(String imrsdid);

    /**
     * @description:删除物资退换货子单信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param imrsid
     * @return
     * @throws Exception:
     */
    int deleteMatReturnsDetailByImrsid(String imrsid);

    /**
     * @description: 插入InvMatReturnsDetail数据
     * @author: 890166
     * @createDate: 2015-3-18
     * @param imrd
     * @return:
     */
    int insertInvMatReturnsDetail(InvMatReturnsDetail imrd);

    /**
     * @description: 更新InvMatReturnsDetail数据
     * @author: 890166
     * @createDate: 2015-6-4
     * @param imrd
     * @return:
     */
    int updateInvMatReturnsDetail(InvMatReturnsDetail imrd);

    /**
     * @description: 根据物资接收单ID，查看对应的所有退货物资明细
     * @author: 890152
     * @createDate: 2015-9-23
     * @param page
     * @return:
     */
    List<InvItemVO> queryMatDetailListByImtid(Page<InvItemVO> page);

    /**
     * @description:查询退库单页面明细
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param paramMap
     * @return:
     */
    List<InvMatReturnsDetailVO> queryRefundingDetailI(Map<String, String> paramMap);

    /**
     * @description:判断是否符合显示按钮要求
     * @author: yuanzh
     * @createDate: 2015-9-25
     * @param paramMap
     * @return:返回Y代表可以隐藏，N代表要展示
     */
    String queryRefundingBtnIsHide(Map<String, String> paramMap);
}
