package com.voovoo.antlr;

import com.voovoo.antlr.entities.ClassDef;
import com.voovoo.antlr.printing.ClassPrinter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DumpAction {

    /**
     * @param def
     * @param outputDir
     * @throws Exception - if the class definition is missing name, general exception is thrown
     * @throws IOException - if <b>outputDir</b> is missing, IOException is thrown
     */

    public void dump(ClassDef def, String outputDir) throws Exception {

        if (Files.isDirectory(Paths.get(outputDir))) {

            final String packageName = def.getPackageName();
            final String packagePath = outputDir + '/' + packageName.replace('.', '/');

            Files.createDirectories(
                    Paths.get(packagePath));
            
            
            if (def.hasValidClassName()) {

                try (PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(
                        Paths.get(packagePath + "/" + def.getName() + ".java")))) {

                    ClassPrinter classPrinter = new ClassPrinter();

                    printWriter.print(classPrinter.print(def));
                }
            }

        } else {
            throw new IOException(outputDir + " directory does not exist!");
        }
    }


    public void dump(List<ClassDef> definitions, String outputDir) throws Exception {

        for (ClassDef classDef : definitions) {
            dump(classDef, outputDir);
        }
    }
}

