/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebias;

import java.util.*;
import java.io.*;
import static java.lang.Thread.sleep;
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
public class GoogleBias {// implements Job {

    /**
     * @param args the command line arguments
     */
    String projectPath;
    String APIkey;
    String SearchEngineID;
    int numberOfPages;
    List<String> listofQueries;

    public GoogleBias(String projectPath) {
        this.projectPath = projectPath;
        String fileName = "init.txt";

        List<String> params = IOUtils.readFileLineByLine(projectPath + "/" + fileName, false);
        this.APIkey = params.get(0);
        this.SearchEngineID = params.get(1);
        this.numberOfPages = Integer.parseInt(params.get(2));

        listofQueries = new ArrayList<>();
        for (int i = 3; i < params.size(); i++) {
            listofQueries.add(params.get(i));
        }

    }

    public void search(String query) {

        try {

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
                try {
                    Thread.sleep(1000);//to comply with the rule of 1query/user/sec
                } catch (InterruptedException ex) {
                    Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
                }
                connection.disconnect();

            }

            String filePath = projectPath + "/data/";//Local

            String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            String dataFile = filePath + query + "_" + currentTime;
            String urlFile = filePath + query + "_url_" + currentTime;
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

    //public void execute(JobExecutionContext context) throws JobExecutionException {
    public static void main(String[] args) {

        /*List<String> listofQueries = new ArrayList<>();

        listofQueries.add("democratic debate");
        listofQueries.add("dem debate");
        listofQueries.add("republican debate");
        listofQueries.add("rep debate");

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
         */
        GoogleBias gb = new GoogleBias(args[0]);

        for (int i = 0; i < gb.listofQueries.size(); i++) {
            gb.search(gb.listofQueries.get(i));
            try {
                Thread.sleep(1000);//to comply with the rule of 1 query/user/sec
            } catch (InterruptedException ex) {
                Logger.getLogger(GoogleBias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
