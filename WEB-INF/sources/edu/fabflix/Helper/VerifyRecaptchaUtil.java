/*============================================
 * VerifyRecaptchaUtil
 *============================================
 * A simple class to verify Google's recaptcha.
 *============================================*/

package edu.fabflix.Helper;

import java.io.InputStream;
import java.io.OutputStream;
 
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.net.ssl.HttpsURLConnection;

import java.net.URL;

import edu.fabflix.Constants;


public class VerifyRecaptchaUtil
{
  public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

  /**
   * Verify that the g-recaptcha-response is valid.
   *
   * @param String gRecaptchaResponse The g-recaptcha-response sent with the request.
   * @return boolean True if valid, False otherwise.
   */
  public static boolean verify(String gRecaptchaResponse)
  {
    if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0)
      return false;

    String params = "secret="+Constants.SECRET_KEY+"&response="+gRecaptchaResponse;
    try
    {
      // Make post request to google's recaptcha verification.
      URL verifyUrl          = new URL(VERIFY_URL);
      HttpsURLConnection con = (HttpsURLConnection) verifyUrl.openConnection();  
      con.setRequestMethod("POST");

      con.setDoOutput(true);
      OutputStream outStream = con.getOutputStream();
      outStream.write(params.getBytes());
      outStream.flush();
      outStream.close();

      // Read response.
      InputStream inStream  = con.getInputStream();
      JsonReader jsonReader = Json.createReader(inStream);
      JsonObject jsonObject = jsonReader.readObject();
      jsonReader.close();
      return jsonObject.getBoolean("success");
    }
    catch (Exception e) { return false; }
  }
}