
package util;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;


public class HttpRequest implements IRequest {

    @Override
    public CompletableFuture<String> getBody(String path) {
        //get Async Http Client
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        return asyncHttpClient
                .prepareGet(path)
                .execute() // ASYNC => não bloqueante
                .toCompletableFuture()  //CF<Response>
                .thenApply(Response::getResponseBody) //CF<String>
                .whenComplete((res, ex) -> closeAHC(asyncHttpClient)); //close client resources

    }

    private static void closeAHC(AsyncHttpClient client){
        try {
            client.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


}