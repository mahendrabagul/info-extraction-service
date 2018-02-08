package io.github.knowmyminister.infoextractionservice.util;

import io.github.knowmyminister.infoextractionservice.Selectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author mahendra.hiraman
 */
@Component
public class InfoExtracter
{
    public void connect(String ministerName)
    {
        try
        {
            Document document = Jsoup.connect("http://www.google.com/search?q=" + ministerName).get();
            fullNameExtractor(document);
            positionExtractor(document);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void positionExtractor(Document document)
    {
        if (Objects.nonNull(document))
        {
            Element positionElement = document.select(Selectors.POSITION_SELECTOR).first();
            if (Objects.nonNull(positionElement))
            {
                System.out.println("Position : " + positionElement.text());
            }
        }
    }

    private void fullNameExtractor(Document document)
    {
        if (Objects.nonNull(document))
        {
            Element fullNameElement = document.select(Selectors.FULL_NAME_SELECTOR_1).first();
            if (Objects.isNull(fullNameElement))
            {
                fullNameElement = document.select(Selectors.FULL_NAME_SELECTOR_2).first();
            }
            if (Objects.nonNull(fullNameElement))
            {
                System.out.println("FullName : " + fullNameElement.text());
            }
        }
    }

    public static void main(String[] args)
    {
        InfoExtracter infoExtracter = new InfoExtracter();
        infoExtracter.connect("arvind kejriwal");
        infoExtracter.connect("Devendra Fadnavis");
        infoExtracter.connect("Narendra Modi");
        infoExtracter.connect("Sharad Pawar");

    }
}
