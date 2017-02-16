package com.timss.ptw.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.ptw.bean.PtwIslMethodDefine;
import com.timss.ptw.vo.IsMethodPointVo;
import com.yudean.itc.dto.Page;




/**
 * @title: {title} 隔离方法Dao
 * @description: {desc} 隔离方法管理
 * @company: gdyd
 * @className: PtwIslMethodDefineDao.java
 * @author: 王中华
 * @createDate: 2014-10-14
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface PtwIslMethodDefineDao {
	
	
	/**
	 * @description: 查询隔离方法列表
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param page
	 * @return:
	 */
	List<PtwIslMethodDefine> queryPtwIsLMethDefList(Page<PtwIslMethodDefine> page);
	
	/**
	 * @description: 查询某条隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param id
	 * @return:
	 */
	PtwIslMethodDefine queryPtwIsLMethDefById(int id);
	 
	/**
	 * @description: 插入新的隔离方法
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param ptwInfo
	 * @return:
	 */
	int insertPtwIsLMethDef( PtwIslMethodDefine ptwInfo);
	 
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
	 * @description: 查询在站点siteId下是否有no为islMethDefNo的记录
	 * @author: 王中华
	 * @createDate: 2014-10-14
	 * @param islMethDefNo:
	 */
	int queryPtwIsLMethDefByNo(@Param("no") String no,@Param("siteId") String siteId);

	/**
         * 
         * @description:添加隔离证弹出页列表
         * @author: fengzt
         * @createDate: 2014年10月27日
         * @param pageVo
         * @return:List<IsMethodPointVo>
         */
        List<IsMethodPointVo> queryIsMethodBySiteId(Page<HashMap<?, ?>> pageVo);

        /**
         * 
         * @description:添加隔离证弹出页--高级查询
         * @author: fengzt
         * @createDate: 2014年10月27日
         * @param pageVo
         * @return: List<IsMethodPointVo>
         */
        //List<IsMethodPointVo> queryIsMethodBySearch(Page<HashMap<?, ?>> pageVo);
	
	 
}
