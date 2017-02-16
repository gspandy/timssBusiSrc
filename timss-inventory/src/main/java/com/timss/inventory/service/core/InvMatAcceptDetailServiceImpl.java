package com.timss.inventory.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.dao.InvMatAcceptDetailDao;
import com.timss.inventory.vo.InvMatAcceptDetailVO;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatAcceptDetailServiceImpl.java
 * @author: 890145
 * @createDate: 2015-11-2
 * @updateUser: 890145
 * @version: 1.0
 */
@Service
public class InvMatAcceptDetailServiceImpl implements InvMatAcceptDetailService{

	@Autowired
	InvMatAcceptDetailDao invMatAcceptDetailDao;
    @Autowired
    private ItcMvcService itcMvcService;
	/* (non-Javadoc)
	 * @see com.timss.inventory.service.core.InvMatAcceptDetailService#insertInvMatAcceptDetail(com.timss.inventory.bean.InvMatAccept, java.util.List)
	 */
	@Override
	@Transactional
	public Map<String, Object> insertInvMatAcceptDetail(
			InvMatAccept invMatAccept,
			List<InvMatAcceptDetail> invMatAcceptDetails) {
        List<InvMatAcceptDetail> newInvMatAcceptDetails=initDetails(invMatAccept, invMatAcceptDetails);
        for(int i=0;i<newInvMatAcceptDetails.size();i++){
        	invMatAcceptDetailDao.insert(newInvMatAcceptDetails.get(i));
        }
		return null;
	}

	/* (non-Javadoc)
	 * @see com.timss.inventory.service.core.InvMatAcceptDetailService#updateInvMatAcceptDetail(com.timss.inventory.bean.InvMatAccept, java.util.List)
	 */
	@Override
	@Transactional
	public Map<String, Object> updateInvMatAcceptDetail(
			InvMatAccept invMatAccept,
			List<InvMatAcceptDetail> invMatAcceptDetails) {
		deleteInvMatAcceptDetailByInacId(invMatAccept.getInacId());
		Map<String, Object> resultsMap=insertInvMatAcceptDetail(invMatAccept, invMatAcceptDetails);
		return resultsMap;
	}
	
	/**
	 * 初始化物资申请记录
	 * @description:
	 * @author: 890145
	 * @createDate: 2015-11-2
	 * @param invMatAccept
	 * @param invMatAcceptDetails
	 * @return:
	 */
	private List<InvMatAcceptDetail> initDetails(InvMatAccept invMatAccept,
			List<InvMatAcceptDetail> invMatAcceptDetails){
        UserInfo userInfo = itcMvcService.getUserInfoScopeDatas();
		List<InvMatAcceptDetail> newInvMatAcceptDetail=new ArrayList<InvMatAcceptDetail>();
		if(invMatAcceptDetails!=null){
			for(int i=0;i<invMatAcceptDetails.size();i++){
				InvMatAcceptDetail invMatAcceptDetail=invMatAcceptDetails.get(i);
				invMatAcceptDetail.setInacId(invMatAccept.getInacId());
				if(invMatAcceptDetail.getCreatedate()==null){
					invMatAcceptDetail.setCreatedate(new Date());
				}
				if(invMatAcceptDetail.getCreateuser()==null){
					invMatAcceptDetail.setCreateuser(userInfo.getUserId());
				}
				if(invMatAcceptDetail.getSiteid()==null){
					invMatAcceptDetail.setSiteid(userInfo.getSiteId());
				}
				if(invMatAcceptDetail.getDeptid()==null){
					invMatAcceptDetail.setDeptid(userInfo.getOrgId());
				}
				newInvMatAcceptDetail.add(invMatAcceptDetail);
			}
		}
		return newInvMatAcceptDetail;
	}

	/* (non-Javadoc)
	 * @see com.timss.inventory.service.core.InvMatAcceptDetailService#deleteInvMatAcceptDetailByInacId(java.lang.String)
	 */
	@Override
	@Transactional
	public int deleteInvMatAcceptDetailByInacId(String inacId) {
		int result=invMatAcceptDetailDao.deleteByInacId(inacId);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.timss.inventory.service.core.InvMatAcceptDetailService#queryInvMatAcceptDetailListByInacId(java.lang.String)
	 */
	@Override
	public List<InvMatAcceptDetailVO> queryInvMatAcceptDetailListByInacId(
			String inacId) {
		InvMatAcceptDetail invMatAcceptDetail=new InvMatAcceptDetail();
		invMatAcceptDetail.setInacId(inacId);
		List<InvMatAcceptDetailVO> invMatAcceptDetails=invMatAcceptDetailDao.queryInvMatAcceptDetailList(invMatAcceptDetail);
		return invMatAcceptDetails;
	}

}
