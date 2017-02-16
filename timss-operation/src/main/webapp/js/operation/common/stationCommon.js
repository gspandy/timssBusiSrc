var isStationOptionOnchange=true;//避免onchange事件循环触发

//填充岗位 basePath; select/combobox id/callBack func when success
function stationOption(basePath, id, successCallBack,params,onchangeCallBack,onchangeParams){
	
	var url = basePath + "/operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var arr = [];
			var stationList = data.result;
			for( var index in stationList ){
				if( index == 0 ){
					arr.push([stationList[index].roleId,stationList[index].name,true]);
				}else{
					arr.push([stationList[index].roleId,stationList[index].name]);
				}
			}
			if(id){
				//$("#" + id ).iCombo("loadData",arr);
				$("#" + id).iCombo("init", {
					data : arr,
					"onChange" : function(val) {
						if(isStationOptionOnchange){
							var isExist=false;
							for(var i=0;i<arr.length;i++){
								if(arr[i][0]==val){
									isExist=true;
									break;
								}
							}
							if(!isExist){//选项不存在
								isStationOptionOnchange=false;
								$("#" + id).iCombo("setVal",null);
								//$("#" + id).iCombo("setTxt","该工种已被删除或不可用");
								$("#readonly_" + id).html("该工种已被删除或不可用");
							}
							if(onchangeCallBack){
								if(onchangeParams){
									onchangeCallBack(onchangeParams,val);
								}else{
									onchangeCallBack(val);
								}
							}
						}else{
							isStationOptionOnchange=true;
						}
					}
				});
			}
			if(successCallBack){
				if(params){
					successCallBack(params,arr);
				}else{
					successCallBack(arr);
				}
			}
		}
	});
}