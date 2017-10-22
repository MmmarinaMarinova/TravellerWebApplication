<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div class="header">
    <form class="navi" action="wanderlust.jsp">
        <input type="submit" value="Wanderlust world">
    </form>
    <form class="navi" action="login.jsp">
        <input type="submit" value="Login">
    </form>
    <form class="navi" action="register.jsp">
        <input type="submit" value="Register">
    </form>
    <form class="navi" action="logout" method="post">
        <input type="submit" value="Logout">
    </form>



    <img id="avatar" src="avatar">
    <h3 class="welcome">Welcome, ${ sessionScope.user.username }</h3>

</div>
</body>
</html>