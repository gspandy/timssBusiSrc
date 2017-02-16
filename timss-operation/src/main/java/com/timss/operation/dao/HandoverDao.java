package com.timss.operation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.timss.operation.bean.Handover;
import com.timss.operation.bean.PersonJobs;
import com.timss.operation.vo.CalendarVo;
import com.timss.operation.vo.HandoverVo;
import com.timss.operation.vo.NoteBaseVo;
import com.yudean.itc.annotation.DynamicFormBind;
import com.yudean.itc.dto.Page;

/**
 * 
 * @title: HandoverMapper 
 * @description: mybatis 接口
 * @company: gdyd
 * @className: HandoverMapper.java
 * @author: huanglw
 * @createDate: 2014年7月3日
 * @updateUser: huanglw
 * @version: 1.0
 */
public interface HandoverDao {
	
    /**
     * @description:插入一条交接班记录
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param handover  其中id为自增，不需要设置
     */
    public void insertHandover(Handover handover );
    
    /**
     * 
     * @description:更新交接班记录表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param HandoverVo:
     * @return int 更新个数
     */
    public int updateHandover(HandoverVo handoverVo);
    
    /**
     * 
     * @description:通过Id拿到交接班记录表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id
     * @return:Handover
     */
    public Handover queryHandoverById(int id);
    
    /**
     * 
     * @description:通过ID 删除 handover
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param id:
     * @return 
     */
    public int deleteHandoverById(int id);

    /**
     * 
     * @description:handover 分页
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return:
     */
    public List<Handover> queryHandoverByPage(Page<HashMap<?, ?>> page);
    
    /**
     * 
     * @description:高级搜索 查询交接班记录列表
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @param page
     * @return: List<Handover>
     */
    public List<Handover> queryHandoverBySearch(Page<HashMap<?, ?>> page );
    
    /**
     * 
     * @description:拿出所有交接班记录handover
     * @author: huanglw
     * @createDate: 2014年7月3日
     * @return:
     */
    public List<Handover> queryAllHandover();

    /**
     * 
     * @description:通过传入的日历信息（里面包含日期、值别）
     * @author: huanglw
     * @createDate: 2014年7月9日
     * @param lastCVo
     * @return:
     */
    public List<Handover> queryHandoverByCalendarVo(CalendarVo cVo);

    /**
     * 
     * @description:通过日期、stationId、shiftid获取交接班记录（可指定或者不指定工种）
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param map
     * @return:List<HandoverVo>
     */
    public List<HandoverVo> queryHandoverBySDS(Map<String, Object> map);

    /**
     * 
     * @description:验证交接班双方是否同在要交接班的岗位
     * @author: huanglw
     * @createDate: 2014年7月22日
     * @param handoverVo
     * @update yyn 20160218
     * @return:
     */
    public List<PersonJobs> validateHandoverUserJobsId(HandoverVo handoverVo);

    /**
     * 
     * @description:获取该工种下当前班和上一班记录
     * @author: huanglw
     * @createDate: 2014年7月23日
     * @param String jobsId
     * @return:List<HandoverVo>
     */
    public List<HandoverVo> queryNowAndLastHandoverVo(String JobsId);

    /**
     * 
     * @description:通过值别、岗位、日期获取交接班记录（可指定或不指定工种）
     * @author: huanglw
     * @createDate: 2014年7月29日
     * @param map
     * @return:
     */
    public List<HandoverVo> queryHandoverByDSD(Map<String, Object> map);
    
    /**
     * 更新交接班的运行记事的基本信息
     * @param vo
     * @return
     */
    @DynamicFormBind(masterKey="keyword")
    Integer updateHandoverNote(NoteBaseVo vo);
    
    /**
     * 添加值班人员
     * @author 890147 2016年11月29日
     * @param handoverId
     * @param userIds
     * @return
     */
    Integer insertHandoverPerson(@Param("handoverId")Integer handoverId,@Param("userIds")String[]userIds);
    
    /**
     * 删除值班人员
     * @author 890147 2016年11月29日
     * @param handoverId
     * @param userIds
     * @return
     */
    Integer deleteHandoverPerson(@Param("handoverId")Integer handoverId,@Param("userIds")String[]userIds);
    
    /**
     * 查询交接班的值班人员
     * @author 890147 2016年11月29日
     * @param handoverId
     * @return
     */
    List<PersonJobs>queryHandoverPersonByHandoverId(@Param("handoverId")Integer handoverId);
}
