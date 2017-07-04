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
			$('#hc_quartz_describer').find("span").html("<strong>确定要删除定时任务吗?</strong>");
			$('#hc_task_quartz').val("* * * * * ?");
		} else if (tag == "modify") {
			$('#hc_quartz_describer').find("span").html("<strong>格式: [秒] [分] [小时] [日] [月] [周] [年]</strong><br> 0 0 12 * * ? 每天12点触发<br> 0 15 10 ? * * 每天10点15分触发<br> 0 0/5 14 * * ? 每天14点到14点59分(整点开始,每隔5分触发)<br> 0 0-5 14 * * ? 每天14点到14点05分(每分触发)<br>");
		} else if (tag == "stop") {
			$('#hc_quartz_describer').find("span").html("<strong>确定要停止定时任务吗?</strong>");
		}else if(tag == "start"){
			$('#hc_quartz_describer').find("span").html("<strong>确定要开始定时任务吗?</strong>");
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