<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="initial-scale=1, minimum-scale=1, width=device-width" name="viewport">
    <title>Edit Post</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <link rel="stylesheet" href="style.css">

</head>
<body>
    <div class="container">
        <div class="text-center mt-4 mb-4">
            <h1>Edit Post</h1>
        </div>
    </div>

    <form action="post" method="POST" class="text-center">
        <div>
            <button type="submit" name="action" value="save" class="btn btn-dark">Save</button>
            <button type="submit" name="action" value="list" class="btn btn-dark">Close</button>
            <button type="submit" name="action" value="preview" class="btn btn-dark">Preview</button>
            <button type="submit" name="action" value="delete" class="btn btn-dark">Delete</button>
        </div>
        <input type="hidden" name="username" value= <%= request.getParameter("username") %> >
        <input type="hidden" name="postid" value= <%= request.getParameter("postid") %> >

        <div class="text-box mt-4 mb-4">
            <!--<label for="title">Title</label>-->
            <input type="text" id="text" name="title" placeholder="Title"
                <c:choose>
                    <c:when test="${requestScope.title != null}"> value = "${requestScope.title}" </c:when>
                    <c:when test="${param.title != null}"> value = "${param.title}" </c:when>
                </c:choose>
            >
        </div>

        <div class="text-box">
            <!--<label for="body">Body</label>-->
            <textarea style="height: 20rem;" id="body" name="body" placeholder="Body" value = "${requestScope.body} ${param.body}" ><c:choose><c:when test="${requestScope.body != null && !requestScope.body.isEmpty()}">${requestScope.body}</c:when><c:when test="${param.body != null && !param.body.isEmpty()}">${param.body}</c:when></c:choose></textarea>
        </div>
    </form>


    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

</body>
</html>
