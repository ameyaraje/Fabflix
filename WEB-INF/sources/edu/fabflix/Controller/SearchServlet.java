/*============================================
 * Search Servlet
 * route: {context}/search
 * method: GET
 *============================================
 * A Servlet to provide advanced search
 * functionality for movies.
 *============================================*/

package edu.fabflix.Controller;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet handles searching of movies.";
  }
  
  /**
   * Handle GET requests. Redirects to search page if no request variables are present
   * else routes to movie list servlet.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (request.getParameterNames().hasMoreElements())
    {
      // Set parent route and forward to movie list servlet.
      request.setAttribute("mode", "search");
      request.getRequestDispatcher("movies").forward(request, response);
      return;
    }

    // Redirect to search form.
    response.setContentType("text/html");
    request.getRequestDispatcher("search.jsp").forward(request, response);
  }
}