<%@ page pageEncoding="UTF-8"%>
<link href="/hc/media/css/public/bootstrap.min.css" rel="stylesheet" />
<link href="/hc/media/css/public/sb-admin.css" rel="stylesheet"/>
<link href="/hc/media/css/public/morris.css" rel="stylesheet"/>
<link href="/hc/media/css/public/font-awesome.min.css" rel="stylesheet"/>
<link href="/hc/media/css/public/magicsuggest.css" rel="stylesheet"/>
<link href="/hc/media/css/public/codemirror.css" rel="stylesheet"/>
<link href="/hc/media/css/public/show-hint.css" rel="stylesheet"/>
<link href="/hc/media/css/public/jquery.terminal.min.css" rel="stylesheet"/>
<link href="/hc/media/css/public/bootstrap-switch.min.css" rel="stylesheet"/>
<link href="/hc/media/css/public/bootstrap-treeview.min.css" rel="stylesheet"/>
<link href="/hc/media/css/public/fileinput.min.css" rel="stylesheet"/>
<link rel="shortcut icon" href="/hc/media/img/favicon.ico" />
<%
	String[] loader = request.getParameterValues("css");
	if (loader == null) {
		return;
	}
	for (String s : loader) {
%>
<link href="/hc/media/css/<%=s%>" rel="stylesheet"/>
<%
	}
%>