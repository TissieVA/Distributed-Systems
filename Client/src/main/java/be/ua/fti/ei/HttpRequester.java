package be.ua.fti.ei;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class HttpRequester {
    //Deze klas maakt requests voor u naar een https server, als je een id wilt weten kan je het gewoon aanvragen, je kan andere files zoeken
    public void httprequestPOST(String url, String body){//string body json object
       /* HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")*/

    }
    public void httprequestGet(String url){

    }
}