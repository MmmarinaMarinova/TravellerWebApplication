<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>Wanderlust</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div class="w3-container">

    <div class="w3-display-container" style="height:300px;">
        <div class="w3-container w3-display-topleft w3-cell">
            <img src="user.png" class="w3-circle"  style="width:25%">
        </div>
        <div class="w3-container w3-teal w3-display-topmiddle w3-cell">
            <p>${sessionScope.user.description}</p>
        </div>
    </div>

</div>



<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>