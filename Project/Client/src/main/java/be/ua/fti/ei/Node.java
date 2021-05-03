package be.ua.fti.ei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Node
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(Node.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
