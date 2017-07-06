$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/hc/applications/yarn/all/ajax",
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

	$(document).on('click', 'a[name=killed]', function() {
		var href = $(this).attr("href");
		var appId = href.split("#")[1];
		$("#killed_div").html("");
		$("#killed_div").append("<a href='/hc/applications/yarn/all/" + appId + "/del' class='btn btn-danger'>Killed</a>");
		$('#doc_info').modal('show');
	});

});