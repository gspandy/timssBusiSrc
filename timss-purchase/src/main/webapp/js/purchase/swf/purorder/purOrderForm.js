//新建采购单
var new_form = [
	{title : "合同名称", id : "sheetname", rules : {required:true}},
    {title : "采购类型", id : "sheetIType", rules : {required:true},
    	type : "combobox",
		dataType : "enum",
		enumCat : "ITEMORDER_TYPE"
    },
    {title : "供应商", id : "companyName",rules : {required:true}},
    
    {title : "招标方式", id : "bidType", rules : {required:true},
    	options:{
        	allowEmpty:true
        },
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_BID_TYPE"
    },
    {title : "合同编号", id : "spNo",
    	rules : {
    		regex : /^(\d{3}-\d{2})$/
    	},
    	messages : {
    		regex : "请按照ddd-dd格式输入"
    	},
		render : function(id){
			$("#"+id).on("change",function(){
				//SynRemoteValid是个同步验证，而且没有留有自定义回调函数的入口了。这里用onchange+settimeout(function(){},0)加以解决
				setTimeout(function(){
					if(""!=$("f_spNo").val()){
						$.ajax({
							url : basePath + "purchase/purorder/isSpNoExisted.do",
					    	type:"POST",
					    	data: {
								spNo: function() {
									return $("#f_spNo").val();
				        		},
				        		sheetId:function(){
				        			if(sheetId){
				        				return sheetId;
				        			}
				        			return "";
				        		}
							},
					    	success:function(data){
					    		if("true" == data["isExisted"]){
					    			FW.error("合同编号已经存在");
					    		}
					    	},
					    	error:function(){
					    		FW.error("无法生成 合同编号");
					    	}
						});
					};	
				},0);
			});	
		}	
    },
    {title : "供应商编码", id : "companyNo",type:"hidden"},
    {title : "要求到货日期", id : "dhdate",type:"date",rules : {required:true}},
    {title : "质保天数", id : "qualityOkLen",type:"text",
    	 rules : { "digits" : true,range:[1,9999]} 
    },
    {title : "总价(元)", id : "totalPrice",type:"label"},
    {title : "统一税率(%)", id : "taxRate",
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_ORDER_TAXRATE",
		initOnChange: false,
		options : {
	      	  onChange : function(val){
	      		allSelectControlCombox(val);
	      	  }
        }
    },
    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
    {title : "申请单编码", id : "sheetno", type:"hidden"},
    {title : "状态", id : "status", type:"hidden"}
];

//编辑采购单	
var edit_form = [
	{title : "合同名称", id : "sheetname", rules : {required:true}},
	{title : "经办人", id : "username", type:"label"},
	{title : "总价(元)", id : "totalPrice",type:"label"},
    {title : "已付款(元)", id : "totalPay",type:"label"},
    {title : "采购类型", id : "sheetIType", rules : {required:true},
    	type : "combobox",
		dataType : "enum",
		enumCat : "ITEMORDER_TYPE"
    },
    {title : "流水号", id : "sheetno", type:"label"},
    {title : "合同编号", id : "spNo",
    	rules : {
    		regex : /^(\d{3}\-\d{2})$/
    	},
    	messages : {
    		regex : "请按照ddd-dd格式输入"
    	},
		render : function(id){
			$("#"+id).on("change",function(){
				//SynRemoteValid是个同步验证，而且没有留有自定义回调函数的入口了。这里用onchange+settimeout(function(){},0)加以解决
				setTimeout(function(){
					if(""!=$("f_spNo").val()){
						$.ajax({
							url : basePath + "purchase/purorder/isSpNoExisted.do",
					    	type:"POST",
					    	data: {
								spNo: function() {
									return $("#f_spNo").val();
				        		},
				        		sheetId:function(){
				        			if(sheetId){
				        				return sheetId;
				        			}
				        			return "";
				        		}
							},
					    	success:function(data){
					    		if("true" == data["isExisted"]){
					    			FW.error("合同编号已经存在");
					    		}
					    	},
					    	error:function(){
					    		FW.error("无法生成 合同编号");
					    	}
						});
					};	
				},0);
			});	
		}
    },
    {title : "商务网订单号", id : "businessno", type:"label",formatter:function(value){return null==value?"":value.replace(Priv.secUser.siteId,'')}},
    {title : "供应商编码", id : "companyNo",type:"hidden"},
    {title : "供应商", id : "companyName",rules : {required:true}},
    {title : "招标方式", id : "bidType", rules : {required:true},
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_BID_TYPE"
    },
    {title : "要求到货日期", id : "dhdate",type:"date", rules : {required:true}},
    {title : "质保天数", id : "qualityOkLen",type:"text",
   	  rules : { "digits" : true,range:[1,9999]} 
    },
    
    {title : "统一税率(%)", id : "taxRate",
    	type : "combobox",
		dataType : "enum",
		enumCat : "PUR_ORDER_TAXRATE",
		initOnChange: false,
		options : {
	      	  onChange : function(val){
	      		  if(formInited&&datagridInited){
	      			allSelectControlCombox(val);  
	      		  }
	      	  }
        }
    },
    {title : "买方信息", id : "purchaserName",type:"label",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
    {title : "状态", id : "status", type:"hidden"}
];

//编辑表单加载数据（通用方法）
function editForm(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purorder/queryPurOrderDetail.do",
		data: {"sheetId":sheetId },
		dataType : "json",
		success : function(data) {
			if(data.totalPrice>20000){
				data.purOrderIsGm = "Y";
			}else{
				data.purOrderIsGm = "N";
			}
			var loaddata = {
					"username" : data.username, 
					"sheetname" : data.sheetname,
					"sheetIType" : data.sheetIType,
					"bidType" : data.bidType,
					"companyName" : data.companyName,
					"companyNo" : data.companyNo, 
					"qualityOkLen":data.qualityOkLen==0?null:data.qualityOkLen,
					"dhdate" : data.dhdate, 
					"totalPrice" : data.totalPrice, 
					"remark" : data.remark,
					"sheetno" : data.sheetno,
					"taxRate" : data.taxRate,
					"purOrderIsGm" : data.purOrderIsGm,
					"status" : data.status,
					"businessno" : data.businessno,
					"purchaserName":data.purchaserName,
					"spNo":data.spNo
				};
			//被终止的合同不显示录入发票和终止合同的按钮 -- 开始
			var status = data.status;
			var createaccount = data.createaccount;
			var curaccount = Priv.secUser.userId;
			//不是创建者打开不可见录入发票
			if(createaccount!=curaccount){
				$("#btn-stopOrder").hide();
			}
			status= new Number(status);
			if(5==status||10==status){
				var invoiceBtn = $("#btn-invoice");
				var stopOrderBtn = $("#btn-stopOrder");
				var createInvAcceptBtn = $("#btn-createInvAccept");
				if(undefined!=invoiceBtn){
					$("#btn-invoice").hide();
				}else{
					$("#btn-invoice").hide();//永远隐藏录入发票按钮，以后将用付款报账功能所替代
				}
				if(undefined!=stopOrderBtn){
					$("#btn-stopOrder").hide();
				}
				if(undefined!=createInvAcceptBtn){
					$("#btn-createInvAccept").hide();
				}
			}else{
				$("#btn-invoice").hide();  //永远隐藏录入发票按钮，以后将用付款报账功能所替代
			}
			//审批结束后才显示执行情况 
			if("作废"!=status){
				$("#btn-doingStatus").hide();
			}
			FW.fixToolbar("#toolbar1");
			//被终止的合同不显示录入发票和终止合同的按钮 -- 结束
			$("#autoform").ITC_Form("loaddata",loaddata);
			//之前在edit按钮中对表单的处理--开始 将初始化list和初始化表单的耦合解开
			var gridEditable = ""!=oper?JSON.parse(oper).editable:"";
			var createUser = $("#autoform").iForm("getVal","username");
			var curUser = Priv.secUser.userName;
			if(processStatus == "first" || processStatus == "first_save"&& createUser==curUser){
				//供应商输入框渲染
				edit_form[3].render = companySearch("f_companyName");
				//合同编号可编辑 
				$("#autoform").iForm("beginEdit","spNo");
				if(null != data.spNo){
					$("#autoform").iForm("setVal",{"spNo":data.spNo.replace("YD/ZSD-WS-","")});
				}
			}else if("editable"==isEdit&&gridEditable){
				edit_form[5].render = companySearch("f_companyName");
			}else{
				$("#autoform").ITC_Form("readonly");
				FW.showVendorInfo("companyNo","companyName");
			}
			if("deputy_general_manager" == process && "ITC" == siteId){
				$("#autoform").iForm("show",["purOrderIsGm"]);
				$("#autoform").iForm("beginEdit", "purOrderIsGm");
			}else{
				$("#autoform").iForm("hide",["purOrderIsGm"]);
			}
			//之前在edit按钮中对表单的处理--结束
			//表单初始化完成
			setTimeout(function(){ formInited = true; },0);
			initFormStatus = $("#autoform").iForm("getVal");
		}
	});
}