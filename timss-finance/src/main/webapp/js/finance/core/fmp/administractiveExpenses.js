var fmpAttachMap=[];
//基础表单域定义
var fmpFormFields=[
{
    title : "类型", 
    id : "type",
    type : "combobox",
    data : [
        ["only","自己报销"],
        ["other","他人报销"],
        ["external","对外报销"]
    ],
	options: {
		initOnChange: true,
		onChange: function(val){
			if("only"==val){
				$("#form1").iForm("hide","payeeOther,payeeMore".split(","));
				$("#form1").iForm("show","payeeOnly");
				$("#f_payeeCode").val(userId);
				$("#f_payeeName").val(userName);
				$("#f_payeeOnly").html(userName);
			}else if("other"==val){
				$("#form1").iForm("hide","payeeOnly,payeeMore".split(","));
				//类型改变时，需要清空
				$("#f_payeeCode").val('');
				$("#f_payeeName").val('');
				$("#form1").iForm("setVal",{"payeeOther":''});
				$("#form1").iForm("show","payeeOther");
			}else if("external"==val){
				$("#form1").iForm("hide","payeeOnly,payeeOther".split(","));
				//类型改变时，需要清空
				$("#f_payeeCode").val('');
				$("#f_payeeName").val('');
				$("#form1").iForm("setVal",{"payeeMore":''});
				$("#form1").iForm("show","payeeMore");
			}
		}
	}
},
{
	title : "报销单名称", id : "name" , 
	rules: {
		required:true,
		maxChLength: 160
	},
	message:{
		maxChLength:"不能输入超过80个汉字"
	}

},
{
    title : "申请单", 
    id : "requestNoteName",
    type : "text",
    rules : {
		required:true
    }
},
{
	title: "报销金额(元)", 
	id: "price",
	type: "label",
	rules : {
		required:true,
		number: true,
		max: 9999999.99,
		min: 0
	}
},
{
	title: "批复金额(元)", 
	id: "budget",
	type: "label"
},
{
	title: "开支科目", 
	id: "subject",
	type: "label",
	formatter:function(val){
		return FW.getEnumMap("FIN_SUBJECT")[val];
	}
},
{title : "填单人", id : "creator" ,type: "label"},//仅仅作为显示用
{title : "收款方", id : "payeeOnly" ,type: "label",wrapXsWidth:12,wrapMdWidth:8},
{title : "收款方", id : "payeeOther" ,type: "text",wrapXsWidth:12,wrapMdWidth:8},
{title : "收款方", id : "payeeMore" ,type: "text",wrapXsWidth:12,wrapMdWidth:8},
{id : "payeeCode" ,type: "hidden"},
{id : "payeeName" ,type: "hidden"},
{id : "id" ,type: "hidden"},
{id : "applyId",type : "hidden"},
{id : "mainId",type : "hidden"},
{id : "status",type : "hidden"},
{id:"processInstId",type:"hidden"}
];
//附件表单域定义
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
		"swf" : basePath+"js/finance/common/uploadify.swf",
		//"fileSizeLimit" : 10 * 1024,
		"initFiles" :fmpAttachMap,
		"delFileAfterPost" : true
	}
}
];

//数据表格的列定义
var expensesListColumns= [[{
	field : 'id',
	title : 'id',
	width : 10,
	hidden : true
}, {
	field : 'join_nbr',
	title : '单据张数',
	fixed : true,
	width : 90,
	editor:{
		type:'text',
		options:{
			rules:{
				required:true,
				number:true
			}
		}
		
	}
}, {
	field : 'amount',
	title : '报销金额(元)',
	fixed : true,
	width : 90,
	editor:{
		type:'text',
		options:{
			rules:{
				required:true,
				number:true
			},
			onKeyUp : function(){
				//统计报销金额 这里js处理时会有精度bug
				var sum = 0;
				$("[name^=amount]").each(function(){
					sum += Number(Number($(this).val()).toFixed(2));
				});
				var budget = $("#form1").iForm("getVal","budget");
				$("#form1").iForm("setVal",{"price":sum});
			},
			onBlur : function(){
				$(this).val(Number($(this).val()).toFixed(2));
			}
		}
		
	}
}, {
	field : 'description',
	title : '报销事由',
	width : 100,
	editor:{
		type:'text',
		options:{
			rules:{
				required:true
			}
		}
	}
}, {
	field : 'remark',
	title : '备注',
	width : 120,
	editor:{
		type:'text',
		options:{
			rules:{
				required:true
			}
		}
	}
}, garbageColunms]];
//初始化附件表单
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
			data:data,
			$form:$form,
			$wrapper:$wrapper,
			attachMap:fmpAttachMap,
			attachFormFields:attachFormFields,
			readOnly:readOnly
		};
	initAttachFormTemplate(result);
}
//初始化报销明细的外框
function initExpensesWrapper(){
	if(!initExpensesWrapper.init){
		$('#expensesListWrapper').iFold();
		initExpensesWrapper.init=true;
	}	
}
//初始化报销明细数据表格
function initExpensesDataGrid(data){
	if(!dataGrid){
		dataGrid=$('#expensesList');
		dataGrid.datagrid({
			fitColumns:true,
			singleSelect:true,
			scrollbarSize:0,
			pageSize : pageSize,// pageSize为全局变量，自动获取的
			onClickCell : function(rowIndex, field, value) {
				//需要判断为编辑状态时(根据页面删除按钮列是否显示来判断)，才执行以下方法
				var isDisplay = $("#expensesListWrapper").find("[field=garbage-colunms]").css("display");
				if("none" != isDisplay){
					deleteGarbageColumnFunction(rowIndex, field, value,"code",dataGrid);
					//重新计算报销金额
					var sum = 0;
					$("[name^=amount]").each(function(){
						sum += Number(Number($(this).val()).toFixed(2));
					});
					var budget = $("#form1").iForm("getVal","budget");
					$("#form1").iForm("setVal",{"price":sum});
				}
			},
			data:data,
			columns : expensesListColumns
		});
	}
}
//按中文字符占字节大小重新计算精度
function fixCnPrec( value ) {
	return (Math.floor(value/3*2));
}
//添加报销明细
function addExpenses(){
	if(!addExpenses.init){
		addExpenses.init=true;
		initExpensesDataGrid();
		var row={};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
	}else{
		var row={};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
	}
	$('#b-add-expensesdtl').html('继续添加明细');
}
//获取报销明细数据
function getExpensesDtlListData(dataGrid){
	var expensesDtlData=null;
	if(dataGrid){
		var rows=dataGrid.datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			dataGrid.datagrid('endEdit',i);
		}
		expensesDtlData=dataGrid.datagrid('getRows');
	}
	return expensesDtlData;
}
//初始化ihint类的组件
function initHint(id){
	var $input=$('#'+id);
	var datasource = "";
	//对外报销的收款方是供应商、他人报销的收款方是用户、还有申请单三个不同的数据源
	if("f_payeeMore"==id){
		//对外报销  供应商的输入框
		datasource = basePath + "finance/expenses/querySupplierFuzzyByName.do";
	}else if("f_payeeOther"==id){
		//他人报销   用户的输入框
		datasource = basePath + "finance/expenses/queryUserInfoFuzzyByName.do";
	}else if("f_requestNoteName"==id){
		//申请单的输入框
		datasource = basePath + "finance/expenses/queryRequestNoteFuzzyByName.do";
	}
	if("f_payeeMore"==id||"f_payeeOther"==id){
		//收款人的ihint的初始化
		var remoteDataInit={ datasource : datasource,
				clickEvent : function(id, name) {
					$input.val(name);
					$("#form1").iForm("setVal",{"payeeCode":id,"payeeName":name});
				}
		};
		$input.iHint('init', remoteDataInit);
	}else if("f_requestNoteName"==id){
		//申请单的ihint的初始化
		var remoteDataInit={ datasource : datasource,
			clickEvent : function(id,name,data) {
				$input.val(data["name"]);
				$("#f_applyId").val(data["id"]);
				var budgetData = data["budget"]==null?"":data["budget"];
				$("#form1").iForm("setVal",{"subject":data["subject"],"budget":budgetData});
			}
		}
		$input.iHint('init', remoteDataInit);
	}
}
//初始化ihint信息，使其能够获取远程数据
function initRemoteData(){
	//申请单
	initHint('f_requestNoteName');
	//收款人_他人报销
	initHint('f_payeeOther');
	//收款人_多人报销
	initHint('f_payeeMore');
	//收款人_自己报销
	initHint('f_payeeOnly');
}
//由于不同的报销方式，收款人的获取是不同的，需要此方法，设定表单项的显示情况
function formElementDisplay(type){
	if("only"==type){
		$("#form1").iForm("hide","payeeOther,payeeMore".split(","));
		$("#form1").iForm("show","payeeOnly");
	}else if("other"==type){
		$("#form1").iForm("hide","payeeOnly,payeeMore".split(","));
		$("#form1").iForm("show","payeeOther");
	}else if("external"==type){
		$("#form1").iForm("hide","payeeOnly,payeeOther".split(","));
		$("#form1").iForm("show","payeeMore");
	}
}
//校验报销明细表
function validExpensesForm(){
	if(!$("#form1").valid()){
		return false;
	}
	if(!$("#expensesListWrapper").valid()){
		return false;
	}
	var budget = $("#form1").iForm("getVal","budget");
	var price = $("#form1").iForm("getVal","price");
	return true;
}