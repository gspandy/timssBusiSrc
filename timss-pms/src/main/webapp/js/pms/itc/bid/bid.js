var typeId="type";//项目类型在业务的字段
var typeEnum="PMS_BID_TYPE"; //项目类型在枚举表中的字段
var prefixId="#f_";//前端控件前缀
var bidAttachMap=[];
var bidMethodAttachMap=[];
var bidResultAttachMap=[];
var bidFormFields=[
	    {id:"bidId",type:"hidden"},
	    {id:"projectId",type:"hidden"},
	    {title : "招标名称", id : "name",
	    	rules:{
	    		required:true
	    	}
	    },
	    {title : "招标预算(元)", id : "budget", 
	    	rules:{
	    		required:true,
	    		number:true
	    	}
	    },
	    {title : "所属项目", id : "projectName"},
	    {
	        title : "招标方式", 
	        id :typeId,
	        type : "combobox",
	        dataType:'enum',
	        enumCat:typeEnum,
	    
	    	rules:{
	    		required:true
	    	}
	    },
	//    {title : "项目进度", id : ""},
	    
	    {
	        title : "备注", 
	        id : "command",
	        type : "textarea",
	        linebreak:true,
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
	    }
];
var bidAttachFormFields = [ {
	title : " ",
	id : "BAttach",
	linebreak : true,
	type : "fileupload",

	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath + "upload?method=uploadFile&jsessionid="
				+ session,
		"delFileUrl" : basePath + "upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath + "upload?method=downloadFile",
		"swf" : basePath + "js/uploadify.swf",
		"fileSizeLimit" : 10 * 1024,
		"initFiles" : bidAttachMap,
		"delFileAfterPost" : true
	}
} ];
var bidMethodFormFields=[
	    {id:"bidId",type:"hidden"},
	    {id:"bidMethodId",type:"hidden"},
	    {
	        title : "备注", 
	        id : "command",
	        type : "textarea",
	        linebreak:true,
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
	    }
];
var bidMethodAttachFormFields = [ {
	title : " ",
	id : "attach",
	linebreak : true,
	type : "fileupload",

	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath + "upload?method=uploadFile&jsessionid="
				+ session,
		"delFileUrl" : basePath + "upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath + "upload?method=downloadFile",
		"swf" : basePath + "js/uploadify.swf",
		"fileSizeLimit" : 10 * 1024,
		"initFiles" : bidMethodAttachMap,
		"delFileAfterPost" : true
	}
} ];
var bidResultFormFields = [
		{
			id : "bidId",
			type : "hidden"
		},
		{
			id : "bidMethodId",
			type : "hidden"
		},
		{
			title : "备注",
			id : "command",
			type : "textarea",
			linebreak : true,
			wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
		} ];
var bidResultAttachFormFields = [ {
	title : " ",
	id : "attach",
	linebreak : true,
	type : "fileupload",

	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath + "upload?method=uploadFile&jsessionid="
				+ session,
		"delFileUrl" : basePath + "upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath + "upload?method=downloadFile",
		"swf" : basePath + "js/uploadify.swf",
		"fileSizeLimit" : 10 * 1024,
		"initFiles" : bidResultAttachMap,
		"delFileAfterPost" : true
	}
} ];

var projectFormFields=[
           	    {id:"projectId",type:"hidden"},
  
           	    {title : "所属项目", id : "projectName"},
           	    {
           	        title : "项目性质", 
           	        id :'property',
           	        type : "combobox",
           	        dataType:'enum',
           	        enumCat:'pms_project_property'
           	    },

           	    {title : "项目金额(元)", id : "total"},
           	    {title : "已用金额", id : "used"},
           	    {title : "冻结金额", id : "fozen"},
           	    {title : "可用金额", id : "left"}

           ];

// 初始化附件表单
function initBidAttachForm(data,$form,$wrapper,readOnly){
	var result={
			data:data,
			$form:$form,
			$wrapper:$wrapper,
			attachMap:bidAttachMap,
			attachFormFields:bidAttachFormFields,
			readOnly:readOnly
		};
	initAttachFormTemplate(result);
}

function initBidMethodAttachForm(data,$form,$wrapper,readOnly){
	var result={
			data:data,
			$form:$form,
			$wrapper:$wrapper,
			attachMap:bidMethodAttachMap,
			attachFormFields:bidMethodAttachFormFields,
			fieldPrefix:'bma_',
			readOnly:readOnly
		};
	initAttachFormTemplate(result);
}

function initBidResultAttachForm(data,$form,$wrapper,readOnly){
	var result={
			data:data,
			$form:$form,
			$wrapper:$wrapper,
			attachMap:bidResultAttachMap,
			attachFormFields:bidResultAttachFormFields,
			fieldPrefix:'bra_',
			readOnly:readOnly
		};
	initAttachFormTemplate(result);
}

/**
 * 初始化招标单位datagrid，如果已经存在，则不处理
 */
function initSupplierDataGrid(data){
	if(!dataGrid){
		dataGrid=$('#supplierList').datagrid({
			fitColumns:true,
			scrollbarSize:0,
			onDblClickRow : function(rowIndex, rowData) {
			},
			onClickCell:function(rowIndex, field, value){
				deleteGarbageColumnFunction(rowIndex, field, value,"companyNo",dataGrid);
			},
			data:data,
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'companyNo',
				hidden : true
			}, {
				field : 'name',
				title : '公司名称',
				width : 300,
				align : 'left',
				fixed : true
			}, {
				field : 'type',
				title : '公司类型',
				width : 120,
				align : 'left',
				fixed : true
			}, {
				field : 'contact',
				title : '联系人',
				width : 80,
				align : 'left',
				fixed : true
			},{
				field:"iswinner",
				title:"是否中标",
				width:60,
				align:'left',
				fixed:true,
				editor:{
					type : 'combobox',
					options : {
						data : [ [true,"是"],[false,"否",true]]
					}
				},
				formatter: function(value,row,index){
 					if(value==true ){
 						return '是';
 					}else {
 						return '否';
 					}
 				}
			},{
				field:"price",
				title:"标价",
				width:105,
				align : 'right',
				fixed:true,
				editor:{
					type : 'text'
				}
			}, {
				field : ' ',
				title : '',
				width : 85,
				align : 'left'
			},garbageColunms
			
			] ]
		});
		dataGrid.datagrid('hideColumn','iswinner');
		dataGrid.datagrid('hideColumn','price');
		dataGrid.datagrid('hideColumn','garbage-colunms');
	}
}

function addSupplier(){
	var btnOpts = [
			{
				"name" : "取消",
				"onclick" : function() {
					return true;
				}
			},
			{
				"name" : "确定",
				"style" : "btn-success",
				"onclick" : function() {
					//itcDlgContent是对话框默认iframe的id
					var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
					var grid = p.dataGrid;
					if (grid) {
						var selected=grid.datagrid('getSelections');
						if(selected && selected.length){
							//初始化datagrid,如果存在则不初始化
							initSupplierDataGrid();
							for(var i in selected){
								var sid=selected[i].companyNo;
								if(!pmsUtil.isExisted(sid)){
									dataGrid.datagrid('appendRow',selected[i]);
									dataGrid.datagrid('beginEdit',i);
									pmsUtil.add(sid);
									changeAddSupplierButton();
								}
							}
						}
					}
					_parent().$("#itcDlg").dialog("close"); 
				}
			} ];
	FW.dialog("init",{src:basePath+"page/pms/core/supplier/supplierList.jsp",btnOpts:btnOpts,dlgOpts:{title:'选择招标单位'}});
}

function showDataGridColumn(){
	dataGrid.datagrid('showColumn','iswinner');
	dataGrid.datagrid('showColumn','price');
}

function changeAddSupplierButton(){
	if(!changeAddSupplierButton.init){
		changeAddSupplierButton.init=true;
		$("#b-add-supplier").html('继续添加招标单位');
	}
}

function beginSupplierEdit(){

	showDataGridColumn();
	var rows=dataGrid.datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		dataGrid.datagrid('beginEdit',i);
	}
}
