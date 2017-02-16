
var tmpSaveSuccessMessage="保存成功";
var tmpSaveFailMessage="保存失败";
var saveSuccessMessage="提交成功";
var saveFailMessage="提交成功";
var saveAndSubmitNextUserSuccessMessage="提交成功";

var delSuccessMessage="删除成功";
var delFailMessage="删除失败";

var queryProjectLeaderUrl="pms/group/queryUserListByGroupId.do?groupId=ITC_PMS_PL";
var queryBusinessLeaderUrl="pms/group/queryUserListByGroupId.do?groupId=ITC_PMS_BL";
//可以打开多个窗口参数
var canOpenMutiTab={
	canOpenMutiTab:true
};
var openMutiTabAndBackReflesh={
	canOpenMutiTab:true,
	refleshFunction:"pmsReload"
};
//打开页面后，关闭时，刷新
var openTabBackReflesh={
		
		refleshFunction:"pmsReload"
	};
/**
 * 获取当前年份
 * @returns
 */
function getCurrentYear(){
	var date=new Date();
	var year=date.getFullYear();
	return year;
}

/**
 * 获取combobox需要的年份数据
 */
function getYearFields(){

	var year=getCurrentYear();
	var before=2,after=5;
	var res=[];
	for(var i=year-before;i<year+after;i++){
		var record=[i,i];
		res.push(record);
	}
	return res;
}
var yearFields=getYearFields();
var garbageColunms={
	field : 'garbage-colunms',
	title : '',
	width : 30,
	align : 'center',
	fixed:true,
    formatter:function(value,row,index){
	     return '<img src="'+basePath+'img/finance/btn_garbage.gif" title="删除当前行" width="16" height="16" >';
	}

};
var delelteGarbageColumnWhenClick=function(rowIndex, field, value){
	if(field=='garbage-colunms'){
		
		deleteGridRow(dataGrid,rowIndex);
	}
};

var delelteGarbageColumnWhenPermit=function(rowIndex, field, value,dataGrid){
    if(field=='garbage-colunms'){
		var rowData=dataGrid.datagrid('getRows')[rowIndex];
		if(rowData["forbidDelete"]){
			FW.error(rowData['forbidDeleteReason']);
		}else{
			deleteGridRow(dataGrid,rowIndex);
		}
		
	}
}

function deleteGarbageColumnFunction(rowIndex, field, value,id,grid){
	if(field=='garbage-colunms'){
		var key=grid.datagrid('getRows')[rowIndex][id];
		deleteGridRow(grid,rowIndex,key);
	}
};
/**
 * 初始化pmsPager变量，该变量用于初始化页面。通过pmsPager.init(options)进行
 */
var pmsPager={
    //详情页面初始化
	init:function(options){
		var opt=$.extend({},pmsPager.options,options);
		pmsPager.opt=opt;
		//模板方式初始化页面,先初始化权限，后按钮，在表单，其他信息初始化，最后是页面事件初始化
		this.excuteFunction(opt.initPrivilege, opt);
		this.excuteFunction(opt.initButton, opt);
		this.excuteFunction(opt.initForm, opt);
		this.excuteFunction(opt.initOther, opt); //一般只需重新改函数
		this.excuteFunction(opt.initPagerEvent, opt);
		
	},
	//列表页面初始化权限信息
	initListPager:function(options){
		var opt=options;
		this.excuteFunction("initPageWithPrivilege", opt);
	},
	//执行函数，可以重载重载功能
	excuteFunction:function(func,opt){
		if(typeof func == 'function'){
			func.call(this,opt);
		}else if(typeof func== "string" && typeof window[func] =='function'){
			window[func].call(this,opt);
		}else if(!func){
			
		}
	},
    //获取流程节点附带信息
	getWorkflowValue:function(priName){
		if(pmsPager && pmsPager.opt && pmsPager.opt.workflow && pmsPager.opt.workflow[priName]){
			return pmsPager.opt.workflow[priName];
		}
		var pri=pmsPager.opt;
		var modified=pri && pri.workflow && pri.workflow.elements && pri.workflow.elements.modifiable;
		if(modified){
			var modifiedJson=JSON.parse(modified);
			pri=modifiedJson;
			if(pri && pri[priName]){
				return pri[priName];
			}
		}
		return null;
	},
	
	//初始化表单是否只读变量
	initReadOnlyPri:function(opt){
		var data=opt.data;
		opt.readOnly=false;
		
		if(data){
			var pri=opt.data.pri;
			if(pri && pri.readOnly){
				opt.readOnly=pri.readOnly;
			}
		}
		
	},
	hasPriInOpt:function(priName){
		return pmsPager.opt[priName]==true;
	},
	//是否具有某个权限,
	hasPri:function(opt,priName){
		if(opt){
			//权限信息自带
			var pri=opt.data.pri;
			if(pri && pri[priName]){
				return true;
			}else {
				pri=opt.pri;
				if(pri && pri[priName]){
					return true;
				}
			}
			pri=opt.data.pri;
			var modified=pri && pri.workflow && pri.workflow.elements && pri.workflow.elements.modifiable;
			if(modified){
				var modifiedJson=JSON.parse(modified);
				pri=modifiedJson.pri;
				if(pri && pri[priName]){
					return true;
				}
			}
		}
	},
	options:{
		initPrivilege:"initPrivilege", //初始化权限信息,必须用string类型以实现函数的覆盖调用
		initButton:"initButton", //初始化按钮
		initForm:"initForm",  //初始化表单
		initOther:"initOther", //初始化其他信息
		initPagerEvent:"initPagerEvent",//初始化页面事件
		data:null,
		privilege:null
	}
};
//页面权限信息初始化
function initPrivilege(opt){
	
	pmsPager.initReadOnlyPri(opt);
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflow ){
		opt.workflow=pmsPager.opt.data.pri.workflow;
		pmsPager.createTime=pmsPager.opt.data.data.createTime;
	}
}
//默认按钮初始化
function initButton(opt){
	if(opt.data){
		var pri=opt.data.pri;
		if(pri){
			//由业务控制的按钮
			var buttons=$(".pms-button");
			for(var i=0;i<buttons.length;i++){
				var id=buttons[i].id;
				if(id ){
				   if(pri[id]!=true){
					   $("#"+id).hide().parent().hide();
					   
				   }
				}
			}
			//业务和权限共同控制的按钮
			var complexButton=$(".pms-complex-privilege");
			for(var i=0;i<complexButton.length;i++){
				var id=complexButton[i].id;
				if(id ){
				   if(pri[id]!=true){
					   $("#"+id).hide();
				   }
				}
			}
			if(pri.workflow && pri.workflow.approve==true){
				$('#b-approve').show();
			}
		}
	}
	if(opt.readOnly==false){
		$(".pms-readOnly").hide();
	}else{
		$(".pms-editOnly").hide();
	}
	Priv.apply();
	FW.fixToolbar("#toolbar1");
}
function getReadOnly(){
	if(pmsPager && pmsPager.opt && pmsPager.opt.readOnly==true){
		return true;
	}
	return false;
}
//默认表单初始化
function initForm(opt){
	var data=opt.data;
	var form=$(opt.form);
	var formFields=opt.formFields;
	form.iForm("init",{"fields":formFields,options:{validate:true,initAsReadonly:getReadOnly()}});
	if(data && data.data){
		form.iForm('setVal',data.data);
	}
	if(opt.readOnly){
		//form.iForm('endEdit');
		var modifiable=null;
		var workflow=getWorkflowData(opt.data,opt.flowStatus);
		if(workflow ){
			modifiable=workflow.elements &&workflow.elements.modifiable;
		}
		initFormByModifiable(modifiable,form,opt);
	}
	Priv.apply();
	FW.fixToolbar("#toolbar1");
}
//其他信息初始化，需要重载
function initOther(opt){
	
}

//页面事件初始化
function initPagerEvent(opt){
	var evtMap = _parent()._ITC.navTab.getEventMap();
    var tabId = FW.getCurrentTabId();
    //编辑后未保存提示框处理
    if(getReadOnly()==false ){
    	//延迟两秒初始化，保证两秒后再初始化
    	setTimeout(function(){
    		pmsPager.initFormData=opt.form.iForm('getVal');
    		pmsPager.initFormDataStart=true;
    	},3000);
    	pmsPager.canTipAfterCloseTab=true;
    	
    	evtMap[tabId].beforeClose = function(){
    		var isFormChanged=pmsPager.canTipAfterCloseTab && pmsPager.initFormDataStart && FW.stringify(pmsPager.initFormData)!=FW.stringify(opt.form.iForm('getVal'));
    		if(isFormChanged){
    			 FW.confirm("确定关闭|未保存的内容将丢失，是否继续？",function(){
    	                delete(evtMap[tabId].beforeClose);
    	                FW.deleteTabById(tabId);
    	            });
    	         return false;
    		}else{
    			return true;
    		}
           
        };
    }
    
}
var pmsUtil={
	data:{},
	add:function(key){
		pmsUtil.data[key]=true;
	},
	remove:function(key){
		delete pmsUtil.data[key];
	},
	isExisted:function(key){
		return pmsUtil.data[key];
	}
};


function forbidTipAfterCloseTab(){
	pmsPager.canTipAfterCloseTab=false;
}
function deleteGridRow(dataGrid,index,id){
	dataGrid.datagrid('deleteRow',index);
	if(id){
		pmsUtil.remove(id);
	}
}

function getDataGridRowData(dataGrid){
	var result=null;
	if(dataGrid){
		var rows=dataGrid.datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			dataGrid.datagrid('endEdit',i);
		}
		result=dataGrid.datagrid('getRows');
	}
	return result;
}

/**
 * 隐藏datagrid columns
 * @param dataGrid datagrid
 * @param fields 要隐藏的列数组
 */
function hideDataGridColumns(dataGrid,fields){
	if(fields && fields.length && dataGrid){
		for(var i=0;i<fields.length;i++){
			dataGrid.datagrid('hideColumn',fields[i]);
		}
	}
}

function showDataGridColumns(dataGrid,fields){
	if(fields && fields.length && dataGrid){
		for(var i=0;i<fields.length;i++){
			dataGrid.datagrid('showColumn',fields[i]);
		}
	}
}
/**
 * 查询url参数值
 * @param name url参数名称
 * @returns
 */
function getUrlParam(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return decodeURIComponent(r[2]); 
	    return null;
}

/**
 * 刷新dataGrid的值
 * @param dataGrid
 */
function refreshDataGrid(dataGrid){
	delete(_itc_grids[dataGrid[0].id]);

}

/**
 * 将后台得到的data数据格式转换为ui组件能够识别的格式
 * @param data 从后台得到的数据
 */
function convertEnumToIComboType(data){
	var result=[];
	var res,crr;
	for(var i in data){
		res=[];
		crr=data[i];
		res.push(crr['code']);
		res.push(crr['label']);
        result.push(res);
	}
	return result;
}

/**
 * 获取后台信息，展示到前台
 * @param result
 * @param successMessage 
 * @param errorMessage
 * @param options 成功时添加的参数
 */
function showBasicMessageFromServer(result,successMessage,errorMessage,options){
	options=options || {};
	var resetId=options.resetId;
	if(result && result.flag=="success"){
		FW.success(successMessage ||result.msg );
		forbidTipAfterCloseTab();
		if(options.successFunction){
			
			options.successFunction.call(this,result,options);
		}
		
		//如何tabOpen存在，则表示不用关闭当前页面
		if(!options.tabOpen){
			
			closeTab();
		}
		
	}else{
		FW.error(result.msg || errorMessage);
		if(resetId){
			buttonReset(resetId);
		}
	}
}


function successWorkflowShow(result){
	//将流程信息赋值到全局变量
	taskId=result['data']['taskId'];
	//打开对话框
	showAudit();
}

/**
 * 关闭当前tab页
 */
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}

/**
 * 新建tab页
 * @param url tab页url
 * @param name tab页名称
 * @param tabIdPrefix tab页id前缀
 */
function openTab(url,name,tabIdPrefix,params){
	var currTabId = FW.getCurrentTabId();
	var newTab=tabIdPrefix || currTabId;
	var rand = "";
	var reflesh="";
	var currentId=FW.getCurrentTabId();
	if(params && params.canOpenMutiTab){
		rand=new Date().getTime() + Math.floor(Math.abs(Math.random() * 100));
	}
	if(params && params.refleshFunction){
		var refleshFunction=params.refleshFunction;
		reflesh="var frame = FW.getFrame('"+currTabId+"');if(frame."+refleshFunction+"){frame."+refleshFunction+"();}";
	}
	var opts = {
		id : newTab + rand,
		name : name,
		url : url,
		tabOpt : {
			closeable : true,
			afterClose :"FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "|pms');"+reflesh /*
			    var frame = FW.getFrame(FW.getCurrentTabId());if(frame.refresh){frame.refresh();}
			    function(id) {
				FW.deleteTab(id);
				
				//切换到某个选项卡 假设该选项卡从ID为proj的选项卡打开 则还应该在关闭后跳回去
				FW.activeTabById(currTabId);
				var frame = FW.getFrame(FW.getCurrentTabId());if(frame.refresh){frame.refresh();}
			}*/
		}
	};
	_parent()._ITC.addTabWithTree(opts); 
}

/**
 * 对表单的样式进行初始化，如果modifiable为空，表单为不可编辑，
 * @param modifiable 变量
 * @param form 表单
 */
function initFormByModifiable(modifiable,form,opt){
	if(modifiable && form){
		var res=JSON.parse(modifiable);
		//表单的只读与编辑状态转换
		if(res.endEdit){
			if(res.endEdit=='none'){
				
			}else{
				form.iForm('endEdit',res.endEdit);
			}
		}else{
			form.iForm('endEdit');
		}
		if(res.beginEdit){
			if(res.beginEdit=='all'){
				form.iForm('beginEdit');
			}else{
				form.iForm('beginEdit',res.beginEdit.split(","));
			}
		}
		//隐藏某些字段
		if(res.formHide){
			form.iForm('hide',res.formHide.split(",") );
		}
		//需要再某个流程节点显示某些页面按钮
		if(res.showButton){
			var buttons=res.showButton.split(',');
			for(var index in buttons){
				var button=$(buttons[index]);
				button.show();
				//显示按钮的按钮组
				if(button && button.length && button.parent().hasClass('btn-group')){
					button.parent().show();
				}
			}
		}
		//是否有附件
		if(res.attach==true){
			opt.attach=true;
		}
		if(res.showForm){
			opt.showForm=res.showForm;
		}
		//将信息复制到全局变量中，供特殊处理使用
		for(var index in res){
			opt[index]=res[index];
		}
	}else if(form){
		form.iForm('endEdit');
	}
}
//判断一个变量是否是一个对象
function isObject(o){
	return Object.prototype.toString.call(o) === '[object Object]';
}
//初始化附件表单

function initAttachFormTemplate(result){
	//初始化状态考虑，当附件为可编辑状态是，不管附件是否为空都要显示；当附件为不可编辑状态时，空附件要用隐藏。
	var data=result.data;
	var $form=result.$form;
	var $wrapper=result.$wrapper;
	var attachMap=result.attachMap;
	var attachFormFields=result.attachFormFields;
	var fieldPrefix=result.fieldPrefix;
	var readOnly=result.readOnly; //附件是否是只读的,兼容多种格式的标志位信息
	
	if(readOnly==false){
		//编辑状态下，data不能为空，否则被隐藏
		data =data || [];
	}else if(readOnly==true){
		
	}else if(isObject(readOnly)){
		//解析readOnly，判断其是否可读
		if(readOnly.readOnly==false){
			readOnly=false;
			data =data || [];
		}else if(readOnly.readOnly==true && readOnly.attach==true){
			readOnly=false;
			data =data || [];
		}else {
			readOnly=true;
		}
	}
	//
	if(data){
		$.extend(true,attachMap,data);
		
		$form.iForm('init',{"fields":attachFormFields,"options":{
			labelFixWidth : 1,
			labelColon : false,
			fieldPrefix:fieldPrefix
		}});
		if(readOnly==true){
			$form.iForm('endEdit');
		}
		$wrapper.iFold();
	}
}

function isNumber(val){
	return !isNaN(parseFloat(val));
}
/**
 * 数字保留两位小数
 * @param val
 * @returns
 */
function NumToFix2(val){
	if(isNumber(val)){
		return new Number(val).toFixed(2);
	}
	return val;
}

//按钮灰化，防止重复提交
function buttonLoading(id){
	$(id).button('loading');
}

//按钮恢复
function buttonReset(id){
	$(id).button('reset');
}

//关闭流程弹出框
function closeWorkflowWindow(){
	_parent().$("#itcDlg").dialog("close");
}

//列表页面处理按钮
function initPageWithPrivilege(){
	var pri=_parent().privMapping;
	if(pri){
		var buttons=$(".pms-privilege");
		for(var i=0;i<buttons.length;i++){
			var id=buttons[i].id;
			if(id ){
			   if(pri[id]!=true){
				   $("#"+id).hide();
			   }
			}
		}
	}
	FW.fixToolbar("#toolbar1");
}

//判断结算计划类型
function getPayType(value){
	
	if(value=='yfk' || !value){
			return '预付款';
		}else if(value=='jdk'){
			return '进度款';
		}else if(value=='jgk'){
			return '竣工款';
		}else if(value=='zbj'){
			return '质保金';
		}else if(value=='first'){
			return '第1阶段款';
		}else if(value=='second'){
			return '第2阶段款';
		}else if(value=='third'){
			return '第3阶段款';
		}else if(value=='fourth'){
			return '第4阶段款';
		}else if(value=='fifth'){
			return '第5阶段款';
		}else if(value=='sixth'){
			return '第6阶段款';
		}else if(value=='seventh'){
			return '第7阶段款';
		}
}

function pmsReflesh(){
	window.location.href=window.location.href;
}

function pmsPrintHelp(buttonId,url,title){
	$(buttonId).click(function(){
		FW.dialog("init",{
			src: url,
			btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		                _parent().$("#itcDlg").dialog("close");
		             }
		        }],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	});

}

function getDatagridData(dataGrid,notRestore){
	var res=null;
	if(dataGrid){
		var rows=dataGrid.datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			dataGrid.datagrid('endEdit',i);
			
		}
		res=dataGrid.datagrid('getRows');
		if(!notRestore){
			for(var i=0;i<rows.length;i++){
				dataGrid.datagrid('beginEdit',i);
			}
		}
		
	}
	return res;
}

function getProgressBar(value){
	var progressBar=$("<div><div>");
	progressBar.progressBar(value,{
		boxImage:basePath+'img/pms/progressbar.gif',
		barImage:{
			0:basePath+'img/pms/progressbg_green.gif'
		}
	});
	return progressBar;
}

//验证datagrid
function validDatagrid(id){
	var datagrid=$(id);
	if(datagrid && datagrid.length && !datagrid.valid()){
		return false;
	}
	return true;
}

function getParentPrivMapping(){
	return _parent().privMapping;
}