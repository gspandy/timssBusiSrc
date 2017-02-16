package com.timss.operation.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.operation.bean.Handover;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.HandoverVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;


/**
 * 
 * @title: 交接班记录Service
 * @description: 
 * @company: gdyd
 * @className: HandoverService.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface HandoverService {
    
    /**
     * @description:提交交接班数据
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param formData JSON String
     * @return:Map<String, Object>
     * @throws Exception 
     */
    Map<String, Object> commitHandover(HandoverVo handoverVo) throws Exception;
    
    /**
     * 
     * @description:根据上一班的交接班记录，插入下一班的交接班记录
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param lasthandoverVo 上一班的交接班记录(交接班页面传入的数据)
     * @return:Handover 返回更新了id的 handover
     * @throws Exception 
     */
    Handover insertHandoverByLastHandoverVo(HandoverVo lasthandoverVo) throws Exception;
    
    /**
     * 
     * @description:插入一条交接班记录(参数是bean)
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param Handover
     * @return:Handover 返回更新了id的 handover
     */
    Handover insertHandover(Handover handover);

    /**
     * 
     * @description:通过高级搜索查询交接班记录
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param map
     * @param pageVo
     * @return:
     */
    List<Handover> queryHandoverBySearch(HashMap<String, Object> map, Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:交接班记录分页
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param pageVo
     * @return:
     */
    List<Handover> queryHandoverByPage(Page<HashMap<?, ?>> pageVo);

    /**
     * 
     * @description:通过ID查询交接班记录
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    Handover queryHandoverById(int id);
    
    /**
     * 
     * @description:更新交接班记录
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param handover
     * @return:
     */
    int updateHandover(HandoverVo handoverVo);

    /**
     * 
     * @description:删除交接班记录
     * @author: huanglw
     * @createDate: 2014年7月4日
     * @param id
     * @return:
     */
    int deleteHandoverById(int id);

    /**
     * 
     * @description:获取上一班的交接班Handover
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dateTime 日期时间 
     * @param stationId 岗位id
     * @return:List<Handover>
     *//*
    List<Handover> queryLastHandover(Date dateTime, String stationId);*/

    /**
     * @description:通过传入的时间和岗位id获取CLN ： current Last Next shift
     * @author: huanglw
     * @createDate: 2014年7月16日
     * @param dateTime 日期时间 
     * @param stationId 岗位id     
     * @return:Map<String, Object> returnCode == success时表示成功
     *//*
    Map<String, Object> queryCLNShiftByDateTime(Date dateTime,String stationId);*/

   /**
    * 
    * @description:通过岗位、日期、班次获取交接班记录，jobsId可选
    * @author: huanglw
    * @createDate: 2014年7月29日
    * @param stationId
    * @param dateYMD
    * @param shiftId
    * @param jobsId（若不指定，可以为null）
    * @return:
    */
    List<HandoverVo> queryHandoverBySDS(String stationId,Date dateYMD,String shiftId,String jobsId);

    /**
     * @description:
     * 获取当班基本信息和上一班基本信息，以map形式返回
     * 其中的rerutnMap的key至少有nowHandoverVo lastHandoverVo returnCode
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param jobsId
     * @return:
     */
    Map<String, Object> queryNowAndLastHandoverVo(String jobsId);

    /**
     * 
     * @description:通过当前时间和岗位id初始化一条未交接的记录
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param stationId
     * @return:新创建的HandoverVo
     */
    HandoverVo initHandover(String stationId,int jobsId);
    
    /**
     * 
     * @description:插入一条交接班记录
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param handoverVo
     * @return:HandoverVo（插入成功时id会自增）
     */
    HandoverVo insertHandover(HandoverVo handoverVo);

    /**
     * @description:判断指定日期、岗位、值别（可选）的日历是否对应有交接班记录
     * @author: huanglw
     * @createDate: 2014年7月9日
     * @param dateYMD String
     * @param stationId String
     * @param dutyId String
     * @return:boolean
     */
    boolean isExistHandoverByDSD(String dateYMD, String stationId, String dutyId);

    /**
     * 
     * @description:通过日期、岗位id，值别获取交接班记录
     * @author: huanglw
     * @createDate: 2014年7月29日
     * @param dateYMD（date类型）
     * @param stationId
     * @param dutyId
     * @return:List<HandoverVo>
     */
    List<HandoverVo> queryHandoverByDSD(Date dateYMD, String stationId, String dutyId);
    
    /**
     * 更新交接班的运行记事的基本信息
     * @param vo
     * @return
     */
    Integer updateHandoverNote(NoteBaseVo vo);
    
    /**
     * 查询交接班的值班人员
     * @author 890147 2016年11月29日
     * @param handoverId
     * @return
     * @throws Exception
     */
    List<PersonJobs>queryHandoverPersonByHandoverId(Integer handoverId)throws Exception;
    
    /**
     * 更新交接班的值班人员
     * @author 890147 2016年11月29日
     * @param handoverId
     * @param userIdList
     * @return
     * @throws Exception
     */
    Integer updateHandoverPerson(Integer handoverId,List<String>userIdList,@Operator SecureUser operator)throws Exception;
}
