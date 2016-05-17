/*============================================
 * Like Predicate Servlet
 * route: {context}/reports/like-predicate
 * method: GET
 *============================================
 * A Servlet to route to like-predicate.txt
 *============================================*/

package edu.fabflix;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LikePredicate extends HttpServlet
{
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    request.getRequestDispatcher("like-predicate.txt").forward(request, response);
  }
}