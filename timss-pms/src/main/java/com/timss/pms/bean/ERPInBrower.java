package com.timss.pms.bean;

import java.util.List;

/**
 * 前端erp凭证展示数据类
 * @ClassName:     ERPInBrower
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-8-24 下午3:38:51
 */
public class ERPInBrower {
	private HeaderPartOfERPIB header; //凭证基本信息部分
	private List<BodyPartOfERPIB> bodys;   //凭证会记分录
	public HeaderPartOfERPIB getHeader() {
		return header;
	}
	public void setHeader(HeaderPartOfERPIB header) {
		this.header = header;
	}
	public List<BodyPartOfERPIB> getBodys() {
		return bodys;
	}
	public void setBodys(List<BodyPartOfERPIB> bodys) {
		this.bodys = bodys;
	}
	
	
}
