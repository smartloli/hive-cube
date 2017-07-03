$(document).ready(function() {
	$("#result").dataTable({
		"bSort" : false,
		"bLengthChange" : false,
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerData" : retrieveData,
		"sAjaxSource" : "/mf/config/hbase/rowkey/table/ajax",
		"aoColumns" : [ {
			"mData" : 'tname'
		}, {
			"mData" : 'regular'
		}, {
			"mData" : 'author'
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

	$(document).on('click', 'a[name=operate_modal_regular]', function() {
		$('#mf_see_dialog').modal('show');
		var href = $(this).attr("href");
		var tname = href.split("#")[1];
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/config/hbase/rowkey/regular/' + tname + '/ajax',
			success : function(datas) {
				$("#mf_hbase_rowkey_see").val(formatJsonParser(datas.regular));
			}
		});
	});

	$(document).on('click', 'a[name=hbase_rowkey_add]', function() {
		$('#mf_add_dialog').modal('show');
	});
	
	$(document).on('click', 'a[name=operate_modal_edit_rowkey]', function() {
		$('#mf_add_dialog').modal('show');
		var href = $(this).attr("href");
		var tname = href.split("#")[1];
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/config/hbase/rowkey/regular/' + tname + '/ajax',
			success : function(datas) {
				$('#mf_hbase_table').val(datas.tname);
				$("#mf_hbase_rowkey_content").val(formatJsonParser(datas.regular));
			}
		});
	});

});