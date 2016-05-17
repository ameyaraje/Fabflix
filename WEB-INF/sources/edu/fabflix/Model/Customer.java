/*============================================
 * Customer
 *============================================
 * A Model for customers.
 *============================================*/

package edu.fabflix.Model;

public class Customer
{
  private int    _id;
  private String _firstname;
  private String _lastname;
  private String _cc_id;
  private String _address;
  private String _email;

  /**
   * Constructor
   */
  public Customer() {}

  /**
   * Constructor
   * @param int id The id of the customer.
   * @param String firstname The customer's first name.
   * @param String lastname The customer's last name.
   * @param String cc_id The customer's credit card id.
   * @param String address The customer's address.
   * @param String email The customer's email address.
   */
  public Customer(int    id,
                  String firstname,
                  String lastname,
                  String cc_id,
                  String address,
                  String email)
  {
    _id        = id;
    _firstname = firstname;
    _lastname  = lastname;
    _cc_id     = cc_id;
    _address   = address;
    _email     = email;
  }

  /**
   * Mutators
   */
  public void id(int id)                   { _id        = id;        }
  public void first_name(String firstname) { _firstname = firstname; }
  public void last_name(String lastname)   { _lastname  = lastname;  }
  public void cc_id(String cc_id)          { _cc_id     = cc_id;     }
  public void address(String address)      { _address   = address;   }
  public void email(String email)          { _email     = email;     }

  /**
   * Accessors
   */
  public int    id()         { return _id;        }
  public String first_name() { return _firstname; }
  public String last_name()  { return _lastname;  }
  public String cc_id()      { return _cc_id;     }
  public String address()    { return _address;   }
  public String email()      { return  _email;    }
}