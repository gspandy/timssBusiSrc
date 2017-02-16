(function(_P){
/***************************itsm statistic defMods begin***************************/
	_P.definedModules.itsmsdstatistic = {
		sizeX : 6,
		sizeY : 6,
		bordered : true,
		module : "highcharts",
		resizable : false			
	};

	_P.definedModules.itsmTeamRespondSolvestatistic = {
			sizeX : 6,
			sizeY : 6,
			noPadding:"lr",
			bordered : true,
			module : "highcharts",
			resizable : false			
		};
	
	_P.definedModules.itsmUnOkWostatistic = {
			sizeX : 3,
			sizeY : 6,
			noPadding:"r",
			bordered : true,
			module : "simple-porlet",
			resizable : false,
			template : "unOKWOStatistic"
		};
	
	_P.definedModules.itsmWoSolveAbilitystatistic = {
			sizeX : 3,
			sizeY : 6,
			noPadding:"r",
			bordered : true,
			module : "simple-porlet",
			resizable : false,
			template : "woSolveAbilitystatistic"
		};
	_P.definedModules.itsmWoAvgRespondTimestatistic = {
			sizeX : 3,
			sizeY : 6,
			noPadding:"r",
			bordered : true,
			module : "simple-porlet",
			resizable : false,
			template : "woAvgRespondTimestatistic"
		};
	/***************************itsm statistic  defMods end***************************/
	
	/***************************itsm template begin***************************/
    _P.templates.unOKWOStatistic = '<div class="inumber-number-wrap wo-statistic">' +
								  	'<div class="inumber-number-itsmtitle"><b>{{item.options.title}}</b></div>'+
								  	'<div class="inumber-number-itsm" style="margin-top: 40px;margin-left: 40px;"><span class="csafe-red" style="font-size:54px;">{{g.unOkRatio}}</span><span class="csafe-red">%</span>&nbsp;&nbsp;&nbsp;{{g.unOkSum}}<span>&nbsp;次</span></div>'+
								  '</div>';
    
   /* */
    _P.templates.woSolveAbilitystatistic = '<div class="inumber-number-wrap woability-statistic">' +
  	'<div class="inumber-number-itsmtitle "><b>{{item.options.title}}</b></div>'+
  	'<table width="200" border="0" cellspacing="0" cellpadding="0" style="margin-top:30px"> '+
  	'<tr valign="baseline"> '+
  	'<td>超时响应率</td> '+
  	'<td><span ng-class="{true: \'up\', false: \'down\'}[g.respondFlag > 0]"><span style="font-size:36px">{{g.respondData}}</span>%</span></td> '+
  	'</tr> '+
  	'<tr valign="baseline"> '+
  	'<td>超时解决率</td> '+
  	'<td><span ng-class="{true: \'up\', false: \'down\'}[g.solveFlag > 0]"><span style="font-size:36px">{{g.solveData}}</span>%</span></td> '+
  	'</tr> '+
  	'</table> '+
  '</div>';
    
   /* _P.templates.woAvgRespondTimestatistic = '<div class="inumber-number-wrap worespondtime-statistic">' +
  	'<div class="inumber-number-itsm"  ng-repeat="item in g">'+
	  	'<table width="200" border="0" cellspacing="0" cellpadding="0" style="margin-top:20px"> '+
		  	'<tr> '+
			  	'<td width="50"><span class="wots-name">{{item.name}}</span></td> '+
			  	'<td><span class="csafe-red" style="font-size:24px">{{item.second}}</span></td> '+
			  	'<td width="50"><span style="margin-left:8px">{{item.unit}}</span></td> '+
			  	'<td>'+
			  		'<span ng-if="item.flag > 0" class="csafe-red" class="wots-arrow">+</span>' +
			  		'<span ng-if="item.flag <= 0" class="csafe-darkgreen" class="wots-arrow">-</span>' +
				  	'<span ng-if="item.flag > 0" class="csafe-red" class="wots-ratio">{{item.ratio}}%</span>' +
				  	'<span ng-if="item.flag <= 0" class="csafe-darkgreen" class="wots-ratio">{{item.ratio}}%</span>' +
			  	'</td> '+
		  	'</tr> '+
	  	'</table> '+
  	'</div>' + 
  '</div>';*/
    _P.templates.woAvgRespondTimestatistic = '<div class="inumber-number-wrap worespondtime-statistic">' +
  	'<div class="inumber-number-itsm"  ng-repeat="item in g">'+
	  	'<table width="200" border="0" cellspacing="0" cellpadding="0" style="margin-top:20px"> '+
		  	'<tr> '+
			  	'<td width="40"><span class="wots-name">{{item.name}}</span></td> '+
			  	'<td width="40" style="text-align:center;"><span class="csafe-red" style="font-size:24px">{{item.second}}</span></td> '+
			  	'<td width="40" style="text-align:center;">{{item.unit}}</td> '+
			  	'<td>'+
				  	'<span class="wots-ratio" style="font-size:10px;color:#999999">(标准{{item.standardLen}}分钟)</span>' +
			  	'</td> '+
		  	'</tr> '+
	  	'</table> '+
  	'</div>' + 
  '</div>';
    /***************************itsm template end***************************/
    
   
})(_portal);