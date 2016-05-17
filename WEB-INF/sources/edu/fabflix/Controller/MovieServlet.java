/*============================================
 * Movie Servlet
 * route: {context}/movie
 * method: GET/POST
 *============================================
 * A Servlet to display movie information, and
 * create new movies.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.fabflix.Model.Movie;
import edu.fabflix.Model.Star;
import edu.fabflix.Helper.Validator;


public class MovieServlet extends HttpServlet {

  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet to display information about a single movie.";
  }

  /**
   * Handle Get requests. Sends movie data to the view.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletException if a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html");

    // Request variables.
    // WARNING: Security risk! Should be cleaned.
    int movieID = 0;
    try
    {
      if (request.getParameter("id") != null) movieID = Integer.parseInt(request.getParameter("id"));
      else
      {
        request.setAttribute("error", "The movie does not exist.");
        request.getRequestDispatcher("movie.jsp").forward(request, response);
        return;
      }
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("error", "The movie does not exist.");
      request.getRequestDispatcher("movie.jsp").forward(request, response);
      return;
    }

    // Database authentications.
    String db_url      = getServletContext().getInitParameter("db_url");
    String db_user     = getServletContext().getInitParameter("db_user");
    String db_password = getServletContext().getInitParameter("db_password");

    // Connect to the database.
    Connection        dbcon     = null;
    PreparedStatement statement = null;
    ResultSet         rs        = null;
    try
    {
      // Find movie.
      String sql = "SELECT m.id m_id, s.id s_id, m.*, s.*, g.* FROM movies m " +
                   "INNER JOIN stars_in_movies sm ON sm.movie_id = m.id " +
                   "INNER JOIN stars s ON s.id = sm.star_id " +
                   "INNER JOIN genres_in_movies gm ON gm.movie_id = m.id " +
                   "INNER JOIN genres g ON g.id = gm.genre_id " +
                   "WHERE m.id = ?";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon     = DriverManager.getConnection(db_url, db_user, db_password);
      statement = dbcon.prepareStatement(sql);
      statement.setInt(1, movieID);
      rs = statement.executeQuery();

      Movie movie = null;
      if (rs.next())
      {
        movie = new Movie(
          rs.getInt("m_id"),
          rs.getString("title"),
          rs.getInt("year"),
          rs.getString("director"),
          rs.getString("banner_url"),
          rs.getString("trailer_url")
        );
        movie.addStar(new Star(
          rs.getInt("s_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("dob"),
          rs.getString("photo_url")
        ));
        movie.addGenre(rs.getString("name"));
      }
      while (rs.next())
      {
        movie.addStar(new Star(
          rs.getInt("s_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("dob"),
          rs.getString("photo_url")
        ));
      }
      if (movie != null) { request.setAttribute("movie", movie); }
      else request.setAttribute("error", "The movie does not exist.");
    }
    catch (SQLException ex)
    {
      request.setAttribute("error", "There was an error retrieving this resource. Please try again later.");
    }
    catch(Exception ex)
    {
      request.setAttribute("error", "There was an error retrieving this resource. Please try again later.");
    }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      request.getRequestDispatcher("movie.jsp").forward(request, response);
    }
  }

  /**
   * Handle POST requests. Add new movie to database.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");

    // Request variables.
    String title       = request.getParameter("title");
    String sYear       = request.getParameter("year");
    int    year        = 0;
    String director    = request.getParameter("director");
    String banner_url  = request.getParameter("banner_url");
    String trailer_url = request.getParameter("trailer_url");

    String first_name = request.getParameter("first_name");
    String last_name  = request.getParameter("last_name");
    String dob        = request.getParameter("dob");
    String photo_url  = request.getParameter("photo_url");

    String genre = request.getParameter("genre");

    Validator validator = new Validator();
    ArrayList<String> errors = new ArrayList<String>();

    // Require necessary variables.
    if (!validator.validateText(title))      errors.add(validator.error("Title"));
    if (!validator.validateYear(sYear))      errors.add(validator.error("Year"));
    else { try { year = Integer.parseInt(sYear); } catch (Exception e) {} }
    if (!validator.validateText(director))   errors.add(validator.error("Director"));
    if (!validator.validateURL(banner_url))  banner_url = "";
    if (!validator.validateURL(trailer_url)) trailer_url = "";

    // If only one name provided, make it the last name.
    if (!validator.validateText(first_name)) first_name = "";
    if (!validator.validateText(last_name))
    {
      if (!first_name.equals(""))
      {
        last_name  = first_name;
        first_name = "";
      }
      else errors.add(validator.error("First/Last name"));
    }
    if (!validator.validateDate(dob))      dob = null;
    if (!validator.validateURL(photo_url)) photo_url = "";

    if (!validator.validateText(genre)) errors.add(validator.error("Genre"));

    if (!errors.isEmpty())
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("/_dashboard").forward(request, response);
      return;
    }

    // Database authentications.
    String db_url      = getServletContext().getInitParameter("db_url");
    String db_user     = getServletContext().getInitParameter("db_user");
    String db_password = getServletContext().getInitParameter("db_password");

    // Connect to the database.
    Connection        dbcon     = null;
    CallableStatement statement = null;
    ResultSet         rs        = null;
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon = DriverManager.getConnection(db_url, db_user, db_password);

      // Called add_movie procedure.
      statement = dbcon.prepareCall("{call add_movie(?,?,?,?,?,?,?,?,?,?)}");
      statement.setString(1, title);
      statement.setInt(2, year);
      statement.setString(3, director);
      statement.setString(4, banner_url);
      statement.setString(5, trailer_url);
      statement.setString(6, first_name);
      statement.setString(7, last_name);
      if (dob == null) statement.setNull(8, java.sql.Types.DATE);
      else              statement.setString(8, dob);
      statement.setString(9, photo_url);
      statement.setString(10, genre);
      rs = statement.executeQuery();
      if (rs.next())
      {
        String procErrors[] = rs.getString("errors").split(";");
        String procSuccesses[] = rs.getString("successes").split(";");

        // Redirect.
        request.setAttribute("errors", procErrors);
        request.setAttribute("successes", procSuccesses);
        request.getRequestDispatcher("/_dashboard").forward(request, response);
      }
    }
    catch (Exception e)
    {
      errors.add(e.getMessage());
      errors.add("Failed to add new movie, please try again later.");
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("/_dashboard").forward(request, response);
    }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}
    }
  }
}