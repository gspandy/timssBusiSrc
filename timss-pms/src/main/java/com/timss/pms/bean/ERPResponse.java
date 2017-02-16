package com.timss.pms.bean;

import com.timss.pms.util.XMLPaerserUtil;

/**
 * erp返回的执行结果类，既包括用xml表示内容，也包括解析后的内容
 * @ClassName:     ERPResponse
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-25 上午9:48:19
 */
public class ERPResponse {
	/**
	 * 为定义的服务的唯一标示,与传入请求时的值一致
	 */
	private String serviceID; 
	/**
	 * 安全令牌，客户端可以验证该返回值的合法性
	 */
	private String token;  
	/**
	 * 如果分多次传送的，每次调用必须使用同一事务ID
	 */
	private String transactionID;
	/**
	 * 表示此次查询结果是否成功,1标示成功，否则为失败
	 */
	private String result;
	/**
	 * 返回的附加信息，如果result=0,则会附加错误原因
	 */
	private String msg;
	/**
	 * 本次接收的记录个数
	 */
	private String received;
	/**
	 * 如果事务提交了，会返回成功提交的总记录数
	 */
	private String submitted;
	
	private String content;//xml表示的erp返回结果
	
	public ERPResponse(){}
	
	public ERPResponse(String content){
		this.content=content;
	}
	
	/**
	 * 解析XML表示的结果
	 * @Title: parse
	 */
	public static ERPResponse parseResult(String xml){
		ERPResponse erpResponse=XMLPaerserUtil.parseXMLStringToObject(xml);
		erpResponse.setContent(xml);
        return erpResponse;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getSubmitted() {
		return submitted;
	}

	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
