package com.voovoo.antlr.entities;

import java.util.ArrayList;

public class EntityNode {

	private EntityType type;
	private String name;
	private ArrayList <EntityNode> entities = new ArrayList <EntityNode>();
	
	
	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addEntity(EntityNode entity) {
		entities.add(entity);
	}
	
	public ArrayList <EntityNode> getEntities() {
		return entities;
	}

}
