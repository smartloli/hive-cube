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

	var offset = 0;
	$(document).on('click', 'a[name=run_task]', function() {
		if (sqlEditor.getValue().toLowerCase().indexOf("drop") > -1 || sqlEditor.getValue().toLowerCase().indexOf("update") > -1 || sqlEditor.getValue().toLowerCase().indexOf("insert") > -1) {
			sqlEditor.setValue("SQL [" + sqlEditor.getValue() + "] is not support");
		} else {
			var sql = sqlEditor.getValue();
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : '/hc/storage/specify/hbase/schema/ajax?sql=' + sql,
				success : function(datas) {
					if (datas) {
						console.log(datas);
						var tabHeader = "<div class='panel-body' id='div_children" + offset + "'><table id='result_children" + offset + "' class='table table-bordered table-hover' width='100%'><thead><tr>"
						var mData = [];
						var i = 0;
						for (var i = 0; i < datas.columns.length; i++) {
							tabHeader += "<th>" + datas.columns[i] + "</th>";
							var obj = {
								mData : datas.columns[i]
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
							"sAjaxSource" : '/hc/storage/hbase/query/result/ajax/?sql=' + sql,
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
									fnCallback(data)
								}
							});
						}

						offset++;
					}
				}
			});
		}
	});

});