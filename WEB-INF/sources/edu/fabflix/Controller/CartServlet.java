/*============================================
 * Cart Servlet
 * route: {context}/cart
 * method: GET/POST
 *============================================
 * A Servlet to serve the cart page.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.fabflix.Model.Cart;
import edu.fabflix.Model.Movie;

public class CartServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet shows all the items in the shopping cart.";
  }

  /**
   * Handle GET requests. Retrieve movies in the shopping cart and send it to the view.
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");

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
      dbcon = DriverManager.getConnection(db_url, db_user, db_password);

      String movieIDs = ((Cart) request.getSession(false).getAttribute("cart")).all().toString();
      movieIDs = movieIDs.replace("[", "");
      movieIDs = movieIDs.replace("]", "");

      String sql = "SELECT m.id m_id, s.id s_id, m.*, s.*, g.* FROM movies m " +
                   "INNER JOIN stars_in_movies sm ON sm.movie_id = m.id " +
                   "INNER JOIN stars s ON s.id = sm.star_id " +
                   "INNER JOIN genres_in_movies gm ON gm.movie_id = m.id " +
                   "INNER JOIN genres g ON g.id = gm.genre_id " +
                   "WHERE m.id IN ("+movieIDs+")";
      statement = dbcon.createStatement();
      rs        = statement.executeQuery(sql);

      boolean prepared = Movie.prepareMovieList(rs, request);
      if (!prepared) { request.setAttribute("error", "No movies in cart."); }
    }
    catch (SQLException ex) { request.setAttribute("error", "No movies in cart."); }
    catch (Exception ex)    { request.setAttribute("error", "No movies in the cart."); }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
  }

  /**
   * Handle POST requests. Modify cart content.
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");
    
    // Only perform one action. Clear cart has priority.
    HttpSession session = request.getSession(false);
    Cart cart = (Cart) session.getAttribute("cart");
    if ("true".equals(request.getParameter("clear")))
    {
      cart.clear();
      session.setAttribute("cart", cart);

      // Redirect to cart.
      request.setAttribute("success", "Cart successfully cleared.");
      request.getRequestDispatcher("cart.jsp").forward(request, response);
      return;
    }

    // Update item quantity in cart.
    int movieID      = 0;
    int quantity     = 0;
    boolean hasError = false;
    try
    {
      if (request.getParameter("id") != null) movieID = Integer.parseInt(request.getParameter("id"));
      else hasError = true;

      if (request.getParameter("quantity") != null) quantity = Integer.parseInt(request.getParameter("quantity"));
      else hasError = true;
    }
    catch (NumberFormatException e) { hasError = true; }

    if (hasError)
    {
      request.setAttribute("error", "Not a valid movie.");
      request.getRequestDispatcher("cart.jsp").forward(request, response);
      return;
    } 
    cart.add(movieID, quantity);
    session.setAttribute("cart", cart);

    // Redirect to cart.
    request.setAttribute("success", "Movie added to cart.");
    doGet(request, response);
    return;
  }
}