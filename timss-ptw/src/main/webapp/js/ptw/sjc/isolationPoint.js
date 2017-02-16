/* 表单字段定义  */
var fields = [
              {title : "id",id : "assetId",type:"hidden"},
				{title : "资产名称",id : "assetName",rules : {	required : true } },
				{title:"资产编码",id:"assetCode",
					rules:{
						remote:{"url": basePath + "asset/assetInfo/isAssetCodeExist.do",
							type:"post",
							data:{
								assetCode : function(){return $("#f_assetCode").val();},
								assetId : function(){return $("#f_assetId").val();}
							}
						}
					}
				},
				{title : "parentId",id : "parentId",type : "hidden"},
				{title : "父资产",id : "locationName",type:"label"},
				{title : "资产类型",id : "assetType" ,
					type : "combobox",
			        dataType : "enum",
			        enumCat : "AST_ASSET_TYPE",
			        value : "production",
			        hide : true
				},
				{title : "参数详细说明",id: "modelDesc",linebreak:true,type:'textarea',
					wrapXsWidth:12,wrapMdWidth:8,height:48
				}
			];

/* 表单字段定义  */
var createFields = [
              {title : "资产名称",id : "assetName",rules : {	required : true,maxChLength : parseInt(100*2/3)}},
              {title:"资产编码",id:"assetCode",
            	  rules:{
            		  maxChLength:parseInt(30*2/3),
            		  remote:{"url": basePath + "asset/assetInfo/isAssetCodeExist.do",
            			  type:"post",
            			  data:{
            				  assetCode : function(){return $("#f_assetCode").val();},
            				  assetId : function(){return $("#f_assetId").val();}
            			  }
            		  }
            	  }
              },
              {title : "parentId",id : "parentId",type : "hidden"},
              {title : "父资产",id : "locationName",type:"label"},
              {title : "资产类型",id : "assetType" ,
            	  type : "combobox",
            	  dataType : "enum",
            	  enumCat : "AST_ASSET_TYPE",
            	  value : "production",
            	  hide : true
              },
              {title : "参数详细说明",id: "modelDesc",linebreak:true,type:'textarea',
            	  wrapXsWidth:12,wrapMdWidth:8,height:48,rules : {maxChLength : parseInt(4000*2/3)}
              }
              ];

var isolationMethodDelteId = 1;
var	isolationMethodGridField = [[
		{field : 'isolationMethodDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.isolationMethodDelteId =isolationMethodDelteId;
				return isolationMethodDelteId++;
			}
		},  
   	    {field : 'no',title : '方法编号',width:80,fixed:true},  
   	    {field : 'method',title : '方法描述',width:400},
		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
			 formatter:function(value,row,index){
				     return '<img src="'+basePath+'img/ptw/btn_garbage.gif" onclick="deleteGridRow('+
				     		"'isolationMethodTable',"+row.isolationMethodDelteId+')" width="16" height="16" >';
			}
		}
	]];

function pointPriv(){
	//新建
	Priv.map("privMapping.PTW_POINT_NEW","PTW_POINT_NEW");
	//编辑
	Priv.map("privMapping.PTW_POINT_EDIT","PTW_POINT_EDIT");
	
	Priv.apply();
}


function initIsolationPointPage(pointNo){
	$("#isolationPointForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	
	$("#title_isolationMethod").iFold("init");
	var isolationMethodDatagrid =  $("#isolationMethodTable").datagrid({
	    columns:isolationMethodGridField,
	    idField:'isolationMethodDelteId',
	    singleSelect:true,
	    fitColumns:true,
	    scrollbarSize:0
	}); 
	
	$.post(basePath + "ptw/ptwIsolationPoint/queryInfoByIsolationPointId.do",{"id":pointNo},
			function(isolationPointInfoData){
				if(isolationPointInfoData.result == "success"){
					$("#btn_isolationPoint_save").hide(); 
					var isolationPointFromData = eval("(" +isolationPointInfoData.formDate+ ")");
					$("#isolationPointForm").iForm("setVal",isolationPointFromData);
					$("#isolationPointForm").iForm("endEdit");
					var isolationRelationData = eval("(" +isolationPointInfoData.islMethodData+ ")");
					var dataGridLength = isolationRelationData.length;
					if(dataGridLength == 0){
						$("#isolationMethodContentDiv").hide();
					}else{
						for(var i=0; i<dataGridLength; i++){
							$("#isolationMethodTable").datagrid("appendRow",isolationRelationData[i] );
						}
						$("#isolationMethodTable").datagrid("hideColumn","oper");//隐藏某一列
						$("#isolationMethodBtnDiv").hide();
					}
					$("#btn_isolationPoint_delete,#btn_isolationPoint_back").hide();
					//权限
					pointPriv();
					FW.fixRoundButtons("#isolationPointbar");
				}
			},"json");
	
	
}

/**
 * @returns 获取配置的隔离方法数据
 */
function getDataGridData(){
	var isolationMethodData = $("#isolationMethodTable").datagrid("getData");
	if(isolationMethodData.total==0){
		FW.error("请添加隔离方法");
		return ;
	}
	return isolationMethodData;
}

/**
 * 提交隔离方法 true--新建方法； false--其他
 */
function commitIsolationPoint( isCreate ){
	if( !$( "#isolationPointForm" ).valid() ){
		return;
	}
	var formData = getFormData( "isolationPointForm" );
	
	var dataGridData = $("#isolationMethodTable").datagrid("getData");
	if(dataGridData.total==0){
		FW.error("请添加隔离方法");
		return ;
	}
	
	$.post(basePath + "ptw/ptwIsolationPoint/insertOrUpdatePtwIsolationPoint.do",
	 		{"isolationMethodDate":FW.stringify(dataGridData),
				formData : formData
	 		},
			function(data){
				if(data.result == "success"){
					FW.success("保存成功");
					//closeCurPage();
					endEdit();
					if( isCreate ){
						addTreeNode( data.assetBean );
						closeTab();
					}else{
						updateTreeNode( data.assetBean );
					}
				}else {
					FW.error("保存失败");
				}
	  },"json");
	
}

/**
 * 编辑隔离方法
 */
function editIsolationPoint(){
	$("#isolationMethodContentDiv").show(); 
	$("#btn_isolationPoint_back").show();  
	$("#btn_isolationPoint_save,#btn_isolationPoint_delete").show(); 
	$("#btn_isolationPoint_edit,#btn_isolationPoint_create").hide(); 
	$("#isolationMethodTable").datagrid("resize"); //缺此步，可能datagrid的宽度显示有问题
	$("#isolationMethodTable").datagrid("showColumn","oper");//隐藏某一列
	$("#isolationMethodBtnDiv").show();
	$("#isolationPointForm").iForm("beginEdit");
	FW.fixRoundButtons("#isolationPointbar");
}


function endEdit(){
	$("#btn_isolationPoint_save,#btn_isolationPoint_delete,#btn_isolationPoint_back").hide(); 
	$("#btn_isolationPoint_edit,#btn_isolationPoint_create").show(); 
	$("#isolationMethodTable").datagrid("hideColumn","oper");//隐藏某一列
	$("#isolationMethodBtnDiv").hide();
	$("#isolationPointForm").iForm("endEdit");
	FW.fixRoundButtons("#isolationPointbar");
}
/**
 * 禁用隔离方法
 */
function deleteIsolationPoint(){
	Notice.confirm("确定删除|确定删除该隔离方法么？该操作无法删除。",function(){
		$.post(basePath + "ptw/ptwIsolationPoint/deleteIsolationPoint.do",
		 		{"id":id},
				function(data){
					if(data.result == "success"){
						FW.success("删除成功");
//						closeCurPage();
					}else {
						FW.error("删除失败");
					}
		  },"json");
	},null,"info");	
}

/**
 * 添加隔离方法弹出框
 */
function appendIsolationMethod(){
	var dataGridData1 = $("#isolationMethodTable").datagrid("getData");
	var oldMethString = FW.stringify(dataGridData1);
	var oldMethJson = FW.parse(oldMethString);
	
	FW.showIsolationDialog({
		onParseData : function(data){
			var oldSize = oldMethJson.total;
			var size = data.length;
			
			for(var j=0; j<oldSize; j++){
				var oldId = oldMethJson.rows[j].methodId;
				for(var i=0;i<size;i++){
					if(data[i] != undefined){
						var newMethId = data[i]["id"];
						if(oldId == newMethId ){
							delete data[i];  //删掉重复的
						}
					}
				}
			}
			
			for(var i=0;i<size;i++){
				if(data[i]!=undefined){
					var newMethId = data[i]["id"];
					data[i]["methodId"] = newMethId;
					delete data[i].id;
					$("#isolationMethodTable").datagrid("appendRow",data[i] );  //插入新增的
				}
			}

			$("#btn_isolationMethodTable").html("继续添加");
			 
		}
	});
	
	
}  
/** 删除dataGrid中的某一行数据
 * @param dataGridId
 * @param index
 */
function deleteGridRow(dataGridId,deleteId){
	$('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
 }

//新建隔离点
function createIslPoint(){
	var formData = $("#isolationPointForm").iForm("getVal");
	var assetId = formData.assetId;
	var assetName = formData.assetName;
	
	var url = basePath + "ptw/ptwIsolationPoint/insertIslPointPage.do?assetId=" + assetId
				+ "&assetName=" + encodeURI(assetName);
	addTabWithTreeNoReload( "addIslPoint" + assetId, "新建隔离点", url,"ptw" );
}

//删除隔离点
function deleteIsolationPoint(){
	var formData = $("#isolationPointForm").iForm("getVal");
	var assetId = formData.assetId;
	var assetName = formData.assetName;
	var parentId = formData.parentId;
	
	var url = basePath + "ptw/ptwIsolationPoint/deleteIsolationPoint.do?assetId=" + assetId;
	
	FW.confirm("确定删除 " + assetName + " 节点及其子节点吗？该操作无法恢复。", function() {
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					FW.success("删除成功！");
		        	deleteTreeNode( parentId );
				}else{
					FW.error( "保存失败 ！");
				}
			}
		});
	});
}
