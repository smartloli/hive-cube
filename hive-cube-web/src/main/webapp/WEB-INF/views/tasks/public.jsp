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

<title><spring:message code="tasks.public.title"></spring:message></title>
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
						<spring:message code="tasks.public.content"></spring:message>
						<small><spring:message code="tasks.public.content.small"></spring:message></small>
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
								code="tasks.public.head.describer"></spring:message></strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i> 任务详情
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<table id="result" class="table table-bordered table-condensed"
								width="100%">
								<thead>
									<tr>
										<th>任务ID</th>
										<th>任务名称</th>
										<th>操作用户</th>
										<th>执行状态</th>
										<th>日志信息</th>
										<th>执行结果</th>
										<th>进度</th>
										<th>文件大小</th>
										<th>开始执行时间</th>
										<th>结束执行时间</th>
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
			<!-- edit modal -->
			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_task_edit_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">任务编辑</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/mf/tasks/content/modify/" method="post"
							onsubmit="return contextFormValid();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">任务名称</label>
									<div class="col-sm-9">
										<input id="mf_task_name" name="mf_task_name" type="text"
											class="form-control"> <input id="mf_task_id"
											name="mf_task_id" type="hidden" class="form-control">
										<input id="mf_task_ref" name="mf_task_ref" type="hidden"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">接收邮件</label>
									<div class="col-sm-9">
										<input id="mf_task_email" name="mf_task_email" type="text"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">执行内容</label>
									<div class="col-sm-9">
										<textarea id="mf_task_content" name="mf_task_content"
											class="form-control" rows="20"></textarea>
									</div>
								</div>
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">输出字段</label>
									<div class="col-sm-9">
										<input id="mf_task_column" name="mf_task_column" type="text"
											class="form-control">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> 输入内容不能为空 .</label>
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
			<!-- auto -->
			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_task_auto_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">定时调度</h4>
						</div>
						<!-- /.row -->
						<form role="form" action="/mf/tasks/auto/quartz/modify/" method="post"
							onsubmit="return contextFormValidQuartz();return false;">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">Quartz</label>
									<div class="col-sm-9">
										<input id="mf_task_quartz" name="mf_task_quartz" type="text"
											class="form-control" placeholder="* * * * * ?"> <input
											id="mf_task_name_id" name="mf_task_name_id" type="hidden"
											class="form-control"> <input id="mf_task_name_ref"
											name="mf_task_name_ref" type="hidden" class="form-control">
											<input id="mf_task_name_action" name="mf_task_name_action"
											type="hidden" class="form-control" value="add">
									</div>
								</div>
								<div class="form-group">
									<div style="left: 40px"
										class="alert alert-info alert-dismissable col-sm-10">
										<button type="button" class="close" data-dismiss="alert"
											aria-hidden="true">×</button>
										<i class="fa fa-info-circle"></i> <strong>格式: [秒] [分]
											[小时] [日] [月] [周] [年]</strong><br> 0 0 12 * * ? 每天12点触发<br> 0
										15 10 ? * * 每天10点15分触发<br> 0 0/5 14 * * ?
										每天14点到14点59分(整点开始,每隔5分触发)<br> 0 0-5 14 * * ?
										每天14点到14点05分(每分触发)<br>
									</div>
								</div>
								<div id="alert_mssage_auto" style="display: none"
									class="alert alert-danger">
									<label> Cron 表达式不能为空 .</label>
								</div>
							</fieldset>
							<div class="modal-footer">
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
			<!-- task executor info -->
			<div class="modal fade" aria-labelledby="mfModalLabel"
				aria-hidden="true" id="mf_task_exec_info_dialog" tabindex="-1"
				role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button class="close" type="button" data-dismiss="modal">×</button>
							<h4 class="modal-title" id="mfModalLabel">任务详情</h4>
						</div>
						<!-- /.row -->
						<fieldset class="form-horizontal">
							<div class="form-group">
								<label for="path" class="col-sm-2 control-label">任务日志</label>
								<div class="col-sm-9">
									<textarea id="mf_task_log_info" name="mf_task_log_info"
										class="form-control" rows="20"></textarea>
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/tasks/mf.json.parse.js" name="loader" />
	<jsp:param value="main/tasks/public.js" name="loader" />
</jsp:include>
<jsp:include page="../public/tscript.jsp"></jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var mf_task_name = $("#mf_task_name").val();
		var mf_task_email = $("#mf_task_email").val();
		var mf_task_content = $("#mf_task_content").val();
		var mf_task_column = $("#mf_task_column").val();
		if (mf_task_name.length == 0 || mf_task_email.length == 0 || mf_task_content.length == 0 || mf_task_column.length == 0) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}
		
		return true;
	}

	function contextFormValidQuartz() {
		var mf_task_quartz = $("#mf_task_quartz").val();
		if (mf_task_quartz.length == 0) {
			$("#alert_mssage_auto").show();
			setTimeout(function() {
				$("#alert_mssage_auto").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
