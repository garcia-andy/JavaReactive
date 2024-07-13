package org.jreactive.Types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.function.Function;

public class UseApi {

    public static Api useApi(String url)
    { return useApi(url,null); }

    public static Api useApi(String url, Api.RequestParams params)
    { return new Api(url,params); }

    // useGet
    public static <T>HttpResponse<T> useGet(
            String url,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url,params).GET(callback); }

    public static <T>HttpResponse<T> useGet(
            String url,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).GET(callback); }

    // useDelete
    public static <T>HttpResponse<T> useDelete(
            String url,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url,params).DELETE(callback); }

    public static <T>HttpResponse<T> useDelete(
            String url,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).DELETE(callback); }

    // usePost
    public static <T>HttpResponse<T> usePost(
            String url,
            String body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).POST(params,body,callback); }

    public static <T>HttpResponse<T> usePost(
            String url,
            byte[] body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).POST(params,body,callback); }

    public static <T>HttpResponse<T> usePost(
            String url,
            Iterable<byte[]> body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).POST(params,body,callback); }

    public static <T>HttpResponse<T> usePost(
            String url,
            Path body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ) throws FileNotFoundException
    { return useApi(url).POST(params,body,callback); }

    public static <T>HttpResponse<T> usePost(
            String url,
            HttpRequest.BodyPublisher body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).POST(params,body,callback); }

    public static <T,B extends InputStream>HttpResponse<T> usePost(
            String url,
            B body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).POST(params,body,callback); }

    // usePut
    public static <T>HttpResponse<T> usePut(
            String url,
            String body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).PUT(params,body,callback); }

    public static <T>HttpResponse<T> usePut(
            String url,
            byte[] body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).PUT(params,body,callback); }

    public static <T>HttpResponse<T> usePut(
            String url,
            Iterable<byte[]> body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).PUT(params,body,callback); }

    public static <T>HttpResponse<T> usePut(
            String url,
            Path body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ) throws FileNotFoundException
    { return useApi(url).PUT(params,body,callback); }

    public static <T>HttpResponse<T> usePut(
            String url,
            HttpRequest.BodyPublisher body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).PUT(params,body,callback); }

    public static <T,B extends InputStream>HttpResponse<T> usePut(
            String url,
            B body,
            Api.RequestParams params,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).PUT(params,body,callback); }

    // useRequest
    public static <T>HttpResponse<T> useRequest(
            String url,
            Api.RequestType type,
            Api.RequestParams req,
            HttpRequest.BodyPublisher body,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).Request(type,req,body,callback); }

    public static <T>HttpResponse<T> useRequest(
            String url,
            Api.RequestType type,
            Api.RequestParams req,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).Request(type,req,null,callback); }

    public static <T>HttpResponse<T> useRequest(
            String url,
            Api.RequestParams req,
            Function<HttpResponse.ResponseInfo,T> callback
    ){ return useApi(url).Request(Api.RequestType.GET,req,null,callback); }

}
