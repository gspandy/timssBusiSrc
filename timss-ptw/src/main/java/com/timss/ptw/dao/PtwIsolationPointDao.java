package com.timss.ptw.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwIsolationPoint;
import com.timss.ptw.vo.IsMethodPointVo;
import com.timss.ptw.vo.PtwIsolationPointVo;
import com.yudean.itc.dto.Page;




public interface PtwIsolationPointDao {
	
	ArrayList<PtwIsolationPointVo> queryBeanByIsolationPointNo(@Param("pointNo")String pointNo ,@Param("siteId")String siteId);
	 
	
	int insertPtwIsolationPoint( PtwIsolationPoint ptwIsolationPoint);
	 
	
	/**
	 * @description:删除隔离点对应的所有隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-20
	 * @param pointNo
	 * @param siteId
	 * @return:
	 */
	int deletePtwIsolationPointByPointNo(@Param("pointNo")String pointNo,@Param("siteId")String siteId);


	/**
	 * @description: 删除隔离点配置的某条隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-21
	 * @param id:
	 */
	void deletePtwIsolationPointById(@Param("id") int id);


	/**
	 * @description: 查询所有配置隔离方法的隔离点对应关系
	 * @author: 王中华
	 * @createDate: 2014-10-22
	 * @param siteId:
	 * @return 
	 */
	ArrayList<PtwIsolationPointVo> queryAllBeanVoBySiteId(Page<PtwIsolationPointVo> page);
	
	/**
	 * 
	 * @description:查询隔离点与隔离方法关联是否已经存在关联
	 * @author: fengzt
	 * @createDate: 2014年11月10日
	 * @param temp
	 * @return:PtwIsolationPoint
	 */
	PtwIsolationPoint queryIslMehtodByPoinIdAndMethodId(PtwIsolationPoint ptwIsolationPoint );

	/**
	 * 
	 * @description:查询隔离点与隔离方法关联
	 * @author: fengzt
	 * @createDate: 2014年11月10日
	 * @param ptwIsolationPoint
	 * @return:PtwIsolationPoint
	 */
    List<IsMethodPointVo> queryIsMethodByIsolationPoint(PtwIsolationPoint ptwIsolationPoint);

    /**
     * 
     * @description:删除隔离点
     * @author: fengzt
     * @createDate: 2015年1月6日
     * @param assetId
     * @return:int
     */
    int deleteIsolationPoint(String assetId);
	 
}
