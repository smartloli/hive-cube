$(document).ready(function() {
	function health() {
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/hc/metrics/yarn/metrics/ajax',
			success : function(datas) {
				if (datas != null) {
					metrics(datas);
				}
			}
		});
	}

	function metrics(datas) {
		var metrics = datas.metrics;
		var nodes = datas.nodes;
		$("#metrics_tab").html("");
		$("#nodes_tab").html("");
		var metrics_tab = "<thead><tr><th>Apps<br/>Running</th><th>Containers<br/>Running</th><th>Memory<br/>Used</th><th>Memory<br/>Total</th><th>VCores<br/>Used</th><th>VCores<br/>Total</th><th>Active<br/>Nodes</th><th>Decommissioned<br/>Nodes</th><th>Lost<br/>Nodes</th><th>Unhealthy<br/>Nodes</th><th>Rebooted<br/>Nodes</th></tr></thead><tbody>";
		metrics_tab += " <tr><td>" + metrics.appsRunning + "</td><td>" + metrics.containersRunning + "</td><td>";
		// memory used
		if (metrics.memoryUsed * 1.0 / metrics.memoryTotal < 0.6) {
			metrics_tab += "<a class='btn btn-success btn-xs'>" + metrics.memoryUsed / 1024 + " GB</a>";
		} else if (metrics.memoryUsed * 1.0 / metrics.memoryTotal >= 0.6 && metrics.memoryUsed * 1.0 / metrics.memoryTotal < 0.8) {
			metrics_tab += "<a class='btn btn-warning btn-xs'>" + metrics.memoryUsed / 1024 + " GB</a>";
		} else {
			metrics_tab += "<a class='btn btn-danger btn-xs'>" + metrics.memoryUsed / 1024 + " GB</a>";
		}
		metrics_tab += "</td><td>" + metrics.memoryTotal / 1024 + " GB</td><td>";
		// vcores used
		if (metrics.vCoresUsed * 1.0 / metrics.vCoresTotal < 0.6) {
			metrics_tab += "<a class='btn btn-success btn-xs'>" + metrics.vCoresUsed + "</a>";
		} else if (metrics.vCoresUsed * 1.0 / metrics.vCoresTotal >= 0.6 && metrics.vCoresUsed * 1.0 / metrics.vCoresTotal < 0.8) {
			metrics_tab += "<a class='btn btn-warning btn-xs'>" + metrics.vCoresUsed + "</a>";
		} else {
			metrics_tab += "<a class='btn btn-danger btn-xs'>" + metrics.vCoresUsed + "</a>";
		}
		metrics_tab += "</td><td>" + metrics.vCoresTotal + "</td><td><a class='btn btn-success btn-xs'>" + metrics.activeNodes + "</a></td><td><a class='btn btn-primary btn-xs'>" + metrics.decommissionedNodes + "</a></td><td><a class='btn btn-danger btn-xs'>" + metrics.lostNodes + "</a></td><td><a class='btn btn-warning btn-xs'>" + metrics.unhealthyNodes + "</a></td><td><a class='btn btn-info btn-xs'>" + metrics.rebootedNodes + "</a></td></tr></tbody>";
		$("#metrics_tab").append(metrics_tab);

		var nodes_tab = "<thead><tr><th>Node<br/>State</th><th>Node<br/>Address</th><th>Node<br/>HttpAddress</th><th>Last<br/>Update</th><th>Health<br/>Report</th><th>Containers<br/>Running</th><th>Memory<br/>Used</th><th>Memory<br/>Avail</th><th>VCores<br/>Used</th><th>VCores<br/>Avail</th></tr></thead><tbody>";
		for (var i = 0; i < nodes.length; i++) {
			nodes_tab += "<tr><td>"
			if (nodes[i].nodeState == "RUNNING") {
				nodes_tab += "<a class='btn btn-success btn-xs'>" + nodes[i].nodeState + "</a>";
			} else if (nodes[i].nodeState == "UNHEALTHY") {
				nodes_tab += "<a class='btn btn-warning btn-xs'>" + nodes[i].nodeState + "</a>";
			} else if (nodes[i].nodeState == "DECOMMISSIONED") {
				nodes_tab += "<a class='btn btn-primary btn-xs'>" + nodes[i].nodeState + "</a>";
			} else if (nodes[i].nodeState == "LOST") {
				nodes_tab += "<a class='btn btn-danger btn-xs'>" + nodes[i].nodeState + "</a>";
			} else if (nodes[i].nodeState == "REBOOTED") {
				nodes_tab += "<a class='btn btn-info btn-xs'>" + nodes[i].nodeState + "</a>";
			}
			nodes_tab += "</td><td>" + nodes[i].nodeAddress + "</td><td>" + nodes[i].nodeHttpAddress + "</td><td>" + nodes[i].lastHealthUpdate + "</td><td>" + nodes[i].healthReport + "</td><td>" + nodes[i].containers + "</td><td>";
			// memory used
			if (nodes[i].memoryUsed * 1.0 / nodes[i].memoryAvail < 0.6) {
				nodes_tab += "<a class='btn btn-success btn-xs'>" + nodes[i].memoryUsed / 1024 + " GB</a>"
			} else if (nodes[i].memoryUsed * 1.0 / nodes[i].memoryAvail >= 0.6 && nodes[i].memoryUsed * 1.0 / nodes[i].memoryAvail < 0.8) {
				nodes_tab += "<a class='btn btn-warning btn-xs'>" + nodes[i].memoryUsed / 1024 + " GB</a>"
			} else {
				nodes_tab += "<a class='btn btn-danger btn-xs'>" + nodes[i].memoryUsed / 1024 + " GB</a>"
			}
			nodes_tab += "</td><td>" + nodes[i].memoryAvail / 1024 + " GB</td><td>";
			// vcores used
			if (nodes[i].vCoresUsed * 1.0 / nodes[i].vCoresAvail < 0.6) {
				nodes_tab += "<a class='btn btn-success btn-xs'>" + nodes[i].vCoresUsed + "</a>"
			} else if (nodes[i].vCoresUsed * 1.0 / nodes[i].vCoresAvail >= 0.6 && nodes[i].vCoresUsed * 1.0 / nodes[i].vCoresAvail < 0.8) {
				nodes_tab += "<a class='btn btn-warning btn-xs'>" + nodes[i].vCoresUsed + "</a>"
			} else {
				nodes_tab += "<a class='btn btn-danger btn-xs'>" + nodes[i].vCoresUsed + "</a>"
			}
			nodes_tab += "</td><td>" + nodes[i].vCoresAvail + "</td></tr>";
		}
		nodes_tab += "</tbody>"
		$("#nodes_tab").append(nodes_tab);
	}
	health();
	setInterval(health, 5000);
});