package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvStocktaking;
import com.timss.inventory.vo.InvStocktakingVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingDao.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvStocktakingDao {

    /**
     * @description:查询列表功能
     * @author: 890166
     * @createDate: 2014-9-30
     * @param page
     * @return:
     */
    List<InvStocktakingVO> queryStocktakingList(Page<?> page);

    /**
     * @description:直接查询表单数据
     * @author: 890166
     * @createDate: 2014-9-30
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    List<InvStocktaking> queryStocktakingForm(Map<String, Object> map);

    /**
     * @description:更新InvStocktaking信息
     * @author: 890166
     * @createDate: 2014-11-12
     * @param ist
     * @return:
     */
    int updateInvStocktaking(InvStocktaking ist);

    /**
     * @description:插入InvStocktaking信息
     * @author: 890166
     * @createDate: 2014-11-12
     * @param ist
     * @return:
     */
    int insertInvStocktaking(InvStocktaking ist);

    /**
     * @description: 通过id和站点id找到sheetNo
     * @author: 890166
     * @createDate: 2014-9-23
     * @return
     * @throws Exception:
     */
    String queryFlowNoByIstid(Map<String, Object> map);

    /**
     * @description: 通过sheetNo和站点id找到id
     * @author: 890166
     * @createDate: 2015-3-2
     * @param flowNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryIstidByFlowNo(Map<String, Object> map);
}
