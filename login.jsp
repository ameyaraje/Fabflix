<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<html>
  <head>
    <meta http-equiv="description" content="Fabflix" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Fabflix</title>

    <link rel="shortcut icon" href="images/favicon.ico">
    <link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src='https://www.google.com/recaptcha/api.js'></script>
  </head>
  <body style="padding: 40px; background-color: #333; color: #FFF; text-align: center;">
    <div class="container">
      <h1>Fabflix Existing Member Log In</h1>

      <c:if test="${not empty errors}">
        <p><ul style="color: red; list-style: none;">
          <c:forEach var="error" items="${errors}">
            <li>${error}</li>
          </c:forEach>
        </ul></p>
      </c:if>

      <form style="max-width: 350px; margin: 60px auto;"action="./login" method="post">
        <div class="form-group">
          <input class="form-control" type="email" id="email" name="email" placeholder="Email Address" required />
        </div>
        <div class="form-group">
          <input class="form-control" type="password" id="password" name="password" placeholder="Password" required />
        </div>
        <c:if test="${not empty u_redirect}">
          <input type="text" name="u_redirect" value="${u_redirect}" hidden />
        </c:if>
        <div class="g-recaptcha" data-sitekey="6LfnEh8TAAAAAHjZKO2N2YN_YnAppONPMLITdlhO"></div>
        <button type="submit" style="background-color: #8F0502;
  border-color: #460908;" class="btn btn-lg btn-primary btn-block">Sign in</button>
      </form>
    </div>
  </body>
</html> 