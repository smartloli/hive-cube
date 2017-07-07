$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/hc/storage/plugins/mysql/table/ajax",
		"aoColumns" : [ {
			"mData" : 'host'
		}, {
			"mData" : 'port'
		}, {
			"mData" : 'modify'
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

	$("#hc-add-mysql-btn").click(function() {
		$('#hc_user_mysql_dialog').modal('show');
	});

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#hc_user_mysql_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/storage/find/' + id + '/ajax',
			success : function(datas) {
				console.log(datas)
				$("#hc_mysql_id").val(datas.id);
				$("#hc_mysql_host").val(datas.host);
				$("#hc_mysql_port").val(datas.port);
				$("#hc_mysql_username").val(datas.username);
				$("#hc_mysql_password").val(datas.password);
			}
		});
	})
});