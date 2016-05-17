/*============================================
 * Checkout Servlet
 * route: {context}/checkout
 * method: GET/POST
 *============================================
 * A Servlet to handle checkout requests.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;
import java.io.IOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.fabflix.Model.Cart;
import edu.fabflix.Model.Customer;
import edu.fabflix.Helper.Validator;

public class CheckoutServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet handles shopping checkout requests.";
  }

  /**
   * Handle GET requests. Serve the checkout page.
   * @param HttpServletRequest request
   * @param HttpServletResponse response
   * @throws IOExpection if an I/O error occurs
   * @throws ServletExceptionif a servlet-specific error occurs
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");

    // Redirect of cart is empty.
    if (((Cart) request.getSession().getAttribute("cart")).all().isEmpty())
    {
      request.setAttribute("error", "Your cart is empty.");
      request.getRequestDispatcher("cart.jsp").forward(request, response);
      return;
    }

    // Redirect to checkout form.
    request.getRequestDispatcher("checkout.jsp").forward(request, response);
    return;
  }

  /**
   * Handle POST requests. Authorize the checkout request.
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
    
    // Redirect of cart is empty.
    if (((Cart) request.getSession().getAttribute("cart")).all().isEmpty())
    {
      request.setAttribute("error", "Your cart is empty.");
      request.getRequestDispatcher("cart.jsp").forward(request, response);
      return;
    }
    
    // Request Parameters.
    // WARNING: Security Risk, SQL Injection.
    String cc_id      = request.getParameter("cc_id");
    String first_name = request.getParameter("first_name");
    String last_name  = request.getParameter("last_name");
    String expiration = request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day");

    // Avoid database requests and redirect back to form with error if input validation failed.
    Validator validator      = new Validator();
    ArrayList<String> errors = new ArrayList<String>();
    if (!validator.validateCreditCard(cc_id)) errors.add(validator.error());
    if (!validator.validateText(first_name))  errors.add(validator.error("First name"));
    if (!validator.validateText(last_name))   errors.add(validator.error("Last name"));
    if (!validator.validateDate(expiration))  errors.add(validator.error("Expiration date"));
    if (!errors.isEmpty())
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("checkout.jsp").forward(request, response);
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
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon = DriverManager.getConnection(db_url, db_user, db_password);
      
      // Prepare query.
      String sql = "SELECT COUNT(*) FROM creditcards " +
                   "WHERE id=? AND first_name=? AND last_name=? AND expiration=?";
      statement = dbcon.prepareStatement(sql);
      statement.setString(1, cc_id);
      statement.setString(2, first_name);
      statement.setString(3, last_name);
      statement.setDate(4, Date.valueOf(expiration));

      rs = statement.executeQuery();
      rs.next();

      if (rs.getInt(1) > 0)
      {
        HttpSession session  = request.getSession(false);
        Customer    customer = (Customer) session.getAttribute("customer");
        Cart        cart     = (Cart)     session.getAttribute("cart");

        // Insert into sales table.
        sql = "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES("+customer.id()+", ?, NOW())";
        statement = dbcon.prepareStatement(sql);

        for (int movieID : cart.all())
        {
          statement.setInt(1, movieID);
          for (int i = 0; i < cart.get(movieID); ++i) { statement.executeUpdate(); }
        }

        // Complete checkout.
        cart.clear();
        session.setAttribute("cart", cart);
        request.setAttribute("checkedout", true);
      }
      else
      {
        errors.add("Credit card information is incorrect.");
        request.setAttribute("errors", errors);
      }
    }
    catch (Exception e)
    {
      errors.add("Could not perform checkout, please try again later.");
      request.setAttribute("errors", errors);
    }
    finally
    {
      // Close dbcons.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}

      // Redirect.
      if (errors.isEmpty()) request.getRequestDispatcher("checkout_confirmation.jsp").forward(request, response);
      else request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }
  }
}