package be.ua.fti.ei;

import be.ua.fti.ei.http.PublishBody;
import be.ua.fti.ei.sockets.NextPreviousBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/find/{filename}")
    Node getFile(@PathVariable String filename, HttpServletRequest request)
    {

        logger.info("Find file request received");

        return Database.getInstance().searchFile(filename);
    }

    @GetMapping("/find/ipaddress/{filename}")
    String getFileIpAddress(@PathVariable String filename)
    {
        logger.info("Get file ip request received");
        return Database.getInstance().searchFile(filename).getIpaddress();
    }

    @PostMapping("/publish")
    NextPreviousBody publishNewNode(@RequestBody PublishBody body)
    {
        logger.info("Publish new node request received");

        if(!Database.getInstance().addNewNode(body.getHostname(), body.getFiles(), body.getIpAddress(),
                body.getMcPort(), body.getFilePort()))

            return null;

        return Database.getInstance().getNeighbours(body.getHostname());
    }

    //The remove node is not yet fully finished, needs to implement the next and previous node
    @GetMapping("/remove/{nodeName}")
    boolean removeNode(@PathVariable String nodeName)
    {
        logger.info("Remove node request received");
        int hash = Hasher.getHash(nodeName);

        NSmessageHandler.getInstance().updateNeighboursAfterDeletion(hash);

        return Database.getInstance().removeNode(nodeName); // remove the node (works)
    }
}
