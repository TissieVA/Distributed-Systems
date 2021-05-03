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
        Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress());

        //if this requester is the only one => nextID = requester, previousID = requester
        int n = (int) Database.getInstance().getHostDatabase().keySet().stream().count();
        if (n == 1)
            return new NextPrevious(Hasher.getHash(body.hostname),Hasher.getHash(body.hostname),n);
        else
        {
            int next,previous;
            Integer higherNeighbour = Database.getInstance().higherNeighbour();
            if(higherNeighbour == null)
                higherNeighbour = Database.getInstance().getHostDatabase().keySet().stream().sorted().findFirst();


            Integer lowerNeighbour = Database.getInstance().lowerNeighbour();
            if (higherNeighbour != null && lowerNeighbour != null)
                return new NextPrevious(higherNeighbour,lowerNeighbour,n);
            else if ()

        }


    }



    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        return Database.getInstance().removeNode(nodeName);
    }
}
