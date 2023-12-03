package org.example.router;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.handler.LoginHandler;
import org.example.handler.RegisterHandler;

public class MainRouter {

    public static Router initializeMainRouter(Vertx vertx, JWTAuth jwtAuth, MongoClient mongoClient) {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.post("/login").handler(new LoginHandler(jwtAuth, mongoClient));
        router.post("/register").handler(new RegisterHandler(mongoClient));

        ItemRouter itemRouter = new ItemRouter(router, jwtAuth, mongoClient);
        itemRouter.buildItemRouter();

        return router;
    }
}
