package com.timss.finance.vo;

import java.util.ArrayList;
import java.util.List;

import com.yudean.itc.dto.interfaces.eip.mobile.RetKeyValue;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: FinListEip.java
 * @author: 890170
 * @createDate: 2014-11-5
 * @updateUser: 890170
 * @version: 1.0
 */
public class FinListEip {
	private String title;
	private List<RetKeyValue> rows = new ArrayList<RetKeyValue>();

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the rows
	 */
	public List<RetKeyValue> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<RetKeyValue> rows) {
		this.rows = rows;
	}
}
