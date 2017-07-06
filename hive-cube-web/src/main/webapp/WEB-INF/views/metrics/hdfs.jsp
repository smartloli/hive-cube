<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="zh">

<head>
<title>HDFS - Hive Cube</title>
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
						HDFS Browse Directory <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Show the list
							of hdfs file or directory for hadoop.</strong>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tasks fa-fw"></i> Files Info
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>Name</th>
										<th>Permission</th>
										<th>Owner</th>
										<th>Group</th>
										<th>Size</th>
										<th>Replication</th>
										<th>Block Size</th>
										<th>Modify</th>
										<th>Children Num</th>
										<th>Type</th>
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
								<p>Are you sure you want to delete it?
								<p>
							</div>
							<div id="killed_div" class="modal-footer"></div>
						</div>
					</div>
				</div>
				<!-- hdfs file detail -->
				<div class="modal fade" aria-labelledby="hdfsModalLabel"
					aria-hidden="true" id="hdfs_file_info" tabindex="-1" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button class="close" type="button" data-dismiss="modal">×</button>
								<h4 class="modal-title" id="hdfsModalLabel">HDFS File Info</h4>
							</div>
							<!-- /.row -->
							<div class="modal-body">
								<div class="row">
									<div class="col-lg-12">
										<div class="panel panel-default">
											<div class="panel-heading">
												<i class="fa fa-file fa-fw"></i> Context
												<div class="pull-right">
													<div id="hdfs_open"></div>
												</div>
											</div>
											<!-- /.panel-heading -->
											<div class="panel-body">
												<textarea id="hdfs_context_info" readonly="readonly"
													class="form-control" rows="3"
													style="margin: 0px -4px 0px 0px; height: 100px; resize: none"></textarea>
											</div>
											<!-- /.panel-body -->
										</div>
									</div>
									<!-- /.col-lg-4 -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/metrics/hdfs.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
</html>