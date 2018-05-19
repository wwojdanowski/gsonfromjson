package com.voovoo.antlr.entities.builder;

import com.voovoo.antlr.entities.EntityType;

public class EntityBuilder {

    public EntityContainer array() {
        return new EntityContainer(EntityType.ARRAY);
    }

    public EntityContainer obj() {
        return new EntityContainer(EntityType.OBJECT);
    }

}