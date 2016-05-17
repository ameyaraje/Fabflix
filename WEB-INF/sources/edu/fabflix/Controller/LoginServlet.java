/*============================================
 * Login Servlet
 * route: {context}/login
 * method: GET/POST
 *============================================
 * A Servlet to validate data from login form.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.fabflix.Model.Cart;
import edu.fabflix.Model.Customer;
import edu.fabflix.Helper.VerifyRecaptchaUtil;
import edu.fabflix.Helper.Validator;

public class LoginServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet connects to MySQL database and validates data from login form";
  }
  
  /**
   * Handle GET requests. Redirects to login page.
   *
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");

    // Make sure customer not already logged in.
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("customer") != null)
    {
      response.sendRedirect("main");
      return;
    }

    // Redirect to login form.
    request.getRequestDispatcher("login.jsp").forward(request, response);
    return;
  }

  /**
   * Handle POST requests. Authenticate customer and redirect on success.
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

    // Make sure customer not already logged in.
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("customer") != null)
    {
      response.sendRedirect("main");
      return;
    }

    // Database authentications.
    String db_url      = getServletContext().getInitParameter("db_url");
    String db_user     = getServletContext().getInitParameter("db_user");
    String db_password = getServletContext().getInitParameter("db_password");

    // Request variables.
    String email    = request.getParameter("email");
    String password = request.getParameter("password");

    // Verify reCAPTCHA.
    ArrayList<String> errors  = new ArrayList<String>();
    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
    if (!VerifyRecaptchaUtil.verify(gRecaptchaResponse)) errors.add("reCAPTCHA is invalid.");

    // Avoid database requests and redirect back to login form with error if input validation failed.
    Validator validator = new Validator();
    if (!validator.validateEmail(email))       errors.add(validator.error());
    if (!validator.validatePassword(password)) errors.add(validator.error());
    if (!errors.isEmpty())
    {
      // Carry redirect URL along.
      request.setAttribute("u_redirect", request.getParameter("u_redirect"));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("login.jsp").forward(request, response);
      return;
    }
    
    // Connect to the database.
    Connection dbcon     = null;
    Statement  statement = null;
    ResultSet  rs        = null;
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon     = DriverManager.getConnection(db_url, db_user, db_password);
      statement = dbcon.createStatement();

      // Query for customer.
      String query = "SELECT * FROM customers WHERE email = '"+email+"' AND password = '"+password+"' LIMIT 1;";
      rs = statement.executeQuery(query);

      if (rs.next())
      {
        Customer customer = new Customer(
          rs.getInt("id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getString("cc_id"),
          rs.getString("address"),
          rs.getString("email"));

        // Set session.
        session = request.getSession(true);
        session.setAttribute("customer", customer);
        session.setAttribute("cart", new Cart());
        session.setMaxInactiveInterval(60 * 60 * 24 * 30); // 30 Days.
      }
      else
      {
        // Set error from user authentication.
        errors.add("Incorrect email/password.");
        request.setAttribute("errors", errors);
      }
    }
    catch (SQLException ex)
    {
      errors.add("Login failed. Please try again later.");
      request.setAttribute("errors", errors);
    }
    catch(Exception ex)
    {
      errors.add("Login failed. Please try again later.");
      request.setAttribute("errors", errors);
    }
    finally
    {
      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      if (errors.isEmpty())
      {
        String u_redirect = (String) request.getParameter("u_redirect");
        if (u_redirect != null &&
            !u_redirect.equals("")) response.sendRedirect(u_redirect);
        else response.sendRedirect("main");
      }
      else
      {
        // Carry redirect URL along.
        request.setAttribute("u_redirect", request.getParameter("u_redirect"));
        request.getRequestDispatcher("login.jsp").forward(request, response);
      }
    }
  }
}