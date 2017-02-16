package com.timss.ptw.service.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.ptw.bean.PtwFireInfo;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.service.PtwInfoService;
import com.timss.ptw.service.PtwInfoServiceDef;
import com.timss.ptw.vo.PtwInfoVoList;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: 工作票信息的Service
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoServiceCore.java
 * @author: 周保康
 * @createDate: 2014-6-27
 * @updateUser: 周保康
 * @version: 1.0
 */
@Service
public class PtwInfoServiceImpl implements PtwInfoService {
	
	@Autowired
    private PtwInfoServiceDef ptwInfoServiceDef;

	@Override
	public Page<PtwInfoVoList> queryPtwInfoVoList(Page<PtwInfoVoList> page) throws Exception {
		return ptwInfoServiceDef.queryPtwInfoVoList(page);
	}
	
	@Override
	public Page<PtwInfoVoList> queryHisPtwList(Page<PtwInfoVoList> page) throws Exception {
		return ptwInfoServiceDef.queryHisPtwList(page);
	}
	
	@Override
    public List<PtwInfo> queryPtwInfoListByWoId(String woId, String siteid) throws Exception {
            return ptwInfoServiceDef.queryPtwInfoListByWoId(woId, siteid);
    }

	@Override
	public PtwInfo queryPtwInfoById(int id) {
		return ptwInfoServiceDef.queryPtwInfoById(id);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int insertPtwInfo(PtwInfo ptwInfo,String ptwTypeCode,String safeItems,PtwFireInfo ptwFireInfo) {
        return ptwInfoServiceDef.insertPtwInfo(ptwInfo, ptwTypeCode, safeItems, ptwFireInfo);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwStatusInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		return ptwInfoServiceDef.updatePtwStatusInfo(ptwInfo, password, modifyDate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwBaseAndLicInfo(PtwInfo ptwInfo, String ptwTypeCode, String safeItems, Date modifyDate, PtwFireInfo ptwFireInfo) {
		return ptwInfoServiceDef.updatePtwBaseAndLicInfo(ptwInfo, ptwTypeCode, safeItems, modifyDate, ptwFireInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwIssueInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		return ptwInfoServiceDef.updatePtwIssueInfo(ptwInfo, password, modifyDate, isEdit, ptwTypeCode, safeItems, ptwFireInfo, false, true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwFinInfo(PtwInfo ptwInfo, String password, Date modifyDate) throws Exception {
		return ptwInfoServiceDef.updatePtwFinInfo(ptwInfo, password, modifyDate, false, true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String updatePtwLicInfo(PtwInfo ptwInfo, String password, Date modifyDate, boolean isEdit, String ptwTypeCode, String safeItems, PtwFireInfo ptwFireInfo)
			throws Exception {
		return ptwInfoServiceDef.updatePtwLicInfo(ptwInfo, password, modifyDate, isEdit, ptwTypeCode, safeItems, ptwFireInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwEndInfo(PtwInfo ptwInfo, String password, Date modifyDate, String safeItems) {
		return ptwInfoServiceDef.updatePtwEndInfo(ptwInfo, password, modifyDate, safeItems, false, true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwCancelInfo(PtwInfo ptwInfo, String password, Date modifyDate) {
		return ptwInfoServiceDef.updatePtwCancelInfo(ptwInfo, password, modifyDate, false, true);
	}

	@Override
	public PtwInfo queryPtwInfoByNo(String wtNo, String siteId) {
		return ptwInfoServiceDef.queryPtwInfoByNo(wtNo, siteId);
	}

	@Override
	public int updatePtwRemark(int ptwId, String remark) {
		return ptwInfoServiceDef.updatePtwRemark(ptwId, remark);
	}

	@Override
	public int deletePtwInfo(int id) {
		return ptwInfoServiceDef.deletePtwInfo(id, false, true);
	}

	@Override
	public int updateRelateKeyBox(int id, String relateKeyBoxId) {
		return ptwInfoServiceDef.updateRelateKeyBox(id, relateKeyBoxId);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId) {
		return ptwInfoServiceDef.queryByKeyBoxId(keyBoxId);
	}

	@Override
	public List<PtwInfo> queryByKeyBoxId(int keyBoxId, String status) {
		return ptwInfoServiceDef.queryByKeyBoxId(keyBoxId, status);
	}

	@Override
	public List<PtwInfo> queryByRelateKeyBoxId(int keyBoxId, String status) {
		return ptwInfoServiceDef.queryByKeyBoxId(keyBoxId);
	}

	@Override
	public HashMap<String, Object> validWpicAvailable(String userId) {
		return ptwInfoServiceDef.validWpicAvailable(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePtwAuditInfo(PtwInfo ptwInfo, String password,
			Date modifyDate,String ptwTypeCode) {
		return ptwInfoServiceDef.updatePtwAuditInfo(ptwInfo, password, modifyDate, ptwTypeCode);
	}

	@Override
	public int updatePtwDepartAuditInfo(PtwInfo ptwInfo, String password,
			boolean isEdit, Date modifyDate, String ptwTypeCode,
			String safeItems) {
		return ptwInfoServiceDef.updatePtwDepartAuditInfo(ptwInfo, password, isEdit, modifyDate, ptwTypeCode, safeItems);
	}
}
