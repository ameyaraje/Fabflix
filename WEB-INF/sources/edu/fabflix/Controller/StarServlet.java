/*============================================
 * Star Servlet
 * route: {context}/star
 * method: GET/POST
 *============================================
 * A Servlet to display star information and
 * add new stars.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;

import java.io.IOException;

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

public class StarServlet extends HttpServlet {

  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet to display information about a single star.";
  }

  /**
   * Handle Get requests. Sends star data to the view.
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
    // WARNING: Security risk! Should be valided!
    int starID = 0;
    try
    {
      if (request.getParameter("id") != null) starID = Integer.parseInt(request.getParameter("id"));
      else
      {
        request.setAttribute("error", "The star does not exists.");
        request.getRequestDispatcher("star.jsp").forward(request, response);
        return;
      }
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("error", "The star does not exists.");
      request.getRequestDispatcher("star.jsp").forward(request, response);
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
      // Find star.
      String sql = "SELECT s.id s_id, m.id m_id, m.title, s.* FROM stars s " +
                   "INNER JOIN stars_in_movies sm ON sm.star_id = s.id " +
                   "INNER JOIN movies m ON m.id = sm.movie_id " +
                   "WHERE s.id = ?";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon     = DriverManager.getConnection(db_url, db_user, db_password);
      statement = dbcon.prepareStatement(sql);
      statement.setInt(1, starID);
      rs = statement.executeQuery();

      Star  star  = null;
      Movie movie = null;
      if (rs.next())
      {
        star = new Star(
          rs.getInt("s_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("dob"),
          rs.getString("photo_url"));
        movie = new Movie();
        movie.id(rs.getInt("m_id"));
        movie.title(rs.getString("title"));
        star.addMovie(movie);
      }
      while (rs.next())
      {
        movie = new Movie();
        movie.id(rs.getInt("m_id"));
        movie.title(rs.getString("title"));
        star.addMovie(movie);
      }
      if (star != null) request.setAttribute("star", star);
      else request.setAttribute("error", "The star does not exists.");
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

      request.getRequestDispatcher("star.jsp").forward(request, response);
    }
  }

  /**
   * Handle POST requests. Add new star to database.
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
    String first_name = request.getParameter("first_name");
    String last_name  = request.getParameter("last_name");
    String dob        = request.getParameter("dob");
    String photo_url  = request.getParameter("photo_url");

    Validator validator         = new Validator();
    ArrayList<String> errors    = new ArrayList<String>();
    ArrayList<String> successes = new ArrayList<String>();

    // If only one name provided, make it the last name.
    if (!validator.validateText(first_name)) first_name = "";
    if (!validator.validateText(last_name))
    {
      if (!first_name.equals(""))
      {
        last_name  = first_name;
        first_name = "";
      }
      else 
      {
        errors.add(validator.error("First/Last name"));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/_dashboard").forward(request, response);
        return;
      }
    }
    if (!validator.validateDate(dob))      dob = null;
    if (!validator.validateURL(photo_url)) photo_url = "";

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
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon = DriverManager.getConnection(db_url, db_user, db_password);

      // Make sure star doesn't already exist.
      String sql = "SELECT photo_url FROM stars WHERE first_name=? AND last_name=? AND (dob=? OR (dob IS NULL AND ? IS NULL))";
      statement = dbcon.prepareStatement(sql);
      statement.setString(1, first_name);
      statement.setString(2, last_name);
      statement.setString(3, dob);
      statement.setString(4, dob);
      rs = statement.executeQuery();

      if (!rs.next())
      {
        // Add star to database.
        sql = "INSERT INTO stars (first_name, last_name, dob, photo_url) VALUES (?, ?, ?, ?)";
        statement = dbcon.prepareStatement(sql);
        statement.setString(1, first_name);
        statement.setString(2, last_name);
        if (dob == null) statement.setNull(3, java.sql.Types.DATE);
        else statement.setString(3, dob);
        statement.setString(4, photo_url);
        statement.executeUpdate();
        successes.add("Successfully added new star.");
      }
      else if (!photo_url.equals(rs.getString("photo_url")))
      {
        // Update photo url.
        sql = "UPDATE stars SET photo_url=? WHERE first_name=? AND last_name=? AND (dob=? OR (dob IS NULL AND ? IS NULL))";
        statement = dbcon.prepareStatement(sql);
        statement.setString(1, photo_url);
        statement.setString(2, first_name);
        statement.setString(3, last_name);
        statement.setString(4, dob);
        statement.setString(5, dob);
        statement.executeUpdate();
        successes.add("Successfully updated star.");
      }
      else errors.add("Star already exists in the database.");
    }
    catch (Exception e) { errors.add(e.getMessage()/*"Failed to add new star, please try again later."*/); }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      request.setAttribute("errors", errors);
      request.setAttribute("successes", successes);
      request.getRequestDispatcher("/_dashboard").forward(request, response);
    }
  }
}