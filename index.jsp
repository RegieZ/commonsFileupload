<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>${NAME}</title>
  <script src="${pageContext.request.contextPath}/js/jquery-3.2.1.js"></script>
</head>
<body>
<h3>文件上传</h3>
<form action="${pageContext.request.contextPath}/UploadServlet" method="post" enctype="multipart/form-data">
    用户名：<input type="text" name="username" id="username"><br>
    上传图片：<input type="file" name="pic"><br>
    <input type = "submit" value="提交表单">
</form>
</body>
</html>
