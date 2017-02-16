(function(_P){
/***************************pur defMods begin***************************/
	_P.definedModules.totalprice = {
		sizeX : 3,
		sizeY : 2,
		bordered : false,
		module : "simple-porlet",
		template : "totalprice"
	};

	_P.definedModules.actualTotal = {
		sizeX : 3,
		sizeY : 2,
		bordered : false,
		module : "simple-porlet",
		template : "totalprice"
	};
	
	_P.definedModules.stockList = {
		sizeX : 3,
		bordered : true,
		module : "list",
		flag : true,
		sum : true,
		noPadding:"lr"
	};
	
	_P.definedModules.majorPurchaseStatistic = {
		sizeX : 6,
		sizeY : 9,
		bordered : true,
		module : "highcharts",
		resizable : false			
	};	
	//当前站点库存报账金额统计（分已报账和未报账）
	_P.definedModules.reimbursedMoneyStatistic = {
		sizeX : 6,
		sizeY : 6,
		bordered : true,
		module : "highcharts",
		resizable : false			
	};	
	
	/***************************pur defMods end***************************/
	
	/***************************pur template begin***************************/
    _P.templates.totalprice = '<div class="inumber-icon-wrap fl {{item.options.iconBackground}}">' +
								  	'<span class="{{item.options.icon}} inumber-icon"></span>' +
								  '</div>' +
								  '<div class="inumber-number-wrap">' +
								  	'<div class="inumber-title">{{item.options.title}}</div>'+
								  	'<div class="inumber-number">{{g.totalPrice}}<span>{{item.options.unit}}</span></div>'+
								  '</div>';
    
    /*_P.templates.invlist = '<div style="width:446px;height:173px;border-top:1px solid #aaa;overflow:auto">' +
    							'<table class="new-table" cellspacing=0 cellpadding=0 style="width:100%">' +
							    '<thead>' +
							         '<tr class="row-item head">' +
							             '<td class="col-left">物资</td>' + 
							             '<td class="col-right">当前库存量</td>' + 
							             '<td class="col-right">安全库存量</td>' + 
							         '</tr>'+
							    '</thead>' + 
							    '<tbody>' +
							    	  '<tr ng-repeat="row in rowData" class="row-item">' +
							    	       '<td class="col-left">' + 
							    	       		'<span class="row-top">{{row.itemname}}</span>' +
							    	       		'<span class="row-bottom">{{row.cusmodel}}</span>' +
							    	       '</td>' + 
							    	       '<td class="col-right align-right">' + 
							    	       		'<b>{{row.stockqty}}</b>' +
							    	       		'<span>{{row.unit}}</span>' +
						    	       	   '</td>' +
							    	       '<td class="col-right align-right">' + 
							    	       		'<span class="row-b">{{row.lowinv}}</span>' +
							    	       		'<span>{{row.unit}}</span>' +
						    	       	   '</td>' +
							    	  '</tr>' +
							    '</tbody>' +
							'</table>'+
    					'</div>';*/

    
    _P.templates.invlist = '<div style="width:446px;height:173px;border-top:1px solid #aaa;overflow:auto">' +
								'<ul class="new-table">' +
									'<li class="row-item head">' +
										'<div class="col-left">物资</div>' +
										'<div class="col-right">当前库存量</div>' +
										'<div class="col-right">安全库存量</div>' +
									'</li>' +
									'<li class="row-item" ng-repeat="row in rowData" style="list-style: none;">' +
									'	<div class="col-left">' +
									'		<span class="row-top">{{row.itemname}}</span>' +
									'		<span class="row-bottom">{{row.cusmodel}}</span>' +
									'	</div>' +
									'	<div class="col-right align-right">' +
									'		<b>{{row.stockqty}}</b>' +
									'		<span>{{row.unit}}</span>' +
									'	</div>' +
									'	<div class="col-right align-right">' +
									'		<span class="row-b">{{row.lowinv}}</span>' +
									'		<span>{{row.unit}}</span>' +
									'	</div>' +
									'</li>' +
								'</ul>' +
							'</div>';
    /***************************pur template end***************************/
	
	/***************************pur directive begin***************************/
	app.directive('totalprice',function($compile){
		return {
			link : function(scope,element,attrs){
				var template = _P.templates.totalprice;
				var el = $compile(template)(scope);
				element.replaceWith(el);
			}
		};
	});
	
	app.directive('list',function($compile){
		return {
			link: function(scope,element,attrs){
				var rowData = [];
				var item = scope.item;
				var preModule = _P.definedModules[item.module];
				scope.options = item.options;
				scope.columns = item.columns;
				scope.rowData = [];
				scope.$on('portal-data-loaded',function(){
					var data = scope.g;
					var columns = scope.columns;
					for(var i=0;i<data.length;i++){
						var row = data[i];
						/*var r = [];
						for(var j=0;j<columns.length;j++){
							var column = columns[j];
							var fieldValue = row[column.field];
							var fieldName = column.field;
							fieldValue = typeof(fieldValue) !== "undefined" ? fieldValue : "";
							r.push({c: fieldValue,w:column.width,style:column.style,f: fieldName});
						}*/
						rowData.push(row);
					}
					scope.rowData = rowData;
				});				
				var el = $compile(_P.templates.invlist)(scope);
				element.replaceWith(el);
			}
		};
	});
	
	/***************************pur directive end***************************/
})(_portal);