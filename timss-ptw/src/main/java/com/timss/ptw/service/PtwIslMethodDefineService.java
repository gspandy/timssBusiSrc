package com.timss.ptw.service;

import java.util.HashMap;
import java.util.List;

import com.timss.ptw.bean.PtwIslMethodDefine;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;

public interface PtwIslMethodDefineService {
	/**
	 * @description: 查询隔离方法列表
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param page
	 * @return:
	 */
	Page<PtwIslMethodDefine> queryPtwIsLMethDefList(Page<PtwIslMethodDefine> page);
	
	/**
	 * @description: 查询某条隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param id
	 * @return:
	 */
	PtwIslMethodDefine queryPtwIsLMethDefByPtwId(int id);
	 
	/**
	 * @description: 插入新的隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param ptwInfo
	 * @return:
	 */
	int insertPtwIsLMethDef( HashMap<String,String> paramsDataMap);
	 
	/**
	 * @description: 修改隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param ptwInfo
	 * @return:
	 */
	int updatePtwIsLMethDef( PtwIslMethodDefine  ptwInfo);
	 
	/**
	 * @description: 删掉隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param id
	 * @return:
	 */
	int deletePtwIsLMethDefById(int id);

	/**
	 * @description: 检查库中是是否没有no，是：true  否 ： false
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param islMethDefNo:
	 */
	Boolean checkIslMethNo(String islMethDefNo);

	/**
	 * 
	 * @description:添加隔离证弹出页--高级查询
	 * @author: fengzt
	 * @createDate: 2014年10月27日
	 * @param map
	 * @param pageVo
	 * @return:List<IsMethodPointVo>
	 */
        List<IsMethodPointVo> queryIsMethodBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);
        /**
         * 
         * @description:添加隔离证弹出页列表
         * @author: fengzt
         * @createDate: 2014年10月27日
         * @param pageVo
         * @return:List<IsMethodPointVo>
         */
        List<IsMethodPointVo> queryIsMethodBySiteId(Page<HashMap<?, ?>> pageVo);
}
