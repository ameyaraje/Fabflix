/*============================================
 * Authentication Filter
 *============================================
 * A Filter to redirect users if they are not
 * logged in.
 *============================================*/

package edu.fabflix.Filter;
 
import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
public class AuthenticationFilter implements Filter
{
  /**
   * Init - required, but no init needed.
   *
   * @param FilterConfig config
   */
  public void init(FilterConfig config) {}

  /**
   * Filter incoming requests. Redirect to login page if user not logged in.
   *
   * @param ServletRequest request
   * @param ServletRequest response
   * @param FilterChain chain
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest req  = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("customer") == null)
    {
      // If user tried to access another page, redirect there after login.
      req.setAttribute("u_redirect", req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
      req.getRequestDispatcher("login").forward(req, res);
    }
    else chain.doFilter(request, response);
  }

  /**
   * Destroy - required, but no clean up needed.
   */
  public void destroy() {}
}
 