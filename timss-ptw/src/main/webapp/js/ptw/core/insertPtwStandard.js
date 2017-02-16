

var fields = [
              	{title : "id", id : "id" , type: "hidden"},
              	{title : "标准工作票编号",id : "wtNo",rules : {required:true}}, 
				{title : "版本号",id : "version",type : "label"}, 
				{title : "生效时间", id : "beginTime", type:"hidden"},
				{title : "失效时间", id : "endTime", type:"hidden"},
				{title : "工作票类型", id : "wtTypeId" ,rules : {required:true},
            	  type : "combobox",
					dataType : "enum",
					enumCat : "ptw_standard_type",
					options:{
						allowEmpty:true,
						/* onChange : onChangeType */
						onChange : function(val){
							//判断是否有权限新建
							//isPriNewSptw(val);
							$("#safeItem1").iFold("show");
							addSafeContents(1, true);
						}
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}  
				},
	  			{title : "设备编码", id : "eqNo", type: "hidden"},
	  			{title : "设备名称",id:"eqName",type:"label",value:"请从左边设备树选择",
	  				formatter:function(val){
	  					if(val == "请从左边设备树选择"){
	  						return "<span style='color:red'>请从左边设备树选择</span>";
	  					}
	  					return val ? val : "";
	  				}},
	  			{title : "工作内容", id : "workContent",wrapXsWidth:12,wrapMdWidth:8,rules : {required:true},linebreak:true},
	  			{title : "工作地点", id : "workPlace",wrapXsWidth:12,wrapMdWidth:8,rules : {required:true},linebreak:true}
              ];
	  		
fields[1].render = function(id){
	$("#"+id).iHint('init', getiHintParams());
};	
function getiHintParams(){
	var iHintParams ={
			datasource : basePath + "ptw/ptwStandard/sptwCodeMultiSearch.do",
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				$.post(basePath + "ptw/ptwStandard/hasSameCodeSptwInAudit.do",{"sptwCode":rowdata.wtNo,"id":id},function(data){
					if(data.result == false){
						$("#inPageTitle").html("标准工作票("+rowdata.code+")升级版本");
						autoFillSptwData(rowdata.id);
					}else{
						FW.error("该编号的标准票已有一张在审批中");
					}
				},"json");
				
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = rowdata.wtNo + " / " + rowdata.workContent;
				return showText;
			}
		};
	return iHintParams;
}

//初始化信息
function autoFillSptwData( ptwId ){
	var url = basePath + "ptw/ptwStandard/queryPtwStandardInfo.do?id=" + ptwId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success"){
				var bean = data.bean;
				bean.id = null;  //自动填充的时候，不要带上id
				bean.version = null;
				bean.beginTime = null;
				bean.endTime = null;
				//标准工作票类型
				stdType = bean.wtTypeId;	
				bean.wtTypeId = null; //赋值的时候，如果下拉框没有这种类型，则赋值为空
				//当前登录人
				currentUser = data.currentUser;
				//初始化form items
				setPtwStandard( bean, data.items );
				//给类型赋值，如果存在的话
				for(var n = 0 ; n<privTypes.length ; n++){
					if(stdType == privTypes[n]){
						$("#autoform").iForm("setVal",{"wtTypeId":stdType});
					}
				}
			}
		}
	});
}


//如果已经有设备树选择
function treeSelectNode(){
	var p = _parent().window.document.getElementById("ptwAssetTree").contentWindow;
	var node = p.getSelectedNode();
	if( node != null && node != "" ){
		eqNo = node.id;
		eqName = node.text;
		setForm();
	}
	
}

//接收树的点击
function onTreeItemClick( node ){
	eqNo = node.id;
	eqName = node.text;
	setForm();
	
}

//设置form表单数据
function setForm( ){
	var data = {
			"eqNo" : eqNo,
			"eqName" : eqName
	};
	
	$("#autoform").iForm("setVal", data);
	//$("#autoform").iForm("endEdit",['eqNo', 'eqName']);
}

//改变工作票类型
function onChangeType(){
	$("#safeItem1").iFold("show");
	addSafeContents(1, true);
}
function isPriNewSptw(val){
	$.post(basePath + "ptw/ptwStandard/queryNewSptwPriv.do?",
			{"sptwTypeVal":val},
		function(data){
			hasNewPriv = data.hasNewPriv;
			if(!hasNewPriv){
				FW.error("您无权新建此类标准操作票");
			}
		},"json");
}
