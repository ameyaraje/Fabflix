DROP PROCEDURE IF EXISTS add_movie;

DELIMITER //
CREATE PROCEDURE add_movie(
  IN _title       VARCHAR(100),
  IN _year        INT UNSIGNED,
  IN _director    VARCHAR(100),
  IN _banner_url  VARCHAR(200),
  IN _trailer_url VARCHAR(200),
  IN _first_name  VARCHAR(50),
  IN _last_name   VARCHAR(50),
  IN _dob         DATE,
  IN _photo_url   VARCHAR(200),
  IN _genre       VARCHAR(32))
BEGIN
  DECLARE _star_id    INT UNSIGNED DEFAULT 0;
  DECLARE _url        VARCHAR(200) DEFAULT "";
  DECLARE _genre_id   INT UNSIGNED DEFAULT 0;
  DECLARE _movie_id   INT UNSIGNED DEFAULT 0;
  DECLARE _g_in_m     INT UNSIGNED DEFAULT 0;
  DECLARE _s_in_m     INT UNSIGNED DEFAULT 0;
  DECLARE _successes  VARCHAR(200) DEFAULT "";
  DECLARE _errors     VARCHAR(200) DEFAULT "";
  DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN END;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    SELECT "There was an error inserting new movie. No changes made." AS "errors", "" AS "successes";
    ROLLBACK;
  END;

  START TRANSACTION;
    SELECT id, photo_url INTO _star_id, _url FROM stars
    WHERE first_name = _first_name
      AND last_name  = _last_name
      AND (dob = _dob OR (dob IS NULL AND _dob IS NULL));

    SELECT id INTO _genre_id FROM genres
    WHERE name = _genre;

    SELECT id INTO _movie_id FROM movies
    WHERE title    = _title
      AND year     = _year
      AND director = _director;

    IF _star_id = 0 THEN
      IF _first_name != "" OR _last_name != "" THEN
        INSERT INTO stars (first_name, last_name, dob, photo_url)
        VALUES (_first_name, _last_name, _dob, _photo_url);
        SET _star_id = LAST_INSERT_ID();
        SET _successes = "Added new star.";
      END IF;
    ELSEIF _url != _photo_url THEN
      UPDATE stars SET photo_url = _photo_url WHERE id = _star_id;
      SET _successes = CONCAT_WS(";", _successes, "Updated star's photo URL.");
    ELSE SET _errors = "Star exists.";
    END IF;

    IF _genre_id = 0 THEN
      IF _genre != "" THEN
        INSERT INTO genres (name) VALUES (_genre);
        SET _genre_id = LAST_INSERT_ID();
        SET _successes = CONCAT_WS(";", _successes, "Added new genre.");
      END IF;
    ELSE SET _errors = CONCAT_WS(";", _errors, "Genre exists.");
    END IF;

    IF _movie_id = 0 THEN
      INSERT INTO movies (title, year, director, banner_url, trailer_url)
      VALUES (_title, _year, _director, _banner_url, _trailer_url);
      SET _movie_id = LAST_INSERT_ID();
      SET _successes = CONCAT_WS(";", _successes, "Added new movie.");
    ELSE SET _errors = CONCAT_WS(";", _errors, "Movie exists.");
    END IF;

    IF _genre != "" THEN
      SELECT COUNT(*) INTO _g_in_m FROM genres_in_movies
      WHERE genre_id = _genre_id AND movie_id = _movie_id;

      IF _g_in_m = 0 THEN
        INSERT INTO genres_in_movies (genre_id, movie_id)
        VALUES (_genre_id, _movie_id);
        SET _successes = CONCAT_WS(";", _successes, "Added new genre to movie.");
      ELSE SET _errors = CONCAT_WS(";", _errors, "Movie already has this genre.");
      END IF;
    END IF;

    IF _first_name != "" OR _last_name != "" THEN
      SELECT COUNT(*) INTO _s_in_m FROM stars_in_movies
      WHERE star_id = _star_id AND movie_id = _movie_id;

      IF _s_in_m = 0 THEN
        INSERT INTO stars_in_movies (star_id, movie_id)
        VALUES (_star_id, _movie_id);
        SET _successes = CONCAT_WS(";", _successes, "Added new star to movie.");
      ELSE SET _errors = CONCAT_WS(";", _errors, "Movie already has this star.");
      END IF;
    END IF;

    SELECT _successes AS "successes", _errors AS "errors";
  COMMIT;
END//
DELIMITER ;