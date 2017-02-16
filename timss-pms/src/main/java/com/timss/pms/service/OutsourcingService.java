package com.timss.pms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timss.pms.bean.Outsourcing;
import com.timss.pms.bean.Project;
import com.timss.pms.vo.OutsourcingVo;

@Service
public interface OutsourcingService {
	int insertOutsourcing(Outsourcing outsourcing);
	
	int updateOutsourcingList(List<Outsourcing> outsourcings,Project project);
	
	int updateOutsourcing(Outsourcing outsourcing);
	
	int deleteOutsourcingByProjectId(String projectId);
	
	List<OutsourcingVo> queryOutsoucingListByProjectId(String projectId);
}
