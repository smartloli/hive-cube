<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="/mf"><i class="fa fa-cubes fa-fw"></i>
			<spring:message code="navbar.title"></spring:message></a>
		<div class="modal fade" aria-labelledby="mfModalLabel"
			aria-hidden="true" id="mf_account_reset_dialog" tabindex="-1"
			role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button class="close" type="button" data-dismiss="modal">×</button>
						<h4 class="modal-title" id="mfModalLabel">
							<spring:message code="login.reset"></spring:message>
						</h4>
					</div>
					<!-- /.row -->
					<form role="form" action="/mf/account/reset/" method="post"
						onsubmit="return contextFormValid();return false;">
						<div class="modal-body">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">新密码</label>
									<div class="col-sm-10">
										<input id="mf_new_password_name" name="mf_new_password_name"
											type="password" class="form-control" placeholder="新密码">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> 密码只能是数据和字母或者特殊符号 .</label>
								</div>
							</fieldset>
						</div>
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
	</div>
	<!-- Top Menu Items -->
	<ul class="nav navbar-right top-nav">
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown" aria-expanded="false"><i
				class="fa fa-language"></i> <spring:message
					code="navbar.language.setting"></spring:message><b class="caret"></b></a>
			<ul class="dropdown-menu alert-dropdown">
				<li><a href="?lang=en_US"><spring:message
							code="navbar.language.en"></spring:message></a></li>
				<li><a href="?lang=zh_CN"><spring:message
							code="navbar.language.zh"></spring:message></a></li>
			</ul></li>
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown"><i class="fa fa-bookmark"></i> V1.0.0 </a></li>
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown" aria-expanded="false"><i
				class="fa fa-user"></i> ${LOGIN_USER_SESSION.realname} <b
				class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a name="mf_account_reset" href="#"><i
						class="fa fa-fw fa-gear"></i> <spring:message code="login.reset"></spring:message></a></li>
				<li><a href="/mf/account/signout"><i
						class="fa fa-fw fa-power-off"></i> <spring:message
							code="login.signout"></spring:message></a></li>
			</ul></li>
	</ul>
	<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav side-nav">
			<li id="navbar_dash"><a href="/mf"><i
					class="fa fa-fw fa-dashboard"></i> <spring:message
						code="navbar.list.dashbaord"></spring:message></a></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo"><i
					class="fa fa-fw fa-database"></i> <spring:message
						code="navbar.list.export"></spring:message> <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo" class="collapse">
					<li id="navbar_list"><a href="/mf/export/common"><i
							class="fa fa-table fa-fw"></i> <spring:message
								code="navbar.list.export.common"></spring:message></a></li>
					<li id="navbar_list"><a href="/mf/export/custom"><i
							class="fa fa-terminal fa-fw"></i> <spring:message
								code="navbar.list.export.sql"></spring:message></a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo2"><i
					class="fa fa-fw fa-tasks"></i> <spring:message
						code="navbar.list.tasks"></spring:message> <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo2" class="collapse">
					<li id="navbar_cli"><a href="/mf/tasks/private"><i
							class="fa fa-user fa-fw"></i> <spring:message
								code="navbar.list.tasks.private"></spring:message></a></li>
					<li id="navbar_cli"><a href="/mf/tasks/auto/private"><i
							class="fa fa-clock-o fa-fw"></i> <spring:message
								code="navbar.list.tasks.automatic"></spring:message></a></li>
					<li id="navbar_zk"><a href="/mf/tasks/public"><i
							class="fa fa-users fa-fw"></i> <spring:message
								code="navbar.list.tasks.all"></spring:message></a></li>
					<li id="navbar_zk"><a href="/mf/tasks/auto/public"><i
							class="fa fa-history fa-fw"></i> <spring:message
								code="navbar.list.tasks.all.automatic"></spring:message></a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo1"><i
					class="fa fa-fw fa-cog"></i> <spring:message
						code="navbar.list.system"></spring:message> <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo1" class="collapse">
					<li id="navbar_modify"><a href="/mf/system/user"><i
							class="fa fa-user fa-fw"></i> <spring:message
								code="navbar.list.system.user"></spring:message></a></li>
					<li id="navbar_modify"><a href="/mf/system/role"><i
							class="fa fa-key fa-fw"></i> <spring:message
								code="navbar.list.system.role"></spring:message></a></li>
					<li id="navbar_system_resource"><a href="/mf/system/resource"><i
							class="fa fa-folder-open fa-fw"></i> <spring:message
								code="navbar.list.system.resource"></spring:message></a></li>
					<li id="navbar_modify"><a href="/mf/system/notice"><i
							class="fa fa-bullhorn fa-fw"></i> <spring:message
								code="navbar.list.system.notice"></spring:message></a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo3"><i
					class="fa fa-fw  fa-globe"></i> <spring:message
						code="navbar.list.config"></spring:message> <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo3" class="collapse">
					<li id="navbar_add"><a href="/mf/config/hive"><i
							class="fa fa-h-square fa-fw"></i> <spring:message
								code="navbar.list.config.hive"></spring:message></a></li>
								<li id="navbar_add"><a href="/mf/config/hbase"><i
							class="fa fa-h-square fa-fw"></i> <spring:message
								code="navbar.list.config.hbase"></spring:message></a></li>
				</ul></li>
			<%-- <li><a href="#" data-toggle="collapse" data-target="#demo4"><i
					class="fa fa-fw fa-maxcdn"></i> <spring:message
						code="navbar.list.mock"></spring:message> <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo4" class="collapse">
					<li id="navbar_add"><a href="#"><i
							class="fa fa-pencil fa-fw"></i> <spring:message
								code="navbar.list.mock.create"></spring:message></a></li>
					<li id="navbar_modify"><a href="#"><i
							class="fa fa-user fa-fw"></i> <spring:message
								code="navbar.list.mock.private"></spring:message></a></li>
					<li id="navbar_modify"><a href="#"><i
							class="fa fa-tasks fa-fw"></i> <spring:message
								code="navbar.list.mock.all"></spring:message></a></li>
				</ul></li> --%>
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</nav>
<script type="text/javascript">
	function contextFormValid() {
		var mf_new_password_name = $("#mf_new_password_name").val();
		var reg = /^[u4E00-u9FA5]+$/;
		if (mf_new_password_name.length == 0 || !reg.test(mf_new_password_name)) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>