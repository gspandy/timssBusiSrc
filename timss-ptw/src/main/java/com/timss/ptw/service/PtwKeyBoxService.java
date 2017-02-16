package com.timss.ptw.service;

import java.util.HashMap;
import java.util.List;

import com.timss.ptw.bean.PtwKeyBox;
import com.yudean.itc.dto.Page;

public interface PtwKeyBoxService {
	
	/**
	 * @description: 查询列表
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param page
	 * @return:
	 */
	Page<PtwKeyBox> queryPtwKeyBoxList(Page<PtwKeyBox> page);
	
	
	/**
	 * @description: 查询某条钥匙箱记录
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param id
	 * @return:
	 */
	PtwKeyBox queryPtwKeyBoxById(int id);
	 
	
	/**
	 * @description: 新建要是箱
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param paramsDataMap
	 * @return:
	 */
	int insertPtwKeyBox( HashMap<String,String> paramsDataMap);
	 
	
	/**
	 * @description: 更新钥匙箱信息
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param ptwKeyBox
	 * @return:
	 */
	int updatePtwKeyBox( PtwKeyBox  ptwKeyBox);
	
	/**
	 * @description: 删除钥匙箱信息
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param id
	 * @return:
	 */
	int deletePtwKeyBoxById(int id);

	
	/**
	 * @description: 检查钥匙箱编号是否唯一
	 * @author: 王中华
	 * @createDate: 2014-10-15
	 * @param keyBoxNo
	 * @return:
	 */
	Boolean checkKeyBoxNo(String keyBoxNo);
	
	/**
	 * 根据工作票Id或隔离证Id查询钥匙箱
	 * @param ids
	 * @return
	 */
	HashMap<String, Object> queryByWtIdorIslId(String wtId,String islId);
	
	/**
	 * 根据Id的字符串进行查询，以,分隔
	 * @param ids
	 * @return
	 */
	List<PtwKeyBox> queryByIds(String ids);
	
	/**
	 * 更新钥匙箱的状态
	 * @param keyBoxId
	 * @param status
	 * @return
	 */
	int updateKeyBoxStatusByPtwOrIslStatus(Integer keyBoxId, int status);
}
