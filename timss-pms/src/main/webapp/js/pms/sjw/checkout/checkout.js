
var payplanIdData=[];
var checkoutAttachMap=[];
var checkoutFormFields=[
		 {id:"id",type:"hidden"},
		
		 {id:"contractId",type:"hidden"},
		 {title : "合同名称", id : "contractName",
			 rules: {
	         	required: true
		     }
		 },
		 {title : "所属项目", id : "projectName"},
		
		 {title : "项目负责人", id : "projectLeader"},
		 {title : "验收类型", id : "type",
		        type : "combobox",
		        dataType : "enum",
		        enumCat : "PMS_CHECKOUT_TYPE",
		        rules: {
	                required: true
		        }
		 },
		 {title : "结算阶段", id : "payplanId",type:"combobox",data:payplanIdData,
		        rules: {
	                required: true
		        }
		 },
		 {title : "项目合作方", id : "xmhzf"},
		 {title : "合作方负责人", id : "hzffzr"},
		 {title : "项目变更", id : "isProjectChange",type:"combobox",
			    data:[["Y","是"],["N","否"]],
		        rules: {
	                required: true
		        }
		 },
		 {title : "验收日期", id : "time",type:"date",dataTime:'date',
		        rules: {
	                required: true
		        }
		 },
		 {title : "开工日期", id : "startDate",type:"date",dataTime:'date',
		        rules: {
	                required: true
		        }
		 },
		 {title : "竣工日期", id : "endDate",type:"date",dataTime:'date',
		        rules: {
	               
		        }
		 },
		 {title : " 验收人", id : "checkUser",
			 rules: {
	                required: true
		        }
		 },
		 {title : "送总经理审批", id : "zjlsp",type:"combobox",
			 data:[["true","是",true],["false","否"]],
		        rules: {
	                required: true
		        }
		 },
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
		"initFiles" :checkoutAttachMap,
		"delFileAfterPost" : true
	}
}
];
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
//初始化附件
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
		data:data,
		$form:$form,
		$wrapper:$wrapper,
		attachMap:checkoutAttachMap,
		attachFormFields:attachFormFields,
		readOnly:readOnly
	};
	initAttachFormTemplate(result);
}

function setPmsPagerId(data){
	pmsPager.id=data.data.id;
}

function getPmsPagerId(){
	return pmsPager.id;
}