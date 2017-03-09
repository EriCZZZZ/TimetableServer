<html>
<head>
    <%@include file="WEB-INF/views/common/header.jsf"%>
    <script type="text/javascript">
        require(['jquery', 'bootstrap', 'eposition'], function() {
            $("body").removeClass("display-none");
            $("#title").verticalCenter().horizontalCenter();
        });
    </script>
</head>
<body class="display-none">
    <div style="width: 100%; height: 100%; background-color: #0f0f0f; text-align: center">
        <h1 class="no-margin" style="font-size: 64px" id="title"><a href="/timetable">_Timetable</a></h1>
    </div>
</body>
</html>
