$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/hc/applications/yarn/failed/ajax",
		"aoColumns" : [ {
			"mData" : 'id'
		}, {
			"mData" : 'user'
		}, {
			"mData" : 'name'
		}, {
			"mData" : 'app_type'
		}, {
			"mData" : 'start_time'
		}, {
			"mData" : 'finish_time'
		}, {
			"mData" : 'state'
		}, {
			"mData" : 'final_status'
		}, {
			"mData" : 'progress'
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
	
});