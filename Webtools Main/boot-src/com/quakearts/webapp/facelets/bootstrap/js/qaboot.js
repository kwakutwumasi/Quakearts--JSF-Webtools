var qab = qab || {};

qab.mlis = function (obj){
	if($(obj).hasClass("disabled"))
		return;

	var select = $(obj);
	var val = select.data("item-value");
	var ipid = select.data("dropdown-input");
	var input = $('#'+ ipid);
	if(select.hasClass('active')){
		input.find('input').each(function(){
			if($(this).val() == val){
				$(this).remove();
			}
		});
		select.removeClass('active');
	} else {
		input.append('<input name="'+ipid+'" value="'+val+'" type="hidden"></input>');
		select.addClass('active');
	}
	//trigger change event
	input.change();
}

qab.ssdd = function(obj){
	if($(obj).hasClass("disabled"))
		return;

	var drop = $('#'+$(obj).data('dropdown'));
	
	if(drop.is(':hidden') && drop.find('a').length>0)	
		drop.slideDown('slow', function(){
			if(($(window).scrollTop() + $(window).height())<(drop.offset().top+drop.height()))
				$(window).scrollTop($(window).scrollTop()+drop.height());
		});		
	else
		drop.slideUp('fast');
}

qab.ssidd =function(obj){
	if($(obj).hasClass("disabled"))
		return;
	
	var drop = $('#'+$(obj).data('dropdown'));
	$('.input-list-group').each(function(){
		if(!$(this).is(':hidden'))
			$(this).slideUp('fast');			
	});
	
	if(drop.is(':hidden') && drop.find('a').length>0)
		drop.slideDown('slow');
	else
		drop.slideUp('fast');	
}

qab.ssime = function(obj,s,e){
	var fc = $(obj);
	if(!fc.hasClass('form-control')){
		if(fc.is(":focus") && e.type!="blur")
			return;
		
		fc = fc.parent();	
	}
	
	if(s){
		fc.addClass('form-select-focus');
	} else {
		fc.removeClass('form-select-focus');
	}
}

qab.olis = function (obj){
	if($(obj).hasClass("disabled"))
		return;

	var select = $(obj);
	var ipid = select.data('dropdown-input');
	var val = select.data('item-value');
	var dspid = select.data('display-span');
	
	select.parent().children().each(function(){
		if($(this).hasClass('active'))
			$(this).removeClass('active');
	});
	select.addClass('active');
	var input = $('#'+ipid);
	
	input.find('input').each(function(){
		if($(this).attr("id"))
			return;
		
		$(this).remove();
	});
	
	input.append('<input name="'+ipid+'" value="'+val+'" type="hidden"></input>');
	
	if(dspid){
		var element = $('#'+dspid);
		if(element.prop("tagName").toLowerCase() == "input"){
			element.val(select.html());
		} else {
			element.html(select.html());
		}
	}
	//trigger change event
	input.change();
}

qab.pp = function(obj){
	var id = $(obj).data("page-input");
	var input= $('#'+id);
	input.val(parseInt(input.val())-1);
	input.change();
}

qab.np = function(obj){
	var id = $(obj).data("page-input");
	var input= $('#'+id);
	input.val(parseInt(input.val())+1);
	input.change();
}

qab.gp = function(obj){
	var id = $(obj).data("page-input");
	var val = $(obj).data("page-value");
	var input= $('#'+id);
	input.val(val);
	input.change();
}

qab.sf = function(fo, nvpair, newTarget) {
	qab.afv(fo, nvpair);
    var ft = fo.target;
    if (newTarget) {
        fo.target = newTarget;
    }
    if (fo.onsubmit) {
        var result = fo.onsubmit();
        if ((typeof result == 'undefined') || result) {
            fo.submit();
        }
    } else {
        fo.submit();
    }
    fo.target = ft;
    mojarra.dpf(fo);
};

qab.afv = function(fo, nvpair) {
    var pairs = new Array();
    fo.pairs = pairs;
    var i = 0;
    for (var k in nvpair) {
        if (nvpair.hasOwnProperty(k)) {
            var p = document.createElement("input");
            p.type = "hidden";
            p.name = k;
            p.value = nvpair[k];
            fo.appendChild(p);
            pairs[i++] = p;
        }
    }
};

qab.rfv = function(fo) {
    var pairs = fo.pairs;
    if (pairs !== null) {
        for (var i = 0; i < pairs.length; i++) {
            fo.removeChild(pairs[i]);
        }
    }
};

qab.escape = function(text) {
	return text.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
};

qab.qact = function(obj,delay){
	if(qab.tmid>0)
		clearTimeout(qab.tmid);
	
	qab.tmid = setTimeout(obj, delay);
};

qab.tmid =0;

qab.sdn = function (obj){
	var dropDown = $('#'+$(obj).data("dropdown"));
	dropDown.animate({scrollTop:dropDown.scrollTop()+dropDown.height()},{duration:500,queue:true,done:function(){
		if(dropDown.scrollTop()+dropDown.height()==dropDown.prop('scrollHeight')){
			$(obj).prop("disabled",true);
		}
		var upbtn = $('#'+$(obj).data("up-button"));
		if(upbtn.prop("disabled"))
			upbtn.prop("disabled",false);										
	}})
	return false;
}

qab.sup = function (obj){
	var dropDown = $('#'+$(obj).data("dropdown"));
	dropDown.animate({scrollTop:dropDown.scrollTop()-dropDown.height()},{duration:500,queue:true, done:function(){
		if(dropDown.scrollTop()==0){
			$(obj).prop("disabled",true);
		}
		var dnbtn = $('#'+$(obj).data("down-button"));
		if(dnbtn.prop("disabled"))
			dnbtn.prop("disabled",false);										
	}})
	return false;
}

qab.rsel = [];
qab.rsi = function () {
	var windowWidth = $(window).width();
	for(var i=0;i<qab.rsel.length;i++){
		qab.rsel[i].resizeImage(windowWidth);
	}
}

qab.op = false;
qab.tm = function(obj){
	var sidemenu = $(obj);
	
	var left=this.op?"-13em":"0";
	sidemenu.animate({
		left:left
	});
	
	this.op = !this.op;
}

qab.dc = function(day,month,year,dsel,
		msel,ysel,insel,dbsel,type){
	return  {
		"day":day,
		"month":month,
		"year":year,
		"dsel":dsel,
		"msel":msel,
		"ysel":ysel,
		"insel":insel,
		"dbsel":dbsel,
		"type":type,
		"updateDay" : function(obj) {
			this.day = obj.innerHTML;
			this.updateComponent();
			$(this.dsel).html($(obj).html());
		},
		"updateMonth" : function(obj, val) {
			this.month = val;
			var totalDays = MonthDays[val - 1];
			if (this.day > totalDays) {
				this.day = totalDays;
				$(this.dsel).html(totalDays + '');
			}
			this.updateComponent();
			$(this.msel).html($(obj).html());
			this.showDays();
		},
		"updateYear" : function(obj) {
			this.year = obj.innerHTML;
			$(this.ysel).html($(obj).html());
			if(this.month)
				this.showDays();
			else {
				$(this.msel).html("Jan");
				this.month = 1;
				this.showDays();
			}
			this.updateComponent();
		},
		"showDays" : function() {
			if(this.type !="dm" && this.type!="dmy")
				return;

			var totalDays = MonthDays[this.month - 1];
			var count = -8;
			var offset = new Date(this.year?this.year:new Date().getFullYear(), this.month-1, 1, 0, 0, 0, 0).getDay();
			$(this.dbsel).parent().find('.buffer').width(offset*39);
			$(this.dbsel).parent().find('div.dropdown-menu')
					.children().each(function() {
						count++;
						if (totalDays < count) {
							$(this).addClass('collapse');
							$(this).removeClass('day');
						} else {
							if ($(this).hasClass('collapse')) {
								$(this).removeClass('collapse');
								$(this).addClass('day');
							}
						}
					});
		},
		"clearComponent" : function() {
			$(this.insel).val('');
			$(this.dsel).html('&nbsp;');
			$(this.msel).html('&nbsp;');
			$(this.ysel).html('&nbsp;');
		},
		"updateComponent" : function() {
			var mnStr = this.month + "";
			switch(this.type){
			case "dm":
					$(this.insel).val((this.day.length == 1 ? "0" + this.day : this.day)
							+ "/"+ (mnStr.length == 1 ? "0" + mnStr: mnStr));
					break;
			case "m":
					$(this.insel).val(mnStr.length == 1 ? "0" + mnStr: mnStr);
					break;
			case "my":
					$(this.insel).val((mnStr.length == 1 ? "0" + mnStr: this.month) 
							+ "/" + this.year);
					break;
			case "y":
					$(this.insel).val(this.year);
					break;
			default:
				$(this.insel).val((this.day.length == 1 ? "0" + this.day : this.day)
									+ "/"+ (mnStr.length == 1 ? "0" + mnStr: mnStr) 
									+ "/" + this.year);
			}
			$(this.insel).change();
		}
	};
};
