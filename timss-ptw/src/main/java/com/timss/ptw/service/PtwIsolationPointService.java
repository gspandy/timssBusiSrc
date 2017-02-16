package com.timss.ptw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.timss.asset.bean.AssetBean;
import com.timss.ptw.vo.PtwIsolationPointVo;
import com.yudean.itc.dto.Page;

public interface PtwIsolationPointService {
	
	
	/**
	 * @description: 查询某条钥匙箱记录
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param id
	 * @return:
	 */
	ArrayList<PtwIsolationPointVo> queryBeanByIsolationPointNo(String isolationPointId,String siteId);
	 
	
	/**
	 * @description: 新建要是箱
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param paramsDataMap
	 * @return:
	 */
	int insertPtwIsolationPoint( HashMap<String,String> paramsDataMap)throws Exception;
	 
	
	
	/**
	 * @description: 查询所有隔离点配置的隔离方法信息
	 * @author: 王中华
	 * @createDate: 2014-10-22
	 * @param siteId
	 * @return:
	 */
	Page<PtwIsolationPointVo> queryAllBeanBySiteId(Page<PtwIsolationPointVo> page);

	/**
	 * 
	 * @description:动态建立隔离点与隔离方法关联关系
	 * @author: fengzt
	 * @createDate: 2014年11月10日
	 * @param methodIds
	 * @param pointNo
	 * @return:Map<String, Object> 
	 */
	Map<String, Object> saveIslMethodByMethodIds(String methodIds, String pointNo);


	/**
	 * 
	 * @description:插入或者更新隔离点
	 * @author: fengzt
	 * @createDate: 2015年1月5日
	 * @param paramsDataMap
	 * @param assetBean:
	 * @return HashMap<String, Object>
	 * @throws Exception 
	 */
        HashMap<String, Object> insertOrUpdatePtwIsolationPoint(HashMap<String, String> paramsDataMap, AssetBean assetBean) throws Exception;

        /**
         * @description:删除隔离点
         * @author: fengzt
         * @createDate: 2015年1月6日
         * @param assetId
         * @return:int
         */
        int deleteIsolationPoint(String assetId);
	
	
}
