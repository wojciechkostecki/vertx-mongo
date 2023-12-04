package org.example.handler;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoDBHandler {
    private static final String DATABASE_NAME = "mongodb";

    public static MongoClient initializeMongoClient(Vertx vertx) {
        JsonObject mongoConfig = new JsonObject()
                .put("connection_string", "mongodb://localhost:27017")
                .put("db_name", DATABASE_NAME);

        return MongoClient.createShared(vertx, mongoConfig);
    }

    public static void ensureCollectionExists(MongoClient mongoClient, String collectionName) {
        mongoClient.getCollections(result -> {
            if (result.succeeded()) {
                if (!result.result().contains(collectionName)) {
                    createCollection(mongoClient, collectionName);
                }
            } else {
                System.err.println("Error checking collections: " + result.cause().getMessage());
            }
        });
    }

    private static void createCollection(MongoClient mongoClient, String collectionName) {
        mongoClient.createCollection(collectionName, createResult -> {
            if (createResult.succeeded()) {
                System.out.println("Collection '" + collectionName + "' created successfully");
            } else {
                System.err.println("Error creating collection '" + collectionName + "': "
                        + createResult.cause().getMessage());
            }
        });
    }
}
