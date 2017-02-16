package com.timss.inventory.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.timss.inventory.bean.InvMatTranRec;
import com.timss.inventory.vo.InvMatTranRecVO;

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
public interface InvMatTranRecDao {

    /**
     * @description:将数据插入交易信息新表
     * @author: 890151
     * @createDate: 2016-5-25
     * @param imtd
     * @return:
     */
    int insertInvMatTranRec ( InvMatTranRec imtr );

    /**
     * @description:将数据更新交易信息新表
     * @author: 890151
     * @createDate: 2016-5-25
     * @param imtd
     * @return:
     */
    int updateInvMatTranRec ( InvMatTranRec imtr );
    
    
    /**
     * @description: 逻辑是按照itemid、invcateid和siteid三个字段来查询可以出库的批次
     * @author: 890151
     * @createDate: 2016-5-25
     * @param itemId 物资id
     * @param invcateid 物资分类id
     * @param siteId 站点id
     * @return:
     */
    List< InvMatTranRec > queryTranRecByBatch ( @Param ( "itemId" ) String itemId ,
	    @Param ( "invcateid" ) String invcateid , @Param ( "siteId" ) String siteId );
    /**
     * @description: 按照imtdid三个字段来查询流水记录
     * @author: 890162
     * @createDate: 2016-6-1
     * @param imtdid 
     * @return:
     */
    List< InvMatTranRec > queryTranRecByimtdid ( @Param ( "imtdid" ) String imtdid );
    /**
     * @description:删除交易明细表
     * @author: 890166
     * @createDate: 2014-7-25
     * @param imtid
     * @return:
     */
    int deleteMatTranRecByImtid ( String imtid );
    /**
     * @description:删除交易明细表
     * @author: 890151
     * @createDate: 2016-06-22
     * @param imtdid
     * @return:
     */
    int deleteMatTranRecById ( String id );
    
    /**
     * @description:查询领料单详情某个物资相关联的交易流水信息
     * @author: 890151
     * @createDate: 2016-7-26
     * @param imadId
     * @return
     */
	List<InvMatTranRecVO> queryRelateTranRecByImadId(String imadId);
}
