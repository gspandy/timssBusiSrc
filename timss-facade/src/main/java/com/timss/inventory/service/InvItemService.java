package com.timss.inventory.service;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvItem;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvSafetyStockVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemService.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvItemService {

    /**
     * 查詢物資列表
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param userInfo
     * @param iiv
     * @return
     * @throws Exception :
     */
    Page<InvItemVO> queryItemsList(UserInfoScope userInfo, InvItemVO iiv) throws Exception;

    /**
     * 通过id查询site
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-11
     * @param siteId
     * @return
     * @throws Exception :
     */
    List<Map<String, String>> querySiteById(String siteId) throws Exception;

    /**
     * 查询InvItem详细信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-14
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    List<InvItemVO> queryInvItemDetail(Map<String, Object> map) throws Exception;

    /**
     * 查询详细主项目信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception :
     */
    List<InvItemVO> queryInvMainItemDetail(UserInfoScope userInfo, String itemId) throws Exception;

    /**
     * 保存主项目
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-17
     * @param userInfo
     * @param ii
     * @param iemList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    Map<String, Object> saveItem(UserInfoScope userInfo, InvItem ii, List<InvWarehouseItemVO> iwiList,
            Map<String, Object> paramMap) throws Exception;

    /**
     * @description: 更新主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return
     * @throws Exception :
     */
    InvItem updateInvItemInfo(InvItem ii) throws Exception;

    /**
     * @description: 插入主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return
     * @throws Exception :
     */
    InvItem insertInvItemInfo(InvItem ii) throws Exception;

    /**
     * 查询到货物资列表
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-24
     * @param userInfo
     * @param ii
     * @return
     * @throws Exception :
     */
    Page<InvItemVO> queryArrivalItem(UserInfoScope userInfo, InvItemVO iiv, Map<String, Object> paramMap)
            throws Exception;
    
    /**
     * @description:查询物资详细信息
     * @author: 890166
     * @createDate: 2014-8-6
     * @param itemid
     * @return
     * @throws Exception :
     */
    List<InvItemVO> queryItemInfo(UserInfoScope userInfo, String itemcode, String warehouseid, String invcateid) throws Exception;

    /**
     * @description:查询低于安全库存的物资列表
     * @author: 890147
     * @createDate: 2014-10-11
     * @param page
     * @return:
     */
    Page<InvItemVO> queryInvItemSafetyStock(Page<InvItemVO> page) throws Exception;

    /**
     * 查询站点中低于安全库存的物资数量，如果存在则在待办中添加提醒，否则从待办中删除提醒
     * 
     * @param userInfo
     * @return
     * @throws Exception
     */
    int checkInvItemSafetyStock(UserInfo userInfo) throws Exception;

    /**
     * @description:转为历史库存
     * @author: fengzt
     * @createDate: 2014年10月20日
     * @param itemCodes
     * @return:Map<String, Object>
     */
    Map<String, Object> updateTurnToHistory(String itemCodes);

    /**
     * @description:转为物资库存
     * @author: fengzt
     * @createDate: 2014年10月20日
     * @param itemCodes
     * @return:Map<String, Object>
     */
    Map<String, Object> updateTurnToWuzi(String itemCodes);

    /**
     * @description: 查询当前安全库存给首页展示
     * @author: 890166
     * @createDate: 2015-2-15
     * @param siteId
     * @return:
     */
    List<InvSafetyStockVO> querySafetyStockNow(String siteId);
    
    /**
     * @description: 查询ImtId对应的业务数据的Id
     * @author: 890162
     * @createDate: 2016-4-25
     * @param imtId
     * @return: sheetId
     */
    String getSheetIdByImtId(String imtId,String type);
    
    /**
     * @description:通过itemcode,warehouseid查询启用的分类
     * @author: zhuw
     * @createDate: 2016-7-25
     * @param itemcode
     * @param warehouseid
     * @return
     * @throws Exception :
     */
    List<InvItemVO> queryInvCategory(String itemcode, String warehouseid) throws Exception;
}
