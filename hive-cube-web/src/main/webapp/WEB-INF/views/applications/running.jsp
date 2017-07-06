<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh">

<head>
<title>Running - Hive Cube</title>
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
						Running Applications <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Show the
							running of tasks for yarn.</strong> Can be performed on the job to stop.
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tasks fa-fw"></i> Running Jobs Info
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>ID</th>
										<th>User</th>
										<th>Name</th>
										<th>App Type</th>
										<th>StartTime</th>
										<th>FinishTime</th>
										<th>State</th>
										<th>FinalStatus</th>
										<th>Progress</th>
										<th>Operate</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<!-- Killed -->
				<div class="modal fade" aria-labelledby="hfModalLabel"
					aria-hidden="true" id="doc_info" tabindex="-1" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">×</button>
								<h4 class="modal-title" id="hfModalLabel">Notify</h4>
							</div>
							<!-- /.row -->
							<div class="modal-body">
								<p>Are you sure you want to kill this job?
								<p>
							</div>
							<div id="killed_div" class="modal-footer"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/applications/running.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
</html>