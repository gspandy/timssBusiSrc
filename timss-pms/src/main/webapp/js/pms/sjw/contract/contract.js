var contractAttachMap=[];
var contractCodeFilter=/^([A-Z0-9\-_])+$/;//合同编号正则表达式
var contractFormFields=[
	    {id:"id",type:"hidden"},
	    {id:"projectId",type:"hidden"},
	    {id:"firstPartyId",type:"hidden"},
	    {id:"secondPartyId",type:"hidden"},
	    {id:"statusApp",type:"hidden"},
	    {id:"processInstId",type:"hidden"},
	    {id:"tipNo",type:"hidden"},
	    {title : "所属项目", id : "projectName"},
	    {title : "合同名称", id : "name",
	        rules: {
                required: true
	        }
	    },
	    {title : "合同编号", id : "contractCode",
			render : function(id){
				$("#"+id).on("change",function(){
					//SynRemoteValid是个同步验证，而且没有留有自定义回调函数的入口了。这里用onchange+settimeout(function(){},0)加以解决
					setTimeout(function(){
						if(undefined!=$("label[for='f_contractCode']").attr("title")){
							$.ajax({
								url : basePath + "pms/contract/generateNewContractCode.do",
						    	type:"POST",
						    	success:function(data){
						    		FW.error("合同编号已经存在，建议使用编号:"+data.newCode);
						    	},
						    	error:function(){
						    		FW.error("无法生成 合同编号");
						    	}
							});
						};	
					},0);
				});	
			},
	    	rules: {
                required: true,
                regex:contractCodeFilter,
                SynRemoteValid:{
                	url:basePath+"pms/contract/isContractCodeExisted.do",
                	type: "post",
                	data: {
                		contractCode: function() {
                			return $("#f_contractCode").val();
                		},
                		contractId:function(){
                			if(window.contractId){
                				return window.contractId;
                			}
                			
                			return "";
                		}
                	}
                }
	        },
            messages:{
            	SynRemoteValid:"合同编号已经存在"
            }
	    },
	    {
	        title : "合同类型", 
	        id :'type',
	        type : "combobox",
	        dataType:'enum',
	        enumCat:'PMS_CONTRACT_TYPE',
	        rules: {
                required: true
	        },
	        options:{
	        	initOnChange : null==contractId?true:false,
	        	onChange : function(val){
		        	var companyName=getCompany();
		        	var firstParty=companyName;
		        	var secondParty='';
		        	var form=pmsPager.opt.form;
		        	var editContractFirstInit  = pmsPager.editContractFirstInit;
		        	if(val=='income'){
			        	if(!editContractFirstInit){
			        		secondParty=companyName;
				        	firstParty='';
				        	var fpLeader = form.iForm("getVal","fpLeader");
				        	var fpPhone = form.iForm("getVal","fpPhone");
				        	if(val!=type){
				        		if(""==fpLeader&&""==fpPhone){
					        		form.iForm("setVal",{firstParty:"",fpLeader:"",fpPhone:"",spLeader:managerName,spPhone:managerTel,secondParty:companyName,secondPartyId:null});	
					        	}else{
					        		form.iForm("setVal",{firstParty:"",fpLeader:"",fpPhone:"",spLeader:fpLeader,spPhone:fpPhone,secondParty:companyName,secondPartyId:null});
					        	}
				        	}
			        	}
		        		form.iForm('endEdit',['secondParty']);
		        		form.iForm('beginEdit',['firstParty']);
		        		partyReadOnly();
		        		if(!editContractFirstInit){
		        			initRemoteFirstPartyData(form);
		        		}
		        	}else{
		        		if(!editContractFirstInit){
			        		var spLeader = form.iForm("getVal","spLeader");
				        	var spPhone = form.iForm("getVal","spPhone");
				        	if(val!=type){
				        		if(""==spLeader&&""==spPhone){
					        		form.iForm("setVal",{secondParty:"",fpLeader:managerName,fpPhone:managerTel,spLeader:"",spPhone:"",firstParty:companyName,firstPartyId:null});	
					        	}else{
					        		form.iForm("setVal",{secondParty:"",fpLeader:spLeader,fpPhone:spPhone,spLeader:"",spPhone:"",firstParty:companyName,firstPartyId:null});
					        	}
				        	}
		        		}
		        		form.iForm('endEdit',['firstParty']);
		        		form.iForm('beginEdit',['secondParty']);
		        		partyReadOnly();
		        		if(!editContractFirstInit){
		        			initRemoteSecondPartyData(form);
		        		}
		        	}
		        	pmsPager.editContractFirstInit = false;
		        	type = val;
		        }
	        }
	        
	    },
	    {title : "合同金额(元)", id : "totalSum",
	        rules: {
                required: true,
                number:true
	        },
	        render:function(id){
	        	$("#"+id).on("change",function(){
	        		calculateTax("","");
	        	})
	        }
	    },
	    {title : "签订时间", id:"signTime",type:"date",dataTime:'date',
	        rules: {
                required: true
	        }
	    },
	    {title : "质保天数", id:"qaTime",
	        rules: {
                number: true
	        },
	        messages: {
	        	number: "请输入数字"
	        }
	    },
	    {title : "税率(%)", id:"taxRate",type:"combobox",
	    	data : [
	            ["3","3%"],
	            ["4","4%"],
	            ["6","6%"],
	            ["11","11%"],
	            ["13","13%"],
	            ["17","17%"]
	        ],
	        options:{
	        	allowEmpty:true
	        },
	        rules: {
                required: true
	        }
	    },
	    
	    
	//    {title : "项目进度", id : ""},
	   
	    {title : "合同甲方", id : "firstParty",
	        rules: {
                required: true
	        }
	    },
	    {title : "甲方负责人", id : "fpLeader"
	    },
	    {title : "甲联系方式", id : "fpPhone"},
	    {title : "合同乙方", id : "secondParty",
	        rules: {
                required: true
	        }
	    },
	    {title : "乙方负责人", id : "spLeader"},
	    {title : "乙联系方式", id : "spPhone"},
	    
	    {
	        title : "合同描述", 
	        id : "command",
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
		"initFiles" :contractAttachMap,
		"delFileAfterPost" : true
	}
}
];

var payplanListColumns=[ [
              			/* {field:'ck',checkbox:true}, */
            			{
            				field : 'id',
            				hidden : true
            			}, {
            				field : 'ischecked',
            				hidden : true
            			}, {
            				field : 'ispayed',
            				hidden : true
            			}, {
            				field : 'payType',
            				title : '结算类型',
            				width : 120,
            				align : 'left',
            				fixed:true,
            				editor:{ 
            					type : 'combobox',

            					options : {
            						data : [ ['yfk',"预付款",true],['jdk',"进度款"],['jgk',"竣工款"],['zbj',"质保金"]]
            						//data:FW.parseEnumData("PMS_PAYPLAN_STAGE")
            					}
            				},
            				formatter: function(value,row,index){
             					return getPayType(value);
             				}
            			}, {
            				field : 'paySum',
            				title : '结算金额(元)',
            				width : 105,
            				align : 'right',
            				fixed : true,
            				editor:{
            					type:'text',
            					
            					options:{
            						rules:{
                						required:true,
                						number:true
                					},
            						onKeyUp : function(){
            							var me = $(this);
            							calculateTax(me,"paySum");
            						}
            					}
            					
            				}
            			}, {
            				field : 'percent',
            				title : '结算比例(%)',
            				width : 90,
            				align : 'right',
            				fixed : true,
            				editor:{
            					type:'text',
            					options:{
            						rules:{
                						required:true,
                						number:true
                					},
            						onKeyUp : function(){
            							var me = $(this);
            							calculateTax(me,"paycent");
            						}
                					
            					}
            				}
            					
            			}, {
            				field : 'needchecked',
            				title : '是否需要验收',
            				width : 120,
            				align : 'left',
            				fixed : true,
            				editor:{ 
            					type : 'combobox',
            					options : {
            						data : [ [true,"是"],[false,"否"]]
            					}
            				},
            				formatter: function(value,row,index){
             					if("true"==value ||true==value ||''==value||null==value||undefined==value){
             						return '是';
             					}else{
             						return '否';
             					}
             				}
            			}, {
            				field : 'checkStatus',
            				title : '验收状态',
            				width : 85,
            				align : 'left',
            				fixed:true,
            				formatter: function(value,row,index){
            					if(row["needchecked"]==false){
            						return '不需验收';
            					}
             					if(!value ){
             						return '未验收';
             					}else if(value=="approved"){
             						return '已验收';
             					}else if(value=="approving"){
             						return '验收中';
             					}
             					return value;
             				}
            			}, {
            				field : 'payStatus',
            				title : '结算状态',
            				width : 85,
            				align : 'left',
            				fixed:true,
            				formatter: function(value,row,index){
             					if(!value ){
             						return '未结算';
             					}else if(value=="approved"){
             						return '已结算';
             					}else if(value=="approving"){
             						return '结算中';
             					}
             					return value;
             				}
            			}, {
            				field : ' ',
            				title : '',
            				width : 85,
            				align : 'left'
            			},garbageColunms
            			
            			] ];
//初始化附件表单
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
			data:data,
			$form:$form,
			$wrapper:$wrapper,
			attachMap:contractAttachMap,
			attachFormFields:attachFormFields,
			readOnly:readOnly
		};
	initAttachFormTemplate(result);
}

//根据站点获取当前站点对应的公司名称
function getCompany(){
	return '沙C多经公司';
}
//根据金额。自动计算比例
function calculateTax(me,from){
	var totalSumVal=$("#f_totalSum").val &&  $("#f_totalSum").val() ;
	if(null==totalSumVal&&pmsPager.opt&&pmsPager.opt.data&&pmsPager.opt.data.data){
		totalSumVal = pmsPager.opt.data.data.totalSum;
	}
	if(""!=me){
		var p = me.parents(".datagrid-row");
		var paySum = p.children("td[field='paySum']").find("input");
		var percent = p.children("td[field='percent']").find("input");
		if("paycent"==from){
			//结算比例修改
			if(isNumber(percent.val()) && isNumber(totalSumVal)){
				var paySumVal=percent.val()*totalSumVal/100;
				paySum.val(NumToFix2(paySumVal));
			}
		}else{
			//结算金额修改
			if(isNumber(paySum.val()) && isNumber(totalSumVal)){
				var percentVal=paySum.val()/totalSumVal*100;
				percent.val(NumToFix2(percentVal));
			}
		}	
	}else{
		//合同金额修改时，修改结算金额
		var payPlanList = $("#payplanList").datagrid("getRows");
		for(var i = 0 ;i < payPlanList.length; i++){
			var paySum = $($("input[name^='paySum']")[i]);
			var percent = $($("input[name^='percent']")[i]);
			if(isNumber(paySum.val()) && isNumber(totalSumVal)){
				var percentVal=paySum.val()/totalSumVal*100;
				percent.val(NumToFix2(percentVal));
			}else if(isNumber(percent.val()) && isNumber(totalSumVal)){
				var paySumVal=percent.val()*totalSumVal/100;
				paySum.val(NumToFix2(paySumVal));
			}
		}
	}
}

function initPayplanWrapper(){
	if(!initPayplanWrapper.init){
		$('#payplanListWrapper').iFold();
		initPayplanWrapper.init=true;
	}
	
}
function initPayplansDataGrid(data){
	if(!dataGrid){
		dataGrid=$('#payplanList');
		dataGrid.datagrid({
			fitColumns:true,
			singleSelect:true,
			scrollbarSize:0,
			pageSize : pageSize,// pageSize为全局变量，自动获取的
			onDblClickRow : function(rowIndex, rowData) {
				if(pmsPager.opt.readOnly==true){
					return;
				}
				//dataGrid.datagrid('beginEdit',rowIndex);
			},
			data:data,
			onClickCell:delelteGarbageColumnWhenClick,
			columns : payplanListColumns
		});
	}
}
//对数据进行验证
function valid(){
	if(!$("#form1").valid()){
		return false;
	}
	if(!$("#payplanListWrapper").valid()){
		return false;
	}
	var rows=dataGrid.datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		dataGrid.datagrid('endEdit',i);
	}
	var payplanData=dataGrid.datagrid('getRows');
	var result=0;
	for(var i=0;i<payplanData.length;i++){
		result+=parseFloat(payplanData[i].paySum);
	}
	var totolSum=$("#f_totalSum").val &&  $("#f_totalSum").val() || pmsPager.opt.data.data.totalSum;
	if(result!=totolSum){
		for(var i=0;i<rows.length;i++){
			dataGrid.datagrid('beginEdit',i);
		}
		FW.error('合同金额与结算计划的金额总数不一致');
		return false;
	}
	
	return true;
}

function getPayplanListData(dataGrid){
	var payplanData=null;
	if(dataGrid){
		var rows=dataGrid.datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			dataGrid.datagrid('endEdit',i);
			
		}
		payplanData=dataGrid.datagrid('getRows');
	}
	return payplanData;
}
function hideColumn(){
	hideDataGridColumns(dataGrid,['payStatus','checkStatus']);
}

function addPayplan(){
	if(!addPayplan.init){
		addPayplan.init=true;
		initPayplansDataGrid();
		
		var row={"needchecked":true};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
		
	}else{
		var row={"needchecked":true};
		dataGrid.datagrid('appendRow',row);
		var rowindex=dataGrid.datagrid('getRowIndex',row);
		dataGrid.datagrid('beginEdit',rowindex);
	}
	$('#b-add-payplan').html('继续添加结算计划');
}

//项目编码事件添加
function addContractCodeEvent(){
//	$("#f_contractCode").keydown(function(e){
//		var _this=this;
//		var code=e.which;
//		var keyValue=String.fromCharCode(code);
//		var regex=/[a-z]/;
//		if(regex.test(keyValue)){
//			_this.value=_this.value+keyValue.toUpperCase(); 
//			return false;
//		}
//		if(code==100 || code==13){
//			_this.value=_this.value.toUpperCase();
//			
//		}
//		
//	}).keyup(function(e){
//		var _this=this;
//		_this.value=_this.value.toUpperCase(); 
//	});
}

function initRemoteFirstPartyData(form){
	var $firstPartyInput=$('#f_firstParty');
	var $firstPartyIdInput = $('#f_firstPartyId');
	var firstPartyInit = {
		datasource : basePath + "pms/supplier/queryFuzzyByName.do",
		clickEvent : function(id, name) {
			initFirstPartyDataBySupplierId(form,id)
		}
	};
	
	$firstPartyInput.iHint('init', firstPartyInit);
}

function initFirstPartyDataBySupplierId(form,id){
	$.post(basePath+'pms/supplier/querySupplierById.do',{id:id},function(result){
		result=result.data;

		var value={
			firstParty:result['name'],
			firstPartyId:result['companyNo'],
			fpLeader:result['contact'],
			fpPhone:result["tel"]
		};
		form.iForm("setVal", value);
		
	});
}

function initRemoteSecondPartyData(form){
	//招标相关
	var $secondPartyInput=$('#f_secondParty');
	
	var $secondPartyIdInput = $('#f_secondPartyId');
	var secondPartyInit = {
		datasource : basePath + "pms/supplier/queryFuzzyByName.do",
		clickEvent : function(id, name) {
			
			initSecondPartyDataBySupplierId(form,id);
		}
	};
	
	$secondPartyInput.iHint('init', secondPartyInit);
}

function initSecondPartyDataBySupplierId(form,id){
	$.post(basePath+'pms/supplier/querySupplierById.do',{id:id},function(result){
		result=result.data;

		var value={
			secondParty:result['name'],
			secondPartyId:result['companyNo'],
			spLeader:result['contact'],
			spPhone:result["tel"]
		};
		form.iForm("setVal", value);
		
	});
}


function partyReadOnly(){
	//根据合同类别设置表单可编辑状态,common.js 中的initForm中readOnly为true时，modifiable beginEdit:all会把所有的表单项设为可编辑
	var data = pmsPager.opt.data;
	if(null!=data){
		var elementMap = data.pri.workflowSec.elements;
		var form = pmsPager.opt.form;
		if(elementMap&&undefined != elementMap.modifiable){
			modifiable=elementMap.modifiable;
			if(-1 == JSON.parse(modifiable)["beginEdit"].split(",").indexOf("firstParty")
					&&-1 == JSON.parse(modifiable)["beginEdit"].split(",").indexOf("all")){
				form.iForm('endEdit',['firstParty']);
			}
			if(-1 == JSON.parse(modifiable)["beginEdit"].split(",").indexOf("secondParty")
					&&-1 == JSON.parse(modifiable)["beginEdit"].split(",").indexOf("all")){
				form.iForm('endEdit',['secondParty']);
			}
		}else{
			if(null!=contractId&&null==flowId&&true!= data.pri.readOnly){
				//草稿，保存了业务数据但还没有流程数据的时候
				var type = $("#form1").iForm("getVal","type");
				if("cost"==type){
					form.iForm('endEdit',['firstParty']);
					initRemoteSecondPartyData(form);
				}else if("income"==type){
					form.iForm('endEdit',['secondParty']);
					initRemoteFirstPartyData(form);
				}
			}else{
				form.iForm('endEdit',['firstParty']);
				form.iForm('endEdit',['secondParty']);
			}
		}
	}
}