package org.example.router;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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

        router.errorHandler(401, MainRouter::handleUnauthorized);

        return router;
    }

    private static void handleUnauthorized(RoutingContext routingContext) {
        String message = "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic.";
        routingContext.response().setStatusCode(401).end(message);
    }
}
