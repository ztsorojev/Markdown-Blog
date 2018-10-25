<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="initial-scale=1, minimum-scale=1, width=device-width" name="viewport">
    <title>Preview Post</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <link rel="stylesheet" href="style.css">

</head>
<body>
        
        <div class="container" style="position: relative;">
            <div class="mt-4 mb-4">
                <div class="closeBtn">
                    <form action="post" method="POST">
                        <input type="hidden" name="username" value= <%= request.getParameter("username") %> >
                        <input type="hidden" name="postid" value= <%= request.getParameter("postid") %> >
                        <input type="hidden" name="title" value= "${param.title}" >
                        <input type="hidden" name="body" value= "${param.body}" >
                        <button type="submit" name="action" value="open" class="btn btn-dark">Close Preview</button>
                    </form>
                </div>

                <h1 id="title" class="text-center"><p><em>  <c:if test="${param.title != null}"> ${param.title} </c:if> </em></p></h1>
                <div id="body" class="preview-box"> <c:if test="${requestScope.body_markdown != null}"> ${requestScope.body_markdown} </c:if> </div>
            </div>
        </div>
</body>
</html>