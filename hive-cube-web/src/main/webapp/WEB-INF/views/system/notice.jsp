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

<title>Notice - Hive Cube</title>
<jsp:include page="../public/css.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="../public/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						System Notice <small>overview</small>
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
						<i class="fa fa-info-circle"></i> <strong>Send a system
							announcement to notify the online user of that day about the
							emergency.</strong>
					</div>
				</div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<i class="fa fa-cogs fa-fw"></i> Edit
							<div class="pull-right"></div>
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<form role="form" action="/hc/system/send/notice/" method="post"
								onsubmit="return contextFormValid();return false;">
								<div class="form-group">
									<label>Content</label>
									<textarea id="hc_notice_content" name="hc_notice_content"
										class="form-control" placeholder="write notice content"
										rows="10"></textarea>
								</div>
								<div id="alert_mssage_notice" style="display: none"
									class="alert alert-danger">
									<label> Oops! Send message can not null .</label>
								</div>
								<button type="submit" class="btn btn-primary" id="create-btn">Send
								</button>
							</form>
						</div>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /#page-wrapper -->
		</div>
	</div>
</body>
<jsp:include page="../public/script.jsp">
	<jsp:param value="main/system/notice.js" name="loader" />
</jsp:include>
<script type="text/javascript">
	function contextFormValid() {
		var hc_notice_content = $("#hc_notice_content").val();
		if (hc_notice_content.length == 0) {
			$("#alert_mssage_notice").show();
			setTimeout(function() {
				$("#alert_mssage_notice").hide()
			}, 3000);
			return false;
		}

		return true;
	}
</script>
</html>
