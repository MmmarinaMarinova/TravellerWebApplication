﻿<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 18.10.2017 г.
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<title>Wanderlust - Start exploring now!</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
	<div class="loginBox">
		<img src="user.png" class="user">
		<h2>Start exploring!</h2>
		<form action="login" method="post">
			<p>
				<font color="white"> username</font>
			</p>
			<c:if test="${isValidData!='false'}">
				<input type="text" placeholder="enter username" name="user">
			</c:if>

			<c:if test="${isValidData=='false'}">
				<input type="text" placeholder="username or password incorrect"
					name="user">
			</c:if>
			<p>
				<font color="white">password</font>
			</p>
			<input type="password" placeholder="••••••••••••••" name="pass">
			<input type="submit" value="Sign in"> <a href="register.jsp">New
				to the travelling world? Join now!</a>
		</form>
	</div>
</body>
</html>