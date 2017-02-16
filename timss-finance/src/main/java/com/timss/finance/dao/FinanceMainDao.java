package com.timss.finance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.finance.bean.FinanceMain;
import com.yudean.itc.dto.Page;

public interface FinanceMainDao {
	/** 
	 * @description: 查找报销单列表信息
	 * @author: 890170
	 * @createDate: 2014-12-16
	 */
	List<FinanceMain> queryFinanceMainList(Page<FinanceMain> page);
	
	/**
	 * @description:增加
	 * @author: wus
	 * @createDate: 2014-6-20
	 * @param 
	 */
	 void insertFinanceMain(FinanceMain financemain);

	 /**
	 * 
	 * @description:查找ByFid
	 * @author: wus
	 * @createDate: 2014-6-23
	 * @param fid
	 */
	 FinanceMain queryFinanceMainByFid(String fid);
	 
	 /**
	  * 
	  * @description:更新ByFid
	  * @author: wus
	  * @createDate: 2014-6-25
	  * @param fid
	  * 
	  * */
	 void updateFinanceMainByFid(FinanceMain financemain);
	 
	 /**
	  * 
	  * @description:deleteFinanceMain,删除
	  * @author: wus
	  * @createDate: 2014-6-27
	  * @param fid
	  * 
	  * */
	 void deleteFinanceMain(String fid) ;
	 /**
	  * 
	  * @description:deleteFinanceMainLogically,逻辑删除
	  * @author: gchw
	  * @createDate: 2015-4-23
	  * @param fid
	  * 
	  * */
	 void deleteFinanceMainLogically(String fid) ;

	/** 
	 * @description: 查询所有报销单 
	 * @author: 890170
	 * @createDate: 2015-1-8
	 */
	List<FinanceMain> queryAllFinanceMainList(Page<FinanceMain> page);

    /**
     * @description:根据申请单，查询关联的费用报销单
     * @author: 王中华
     * @createDate: 2016-6-8
     * @param applyId
     * @param siteid
     * @return:
     */
    List<FinanceMain> queryFinanceMainListByApplyId(@Param("applyId")String applyId, @Param("siteid")String siteid);

    /**
     * @description:更新报销总金额
     * @author: 王中华
     * @createDate: 2016-9-27
     * @param fid
     * @param total_amount:
     */
    void updateFinanceTotalAmount(@Param("fid")String fid, @Param("total_amount")double total_amount);
}
