package com.timss.inventory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatRecipients;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatRecipientsDetailVO;
import com.timss.inventory.vo.InvMatRecipientsVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsService.java
 * @author: 890166
 * @createDate: 2014-9-28
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatRecipientsService {

    /**
     * @description:自动生成领用情况信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception:
     */
    Map< String , Object > autoGenerateConsuming ( UserInfoScope userInfo , InvMatApply ima ,
	    List< InvMatApplyDetailVO > imaList , Map< String , Object > paramMap ) throws Exception;

    /**
     * @description:查询表单信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    List< InvMatRecipientsVO > queryInvMatRecipientsForm ( UserInfoScope userInfo , String imrid ) throws Exception;

    /**
     * @description:查看列表信息
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    Page< InvMatRecipientsDetailVO > queryInvMatRecipientsList ( UserInfoScope userInfo , String imrid )
	    throws Exception;

    
    /**
     * @description: 查询某个物资领料单中某类物资的多次发料记录
     * @author: 王中华
     * @createDate: 2016-5-31
     * @param imaid 物资领料单ID
     * @param siteid 站点id
     * @param itemid  物资id
     * @return:
     */
    List< InvMatRecipientsDetailVO > queryInvMatRecipListByItem(String imaid,String siteid,String itemid,String warehouseid);
    /**
     * @description:保存方法
     * @author: 890166
     * @createDate: 2014-9-28
     * @param userInfo
     * @param imr
     * @param imrList
     * @param paramMap
     * @return
     * @throws Exception:
     */

    Map< String , Object > saveRecipientsTran ( UserInfoScope userInfo , InvMatRecipients imr ,
	    List< InvMatRecipientsDetailVO > imrList , String uploadIds) throws Exception;

    /**
     * @description:根据sheetno查询id
     * @author: 890166
     * @createDate: 2014-10-24
     * @param sheetNo
     * @return
     * @throws Exception:
     */
    String queryRecipientsIdBySheetNo ( UserInfoScope userInfo , String sheetNo ) throws Exception;

    /**
     * @description: 通过imaid查询发料单数据
     * @author: 890166
     * @createDate: 2015-1-6
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    List< InvMatRecipientsVO > queryRecipientsByImaId ( UserInfoScope userInfo , String imaid ) throws Exception;

    /**
     * @description:验证物资是否能够出库
     * @author: 890166
     * @createDate: 2015-6-17
     * @param userInfo
     * @param imrid
     * @return
     * @throws Exception:
     */
    Map< String , Object > validateCanOut ( UserInfoScope userInfo , List< InvMatRecipientsDetailVO > imrList )
	    throws Exception;
    
    /**
     * @description:根据发料单ID删除发料主表和子表记录
     * @author: 890151
     * @createDate: 2016-8-25
     * @param imrId
     * @param siteId
     * @return
     * @throws Exception:
     */
    boolean deleteInvMatRecipientsByImrId ( String imrId, String siteId ) throws Exception;
    
    /**
     * @description:查看物资发料单列表
     * @author: 890199
     * @createDate: 2016-11-09
     * @param siteId
     * @return
     * @throws Exception:
     */
    List<InvMatRecipientsVO> queryInvMatRecipientsApplyList (Page<InvMatRecipientsVO> page);
    
    /**
     * @description:保存附件功能
     * @author: 890199
     * @createDate: 2017-01-13
     * @param paramMap
     * @return
     * @throws Exception:
     */
    Map<String, Object> saveInvMatRecipientsAttach (Map<String, Object> paramMap);
    
    /**
     * @description:获取附件
     * @author: 890199
     * @createDate: 2017-01-16
     * @param paramMap
     * @return
     * @throws Exception:
     */
	List<Map<String, Object>> queryMatRecipientsAttach(String istid) throws Exception;
}
