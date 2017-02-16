package com.timss.attendance.dao;

import java.util.List;

import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.vo.SealApplyVo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;

/**
 * @title: 用章申请DAO
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-08-30
 * @updateUser: 890199
 * @version:1.0
 */
public interface SealApplyDao{
	/**
	 * @description: 查看用章申请列表
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: page
	 * @return:
	 * @throws Exception:
	 */
	//行权限
	@RowFilter(flowIdColumn="sa_no",exclusiveRule="ATD_EXCLUDE",exclusiveDeptRule="ATD_DEPT_EXCLUDE")
	List<SealApplyVo> queryAllSealApply(Page<SealApplyVo> page);
	
	/**
	 * @description: 查看用章申请详情
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: saId
	 * @return:
	 * @throws Exception:
	 */
	List<SealApplyVo> querySealApplyById(String saId);
	
	/**
	 * @description: 新建用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	int insertSealApply(SealApplyBean sealApplyBean); 
	
	
	/**
	 * @description: 更新申请单状态,作废,删除(逻辑)
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	int updateSealApply(SealApplyBean sealApplyBean);
	
	/**
	 * @description: 删除用章申请(物理删除)
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: saId
	 * @return:
	 * @throws Exception:
	 */
	int removeSealApply(String saId);
}
