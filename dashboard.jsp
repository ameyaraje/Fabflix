<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<html>
  <head>
    <meta http-equiv="description" content="Fabflix" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Fabflix</title>

    <link rel="shortcut icon" href="images/favicon.ico">
   
    <link rel="stylesheet" type="text/css" href="./css/main.css">
    <link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <style>
      table, th, td {
          border: 1px solid black;
          border-collapse: collapse;
      }
      th, td {
          padding: 5px;
      }
    </style>
  </head>
  <body style="padding: 40px; background-color: #333; color: #555; text-align: center;">
    <div class="container">
      <h1>Employee Dashboard</h1>
      <div>
        <c:if test="${not empty tables}">
          <h3>Database metadata</h3>
          <c:forEach var="table" items="${tables}">
            <table>
              <tr>
                <th colspan="${table.value.size()}">TABLE: ${table.key}</th>
              </tr>
              <tr>
                <c:forEach var="column" items="${table.value}">
                  <td>${column.key}: ${column.value}</td>
                </c:forEach>
              </tr>
            </table>
          </c:forEach>
        </c:if>
      </div>
      <div>
        <c:if test="${not empty errors}">
          <p><ul style="color: red; list-style: none;">
            <c:forEach var="error" items="${errors}">
              <li>${error}</li>
            </c:forEach>
          </ul></p>
        </c:if>

        <c:if test="${not empty successes}">
          <p><ul style="color: green; list-style: none;">
            <c:forEach var="success" items="${successes}">
              <li>${success}</li>
            </c:forEach>
          </ul></p>
        </c:if>
        
        <div>
          <h3>Add New Star</h3>
          <form style="max-width: 500px; margin: 0 auto; text-align: left;" action="/fabflix/_dashboard/star" method="post">
            <label style="width: 100px;">Star's Name:</label>
            <input type="text" name="first_name" placeholder="Scarlett">
            <input type="text" name="last_name" placeholder="Johansson"><br /><br />
            <label style="width: 100px;" for="dob">Date of Birt:</label>
            <input style="width: 353px;" type="text" id="dob" name="dob" placeholder="yyyy-mm-dd"><br /><br />
            <label style="width: 100px;" for="photo_url">Photo URL:</label>
            <input style="width: 353px;" type="text" id="photo_url" name="photo_url" placeholder="www.example.com/photo_url"><br /><br />
            <input type="submit" style="background-color: #8F0502;
      border-color: #460908;" class="btn btn-primary pull-right" value="Add Star">
          </form>
        </div>

        <div>
          <h3>Add New Movie</h3>
          <form style="max-width: 500px; margin: 0 auto; text-align: left;" action="/fabflix/_dashboard/movie" method="post">
            <label style="width: 100px;" for="title">Title:</label>
            <input style="width: 353px;" type="text" id="title" name="title" placeholder="The Avengers" required><br /><br />
            <label style="width: 100px;" for="year">Year:</label>
            <input style="width: 37px; margin-right: 33px;" maxlength="4" type="text" id="year" name="year" placeholder="2012" required>
            <label style="width: 100px;" for="director">Director:</label>
            <input type="text" id="director" name="director" placeholder="Joss Whedon" required><br /><br />
            <label style="width: 100px;" for="banner_url">Banner URL:</label>
            <input style="width: 353px;" type="text" id="banner_url" name="banner_url" placeholder="www.example.com/banner_url"><br /><br />
            <label style="width: 100px;" for="trailer_url">Trailer URL:</label>
            <input style="width: 353px;" type="text" id="trailer_url" name="trailer_url" placeholder="www.example.com/trailer_url"><br /><br />
            <label style="width: 100px;">Star's Name:</label>
            <input type="text" name="first_name" placeholder="Scarlett">
            <input type="text" name="last_name" placeholder="Johansson"><br /><br />
            <label style="width: 100px;" for="dob">Date of Birt:</label>
            <input style="width: 353px;" type="text" id="dob" name="dob" placeholder="yyyy-mm-dd"><br /><br />
            <label style="width: 100px;" for="photo_url">Photo URL:</label>
            <input style="width: 353px;" type="text" id="photo_url" name="photo_url" placeholder="www.example.com/photo_url"><br /><br />
            <label style="width: 100px;" for="genre">Genre:</label>
            <input style="width: 353px;" type="text" id="genre" name="genre" placeholder="Action" required><br /><br />
            <input type="submit" style="background-color: #8F0502;
      border-color: #460908;" class="btn btn-primary pull-right" value="Add Movie">
          </form>
        </div>
      </div>
    </div>
  </body>
</html> 
