$(document).ready(function() {
	var url = window.location.href;
	var ret = url.split("hc")[1];
	if (ret.indexOf("/tasks") > -1) {
		$("#demo2").addClass('collapse in');
		$("#demo2").attr("aria-expanded", true);
	} else if (ret == "/consumers") {
		$("div[id='navbar_click'] li").removeClass('active')
		$("#navbar_consumers").addClass('active');
	} else if (ret == "/") {
		$("div[id='navbar_click'] li").removeClass('active')
		$("#navbar_dash").addClass('active');
	} else if (ret.indexOf("/export") > -1) {
		$("#demo").addClass('collapse in');
		$("#demo").attr("aria-expanded", true);
	} else if (ret.indexOf("/system") > -1) {
		$("#demo1").addClass('collapse in');
		$("#demo1").attr("aria-expanded", true);
	}else if (ret.indexOf("/config") > -1) {
		$("#demo3").addClass('collapse in');
		$("#demo3").attr("aria-expanded", true);
	}else if (ret.indexOf("/metrics") > -1) {
		$("#demo4").addClass('collapse in');
		$("#demo4").attr("aria-expanded", true);
	}else if (ret.indexOf("/storage") > -1) {
		$("#demo5").addClass('collapse in');
		$("#demo5").attr("aria-expanded", true);
	}else if (ret.indexOf("/applications") > -1) {
		$("#demo6").addClass('collapse in');
		$("#demo6").attr("aria-expanded", true);
	}

	$(document).on('click', 'a[name=hc_account_reset]', function() {
		$('#hc_account_reset_dialog').modal('show');
		$(".modal-backdrop").css({
			"z-index": "999"
		});
	});

});