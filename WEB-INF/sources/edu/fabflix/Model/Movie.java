/*============================================
 * Movie
 *============================================
 * A Model for movies.
 *============================================*/

package edu.fabflix.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public class Movie
{
  private int    _id;
  private String _title;
  private int    _year;
  private String _director;
  private String _banner_url;
  private String _trailer_url;

  private ArrayList<Star> _stars;
  private ArrayList<String> _genres;

  /**
   * Constructor
   */
  public Movie()
  {
    _stars  = new ArrayList<Star>();
    _genres = new ArrayList<String>();
  }

  /**
   * Constructor
   * @param int id The id of the movie.
   * @param String title The movie's title.
   * @param String year The movie's year.
   * @param String director The movie's director.
   * @param String banner_url The movie's banner url.
   * @param String trailer_url The movie's trailer url.
   */
  public Movie(int    id,
               String title,
               int    year,
               String director,
               String banner_url,
               String trailer_url)
  {
    _id          = id;
    _title       = title;
    _year        = year;
    _director    = director;
    _banner_url  = banner_url;
    _trailer_url = trailer_url;

    _stars  = new ArrayList<Star>();
    _genres = new ArrayList<String>();
  }

  /**
   * Overrides
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equals = false;
    if (object != null && object instanceof Movie) equals = _id == ((Movie) object).id();
    return equals;
  }

  /**
   * Mutators
   */
  public void id(int id)                       { _id          = id;          }
  public void title(String title)              { _title       = title;       }
  public void year(int year)                   { _year        = year;        }
  public void director(String director)        { _director    = director;    }
  public void banner_url(String banner_url)    { _banner_url  = banner_url;  }
  public void trailer_url(String trailer_url)  { _trailer_url = trailer_url; }
  public void addStar(Star star)
  {
    if (_stars.contains(star)) return;
    _stars.add(star);
  }
  public void addGenre(String genre)
  { 
    if (_genres.contains(genre)) return;
    _genres.add(genre);
  }

  /**
   * Accessors
   */
  public int    id()          { return _id;           }
  public String title()       { return _title;        }
  public int    year()        { return _year;         }
  public String director()    { return _director;     }
  public String banner_url()  { return _banner_url;   }
  public String trailer_url() { return _trailer_url;  }
  public ArrayList<Star> stars()    { return _stars;  }
  public ArrayList<String> genres() { return _genres; }

  /**
   * Prepares the view with a list of movies.
   *
   * @param ResultSet rs The result set from a query containing movies.
   * @param HttpServletRequest request The current request.
   * @param String requestURL The url of the current request.
   * @param int rowCount The number of total movies this subset of movies is from.
   * @return boolean True if the list was correctly prepared, False otherwise.
   */
  static public boolean prepareMovieList(ResultSet rs, HttpServletRequest request)
    throws SQLException, Exception
  {
    // Full request url.
    String requestURL  = request.getRequestURL().toString();
    String queryString = request.getQueryString();
    if (queryString != null && !queryString.equals("")) requestURL += "&"+queryString;
    return prepareMovieList(rs, request, requestURL, 0);
  }
  static public boolean prepareMovieList(ResultSet rs, HttpServletRequest request, String requestURL, int rowCount)
    throws SQLException, Exception
  {
    // Request variables.
    // WARNING: Security risk! Should be valided!
    int pageNum = 1;
    try
    {
      if (request.getParameter("page_num") != null) pageNum = Integer.parseInt(request.getParameter("page_num"));
    }
    catch (NumberFormatException e) {}

    int numPerPage = 5;
    try
    {
      if (request.getParameter("num_per_page") != null) numPerPage = Integer.parseInt(request.getParameter("num_per_page"));
    }
    catch (NumberFormatException e) {}

    Movie movie = new Movie();
    movie.id(0);
    Star star;
    ArrayList<Movie>        movies         = new ArrayList<Movie>();
    HashMap<Integer, Movie> movieContainer = new HashMap<Integer, Movie>();
    while (rs.next())
    {
      // Add genre and star
      if (movieContainer.containsKey(rs.getInt("m_id")))
      {
        movie = movieContainer.get(rs.getInt("m_id"));
        movie.addGenre(rs.getString("name"));
        movie.addStar(new Star(
          rs.getInt("s_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("dob"),
          rs.getString("photo_url")
        ));
      }
      else
      {
        movie = new Movie(
          rs.getInt("m_id"),
          rs.getString("title"),
          rs.getInt("year"),
          rs.getString("director"),
          rs.getString("banner_url"),
          rs.getString("trailer_url"));
        movie.addGenre(rs.getString("name"));
        movie.addStar(new Star(
          rs.getInt("s_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("dob"),
          rs.getString("photo_url")
        ));

        movies.add(movie);
        movieContainer.put(movie.id(), movie);
      }
    }
    if (!movies.isEmpty())
    {
      request.setAttribute("movies", movies);
      request.setAttribute("total_pages", (rowCount+numPerPage-1)/numPerPage); // Round up.
      request.setAttribute("page_number", pageNum);
      request.setAttribute("u_pagination", preparePaginationURL(requestURL, pageNum, numPerPage));
      request.setAttribute("u_traversal", prepareTraversalURL(requestURL, pageNum));
      return true;
    }
    else return false;
  }

  /**
   * Builds a url for the pagination of movies, carrying along request variables.
   *
   * @param String requestURL The request url with request variables.
   * @param int pageNum The current page number.
   * @param int numPerPage The current number of movies to display per page.
   * @return String The built URL.
   */
  static private String preparePaginationURL(String requestURL,
                                      int    pageNum,
                                      int    numPerPage)
  {
    String paginationURL = requestURL;
    paginationURL = paginationURL.replace("page_num="+pageNum, "page_num=1");
    paginationURL = paginationURL.replace("&num_per_page="+numPerPage+"&", "&");
    paginationURL = paginationURL.replace("&num_per_page="+numPerPage, "");
    return paginationURL + "&num_per_page=";
  }

  /**
   * Builds a url for the traversal of movies, carrying along request variables.
   *
   * @param String requestURL The request url with request variables.
   * @param int pageNum The current page numver.
   * @return String The built URL.
   */
  static private String prepareTraversalURL(String requestURL,
                                     int   pageNum)
  {
    String traversalURL = requestURL;
    traversalURL = traversalURL.replace("&page_num="+pageNum+"&", "&");
    traversalURL = traversalURL.replace("&page_num="+pageNum, "");
    return traversalURL + "&page_num=";
  }
}