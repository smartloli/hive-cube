$(document).ready(function() {
	$("#result").dataTable({
		// "searching" : false,
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/mf/config/hive/all/table/ajax",
		"aoColumns" : [ {
			"mData" : 'tablename'
		}, {
			"mData" : 'aliasname'
		}, {
			"mData" : 'status'
		}, {
			"mData" : 'time'
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
		var tableName = href.split("#")[1];
		console.log(tableName);
		$("#mf_name_hive_table").val(tableName);
		$('#mf_edit_dialog').modal('show');
	});

});