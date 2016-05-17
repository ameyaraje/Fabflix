/*============================================
 * Star
 *============================================
 * A Model for stars.
 *============================================*/

package edu.fabflix.Model;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Star
{
  private int _id;
  private String _first_name;
  private String _last_name;
  private Date   _dob;
  private String _photo_url;
  private ArrayList<Movie> _movies;

  /**
   * Constructor
   */
  public Star()
  {
    _id         = 0;
    _first_name = "";
    _last_name  = "";
    _dob        = null;
    _photo_url  = "";
    _movies     = new ArrayList<Movie>();
  }

  /**
   * Constructor
   */
  public Star(int    id,
              String first_name,
              String last_name,
              Date   dob,
              String photo_url)
  {
    _id         = id;
    _first_name = first_name;
    _last_name  = last_name;
    _dob        = dob;
    _photo_url  = photo_url;
    _movies     = new ArrayList<Movie>();
  }

  /**
   * Overrides
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equals = false;
    if (object != null && object instanceof Star) equals = _id == ((Star) object).id();
    return equals;
  }

  /**
   * Mutators
   */
  public void id(int id)                    { _id         = id;         }
  public void first_name(String first_name) { _first_name = first_name; }
  public void last_name(String last_name)   { _last_name  = last_name;  }
  public void dob(Date dob)                 { _dob        = dob;        }
  public void photo_url(String photo_url)   { _photo_url  = photo_url;  }
  public void addMovie(Movie movie)
  {
    if (_movies.contains(movie)) return;
    _movies.add(movie);
  }

  /**
   * Accessors
   */
  public int    id()         { return _id;         }
  public String first_name() { return _first_name; }
  public String last_name()  { return _last_name;  }
  public Date   dob()        { return _dob;        }
  public String photo_url()  { return _photo_url;  }
  public String full_name()  { return _first_name + " " + _last_name; }
  public ArrayList<Movie> movies() { return _movies; }
}