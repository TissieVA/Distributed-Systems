package be.ua.fti.ei;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller
{
    @GetMapping("/find/{filename}")
    Node getFile(@PathVariable String filename)
    {
        return Database.getInstance().searchFile(filename);
    }

    @GetMapping("/find/ipaddress/{filename}")
    String getFileIpAddress(@PathVariable String filename)
    {
        try
        {
            Thread.sleep(3000);
        }catch (Exception e){}
        
        return Database.getInstance().searchFile(filename).getIpaddress();
    }

    @PostMapping("/publish")
    boolean publishNewNode(@RequestBody PublishBody body)
    {
        return Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress());
    }

    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        return Database.getInstance().removeNode(nodeName);
    }
}
