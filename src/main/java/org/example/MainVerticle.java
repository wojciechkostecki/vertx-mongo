package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.handler.LoginHandler;
import org.example.handler.MongoDBHandler;
import org.example.handler.RegisterHandler;

public class MainVerticle extends AbstractVerticle {

    private MongoClient mongoClient;

    private JWTAuth jwtAuth;

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        MongoClient mongoClient = MongoDBHandler.initializeMongoClient(vertx);

        JWTAuth jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("keyboard cat")));

        router.route().handler(BodyHandler.create());

        router.post("/login").handler(new LoginHandler(jwtAuth, mongoClient));
        router.post("/register").handler(new RegisterHandler(mongoClient));

        server.requestHandler(router).listen(3000);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}