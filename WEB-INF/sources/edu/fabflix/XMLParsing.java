/*============================================
 * XML Parsing Optimization Servlet
 * route: {context}/reports/xml_parsing_optimization
 * method: GET
 *============================================
 * A Servlet to route to
 * xml_parsing_optimization.txt
 *============================================*/

package edu.fabflix;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class XMLParsing extends HttpServlet
{
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    request.getRequestDispatcher("xml_parsing_optimization.txt").forward(request, response);
  }
}