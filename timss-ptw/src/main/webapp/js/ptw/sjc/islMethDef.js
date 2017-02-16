/* 表单字段定义  */
var fields = [
				{title : "ID", id : "id",type : "hidden"},
				{title : "编号", id:"no",
					wrapXsWidth:4,
			        wrapMdWidth:3,
					rules : {required:true,remote:{
					url: basePath + "ptw/ptwIslMethDef/checkIslMethNo.do",
					type: "post",
					data: {
						"islMethDefData": function() {
							var temp = { 
											id: $("#f_id").val(),
											no:$("#f_no").val()
										};
					        return FW.stringify(temp);
					    }
					}
				}},messages:{remote:"编号已存在，请输入唯一编号"}},
				{
			        title : "方法名", 
			        id : "method",
			        type : "textarea",
			        linebreak:true,
			        rules : {required:true,maxChLength:160},
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:55
			    }
			];

function initIslMethDefPage(id){
	$("#islMethDefForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	
	if(id != 0){
		$.post(basePath + "ptw/ptwIslMethDef/queryPtwIslMethDefById.do",{"id":id},
				function(islMethodDefineData){
					if(islMethodDefineData.result == "success"){
						$("#btn_islMethDef_save").hide(); 
						var islMethodDefineFromData = eval("(" +islMethodDefineData.data+ ")");
						$("#islMethDefForm").iForm("setVal",islMethodDefineFromData);
						$("#islMethDefForm").iForm("endEdit");
						FW.fixRoundButtons("#toolbar");
					}
				},"json");
	}
	
}

/**
 * @returns 获取表单值
 */
function getFormData(){
	
	if(! $("#islMethDefForm").valid()){
		return null;
	}
	var formData = $("#islMethDefForm").iForm("getVal");
	return formData;
}

/**
 * 提交隔离方法
 */
function commitIslMethDef(){
	var formData = getFormData();
	
	$.post(basePath + "ptw/ptwIslMethDef/commitPtwIslMethDef.do",
	 		{"islMethDefFormDate":JSON.stringify(formData)},
			function(data){
				if(data.result == "success"){
					FW.success("保存成功");
					closeCurPage();
				}else {
					FW.error("保存失败");
				}
	  },"json");
	
}

/**
 * 编辑隔离方法
 */
function editIslMethDef(){
	$("#islMethDefForm").iForm("beginEdit"); 
	$("#btn_islMethDef_save").show(); 
	$("#btn_islMethDef_edit").hide(); 
	FW.fixRoundButtons("#toolbar");
}

/**
 * 禁用隔离方法
 */
function deleteIslMethDef(){
	Notice.confirm("确定删除|确定删除该隔离方法么？该操作无法撤销。",function(){
		$.post(basePath + "ptw/ptwIslMethDef/unavailableIslMethDef.do",
		 		{"id":id},
				function(data){
					if(data.result == "success"){
						FW.success("删除成功");
						closeCurPage();
					}else {
						FW.error("删除失败");
					}
		  },"json");
	},null,"info");	
	
}

