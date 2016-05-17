<%@include  file="header.jsp" %>
<link href="css/results.css" rel="stylesheet">

<h3 style="text-align: left">Advanced Movie Search Options:</h3><hr />
<p>Search for movies by any of the following attributes or their combination. Partial matching will be used for title, director, and star first and last name.</p><br />
<form style="max-width: 500px; margin: 0 auto; text-align: left;" action="./search" method="get">
  <label style="width: 100px;" for="title">Title:</label>
  <input style="width: 353px;" type="text" id="title" name="title" placeholder="The Avengers"><br /><br />
  <label style="width: 100px;" for="year">Year:</label>
  <input style="width: 37px; margin-right: 33px;" maxlength="4" type="text" id="year" name="year" placeholder="2012">
  <label style="width: 100px;" for="director">Director:</label>
  <input type="text" id="director" name="director" placeholder="Joss Whedon"><br /><br />
	<label style="width: 100px;">Star's Name:</label>
	<input type="text" name="star_first_name" placeholder="Scarlett">
	<input type="text" name="star_last_name" placeholder="Johansson"><br /><br />
	<input type="submit" class="btn btn-primary pull-right" value="Search Movies">
</form>

<%@include  file="footer.jsp" %>
