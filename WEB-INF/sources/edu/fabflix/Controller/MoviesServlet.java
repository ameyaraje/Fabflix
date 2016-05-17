/*============================================
 * Movies Servlet
 * route: {context}/movies
 * method: GET
 *============================================
 * A Servlet to provide movie data based on
 * database query results.
 *============================================*/

package edu.fabflix.Controller;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.fabflix.Model.Movie;
import edu.fabflix.Model.Star;

public class MoviesServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet handles the searching, sorting, and displaying of movie data.";
  }
  
  /**
   * Handle GET requests. Query database based on request and send movie data back to
   * the session.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");

    // Request variables.
    // WARNING: Security risk! Should be valided!
    int pageNum = 1;
    try
    {
      if (request.getParameter("page_num") != null) pageNum = Integer.parseInt(request.getParameter("page_num"));
    }
    catch (NumberFormatException e) {}

    int numPerPage = 5;
    try
    {
      if (request.getParameter("num_per_page") != null) numPerPage = Integer.parseInt(request.getParameter("num_per_page"));
    }
    catch (NumberFormatException e) {}

    String sortBy  = request.getParameter("sort_by");
    String orderBy = request.getParameter("order_by");
    String mode = (String) request.getAttribute("mode");
    if (mode == null || mode.equals("")) mode = request.getParameter("mode");

    // Full request url.
    String requestURL  = request.getRequestURL()+"?mode="+mode;
    String queryString = request.getQueryString();
    if (queryString != null && !queryString.equals("")) requestURL += "&"+queryString;


    // Prepare Sorting URL's
    request.setAttribute("u_title_asc", prepareSortingURL(requestURL, sortBy, orderBy, "title", "ASC"));
    request.setAttribute("u_title_desc", prepareSortingURL(requestURL, sortBy, orderBy, "title", "DESC"));
    request.setAttribute("u_year_asc", prepareSortingURL(requestURL, sortBy, orderBy, "year" , "ASC"));
    request.setAttribute("u_year_desc", prepareSortingURL(requestURL, sortBy, orderBy, "year" , "DESC"));

    // Default settings.
    sortBy  = (sortBy  == null || sortBy.equals(""))  ? "m.title" : "m."+sortBy;
    orderBy = (orderBy == null || orderBy.equals("")) ? "ASC"     : orderBy;
    int start = (pageNum - 1) * numPerPage;

    // Build the query.
    String query;
    if ("search".equals(mode)) query = buildSearchQuery(request, response);
    else query = buildBrowseQuery(request, response);

    // Database authentications.
    String db_url      = getServletContext().getInitParameter("db_url");
    String db_user     = getServletContext().getInitParameter("db_user");
    String db_password = getServletContext().getInitParameter("db_password");

    // Connect to the database.
    Connection dbcon     = null;
    Statement  statement = null;
    ResultSet  rs        = null;
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon     = DriverManager.getConnection(db_url, db_user, db_password);
      statement = dbcon.createStatement();
      rs        = statement.executeQuery(query);

      // Retrieve results for pagination.
      int rowCount = 0;
      StringBuilder movieIDs = new StringBuilder("0,");
      while (rs.next())
      {
        ++rowCount;
        movieIDs.append(rs.getInt("id") + ",");
      }

      int totalPages = (rowCount + numPerPage - 1) / numPerPage; // Round up.
      if (movieIDs.length() > 0) movieIDs.setLength(movieIDs.length() - 1); // Remove last comma.

      // Retrieve only results needed in current page.
      query = "SELECT m.id m_id, s.id s_id, m.*, s.*, g.* " +
              "FROM movies m " +
              "INNER JOIN stars_in_movies sm ON sm.movie_id = m.id " +
              "INNER JOIN stars s ON s.id = sm.star_id " +
              "INNER JOIN genres_in_movies gm ON gm.movie_id = m.id " +
              "INNER JOIN genres g ON g.id = gm.genre_id " +
              "INNER JOIN (" +
                "SELECT id FROM movies " +
                "WHERE id IN ("+movieIDs.toString()+") " +
                "GROUP BY id LIMIT " + start +", "+ numPerPage + ") ms " +
              "ON ms.id = m.id " +
              "ORDER BY " + sortBy +" "+ orderBy;
      rs = statement.executeQuery(query);

      boolean prepared = Movie.prepareMovieList(rs, request, requestURL, rowCount);
      if (!prepared) { request.setAttribute("error", "No movies found."); }
    }
    catch (SQLException ex) { request.setAttribute("error", "No movies found."); }
    catch (Exception ex)    { request.setAttribute("error", "No movies found."); }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      request.getRequestDispatcher("movies.jsp").forward(request, response);
    }
  }

  /**
   * Helper method to build where clause.
   *
   * @param StringBuilder clause The where clause.
   * @param String column name.
   * @param String operator The operator (either = or LIKE).
   * @param String field The variable to check agaisnt the column.
   * @return StringBuilder The where clause.
   */
  private StringBuilder buildWhereClause(StringBuilder clause, String column, String operator, String field)
  {
    if (field != null && !field.equals(""))
    {
      if (clause.length() > 0) clause.append(" AND ");
      else clause.append(" WHERE ");
      if (operator.equals("LIKE")) field = "LOWER('%" + field + "%')";
      clause.append(column +" "+ operator + " " + field);
    }
    return clause;
  }

  /**
   * Builds a query string based on request parameter.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   */
  private String buildSearchQuery(HttpServletRequest request, HttpServletResponse response)
  {
    // Request variables.
    // WARNING: Security risk! Should be validated!
    String title         = request.getParameter("title");
    String year          = request.getParameter("year");
    String director      = request.getParameter("director");
    String starFirstname = request.getParameter("star_first_name");
    String starLastname  = request.getParameter("star_last_name");

    // Build query.
    StringBuilder query = new StringBuilder("SELECT m.id FROM movies m");


    // Add joins.
    query.append(" INNER JOIN stars_in_movies sm ON sm.movie_id = m.id" +
                 " INNER JOIN stars s ON s.id = sm.star_id");

    // Add where clause.
    StringBuilder where = new StringBuilder();
    where = buildWhereClause(where, "LOWER(m.title)", "LIKE", title);
    where = buildWhereClause(where, "m.year", "=", year);
    where = buildWhereClause(where, "LOWER(m.director)", "LIKE", director);
    where = buildWhereClause(where, "LOWER(s.first_name)", "LIKE", starFirstname);
    where = buildWhereClause(where, "LOWER(s.last_name)", "LIKE", starLastname);
    query.append(where);

    query.append(" GROUP BY m.id");

    return query.toString();
  }

  /**
   * Builds a query string based on request parameter.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   */
  private String buildBrowseQuery(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    // Request variables.
    // WARNING: Security risk! Should be validated!
    String by      = request.getParameter("by");
    String name    = request.getParameter("name");
    if (by == null || by.equals("") || name == null || name.equals(""))
    {
      by   = "title";
      name = "a";
    }

    // Build query.
    StringBuilder query = new StringBuilder("SELECT m.id FROM movies m");

    // Add joins.
    query.append(" INNER JOIN genres_in_movies gm ON gm.movie_id = m.id" +
                 " INNER JOIN genres g ON g.id = gm.genre_id");

    // Add where clause.
    if (by.equals("genre")) query.append(" WHERE LOWER(g.name) = LOWER('" + name + "')");
    else query.append(" WHERE LOWER(m.title) LIKE LOWER('" + name + "%')");

    query.append(" GROUP BY m.id");

    return query.toString();
  }

  /**
   * Builds a url for the sorting of movies, carrying along request variables.
   *
   * @param String requestURL The request url with request variables.
   * @param String sortBy The original sort_by. 
   * @param String orderBy The original order_by.
   * @param String newSortBy The new column to sort by.
   * @param String newOrderBy The ordering (ASC/DESC).
   * @return String The built URL.
   */
  private String prepareSortingURL(String requestURL,
                                   String sortBy,
                                   String orderBy,
                                   String newSortBy,
                                   String newOrderBy)
  {
    String sortURL = requestURL;
    if (sortBy != null && !sortBy.equals("")) sortURL = sortURL.replace("sort_by="+sortBy, "sort_by="+newSortBy);
    else sortURL += "&sort_by="+newSortBy;

    if (orderBy != null && !orderBy.equals("")) sortURL = sortURL.replace("order_by="+orderBy, "order_by="+newOrderBy);
    else sortURL += "&order_by="+newOrderBy;
    return sortURL;
  }
}