package com.timss.itsm.dao;

import com.timss.itsm.bean.ItsmComplainRd;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ItsmComplainRdDao {
	
	/**插入
	 * @param complainRd
	 * @return
	 */
	int insertComplainRd(ItsmComplainRd complainRd);
	
	/**跟新
	 * @param complainRd
	 * @return
	 */
	int updateComplainRd(ItsmComplainRd complainRd);
	
	/**删除
	 * @param id
	 */
	void deleteComplainRd(String id);
	
	/**根据id查找
	 * @param complainRdId
	 * @return
	 */
	ItsmComplainRd queryCpRdById(String complainRdId);
	
	/**列表页面查询
	 * @param page
	 * @return
	 */
	@RowFilter(flowIdColumn="CODE")
	List<ItsmComplainRd> queryComplainRdList(Page<ItsmComplainRd> page);
	
	/**
	 * @description:更新投诉记录的状态
	 */
	int updateComplainRdStatus(ItsmComplainRd complainRd);

	/**
	 * @description:修改当前处理人的信息
	 */
	void updateCurrHandUserById(Map<String, String> parmas);
	
}
