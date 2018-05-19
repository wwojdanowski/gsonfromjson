package com.voovoo.antlr.entities;


import com.voovoo.antlr.entities.builder.EntityBuilder;
import com.voovoo.antlr.entities.builder.EntityContainer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ClassGeneratorTest {

    ClassGenerator generator = new ClassGenerator();

    @DataProvider
    public Object[][] entityProvider() {

        return null;
    }

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


    @Test(enabled = true)
    public void findClassDefinitionFromArrayInObject() {
        EntityContainer array = new EntityBuilder().obj()
                .string("hostname")
                .number("ram")
                .number("cpus")
                .array("ips");

                array
                .obj()
                .string("type")
                .string("value")
                .obj("library")
                .number("version");

        EntityNode node = array
                .obj()
                .string("format")
                .string("value")
                .create();

        node.setName("computer");

        ArrayList<ClassDef> classDefs = generator.findClassDefinitions(node);

        assertTrue(classDefs.stream().count() == 3,
                "Expected 3 classes to be generated but found: " + classDefs.stream().count());

        List<String> expectedResult = Arrays.asList("ips", "library", "computer");

        expectedResult.forEach(
                cl -> assertTrue(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Expected to find " + cl + " class in generated set!"));

    }

    @Test
    public void findDefinitionsFromArrayAsTopLevelObject() {
        EntityContainer topLevel = new EntityBuilder()
                .array();

            topLevel.obj()
            .string("name")
            .number("age");

            topLevel.obj()
                    .string("name")
                    .obj("coordinates")
                    .string("name")
                    .number("lat")
                    .number("lon");

        EntityNode node = topLevel.create();

        node.setName("toplevel");

        ArrayList<ClassDef> classDefs = generator.findClassDefinitions(node);

        List<String> expectedResult = Arrays.asList("coordinates", "toplevel");

        expectedResult.forEach(
                cl -> assertTrue(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Expected to find " + cl + " class in generated set!"));

    }

    @Test
    public void emptyArrayInObject() {
        EntityContainer topLevel = new EntityBuilder().obj();

        EntityNode node = topLevel.array("empty").create();

        node.setName("toplevel");

        ArrayList<ClassDef> classDefs = generator.findClassDefinitions(node);

        List<String> expectedResult = Arrays.asList("toplevel", "empty");

        expectedResult.forEach(
                cl -> assertTrue(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Expected to find " + cl + " class in generated set!"));

    }


    /*
        TODO: Make a proper assertion
     */
    @Test
    public void arrayPropertyWithPrimitives() {
        EntityContainer topLevel = new EntityBuilder().obj();

        EntityNode node = topLevel.array("primitives")
                .string("one")
                .string("two")
                .string("three")
                .create();

        node.setName("toplevel");

        ArrayList<ClassDef> classDefs = generator.findClassDefinitions(node);

        classDefs.forEach(def -> System.out.println(def));

        List<String> expectedResult = Arrays.asList("toplevel");

        expectedResult.forEach(
                cl -> assertTrue(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Expected to find " + cl + " class in generated set!"));

        assertTrue(classDefs.stream().filter(
                def -> def.getField("primitives").equals("[String]")).count() == 1,
                "An array property should be created which contains Strings only!");

    }

    @Test
    public void arrayPropertyWithPrimitivesAndFollowingObject() {
        EntityContainer topLevel = new EntityBuilder().obj();

        EntityNode node = topLevel.array("primitives")
                .string("one")
                .string("two")
                .string("three")
                .obj("coordinates")
                .number("lat")
                .number("lon")
                .create();

        node.setName("toplevel");

        ArrayList<ClassDef> classDefs = generator.findClassDefinitions(node);

        List<String> expectedResult = Arrays.asList("toplevel");

        expectedResult.forEach(
                cl -> assertTrue(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Expected to find " + cl + " class in generated set!"));

        List<String> shouldNotFind = Arrays.asList("coordinates");

        shouldNotFind.forEach(
                cl -> assertFalse(classDefs.stream().filter(
                        def -> def.getName().equals(cl)).count() == 1,
                        "Class " + cl + " should not be in generated set!"));

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
