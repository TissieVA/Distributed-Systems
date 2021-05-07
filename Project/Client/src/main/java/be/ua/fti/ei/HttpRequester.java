package be.ua.fti.ei;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;

public class HttpRequester
{
    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

    //we are using Gson, Gson is a java library that can convert Java Objects into their JSON representation,
    // it can also convert a JSON string into a java object. Exactly what we need. More info on https://github.com/google/gson
    private static final Gson gsonConvertor = new Gson();//has to be static cause otherwise can't access it from a static context.

    /**
     * Do a HTTP POST request with an empty response
     * @param url The url to POST to
     * @param body A JSON string containing the POST data
     */
    public static void httpRequestPOST(String url, String body)
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var client = HttpClient.newHttpClient();

        try
        {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    /**
     * Do a HTTP POST request with a response body
     * @param url The url to POST to
     * @param body A JSON string containing the POST data
     * @param cl The type of the response body
     * @return The response
     */
    public static Object httpRequestPOST(String url, String body, Class cl)
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var client = HttpClient.newHttpClient();

        try
        {
            var res = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(res.statusCode()==200)
            {
                return gsonConvertor.fromJson(res.body(), cl);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * Do a HTTP GET request with an empty response body
     * @param url The url to GET from
     */
    public static void httpRequestGet(String url)
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try
        {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    /**
     * Do a HTTP GET request with a response body
     * @param url The url to GET from
     * @param cl The type of the response body
     * @return The response
     */
    public static Object httpRequestGet(String url, Class cl)
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try
        {
            var res = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200)
            {
                //if the status code is 200 it means that the response is OK
                //we get a JSON response back so now we need to convert it to the class that was given as a parameter. See JSON conversion function(static)
                return gsonConvertor.fromJson(res.body(), cl);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }

        return null;
    }
}