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

<title><spring:message code="export.common.title"></spring:message></title>
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

.ucuser .checkbox-inline+.checkbox-inline {
	margin-left: 0;
}

.ucuser .checkbox-inline {
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
						<spring:message code="export.common.header"></spring:message>
						<small><spring:message code="export.common.header.small"></spring:message></small>
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
						<i class="fa fa-info-circle"></i> <strong><spring:message
								code="export.common.header.context"></spring:message></strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<!-- hive log table -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i>
							<spring:message code="export.common.upload.switch"></spring:message>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div class="form-group">
										<label><spring:message
												code="export.common.upload.whether"></spring:message> </label> <input
											type="checkbox" name="userid-upload-switch" checked>
									</div>

									<div id="mf-btn-upload" style="display: none"
										class="form-group">
										<label><spring:message code="export.common.upload.btn"></spring:message></label>
										<button id="mf_upload_file" type="button"
											class="btn btn-default btn-sm">上次文件</button>
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
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-file-text-o fa-fw"></i>
							<spring:message code="export.common.log.info"></spring:message>
							<input type="checkbox" name="userid-log-switch" checked>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div id="mf_user_log_info">
										<form action="" method="post">
											<div class="form-group">
												<label><spring:message
														code="export.common.table.select"></spring:message></label> <select
													id="select2val" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<label><spring:message code="export.common.date"></spring:message></label>
												<div id="reportrange"
													style="position: absolute; top: -3px; left: 315px; width: 225px; background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc;">
													<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
													<span></span> <b class="caret"></b>
												</div>
												<div style="position: absolute; top: 0px; left: 544px">
													<input id="hive_realtime_data" type="checkbox" value="">实时数据
												</div>
											</div>
											<div class="well" id="hiveColumns" style="display: none;"></div>
											<div style="display: none" class="form-group">
												<label><spring:message
														code="export.common.date.formatter"></spring:message></label> <select
													id="select2val_formatter" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<label><spring:message
														code="export.common.date.formatter.style"></spring:message></label>
												<select id="select2val_formatter_date" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<button id="btn_formatter" type="button"
													class="btn btn-primary btn-sm">添加</button>
											</div>
											<div class="form-group">
												<label><spring:message
														code="export.common.date.filter"></spring:message></label> <select
													id="select2val_filter" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<select id="select2val_filter_condition" tabindex="-1"
													style="width: 120px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<input id="hive_table_input_filter" class="form-control"
													style="position: relative; width: 100px; height: 28px; top: -28px; left: 388px;">
												<button id="btn_filter_condition" type="button"
													style="position: relative; top: -57px; left: 493px;"
													class="btn btn-primary btn-sm">添加</button>
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
			<!-- user detail information -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-user fa-fw"></i>
							<spring:message code="export.common.user.info"></spring:message>
							<input type="checkbox" name="userid-user-switch" checked>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div id="mf_user_user_info" style="display: none;">
										<form action="" method="post">
											<input type="checkbox" id="checkall"
												style="margin: 2px 6px 3px 0; vertical-align: middle;">
											<label><spring:message
													code="export.common.task.user.checkall"></spring:message>[<span
												style="color: green; font-weight: bold;"><spring:message
														code="export.common.task.user.checkall.describe"></spring:message></span>]：
											</label>
											<div class="well ucuser" id="hbase_user_info"
												style="display: none;"></div>
											<div style="height: 30px" class="form-group">
												<label><spring:message
														code="export.common.date.filter"></spring:message></label> <select
													id="select2val_filter_hbase" tabindex="-1"
													style="width: 200px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<select id="select2val_filter_condition_hbase" tabindex="-1"
													style="width: 120px; font-family: 'Microsoft Yahei', 'HelveticaNeue', Helvetica, Arial, sans-serif; font-size: 1px;"></select>
												<input class="form-control"
													style="position: relative; width: 100px; height: 28px; top: -28px; left: 388px;">
												<button id="btn_filter_condition_hbase" type="button"
													style="position: relative; top: -57px; left: 493px;"
													class="btn btn-primary btn-sm">添加</button>
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
							<i class="fa fa-tasks fa-fw"></i>
							<spring:message code="export.common.task.setting"></spring:message>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12">
									<div>
										<form action="" method="post">
											<div class="form-group">
												<label class="control-label"><spring:message
														code="export.common.task.name"></spring:message></label> <input
													id="mf_hive_task_name" name="mf_hive_task_name" type="text"
													class="form-control"
													style="position: relative; width: 250px; top: -32px; left: 75px;">
												<button id="hive_btn_submit_task" type="button"
													class="btn btn-primary">提交任务</button>
											</div>
											<div id="alert_mssage_common" style="display: none"
												class="alert alert-danger">
												<label> 提交内容不能为空 .</label>
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
			<div id="batchImportModal" class="modal fade" role="dialog"
				aria-labelledby="gridSystemModalLabel">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
						</div>
						<div class="modal-body">
							<form role="form" action="javascript:void(0)">
								<div class="form-group">
									<label>文件选择</label> <input id="file" class="form-control"
										name="file" type="file">
								</div>
								<div class="progress progress-striped active"
									style="display: none">
									<div id="progressBar" class="progress-bar progress-bar-info"
										role="progressbar" aria-valuemin="0" aria-valuenow="0"
										aria-valuemax="100" style="width: 0%"></div>
								</div>

								<div class="form-group">
									<button id="uploadBtn" type="submit" class="btn btn-success">上传文件</button>
								</div>

								<div class="form-group">
									<div id="alert_mssage_failed" style="display: none"
										class="alert alert-danger"></div>
									<div id="alert_mssage_success" style="display: none"
										class="alert alert-success"></div>
									<div id="alert_mssage" style="display: none"
										class="alert alert-danger">
										<label> 标签不能为空 .</label>
									</div>
								</div>

							</form>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /#page-wrapper -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/export/ucuser.js" name="loader" />
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
