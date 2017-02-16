
//获取表单数据Json格式  id 为表单Form的ID 
function getFormData(id){
	var formResult = $("#" + id).iForm("getVal");
	var s = JSON.stringify( formResult );
	return s;
}