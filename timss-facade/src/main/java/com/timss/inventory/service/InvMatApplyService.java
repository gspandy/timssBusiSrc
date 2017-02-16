package com.timss.inventory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatApply;
import com.timss.inventory.bean.InvMatApplyDetail;
import com.timss.inventory.vo.InvMatApplyDetailVO;
import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.inventory.vo.InvMatApplyVO;
import com.yudean.itc.annotation.CalulateItem;
import com.yudean.itc.annotation.CalulateItemEntry;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyService.java
 * @author: 890166
 * @createDate: 2014-7-26
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatApplyService {

    /**
     * @description:物资领料列表数据
     * @author: 890166
     * @createDate: 2014-7-27
     * @param userInfo
     * @param ima
     * @return
     * @throws Exception :
     */
    Page< InvMatApplyVO > queryMatApplyList ( UserInfoScope userInfo , InvMatApplyVO ima ) throws Exception;

    /**
     * @description: 查询表单数据
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    List< InvMatApplyVO > queryMatApplyForm ( UserInfoScope userInfo , String imaid , String type ) throws Exception;

    /**
     * @description: 查询物资领料关联的采购申请
     * @author: 890162
     * @createDate: 2016-8-24
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception :
     */
    List<String> queryPurApplyOfMatApply (UserInfoScope userInfo ,String imaid ,String type ) throws Exception;
    /**
     * @description:保存信息
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    @CalulateItem ( caluType = "All" )
    Map< String , Object > saveMatApply ( UserInfoScope userInfo , InvMatApply ima ,
	    @CalulateItemEntry List< InvMatApplyDetail > imaList , Map< String , Object > paramMap ) throws Exception;

    /**
     * @description:保存交易信息
     * @author: 890166
     * @createDate: 2014-7-30
     * @param userInfo
     * @param ima
     * @param imaList
     * @param paramMap
     * @return
     * @throws Exception :
     */
    Map< String , Object > savePickingTran ( UserInfoScope userInfo , InvMatApply ima ,
	    List< InvMatApplyDetailVO > imaList , HashMap< String , Object > paramMap ) throws Exception;

    /**
     * @description:查询物资类型
     * @author: 890166
     * @createDate: 2014-7-28
     * @param userInfo
     * @return
     * @throws Exception :
     */
    /*
     * List<HashMap<String, String>> queryApplyType(UserInfoScope userInfo)
     * throws Exception;
     */

    /**
     * @description:更新主单信息
     * @author: 890166
     * @createDate: 2014-9-18
     * @param ima
     * @return
     * @throws Exception:
     */
    int updateMatApply (InvMatApply ima ) throws Exception;
    
    /**
     * @description:更新主单信息(触发实时表更新)
     * @author: zhuw
     * @createDate: 2016-07-07
     * @param ima
     * @return
     * @throws Exception:
     */
    @CalulateItem ( caluType = "All" )
    int updateInvMatApply ( @CalulateItemEntry List< InvMatApplyDetail > imaList , InvMatApply ima ) throws Exception;

    /**
     * 工单接口，操作库存数据
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-16
     * @param json
     * @return
     * @throws Exception :
     */
    void workOrderTriggerProcesses ( HashMap< String , Object > hMap ) throws Exception;

    /**
     * @description:通过woid查询领用申请信息
     * @author: 890166
     * @createDate: 2014-9-30
     * @param woId
     * @return
     * @throws Exception:
     */
    Page< InvMatApplyToWorkOrder > queryMatApplyByWoId ( String woId , String applyType ) throws Exception;

    /**
     * @description: 通过外部ID找到领料申请信息
     * @author: yuanzh
     * @createDate: 2015-12-17
     * @param woId
     * @param applyType
     * @return
     * @throws Exception:
     */
    List< InvMatApply > queryMatApplyByOutterId ( String woId , String applyType ) throws Exception;

    /**
     * @description: 通过sheetNo和站点id找到申请表id
     * @author: 890166
     * @createDate: 2014-9-23
     * @param sheetNo
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryImaIddByFlowNo ( String sheetNo , String siteId ) throws Exception;

    /**
     * @description: 通过申请表id和站点id找到sheetNo
     * @author: 890166
     * @createDate: 2014-9-23
     * @param imaId
     * @param siteId
     * @return
     * @throws Exception:
     */
    String queryFlowNoByImaId ( String imaId , String siteId ) throws Exception;


    /**
     * @description: 终止领料
     * @author: 890151
     * @createDate: 2016年9月12日
     * @param imaId
     * @param taskId
     * @return
     * @throws Exception:
     */
    Map< String , Object > stopSend(String imaId, String taskId) throws Exception;
}
