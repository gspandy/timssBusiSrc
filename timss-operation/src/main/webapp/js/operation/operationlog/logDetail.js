//填充运行记事列表数据
function newrow( i  )
{
	var html = "<tr>";
		
		html += "<td><div id='td"+i+"_1' style='width:140px'></div></td>"
		+"<td><div id='td"+i+"_2' style='width: 100px'></td>"
		+"<td><div id='td"+i+"_3' style='width: 500px'></td>"
		+"<td><div id='td"+i+"_4' style='width: 80px'></td>"
		+"</tr>";
		
	$("#operationData").append(html);
	$('#td' + i +'_1').html( "2014-05-05 16:28" );
	$('#td' + i +'_2').html( "接班重要参数" );
	$('#td' + i +'_3').html( "签发 机械班 机械工作票，工作内容为：1~5泄洪闸门及定期检查" );
	$('#td' + i +'_4').html( "工人" + i );
	
}
//记事列表数据
function newrow3( i , flag )
{
	var html = "<tr>";
	if( flag ){
		html += "<td><span class='itcui_chkbox' id='chkbox_'+ "+ i +"></span></td>";
	}
	html += "<td><div id='td"+i+"_1' style='width:120'></div></td>"
	+"<td><div id='td"+i+"_2' style='width: 100'></td>"
	+"<td><div id='td"+i+"_3' style='width: 80'></td>"
	+"<td><div id='td"+i+"_4' style='width: 500'></td>"
	/*+"<td><div id='td"+i+"_5' style='width: 80px'></td>"*/
	+"</tr>";
	
	$("#operationData").append(html);
	$('#td' + i +'_1').html( "2014-05-05 16:28" );
	$('#td' + i +'_2').html( "接班重要参数" );
	$('#td' + i +'_3').html( "#1" );
	$('#td' + i +'_4').html( "签发 机械班 机械工作票，工作内容为：1~5泄洪闸门及定期检查" );
	/*$('#td' + i +'_5').html( "工人" + i );*/
	
}

//填充运行记事列表数据
function newrow2( i )
{
	var html = "<tr>"
		+"<td id='td"+i+"_11' style='width: 70px'></td>"
		+"<td id='td"+i+"_31' style='width: 60px'></td>"
		+"<td id='td"+i+"_21' style='width: 40px'></td>"
		+"<td id='td"+i+"_41' style='width: 40px'></td>"
		+"<td id='td"+i+"_51' style='width: 400px'></td>"
	/*	+"<td><div id='td"+i+"_61' style='width: 140px'></td>"
		+"<td><div id='td"+i+"_71' style='width: 80px'></td>"
		+"<td><div id='td"+i+"_81' style='width: 80px'></td>"
		+"<td><div id='td"+i+"_91' style='width: 80px'></td>"*/
		+"</tr>";
		
	$("#quartzData").append(html);
	$('#td' + i +'_11').html( "2014-05-05 16:28" );
	$('#td' + i +'_31').html( "接班重要参数" );
	$('#td' + i +'_21').html( "部门1" );
	$('#td' + i +'_41').html( "工人" + i );
	$('#td' + i +'_51').html( "签发 机械班 机械工作票，工作内容为：1~5泄洪闸门及定期检查" );
	/*$('#td' + i +'_61').html( "2014-05-05" );
	$('#td' + i +'_71').html( "2014-05-05" );
	$('#td' + i +'_81').html( "工作" );
	$('#td' + i +'_91').html( "执行成功" );*/
	
}

//第0步要展示的DIV
function step0(){
	//toolbar
	$("#toolBarDiv").show();
	$("#processDiv").hide();
	$("#jiaobanToolbar").hide();
	
	//基础信息
	$("#heardDiv").show();
	
	//交接班情况
	$("#detail").show();
	$("#detail_subitem").show();
	$("#blockSpace").hide();
	
	//运行记事
	$("#operation").show();
	$("#operationDetail").show();
	$("#noteDiv").show();
	
	//定期工作
	$("#quartz").hide();
	$("#quartzDetail").hide();
	
	//交接班DIV
	$("#jiaobanFormDiv").hide();
	
}
//第一步要展示的DIV
function step1(){
	//toolbar
	$("#toolBarDiv").hide();
	$("#processDiv").show();
	$("#jiaobanToolbar").hide();
	
	//基础信息
	$("#heardDiv").show();
	$("#blockSpace").show();
	
	//交接班情况
	$("#detail").show();
	$("#detail_subitem").show();
	
	//运行记事
	$("#noteDiv").hide();
	$("#operation").hide();
	$("#operationDetail").hide();
	
	//定期工作
	$("#quartz").hide();
	$("#quartzDetail").hide();
}

//第2步要展示的DIV
function step2(){
	//toolbar
	$("#toolBarDiv").hide();
	$("#processDiv").show();
	$("#jiaobanToolbar").hide();
	
	//基础信息
	$("#heardDiv").hide();
	$("#blockSpace").hide();
	
	//交接班情况
	$("#detail").hide();
	$("#detail_subitem").hide();
	
	//运行记事
	$("#operation").show();
	$("#operationDetail").show();
	$("#noteDiv").hide();
	
	//定期工作
	$("#quartz").hide();
	$("#quartzDetail").hide();
}
//第3步要展示的DIV
function step3(){
	//toolbar
	$("#toolBarDiv").hide();
	$("#processDiv").show();
	$("#jiaobanToolbar").hide();
	
	//基础信息
	$("#heardDiv").hide();
	$("#blockSpace").hide();
	
	//交接班情况
	$("#detail").hide();
	$("#detail_subitem").hide();
	$("#noteDiv").hide();
	
	//运行记事
	$("#operation").hide();
	$("#operationDetail").hide();
	
	//定期工作
	$("#quartz").show();
	$("#quartzDetail").show();
}

//第四步
function step4(){
//	step0();
//	var url = "page/workconsole/jiaoBanSave.jsp";
//	addTab( "jiaoBan",  "填写交班信息", url ); 
	//toolbar
	$("#toolBarDiv").hide();
	$("#processDiv").hide();
	$("#jiaobanToolbar").show();
	
	//基础信息
	$("#heardDiv").hide();
	$("#blockSpace").hide();
	
	//交接班情况
	$("#detail").show();
	$("#detail_subitem").show();
	
	//运行记事
	$("#operation").hide();
	$("#operationDetail").hide();
	
	//定期工作
	$("#quartz").hide();
	$("#quartzDetail").hide();

	//交接班DIV
	$("#jiaobanFormDiv").show();
}

//展示步骤
function selectShow( flag ){
	if( flag == 1){
		step1();
	}else if ( flag == 2 ){
		step2();
	}else if ( flag == 3 ){
		step3();
	}else if( flag == 4 ){
		step4();
	}else{
		step0();
	}
}

//交班
function jiaoban(){
	var fields = [
	             {
	                  title : "交班情况", 
	                  id : "logContent",
	                  type : "textarea",
	                  linebreak:true,
	                  wrapXsWidth:12,
	                  wrapMdWidth:12,
	                  labelMdWidth:1,
	                  labelXsWidth:125,
	                  inputXsWidth:875
	              },
	              {
	                  title : "交班人账号", 
	                  id : "curruntUser",
	                  type : "text",
	                  linebreak:true
	              },
	              {
	                  title : "交班人密码", 
	                  id : "curruntPassword",
	                  type : "text",
	                  linebreak:false
	              },
	              {
	                  title : "接班人账号", 
	                  id : "jiebanUser",
	                  type : "text",
	                  linebreak:true
	              },
	              {
	                  title : "接班人密码", 
	                  id : "jiebanPassword",
	                  type : "text",
	                  linebreak:false
	              }
	          ];
	
	$("#jiaobanForm").ITC_Form({},fields);
}