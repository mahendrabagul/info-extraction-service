package io.github.knowmyminister.infoextractionservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.knowmyminister.infoextractionservice.domain.Minister;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONGenerator {
    public static void generateFile(List<Minister> ministers) throws IOException
    {
        new ObjectMapper().writeValue(new File("ministersData.json"), ministers);
    }

    public static String generate(List<Minister> ministers) throws IOException
    {
        return new ObjectMapper().writeValueAsString(ministers);
    }
}
