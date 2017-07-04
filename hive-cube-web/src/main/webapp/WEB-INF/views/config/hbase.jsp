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

<title>HBase - Hive Cube</title>
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
						HBase Schema <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Manage the
							Schema generation rules of the HBase table, which can be added,
							modified, deleted, and so on. Use sql to query HBase table data.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-table fa-fw"></i> HBase Content
							<div class="pull-right">
								<a name="hbase_rowkey_add" href="#"
									class="btn btn-primary btn-xs">Add</a>
							</div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>HTable</th>
										<th>Regular</th>
										<th>Owner</th>
										<th>Modify</th>
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
			<!-- modal -->
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_add_dialog" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">Add HBase Schema</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/hc/config/hbase/rowkey/add/"
							method="post" onsubmit="return contextFormValid();return false;">
							<div class="modal-body">
								<fieldset class="form-horizontal">
									<div class="form-group">
										<label for="path" class="col-sm-2 control-label">
											Table</label>
										<div class="col-sm-10">
											<input id="hc_hbase_table" name="hc_hbase_table"
												class="form-control" placeholder="HBase Table">
										</div>
									</div>
									<div class="form-group">
										<label for="path" class="col-sm-2 control-label">规则</label>
										<div class="col-sm-10">
											<textarea id="hc_hbase_rowkey_content"
												name="hc_hbase_rowkey_content" class="form-control"
												rows="20"></textarea>
										</div>
									</div>
									<div id="alert_add_mssage" style="display: none"
										class="alert alert-danger">
										<label> The table name and rule submitted cannot be
											empty.</label>
									</div>
								</fieldset>
							</div>
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
			<div class="modal fade" aria-labelledby="hcModalLabel"
				aria-hidden="true" id="hc_see_dialog" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="hcModalLabel">View HBase Schema
							</h4>
						</div>
						<!-- /.row -->
						<div class="modal-body">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Regular</label>
									<div class="col-sm-10">
										<textarea id="hc_hbase_rowkey_see" name="hc_hbase_rowkey_see"
											class="form-control" rows="20"></textarea>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
			<!-- end modal -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/tasks/hc.json.parse.js" name="loader" />
	<jsp:param value="main/config/hbase.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_hbase_table = $("#hc_hbase_table").val();
		var hc_hbase_rowkey_content = $("#hc_hbase_rowkey_content").val();
		if (hc_hbase_table.length == 0 || hc_hbase_rowkey_content.length == 0) {
			$("#alert_add_mssage").show();
			setTimeout(function() {
				$("#alert_add_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
