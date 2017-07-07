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

<title>User - Hive Cube</title>
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
						User Manager <small>overview</small>
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
							can perform actions such as adding, modifying, and deleting users
							and assigning roles to the user.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i> User
							<div class="pull-right">
								<button id="hc-add-user-btn" type="button"
									class="btn btn-primary btn-xs">Add</button>
							</div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>No</th>
										<th>Name</th>
										<th>Alias</th>
										<th>Email</th>
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
			<!-- add modal -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_user_add_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Add New User</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/system/user/add/" method="post"
							onsubmit="return contextFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">No</label>
									<div class="col-sm-9">
										<input id="hc_rtxno_name" name="hc_rtxno_name" type="text"
											class="form-control" placeholder="0001">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Alias</label>
									<div class="col-sm-9">
										<input id="hc_real_name" name="hc_real_name" type="text"
											class="form-control" placeholder="chinese name">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Name</label>
									<div class="col-sm-9">
										<input id="hc_user_name" name="hc_user_name" type="text"
											class="form-control" placeholder="smartloli">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Email</label>
									<div class="col-sm-9">
										<input id="hc_user_email" name="hc_user_email" type="text"
											class="form-control" placeholder="smartloli@org.com">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> Oops! Please make some changes .</label>
								</div>
							</fieldset>

							<div id="remove_div" class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Cancle</button>
								<button type="submit" class="btn btn-primary" id="create-btn">Sure
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- modal -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_setting_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Authority
								Assignment</h4>
							<div id="alert_mssage_info"></div>
						</div>
						<!-- /.row -->
						<div class="modal-body">
							<div id="hc_role_list"></div>
						</div>
						<div id="remove_div" class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
			<!-- modal modify -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_user_modify_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Modify User</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/system/user/modify/" method="post"
							onsubmit="return contextModifyFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">No</label>
									<div class="col-sm-9">
										<input id="hc_user_id_modify" name="hc_user_id_modify"
											type="hidden" class="form-control" placeholder="1000">
										<input id="hc_rtxno_name_modify" name="hc_rtxno_name_modify"
											type="text" class="form-control" placeholder="1000">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">RealName</label>
									<div class="col-sm-9">
										<input id="hc_real_name_modify" name="hc_real_name_modify"
											type="text" class="form-control" placeholder="chinese name">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">UserName</label>
									<div class="col-sm-9">
										<input id="hc_user_name_modify" name="hc_user_name_modify"
											type="text" class="form-control" placeholder="smartloli">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Email</label>
									<div class="col-sm-9">
										<input id="hc_user_email_modify" name="hc_user_email_modify"
											type="text" class="form-control"
											placeholder="smartloli@gmail.com">
									</div>
								</div>
								<div id="alert_mssage_modify" style="display: none"
									class="alert alert-danger">
									<label> Oops! Please make some changes .</label>
								</div>
							</fieldset>

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
			<!-- /#page-wrapper -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/system/user.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_rtxno_name = $("#hc_rtxno_name").val();
		var hc_real_name = $("#hc_real_name").val();
		var hc_user_name = $("#hc_user_name").val();
		var hc_user_email = $("#hc_user_email").val();
		if (hc_rtxno_name.length == 0 || hc_real_name.length == 0 || hc_user_name.length == 0 || hc_user_email.length == 0) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
