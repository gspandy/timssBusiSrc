package com.timss.attendance.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.timss.attendance.bean.SealApplyBean;
import com.timss.attendance.vo.SealApplyVo;
import com.yudean.itc.dto.Page;

/**
 * @title: 用章申请
 * @company: gdyd
 * @author: 890199
 * @createDate: 2016-08-30
 * @updateUser: 890199
 * @version:1.0
 */
public interface SealApplyService {
	/**
	 * @description: 查看用章申请列表
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: page
	 * @return:
	 * @throws Exception:
	 */
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
	 * @description: 提交用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	int commitSealApply(SealApplyBean sealApplyBean) throws Exception;
	
	/**
	 * @description: 暂存用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	int saveSealApply(SealApplyBean sealApplyBean) throws Exception;
	
	/**
	 * @description: 更新用章申请状态
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	int updateSealApply(SealApplyBean sealApplyBean) throws Exception;
	
	/**
	 * @description: 作废用章申请
	 * @author: 890199
	 * @createDate: 2016-08-30
	 * @param: sealApplyBean
	 * @return:
	 * @throws Exception:
	 */
	Map<String,Object> invalidSealApply(String saId);
	
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
