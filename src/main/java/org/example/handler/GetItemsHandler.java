package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class GetItemsHandler implements Handler<RoutingContext> {

    private final JWTAuth jwtAuth;
    private final MongoClient mongoClient;

    public GetItemsHandler(JWTAuth jwtAuth, MongoClient mongoClient) {
        this.jwtAuth = jwtAuth;
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String currentUserId = routingContext.user().principal().getString("user_id");

        if (currentUserId != null) {
            JsonObject query = new JsonObject().put("ownerId", currentUserId);

            mongoClient.find("items", query, result -> {
                if (result.succeeded()) {
                    JsonArray items = new JsonArray(result.result());
                    routingContext.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encodePrettily(items));
                } else {
                    routingContext.response().setStatusCode(500).end();
                }
            });
        } else {
            routingContext.response().setStatusCode(404).end("Current user id not found");
        }
    }

}
