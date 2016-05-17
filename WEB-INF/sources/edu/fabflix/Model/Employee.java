/*============================================
 * Employee
 *============================================
 * A Model for employees.
 *============================================*/

package edu.fabflix.Model;

public class Employee
{
  private String _email;
  private String _fullname;

  /**
   * Constructor
   */
  public Employee() {}

  /**
   * Constructor
   * @param String email    The employee's email.
   * @param String fullname The employee's fullname.
   */
  public Employee(String email, String fullname)
  {
    _email    = email;
    _fullname = fullname;
  }

  /**
   * Accessors
   */
  public String email()    { return _email;    }
  public String fullname() { return _fullname; }
}