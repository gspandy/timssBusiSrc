/* 表单字段定义  */
var fields = [
				{title : "ID", id : "id",type : "hidden"},
				{title : "编号", id:"keyBoxNo",
					wrapXsWidth:4,
			        wrapMdWidth:4,
					rules : {required:true,remote:{
					url: basePath + "ptw/ptwKeyBox/checkKeyBoxNo.do",
					type: "post",
					data: {
						"keyBoxData": function() {
							var temp = { 
											id: $("#f_id").val(),
											keyBoxNo:$("#f_keyBoxNo").val()
										};
					        return FW.stringify(temp);
					    }
					}
				}},messages:{remote:"编号已存在，请输入唯一编号"}},
				
				{
			    	title : "类型",
			    	id : "useType",
			    	type : "combobox",
			    	rules : {required:true},
			    	options : {
			    		url :  ItcMvcService.getEnumPath() + "?data=PTW_KEYBOXTYPE",
			    		"onRemoteData" : function(val){
			    	          return FW.parseEnumData("PTW_KEYBOXTYPE",val);
			    	    },
			    	    remoteLoadOn : formRead?"set":"init"
			    	}
			    },
				{
			    	title : "状态",
			    	id : "curStatus",
			    	type : "combobox",
			    	options : {
			    		url :  ItcMvcService.getEnumPath() + "?data=PTW_KEYBOXSTATUS",
			    		"onRemoteData" : function(val){
			    	          return FW.parseEnumData("PTW_KEYBOXSTATUS",val);
			    	    },
			    	    remoteLoadOn : formRead?"set":"init"
			    	}
			    },
				{
			        title : "用途", 
			        id : "purpose",
			        type : "textarea",
			        linebreak:true,
			        rules : {required:true,maxChLength:160},
			        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:55
			    }
			];

function initKeyBoxPage(id){
	$("#keyBoxForm").iForm("init",{"fields" : fields,"options":{validate:true,initAsReadonly:formRead}});
	
	if(id != 0){
		$.post(basePath + "ptw/ptwKeyBox/queryPtwKeyBoxById.do",{"id":id},
				function(keyBoxData){
					if(keyBoxData.result == "success"){
						$("#btn_keyBox_save").hide(); 
						var keyBoxDataFromData = eval("(" +keyBoxData.data+ ")");
						$("#keyBoxForm").iForm("setVal",keyBoxDataFromData);
						$("#keyBoxForm").iForm("endEdit");
						FW.fixRoundButtons("#toolbar");
						initPtwAndIslDatagrid(id);
					}
				},"json");
	}else{
		$("#keyBoxForm").iForm("hide","curStatus");
	}
	
}

/**
 * @returns 获取表单值
 */
function getFormData(){
	
	if(! $("#keyBoxForm").valid()){
		return null;
	}
	var formData = $("#keyBoxForm").iForm("getVal");
	return formData;
}

/**
 * 提交钥匙箱
 */
function commitKeyBox(){
	var formData = getFormData();
	
	$.post(basePath + "ptw/ptwKeyBox/commitPtwKeyBox.do",
	 		{"keyBoxFormDate":JSON.stringify(formData)},
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
 * 编辑钥匙箱
 */
function editKeyBox(){
	$("#keyBoxForm").iForm("beginEdit"); 
	$("#keyBoxForm").iForm("endEdit","curStatus"); 
	$("#btn_keyBox_save").show(); 
	$("#btn_keyBox_edit").hide(); 
	FW.fixRoundButtons("#toolbar");
}

/**
 * 禁用钥匙箱
 */
function deleteKeyBox(){
	Notice.confirm("确定删除|确定删除该钥匙箱么？该操作无法撤销。",function(){
		$.post(basePath + "ptw/ptwKeyBox/deleteKeyBox.do",
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

function initPtwAndIslDatagrid(keyBoxId){
	initPtwTableDiv();
	initIslTableDiv();
}

function initPtwTableDiv(){
	var tableId = "Ptw";
	var columns = [[
	           	    {field:'wtNo',title:'编号',width:150,fixed:true},
	           	    {field:'wtStatus',title:'状态',width:85,fixed:true,
	           			formatter : function(value,row,index){
	           				return ptwUtil.getStatusNameByWtStatus(value);
	           			}
	           		},
	           		{field:'eqName',title:'设备名称',width:200,fixed:true},
	           		{field:'relateKeyBoxId',title:'关联的钥匙箱',width:200,fixed:true,
	           			formatter : function(value,row,index){
	           				var id = row.relateKeyBoxId;
	           				if(id){
		           				var ids = id.split(",");
		           				var nos = row.keyBoxNo.split(",");
		           				var links = [];
		           				for(var i in ids){
		           					links[i] = "<a onclick='openKeyBoxPage("+ids[i]+",\""+nos[i]+"\");'>"+nos[i]+"</a>";
		           				}
		           				return links.join(" , ");
	           				}else{
	           					return "无";
	           				}	           			}
	           		},
	           		{field:'workContent',title:'工作内容',width:180}
	           	  ]];
	$("#title"+tableId).iFold("init");
	$("#"+tableId+"Datagrid").datagrid({
	    columns:columns,
	    fitColumns:true,
	    scrollbarSize:0,
	    url : basePath + "ptw/ptwInfo/findPtwInfoByKeyBoxId.do?keyBoxId="+id,
	    singleSelect:true,
	    onDblClickRow:function(rowIndex,rowData){
        	var id = rowData.id;
        	var tabId = "ptwInfoDetail"+id;
        	openNewTab(tabId,"查看工作票","ptw/ptwInfo/preQueryPtwInfo.do",{opType:"handlePtw",id:id});	        	
        },
        onLoadSuccess: function(data){
        	if(data.total == 0){
        		$("#"+tableId+"Div").hide();
        	}
        }
	}); 
}

function initIslTableDiv(){
	var tableId = "Isl";
	var columns = [[
	           		{field:'no',title:'编号',width:150,fixed:true},
	           		{field:'status',title:'状态',width:85,fixed:true,
	           			formatter : function(value,row,index){
	           				return ptwUtil.getStatusNameByWtStatus(value);
	           			}
	           		},
	           		{field:'eqName',title:'设备名称',width:200,fixed:true},
	           		{field:'relateKeyBoxId',title:'关联的钥匙箱',width:200,fixed:true,
	           			formatter : function(value,row,index){
	           				var id = row.relateKeyBoxId;
	           				if(id){
		           				var ids = id.split(",");
		           				var nos = row.keyBoxNo.split(",");
		           				var links = [];
		           				for(var i in ids){
		           					links[i] = "<a onclick='openKeyBoxPage("+ids[i]+",\""+nos[i]+"\");'>"+nos[i]+"</a>";
		           				}
		           				return links.join(" , ");
	           				}else{
	           					return "无";
	           				}
	           			}
	           		},
	           		{field:'workContent',title:'工作内容',width:180}
	           	  ]];
	$("#title"+tableId).iFold("init");
	$("#"+tableId+"Datagrid").datagrid({
	    columns:columns,
	    fitColumns:true,
	    scrollbarSize:0,
	    url : basePath + "ptw/ptwIsolation/findIslInfoByKeyBoxId.do?keyBoxId="+id,
	    singleSelect:true,
	    onDblClickRow:function(rowIndex,rowData){
        	var id = rowData.id;
        	var tabId = "islInfoDetail"+id;
        	openNewTab(tabId,"查看隔离证","ptw/ptwIsolation/preQueryIslInfo.do",{id:id,opType:"islDetail"});	        	
        },
        onLoadSuccess: function(data){
        	if(data.total == 0){
        		$("#"+tableId+"Div").hide();
        	}
        }
	}); 
}

