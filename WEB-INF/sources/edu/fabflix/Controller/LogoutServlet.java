/*============================================
 * Logout Servlet
 * route: {context}/logout
 * method: POST
 *============================================
 * A Servlet to log the customer out.
 *============================================*/

package edu.fabflix.Controller;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet disconnects the user by killing their sessions.";
  }

  /**
   * Handle POST requests. Kills the customer's session.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // Kill session.
    HttpSession session = request.getSession(false);
    if (session != null) session.invalidate();
    response.sendRedirect("login");
  }
}