package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.example.handler.MongoDBHandler;
import org.example.router.MainRouter;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {

        MongoClient mongoClient = MongoDBHandler.initializeMongoClient(vertx);

        JWTAuth jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("keyboard cat")));

        Router router = MainRouter.initializeMainRouter(vertx, jwtAuth, mongoClient);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(3000);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}