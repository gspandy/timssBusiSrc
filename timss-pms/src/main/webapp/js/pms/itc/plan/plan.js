/**
* 年度计划基本的js引用页面
 */
var typeId="type";//项目类型在业务的字段
var typeEnum="pms_project_type"; //项目类型在枚举表中的字段
var prefixId="#f_";//前端控件前缀
var planAttachMap=[];
var planFormFields=[
	    {id:"id",type:"hidden"},
	    {title : "项目名称", id : "planName", rules: {
                required: true
	        },
	        messages: {
	            required: "请输入项目名称"
	        }
        },
	    {title : "立项年度", id : "year",type : "combobox",value:getCurrentYear(),
	        data:yearFields, rules: {
                required: true
	        }
	    },
	    {
	        title : "项目类型", 
	        id :typeId,
	        type : "combobox",
	        dataType:'enum',
	        enumCat:'pms_project_type',
	        rules: {
                required: true
	        }
	    },
	//    {title : "项目进度", id : ""},
	    {title : "项目进度(%)", id : "planPercent",
	        rules: {
                required: true,
                number:true,
                range:[0,100]
	        }
	    },
	    {title : "项目收入(元)", id : "projectIncome",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {title : "项目成本(元)", id : "projectCost",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {title : "本年进度(%)", id : "annualPercent",
	        rules: {
                required: true,
                number:true,
                range:[0,100]
	        }
	    },
	    {title : "本年收入(元)", id : "annualIncome",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {title : "本年成本(元)", id : "annualCost",
	        rules: {
                required: true,
                number:true
	        }
	    },
	    {title : "项目利润(元)", id : "projectProfit",type:"label",value:"0",
	        rules: {
                required: true,
                number:true
	        }, linebreak:true
	    },
	    {title : "本年利润(元)", id : "annualProfit",type:"label",value:"0",
	        rules: {
                required: true,
                number:true
	        }
	       
	    },
	    {title : "项目负责人", id : "projectLeader",
	    	 type : "combobox",
	    	 options:{
	    		 url:basePath+"pms/group/queryUserListByGroupId.do?groupId=ITC_PMS_PL",
	    		 remoteLoadOn:"init"
	    	 },
	    	 rules: {
	                required: true
		        }
	    },
	    {title : "计划开始日期", id:"startTime",type:"date",
	        rules: {
                required: true
	        }
	    },
	    {title : "计划结束日期", id:"endTime",type:"date",
	        rules: {
                required: true,
                greaterEqualThan:"#f_startTime"
	        }
	    },
	    {title : "商务负责人", id : "customManager",
	    	 type : "combobox",
	    	
	    	 options:{ 
	    		 url:basePath+"pms/group/queryUserListByGroupId.do?groupId=ITC_PMS_BL",
	    		 remoteLoadOn:"init"
	    	 },
	    	 rules: {
	                required: true
		        }},
	    
	    {
	        title : "项目描述", 
	        id : "command",
	        type : "textarea",
	        linebreak:true,
	        wrapXsWidth:12,
	        wrapMdWidth:8,
	        height:48
	    }
];

var historyFormField=[
                      {id:"id",type:"hidden"},
		{
	        title : "累计进度(%)", 
	        id :"histPercent"
	    },
	    {
	    	title : "累计收入(元)", 
	        id :"histIncome"
	    },
	    {
	    	title : "累计成本(元)", 
	        id :"histCost"
	    },
	    {
	    	title : "累计利润(元)", 
	        id :"histProfit"
	    },
	    {
	    	title : "实际结束时间", 
	        id :"actualEndTime",
	        type:"date"
	    }
];

var attachFormFields=[
{
	title : " ",
	id : "planAttach",
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
		"initFiles" :planAttachMap,
		"delFileAfterPost" : true
		
		
     }
}
];
/**
 * 为表单的enum变量表格赋值
 */
function initEnumType(){
//	var searchEnum=typeEnum;
//	//从后台获取枚举变量
//	TimssService.getEnumParams(searchEnum, function(data){
//		//为表单枚举变量赋值
//		$(prefixId+typeId).iCombo('loadData',convertEnumToIComboType(data[typeEnum]));
//	});
}

//初始化附件表单
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
		data:data,
		$form:$form,
		$wrapper:$wrapper,
		attachMap:planAttachMap,
		attachFormFields:attachFormFields,
		readOnly:readOnly
	};
	initAttachFormTemplate(result);
}


/**
 * 计算$(result).val($(first).val()-$(first).val());
 * @param first
 * @param second
 * @param result
 */
function initProfitAutocalculate(first,second,result,resultField){
	var firstVal= first && $(first).val();
	var secondVal= second && $(second).val();
	var resultVal="";
	if(isNumber(firstVal) && isNumber(secondVal) ){
		firstVal=NumToFix2(firstVal);
		secondVal=NumToFix2(secondVal);
		resultVal=firstVal-secondVal;
		//$(result).html(NumToFix2(resultVal));
		setFormVal(resultField,resultVal);
	}
	$(first).val(NumToFix2(firstVal));
	$(second).val(NumToFix2(secondVal));
	
}

function setFormVal(field,value){
	var form = pmsPager.opt.form;
	var data={};
	data[field]=value;
	form.iForm('setVal',data);
}


function initProfit(){
	function initProjectProfit(){
		initProfitAutocalculate("#f_projectIncome","#f_projectCost","#f_projectProfit","projectProfit");
	}
	function initAnnualProfit(){
		initProfitAutocalculate("#f_annualIncome","#f_annualCost","#f_annualProfit","annualProfit");
	}
	$('#f_projectIncome').blur(initProjectProfit).trigger('blur');
	$('#f_projectCost').blur(initProjectProfit).trigger('blur');
	$('#f_annualIncome').blur(initAnnualProfit).trigger('blur');
	$('#f_annualCost').blur(initAnnualProfit).trigger('blur');
}