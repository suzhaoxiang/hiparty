<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/1
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basepath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basepath%>">
    <title>upload</title>
</head>
<h1>文件上传test</h1>
<form method="upload" action="/user/upload" enctype="multipart/form-data">
    <input type="file" name="file" />
    <br/><br/>
    <input name = "type" >
    <input type="submit" value="上传" />
</form>
</body>
</html>
