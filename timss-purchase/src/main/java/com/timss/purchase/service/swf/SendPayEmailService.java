package com.timss.purchase.service.swf;

public interface SendPayEmailService {

	public void sendPayEmail(String payId) throws RuntimeException, Exception;
}
