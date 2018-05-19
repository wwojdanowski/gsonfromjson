package com.voovoo.antlr.entities.builder;

import com.voovoo.antlr.entities.EntityNode;
import com.voovoo.antlr.entities.EntityType;

public class EntityContainer {

    private EntityNode current;
    private EntityNode top;

    public EntityContainer(EntityType type) {
        current = new EntityNode();
        current.setType(type);
        top = current;
    }

    EntityContainer(EntityNode node, EntityNode topContainer) {
        current = node;
        top = topContainer;
    }

    public EntityContainer number(String name) {
        EntityNode node = new EntityNode();
        node.setType(EntityType.NUMBER);
        node.setName(name);
        current.addEntity(node);
        return this;
    }

     public EntityContainer string(String name) {
        EntityNode node = new EntityNode();
        node.setType(EntityType.STRING);
        node.setName(name);
        current.addEntity(node);
        return this;
    }

    public EntityContainer obj(String name) {
        EntityNode node = new EntityNode();
        node.setType(EntityType.OBJECT);
        node.setName(name);
        current.addEntity(node);
        return new EntityContainer(node, top);
    }

    public EntityContainer array() {
        EntityNode node = new EntityNode();
        node.setType(EntityType.ARRAY);
        current.addEntity(node);
        return new EntityContainer(node, top);
    }

    public EntityContainer array(String name) {
        EntityNode node = new EntityNode();
        node.setType(EntityType.ARRAY);
        node.setName(name);
        current.addEntity(node);
        return new EntityContainer(node, top);
    }

    public EntityContainer obj() {
        EntityNode node = new EntityNode();
        node.setType(EntityType.OBJECT);
        current.addEntity(node);
        return new EntityContainer(node, top);
    }

    public EntityNode create() {
        return top;
    }

}
