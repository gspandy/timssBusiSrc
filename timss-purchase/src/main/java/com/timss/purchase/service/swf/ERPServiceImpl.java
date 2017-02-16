package com.timss.purchase.service.swf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.timss.purchase.bean.ErpIdsApInvoicesIntVo;
import com.timss.purchase.bean.ErpIdsApInvoicesLinesIntVo;
import com.timss.purchase.bean.ErpIdsControlVo;
import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.service.ERPService;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.interfaces.service.IEsbInterfaceService;
import com.yudean.itc.dto.interfaces.esb.ErpPutParam;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSysConfService;


/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ERPServiceImpl.java
 * @author: 890124
 * @createDate: 2016-5-19
 * @updateUser: 890124
 * @version: 1.0
 */
@Service
public class ERPServiceImpl implements ERPService {

	private static final Logger LOGGER = Logger.getLogger(ERPServiceImpl.class);
	@Autowired
    IEsbInterfaceService iEsbInterfaceService;
    @Autowired
	PurPayService purPayService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    PurPayDao purPayDao;
    @Autowired
    ItcSysConfService itcSysConfService;
    
	@SuppressWarnings("deprecation")
        @Override
	public String sendToERP(String payId,String invNum,String invDesc) throws Throwable {
		
		UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
		PurPayVO purPayVo = purPayService.queryPurPayVoByPayId(userInfo, payId);
		List<PurPayDtlVO> payDtlList = purPayService.queryPurPayDtlVoListByCondition(userInfo, payId, null, null);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateString = formatter.format(date);
		
		ErpPutParam erpPutParam = new ErpPutParam();
		erpPutParam.setServiceID("ap_invoice");//ESB规定,应付为ap_invoice
		String controllId = getControlId();
		List<Object> erpList = new ArrayList<Object>();
		ErpIdsControlVo erpIdsControl =new ErpIdsControlVo();
		Map<String, BigDecimal> payMap = groupPurPayDtl(payDtlList);
		//设置ErpIdsControl节点的数据
		erpIdsControl.setControlId(new BigDecimal(controllId));
		erpIdsControl.setSourceCode("TIMSS");
		erpIdsControl.setSourceCompCode(userInfo.getSiteId()); //这个需要确认
		erpIdsControl.setTargetCode("EBS");       //这个需要确认
		erpIdsControl.setTargetCompCode(userInfo.getSiteId());   //需要确认
		erpIdsControl.setTargetModel("AP");       
		erpIdsControl.setProcessStatus("U");
		erpIdsControl.setRecordCount(1);//发票张数，目前每个付款只有一张发票
		erpIdsControl.setCreationDate(dateString);
		erpIdsControl.setCreatedBy(new BigDecimal(userInfo.getUserId()));
	    erpIdsControl.setLastUpdateDate(dateString);
	    erpIdsControl.setLastUpdatedBy(new BigDecimal(userInfo.getUserId()));
	    erpList.add(erpIdsControl);
	    
		//对应ERP的应付主表,支付审批表中任何类型都存在主表信息；
		ErpIdsApInvoicesIntVo erpIdsApInvoicesIntVo = new ErpIdsApInvoicesIntVo();
		
		//设置应付主表的数据
		erpIdsApInvoicesIntVo.setControlId(new BigDecimal(controllId));
		erpIdsApInvoicesIntVo.setCompCode(userInfo.getSiteId());
		erpIdsApInvoicesIntVo.setInvType("标准发票");//需要考虑处理预付款的
		erpIdsApInvoicesIntVo.setInvNum(invNum);
		erpIdsApInvoicesIntVo.setInvoiceDate(dateString);
		erpIdsApInvoicesIntVo.setAccountingDate(dateString);
		erpIdsApInvoicesIntVo.setVendorCode(purPayVo.getSupplierCode());//供应商编码
		erpIdsApInvoicesIntVo.setVendorName(purPayVo.getSupplierName());
		erpIdsApInvoicesIntVo.setVendorSiteCode("材料"); //目前只有材料这个类型，后续需要改为从数据库中读取
		//erpIdsApInvoicesIntVo.setItemType(itemType);
		erpIdsApInvoicesIntVo.setInvoiceAmount(new BigDecimal(purPayVo.getPay()).setScale(2, BigDecimal.ROUND_HALF_UP));
		erpIdsApInvoicesIntVo.setInvoiceDesc(invDesc);
		erpIdsApInvoicesIntVo.setCreationDate(dateString);
		erpIdsApInvoicesIntVo.setCreatedBy(new BigDecimal(userInfo.getUserId()));
		erpIdsApInvoicesIntVo.setLastUpdateDate(dateString);
		erpIdsApInvoicesIntVo.setLastUpdatedBy(new BigDecimal(userInfo.getUserId()));
		erpIdsApInvoicesIntVo.setCurrencyCode("CNY"); //货币，目前只有人民币
		//erpIdsApInvoicesIntVo.setInvoiceFmoney(invoiceFmoney);
		//erpIdsApInvoicesIntVo.setInvoiceVat(invoiceVat);
		
		erpList.add(erpIdsApInvoicesIntVo);

		//Map<String, BigDecimal> payMap = groupPurPayDtl(payDtlList);
		int i = 1;
		for (String key : payMap.keySet()) {
			//System.out.println("key= "+ key + " and value= " + map.get(key));
			ErpIdsApInvoicesLinesIntVo erpIdsApInvoicesLinesIntVo=new ErpIdsApInvoicesLinesIntVo();
			//设置应付明细数据
			erpIdsApInvoicesLinesIntVo.setControlId(new BigDecimal(controllId));
			erpIdsApInvoicesLinesIntVo.setCompCode(userInfo.getSiteId());
			erpIdsApInvoicesLinesIntVo.setInvNum(invNum);
			erpIdsApInvoicesLinesIntVo.setLineNum(new BigDecimal(i));
			erpIdsApInvoicesLinesIntVo.setLineType(key);
			erpIdsApInvoicesLinesIntVo.setLineAmount(payMap.get(key).setScale(2, BigDecimal.ROUND_HALF_UP));
			erpIdsApInvoicesLinesIntVo.setLineDesc(invDesc);
			erpIdsApInvoicesLinesIntVo.setCreationDate(dateString);
			erpIdsApInvoicesLinesIntVo.setCreatedBy(new BigDecimal(userInfo.getUserId()));
			erpIdsApInvoicesLinesIntVo.setLastUpdateDate(dateString);
			erpIdsApInvoicesLinesIntVo.setLastUpdatedBy(new BigDecimal(userInfo.getUserId()));
			i++;
			erpList.add(erpIdsApInvoicesLinesIntVo);
		}
		//处理税额
//		ErpIdsApInvoicesLinesIntVo erpIdsApRax=new ErpIdsApInvoicesLinesIntVo();
//
//		erpList.add(erpIdsApInvoicesLinesIntVo);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("ErpIdsControl", ErpIdsControlVo.class);
		xstream.alias("ErpIdsApInvoice", ErpIdsApInvoicesIntVo.class);
		xstream.alias("ErpIdsApInvoiceLine",ErpIdsApInvoicesLinesIntVo.class);
	    String xmlData = xstream.toXML(erpList);
	    
	    String resultData;
	    erpPutParam.setData(xmlData);

	    resultData = iEsbInterfaceService.purErpData(erpPutParam);
	    LOGGER.info(resultData);
	    String result = getResult(resultData);
	    if("1".equals(result)){
		    //修改业务
		    purPayDao.updatePurPayERPStatus(payId, "1",new Date());
	    }

	    return result;
	}
	
	public String getControlId() throws Throwable{
		//String ControIdUrl="http://udx-dev.gdyd.com:8081/rest/erp/controlid";//测试环境
		//	   ControIdUrl="http://udx.gdyd.com:8081/rest/erp/controlid";//生产环境
		
		String ControIdUrl = itcSysConfService.queryBSysById("interface_ESB_ErpControlIdURL", "NaN").getVal();
		URL url=new URL(ControIdUrl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
		String inputLine = "";
		String ControlId="";
		while ((inputLine = in.readLine()) != null) {
			
			ControlId=inputLine;
		}
		return  ControlId;
	}
	
	/**
	 * 对付款明细进行分组
	 * @description:
	 * @author: 890124
	 * @createDate: 2016-6-1
	 * @param payDtlList
	 * @return:
	 */
	private Map<String, BigDecimal> groupPurPayDtl(List<PurPayDtlVO> payDtlList){
		
		if(payDtlList == null || payDtlList.size() == 0){
			return null;
		}
		String tax = "TAX";
		double taxValue = 0;
		Map<String, BigDecimal> payMap = new HashMap<String, BigDecimal>();
		for (PurPayDtlVO payDtlVO : payDtlList) {
			String invateName = payDtlVO.getInvcateName();
			//taxValue.add(new BigDecimal(payDtlVO.getTaxPrice()));
			taxValue += payDtlVO.getTaxTotal(); 
			BigDecimal lineAmount = new BigDecimal(payDtlVO.getNotaxTotal()); //这里需要确认
			if(payMap.containsKey(invateName)){
				BigDecimal value = payMap.get(invateName);
				payMap.put(invateName, value.add(lineAmount));
			}else{
				payMap.put(invateName, lineAmount);
				
			}
		}
		
		payMap.put(tax, new BigDecimal(taxValue));
		return payMap;
		
	}
	
	/**
	 * 从统一数据交换平台中获取返回结果
	 * @param resultData
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	@SuppressWarnings("rawtypes")
        private String getResult(String resultData) throws UnsupportedEncodingException, DocumentException{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new ByteArrayInputStream(resultData.getBytes("UTF-8")));
		Element rootResp = document.getRootElement();
		Element resultP = null;
		String sResult = "";
		String message = "";
		if (rootResp != null) {
			for (Iterator i = rootResp.elementIterator(); i.hasNext();) {
				resultP = (Element) i.next();
				if (resultP.getName().equals("result")) {
					sResult = resultP.getText();
				}
				if(resultP.getName().equals("msg")){
					message = resultP.getText();
				}
			}
		}
		if(!"1".equals(sResult)){
			//分析是否为发票号重复
			if(message.indexOf("ORA-00001") != -1){
				sResult = "-1";
			}
		}
		return sResult;
	}
}
