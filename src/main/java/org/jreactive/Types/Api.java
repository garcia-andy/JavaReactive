package org.jreactive.Types;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

public class Api {
    private URI apiUrl;
    private RequestParams defaultParams;

    public record RequestParams(
            String endpoint,
            Map<String,String> params,
            Map<String,String> headers,
            int connectionTimeout,
            boolean followRedirects
    ){
        public RequestParams(){
            this(null,null,null,10000,true);
        }

        public RequestParams(String endpoint){
            this(endpoint,null,null,10000,true);
        }

        public RequestParams(String endpoint,Map<String,String> params){
            this(endpoint,params,null,10000,true);
        }

        public RequestParams(String endpoint,Map<String,String> params, Map<String,String> headers){
            this(endpoint,params,headers,10000,true);
        }

        public RequestParams(String endpoint,Map<String,String> params, Map<String,String> headers, int rTime){
            this(endpoint,params,headers,rTime,true);
        }
    }

    public enum RequestType{
        GET,
        POST,
        PUT,
        DELETE
    }

    public Api(String Url){
        this(Url,new RequestParams());
    }

    public Api(String Url, RequestParams p) {
        setApi(Url);
        setParams(p,new RequestParams());
    }

    public void setParams(RequestParams p){
        setParams(p,defaultParams);
    }

    public void setParams(RequestParams p, RequestParams elseP){
        defaultParams = (p != null) ? p : elseP;
    }

    public RequestParams getParams(){
        return defaultParams;
    }

    public void setApi(String Url){
        // Sanitize the URL
        Url = (Url.endsWith("/")) ? Url : Url + "/";
        apiUrl = URI.create(Url);
    }

    public URI getApi()
    { return apiUrl; }

    /**
     * Construct URL with params
     */
    private static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return !resultString.isEmpty()
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    // GET

    public <T>HttpResponse<T> GET(
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return GET(null, callback); }

    public <T>HttpResponse<T> GET(
            RequestParams req,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.GET,req,null,callback); }

    // DELETE

    public <T>HttpResponse<T> DELETE(
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return DELETE(null, callback); }

    public <T>HttpResponse<T> DELETE(
            RequestParams req,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.DELETE,req,null,callback); }

    /* POSTS */

    public <T>HttpResponse<T> POST(
            RequestParams req,
            String body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.POST,req, HttpRequest.BodyPublishers.ofString(body),callback); }

    public <T>HttpResponse<T> POST(
            RequestParams req,
            byte[] body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.POST,req, HttpRequest.BodyPublishers.ofByteArray(body),callback); }

    public <T>HttpResponse<T> POST(
            RequestParams req,
            Iterable<byte[]> body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.POST,req, HttpRequest.BodyPublishers.ofByteArrays(body),callback); }

    public <T>HttpResponse<T> POST(
            RequestParams req,
            Path body,
            Function<HttpResponse.ResponseInfo,T> callback
    ) throws FileNotFoundException
    { return Request(RequestType.POST,req, HttpRequest.BodyPublishers.ofFile(body),callback); }

    public <T>HttpResponse<T> POST(
            RequestParams req,
            HttpRequest.BodyPublisher body,
            Function<HttpResponse.ResponseInfo,T> callback
    )
    { return Request(RequestType.POST,req, HttpRequest.BodyPublishers.concat(body),callback); }

    public <T, S extends InputStream>HttpResponse<T> POST(
            RequestParams req,
            S body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.POST,req, HttpRequest.BodyPublishers.ofInputStream(() -> body),callback); }

    // PUTS
    public <T>HttpResponse<T> PUT(
            RequestParams req,
            String body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.ofString(body),callback); }

    public <T>HttpResponse<T> PUT(
            RequestParams req,
            byte[] body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.ofByteArray(body),callback); }

    public <T>HttpResponse<T> PUT(
            RequestParams req,
            Iterable<byte[]> body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.ofByteArrays(body),callback); }

    public <T>HttpResponse<T> PUT(
            RequestParams req,
            Path body,
            Function<HttpResponse.ResponseInfo,T> callback
    ) throws FileNotFoundException
    { return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.ofFile(body),callback); }

    public <T>HttpResponse<T> PUT(
            RequestParams req,
            HttpRequest.BodyPublisher body,
            Function<HttpResponse.ResponseInfo,T> callback
    )
    { return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.concat(body),callback); }

    public <T, S extends InputStream>HttpResponse<T> PUT(
            RequestParams req,
            S body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return Request(RequestType.PUT,req, HttpRequest.BodyPublishers.ofInputStream(() -> body),callback); }


    // REQUEST IMPLEMENTATION
    public <T>HttpResponse<T> Request(
            RequestType type,
            RequestParams req,
            HttpRequest.BodyPublisher body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){
        HttpResponse<T> ret = null;
        if( req == null )
            req = defaultParams;

        String endpoint = req.endpoint();
        int connectionTimeout = req.connectionTimeout();
        var headers = req.headers();
        var params = req.params();
        boolean followRedirects = req.followRedirects();

        // Setting the endpoint
        if( endpoint != null && (!endpoint.equals("/") && !endpoint.isEmpty()) )
            apiUrl = URI.create(apiUrl.toString() + endpoint);
        // Setting parameters
        if( params != null && !params.isEmpty() ){
            apiUrl = URI.create(apiUrl.toString() + "?" + getParamsString(params));
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder(apiUrl);
        body = (body == null) ? HttpRequest.BodyPublishers.noBody() : body;

        switch (type){
            case RequestType.GET -> builder = builder.GET();
            case RequestType.POST -> builder = builder.POST(body);
            case RequestType.PUT -> builder = builder.PUT(body);
            case RequestType.DELETE -> builder = builder.DELETE();
        }

        // Setting headers
        if( headers != null )
            for(String k: headers.keySet())
                builder.setHeader(k,headers.get(k));

        // Building the http client
        HttpClient.Builder clientBuild =HttpClient.newBuilder();
        // Http Version on V2 TODO configurable version
        clientBuild.version(HttpClient.Version.HTTP_2);
        // Setting the following redirections
        clientBuild.followRedirects( followRedirects ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NORMAL );
        // Setting the connection timeout
        clientBuild.connectTimeout(Duration.ofMillis(connectionTimeout));

        try(HttpClient client = clientBuild.build()){
            ret = client.send(
                    builder.build(),
                    res -> HttpResponse.BodySubscribers.replacing( callback.apply(res) )
            );
        }catch(IOException | InterruptedException err){
            err.printStackTrace();
        }

        return ret;
    }
}
