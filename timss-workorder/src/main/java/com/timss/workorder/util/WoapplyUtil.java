package com.timss.workorder.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.timss.workorder.bean.WoapplyRisk;
import com.timss.workorder.bean.WoapplySafeInform;
import com.timss.workorder.bean.WoapplyWorker;
import com.yudean.itc.util.json.JsonHelper;


public class WoapplyUtil {
    
    public static void setWoapplyIdInList(List<WoapplySafeInform> woapplySafeInformList,
            List<WoapplyWorker> woapplyWorkerList, List<WoapplyRisk> riskAssessmentList, String woapplyId) {
        for ( WoapplySafeInform woapplySafeInform : woapplySafeInformList ) {
            woapplySafeInform.setWorkapplyId( woapplyId );
        }
        for ( WoapplyWorker woapplyWorker : woapplyWorkerList ) {
            woapplyWorker.setWorkapplyId( woapplyId );
        }
        for ( WoapplyRisk woapplyRisk : riskAssessmentList ) {
            woapplyRisk.setWorkapplyId( woapplyId );
        }
    }
    
    public static ArrayList<WoapplyWorker> convertToWoapplyWorkerList(String workerData) {
        ArrayList<WoapplyWorker> woapplyWorkerList = new ArrayList<WoapplyWorker>();
        JSONObject workerJsonObj = JSONObject.fromObject(workerData);
          int workerDatagridNum =Integer.valueOf(workerJsonObj.get("total").toString());  //记录数
        //插入外来施工人员
          JSONArray workerJsonArray = workerJsonObj.getJSONArray("rows"); //记录数组
          for(int i=0; i<workerDatagridNum; i++){
                  String workerRecord = workerJsonArray.get(i).toString();  //某条记录的字符串表示
                  WoapplyWorker woapplyWorker = JsonHelper.fromJsonStringToBean(workerRecord, WoapplyWorker.class);
                  woapplyWorkerList.add( woapplyWorker );
          }   
        return woapplyWorkerList;
    }

    public static  List<WoapplySafeInform> converToWoapplySafeInform(String safeinform) {
        //[{"content":"电饭锅","showOrder":1},{"content":"反攻倒算","showOrder":2},{"content":"法国和","showOrder":3}]
        List<WoapplySafeInform> itemList = JsonHelper.toList( safeinform, WoapplySafeInform.class );
        return itemList;
    }
    
    public static ArrayList<WoapplyRisk> convertToRiskAssessmentList(String riskAssessmentData) {
        ArrayList<WoapplyRisk> riskAssessmentList =  new ArrayList<WoapplyRisk>();
          JSONObject riskAssessmentJsonObj = JSONObject.fromObject(riskAssessmentData);
          int riskAssessmentDatagridNum =Integer.valueOf(riskAssessmentJsonObj.get("total").toString());  //记录数
        //插入风险评估
          JSONArray riskAssessmentJsonArray = riskAssessmentJsonObj.getJSONArray("rows"); //记录数组
          for(int i=0; i<riskAssessmentDatagridNum; i++){
                  String riskAssessmentRecord = riskAssessmentJsonArray.get(i).toString();  //某条记录的字符串表示
                  WoapplyRisk woapplyRisk = JsonHelper.fromJsonStringToBean(riskAssessmentRecord, WoapplyRisk.class);
                  riskAssessmentList.add( woapplyRisk );
          }   
        return riskAssessmentList;
    }
  
}
