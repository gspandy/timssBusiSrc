package com.timss.operation.util;


/**
 * @title: 返回码对应的错误提示文字
 * @description: {desc}
 * @company: gdyd
 * @className: returnCodeUtil.java
 * @author: huanglw
 * @createDate: 2014年7月16日
 * @version: 1.0
 */
public class ReturnCodeUtil {

    public static final String NOT_ENOUGH_SHIFT = "非休息类型班次不足2个";
    public static final String LACK_OF_CLN_CALENDARVO = "缺少排班日历数据";
    public static final String SUCCESS = "success";
    public static final String NO_MATCHED_SHIFT = "没有对应日期时间的班次";
    public static final String PERSON_NOT_EXIST = "输入的工号不存在";
    public static final String FAIl = "fail";
    public static final String PERSON_NOT_ONDUTY = "该工号对应员工今天不值班";
    public static final String LOGIN_USER_NOT_ONDUTY = "当前登录的用户今天不值班";
    public static final String HANDOVERPERSON_NOT_RIGHT = "交班人或接班人账号或密码错误";
    public static final String COMMIT_HANDOVER_FAIL = "更新或插入交接班记录失败，请联系维护人员";
    public static final String LOGINUSER_NOT_EXIST = "当前登录人没有岗位等基本信息";
    public static final String INIT_HANDOVER_FAIL = "初始化当班交接班记录失败";
    public static final String UPDATE_HANDOVER_FAIL = "更新当前交接班记录失败";
    public static final String INSERT_HANDOVER_FAIL = "插入新的交接班记录失败";
    public static final String CURRENTPERSON_NOT_EXIST = "该部门没有此交班人账号";
    public static final String NEXTPERSON_NOT_EXIST = "该部门没有此接班人账号";
    public static final String STATIONID_NOT_EXIST = "岗位ID不存在，请联系维护人员";
    public static final String JOBSID_NOT_EXIST = "工种ID不存在，请联系维护人员";
    public static final String CURRENT_PWD_ERROE = "交班人密码错误";
    public static final String NEXT_PWD_ERROE = "接班人密码错误";
    
}
