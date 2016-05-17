/*============================================
 * Dashboard Servlet
 * route: {context}/_dashboard
 * method: GET/POST
 *============================================
 * A Servlet to allow employees to login and
 * add new content to the movies database.
 *============================================*/

package edu.fabflix.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.fabflix.Model.Employee;
import edu.fabflix.Helper.Validator;

public class DashboardServlet extends HttpServlet
{
  /**
   * Returns information about the servlet, such as author, version, and copyright
   *
   * @return String information about the servlet, such as author, version, and copyright.
   */
  public String getServletInfo()
  {
    return "Servlet allows employees to login and add new movies and stars.";
  }

  /**
   * Handle GET requests. Redirects to login page if not logged in,
   * else serve dashboard.
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

    // Serve login page if employee not authenticated, else serve dashboard.
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("employee") != null)
    {
      // Send database metadata to dashboard.
      HashMap<String, HashMap<String, String>> tables = getDatabaseMetaData();
      if (tables == null) request.setAttribute("errors", new String[]{"Failed to retrieve database metaadata."});
      else request.setAttribute("tables", tables);
      request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
    else
      request.getRequestDispatcher("/dashboard_login.jsp").forward(request, response);
  }

  /**
   * Handle POST requests. Authenticate employee and redirect on success.
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

    // Make sure the employee is not already logged in.
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("employee") != null)
    {
      // Send database metadata to dashboard.
      HashMap<String, HashMap<String, String>> tables = getDatabaseMetaData();
      if (tables == null) request.setAttribute("errors", new String[]{"Failed to retrieve database meetadata."});
      else request.setAttribute("tables", tables);
      request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
      return;
    }

    // Request variables.
    String email    = request.getParameter("email");
    String password = request.getParameter("password");

    // Avoid database requests and redirect back to login form with error if validation failed.
    Validator validator      = new Validator();
    ArrayList<String> errors = new ArrayList<String>();
    if (!validator.validateEmail(email))       errors.add(validator.error());
    if (!validator.validatePassword(password)) errors.add(validator.error());
    if (!errors.isEmpty())
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher("/dashboard_login.jsp").forward(request, response);
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

      // Query for employee.
      String sql = "SELECT * FROM employees WHERE email=? AND password=?";
      statement = dbcon.prepareStatement(sql);
      statement.setString(1, email);
      statement.setString(2, password);
      rs = statement.executeQuery();

      if (rs.next())
      {
        Employee employee = new Employee(
          rs.getString("email"),
          rs.getString("fullname"));

        // Set session employee.
        session = request.getSession(true);
        session.setAttribute("employee", employee);
        session.setMaxInactiveInterval(60 * 30); // 30 min.
      }
      else errors.add("Incorrect email/password.");
    }
    catch (Exception e) { errors.add("Login failed. Please try again later."); }
    finally
    {
      // Redirect.
      if (errors.isEmpty())
      {
        // Send database metadata to dashboard.
        HashMap<String, HashMap<String, String>> tables = getDatabaseMetaData(dbcon);
        if (tables == null) errors.add("Failed to retrieve database metadataa.");
        else request.setAttribute("tables", tables);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
      }
      else
      {
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/dashboard_login.jsp").forward(request, response);
      }

      // Close connections.
      try { rs.close();        } catch (Exception e) {}
      try { statement.close(); } catch (Exception e) {}
      try { dbcon.close();     } catch (Exception e) {}
    }
  }

  /**
   * Gets the database meta data.
   *
   * @param  Connection dbcon An active database connection.
   * @return HashMap<String, HashMap<String, String>> {Table Name: {Column Name: Data Type}}.
   */
  private HashMap<String, HashMap<String, String>> getDatabaseMetaData()
  {
    // Database authentications.
    String db_url      = getServletContext().getInitParameter("db_url");
    String db_user     = getServletContext().getInitParameter("db_user");
    String db_password = getServletContext().getInitParameter("db_password");

    // Connect to the database.
    Connection dbcon = null;
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      dbcon = DriverManager.getConnection(db_url, db_user, db_password);
      return getDatabaseMetaData(dbcon);
    }
    catch (Exception e) { return null; }
    finally { try { dbcon.close(); } catch (Exception e) {} }
  }
  private HashMap<String, HashMap<String, String>> getDatabaseMetaData(Connection dbcon)
  {
      ResultSet rs = null;
      DatabaseMetaData metadata = null;
      HashMap<String, HashMap<String, String>> tables = null;
      try
      {
        metadata = dbcon.getMetaData();
        String types[] = {"TABLE"};

        // Get all tables.
        rs     = metadata.getTables(null, null, null, types);
        tables = new HashMap<String, HashMap<String, String>>();
        while (rs.next())
          tables.put(rs.getString("TABLE_NAME"), new HashMap<String, String>());

        // Get all columns.
        for (String table : tables.keySet())
        {
          rs = metadata.getColumns(null, null, table, null);
          while (rs.next())
            tables.get(table).put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME")+"("+rs.getString("COLUMN_SIZE")+")");
        }
        return tables;
      }
      catch (Exception e) {}
      finally
      {
        // Close result set.
        try { rs.close(); } catch (Exception e) {}
        return tables;
      }
  }
}