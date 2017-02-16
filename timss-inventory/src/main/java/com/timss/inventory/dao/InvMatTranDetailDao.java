package com.timss.inventory.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.asset.vo.AssetVo;
import com.timss.inventory.bean.InvMatTranDetail;
import com.timss.inventory.vo.InvItemVO;
import com.timss.inventory.vo.InvMatTranDetailVO;
import com.timss.inventory.vo.InvWarehouseItemVO;
import com.timss.purchase.vo.PurRemainVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatTranDetailDao.java
 * @author: 890166
 * @createDate: 2014-7-15
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatTranDetailDao {
    /*------------------------旧的流水表增删改在此处，新的流水表增删改在InvMatTranRecDao中-------------------------------------------*/
    /**
     * @description:将数据插入交易信息表
     * @author: 890166
     * @createDate: 2014-7-18
     * @param imtd
     * @return:
     */
    int insertInvMatTranDetail ( InvMatTranDetail imtd );

    /**
     * @description:将数据更新交易信息表
     * @author: 890166
     * @createDate: 2014-8-20
     * @param imtd
     * @return:
     */
    int updateInvMatTranDetail ( InvMatTranDetail imtd );

    /**
     * @description:删除交易明细表
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imtid
     * @return:
     */
    int deleteMatTranDetailByImtid ( String imtid );
    /**
     * @description:删除交易明细表
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imtid
     * @return:
     */
    int deleteMatTranDetailById ( String id );
    
    
    
    
    
    
    /*------------------------新的流水表查询都放在该Dao中-------------------------------------------*/
    /**
     * @description:查询库存情况
     * @author: 890166
     * @createDate: 2014-7-15
     * @param map
     * @return
     * @throws Exception :
     */
    List< InvMatTranDetailVO > queryStockInfo ( Map< String , Object > map );

    /**
     * @description:查询库存操作信息
     * @author: 890166
     * @createDate: 2014-7-15
     * @param map
     * @return:
     */
    List< InvMatTranDetailVO > queryStockOperInfo ( Page< ? > page );

    /**
     * @description:查询当前领料申请已经出库的记录
     * @author: 890166
     * @createDate: 2014-9-26
     * @param page
     * @return:
     */
    List< InvMatTranDetailVO > queryAlreadyOut ( Page< ? > page );

    /**
     * @description:通过id查询InvMatTranDetail
     * @author: 890166
     * @createDate: 2014-7-18
     * @param itemid
     * @return:
     */
    List< InvMatTranDetail > queryInvMatTranDetailByImtdid ( String imtdid );

    /**
     * @description:通过Item信息查询InvMatTranDetail
     * @author: 890166
     * @createDate: 2014-7-16
     * @param map
     * @return:
     */
    List< InvMatTranDetail > queryInvMatTranDetailByItemInfo ( Map< String , Object > map );
    
    /**
     * @description:表单中列表数据查询
     * @author: 890166
     * @createDate: 2014-7-23
     * @param page
     * @return:
     */
    List< InvItemVO > queryMatTranDetail ( Page< ? > page );
    
    /**
     * @description: 解除物资类型绑定
     * @author: 890166
     * @createDate: 2015-1-4
     * @param iwiid
     * @return:
     */
    int deleteBindWarehouse ( String iwiid );

    /**
     * @description:通过imtid查询InvMatTranDetail
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imtid
     * @return:
     */
    List< InvMatTranDetail > queryMatTranDetailByImtid ( String imtid );

    /**
     * @description:获取最新的批次号
     * @author: 890166
     * @createDate: 2014-8-20
     * @param outterid
     * @return
     * @throws Exception :
     */
    String queryMatTranMaxLotno ( String outterid );

    /**
     * @description:根据采购单号获取物资接收
     * @author: 890162
     * @createDate: 2015-10-08
     * @param outterid
     * @return
     * @throws Exception :
     */
    List< String > queryMatTranSheetNoByOutterId ( String outterid );

    /**
     * @description:自动触发物资接收后会产生无效的初始化数据，获取出来将其删除
     * @author: 890166
     * @createDate: 2014-8-28
     * @param dbId
     * @return
     * @throws Exception :
     */
    List< InvMatTranDetail > getNoMainDetailDataBydbId ( Map< String , Object > map );

    /**
     * @description:查询还剩多少物资没有入库
     * @author: 890166
     * @createDate: 2014-10-16
     * @param imtdid
     * @return:
     */
    List< PurRemainVO > queryRemainWarehouseNum ( String imtdId );

    /**
     * @description: 通过站点查询实时的库存金额
     * @author: 890166
     * @createDate: 2015-2-4
     * @return
     * @throws Exception
     */
    BigDecimal queryInvPriceTotal ( Map< String , Object > map );

    /**
     * @description: 根据实时表查询含税单价库存总金额 
     * @author: 890151
     * @createDate: 2016-7-19
     * @return
     * @throws Exception
     */
    BigDecimal queryInvWithTaxPriceTotal ( Map< String , Object > map );

    /**
     * @description: 根据实时表查询不含税单价库存总金额
     * @author: 890151
     * @createDate: 2016-7-19
     * @return
     * @throws Exception
     */
    BigDecimal queryInvNoTaxPriceTotal ( Map< String , Object > map );

    /**
     * @description: 查询库存总金额（含税）
     * @author: 890151
     * @createDate: 2016-10-28
     * @return
     * @throws Exception
     */
    BigDecimal queryStockMoneyWithTaxTotal ( Map< String , Object > map );

    /**
     * @description: 查询库存总金额（不含税）
     * @author: 890151
     * @createDate: 2016-10-28
     * @return
     * @throws Exception
     */
    BigDecimal queryStockMoneyNoTaxTotal ( Map< String , Object > map );

    /**
     * @description: 查询当前库存量
     * @author: 890166
     * @createDate: 2015-6-17
     * @param map
     * @return:
     */
    BigDecimal queryInvStockQty ( Map< String , Object > map );

    /**
     * @description:物资领料页面查询退库列表
     * @author: yuanzh
     * @createDate: 2015-9-24
     * @param page
     * @return:
     */
    List< InvMatTranDetailVO > queryAlreadyRefunding ( Page< ? > page );

    /**
     * @description: 根据物资接收单ID，判断此接收单是否完全接收
     * @author: 890152
     * @createDate: 2015-10-9
     * @param imtid
     * @return:
     */
    int queryIsReviceAllItemBySheetNo ( @Param ( "sheetNo" ) String sheetNo );

    /**
     * @description: 流水表联合查询主项目新建项目
     * @author: user
     * @createDate: 2016-3-23
     * @param iwiv
     * @return:
     */
    InvMatTranDetailVO queryInvMatTranDetailUnionIMT ( @Param ( "InvWarehouseItemVO" ) InvWarehouseItemVO iwiv );

    /**
     * @description: 逻辑是按照itemid、invcateid和siteid三个字段来查询可以出库的批次
     * @author: yuanzh
     * @createDate: 2016-5-4
     * @param itemId 物资id
     * @param invcateid 物资分类id
     * @param siteId 站点id
     * @return:
     */
    List< InvMatTranDetail > queryTranDetailByBatch ( @Param ( "itemId" ) String itemId ,
	    @Param ( "invcateid" ) String invcateid , @Param ( "siteId" ) String siteId );
    
    /**
     * @description: 根据imtdid获取资产化记录次数
     * @author: yucz
     * @createDate: 2016-11-28
     * @param imtdId
     * @return:
     */
    int queryAssetApplyByImtdId(String imtdId);
    
    /**
     * @description: 根据imtdid获取资产化记录
     * @author: yucz
     * @createDate: 2016-12-01
     * @param imtdId
     * @return:
     */
    List<AssetVo> queryRelateAssetByImtId(String imtId);
}
