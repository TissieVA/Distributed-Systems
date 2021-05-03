package be.ua.fti.ei;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;

public class HttpRequester {


    //we are using Gson, Gson is a java library that can convert Java Objects into their JSON representation,
    // it can also convert a JSON string into a java object. Exactly what we need. More info on https://github.com/google/gson
    private static Gson gsonConvertor=new Gson();//has to be static cause otherwise can't acces it from a static context.

    //when we don't get anything back we can use this
    public static void httprequestPOST(String url, String body){//string body json object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var client = HttpClient.newHttpClient();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //when we don't get anything back we can use this
    public static Object httprequestPOST(String url, String body,Class cl){//string body json object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var client = HttpClient.newHttpClient();
        try {
            var res=client.send(request, HttpResponse.BodyHandlers.ofString());
            if(res.statusCode()==200){
                return gsonConvertor.fromJson(res.body(), cl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }








    //GET that doesn't get anything back
    public static void httprequestGet(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //GET that doesn't get anything back
    public static Object httprequestGet(String url,Class cl){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            var res = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode()==200){//if the status code is 200 it means that the response is OK
                //we get a JSON response back so now we need to convert it to the class that was given as a parameter. See JSON conversion function(static)
                return gsonConvertor.fromJson(res.body(),cl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }





}