var onchangeFlag = false;
var InvAcceptForm={
	init:function(data){
		var form=InvAcceptForm.initForm(data);
		InvAcceptForm.initFormData(form,data);
	},
	
	basicFormField : [
		    {id:"inacId",type:"hidden"},
		    {id:"inacNo",type:"hidden"},
		    {id:"poId",type:"hidden"},
		    {id:"poName",type:"hidden"},
		    {id:"specialMaterials",type:"hidden",value:'NONE'},
    		{title : "采购合同号", id : "poSheetno",render:function(id){
    			purOrderNoSearch(id);
    		}},
    		{title : "采购类型", id : "sheetType",type:"label"},

    		{title : "采购名称", id : "sheetName", type:"label"},
        	{title : "申请日期", id : "createdate",type:"date" ,dataType : "date"},
    	    {title : "采购员", id : "createusername"},
    	    {title : "送货单位", id : "deliveryName"},
    	    {title : "送货日期", id : "deliveryDate",type:"date" ,dataType : "date"},
    	    {title : "送货人/电话", id : "deliveryMan",type : "text"}

      ],
    acceptFormField : [
     		{id:"inacId",type:"hidden"},
     		{id:"poId",type:"hidden"},
		    {id:"inacNo",title:"编号"},
		    {id:"poName",type:"hidden"},
		    {id:"specialMaterials",type:"hidden"},
    	    {title : "状态", id : "status", 
    	    	type:"combobox",
    	    	dataType:'enum',
    		    enumCat:'ACPT_STATUS'
    	    },
    		{title : "采购合同号", id : "poSheetno", type:"label"},
    		{title : "采购类型", id : "sheetType",type:"label"},
    		{title : "采购名称", id : "sheetName", type:"label"},
        	{title : "申请日期", id : "createdate",type:"date" ,dataType : "date"},
    	    {title : "采购员", id : "createusername",type:"label"},
    	    {title : "送货单位", id : "deliveryName"},
    	    {title : "送货日期", id : "deliveryDate",type:"date" ,dataType : "date"},
    	    {title : "送货人/联系电话", id : "deliveryMan",type : "text"},
    	    {title : "验收结论", id : "acptCnlus", rules : {required:true},
    	    	type:"combobox",
    	    	dataType:'enum',
    		    enumCat:'ACPT_CNLUS',
    		    options:{
    		    	allowEmpty:true,
    		    	initOnChange:false,
    		    	onChange:function(val){
    		    	    if(val == 'FAILURE'&& onchangeFlag == true){
    		    	    	FW.confirm("验收结论为不合格时，物资不能自动入库，确认不合格结论？",function(){
    		    	    	});
    		    	    }
    		    	}
    		    }
    	    },
    	    {title : "验收方式", id : "acptType", rules : {required:true},
	    	type:"combobox",
	    	dataType:'enum',
		    enumCat:'ACPT_TYPE',
		    options:{
		    	allowEmpty:true
		    }
	    }
     ],
    initForm:function(data){
    	var formField=InvAcceptForm.getFormField(data);
    	return $("#autoform").iForm("init",{"fields":formField,"options":{validate:true}});
    },
    initFormData:function(form,data){
    	if(!data.isEdit){
    		InvAcceptForm.initFormForAdd(form,data);
    		onchangeFlag = true;
    		
    	}else{
    		InvAcceptForm.initFormForEdit(form,data);
    		onchangeFlag = true;
    	}
    },
    initFormForAdd:function(form,data){
    	if(data.poId){
    		var formData={
    		    poSheetno:data.purOrder && data.purOrder.sheetno,
    			poId:data.poId,
    			createdate:new Date(),
    			createusername:data.createusername,
    			poName:data.purOrder && data.purOrder.sheetname,
    			sheetType:data.purOrder && FW.getEnumMap("ITEMORDER_TYPE")[data.purOrder.sheetIType],
    			sheetName:data.purOrder && data.purOrder.sheetname,
    			deliveryName:data.purOrder.companyName
    		};
    		form.iForm('setVal',formData);
    		form.iForm('endEdit',["poSheetno","createdate","createusername"]);
    	}else{
    		var formData={
        			createdate:new Date(),
        			createusername:data.createusername
        		};
    		form.iForm('setVal',formData);
    		form.iForm('endEdit',["createdate","createusername"]);
    	}
    },
    initFormForEdit:function(form,data){
    	var invMatAccept=data.invMatAccept;
    	//将sheettype转换成名称
    	//invMatAccept.sheetType为数字，审批通过状态采用的是acceptFormField，其他状态basicFormField
    	invMatAccept.sheetType = FW.getEnumMap("ITEMORDER_TYPE")[invMatAccept.sheetType];
    	form.iForm('setVal',invMatAccept);
		form.iForm('endEdit');
		if(InvAcceptPriv.isApprovalStatus(data) || InvAcceptPriv.isSubmitStatus(data)){
			var field=InvAcceptForm.getFormEditField(data);
			if(field){
				form.iForm('beginEdit',field);
			}
		}
    },
    getFormEditField:function(data){
    	var field=null;
    	var modi=InvAcceptPriv.getFlowElementMod(data);
		if(modi){
			var field=modi.editFieldName;
		}
		return field;
    },
    getFormField:function(data){
    	var field=InvAcceptForm.basicFormField;
    	var modi=InvAcceptPriv.getFlowElementMod(data);
		if(modi){
			var fieldName=modi.fieldName;
			if(fieldName){
				field=InvAcceptForm[fieldName];
			}
		}else{
			//如果流程为审批通过状态
			if(InvAcceptPriv.isPassApproved(data)){
				field=InvAcceptForm.acceptFormField;
			}
		}
		return field;
    }
};


//采购单号查询
function purOrderNoSearch(id){
  	//放大镜图标属于基础资源，可以直接引用
  	$("#" + id).attr("icon","itcui_btn_mag").removeClass("form-control").ITCUI_Input();
  	$("#" + id).next(".itcui_input_icon").on("click",function(){
  		FW.dialog("init",{
  			src: basePath+"inventory/invmataccept/purOrderNoList.do",
  			btnOpts:[{
  		            "name" : "取消",
  		            "float" : "right",
  		            "style" : "btn-default",
  		            "onclick" : function(){
  		                _parent().$("#itcDlg").dialog("close");
  		             }
  		        },
  		        {
  		            "name" : "确定",
  		            "float" : "right",
  		            "style" : "btn-success",
  		            "onclick" : function(){
  		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
  		                var info = p.$("#purorder_grid").datagrid('getChecked');
  		                if(!info || info.length==0){
  		                	FW.error("需要选择一个采购合同");
  		                	return ;
  		                }else if(info.length>1){
  		                	FW.error("只能选择一个");
  		                	return ;
  		                }
  		                var po=info[0];
	                	
  		               $("#autoform").iForm("setVal",{
  		            	   poSheetno:po.sheetno,
  		      			   poName:po.sheetname,
  		      			   sheetName:po.sheetname,
  		      			   sheetType:FW.getEnumMap("ITEMORDER_TYPE")[po.applyTypeName]
  		      			});
                        
	                	//加载添加物资的列表
	                	$.ajax({
	                		type : "POST",
	            			async: false,
	            			url: basePath+"inventory/invmataccept/queryInvMatAcceptByPoNo.do",
	            			data: {"poNo":po.sheetno},
	            			dataType : "json",
	            			complete:function(data){
	                		        data=data.responseJSON;
	                		        if(data.result=="success"){
	                		        	
	                		        	$("#autoform").iForm("setVal",{
	                		        	   poSheetno:po.sheetno,
	               		         		   poId:data.poId,
	               		      			   poName:po.sheetname,
	               		      			   deliveryName:data.companyName
	               		      			});
	                		        	InvAcceptDetailList.dataGrid.datagrid("loadData",data.invMatAcceptDetails);
				            			
		                				_parent().$("#itcDlg").dialog("close"); 
	                		        }else{
	                		        	FW.error("获取采购合同信息失败");
	                		        }
			                		
	                		}
	                	});
	                }
  		        }],
  			dlgOpts:{ width:800, height:520, closed:false, title:"采购合同查询", modal:true }
  		        })
  		 });
  }