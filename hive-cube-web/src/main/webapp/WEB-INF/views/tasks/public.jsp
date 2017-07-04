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

<title>Public - Hive Cube</title>
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
						Public Task <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Show all
							current submitted tasks, edit, execute, etc. Administrators can
							have priority to enforce permissions.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i> Task Describer
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>ID</th>
										<th>Name</th>
										<th>Owner</th>
										<th>Status</th>
										<th>Log</th>
										<th>Result</th>
										<th>Progress</th>
										<th>Size</th>
										<th>Start Time</th>
										<th>End Time</th>
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
			<!-- edit modal -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_task_edit_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Task Edit</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/tasks/content/modify/" method="post"
							onsubmit="return contextFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Name</label>
									<div class="col-sm-9">
										<input id="hc_task_name" name="hc_task_name" type="text"
											class="form-control"> <input id="hc_task_id"
											name="hc_task_id" type="hidden" class="form-control">
										<input id="hc_task_ref" name="hc_task_ref" type="hidden"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Email</label>
									<div class="col-sm-9">
										<input id="hc_task_email" name="hc_task_email" type="text"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Content</label>
									<div class="col-sm-9">
										<textarea id="hc_task_content" name="hc_task_content"
											class="form-control" rows="20"></textarea>
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Columns</label>
									<div class="col-sm-9">
										<input id="hc_task_column" name="hc_task_column" type="text"
											class="form-control">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> Input content can not null .</label>
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
			<!-- auto -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_task_auto_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Crontab Config</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/tasks/auto/quartz/modify/"
							method="post"
							onsubmit="return contextFormValidQuartz();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Quartz</label>
									<div class="col-sm-9">
										<input id="hc_task_quartz" name="hc_task_quartz" type="text"
											class="form-control" placeholder="* * * * * ?"> <input
											id="hc_task_name_id" name="hc_task_name_id" type="hidden"
											class="form-control"> <input id="hc_task_name_ref"
											name="hc_task_name_ref" type="hidden" class="form-control">
										<input id="hc_task_name_action" name="hc_task_name_action"
											type="hidden" class="form-control" value="add">
									</div>
								</div>
								<div class="form-group">
									<div style="left: 40px"
										class="alert alert-info alert-dismissable col-sm-10">
										<button type="button" class="close" data-dismiss="alert"
											aria-hidden="true">×</button>
										<i class="fa fa-info-circle"></i> <strong>Formmat:
											[s] [m] [h] [d] [m] [w] [y]</strong><br> 0 0 12 * * ? It's
										triggered at 12:00 every day<br> 0 15 10 ? * * It's
										triggered at 10:15 every day<br> 0 0/5 14 * * ? From
										14:00 to 14:59 every day (starting at 5 every minute)<br>
										0 0-5 14 * * ? 14:00 to 14:05 every day (triggered by each
										minute)<br>
									</div>
								</div>
								<div id="alert_mssage_auto" style="display: none"
									class="alert alert-danger">
									<label> Cron expression cannot be empty .</label>
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
			<!-- modal -->
			<!-- task executor info -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_task_exec_info_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Task Detail</h4>
						</div>
						<!-- /.row -->
						<fieldset class="form-horizontal">
							<div class="form-group">
								<label for="path" class="col-sm-2 control-label">Logger</label>
								<div class="col-sm-9">
									<textarea id="hc_task_log_info" name="hc_task_log_info"
										class="form-control" rows="20"></textarea>
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/tasks/hc.json.parse.js" name="loader" />
	<jsp:param value="main/tasks/public.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_task_name = $("#hc_task_name").val();
		var hc_task_email = $("#hc_task_email").val();
		var hc_task_content = $("#hc_task_content").val();
		var hc_task_column = $("#hc_task_column").val();
		if (hc_task_name.length == 0 || hc_task_email.length == 0 || hc_task_content.length == 0 || hc_task_column.length == 0) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}

	function contextFormValidQuartz() {
		var hc_task_quartz = $("#hc_task_quartz").val();
		if (hc_task_quartz.length == 0) {
			$("#alert_mssage_auto").show();
			setTimeout(function() {
				$("#alert_mssage_auto").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
