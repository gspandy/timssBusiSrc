package com.timss.inventory.service.swf;

public interface SendReceiveAllEmailService {

	public void sendEmail(String purOrderNo,String purOrderName) throws RuntimeException, Exception;
}
