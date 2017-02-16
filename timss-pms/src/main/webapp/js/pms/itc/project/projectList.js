

function addNewPlanTab() {
	openTab(basePath + "pms/project/addProjectJsp.do","立项","pmsNewProjectTab",canOpenMutiTab);
	
}

function openEditProjectTab(id,tabid) {
	openTab( basePath + "pms/project/editProjectJsp.do?id=" + id,"立项",tabid || "pmsViewProjectTab"+id);
}

function _afterCloseCallBack(){
	dataGrid.datagrid('reload');
}