package com.zoonies.cinc.connectors.pollen;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * This Java source file was auto generated by running 'gradle buildInit --type java-library'
 * by 'tboyles' at '12/6/14 1:57 AM' with Gradle 2.0
 *
 * @author tboyles, @date 12/6/14 1:57 AM
 */
public class Pollen {

    private static final String URL = "http://www.wunderground.com/DisplayPollen.asp?Zipcode=94402";


    public static void main(String[] args) {
        System.out.println("Im the main method of the pollen scraper");
        try {
            System.out.println(getRawPollenData());
        } catch (Exception e) {
            System.out.println("Boo!");
        }
    }

    public static String getRawPollenData() throws Exception {
        String rawPollenData = "Im some data";
        try {

            Document doc = Jsoup.connect(URL).get();
            Elements levels = doc.getElementsByClass("levels");
            // URL obj = new URL(URL);
            // HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
            // con.setRequestMethod("GET");
 
            // con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
            // int responseCode = con.getResponseCode();
            // System.out.println("\nSending 'GET' request to URL : " + URL);
            // System.out.println("Response Code : " + responseCode);
 
            // BufferedReader in = new BufferedReader(
            //     new InputStreamReader(con.getInputStream()));
            // String inputLine;
            // StringBuffer response = new StringBuffer();
            // while ((inputLine = in.readLine()) != null) {
            //     response.append(inputLine);
            // }
            // in.close();
            // rawPollenData = response.toString();
            System.out.println("rawPollenData");

        } catch (Exception e) {
            throw new Exception ("Something untoward has happened: " + e.toString());
        }

        return rawPollenData;
    }

    public boolean someLibraryMethod() {
        return true;
    }
}
