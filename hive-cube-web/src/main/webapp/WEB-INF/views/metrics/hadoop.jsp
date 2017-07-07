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

<title>Hadoop - Hive Cube</title>
<jsp:include page="../public/css.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Hadoop Nodes <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Display hive
							client connection address and Hadoop node information</strong> If you
						don't know the usage of Hadoop and Hive, you can visit the website
						of <a href="http://hadoop.apache.org/" target="_blank"
							class="alert-link">Hadoop</a> to view the relevant usage.
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tag fa-fw"></i> <a id="hdp_version"
								class="btn btn-info btn-xs">NaN</a>
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-4">
									<div class="panel panel-default">
										<div class="panel-heading">
											<i class="fa fa-database fa-fw"></i> Hadoop Capacity
											<div class="pull-right"></div>
										</div>
										<!-- /.panel-heading -->
										<div class="panel-body">
											<div id="hdp-capacity"></div>
										</div>
										<!-- /.panel-body -->
									</div>
								</div>
								<!-- /.col-lg-4 -->
								<div class="col-lg-4">
									<div class="panel panel-default">
										<div class="panel-heading">
											<i class="fa fa-adjust fa-fw"></i> Hadoop DFS
											<div class="pull-right"></div>
										</div>
										<!-- /.panel-heading -->
										<div class="panel-body">
											<div id="hdp-dfs"></div>
										</div>
										<!-- /.panel-body -->
									</div>
								</div>
								<!-- /.col-lg-4 -->
								<div class="col-lg-4">
									<div class="panel panel-default">
										<div class="panel-heading">
											<i class="fa fa-calendar fa-fw"></i> Hadoop Run Days (Day)
											<div class="pull-right"></div>
										</div>
										<!-- /.panel-heading -->
										<div class="panel-body">
											<div id="hdp-run-days"></div>
										</div>
										<!-- /.panel-body -->
									</div>
								</div>
								<!-- /.col-lg-4 -->
							</div>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<!-- /.col-lg-4 -->
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-sitemap fa-fw"></i> Hadoop Nodes Info
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div id="zookeeper_cluster_info">
								<table id="nodes_tab" class="table table-bordered table-hover">
								</table>
							</div>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<!-- /.col-lg-4 -->
			</div>
			<!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/metrics/hadoop.js" name="loader" />
</jsp:include>
</html>
