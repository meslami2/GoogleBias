/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebias;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Motahhare
 */
public class DataAnalyze {

    //***Returns the domain name of a url
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    //***Build a map with key:domain and value: all  properties of a Media to use as dictionary
    public static Map<String, Media> buildADAMap(String ADAFilePath) {//
        Map<String, Media> ADAMap = new HashMap<>();

        List<String> lines = IOUtils.readFileLineByLine(ADAFilePath, true);
        for (int i = 0; i < lines.size(); i++) {
            String[] props = lines.get(i).split("\\s+");//Domain    Name    Type    Score
            /*for (int j = 0; j < props.length; j++){
                System.out.println("props"+ j+ ":"+ props[j]);
            }*/
            Media m = new Media(props[0], props[1], props[2], Double.parseDouble(props[3]));
            //System.out.println("media:" + m);
            ADAMap.put(props[0], m);
        }
        return ADAMap;
    }

    //Traverse data files in dir with "keyword" in the files' names to build the main file
    public static void traverseFiles(String dir, String keyword, String ADAFilePath, String dataFilePath) {
        File directory;
        File[] files;
        Map<String, Media> ADAMap = DataAnalyze.buildADAMap(ADAFilePath);
        // "C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/ADAScores.txt"       

        try {
            directory = new File(dir);
            // returns pathnames for files and directory
            files = directory.listFiles();

            for (File f : files) {
                // System.out.println(f.getPath());// prints file and directory paths
                if (f.getName().contains(keyword)) { //API: "_url_". Scrape: "_info_"
                    //***Parse File Name***
                    String fileName = f.getName();
                    String[] fileprops = fileName.split("_");
                    //File Info
                    String query = fileprops[0];
                    String date = fileprops[2];
                    String time = fileprops[3];

                    //Line Info
                    int index = 0;
                    String domain;
                    String name;
                    String type;
                    double score;

                    //***Read the File***
                    List<String> lines = IOUtils.readFileLineByLine(f.getPath(), false);
                    String input = "";
                    for (int i = 0; i < lines.size(); i++) {

                        index = i + 1;
                        domain = getDomainName(lines.get(i));
                        input += query + "\t" + date + "\t" + time + "\t" + index + "\t"
                                + domain + "\t";
                        if (ADAMap.get(domain) != null) {//if this domain exists in dic
                            Media m = ADAMap.get(domain);
                            input += m.name + "\t" + m.type + "\t" + m.score + "\n";
                        } else {
                            input += "null" + "\t" + "null" + "\t" + "null" + "\n";
                        }
                    }
                    
                    IOUtils.writeDataIntoFile(input, dataFilePath, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        //System.out.println(DataAnalyze.getDomainName("http://money.cnn.com/2015/10/14/media/cnn-democratic-debate-ratings-record/"));
        //DataAnalyze.traverseFiles("C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/data/us", "_url_");
        /* 
        Map<String, Media> ADAMap = DataAnalyze.buildADAMap("C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/ADAScores.txt");
        for (String key : ADAMap.keySet()) {
            System.out.println(key + " " + ADAMap.get(key));
        }
         */
        String dir = "C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/data/us";
        String keyword = "_url_";
        String ADAFilePath = "C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/ADAScores.txt";
        String dataFilePath = "C:/Users/Mostafa/Documents/NetBeansProjects/BiasAPI/data/dataFile.txt";
        DataAnalyze.traverseFiles(dir, keyword, ADAFilePath, dataFilePath);
    }

}
