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

<title>Common - Hive Cube</title>
<jsp:include page="../public/css.jsp">
	<jsp:param value="plugins/select2/select2.min.css" name="css" />
	<jsp:param value="plugins/datatimepicker/daterangepicker.css"
		name="css" />
</jsp:include>
</head>
<style type="text/css">
.checkbox-inline {
	width: 200px;
	height: 20px;
	line-height: 20px;
	overflow: hidden;
}
</style>
<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Common Export <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Select the
							field of the hive data warehouse table for data export.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-file-text-o fa-fw"></i> Hive Table
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div id="hc_user_log_info">
										<form action="" method="post">
											<div class="form-group">
												<label>Table</label> <select id="select2val" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<label>Date</label>
												<div id="reportrange"
													style="position: absolute; top: -3px; left: 295px; width: 230px; background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc;">
													<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
													<span></span> <b class="caret"></b>
												</div>
											</div>
											<div class="well" id="hiveColumns" style="display: none;"></div>
											<div style="display: none" class="form-group">
												<label>Formatter</label> <select id="select2val_formatter"
													tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
											</div>
											<div class="form-group">
												<label>Filter</label> <select id="select2val_filter"
													tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<select id="select2val_filter_condition" tabindex="-1"
													style="width: 120px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<input id="hive_table_input_filter" class="form-control"
													style="position: relative; width: 100px; height: 28px; top: -28px; left: 366px;">
												<button id="btn_filter_condition" type="button"
													style="position: relative; top: -57px; left: 470px;"
													class="btn btn-primary btn-sm">Add</button>
												<div id="hive_table_info_tag"></div>
											</div>
										</form>
									</div>
								</div>
							</div>
							<!-- /.panel-body -->
						</div>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- export task settting -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tasks fa-fw"></i> Export Setting
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div>
										<form action="" method="post">
											<div class="form-group">
												<label class="control-label">Name</label> <input
													id="hc_hive_task_name" name="hc_hive_task_name" type="text"
													class="form-control"
													style="position: relative; width: 250px; top: -32px; left: 75px;">
												<button id="hive_btn_submit_task" type="button"
													class="btn btn-primary">Submit</button>
											</div>
											<div id="alert_mssage_common" style="display: none"
												class="alert alert-danger">
												<label> Submit content (partition or filter) can not
													null .</label>
											</div>
										</form>
									</div>
								</div>
							</div>
							<!-- /.panel-body -->
						</div>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- modal -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_task_alert_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Task Hint</h4>
						</div>
						<div class="modal-body">
							<div id="hc_task_result_dialog"></div>
						</div>
						<!-- /.row -->
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/export/common.js" name="loader" />
	<jsp:param value="plugins/select2/select2.min.js" name="loader" />
	<jsp:param value="plugins/datatimepicker/moment.min.js" name="loader" />
	<jsp:param value="plugins/datatimepicker/daterangepicker.js"
		name="loader" />
</jsp:include>
<script type="text/javascript">
	function labelClose(o) {
		$(o).parent().parent().remove();
	}
</script>
</html>
