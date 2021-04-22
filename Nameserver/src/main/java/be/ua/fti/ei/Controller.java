package be.ua.fti.ei;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller
{
    @GetMapping("/mapping/{filename}")
    Integer getFile(@PathVariable String filename)
    {
        return Database.getInstance().getID(filename);
    }

    @GetMapping("/mapping/createXML")
    boolean createXML()
    {
       return Database.getInstance().outputXML();
    }
}
