var wuziColumns = [[
	            {field:'itemId',title:'物资Id',width:140,fixed:true,hidden:true},
	            {field:'version',title:'版本号',width:140,fixed:true,hidden:true},
	            {field:'isReceived',title:'是否已经入库',width:140,fixed:true,hidden:true},
	            {field:'itemCode',title:'物资编号',width:100,fixed:true,
	            	formatter:function(value,row,index){
	            		return "<a onclick='FW.showItemInfo(\""+row.itemCode+"\",\""+row.warehouseid+"\");'>"+row.itemCode+"</a>";
	  				}	
	            },
	            {field:'purchName',title:'物资名称',width:180,fixed:true},
	            {field:'type',title:'物资型号',width:140,fixed:true},
	            {field:'unit',title:'单位',width:50,fixed:true},
	            {field:'unitPrice',title:'采购单价',width:80,fixed:true},
	            {field:'mount',title:'采购量',width:50,fixed:true},
	            {field:'receivedMount',title:'已接收数量',width:75,fixed:true},
	            {field:'receivedPrice',title:'已接收金额',width:80,fixed:true},
	            {field:'warehouseid',title:'仓库id',hidden:true},
	            {field:'noTaxInvoicePrice',title:'发票单价',width:80,fixed:true,
	            	formatter:function(value,row,index){
	            		 return value;
	  				}	
	            },
	            {field:'taxUnitPrice',title:'含税单价',width:85,fixed:true,
	            	formatter:function(value,row,index){
	            		 return value;
	  				}
	            },
	            {field:'taxSum',title:'税额',width:80,fixed:true, editor : {
				 	type:"text","options" : {
				 		onChange : 	setNoTaxSumPrice,
			        	rules : {
			        		required:true,number:true
						}	
			        }
		    	}},
				{field:'noTaxSumPrice',title:'不含税开票金额',width:110, editor : {
				 	type:"text","options" : {
						onChange : 	setNoTaxSumPrice,
			        	rules : {
			        		required:true,number:true
						}	
			        }
		    	}},
		    	{field:'imtNo',title:' ',width:50,
					 formatter:function(value,row,index){
	  				     return '<img src="'+basePath+'img/purchase/btn_garbage.gif" width="16" height="16" style="cursor:pointer" />';
	  				}	
				}
				]];	

//设置明细datagrid
	function setWuziApplyDatagrid( dataArray ){
		$("#wuziDatagrid").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect:true,
	        fitColumns:true,
	        nowrap : false,
	        data : dataArray,
	        columns: wuziColumns,
			onLoadError: function(){
				//加载错误的提示 可以根据需要添加
			},
	        onLoadSuccess: function(data){
	        	
	        },onClickCell : function(rowIndex, field, value) {
				if (field == 'imtNo') {
					deleteThisRow(rowIndex, field, value);
				}
			}

	    });
	}
	
	
	//删除一行datagrid数据
	function deleteThisRow(rowIndex, field, value) {
		if (field == 'imtNo') {
			FW.confirm("删除？| 确定删除所选项吗？该操作无法撤销。", function() {
				$("#wuziDatagrid").datagrid("deleteRow",rowIndex);
				setNoTaxSumPrice();
			});
		}
	}
	
	//设置不含税开票金额
	function setNoTaxSumPrice(){
		var datagridData = $("#wuziDatagrid").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
		var len = datagridData.length;
		//不含税金额
		var noTaxSum = 0;
		//税额
		var tax = 0;
		
		var rowDatas = $("#wuziDatagrid").datagrid("getRows");
		
		for( var i = 0 ; i < len; i++ ){
			var row = $(datagridData[i]);
			//不含税开票金额
			var noTaxSumPrice = row.children("td[field='noTaxSumPrice']").find("input").val();
			if( isNull(noTaxSumPrice) ){
				noTaxSumPrice = rowDatas[i].noTaxSumPrice;
			}
			//税额
			var taxPrice = row.children("td[field='taxSum']").find("input").val();
			if( isNull(taxPrice) ){
				taxPrice = rowDatas[i].taxSum;
			}
			//接受数量
			var receivedMount = parseFloat( rowDatas[i].receivedMount );
			//发票单价
			var noTaxInvoicePrice = 0;
			//含税单价
			var taxUnitPrice = 0;
			
			//接受物资数量不为0
			if( receivedMount > 0 ){
				//发票单价
				noTaxInvoicePrice = parseFloat( noTaxSumPrice ) / receivedMount;
				//含税单价
				taxUnitPrice = ( parseFloat( taxPrice ) + parseFloat( noTaxSumPrice ) )/ receivedMount;
			}
			
			$("#wuziDatagrid").datagrid("updateRow",{
				index: i,
				row: {  "noTaxInvoicePrice": spliteDouble( noTaxInvoicePrice ),
					"taxUnitPrice": spliteDouble( taxUnitPrice ),
					"noTaxSumPrice" : noTaxSumPrice, "taxSum" : taxPrice}
			});
			$("#wuziDatagrid").datagrid('beginEdit', i);

			noTaxSum +=  parseFloat( noTaxSumPrice );
			tax += parseFloat( taxPrice );
		}
		//含税金额
		var sumPrice = tax + noTaxSum;
		$("#autoform").iForm("setVal",{noTaxSumPrice: spliteDouble( parseFloat( noTaxSum ) ),
			tax: spliteDouble( parseFloat( tax ) ),
			sumPrice: spliteDouble( parseFloat( sumPrice ) )} );
		var textVal =  len > 0 ? "继续添加物资" : "添加物资";
		$("#addDetail").text( textVal );
		
	}
	
	//如果小数位 > 2,截取两位小数
	function spliteDouble( num ){
		return eval(new Number( num ).toFixed(2));
	}
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//开启编辑模式
	function beginEditor( ){
		var rowSize = $("#wuziDatagrid").datagrid('getRows').length;
		for( var i = 0 ; i < rowSize; i++ ){
	    	$("#wuziDatagrid").datagrid('beginEdit', i);
		}
	}
	

	//请假单明细对话框
	var pri_dlgOpts = {
		width : 900,
		height : 600,
		closed : false,
		title : "添加物资明细",
		modal : true
	};
	

	//添加物资明细
	function showDtlIframe( contractId ){
		if(isNull( contractId) ){
			FW.error("请先选择采购合同");
			return false;
		}
		var src = basePath + "purchase/purInvoice/queryInvoiceItemToPage.do?contractId=" + contractId;
		
		var btnOpts = [{
			"name" : "取消",
			"float" : "right",
			"style" : "btn-default",
			"onclick" : function() {
				return true;
			}
		},{
			"name" : "确定",
			"float" : "right",
			"style" : "btn-success",
			"onclick" : function() {				
				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
				var selectRowDatas = conWin.$("#contentTb").datagrid("getSelections");
				var rowDatas = $('#wuziDatagrid').datagrid("getRows");
				//检测是否已经存在在列表中
				var resultDatas = new Array();
				for( i in selectRowDatas ){
					var selectRow = selectRowDatas[ i ];
					var flag = false;
					for( j in rowDatas ){
						if( selectRow.itemId == rowDatas[j].itemId && selectRow.imtNo == rowDatas[j].imtNo ){
							flag = true;
						}
					}
					if( !flag ){
						resultDatas.push(selectRow);
						//不含税开票金额
						selectRow.noTaxSumPrice = parseFloat( selectRow.noTaxInvoicePrice ) * parseFloat(selectRow.receivedMount);
						$('#wuziDatagrid').datagrid('appendRow',selectRow );
					}
				}
				//插入选中的结果
				if( selectRowDatas.length != resultDatas.length ){
					FW.error("部分物资已存在");
				}
				beginEditor();
				setNoTaxSumPrice();
				
				return true;
			}
		} ];

		FW.dialog("init", {
			"src" : src,
			"dlgOpts" : pri_dlgOpts,
			"btnOpts" : btnOpts
		});
	}
	
	//iHint 合同
	function searchContractHint( inputId, inputName, url ){
		var $firstPartyInput=$('#f_' + inputName);
		
		var firstPartyInit = {
			datasource : url,
			clickEvent : function(id, name) {
				$("#f_" + inputId ).val( id );
				contractId = id;
				var arr = name.split( "/" );
				$firstPartyInput.val( arr[0] );
				contractNo = arr[0];
				//通过合同ID查询发票基础表单需要的数据
				queryMainVoiceInfo( contractId );
				//加载物资列表
				getWuziDatagridData( contractId );
			},maxItemCount : 100
		};
		
		$firstPartyInput.change(function(){
			var val = $.trim( $firstPartyInput.val() );
			if( val == "" ){
				$("#f_" + inputId ).val( "" );
			}
		});
		$firstPartyInput.iHint('init', firstPartyInit);
	}
	
	//通过合同ID查询发票基础表单需要的数据
	function queryMainVoiceInfo( id ){
		var url = basePath + "purchase/purInvoice/queryVoiceAssetByContractId.do?contractId=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				loadFormData( data );
			}
		});
	}
	
	//填充发票form
	function loadFormData( rowData ){
		var businessNo = rowData.businessno;
		if( isNull( businessNo ) ){
			businessNo = "无";
		}
		var data = {
				"supplierId" : rowData.companyNo,
				"supplier" :rowData.companyName,
				"businessNo" : businessNo
			};
			$("#autoform").iForm("setVal",data);
	}
	
	//通过合同单号加载物资清单
	function getWuziDatagridData( id ){
		var url = basePath + "purchase/purInvoice/queryWuziDatagridByContractId.do?contractId=" + id;
		
		$("#wuziDatagrid").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect:true,
	        fitColumns:true,
	        nowrap : false,
	        url : url,
	        columns: wuziColumns,
			onLoadError: function(){
				//加载错误的提示 可以根据需要添加
			},
	        onLoadSuccess: function(data){
	        	beginEditor();
				setNoTaxSumPrice();
	        },onClickCell : function(rowIndex, field, value) {
				if (field == 'imtNo') {
					deleteThisRow(rowIndex, field, value);
				}
			}

	    });
		
	}
	
	//清空datagrid内容
	function clearWuziDatagrid(){
		var len = $('#wuziDatagrid').datagrid( "getRows" ).length;
		for( var index = 0; index < len; index ++ ){
			 $('#wuziDatagrid').datagrid( "deleteRow", index );
		}
	}
	
	