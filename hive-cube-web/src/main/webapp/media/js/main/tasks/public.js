$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/mf/tasks/public/table/ajax",
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

	$(document).on('click', 'a[name=operater_modal_auto]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_task_auto_dialog').modal('show');
		$('#mf_task_name_id').val(id);
		$('#mf_task_name_ref').val("private");
	})

	$(document).on('click', 'a[name=operate_process_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_task_exec_info_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/tasks/executor/log/' + id + '/ajax',
			success : function(datas) {
				$("#mf_task_log_info").val(datas.log);
			}
		});
	})

	$(document).on('click', 'a[name=operate_task_log]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_task_exec_info_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/tasks/content/' + id + '/ajax',
			success : function(datas) {
				$("#mf_task_log_info").val(datas.log);
			}
		});
	})

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_task_edit_dialog').modal('show');
		$('#mf_task_id').val(id);
		$('#mf_task_ref').val("/public");
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/tasks/content/' + id + '/ajax',
			success : function(datas) {
				console.log(datas);
				$("#mf_task_name").val(datas.name);
				$("#mf_task_email").val(datas.email);
				$("#mf_task_content").val(formatJsonParser(datas.content));
				$("#mf_task_column").val(datas.column);
			}
		});
	});
});