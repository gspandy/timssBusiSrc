package com.timss.ptw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface PtwUtilDao {
   
    List<String> queryEnumValueByEcatcode(@Param("ecatcode")String ecatcode , @Param("siteid")String seachSiteid);
    
    
}
