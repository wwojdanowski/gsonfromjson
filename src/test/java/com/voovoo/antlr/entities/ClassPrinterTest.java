package com.voovoo.antlr.entities;

import com.voovoo.antlr.printing.ClassPrinter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertFalse;

public class ClassPrinterTest {

    static private ClassPrinter printer;

    @BeforeClass
    void initialize() {
        printer = new ClassPrinter();
    }


    @Test
    public void printerShouldBeAbleToGenerateAnEmptyClass() {
        ClassDef classDef = new ClassDef();

        classDef.setName("Duck");

        Pattern p = Pattern.compile("[ \n]*class[ ]+Duck[ ]* \\{[ \n]*\\}[ \n]*");

        String output = null;

        try {
            output = Optional.ofNullable(printer.print(classDef)).orElse("An empty string was returned!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }

        Matcher m = p.matcher(output);

        assertTrue(m.matches());
    }

    @Test
    public void generatedEmptyClassShouldNotContainAnyFields() {
        ClassDef classDef = new ClassDef();

        classDef.setName("Duck");

        Pattern p = Pattern.compile("[ \n]*class[ ]+Duck[ ]* \\{[ \n]*" +
                "private Integer age;" +
                "[ \n]*\\}[ \n]*");

        String output = null;
        try {
            output = Optional.ofNullable(printer.print(classDef)).orElse("An empty string was returned!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }

        Matcher m = p.matcher(output);

        assertFalse(m.matches());
    }

    @Test
    public void classFieldsShouldBePrinted() {
        ClassDef classDef = new ClassDef();

        classDef.setName("Duck");
        classDef.addField("age", "Integer");
        classDef.addField("name", "Name");

        final String strPattern = "[ \n]*class[ ]+Duck[ ]* \\{" +
                "[ \t\n]*private Name name;[ \n]*" +
                "[ \t\n]*private Integer age;[ \n]*" +
                "\\}[ \n]*";

        Pattern p = Pattern.compile(strPattern);

        String output = null;
        try {
            output = Optional.ofNullable(printer.print(classDef)).orElse("An empty string was returned!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }

        Matcher m = p.matcher(output);

        assertTrue(m.matches());
    }

    @Test
    public void packageNameShouldBePrintedAtTheTop() {
        ClassDef classDef = new ClassDef();

        classDef.setPackageName("org.duck.world");
        classDef.setName("Duck");
        classDef.addField("age", "Integer");
        classDef.addField("name", "String");

        final String strPattern = "org.duck.world;" +
                "[ \n]*class[ ]+Duck[ ]* \\{" +
                "[ \t\n]*private String name;[ \n]*" +
                "[ \t\n]*private Integer age;[ \n]*" +
                "\\}[ \n]*";

        Pattern p = Pattern.compile(strPattern);

        String output = null;
        try {
            output = Optional.ofNullable(printer.print(classDef)).orElse("An empty string was returned!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed!");
        }

        Matcher m = p.matcher(output);

        assertTrue(m.matches());
    }

    @Test
    public void printerShouldGenerateValidFieldFromStringEntity() {

        ClassDef classDef = new ClassDef();

        classDef.setName("Test");
        classDef.addField("duck", "String");

        Pattern p = Pattern.compile("^[ ]*private[ ]+String[ ]+duck;$");

        String output = Optional.ofNullable(printer.printField("duck", classDef)).orElse("An empty string was returned!");

        Matcher m = p.matcher(output);

        assertTrue(m.matches());
    }

    @Test
    public void printerShouldGenerateValidFieldFromIntegerEntity() {
        ClassDef classDef = new ClassDef();

        classDef.setName("Test");
        classDef.addField("age", "Integer");

        Pattern p = Pattern.compile("^[ ]*private[ ]+Integer[ ]+age;$");

        String output = Optional.ofNullable(printer.printField("age", classDef)).orElse("An empty string was returned!");

        Matcher m = p.matcher(output);

        assertTrue(m.matches());
    }

}
