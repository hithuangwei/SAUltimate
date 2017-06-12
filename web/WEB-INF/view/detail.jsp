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

    <title>微博详情</title>

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
            <center><span style="color: white;"><strong></strong></span></center>
        </div>

    </div>
    <!-- 右侧具体内容栏目 -->
    <div id="rightContent" style="margin-left:20px;">

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
                  style="font-size:15px;float: right;margin-right: 20px;margin-top: 20px;color: white"></span></div>
        <center>
            <div class="ibox-content" style="margin-top: 20px;">

                <form method="get" class="form-horizontal">
                    <div class="form-group"><label class="col-sm-2 control-label">微博内容</label>

                        <div class="col-sm-8">${item.content}</div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group"><label class="col-sm-2 control-label">发布日期</label>
                        <div class="col-sm-10">${item.createDate}

                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group"><label class="col-sm-2 control-label">发布人</label>

                        <div class="col-sm-10">${item.userName}</div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group"><label class="col-sm-2 control-label">点击量</label>

                        <div class="col-sm-10">${item.clickNum}</div>
                    </div>
                    <div class="hr-line-dashed"></div>


                </form>

            </div>
            <button class="btn btn-yellow btn-xs" style="margin-left: 20px;" id="btn_back">返回</button>
        </center>
    </div>
</div>
</div>

</body>


<script src="js/jquery.nouislider.js"></script>


<script type="application/javascript">
    function getQueryStringByName(name) {

        var result = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));

        if (result == null || result.length < 1) {

            return "";

        }

        return result[1];

    }
    $(document).ready(function () {




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
        var date = new Date().Format("yyyy-MM-dd");
        $("#datespan").text(date);
        $("#btn_clear").click(function () {
            $("#weibo_content").text();
        });
        $("#btn_back").click(function () {
            window.location.href = "<%=basePath%>" + "View.do";
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


</html>