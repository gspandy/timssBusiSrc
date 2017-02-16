package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.bean.PtwIsolationBean;
import com.timss.ptw.bean.PtwKeyBox;
import com.timss.ptw.dao.PtwKeyBoxDao;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwIsolationService;
import com.timss.ptw.service.PtwKeyBoxService;
import com.timss.ptw.util.PtwConstants.KeyBoxStatus;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class PtwKeyBoxServiceImpl implements PtwKeyBoxService {
    private static final Logger log = Logger.getLogger(PtwKeyBoxServiceImpl.class);

    @Autowired
    PtwKeyBoxDao  ptwKeyBoxDao;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    PtwInfoService ptwInfoService;
    @Autowired
    PtwIsolationService ptwIsolationService;
	@Override
	public Page<PtwKeyBox> queryPtwKeyBoxList(Page<PtwKeyBox> page) {
		List<PtwKeyBox> ret = ptwKeyBoxDao.queryPtwKeyBoxList(page);
		page.setResults(ret);
		return page;
	}

	@Override
	public PtwKeyBox queryPtwKeyBoxById(int id) {
		return ptwKeyBoxDao.queryPtwKeyBoxById(id);
	}

	@Override
	public int insertPtwKeyBox( HashMap<String,String> paramsDataMap) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String userId = userInfoScope.getUserId();
		String siteId = userInfoScope.getSiteId();
		String keyBoxForm = paramsDataMap.get("keyBoxForm");
		
		PtwKeyBox ptwKeyBox = JsonHelper.fromJsonStringToBean(keyBoxForm, PtwKeyBox.class);
		
		ptwKeyBox.setSiteid(siteId);
		ptwKeyBox.setCreatedate(new Date());
		ptwKeyBox.setModifydate(new Date());
		ptwKeyBox.setCreateuser(userId);
		ptwKeyBox.setModifyuser(userId);
		ptwKeyBox.setYxbz(1);
		
		return ptwKeyBoxDao.insertPtwKeyBox(ptwKeyBox);
	}

	@Override
	public int updatePtwKeyBox(PtwKeyBox ptwInfo) {
		return ptwKeyBoxDao.updatePtwKeyBox(ptwInfo);
	}

	@Override
	public int deletePtwKeyBoxById(int id) {
		return ptwKeyBoxDao.deletePtwKeyBoxById(id);
	}

	@Override
	public Boolean checkKeyBoxNo(String keyBoxNo) {
		boolean result = false;
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		String siteId = userInfoScope.getSiteId();
		
		int count = ptwKeyBoxDao.queryPtwKeyBoxByNo(keyBoxNo,siteId);
		if(count == 0){  
			result= true;
		}
		return result;
	}

	@Override
	public HashMap<String, Object> queryByWtIdorIslId(String wtId,String islId) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("total", 0);
		result.put("rows", new String[0]);
		
		String relateIds = null;
		if (null != wtId && !"".equals(wtId) && !"0".equals(wtId)) {
			PtwInfo ptwInfo = ptwInfoService.queryPtwInfoById(Integer.parseInt(wtId));
			relateIds = ptwInfo.getRelateKeyBoxId();
		}		
		if (null != islId && !"".equals(islId) && !"0".equals(islId)) {
			PtwIsolationBean ptwIsolationBean = ptwIsolationService.queryPtwIsolationById(Integer.parseInt(islId));
			relateIds = ptwIsolationBean.getRelateKeyBoxId();
		}
		if (relateIds == null || "".equals(relateIds)) {
			return result;
		}
		List<PtwKeyBox> list = this.queryByIds(relateIds);
		result.put("total", list.size());
		result.put("rows", list);
		return result;
	}

	@Override
	public List<PtwKeyBox> queryByIds(String ids) {
		if (ids == null || "".equals(ids)) {
			return null;
		}
		return ptwKeyBoxDao.queryByIds(ids);
	}

	@Override
	public int updateKeyBoxStatusByPtwOrIslStatus(Integer keyBoxId, int status) {
		log.info("准备更新钥匙箱"+keyBoxId+",状态为" + status);
		int result = 0;
		if (keyBoxId == null || keyBoxId <= 0) {
			log.info("尝试更新钥匙箱当前状态时，未传入钥匙箱Id");
			return result;
		}
		//如果为终结工作票时，需要校验是否有其他关联的要是工作票在上面
		PtwKeyBox ptwKeyBox  = this.queryPtwKeyBoxById(keyBoxId);
		
		
		if (status == 300) {
			//工作票或隔离证为未签发时，钥匙箱状态应该变为“使用中”，但如果已经为“已安全”或“已确认”，则不需要改
			if (ptwKeyBox.getCurStatus().equals(KeyBoxStatus.safe.toString()) 
					|| ptwKeyBox.getCurStatus().equals(KeyBoxStatus.confirm.toString())) {
				log.info("钥匙箱的状态已经为:" + ptwKeyBox.getCurStatus() + ",不需要改变了");
				return result;
			}
		}else if (status == 400) {
			//工作票或隔离证为已签发时，钥匙箱状态应该变为“已确认”，但如果已经为“已安全”，则不需要改
			if (ptwKeyBox.getCurStatus().equals(KeyBoxStatus.safe.toString())) {
				log.info("钥匙箱的状态已经为:" + ptwKeyBox.getCurStatus() + ",不需要改变了");
				return result;
			}
		}else if (status == 700 || status == 800) {
			//工作票或隔离证为终结或作废时，需要查询关联钥匙箱的状态，如果还有关联的工作票，则不能改变状态
			String statusToQuery = "";
			statusToQuery = "300,400,500,600";
			List<PtwInfo> ptwInfos = ptwInfoService.queryByKeyBoxId(keyBoxId, statusToQuery);
			List<PtwInfo> ptwInfosRelate = ptwInfoService.queryByRelateKeyBoxId(keyBoxId, statusToQuery);
			List<PtwIsolationBean> ptwIsolationBeans = ptwIsolationService.queryByKeyBoxId(keyBoxId, statusToQuery);
			List<PtwIsolationBean> ptwIsolationBeansRelate = ptwIsolationService.queryByRelateKeyBoxId(keyBoxId, statusToQuery);
			int size = 0;
			if (!checkListIsEmpty(ptwInfos)) {
				log.info("目前还有对应的工作票");
				size += ptwInfos.size();
			}
			if (!checkListIsEmpty(ptwInfosRelate)) {
				log.info("目前还有关联的工作票");
				size += ptwInfosRelate.size();
			}
			if (!checkListIsEmpty(ptwIsolationBeans)) {
				log.info("目前还有对应的隔离证");
				size += ptwIsolationBeans.size();
			}
			if (!checkListIsEmpty(ptwIsolationBeansRelate)) {
				log.info("目前还有关联的隔离证");
				size += ptwIsolationBeansRelate.size();
			}
			if (size > 0) {
				return result;
			}
		}
		
		//根据工作票和隔离证的相关信息判断是否更新钥匙箱的信息		
		KeyBoxStatus keyBoxStatus = queryKeyBoxStatusByPtwOrIslStatus(status);
		result = ptwKeyBoxDao.updateKeyBoxStatus(keyBoxId, keyBoxStatus);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean checkListIsEmpty(List list){
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据工作票的状态来确定钥匙箱的状态
	 * 未签发->在用
	 * 已签发->使用中
	 * 已许可->已安全
	 * 已结束->已安全
	 * 已终结->可用
	 * 已作废->可用
	 * @param status
	 * @return
	 */
	private KeyBoxStatus queryKeyBoxStatusByPtwOrIslStatus(int status){
		KeyBoxStatus keyBoxStatus = KeyBoxStatus.useable;
		switch (status) {
		case 300:
			keyBoxStatus = KeyBoxStatus.using;
			break;
		case 400:
			keyBoxStatus = KeyBoxStatus.confirm;
			break;
		case 500:
			keyBoxStatus = KeyBoxStatus.safe;
			break;
		case 600:
			keyBoxStatus = KeyBoxStatus.safe;
			break;
		case 700:
			keyBoxStatus = KeyBoxStatus.useable;
			break;
		case 800:
			keyBoxStatus = KeyBoxStatus.useable;
			break;
		default:
			break;
		}
		return keyBoxStatus;
	}
}
