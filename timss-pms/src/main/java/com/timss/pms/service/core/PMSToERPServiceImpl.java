package com.timss.pms.service.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.finance.vo.ErpIdsControl;
import com.timss.finance.vo.ErpIdsGLInt;
import com.timss.pms.bean.AccountSubject;
import com.timss.pms.bean.BodyPartOfERPIB;
import com.timss.pms.bean.ERPInBrower;
import com.timss.pms.bean.ERPResponse;
import com.timss.pms.bean.HeaderPartOfERPIB;
import com.timss.pms.dao.ContractDao;
import com.timss.pms.dao.InvoiceDao;
import com.timss.pms.dao.PayDao;
import com.timss.pms.service.PMSToERPService;
import com.timss.pms.util.NumberUtil;
import com.timss.pms.util.XMLPaerserUtil;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.InvoiceVo;
import com.timss.pms.vo.PayDtlVo;
import com.yudean.interfaces.service.IEsbInterfaceService;
import com.yudean.itc.dto.interfaces.esb.ErpPutParam;
import com.yudean.mvc.bean.userinfo.UserInfo;

@Service
public class PMSToERPServiceImpl implements PMSToERPService {
    private static final String ERP_SYSTERMNAME = "EBS";

	public static final String SYSTERMNAME = "TIMSS";

	@Autowired
    IEsbInterfaceService  iEsbInterfaceService;
	@Autowired
	PayDao payDao;
	@Autowired
	ContractDao contractDao;
	@Autowired
	InvoiceDao invoiceDao;
    
    private static final Logger LOGGER=Logger.getLogger(PMSToERPServiceImpl.class);
    
    
	@Override
	public ERPResponse sendReceiptMessage(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body, UserInfo userInfo) {
		
		try {
			LOGGER.info("准备推送收款凭证数据给ERP,数据为payId:"+payId+",header:"+header+",body:"+body+",userid:"+userInfo.getUserId());
			//根据收款数据组装发送给ERP数据
			ErpPutParam erpPutParam = assembleReceiptERPPutParam(payId,header,body,userInfo);
			
			LOGGER.info("发送给erp的数据为data:"+erpPutParam.getData()+",payId:"+payId);
			//推送凭证数据给ERP
			String responseFromERP = iEsbInterfaceService.purErpData(erpPutParam);
			LOGGER.debug("erp的返回数据为data:"+responseFromERP+",payId:"+payId);
			//解析ERP的响应数据，并根据放回结果更新结算的凭证发送状态
			ERPResponse response=parseResponseAndUpdatePayStatus(responseFromERP,payId);
			LOGGER.info("erp处理结果为"+responseFromERP+",payId:"+payId+",发送数据为data："+erpPutParam.getData());
			return response;
		} catch (Exception e) {
			throw new RuntimeException("推送ERP收款数据时出错，id为"+payId+",用户为"+userInfo.getUserId()+",erpHeader:"+header+",erpBody:"+body, e);
		}
		
		
		
	}
	
	//构造erp推送肿胀接口所属参数
	private ErpPutParam assembleReceiptERPPutParam(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body,
			UserInfo userInfo) {
		//Erp接口接受的参数
		ErpPutParam erpPutParam=new ErpPutParam();
		//构造接口参数主要内容data值
		List<Object> erpPutParamData=assembleReceiptERPPutParamData(payId,header,body,userInfo);
		erpPutParam.setData(XMLPaerserUtil.parseObjectToXMLString(erpPutParamData));
		return erpPutParam;
	}
	//构造erp推送肿胀接口所属参数的data属性值（javaBean表示）
	private List<Object> assembleReceiptERPPutParamData(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body,
			UserInfo userInfo) {
		List<Object> erpPutParamData=new ArrayList<Object>();
		//构造erp接口所需的erpIdsControl内容
		ErpIdsControl erpIdsControl = assembleERPErpIdsControl(payId,header,body,userInfo);
		erpPutParamData.add(erpIdsControl);
		
		//构造erp接口所需的erpIdsGLInt内容，该内容可能存在多个
		
		List<ErpIdsGLInt> erpIdsGLInts=assembleERPErpIdsGLInts(payId,header,body,userInfo,erpIdsControl);
		if(erpIdsGLInts!=null && !erpIdsGLInts.isEmpty()){
			for(int i=0;i<erpIdsGLInts.size();i++){
				erpPutParamData.add(erpIdsGLInts.get(i));
			}
		}
		return erpPutParamData;
	}
	
	//构建ErpIdsControl
	private ErpIdsControl assembleERPErpIdsControl(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body,
			UserInfo userInfo)  {
		ErpIdsControl erpIdsControl=new ErpIdsControl();
		//从ERP获取controlId
		String controlId = getControlIdFromERP(payId,header,body, userInfo);
		BigDecimal recordCountDec=new BigDecimal(3);
		Date date=new Date();//创建时间，修改时间
		
		erpIdsControl.setControlId(new BigDecimal(controlId));
		erpIdsControl.setSourceCode(SYSTERMNAME); //来源系统
		String compCode=userInfo.getSiteId();
        erpIdsControl.setSourceCompCode(compCode); //来源系统的公司三字码
        erpIdsControl.setTargetCode(ERP_SYSTERMNAME); //目标系统
        erpIdsControl.setTargetCompCode(compCode); //目标系统的公司三字码
        erpIdsControl.setTargetModel("GL");   //ERP模块总账模块
        erpIdsControl.setProcessStatus("U");
        erpIdsControl.setReceiveStatus("");
        erpIdsControl.setRecordCount(recordCountDec); //本批数据笔数
        erpIdsControl.setErrorMessage("");
        
        erpIdsControl.setAttribute2("外围报销系统");
        
        erpIdsControl.setCreationDate(date);
        //创建ERP默认值
        erpIdsControl.setCreatedBy(BigDecimal.valueOf(-1));
        erpIdsControl.setLastUpdateDate(date);
        erpIdsControl.setLastUpdatedBy(BigDecimal.valueOf(-1));
        erpIdsControl.setAttribute2("");
		return erpIdsControl;
	}
	private String getControlIdFromERP(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body, UserInfo userInfo) {
		String controlId=null;
		try {
			//获取controlId
			controlId= iEsbInterfaceService.getErpControlID();
			LOGGER.debug("payId="+payId+"获取到ERP控制信息controlId为"+controlId);
		} catch (Exception e) {
			throw new RuntimeException("获取ERP控制信息ErpControlID时出错", e);
		}
		return controlId;
	}
	//构造erpIdsGLInt
	private List<ErpIdsGLInt> assembleERPErpIdsGLInts(String payId, HeaderPartOfERPIB header, List<BodyPartOfERPIB> body,
			UserInfo userInfo,ErpIdsControl erpIdsControl) {
		List<ErpIdsGLInt> erpIdsGLInts=new ArrayList<ErpIdsGLInt>();
		
		Date date=new Date();
		//循环构造erpIdsGLInt
		for(int i=0;i<body.size();i++){
			ErpIdsGLInt erpIdsGLInt=new ErpIdsGLInt();
			erpIdsGLInt.setControlId(erpIdsControl.getControlId());
	        erpIdsGLInt.setCompCode(userInfo.getSiteId());
	        erpIdsGLInt.setSourceDocNum("收款"+payId);
	        erpIdsGLInt.setAccountingDate(date);
	        erpIdsGLInt.setCurrencyCode(header.getfCcy());
	        erpIdsGLInt.setUserJeCategoryName(header.getfCertType());
	        //源系统名称
	        erpIdsGLInt.setUserJeSourceName("外围报销系统");
	        erpIdsGLInt.setPeriodName(header.getfAccMonth());
	        //货币利率
	        erpIdsGLInt.setCurrencyConversionRate(new BigDecimal(1.00));
	        erpIdsGLInt.setJeDesc(header.getfCertHeadDesc());
	        erpIdsGLInt.setJeLineNum(new Long((i+1)*10));
	        erpIdsGLInt.setLineDesc(body.get(i).getCertrowdesc());
	        erpIdsGLInt.setEnteredDr(body.get(i).getDebitamt().equals("0") ? null : new BigDecimal(body.get(i).getDebitamt()));
	        erpIdsGLInt.setEnteredCr(body.get(i).getCreditamt().equals("0") ? null : new BigDecimal(body.get(i).getCreditamt()));
	        //分解科目并赋值给erp参数
	        AccountSubject accountSubject=new AccountSubject(body.get(i).getSubject());
	        accountSubject.dispart();
	        erpIdsGLInt.setSegment1(accountSubject.getSegment1());
	        erpIdsGLInt.setSegment2(accountSubject.getSegment2());
	        erpIdsGLInt.setSegment3(accountSubject.getSegment3());
	        erpIdsGLInt.setSegment4(accountSubject.getSegment4());
	        erpIdsGLInt.setSegment5(accountSubject.getSegment5());
	        erpIdsGLInt.setSegment6(accountSubject.getSegment6());
	        erpIdsGLInt.setSegment7(accountSubject.getSegment7());
	        erpIdsGLInt.setSegment8(accountSubject.getSegment8());
	        erpIdsGLInt.setSegment9(accountSubject.getSegment9());
	        erpIdsGLInt.setSegment10(accountSubject.getSegment10());
	       
	        erpIdsGLInt.setAttribute21(body.get(i).getCashitem());
	        erpIdsGLInt.setAttribute22(body.get(i).getIntervalunit());

	        erpIdsGLInt.setCreationDate(date);
	        erpIdsGLInt.setCreatedBy(BigDecimal.valueOf(-1));
	        erpIdsGLInt.setLastUpdateDate(date);
	        erpIdsGLInt.setLastUpdatedBy(BigDecimal.valueOf(-1));
	        
			erpIdsGLInts.add(erpIdsGLInt);
		}
		return erpIdsGLInts;
	}
	
	private ERPResponse parseResponseAndUpdatePayStatus(String responseFromERP,String payId) {
		//解析erp返回值
		ERPResponse response=ERPResponse.parseResult(responseFromERP);
		//根据erp的返回结果，判断是否更新结算的凭证发送状态
        if( "1".equals(response.getResult()) ) {
        	payDao.updateSendedToERP("1", payId);
        } 
        return response;
		
	}


	@Override
	public ERPInBrower getERPDataFromReceiptId(String payId,
			UserInfo userInfo) {
		//准备基础数据
		List<InvoiceVo> invoiceVo=invoiceDao.queryInvoiceListByPayId(Integer.parseInt(payId));
		double debitamt1=0;
		double creditamt2=0;
		double creditamt3=0;
		boolean is17 =false;//发票税率是否为17%
		boolean is0=false;
		if(invoiceVo!=null&&!invoiceVo.isEmpty()){
			for(int i=0;i<invoiceVo.size();i++){
				creditamt2+=invoiceVo.get(i).getTax();
				creditamt3+=invoiceVo.get(i).getWithoutTax();
				debitamt1+=invoiceVo.get(i).getSum();
				if(invoiceVo.get(i).getRate()==17){
					is17=true;
				}
				if(invoiceVo.get(i).getRate()==0){
					is0=true;
				}
			}
		}
		//前端所需数据
		ERPInBrower erpInBrower=new ERPInBrower();
		//凭证基本信息生成
		HeaderPartOfERPIB header=new HeaderPartOfERPIB();
		PayDtlVo payDtlVo=payDao.queryPayById(Integer.parseInt(payId));
		ContractDtlVo contractDtlVo=contractDao.queryContractById(payDtlVo.getContractId());
		header.setfCertHeadDesc("本月确认收入("+contractDtlVo.getFirstParty()+","+contractDtlVo.getContractCode()+")");
		erpInBrower.setHeader(header);
		
		List<BodyPartOfERPIB> bodys=new ArrayList<BodyPartOfERPIB>();
		//借方body构造
		BodyPartOfERPIB body1=new BodyPartOfERPIB();
		body1.setSubject("ITC.0.112201.0.0.0.0.0.0.0");
		body1.setSubjectremark("科技公司.-.应收账款-应收账款.-.-.-.-.-.-.-");
		body1.setDebitamt(NumberUtil.to2PrecisionString(debitamt1));
		body1.setCreditamt("0");
		body1.setCertrowdesc("本月确认应收("+contractDtlVo.getFirstParty()+","+contractDtlVo.getContractCode()+")");
		bodys.add(body1);
		
		//贷方1 body构造
		BodyPartOfERPIB body2=new BodyPartOfERPIB();
		body2.setSubject("ITC.0.22210105.0.0.0.0.0.0.0");
		body2.setSubjectremark("科技公司.-.应交税费-应交增值税-销项税额.-.-.-.-.-.-.-");
		body2.setDebitamt("0");
		body2.setCreditamt(NumberUtil.to2PrecisionString(creditamt2));
		body2.setCertrowdesc("本月开票计提销项税额");
		bodys.add(body2);
		erpInBrower.setBodys(bodys);
		
		//贷方3 body构造
		BodyPartOfERPIB body3=new BodyPartOfERPIB();
		AccountSubject accountSubject=new AccountSubject();
		accountSubject.setSegment1("ITC");
		accountSubject.setSegment2("0");
		accountSubject.setSegment3("600199");
		accountSubject.setSegment4(is17?"0":"19760019901");
		accountSubject.setSegment5("0");
		accountSubject.setSegment6("0");
		accountSubject.setSegment7(is17?"ITC-001":"ITC-002");
		accountSubject.setSegment8("0");
		accountSubject.setSegment9(is0?"ITC-001":"0");
		accountSubject.setSegment10("0");
		accountSubject.merge();
		body3.setSubject(accountSubject.getContent());
		body3.setSubjectremark("科技公司.-.主营业务收入-其他.-.-.-.-.-.-.-");
		body3.setDebitamt("0");
		body3.setCreditamt(NumberUtil.to2PrecisionString(creditamt3));
		body3.setCertrowdesc("本月确认收入("+contractDtlVo.getFirstParty()+","+contractDtlVo.getContractCode()+")");
		bodys.add(body3);
		
		erpInBrower.setBodys(bodys);
		return erpInBrower;
	}

}
