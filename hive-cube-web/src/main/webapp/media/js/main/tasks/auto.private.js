$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/hc/tasks/auto/private/table/ajax",
		"aoColumns" : [ {
			"mData" : 'id'
		}, {
			"mData" : 'name'
		}, {
			"mData" : 'owner'
		}, {
			"mData" : 'status'
		}, {
			"mData" : 'log'
		}, {
			"mData" : 'result'
		}, {
			"mData" : 'process'
		}, {
			"mData" : 'filesize'
		}, {
			"mData" : 'stime'
		}, {
			"mData" : 'etime'
		}, {
			"mData" : 'operate'
		} ]
	});

	function retrieveData(sSource, aoData, fnCallback) {
		$.ajax({
			"type" : "get",
			"contentType" : "application/json",
			"url" : sSource,
			"dataType" : "json",
			"data" : {
				aoData : JSON.stringify(aoData)
			},
			"success" : function(data) {
				fnCallback(data)
			}
		});
	}

	$(document).on('click', 'a[name=operate_process_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#hc_task_exec_info_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/tasks/executor/log/' + id + '/ajax',
			success : function(datas) {
				$("#hc_task_log_info").val(datas.log);
			}
		});
	})
	
	$(document).on('click', 'a[name=operate_task_log]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#hc_task_exec_info_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/tasks/content/' + id + '/ajax',
			success : function(datas) {
				$("#hc_task_log_info").val(datas.log);
			}
		});
	})

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#hc_task_edit_dialog').modal('show');
		$('#hc_task_id').val(id);
		$('#hc_task_ref').val("/auto/private");
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/tasks/content/' + id + '/ajax',
			success : function(datas) {
				console.log(datas);
				$("#hc_task_name").val(datas.name);
				$("#hc_task_email").val(datas.email);
				$("#hc_task_content").val(formatJsonParser(datas.content));
				$("#hc_task_column").val(datas.column);
			}
		});
	});

	$(document).on('click', 'a[name=operater_modal_setting]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		var tag = $(this).attr('tag');
		if (tag == "delete") {
			$('#hc_quartz_describer').find("span").html("<strong>Are you sure you want to delete the quartz task?</strong>");
			$('#hc_task_quartz').val("* * * * * ?");
		} else if (tag == "modify") {
			$('#hc_quartz_describer').find("span").html("<strong>Formmat:[s] [m] [h] [d] [m] [w] [y]</strong><br> 0 0 12 * * ? It's triggered at 12:00 every day<br> 0 15 10 ? * * It's triggered at 10:15 every day<br> 0 0/5 14 * * ? From 14:00 to 14:59 every day (starting at 5 every minute)<br> 0 0-5 14 * * ? 14:00 to 14:05 every day (triggered by each minute)<br>");
		} else if (tag == "stop") {
			$('#hc_quartz_describer').find("span").html("<strong>Are you sure you want to stop the quartz task?</strong>");
		}else if(tag == "start"){
			$('#hc_quartz_describer').find("span").html("<strong>Are you sure you want to start the quartz task?</strong>");
		}
		$('#hc_task_auto_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/tasks/auto/quartz/content/' + id + '/ajax',
			success : function(datas) {
				$("#hc_task_quartz").val(datas.cron);
			}
		});
		$('#hc_task_name_id').val(id);
		$('#hc_task_name_ref').val("private");
		$('#hc_task_name_action').val(tag);
	})
});