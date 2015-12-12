/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebias;

import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.slf4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Motahhare
 */
public class GoogleBias implements Job{

    /**
     * @param args the command line arguments
     */
    public static void search(String query, int numberOfPages) {

        try {

            // Setting up the google API URL
            String APIkey = "AIzaSyBjuuChi0hV1B8WR9Jlu8_IaY3Am7cLLLI";
            String SearchEngineID = "010165319554274961907:fpaojyvacss";
            query = URLEncoder.encode(query, "UTF-8");
            String result = "";
            String urls = "";

            for (int i = 0; i < numberOfPages; i++) {
                // Using google developer API key to use custom search
                //System.out.println(i);
                String start = Integer.toString(i * 10 + 1);
                String stringURL = "https://www.googleapis.com/customsearch/v1?key=" + APIkey + "&cx=" + SearchEngineID + "&q=" + query
                        + "&alt=json" + "&start=" + start;
                //System.out.println("***Query=" + stringURL);
                URL url = new URL(stringURL);

                // Opening a connection and sending a request
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                BufferedReader buffer;

                // To check for potential errors
                if (connection.getResponseCode() >= 400) {
                    buffer = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                    String bufferLine;
                    while ((bufferLine = buffer.readLine()) != null) {
                        System.out.println("Error:: " + bufferLine);
                    }
                } else {
                    String bufferLine;
                    buffer = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                    while ((bufferLine = buffer.readLine()) != null) {
                        // System.out.println(bufferLine);
                        result += bufferLine + "\n";

                        if (bufferLine.contains("\"link\": \"")) {
                            String link = bufferLine.substring(bufferLine.indexOf("\"link\": \"") + ("\"link\": \"").length(),
                                    bufferLine.indexOf("\","));

                            urls += link + "\n";
                        }
                    }
                }

                connection.disconnect();
            }

            String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            String dataFile = "data/" + query + "_" + currentTime;
            String urlFile = "data/" + query + "_url_" + currentTime;
            IOUtils.writeDataIntoFile(result, dataFile);
            IOUtils.writeDataIntoFile(urls, urlFile);

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        
   
        List<String> listofQueries = new ArrayList<>();
        
        listofQueries.add("democratic debate");
        listofQueries.add("dem debate");
        listofQueries.add("republican debate");
        listofQueries.add("rep debate");
        listofQueries.add("democratic debate");
        
        listofQueries.add("Bernie Sanders");
        listofQueries.add("Martin O'Malley");
        listofQueries.add("Hillary Clinton");
          
        listofQueries.add("Jeb Bush");
        listofQueries.add("Ben Carson");
        listofQueries.add("Chris Christie");
        listofQueries.add("Ted Cruz");
        listofQueries.add("Carly Fiorina");
        listofQueries.add("Jim Gilmore");
        listofQueries.add("Lindsey Graham");
        listofQueries.add("Mike Huckabee");
        listofQueries.add("John Kasich");
        listofQueries.add("George Pataki");
        listofQueries.add("Rand Paul");
        listofQueries.add("Marco Rubio");
        listofQueries.add("Rick Santorum");
        listofQueries.add("Donald Trump");
        
        for (int i = 0; i < listofQueries.size(); i++) {
            search(listofQueries.get(i), 2);
        }

    }

}
