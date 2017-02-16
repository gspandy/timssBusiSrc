package com.timss.ptw.util;

public class PtwConstants {
	public static enum KeyBoxStatus {
		//使用中,
		using,
		//已确认
		confirm,
		//已安全,
		safe,
		//可用
		useable
	};
	public static void main(String[] args) {
		System.out.println("safe".equals(KeyBoxStatus.safe.toString()));
	}
}
