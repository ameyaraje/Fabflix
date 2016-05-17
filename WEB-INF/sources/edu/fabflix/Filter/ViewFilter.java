/*============================================
 * View Filter
 *============================================
 * A Filter to redirect users if they try to
 * access a .jsp.
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
 
public class ViewFilter implements Filter
{
  /**
   * Init - required, but no init needed.
   *
   * @param FilterConfig config
   */
  public void init(FilterConfig config) {}

  /**
   * Filter incoming requests. Redirect to login page if user tries to access
   * a view directly.
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

    res.sendRedirect("login");
  }

  /**
   * Destroy - required, but no clean up needed.
   */
  public void destroy() {}
}
 