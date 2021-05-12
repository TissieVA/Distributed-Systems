package be.ua.fti.ei;

import be.ua.fti.ei.http.PublishBody;
import be.ua.fti.ei.sockets.NextPreviousBody;
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
        return Database.getInstance().searchFile(filename).getIpaddress();
    }

    @PostMapping("/publish")
    NextPreviousBody publishNewNode(@RequestBody PublishBody body)
    {
        if(!Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress(),
                body.getMcPort(), body.getFilePort()))
            return null;

        return Database.getInstance().getNeighbours(body.getHostname());
    }

    //The remove node is not yet fully finished, needs to implement the next and previous node
    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        int hash = Hasher.getHash(nodeName);

        NSmessageHandler.getInstance().updateNeighboursAfterDeletion(hash);

        return Database.getInstance().removeNode(nodeName);// remove the node (works)
    }
}
