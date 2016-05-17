/*============================================
 * Browse Servlet
 * route: {context}/browse
 * method: GET
 *============================================
 * A Servlet to provide browsing
 * functionality for movies.
 *============================================*/

package edu.fabflix.Controller;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BrowseServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet handles browsing of movies by genre and title.";
  }
  
  /**
   * Handle GET requests. Routes to movie list servlet with browse by title if no
   * request variables were given, else route with request variable.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    // Set parent route and forward to movie list servlet.
    request.setAttribute("mode", "browse");
    request.getRequestDispatcher("movies").forward(request, response);
  }
}