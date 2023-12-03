package org.example.model;

import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class Item {

    private UUID id;

    private UUID ownerId;

    private String name;

    public Item(UUID ownerId, String name) {
        this.id = UUID.randomUUID();
        this.ownerId = ownerId;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("_id", id != null ? id.toString() : null)
                .put("ownerId", ownerId != null ? ownerId.toString() : null)
                .put("name", name);
    }
}
