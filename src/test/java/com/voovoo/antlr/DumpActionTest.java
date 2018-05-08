package com.voovoo.antlr;

import com.voovoo.antlr.entities.ClassDef;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class DumpActionTest {

    String testOutputDirectory = null;

    @BeforeClass
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

        } catch (IOException e) {
            e.printStackTrace();
            fail("Test failed!");
        }
    }
}
