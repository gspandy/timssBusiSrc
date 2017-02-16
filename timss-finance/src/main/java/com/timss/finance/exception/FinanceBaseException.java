package com.timss.finance.exception;

public class FinanceBaseException extends RuntimeException{
	public FinanceBaseException() {
		super();
	}
	public FinanceBaseException(String message,Throwable throwable){
		super(message,throwable);
	}
	public FinanceBaseException(Throwable throwable){
		super(throwable);
	}
	public FinanceBaseException(String message){
		super(message);
	}
}
