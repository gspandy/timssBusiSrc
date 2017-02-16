package com.timss.finance.service.core;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.finance.bean.FinanceMainDetailCost;
import com.timss.finance.dao.FinanceMainDetailCostDao;
import com.timss.finance.service.FinanceMainDetailCostService;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class FinanceMainDetailCostServiceImpl implements FinanceMainDetailCostService {
	private Logger logger = Logger.getLogger(FinanceMainDetailCostServiceImpl.class);

	@Autowired
	private FinanceMainDetailCostDao financeMainDetailCostDao;
	@Autowired
	private ItcMvcService itcMvcService;
	

//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public void insertFinanceMainDetailCost(List<FinanceMainDetail> mainDtlList, String detail){
//	    String siteid=itcMvcService.getUserInfoScopeDatas().getSiteId();
//		
//	    JSONArray jsonArr = JSONArray.fromObject(detail);
//		
//	    //遍历明细
//	    for (int i = 0; i < jsonArr.size() && i < mainDtlList.size(); i++) {
//		JSONObject jsonObject = jsonArr.getJSONObject(i);
//		//依次插入每一条明细对应的多条费用记录
//		insertAFinMainDetailAllCost(jsonObject,mainDtlList.get(i).getId(),siteid);
//	    }
//	}
	
	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2015-8-21
	 * @param jsonObject 某一条明细对应的form表单数据
	 * @param mainDtId 要插入的from对应的datagrid中的某条明细的id
	 * @param siteid:
	 */
	@Override
	public void insertAFinMainDetailAllCost(JSONObject jsonObject,String mainDtId, String siteid) {
	    logger.info( "-------------开始插入明细编号为："+mainDtId+"的所有费用记录----------------" );
	    //获取所有费用的枚举值
	    List<AppEnum> appEnums = itcMvcService.getEnum( "FIN_COST_TYPE" );
	    //循环的一次插入每一种费用的报销记录
	    for ( AppEnum appEnum: appEnums) {
	        String costTypeKey = appEnum.getCode();
	        String costTypeLabel = appEnum.getLabel();
	        insertAFinMainDetailCost(jsonObject,mainDtId,costTypeKey,costTypeLabel,siteid);
            }
        }

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-8-21
     * @param jsonObject 某一条明细对应的form表单数据
     * @param mainDtId 要插入的from对应的datagrid中的某条明细的id
     * @param costTypekey 要插入的费用类型编码（例如：会议费、火车费、长途汽车费、……对应的枚举code）
     * @param costTypelabel 要插入的费用类型中文名字（例如：会议费、火车费、长途汽车费、……）
     * @param siteid:
     */
    private void insertAFinMainDetailCost(JSONObject jsonObject, String mainDtId, String costTypekey, 
            String costTypelabel,String siteid) {
        if(jsonObject.containsKey(costTypekey) && jsonObject.get( costTypekey )!= null){
            double cost = jsonObject.getDouble(costTypekey); //机票费
            FinanceMainDetailCost financemainDetailCost = null;
            if( cost != 0 ) {
                    financemainDetailCost = new FinanceMainDetailCost();
                    financemainDetailCost.setId(mainDtId);
                    financemainDetailCost.setCost_id(costTypekey);
                    financemainDetailCost.setCost_type(costTypelabel);
                    financemainDetailCost.setAmount(cost);
                    financemainDetailCost.setSiteid(siteid);
                    financeMainDetailCostDao.insertFinanceMainDetailCost(financemainDetailCost);
            }
        }
    }

    @Override
    public void deleteFinanceMainDetailCostByFid(String fid) {
	financeMainDetailCostDao.deleteFinanceMainDetailCostByFid(fid);
    }

    @Override
    public void updateAFinMainDetailAmount(String id, String costId, double amount) {
        financeMainDetailCostDao.updateAFinMainDetailAmount(id,costId,amount);
    }
}
