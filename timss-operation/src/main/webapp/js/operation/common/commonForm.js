
//获取表单数据Json格式  id 为表单Form的ID 
function getFormData(id){
	var formResult = $("#" + id).iForm("getVal");
	var s = JSON.stringify( formResult );
	return s;
}

function substr(str,length,suffix){
	suffix=suffix||"...";
	if(str){
		var tmp=str.substr(0,length);
		if(tmp<str){
			str=tmp+suffix;
		}
	}else{
		str="";
	}
	return str;
}