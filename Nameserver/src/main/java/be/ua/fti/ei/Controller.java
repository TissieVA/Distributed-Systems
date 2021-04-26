package be.ua.fti.ei;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller
{
    @GetMapping("/find/{filename}")
    String getFile(@PathVariable String filename)
    {
        return Database.getInstance().searchFile(filename);
    }

    @PostMapping("/publish")
    boolean publishNewNode(@RequestBody PublishBody body)
    {
        return Database.getInstance().addNewNode(body.getHostname(), body.getFiles());
    }
}
