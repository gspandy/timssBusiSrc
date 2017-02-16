package com.timss.finance.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timss.facade.util.AttachUtil;
import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.dao.FinanceManagementApplyDao;
import com.timss.finance.service.FMDetailListService;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.vo.FinanceMainDetailVo;
import com.timss.finance.vo.FinanceManagementApplyDtlVo;
import com.timss.finance.vo.FinanceManagementApplyVo;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.support.IEnumerationManager;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;

@Service
public class FinanceManagementApplyServiceImpl implements FinanceManagementApplyService {
    @Autowired
    FinanceManagementApplyDao financeManagementApplyDao;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    FMDetailListService fmDetailListService;
    @Autowired 
    private IEnumerationManager iEnumerationManager;
    
    Logger logger = Logger.getLogger( FinanceManagementApplyServiceImpl.class );

    @Override
    @Transactional
    public int insertFinanceManagementApply(FinanceManagementApply financeManagementApply) {
        logger.info( "开始插入费用申请信息" );
        UserInfoScope infoScope = itcMvcService.getUserInfoScopeDatas();
        // 更新创建信息
        InitUserAndSiteIdNewUtil.initCreate( financeManagementApply, itcMvcService );
        financeManagementApply.setCurrHandUser( infoScope.getUserId() );
        financeManagementApply.setCurrHandUserName( infoScope.getUserName() );
       
        financeManagementApplyDao.insertFinanceManagementApply( financeManagementApply );
        // 附件信息持久化
        AttachUtil.bindAttach( attachmentMapper, "", financeManagementApply.getAttach() );
        logger.info( "完成插入费用申请信息" );
        return 0;
    }

    private void initApplyTypeValue(FinanceManagementApply financeManagementApply, ItcMvcService itcMvcService2) {
        UserInfoScope infoScope=itcMvcService.getUserInfoScopeDatas();
        String siteid = infoScope.getSiteId();
        String applyType = financeManagementApply.getApplyType();
        //创建待办
       List<AppEnum> enumList = iEnumerationManager.retriveEnumerationsByCat( "FIN_APPLY_TYPE" );
       for ( AppEnum appEnum : enumList ) {
           if(appEnum.getCode().equals( applyType ) && siteid.equals( appEnum.getSiteId() )){
               financeManagementApply.setApplyType( appEnum.getLabel() ) ;
           }
       }
    }

    @Override
    @Transactional
    public int updateFinanceManagementApply(FinanceManagementApply financeManagementApply) {
        logger.info( "开始更新费用申请" );
        String id = financeManagementApply.getId();
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyDao
                .queryFinanceManagementApplyById( id );
        // 更新附件信息，先删除旧的附件信息，后更新信息的附件信息
        AttachUtil.bindAttach( attachmentMapper, financeManagementApplyDtlVo.getAttach(),
                financeManagementApply.getAttach() );
        //更新业务表中的附件字段的信息（下面的代码是为了防止清空附件后，updateFinanceManagementApply无法清空业务表中附件信息）
        if(financeManagementApply.getAttach() != null){
            financeManagementApplyDao.updateFinManagementApplyAttach(id,financeManagementApply.getAttach());
        }
        
        // 更新管理费用申请信息
        InitUserAndSiteIdNewUtil.initUpdate( financeManagementApply, itcMvcService );
        financeManagementApplyDao.updateFinanceManagementApply( financeManagementApply );

       
        logger.info( "完成更新费用申请" );
        return 0;
    }

    @Override
    @Transactional
    public int deleteFinanceManagementApply(String id) {
        logger.info( "开始删除管理费用申请记录，id为" + id );
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyDao
                .queryFinanceManagementApplyById( id );
        // 删除旧的附件信息
        AttachUtil.bindAttach( attachmentMapper, financeManagementApplyDtlVo.getAttach(), null );
        financeManagementApplyDao.deleteFinanceManagementApplyById( id );

        logger.info( "完成删除管理费用申请记录，id为" + id );
        return 0;
    }

    @Override
    public FinanceManagementApplyDtlVo queryFinanceManagementApplyById(String id) {
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyDao
                .queryFinanceManagementApplyById( id );
        List<FinanceMainDetailVo> financeMainDetailVos = fmDetailListService.queryFinanceMainDetailVosByParentId( id );
        financeManagementApplyDtlVo.setFinanceMainDetailVos( financeMainDetailVos );
        financeManagementApplyDtlVo.setAttachMap( AttachUtil.generatAttach( financeManagementApplyDtlVo.getAttach() ) );
        return financeManagementApplyDtlVo;
    }

    @Override
    public Page<FinanceManagementApplyVo> queryFinanceManagementApplyLsit(Page<FinanceManagementApplyVo> page) {
        page.setFuzzyParameter( "siteid", itcMvcService.getUserInfoScopeDatas().getSiteId() );
        List<FinanceManagementApplyVo> financeManagementApplyVos = financeManagementApplyDao
                .queryFinanceManagementList( page );
        page.setResults( financeManagementApplyVos );
        return page;
    }

    @Override
    public Page<FinanceManagementApplyVo> queryFuzzyFinanceManagementList(Page<FinanceManagementApplyVo> page) {
        page.setFuzzyParameter( "siteid", itcMvcService.getUserInfoScopeDatas().getSiteId() );
        List<FinanceManagementApplyVo> financeManagementApplyVos = financeManagementApplyDao
                .queryFuzzyFinanceManagementList( page );
        page.setResults( financeManagementApplyVos );
        return page;
    }
    
    @Override
    public int updateFinanceManagementApplyBasic(FinanceManagementApply financeManagementApply) {
        financeManagementApplyDao.updateFinanceManagementApply( financeManagementApply );
        logger.info( "完成更新管理费用申请记录，bean为" + financeManagementApply );
        return 0;
    }

    @Override
    public List<Map<String, Object>> queryApplyInfoFuzzyByName(String name, String type) {
      //初始化参数userInfo
        UserInfo userInfo=itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> result=new ArrayList<Map<String,Object>>(0);
        if("requestnote".equals(type)){
                //申请单的查询
                FinanceManagementApply fma = new FinanceManagementApply();
                fma.setName(name);
                Page<FinanceManagementApplyVo> fmas=new Page<FinanceManagementApplyVo>(1,11);
                try {
                        fmas.setFuzzyParameter("name", name);
                        //审批通过的申请单才显示,因为申请和报销连起来了，所以不是结束了的申请单才能报销
                       // fmas.setFuzzyParameter("status", "AE");
                        //根据申请单名称，模糊查询申请单信息
                        fmas= queryFuzzyFinanceManagementList(fmas);
                } catch (Exception e) {
                        logger.error("根据名称："+name+"查询申请单信息时，出错",e);
                }//将Page数据转换为[{id:""},{name:""}]格式的数据
                List<FinanceManagementApplyVo> originList=fmas.getResults();
                if(fmas!=null){
                        for(int i=0;i<originList.size();i++){
                                FinanceManagementApplyVo fmaTmp=originList.get(i);
                                Map<String,Object> map=new HashMap<String,Object>();
                                map.put("id", fmaTmp.getId());
                                map.put("name", null==fmaTmp.getName()?"-":fmaTmp.getName());
                                map.put("budget", fmaTmp.getBudget());
                                map.put("subject", fmaTmp.getSubject());
                                result.add(map);
                        }
                }
        }
        return result;
    }

    @Override
    public FinanceManagementApplyDtlVo queryFinanceManagementApplyByProcessId(String processId) {
        
        FinanceManagementApplyDtlVo financeManagementApplyDtlVo = financeManagementApplyDao
                .queryFinanceManagementApplyByProcessId( processId );
        
        return financeManagementApplyDtlVo;
    }

    @Override
    public List<FinanceManagementApply> queryTravelApplyByDiffDay(String siteid, String userId, Date beginDate,
            Date endDate) {
        List<FinanceManagementApply> financeManagementApplys = financeManagementApplyDao
                .queryFinanceManagementListByDiffDay( siteid, userId, "travelapply", beginDate, endDate);
        return financeManagementApplys;
    }
    
    

}
