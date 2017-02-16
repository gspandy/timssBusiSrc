package com.timss.ptw.service.core;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sql.DATE;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.timss.ptw.bean.PtwInfo;
import com.timss.ptw.dao.PtwInfoDao;
import com.timss.ptw.service.PtwChangeWpicService;
import com.timss.ptw.service.PtwInfoNoService;

@Service
public class PtwInfoNoServiceImpl implements PtwInfoNoService {
    private static final Logger log = Logger.getLogger(PtwChangeWpicService.class);
    @Autowired
    PtwInfoDao ptwInfoDao;
    
    @Override
    public String genPtwNo(PtwInfo ptwInfo, String ptwTypeCode) {
    	DateFormat df = new SimpleDateFormat("yyyy");
        String middle =  ptwInfo.getIsStdWt() == 0 ? "TMP" : "STD";
        int maxNum = ptwInfoDao.queryMaxPtwNumOfYear( ptwInfo );
        maxNum++;
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        
        String result = ptwInfo.getSiteId() +"-"+ middle+"-" + df.format( ptwInfo.getCreateDate() ) + decimalFormat.format( maxNum );
        return result;
    }
    
    @Override
    public String genFixedPtwNo(PtwInfo ptwInfo, String ptwTypeCode) {
        DateFormat df = new SimpleDateFormat("yyyyMM");
        String middle = df.format( new Date() );
        //查找签发时间为当前日期的工作票编号数目
        int createdNumber = ptwInfoDao.queryCreatedNumber( ptwInfo );
        createdNumber++;
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        String ptwNo = ptwInfo.getSiteId()+ "-"+ptwTypeCode+"-"+ middle + decimalFormat.format( createdNumber );
        log.debug("生成工作票的编号"+ptwNo);
        return ptwNo;
    }
    
    @Override
    public int updatePtwNoAfterIssue(PtwInfo ptwInfo,String ptwTypeCode) {
        DateFormat df = new SimpleDateFormat("yyyyMM");
        String middle = df.format( ptwInfo.getIssuedTime() );
        //查找签发时间为当前日期的工作票编号数目
        int issuedNumber = ptwInfoDao.queryIssuedNumber( ptwInfo );
        issuedNumber++;
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        String ptwNo = ptwInfo.getSiteId()+ "-"+ptwTypeCode+"-"+ middle + decimalFormat.format( issuedNumber );
        ptwInfo.setWtNo( ptwNo );
        log.debug("更新工作票的编号"+ptwNo);
        return ptwInfoDao.updatePtwNo( ptwInfo );
    }

  
    
    

}
