package com.timss.finance.service.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.finance.bean.FinInsertParams;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.dao.FinanceMainDao;
import com.timss.finance.dao.FinanceMainDetailDao;
import com.timss.finance.service.FinanceMainDetailCostService;
import com.timss.finance.service.FinanceMainDetailService;
import com.timss.finance.util.FinanceUtil;
import com.timss.finance.vo.FinanceMainDetailCostVo;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class FinanceMainDetailServiceImpl implements FinanceMainDetailService {
	private Logger logger = Logger
			.getLogger(FinanceMainDetailServiceImpl.class);

	@Autowired
	private FinanceMainDetailDao financeMainDetailDao;
	@Autowired
	private FinanceMainDetailCostService finMainDetailCostService;
	@Autowired
        private FinanceMainDao financeMainDao;
	@Autowired
	private ItcMvcService itcMvcService;

	@Override
	public Page<FinanceMainDetailCostVo> queryFinanceMainDetailList(
			Page<FinanceMainDetailCostVo> page, UserInfoScope userInfoScope,
			String fid)  {
	    List<FinanceMainDetailCostVo> list = new ArrayList<FinanceMainDetailCostVo>();
	    // 以报销编号查询报销信息
	    FinanceMain financeMain = financeMainDao.queryFinanceMainByFid(fid);
	        
	    if(fid != null && !"".equals( fid )){
                page.setParameter("fid", fid);
               list = financeMainDetailDao.queryFinanceMainDetailList(page);
               setEveryDetailAmount(list,financeMain);
                page.setResults(list);
	    }
		return page;
	}
	
    private void setEveryDetailAmount(List<FinanceMainDetailCostVo> list,FinanceMain financeMain) {
        for ( int i = 0; i < list.size(); i++ ) {
            FinanceMainDetailCostVo detailBean = list.get( i );
            double amount = getAdetailAmount(detailBean,financeMain.getFinance_flowid());
            detailBean.setAmount( amount );
        }
    }

    private double getAdetailAmount(FinanceMainDetailCostVo detailBean,String finFlowId) {
        double result = 0;
        result = detailBean.getAllowancecost() + detailBean.getBridgecost() +detailBean.getCarcost() +
                detailBean.getCitytrafficcost() +detailBean.getFuelcost() +detailBean.getHuochecost() +
                detailBean.getIncidentalcost() +detailBean.getLongbuscost() +detailBean.getOthertrafficcost() +
                detailBean.getStaycost() + detailBean.getTicketcost()+                
                detailBean.getMeetingcost() +detailBean.getBusinessentertainment() +detailBean.getOfficecost() +
                detailBean.getWelfarism() +detailBean.getFamilymedicinecost() +
                detailBean.getMedicalinsurance() +detailBean.getTraincost() +detailBean.getPettycash();
        //油费和路桥费计算了两次，被计算到车辆费里面了，所以总额里面要排除一次
        result = result - detailBean.getFuelcost() - detailBean.getBridgecost();
        return result;
    }

    @Override
	public List<FinanceMainDetailCostVo> queryFinanceMainDetailCostListByFid(String fid) {
		List<FinanceMainDetailCostVo> list = financeMainDetailDao
				.queryFinanceMainDetailCostListByFid(fid);
		return list;
	}

	@Override
	public List<FinanceMainDetail> queryFinanceMainDetailByFid(String fid) {
		List<FinanceMainDetail> list = financeMainDetailDao
				.queryFinanceMainDetailByFid(fid);
		return list;
	}
	
	@Override
	public FinanceMainDetail queryFinanceMainDetailById(String id)  {
		FinanceMainDetail financeMainDetail = financeMainDetailDao.queryFinanceMainDetailById(id);
		return financeMainDetail;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public FinanceMainDetail historyFinanceMainDetail(FinanceMain financeMain)
			 {
		financeMainDetailDao.historyFinanceMainDetail(financeMain.getFid());
		return null;
	}
	
	@Override
        @Transactional(propagation = Propagation.REQUIRED)
        public FinanceMainDetail deleteFinanceMainDetail(FinanceMain financeMain)
                         {
                financeMainDetailDao.deleteFinanceMainDetail(financeMain.getFid());
                return null;
        }


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<FinanceMainDetail> insertFinanceMainDetail(FinanceMain getMain, FinInsertParams insertParams) {
	     String siteid = itcMvcService.getUserInfoScopeDatas().getSiteId();
	     String fid = getMain.getFid();  //报销单ID
	     String detail = insertParams.getDetail();  //报销明细
	     JSONArray jsonArr = JSONArray.fromObject(detail);  //报销明细
                 
             int docNbr = 0; //单据张数
             int allowance_days = 0; //补贴天数
             int stay_days = 0; //住宿天数
             int other_days = 0; //其他天数
             LinkedList<FinanceMainDetail> dtlList = new LinkedList<FinanceMainDetail>(); // 存储明细列表
             for (int i = 0; i < jsonArr.size(); i++) {
                     JSONObject aDetailJson  = jsonArr.getJSONObject(i);//一条明细的json格式数据
                     // 赋值 start
                     FinanceMainDetail financemainDetail = new FinanceMainDetail();
                     financemainDetail.setFid(fid); // 报销单编号
                     financemainDetail.setSiteid(siteid);
                     financemainDetail.setAmount(Double.parseDouble(aDetailJson.getString("amount"))); // 报销金额(元)
                     
                     //特殊字段的填充
                     fillFinancemainDetail(financemainDetail,getMain,insertParams,aDetailJson);
                     
                     String docNbrString = fieldTranslateTo(aDetailJson,"doc_nbr","int");
                     docNbr =Integer.valueOf( "".equals( docNbrString )?"0":docNbrString ) ;
                     String stay_daysString = fieldTranslateTo(aDetailJson,"stay_days","int");
                     stay_days = Integer.valueOf( "".equals( stay_daysString )?"0":stay_daysString ) ;
                     String allowance_daysString = fieldTranslateTo(aDetailJson,"allowance_days","int");
                     allowance_days = Integer.valueOf( "".equals( allowance_daysString )?"0":allowance_daysString ) ;
                     String other_daysString = fieldTranslateTo(aDetailJson,"other_days","int");
                     other_days = Integer.valueOf( "".equals( other_daysString )?"0":other_daysString ) ;
                     
                     financemainDetail.setAddress(fieldTranslateTo(aDetailJson,"address","string")); // 起止地址
                     financemainDetail.setDoc_nbr(docNbr); // 单据张数
                     financemainDetail.setDepartmentid(itcMvcService.getUserInfoById(financemainDetail.getBeneficiaryid()).getOrgId()); //部门编号
                     financemainDetail.setDepartment(itcMvcService.getUserInfoById(financemainDetail.getBeneficiaryid()).getOrgName()); //部门
                     financemainDetail.setAllowance_days(allowance_days); //补贴天数
                     financemainDetail.setAllowanceType( fieldTranslateTo(aDetailJson,"allowanceType","string") );  //补贴类型
                     financemainDetail.setStay_days(stay_days); //住宿天数
                     financemainDetail.setOther_days(other_days); //其他天数
                     financemainDetail.setRemark(fieldTranslateTo(aDetailJson,"remark","string")); // 备注
//                     financemainDetail.setSpremark(fieldTranslateTo(aDetailJson,"spremark","string")); // 特殊说明
                     // 插入报销明细表
                     financeMainDetailDao.insertFinanceMainDetail(financemainDetail);
                   //存明细费用表信息,依次插入每一条明细对应的多条费用记录
                     finMainDetailCostService.insertAFinMainDetailAllCost(aDetailJson,financemainDetail.getId(),siteid);
                     // 明细列表
                     dtlList.add(financemainDetail);
             }
             return dtlList;
	  }

    private void fillFinancemainDetail(FinanceMainDetail financemainDetail, FinanceMain getMain, 
            FinInsertParams insertParams,JSONObject aDetailJson) {
        
        String finTypeEn = getMain.getFinance_typeid();  //报销类型
        String beneficiaryid = insertParams.getBeneficiaryid();  //报销人
        String formData = insertParams.getFormData();  //form表单数据
        String beneficiaryids[] = beneficiaryid.split("_"); // 报销人编号
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if ( finTypeEn.equals( "only" ) ) {
            String joinNbrString = fieldTranslateTo( aDetailJson, "join_nbr", "int" );
            int joinNbr = Integer.valueOf( "".equals( joinNbrString ) ? "0" : joinNbrString ); // 参与人数
            if(aDetailJson.containsKey( "description" )){
                financemainDetail.setDescription( aDetailJson.getString( "description" ) ); // 报销事由
            }
            financemainDetail.setBeneficiaryid( getMain.getCreateid() ); // 报销人编号
            financemainDetail.setBeneficiary( getMain.getCreatorname() ); // 报销人
            financemainDetail.setJoin_boss( fieldTranslateTo( aDetailJson, "join_boss", "string" ) ); // 参与领导
            financemainDetail.setJoin_bossid( fieldTranslateTo( aDetailJson, "join_bossid", "string" ) ); // 参与领导编号
            financemainDetail.setJoin_nbr( joinNbr ); // 参与人数
            financemainDetail.setStrdate( fieldTranslateToDate( aDetailJson, "strdate" ) ); // 开始日期
            financemainDetail.setEnddate( fieldTranslateToDate( aDetailJson, "enddate" ) ); // 结束日期

        } else if ( finTypeEn.equals( "other" ) ) {
            String joinNbrString = fieldTranslateTo( aDetailJson, "join_nbr", "int" );
            int joinNbr = Integer.valueOf( "".equals( joinNbrString ) ? "0" : joinNbrString );
           if(aDetailJson.containsKey( "description" )){
               financemainDetail.setDescription( aDetailJson.getString( "description" ) ); // 报销事由
           }
            financemainDetail.setBeneficiaryid( beneficiaryids[0] ); // 报销人编号
            financemainDetail.setBeneficiary( FinanceUtil.getJsonFieldString( formData, "beneficiary" ) ); // 报销人
            financemainDetail.setJoin_nbr( joinNbr ); // 参与人数
            financemainDetail.setJoin_boss( fieldTranslateTo( aDetailJson, "join_boss", "string" ) ); // 参与领导
            financemainDetail.setJoin_bossid( fieldTranslateTo( aDetailJson, "join_bossid", "string" ) ); // 参与领导编号
            financemainDetail.setStrdate( fieldTranslateToDate( aDetailJson, "strdate" ) ); // 开始日期
            financemainDetail.setEnddate( fieldTranslateToDate( aDetailJson, "enddate" ) ); // 结束日期

        } else if ( finTypeEn.equals( "more" ) ) {
            String joinNbrString = FinanceUtil.getJsonFieldString( formData, "join_nbr" );
            int joinNbr = Integer.parseInt( joinNbrString.length() == 0 ? "0" : joinNbrString ); // 参与人数
            
                String strDateString = FinanceUtil.getJsonFieldString( formData, "strdate" );// 开始日期
                String endDateString = FinanceUtil.getJsonFieldString( formData, "enddate" );// 结束日期
                Date strDate = null;
                Date endDate = null;
                if(!"".equals( strDateString )){
                   // strDate = sdf.parse(strDateString);
                    strDate = new Date(Long.valueOf( strDateString));
                }
                if(!"".equals( endDateString )){
                   // endDate = sdf.parse(endDateString);
                    endDate = new Date(Long.valueOf( endDateString));
                }
                financemainDetail.setStrdate( strDate ); // 开始日期
                financemainDetail.setEnddate( endDate ); // 结束日期
           
            financemainDetail.setDescription( FinanceUtil.getJsonFieldString( formData, "description" ) ); // 报销事由
            financemainDetail.setBeneficiaryid( aDetailJson.getString( "beneficiaryid" ) ); // 报销人编号
            financemainDetail.setBeneficiary( aDetailJson.getString( "beneficiary" ) ); // 报销人
            financemainDetail.setJoin_nbr( joinNbr ); // 参与人数
            financemainDetail.setJoin_boss( FinanceUtil.getJsonFieldString( formData, "join_boss" ) ); // 参与领导
            financemainDetail.setJoin_bossid( FinanceUtil.getJsonFieldString( formData, "join_bossid" ) ); // 参与领导编号
        }

    }


    private Date fieldTranslateToDate(JSONObject jsonObject, String fieldName) {
        Date resultDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if( jsonObject.containsKey(fieldName) ) {
            try {
                    
                    if( !jsonObject.getString(fieldName).equals("null")) {
                        resultDate = sdf.parse(jsonObject.getString(fieldName)); //开始日期
                    }
            } catch (ParseException e) {
                    // 如果前台传过来的是long类型
                    long lStrDate = jsonObject.getLong(fieldName);
                    resultDate = new Date(lStrDate);
            }
        }
        return resultDate;
    }

    /**
     * @description:类型转换
     * @author: 王中华
     * @createDate: 2015-8-20
     * @param jsonObject
     * @param fieldName
     * @param translateType
     * @return:
     */
    private String fieldTranslateTo(JSONObject jsonObject, String fieldName, String translateType) {
        String result= "";
        if( jsonObject.containsKey(fieldName) ) {
            try {
                if("int".equals( translateType )){
                    String resultString = jsonObject.getString( fieldName );
                    int resultInt = 0;
                    if(!"".equals( resultString )){
                        resultInt = Integer.parseInt(resultString);
                    }
//                    int resultInt = jsonObject.getInt(fieldName); 
                    result = String.valueOf( resultInt );
                }
                if("string".equals( translateType )){
                    result = jsonObject.getString(fieldName); 
                    if("null".equals( result )){
                        result = null;
                    }
                }
            } catch (JSONException e) {
                    logger.info(fieldName +"  为空");
                    throw new RuntimeException( e );
            }
        }
        return result;
    }

    @Override
    public int updateFinDetailAllowanceType(String id,String allwoanceType,double amount,double allowancecost) {
        String costId = "allowancecost";  //修改补贴费的金额
        finMainDetailCostService.updateAFinMainDetailAmount( id, costId, allowancecost );
        return financeMainDetailDao.updateFinDetailAllowanceType( id, allwoanceType, amount );
    }
}
