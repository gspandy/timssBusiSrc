package com.timss.pms.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.timss.pms.vo.BidDtlVo;
import com.timss.pms.vo.BidResultVo;
import com.timss.pms.vo.BidVo;
import com.timss.pms.vo.CheckoutVo;
import com.timss.pms.vo.ContractVo;
import com.timss.pms.vo.PayVo;
import com.timss.pms.vo.PayplanVo;
import com.timss.pms.vo.PlanVo;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;


/**
 * 
 * @ClassName:     InitVoEnumUtil
 * @company: gdyd
 * @Description: 对vo对象的枚举类型进行初始化的帮助类
 * @author:    黄晓岚
 * @date:   2014-7-3 上午11:40:53
 */
public class InitVoEnumUtil {
    private static String bidTypeEnum="PMS_BID_TYPE";
    
    private static String projectTypeEnum="pms_project_type";
    
    private static String contractTypeEnum="PMS_CONTRACT_TYPE";
    
    private static String status="PMS_STATUS";
    
    private static String payplanStage="PMS_PAYPLAN_STAGE";
    
    private static String checkType="PMS_CHECKOUT_TYPE";
    
    
    private static final Logger LOGGER=Logger.getLogger(InitVoEnumUtil.class);
    
    //将枚举变量code转换为对应的val
    public static String getEnumVal(String enumCode,List<AppEnum> list){
    	String val=null;
    	if(enumCode==null || enumCode.equals("")){
    		return null;
    	}
    	if(list!=null){
    		for(int i=0;i<list.size();i++){
    			AppEnum appEnum=list.get(i);
    			if(enumCode.equals(appEnum.getCode())){
    				val=appEnum.getLabel();
    				break;
    			}
    		}
    	}
    	return val;
    }
	
    /**
     * 
     * @Title: initBidVoList
     * @Description: 将bidVo 该bean类中的枚举变量进行赋值
     * @param lists
     * @param itcMvcService
     * @throws Exception
     */
	public static void initBidVoList(List<BidVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目的招标类型信息
			List<AppEnum> typeEnums;
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			try {
				typeEnums=itcMvcService.getEnum(bidTypeEnum);
			} catch (Exception e) {
				LOGGER.warn("转换bidVo的枚举类型时，获取不到枚举变量");
				return ;
			}
			
			for(int i=0;i<lists.size();i++){
				BidVo bidVo=lists.get(i);
				if(bidVo!=null){
					String type=bidVo.getType();
					bidVo.setTypeName(getEnumVal(type,typeEnums));
					String status=bidVo.getStatus();
					bidVo.setStatusValue(getEnumVal(status, statusEnums));
				}
			}
		}
	}
	
	public static void initBidResultVoList(List<BidResultVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目的招标类型信息
			List<AppEnum> typeEnums;
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			try {
				typeEnums=itcMvcService.getEnum(bidTypeEnum);
			} catch (Exception e) {
				LOGGER.warn("转换bidVo的枚举类型时，获取不到枚举变量");
				return ;
			}
			
			for(int i=0;i<lists.size();i++){
				BidResultVo bidVo=lists.get(i);
				if(bidVo!=null){
					String type=bidVo.getType();
					bidVo.setTypeName(getEnumVal(type,typeEnums));
					String status=bidVo.getStatus();
					bidVo.setStatusName(getEnumVal(status, statusEnums));
				}
			}
		}
	}
	
	/**
	 * 将bidDtlVo 该bean类中的枚举变量进行赋值
	 * @Title: initBidDtlVo
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bidDtlVo
	 * @param itcMvcService
	 */
	public static void initBidDtlVo(BidDtlVo bidDtlVo,ItcMvcService itcMvcService){
		if(bidDtlVo!=null){
			//获取项目的招标类型信息
			List<AppEnum> typeEnums;
			try {
				typeEnums=itcMvcService.getEnum(bidTypeEnum);
			} catch (Exception e) {
				LOGGER.warn("转换bidDtlVo的枚举类型时，获取不到枚举变量");
				return ;
			}
	
			String type = bidDtlVo.getType();
			bidDtlVo.setTypeName(getEnumVal(type, typeEnums));

		}
	}
	
	/**
	 * 将planVo 该bean类的枚举变量进行赋值
	 * @Title: initPlanVoList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param lists
	 * @param itcMvcService
	 */
	public static void initPlanVoList(List<PlanVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目类型信息
			List<AppEnum> typeEnums=itcMvcService.getEnum(projectTypeEnum);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			for(int i=0;i<lists.size();i++){
				PlanVo planVo=lists.get(i);
				if(planVo!=null){
					String type=planVo.getType();
					planVo.setTypeName(getEnumVal(type,typeEnums));
					
					String status=planVo.getStatus();
					planVo.setStatusValue(getEnumVal(status, statusEnums));
				}
			}
		}
	}
	
	/**
	 * 将projectVo该bean类的枚举类型进行赋值
	 * @Title: initProjectVoList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param lists
	 * @param itcMvcService
	 */
	public static void initProjectVoList(List<ProjectVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目类型信息
			List<AppEnum> typeEnums=itcMvcService.getEnum(projectTypeEnum);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			for(int i=0;i<lists.size();i++){
				ProjectVo projectVo=lists.get(i);
				if(projectVo!=null){
					String type=projectVo.getPtype();
					projectVo.setTypeName(getEnumVal(type,typeEnums));
					
					String status=projectVo.getStatus();
					projectVo.setStatusValue(getEnumVal(status, statusEnums));
				}
			}
		}
	}
	
	public static void initContractVoList(List<ContractVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目类型信息
			List<AppEnum> typeEnums=itcMvcService.getEnum(contractTypeEnum);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			for(int i=0;i<lists.size();i++){
				ContractVo contractVo=lists.get(i);
				if(contractVo!=null){
					String type=contractVo.getType();
					contractVo.setTypeName(getEnumVal(type,typeEnums));
					
					//String status=contractVo.getStatus();
					String statusApp=contractVo.getStatusApp();
					String status = contractVo.getStatus();
					contractVo.setStatusValue(getEnumVal(statusApp, statusEnums));
					contractVo.setStatusChangeValue(getEnumVal(status, statusEnums));
				}
			}
		}
	}
	
	/**
	 * 初始化省风电站点的枚举变量
	 * @Title: initSFCContractVoList
	 * @param lists
	 * @param itcMvcService
	 */
	public static void initSFCContractVoList(List<ContractVo> lists,ItcMvcService itcMvcService){
		
		List<AppEnum> catEnums=itcMvcService.getEnum("PMS_CONTRACT_CATEGORY");
		List<AppEnum> belongToEnums=itcMvcService.getEnum("PMS_CONTRACT_BELONGTO");
		List<AppEnum> statusEnums=itcMvcService.getEnum(status);
		for(int i=0;i<lists.size();i++){
			ContractVo contractVo=lists.get(i);
			if(contractVo!=null){
				
				contractVo.setContractCategoryName(getEnumVal(contractVo.getContractCategory(),catEnums));
				String status = contractVo.getStatus();
				contractVo.setStatusValue(getEnumVal(status, statusEnums));
				contractVo.setBelongToName(getEnumVal(contractVo.getBelongTo(),belongToEnums));
			}
		}
	}
	
	public static void initPayplanVoList(List<PayplanVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			//获取项目类型信息
			List<AppEnum> typeEnums=itcMvcService.getEnum(payplanStage);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			for(int i=0;i<lists.size();i++){
				PayplanVo payplanVo=lists.get(i);
				if(payplanVo!=null){
					String type=payplanVo.getPayType();
					payplanVo.setTypeName(getEnumVal(type,typeEnums));
					
				
				}
			}
		}
	}
	
	public static void initCheckoutVoList(List<CheckoutVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
			List<AppEnum> typeEnums=itcMvcService.getEnum(checkType);
			List<AppEnum> conAppEnums=itcMvcService.getEnum(contractTypeEnum);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			List<AppEnum> payplanEnums=itcMvcService.getEnum(payplanStage);
			for(int i=0;i<lists.size();i++){
				CheckoutVo checkoutVo=lists.get(i);
				if(checkoutVo!=null){
					String type=checkoutVo.getType();
					checkoutVo.setCheckTypeName(getEnumVal(type,typeEnums));
					
					String statusName=checkoutVo.getStatus();
					checkoutVo.setStatusName(getEnumVal(statusName, statusEnums));
					
					String contractType=checkoutVo.getContractType();
					checkoutVo.setContractType(getEnumVal(contractType, conAppEnums));
					
					//String payType=checkoutVo.getPayTypeName();
					//checkoutVo.setPayTypeName(getEnumVal(payType, payplanEnums));
				}
			}
		}
	}
	
	public static void initPayVoList(List<PayVo> lists,ItcMvcService itcMvcService){
		if(lists!=null){
	        
                        List<AppEnum> conAppEnums=itcMvcService.getEnum(contractTypeEnum);
			List<AppEnum> statusEnums=itcMvcService.getEnum(status);
			//补充一个退票中的枚举
			AppEnum appEnum = new AppEnum();
			appEnum.setCategoryCode( "PMS_STATUS" );
			appEnum.setCode( "undoing" );
			appEnum.setLabel( "退票中" );
			appEnum.setSiteId( "ITC" );
			statusEnums.add( appEnum );
			List<AppEnum> payplanEnums=itcMvcService.getEnum(payplanStage);
			for(int i=0;i<lists.size();i++){
				PayVo payVo=lists.get(i);
				if(payVo!=null){
					
					
					String statusName=payVo.getStatus();
					payVo.setStatusName(getEnumVal(statusName, statusEnums));
					
//					String contractType=payVo.getContractType();
//					payVo.setContractType(getEnumVal(contractType, conAppEnums));
					
//					String payType=payVo.getPayTypeName();
//					payVo.setPayTypeName(getEnumVal(payType, payplanEnums));
				}
			}
		}
	}
	
	public static boolean isCodeInEnum(String enumCode,String enumId,ItcMvcService itcMvcService){
		List<AppEnum> appEnums=itcMvcService.getEnum(enumId);
		String enumVal=getEnumVal(enumCode, appEnums);
		if(enumVal==null){
			return false;
		}else{
			return true;
		}
		
	}
	
}
