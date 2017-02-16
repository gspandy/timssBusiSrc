package com.timss.pms.listener.fordelete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.pms.service.ProjectService;

@Service
public class ProjectDraftDelete implements IDraftDelete{
    @Autowired
    ProjectService projectService;
	@Override
	public void deleteDraft(String id) {
		projectService.deleteProject(id);
	}

}
