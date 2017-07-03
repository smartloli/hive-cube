$(document).ready(function() {
	var mime = 'text/x-mariadb';
	// get mime type
	if (window.location.href.indexOf('mime=') > -1) {
		mime = window.location.href.substr(window.location.href.indexOf('mime=') + 5);
	}
	var sqlEditor = CodeMirror.fromTextArea(document.getElementById('code'), {
		mode : mime,
		indentWithTabs : true,
		smartIndent : true,
		lineNumbers : true,
		matchBrackets : true,
		autofocus : true,
		extraKeys : {
			"Alt-/" : "autocomplete"
		}
	});

	$("[name='userid-user-switch']").bootstrapSwitch('state', false);
	$("#mf_hive_task_name").val("数据导出 | " + getNowFormatDate());

	// hbase user info
	$("[name='userid-user-switch']").on('switchChange.bootstrapSwitch', function(event, state) {
		if (state) {
			$("#mf_user_user_info").show()
			$("#hbase_user_info").show();
		} else {
			$("#mf_user_user_info").hide()
			$("#hbase_user_info").hide();
		}
	});

	var filter_hbases = new Array();
	$(ucuser).each(function() {
		filter_hbases.push(this.valueName + "(" + this.value + ")");
	});

	$("#select2val_filter_hbase").select2({
		data : filter_hbases
	});

	$("#select2val_filter_condition_hbase").select2({
		data : [ "等于(=)", "大于(>)", "大于等于(>=)", "小于(<)", "小于等于(<=)", "包含(in)", "匹配(like)" ]
	});

	// load user info
	loadUcuser();

	function getNowFormatDate() {
		var date = new Date();
		var seperator1 = "-";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		var hour = date.getHours();
		var min = date.getMinutes();
		var sec = date.getSeconds()
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		if (hour >= 0 && hour <= 9) {
			hour = "0" + hour;
		}
		if (min >= 0 && min <= 9) {
			min = "0" + min;
		}
		if (sec >= 0 && sec <= 9) {
			sec = "0" + sec;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hour + seperator2 + min + seperator2 + sec;
		return currentdate;
	}

	$(document).on("click", "#hive_btn_submit_task", function() {
		if (sqlEditor.getValue().length == 0) {
			$("#alert_mssage_common").show();
			$("#alert_mssage_common").find("label").text(" 提交内容不能为空 .");
			setTimeout(function() {
				$("#alert_mssage_common").hide()
			}, 3000);
		} else {
			var json = "";
			var switchState = $("[name='userid-user-switch']").bootstrapSwitch('state');
			if (switchState) {
				// process2
				var items = $('input:checkbox[name=items]:checked').map(function() {
					return this.value;
				}).get().join();
				var itemsName = $('input:checkbox[name=items]:checked').map(function() {
					return $(this).attr("valueName");
				}).get().join();

				if (!(sqlEditor.getValue().toLowerCase().indexOf("_uid") > -1 && sqlEditor.getValue().toLowerCase().indexOf("_plat") > -1)) {
					$("#alert_mssage_common").show();
					$("#alert_mssage_common").find("label").text(" 关联用户信息,提交内容SQL语句必须包含`_uid`,`_plat` .");
					setTimeout(function() {
						$("#alert_mssage_common").hide()
					}, 3000);
					return;
				}
				json = {
					"column" : $("#mf_hive_column_name").val(),
					"context" : [ {
						"operate" : sqlEditor.getValue().toLowerCase(),
						"type" : "hive",
						"order" : 1
					}, {
						"operate" : {
							"column" : items,
							"alias" : itemsName,
							"tname" : "user_ucsuer",
							"filter" : ""
						},
						"type" : "hbase",
						"order" : 2
					} ]
				}
			} else {
				// process1
				json = {
					"column" : $("#mf_hive_column_name").val(),
					"context" : [ {
						"operate" : sqlEditor.getValue().toLowerCase(),
						"type" : "hive",
						"order" : 1
					} ]
				}
			}

			var url = "";
			if (sqlEditor.getValue().toLowerCase().indexOf("select") > -1) {
				url = '/mf/export/custom/task/ajax?content=' + JSON.stringify(json) + '&name=' + $("#mf_hive_task_name").val();
			} else {
				url = '/mf/export/custom/task/vip/ajax?content=' + JSON.stringify(json) + '&name=' + $("#mf_hive_task_name").val();
			}
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : url,
				success : function(datas) {
					if (datas != null) {
						console.log(datas);
						$('#mf_task_alert_dialog').modal('show');
						if (datas.code > 0) {
							$("#mf_task_result_dialog").html("");
							$("#mf_task_result_dialog").append("<span style='color:green'>任务添加成功</span>, 请在<a href='/mf/tasks/private'>任务管理</a>查看当前任务详情.");
						} else {
							$("#mf_task_result_dialog").html("");
							$("#mf_task_result_dialog").append("<span style='color:red'>任务添加失败, 请检查编写任务是否符合规范.</span>");
						}
					}
				}
			});
		}
	})

});