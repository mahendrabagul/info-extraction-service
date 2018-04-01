package io.github.knowmyminister.infoextractionservice.rest.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class UploadController {

    private static String API_URL = "/admin/";
    private static String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");

    @GetMapping("/")
    public String index()
    {
        return "upload";
    }

    @PostMapping("/uploadMinisters")
    public String uploadMinistersData(
            @RequestParam("file")
                    MultipartFile file, RedirectAttributes redirectAttributes)
    {
        System.out.println(UPLOADED_FOLDER);
        if (file.isEmpty())
        {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:" + API_URL + "uploadStatus";
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
                message = "You successfully uploaded '" + file.getOriginalFilename() + "'";
            }
            redirectAttributes.addFlashAttribute("message", message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "redirect:" + API_URL + "uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus()
    {
        return "uploadStatus";
    }

}
