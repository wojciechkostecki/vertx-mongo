package org.example.router;

import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.example.handler.CreateItemHandler;
import org.example.handler.GetItemsHandler;

public class ItemRouter {

    private final Router router;

    private final JWTAuth jwtAuth;

    private final MongoClient mongoClient;

    public ItemRouter(Router router, JWTAuth jwtAuth, MongoClient mongoClient) {
        this.router = router;
        this.jwtAuth = jwtAuth;
        this.mongoClient = mongoClient;
    }

    public void buildItemRouter() {
        router.route("/items").handler(JWTAuthHandler.create(jwtAuth));
        router.post("/items").handler(new CreateItemHandler(jwtAuth, mongoClient));
        router.get("/items").handler(new GetItemsHandler(jwtAuth, mongoClient));
    }
}
