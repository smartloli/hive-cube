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

<title><spring:message code="export.custom.title"></spring:message></title>
<jsp:include page="../public/css.jsp">
	<jsp:param value="plugins/select2/select2.min.css" name="css" />
</jsp:include>
</head>
<style>
.CodeMirror {
	border-top: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	border-right: 1px solid #ddd;
	border-left: 1px solid #ddd;
}

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
						<spring:message code="export.custom.query.edit"></spring:message>
						<small><spring:message
								code="export.custom.query.edit.small"></spring:message></small>
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
								code="export.custom.query.sql"></spring:message></strong>
						<spring:message code="export.custom.query.sql.demo"></spring:message>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tasks fa-fw"></i>
							<spring:message code="export.custom.query.sql.content"></spring:message>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div>
								<form>
									<textarea id="code" name="code"></textarea>
								</form>
							</div>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<!-- /.col-lg-4 -->
			</div>
			<!-- /.row -->
			<!-- user info from hbase -->
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
			<!-- submit -->
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
												<label class="control-label"><spring:message
														code="export.custom.columns.name"></spring:message></label> <input
													id="mf_hive_column_name" name="mf_hive_column_name"
													type="text" class="form-control"
													placeholder="用户ID,游戏币总数,充值金额总数,消费金币总数"
													style="position: relative; width: 500px; top: -34px; left: 75px;"><span
													style="color: red; position: relative; top: -34px; left: 80px;">(若为空,
													则默认使用SQL语句字段名.)</span>
												<button id="hive_btn_submit_task" type="button"
													style="position: relative; top: -7px; left: -234px;"
													class="btn btn-primary">提交任务</button>
											</div>
											<div id="alert_mssage_common" style="display: none"
												class="alert alert-danger">
												<label></label>
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

			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_task_alert_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">任务提示</h4>
						</div>
						<div class="modal-body">
							<div id="mf_task_result_dialog"></div>
						</div>
						<!-- /.row -->
						<div id="remove_div" class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /#page-wrapper -->
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/export/ucuser.js" name="loader" />
	<jsp:param value="plugins/select2/select2.min.js" name="loader" />
	<jsp:param value="main/export/custom.js" name="loader" />
	<jsp:param value="plugins/codemirror/codemirror.js" name="loader" />
	<jsp:param value="plugins/codemirror/sql.js" name="loader" />
	<jsp:param value="plugins/codemirror/show-hint.js" name="loader" />
	<jsp:param value="plugins/codemirror/sql-hint.js" name="loader" />
</jsp:include>
</html>
