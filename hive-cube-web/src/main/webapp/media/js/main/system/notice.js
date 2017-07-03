$(document).ready(function() {

	$(document).on('click', 'a[name=operater_modal]', function() {
		var href = $(this).attr("href");
		var id = href.split("#")[1];
		$('#mf_setting_dialog').modal('show');
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/system/role/resource/' + id + '/ajax',
			success : function(datas) {
				if (datas != null) {
					console.log(datas);
					$('#treeview-checkable').treeview({
						data : datas,
						showIcon : false,
						showCheckbox : true,
						onNodeChecked : function(event, node) {
							updateRole('/mf/system/role/insert/' + id + '/' + node.href + '/');
						},
						onNodeUnchecked : function(event, node) {
							updateRole('/mf/system/role/delete/' + id + '/' + node.href + '/');
						}
					});
				}
			}
		});
	});

	function updateRole(url) {
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : url,
			success : function(datas) {
				if (datas != null) {
					console.log(datas);
					$("#mf_role_alert_mssage").html("");
					$("#mf_role_alert_mssage").append("<label>" + datas.info + "</label>")
					$("#mf_role_alert_mssage").show();
					if (datas.code > 0) {
						$("#mf_role_alert_mssage").addClass("alert alert-success");
					} else {
						$("#mf_role_alert_mssage").addClass("alert alert-danger");
					}
					setTimeout(function() {
						$("#mf_role_alert_mssage").hide()
					}, 3000);
				}
			}
		});
	}

});