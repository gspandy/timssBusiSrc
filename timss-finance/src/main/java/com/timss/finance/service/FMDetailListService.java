package com.timss.finance.service;

import java.util.List;

import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.vo.FinanceMainDetailVo;

public interface FMDetailListService {
	public boolean insertFMDetailList(String parentId,List<FinanceMainDetail> details);
	
	public boolean updateFMDetailList(String parentId,List<FinanceMainDetail> details);
	
	public boolean deleteFMDetailList(String parentId);
	
	public List<FinanceMainDetailVo> queryFinanceMainDetailVosByParentId(String parentId);
}
