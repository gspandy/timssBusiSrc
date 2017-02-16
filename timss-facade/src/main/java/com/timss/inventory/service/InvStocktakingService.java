package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvStocktaking;
import com.timss.inventory.bean.InvStocktakingDetail;
import com.timss.inventory.vo.InvStocktakingVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvStocktakingService.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvStocktakingService {

    /**
     * @description:查询列表功能
     * @author: 890166
     * @createDate: 2014-9-30
     * @param userInfo
     * @param isv
     * @return
     * @throws Exception:
     */
    Page<InvStocktakingVO> queryStocktakingList(UserInfoScope userInfo, InvStocktakingVO isv) throws Exception;

    /**
     * @description:直接查询表单数据
     * @author: 890166
     * @createDate: 2014-9-30
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    List<InvStocktakingVO> queryStocktakingForm(UserInfoScope userInfo, String istid) throws Exception;

    /**
     * @description:保存盘点信息
     * @author: 890166
     * @createDate: 2014-11-10
     * @param userInfo
     * @param ist
     * @param istdList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    Map<String, Object> saveStocktaking(UserInfoScope userInfo, InvStocktaking ist,
            List<InvStocktakingDetail> istdList, Map<String, Object> paramMap) throws Exception;

    /**
     * @description:更新当前库存数量
     * @author: 890166
     * @createDate: 2014-11-17
     * @param userInfo
     * @param istdList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    Map<String, Object> changeInvStock(UserInfoScope userInfo, Map<String, Object> paramMap) throws Exception;

    /**
     * @description: 查询并获取盘点附件
     * @author: 890166
     * @createDate: 2014-11-18
     * @param istid
     * @return
     * @throws Exception:
     */
    List<Map<String, Object>> queryStocktakingAttach(String istid) throws Exception;

    /**
     * @description: 通过id和站点id找到sheetNo
     * @author: 890166
     * @createDate: 2014-9-23
     * @param imaId
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryFlowNoByIstid(String istId, String siteId) throws Exception;

    /**
     * @description: 通过sheetNo和站点id找到id
     * @author: 890166
     * @createDate: 2015-3-2
     * @param flowNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryIstidByFlowNo(String sheetNo, String siteId) throws Exception;

    /**
     * @description:更新盘点数据
     * @author: 890166
     * @createDate: 2015-2-28
     * @param userInfo
     * @param ist
     * @throws Exception:
     */
    void updateStocktaking(UserInfoScope userInfo, InvStocktaking ist) throws Exception;

    /**
     * @description: 获取表单主信息
     * @author: 890166
     * @createDate: 2015-3-2
     * @param userInfo
     * @param istid
     * @return
     * @throws Exception:
     */
    List<InvStocktaking> queryStocktakingInfo(UserInfoScope userInfo, String istid) throws Exception;
}
