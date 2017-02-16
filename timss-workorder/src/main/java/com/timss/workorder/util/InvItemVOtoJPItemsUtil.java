package com.timss.workorder.util;

import java.util.ArrayList;
import java.util.List;

import com.timss.inventory.vo.InvMatApplyToWorkOrder;
import com.timss.workorder.bean.JPItems;

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
	public static List<JPItems> invItemVOtoJPItem(List<InvMatApplyToWorkOrder> invItemVOList){
		List<JPItems> jpItemsList = new ArrayList<JPItems>();
		int size = invItemVOList.size();
		for (int i = 0; i < size; i++) {
			JPItems jpItems = new JPItems();
			InvMatApplyToWorkOrder invItemVO = invItemVOList.get(i);
			double applyCount = 0;//申请数量
			if(invItemVO.getQtyApply()!=null){
				applyCount = invItemVO.getQtyApply().doubleValue();
			}
			double getCount = 0;//领取数量
			if(invItemVO.getOutQty()!=null){
				getCount = invItemVO.getOutQty().doubleValue();
			}
			double usedCount = 0; //实际使用数量
			if(invItemVO.getRefundQty()!=null){
				usedCount = getCount - invItemVO.getRefundQty().doubleValue();
			}
			//jpItems.setBin(invItemVO.getBin()); //货柜
			jpItems.setApplyCount(applyCount); //申请数量
			jpItems.setGetCount(getCount);//领取数量
			jpItems.setUsedCount(usedCount);//使用数量
			jpItems.setItemsCode(invItemVO.getItemcode()); //物资编码
			jpItems.setItemsId(invItemVO.getItemid());  //物资Id
			jpItems.setItemsName(invItemVO.getItemname());//工具名称
			jpItems.setUnit(invItemVO.getUnit1()); //单位
			jpItems.setItemsModel(invItemVO.getCusmodel());//工具型号
			jpItems.setWarehouse(invItemVO.getWarehousename());  //仓库
			jpItems.setCateName(invItemVO.getInvcatename()); //物资类型名称
			jpItemsList.add(jpItems);
		}
		return jpItemsList;
	}
	
	
	
	
}
