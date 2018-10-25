<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="initial-scale=1, minimum-scale=1, width=device-width" name="viewport">
    <title>Preview Post</title>
</head>
<body>
        <div>
            <h3><p><em>  You made an error. </em></p></h3>
            <div> ${requestScope.error} </div>
        </div>
</body>
</html>