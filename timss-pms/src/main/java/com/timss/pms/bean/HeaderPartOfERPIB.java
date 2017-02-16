package com.timss.pms.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HeaderPartOfERPIB {
	private String fCertHeadDesc;//凭证头描述
	private String fCertType;//凭证类型
	private String fSubType;//凭证子类
	private String fAccMonth;//凭证期间，月份
	private String fAccDate;//记账日期
	private String fCcy;//币种
	private String fDocNbr;//单据张数 
	
	public HeaderPartOfERPIB(){
		//生成凭证日期
		Date date=new Date();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
		fAccMonth=simpleDateFormat.format(date);
		simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		fAccDate=simpleDateFormat.format(date);
		//默认币种为CNY
		fCcy="CNY";
		//默认凭证类型为记帐凭证
		fCertType="记帐凭证";
	}
	
	public String getfCertHeadDesc() {
		return fCertHeadDesc;
	}
	public void setfCertHeadDesc(String fCertHeadDesc) {
		this.fCertHeadDesc = fCertHeadDesc;
	}
	public String getfCertType() {
		return fCertType;
	}
	public void setfCertType(String fCertType) {
		this.fCertType = fCertType;
	}
	public String getfSubType() {
		return fSubType;
	}
	public void setfSubType(String fSubType) {
		this.fSubType = fSubType;
	}
	public String getfAccMonth() {
		return fAccMonth;
	}
	public void setfAccMonth(String fAccMonth) {
		this.fAccMonth = fAccMonth;
	}
	public String getfAccDate() {
		return fAccDate;
	}
	public void setfAccDate(String fAccDate) {
		this.fAccDate = fAccDate;
	}
	public String getfCcy() {
		return fCcy;
	}
	public void setfCcy(String fCcy) {
		this.fCcy = fCcy;
	}
	public String getfDocNbr() {
		return fDocNbr;
	}
	public void setfDocNbr(String fDocNbr) {
		this.fDocNbr = fDocNbr;
	}

	@Override
	public String toString() {
		return "HeaderPartOfERPIB [fCertHeadDesc=" + fCertHeadDesc
				+ ", fCertType=" + fCertType + ", fSubType=" + fSubType
				+ ", fAccMonth=" + fAccMonth + ", fAccDate=" + fAccDate
				+ ", fCcy=" + fCcy + ", fDocNbr=" + fDocNbr + "]";
	}
	
	
}
