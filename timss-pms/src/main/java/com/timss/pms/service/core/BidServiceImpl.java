package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidCon;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.dao.BidDao;
import com.timss.pms.dao.ProjectDao;
import com.timss.pms.service.BidConService;
import com.timss.pms.service.BidService;
import com.timss.pms.util.AttachUtil;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidConVo;
import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidVo;
import com.timss.pms.vo.ProjectDtlVo;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.mvc.service.ItcMvcService;

/**
 * 
 * @ClassName:     BidServiceImpl
 * @company: gdyd
 * @Description: 招标service接口实现类
 * @author:    黄晓岚
 * @date:   2014-7-2 下午3:37:35
 */
@Service
public class BidServiceImpl implements BidService {
	
	Logger LOGGER=Logger.getLogger(BidServiceImpl.class);
	@Autowired
	BidDao bidDao;
	@Autowired
	BidConService bidConService;
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	AttachmentMapper attachmentMapper;
	@Autowired
	ProjectDao projectDao;
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertBid(Bid bid, List<BidCon> bidCons) {
		LOGGER.info("开始插入招标");
		ChangeStatusUtil.changeSToValue(bid, ChangeStatusUtil.approvalCode);
		InitUserAndSiteIdUtil.initCreate(bid, itcMvcService);
		bidDao.insertBid(bid);
		bidConService.insertBidConList(bidCons,bid.getBidId());
		AttachUtil.bindAttach(attachmentMapper, null, bid.getBAttach());
		LOGGER.info("完成插入招标");
	}
	@Override
	public List<BidVo> queryBidListByProjectId(String projectId){
		LOGGER.info("查询项目id："+projectId+"所属的招标信息");
		List<BidVo> lists=bidDao.queryBidListByProjectId(Integer.valueOf(projectId));
		
		InitVoEnumUtil.initBidVoList(lists, itcMvcService);
		return lists;
	}
	@Override
	public BidDtlVo queryBidByBidId(int bidId) {
		LOGGER.info("查询bidid："+bidId+"的招标信息");
		BidDtlVo bidDtlVo=bidDao.queryBidByBidId(bidId);
		
		ProjectDtlVo projectDtlVo=projectDao.queryProjectById(bidDtlVo.getProjectId());
		bidDtlVo.setProjectName(projectDtlVo.getProjectName());
		
		
		List<BidConVo> bidConVos=bidConService.queryBidConListByBidId(String.valueOf(bidId));
		
		bidDtlVo.setBidConVos(bidConVos);
		//初始化枚举类型
		InitVoEnumUtil.initBidDtlVo(bidDtlVo, itcMvcService);
		//初始化附件信息，供前端显示
		initAttachMap(bidDtlVo);
		return bidDtlVo;
	}
	
	/**
	 * 附件信息处理
	 * @Title: initAttachMap
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidDtlVo
	 */
	private void initAttachMap(BidDtlVo bidDtlVo){
		ArrayList<HashMap<String,Object>> attachMap=AttachUtil.generatAttach(bidDtlVo.getBAttach());
		bidDtlVo.setBidAttachMap(attachMap);
		
		//如果存在评信息，则初始化招标信息，否则设置为空
		List<BidMethod> bidMethod=bidDtlVo.getBidMethod();
		if(bidMethod!=null && bidMethod.size()!=0 && bidMethod.get(0).getBidMethodId()!=null){
			attachMap=AttachUtil.generatAttach(bidMethod.get(0).getAttach());
			bidDtlVo.setBidMethodAttachMap(attachMap);
		}else{
			bidDtlVo.setBidMethod(null);
		}
		//如果存在招标结果，初始化其附件信息，否则招标结果设置为空
		List<BidResult> bidResult=bidDtlVo.getBidResult();
		if(bidResult!=null && bidResult.size()!=0 && bidResult.get(0).getBidResultId()!=null){
			attachMap=AttachUtil.generatAttach(bidResult.get(0).getAttach());
			bidDtlVo.setBidResultAttachMap(attachMap);
		}else{
			bidDtlVo.setBidResult(null);
		}
	}
	@Override
	@Transactional
	public void insertBidMethod(BidMethod bidMethod) {
		LOGGER.info("开始插入招标方法");
		ChangeStatusUtil.changeSToValue(bidMethod, ChangeStatusUtil.approvalCode);
		InitUserAndSiteIdUtil.initCreate(bidMethod, itcMvcService);
		bidDao.insertBidMethod(bidMethod);
		AttachUtil.bindAttach(attachmentMapper, null, bidMethod.getAttach());
		LOGGER.info("完成插入招标方法");
		
	}
	@Override
	public void insertBidResult(BidResult bidResult,List<BidCon> bidCons) {
		LOGGER.info("开始插入招标结果");
		ChangeStatusUtil.changeSToValue(bidResult, ChangeStatusUtil.approvalCode);
		InitUserAndSiteIdUtil.initCreate(bidResult, itcMvcService);
		bidDao.insertBidResult(bidResult);
		bidConService.updateBidConList(bidCons);
		AttachUtil.bindAttach(attachmentMapper, null, bidResult.getAttach());
		LOGGER.info("完成插入招标结果");
		
	}


}
