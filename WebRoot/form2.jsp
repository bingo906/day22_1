<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'form1.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <h1>上传2</h1>
  <form action="<c:url value="/Upload2Servlet" />" method="post" enctype="multipart/form-data">
	<table>
	<tr>
		<td>用户名:</td>
		<td><input type="text" name="username" /></td>
	</tr>
	<tr>
		<td align="right">照片:</td>
		<td><input type="file" name="zhaopian"/></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="上传" /></td>
	</tr>
	</table>
	
<!-- 	用户名：<input type="text" name="username" /><br/>
	照   片：<input type="file" name="zhaopian"/><br>
		<input type="submit" value="上传" /> -->
  </form>
  </body>
</html>
