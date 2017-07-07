<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>MySql - Hive Cube</title>
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
						MySQL <small>Manager</small>
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
						<i class="fa fa-info-circle"></i> <strong>Manager MySQL
							node information,you can add, modify or delete operate etc .</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<!-- Profile list -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-pencil"></i> MySQL Server Information
							<div class="pull-right">
								<button id="hc-add-mysql-btn" type="button"
									class="btn btn-primary btn-xs">Add</button>
							</div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>Host</th>
										<th>Port</th>
										<th>Modify</th>
										<th>Operate</th>
									</tr>
								</thead>
							</table>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<!-- /.col-lg-4 -->
			</div>
			<!-- row -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_user_mysql_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Add New MySql</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/storage/mysql/add/" method="post"
							onsubmit="return contextFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<div class="col-sm-9">
										<input id="hc_mysql_id" name="hc_mysql_id" type="hidden"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Host</label>
									<div class="col-sm-9">
										<input id="hc_mysql_host" name="hc_mysql_host" type="text"
											class="form-control" placeholder="127.0.0.1">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Port</label>
									<div class="col-sm-9">
										<input id="hc_mysql_port" name="hc_mysql_port" type="text"
											class="form-control" placeholder="3306">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">UserName</label>
									<div class="col-sm-9">
										<input id="hc_mysql_username" name="hc_mysql_username"
											type="text" class="form-control" placeholder="root">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Password</label>
									<div class="col-sm-9">
										<input id="hc_mysql_password" name="hc_mysql_password"
											type="text" class="form-control" placeholder="root">
									</div>
								</div>
								<div id="mysql_alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> Oops! Please make some changes .</label>
								</div>
							</fieldset>

							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Cancle</button>
								<button type="submit" class="btn btn-primary" id="create-btn">Submit
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/storage/mysql.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_mysql_host = $("#hc_mysql_host").val();
		var hc_mysql_port = $("#hc_mysql_port").val();
		var hc_mysql_username = $("#hc_mysql_username").val();
		var hc_mysql_password = $("#hc_mysql_password").val();
		if (hc_mysql_host.length == 0 || hc_mysql_port.length == 0 || hc_mysql_username.length == 0 || hc_mysql_password.length == 0) {
			$("#mysql_alert_mssage").show();
			setTimeout(function() {
				$("#mysql_alert_mssage").hide()
			}, 3000);
			return false;
		}
		return true;
	}
</script>
</html>
