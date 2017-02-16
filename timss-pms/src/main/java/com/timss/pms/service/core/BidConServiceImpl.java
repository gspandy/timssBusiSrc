package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.BidCon;
import com.timss.pms.dao.BidConDao;
import com.timss.pms.exception.PmsBasicException;
import com.timss.pms.service.BidConService;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.vo.BidConVo;
import com.timss.purchase.bean.PurVendor;
import com.yudean.mvc.service.ItcMvcService;

//TODO 恢复
@Service
public class BidConServiceImpl implements BidConService{

	@Autowired
	BidConDao bidConDao;
	@Autowired
	ItcMvcService itcMvcService;
	

	
	Logger LOGGER=Logger.getLogger(BidConServiceImpl.class);
	@Override
	@Transactional
	public void insertBidConList(List<BidCon> bidCons,int bidId) {
		if(bidCons==null || bidCons.size()==0){
			LOGGER.info("要插入的招标单位信息为空");
			return ;
			
		}
		LOGGER.info("开始插入招标单位信息");
		initBidCon(bidCons,bidId);
		bidConDao.insertBidConList(bidCons);
		LOGGER.info("完成插入招标单位信息");
	}

	@Override
	public List<BidConVo> queryBidConListByBidId(String bidId) {
		LOGGER.info("查询招标单位信息，在招标id为："+bidId);
		List<BidConVo> bidConVos=bidConDao.queryBidConListByBidId(Integer.parseInt(bidId));
		initBidConVos(bidConVos);
		return bidConVos;
	}
	
	private void initBidConVos(List<BidConVo> bidConVos){
		if(bidConVos!=null && bidConVos.size()!=0){
			List<String> companyNos=new ArrayList<String>();
			Map map=new HashMap();
			for(int i=0;i<bidConVos.size();i++){
				BidConVo bidConVo=bidConVos.get(i);
				companyNos.add(bidConVo.getCompanyNo());
				map.put(bidConVo.getCompanyNo(), bidConVo);
			}
			String siteid=itcMvcService.getUserInfoScopeDatas().getSiteId();
			List<PurVendor> lists=null;
			try{
				//TODO
				//lists=purVendorService.queryPurVendorBycompanyNos(companyNos, siteid);
			}catch (Exception e) {
				throw new PmsBasicException("获取采购模块的供应商信息出错", e );
			}
			if(lists!=null){
				for(int i=0;i<lists.size();i++){
					PurVendor purVendor=lists.get(i);
					String companyNo=purVendor.getCompanyNo();
					BidConVo bidConVo=(BidConVo) map.get(companyNo);
					bidConVo.setName(purVendor.getName());
					bidConVo.setType(purVendor.getType());
					bidConVo.setContact(purVendor.getContact());
				}
			}
		}
	}
	
	private void initBidCon(List<BidCon> bidCons,int bidId){
	
		for(int i=0;i<bidCons.size();i++){
			BidCon bidCon=bidCons.get(i);
			InitUserAndSiteIdUtil.initCreate(bidCon, itcMvcService);
			bidCon.setBidId(bidId);
		}
	}

	@Override
	public void updateBidConList(List<BidCon> bidCons) {
		LOGGER.info("开始更新招标单位信息");
		if(bidCons!=null && bidCons.size()!=0){
			for(int i=0;i<bidCons.size();i++){
				BidCon bidCon=bidCons.get(i);
				if(bidCon.getId()!=null){
					bidConDao.updateBidCon(bidCon);
				}
			}
			
		}
		LOGGER.info("完成更新招标单位信息");
		
	}

}
