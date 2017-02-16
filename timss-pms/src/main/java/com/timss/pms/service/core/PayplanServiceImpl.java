package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.dao.PayplanDao;
import com.timss.pms.service.PayplanService;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.PayplanVo;
import com.yudean.mvc.service.ItcMvcService;
@Service
public class PayplanServiceImpl implements PayplanService {
	@Autowired
	PayplanDao payplanDao;
	@Autowired
	ItcMvcService itcMvcService;
	static String approved="approved";
	static String approving="approving";
	static String reset="";
	static Logger LOGGER=Logger.getLogger(PayplanServiceImpl.class);
	@Override
	public void insertPayplan(List<Payplan> payplans) {
		LOGGER.info("开始插入结算计划");
		payplanDao.insertPayplanList(payplans);
		LOGGER.info("完成插入结算计划");

	}

	@Override
	public List<PayplanVo> queryPayplanListByContractId(int id) {
		LOGGER.info("查询合同id："+id+"所属的结算计划信息");
		List<PayplanVo> payplans=payplanDao.queryPayplanListByContractId(id);
		return payplans;
	}

	@Override
	public boolean updateCheckStatusApproval(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的验收状态");
		boolean b=updateCheckStatus(id, approved);
		return b;
	}

	@Override
	public boolean updateCheckStatusApproving(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的验收状态");
		return updateCheckStatus(id, approving);
	}

	@Override
	public boolean resetCheckStatus(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的验收状态");
		return updateCheckStatus(id, reset);
	}

	@Override
	public boolean updatePayStatusApproval(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的结算状态");
		return updatePayStatus(id, approved);
	}

	@Override
	public boolean updatePayStatusApproving(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的结算状态");
		return updatePayStatus(id, approving);
	}

	@Override
	public boolean resetPayStatus(int id) {
		LOGGER.info("更新id："+id+" 的结算计划的结算状态");
		return updatePayStatus(id, reset);
	}
	
	/**
	 * 根据payStatus更新结算状态
	 * @Title: updatePayStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @param payStatus
	 * @return
	 */
	private boolean updatePayStatus(int id,String payStatus){
		Payplan payplan=new Payplan();
		payplan.setId(id);
		payplan.setPayStatus(payStatus);
		int i=payplanDao.updatePayplanPayStatus(payplan);
		return i!=0;
	}
	
	/**
	 * 根据checkStatus更新结算状态
	 * @Title: updateCheckStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @param checkStatus
	 * @return
	 */
	private boolean updateCheckStatus(int id,String checkStatus){
		Payplan payplan=new Payplan();
		payplan.setId(id);
		payplan.setCheckStatus(checkStatus);
		int i=payplanDao.updatePayplanCheckStatus(payplan);
		return i!=0;
	}

	@Override
	public List<PayplanVo> getCheckableByContractId(int id) {
		List<PayplanVo> payplanVos=payplanDao.queryPayplanListByContractId(id);
		List<PayplanVo> payplans=new ArrayList<PayplanVo>();
		if(payplanVos!=null){
			for(int i=0;i<payplanVos.size();i++){
				PayplanVo payplanVo=payplanVos.get(i);
				if((payplanVo.getNeedchecked()!=null && payplanVo.getNeedchecked()==true) && (payplanVo.getCheckStatus()==null  ||
						payplanVo.getCheckStatus().equals("reset") || payplanVo.getCheckStatus().equals(""))){
					payplans.add(payplanVo);
				}
			}
		}
		InitVoEnumUtil.initPayplanVoList(payplans, itcMvcService);
		return payplans;
	}

	@Override
	public List<PayplanVo> getPayableByContractId(int id) {
		List<PayplanVo> payplanVos=payplanDao.queryPayplanListByContractId(id);
		List<PayplanVo> payplans=new ArrayList<PayplanVo>();
		if(payplanVos!=null){
			for(int i=0;i<payplanVos.size();i++){
				PayplanVo payplanVo=payplanVos.get(i);
				if((StringUtils.isEmpty( payplanVo.getPayStatus())|| "reset".equals( payplanVo.getPayStatus() )) &&  ( Boolean.FALSE ==payplanVo.getNeedchecked() || "approved".equals(payplanVo.getCheckStatus()))){
					payplans.add(payplanVo);
				}
			}
		}
		InitVoEnumUtil.initPayplanVoList(payplans, itcMvcService);
		return payplans;
	}

	@Override
	public PayplanVo queryPayplanById(int id) {
		LOGGER.info("查询结算计划通过id："+id);
		PayplanVo payplanVo=payplanDao.queryPayplanById(id);
		return payplanVo;
	}

	@Override
	@Transactional
	
	public int updatePayplan(List<Payplan> payplans, Contract contract) {
		payplanDao.deletePayplanByContractId(contract.getId());
		initPayplans(payplans, contract);
		insertPayplan(payplans);
		return 0;
	}
	
	private void initPayplans(List<Payplan> payplans,Contract contract){
		if(payplans!=null && payplans.size()!=0 ){
			for(int i=0;i<payplans.size();i++){
				Payplan payplan=payplans.get(i);
				payplan.setContractId(contract.getId());
			}
		}
	}

	@Override
	@Transactional
	public int updatePayplanByPayplanTmp(List<PayplanTmp> payplanTmps,
			Contract contract) {
		List<PayplanVo> existPayplans=payplanDao.queryPayplanListByContractId(contract.getId());
		//存放已经存在的payplans 的id
		HashMap<Integer, Integer> hashMap=new HashMap<Integer, Integer>();
		for(int i=0;i<existPayplans.size();i++){
			hashMap.put(existPayplans.get(i).getId(),1);
		}
		//需要插入数据库的数据
		List<PayplanTmp> newpPayplans=new ArrayList<PayplanTmp>();
		//需要更新的数据
		List<PayplanTmp> updatePayplans=new ArrayList<PayplanTmp>();
	
		if(payplanTmps!=null && payplanTmps.size()!=0){
			for(int i=0;i<payplanTmps.size();i++){
				PayplanTmp payplanTmp=payplanTmps.get(i);
				if(payplanTmp.getpayplanId()==null){
					newpPayplans.add(payplanTmp);
				}else if(hashMap.get(payplanTmp.getpayplanId())==1){
					updatePayplans.add(payplanTmp);
					hashMap.put(payplanTmp.getpayplanId(),2);
				}
			}
		}
		//删除已有的数据
		Iterator iterator = hashMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry entry=(Entry) iterator.next();
			Integer key=(Integer) entry.getKey();
			Integer value=(Integer) entry.getValue();
			if(value==1){
				payplanDao.deletePayplanById(key);
			}
			
		}
		//添加数据
		if(newpPayplans!=null && newpPayplans.size()!=0){
			List<Payplan> payplans=changePayplanTmpsToPayplans(newpPayplans);
			
			payplanDao.insertPayplanList(payplans);
		}
		//修改数据
		if(updatePayplans!=null && updatePayplans.size()!=0){
			List<Payplan> payplans=changePayplanTmpsToPayplans(updatePayplans);
			for(int i=0;i<payplans.size();i++){
				payplanDao.updatePayplan(payplans.get(i));
			}
			
		}
		return 0;
		
	}
	
	private List<Payplan> changePayplanTmpsToPayplans(List<PayplanTmp> payplanTmps){
		List<Payplan> payplans=new ArrayList<Payplan>();
		for(int i=0;i<payplanTmps.size();i++){
			PayplanTmp payplanTmp=payplanTmps.get(i);
			Payplan payplan=new Payplan();
			
			payplan.setId(payplanTmp.getpayplanId());
			payplan.setPayType(payplanTmp.getPayType());
			payplan.setPaySum(payplanTmp.getPaySum());
			payplan.setNeedchecked(payplanTmp.getNeedchecked());
			payplan.setPayStatus(payplanTmp.getPayStatus());
			payplan.setCheckStatus(payplanTmp.getCheckStatus());
			payplan.setCommand(payplanTmp.getCommand());
			payplan.setContractId(payplanTmp.getContractId());
			payplan.setPercent(payplanTmp.getPercent());
			payplan.setSiteid(payplanTmp.getSiteid());
			
			payplans.add(payplan);
		}
		return payplans;
	}

	

}
