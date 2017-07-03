$(document).ready(function() {
	var url = window.location.href;
	var tmp = url.split("hive/")[1];
	var tableName = tmp.split("/")[0];
	$("#mf_table_name_head").find("strong").text(tableName);

	$("#result").dataTable({
		// "searching" : false,
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/mf/config/hive/columns/table/" + tableName + "/ajax",
		"aoColumns" : [ {
			"mData" : 'column_name'
		}, {
			"mData" : 'comment'
		}, {
			"mData" : 'type'
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

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var column = href.split("#")[1];
		$("#mf_name_hive_table").val(tableName);
		$("#mf_column_name_hive_table").val(column);
		$('#mf_edit_dialog').modal('show');
	});

});