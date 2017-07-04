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

	$("#hc_hive_task_name").val("Export Data | " + getNowFormatDate());

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
			$("#alert_mssage_common").find("label").text(" Submit content can not null .");
			setTimeout(function() {
				$("#alert_mssage_common").hide()
			}, 3000);
		} else {
			var json = {
				"column" : $("#hc_hive_column_name").val(),
				"context" : [ {
					"operate" : sqlEditor.getValue().toLowerCase(),
					"type" : "hive",
					"order" : 1
				} ]
			}

			var url = "";
			if (sqlEditor.getValue().toLowerCase().indexOf("select") > -1) {
				url = '/hc/export/custom/task/ajax?content=' + JSON.stringify(json) + '&name=' + $("#hc_hive_task_name").val();
			} else {
				url = '/hc/export/custom/task/vip/ajax?content=' + JSON.stringify(json) + '&name=' + $("#hc_hive_task_name").val();
			}
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : url,
				success : function(datas) {
					if (datas != null) {
						console.log(datas);
						$('#hc_task_alert_dialog').modal('show');
						if (datas.code > 0) {
							$("#hc_task_result_dialog").html("");
							$("#hc_task_result_dialog").append("<span style='color:green'>The task was added successfully</span>. Please visit <a href='/hc/tasks/private'>Tasks</a> view current task details.");
						} else {
							$("#hc_task_result_dialog").html("");
							$("#hc_task_result_dialog").append("<span style='color:red'>The task was added failed. Check to see if the writing task is up to specification.</span>");
						}
					}
				}
			});
		}
	})

});