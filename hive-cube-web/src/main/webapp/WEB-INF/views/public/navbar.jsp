<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only"></span> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="/hc"><i class="fa fa-cubes fa-fw"></i>
			Hive Cube</a>
		<div class="modal fade" aria-labelledby="hcModalLabel"
			aria-hidden="true" id="hc_account_reset_dialog" tabindex="-1"
			role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button class="close" type="button" data-dismiss="modal">×</button>
						<h4 class="modal-title" id="hcModalLabel">Reset password</h4>
					</div>
					<!-- /.row -->
					<form role="form" action="/hc/account/reset/" method="post"
						onsubmit="return contextFormValid();return false;">
						<div class="modal-body">
							<fieldset class="form-horizontal">
								<div class="form-group">
									<label for="path" class="col-sm-2 control-label">New</label>
									<div class="col-sm-10">
										<input id="hc_new_password_name" name="hc_new_password_name"
											type="password" class="form-control" placeholder="New">
									</div>
								</div>
								<div id="alert_mssage" style="display: none"
									class="alert alert-danger">
									<label> Passwords can only be data and letters or
										special symbols .</label>
								</div>
							</fieldset>
						</div>
						<div id="remove_div" class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Cancle</button>
							<button type="submit" class="btn btn-primary" id="create-btn">Sure
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- Top Menu Items -->
	<ul class="nav navbar-right top-nav">
		<%-- <li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown" aria-expanded="false"><i
				class="fa fa-language"></i> <spring:message
					code="navbar.language.setting"></spring:message><b class="caret"></b></a>
			<ul class="dropdown-menu alert-dropdown">
				<li><a href="?lang=en_US"><spring:message
							code="navbar.language.en"></spring:message></a></li>
				<li><a href="?lang=zh_CN"><spring:message
							code="navbar.language.zh"></spring:message></a></li>
			</ul></li> --%>
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown"><i class="fa fa-bookmark"></i> V1.0.0 </a></li>
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown" aria-expanded="false"><i
				class="fa fa-user"></i> ${LOGIN_USER_SESSION.realname} <b
				class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a name="hc_account_reset" href="#"><i
						class="fa fa-fw fa-gear"></i> Reset</a></li>
				<li><a href="/hc/account/signout"><i
						class="fa fa-fw fa-power-off"></i> Signout</a></li>
			</ul></li>
	</ul>
	<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav side-nav">
			<li id="navbar_dash"><a href="/hc"><i
					class="fa fa-fw fa-dashboard"></i> Dashboard</a></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo"><i
					class="fa fa-fw fa-cube"></i> Hive <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo" class="collapse">
					<li id="navbar_list"><a href="/hc/export/common"><i
							class="fa fa-table fa-fw"></i> Common</a></li>
					<li id="navbar_list"><a href="/hc/export/custom"><i
							class="fa fa-terminal fa-fw"></i> Custom</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo2"><i
					class="fa fa-fw fa-tasks"></i> Tasks <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo2" class="collapse">
					<li id="navbar_cli"><a href="/hc/tasks/private"><i
							class="fa fa-user fa-fw"></i> Private</a></li>
					<li id="navbar_cli"><a href="/hc/tasks/auto/private"><i
							class="fa fa-clock-o fa-fw"></i> Auto</a></li>
					<li id="navbar_zk"><a href="/hc/tasks/public"><i
							class="fa fa-users fa-fw"></i> Public</a></li>
					<li id="navbar_zk"><a href="/hc/tasks/auto/public"><i
							class="fa fa-history fa-fw"></i> All-Auto</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo4"><i
					class="fa fa-fw fa-eye"></i> Metrics <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo4" class="collapse">
					<li id="navbar_cli"><a href="/hc/metrics/hdfs"><i
							class="fa fa-file-text-o fa-fw"></i> HDFS</a></li>
					<li id="navbar_cli"><a href="/hc/metrics/yarn"><i
							class="fa fa-hacker-news fa-fw"></i> YARN</a></li>
					<li id="navbar_zk"><a href="/hc/metrics/hadoop"><i
							class="fa fa-sitemap fa-fw"></i> Hadoop</a></li>
					<li id="navbar_zk"><a href="/hc/metrics/hbase"><i
							class="fa fa-h-square fa-fw"></i> HBase</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo6"><i
					class="fa fa-fw fa-adn"></i> Applications <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo6" class="collapse">
					<li id="navbar_list"><a href="/hc/applications/all"><i
							class="fa fa-list fa-fw"></i> All</a></li>
					<li id="navbar_list"><a href="/hc/applications/running"><i
							class="fa fa-play fa-fw"></i> Running</a></li>
					<li id="navbar_list"><a href="/hc/applications/finished"><i
							class="fa fa-circle fa-fw"></i> Finished</a></li>
					<li id="navbar_list"><a href="/hc/applications/failed"><i
							class="fa fa-times-circle fa-fw"></i> Failed</a></li>
					<li id="navbar_list"><a href="/hc/applications/killed"><i
							class="fa fa-stop fa-fw"></i> Killed</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo5"><i
					class="fa fa-fw fa-database"></i> Storage <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo5" class="collapse">
					<li id="navbar_list"><a href="/hc/storage/mysql"><i
							class="fa fa-maxcdn fa-fw"></i> MySQL</a></li>
					<li id="navbar_list"><a href="/hc/storage/redis"><i
							class="fa fa-star fa-fw"></i> Redis</a></li>
					<li id="navbar_list"><a href="/hc/storage/hbase"><i
							class="fa fa-h-square fa-fw"></i> HBase</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo1"><i
					class="fa fa-fw fa-cog"></i> System <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo1" class="collapse">
					<li id="navbar_modify"><a href="/hc/system/user"><i
							class="fa fa-user fa-fw"></i> User</a></li>
					<li id="navbar_modify"><a href="/hc/system/role"><i
							class="fa fa-key fa-fw"></i> Role</a></li>
					<li id="navbar_system_resource"><a href="/hc/system/resource"><i
							class="fa fa-folder-open fa-fw"></i> Resource</a></li>
					<li id="navbar_modify"><a href="/hc/system/notice"><i
							class="fa fa-bullhorn fa-fw"></i> Notice</a></li>
				</ul></li>
			<li><a href="#" data-toggle="collapse" data-target="#demo3"><i
					class="fa fa-fw  fa-globe"></i> Config <i
					class="fa fa-fw fa-caret-down"></i></a>
				<ul id="demo3" class="collapse">
					<li id="navbar_add"><a href="/hc/config/hive"><i
							class="fa fa-h-square fa-fw"></i> Hive</a></li>
					<li id="navbar_add"><a href="/hc/config/hbase"><i
							class="fa fa-h-square fa-fw"></i> HBase</a></li>
				</ul></li>
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</nav>
<script type="text/javascript">
	function contextFormValid() {
		var hc_new_password_name = $("#hc_new_password_name").val();
		var reg = /^[u4E00-u9FA5]+$/;
		if (hc_new_password_name.length == 0 || !reg.test(hc_new_password_name)) {
			$("#alert_mssage").show();
			setTimeout(function() {
				$("#alert_mssage").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>