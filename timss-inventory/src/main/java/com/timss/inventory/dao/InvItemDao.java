package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvItem;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvSafetyStockVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvItemDao.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvItemDao {

    /**
     * @description:查詢物資列表
     * @author: 890166
     * @createDate: 2014-7-11
     * @param page
     * @return
     * @throws Exception:
     */
    List<InvItemVO> queryItemsList(Page<?> page);

    /**
     * @description:通过id查询site
     * @author: 890166
     * @createDate: 2014-7-11
     * @param siteId
     * @return
     * @throws Exception:
     */
    List<Map<String, String>> querySiteById(String siteId);

    /**
     * @description:查询InvItem详细信息
     * @author: 890166
     * @createDate: 2014-7-14
     * @param userInfo
     * @param itemId
     * @return
     * @throws Exception:
     */
    List<InvItemVO> queryInvItemDetail(Map<String, Object> map);

    /**
     * @description:查询详细主项目信息
     * @author: 890166
     * @createDate: 2014-7-16
     * @param map
     * @return:
     */
    List<InvItemVO> queryInvMainItemDetail(Map<String, Object> map);

    /**
     * @description:用id查询item
     * @author: 890166
     * @createDate: 2014-7-18
     * @param itemId
     * @return:
     */
    List<InvItem> queryInvItemByItemid(String itemId);

    /**
     * @description:更新主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return:
     */
    int updateInvItemInfo(InvItem ii);

    /**
     * @description:插入主项目信息
     * @author: 890166
     * @createDate: 2014-7-17
     * @param ii
     * @return:
     */
    int insertInvItemInfo(InvItem ii);

    /**
     * @description:查询到货物资列表
     * @author: 890166
     * @createDate: 2014-7-24
     * @param page
     * @return:
     */
    List<InvItemVO> queryArrivalItem(Page<?> page);

    /**
     * @description:查询物资详细信息
     * @author: 890166
     * @createDate: 2014-8-6
     * @param map
     * @return:
     */
    List<InvItemVO> queryItemInfo(Map<String, Object> map);

    /**
     * @description:查询低于安全库存的物资列表
     * @author: 890147
     * @createDate: 2014-10-11
     * @param page
     * @return:
     */
    List<InvItemVO> queryInvItemSafetyStock(Page<InvItemVO> page);

    /**
     * 查询站点中低于安全库存的物资数量，用于判断是否需要进行安全库存提醒
     * 
     * @param siteId
     * @return
     * @throws Exception
     */
    int queryInvItemSafetyStockNum(String siteId);

    /**
     * @description:转为历史库存
     * @author: 890167
     * @createDate: 2014年10月20日
     * @param params
     * @return:int
     */
    int batchUpdateInvItemHistory(Map<String, Object> params);

    /**
     * @description:查询是否存在物资在流程中
     * @author: 890166
     * @createDate: 2014-10-21
     * @param params
     * @return:
     */
    int queryIsBusyInvItem(Map<String, Object> params);
    
    /**
     * @description:查询物资库存总数量
     * @author: zhuw
     * @createDate: 2016-07-22
     * @param params
     * @return:
     */
    int queryCurItemNum(Map<String, Object> params);

    /**
     * @description: 查询当前安全库存给首页展示
     * @author: 890166
     * @createDate: 2015-2-15
     * @param siteId
     * @return:
     */
    List<InvSafetyStockVO> querySafetyStockNow(String siteId);

    /**
     * @description:
     * @author: yuanzh
     * @createDate: 2016-1-15
     * @param iItem 物资参数
     * @return:
     */
    List<InvItem> queryInvItem(InvItemVO iItem);
    
    String queryImaidByImtId(String imtId);
    String queryIstidByImtId(String imtId);
    String queryInacidByImtId(String imtId);
    String queryImrsidByImtId(@Param("imtId")  String imtId, @Param("type") String type);
    String queryImtidByImtId(String imtId);
    String queryImridByImtId(String imtId);
    
    /**
     * @description:通过itemcode,warehouseid查询启用的分类
     * @author: zhuw
     * @createDate: 2016-7-25
     * @param itemcode
     * @param warehouseid
     * @return
     */
    List<InvItemVO> queryInvCategory(Map<String, Object> map);
}
