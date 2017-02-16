package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsDao.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatRecipientsDao {

    /**
     * @description:插入领用情况
     * @author: 890166
     * @createDate: 2014-9-28
     * @param imr
     * @return:
     */
    int insertInvMatRecipients(InvMatRecipients imr);

    /**
     * @description:更新领用情况
     * @author: 890151
     * @createDate: 2016-11-9
     * @param imr
     * @return:
     */
    int updateInvMatRecipients(InvMatRecipients imr);

    /**
     * @description:查询表单信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    List<InvMatRecipientsVO> queryInvMatRecipientsForm(Map<String, Object> map);

    /**
     * @description:查看列表信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    List<InvMatRecipientsDetailVO> queryInvMatRecipientsList(Page<?> page);

    /**
     * @description:根据sheetno查询id
     * @author: 890166
     * @createDate: 2014-10-24
     * @param sheetNo
     * @return
     * @throws Exception:
     */
    String queryRecipientsIdBySheetNo(Map<String, Object> map);

    /**
     * @description: 通过imaid查询发料单数据
     * @author: 890166
     * @createDate: 2015-1-6
     * @param map
     * @return:
     */
    List<InvMatRecipientsVO> queryRecipientsByImaId(Map<String, Object> map);

    /**
     * @description: 通过imrid查询发料子表数据
     * @author: 890151
     * @createDate: 2016-6-2
     * @param map
     * @return:
     */
    List<InvMatRecipientsDetailVO> queryRecipientsDetailByImrId(Map<String, Object> map);

    /**
     * @description: 查询某个领料单下的同仓库同一种物资的多次发料记录
     * @author: 890152
     * @createDate: 2016-5-31
     * @param imaid 领料单id
     * @param siteid 站点id
     * @param itemid 物资id
     * @return:
     */
    List<InvMatRecipientsDetailVO> queryInvMatRecipListByItem(@Param("imaid")String imaid, 
            @Param("siteid")String siteid, @Param("itemid")String itemid,@Param("warehouseid")String warehouseid);
    
    /**
     * @description:根据领料单ID删除未发料的发料单主表信息
     * @author: 890151
     * @createDate: 2016-8-25
     * @param imrId
     * @return
     * @throws Exception:
     */
    int deleteInvMatRecipients(@Param("imrId") String imrId, @Param("siteId")String siteId);

    /**
     * @description:查看物资发料单列表
     * @author: 890199
     * @createDate: 2016-11-09
     * @param siteId
     * @return
     * @throws Exception:
     */
    List<InvMatRecipientsVO> queryInvMatRecipientsApplyList(Page<InvMatRecipientsVO> page);
    
    /**
     * @description:根据物资编号或名称获取该站点下物资发料列表
     * @author: 890199
     * @createDate: 2016-11-10
     * @param siteId
     * @return
     * @throws Exception:
     */
    List<InvMatRecipientsVO> searchInvMatRecipientsApplyList(Page<InvMatRecipientsVO> page);
}
