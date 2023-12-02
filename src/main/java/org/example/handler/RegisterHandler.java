package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.example.model.User;

public class RegisterHandler implements Handler<RoutingContext> {

    private final MongoClient mongoClient;

    public RegisterHandler(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.getBodyAsJson();
        String login = requestBody.getString("login");
        String password = requestBody.getString("password");

        if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            routingContext.response().setStatusCode(404).end("Login and password cannot be empty.");
        }

        MongoDBHandler.ensureCollectionExists(mongoClient, "users");

        JsonObject query = new JsonObject().put("login", login);

        mongoClient.findOne("users", query, null, result -> {
            if (result.succeeded()) {
                if (result.result() != null) {
                    String errorMessage = "Login '" + login + "' is already taken.";
                    routingContext.response().setStatusCode(400).end(errorMessage);
                } else {
                    User newUser = new User(login, password);

                    mongoClient.save("users", newUser.toJson(), saveResult -> {
                        if (saveResult.succeeded()) {
                            routingContext.response().setStatusCode(204).end("Registration successful.");
                        } else {
                            routingContext.fail(500, new RuntimeException("Registration failed"));
                        }
                    });
                }
            } else {
                routingContext.fail(500, new RuntimeException("Registration failed"));
            }
        });
    }

}
