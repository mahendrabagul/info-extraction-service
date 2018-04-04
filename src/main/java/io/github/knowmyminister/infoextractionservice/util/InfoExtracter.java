package io.github.knowmyminister.infoextractionservice.util;

import io.github.knowmyminister.infoextractionservice.common.KMM_CONSTANTS;
import io.github.knowmyminister.infoextractionservice.domain.Minister;
import io.github.knowmyminister.infoextractionservice.selectors.SelectorsContants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author mahendra.hiraman
 */
@Component
public class InfoExtracter {
    private static int count = 0;

    public static void main(String[] args) throws IOException {
//        generateJson();
        generateImages();
    }

    private static void generateImages() throws IOException {
        InfoExtracter infoExtracter = new InfoExtracter();
        List<String> images = new ArrayList<>();
        for (String ministerName : readMinistersName()) {
            images.add(infoExtracter.getImage(ministerName));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(images);
        images.forEach(image -> decodeToImage(image));
    }

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private static void generateJson() throws IOException {
        InfoExtracter infoExtracter = new InfoExtracter();
        List<Minister> ministers = new ArrayList<>();
        for (String ministerName : readMinistersName()) {
            ministers.add(infoExtracter.connect(ministerName));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JSONGenerator.generateFile(ministers);
    }

    private static Set<String> readMinistersName() {
        Set<String> ministers = new HashSet<>();
        try (FileReader fr = new FileReader(KMM_CONSTANTS.FILENAME); BufferedReader br = new BufferedReader(fr)) {
            String ministerName = null;
            while ((ministerName = br.readLine()) != null) {
                ministers.add(ministerName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ministers;
    }

    public String getImage(String ministerName) {
        String image = null;
        String url = KMM_CONSTANTS.GOOGLE_URL + ministerName;
        try {
            Document document = Jsoup.connect(url)
                    .timeout(5000)
                    .get();
            image = extractAttr(document, SelectorsContants.IMAGE, "src");
            System.out.println(++count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public Minister connect(String ministerName) {
        Minister minister = new Minister();
        try {
            Document document = Jsoup.connect(KMM_CONSTANTS.GOOGLE_URL + ministerName)
                    .timeout(5000)
                    .get();
            String fullName = extractVal(document, SelectorsContants.FULL_NAME_SELECTOR_1);
            if (StringUtils.isEmpty(fullName)) {
                minister.setFullName(extractVal(document, SelectorsContants.FULL_NAME_SELECTOR_2));
            } else {
                minister.setFullName(fullName);
            }
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return minister;
    }

    private String extractVal(Document document, String selectorConstant) {
        if (Objects.nonNull(document)) {
            Element selectorElement = document.select(selectorConstant)
                    .first();
            if (Objects.nonNull(selectorElement)) {
                return selectorElement.text();
            }
        }
        return null;
    }

    private String extractAttr(Document document, String selectorConstant, String attribute) {
        if (Objects.nonNull(document)) {
            Element selectorElement = document.select(selectorConstant)
                    .first();
            Attributes attr = selectorElement.attributes();
            System.out.println(attr);
            if (Objects.nonNull(selectorElement)) {
                return selectorElement.attr(attribute);
            }
        }
        return null;
    }
}
