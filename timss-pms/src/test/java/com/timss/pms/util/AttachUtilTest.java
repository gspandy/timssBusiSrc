package com.timss.pms.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.timss.pms.dao.ProjectDao;
import com.timss.pms.vo.ProjectVo;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.interfaces.eip.mobile.RetAttachmentBean;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml",
"classpath:config/context/applicationContext-webserviceClient-config.xml"})
public class AttachUtilTest extends TestUnit{
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired 
    ProjectDao projectDao;
    @Autowired
    ItcMvcService itcMvcService;
	@Test
	public void test() {
		Page<ProjectVo> page=new Page<ProjectVo>(1,400);
		page.setFuzzyParameter("status", ChangeStatusUtil.approvalCode);
		page.setFuzzyParameter("siteid", itcMvcService.getUserInfoScopeDatas().getSiteId());
		String kw="";
		if(kw!=null && !"".equals(kw) ){
			page.setFuzzyParameter("project_name", kw);
		}
		List<ProjectVo> projects=projectDao.queryProjectList(page);
		
		List<Map> maps=new ArrayList<Map>();
		if(projects!=null){
			for(int i=0;i<projects.size();i++){
				HashMap map=new HashMap();
				map.put("id", projects.get(i).getId());
				map.put("name", projects.get(i).getProjectName());
				maps.add(map);
			}
		}
		System.out.println("--------------"+projects.size());
	}

}
