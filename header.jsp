<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
﻿<html>
  <head>
    <meta http-equiv="description" content="Fabflix" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Fabflix</title>

    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="./css/main.css">

    <link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script type="text/javascript" src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> 

    <noscript>Please enable JavaScript for website's full functionality.</noscript>
  </head>
  <a class="header" href="main.jsp"><header>Fabflix</header></a>
  <body>
    <div id="navbar">
      <a href="./main">Home</a> | <a href="./browse">Browse</a> | <a href="./search">Advanced Search</a> | <a href="./cart">Cart</a> | <a href="./checkout">Checkout</a> |       
      <form action="./logout" method="post">
        <input type="submit" value="Logout">
      </form>
  </div>
  <div class="content-container" id="index-content">