package io.github.knowmyminister.infoextractionservice.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseZillowGuide {
    public static void main(String args[])
    {
        print("running...");
        Document document;
        try
        {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect("https://www.google.co.in/search?q=narendra+modi")
                    .get();
            document = Jsoup.connect("https://www.google.co.in/search?q=narendra+modi")
                    .timeout(5000)
                    .get();
            String title = document.title(); //Get title
            print("  Title: " + title); //Print title.

            Elements price = document.select("#rhs_block > div > div.kp-blk.knowledge-panel.Wnoohf.OJXvsb > div > div.ifM9O > div:nth-child(2) > div.kp-header > div > div.kp-hc > div > div > div.d1rFIf > div.kno-ecr-pt.kno-fb-ctx > span"); //Get price

            print(price.text());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        print("done");

    }


    public static void print(String string)
    {
        System.out.println(string);
    }

}