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

<title><spring:message code="system.user.title"></spring:message></title>
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
						<spring:message code="system.user.content"></spring:message>
						<small><spring:message code="system.user.content.small"></spring:message></small>
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
								code="system.user.head.describer"></spring:message></strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i>
							<spring:message code="system.user.head.title"></spring:message>
							<div class="pull-right">
								<button id="mf-add-user-btn" type="button"
									class="btn btn-primary btn-xs">添加</button>
							</div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>工号</th>
										<th>英文名</th>
										<th>中文名</th>
										<th>邮箱</th>
										<th>操作</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- add modal -->
			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_user_add_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">
								<spring:message code="system.user.add"></spring:message>
							</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/mf/system/user/add/" method="post"
							onsubmit="return contextFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">工号</label>
									<div class="col-sm-9">
										<input id="mf_rtxno_name" name="mf_rtxno_name" type="text"
											class="form-control" placeholder="0001">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">中文名</label>
									<div class="col-sm-9">
										<input id="mf_real_name" name="mf_real_name" type="text"
											class="form-control" placeholder="张三">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">英文名</label>
									<div class="col-sm-9">
										<input id="mf_user_name" name="mf_user_name" type="text"
											class="form-control" placeholder="zhangsan">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">邮箱</label>
									<div class="col-sm-9">
										<input id="mf_user_email" name="mf_user_email" type="text"
											class="form-control" placeholder="zhangsan@email.com">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> Oops! Please make some changes .</label>
								</div>
							</fieldset>

							<div id="remove_div" class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">取消</button>
								<button type="submit" class="btn btn-primary" id="create-btn">确认
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- modal -->
			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_setting_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">
								<spring:message code="system.user.setting"></spring:message>
							</h4>
							<div id="alert_mssage_info"></div>
						</div>
						<!-- /.row -->
						<div class="modal-body">
							<div id="mf_role_list">
								
							</div>
						</div>
						<div id="remove_div" class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">关闭</button>
						</div>
					</div>
				</div>
			</div>
			<!-- /#page-wrapper -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/system/user.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var mf_rtxno_name = $("#mf_rtxno_name").val();
		var mf_real_name = $("#mf_real_name").val();
		var mf_user_name = $("#mf_user_name").val();
		var mf_user_email = $("#mf_user_email").val();
		if (mf_rtxno_name.length == 0 || mf_real_name.length == 0 || mf_user_name.length == 0 || mf_user_email.length == 0) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
