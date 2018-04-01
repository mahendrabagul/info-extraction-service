package io.github.knowmyminister.infoextractionservice.util;

import io.github.knowmyminister.infoextractionservice.common.KMM_CONSTANTS;
import io.github.knowmyminister.infoextractionservice.domain.Minister;
import io.github.knowmyminister.infoextractionservice.selectors.SelectorsContants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author mahendra.hiraman
 */
@Component
public class InfoExtracter {
    private static int count = 0;

    public static void main(String[] args) throws IOException
    {
        InfoExtracter infoExtracter = new InfoExtracter();
        List<Minister> ministers = new ArrayList<>();
        readMinistersName();
        for (String ministerName : readMinistersName())
        {
            ministers.add(infoExtracter.connect(ministerName));
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        //ExcelGenerator.generate(ministers);
        JSONGenerator.generate(ministers);
    }

    private static Set<String> readMinistersName()
    {
        Set<String> ministers = new HashSet<>();
        try (FileReader fr = new FileReader(KMM_CONSTANTS.FILENAME); BufferedReader br = new BufferedReader(fr))
        {
            String ministerName = null;
            while ((ministerName = br.readLine()) != null)
            {
                ministers.add(ministerName);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ministers;
    }

    public Minister connect(String ministerName)
    {
        Minister minister = new Minister();
        try
        {
            Document document = Jsoup.connect(KMM_CONSTANTS.GOOGLE_URL + ministerName)
                    .timeout(5000)
                    .get();
            String fullName = extractVal(document, SelectorsContants.FULL_NAME_SELECTOR_1);
            if (StringUtils.isEmpty(fullName))
            { minister.setFullName(extractVal(document, SelectorsContants.FULL_NAME_SELECTOR_2)); }
            else { minister.setFullName(fullName); }
            minister.setBrief(extractVal(document, SelectorsContants.BRIEF_INFO_SELECTOR));
            minister.setCurrentDesignation(extractVal(document, SelectorsContants.DESIGNATION_SELECTOR));
            minister.setBorn(extractVal(document, SelectorsContants.BIRTHDATE_SELECTOR));
            minister.setOfficialSite(extractVal(document, SelectorsContants.WEBSITE_SELECTOR));
            minister.setParty(extractVal(document, SelectorsContants.PARTY_SELECTOR));
            minister.setSpouse(extractVal(document, SelectorsContants.SPOUSE_SELECTOR));
            minister.setWikipediaUrl(extractAttr(document, SelectorsContants.WIKIPEDIA_LINK_SELECTOR, "href"));
            minister.setFacebookUrl(extractAttr(document, SelectorsContants.FACEBOOK_LINK_SELECTOR, "href"));
            minister.setInstagramUrl(extractAttr(document, SelectorsContants.INSTAGRAM_LINK_SELECTOR, "href"));
            minister.setGooglePlusUrl(extractAttr(document, SelectorsContants.GOOGLE_PLUS_LINK_SELECTOR, "href"));
            minister.setLinkedInUrl(extractAttr(document, SelectorsContants.LINKEDIN_LINK_SELECTOR, "href"));
            minister.setTwitterUrl(extractAttr(document, SelectorsContants.TWITTER_LINK_SELECTOR, "href"));
            minister.setYoutubeUrl(extractAttr(document, SelectorsContants.YOUTUBE_LINK_SELECTOR, "href"));

            System.out.println(++count);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return minister;
    }

    private String extractVal(Document document, String selectorConstant)
    {
        if (Objects.nonNull(document))
        {
            Element selectorElement = document.select(selectorConstant)
                    .first();
            if (Objects.nonNull(selectorElement))
            {
                return selectorElement.text();
            }
        }
        return null;
    }

    private String extractAttr(Document document, String selectorConstant, String attribute)
    {
        if (Objects.nonNull(document))
        {
            Element selectorElement = document.select(selectorConstant)
                    .first();
            if (Objects.nonNull(selectorElement))
            {
                return selectorElement.attr(attribute);
            }
        }
        return null;
    }
}
