package io.github.knowmyminister.infoextractionservice.rest.resource;

import io.github.knowmyminister.infoextractionservice.domain.Minister;
import io.github.knowmyminister.infoextractionservice.service.AsyncService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/admin")
public class UploadController {

    private static String API_URL = "/admin/";
    @Value("${knowmyminister.uploadedfiles.path}") private String uploadedFilesPath;

    private static String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");
    @Resource AsyncService services;

    @GetMapping("/")
    public String index()
    {
        return "upload";
    }

    @PostMapping("/uploadFile")
    public String uploadMinistersData(
            @RequestParam("file")
                    MultipartFile file, RedirectAttributes redirectAttributes)
    {
        if (file.isEmpty())
        {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:" + API_URL;
        }
        try
        {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            String message = null;
            if (!(file.getOriginalFilename()
                    .endsWith("xls") || file.getOriginalFilename()
                    .endsWith("xlsx")))
            {
                message = "You have uploaded wrong file : '" + file.getOriginalFilename() + "'";
                Files.delete(path);
            }
            else
            {
                Files.copy(path, Paths.get(uploadedFilesPath + file.getOriginalFilename()));
                Future<List<Minister>> process = services.process(path.toString());
                while (!(process.isDone()))
                {
                    Thread.sleep(2000);
                }
                List<Minister> ministers = process.get();
                Map<String, String> result = new HashMap<>();
                result.put("TOTAL", String.valueOf(ministers.size()));
                message = result.toString();
            }
            redirectAttributes.addFlashAttribute("message", message);
        }
        catch (IOException | InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return "redirect:" + API_URL;
    }
}
