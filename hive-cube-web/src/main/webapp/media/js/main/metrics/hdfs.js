$(document).ready(function() {
	var url = window.location.href;
	if (url.indexOf("?") > -1) {
		var path = url.split("?")[1];
		if (path.length == 0) {
			path = "root";
			hdfs(path);
		} else {
			hdfs(path.replace(/\//g, "________"));
		}
	} else {
		hdfs("root");
	}

	function hdfs(visit) {
		$("#result").dataTable({
			"bSort" : false,
			"bLengthChange" : false,
			"bProcessing" : true,
			"bServerSide" : true,
			"fnServerData" : retrieveData,
			"sAjaxSource" : "/hc/metrics/hdfs/" + visit + "/ajax",
			"aoColumns" : [ {
				"mData" : 'name'
			}, {
				"mData" : 'permisson'
			}, {
				"mData" : 'owner'
			}, {
				"mData" : 'group'
			}, {
				"mData" : 'size'
			}, {
				"mData" : 'replication'
			}, {
				"mData" : 'block_size'
			}, {
				"mData" : 'modify'
			}, {
				"mData" : 'children_num'
			}, {
				"mData" : 'type'
			}, {
				"mData" : 'operate'
			} ]
		});
	}

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
		var hdfsDelPath = href.split("#")[1];
		hdfsDelPath = hdfsDelPath.replace(/\//g, "________");
		$("#killed_div").html("");
		$("#killed_div").append("<a href='/hc/metrics/hdfs/" + hdfsDelPath + "/del' class='btn btn-danger'>DELETE</a>");
		$('#doc_info').modal('show');
	});

	$(document).on('click', 'a[name=hdfs_info]', function() {
		var href = $(this).attr("href");
		var hdfsDelPath = href.split("#")[1];
		hdfsDelPath = hdfsDelPath.replace(/\//g, "________");
		$('#hdfs_file_info').modal('show');
		getHdfsContext(hdfsDelPath);
	});

	function getHdfsContext(fName) {
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/metrics/hdfs/context/' + fName + '/ajax',
			success : function(datas) {
				if (datas != null) {
					$("#hdfs_context_info").text(datas.context);
					$("#hdfs_open").html("");
					$("#hdfs_open").append("<a href='" + datas.open + "' class='btn btn-primary btn-xs'><i class='fa fa-download fa-fw'></i> DOWNLOAD</a>");
				}
			}
		});
	}

});