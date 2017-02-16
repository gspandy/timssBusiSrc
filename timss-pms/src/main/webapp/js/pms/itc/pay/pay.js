
var payplanIdData=[];
var payAttachMap=[];
var payFormFields=[
           	    {id:"id",type:"hidden"},
           	    {id:"undoFlowId",type:"hidden"},
           	    {id:"contractId",type:"hidden"},
           	    {id:"projectId",type:"hidden"},
           	    {title : "合同名称", id : "contractName"},
           	    {title : "合同金额(元)", id : "totalSum"},
        	    {title : "合同编码", id : "contractCode"},
        	    {title : "合同类型", id : "type",
        	    	type:"combobox",
        	    	dataType:'enum',
        		    enumCat:'PMS_CONTRACT_TYPE'
        		},
        		{title : "所属项目", id : "projectName"},
        	    {title : payOrReceipt()+"方式", id : "payway",
        	    	type:"combobox",
        	    	dataType:'enum',
        		    enumCat:'PMS_PAY_PAYWAY',
        		    value:"transfer"
        		    
        		},
        	    {   title : "结算阶段", id : "payplanId",type:"combobox",data:payplanIdData,
        		    rules:{
        		    	required:true
        		    },
        		    options:{
        		    	onChange:function(val){
        	        		if(getReadOnly()){
        	        			return;
        	        		}
        		    		var prepareData=pmsPager.opt.otherData&&pmsPager.opt.otherData.data &&pmsPager.opt.otherData.data.payplanVos;
        		    		var actualpay=pmsPager.opt.data&&pmsPager.opt.data.data &&pmsPager.opt.data.data.actualpay;
        		    		var payplanData=pmsPager.otherData||prepareData;
        		    		//var payplanData=payplanIdData;
        		    		var currentPay=null;
        		    		for(var i in payplanData){
        		    			if(payplanData[i].id==val){
        		    				currentPay=payplanData[i].paySum;
        		    				break;
        		    			}
        		    		}
        		    		$('#f_actualpay').val(currentPay);
        		    		if(null==currentPay){
        		    			$('#f_actualpay').val(actualpay);
        		    		}
        		    		$('#f_actualpay').trigger('blur');
        		    		
        		    	}
        		    }
        		},
        	    {title : "累计已"+payOrCheck(), id : "bepay" ,
        			rules:{
	    		    	required:true,
	    		    	number:true
	    		    },
	    		    messages:{
	    	    		number:"请输入数字"
	    		    }
    		    },
        	    {title : "已"+payOrCheck()+"比例(%)", id : "bepayPercent",
        	    	rules:{
        	    		number:true,
        	    		max:100,
        	    		min:0
        	    	},
        	    	messages:{
        	    		number:"请输入数字",
        	    		max:"不得大于100",
        	    		min:"不得小于0"
        	    	}
        	    },
        	    {title : "本次"+payOrCheck(), id : "actualpay",
        	    	 rules:{
         		    	required:true,
         		    	number:true
         		    },
         		    messages:{
         	    		number:"请输入数字"
         		    }
        	    },
        	    {title : payOrCheck()+"比例(%)", id : "actualpayPercent",
        	    	rules:{
        	    		number:true,
        	    		max:100,
        	    		min:0
        	    	},
        	    	messages:{
        	    		number:"请输入数字",
        	    		max:"不得大于100",
        	    		min:"不得小于0"
        	    	}
        	    },
        	    {title : "结算日期", id : "payDate",type:"date" ,dataType : "date"},
        	    {
        	        title : "合作方", 
        	        id :'xmhzf'
        	    },
        	    {
        	        title : "备注", 
        	        id : "fcondition",
        	        type : "textarea",
        	        linebreak:true,
        	        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:48
        	    },
        	    {
        	        title : "退票备注", 
        	        id : "undoRemark",
        	        type : "textarea",
        	        linebreak:true,
        	        wrapXsWidth:12,
			        wrapMdWidth:8,
			        height:48
        	    }
        ];
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
		"swf" : basePath+"js/uploadify.swf",
		"fileSizeLimit" : 10 * 1024,
		"initFiles" :payAttachMap,
		"delFileAfterPost" : true
	}
}
];

//收入合同显示收款
function payOrReceipt(){
	if(window.ctype=="income"){
		return "收款";
	}else{
		return "付款";
	}
}

//收入合同显示已开票
function payOrCheck(){
	if(window.ctype=="income"){
		return "开票";
	}else{
		return "付款";
	}
}
//后台传来的payplan数据转为combobox可识别的数据
function getPayplanData(originData){
	var res=[];
	for(var i in originData){
		var r=[];
		r.push(originData[i].id);
		r.push(originData[i].typeName);
		res.push(r);
	}
	return res;
}

function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
		data:data,
		$form:$form,
		$wrapper:$wrapper,
		attachMap:payAttachMap,
		attachFormFields:attachFormFields,
		readOnly:readOnly
	};
	initAttachFormTemplate(result);
}

function calculatePercent(pay,payPercent){
	var payValue=$(pay).val();
	if(payValue!=null && payValue!=""){
		var totalSum = pmsPager.totalSum||pmsPager.opt.data.data.totalSum;
		var percent=$(pay).val() / totalSum *100;
		$(payPercent).val( NumToFix2(percent) );
	}
}
function calculatePay(pay,payPercent){
	var payPercentValue=$(payPercent).val();
	if(payPercentValue!=null && payPercentValue!=""){
		var totalSum = pmsPager.totalSum||pmsPager.opt.data.data.totalSum;
		var payVal = payPercentValue * totalSum /100;
		$(pay).val( NumToFix2(payVal) );
	}
}
function initEvent(){
	$('#f_bepay').blur(function(){
		calculatePercent("#f_bepay","#f_bepayPercent");
	}).trigger('blur');
	$('#f_actualpay').blur(function(){
		calculatePercent("#f_actualpay","#f_actualpayPercent");
	}).trigger('blur');
	$('#f_bepayPercent').blur(function(){
		calculatePay("#f_bepay","#f_bepayPercent");
	}).trigger('blur');
	$('#f_actualpayPercent').blur(function(){
		calculatePay("#f_actualpay","#f_actualpayPercent");
	}).trigger('blur');
}

function calculateTax(me){
	var p = me.parents(".datagrid-row");
	var rate = p.children("td[field='rate']").find("input");
	var sum=p.children("td[field='sum']").find("input");
	
	if(isNumber(rate.val()) && isNumber(sum.val())){
		var tax=p.children("td[field='tax']").find("input");
		var withoutTax=p.children("td[field='withoutTax']").find("input");
		var withoutTaxVal=sum.val() /(1+new Number(rate.val())/100);
		var taxVal=sum.val()-withoutTaxVal;
		tax.val(NumToFix2(taxVal));
		withoutTax.val(NumToFix2(withoutTaxVal));
//		sum.val(NumToFix2(sum.val()));
//		rate.val(NumToFix2(rate.val()));
	}
}

function initInvoice(data){
	if(!initInvoice.init){
		initInvoice.init=true;
		window.dataGrid=$('#invoiceList').datagrid({
			fitColumns:true,
			singleSelect:true,
			scrollbarSize:0,
			pageSize : pageSize,// pageSize为全局变量，自动获取的
			onDblClickRow : function(rowIndex, rowData) {
				if(pmsPager.opt.readOnly==true){
					return;
				}
				dataGrid.datagrid('beginEdit',rowIndex);
			},
			data:data,
			onClickCell:delelteGarbageColumnWhenClick,
			columns:[[
				{
					field : 'id',
					hidden : true
				}, {
					field : 'contractId',
					hidden : true
				}, {
					field : 'payId',
					hidden : true
				}, {
					field : 'payplanId',
					hidden : true
				}, {
					field : 'invoiceCode',
					title : '发票代码',
					width : 110,
					align : 'left',
					fixed:true,
					editor:{ 
						type : 'text',
						options : {
							rules : {
								required : true
							}
						}
					}
				}, {
					field : 'code',
					title : '发票号',
					
					align : 'left',
					width : 110,
					editor:{ 
						type : 'text',
						options : {
							rules : {
								required : true
							}
						}
					}
				},
				{
					field : 'sum',
					title : '发票金额(元)',
					width : 105,
					align : 'right',
					fixed:true,
					editor:{ 
						type : 'text',
						options : {
							onKeyUp : function(){
								var me = $(this);
								calculateTax(me);
								
							}
						}
					}
				},
				{
					field : 'rate',
					title : '发票税率(%)',
					width : 90,
					align : 'right',
					fixed:true,
					editor:{ 
						type : 'text',
						options : {
							onKeyUp : function(){
								var me = $(this);
								calculateTax(me);
								
							}
						}
					}
				},
				{
					field : 'withoutTax',
					title : '不含税金额(元)',
					width : 105,
					align : 'right',
					fixed:true,
					editor:{ 
						type : 'text',
						options : {
							rules : {
								required : true
							}
						}
					}
				},
				{
					field : 'tax',
					title : '税额(元)',
					width : 105,
					align : 'right',
					fixed:true,
					editor:{ 
						type : 'text',
						options : {
							
							
						}
					}
				},
				{
					field : 'invoiceDate',
					title : '开票日期',
					width : 105,
					align : 'left',
					fixed:true,
					editor:{ 
						type : 'datebox',
						options : {
							minView:2,
							dataType:"date",
							format:"yyyy-mm-dd",
							rules : {
								required : true
							}
						}
					},
					formatter: function(value,row,index){
	 					//时间转date的string，还有long2date(value)方法
	 					return FW.long2date(value);
	 				}
				},
				{
					field : 'command',
					title : '备注',
					width : 90,
					align : 'left',
					editor:{ 
						type : 'text'
					}
				},garbageColunms
			]]
		});
	}
}

function openContractPage(contractId){
	openTab(basePath+"pms/contract/editContractJsp.do?contractId="+contractId,'合同','pmsViewContractTab'+contractId);

}