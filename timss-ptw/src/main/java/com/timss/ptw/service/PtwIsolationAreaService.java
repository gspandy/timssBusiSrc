package com.timss.ptw.service;

import java.util.HashMap;
import java.util.List;

import com.timss.ptw.bean.PtwIsolationArea;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;

public interface PtwIsolationAreaService {
	
	/**
	 * @description: 查询列表
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param page
	 * @return:
	 */
	Page<PtwIsolationArea> queryPtwIsolationAreaList(Page<PtwIsolationArea> page);
	
	
	/**
	 * @description: 查询某条钥匙箱记录
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param id
	 * @return:
	 */
	PtwIsolationArea queryPtwIsolationAreaById(int id);
	 
	
	/**
	 * @description: 新建要是箱
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param paramsDataMap
	 * @return:
	 */
	int insertPtwIsolationArea( HashMap<String,String> paramsDataMap)throws Exception;
	 
	
	/**
	 * @description: 更新钥匙箱信息
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param ptwIsolationArea
	 * @return:
	 */
	int updatePtwIsolationArea( HashMap<String,String> paramsDataMap)throws Exception;
	
	/**
	 * @description: 删除钥匙箱信息
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param id
	 * @return:
	 */
	int deletePtwIsolationAreaById(int id);

	
	/**
	 * @description: 检查钥匙箱编号是否唯一
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param keyBoxNo
	 * @return:
	 */
	Boolean checkIsolationAreaNo(String keyBoxNo);

	/**
	 * 
	 * @description:通过隔离证ID查找列表
	 * @author: fengzt
	 * @createDate: 2014年10月28日
	 * @param areaId
	 * @return:List<IsMethodPointVo>
	 */
	List<IsMethodPointVo> queryIsolationMethodList(String areaId);
}
