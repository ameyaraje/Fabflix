<%@include  file="header.jsp" %>
<link href="css/results.css" rel="stylesheet" >

<h3 style="text-align: left">Checkout:</h3><hr />
<p>Please provide a first name, a last name, and a credit card with an expiration date. If successful, the transaction will be recorded in the system. </p><br />

<c:if test="${not empty errors}">
  <p><ul style="color: red; list-style: none;">
    <c:forEach var="error" items="${errors}">
      <li>${error}</li>
    </c:forEach>
  </ul></p>
</c:if>

<form style="max-width: 330px; margin: 0 auto; text-align: left;" action="./checkout" method="post">
  <label for="cc_id" style="width: 150px">Credit Card Number:</label>
  <input style="width: 168px;" type="text" id="cc_id" name="cc_id" placeholder="1234567890" required><br /><br />
  <label style="width: 150px">Name</label>
  <input style="width: 82px;" type="text" name="first_name" placeholder="First" required>
  <input style="width: 82px;" type="text" name="last_name" placeholder="Last" required><br /><br />
  <label style="width: 150px">Expiration Date</label>
  <input style="width: 40px;" type="text" name="month" maxlength="2" placeholder="MM" required>
  <input style="width: 40px;" type="text" name="day" maxlength="2" placeholder="DD" required>
  <input style="width: 80px;" type="text" name="year" maxlength="4" placeholder="YYYY" required><br /><br />
  <input type="submit" class="btn btn-primary pull-right" value="Checkout">
</form>

<%@include  file="footer.jsp" %>
