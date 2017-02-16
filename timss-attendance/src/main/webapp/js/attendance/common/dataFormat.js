var AtdDataFormat={//数据格式化
	formatDoubleDays:function(value){//格式化天数的小数位
		//var formatDayNumReg = /^-?[0-9]\d*\.\d*|-0\.\d*[1-9]\d*$/;
		var formatDayNumReg = /^-?\d+\.?\d{4,}$/;//如果有超过3位的小数，则四舍五入为3位小数
		return formatDayNumReg.test( value )?parseFloat(value).toFixed(3):value;
	}
}