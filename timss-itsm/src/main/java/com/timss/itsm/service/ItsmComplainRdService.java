package com.timss.itsm.service;

import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.timss.itsm.bean.ItsmComplainRd;
import com.timss.itsm.vo.ItsmComplainRdVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

public interface ItsmComplainRdService {
	
	/**根据id查找
	 * @param complainRdIdVO
	 * @return
	 */
	ItsmComplainRdVO queryCpRdById(String complainRdId);
	/**
	 * 列表页面查询
	 */
	Page<ItsmComplainRd> queryComplainRdList(Page<ItsmComplainRd> page);
	/**
	 * @description:更新投诉记录的状态
	 */
	void updateComplainRdStatus(@Param("complainRdId")String complainRdId ,@Param("currStatus")String currStatus);
	/**
	 * @description:修改当前处理人机流程信息
	 * @param complainRdId
	 * @param userInfoScope
	 * @param flag :正常流程时为“normal”，回退时为“rollback”
	 */
	void updateCurrHandlerUser(String complainRdId,UserInfoScope userInfoScope,String flag) throws Exception;
	
	/**对于一些回退的记录进行跟新
	 * @param complainRdVO
	 * @return
	 * @throws Exception
	 */
	ItsmComplainRdVO updateComplainRd(ItsmComplainRdVO complainRdVO) throws Exception;
	/**
	 * @description: 回退到创建人之后，再提交
	 */
	void rollbackCommit(ItsmComplainRdVO complainRdVO) throws Exception;
	
	/**插入
	 * @param complainRdVO
	 * @return
	 * @throws Exception
	 */
	ItsmComplainRdVO insertComplainRd(ItsmComplainRdVO complainRdVO) throws Exception;
	
	/**暂存
	 * @param complainRdVO
	 * @return
	 * @throws Exception
	 */
	ItsmComplainRdVO saveComplainRd(ItsmComplainRdVO complainRdVO) throws Exception;
	
	/**
	 * @description: 作废工单
	 */
	void obsoleteWorkOrder(String complainRdId);
	/**
	 * @description: 删除工单
	 */
	void deleteWorkOrder(String complainRdId);
}
