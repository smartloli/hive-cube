$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/hc/tasks/private/table/ajax",
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
		$('#hc_task_ref').val("/private");
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
});