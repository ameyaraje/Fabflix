<%@include  file="header.jsp" %>
<link href="css/results.css" rel="stylesheet" >

<h3 style="text-align: left">Shopping Cart:</h3><hr />
<table width="100%">
  <c:choose>
    <c:when test="${error != null}">
      <h1>${error}</h1>
    </c:when>
    <c:when test="${success != null}">
      <h1>${success}</h1>
    </c:when>
  </c:choose>
  <c:if test="${movies != null}">
    <tr>
      <td><h3 style="float: left;">Movie</h3></td>
      <td><h3 style="float: left;">Quantity</h3></td>
    </tr>
  </c:if>
  <c:forEach items="${movies}" var="movie">
    <tr>
      <td>${movie.title()}</td>
      <td>
        <form method="POST" action="./cart?id=${movie.id()}">
          <input type="text" name="quantity" value="${cart.get(movie.id())}" style="width: 40px;"/>&nbsp&nbsp
          <input type="submit" class="btn btn-xs btn-success" value="Update Quantity" />
        </form>
      </td>
    </tr>
  </c:forEach>

  <tr>
    <td><br /><br /><a href="./checkout" class="btn btn-primary">Checkout</a></td>
    <td><br /><br />
      <form method="POST" action="./cart?clear=true">
        <input type="submit" class="btn btn-primary" id="cart" value="Clear Cart" />
      </form>
    </td>
  </tr>
</table>
<%@include  file="footer.jsp" %>
