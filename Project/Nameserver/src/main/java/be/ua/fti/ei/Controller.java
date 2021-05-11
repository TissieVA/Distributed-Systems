package be.ua.fti.ei;

import be.ua.fti.ei.http.NextPrevious;
import be.ua.fti.ei.http.PublishBody;
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
    NextPrevious publishNewNode(@RequestBody PublishBody body)
    {
        if(!Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress()))
            return null;

        return Database.getInstance().getNextPrevious(body.getHostname());
    }
    //The remove node is not yet fully finished, needs to implement the next and previous node
    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        int hash = Hasher.getHash(nodeName);
        //these two neighbours need to change. The higher neigbour needs to be connected to the lowerNeighbour.
        int higherNeighbour =Database.getInstance().getHigherNeighbour(hash);
        int lowerNeighbour = Database.getInstance().getLowerNeighbour(hash);
        return Database.getInstance().removeNode(nodeName);// remove the node (works)
    }
}
