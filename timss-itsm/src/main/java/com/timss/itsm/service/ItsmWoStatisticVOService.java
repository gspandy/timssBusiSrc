package com.timss.itsm.service;

import java.util.List;

import com.timss.itsm.vo.ItsmWoStatisticVO;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ItsmWoStatisticVOService.java
 * @author: 王中华
 * @createDate: 2015-2-6
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface ItsmWoStatisticVOService {

	
	int insertBatchItWoStatisticVO(List<ItsmWoStatisticVO> itWoStatisticVOList);
	
	int getNextPrintNum();
}
