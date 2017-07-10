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

	var jobId = "";
	var triggerTask;
	var offset = 0;
	$(document).on('click', 'a[name=run_task]', function() {
		if (sqlEditor.getValue().toLowerCase().indexOf("drop") > -1 || sqlEditor.getValue().toLowerCase().indexOf("update") > -1 || sqlEditor.getValue().toLowerCase().indexOf("insert") > -1) {
			sqlEditor.setValue("SQL [" + sqlEditor.getValue() + "] is not support");
		} else {
			jobId = "job_" + new Date().getTime();
			var sql = sqlEditor.getValue();
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : '/hc/storage/specify/hbase/task/ajax?sql=' + sql + '&jobId=' + jobId,
				success : function(datas) {
					if (datas) {
						triggerTask = setInterval(getProgress, 1000);
					}
				}
			});
		}
	});

	var offset = 0;
	var hbaseColumn = null;
	function getProgress() {
		if (hbaseColumn != null) {
			clearInterval(triggerTask);
			var ret = JSON.parse(hbaseColumn);
			var tabHeader = "<div class='panel-body' id='div_children" + offset + "'><table id='result_children" + offset + "' class='table table-bordered table-hover' width='100%'><thead><tr>"
			var mData = [];
			var i = 0;
			for ( var key in ret[0]) {
				tabHeader += "<th>" + key + "</th>";
				var obj = {
					mData : key
				};
				mData.push(obj);
			}

			tabHeader += "</tr></thead></table></div>";
			$("#result_textarea").append(tabHeader);
			if (offset > 0) {
				$("#div_children" + (offset - 1)).remove();
			}

			$("#result_children" + offset).dataTable({
				"searching" : false,
				"bSort" : false,
				"bLengthChange" : false,
				"bProcessing" : true,
				"bServerSide" : true,
				"fnServerData" : retrieveData,
				"sAjaxSource" : "/hc/storage/hbase/query/result/" + jobId + "/",
				"aoColumns" : mData
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
						fnCallback(data.result)
					}
				});
			}

			offset++;
		} else {
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : '/hc/storage/hbase/query/result/' + jobId + '/',
				success : function(datas) {
					if (datas != null) {
						hbaseColumn = datas.column;
					}
				}
			});
		}
	}

});