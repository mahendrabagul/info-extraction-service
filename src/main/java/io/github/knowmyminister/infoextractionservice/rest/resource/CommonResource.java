package io.github.knowmyminister.infoextractionservice.rest.resource;

import io.github.knowmyminister.infoextractionservice.util.InfoExtracter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mahendra.hiraman on 2/8/2018.
 */
@RestController
@RequestMapping("/")
public class CommonResource
{
    @Autowired
    InfoExtracter infoExtracter;

    @GetMapping("/hello")
    public String sayHello()
    {
        infoExtracter.connect("");
        return "Mahendra";
    }
}
