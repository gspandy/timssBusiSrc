
//页面生成帮助类
var pagerHelper={
	mapping:{},
	//获取jquery数据并使用缓存
	getJquery:function(str,reflesh){
		//如果不要求强制刷新，同时数据存在，就用缓存
		if(pagerHelper.mapping.str && !reflesh){
			return pagerHelper.mapping.str;
		}
		pagerHelper.mapping.str=$(str);
		return pagerHelper.mapping.str;
	},
	//处理整个表单数据
	initPager:function(pagerData){
		for(var i in pagerData){
			pagerHelper.generate(pagerData[i]);
		}
	},
	//处理单个数据，生成对应html dom
	generate:function(data){
		var type = data && data.type;
		if(type){
			pagerHelper._executeFunction(type,data,generateDomFunctions);
		}else{
			pagerHelper.log("数据的type字段不能为空");
		}
	},
	//根据类型处理数据
	_executeFunction:function(type,data,functions){
		if(functions && functions[type]){
			functions[type].call(this,data);
		}else{
			pagerHelper.log("找不到类型为"+type+"的处理函数");
		}
	},
	_executeFun:function(fun,message){
		if(fun){
			fun.call(this);
		}else{
			pagerHelper.log(message);
		}
	},
	//输出错误信息
	log:function(message){
		if(console && console.log){
			console.log(message);
		}
	}
};

var generateDomFunctions={
	pageTitle:function(data){
		var dom=pagerHelper.getJquery(data.id);
		dom.html(data.content);
	},
	form:function(data){
		
		var data = $.extend(true,data, pagerHelper._executeFun(data.specialFunction,"找不到specialFunction"));
		var $form=pagerHelper.getJquery(data.id);
		var formFields=data.formFields;
		var options=data.options;
		var formData=data.formData;
		var beginEdit=data.beginEdit;
		form.iForm('init',{"fields":formFields,options:options});
		form.iForm('setVal',formData);
		if(beginEdit){
			form.iForm('beginEdit',beginEdit);
		}
	},
	attachment:function(data){
		var $dom=pagerHelper.getJquery(data.id);
	}
};