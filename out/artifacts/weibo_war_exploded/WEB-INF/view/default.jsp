<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ch">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="weibo">
    <meta name="keywords" content="weibo">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">

    <title>微博Demo</title>

    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <style>
        ::-webkit-input-placeholder { /* WebKit, Blink, Edge */
            color: #777;
        }

        :-moz-placeholder { /* Mozilla Firefox 4-18 */
            color: #777;
        }

        ::-moz-placeholder { /* Mozilla Firefox 19+ */
            color: #777;
        }

        :-ms-input-placeholder { /* Internet Explorer 10-11 */
            color: #777;
        }

        /* scrolltext */
        .scrolltext {
            padding: 57px 15px 0 14px;
            width: 189px;
            height: 389px;
            overflow: hidden;
            margin: 20px auto 0 auto;
        }

        .scrolltext ol li {
            padding-left: 7px;
            width: 182px;
            height: 25px;
            font-size: 13px;
            line-height: 25px;
            border-bottom: 2px solid #fff;
        }

        .scrolltext ol li a {
            color: #6f746e;
            display: block;
            width: 172px;
            white-space: nowrap;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
            overflow: hidden;
        }

        .scrolltext ol li a:hover {
            color: #ef9b11;
            text-decoration: none;
        }

        .scrolltext .part {
            width: 100%;
            padding-top: 20px;
        }

        .scrolltext .part span {
            margin-left: 26px;
            cursor: pointer;
        }

        #breakNews {
            padding: 0 0 0px 2px;
        }

        #breakNews .list6 {
            height: 330px;
            overflow: hidden;
            width: 100%;
        }


    </style>
    <base href="<%=basePath%>">
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <script src="js/scroll.js"></script>
    <script src="js/ScrollText.js"></script>
    <script>
        $(function () {
            var scroll2 = new ScrollText("breakNewsList", "pre2", "next2", true, 50, true);
            scroll2.LineHeight = 120;

            var date = new Date().Format("yyyy-MM-dd");
            $("#datespan").text(date);

        })
    </script>

    <![endif]-->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/common.css"/>
    <link rel="stylesheet" type="text/css" href="css/slide.css"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="css/flat-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="css/jquery.nouislider.css">

</head>

<body>
<div id="wrap">
    <!-- 左侧菜单栏目块 -->
    <div class="leftMeun" id="leftMeun" style="height:100%;width: 220px;">
        <div id="logoDiv">
            <p id="logoP"><img id="logo" alt="tiny weibo" src="images/logo.png">
                <span>小型微博</span></p>
        </div>
        <div>
            <center><span style="color: white;"><strong>微博热搜榜</strong></span></center>
        </div>
        <div class="scrolltext"
             style="margin-left: -30px;margin-top:-45px;font-size: 12px;color: white;width: 280px;height: 700px;">
            <div id="breakNews">
                <ol id="breakNewsList" class="list6" style="height: 700px;">

                </ol>
            </div>

        </div>
    </div>
    <!-- 右侧具体内容栏目 -->
    <div id="rightContent">
        <div style="background-color: #354457;width: 100%;height: 70px;">
            <button class="btn btn-success btn-xs" id="btn_logout"
                    style="font-size:20px;background-color: #354457;
                    float: right;margin-right: 20px;margin-top: 20px;color: white">logout
            </button>

            <span style="font-size:15px;float: right;margin-right: 20px;margin-top: 20px;color: white"> <c:if
                    test="${sessionScope.username==null}">Anomymous</c:if></span>
            <span style="font-size:15px;float: right;margin-right: 20px;margin-top: 20px;color: white"> <c:if
                    test="${sessionScope.username!=null}">${sessionScope.username}</c:if></span>

            <span id="datespan"
                  style="font-size:15px;float: right;margin-right: 20px;margin-top: 20px;color: white"></span>



        </div>
        <a class="toggle-btn" id="nimei">
            <i class="glyphicon glyphicon-align-justify"></i>
        </a>
        <!-- Tab panes -->
        <div class="tab-content">

            <!-- 资源管理模块 -->
            <div role="tabpanel" class="tab-pane active" id="sour">
                <div>
                    <center style="margin-bottom: 15px;">
                        <div style="color: black;font-size: 15px;"><strong>微博发布：</strong></div>
                        <textarea id="weibo_content" style="width: 85%;color: black;font-size: 13px;" rows="6">
                        </textarea>

                    </center>
                </div>
                <div style="margin-bottom: 15px;">
                    <center>
                        <button class="btn btn-yellow btn-xs" id="btn_publish">发布</button>
                        <button class="btn btn-yellow btn-xs" style="margin-left: 20px;" id="btn_clear">清除</button>
                    </center>
                </div>
                <div class="data-div">
                    <div class="row tableHeader">
                        <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1 ">
                            发布人
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
                            更新时间
                        </div>

                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                            摘要
                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                            操作
                        </div>
                    </div>
                    <div class="tablebody">

                        <div id="collapseSystem" class="collapse in" aria-expanded="true">

                            <div id="collapseAccount" class="collapse in" aria-expanded="true">


                                <c:forEach var="item" items="${weiboList}">
                                    <div class="row">

                                        <div class="col-lg-1 col-md-1 col-sm-1 col-xs-1  levl3 ">
                                                ${item.userName}
                                        </div>
                                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4  levl3">
                                            <span>${item.createDate}</span>
                                        </div>

                                        <a class="col-lg-5 col-md-5 col-sm-5 col-xs-5"
                                           href="<%= basePath%>detail?id=${item.id}">
                                            <span> ${item.content}</span>
                                        </a>
                                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2">
                                            <a class="btn btn-success btn-xs" data-toggle="modal"
                                               href="<%= basePath%>modify?id=${item.id}">修改
                                            </a>

                                            <a class="btn btn-danger btn-xs" data-toggle="modal"
                                               href="<%= basePath%>delete?id=${item.id}">删除
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>
                    </div>
                </div>
                <!--页码块-->
                <footer class="footer">
                    <ul class="pagination">
                        <li>
                            <select>
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                                <option>6</option>
                                <option>7</option>
                                <option>8</option>
                                <option>9</option>
                                <option>10</option>
                            </select>
                            页
                        </li>
                        <li class="gray">
                            共20页
                        </li>
                        <li>
                            <i class="glyphicon glyphicon-menu-left">
                            </i>
                        </li>
                        <li>
                            <i class="glyphicon glyphicon-menu-right">
                            </i>
                        </li>
                    </ul>
                </footer>

            </div>
        </div>
    </div>


    <form id="id_form" action="<%=basePath%>add" method="post" class="hidden">
        <input name="content" id="id_content"/>
    </form>
    <form id="form_login" action="<%=basePath%>login" method="post" class="hidden">
        <input name="username" id="username"/>
        <input name="password" id="pwd"/>
    </form>
    <form id="form_logout" action="<%=basePath%>logout" method="post" class="hidden">

    </form>
</div>
<script src="js/jquery.nouislider.js"></script>

<script type="application/javascript">

    function getTopWeibo() {
        $.ajax({

            dataType: "json",
            type: "get",
            url: "<%=basePath%>top",
            success: function (ret) {
                if (ret.status == 200) {
                    $("#breakNewsList").empty();
                    for (var i = 0; i < ret.data.length; i++) {
                        $("#breakNewsList").append(ret.data[i]);
                    }
                }

            }


        });
    }
    setTimeout(function () {
            getTopWeibo();
        },
        100);
    setInterval(function () {

            getTopWeibo();

        },
        20000);
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
    $(document).ready(function () {
        $("#weibo_content").text("");
        $("#btn_clear").click(function () {
            $("#weibo_content").text("");
        });


        $("#btn_login").click(function () {
            $("#username").val($("input[name='username']").val());
            $("#pwd").val($("input[name='password']").val());
            $("#form_login").submit();
        });
        $("#btn_logout").click(function () {
            $("#form_logout").submit();
        });
        $("#weibo_content").attr("placeholder", "在这里发布微博");
        $("#btn_publish").click(function () {
            var content = $("#weibo_content").val();
            console.log(content);
            $("#id_content").val(content);
            $("#id_form").submit();
            $("#id_content").val();
        });
    });
</script>
</body>

</html>