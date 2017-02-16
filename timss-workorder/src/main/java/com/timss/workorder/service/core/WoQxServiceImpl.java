package com.timss.workorder.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WoQx;
import com.timss.workorder.dao.WoQxDao;
import com.timss.workorder.service.WoQxService;
import com.timss.workorder.vo.WoQxVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class WoQxServiceImpl implements WoQxService {
	@Autowired
	WoQxDao woBdzqxDao;
	@Autowired
	private ItcMvcService itcMvcService;
	
	private static Logger logger = Logger.getLogger(WoQxServiceImpl.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, String>  insertUpdateWoQx(WoQx woQx) {
		Map<String,String>  resultMap = new HashMap<String,String>();
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteid = userInfoScope.getSiteId();
		String deptid = userInfoScope.getOrgId();
		int updateNum=0;
		String woId= woQx.getId();
		
		if("".equals(woId)||woId==null){
			woQx.setCreatedate(new Date());
			woQx.setCreateuser(userId);
			woQx.setSiteid(siteid);
			woQx.setDeptid(deptid);
			woQx.setYxbz(1);
			updateNum = woBdzqxDao.insertWoQx(woQx);
		}else{
			woQx.setModifydate(new Date());
			woQx.setModifyuser(userId);
			updateNum = woBdzqxDao.updateWoQx(woQx);
		}
		
		if(updateNum>0){
			resultMap.put("result", "success");
		}else{
			resultMap.put("result", "error");
		}
		return resultMap;
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateWoQx(WoQx woQx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WoQx queryWoQxById(String id) {
		return woBdzqxDao.queryWoQxById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, String>  deleteWoQxById(String id) {
		int updateNum = woBdzqxDao.deleteWoQxById(id);
		Map<String,String>  resultMap = new HashMap<String,String>();
		if(updateNum>0){
			resultMap.put("result", "success");
		}else{
			resultMap.put("result", "error");
		}
		return resultMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Page<WoQx> queryAllWoQx(Page<WoQx> page) {
		List<WoQx> ret = woBdzqxDao.queryAllWoQx(page);
		page.setResults(ret);
		logger.info("查询缺陷列表信息");
		
		return page;
	}

	@Override
	public List<WoQx> queryQxByAssetId(String assetId, String siteId) {
		List<WoQx> result = woBdzqxDao.queryQxByAssetId(assetId,siteId);
		return result;
	}

        @Override
        public Page<WoQxVo> queryWoQxVoStat(Page<WoQxVo> page) {
            List<WoQxVo> ret = woBdzqxDao.queryWoQxVoStat(page);
            page.setResults(ret);
            logger.info("查询缺陷统计列表信息");
            return page;
        }
}
