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

<title>Dashboard - Hive Cube</title>
<jsp:include page="../public/css.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Dashboard <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>The data hive
							cube control panel displays export tasks, such as approval tasks,
							running tasks, queuing tasks, and failed tasks. Also, visualize
							the historical trend diagram (new tasks). At the same time, list
							the details of the last ten tasks.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-tasks fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div id="task_running_count" class="huge">0</div>
									<div>Running</div>
								</div>
							</div>
						</div>
						<a href="/hc/tasks/public">
							<div class="panel-footer">
								<span class="pull-left">Detail</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-comment-o fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div id="task_finished_count" class="huge">0</div>
									<div>Finished</div>
								</div>
							</div>
						</div>
						<a href="/hc/tasks/public">
							<div class="panel-footer">
								<span class="pull-left">Detail</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-yellow">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-sitemap fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div id="task_queue_count" class="huge">0</div>
									<div>Queue</div>
								</div>
							</div>
						</div>
						<a href="/hc/tasks/public">
							<div class="panel-footer">
								<span class="pull-left">Detail</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-users fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div id="task_failed_count" class="huge">0</div>
									<div>Failed</div>
								</div>
							</div>
						</div>
						<a href="/hc/tasks/public">
							<div class="panel-footer">
								<span class="pull-left">Detail</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">
								<i class="fa fa-bar-chart-o fa-fw"></i> Today Tasks
							</h3>
						</div>
						<div class="panel-body">
							<div id="morris-area-chart"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">
								<i class="fa fa-table fa-fw"></i> Lastest 10
							</h3>
						</div>
						<div class="panel-body">
							<div class="table-responsive">
								<table id="result"
									class="table table-bordered table-hover table-striped">
								</table>
							</div>
							<div class="text-right">
								<a href="/hc/tasks/public">Scan All Task <i
									class="fa fa-arrow-circle-right"></i></a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/index.js" name="loader" />
</jsp:include>
</html>
