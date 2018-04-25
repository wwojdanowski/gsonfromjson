package com.voovoo.antlr.entities;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.annotations.Test;

public class ClassGeneratorTest {

	ClassGenerator generator = new ClassGenerator();

	@Test
	public void findClassDefinitions() {

		EntityNode mainNode = new EntityNode();
		mainNode.setType(EntityType.OBJECT);
		mainNode.setName("Duck");

		EntityNode name = new EntityNode();

		name.setName("name");
		name.setType(EntityType.STRING);
		mainNode.addEntity(name);

		EntityNode age = new EntityNode();
		age.setType(EntityType.NUMBER);
		age.setName("age");

		mainNode.addEntity(age);

		EntityNode friends = new EntityNode();
		friends.setType(EntityType.ARRAY);
		friends.setName("friends");

		EntityNode friend1 = makeEntityNode(EntityType.OBJECT, null);
		EntityNode friend1Name = makeEntityNode(EntityType.STRING, "name");
		EntityNode friend1Age = makeEntityNode(EntityType.NUMBER, "age");
		
		friend1.addEntity(friend1Name);
		friend1.addEntity(friend1Age);
		friends.addEntity(friend1);
		
		EntityNode friend2 = makeEntityNode(EntityType.OBJECT, null);
		EntityNode friend2Name = makeEntityNode(EntityType.STRING, "name");
		EntityNode friend2Age = makeEntityNode(EntityType.NUMBER, "age");
		
		friend2.addEntity(friend2Name);
		friend2.addEntity(friend2Age);
		friends.addEntity(friend2);
		
		mainNode.addEntity(friends);
		
		ArrayList<ClassDef> classDefinitions = generator.findClassDefinitions(mainNode);
		
		assertTrue(classDefinitions.size() == 2);
		
		boolean duckClassFound = false;
		
		for (ClassDef cl : classDefinitions) {
			String cln = cl.getName();
			
			
			if (cln != null && cl.getName().equals("Duck")) {
				duckClassFound = true;
				break;
			}
		}
		
		assertTrue(duckClassFound);
	}

	@Test(enabled = false)
	public void findClassDefinitionsInternal() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void processArray() {
		throw new RuntimeException("Test not implemented");
	}
	

	private EntityNode makeEntityNode(EntityType type, String name) {
		EntityNode node = new EntityNode();
		
		node.setName(name);
		node.setType(type);
		
		return node;
	}
}
