package com.timss.itsm.vo;

import java.io.Serializable;

import com.timss.itsm.bean.ItsmQuestionRd;

public class ItsmQuestionRdVo extends ItsmQuestionRd implements Serializable {

	private static final long serialVersionUID = -5934855154229190292L;
	
	private String attach;

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	
}
