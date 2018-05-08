package com.voovoo.antlr;

import com.voovoo.antlr.entities.ClassDef;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class DumpActionTest {

    String testOutputDirectory = null;

    @BeforeMethod
    public void setup(ITestContext ctx) {
        TestRunner runner = (TestRunner) ctx;
        testOutputDirectory = runner.getOutputDirectory();

        try {

            Path path = Paths.get(testOutputDirectory);

            if (Files.isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }

            Files.createDirectory(Paths.get(testOutputDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void dumpingSingleClassInNonDefaultPackage() {
        DumpAction action = new DumpAction();
        ClassDef classDef = new ClassDef();
        classDef.setPackageName("com.voovoo");
        classDef.setName("Duck");
        classDef.addField("name", "String");
        classDef.addField("age", "Integer");

        try {

            Files.deleteIfExists(Paths.get(testOutputDirectory + "/com/voovoo"));

            action.dump(classDef, testOutputDirectory);

            assertTrue(Files.isDirectory(Paths.get(testOutputDirectory + "/com/voovoo")),
                    "Package directory structure was not created!");

            assertTrue(Files.exists(Paths.get(testOutputDirectory + "/com/voovoo/Duck.java")),
                    "Class file for " + classDef.getName() + " class was not created in "
                            + classDef.getPackageName() + " package!");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }
    }

    @Test
    public void dumpingMultipleClassesInDifferentPackages() {
        DumpAction action = new DumpAction();

        List <ClassDef> classDefs = new ArrayList<ClassDef>();

        ClassDef classDef1 = new ClassDef();
        classDef1.setPackageName("com.voovoo.places");
        classDef1.setName("Zoo");
        classDef1.addField("address", "String");
        classDef1.addField("noOfPets", "Integer");

        ClassDef classDef2 = new ClassDef();
        classDef2.setPackageName("com.voovoo.pets");
        classDef2.setName("Duck");
        classDef2.addField("name", "String");
        classDef2.addField("age", "Integer");

        classDefs.add(classDef1);
        classDefs.add(classDef2);

        try {

            Files.deleteIfExists(Paths.get(testOutputDirectory + "/com/voovoo/places"));
            Files.deleteIfExists(Paths.get(testOutputDirectory + "/com/voovoo/pets"));

            action.dump(classDefs, testOutputDirectory);

            assertTrue(Files.isDirectory(Paths.get(testOutputDirectory + "/com/voovoo/places")),
                    "Package directory structure was not created!");

            assertTrue(Files.exists(Paths.get(testOutputDirectory + "/com/voovoo/places/Zoo.java")),
                    "Class file for " + classDef1.getName() + " class was not created in "
                            + classDef1.getPackageName() + " package!");

            assertTrue(Files.isDirectory(Paths.get(testOutputDirectory + "/com/voovoo/pets")),
                    "Package directory structure was not created!");

            assertTrue(Files.exists(Paths.get(testOutputDirectory + "/com/voovoo/pets/Duck.java")),
                    "Class file for " + classDef2.getName() + " class was not created in "
                            + classDef2.getPackageName() + " package!");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }
    }
}
