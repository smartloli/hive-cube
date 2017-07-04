<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="zh">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Hive - Hive Cube</title>
<jsp:include page="../public/css.jsp"></jsp:include>
<jsp:include page="../public/tcss.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Hive <small>overview</small>
					</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="alert alert-info alert-dismissable">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">×</button>
						<i class="fa fa-info-circle"></i> <strong>Administrators
							can edit the Hive table structure in the admin interface, modify
							the field notes and other operations.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-table fa-fw"></i> Content Edit
							<div class="pull-right">
								<a href="/hc/config/hive/sync/all/"
									class="btn btn-primary btn-xs">Sync-All</a>
							</div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>Table</th>
										<th>Alias</th>
										<th>Status</th>
										<th>Modify</th>
										<th>Operate</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- modal -->
			<div class="modal fade" aria-labelledby="hcModalLabelDelete"
				aria-hidden="true" id="hc_edit_dialog" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabelDelete">Modify Table
								Alias</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/config/hive/sync/one/" method="post"
							onsubmit="return contextFormValid();return false;">
							<div class="modal-body">
								<fieldset class="form-horizontal">
									<div class="form-group">
										<label for="path" class="col-sm-2 control-label">Alias</label>
										<div class="col-sm-10">
											<input id="hc_alias_hive_table" name="hc_alias_hive_table"
												class="form-control" placeholder="alias"> <input
												id="hc_name_hive_table" name="hc_name_hive_table"
												type="hidden">
										</div>
									</div>
									<div id="alert_edit_mssage" style="display: none"
										class="alert alert-danger">
										<label> Modify alias can not null .</label>
									</div>
								</fieldset>
							</div>
							<div id="remove_div" class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Cancle</button>
								<button type="submit" class="btn btn-primary" id="create-btn">Submit
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- end modal -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/config/hive.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_alias_hive_table = $("#hc_alias_hive_table").val();
		if (hc_alias_hive_table.length == 0) {
			$("#alert_edit_mssage").show();
			setTimeout(function() {
				$("#alert_edit_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
