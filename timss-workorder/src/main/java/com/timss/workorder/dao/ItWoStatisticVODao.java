package com.timss.workorder.dao;

import java.util.ArrayList;
import java.util.List;

import com.timss.workorder.vo.ItWoStatisticVO;


public interface ItWoStatisticVODao {
	
	/**
	 * @description:插入一条统计记录
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param itWoStatisticVO
	 * @return:
	 */
	int insertBatchItWoStatisticVO(List<ItWoStatisticVO> itWoStatisticVOList);
	/**
	 * @description:为将一次查询的结果插入到表中，用一个字段PrintNum来标识是某次查询结果
	 * @author: 王中华
	 * @createDate: 2014-8-13
	 * @return:
	 */
	int getNextPrintNum();
	
}
