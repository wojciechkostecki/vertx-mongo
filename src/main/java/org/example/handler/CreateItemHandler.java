package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.example.model.Item;

import java.util.UUID;

public class CreateItemHandler implements Handler<RoutingContext> {

    private final JWTAuth jwtAuth;
    private final MongoClient mongoClient;

    public CreateItemHandler(JWTAuth jwtAuth, MongoClient mongoClient) {
        this.jwtAuth = jwtAuth;
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.getBodyAsJson();
        String name = requestBody.getString("name");

        String currentUserId = routingContext.user().principal().getString("user_id");

        if (currentUserId != null) {
            Item newItem = new Item(UUID.fromString(currentUserId), name);

            mongoClient.save("items", newItem.toJson(), result -> {
                if (result.succeeded()) {
                    routingContext.response()
                            .setStatusCode(204)
                            .end("Item created successfully.");
                } else {
                    routingContext.response().setStatusCode(500).end();
                }
            });
        } else {
            routingContext.response().setStatusCode(404).end("Current user id not found");
        }
    }

}
