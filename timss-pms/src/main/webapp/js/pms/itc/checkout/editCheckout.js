function openViewProject(){
	var projectId=$("#form1").iForm('getVal')["projectId"];
	openTab('pms/project/editProjectJsp.do?id='+projectId,'立项','pmsViewProjectTab'+projectId);
}