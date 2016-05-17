<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@include  file="header.jsp" %>

<link href="css/results.css" rel="stylesheet" >

<h3 style="text-align: left">Movies</h3><hr />
  <p class="bold">Browse Movie by Genre</p> 
  <table style="width:100%">
    <tr>
      <td><a href="./browse?by=genre&name=action">Action</a></td>
      <td><a href="./browse?by=genre&name=adventure">Adventure</a></td>
      <td><a href="./browse?by=genre&name=animation">Animation</a></td>
      <td><a href="./browse?by=genre&name=biography">Biography</a></td>
      <td><a href="./browse?by=genre&name=classic">Classic</a></td>
      <td><a href="./browse?by=genre&name=comedy">Comedy</a></td>
      <td><a href="./browse?by=genre&name=crime">Crime</a></td>
    </tr>
    <tr>
      <td><a href="./browse?by=genre&name=documentary">Documentary</a></td>
      <td><a href="./browse?by=genre&name=drama">Drama</a></td>
      <td><a href="./browse?by=genre&name=family">Family</a></td>
      <td><a href="./browse?by=genre&name=fantasy">Fantasy</a></td>
      <td><a href="./browse?by=genre&name=foreign">Foreign</a></td>
      <td><a href="./browse?by=genre&name=horror">Horror</a></td>
      <td><a href="./browse?by=genre&name=music">Music</a></td>
    </tr>
    <tr>
      <td><a href="./browse?by=genre&name=musical">Musical</a></td>
      <td><a href="./browse?by=genre&name=mystery">Mystery</a></td>
      <td><a href="./browse?by=genre&name=roman">Roman</a></td>
      <td><a href="./browse?by=genre&name=romance">Romance</a></td>
      <td><a href="./browse?by=genre&name=sci-fi">Sci-Fi</a></td>
      <td><a href="./browse?by=genre&name=thriller">Thriller</a></td>
      <td><a href="./browse?by=genre&name=war">War</a></td>
    </tr>
  </table>

<div>
	<p class="bold">Browse Movie by Title</p>
  <a href="./browse?by=title&name=0">0</a> | <a href="./browse?by=title&name=1">1</a> | <a href="./browse?by=title&name=2">2</a> | <a href="./browse?by=title&name=3">3</a> | <a href="./browse?by=title&name=4">4</a> | <a href="./browse?by=title&name=5">5</a> | <a href="./browse?by=title&name=6">6</a> | <a href="./browse?by=title&name=7">7</a> | <a href="./browse?by=title&name=8">8</a> | <a href="./browse?by=title&name=9">9</a>  <br />

  <a href="./browse?by=title&name=a">A</a> | <a href="./browse?by=title&name=b">B</a> | <a href="./browse?by=title&name=c">C</a> | <a href="./browse?by=title&name=d">D</a> | <a href="./browse?by=title&name=e">E</a> | <a href="./browse?by=title&name=f">F</a> | <a href="./browse?by=title&name=g">G</a> | <a href="./browse?by=title&name=h">H</a> | <a href="./browse?by=title&name=i">I</a> | <a href="./browse?by=title&name=j">J</a> | <a href="./browse?by=title&name=k">K</a> | <a href="./browse?by=title&name=l">L</a> | <a href="./browse?by=title&name=m">M</a> | <a href="./browse?by=title&name=n">N</a> | <a href="./browse?by=title&name=o">O</a> | <a href="./browse?by=title&name=p">P</a> | <a href="./browse?by=title&name=q">Q</a> | <a href="./browse?by=title&name=r">R</a> | <a href="./browse?by=title&name=s">S</a> | <a href="./browse?by=title&name=t">T</a> |<a href="./browse?by=title&name=u">U</a> | <a href="./browse?by=title&name=v">V</a> | <a href="./browse?by=title&name=w">W</a> | <a href="./browse?by=title&name=x">X</a> | <a href="./browse?by=title&name=y">Y</a> | <a href="./browse?by=title&name=z">Z</a>

</div>

<p id="sort-nav">Sort By:&nbsp;&nbsp;&nbsp;
  <a href="${u_title_asc}">
    Title <span class="glyphicon glyphicon-triangle-bottom" style="color:green;"></span></a>&nbsp;&nbsp;&nbsp;
  <a href="${u_title_desc}">
    Title <span class="glyphicon glyphicon-triangle-top" style="color:red;"></span></a>&nbsp;&nbsp;&nbsp;
  <a href="${u_year_desc}">
    Year <span class="glyphicon glyphicon-triangle-bottom" style="color:green;"></span></a>&nbsp;&nbsp;&nbsp;
  <a href="${u_year_asc}">
    Year <span class="glyphicon glyphicon-triangle-top" style="color:red;"></span></a>
</p>

<c:if test="${error != null}">
  <h1>${error}</h1>
</c:if>
<c:forEach var="movie" items="${movies}">
  <table width="%100" border="0" cellspacing="0" cellpadding="0">
    <col width="25%%">
    <col width="15%">
    <tr>
      <td width="200" rowspan="8">
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
    </c:if>
  </table>
</c:forEach>

<c:if test="${page_number > 1}">
  <a href="${u_traversal}${page_number-1}" class="btn btn-primary">Previous Page</a>&nbsp;&nbsp;&nbsp;
</c:if>
<c:if test="${page_number < total_pages}">
  <a href="${u_traversal}${page_number+1}" class="btn btn-primary">Next Page</a>
</c:if>
<p id="num-nav">Results per page:
  <a href="${u_pagination}5">5</a> |
  <a href="${u_pagination}10">10</a> | 
  <a href="${u_pagination}15">15</a> | 
  <a href="${u_pagination}20">20</a> | 
  <a href="${u_pagination}25">25</a>
</p>

<%@ include  file="footer.jsp" %>
