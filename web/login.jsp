<%--
  Created by IntelliJ IDEA.
  User: Marina
  Date: 18.10.2017 г.
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>I came to partyyy</title>
    <link rel= "stylesheet" href="style.css">
</head>
<body>
<div class = "loginBox">
    <img src = "user.png" class="user">
    <h2> Log In Here</h2>
    <form action="login" method="post">
        <p>Username</p>
        <input type = "text"  placeholder="Enter username" name="user">
        <p>Password</p>
        <input type = "password"  placeholder="••••••••••••••" name="pass">
        <input type = "submit"  value="Sign in" >
        <a href="register">New to the travelling world? Join now and start exploring!</a>
    </form>
</div>
</body>
</html>

