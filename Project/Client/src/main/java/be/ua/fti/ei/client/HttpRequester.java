package be.ua.fti.ei.client;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;

public class HttpRequester
{
    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);

    private static final Gson gson = new Gson();

    public static void POST(String url, Object body)
    {
        HttpRequester.POST(url, gson.toJson(body));
    }

    /**
     * Do a HTTP POST request with an empty response
     * @param url The url to POST to
     * @param body A JSON string containing the POST data
     */
    public static void POST(String url, String body)
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

    public static Object POST(String url, Object body, Class cl)
    {
        return HttpRequester.POST(url, gson.toJson(body), cl);
    }

    /**
     * Do a HTTP POST request with a response body
     * @param url The url to POST to
     * @param body A JSON string containing the POST data
     * @param cl The type of the response body
     * @return The response
     */
    public static Object POST(String url, String body, Class cl)
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
                return gson.fromJson(res.body(), cl);
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
    public static void GET(String url)
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
    public static Object GET(String url, Class cl)
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
                return gson.fromJson(res.body(), cl);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }

        return null;
    }
}