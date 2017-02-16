package com.timss.pms.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.SupplierService;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.supplier.bean.SupBaseInfo;
import com.yudean.supplier.service.SupplierForModelService;
import com.yudean.supplier.vo.SupBaseInfoVo;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    SupplierForModelService supplierForModelService;

    
    private static final Logger LOGGER=Logger.getLogger(SupplierServiceImpl.class);
	@Override
	public List queryFuzzyByName(String name) {
		//初始化参数userInfo
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		//初始化参数purVendor，
		SupBaseInfo supBaseInfo=new SupBaseInfo();
		supBaseInfo.setName(name);
		//返回结果
		List<SupBaseInfoVo> purVendors=null;
		try {
			//根据供应商名称，模糊查询供应商信息
			purVendors=supplierForModelService.querySupplierList(userInfo, supBaseInfo);
		} catch (Exception e) {
			LOGGER.error("根据名称："+name+"查询供应商信息时，出错",e);
		}
		//将Page数据转换为[{id:""},{name:""}]格式的数据
		List result=changeToSpecialStyle(purVendors);
		return result;
	}
	private List changeToSpecialStyle(List<SupBaseInfoVo> purVendors) {
		
		List resultList=new ArrayList();
		if(purVendors!=null){
			for(int i=0;i<purVendors.size();i++){
				SupBaseInfoVo purVendor=purVendors.get(i);
				HashMap map=new HashMap();
				map.put("id", purVendor.getCode());
				map.put("name", purVendor.getName());
				resultList.add(map);
			}
		}
		return resultList;
	}
	@Override
	public Object querySupplierById(String id) {
		//初始化参数userInfo
		UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
		//初始化参数purVendor，
		SupBaseInfo supBaseInfo=new SupBaseInfo();
		supBaseInfo.setCode(id);
		//返回结果
		List<SupBaseInfoVo> purVendors=null;
		try {
			//根据供应商名称，模糊查询供应商信息
			purVendors=supplierForModelService.querySupplierList(userInfo, supBaseInfo);
		} catch (Exception e) {
			LOGGER.error("根据编码："+id+"查询供应商信息时，出错",e);
		}
		//调整获取的供应商数据格式，使其满足前端的需求
		Map<String,String> resultMap=new HashMap<String, String>();
		if(purVendors!=null && !purVendors.isEmpty()){
			SupBaseInfoVo supBaseInfoVo=purVendors.get(0);
			resultMap.put("companyNo", supBaseInfoVo.getCode());
			resultMap.put("contact", supBaseInfoVo.getSpiName());
			resultMap.put("tel", supBaseInfoVo.getSpiCellphone());
			resultMap.put("name", supBaseInfoVo.getName());
		}
		return resultMap;

	}

}
