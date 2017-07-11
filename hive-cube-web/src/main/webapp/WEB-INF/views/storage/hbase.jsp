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

<title>HBase - Hive Cube</title>
<jsp:include page="../public/css.jsp">
	<jsp:param value="plugins/select2/select2.min.css" name="css" />
</jsp:include>
<jsp:include page="../public/tcss.jsp"></jsp:include>
</head>
<style>
.CodeMirror {
	border-top: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	border-right: 1px solid #ddd;
	border-left: 1px solid #ddd;
}
</style>
<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						HBase <small>Shell</small>
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
						<i class="fa fa-info-circle"></i> <strong>Write the sql
							statement to manipulate the HBase .</strong> Example SQL : select * from
						"hbase_user" where "rowkey">'001040000002620' and
						"rowkey"<'001040000002658'
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-tasks"></i> HBase SQL
							<div class="pull-right">
								<a id="run_task" name="run_task" href="#"
									class="btn btn-primary btn-xs"><i class="fa fa-play"></i>
									Run</a>
							</div>
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
			<!-- Redis data list -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-table"></i> HBase Result Information
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<div id="result_textarea"></div>
						</div>
						<!-- /.panel-body -->
					</div>
				</div>
				<!-- /.col-lg-4 -->
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/storage/hbase.js" name="loader" />
	<jsp:param value="plugins/select2/select2.min.js" name="loader" />
	<jsp:param value="plugins/codemirror/codemirror.js" name="loader" />
	<jsp:param value="plugins/codemirror/sql.js" name="loader" />
	<jsp:param value="plugins/codemirror/show-hint.js" name="loader" />
	<jsp:param value="plugins/codemirror/sql-hint.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
</html>
