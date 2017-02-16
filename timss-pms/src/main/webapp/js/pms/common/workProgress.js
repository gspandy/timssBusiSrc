(function($,win){
	$.fn.progressSquareBar=function(data,args){
		var _this=this;
		var args=$.fn.extend({},$.fn.progressSquareBar.defaults,args);
		
		var html=parseDataToSquare(data,args);
		_this.html(html);
		return _this;
	}; 
	//将进度数据转换为进度条html
	function parseDataToSquare(data,args){
		var html=[];
		var cssPrefix=args.cssPrefix;
		html.push("<div class='pms-progress-wrapper'>");
		//将每一个阶段解析为对应的div方块
		for(var k in data){
			var item=data[k];
			var status=getDataItemStatus(item);
			var name=getDataItemName(item);
			html.push("<div class='");
			html.push(cssPrefix+status);
			html.push("' title='");
			html.push(name);
			html.push("'></div>");
		}
		html.push("</div>");
		return html.join("");
	}
	//分析进度状态
	function getDataItemStatus(item){
		if(item.actualTime && FW.long2date(item.actualTime)<=FW.long2date(item.expectedTime)){
			return "finished";
		}else if(!item.actualTime && FW.long2date(item.expectedTime)>FW.long2date(FW.long2date(new Date()))){
			return "unfinished";
		}else {
			return "defered";
		}
	}
	
	function getDataItemName(item){
		return item.milestoneName;
	}
	$.fn.progressSquareBar.defaults={
		width:200,
		height:30,
		cssStyle:"",
		cssPrefix:"pms-progress-"
	};
})($,window);