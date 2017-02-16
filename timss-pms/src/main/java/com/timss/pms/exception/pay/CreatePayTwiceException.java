package com.timss.pms.exception.pay;

import com.timss.pms.exception.PmsBasicException;

public class CreatePayTwiceException extends PmsBasicException{
	public CreatePayTwiceException(String str){
		super(str);
	}
}
