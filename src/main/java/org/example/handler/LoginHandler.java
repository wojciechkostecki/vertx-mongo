package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.mindrot.jbcrypt.BCrypt;

public class LoginHandler implements Handler<RoutingContext> {

    private final JWTAuth jwtAuth;
    private final MongoClient mongoClient;

    public LoginHandler(JWTAuth jwtAuth, MongoClient mongoClient) {
        this.jwtAuth = jwtAuth;
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.getBodyAsJson();
        String login = requestBody.getString("login");
        String password = requestBody.getString("password");

        JsonObject query = new JsonObject().put("login", login);

        mongoClient.findOne("users", query, null, result -> {
            if (result.succeeded()) {
                JsonObject user = result.result();

                if (user != null) {
                    String hashedPassword = user.getString("password");

                    if (BCrypt.checkpw(password, hashedPassword)) {
                        String token = jwtAuth.generateToken(new JsonObject().put("login", login)
                                .put("user_id", user.getValue("_id")));

                        routingContext.response().setStatusCode(200).end(new JsonObject().put("token", token).encode());
                    } else {
                        routingContext.response().setStatusCode(401).end("Invalid credentials");
                    }

                } else {
                    routingContext.response().setStatusCode(401).end("Invalid credentials");
                }
            } else {
                routingContext.response().setStatusCode(500).end("Internal Server Error");
            }
        });
    }
}
