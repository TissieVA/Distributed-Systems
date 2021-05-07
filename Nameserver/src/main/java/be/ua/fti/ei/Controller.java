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
        return Database.getInstance().searchFile(filename).getIpaddress();
    }

    @PostMapping("/publish")
    NextPrevious publishNewNode(@RequestBody PublishBody body)
    {
        if(!Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress()))
            return null;

        // If no other nodes exist => nextID = requester, previousID = requester
        int hosts = Database.getInstance().getHostDatabase().size();
        int hash = Hasher.getHash(body.getHostname());

        if (hosts == 1)
        {
            return new NextPrevious(hash, hash, hosts);
        }
        else
        {
            int higherNeighbour = Database.getInstance().getHigherNeighbour(hash);
            int lowerNeighbour = Database.getInstance().getLowerNeighbour(hash);
            return new NextPrevious(lowerNeighbour, higherNeighbour, hosts);
        }
    }

    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        return Database.getInstance().removeNode(nodeName);
    }
}
