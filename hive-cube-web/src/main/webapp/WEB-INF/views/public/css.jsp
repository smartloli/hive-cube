<%@ page pageEncoding="UTF-8"%>
<link href="/mf/media/css/public/bootstrap.min.css" rel="stylesheet" />
<link href="/mf/media/css/public/sb-admin.css" rel="stylesheet"/>
<link href="/mf/media/css/public/morris.css" rel="stylesheet"/>
<link href="/mf/media/css/public/font-awesome.min.css" rel="stylesheet"/>
<link href="/mf/media/css/public/magicsuggest.css" rel="stylesheet"/>
<link href="/mf/media/css/public/codemirror.css" rel="stylesheet"/>
<link href="/mf/media/css/public/show-hint.css" rel="stylesheet"/>
<link href="/mf/media/css/public/jquery.terminal.min.css" rel="stylesheet"/>
<link href="/mf/media/css/public/bootstrap-switch.min.css" rel="stylesheet"/>
<link href="/mf/media/css/public/bootstrap-treeview.min.css" rel="stylesheet"/>
<link href="/mf/media/css/public/fileinput.min.css" rel="stylesheet"/>
<link rel="shortcut icon" href="/mf/media/img/favicon.ico" />
<%
	String[] loader = request.getParameterValues("css");
	if (loader == null) {
		return;
	}
	for (String s : loader) {
%>
<link href="/mf/media/css/<%=s%>" rel="stylesheet"/>
<%
	}
%>