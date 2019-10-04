
/* A Java program for a Client */
import java.net.*;
import java.io.*;
import java.util.*;

public class http_client
{

/* constructor */
public http_client(String loc)
{
  try{
    URL url = new URL(loc);
    HttpURLConnection c = (HttpURLConnection) url.openConnection();
    c.setRequestMethod("GET");
    int responseCode = c.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
      String newLoc = c.getHeaderField("Location");
      URL newurl = new URL(newLoc);
      c = (HttpURLConnection) newurl.openConnection();
      responseCode = c.getResponseCode();
    }

    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedWriter output = new BufferedWriter(new FileWriter("http_client_output"));
      output.write("Printing HTTP header info from: " + loc + "\n");

      Map<String, List<String>> headers = c.getHeaderFields();
      Set<String> keys = headers.keySet();
      for (String k:keys) {
        output.append(k + " " + headers.get(k) + "\n");
      }
      output.append("\n\nURL content...\n");

      BufferedReader input = new BufferedReader(new InputStreamReader(c.getInputStream()));

      String line = input.readLine();
      while(line != null) {
        output.append(line);
        line = input.readLine();
      }

      input.close();
      output.close();

    } else {
      System.out.println("GET failed");
    }

  } catch(Exception e) {
    System.out.println(e);
  }
}

public static void main(String args[])
{
	if (args.length < 1) {
		System.out.println("http_client usage: java http_client #http://hostname/path/to/file");
	}
	else {
		http_client client = new http_client(args[0]);
	}
}

}
