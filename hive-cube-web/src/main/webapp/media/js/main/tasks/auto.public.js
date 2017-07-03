$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/mf/tasks/auto/public/table/ajax",
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

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_task_edit_dialog').modal('show');
		$('#mf_task_id').val(id);
		$('#mf_task_ref').val("/auto/private");
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/tasks/content/' + id + '/ajax',
			success : function(datas) {
				$("#mf_task_name").val(datas.name);
				$("#mf_task_email").val(datas.email);
				$("#mf_task_content").val(formatJsonParser(datas.content));
				$("#mf_task_column").val(datas.column);
			}
		});
	});

	$(document).on('click', 'a[name=operater_modal_setting]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		var tag = $(this).attr('tag');
		if (tag == "delete") {
			$('#mf_quartz_describer').find("span").html("<strong>确定要删除定时任务吗?</strong>");
			$('#mf_task_quartz').val("* * * * * ?");
		} else if (tag == "modify") {
			$('#mf_quartz_describer').find("span").html("<strong>格式: [秒] [分] [小时] [日] [月] [周] [年]</strong><br> 0 0 12 * * ? 每天12点触发<br> 0 15 10 ? * * 每天10点15分触发<br> 0 0/5 14 * * ? 每天14点到14点59分(整点开始,每隔5分触发)<br> 0 0-5 14 * * ? 每天14点到14点05分(每分触发)<br>");
		} else if (tag == "stop") {
			$('#mf_quartz_describer').find("span").html("<strong>确定要停止定时任务吗?</strong>");
		}else if(tag == "start"){
			$('#mf_quartz_describer').find("span").html("<strong>确定要开始定时任务吗?</strong>");
		}else if(tag=="cancle"){
			$('#mf_quartz_describer').find("span").html("<strong>确定要取消定时任务吗?</strong>");
		}
		$('#mf_task_auto_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/tasks/auto/quartz/content/' + id + '/ajax',
			success : function(datas) {
				$("#mf_task_quartz").val(datas.cron);
			}
		});
		$('#mf_task_name_id').val(id);
		$('#mf_task_name_ref').val("public");
		$('#mf_task_name_action').val(tag);
	})

	var formatJson = function(json, options) {
		var reg = null, formatted = '', pad = 0, PADDING = '    ';

		// optional settings
		options = options || {};
		// remove newline where '{' or '[' follows ':'
		options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
		// use a space after a colon
		options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

		// begin formatting...
		if (typeof json !== 'string') {
			// make sure we start with the JSON as a string
			json = JSON.stringify(json);
		} else {
			// is already a string, so parse and re-stringify in
			// order to remove
			// extra whitespace
			json = JSON.parse(json);
			json = JSON.stringify(json);
		}

		// add newline before and after curly braces
		reg = /([\{\}])/g;
		json = json.replace(reg, '\r\n$1\r\n');

		// add newline before and after square brackets
		reg = /([\[\]])/g;
		json = json.replace(reg, '\r\n$1\r\n');

		// add newline after comma
		reg = /(\,)/g;
		json = json.replace(reg, '$1\r\n');

		// remove multiple newlines
		reg = /(\r\n\r\n)/g;
		json = json.replace(reg, '\r\n');

		// remove newlines before commas
		reg = /\r\n\,/g;
		json = json.replace(reg, ',');

		// optional formatting...
		if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
			reg = /\:\r\n\{/g;
			json = json.replace(reg, ':{');
			reg = /\:\r\n\[/g;
			json = json.replace(reg, ':[');
		}
		if (options.spaceAfterColon) {
			reg = /\:/g;
			json = json.replace(reg, ': ');
		}

		$.each(json.split('\r\n'), function(index, node) {
			var i = 0, indent = 0, padding = '';

			if (node.match(/\{$/) || node.match(/\[$/)) {
				indent = 1;
			} else if (node.match(/\}/) || node.match(/\]/)) {
				if (pad !== 0) {
					pad -= 1;
				}
			} else {
				indent = 0;
			}

			for (i = 0; i < pad; i++) {
				padding += PADDING;
			}

			if (padding.length > 0 || node.length > 0) {
				formatted += padding + node + '\r\n';
			}
			pad += indent;
		});

		return formatted;
	};
});