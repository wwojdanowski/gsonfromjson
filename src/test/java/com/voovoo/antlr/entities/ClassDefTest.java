package com.voovoo.antlr.entities;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class ClassDefTest {

    @Test
    public void packageNameAvailabilityShouldBeNotedByBoolean() {

        ClassDef def = new ClassDef();

        assertFalse(def.hasValidPackageName());
        def.setPackageName("org.testing");
        assertTrue(def.hasValidPackageName());
    }

    @Test
    public void classNameAvailabilityShouldBeNotedByBoolean() {
        ClassDef def = new ClassDef();

        assertFalse(def.hasValidClassName());
        def.setName("Duck");
        assertTrue(def.hasValidClassName());
    }
}
