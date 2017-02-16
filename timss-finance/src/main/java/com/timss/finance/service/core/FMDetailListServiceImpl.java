package com.timss.finance.service.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinanceMainDetailDao;
import com.timss.finance.service.FMDetailListService;
import com.timss.finance.vo.FinanceMainDetailVo;
import com.yudean.mvc.service.ItcMvcService;
@Service 
public class FMDetailListServiceImpl implements FMDetailListService{
    @Autowired
    FinanceMainDetailDao financeMainDetailDao;
    @Autowired
    ItcMvcService itcMvcService;
	@Override
	public boolean insertFMDetailList(String parentId,
			List<FinanceMainDetail> details) {
		initFMDetailList(parentId,details);
		if(details!=null){
			for(int i=0;i<details.size();i++){
				financeMainDetailDao.insertFinanceMainDetail(details.get(i));
			}
		}
		return true;
	}

	private void initFMDetailList(String parentId,
			List<FinanceMainDetail> details) {
		if(details!=null){
			for(int i=0;i<details.size();i++){
				FinanceMainDetail financeMainDetail=details.get(i);
				financeMainDetail.setFid(parentId);
				InitUserAndSiteIdNewUtil.initCreate(financeMainDetail, itcMvcService);
			}
		}
		
	}

	@Override
	public boolean updateFMDetailList(String parentId,
			List<FinanceMainDetail> details) {
		deleteFMDetailList(parentId);
		insertFMDetailList(parentId, details);
		return true;
	}

	@Override
	public boolean deleteFMDetailList(String parentId) {
		financeMainDetailDao.deleteFinanceMainDetail(parentId);
		return true;
	}

	@Override
	public List<FinanceMainDetailVo> queryFinanceMainDetailVosByParentId(
			String parentId) {
		List<FinanceMainDetail> lists=financeMainDetailDao.queryFinanceMainDetailByFid(parentId);
		
		return convert2detailVos(lists);
	}

	private List<FinanceMainDetailVo> convert2detailVos(
			List<FinanceMainDetail> lists) {
		List<FinanceMainDetailVo> results=new ArrayList<FinanceMainDetailVo>();
		if(lists!=null){
			for(int i=0;i<lists.size();i++){
				FinanceMainDetail financeMainDetail=lists.get(i);
				FinanceMainDetailVo financeMainDetailVo=new FinanceMainDetailVo();
				financeMainDetailVo.setId(financeMainDetail.getId());
				financeMainDetailVo.setAmount(financeMainDetail.getAmount());
				financeMainDetailVo.setDoc_nbr(financeMainDetail.getDoc_nbr());
				financeMainDetailVo.setDescription(financeMainDetail.getDescription());
				financeMainDetailVo.setRemark(financeMainDetail.getRemark());
				financeMainDetailVo.setSiteid(financeMainDetail.getSiteid());
				financeMainDetailVo.setCreateuser(financeMainDetail.getCreateuser());
				financeMainDetailVo.setCreatedate(financeMainDetail.getCreatedate());
				financeMainDetailVo.setFid(financeMainDetail.getFid());
				results.add(financeMainDetailVo);
			}
		}
		return results;
	}

}
