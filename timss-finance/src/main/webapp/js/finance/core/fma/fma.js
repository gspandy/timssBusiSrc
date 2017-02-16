var fmaFormFields=[
		 {id:"id",type:"hidden"},
		 {title : "申请类型", id:"applyType", type : "combobox",
				dataType : "enum",
				enumCat : "FIN_APPLY_TYPE"
		 },
		 {id:"deptid",type:"hidden"},	
		 {id:"proInstId",type:"hidden"},
		 {id:"applyUser",type:"hidden"},
		 {
			title : "名称", 
			id : "name",
			rules : {
				required : true
			}
		 },
		 {
			title : "列支科目", 
			id : "subject",
		    type : "combobox",
			dataType : "enum",
			enumCat : "FIN_SUBJECT",
			rules : {
				required : true
			}
		 },
		 {
			title : "申请费用(元)", 
			id : "budget",
			rules : {
				required : true,
				number : true
			}
		 },
		 {
			title : "申请人",
			id : "applyUsername",
			rules : {
				required : true
			}
		},
			{
				title : "申请部门",
				id : "deptname"
				
				
			},
			{
				title : "总经理审批",
				id : "needZJL",
				type : "combobox",
				data : [ [ "Y", "是" ], [ "N", "否" ] ],
				rules : {
					required : true
				}
			},
			{
				title : "内部会签",
				id : "needHQ",
				type : "combobox",
				data : [ [ "Y", "是" ], [ "N", "否" ] ],
				allowEmpty:true,
				rules : {
					required : true
				}
			},
				
		 {
		        title : "事由说明", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:48
		    }
	    
];
var fmaAttachMap=[];

var attachFormFields=[
{
	title : " ",
	id : "attach",
	linebreak : true,
	type : "fileupload",
	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath+"upload?method=uploadFile&jsessionid="+session,
		"delFileUrl" : basePath+"upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath+"upload?method=downloadFile",
		"swf" : basePath+"itcui/js/uploadify.swf",
		//"fileSizeLimit" : 10 * 1024,
		"initFiles" :fmaAttachMap,
		"delFileAfterPost" : true
	}
}
];

//初始化附件
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
		data:data,
		$form:$form,
		$wrapper:$wrapper,
		attachMap:fmaAttachMap,
		attachFormFields:attachFormFields,
		readOnly:readOnly
	};
	initAttachFormTemplate(result);
}


function initFMATable(data){
	if(!initFMATable.dataGrid){
		$('#fmaList').iFold();
		if(data && data.length){
			$("#b-add-fma").html('继续添加');
		}else{
			$("#b-add-fma").html('添加');
		}
		initFMATable.dataGrid=$("#fmaTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
		
			onDblClickRow : function(rowIndex, rowData) {
				
			},
			data:data,
			onClickCell:function(rowIndex, field, value){
				deleteGarbageColumnFunction(rowIndex, field, value,"code",initFMATable.dataGrid);
				
			},
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'id',
				hidden : true
			},{
				field : 'fid',
				hidden : true
			}, {
				field : 'description',
				title : '申请事由',
				width : 150,
				
				align : 'left',
				editor:{
					type:"text"
				}
			}, {
				field : 'amount',
				title : '申请金额(元)',
				width : 110,
				fixed :true,
				align : 'left',
				editor:{
					type:"text",
					options:{
						rules:{
							required:true,
							number:true
						}
					}
				}
				
			}, {
				field : 'remark',
				title : '备注',
				width : 200,
				
				align : 'left',
				editor:{
					type:"text"
				}
			}, garbageColunms ] ]
		});
	}
}

function addFMA(){
	var dataGrid=initFMATable.dataGrid;
	var row={};
	dataGrid.datagrid("appendRow",row);
	var rowindex=dataGrid.datagrid('getRowIndex',row);
	dataGrid.datagrid('beginEdit',rowindex);

	$('#b-add-fma').html('继续添加');
}