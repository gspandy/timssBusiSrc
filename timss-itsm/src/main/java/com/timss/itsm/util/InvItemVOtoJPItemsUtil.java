package com.timss.itsm.util;

import java.util.ArrayList;
import java.util.List;

import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.itsm.bean.ItsmJPItems;

public class InvItemVOtoJPItemsUtil{
	/**
	 * 
	 * @description: Bean转换
	 * @author: 王中华
	 * @createDate: 2014-6-25
	 * @param woId
	 * @param preFix
	 * @return:
	 */
	public static List<ItsmJPItems> invItemVOtoJPItem(List<InvMatApplyToWorkOrder> invItemVOList){
		List<ItsmJPItems> jpItemsList = new ArrayList<ItsmJPItems>();
		int size = invItemVOList.size();
		for (int i = 0; i < size; i++) {
			ItsmJPItems jpItems = new ItsmJPItems();
			InvMatApplyToWorkOrder invItemVO = invItemVOList.get(i);
			int applyCount = invItemVO.getQtyApply().intValue();//申请数量
			int getCount = invItemVO.getOutQty().intValue();//领取数量
			int usedCount = getCount - invItemVO.getRefundQty().intValue(); //实际使用数量
			
			
//			jpItems.setBin(invItemVO.getBin()); //货柜
			jpItems.setApplyCount(applyCount); //申请数量
			jpItems.setGetCount(getCount);//领取数量
			jpItems.setUsedCount(usedCount);//使用数量
			jpItems.setItemsCode(invItemVO.getItemcode()); //物资编码
			jpItems.setItemsId(invItemVO.getItemid());  //物资Id
			jpItems.setItemsName(invItemVO.getItemname());//工具名称
			jpItems.setUnit(invItemVO.getUnit1()); //单位
			jpItems.setItemsModel(invItemVO.getCusmodel());//工具型号
//			jpItems.setWarehouse(invItemVO.getWarehouse());  //仓库
			
			jpItemsList.add(jpItems);
		}
		return jpItemsList;
	}
	
	
	
	
}
