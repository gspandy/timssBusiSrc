package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.dao.BidResultDao;
import com.timss.pms.service.BidQueryService;
import com.timss.pms.util.ChangeStatusUtil;
import com.timss.pms.util.InitVoEnumUtil;
import com.timss.pms.vo.BidResultVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class BidQueryServiceImpl implements BidQueryService {
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	BidResultDao bidResultDao;
    private static final Logger LOGGER =Logger.getLogger(BidQueryServiceImpl.class);
	@Override
	public Page<BidResultVo> queryBidResultListAndFilter(Page<BidResultVo> page,
			UserInfoScope userInfo) {
		
		LOGGER.info("开始查询招标结果数据");
		//根据站点id查询
		page.setFuzzyParameter("siteid", userInfo.getSiteId());
		List<BidResultVo> projects = bidResultDao.queryBidResultListAndFilter(page);
		InitVoEnumUtil.initBidResultVoList(projects, itcMvcService);

		page.setResults(projects);
		LOGGER.info("查询招标结果数据成功");
		
		return page;
	}
	@Override
	public List<Map<String, String>> queryBidResultListByKeyWord(String kw) {
		UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
		Page<BidResultVo> page = new Page<BidResultVo>(1,11);
        //设置查询调整
		if(StringUtils.isNotBlank(kw)){
			page.setFuzzyParameter("name",kw);
		}
		
		page.setFuzzyParameter("siteid", userInfoScope.getSiteId());
		page.setFuzzyParameter("status", ChangeStatusUtil.approvalCode);
		//根据站点和招标名称模糊搜索
		List<BidResultVo> bidResultVos = bidResultDao.queryBidResultList(page);
		
		//将招标结果转换为iHint组件需要的格式
		List<Map<String,String>> maps=new ArrayList<Map<String,String>>();
		if(bidResultVos!=null){
			for(int i=0;i<bidResultVos.size();i++){
				Map<String,String> map=new HashMap<String,String>();
				map.put("id", bidResultVos.get(i).getBidResultId().toString());
				map.put("name", bidResultVos.get(i).getName());
				maps.add(map);
			}
		}
		return maps;
	}

}
