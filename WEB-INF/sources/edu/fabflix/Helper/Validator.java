/*============================================
 * Validator
 *============================================
 * A simple class to validate form data.
 *============================================*/

package edu.fabflix.Helper;

public class Validator
{
  private String _error;

  /**
   * Constructor
   */
  public Validator() { _error = ""; }

  /**
   * Validates an email input.
   *
   * @param String email The email address to be validated.
   * @return boolean True if valid email, False otherwise.
   */
  public boolean validateEmail(String email)
  {
    if (email != null && email.contains("@") && email.contains(".")) return true;
    _error = "Email address is invalid.";
    return false;
  }

  /**
   * Validates a password input.
   *
   * @param String password The password to be validated.
   * @return boolean True if valid password, False otherwise.
   */
  public boolean validatePassword(String password)
  {
    if (password != null && !password.equals("")) return true;
    _error = "Password cannot be empty.";
    return false;
  }

  /**
   * Validates a credit card number.
   *
   * @param String card The credit card number.
   * @return boolean True if valid card, False otherwise.
   */
  public boolean validateCreditCard(String card)
  {
    if (card != null && !card.equals("")) return true;
    _error = "Credit card cannot be empty.";
    return false;
  }

  /**
   * Validates a URL. A field name must be provided using the error(String) method.
   *
   * @param String url
   * @return boolean True if valid, False otherwise.
   */
  public boolean validateURL(String url)
  {
    if (url != null && !url.equals("")) return true;
    _error = "field cannot be empty.";
    return false;
  }

  public boolean validateYear(String year)
  {
    if (year != null && !year.equals(""))
    {
      if (year.length() != 4)
      {
        _error = "field is invalid.";
        return false;
      }
      try { Integer.parseInt(year); }
      catch (Exception e)
      {
        _error = "field is invalid.";
        return false;
      }
      return true;
    }
    else
    {
      _error = "field cannot be empty.";
      return false;
    }
  }

  /**
   * Validates a date. A field name must be provided using the error(String) method.
   *
   * @param String date
   * @return boolean True if valid, False otherwise.
   */
  public boolean validateDate(String date)
  {
    if (date == null || date.equals(""))
    {
      _error = "field is missing.";
      return false;
    }
    else if (!date.matches("[0-9-]+"))
    {
      _error = "field is invalid.";
      return false;
    }
    return true;
  }

  /**
   * Validates a text. A field name must be provided using the error(String) method.
   *
   * @param String text
   * @return boolean True if valid, False otherwise.
   */
  public boolean validateText(String text)
  {
    if (text != null && !text.equals("")) return true;
    _error = "field is missing.";
    return false;
  }

  /**
   * Returns the error of the current input field.
   *
   * @return String The error of the current input, empty String if no error.
   */
  public String error()
  {
    return _error;
  }

  /**
   * Returns the error of the current input field.
   *
   * @param String field The name of the input field.
   * @return String The error of the current input, empty String if no error.
   */
  public String error(String field)
  {
    return _error.replace("field", field);
  }
}