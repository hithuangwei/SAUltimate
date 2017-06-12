<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>微博登陆</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">

    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    %>

    <base href="<%=basePath%>">
</head>

<body class="gray-bg">

<div class="middle-box text-center loginscreen animated fadeInDown">
    <div>
        <div>
        </div>
        <h3>Welcome to TinyWeibo</h3>
        <p>微博登录
            <!--Continually expanded and constantly improved Inspinia Admin Them (IN+)-->
        </p>

        <form class="m-t" role="form" action="<%=basePath%>Login.do" method="post">
            <div class="form-group">
                <input type="text" name="username" class="form-control" placeholder="用户名" required="">
            </div>
            <div class="form-group">
                <input type="password" name="password" class="form-control" placeholder="密码" required="">
            </div>
            <c:if test="${reason!=null}">
            <p style="color: red;">用户认证失败<p>
            </c:if>
            <button type="submit" class="btn btn-primary block full-width m-b">登录</button>


            <p class="text-muted text-center"><small>没有账户?</small></p>
            <a class="btn btn-sm btn-white btn-block" href="<%=basePath%>Reg.do">注册</a>
        </form>
        <%--<p class="m-t"> --%>
            <%--<small>Inspinia we app framework base on Bootstrap 3 &copy; 2014</small>--%>
        <%--</p>--%>
    </div>
</div>

<!-- Mainly scripts -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</body>

</html>
