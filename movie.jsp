<%@include  file="header.jsp" %>

<link href="css/results.css" rel="stylesheet" >

<h3 style="text-align: left">Movie Details:</h3><hr />

<c:if test="${not empty error}">
  <h1>${error}</h1>
</c:if>

<c:if test="${not empty movie}">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <col width="18%%">
    <col width="15%">
    <tr>
      <td width="200" rowspan="10">
        <div class="movie-banner">
          <img src="${movie.banner_url()}">
        </div>
        <form method="POST" action="./cart?id=${movie.id()}">
          <input style="width: 30px;" type="text" value="${cart.get(movie.id())}" name="quantity" />&nbsp;&nbsp;
          <input type="submit" class="btn btn-success btn-xs" value="Add To Cart" />
        </form>
      </td>
    </tr>
    <tr>
      <td class="movie-info">Title:</td>
      <td class="movie-info"><a href="./movie?id=${movie.id()}">${movie.title()}</a></td>
    </tr>
    <tr>
      <td class="movie-info">Year:</td>
      <td class="movie-info">${movie.year()}</td>
    </tr>
    <tr>
      <td class="movie-info">Director:</td>
      <td class="movie-info">${movie.director()}</td>
    </tr>
    <tr>
      <td class="movie-info">Movie id:</td>
      <td class="movie-info">${movie.id()}</td>
    </tr>
    <c:if test="${not empty movie.stars()}">
      <tr>
        <td class="movie-info">Stars:</td>
        <td class="movie-info">
          <c:forEach var="star" items="${movie.stars()}" varStatus="status">
            <a href="./star?id=${star.id()}">${star.full_name()}</a><c:if test="${!status.last}">,</c:if>
          </c:forEach>
        </td>
      </tr>
    </c:if>
    <c:if test="${not empty movie.genres()}">
      <tr>
        <td class="movie-info">Genre:</td>
        <td class="movie-info">
          <c:forEach var="genre" items="${movie.genres()}" varStatus="status">
            <a href="./browse?by=genre&name=${genre}">${genre}</a><c:if test="${!status.last}">,</c:if>
          </c:forEach>
        </td>
      </tr>
      <tr>
        <td class="movie-info"><a href="${movie.trailer_url()}">Click here for trailer</a></td>
      </tr>
    </c:if>
  </table>
</c:if>

<%@include  file="footer.jsp" %>
