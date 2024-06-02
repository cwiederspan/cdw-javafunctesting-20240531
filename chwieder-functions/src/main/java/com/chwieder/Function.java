package com.chwieder;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import reactor.core.publisher.Mono;

// import java.util.Optional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("Hello")
    public HttpResponseMessage hello(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context
    ) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("Cosmos")
    public HttpResponseMessage cosmos(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        var repo = new TableRepository();
        var guid = UUID.randomUUID().toString();

        var createStart = System.nanoTime();
        repo.create(guid, guid);
        double createMs = (System.nanoTime() - createStart * 1.0) / 1000000;

        var readStart = System.nanoTime();
        repo.read(guid, guid);
        double readMs = (System.nanoTime() - readStart * 1.0) / 1000000;

        var deleteStart = System.nanoTime();
        repo.delete(guid, guid);
        double deleteMs = (System.nanoTime() - deleteStart * 1.0) / 1000000;

        return request.createResponseBuilder(HttpStatus.OK).body(new Output(createMs, readMs, deleteMs)).build();
    }

    @FunctionName("CosmosAsync")
    public HttpResponseMessage cosmosAsync(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        var repo = new TableRepositoryAsync();
        var guid = UUID.randomUUID().toString();

        var createStart = System.nanoTime();
        return repo.create(guid, guid).then(Mono.fromCallable(() -> {
            var createMs = (System.nanoTime() - createStart * 1.0) / 1000000;
            var readStart = System.nanoTime();
            return repo.read(guid, guid).then(Mono.fromCallable(() -> {
                var readMs = (System.nanoTime() - readStart * 1.0) / 1000000;
                var deleteStart = System.nanoTime();
                return repo.delete(guid, guid).then(Mono.fromCallable(() -> {
                    var deleteMs = (System.nanoTime() - deleteStart * 1.0) / 1000000;
                    return request.createResponseBuilder(HttpStatus.OK).body(new Output(createMs, readMs, deleteMs)).build();
                })).block();
            })).block();
        })).block();
    }
}