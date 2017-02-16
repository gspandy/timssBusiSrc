//表单域定义
var formBaseFields = [
	{id : "fid" ,type: "hidden"},
	{title : "报销类型", id : "finance_typeid",rules : {required:true},
		type : "combobox",
		dataType : "enum",
		enumCat : "FIN_REIMBURSE_TYPE",
		options: {
			initOnChange: false,
			onChange: changeFinTypeComboBox
		}
	},
	{
	    title : "记账类型", 
	    id : "accType",
	    type : "combobox",
	    data : [
	        ["department","部门"],
	        ["company","公司"]
	    ]
	},
	{title : "报销单名称", id : "fname" ,rules: {required:true,maxChLength: fixCnPrec(254)}},
	{title : "申请单编号", id : "applyId",type: "hidden"},
	{title : "相关申请单", id : "applyName"},
	{title : "开始日期",id : "strdate",type : "date",dataType:"datetime",
		rules : {required : true}	
	},
	{title : "结束日期",id : "enddate",type : "date",dataType:"datetime",
		rules : {required : true,greaterEqualThan:"#f_strdate"}	
	},
	{title : "报销事由",id : "description",rules: {required: true,maxChLength: fixCnPrec(254)}}, 
	{
		title: "报销人",
		id: "beneficiary",
		rules: {required:true},
		render: function(id) {
			$("#" + id).attr("placeholder","请输入姓名或账号").initHintPersonList();
			
			$("#" + id).initHintPersonList({
				clickEvent: function(id, name) {
					//$("#f_beneficiary").val(name);
					var uid = id.split("_")[0]; //[1]是用户在当前站点下对应的一个组织编号
					beneficiaryid = uid;
					$("#autoform").iForm( "setVal", {beneficiary: name} );
				}
			});
		}
	},
	{title:"报销人ID",id:"beneficiaryid",type: "hidden"},
	{title:"参与领导",id:"join_boss",
		render:function(id){
			$("#" + id).attr("placeholder","请点放大镜选人").iInput("init",{
				icon : "itcui_btn_mag",
				onClickIcon : function(){
					FW.selectPersonByTree({
						single : false,
						onSelect : function(obj){
							var names = [];
							var ids = [];
							for(var k in obj){
								names.push(obj[k]);
								ids.push(k);
							}
							$("#f_join_boss").val(names.join(","));
							$("#f_join_bossid").val(ids.join(","));
						}
					});
				}				
			});
		},
		rules: {
			maxChLength: fixCnPrec(50)
		}
	}, 
	{title:"参与领导编号",id:"join_bossid",type: "hidden"},
	{title : "参与人数",id : "join_nbr",rules : {digits: true,maxEnLength: 3}},
	{title: "报销金额(元)", id: "price",type: "label",
		rules : {required:true,	number: true,max: 9999999.99,min: 0}
	},
	{title: "批复预算金额(元)", id: "apply_budget",type: "label"	},
	{title: "开支科目",id: "subject",type: "label",
		formatter:function(val){
			return FW.getEnumMap("FIN_SUBJECT")[val];
		}
	},
	{title : "填单人", id : "creatorname" ,type: "label"},
	{title : "填单人ID", id : "createid" ,type: "hidden"},
	{title : "收款方", id : "payeeOnly" ,type: "label",wrapXsWidth:12,wrapMdWidth:8},
	{title : "收款方", id : "payeeOther" ,type: "text",wrapXsWidth:12,wrapMdWidth:8},
	{title : "收款方", id : "payeeMore" ,type: "text",wrapXsWidth:12,wrapMdWidth:8},
	{id : "payeeCode" ,type: "hidden"},
	{id : "payeeName" ,type: "hidden"},
	{id : "applyId",type : "hidden"},
	{id : "status",type : "hidden"},
	{id:"finance_flowid",type:"hidden"},
	
	{title : "填表日期" , id : "formDay" ,type: "hidden"},
	{title: "总金额(元)", 	id: "total_amount",	type: "label",
		rules : {
			number: true,
			max: 9999999.99,
			min: 0
		}
	},
	{
		title : "需要修改", 
		id : "needModify",
		type : "combobox",
		options:{allowEmpty:true},
		rules : {required : true},
		data : [
		        ["Y","是"],
		        ["N","否"]
		    ]
	}
	];


//表格列
var datagridBaseColumns = [[
   {field : 'id',title : 'id',hidden : true}, 
   {field : 'strdate',title : '开始日期',fixed : true,width : 120,
		formatter: function(value,row,index){
			//时间转date的string，还有long2date(value)方法
			return FW.long2date(value);
		}		
	},
   {field : 'beneficiary',	title : '报销人',fixed : true,width : 90}, 
   {field : 'department',title : '部门',	fixed : true,width : 90}, 
   {field : 'doc_nbr',title : '单据张数',fixed : true,width : 90}, 
   {field : 'description',title : '报销事由',width : 150},
   {field : 'amount',title : '报销金额(元)',fixed : true,width : 90,align:"right"}, 
   {field : 'incidentalcost',title : '杂费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.incidentalcost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	}, 
	{field : 'trafficcost',title : '交通费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.citytrafficcost + row.longbuscost + row.huochecost + row.ticketcost + row.othertrafficcost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	}, 
	{field : 'carcost',title : '车辆费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.fuelcost + row.bridgecost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	}, 
	{field : 'meetingcost',	title : '会议费(元)',fixed : true,	width : 100	,align:"right"},  
	{field : 'staycost',title : '住宿费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.staycost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	},
	{field : 'allowanceType',title : '补贴标准类型',hidden : true}, 
	{field : 'allowancecostPerDay',title : '补贴标准(元/天)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = 0 ;
			if(row.allowanceType ){
				amount = realAllowancePerDay = getAllowanceTypeEnum("FIN_ALLOWANCE_TYPE",row.allowanceType);
			}
			amount = chgAmtFormat(amount);
			return amount;
		}
	}, 
	{field : 'allowancecost',title : '补贴费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.allowancecost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	}, 
	{field : 'traincost',title : '培训费(元)',fixed : true,width : 100,align:"right",
		formatter: function(value, row, index) {
			var amount = row.traincost;
			amount = chgAmtFormat(amount);
			return amount;
		}
	},
   {field : 'remark',title : '备注',width : 150}, 
//   {field : 'spremark',title : '特殊说明',	width : 150}, 
   {field : 'delete',title : '',fixed : true,width : 45,formatter : deleteimg,	align : 'center'}
   ]];

//datagrid明细表单域
var dgFormBaseFields = [
	{title : "报销金额(元)",	id : "oo_businessentertainment",
		rules : {required: true,number: true,max: 9999999.99, min: 0.01/*,greaterThan:"0.1"*/}
		/*messages : {
		 "greaterThan" : "报销金额不能小于等于0"
		 }*/
	},
	{title : "报销金额(元)",	id : "oo_carcost",
		rules : {required: true,number: true,max: 9999999.99,min: 0.01}
	}, 
	{title : "报销金额(元)",id : "oo_officecost",
		rules : {required: true,number: true,max: 9999999.99,min: 0.01}
	},
	{title : "报销金额(元)",	id : "oo_welfarism",
		rules : {required: true,number: true,max: 9999999.99,min: 0.01}
	}, 
	{title: "报销人",id: "oo_beneficiary",rules: {required:true},
		render: function(id) {
			$("#" + id).attr("placeholder","请输入姓名或账号").initHintPersonList();
			$("#" + id).initHintPersonList({
				clickEvent: function(id, name) {
					var uid = id.split("_")[0]; //[1]是用户在当前站点下对应的一个组织编号
					$("#autoformsingle").iForm( "setVal", {oo_beneficiaryid: uid, oo_beneficiary: name} );
				}
			});
		}
	},
	{title: "报销人编号",id: "oo_beneficiaryid",type: "hidden"}, 
	{title : "报销部门",id : "oo_department"}, 
	{title : "单据张数",id : "oo_doc_nbr",
		rules : {required: true,digits: true,min: 0,maxEnLength: 3}
	}, 
	
	
	{title : "报销事由",id : "oo_description",linebreak : true,
		wrapXsWidth : 12,wrapMdWidth : 12,labelMdWidth : 1,
		labelXsWidth : 125,inputXsWidth : 875,
		rules: {required: true,maxChLength: fixCnPrec(254)}
	}, 
	{title : "参与人数",id : "oo_join_nbr",
		rules : {digits: true,maxEnLength: 3}
	}, 
	{title:"参与领导",id:"oo_join_boss",
		render:function(id){
			$("#" + id).attr("placeholder","请点放大镜选人").iInput("init",{
				icon : "itcui_btn_mag",
				onClickIcon : function(){
					FW.selectPersonByTree({
						single : false,
						onSelect : function(obj){
							var names = [];
							var ids = [];
							for(var k in obj){
								names.push(obj[k]);
								ids.push(k);
							}
							$("#f_oo_join_boss").val(names.join(","));
							$("#f_oo_join_bossid").val(ids.join(","));
						}
					});
				}				
			});
		},
		rules: {maxChLength: fixCnPrec(50)}
	}, 
	{title:"参与领导编号",id:"oo_join_bossid",type: "hidden"}, 
	
	
	
	{title : "开始日期",id : "oo_strdate",type : "date",rules : {required : true}}, 
	{title : "结束日期",id : "oo_enddate",type : "date",
		rules : {greaterEqualThan: "#f_oo_strdate",required: true}
	},
	{title : "起止地址",id : "oo_address",linebreak : true,
		wrapXsWidth : 12,wrapMdWidth : 12,labelMdWidth : 1,
		labelXsWidth : 125,inputXsWidth : 875,
		rules: {required: true,maxChLength: fixCnPrec(254)}
	}, 
	{title : "备注",id : "oo_remark",type : "textarea",linebreak : true,
		wrapXsWidth : 12,wrapMdWidth : 12,labelMdWidth : 1,
		labelXsWidth : 125,	inputXsWidth : 875, height:72,
		rules: {maxChLength: fixCnPrec(1024)}
	}, 
      
	{title : "住宿天数",id : "oo_stay_days",
		rules : {digits: true,maxEnLength: 3}
	}, 
	{title : "补贴天数",id : "oo_allowance_days",
		rules : {digits: true,maxEnLength: 3}
	}, 
	{title : "补贴标准",id : "oo_allowance_type",
		type : "combobox",
		options:{
			allowEmpty:true			
		},
		data : getAllowanceType()
	}, 
	{title : "其他天数",id : "oo_other_days",
		rules : {digits: true,maxEnLength: 3}
	}, 
	{title : "会议费(元)",id : "oo_meetingcost",
		rules : {number: true,max: 9999999.99,min: 0}
	},
	{title : "机票费(元)",id : "oo_ticketcost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "住宿费(元)",id : "oo_staycost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "市内交通费(元)",id : "oo_citytrafficcost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "油费(元)",id : "oo_fuelcost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "长途汽车费(元)",id : "oo_longbuscost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "路桥费(元)",id : "oo_bridgecost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "火车费(元)",id : "oo_huochecost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "杂费(元)",id : "oo_incidentalcost",
		rules : {number: true,max: 9999999.99,min: 0}
	}, 
	{title : "培训费(元)",id : "oo_traincost",
		rules : {number: true,max: 9999999.99,min: 0}
	},
	{title : "其他交通费(元)",id : "oo_othertrafficcost",
		rules : {number: true,max: 9999999.99,min: 0}
	}	
];

/** 切换报销类型,动态的修改form表单和datagrid显示的内容
 * @param val
 * @param combo
 */
function changeFinTypeComboBox(val, combo) {
	if( finTypeEn != val ) {
		finTypeEn = val;
		showForm();
		createdg();
		getDefKeyByfinTypeEn(finTypeEn); 
	}
}

//初始化ihint类的组件
function initHint(id){
	var $input=$('#'+id);
	var datasource = "";
	
	if("f_applyName"==id){
		//申请单的输入框
		datasource = basePath + "finance/fma/queryApplyInfoFuzzyByName.do";
	}
	
	if("f_applyName"==id){
		//申请单的ihint的初始化
		var remoteDataInit={ datasource : datasource,
			clickEvent : function(id,name,data) {
				$("#autoform").iForm("setVal",{"applyId":data.id,"fname":data.name,
					"applyName":data.name,"apply_budget":data.budget});
			}
		}
		$input.iHint('init', remoteDataInit);
	}
}

/**
 * 流程走分支需要的参数，即分支判断条件
 * */
var flowPathParams = {  "reimburseType":"",
						"isLeader":"",
						"amount":"",
						"accType":"",
						"isXZB":"",
						"needSubmitToManager":"",
						"needHQ":"",
						"isXZBoJYB":""
					}

function getAllowanceType(){
	if(FW.get("allowanceType")){
		return FW.get("allowanceType");
	}
	return null;
}