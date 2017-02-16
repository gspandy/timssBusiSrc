

function addNewPlanTab(){
	openTab(basePath+ "pms/plan/addPlanJsp.do","年度计划","pmsAddPlanTab",canOpenMutiTab);

}

function openEditPlanTab(id){
	openTab(basePath+ "pms/plan/editPlanJsp.do?id="+id,"年度计划","pmsViewPlanTab"+id);
}

function _afterCloseCallBack(){
	dataGrid.datagrid('reload');
}