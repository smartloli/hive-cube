$(document).ready(function() {
	$.ajax({
		type : 'get',
		dataType : 'json',
		url : '/hc/metrics/hadoop/nodes/info/ajax',
		success : function(datas) {
			if (datas != null) {
				var nodes = datas.nodes;
				$("#nodes_tab").html("");

				var nodes_tab = "<thead><tr><th>Node</th><th>Admin State</th><th>Capacity</th><th>Used</th><th>Non DFS Used</th><th>Remaining</th><th>Block pool used</th><th>Lastest Time</th></tr></thead><tbody>";
				for (var i = 0; i < nodes.length; i++) {
					nodes_tab += " <tr><td>" + nodes[i].hostname + "</td><td>" + nodes[i].state + "</td><td>" + nodes[i].capacity + "</td><td>" + nodes[i].used + "</td><td>" + nodes[i].non_dfs_used + "</td><td>" + nodes[i].remaining + "</td><td>" + nodes[i].block_pool_used + "</td><td>" + nodes[i].update_date + "</td></tr>";
				}
				nodes_tab += "</tbody>"
				$("#nodes_tab").append(nodes_tab);
			}
		}
	});
	
	$.ajax({
		type : 'get',
		dataType : 'json',
		url : '/hc/metrics/hadoop/chart/info/ajax',
		success : function(datas) {
			if (datas != null) {
				console.log(datas);
				$("#hdp_version").html("Hadoop "+datas.version);
				Morris.Donut({
					element : 'hdp-capacity',
					data : [ {
						label : "Hadoop Capacity ("+datas.unit+")",
						value : datas.capacity
					} ],
					resize : true
				});
				
				Morris.Donut({
					element : 'hdp-dfs',
					data : [ {
						label : "Dfs Used ("+datas.unit+")",
						value : datas.dfsUsed
					} ,{
						label : "Non DFS Used ("+datas.unit+")",
						value : datas.nonDFSUsed
					} ,{
						label : "Dfs Remaining ("+datas.unit+")",
						value : datas.dfsRemaining
					} ],
					colors : [ '#3c763d', '#5cb85c','#7cb47c' ],
					resize : true
				});
				
				Morris.Donut({
					element : 'hdp-run-days',
					data : [ {
						label : "Hadoop Run Days",
						value : datas.clusterStartTime
					} ],
					colors : [ '#6DA398'],
					resize : true
				});
			}
		}
	});
	
});