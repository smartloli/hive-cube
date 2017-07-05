$(document).ready(function() {
	var chart = Morris.Area({
		element : 'morris-area-chart',
		data : [],
		xkey : 'period',
		ykeys : [ 'New Tasks' ],
		labels : [ 'New Tasks' ],
		lineColors : [ '#7cb47c' ],
		pointSize : 2,
		hideHover : 'auto',
		resize : true
	});

	function dashboard() {
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/dash/tasks/ajax',
			success : function(datas) {
				$('#task_finished_count').text(datas.panel.finished);
				$('#task_running_count').text(datas.panel.running);
				$('#task_queue_count').text(datas.panel.queue);
				$('#task_failed_count').text(datas.panel.failed);
				chart.setData(datas.chart);
				lastest10(datas.lastest);
			}
		});
	}

	function lastest10(datas) {
		var tab = "<thead><tr><th>ID</th><th>Name</th><th>Owner</th><th>Status</th><th>Size</th><th>Start Time</th><th>End Time</th></tr></thead><tbody>";
		for (var i = 0; i < datas.length; i++) {
			tab += "<tr><td>" + datas[i].id + "</td><td>" + datas[i].name + "</td><td>" + datas[i].owner + "</td><td>" + datas[i].status + "</td><td>" + datas[i].size + "</td><td>" + datas[i].stime + "</td><td>" + datas[i].etime + "</td></tr>"
		}
		tab += "</tbody>"
		$('#result').html("");
		$('#result').append(tab);
	}

	dashboard();
	setInterval(dashboard, 1000 * 60 * 1);

});