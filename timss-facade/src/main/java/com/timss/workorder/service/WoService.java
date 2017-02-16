package com.timss.workorder.service;

import java.util.List;
import java.util.Map;

import com.timss.workorder.vo.MaintainPlanVO;
import com.timss.workorder.vo.WorkOrderVO;
/***
 * 工单  Service 操作
 * @author 王中华
 * 2014-6-11
 */
public interface WoService {
	
	
	//------------------------提供给资产接口----------------------------
	/**
	 * @description: 根据设备编号，查看该设备出现过的维修记录
	 * @author: 王中华
	 * @createDate: 2014-7-9
	 * @param equipCode
	 * @return
	 * @throws Exception:
	 */
	List<WorkOrderVO> queryWOVOByAssetId(String assetId, String siteId);
	/**
	 * @description:通过设备编码查找与该设备相关的维护计划信息
	 * @author: 王中华
	 * @createDate: 2014-7-9
	 * @param equipCode
	 * @return
	 * @throws Exception:
	 */
	List<MaintainPlanVO> queryMTPVOByAssetId(String assetId, String siteId);
	
	
	//-------------------------------提供给工作票接口--------------------------------
	/**
	 * @description:根据工单编号查询工单基本信息
	 * @author: 王中华
	 * @createDate: 2014-7-24
	 * @param woCode
	 * @return:
	 */
	Map<String, Object> queryWOBaseInfoByWOCode(String woCode ,String siteId);
	/**
	 * @description:工作票流程走完后，结束工单流程中的节点，走到下一节点
	 * @author: 王中华
	 * @createDate: 2014-7-24
	 * @param woId
	 * @param ptwId:
	 */
	void inPtwToNextStep(int woId ,String userId);

	/**
	 * @description: 给工单添加工作票ID信息
	 * @author: 王中华
	 * @createDate: 2014-7-24
	 * @param woId
	 * @param ptwId
	 * @throws Exception:
	 */
	void updateWOAddPTWId(int woId,int ptwId);
	
}
