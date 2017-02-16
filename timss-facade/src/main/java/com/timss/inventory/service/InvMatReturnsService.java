package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatReturns;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatReturnsDetailVO;
import com.timss.inventory.vo.InvMatReturnsVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsService.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatReturnsService {

    /**
     * @description:查询全部被退货的物资
     * @author: 890166
     * @createDate: 2015-3-12
     * @param userInfo
     * @return
     * @throws Exception:
     */
    Page<InvMatReturnsVO> queryAllReturnItem(UserInfoScope userInfo, String schfield) throws Exception;

    /**
     * @description:表单列表查询
     * @author: 890166
     * @createDate: 2015-3-16
     * @param userInfo
     * @param imrsid
     * @return
     * @throws Exception:
     */
    Page<InvMatReturnsDetailVO> queryMatReturnsDetailList(UserInfoScope userInfo, String imrsid) throws Exception;

    /**
     * @description:删除退换货记录
     * @author: 890166
     * @createDate: 2015-3-12
     * @param imrvList
     * @return
     * @throws Exception:
     */
    boolean deleteMatReturns(UserInfoScope userInfo, List<InvMatReturnsVO> imrvList) throws Exception;

    /**
     * @description: 查询表单详细信息
     * @author: 890166
     * @createDate: 2015-3-13
     * @param userInfo
     * @param imrsid
     * @return
     * @throws Exception:
     */
    List<InvMatReturnsVO> queryMatReturnsForm(UserInfoScope userInfo, String imrsid) throws Exception;

    /**
     * @description: 弹出页面查询可退回物资
     * @author: 890166
     * @createDate: 2015-3-18
     * @param userInfo
     * @param invMatReturnsDetailVO
     * @return
     * @throws Exception:
     */
    Page<InvMatReturnsDetailVO> queryMatReturnsItemList(UserInfoScope userInfo,
            InvMatReturnsDetailVO invMatReturnsDetailVO) throws Exception;

    /**
     * @description: 保存退换货物资信息
     * @author: 890166
     * @createDate: 2015-3-18
     * @param userInfo
     * @param imr
     * @param imrdList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    Map<String, Object> saveMatReturns(UserInfoScope userInfo, InvMatReturns imr, List<InvMatReturnsDetailVO> imrdList,
            Map<String, Object> paramMap) throws Exception;

    /**
     * @description:根据物资接收单id查看其对应的所有退货明细(或者某次退货明细)
     * @author: 王中华
     * @createDate: 2015-9-23
     * @param userInfo
     * @param imtid 物资接收单id
     * @param imrsid 某次退货的id，可以为null
     * @return:
     */
    Page<InvItemVO> queryMatReturnsDetailListByImtId(UserInfoScope userInfo, String imtid,String imrsid);

    /**
     * @description: 根据sheetno找到退库信息
     * @author: yuanzh
     * @createDate: 2015-9-28
     * @param userInfo
     * @param sheetNo
     * @return:
     */
    InvMatReturns queryReturnsBySheetNo(UserInfoScope userInfo, String sheetNo);
    
    /**
     * @description:
     * @author: 王中华
     * @createDate: 2016-5-25
     * @param sheetNo 
     * @return:
     */
    boolean isReceiveAllItem(String sheetNo );
}
