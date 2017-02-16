package com.timss.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import com.timss.asset.bean.AssetBean;
import com.timss.asset.vo.AssetVo;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailService.java
 * @author: 890166
 * @createDate: 2014-7-15
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatTranDetailService {

    /**
     * 查询库存情况
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-15
     * @param userInfo
     * @param itemCode
     * @return
     * @throws Exception :
     */
    Page< InvMatTranDetailVO > queryStockInfo ( UserInfoScope userInfo , String itemCode , String wareId, String invcateId )
	    throws Exception;

    /**
     * @description:查询当前领料申请已经出库的记录
     * @author: 890166
     * @createDate: 2014-9-26
     * @param userInfo
     * @param imaid
     * @return
     * @throws Exception:
     */
    Page< InvMatTranDetailVO > queryAlreadyOut ( UserInfoScope userInfo , String imaid ) throws Exception;

    /**
     * 查询库存操作信息
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-15
     * @param userInfo
     * @param itemCode
     * @return
     * @throws Exception :
     */
    Page< InvMatTranDetailVO > queryStockOperInfo ( UserInfoScope userInfo , String itemCode , InvMatTranDetailVO imtdv )
	    throws Exception;

    /**
     * 表单中列表数据查询
     * 
     * @description:
     * @author: 890166
     * @createDate: 2014-7-23
     * @param userInfo
     * @param imtid
     * @return
     * @throws Exception :
     */
    Page< InvItemVO > queryMatTranDetail ( UserInfoScope userInfo , String imtid , String openType ) throws Exception;

    /**
     * @description:获取最新的批次号
     * @author: 890166
     * @createDate: 2014-8-20
     * @param outterid
     * @return
     * @throws Exception :
     */
    String queryMatTranMaxLotno ( String outterid ) throws Exception;

    /**
     * @description:自动触发物资接收后会产生无效的初始化数据，获取出来将其删除
     * @author: 890166
     * @createDate: 2014-8-28
     * @param dbId
     * @return
     * @throws Exception:
     */
    void deleteNoMainDetailDataBydbId ( String dbId ) throws Exception;

    /**
     * @description: 解除绑定物资类型
     * @author: 890166
     * @createDate: 2015-1-4
     * @param iwiid
     * @return
     * @throws Exception:
     */
    boolean deleteBindWarehouse ( String iwiid ) throws Exception;

    /**
     * @description: 通过站点查询实时的库存金额
     * @param siteId
     * @return
     * @throws Exception
     */
    String queryInvPriceTotal ( String siteId ) throws Exception;


    /**
     * @description: 通过站点根据新流水表查询库存总金额
     * @param siteId 
     * @param wareHouseId 仓库ID，如果不限，则传入null
     * @return
     * @throws Exception
     */
    BigDecimal queryStockMoneyTotal ( String siteId, String wareHouseId ) throws Exception;

    /**
     * @description:根据采购合同id获得物资领料id
     * @author: 890162
     * @createDate: 2015-10-08
     * @param outterid
     * @return List<String>
     */
    List< String > queryMatTranSheetNoByOutterId ( String outterid );

    /**
     * @description:逻辑是按照itemid、invcateid和siteid三个字段来查询可以出库的批次
     * @author: yuanzh
     * @param itemId 物资id
     * @param invcateid 物资分类id
     * @param siteId 站点id
     * @return
     */
    List< InvMatTranDetail > queryTranDetailByBatch ( String itemId , String invcateid , String siteId );

    /**
     * @description: 根据imtdid获取资产化记录次数
     * @author: yucz
     * @createDate: 2016-11-28
     * @param imtdId
     * @return:
     */
    int queryAssetApplyByImtdId(String imtdId);
    
    /**
     * @description:查询接收单详情某个物资资产化记录
     * @author: 890199
     * @createDate: 2016-12-01
     * @param imtdId
     * @return
     */
    List<AssetVo> queryRelateAssetByImtId(String imtId);
    
}
