(function(_P){
//合同总额
	_P.definedModules.contract = {
			title:"",
			sizeX : 3,
			sizeY : 2,
			bordered : true,
			module : "contract",
			template : "contract"
	};

	_P.templates.contract = '<div class="inumber-icon-wrap fl bsafe-skyblue">' +
				  '<span class="icon-exchange inumber-icon"></span>' +
				  '</div>' +
				  '<div class="inumber-number-wrap">' +
				  '<div class="inumber-title">{{item.options.title}}</div>'+
				  '<div class="inumber-number">{{g}}<span>万元</span></div>'+
				  '</div>';
	app.directive('contract',function($compile){
		return {
			link : function(scope,element,attrs){
				var template = _P.templates.contract;
				var el = $compile(template)(scope);
				element.replaceWith(el);
			}
		};
	});
})(_portal);