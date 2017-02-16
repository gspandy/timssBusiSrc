package com.timss.inventory.exception;

/**
 * @title: 出库时可出库数量不足异常
 * @description: 某个物资出库时会消耗入库批次的可用库存，如果某个物资出库时消耗掉所有入库批次的可出库数量还是不够，则提示该错误
 * @company: gdyd
 * @className: InsufficientException.java
 * @author: 890151
 * @createDate: 2016-7-1
 * @updateUser: 890151
 * @version: 1.0
 */
public class InsufficientException extends RuntimeException {


}