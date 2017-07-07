$(document).ready(function() {
	var url = window.location.href;
	var tmp = url.split("mysql/")[1];
	var id = tmp.split("/")[0];

	$("#select2val").select2({
		placeholder : "database"
	});

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

	$.ajax({
		type : 'get',
		dataType : 'json',
		url : '/hc/storage/specify/' + id + '/db/ajax',
		success : function(datas) {
			if (datas != null) {
				$("#select2val").html("");
				$("#select2val").append("<option></option>");
				for (var i = 0; i < datas.db.length; i++) {
					$("#select2val").append("<option>" + datas.db[i] + "</option>");
				}
			}
		}
	});

	var offset = 0;
	$(document).on('click', 'a[name=run_task]', function() {
		var db = $("#select2val").val();
		if (db.length == 0) {
			sqlEditor.setValue("Please select a database first");
		} else if (sqlEditor.getValue().toLowerCase().indexOf("drop") > -1 || sqlEditor.getValue().toLowerCase().indexOf("update") > -1 || sqlEditor.getValue().toLowerCase().indexOf("insert") > -1) {
			sqlEditor.setValue("SQL [" + sqlEditor.getValue() + "] is not support");
		} else {
			var sql = sqlEditor.getValue();
			$.ajax({
				type : 'get',
				dataType : 'json',
				url : '/hc/storage/specify/' + id + '/column/ajax?db=' + db + '&sql=' + sql,
				success : function(datas) {
					if (datas != null) {
						var tabHeader = "<div class='panel-body' id='div_children" + offset + "'><table id='result_children" + offset + "' class='table table-bordered table-hover' width='100%'><thead><tr>"
						var mData = [];
						var i = 0;
						for (var i = 0; i < datas.column.length; i++) {
							tabHeader += "<th>" + datas.column[i] + "</th>";
							var obj = {
								mData : datas.column[i]
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
							"sAjaxSource" : '/hc/storage/specify/' + id + '/datasets/query/ajax?db=' + db + '&sql=' + sql,
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