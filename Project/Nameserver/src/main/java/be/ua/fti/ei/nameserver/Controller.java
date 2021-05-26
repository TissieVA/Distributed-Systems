package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.Hasher;
import be.ua.fti.ei.utils.http.FileBody;
import be.ua.fti.ei.utils.http.NodeBody;
import be.ua.fti.ei.utils.http.PublishBody;
import be.ua.fti.ei.utils.sockets.NextPreviousBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controller
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/find/{filename}")
    NodeBody getFile(@PathVariable String filename, HttpServletRequest request)
    {
        logger.info("Find file request received");

        Node n = Database.getInstance().searchFile(filename);
        return new NodeBody(n.getName(),n.getIpaddress(), n.getFilePort());
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


    @GetMapping("/nodes")
    List<String> getAllNodes()
    {
        return  Database.getInstance().getHostDatabase().values().stream().map(Node::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/files")
    ArrayList<String> getAllFiles()
    {
        return (ArrayList<String>) Database.getInstance().getHostDatabase().values().stream()
                .flatMap(n -> n.getFiles().stream()).collect(Collectors.toList());
    }

    @GetMapping("/files/{node}")
    ArrayList<String> getFilesInNode(@PathVariable String node)
    {
        int hash = Hasher.getHash(node);

        return Database.getInstance().getHostDatabase().get(hash).getFiles();
    }


    @GetMapping("/replicates/{nodeName}")
    List<FileBody> getReplicates(@PathVariable String nodeName)
    {
        logger.info("getReplicated request received");
        return Database.getInstance().getReplicatesOfNode(Hasher.getHash(nodeName));
    }
}
