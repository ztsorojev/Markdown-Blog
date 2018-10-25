<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content="initial-scale=1, minimum-scale=1, width=device-width" name="viewport">
    <title>Post List</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <link rel="stylesheet" href="style.css">

</head>
<body>

    <div class="container">
        <div class="text-center mt-4 mb-4">
            <h1> ${param.username}'s Blog </h1>
        </div>
        
    </div>
    
    <div class="container">
        <table class="table">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">Title</th>
                    <th scope="col">Created</th>
                    <th scope="col">Modified</th>
                    <th scope="col">&nbsp;</th>
                </tr>
            </thead>

            <tbody>
              <%              
                int numPosts = (Integer) request.getAttribute("nPosts");
                ArrayList <Integer> postids = (ArrayList <Integer>)request.getAttribute("postid");
                ArrayList <String> titles = (ArrayList <String>)request.getAttribute("title");
                ArrayList <Timestamp> createdTimes = (ArrayList <Timestamp>)request.getAttribute("created");
                ArrayList <Timestamp> modifiedTimes = (ArrayList <Timestamp>)request.getAttribute("modified");
                for(int i=0; i < numPosts; i++) { 
              %>

                <tr>
                    <form id= <%= i + 1 %> action="post" method="POST"> 
                        <input type="hidden" name="username" value= <%= request.getParameter("username") %> >
                        <input type="hidden" name="postid" value= <%= postids.get(i) %> >
                        <td> <%= titles.get(i) %> </td>
                        <td> <%= createdTimes.get(i) %> </td>
                        <td> <%= modifiedTimes.get(i) %> </td>
                        <td>
                            <button type="submit" name="action" value="open" class="iconBlog"><i class='fa fa-pencil'></i></button>
                            <button type="submit" name="action" value="delete" class="iconBlog"><i class='fa fa-times'></i></button>
                        </td>
                    </form>
                </tr>
            <% } %>
            </tbody>

        </table>
    </div>

    <div class="newPost text-center">
        <form action="post" id="0">
            <input type="hidden" name="username" value= ${param.username}>
            <input type="hidden" name="postid" value="0">
            <button type="submit" class="newPost-btn" name="action" value="open"><i class="fa fa-plus"></i></button>
        </form>
    </div>


    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

</body>
</html>
